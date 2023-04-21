package net.sourceforge.univecs.geom.hex;

import net.sourceforge.univecs.util.CloneNotSupportedError;

/**
 * Coordinates in the discrete hexagonal map space. Each discrete position in
 * the grid is uniquely identified by a pair of integer coordinates. The
 * {@link Numbering} scheme determines how the <code>x</code> and <code>y</code>
 * coordinates map to a hex.
 * 
 * @author Jason Steele
 * @author Copyright (c) 2008, UniVeCS Dev Team. All rights reserved.
 */
public class HexCoordinates implements Cloneable {

    /** Numbering system for this coordinate pair. */
    private Numbering numbering;

    /** Coordinate system this coordinate pair belongs to. */
    private HexSystem system;

    /** X coordinate of pair. */
    private int x;

    /** Y coordinate of pair. */
    private int y;

    /**
    * Creates the hex coordinates. This is not normally invoked by the user; use
    * {@link HexSystem#getCoord(int, int)} or
    * {@link HexSystem#getCoord(int, int, Numbering)} instead.
    * 
    * @param system coordinate system this coordinate pair belongs to
    * @param x x coordinate of hex according to <code>numbering</code>
    * @param y y coordinate of hex according to <code>numbering</code>
    * @param numbering numbering system for the coordinate pair
    */
    protected HexCoordinates(final HexSystem system, final int x, final int y, final Numbering numbering) {
        if (null == system) {
            throw new IllegalArgumentException("null system");
        }
        if (null == numbering) {
            throw new IllegalArgumentException("null numbering");
        }
        this.system = system;
        this.x = x;
        this.y = y;
        this.numbering = numbering;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public Object clone() {
        try {
            final HexCoordinates clone = (HexCoordinates) super.clone();
            clone.numbering = numbering;
            clone.system = system;
            clone.x = x;
            clone.y = y;
            return clone;
        } catch (CloneNotSupportedException cnse) {
            throw new CloneNotSupportedError(super.getClass() + " threw exception in clone(): " + cnse, cnse);
        }
    }

    /**
    * Converts the coordinate pair to another numbering system.
    * 
    * @param numbering new numbering system
    */
    public void convert(final Numbering numbering) {
        if (this.numbering != numbering) {
            convertToAskewRising();
            convertFromAkewRising(numbering);
        }
    }

    /**
    * Converts the coordinate pair from {@link Numbering#ASKEW_RISING}.
    * 
    * @param numbering numbering system to convert to
    */
    protected void convertFromAkewRising(final Numbering numbering) {
        if (Orientation.HORIZONTAL == system.getOrientation()) {
            if (Numbering.ASKEW_FALLING == numbering) {
                x = x + y;
            } else if (Numbering.PERPENDICULAR_DOWN == numbering) {
                x = x + (y >= 0 ? y / 2 : (y - 1) / 2);
            } else if (Numbering.PERPENDICULAR_UP == numbering) {
                x = x + (y >= 0 ? (y + 1) / 2 : y / 2);
            }
        } else {
            if (Numbering.ASKEW_FALLING == numbering) {
                y = y + x;
            } else if (Numbering.PERPENDICULAR_DOWN == numbering) {
                y = y + (x >= 0 ? x / 2 : (x - 1) / 2);
            } else if (Numbering.PERPENDICULAR_UP == numbering) {
                y = y + (x >= 0 ? (x + 1) / 2 : x / 2);
            }
        }
        this.numbering = numbering;
    }

    /**
    * Converts the coordinate pair to {@link Numbering#ASKEW_RISING} numbering.
    */
    protected void convertToAskewRising() {
        if (Orientation.HORIZONTAL == system.getOrientation()) {
            if (Numbering.ASKEW_FALLING == this.numbering) {
                x = x - y;
            } else if (Numbering.PERPENDICULAR_DOWN == this.numbering) {
                x = x - (y >= 0 ? y / 2 : (y - 1) / 2);
            } else if (Numbering.PERPENDICULAR_UP == this.numbering) {
                x = x - (y >= 0 ? (y + 1) / 2 : y / 2);
            }
        } else {
            if (Numbering.ASKEW_FALLING == this.numbering) {
                y = y - x;
            } else if (Numbering.PERPENDICULAR_DOWN == this.numbering) {
                y = y - (x >= 0 ? x / 2 : (x - 1) / 2);
            } else if (Numbering.PERPENDICULAR_UP == this.numbering) {
                y = y - (x >= 0 ? (x + 1) / 2 : x / 2);
            }
        }
        numbering = Numbering.ASKEW_RISING;
    }

    /**
    * Gets the distance to another hex.
    * 
    * @param coord coordinates of the other hex
    * @return least number of hexes required to traverse from this coordinate
    */
    public int distance(final HexCoordinates coord) {
        final Numbering myOrigNumbering = numbering;
        final Numbering otherOrigNumbering = coord.getNumbering();
        if (myOrigNumbering != Numbering.ASKEW_RISING) {
            convert(Numbering.ASKEW_RISING);
        }
        coord.convert(numbering);
        final int dX = getX() - coord.getX();
        final int dY = getY() - coord.getY();
        int distance = (Math.abs(dX) + Math.abs(dY) + Math.abs(dY + dX)) / 2;
        if (myOrigNumbering != Numbering.ASKEW_RISING) {
            convert(myOrigNumbering);
        }
        coord.convert(otherOrigNumbering);
        return distance;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HexCoordinates)) {
            return false;
        }
        HexCoordinates other = (HexCoordinates) obj;
        if (system != other.system) {
            return false;
        }
        if (numbering != other.numbering) {
            other = (HexCoordinates) other.clone();
            other.convert(numbering);
        }
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    /**
    * Gets the numbering system for this coordinate pair.
    * 
    * @return numbering system
    */
    public Numbering getNumbering() {
        return numbering;
    }

    /**
    * Gets the coordinate system this coordinate pair belongs to.
    * 
    * @return the system
    */
    public HexSystem getSystem() {
        return system;
    }

    /**
    * Gets the X coordinate offset of an adjacent hex in the
    * {@link Numbering#ASKEW_RISING} numbering system.
    * 
    * @param dir direction to the adjacent hex
    * @return x coordinate increment, -1, 0 or 1
    */
    protected int getUnitX(final HexDirection dir) {
        switch(dir.getAngle()) {
            case 0:
                return Orientation.HORIZONTAL == system.getOrientation() ? 1 : 0;
            case 1:
                return Orientation.HORIZONTAL == system.getOrientation() ? 1 : 1;
            case 2:
                return Orientation.HORIZONTAL == system.getOrientation() ? 0 : 1;
            case 3:
                return Orientation.HORIZONTAL == system.getOrientation() ? -1 : 0;
            case 4:
                return Orientation.HORIZONTAL == system.getOrientation() ? -1 : -1;
            case 5:
                return Orientation.HORIZONTAL == system.getOrientation() ? 0 : -1;
            default:
                throw new IllegalStateException("invalid direction angle " + dir.getAngle());
        }
    }

    /**
    * Gets the Y coordinate offset of an adjacent hex in the
    * {@link Numbering#ASKEW_RISING} numbering system.
    * 
    * @param dir direction to the adjacent hex
    * @return y coordinate increment, -1, 0 or 1
    */
    protected int getUnitY(final HexDirection dir) {
        switch(dir.getAngle()) {
            case 0:
                return Orientation.HORIZONTAL == system.getOrientation() ? 0 : 1;
            case 1:
                return Orientation.HORIZONTAL == system.getOrientation() ? -1 : 0;
            case 2:
                return Orientation.HORIZONTAL == system.getOrientation() ? -1 : -1;
            case 3:
                return Orientation.HORIZONTAL == system.getOrientation() ? 0 : -1;
            case 4:
                return Orientation.HORIZONTAL == system.getOrientation() ? 1 : 0;
            case 5:
                return Orientation.HORIZONTAL == system.getOrientation() ? 1 : 1;
            default:
                throw new IllegalStateException("invalid direction angle " + dir.getAngle());
        }
    }

    /**
    * Gets the X coordinate for this hex.
    * 
    * @return x coordinate
    */
    public int getX() {
        return x;
    }

    /**
    * Gets the Y coordinate for this hex.
    * 
    * @return y coordinate
    */
    public int getY() {
        return y;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((numbering == null) ? 0 : numbering.hashCode());
        result = prime * result + ((system == null) ? 0 : system.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
    * Translate the coordinate pair.
    * 
    * @param dir direction to translate
    * @param distance distance to translate, in hexes
    */
    public void move(final HexDirection dir, final int distance) {
        final Numbering originalNumbing = numbering;
        convert(Numbering.ASKEW_RISING);
        x += getUnitX(dir) * distance;
        y += getUnitY(dir) * distance;
        convert(originalNumbing);
    }

    /**
    * Sets the X coordinate for this hex.
    * 
    * @param x new X coordinate
    */
    public void setX(final int x) {
        this.x = x;
    }

    /**
    * Sets the Y coordinate for this hex.
    * 
    * @param y new Y coordinate
    */
    public void setY(final int y) {
        this.y = y;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(x);
        sb.append(",");
        sb.append(y);
        sb.append(":");
        sb.append(numbering);
        sb.append(")");
        return sb.toString();
    }
}
