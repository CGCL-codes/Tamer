package org.amityregion5.projectx.common.entities.items.held;

/**
 * A Weapon which can be used only within melee range.
 *
 * @author Mike DiBuduo
 */
public abstract class MeleeWeapon extends Weapon {

    private static final long serialVersionUID = 605L;

    public static final int MELEE_RANGE = 5;

    private int damage;

    public MeleeWeapon(int rate, int damage) {
        super(MELEE_RANGE, rate);
        this.damage = damage;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public boolean fire() {
        return true;
    }

    @Override
    public boolean hasAmmo() {
        return true;
    }
}
