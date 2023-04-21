package gr.skill;

import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import gr.bluevibe.fire.components.Component;
import gr.bluevibe.fire.displayables.FireScreen;

/**
 * @author padeler
 */
public class World extends Component {

    private Random rnd = new Random(System.currentTimeMillis());

    private Image worldLayer = null;

    private Image gameLayer = null;

    private Image frontLayer = null;

    private Image backLayer = null;

    private Hero hero;

    private int score = 0;

    private int clockStep;

    private int caveSize;

    private int caveGeneratorPos;

    private int caveGeneratorSpeed;

    private int caveGeneratorCounter;

    private int caveGeneratorRandomSpeedReverse;

    private int caveGeneratorRandomSpeedChange;

    private int frameCounter = 0;

    private Skill parent;

    /**
	 * 
	 */
    public World(Skill parent) {
        this.parent = parent;
        hero = new Hero();
    }

    public void validate() {
        setHeight(FireScreen.getScreen(null).getHeight());
        frameCounter = 0;
        clockStep = 3;
        caveSize = (getHeight() * 1) / 3;
        caveGeneratorPos = getHeight() / 2;
        caveGeneratorSpeed = 2;
        caveGeneratorCounter = 0;
        caveGeneratorRandomSpeedReverse = 10;
        caveGeneratorRandomSpeedChange = 35;
        if (frontLayer == null) {
            frontLayer = Image.createImage(getWidth(), getHeight());
            try {
                Image tile = Image.createImage(this.getClass().getResourceAsStream("/front.jpg"));
                int tw = tile.getWidth();
                int th = tile.getHeight();
                int xmax = getWidth() / tw;
                int ymax = getHeight() / th;
                Graphics g = frontLayer.getGraphics();
                for (int i = 0; i <= xmax; ++i) {
                    for (int j = 0; j <= ymax; ++j) {
                        g.drawImage(tile, i * tw, j * th, Graphics.TOP | Graphics.LEFT);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Graphics g = frontLayer.getGraphics();
                g.setColor(0x00000000);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        if (backLayer == null) {
            backLayer = Image.createImage(getWidth(), getHeight());
            try {
                Image tile = Image.createImage(this.getClass().getResourceAsStream("/back.jpg"));
                int tw = tile.getWidth();
                int th = tile.getHeight();
                int xmax = getWidth() / tw;
                int ymax = getHeight() / th;
                Graphics g = backLayer.getGraphics();
                for (int i = 0; i <= xmax; ++i) {
                    for (int j = 0; j <= ymax; ++j) {
                        g.drawImage(tile, i * tw, j * th, Graphics.TOP | Graphics.LEFT);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Graphics g = backLayer.getGraphics();
                g.setColor(0x00000000);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        if (worldLayer == null) {
            worldLayer = Image.createImage(getWidth(), getHeight());
            gameLayer = Image.createImage(getWidth(), getHeight());
            hero.x = (getWidth()) / 3;
            hero.y = getHeight() / 2;
        }
    }

    public void paint(Graphics g) {
        g.drawImage(worldLayer, 0, 0, Graphics.TOP | Graphics.LEFT);
        g.drawImage(hero.getSprite(), hero.x, hero.y, Graphics.HCENTER | Graphics.VCENTER);
        g.setColor(0x000000FF);
        g.drawString("" + score, 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    private boolean checkIfCrashed() {
        if (hero.y <= 2 || hero.y >= getHeight() - 2) {
            System.out.println("Crash !!!!!!!!!!!");
            return true;
        }
        int[] rgb = new int[16];
        gameLayer.getRGB(rgb, 0, 4, hero.x - 2, hero.y - 2, 4, 4);
        int res = 0xFFFFFFFF;
        for (int i = 0; i < rgb.length; ++i) res &= rgb[i];
        if (res != 0xFFFFFFFF) {
            System.out.println("Crash !!!!!!!!!!!");
            return true;
        }
        return false;
    }

    int lmX = 0, lmY = 0;

    public boolean clock() {
        boolean crashed = checkIfCrashed();
        if (crashed) {
            parent.gameOver(score);
            return false;
        }
        hero.clock();
        Graphics g = gameLayer.getGraphics();
        g.copyArea(clockStep, 0, gameLayer.getWidth() - clockStep, gameLayer.getHeight(), 0, 0, Graphics.TOP | Graphics.LEFT);
        Graphics wg = worldLayer.getGraphics();
        wg.copyArea(clockStep, 0, gameLayer.getWidth() - clockStep, gameLayer.getHeight(), 0, 0, Graphics.TOP | Graphics.LEFT);
        score++;
        caveGeneratorCounter++;
        updateDifficulty();
        int h = getHeight();
        int w = getWidth();
        if (rnd.nextLong() % (100) < caveGeneratorRandomSpeedReverse) caveGeneratorSpeed = -caveGeneratorSpeed;
        if (rnd.nextLong() % (100) < caveGeneratorRandomSpeedChange) {
            caveGeneratorSpeed += (rnd.nextLong() % (2) == 0 ? -1 : 1);
            if (caveGeneratorSpeed >= 3 || caveGeneratorSpeed <= -3) caveGeneratorSpeed = 0;
        }
        for (int i = clockStep; i > 0; --i) {
            frameCounter++;
            if (caveGeneratorPos >= h || caveGeneratorPos < 0) caveGeneratorSpeed = -caveGeneratorSpeed;
            caveGeneratorPos += caveGeneratorSpeed;
            g.setColor(0x00000000);
            g.drawLine(w - i, 0, w - i, h);
            g.setColor(0xFFFFFFFF);
            g.drawLine(w - i, (caveGeneratorPos - (caveSize / 2)), w - i, (caveGeneratorPos + (caveSize / 2)));
            wg.drawRegion(frontLayer, frameCounter % w, 0, 1, h, Sprite.TRANS_NONE, w - i, 0, Graphics.TOP | Graphics.LEFT);
            wg.drawRegion(backLayer, frameCounter % w, (caveGeneratorPos - (caveSize / 2)), 1, caveSize, Sprite.TRANS_NONE, w - i, (caveGeneratorPos - (caveSize / 2)), Graphics.TOP | Graphics.LEFT);
        }
        return true;
    }

    private void updateDifficulty() {
        if (caveGeneratorCounter % 100 == 0) ++clockStep;
    }

    public boolean keyEvent(int key) {
        if (key == Canvas.FIRE) hero.fire();
        return true;
    }

    public boolean isAnimated() {
        return true;
    }

    class Hero {

        static final long timeStep = FireScreen.CLOCK_STEP;

        static final int maxSpeed = 10000;

        static final int heroRadious = 1;

        static final int heroEngineStep = -1500;

        static final int gravityStep = 2400;

        private Image sprite[] = new Image[3];

        int x;

        int y;

        long vy = 0;

        public Hero() {
            try {
                sprite[0] = Image.createImage(this.getClass().getResourceAsStream("/ship.png"));
                sprite[1] = Image.createImage(this.getClass().getResourceAsStream("/ship-up.png"));
                sprite[2] = Image.createImage(this.getClass().getResourceAsStream("/ship-down.png"));
            } catch (IOException e) {
                System.out.println("Failed to load ship sprite.");
            }
        }

        /**
		 * adjust horizontal speed (vy)
		 */
        void fire() {
            vy += heroEngineStep;
        }

        /**
		 * time related events like position changes and gravity induced speed
		 * change. Reduce the vertical speed (vx) only if vy>=0
		 */
        void clock() {
            vy += (gravityStep * timeStep) / 1000;
            if (vy > maxSpeed) vy = maxSpeed;
            if (vy < -maxSpeed) vy = -maxSpeed;
            y += (vy * timeStep) / 100000;
        }

        public Image getSprite() {
            if (vy < heroEngineStep) return sprite[1];
            if (vy > gravityStep) return sprite[2];
            return sprite[0];
        }
    }
}
