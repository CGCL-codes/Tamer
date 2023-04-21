package org.t2framework.samples.lucy.dbflute.cbean.cq;

import org.seasar.dbflute.cbean.ConditionQuery;
import org.seasar.dbflute.cbean.sqlclause.SqlClause;
import org.t2framework.samples.lucy.dbflute.cbean.cq.bs.BsPersonCQ;

/**
 * The condition-query of PERSON.
 * <p>
 * You can implement your original methods here. This class remains when
 * re-generating.
 * </p>
 * 
 * @author DBFlute(AutoGenerator)
 */
public class PersonCQ extends BsPersonCQ {

    /**
	 * Constructor.
	 * 
	 * @param childQuery
	 *            Child query as abstract class. (Nullable: If null, this is
	 *            base instance.)
	 * @param sqlClause
	 *            SQL clause instance. (NotNull)
	 * @param aliasName
	 *            My alias name. (NotNull)
	 * @param nestLevel
	 *            Nest level.
	 */
    public PersonCQ(ConditionQuery childQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(childQuery, sqlClause, aliasName, nestLevel);
    }
}
