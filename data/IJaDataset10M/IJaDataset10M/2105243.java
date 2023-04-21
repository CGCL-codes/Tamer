package com.google.code.norush;

/**
 * Represents a single (immutable) vehicle on the Rush Hour board.
 * 
 * Allows comparing two vehicles according to their position. Note that the
 * equals method also takes into account the type and name of the vehicle, so
 * theoretically compareTo might return 0 when equals return false.
 * 
 * @author DL, GT
 */
public class Vehicle implements Comparable<Vehicle> {

    /**
     * Constructor.
     * @param type        Vehicle's type.
     * @param poisition   Vehicle's position on the RushHour board (zero-based).
     * @param name        Vehicle's name.
     */
    public Vehicle(VehicleType type, int position, char name) {
        this.type = type;
        this.position = position;
        this.name = name;
    }

    /** @return the vehicle's type. */
    public VehicleType getType() {
        return type;
    }

    /** @return the vehicle's position on the RushHour board (zero-based). */
    public int getPosition() {
        return position;
    }

    /** @return the vehicle's name. */
    public char getName() {
        return name;
    }

    /** @see Comparable#compareTo */
    @Override
    public int compareTo(Vehicle other) {
        return (position - other.position);
    }

    /** @see Object#equals() */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Vehicle)) {
            return false;
        }
        Vehicle otherVehicle = (Vehicle) other;
        return ((type == otherVehicle.type) && (position == otherVehicle.position) && (name == otherVehicle.name));
    }

    /** @see Object#hashCode() */
    @Override
    public int hashCode() {
        return type.ordinal() * 10000 + position * 100 + name;
    }

    private final VehicleType type;

    private final int position;

    private final char name;
}
