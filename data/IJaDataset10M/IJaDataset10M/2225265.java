package org.rivalry.core.comparator;

import java.util.Comparator;
import org.rivalry.core.model.Category;

/**
 * Provides a default implementation of a candidate comparator which compares by
 * name.
 */
public class DefaultCategoryComparator implements Comparator<Category> {

    @Override
    public int compare(final Category element0, final Category element1) {
        int answer;
        if (element0 == element1) {
            answer = 0;
        } else if (element0 == null) {
            answer = 1;
        } else if (element1 == null) {
            answer = -1;
        } else {
            final String name0 = element0.getName();
            final String name1 = element1.getName();
            answer = name0.compareTo(name1);
        }
        return answer;
    }
}
