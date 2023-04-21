package org.magiclabs.sql;

import java.sql.ResultSet;
import org.magiclabs.fluentsql.FluentDelete;
import org.magiclabs.fluentsql.FluentQuery;
import org.magiclabs.fluentsql.FluentSelect;
import org.magiclabs.fluentsql.SQLClauses;
import org.magiclabs.fluentsql.SQLConditions;
import org.magiclabs.fluentsql.SQLElement;
import org.magiclabs.fluentsql.SQLField;
import org.magiclabs.fluentsql.SQLFunctions;
import org.magiclabs.fluentsql.SQLTable;
import org.magiclabs.fluentsql.SQLConditions.Between;
import org.magiclabs.fluentsql.SQLConditions.Eq;
import org.magiclabs.fluentsql.SQLConditions.In;
import junit.framework.TestCase;

public class TestQuery extends TestCase {

    private static class QuerySuperclass extends FluentSelect<Object> {

        private final SQLField f1 = new SQLField("F1");

        private final SQLField f2 = new SQLField("F2");

        private QuerySuperclass(String table) {
            FROM(new SQLTable(table));
            addFields();
        }

        protected SQLElement.SQLCondition createCondition() {
            return null;
        }

        protected void createClauses() {
        }

        public Object process(ResultSet row) {
            return null;
        }
    }

    private static class QueryInherited extends QuerySuperclass {

        private QueryInherited() {
            super("TESTTBL");
        }
    }

    FluentQuery query;

    String tableName;

    SQLField fieldWithAlias;

    SQLField fieldNormal;

    SQLConditions.Eq conditionEq;

    SQLConditions.Eq conditionEqWithField;

    SQLConditions.GtOrEq conditionGtOrEq;

    SQLConditions.Gt conditionGtWithIntValue;

    SQLConditions.In conditionIn;

    SQLConditions.And conditionAnd;

    SQLConditions.ComplexCondition complexCondition;

    SQLTable tableWithJoins;

    QueryInherited ooInherited;

    FluentQuery queryWithoutCondition;

    protected void setUp() throws Exception {
        query = new FluentQuery().FROM(new SQLTable("TESTTBL"));
        tableName = convertToDbName("TESTTBL");
        fieldWithAlias = new SQLField("fname2", "alias");
        fieldNormal = new SQLField("fname1");
        conditionEq = new SQLConditions.Eq("fname1", "value1");
        conditionEqWithField = new SQLConditions.Eq(fieldWithAlias, "value2");
        conditionGtOrEq = new SQLConditions.GtOrEq("fname1", "value1");
        conditionGtWithIntValue = new SQLConditions.Gt("fname3", new Integer(10));
        conditionAnd = new SQLConditions.And();
        conditionIn = new SQLConditions.In(new SQLField("fname1", "sussi"), new String[] { "value1", "value2" });
        tableWithJoins = new SQLTable("TESTTBL").addJoin(new SQLTable.Join(new SQLTable("OTHERTABLE")).LEFT().OUTER().ON(new SQLConditions.Eq(new SQLField("A"), new SQLField("B")))).addJoin(new SQLTable.Join(new SQLTable("EVENANOTHERTABLE")).LEFT().OUTER().ON(new SQLConditions.Eq(new SQLField("C"), "value of c")));
        complexCondition = new SQLConditions.Or().add(new SQLConditions.And().add(new SQLConditions.Eq(new SQLField("A"), "B")).add(new SQLConditions.Eq(new SQLField("C"), "D"))).add(new SQLConditions.And().add(new SQLConditions.Eq(new SQLField("E"), "F")).add(new SQLConditions.Eq(new SQLField("G"), "H")));
        ooInherited = new QueryInherited();
        queryWithoutCondition = new FluentQuery().FROM(new SQLTable("TESTTBL"));
        queryWithoutCondition.WHERE(null);
    }

    public static String convertToDbName(String table) {
        return table;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBeautifyAndClausePriority() {
        FluentQuery q = new FluentQuery().FROM(new SQLTable("TESTTBL"));
        q.addField("f1");
        q.addField("f2");
        q.WHERE(new SQLConditions.And().add(new SQLConditions.Eq("fieldTest1", "value1")).add(new SQLConditions.Gt("fieldTest2", new Integer(10))));
        q.addClause(new SQLClauses.Limit(123));
        q.addClause(new SQLClauses.OrderBy(this.fieldWithAlias, SQLClauses.OrderBy.ASCENDING));
        q.addClause(new SQLClauses.GroupBy(new SQLField("anotherField", "grouped")));
        assertEquals("SELECT\n  f1, f2\nFROM " + this.tableName + "\nWHERE fieldTest1 = 'value1' AND\n  fieldTest2 > 10\n" + "ORDER BY alias ASC\n" + "GROUP BY grouped\n" + "FETCH FIRST 123 ROWS ONLY", q.toSQL());
    }

    public void testFetchOnly() {
        this.query.addClause(new SQLClauses.ForFetchOnly());
        assertEquals("SELECT\n\nFROM " + this.tableName + "\nFOR FETCH ONLY", this.query.toSQL());
    }

    public void testField1() {
        query.addField("testField1");
        assertEquals("SELECT\n  testField1\nFROM " + this.tableName, query.toSQL());
        query.addField("testField2");
        assertEquals("SELECT\n  testField1, testField2\nFROM " + this.tableName, query.toSQL());
    }

    public void testField2() {
        assertEquals("fname1", fieldNormal.getPhysicalName());
        assertEquals("fname2", fieldWithAlias.getPhysicalName());
        assertEquals("fname1", fieldNormal.toSQL());
        assertEquals("fname2 AS alias", fieldWithAlias.toSQL());
        query.addField(fieldNormal);
        assertEquals("SELECT\n  fname1\nFROM " + this.tableName, query.toSQL());
        query.addField(fieldWithAlias);
        assertEquals("SELECT\n  fname1, fname2 AS alias\nFROM " + this.tableName, query.toSQL());
    }

    public void testInheritanceWithReflection() {
        assertEquals("SELECT\n  F1, F2\nFROM " + this.tableName, this.ooInherited.toSQL());
    }

    public void testConditionAnd() {
        assertEquals("fname1 = 'value1'", conditionEq.toSQL());
        assertEquals("fname2 = 'value2'", conditionEqWithField.toSQL());
    }

    public void testConditionSimpleConditions() {
        assertEquals("fname1 = 'value1'", conditionEq.toSQL());
        assertEquals("fname2 = 'value2'", conditionEqWithField.toSQL());
        assertEquals("fname1 >= 'value1'", conditionGtOrEq.toSQL());
        assertEquals("fname1 IN\n('value1',\n'value2')", conditionIn.toSQL());
        assertEquals("FIELD1 = FIELD2", new SQLConditions.Eq(new SQLField("FIELD1"), new SQLField("FIELD2")).toSQL());
    }

    public void testJoins() {
        String expected = "";
        expected += this.tableName + " LEFT OUTER JOIN " + convertToDbName("OTHERTABLE") + " ON\n";
        expected += "A = B\n";
        expected += "LEFT OUTER JOIN " + convertToDbName("EVENANOTHERTABLE") + " ON\n";
        expected += "C = 'value of c'";
        assertEquals(expected, tableWithJoins.toSQL());
    }

    public void testComplexCondition() {
        assertEquals("(A = 'B' AND\n  C = 'D') OR\n  (E = 'F' AND\n  G = 'H')", complexCondition.toSQL());
    }

    public void testQueryWithoutCondition() {
        assertEquals("SELECT\n\nFROM " + this.tableName, this.queryWithoutCondition.toSQL());
    }

    public void testQueryEmptyComplexCondition() {
        FluentQuery q = new FluentQuery().FROM(new SQLTable("TESTTBL")).WHERE(new SQLConditions.And().add(new SQLConditions.Eq("A", "B")).add(new SQLConditions.And()));
        assertEquals("SELECT\n\nFROM " + this.tableName + "\nWHERE A = 'B'", q.toSQL());
    }

    public void testEscape() {
        assertEquals("56", FluentQuery.escape(new Integer(56)));
        assertEquals("'ciao'", FluentQuery.escape("ciao"));
    }

    public void testGroupBy() {
        SQLClauses.GroupBy g = new SQLClauses.GroupBy(new SQLField[] { new SQLField("A"), new SQLField("B") });
        assertEquals("GROUP BY A, B", g.toSQL());
    }

    public void testBetween() {
        Between between = new SQLConditions.Between(new SQLField("ciao"), "questo", "quello");
        assertEquals("ciao BETWEEN 'questo' AND 'quello'", between.toSQL());
    }

    public void testSubquery() {
        FluentQuery q = new FluentQuery();
        q.addField(new SQLField("X")).FROM(new SQLTable("TESTTBL")).WHERE(new Eq(new SQLField("A"), "B"));
        In condition = new In(new SQLField("PK"), q);
        assertEquals("PK IN\n(SELECT\n  X\nFROM " + this.tableName + "\nWHERE A = 'B')", condition.toSQL());
    }

    public void testDelete() {
        FluentDelete query = new FluentDelete().setTable(new SQLTable("TESTTBL")).setCondition(new Eq(new SQLField("MAMMA"), "ecco"));
        assertEquals("DELETE FROM " + this.tableName + "\nWHERE MAMMA = 'ecco'", query.toSQL());
    }

    public void testFieldFunctions() {
        assertEquals("SUM(ciao) AS mamma", new SQLFunctions.Sum("ciao", "mamma").toSQL());
        assertEquals("STDDEV(ciao) / AVG(ciao) AS mamma", new SQLFunctions.Variationskoeff("ciao", "mamma").toSQL());
    }

    public void testTableAlias() {
        assertEquals(this.tableName + " AS mamma", new SQLTable("TESTTBL", "mamma").toSQL());
    }

    public void testIsNull() {
        assertEquals("mamma IS NULL", new SQLConditions.IsNull(new SQLField("mamma", "alias")).toSQL());
    }

    public void testMultipleConcat() {
        assertEquals("CONCAT(A, CONCAT(B, C))", new SQLFunctions.Concat(new String[] { "A", "B", "C" }).toSQL());
        assertEquals("CONCAT(A, B)", new SQLFunctions.Concat(new String[] { "A", "B" }).toSQL());
        assertEquals("A", new SQLFunctions.Concat(new String[] { "A" }).toSQL());
    }
}
