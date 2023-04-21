package cockfight.gameworld;

import cockfight.net.InputEvent;

/**
 * The class that describes an object that is controllable by a human player.
 * 
 * @author G4 Andrey Khanov, Sean Burau
 */
public abstract class Player extends CollisionObject implements TeamMember, Spawnable, Damageable {

    private static final long serialVersionUID = 1L;

    private static final float ACCEL = 1500.0f;

    private boolean waitingForSpawn, doesExist, active = false, hasOwner = false;

    /**
	 * The number of points this player gives if it gets killed
	 */
    protected int pointValue;

    /**
	 * The number of points this player has gained in the game
	 */
    protected int pointsGained;

    /**
	 * The current state of the Player's health
	 */
    protected Health health;

    /**
	 * The InputState object describing the input state of this Player
	 */
    protected InputState inputState;

    /**
	 * Creates the Player object
	 * @param width the width of the Player
	 * @param height the height of the Player
	 */
    protected Player(int width, int height) {
        super(width, height);
        inputState = new InputState();
        setActive(false);
    }

    /**
	 * Returns the point value for killing this player
	 * @return the point value for killing this player
	 */
    public int getPointValue() {
        return pointValue;
    }

    /**
	 * Sets the point value for killing this player
	 * @param pointValue the point value for killing this player
	 */
    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    /**
	 * Returns the number of points the player has gained
	 * @return the number of points the player has gained
	 */
    public int getPointsGained() {
        return pointsGained;
    }

    /**
	 * Sets the number of points the player has gained
	 * @param pointsGained the number of points the player has gained
	 */
    public void setPointsGained(int pointsGained) {
        this.pointsGained = pointsGained;
    }

    /**
	 * Returns an object containing the health information for the player.
	 * @return an object containing the health information for the player.
	 */
    public Health getHealth() {
        return this.health;
    }

    /**
	 * Acquires input from an InputEvent object
	 * @param e the InputEvent object from which input will be acquired
	 */
    public void updateInputState(InputEvent e) {
        inputState.update(e);
    }

    /**
	 * Updates the player status
	 * @param ms time in milliseconds
	 * @param game Game object that this player is part of
	 */
    public void update(long ms, Game game) {
        float t = ms / 1000.0f;
        if (!isDead()) {
            if (inputState.isLeftPressed()) {
                vel.x -= ACCEL * t;
            }
            if (inputState.isRightPressed()) {
                vel.x += ACCEL * t;
            }
            if (inputState.isJumpPressed()) {
                if (onGround) {
                    vel.y -= GRAVITY * 40.0f * .01f;
                    onGround = false;
                    inputState.setJumpPressed(false);
                }
            }
        }
        super.update(ms, game.getMap());
    }

    /**
	 * Returns true if the player is active(connected), otherwise false.
	 * @return true if the player is active(connected), otherwise false.
	 */
    public boolean isActive() {
        return active;
    }

    /**
	 * Sets the player's active(connected) state.
	 * @param active true if the player is active(connected), otherwise false;
	 */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
	 * Returns true if the player exists, otherwise false.
	 * @return true if the player exists, otherwise false.
	 */
    public boolean doesExist() {
        return doesExist;
    }

    /**
	 * Sets the player's existence state.
	 * @param doesExist true if the player should exist, otherwise false;
	 */
    public void setExists(boolean doesExist) {
        this.doesExist = doesExist;
    }

    /**
	 * Returns true if the player is waiting to be respawned.
	 * @return  true if the player is waiting to be respawned.
	 */
    public boolean isWaitingForSpawn() {
        return waitingForSpawn;
    }

    /**
	 * Sets whether or not the player should be waiting to be respawned.
	 * @param isWaiting true if the player should be waiting to be respawned.
	 */
    public void setWaitingForSpawn(boolean isWaiting) {
        this.waitingForSpawn = isWaiting;
    }

    /**
	 * Sets whether or not there is a player controlling this character.
	 * @param hasOwner true if a player is controlling this character.
	 */
    public void setHasOwner(boolean hasOwner) {
        this.hasOwner = hasOwner;
    }

    /**
	 * Gets whether or not there is a player controlling this character.
	 * return Whether or not a player controls this character
	 */
    public boolean getHasOwner() {
        return hasOwner;
    }

    /**
	 * Spawns the player at the specified location.
	 * @param position the position the player should spawn at.
	 */
    public void spawn(Vector2 position) {
        pos.x = position.x;
        pos.y = position.y;
        this.doesExist = true;
        health.setCurrentHitPoints(health.getMaxHitPoints());
    }
}
