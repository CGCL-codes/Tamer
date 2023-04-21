package game.characters;

import game.actions.ActionMenu;
import game.terrain.IMapUnit;
import game.things.*;

/**
 *
 */
public class GenericCharacter implements ICharacter {

    /**
	 * @uml.property  name="name"
	 */
    protected String name;

    /**
	 * @uml.property  name="energy"
	 */
    protected int energy;

    /**
	 * @uml.property  name="currentPosition"
	 * @uml.associationEnd  
	 */
    protected IMapUnit currentPosition;

    /**
	 * @uml.property  name="isAlive"
	 */
    protected boolean isAlive = true;

    /**
	 * @uml.property  name="capital"
	 */
    protected int capital = 0;

    /**
	 * @uml.property  name="actionsMenu"
	 * @uml.associationEnd  
	 */
    private ActionMenu actionsMenu = null;

    /**
	 * @see game.characters.ICharacter#setLocation(game.terrain.IMapUnit)
	 */
    public void setLocation(IMapUnit pos) {
        if (currentPosition != null) currentPosition.removeCharecter(this);
        currentPosition = pos;
        currentPosition.addCharacter(this);
    }

    /**
	 * @see game.characters.ICharacter#getCurrentLocation()
	 */
    public IMapUnit getCurrentLocation() {
        return currentPosition;
    }

    public String toString() {
        return name + " (energy: " + this.energy + ")";
    }

    /**
	 * @see game.characters.ICharacter#getEnenergy()
	 */
    public int getEnenergy() {
        return energy;
    }

    /**
	 * @see game.characters.ICharacter#increaseEnergy(int)
	 */
    public void increaseEnergy(int offset) {
        energy += offset;
    }

    /**
	 * @return
	 * @uml.property  name="capital"
	 */
    public int getCapital() {
        return capital;
    }

    /**
	 * @see game.characters.ICharacter#increaseCapital(int)
	 */
    public void increaseCapital(int offset) {
        capital += offset;
    }

    /**
	 * @see game.characters.ICharacter#decreaseCapital(int)
	 */
    public void decreaseCapital(int offset) {
        capital -= offset;
    }

    /**
	 * Constructor.
	 * 
	 * @param description
	 * @param energy
	 * @param actMenu
	 */
    public GenericCharacter(String description, int energy, ActionMenu actMenu) {
        super();
        this.name = description;
        this.energy = energy;
        actionsMenu = actMenu;
    }

    /**
	 * @see game.characters.ICharacter#decreaseEnergy(int)
	 */
    public void decreaseEnergy(int offset) {
        this.energy -= offset;
        if (this.energy <= 0) die();
    }

    /**
	 * Constructor. This method must be used together with create(args[]).
	 * 
	 * @param actionsMenu
	 */
    public GenericCharacter(ActionMenu actionsMenu) {
        super();
        this.actionsMenu = actionsMenu;
    }

    /**
	 * The first String must be the the energy.
	 * 
	 * @see game.characters.ICharacter#create(java.lang.String[])
	 */
    public void create(String[] args) {
        if (args.length >= 2) {
            this.name = args[0];
            this.energy = Integer.parseInt(args[1]);
        }
    }

    /**
	 * Create a gold thing with his capital. Remove the character of the map.
	 * 
	 * @see game.characters.ICharacter#die()
	 */
    public void die() {
        if (this.capital > 0) {
            IThing gold = new Gold(this.capital);
            this.getCurrentLocation().addThing(gold);
        }
        this.getCurrentLocation().removeCharecter(this);
        this.isAlive = false;
    }

    /**
	 * @see game.characters.ICharacter#getActionMenu()
	 */
    public ActionMenu getActionMenu() {
        return actionsMenu;
    }

    /**
	 * @see  game.characters.ICharacter#isAlive()
	 * @uml.property  name="isAlive"
	 */
    public boolean isAlive() {
        return isAlive;
    }

    /**
	 * @return
	 * @uml.property  name="name"
	 */
    public String getName() {
        return name;
    }
}
