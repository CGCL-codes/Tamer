package org.springmodules.cache;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <p>
 * Template for cache entries that adds support for time expiration.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 458 $ $Date: 2006-02-23 23:52:57 -0500 (Thu, 23 Feb 2006) $
 */
public abstract class AbstractCacheEntry implements Serializable {

    /**
   * Specifying this as the refresh period ensures an entry does not become
   * stale until it is either explicitly flushed.
   */
    public static final int INDEFINITE_EXPIRY = -1;

    /**
   * Default initialization value for <code>{@link #lastUpdate}</code>.
   */
    private static final byte NOT_YET = -1;

    /**
   * The object to store in the cache.
   */
    private Object content;

    /**
   * The time this entry was last updated.
   */
    private long lastUpdate;

    /**
   * Period of refresh (in seconds). Setting it to {@link #INDEFINITE_EXPIRY}
   * will result in the content never becoming stale unless it is explicitly
   * flushed. Setting it to 0 will always result in a refresh being required.
   */
    private int refreshPeriod;

    /**
   * Constructor.
   */
    public AbstractCacheEntry() {
        super();
        this.setLastUpdate(NOT_YET);
    }

    /**
   * Indicates whether some other object is "equal to" this one.
   * 
   * @param obj
   *          the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument;
   *         <code>false</code> otherwise.
   * 
   * @see Object#equals(java.lang.Object)
   */
    public boolean equals(Object obj) {
        boolean equals = true;
        if (null == obj || !(obj instanceof AbstractCacheEntry)) {
            equals = false;
        } else if (this != obj) {
            AbstractCacheEntry entry = (AbstractCacheEntry) obj;
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            equalsBuilder.append(this.getContent(), entry.getContent());
            equalsBuilder.append(this.getLastUpdate(), entry.getLastUpdate());
            equalsBuilder.append(this.getRefreshPeriod(), entry.getRefreshPeriod());
            equals = equalsBuilder.isEquals();
        }
        return equals;
    }

    /**
   * Getter for field <code>{@link #content}</code>.
   * 
   * @return the field <code>content</code>.
   */
    public final Object getContent() {
        return this.content;
    }

    /**
   * Getter for field <code>{@link #lastUpdate}</code>.
   * 
   * @return the field <code>lastUpdate</code>.
   */
    public final long getLastUpdate() {
        return this.lastUpdate;
    }

    /**
   * Getter for field <code>{@link #refreshPeriod}</code>.
   * 
   * @return the field <code>refreshPeriod</code>.
   */
    public final int getRefreshPeriod() {
        return this.refreshPeriod;
    }

    /**
   * Returns a hash code value for the object. This method is supported for the
   * benefit of hashtables such as those provided by
   * <code>java.util.Hashtable</code>.
   * 
   * @return a hash code value for this object.
   * 
   * @see Object#hashCode()
   */
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(5, 11);
        hashCodeBuilder.append(this.getContent());
        hashCodeBuilder.append(this.getLastUpdate());
        hashCodeBuilder.append(this.getRefreshPeriod());
        int hashCode = hashCodeBuilder.toHashCode();
        return hashCode;
    }

    /**
   * Checks if this entry needs to be refreshed.
   * 
   * @return <code>true</code> if this entry needs to be refreshed,
   *         <code>false</code> otherwise.
   */
    public final boolean needsRefresh() {
        boolean stale;
        if (this.lastUpdate == NOT_YET) {
            stale = true;
        } else if (this.refreshPeriod == INDEFINITE_EXPIRY) {
            stale = false;
        } else if (this.refreshPeriod == 0) {
            stale = true;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            long currentLiveTime = this.lastUpdate + (this.refreshPeriod * 1000L);
            if (this.refreshPeriod > 0 && currentTimeMillis >= currentLiveTime) {
                stale = true;
            } else {
                stale = false;
            }
        }
        return stale;
    }

    /**
   * Setter for the field <code>{@link #content}</code>.
   * 
   * @param content
   *          the new value to set
   */
    public final void setContent(Object content) {
        this.content = content;
        this.setLastUpdate(System.currentTimeMillis());
    }

    /**
   * Setter for the field <code>{@link #lastUpdate}</code>.
   * 
   * @param lastUpdate
   *          the new value to set
   */
    public final void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
   * Setter for the field <code>{@link #refreshPeriod}</code>.
   * 
   * @param refreshPeriod
   *          the new value to set
   */
    public final void setRefreshPeriod(int refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }
}
