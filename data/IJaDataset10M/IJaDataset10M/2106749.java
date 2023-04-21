package at.momberban.game.me;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Layer;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

/**
 * @author patchinsky
 */
public class MomberBanCanvas extends GameCanvas implements Runnable {

    private static final int EXPLOSION_RENDER_AMOUNT = 5;

    private static final int THREAD_DELAY = 30;

    private Logic logic = null;

    private Game game = null;

    private LayerManager layers;

    private boolean running;

    private Hashtable explosions;

    private final Layer background;

    private final Sprite currentPlayer;

    private final int displayWidth;

    private final int displayHeight;

    private int deltaX;

    private int deltaY;

    private int bombCount;

    /**
     * constructor
     * 
     * @param game
     */
    public MomberBanCanvas(Game game) {
        super(false);
        this.game = game;
        this.logic = game.getLogic();
        this.background = this.game.getMap().getBackgroundLayer();
        this.currentPlayer = this.game.getPlayer();
        if (this.currentPlayer == null) {
            throw new RuntimeException("current player sprite not assigned");
        }
        this.setFullScreenMode(true);
        this.displayWidth = getWidth();
        this.displayHeight = getHeight();
        this.deltaX = 0;
        this.deltaY = 0;
        this.layers = new LayerManager();
        this.explosions = new Hashtable();
        Enumeration i = this.game.getPlayers().keys();
        while (i.hasMoreElements()) {
            Player p = (Player) this.game.getPlayers().get(i.nextElement());
            this.layers.append(p);
        }
        this.layers.append(this.game.getMap().getUnbreakableLayer());
        this.layers.append(this.game.getMap().getBreakableLayer());
        this.layers.append(this.game.getMap().getBackgroundLayer());
    }

    /**
     * render the display
     * 
     * @see javax.microedition.lcdui.game.GameCanvas#paint(javax.microedition.lcdui.Graphics)
     */
    public void paint(Graphics g) {
        deltaX = origin(currentPlayer.getX() + currentPlayer.getWidth() / 2, background.getWidth(), displayWidth);
        deltaY = origin(currentPlayer.getY() + currentPlayer.getHeight() / 2, background.getHeight(), displayHeight);
        g.setClip(deltaX, deltaY, background.getWidth(), background.getHeight());
        g.translate(deltaX, deltaY);
        this.layers.paint(g, 0, 0);
        Enumeration i2 = this.game.getBombs().keys();
        while (i2.hasMoreElements()) {
            Integer bombId = (Integer) i2.nextElement();
            Bomb b = ((Bomb) this.game.getBombs().get(bombId));
            if (b.isTriggered()) {
                explode(b);
                this.game.getBombs().remove(bombId);
            } else {
                b.paint(g);
            }
        }
        Enumeration i3 = this.explosions.keys();
        while (i3.hasMoreElements()) {
            Sprite sprite = (Sprite) i3.nextElement();
            Integer renderCount = (Integer) this.explosions.get(sprite);
            if (renderCount.intValue() > EXPLOSION_RENDER_AMOUNT) {
                this.layers.remove(sprite);
            } else {
                this.explosions.put(sprite, IntegerCache.valueOf(renderCount.intValue() + 1));
            }
        }
        g.translate(-deltaX, -deltaY);
        g.setClip(0, 0, displayWidth, displayHeight);
    }

    private int origin(final int focus, final int fieldLength, final int screenLength) {
        int origin;
        if (screenLength >= fieldLength) {
            origin = (screenLength - fieldLength) / 2;
        } else if (focus <= screenLength / 2) {
            origin = 0;
        } else if (focus >= (fieldLength - screenLength / 2)) {
            origin = screenLength - fieldLength;
        } else {
            origin = screenLength / 2 - focus;
        }
        return origin;
    }

    /**
     * override
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        this.running = true;
        while (this.running) {
            this.logic.handleEvents();
            this.logic.gameTick();
            moveKeys();
            repaint();
            try {
                Thread.sleep(THREAD_DELAY);
            } catch (InterruptedException e) {
            }
        }
        Logger.log("game loop ended");
    }

    /**
     * handle moves only (actions by keypressed)
     */
    private void moveKeys() {
        int keyState = getKeyStates();
        int gameAction = -1;
        if ((keyState & LEFT_PRESSED) != 0) {
            gameAction = LEFT;
        } else if ((keyState & RIGHT_PRESSED) != 0) {
            gameAction = RIGHT;
        } else if ((keyState & UP_PRESSED) != 0) {
            gameAction = UP;
        } else if ((keyState & DOWN_PRESSED) != 0) {
            gameAction = DOWN;
        } else {
        }
        if (gameAction != -1) {
            performAction(gameAction);
        }
    }

    /**
     * perform a game action
     * 
     * @param gameAction
     */
    private void performAction(int gameAction) {
        switch(gameAction) {
            case DOWN:
            case UP:
            case LEFT:
            case RIGHT:
                logic.moveKey(gameAction);
                break;
            case FIRE:
            case GAME_A:
            case GAME_B:
                logic.actionKey(gameAction);
                break;
        }
    }

    /**
     * override
     * 
     * @see javax.microedition.lcdui.Canvas#keyPressed(int)
     */
    protected void keyPressed(int keyCode) {
        performAction(getGameAction(keyCode));
    }

    /**
     * bomb explosion effect
     * 
     * @param bombId
     * @param b
     */
    public void explode(Bomb b) {
        try {
            Logger.log("explosion effect");
            Sprite[] sprites = b.explode();
            TiledLayer breakable = this.game.getMap().getBreakableLayer();
            TiledLayer unbreakable = this.game.getMap().getUnbreakableLayer();
            Hashtable players = this.game.getPlayers();
            Hashtable bombs = this.game.getBombs();
            boolean[] hitDir = { false, false, false, false };
            for (int i = 0; i < sprites.length; i++) {
                Sprite sprite = sprites[i];
                int directionKey = i % 4;
                boolean display = true;
                if (hitDir[directionKey]) {
                    display = false;
                } else if (sprite.collidesWith(unbreakable, false)) {
                    display = false;
                    hitDir[directionKey] = true;
                } else if ((sprite.collidesWith(breakable, false))) {
                    if (!hitDir[directionKey]) {
                        hitDir[directionKey] = true;
                        breakable.setCell(sprite.getX() / breakable.getCellWidth(), sprite.getY() / breakable.getCellHeight(), 0);
                    }
                } else {
                    for (int j = 1; j <= players.size(); j++) {
                        Player p = (Player) players.get(IntegerCache.valueOf(j));
                        if ((p != null) && (p.collidesWith(sprite, true))) {
                            Logger.log("#########################################");
                            Logger.log("DEAD DEAD DEAD " + p.getName() + " DEAD DEAD DEAD ");
                            Logger.log("#########################################");
                            this.logic.playerDied(p, b.getPlayer());
                            this.layers.remove(p);
                        }
                    }
                    Enumeration keys = bombs.keys();
                    while (keys.hasMoreElements()) {
                        Object key = keys.nextElement();
                        Bomb bomb = (Bomb) bombs.get(key);
                        if (bomb.collidesWith(sprite, false)) {
                            Logger.log("Bombreaction");
                            this.logic.explode((Integer) key, bomb);
                        }
                    }
                }
                if (display) {
                    this.explosions.put(sprites[i], IntegerCache.valueOf(0));
                    this.layers.insert(sprites[i], layers.getSize() - 1);
                    if (hitDir[directionKey]) {
                        if ((directionKey == 2) || (directionKey == 3)) {
                            sprite.setFrame(2);
                        } else {
                            sprite.setFrame(0);
                        }
                    }
                } else {
                    if (i > 4) {
                        Sprite last = sprites[i - 4];
                        if ((directionKey == 2) || (directionKey == 3)) {
                            last.setFrame(2);
                        } else {
                            last.setFrame(0);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
