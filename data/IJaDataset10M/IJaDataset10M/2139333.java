package com.beacon.rpg.server.types;

/**
 *
 * @author cternent
 */
public class RPGCharacter {

    private Integer characterId;

    private String name;

    private String account;

    private byte[] avatar;

    private Integer health;

    private Integer observation;

    private Integer strength;

    private Integer intelligence;

    private Integer agility;

    private Integer charm;

    private String location;

    private Integer zoneId;

    private Integer x;

    private Integer y;

    private String faction;

    /**
     * @return the characterId
     */
    public Integer getCharacterId() {
        return characterId;
    }

    /**
     * @param characterId the characterId to set
     */
    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the health
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * @param health the health to set
     */
    public void setHealth(Integer health) {
        this.health = health;
    }

    /**
     * @return the observation
     */
    public Integer getObservation() {
        return observation;
    }

    /**
     * @param observation the observation to set
     */
    public void setObservation(Integer observation) {
        this.observation = observation;
    }

    /**
     * @return the strength
     */
    public Integer getStrength() {
        return strength;
    }

    /**
     * @param strength the strength to set
     */
    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    /**
     * @return the intelligence
     */
    public Integer getIntelligence() {
        return intelligence;
    }

    /**
     * @param intelligence the intelligence to set
     */
    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    /**
     * @return the agility
     */
    public Integer getAgility() {
        return agility;
    }

    /**
     * @param agility the agility to set
     */
    public void setAgility(Integer agility) {
        this.agility = agility;
    }

    /**
     * @return the charm
     */
    public Integer getCharm() {
        return charm;
    }

    /**
     * @param charm the charm to set
     */
    public void setCharm(Integer charm) {
        this.charm = charm;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @return the avatar
     */
    public byte[] getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    /**
     * @return the faction
     */
    public String getFaction() {
        return faction;
    }

    /**
     * @param faction the faction to set
     */
    public void setFaction(String faction) {
        this.faction = faction;
    }

    /**
     * @return the zoneId
     */
    public Integer getZoneId() {
        return zoneId;
    }

    /**
     * @param zoneId the zoneId to set
     */
    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }
}
