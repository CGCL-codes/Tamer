package GamePlay.GameObjects;

import java.awt.Image;
import java.awt.Toolkit;
import GamePlay.GameEnv;
import GamePlay.Location;
import GamePlay.Collision.*;

public class UserShip extends GameObject {

    public static boolean up = false;

    public static boolean down = false;

    public static boolean left = false;

    public static boolean right = false;

    public static final double SPEED = 0.4;

    public static int which;

    public static final int screenwidth = 900;

    public static final int screenheight = 700;

    public int health = 100;

    public int score = 0;

    public int HEIGHT;

    public int WIDTH;

    public int numLives;

    public boolean mustShoot = false;

    private Image leftImage;

    private Image rightImage;

    public int numGuns = 1;

    public UserShip(GameEnv env) {
        super(env);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        myImage = toolkit.getImage("images/GOImages/theShipSmall.gif");
        WIDTH = 70;
        HEIGHT = 72;
        myLoc = new Location(900 / 2, 350);
        numLives = 5;
        CollisionShape body = new CollisionRect(2, this, new Location(0, 0), WIDTH, HEIGHT);
        this.addCollisionShape(body);
    }

    public UserShip(GameEnv env, int lives) {
        super(env);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        myImage = toolkit.getImage("images/GOImages/theShipSmall.gif");
        WIDTH = 70;
        HEIGHT = 72;
        myLoc = new Location(900 / 2, 550);
        numLives = lives;
        CollisionShape body = new CollisionRect(2, this, new Location(0, 0), WIDTH, HEIGHT);
        env.addCO(body, 2);
    }

    public void damage(int amount) {
        if (health > amount) health -= amount; else {
            Explosion exp = new Explosion(theEnv, 85, myLoc, .1);
            theEnv.addGO(exp, 3);
            if (numLives > 0) {
                health = 100;
                myLoc = new Location(900 / 2, 550);
                numLives--;
            } else {
                removeMe();
            }
        }
    }

    public void moveTowardCenter(long millis) {
        double moveSpeed = .005;
        myLoc.moveX((900 / 2 - myLoc.getX()) * moveSpeed);
        myLoc.moveY((700 / 2 - myLoc.getY()) * moveSpeed);
    }

    public void pulse(long millis) {
        double myX = myLoc.getX();
        double myY = myLoc.getY();
        if ((myX > -10 && myX < 860 && myY > 50 && myY < 650) == false) {
            moveTowardCenter(millis);
        } else {
            double amount = millis * this.SPEED;
            if (left) {
                myLoc.moveX(-amount);
            }
            if (right) {
                myLoc.moveX(amount);
            }
            if (up) {
                myLoc.moveY(-amount);
            }
            if (down) {
                myLoc.moveY(amount);
            }
        }
        if (mustShoot) {
            shoot();
            mustShoot = false;
        }
    }

    private void shoot() {
        double shift = WIDTH / (numGuns + 1);
        for (int x = 1; x <= numGuns; x++) {
            theEnv.addGO(new Bullet(myLoc.addLocs(new Location(shift * x, -10.0)), theEnv, true, 3), 2);
        }
    }

    public void collide(GameObject theGO) {
        if (theGO instanceof Saucer || theGO instanceof Saucer2) damage(1); else if (theGO instanceof Bullet) {
            damage(((Bullet) theGO).damage);
        }
    }
}
