package edge.querybuilder.generic.impl.expression;

import edge.querybuilder.generic.Column;
import edge.querybuilder.generic.SelectQuery;
import edge.querybuilder.generic.impl.query.QueryFactory;
import static edge.querybuilder.test.QueryBuilderTestHelper.assertStringEqualsNoFormatting;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AbstractSubqueryExpressionTest {

    @Test
    public void getName() {
        SelectQuery query = QueryFactory.selectQuery(null);
        AbstractSubqueryExpression expression = new AbstractSubqueryExpression(query, "alias") {
        };
        assertStringEqualsNoFormatting("Name is incorrect", "(SELECT * FROM dual)", expression.getName());
    }

    @Test
    public void getAlias() {
        SelectQuery query = QueryFactory.selectQuery(null);
        AbstractSubqueryExpression expression = new AbstractSubqueryExpression(query, "alias") {
        };
        assertEquals("Alias is incorrect", "alias", expression.getAlias());
    }

    @Test
    public void getExpressionNoAlias() {
        SelectQuery query = QueryFactory.selectQuery(null);
        AbstractSubqueryExpression expression = new AbstractSubqueryExpression(query, null) {
        };
        assertStringEqualsNoFormatting("Expression is incorrect", "(SELECT * FROM dual)", expression.getExpression());
    }

    @Test
    public void getExpressionWithAlias() {
        SelectQuery query = QueryFactory.selectQuery(null);
        AbstractSubqueryExpression expression = new AbstractSubqueryExpression(query, "alias") {
        };
        assertEquals("Expression is incorrect", "alias", expression.getExpression());
    }

    @Test
    public void createColumn() {
        SelectQuery query = QueryFactory.selectQuery(null);
        AbstractSubqueryExpression expression = new AbstractSubqueryExpression(query, "alias") {
        };
        Column column = expression.column("my_column", null);
        assertEquals("Column expression is incorrect", "alias.my_column", column.getExpression());
    }

    @Test(expected = Exception.class)
    public void createColumnNoTableAlias() {
        SelectQuery query = QueryFactory.selectQuery(null);
        AbstractSubqueryExpression expression = new AbstractSubqueryExpression(query, null) {
        };
        Column column = expression.column("my_column", null);
    }
}
