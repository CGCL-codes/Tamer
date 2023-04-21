package bbalc.core;

import bbalc.core.exceptions.*;

/**
 * This interface defines methods for all Team objects.
 */
public interface ITeam {

    /**
	 * Adds a Player to the Team.
	 * @param player The player to add to the team.
	**/
    public abstract void addPlayer(Player player);

    /**
	 * This method determines if an Apothecary is present.
	 * @return true if there is an apothecary, defaults to false.
	**/
    public abstract int getApothecaries();

    /**
	 * This method determines the number of Assistant Coaches the Team has.
	 * @return the number of AssistantCoaches;
	**/
    public abstract int getAssistantCoaches();

    /**
	 * This method determines the number of Cheerleader the Team has.
	 * @return the number of Cheerleader.
	**/
    public abstract int getCheerleaders();

    /**
	 * Determines the coaches name.
	 * @return the coaches name.
	**/
    public abstract String getCoach();

    /**
	 * This method determines the teams FanFactor.
	* @return the teams FanFactor.
	**/
    public abstract int getFanFactor();

    /**
	 * Determines the Teams name.
	 * @return the teams name.
	**/
    public abstract String getName();

    /**
	 * Determines all of the Teams Players.
	 * @return an Array of IPlayer.
	**/
    public abstract Player[] getPlayer();

    /**
	 * Determines Player Number "n" (1 <= n <= IRules.MAX_PLAYER).
	 * 
	 * @param n The players number.
	 * @return IPlayer The requested player. 
	**/
    public abstract Player getPlayer(int n) throws NoSuchPlayerException;

    /**
	 * Determines the Teams Race.
	 * @return the teams Race.
	**/
    public abstract String getRace();

    /**
	 * Determines the Teams ReRolls.
	 * @return the teams Rerolls.
	**/
    public abstract int getReRolls();

    /**
	 * Determines the Teams TeamRating.
	 * @return the TeamRating.
	**/
    public abstract int getTeamRating();

    /**
	 * Determines the Teams Treasury.
	 * @return the teams Treasury.
	**/
    public abstract int getTreasury();

    /**
	 * Determines the Teams Treasury.
	 * @return the teams Treasury as String.
	**/
    public abstract String getTreasuryString();

    /**
	 * This method determines if a Wizard is present.
	 * @return true, if there is a Wizard present in this team, defaults to false.
	**/
    public abstract int getWizard();

    /**
	 * This method sets the Apothecary.
	 * @param apothecary Set to true is there is an Apothcary in this team else use false.
	**/
    public abstract void setApothecary(int apothecarys);

    /**
	 * This method sets the number of Assistant Coaches.
	 * @param assistantcoaches The number of Assistantcoaches in this team.
	**/
    public abstract void setAssistantCoaches(int assistantcoaches);

    /**
	 * This method sets the number of Cheerleaders.
	 * @param cheerleaders The number of Cheerleaders in this team.
	**/
    public abstract void setCheerleaders(int cheerleaders);

    /**
	 * This method sets the coaches name.
	 * @param coach The coaches name.
	**/
    public abstract void setCoach(String coach);

    /**
	 * This method sets the FanFactor.
	 * @param fanfactor The Teams FanFactor.
	**/
    public abstract void setFanFactor(int fanfactor);

    /**
	 * This method sets the Teamname.
	 * @param name The teams name.
	**/
    public abstract void setName(String name);

    /**
	 * This method sets the race.
	 * @param race The teams race.
	**/
    public abstract void setRace(String race);

    /**
	 * This method sets the Rerolls
	 * @param rerolls the teams ReRolls.
	**/
    public abstract void setReRolls(int rerolls);

    /**
	 * This method sets the TeamRating.
	 * @param teamrating The Teams Rating.
	**/
    public abstract void setTeamRating(int teamrating);

    /**
	 * Sets the Treasury.
	 * @param treasury The teams Treasury.
	**/
    public abstract void setTreasury(int treasury);

    /**
	 * This method sets the Wizard.
	 * @param wizard Returns true if the team has a Wizard, defaults to false.
	**/
    public abstract void setWizard(int wizard);

    /**
	 * Sets the player.
	 * @param player an array of player.
	**/
    public abstract void setPlayer(Player[] player);

    /**
	 * Converts the teams to a (long) String.
	 * @return The teamdata as String.
	**/
    public abstract String toString();
}
