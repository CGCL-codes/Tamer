package com.volantis.cache.group;

import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.notification.RemovalListener;
import com.volantis.cache.stats.StatisticsSnapshot;

/**
 * Contains a number of entries and possible other groups.
 *
 * <p>Groups are used to structure a cache internally. They are organized into
 * a hierarchy and can container entries and/or other groups.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface Group {

    /**
     * Add a listener that will be notified when entries are removed from the
     * cache.
     *
     * @param listener The listener to add.
     */
    void addRemovalListener(RemovalListener listener);

    /**
     * Add a nested group.
     *
     * @param groupName The name of the group, must be unique within this group.
     * @param builder   The builder of the group.
     * @return The group that was built.
     * @throws IllegalStateException if the group already exists.
     */
    Group addGroup(Object groupName, GroupBuilder builder);

    /**
     * Get the nested group with the specified name.
     *
     * @param groupName The name of the group.
     * @return The group with that name.
     * @throws IllegalStateException if the group does not exist.
     */
    Group getGroup(Object groupName);

    /**
     * Get the nested group with the specified name.
     *
     * @param groupName The name of the group.
     * @return The group with that name, or null if it could not be found.
     */
    Group findGroup(Object groupName);

    /**
     * Flush the entries of this group and all nested groups that are matched
     * by the filter.
     *
     * @param filter The filter to select entries, if this is null then it is
     *               assumed that all entries will be removed.
     */
    void flush(CacheEntryFilter filter);

    /**
     * Get a snapshot of the statistics for this group and all nested groups.
     *
     * @return A snapshot of the statistics for this group and all nested
     *         groups.
     */
    StatisticsSnapshot getStatisticsSnapshot();
}
