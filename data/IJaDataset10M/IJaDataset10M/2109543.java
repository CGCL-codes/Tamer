package Main;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.LayerManager;

/**
 *
 * @author D1m0n
 *
 * The game class that manages tanks, levels and other stuff.
 *
 */
public class Game extends TanksCanvas {

    private Level level;

    private TextBox txtbox;

    private TankGroup enemies;

    private Tank Player;

    LayerManager lm;

    public static int bonusTime = 15000;

    public static int bonusProbability = 30;

    public static int freezeTime = 50000;

    long currentMillis = 0;

    long prevMillis = 0;

    int frameCount = 0, FPS = 0;

    long prevTime;

    boolean isGameOver = false;

    class Bonus {

        private java.util.Random rnd;

        private TSprite sprite, bomb, newlife, star, clock;

        AABox box;

        public int Type;

        private int timeLeft;

        public boolean isActive;

        public final int BOMB = 0;

        public final int NEWLIFE = 1;

        public final int STAR = 2;

        public final int CLOCK = 3;

        public final int LAST_TYPE = 4;

        public Bonus() {
            bomb = SpriteManager.getIt().getTSprite("bomb");
            bomb.setVisible(false);
            newlife = SpriteManager.getIt().getTSprite("newlife");
            newlife.setVisible(false);
            star = SpriteManager.getIt().getTSprite("star");
            star.setVisible(false);
            clock = SpriteManager.getIt().getTSprite("clock");
            clock.setVisible(false);
            rnd = new java.util.Random(System.currentTimeMillis());
            box = new AABox(0, 0, star.getWidth(), star.getHeight());
            isActive = false;
        }

        public void appearAt(int type, int x, int y, int time) {
            Type = type;
            if (sprite != null) sprite.setVisible(false);
            if (type == BOMB) sprite = bomb; else if (type == NEWLIFE) sprite = newlife; else if (type == STAR) sprite = star; else if (type == CLOCK) sprite = clock;
            sprite.setVisible(true);
            sprite.setPosition(x, y);
            timeLeft = time;
            isActive = true;
            box.x = x;
            box.y = y;
        }

        public void randomAppear(Level l, int time) {
            int x, y, type;
            x = rnd.nextInt(l.getWidth() - box.width);
            y = rnd.nextInt(l.getHeight() - box.height);
            type = rnd.nextInt(LAST_TYPE);
            appearAt(type, x, y, time);
        }

        public boolean update(int dt, Tank t) {
            if (t.getAABox().TestCollision(box) && isActive) {
                isActive = false;
                sprite.setVisible(false);
                return true;
            } else if (timeLeft > 0) {
                timeLeft -= dt;
                if (timeLeft <= 0) {
                    isActive = false;
                    sprite.setVisible(false);
                }
            }
            return false;
        }

        public void getLayers(LayerManager lm, int Layer) {
            if (Layer == 0) {
                lm.append(bomb);
                lm.append(newlife);
                lm.append(star);
                lm.append(clock);
            }
        }
    }

    ;

    class Flag {

        private TSprite flag, blow, death;

        public AABox box;

        private int State;

        private final int ALIVE = 0;

        private final int BLOWING = 1;

        private final int DEATH = 2;

        public Flag(Level l) {
            box = l.getFlagPos();
            flag = SpriteManager.getIt().getTSprite("flag");
            flag.setPosition(box.x, box.y);
            death = SpriteManager.getIt().getTSprite("death");
            death.setVisible(false);
            blow = SpriteManager.getIt().getTSprite("bigboom");
            blow.setVisible(false);
            State = ALIVE;
        }

        public void getLayers(LayerManager lm, int Layer) {
            if (Layer == 0) {
                lm.append(blow);
            } else {
                lm.append(flag);
                lm.append(death);
            }
        }

        public void Die() {
            if (State != ALIVE) return;
            State = BLOWING;
            flag.setVisible(false);
            blow.setVisible(true);
            blow.startAnimation();
            blow.setPosition(box.x + (box.width >> 1) - (blow.getWidth() >> 1), box.y + (box.height >> 1) - (blow.getHeight() >> 1));
        }

        public void update(int dt) {
            if (State == BLOWING) {
                blow.UpdateNC();
                if (blow.IsFinished()) {
                    death.setVisible(true);
                    death.setPosition(box.x, box.y);
                    blow.setVisible(false);
                    State = DEATH;
                }
            }
        }
    }

    ;

    private int numBonuses;

    private Bonus bonus;

    private Flag flag;

    /** Creates a new instance of Game */
    public Game(TanksMidlet midlet) {
        super(midlet);
        SpriteManager.getIt().LoadFromXML("/sprites.xml");
        playLevel("/testlevel2.xml");
    }

    public void cleanUp() {
        level = null;
        System.gc();
    }

    public void playLevel(String levelname) {
        level = new Level();
        level.LoadFromXML(levelname);
        Player = new Tank(Tank.GOOD_TANK1);
        Player.init();
        AABox p = level.getPlayerRespawnPoint();
        Player.setPos(p.x, p.y);
        enemies = new TankGroup(10, 3, level, Player);
        flag = new Flag(level);
        bonus = new Bonus();
        lm = new LayerManager();
        bonus.getLayers(lm, 0);
        Player.getLayers(lm, 0);
        enemies.getLayers(lm, 0);
        level.getLayers(lm, 0);
        flag.getLayers(lm, 0);
        flag.getLayers(lm, 1);
        Player.getLayers(lm, 1);
        enemies.getLayers(lm, 1);
        level.getLayers(lm, 1);
        level.getLayers(lm, 2);
        prevMillis = System.currentTimeMillis();
        Player.Born();
        prevTime = prevMillis;
        bonus.appearAt(bonus.CLOCK, 100, 100, bonusTime);
        txtbox = new TextBox();
        txtbox.loadFontsFromXML("/fonts.xml");
    }

    public void lifecycle(Graphics g) {
        currentMillis = System.currentTimeMillis();
        int diffTime = (int) (currentMillis - prevMillis);
        if (currentMillis - prevTime > 1000) {
            FPS = (int) ((frameCount * 1000) / (currentMillis - prevTime));
            prevTime = currentMillis;
            frameCount = 0;
        } else frameCount++;
        if (isGameOver) {
        } else if ((Keys & KEY_UP) != 0) {
            Player.move(0, -1, diffTime);
        } else if ((Keys & KEY_DOWN) != 0) {
            Player.move(0, 1, diffTime);
        } else if ((Keys & KEY_LEFT) != 0) {
            Player.move(-1, 0, diffTime);
        } else if ((Keys & KEY_RIGHT) != 0) {
            Player.move(1, 0, diffTime);
        } else if ((Keys & KEY_FIRE) != 0) {
            Player.Fire(diffTime);
        }
        Player.update(diffTime, level);
        enemies.update(diffTime);
        flag.update(diffTime);
        if (enemies.isSomeOneGotShot()) {
            bonus.randomAppear(level, bonusTime);
        }
        if (enemies.checkBulletCollision(flag.box)) {
            flag.Die();
            isGameOver = true;
        }
        int b = Player.testBulletsCollision(flag.box);
        if (b != -1) {
            flag.Die();
            Player.blowBullet(b);
            isGameOver = true;
        }
        level.update(currentMillis);
        if (bonus.update(diffTime, Player)) {
            if (bonus.Type == bonus.STAR) Player.upgrade(); else if (bonus.Type == bonus.NEWLIFE) {
            } else if (bonus.Type == bonus.BOMB) {
                enemies.dieAlive();
            } else if (bonus.Type == bonus.CLOCK) {
                enemies.freeze(freezeTime);
            }
        }
        g.setColor(255, 255, 255);
        lm.paint(g, 10, 25);
        g.drawString("FPS: " + Integer.toString(FPS), 0, 0, 0);
        if (isGameOver) {
            txtbox.animateText(100);
        }
        txtbox.drawAText(g, 25, 100, "���� ��������", diffTime);
        prevMillis = currentMillis;
        Keys = 0;
    }
}
