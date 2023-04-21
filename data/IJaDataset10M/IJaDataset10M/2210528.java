package sushiwar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import player.Player;
import scenario.Terrain;
import sound.Music;
import sound.Sound;
import sushiwar.Main.MenuStatus;
import timer.Timer;
import timer.TimerListener;
import units.Agent;
import units.Niguiri.Niguiri;
import units.Niguiri.Niguiri.NiguiriStatus;
import units.missile.Missile;

/**
 * @author Hossomi
 * 
 * CLASS Screen -------------------------------------------
 * A parte gráfica da janela, onde tudo será mostrado.
 */
public class Screen extends JPanel implements Constants {

    public int width;

    public int height;

    private Main frame = null;

    private Terrain terrain = null;

    private Image background;

    private Player playerActive = null;

    private int playerActiveId = 0;

    private ArrayList<Player> playerList = null;

    private ArrayList<Niguiri> niguiriList = null;

    private Boolean lockPrint = false;

    private ArrayList<Missile> missileList = null;

    private GameStatus gameStatus = GameStatus.PLAYER_TURN;

    private Timer gameTimer;

    private Music gameMusic;

    private boolean gameOver;

    private double shakeMagnitude = 0;

    private Timer shakeTimer;

    private int skipCycle;

    public enum GameStatus {

        PLAYER_TURN, MISSILE_FLY, EXPLOSION_TIME, DAMAGE_DEAL, NIGUIRI_DEATH
    }

    public Screen(int w, int h, Main frame) {
        super();
        this.frame = frame;
        this.width = w;
        this.height = h;
        this.setLayout(null);
        setSize(w, h);
        setBackground(SCREEN_DEFAULT_BGCOLOR);
        URL url = Screen.class.getResource("/assets/background.png");
        background = new ImageIcon(url).getImage();
        this.setForeground(Color.white);
        InputStream is = Screen.class.getResourceAsStream("/assets/InfoBarFont.ttf");
        try {
            Font theFont = Font.createFont(Font.TRUETYPE_FONT, is);
            setFont(theFont.deriveFont(Font.PLAIN, 20));
        } catch (FontFormatException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        missileList = new ArrayList<Missile>(0);
        niguiriList = new ArrayList<Niguiri>(0);
        playerList = new ArrayList<Player>(0);
        frame.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && Screen.this.isVisible()) Screen.this.frame.toggleMenu(MenuStatus.MENU_MAIN);
            }
        });
    }

    public void startGame(int playerCount, String land) {
        clearGame();
        terrain = new Terrain(land, this);
        int niguiriCount = 10 / playerCount;
        for (int i = 0; i < playerCount; i++) {
            playerList.add(new Player(niguiriCount, this));
        }
        playerActiveId = 0;
        playerActive = playerList.get(0);
        playerActive.toggle(true);
        for (Player p : playerList) {
            p.startNiguiri();
        }
        gameTimer = new Timer(new TimerControl(), 250);
        gameTimer.start();
        shakeTimer = new Timer(new ShakeControl(), 100);
        shakeTimer.start();
    }

    public void clearGame() {
        for (int i = niguiriList.size(); i > 0; i--) niguiriList.get(0).remove();
        playerList.clear();
        missileList.clear();
        if (gameTimer != null) gameTimer.finish();
        if (shakeTimer != null) shakeTimer.finish();
        Player.resetPlayerCount();
        gameOver = false;
        gameStatus = GameStatus.PLAYER_TURN;
    }

    public void showGame() {
        setVisible(true);
    }

    public void hideGame() {
        setVisible(false);
        if (gameMusic != null) {
            gameMusic.halt();
        }
    }

    public void pauseGame() {
        frame.toggleMenu(MenuStatus.MENU_MAIN);
    }

    public void addNiguiri(Niguiri niguiri) {
        niguiriList.add(niguiri);
        frame.addKeyListener(niguiri);
    }

    public void removeNiguiri(Niguiri niguiri) {
        niguiriList.remove(niguiri);
        frame.removeKeyListener(niguiri);
    }

    public void addMissile(Missile missile) {
        missileList.add(missile);
    }

    public void removeMissile(Missile missile) {
        missileList.remove(missile);
    }

    public void removePlayer(Player player) {
        if (player == playerActive) {
            playerActiveId = (playerActiveId + 1) % playerList.size();
            playerActive = playerList.get(playerActiveId);
        }
        playerList.remove(player);
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i) == playerActive) {
                playerActiveId = i;
            }
        }
    }

    public void setGameStatus(GameStatus status) {
        gameStatus = status;
        if (status == GameStatus.PLAYER_TURN) {
            pauseTurn(false);
        } else {
            pauseTurn(true);
        }
    }

    public void setShakeMagnitude(double mag) {
        shakeMagnitude = mag;
    }

    public void pauseTurn(boolean pause) {
        playerActive.toggle(!pause);
    }

    public void skipCycles(int skip) {
        skipCycle = skip;
    }

    public void explode(double x, double y, int damage, double radius, double power) {
        terrain.explode(x, y, radius);
        Sound explosionSound = new Sound("Explosion");
        explosionSound.play();
        lockPrint = true;
        for (Niguiri target : niguiriList) {
            double dx = target.getPositionX() - x;
            double dy = target.getPositionY() - y;
            if (dx * dx + dy * dy <= radius * radius) {
                double angle = Math.atan2(dy, dx);
                target.applySpeed(power * Math.cos(angle), power * Math.sin(angle));
                target.applyDamage(damage);
                target.setStatus(NiguiriStatus.DIZZY);
            }
        }
        lockPrint = false;
        setGameStatus(GameStatus.EXPLOSION_TIME);
        setShakeMagnitude(power * 10);
    }

    public void nextTurn() {
        if (!playerList.isEmpty()) {
            playerActive.toggle(false);
            playerActiveId = (playerActiveId + 1) % playerList.size();
            playerActive = playerList.get(playerActiveId);
            playerActive.nextNiguiri();
            playerActive.toggle(true);
            setGameStatus(GameStatus.PLAYER_TURN);
        }
    }

    public int isPointInScreen(int x, int y) {
        int result = 0;
        if (0 <= x && x <= this.getWidth()) {
            result += 1;
        }
        if (0 <= y && y <= this.getHeight()) {
            result += 2;
        }
        return result;
    }

    public int isBoxInScreen(Rectangle r) {
        int result = 0;
        if (r.getMinX() < 0) {
            result += SCREEN_OUT_LEFT;
            if (r.getMaxX() < 0) {
                result += SCREEN_OUT_TOTAL;
            }
        } else if (r.getMaxX() > width) {
            result += SCREEN_OUT_RIGHT;
            if (r.getMinX() > width) {
                result += SCREEN_OUT_TOTAL;
            }
        }
        if (r.getMinY() < 0) {
            result += SCREEN_OUT_TOP;
            if (r.getMaxY() < 0) {
                result += SCREEN_OUT_TOTAL;
            }
        } else if (r.getMaxY() > height) {
            result += SCREEN_OUT_BOTTOM;
            if (r.getMinY() > height) {
                result += SCREEN_OUT_TOTAL;
            }
        }
        return result;
    }

    public int adjustAgentInScreen(Agent ag) {
        int result = this.isBoxInScreen(ag.getBox());
        if ((result & SCREEN_OUT_TOP) != 0) {
            ag.setPositionY(ag.getHeight() / 2);
        } else if ((result & SCREEN_OUT_BOTTOM) != 0) {
            ag.setPositionY(height - ag.getHeight() / 2);
        }
        if ((result & SCREEN_OUT_LEFT) != 0) {
            ag.setPositionX(ag.getWidth() / 2);
        } else if ((result & SCREEN_OUT_RIGHT) != 0) {
            ag.setPositionX(width - ag.getWidth() / 2);
        }
        return result;
    }

    public int getAgentFlyHeight(Agent ag) {
        return terrain.getAgentFlyHeight(ag);
    }

    public boolean hitTerrain(Agent ag, boolean adjust) {
        return terrain.collided(ag, adjust);
    }

    public int getRandomX(int size) {
        return size / 2 + (int) (Math.random() * (width - size));
    }

    public boolean checkMovement() {
        for (Niguiri n : niguiriList) {
            if (n.getSpeed() > 0) {
                return true;
            }
        }
        return false;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void update() {
        if (skipCycle > 0) {
            skipCycle--;
            return;
        }
        if (gameStatus == GameStatus.EXPLOSION_TIME) {
            if (!checkMovement()) {
                setGameStatus(GameStatus.DAMAGE_DEAL);
                skipCycles(6);
            }
        } else if (gameStatus == GameStatus.DAMAGE_DEAL) {
            int totalDamage = 0;
            for (Niguiri n : niguiriList) {
                totalDamage += n.doDamage();
            }
            if (totalDamage > 0) {
                new Sound("Pain").play();
            }
            skipCycles(6);
            setGameStatus(GameStatus.NIGUIRI_DEATH);
        } else if (gameStatus == GameStatus.NIGUIRI_DEATH) {
            boolean hasDeath = false;
            for (Niguiri n : niguiriList) {
                if (n.getLife() == 0) {
                    n.kill();
                    hasDeath = true;
                }
            }
            if (hasDeath) new Sound("Death").play(); else {
                if (playerList.size() == 1) {
                    JOptionPane.showMessageDialog(frame, "Jogador " + (playerList.get(0).getId() + 1) + " venceu!", "Game Over", JOptionPane.INFORMATION_MESSAGE, null);
                    gameOver = true;
                } else if (playerList.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Empate!", "Game Over", JOptionPane.INFORMATION_MESSAGE, null);
                    gameOver = true;
                }
                if (!gameOver) {
                    nextTurn();
                } else {
                    frame.resetGame();
                }
            }
            skipCycles(6);
        }
    }

    public void updateShake() {
        shakeMagnitude /= 2;
        if (shakeMagnitude < 3) {
            shakeMagnitude = 0;
        }
    }

    class TimerControl implements TimerListener {

        @Override
        public int update() {
            Screen.this.update();
            return 0;
        }
    }

    class ShakeControl implements TimerListener {

        @Override
        public int update() {
            Screen.this.updateShake();
            return 0;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        double sx = Math.random() * shakeMagnitude - shakeMagnitude / 2;
        double sy = Math.random() * shakeMagnitude - shakeMagnitude / 2;
        g.drawImage(background, (int) sx, (int) sy, null);
        if (terrain != null) {
            terrain.print(g, sx, sy);
        }
        if (!lockPrint) {
            for (Player p : playerList) {
                p.printNiguiri(g, sx, sy);
            }
            for (Niguiri n : niguiriList) {
                n.printCrosshair(g, sx, sy);
            }
        }
        for (Missile m : missileList) {
            m.print(g);
        }
    }
}
