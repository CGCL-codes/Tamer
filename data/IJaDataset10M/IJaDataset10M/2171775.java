package net.diet_rich.jabak.core.backup;

import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import net.diet_rich.util.ChangeListener;
import net.diet_rich.util.ref.LongCounter;

/**
 * set of backup items combined with a counter and sorted by the natural
 * ordering of the items.
 * 
 * @author Georg Dietrich
 */
public class CountSet {

    /** the item counter */
    private LongCounter count = new LongCounter();

    /** the items */
    private NavigableSet<BackupItem> items = new TreeSet<BackupItem>();

    /** add an item count listener */
    public void addCountListener(ChangeListener<Long> listener) {
        count.addChangeListener(listener);
    }

    /** add an item to the set */
    public void add(BackupItem item) {
        items.add(item);
    }

    /** retrieve and remove the first item from the set */
    public BackupItem get() {
        return items.pollFirst();
    }

    /** return the set as umodifiable set */
    public Set<BackupItem> set() {
        return Collections.unmodifiableSet(items);
    }
}
