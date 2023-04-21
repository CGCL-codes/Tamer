package net.sf.antcontrib.cpptasks;

import java.util.Vector;

/**
 * @author Curt Arnold
 */
public final class DependencyInfo {

    /**
     * Last modified time of this file or anything that it depends on.
     * 
     * Not persisted since almost any change could invalidate it. Initialized
     * to long.MIN_VALUE on construction.
     */
    private long compositeLastModified;

    private String includePathIdentifier;

    private String[] includes;

    private String source;

    private long sourceLastModified;

    private String[] sysIncludes;

    public DependencyInfo(String includePathIdentifier, String source, long sourceLastModified, Vector includes, Vector sysIncludes) {
        if (source == null) {
            throw new NullPointerException("source");
        }
        if (includePathIdentifier == null) {
            throw new NullPointerException("includePathIdentifier");
        }
        this.source = source;
        this.sourceLastModified = sourceLastModified;
        this.includePathIdentifier = includePathIdentifier;
        this.includes = new String[includes.size()];
        if (includes.size() == 0) {
            compositeLastModified = sourceLastModified;
        } else {
            includes.copyInto(this.includes);
            compositeLastModified = Long.MIN_VALUE;
        }
        this.sysIncludes = new String[sysIncludes.size()];
        sysIncludes.copyInto(this.sysIncludes);
    }

    /**
     * Returns the latest modification date of the source or anything that it
     * depends on.
     * 
     * @return the composite lastModified time, returns Long.MIN_VALUE if not
     * set
     */
    public long getCompositeLastModified() {
        return compositeLastModified;
    }

    public String getIncludePathIdentifier() {
        return includePathIdentifier;
    }

    public String[] getIncludes() {
        String[] includesClone = (String[]) includes.clone();
        return includesClone;
    }

    public String getSource() {
        return source;
    }

    public long getSourceLastModified() {
        return sourceLastModified;
    }

    public String[] getSysIncludes() {
        String[] sysIncludesClone = (String[]) sysIncludes.clone();
        return sysIncludesClone;
    }

    public void setCompositeLastModified(long lastMod) {
        compositeLastModified = lastMod;
    }
}
