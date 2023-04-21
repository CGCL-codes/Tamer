package org.tamacat.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.tamacat.dao.Condition;
import org.tamacat.sql.SQLParser;

public class ConditionTest {

    @Test
    public void testGetReplaceHolder() {
        assertEquals(SQLParser.VALUE1, Condition.EQUAL.getReplaceHolder());
        assertEquals("#{value1}", Condition.EQUAL.getReplaceHolder());
    }

    @Test
    public void testGetCondition() {
        assertEquals(" like ", Condition.LIKE_HEAD.getCondition());
        assertEquals("<>", Condition.NOT_EQUAL.getCondition());
        assertEquals("=", Condition.EQUAL.getCondition());
    }
}
