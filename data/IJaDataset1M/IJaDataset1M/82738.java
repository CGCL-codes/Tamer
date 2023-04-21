package com.liferay.portal.kernel.search;

/**
 * <a href="BooleanClauseOccur.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface BooleanClauseOccur {

    public static final BooleanClauseOccur MUST = new BooleanClauseOccurImpl("MUST");

    public static final BooleanClauseOccur MUST_NOT = new BooleanClauseOccurImpl("MUST_NOT");

    public static final BooleanClauseOccur SHOULD = new BooleanClauseOccurImpl("SHOULD");
}
