package universe.server.database;

import universe.common.*;
import universe.server.*;

public class CivOptions implements java.io.Serializable {

    private RaceBase myFoundingSpecies;

    private PlayerType myPlayerType;

    /**  This is the designated constructor for objects
         *   of this class.  Default values are assigned
         *   to each attribute.
         *
	 */
    public CivOptions() {
        myFoundingSpecies = RaceRandom.RANDOM;
        myPlayerType = PlayerType.COMPUTER;
    }

    /**  This method returns the founding species stored in
	 *   the receiving instance.
         */
    public RaceBase getFoundingSpecies() {
        return myFoundingSpecies;
    }

    /** This method sets the founding species to be stored
	 *  in the receiving instance.
         *
         *  @param newValue The new founding species value.
	 *
         *  @see #getFoundingSpecies()
	 */
    public void setFoundingSpecies(RaceBase newValue) {
        myFoundingSpecies = newValue;
    }

    /**  This method returns the player type stored in
	 *   the receiving instance.
         */
    public PlayerType getPlayerType() {
        return myPlayerType;
    }

    /** This method sets the player type to be stored
	 *  in the receiving instance.
         *
         *  @param newValue The new player type value.
	 *
         *  @see #getPlayerType()
	 */
    public void setPlayerType(PlayerType newValue) {
        myPlayerType = newValue;
    }

    /** This method causes the civilization options object to
    	 *  adopt attribute values previously stored in the persistent
	 *  configuraiton object.  If no valid corresponding values
         *  can be found stored in the configuration, this method
	 *  simply leaves the current atrributes unchanged.
   	 *
   	 *  This method is called by the constructor method
    	 *  of the GameOptions object.
    	 *
    	 *  @param index Unique number used to distinguish each civilization object's values from its fellows.
    	 */
    public void readFromConfig(int index) {
        String key;
        PlayerType pt = null;
        RaceBase rb = null;
        String indexString = String.valueOf(index);
        if (index == 1) {
            myPlayerType = PlayerType.HUMAN;
        } else {
            key = Config.getString("gameoptions.civ." + indexString + ".player_type");
            if (key != null) {
                pt = PlayerType.FindByKey(key);
            }
            if (pt != null) {
                myPlayerType = pt;
            }
        }
        key = Config.getString("gameoptions.civ." + indexString + ".founding_species");
        if (key != null) {
            rb = RaceBase.FindByKey(key);
        }
        if (rb != null) {
            myFoundingSpecies = rb;
        } else {
            Log.warning("Did not find species with the key, " + key + ", that was specified in the config file for a starting civilization. " + "Assuming that its not defined/allowed in the current scheme/theme and using random species instead.");
        }
    }

    /** This method causes the civilization options object to
    	 *  persistently save its current values to the game
    	 *  configuration file so that they will be used the
    	 *  next time the game is run.
   	 *
   	 *  This method is called by the saveToConfig method
    	 *  of the GameOptions object.
    	 *
    	 *  @param index Unique number used to distinguish each civilization object's values from its fellows.
    	 */
    public void saveToConfig(int index) {
        String indexString = String.valueOf(index);
        Config.putString("gameoptions.civ." + indexString + ".player_type", myPlayerType.getKey());
        Config.putString("gameoptions.civ." + indexString + ".founding_species", myFoundingSpecies.getKey());
    }
}
