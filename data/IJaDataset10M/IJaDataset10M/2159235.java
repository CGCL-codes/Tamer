package killall;

import killall.items.Enemy;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.midlet.MIDlet;
import killall.items.Bonus;

/**
 * Main game class
 * @author danil
 */
public class Screen extends Canvas implements CommandListener, Runnable {

    /**
     * Game state: game not started, displaying logo.
     */
    public static final int STATE_NOT_STARTED = 0;

    /**
     * Game state: main playing loop.
     */
    public static final int STATE_PLAYING = 1;

    /**
     * Game state: player dead, displaying fail message and level.
     */
    public static final int STATE_DEAD = 2;

    /**
     * Game state: paused, displaying pause message + screen.
     */
    public static final int STATE_PAUSED = 3;

    /**
     * Enemy image path.
     */
    public static final String IMAGE = "/img/enemy.png";

    /**
     * Fail message.
     */
    public static final String FAIL = "Fayul!";

    /**
     * Pause message.
     */
    public static final String PAUSE = "Пауза";

    /**
     * Enemy image width.
     */
    public static final int IWIDTH = 64;

    /**
     * Enemy image height.
     */
    public static final int IHEIGHT = 64;

    /**
     * Enemy standart health.
     */
    public static final int STD_HEALTH = 10;

    /**
     * Maximum decal images.
     * @deprecated replaced with "blood buffer"
     */
    public static final int DECAL_NUM = 64;

    /**
     * Decal species.
     */
    public static final int DECAL_SPECIES = 3;

    /**
     * Enemy moving pixels per second.
     */
    public static final int PPS = 20;

    /**
     * Additional pixels per level.
     */
    public static final int PPL = 1;

    /**
     * Parent midlet.
     */
    private MIDlet parent;

    /**
     * Repaint thread.
     */
    private Thread runner;

    /**
     * Exit command.
     */
    private Command EXIT = new Command("Exit", Command.EXIT, 1);

    /**
     * Game current state.
     */
    public int state = STATE_NOT_STARTED;

    /**
     * Enemy adder.
     */
    private ItemManager manp;

    private Random rand = new Random();

    /**
     * Enemy array.
     */
    public Enemy[] enemies = new Enemy[8];

    /**
     * Current bonus.
     */
    public Bonus bonus;

    /**
     * Bonus image.
     */
    public Image ibonus;

    /**
     * "Play" image.
     */
    private Image iplay;

    /**
     * "Pause" image.
     */
    private Image ipause;

    /**
     * Logo image.
     */
    private Image ilogo;

    /**
     * Blood decals.
     */
    Image[] ibdecals = new Image[DECAL_SPECIES];

    /**
     * Enemy sprite.
     */
    private Image ienemy;

    /**
     * Backbuffer.
     */
    private Image ibuffer;

    /**
     * Shot sprite.
     */
    private Image shot;

    /**
     * Transparent image data.
     */
    private int[] idata;

    /**
     * "Blood buffer".
     */
    private Image ibloodb;

    /**
     * Backbuffer graphics.
     */
    private Graphics gb;

    /**
     * "Blood buffer" graphics.
     */
    private Graphics ibloodg;

    /**
     * Sound input stream.
     */
    private InputStream is;

    /**
     * Sound player.
     */
    private Player p;

    /**
     * Time of death.
     */
    private long dieTime = 0;

    /**
     * Game start time.
     */
    private long startTime = 0;

    /**
     * Current level.
     */
    private int level = 1;

    /**
     * Current score.
     */
    private int score = 0;

    /**
     * Is game initialized? (got screen height, width, etc.)
     */
    public boolean initialized = false;

    public int w = -1, h = -1, cx = -1, cy = -1, fh = -1, hc = -1, wc = -1;

    /**
     * Constructor. Requires parent MIDlet.
     * @param parent
     */
    public Screen(MIDlet parent) {
        this.parent = parent;
        this.setFullScreenMode(true);
        setCommandListener(this);
        addCommand(EXIT);
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
            enemies[i].state = Enemy.INACTIVE;
        }
        runner = new Thread(this);
        runner.start();
        manp = new ItemManager(this);
        bonus = new Bonus();
        bonus.state = Bonus.INACTIVE;
        try {
            is = getClass().getResourceAsStream("/img/snd.wav");
            p = Manager.createPlayer(is, "audio/X-wav");
            p.realize();
            is.close();
        } catch (IOException ioe) {
        } catch (MediaException me) {
        }
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Repaint method.
     * @param g graphics to paint on.
     */
    public void paint(Graphics g) {
        if (!initialized) {
            w = g.getClipWidth();
            h = g.getClipHeight();
            cx = g.getClipX();
            cy = g.getClipY();
            fh = g.getFont().getHeight();
            wc = w / 2;
            hc = h / 2;
            ibuffer = Image.createImage(w, h);
            gb = ibuffer.getGraphics();
            ibloodb = Image.createImage(w, h);
            ibloodg = ibloodb.getGraphics();
            try {
                ienemy = Image.createImage(IMAGE);
                ilogo = Image.createImage("/img/logo.jpg");
                iplay = Image.createImage("/img/play.png");
                ipause = Image.createImage("/img/pause.png");
                ibonus = Image.createImage("/img/bonus.png");
                shot = Image.createImage("/img/shot.png");
                for (int i = 0; i < Screen.DECAL_SPECIES; i++) {
                    ibdecals[i] = Image.createImage("/img/blood" + i + ".png");
                }
                idata = new int[w * h];
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            initialized = true;
        }
        switch(state) {
            case STATE_NOT_STARTED:
                gb.setColor(0x00FFFFFF);
                gb.fillRect(cx, cy, w, h);
                gb.drawImage(ilogo, (w - ilogo.getWidth()) / 2, (h - ilogo.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
                break;
            case STATE_PLAYING:
                gb.drawRGB(idata, 0, 0, 0, 0, w, h, true);
                gb.drawImage(ibloodb, 0, 0, Graphics.TOP | Graphics.LEFT);
                this.updateEnemies(System.currentTimeMillis());
                this.updateBonus(System.currentTimeMillis());
                gb.setColor(0xFFFFFFFF);
                gb.fillRect(cx, cy, w, 64);
                gb.drawImage(ipause, w - 64, 0, Graphics.TOP | Graphics.LEFT);
                gb.setColor(0);
                gb.drawString("Счёт: " + score, 10, 10, Graphics.TOP | Graphics.LEFT);
                gb.drawString("Уровень: " + level, 10, 12 + fh, Graphics.TOP | Graphics.LEFT);
                break;
            case STATE_DEAD:
                gb.setColor(0x00FFFFFF);
                gb.fillRect(cx, cy, w, h);
                gb.setColor(0);
                gb.drawString(FAIL, (w - g.getFont().stringWidth(FAIL)) / 2, (h - fh) / 2, Graphics.TOP | Graphics.LEFT);
                gb.drawString("Уровень: " + level, (w - g.getFont().stringWidth("Уровень: " + level)) / 2, (h - fh) / 2 + fh, Graphics.TOP | Graphics.LEFT);
                break;
            case STATE_PAUSED:
                long curtime = System.currentTimeMillis();
                for (int i = 0; i < enemies.length; i++) {
                    enemies[i].upd = curtime;
                }
                bonus.upd = curtime;
                gb.setColor(0);
                gb.drawString(PAUSE, (w - gb.getFont().stringWidth(PAUSE)) / 2, (h - gb.getFont().getHeight()) / 2, Graphics.TOP | Graphics.LEFT);
                gb.drawImage(iplay, w - 64, 0, Graphics.TOP | Graphics.LEFT);
                break;
        }
        g.drawImage(ibuffer, 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    /**
     * Updating enemies cycle.
     * @param curtime
     */
    public void updateEnemies(long curtime) {
        double timePassed;
        int move;
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].state == Enemy.ACTIVE) {
                timePassed = (curtime - enemies[i].upd) / 1000.0;
                move = (int) (PPS * timePassed + level * PPL) + 1;
                enemies[i].loc.y += move;
                if (enemies[i].loc.y >= h) {
                    this.dieTime = curtime;
                    this.state = STATE_DEAD;
                    this.bonus.state = Bonus.INACTIVE;
                    this.score = 0;
                    reset();
                    break;
                }
                gb.drawImage(ienemy, enemies[i].loc.x, enemies[i].loc.y, Graphics.TOP | Graphics.LEFT);
                enemies[i].upd = curtime;
            }
        }
    }

    /**
     * Updates bonus.
     * @param curtime
     */
    public void updateBonus(long curtime) {
        double timePassed;
        int move;
        if (bonus.state == Bonus.ACTIVE) {
            timePassed = (curtime - bonus.upd) / 1000.0;
            move = (int) (PPS * timePassed + level * PPL) + 1;
            bonus.loc.y += move;
            if (bonus.loc.y > h) {
                bonus.state = Bonus.INACTIVE;
                bonus.loc.x = 0;
                bonus.loc.y = 0;
                bonus.upd = curtime;
            }
            gb.drawImage(ibonus, bonus.loc.x, bonus.loc.y, Graphics.TOP | Graphics.LEFT);
            bonus.upd = curtime;
        }
    }

    /**
     * Reset game.
     */
    public void reset() {
        for (int j = 0; j < enemies.length; j++) {
            enemies[j].state = Enemy.INACTIVE;
            enemies[j].loc.x = 0;
            enemies[j].loc.y = 0;
            enemies[j].health = 10;
            enemies[j].upd = 0;
        }
        ibloodg.setColor(0x00FFFFFF);
        ibloodg.fillRect(cx, cy, w, h);
        System.gc();
    }

    /**
     * Called on pointer press.
     */
    protected void pointerPressed(int x, int y) {
        switch(state) {
            case STATE_PLAYING:
                if (x > w - 64 && y < 64) {
                    this.state = STATE_PAUSED;
                    System.gc();
                    return;
                }
                if (y < 64) {
                    return;
                }
                if (bonus.state == Bonus.ACTIVE) if (x > bonus.loc.x && x < bonus.loc.x + IWIDTH && y > bonus.loc.y && y < bonus.loc.y + IHEIGHT) {
                    bonus.state = bonus.INACTIVE;
                    for (int i = 0; i < enemies.length; i++) {
                        if (enemies[i].state == Enemy.ACTIVE) score += 20;
                        enemies[i].state = Enemy.INACTIVE;
                        ibloodg.drawImage(ibdecals[Math.abs(rand.nextInt() % Screen.DECAL_SPECIES)], enemies[i].loc.x, enemies[i].loc.y, Graphics.TOP | Graphics.LEFT);
                    }
                    this.updateLevel();
                    return;
                }
                try {
                    p.realize();
                    p.setMediaTime(0);
                    p.start();
                } catch (MediaException ex) {
                }
                int ox, oy;
                for (int i = 0; i < enemies.length; i++) {
                    if (enemies[i].state == Enemy.ACTIVE) {
                        ox = enemies[i].loc.x;
                        oy = enemies[i].loc.y;
                        if (x > ox && x < ox + IWIDTH && y > oy && y < oy + IHEIGHT) {
                            ibloodg.drawImage(ibdecals[Math.abs(rand.nextInt() % Screen.DECAL_SPECIES)], x - 32, y - 32, Graphics.TOP | Graphics.LEFT);
                            enemies[i].health -= Enemy.HEALTHHIT;
                            if (enemies[i].health <= 0) {
                                enemies[i].state = Enemy.INACTIVE;
                                score += 5;
                                this.updateLevel();
                            }
                            return;
                        }
                    }
                }
                ibloodg.drawImage(shot, x - 8, y - 8, Graphics.TOP | Graphics.LEFT);
                break;
            case STATE_PAUSED:
                if (x > w - 64 && y < 64) {
                    System.gc();
                    this.state = STATE_PLAYING;
                }
                break;
            case STATE_NOT_STARTED:
                if (System.currentTimeMillis() - this.startTime > 2000) {
                    this.state = STATE_PLAYING;
                }
                break;
            case STATE_DEAD:
                if (System.currentTimeMillis() - dieTime > 3000) {
                    this.state = STATE_PLAYING;
                    this.level = 1;
                }
                break;
        }
    }

    /**
     * Recalculate game level.
     */
    public void updateLevel() {
        level = score / 100 + 1;
    }

    /**
     * Called when player releases his finger/stylus
     * @param x
     * @param y
     */
    protected void pointerReleased(int x, int y) {
    }

    public void commandAction(Command c, Displayable displayable) {
        if (c == EXIT) {
            parent.notifyDestroyed();
        }
    }

    /**
     * Repaint cycle.
     */
    public void run() {
        int ptp = 0;
        while (true) {
            this.repaint();
            if (this.state == Screen.STATE_PLAYING) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                ptp++;
                if (ptp >= 1000) {
                    System.gc();
                    ptp = 0;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
