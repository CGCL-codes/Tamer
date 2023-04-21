package Code.Objects.Effects.Projectiles;

import org.jrabbit.base.graphics.transforms.Vector2d;
import org.jrabbit.base.managers.ResourceManager;
import org.jrabbit.standard.game.graphics.skins.animation.AnimatedSkin;

public class StandardBullet extends Bullet {

    public StandardBullet(String filepath, float range, double angle, float speed, int type, float damage, Vector2d origin, float barrelLength, float inaccuracy) {
        super(filepath, range, angle, speed, type, damage);
        adjust(origin, barrelLength, inaccuracy);
    }

    public StandardBullet(AnimatedSkin skin, float range, int type, float damage, float speed, double angle, Vector2d origin, float barrelLength, float inaccuracy) {
        super(skin, range, angle, speed, type, damage);
        adjust(origin, barrelLength, inaccuracy);
    }

    private void adjust(Vector2d origin, float barrelLength, float inaccuracy) {
        applySizing();
        location.set(origin);
        double theta = rotation.theta();
        location.addPolar(barrelLength, theta);
        rotation.rotate((ResourceManager.random().nextFloat() - 0.5f) * inaccuracy);
        theta = rotation.theta();
        location.addPolar(getScaledWidth() / 2, theta);
        updateGeometry();
    }

    protected void applySizing() {
    }
}
