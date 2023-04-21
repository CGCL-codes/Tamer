package com.syrus.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A {@code Team} of developers that must develop that customer's project.
 *
 * @author Olivier Houyoux
 */
public class Team implements Serializable {

    private static final long serialVersionUID = -4160463861961601036L;

    private static final Logger LOGGER = Logger.getLogger(Team.class.getName());

    private float velocity;

    private Set<String> developers = new HashSet<String>();

    /**
     * Constructs a new {@code Team} with a default velocity of 0.7.
     */
    public Team() {
        this(0.7f);
        LOGGER.entering("Team", "Team");
        LOGGER.exiting("Team", "Team");
    }

    /**
     * Constructs a new {@code Team} with the given velocity.
     *
     * @param velocity the default velocity factor of this {@code Team}
     */
    public Team(float velocity) {
        LOGGER.entering("Team", "Team", velocity);
        setVelocity(velocity);
        LOGGER.exiting("Team", "Team");
    }

    /**
     * Changes the velocity of this development {@code Team}.
     * The velocity tells how fast the {@code Team} worked in the last
     * {@code Iteration}. It measures how the {@code Team} performs in reality
     * based on how it performed in the past. So it helps to plan the
     * {@code Iteration} realistically.
     *
     * @param velocity the new velocity factor
     * @see Iteration
     * @throws IllegalArgumentException if {@code velocity} is not a percentage
     */
    public void setVelocity(float velocity) {
        LOGGER.entering("Team", "setVelocity", velocity);
        if (velocity < 0 || velocity > 1) {
            throw new IllegalArgumentException("Velocity should be a " + "percentage");
        }
        this.velocity = velocity;
        LOGGER.exiting("Team", "setVelocity", this.velocity);
    }

    /**
     * Returns the velocity of this {@code Team}.
     *
     * @return the velocity factor
     */
    public float getVelocity() {
        LOGGER.entering("Team", "getVelocity");
        LOGGER.exiting("Team", "getVelocity", this.velocity);
        return this.velocity;
    }

    /**
     * Adds a new developer to this {@code Team}.
     *
     * @param developer the new developer's nickname
     * @throws IllegalArgumentException if {@code developer} is {@code null}
     */
    public void addDeveloper(String developer) {
        LOGGER.entering("Team", "addDeveloper", developer);
        if (developer == null) {
            throw new IllegalArgumentException("Developer should not be null");
        }
        developers.add(developer);
        LOGGER.exiting("Team", "addDeveloper");
    }

    /**
     * Returns the size of this {@code Team}.
     *
     * @return the number of developers that compose this {@code Team}
     */
    public int getSize() {
        LOGGER.entering("Team", "getSize");
        int size = developers.size();
        LOGGER.exiting("Team", "getSize", size);
        return size;
    }

    /**
     * Returns the members of this {@code Team}.
     *
     * @return the {@code Collection} of developers who belong to this
     *     {@code Team}
     */
    public Collection<String> getDevelopers() {
        LOGGER.entering("Team", "getDevelopers");
        Collection<String> devs = new ArrayList<String>(developers);
        devs = Collections.unmodifiableCollection(devs);
        LOGGER.exiting("Team", "getDevelopers", devs);
        return devs;
    }

    /**
     * Tests if some other object is "equal to" this one.
     *
     * @param obj the {@code Object} to test for equality
     * @return {@code true} if the specified {@code Object} equals this
     *     {@code Team}; {@code false} otherwise
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        LOGGER.entering("Team", "equals", obj);
        if (obj == this) {
            LOGGER.exiting("Team", "equals", true);
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            Team team = (Team) obj;
            boolean equals = velocity == team.velocity && developers.equals(team.developers);
            LOGGER.exiting("Team", "equals", equals);
            return equals;
        }
        LOGGER.exiting("Team", "equals", false);
        return false;
    }

    /**
     * Returns a hash code value for this {@code Team}.
     *
     * @return a hash code value for this {@code Team}
     * @see #equals(Object)
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        LOGGER.entering("Team", "hashCode");
        int hashCode = 11;
        hashCode = 31 * hashCode + Float.valueOf(velocity).hashCode();
        for (String developer : developers) {
            hashCode = 31 * hashCode + developer.hashCode();
        }
        LOGGER.exiting("Team", "hashCode", hashCode);
        return hashCode;
    }

    /**
     * Returns a string representation of this {@code Team}.
     *
     * @return a string representation of the {@code Team}
     * @see Object#toString()
     */
    @Override
    public String toString() {
        LOGGER.entering("Team", "toString");
        StringBuilder buffer = new StringBuilder("[Team: ");
        buffer.append(developers).append(']');
        LOGGER.exiting("Team", "toString", buffer.toString());
        return buffer.toString();
    }
}
