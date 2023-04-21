package world.shot.weapon;

import world.shot.Shot;
import utilities.Location;
import world.owner.Owner;

/**
 * the weapon attached to a unit that determines how
 * the unit shoots
 * @author Jack
 *
 */
public abstract class Weapon {

    boolean fired = false;

    int damage;

    int range;

    int movement;

    int shotSize = 3;

    int shotCount;

    int maxShotCount;

    /**
	 * creates a new weapon
	 * @param damage the damage inflicted when a shot from the
	 * weapon collides with a target
	 * @param range the range of the weapon's shots
	 * @param shotSpeed the speed at which the weapon's shots
	 * travel (pixels/iteration)
	 * @param reloadTime number of iterations before weapon can be
	 * fired again
	 */
    public Weapon(int damage, int range, int shotSpeed, int reloadTime) {
        this.damage = damage;
        this.range = range;
        maxShotCount = reloadTime;
        this.movement = shotSpeed;
    }

    public void fireWeapon() {
        fired = true;
    }

    public boolean isFired() {
        return fired;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public Shot getShot(Owner o, Location start, Location end) {
        Shot s = new Shot(o, start, end, damage, movement, shotSize);
        return s;
    }

    /**
	 * increments the shot counter, when it reaches its
	 * max point (reload time) the weapon can be fired again
	 */
    public void updateShotCount() {
        shotCount++;
        if (shotCount == maxShotCount) {
            shotCount = 0;
            fired = false;
        }
    }
}
