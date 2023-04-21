package org.game.thyvin.logic.room;

public class Unit {

    private ThyvinSampleNode node;

    private final UnitClass unitClass;

    private int currentHealth;

    private int currentMoves;

    private int playerId;

    private int team;

    public Unit(UnitClass unitClass) {
        this.unitClass = unitClass;
        currentHealth = unitClass.getMaxHealth();
        currentMoves = unitClass.getMaxMoves();
    }

    public ThyvinSampleNode getNode() {
        return node;
    }

    public void setNode(ThyvinSampleNode myNode) {
        this.node = myNode;
    }

    public int getMaxHealth() {
        return unitClass.getMaxHealth();
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
        if (currentHealth < 0) {
            this.currentHealth = 0;
        }
        if (currentHealth > unitClass.getMaxHealth()) {
            this.currentHealth = unitClass.getMaxHealth();
        }
    }

    public int getCurrentMoves() {
        return currentMoves;
    }

    public void setCurrentMoves(int currentMoves) {
        this.currentMoves = currentMoves;
    }

    public int getMaxMoves() {
        return unitClass.getMaxMoves();
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
        this.playerId = team;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.team = playerId;
        this.playerId = playerId;
    }

    public UnitClass getUnitClass() {
        return unitClass;
    }
}
