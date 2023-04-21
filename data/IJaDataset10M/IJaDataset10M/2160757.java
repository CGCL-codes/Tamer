package org.jimcat.model.filter.logical;

import java.util.Set;
import org.jimcat.model.Image;
import org.jimcat.model.filter.Filter;

/**
 * 
 * Combines two filters where both must match (logical AND).
 * 
 * 
 * $Id: AndFilter.java 998 2007-08-29 20:36:25Z cleiter $
 * 
 * @author Christoph
 */
public class AndFilter extends BinaryFilter implements AssociativeCombinationFilter {

    /**
	 * 
	 * construct a new AndFilter with the two given filter
	 * 
	 * @param first
	 * @param second
	 */
    public AndFilter(Filter first, Filter second) {
        super(first, second);
    }

    /**
	 * test if given image is matching this subfilter
	 * 
	 * @see org.jimcat.model.filter.logical.BinaryFilter#matches(org.jimcat.model.Image)
	 */
    @Override
    public boolean matches(Image image) {
        boolean result = true;
        if (first != null) {
            result = result && first.matches(image);
        }
        if (second != null) {
            result = result && second.matches(image);
        }
        return result;
    }

    /**
	 * build a efficient version fo this filter
	 * 
	 * @see org.jimcat.model.filter.logical.BinaryFilter#build(org.jimcat.model.filter.Filter,
	 *      org.jimcat.model.filter.Filter)
	 */
    @Override
    protected Filter build(Filter left, Filter right) {
        return create(left, right);
    }

    /**
	 * Join given filter with an and operation
	 * 
	 * @param first
	 * @param second
	 * @return a new Filter which is a AND version of the first and the second
	 */
    public static Filter create(Filter first, Filter second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        return new AndFilter(second, first);
    }

    /**
	 * calculate possible members
	 * 
	 * @see org.jimcat.model.filter.Filter#possibleMembers()
	 */
    @Override
    public Set<Image> possibleMembers() {
        Set<Image> a = null;
        if (first != null) {
            a = first.possibleMembers();
        }
        Set<Image> b = null;
        if (second != null) {
            b = second.possibleMembers();
        }
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        a.retainAll(b);
        return a;
    }
}
