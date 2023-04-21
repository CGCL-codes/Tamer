package remato.domain.common;

import java.util.Set;
import remato.common.domain.helpers.Parentable;

public interface CategoryParentable extends Parentable {

    public Set<Category> getCategoryChildren();

    /**
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
     */
    public boolean addCategoryChild(Category child);

    /**
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
	 */
    public boolean removeCategoryChild(Category child);
}
