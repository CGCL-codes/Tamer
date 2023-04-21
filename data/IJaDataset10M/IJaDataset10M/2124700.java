package ch.unisi.inf.pfii.teamblue.jark.model.bonus;

import ch.unisi.inf.pfii.teamblue.jark.model.Game;

/**
 * Decrease the player lives by one.
 * 
 * @author Stefano.Pongelli@lu.unisi.ch, Thomas.Selber@lu.unisi.ch
 * @version $LastChangedDate: 2009-05-24 08:56:38 -0400 (Sun, 24 May 2009) $
 * 
 */
public final class RemoveLifeBonus extends PlayerBonus {

    public RemoveLifeBonus() {
        super(0);
        super.setLife(INSTANTANEOUS);
    }

    @Override
    public final String toString() {
        return "malus_removelife";
    }

    @Override
    public final void apply(final Game game) {
        game.getPlayer().decrementLives();
    }
}
