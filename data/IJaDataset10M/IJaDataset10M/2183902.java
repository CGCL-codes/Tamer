package org.makagiga.tags;

import java.util.TreeSet;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.TK;
import org.makagiga.fs.MetaInfo;
import org.makagiga.search.Query;
import org.makagiga.search.SortMethod;

public final class Tags extends TreeSet<String> {

    private static boolean needUpdate = true;

    private static Tags all = new Tags();

    public Tags() {
        super(TagsUtils.COMPARATOR);
    }

    /**
	 * @since 3.0
	 */
    public static void addAll(final String tags) {
        if (!TK.isEmpty(tags)) {
            Tags fixed = TagsUtils.removeDuplicates(tags);
            if (!fixed.isEmpty()) {
                synchronized (Tags.class) {
                    all.addAll(fixed);
                }
            }
        }
    }

    /**
	 * @since 2.4
	 */
    public static void clearAll() {
        MLogger.debug("tags", "Clearing tag cache...");
        synchronized (Tags.class) {
            all.clear();
            needUpdate = true;
        }
    }

    /**
	 * @since 2.4
	 */
    public static synchronized Tags getAll() {
        if (all.isEmpty() && needUpdate) {
            MLogger.debug("tags", "Recreating tag cache...");
            for (MetaInfo metaInfo : Query.all(SortMethod.UNSORTED)) {
                if (metaInfo.isAnyFile() || metaInfo.isAnyFolder()) all.addAll(TagsUtils.removeDuplicates(metaInfo.getTags()));
            }
            needUpdate = false;
        } else {
            MLogger.debug("tags", "Using cached tags... (size=%s)", all.size());
        }
        return all;
    }

    /**
	 * Returns a space-separated, case insensitive sorted list of tags.
	 * Returns an empty string if no tags.
	 */
    @Override
    public String toString() {
        return TK.toString(this, " ");
    }
}
