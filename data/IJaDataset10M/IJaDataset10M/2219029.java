package units.missile;

import sound.Sound;
import sushiwar.Screen;
import sushiwar.Screen.GameStatus;
import units.Unit;

/**
 *
 * @author Hossomi
 */
public abstract class Missile extends Unit {

    private int damage;

    private double explosionRadius;

    protected double explosionPower;

    protected static final double MAX_SPEED = 75;

    public Missile(double x, double y, int w, int h, int damage, int explosionRadius, int explosionPower, Screen screen) {
        super(x, y, w, h, screen);
        setCollisionBoxCenter(5, 5);
        this.damage = damage;
        this.explosionRadius = explosionRadius;
        this.explosionPower = explosionPower;
    }

    @Override
    public int update() {
        int updateStatus = super.update();
        if (updateStatus > 0) explode();
        int boxStatus = screen.isBoxInScreen(box);
        if ((boxStatus & 0x01011) != 0 && (boxStatus & 0x10000) > 0) {
            screen.removeMissile(this);
            this.moveTimer.finish();
            screen.setGameStatus(GameStatus.DAMAGE_DEAL);
        }
        return updateStatus;
    }

    public void explode() {
        screen.explode(x, y, damage, explosionRadius, explosionPower);
        screen.removeMissile(this);
        this.moveTimer.finish();
    }
}
