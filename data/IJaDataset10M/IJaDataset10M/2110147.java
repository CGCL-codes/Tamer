package juegos.lajoya;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import juegos.SpriteCache;
import juegos.Stage;

public class Hole {

    private ArrayList hoyos;

    private Stage stage;

    /** Creates a new instance of Hole */
    public Hole(Stage stage) {
        hoyos = new ArrayList();
        this.stage = stage;
    }

    public boolean isEmpty() {
        return (hoyos.size() == 0);
    }

    public void Act() {
        for (int i = 0; i < hoyos.size(); i++) {
            Hoyo h = (Hoyo) hoyos.get(i);
            if (!(h.Act())) {
                stage.removeHole(h.getX(), h.getY());
                hoyos.remove(i);
            }
        }
    }

    public void addHole(int x, int y) {
        Hoyo h = new Hoyo(x, y, stage);
        hoyos.add(h);
        stage.addChanges(x, y, Stage.HOLE);
    }

    public void paint(Graphics2D g, int X, int Y) {
        for (int i = 0; i < hoyos.size(); i++) {
            Hoyo h = (Hoyo) hoyos.get(i);
            if ((X == h.getX()) && (Y == h.getY())) {
                ((Hoyo) hoyos.get(i)).paint(g);
                break;
            }
        }
    }

    public class Hoyo {

        private int x, y;

        private int currentFrame;

        private int time;

        private Stage stage;

        private SpriteCache spriteCache;

        private String[] spriteNames;

        private static final int SPEED = 8;

        public Hoyo(int x, int y, Stage stage) {
            this.x = x;
            this.y = y;
            currentFrame = 0;
            time = 0;
            this.stage = stage;
            this.spriteCache = stage.getSpriteCache();
            spriteNames = new String[] { "hole1.gif", "hole2.gif", "hole3.gif" };
            for (int i = 0; i < spriteNames.length; i++) spriteCache.getSprite(spriteNames[i]);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void paint(Graphics2D g) {
            if (currentFrame == 0) return;
            g.drawImage(spriteCache.getSprite(spriteNames[currentFrame / SPEED]), x * Stage.CELL, y * Stage.CELL, stage);
        }

        private boolean Act() {
            if (time >= 150) {
                currentFrame++;
                return (currentFrame < SPEED * 3);
            }
            time++;
            return true;
        }
    }
}
