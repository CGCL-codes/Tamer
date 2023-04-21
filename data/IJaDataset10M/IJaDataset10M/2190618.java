package hailmary.bloodbowl;

import java.awt.Image;

public class Player {

    private int number;

    private String name;

    private String positionName;

    private String id;

    private int movement;

    private int strength;

    private int agility;

    private int armor;

    private String injuriesString;

    private String skillsString;

    private String costString;

    private Image picture;

    private Team team;

    private int completions;

    private int touchdowns;

    private int interceptions;

    private int casualties;

    private int mvps;

    private int spps;

    public Player() {
    }

    /**
	 * @return Returns the agility.
	 */
    public int getAgility() {
        return agility;
    }

    /**
	 * @return Returns the armor.
	 */
    public int getArmor() {
        return armor;
    }

    /**
	 * @return Returns the movement.
	 */
    public int getMovement() {
        return movement;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the positionName.
	 */
    public String getPositionName() {
        return positionName;
    }

    /**
	 * @return Returns the skillsString.
	 */
    public String getSkillsString() {
        return skillsString;
    }

    /**
	 * @return Returns the strength.
	 */
    public int getStrength() {
        return strength;
    }

    /**
	 * @return Returns the number.
	 */
    public int getNumber() {
        return number;
    }

    /**
	 * @param number The number to set.
	 */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
	 * @param agility The agility to set.
	 */
    public void setAgility(int agility) {
        this.agility = agility;
    }

    /**
	 * @param armor The armor to set.
	 */
    public void setArmor(int armor) {
        this.armor = armor;
    }

    /**
	 * @param movement The movement to set.
	 */
    public void setMovement(int movement) {
        this.movement = movement;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @param positionName The positionName to set.
	 */
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    /**
	 * @param skillsString The skillsString to set.
	 */
    public void setSkillsString(String skillsString) {
        this.skillsString = skillsString;
    }

    /**
	 * @param strength The strength to set.
	 */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
	 * @return Returns the costString.
	 */
    public String getCostString() {
        return costString;
    }

    /**
	 * @param costString The costString to set.
	 */
    public void setCostString(String costString) {
        this.costString = costString;
    }

    /**
	 * @return Returns the pictureFile.
	 */
    public Image getPicture() {
        return picture;
    }

    /**
	 * @param pictureFile The pictureFile to set.
	 */
    public void setPicture(Image picture) {
        this.picture = picture;
    }

    /**
	 * @return Returns the team.
	 */
    public Team getTeam() {
        return team;
    }

    /**
	 * @param team The team to set.
	 */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
	 * @return Returns the casualties.
	 */
    public int getCasualties() {
        return casualties;
    }

    /**
	 * @param casualties The casualties to set.
	 */
    public void setCasualties(int casualties) {
        this.casualties = casualties;
    }

    /**
	 * @return Returns the completions.
	 */
    public int getCompletions() {
        return completions;
    }

    /**
	 * @param completions The completions to set.
	 */
    public void setCompletions(int completions) {
        this.completions = completions;
    }

    /**
	 * @return Returns the interceptions.
	 */
    public int getInterceptions() {
        return interceptions;
    }

    /**
	 * @param interceptions The interceptions to set.
	 */
    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    /**
	 * @return Returns the mvps.
	 */
    public int getMvps() {
        return mvps;
    }

    /**
	 * @param mvps The mvps to set.
	 */
    public void setMvps(int mvps) {
        this.mvps = mvps;
    }

    /**
	 * @return Returns the spps.
	 */
    public int getSpps() {
        return spps;
    }

    /**
	 * @param spps The spps to set.
	 */
    public void setSpps(int spps) {
        this.spps = spps;
    }

    /**
	 * @return Returns the touchdowns.
	 */
    public int getTouchdowns() {
        return touchdowns;
    }

    /**
	 * @param touchdowns The touchdowns to set.
	 */
    public void setTouchdowns(int touchdowns) {
        this.touchdowns = touchdowns;
    }

    /**
	 * @return Returns the injuriesString.
	 */
    public String getInjuriesString() {
        return injuriesString;
    }

    /**
	 * @param injuriesString The injuriesString to set.
	 */
    public void setInjuriesString(String injuriesString) {
        this.injuriesString = injuriesString;
    }

    /**
	 * Calculates the players number of Star player points based on number of completions,
	 * touchdowns, interceptions, casualties and MVPs
	 * @return Calculated number of SPPs
	 */
    public int calculateSpps() {
        int numSpps = (this.getCompletions() * 1) + (this.getTouchdowns() * 3) + (this.getInterceptions() * 2) + (this.getCasualties() * 2) + (this.getMvps() * 5);
        return numSpps;
    }

    /**
	 * @return
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param playerId What to set the player's id to.
	 */
    public void setId(String playerId) {
        id = playerId;
    }
}
