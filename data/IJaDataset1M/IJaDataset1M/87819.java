package net.sourceforge.blogentis.rss;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * A factory for RSSVersion objects.
 * 
 * @author abas
 */
public class RSSVersionFactory {

    public static final String RDF_VERSION_091 = "0.91";

    public static final String RDF_VERSION_092 = "0.92";

    public static final String RDF_VERSION_10 = "1.0";

    public static final String RDF_VERSION_20 = "2.0";

    static Hashtable rssVersions = null;

    static {
        RSSVersion rss = new RSSVersion("RSS 2.0", "RSS20.vm", "rss");
        RSSVersion rdf = new RSSVersion("RDF 1.0", "RSS10.vm", "rdf");
        RSSVersion atom = new RSSVersion("ATOM 0.3", "Atom.vm", "atom");
        rssVersions = new Hashtable();
        rssVersions.put(rss.getVersion(), rss);
        rssVersions.put(rdf.getVersion(), rdf);
        rssVersions.put(atom.getVersion(), atom);
    }

    private RSSVersionFactory() {
    }

    /**
     * Retrieve the appropriate object for the specified version.
     * 
     * @param version
     *            the version for which you want the corresponding RSSVersion.
     * @return the RSSVersion corresponding to the version specified, or null if
     *         such a version does not exist.
     */
    public static RSSVersion getVersionFor(String version) {
        return (RSSVersion) rssVersions.get(version);
    }

    /**
     * List all the various RSS versions available
     * 
     * @return an iterator that returns RSSVersion .
     */
    public static Iterator getIterator() {
        return rssVersions.values().iterator();
    }
}
