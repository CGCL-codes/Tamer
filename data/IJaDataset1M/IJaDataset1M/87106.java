package com.crossdb.sql;

import java.util.List;

/**
 * 
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Apr 24, 2003
 * Time: 10:11:15 PM
 * @version 0.1
 */
public interface IWhereClause {

    /**
	 defaults to AND
	 */
    void addCondition(WhereCondition cond);

    void addCondition(String and_or, WhereCondition cond);

    /**
	 defaults to AND
	 */
    void addClause(IWhereClause clause);

    void addClause(String and_or, IWhereClause clause);

    /**
	 Not sure if this will work in all dbs, but for now, it should be fine.
	 <p>
	 This function could turn into debugging or sample if not.
	 */
    String toString();

    boolean hasConditions();

    List getConditions();

    List getSeparators();

    void addWhereIn(String col, String commaSeparatedList);

    void addWhereIn(String and_or, String col, String commaSeparatedList);

    /**
     * Will compare toCompare to the value in col where the left side is the same
     * @param col
     * @param toCompare
     */
    void addWhereLikeLeft(String col, String toCompare);

    void addWhereLikeLeft(String and_or, String col, String toCompare);

    /**
     * Will compare toCompare to the value in col where the right side is the same
     * @param col
     * @param toCompare
     */
    void addWhereLikeRight(String col, String toCompare);

    void addWhereLikeRight(String and_or, String col, String toCompare);

    /**
     * this will compare the string to anywhere in the string
     * @param col
     * @param toCompare
     */
    void addWhereLikeAny(String col, String toCompare);

    void addWhereLikeAny(String and_or, String col, String toCompare);
}
