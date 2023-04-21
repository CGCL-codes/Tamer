package game;

import java.awt.Point;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Animation implements Runnable {

    private static String resourceFolder = "/Users/micapo2607/Documents/workspace/SAF/src/com/mike/SAF/Resources/Sprites/";

    private int orientation;

    private static String lr = "Left-Right";

    private static String rl = "Right-Left";

    private String location;

    private int numberOfSprites;

    private String[] spriteLocations;

    ArrayList<ImageIcon> sprites = new ArrayList<ImageIcon>();

    private Thread thread;

    private JLabel avatar;

    private Point from;

    private Point to;

    private int speed;

    public ThreadHandler th;

    private boolean animationFinished = false;

    private static int step = 70;

    private static int jump = 140;

    public Animation(JLabel player, String name, int orientation, Point p, int speed, ThreadHandler th) {
        this.speed = speed;
        this.from = p;
        this.orientation = orientation;
        this.to = getResultingPoint(name, p, orientation);
        this.avatar = player;
        this.sprites = getSprites(name, orientation);
        avatar.setLocation(p.x, p.y);
        this.th = th;
        thread = new Thread(this);
        thread.start();
    }

    private void setPriorities() {
        if (orientation == -1) thread.setPriority(Thread.MAX_PRIORITY); else thread.setPriority(Thread.NORM_PRIORITY);
    }

    @Override
    public void run() {
        playAnimation();
    }

    public void setAnimation(String name, int orientation, Point p, ThreadHandler th) {
        this.from = p;
        System.out.println(name);
        sprites = getSprites(name, orientation);
        this.to = getResultingPoint(name, p, orientation);
        int boardWidth = avatar.getParent().getSize().width - 120;
        if (this.to.x > boardWidth || this.to.x < 0) {
            name = "stand";
            this.to = p;
        }
        thread = new Thread(this);
        setPriorities();
        thread.start();
        run();
    }

    public Thread getThread() {
        return thread;
    }

    private ArrayList<ImageIcon> getSprites(String name, int orientation) {
        this.location = resourceFolder + ((orientation == 1) ? lr : rl) + "/" + name + "/";
        sprites = new ArrayList<ImageIcon>();
        File f = new File(this.location);
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.contains(".DS_Store")) return false; else return true;
            }
        };
        this.spriteLocations = f.list(filter);
        this.numberOfSprites = this.spriteLocations.length;
        for (String sprite : spriteLocations) {
            sprites.add(new ImageIcon(location + sprite));
        }
        return sprites;
    }

    public JLabel getAvatar() {
        return avatar;
    }

    private void playAnimation() {
        int state = 0;
        if (!sprites.isEmpty()) while (!isAnimationDone(state)) {
            Double s = (state + 1) * 1.0;
            Double n = this.numberOfSprites * 1.0;
            try {
                th.countDown();
                avatar.setIcon(sprites.get(state));
                Point tempLocation = new Point((from.x + ((int) ((to.x - from.x) * (s / n)))), to.y);
                avatar.setBounds(tempLocation.x, tempLocation.y, 120, 116);
                if (state + 1 != numberOfSprites) Thread.sleep(speed);
                th.awaitZero();
            } catch (InterruptedException e) {
            }
            state++;
        }
        animationFinished = true;
        thread = null;
    }

    private synchronized boolean isAnimationDone(int state) {
        boolean result = false;
        if (this.numberOfSprites == state) result = true;
        return result;
    }

    public boolean isAnimationFinished() {
        return this.animationFinished;
    }

    private Point getResultingPoint(String name, Point currentPoint, int orientation) {
        if (name.equals("run_towards")) {
            if (orientation == 1) {
                return new Point(currentPoint.x + jump, currentPoint.y);
            } else return new Point(currentPoint.x - jump, currentPoint.y);
        } else if (name.equals("walk_towards")) {
            if (orientation == 1) {
                return new Point(currentPoint.x + step, currentPoint.y);
            } else return new Point(currentPoint.x - step, currentPoint.y);
        } else if (name.equals("walk_away")) {
            if (orientation == 1) {
                return new Point(currentPoint.x - step, currentPoint.y);
            } else return new Point(currentPoint.x + step, currentPoint.y);
        } else if (name.equals("run_away")) {
            if (orientation == 1) {
                return new Point(currentPoint.x - jump, currentPoint.y);
            } else return new Point(currentPoint.x + jump, currentPoint.y);
        } else return currentPoint;
    }
}
