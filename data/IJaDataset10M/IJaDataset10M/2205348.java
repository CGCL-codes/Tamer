package org.mortbay.jetty.plugin.util;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * ScanTargetPattern
 *
 * Utility class to provide the ability for the mvn jetty:run 
 * mojo to be able to specify filesets of extra files to 
 * regularly scan for changes in order to redeploy the webapp.
 * 
 * For example:
 * 
 * &lt;scanTargetPattern&gt;
 *   &lt;directory&gt;/some/place&lt;/directory&gt;
 *   &lt;includes&gt;
 *     &lt;include&gt;some ant pattern here &lt;/include&gt;
 *     &lt;include&gt;some ant pattern here &lt;/include&gt; 
 *   &lt;/includes&gt;
 *   &lt;excludes&gt;
 *     &lt;exclude&gt;some ant pattern here &lt;/exclude&gt;
 *     &lt;exclude&gt;some ant pattern here &lt;/exclude&gt;
 *   &lt;/excludes&gt;
 * &lt;/scanTargetPattern&gt;
 */
public class ScanTargetPattern {

    private File _directory;

    private List _includes = Collections.EMPTY_LIST;

    private List _excludes = Collections.EMPTY_LIST;

    /**
     * @return the _directory
     */
    public File getDirectory() {
        return _directory;
    }

    /**
     * @param _directory the _directory to set
     */
    public void setDirectory(File directory) {
        this._directory = directory;
    }

    public void setIncludes(List includes) {
        _includes = includes;
    }

    public void setExcludes(List excludes) {
        _excludes = excludes;
    }

    public List getIncludes() {
        return _includes;
    }

    public List getExcludes() {
        return _excludes;
    }
}
