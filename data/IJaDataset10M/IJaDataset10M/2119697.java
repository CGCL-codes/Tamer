package game.characters;

import game.actions.ActionMenu;
import game.terrain.IMapUnit;

/**
 * @author  vidma
 */
public interface ICharacter {

    /**
	 * This method is used to define parameters of the character.
	 * 
	 * @param args
	 */
    void create(String[] args);

    /**
	 * this method return the energy of the character
	 * 
	 * @return int
	 */
    int getEnenergy();

    /**
	 * this method return the name of the character.
	 * @return
	 * @uml.property  name="name"
	 */
    String getName();

    /**
	 * This method must increase the energy of +offset
	 * 
	 * @param offset
	 */
    void increaseEnergy(int offset);

    /**
	 * This method must decrease the energy of -offset
	 * 
	 * @param offset
	 */
    void decreaseEnergy(int offset);

    public int getCapital();

    /**
	 * this method must decrease the capital of -offset
	 * 
	 * @param offset
	 */
    void decreaseCapital(int offset);

    /**
	 * this method must increase the capital of +offset.
	 * 
	 * @param offset
	 */
    void increaseCapital(int offset);

    /**
	 * This method returns the location of the character.
	 * @return
	 * @uml.property  name="currentLocation"
	 * @uml.associationEnd  
	 */
    IMapUnit getCurrentLocation();

    /**
	 * This method defines the position of the character on the map
	 * 
	 * @param pos
	 */
    void setLocation(IMapUnit pos);

    /**
	 * Returns whether the character is alive
	 * 
	 */
    boolean isAlive();

    /**
	 * "kills" the character
	 * 
	 */
    void die();

    /**
	 * This method returns the ActionMenu, which contains the actions the character can performe.
	 * @return
	 * @uml.property  name="actionMenu"
	 * @uml.associationEnd  
	 */
    ActionMenu getActionMenu();
}
