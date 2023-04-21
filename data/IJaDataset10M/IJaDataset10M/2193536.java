package org.antlride.internal.core.model.statement;

import org.antlride.core.ast.ModelElementVisitor;
import org.antlride.core.model.ModelElement;
import org.antlride.core.model.NotStatement;
import org.antlride.core.model.SourceElement;
import org.antlride.core.model.Statement;
import org.eclipse.core.runtime.Assert;

/**
 * <p>
 * Syntax
 * </p>
 * 
 * <pre>
 *  rule
 *     :
 *        ~ terminal  |
 *        ~ block
 *     ;
 * </pre>
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public class NotStatementImpl extends StatementImpl implements NotStatement {

    /**
   * The not token.
   */
    private final SourceElement not;

    /**
   * The negated statement.
   */
    private final Statement statement;

    /**
   * Creates a new {@link NotStatementImpl}.
   * 
   * @param not The not token. Cannot be null.
   * @param statement The negated statement. Cannot be null.
   */
    public NotStatementImpl(SourceElement not, Statement statement) {
        super(not.sourceStart(), not.sourceEnd());
        Assert.isNotNull(statement);
        this.not = not;
        this.statement = statement;
    }

    @Override
    public String getElementName() {
        return "<<not>>";
    }

    /**
   * Return the not token.
   * 
   * @return The not token.
   */
    public SourceElement getNot() {
        return not;
    }

    /**
   * Returns the negated statement.
   * 
   * @return The negated statement.
   */
    public Statement getStatement() {
        return statement;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void traverse(ModelElementVisitor<? extends ModelElement, ?> visitor) {
        if (visitor.visit(this)) {
            statement.traverse(visitor);
            visitor.endvisit(this);
        }
    }

    @Override
    public void toString(StringBuilder buff) {
        buff.append(not).append(" ").append(statement);
    }
}
