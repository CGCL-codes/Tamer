package content;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 * @author Justin
 *
 */
public class EasyAI extends AI {

    private Timer fireTimer;

    private final int fireRate = 700;

    public EasyAI(int x, int y) {
        PUP_SPAWN_PROB = 0.20;
        scoreValue = 10;
        SpriteImage = new BufferedImage[4];
        SpriteImage[0] = LoadSpriteImage("graphics/enemy1.gif");
        SpriteImage[1] = LoadSpriteImage("graphics/enemy2.gif");
        SpriteImage[2] = LoadSpriteImage("graphics/enemy3.gif");
        SpriteImage[3] = LoadSpriteImage("graphics/enemy4.gif");
        this.health = 60;
        setAlive(true);
        setX(x);
        setY(y);
        setVelx(0);
        setVely(2);
        fireTimer = new Timer(fireRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireReady = true;
            }
        });
        fireTimer.start();
    }

    @Override
    public ArrayList<Projectile> shoot(ArrayList<Projectile> projectileList, Player player) {
        if (!explode) {
            Projectile LevelOneProjectile2 = new StockBullet(0, 5);
            LevelOneProjectile2.setX(this.getX() + this.SpriteImage[0].getWidth() / 2 - LevelOneProjectile2.SpriteImage[0].getWidth() / 2);
            LevelOneProjectile2.setY(this.getY() + SpriteImage[0].getHeight());
            projectileList.add(LevelOneProjectile2);
        }
        return projectileList;
    }

    @Override
    public void updateSprite() {
        super.updateSprite();
        Animate();
        if (y > 200) {
            vely = 0;
            velx = 2;
        }
        if (x > 800 - SpriteImage[0].getWidth()) {
            vely = 2;
            velx = 0;
        }
        if (!isAlive()) {
            fireTimer.stop();
        }
    }
}
