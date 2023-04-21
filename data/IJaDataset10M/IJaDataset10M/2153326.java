package com.electionpredictor.seats;

/**
 * The region a seat is in
 * 
 * @author Niels Stchedroff
 */
public class Region implements Comparable<Region> {

    /**
	 * The region name
	 */
    private final String mName;

    /**
	 * The region constructor
	 * 
	 * @param name
	 *            The name of the region
	 */
    public Region(final String name) {
        if ((name == null) || name.equalsIgnoreCase("")) {
            throw new IllegalArgumentException("The region name must not be null or empty");
        }
        mName = name;
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    @Override
    public int compareTo(final Region o) {
        final Region that = o;
        return getName().compareTo(that.getName());
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(final Object pObj) {
        if ((pObj != null) && (pObj instanceof Region)) {
            final Region that = (Region) pObj;
            return getName().equalsIgnoreCase(that.getName());
        }
        throw new IllegalArgumentException(pObj + " is either null or not a Region object");
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return getName();
    }

    /**
	 * @return the name
	 */
    private String getName() {
        return mName;
    }
}
