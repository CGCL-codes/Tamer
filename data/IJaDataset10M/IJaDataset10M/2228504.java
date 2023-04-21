package GameThings;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import RootP.Level;

public class Oillamp extends PlayerTool {

    int y_start;

    int wurfweite = 200;

    double a = 0.003;

    int x_parabola = (int) -wurfweite / 2, y_parabola = 0;

    int speed = 15;

    AudioClip sound;

    Figure figure;

    Image firePic0, firePic1;

    boolean hasHitGround;

    int eventCounter = 0;

    public Oillamp() {
        super(0, 0, 30, 23, "oillamp");
        pic = level.getPic("gameThing_images/oillamp.png");
        try {
            File file = new File("gameThing_images/burn.wav");
            sound = Applet.newAudioClip(file.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        firePic0 = Level.getPic("gameThing_images/oillamp_fire_0.png");
        firePic1 = Level.getPic("gameThing_images/oillamp_fire_1.png");
    }

    public boolean use(Figure FIGURE) {
        GameThing.level.eventLoop.figures.add(this);
        x = FIGURE.x;
        y = FIGURE.y;
        y_start = y - (int) (a * Math.pow(x_parabola, 2));
        setVisible(true);
        collidable = true;
        figure = FIGURE;
        return true;
    }

    @Override
    public void doAction(Figure FIGURE) {
        if ((FIGURE instanceof Player) && hasHitGround) ((Player) FIGURE).burn(); else if (FIGURE instanceof WoodSoldier) ((WoodSoldier) FIGURE).burn();
    }

    @Override
    public void event() {
        if (!hasHitGround) {
            x += speed;
            x_parabola += speed;
            y_parabola = (int) (a * Math.pow(x_parabola, 2));
            y = y_start + y_parabola;
            setLocation(x, y);
            GameThing collision = collisionWithAnything();
            if (collision != null && collision != figure) {
                if (collision instanceof Damageable) {
                    sound.play();
                    System.out.println("Fire, Water ,Burn");
                    if (collision.getType().equals("woodsoldier")) {
                        ((WoodSoldier) collision).burn();
                        if (figure instanceof Player) ((Player) figure).dance();
                    } else if (collision instanceof Tree) ((Tree) collision).burn(); else ((Damageable) collision).damagedBy(this, 10);
                    GameThing.ThingList.remove(this);
                    GameThing.level.eventLoop.figures.remove(this);
                }
            } else if (x_parabola > wurfweite / 2) {
                hasHitGround = true;
            }
        } else {
            if (eventCounter % 2 < 1) pic = firePic0; else pic = firePic1;
            eventCounter++;
            if (eventCounter >= 30) {
                ThingList.remove(this);
                level.eventLoop.figures.remove(this);
            }
        }
    }
}
