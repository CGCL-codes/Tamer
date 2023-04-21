package org.torweg.pulse.configuration;

import org.jdom.Element;
import org.torweg.pulse.util.time.TimeSpan;

/**
 * configuration for the {@code PoorMansCache}, configuring the VFS cache.
 * 
 * @author Thomas Weber
 * @version $Revision: 1387 $
 */
public final class PoorMansCacheConfiguration extends AbstractConfigBean {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = -8548196993088036732L;

    /**
	 * is the VFS cache enabled.
	 */
    private boolean virtualFileCacheEnabled;

    /**
	 * the maximum file size, for virtual files to get cached.
	 */
    private long maxFileSize;

    /**
	 * the maximum size of the VFS cache.
	 */
    private long maxCacheSize;

    /**
	 * the maximum time of files to be inactive in the cache.
	 */
    private TimeSpan maxInactive;

    /**
	 * initialises the config bean from JDOM.
	 * 
	 * @param conf
	 *            the configuration JDOM
	 * @see org.torweg.pulse.configuration.ConfigBean#init(org.jdom.Element)
	 */
    public void init(final Element conf) {
        this.virtualFileCacheEnabled = Boolean.parseBoolean(conf.getAttributeValue("vfs-cache-enabled"));
        this.maxFileSize = Long.parseLong(conf.getChild("max-file-size").getAttributeValue("kBytes")) * 1024L;
        this.maxCacheSize = Long.parseLong(conf.getChild("max-cache-size").getAttributeValue("kBytes")) * 1024L;
        this.maxInactive = new TimeSpan(conf.getChild("max-inactive").getAttributeValue("timespan"));
    }

    /**
	 * returns whether the VFS cache is enabled.
	 * 
	 * @return {@code true}, if and only if the VFS cache is enabled.
	 *         Otherwise {@code false}.
	 */
    public boolean isVirtualFileCacheEnabled() {
        return this.virtualFileCacheEnabled;
    }

    /**
	 * returns the maximum file size, for virtual files to get cached.
	 * 
	 * @return the maximum file size
	 */
    public long getMaxFileSize() {
        return this.maxFileSize;
    }

    /**
	 * returns the maximum size of the VFS cache.
	 * 
	 * @return the maximum size of the VFS cache
	 */
    public long getMaxCacheSize() {
        return this.maxCacheSize;
    }

    /**
	 * returns the maximum time of files to be inactive in the cache.
	 * 
	 * @return the maximum time of files to be inactive
	 */
    public TimeSpan getMaxInactive() {
        return new TimeSpan(this.maxInactive);
    }
}
