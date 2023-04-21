package ru.pbem.olympia.battles;

/**
 * User: roman.pavlov
 * Date: 15-Oct-2010
 * Time: 14:18:28
 */
public class Fighter implements FightingUnit {

    Combatant unit;

    FightingUnit shelter;

    boolean firstLine;

    int damageToDefense;

    int damageToHealth;

    public Fighter(Combatant unit, FightingUnit shelter) {
        this.unit = unit;
        this.shelter = shelter;
    }

    @Override
    public int getEffectiveDefense(boolean ignoreShelterBonus) {
        final int shelterBonus = shelter != null && shelter.isAlive() && !ignoreShelterBonus ? shelter.getEffectiveDefense(false) : 0;
        return unit.getDefense() - damageToDefense + shelterBonus;
    }

    @Override
    public int getEffectiveOffense(boolean ignoreDistance) {
        final int ranged = unit.getRangedAttack();
        final int melee = unit.getMeleeAttack();
        return (firstLine || ignoreDistance) && (melee > ranged) ? melee : ranged;
    }

    @Override
    public boolean isSiegeDevice() {
        return unit.isSiegeDevice();
    }

    @Override
    public boolean isAlive() {
        return unit.getHitPoints() > damageToHealth;
    }

    @Override
    public void hit(int i) {
        if (unit instanceof Shelter && getEffectiveDefense(false) > 0) {
            if (i < getEffectiveDefense(false)) {
                damageToDefense += i;
                i = 0;
            } else {
                i -= getEffectiveDefense(false);
                damageToDefense += getEffectiveDefense(false);
            }
        }
        damageToHealth += i;
    }

    @Override
    public void moveToFirstLine() {
        firstLine = true;
    }
}
