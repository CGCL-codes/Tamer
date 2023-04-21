package ac.uk.napier.dfisch.jwurm.gameobjects.actors.Projectile;

import ac.uk.napier.dfisch.jwurm.gameobjects.actors.Actor;
import ac.uk.napier.dfisch.jwurm.gameobjects.IState;
import javax.microedition.lcdui.game.Sprite;

/**
 *
 * @author kdfisch
 */
public class Projectile extends Actor {

    private double areaOfEffect;

    private double baseDamage;

    private DamageAttenuation attenuation;

    public double getAreaOfEffect() {
        return areaOfEffect;
    }

    public DamageAttenuation getAttenuation() {
        return attenuation;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public void update(int timeStep) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IState getState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setState(IState s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Projectile(Sprite s, String id) {
        super(s, id);
    }
}
