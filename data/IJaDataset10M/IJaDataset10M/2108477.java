package org.tmapi.index;

/**
 * Interface representing static index meta data.
 * @since 1.0
 */
public interface IndexFlags {

    /**
	 * Returns the auto-update flag for the index. If
	 * the value of this flag is <code>true</code>, then
	 * the index is automatically kept synchronized with 
	 * the TopicMap as values are changed. If the value is
	 * <code>false</code>, then the {@link Index#reindex()}
	 * method must be called to resynchronize the Index with
	 * the TopicMap after values are changed.
	 * @return the auto-update flag value.
	 */
    public boolean isAutoUpdated();
}
