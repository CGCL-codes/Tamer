package net.harrierattack.ordinance;

import net.engine.GamePanel;
import net.engine.cel.CelStore;
import net.engine.graphics.Sprite;
import net.engine.math.Angle;
import net.engine.math.Float2;
import net.harrierattack.HarrierAttackPanel;
import net.harrierattack.explosion.Explosion;
import java.util.Random;

public class Projectile extends Sprite {

    public Projectile(GamePanel gamePanel, float x, float y, float velocityX, double angle, float speed, String celHelperName) {
        super(gamePanel, x, y);
        addCelsFromCelHelper(CelStore.getInstance().get(celHelperName));
        addBoundingBoxes(0);
        setLayer(16);
        setName(celHelperName);
        celFrame = Angle.getFrameForAngle(angle, 0, 7, 1, true);
        collisionFrame = celFrame;
        this.velocity = Angle.angleToVector(angle, speed);
        setPosition(new Float2(getPosition()).add(new Float2(velocity).multiply(4)));
        this.velocity.x += velocityX;
    }

    public boolean isOnGround() {
        HarrierAttackPanel harrierAttackPanel = (HarrierAttackPanel) gamePanel;
        int height = harrierAttackPanel.terrain.getHeight((int) getPosition().x);
        if (height <= getPosition().y) {
            return true;
        }
        return false;
    }

    public void explode() {
        Random random = gamePanel.getRandom();
        float x = getPosition().x + random.nextInt(9) - 4;
        float y = getPosition().y + random.nextInt(9) - 4;
        int rate = random.nextInt(2) + 2;
        new Explosion(gamePanel, x, y, "Small Explosion", rate);
        remove();
    }
}
