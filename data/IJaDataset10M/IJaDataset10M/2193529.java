package dsb.bar.tks.server.api.administration;

/**
 * Server software version information.
 */
public interface VersionService {

    /**
	 * Get the major version number of the server software.
	 * 
	 * @return An integer representing the major version.
	 */
    public int getMajor();

    /**
	 * Get the minor version number of the server software.
	 * 
	 * @return An integer representing the minor version.
	 */
    public int getMinor();

    /**
	 * Get the patch number of the version of the server software.
	 * 
	 * @return An integer representing the patch number.
	 */
    public int getPatch();

    /**
	 * Get the build version of the server software.
	 * 
	 * @return An integer representing the build version.
	 */
    public long getBuild();

    /**
	 * Get the version string of the server software.
	 * 
	 * @return A string representing the version of the server software.
	 */
    public String getVersion();
}
