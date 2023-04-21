package nitf;

/**
 * Enumeration class for the NITF version
 */
public final class Version {

    /**
     * Represents a NITF 2.0 type
     */
    public static final Version NITF_20 = new Version("NITF 2.0");

    /**
     * Represents a NITF 2.1 type
     */
    public static final Version NITF_21 = new Version("NITF 2.1");

    private String name;

    /**
     * Return a name that describes the type
     * 
     * @return a descriptive name
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    private Version(String name) {
        this.name = name;
    }
}
