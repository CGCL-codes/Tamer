package ch.unisi.inf.pfii.teamred.pacman.model;

/**
 * This abstract class defines an item that can be found in the board of the
 * game.
 * 
 * @author mark.pruneri@lu.unisi.ch
 * @author luca.vignola@lu.unisi.ch
 * 
 */
public abstract class Item {

    private String name;

    public abstract String toString();

    public abstract int doAction(final Creature creature);

    public final String getName() {
        return name;
    }

    protected final void setName(String name) {
        this.name = name;
    }
}
