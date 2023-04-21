package spread;

/**
 * A SpreadVersion object is used to get the version of spread that is being used.
 */
public class SpreadVersion {

    private int majorVersion = 3;

    private int minorVersion = 16;

    private int patchVersion = 1;

    /**
	 * Returns the spread version as a float.  The float is of
	 * the form A.B where A is the major version and B is the minor version.
	 * The patch version is not returned.
	 * 
	 * @return  the spread version
	*/
    public float getVersion() {
        return (float) ((float) majorVersion + ((float) minorVersion / 100.0));
    }

    /**
	 * Returns the spread major version as an int.
	 * 
	 * @return  the spread major version
	 */
    public int getMajorVersion() {
        return majorVersion;
    }

    /** Returns the spread minor version as an int.
	 * 
	 * @return  the spread minor version
	 */
    public int getMinorVersion() {
        return minorVersion;
    }

    /** Returns the spread patch version as an int.
	 * 
	 * @return  the spread patch version
	 */
    public int getPatchVersion() {
        return patchVersion;
    }

    /**
	 * Returns the spread version in string form.  The string is of
	 * the form A.BB.CC where A is the major version and BB is the minor version
	 * and CC is the patch version.
	 */
    public String toString() {
        return new String(majorVersion + "." + (minorVersion / 10) + "" + (minorVersion % 10) + "." + (patchVersion / 10) + (patchVersion % 10));
    }
}
