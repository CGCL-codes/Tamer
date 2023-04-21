package ssgen.sql.common.writer;

import ssgen.core.writer.ElementWriter;
import ssgen.core.writer.LiteralWriter;
import ssgen.core.element.SsGenElement;
import ssgen.core.element.LiteralElement;
import ssgen.core.element.ElementList;
import ssgen.core.element.ExpressionMarker;
import ssgen.core.SsGenException;
import ssgen.sql.common.element.AliasElement;
import ssgen.sql.common.element.ParenthesisElement;
import ssgen.sql.common.element.SingleQuoteElement;
import ssgen.sql.common.element.statement.SelectStatement;
import ssgen.sql.common.element.statement.UpdateStatement;
import ssgen.sql.common.element.statement.InsertStatement;
import ssgen.sql.common.element.statement.DeleteStatement;
import ssgen.sql.common.element.function.TrimFunction;
import ssgen.sql.common.element.operator.*;
import ssgen.sql.common.element.clause.*;
import ssgen.sql.common.writer.clause.*;
import ssgen.sql.common.writer.operator.*;
import ssgen.sql.common.writer.function.TrimFunctionWriter;
import ssgen.sql.common.writer.statement.SelectStatementWriter;
import ssgen.sql.common.writer.statement.UpdateStatementWriter;
import ssgen.sql.common.writer.statement.InsertStatementWriter;
import ssgen.sql.common.writer.statement.DeleteStatementWriter;
import ssgen.sql.common.writer.expression.CommonExpressionWriter;
import ssgen.sql.common.expression.SelectStatementExpression;
import ssgen.sql.common.expression.InsertStatementExpression;
import ssgen.sql.common.expression.UpdateStatementExpression;
import ssgen.sql.common.expression.DeleteStatementExpression;
import java.util.Map;
import java.util.HashMap;

/**
 */
public abstract class AbstractSharedQueryWriter implements QueryWriter {

    private static final Map<Class<? extends SsGenElement>, ElementWriter> sharedWriters;

    static {
        sharedWriters = new HashMap<Class<? extends SsGenElement>, ElementWriter>();
        registerWriter(LiteralElement.class, new LiteralWriter());
        registerWriter(AliasElement.class, new AliasElementWriter());
        registerWriter(ElementList.class, new ElementListWriter());
        registerWriter(ParenthesisElement.class, new ParenthesisElementWriter());
        registerWriter(SingleQuoteElement.class, new SingleQuoteElementWriter());
        registerWriter(SelectClause.class, new SelectClauseWriter());
        registerWriter(FromClause.class, new FromClauseWriter());
        registerWriter(WhereClause.class, new WhereClauseWriter());
        registerWriter(GroupByClause.class, new GroupByClauseWriter());
        registerWriter(HavingClause.class, new HavingClauseWriter());
        registerWriter(OrderByClause.class, new OrderByClauseWriter());
        registerWriter(UpdateClause.class, new UpdateClauseWriter());
        registerWriter(SetClause.class, new SetClauseWriter());
        registerWriter(InsertClause.class, new InsertClauseWriter());
        registerWriter(IntoClause.class, new IntoClauseWriter());
        registerWriter(ValuesClause.class, new ValuesClauseWriter());
        registerWriter(DeleteClause.class, new DeleteClauseWriter());
        registerWriter(AllOperator.class, new QuantifiedOperatorWriter());
        registerWriter(SomeOperator.class, new QuantifiedOperatorWriter());
        registerWriter(AnyOperator.class, new QuantifiedOperatorWriter());
        registerWriter(UnionOperator.class, new AggregationOperatorWriter());
        registerWriter(ExceptOperator.class, new AggregationOperatorWriter());
        registerWriter(IntersectOperator.class, new AggregationOperatorWriter());
        registerWriter(UnaryOperator.class, new UnaryOperatorWriter());
        registerWriter(BinaryOperator.class, new BinaryOperatorWriter());
        registerWriter(BetweenOperator.class, new BetweenOperatorWriter());
        registerWriter(InOperator.class, new InOperatorWriter());
        registerWriter(LikeOperator.class, new LikeOperatorWriter());
        registerWriter(JunctionOperator.class, new JunctionOperatorWriter());
        registerWriter(JoinOperator.class, new JoinOperatorWriter());
        registerWriter(SelectStatement.class, new SelectStatementWriter());
        registerWriter(UpdateStatement.class, new UpdateStatementWriter());
        registerWriter(InsertStatement.class, new InsertStatementWriter());
        registerWriter(DeleteStatement.class, new DeleteStatementWriter());
        registerWriter(SelectStatementExpression.class, new CommonExpressionWriter());
        registerWriter(InsertStatementExpression.class, new CommonExpressionWriter());
        registerWriter(UpdateStatementExpression.class, new CommonExpressionWriter());
        registerWriter(DeleteStatementExpression.class, new CommonExpressionWriter());
    }

    private static void registerWriter(Class<? extends SsGenElement> clazz, ElementWriter writer) {
        sharedWriters.put(clazz, writer);
    }

    protected static Map<Class<? extends SsGenElement>, ElementWriter> getSharedWriters() {
        return new HashMap<Class<? extends SsGenElement>, ElementWriter>(sharedWriters);
    }

    public String writeElement(SsGenElement element) {
        final Map<Class<? extends SsGenElement>, ElementWriter> writers = getElementWriters();
        final Class<? extends SsGenElement> clazz = element.getElementClass();
        ElementWriter writer = writers.get(clazz);
        if (writer == null) {
            throw new SsGenException("No corresponding writer for " + clazz.getName());
        }
        writer.setWriters(writers);
        final String sql = writer.write(element);
        final StringBuilder sb = new StringBuilder();
        sb.append(sql);
        return sb.toString();
    }

    public String write(ExpressionMarker expression) {
        return writeElement(expression.getElement());
    }

    protected abstract Map<Class<? extends SsGenElement>, ElementWriter> getElementWriters();
}
