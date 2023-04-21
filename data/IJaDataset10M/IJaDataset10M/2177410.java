package org.rivalry.core.comparator;

import java.util.Comparator;
import org.rivalry.core.model.Category;
import org.rivalry.core.model.Criterion;

/**
 * Provides a default implementation of a candidate comparator which compares by
 * name.
 */
public class CriterionByCategoryComparator implements Comparator<Criterion> {

    /** Category comparator. */
    private final Comparator<Category> _categoryComparator;

    /**
     * Construct this object with the given parameter.
     * 
     * @param categoryComparator Category comparator.
     */
    public CriterionByCategoryComparator(final Comparator<Category> categoryComparator) {
        _categoryComparator = categoryComparator;
    }

    @Override
    public int compare(final Criterion element0, final Criterion element1) {
        int answer;
        if (element0 == element1) {
            answer = 0;
        } else if (element0 == null) {
            answer = 1;
        } else if (element1 == null) {
            answer = -1;
        } else {
            final Category category0 = element0.getCategory();
            final Category category1 = element1.getCategory();
            answer = _categoryComparator.compare(category0, category1);
            if (answer == 0) {
                final String name0 = element0.getName();
                final String name1 = element1.getName();
                answer = name0.compareTo(name1);
            }
        }
        return answer;
    }
}
