package ronp.lib.entities.characters;

import ronp.lib.entities.Damagable;
import ronp.lib.entities.GravityEntity;
import ronp.lib.entities.weapons.Weapon;
import ronp.lib.input.MCMoveListener;
import ronp.lib.tools.ConcurrentHashMapWrapper;

/**
 * Extend this to be the MainCharacter. Only one should be created.
 * 
 * @author Daniel Centore
 *
 */
public abstract class MainCharacter extends GravityEntity implements Damagable {

    private ConcurrentHashMapWrapper<MCMoveListener> moveListeners;

    private double health;

    private Weapon weapon;

    /**
	 * Creates a {@link MainCharacter}
	 */
    public MainCharacter() {
        moveListeners = new ConcurrentHashMapWrapper<MCMoveListener>();
        health = getMaximumHealth();
        weapon = null;
    }

    public void clearMoveListeners() {
        moveListeners.clear();
    }

    public void addMoveListener(MCMoveListener m) {
        moveListeners.add(m);
    }

    public void removeMoveListener(MCMoveListener m) {
        moveListeners.remove(m);
    }

    @Override
    public void incrementX(double by) {
        super.incrementX(by);
        for (MCMoveListener m : moveListeners) m.mcMoved();
    }

    @Override
    public void incrementY(double by) {
        super.incrementY(by);
        for (MCMoveListener m : moveListeners) m.mcMoved();
    }

    @Override
    public void setX(double by) {
        super.setX(by);
        for (MCMoveListener m : moveListeners) m.mcMoved();
    }

    @Override
    public void setY(double by) {
        super.setY(by);
        for (MCMoveListener m : moveListeners) m.mcMoved();
    }

    /**
	 * @return The number of pixels we should move left and right per iteration
	 */
    public abstract double getMoveSpeed();

    /**
	 * @return How many pixels we jump up
	 */
    public abstract int getJumpHeight();

    @Override
    public boolean applyDamage(double damage) {
        double h = health;
        h -= damage;
        if (h < 0) h = 0;
        health = h;
        boolean died = (health == 0);
        if (died) level.youDied();
        return died;
    }

    @Override
    public void applyRevive(double amount) {
        double h = health;
        h += amount;
        if (h > getMaximumHealth()) h = getMaximumHealth();
        health = h;
    }

    /**
	 * Revives the main character completely
	 */
    public void revive() {
        health = getMaximumHealth();
    }

    @Override
    public double getCurrentHealth() {
        return health;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
