package content;

/**
 * @author Justin
 *
 */
public class Level1 extends Sprite {

    public Level1() {
        SpriteImage[0] = LoadSpriteImage("graphics/back1.gif");
        setX(0);
        setY(-(this.SpriteImage[0].getHeight() - 605));
        setVelx(0);
        setVely(1.0);
        alive = true;
    }

    public void updateSprite() {
        if (y < 0) {
            x = x + velx;
            y = y + vely;
        }
    }
}
