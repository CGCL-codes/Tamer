package edu.gatech.cs2340.teamrocket.model.pokemon.ailments;

import edu.gatech.cs2340.teamrocket.model.pokemon.Pokemon;

/**
 * Represents a Curse status ailment.
 *
 * @author Joe Tacheron
 */
public class Curse implements Ailment {

    private static final int DAMAGE = Pokemon.MAX_HEALTH / 4;

    /**
     * Lose 1/4 of max health each turn.
     *
     * @param t
     * @return
     */
    @Override
    public String affect(Pokemon t) {
        t.takeDamage(DAMAGE);
        if (t.isFainted()) return faintMessage(t); else return null;
    }

    @Override
    public String catchMessage(Pokemon t) {
        return t + " is cursed!";
    }

    @Override
    public String healMessage(Pokemon t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String faintMessage(Pokemon t) {
        return t + " fainted from his curse!";
    }

    @Override
    public String hurtMessage(Pokemon t) {
        return t + " was hurt by its curse!";
    }

    /**
     *
     * @return false
     */
    @Override
    public boolean healedByFullHeal() {
        return false;
    }

    /**
     *
     * @return "CRS" colored purple
     */
    @Override
    public String statusString() {
        return "<span color='purple'>CRS</span>";
    }
}
