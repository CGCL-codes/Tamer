package net.eiroca.j2me.pacman;

import PacMan;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;

/**
 * The Class PacManScreen.
 */
public final class PacManScreen extends GameScreen {

    /** The Constant NONE. */
    public static final int NONE = -1;

    /** The Constant UP. */
    public static final int UP = 0;

    /** The Constant LEFT. */
    public static final int LEFT = 1;

    /** The Constant DOWN. */
    public static final int DOWN = 2;

    /** The Constant RIGHT. */
    public static final int RIGHT = 3;

    /** The field. */
    private final GameField field;

    /** The layer manager. */
    private final LayerManager layerManager;

    /** The pacman. */
    private final PacmanSprite pacman;

    /** The blinky. */
    private final GhostSprite blinky;

    /** The pinky. */
    private final GhostSprite pinky;

    /** The inkey. */
    private final GhostSprite inkey;

    /** The clyde. */
    private final GhostSprite clyde;

    /** The supermode. */
    private int supermode = 0;

    /** The font height. */
    private int fontHeight = 0;

    /** The font. */
    private Font font;

    /** The closing. */
    private boolean closing;

    /** The scr liv pos x. */
    private int scrLivPosX;

    /** The scr liv pos y. */
    private int scrLivPosY;

    /** The scr liv siz. */
    private int scrLivSiz;

    /** The scr liv off. */
    private int scrLivOff;

    /** The num tick. */
    private int numTick = 0;

    /**
   * Instantiates a new pac man screen.
   * 
   * @param midlet the midlet
   * @param suppressKeys the suppress keys
   * @param fullScreen the full screen
   */
    public PacManScreen(final GameApp midlet, final boolean suppressKeys, final boolean fullScreen) {
        super(midlet, suppressKeys, fullScreen, 30);
        name = Application.messages[PacMan.MSG_PACMAN_NAME];
        layerManager = new LayerManager();
        pacman = new PacmanSprite(this);
        clyde = new GhostSprite(this, 0);
        blinky = new GhostSprite(this, 1);
        pinky = new GhostSprite(this, 2);
        inkey = new GhostSprite(this, 3);
        field = new GameField();
        layerManager.append(pacman);
        layerManager.append(clyde);
        layerManager.append(blinky);
        layerManager.append(pinky);
        layerManager.append(inkey);
        layerManager.append(field);
    }

    public void initGraphics() {
        super.initGraphics();
        font = Font.getDefaultFont();
        fontHeight = font.getHeight();
        scrLivPosX = font.stringWidth(Application.messages[PacMan.MSG_PACMAN_LIVES]);
        scrLivPosY = screenHeight - fontHeight - 1;
        scrLivSiz = fontHeight - 2;
        scrLivOff = fontHeight;
    }

    public void init() {
        super.init();
        score.beginGame(3, 0, 0);
        level_init();
        numTick = 0;
        closing = false;
        pacman.init();
    }

    public boolean tick() {
        numTick++;
        if (numTick < 30) {
            draw();
            draw_level();
        } else {
            if (score.getLives() == 0) {
                closing = true;
            }
            int direction = PacManScreen.NONE;
            if (!closing) {
                if (field.getPills() == 0) {
                    level_init();
                }
                int keyStates = getKeyStates();
                if (keyStates == GameCanvas.FIRE_PRESSED) {
                    midlet.doGamePause();
                }
                keyStates &= ~GameCanvas.FIRE_PRESSED;
                direction = (keyStates == GameCanvas.UP_PRESSED) ? PacManScreen.UP : (keyStates == GameCanvas.LEFT_PRESSED) ? PacManScreen.LEFT : (keyStates == GameCanvas.DOWN_PRESSED) ? PacManScreen.DOWN : (keyStates == GameCanvas.RIGHT_PRESSED) ? PacManScreen.RIGHT : PacManScreen.NONE;
                pacman.tick(direction);
            } else {
                if (pacman.dead) {
                    pacman.tick(direction);
                } else {
                    midlet.doGameStop();
                }
            }
            blinky.tick();
            pinky.tick();
            inkey.tick();
            clyde.tick();
            field.tick();
            if (supermode > 0) {
                supermode--;
                if ((supermode <= 50) && (supermode % 10 == 0)) {
                    GameApp.vibrate(200);
                }
            }
            draw();
        }
        return true;
    }

    /**
   * Level_init.
   */
    public void level_init() {
        score.nextLevel();
        field.init();
        supermode = 0;
        pacman.setRefPixelPosition(11 * 10 - 5, 12 * 10 - 5);
        blinky.setRefPixelPosition(11 * 10 - 4, 6 * 10 - 4);
        blinky.eyeonly = false;
        inkey.setRefPixelPosition(10 * 10 - 4, 8 * 10 - 4);
        inkey.eyeonly = false;
        pinky.setRefPixelPosition(11 * 10 - 4, 8 * 10 - 4);
        pinky.eyeonly = false;
        clyde.setRefPixelPosition(12 * 10 - 4, 8 * 10 - 4);
        clyde.eyeonly = false;
    }

    /**
   * Gets the field.
   * 
   * @return the field
   */
    public GameField getField() {
        return field;
    }

    /**
   * Gets the pacman x.
   * 
   * @return the pacman x
   */
    public int getPacmanX() {
        return pacman.getRefPixelX();
    }

    /**
   * Gets the pacman y.
   * 
   * @return the pacman y
   */
    public int getPacmanY() {
        return pacman.getRefPixelY();
    }

    /**
   * Gets the pacman dead.
   * 
   * @return the pacman dead
   */
    public boolean getPacmanDead() {
        return pacman.getDead();
    }

    /**
   * Overlaps ghost.
   * 
   * @param sprite the sprite
   * @return true, if successful
   */
    public boolean overlapsGhost(final Sprite sprite) {
        if (sprite.collidesWith(pinky, false) && (pinky.eyeonly == false)) {
            return true;
        }
        if (sprite.collidesWith(blinky, false) && (blinky.eyeonly == false)) {
            return true;
        }
        if (sprite.collidesWith(inkey, false) && (inkey.eyeonly == false)) {
            return true;
        }
        if (sprite.collidesWith(clyde, false) && (clyde.eyeonly == false)) {
            return true;
        }
        return false;
    }

    /**
   * Overlaps pacman.
   * 
   * @param sprite the sprite
   * @return true, if successful
   */
    public boolean overlapsPacman(final Sprite sprite) {
        if (sprite.collidesWith(pacman, false)) {
            return true;
        }
        return false;
    }

    /**
   * Draw.
   */
    public void draw() {
        screen.setColor(Application.background);
        screen.fillRect(0, 0, screenWidth, screenHeight);
        final int dx = origin(pacman.getX() + pacman.getWidth() / 2, field.getWidth(), screenWidth);
        final int dy = origin(pacman.getY() + pacman.getHeight() / 2, field.getHeight(), screenHeight - fontHeight);
        screen.setClip(dx, dy, field.getWidth(), field.getHeight());
        screen.translate(dx, dy);
        layerManager.paint(screen, 0, 0);
        screen.translate(-dx, -dy);
        screen.setClip(0, 0, screenWidth, screenHeight);
        screen.setColor(Application.background);
        screen.fillRect(0, screenHeight - fontHeight, screenWidth, fontHeight);
        screen.setColor(Application.foreground);
        screen.setFont(font);
        screen.drawString(Integer.toString(score.getScore()), screenWidth - 1, screenHeight, Graphics.BOTTOM | Graphics.RIGHT);
        screen.drawString(Application.messages[PacMan.MSG_PACMAN_LIVES], 1, screenHeight, Graphics.BOTTOM | Graphics.LEFT);
        screen.setColor(Application.foreground);
        final int lives = score.getLives();
        if (lives > 1) {
            screen.fillArc(scrLivPosX, scrLivPosY, scrLivSiz, scrLivSiz, 45, 270);
        }
        if (lives > 2) {
            screen.fillArc(scrLivPosX + scrLivOff, scrLivPosY, scrLivSiz, scrLivSiz, 45, 270);
        }
    }

    /**
   * Draw_level.
   */
    public void draw_level() {
        String slevel;
        slevel = Application.messages[PacMan.MSG_PACMAN_LEVEL] + Integer.toString(score.getLevel());
        screen.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
        final int centerX = screenWidth / 2;
        final int centerY = screenHeight / 2;
        screen.setColor(Application.background);
        screen.drawString(slevel, centerX, centerY - 1, Graphics.BOTTOM | Graphics.HCENTER);
        screen.drawString(slevel, centerX, centerY + 1, Graphics.BOTTOM | Graphics.HCENTER);
        screen.drawString(slevel, centerX - 1, centerY, Graphics.BOTTOM | Graphics.HCENTER);
        screen.drawString(slevel, centerX + 1, centerY, Graphics.BOTTOM | Graphics.HCENTER);
        screen.setColor(Application.foreground);
        screen.drawString(slevel, centerX, centerY, Graphics.BOTTOM | Graphics.HCENTER);
    }

    /**
   * Origin.
   * 
   * @param focus the focus
   * @param fieldLength the field length
   * @param screenLength the screen length
   * @return the int
   */
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
   * Sets the magic mode.
   */
    public void setMagicMode() {
        supermode = 550 - score.getLevel() * 50;
        if (supermode < 100) {
            supermode = 100;
        }
    }

    /**
   * Gets the magic mode.
   * 
   * @return the magic mode
   */
    public boolean getMagicMode() {
        if (supermode > 1) {
            return true;
        }
        return false;
    }
}
