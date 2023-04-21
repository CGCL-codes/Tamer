package jhomenet.commons.configuration;

import java.util.Date;
import jhomenet.commons.Library;

/**
 * A configuration interface.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public interface SystemConfiguration {

    /**
	 * Print the configuration.
	 */
    public void printConfiguration();

    /**
	 * 
	 * @return
	 */
    public String getMajorVersion();

    public String getMinorVersion();

    public String getMicroVersion();

    public String getFeatureSet();

    public String getBuildId();

    public Date getBuildDate();

    public String getVersionName();

    public String getFullVersion();

    public Library.LibraryVersion getVersion();
}
