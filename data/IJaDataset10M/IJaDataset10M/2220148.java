package org.antlride.internal.antlr.parser.model;

import org.antlr.runtime.tree.CommonTree;
import org.antlride.core.model.SourceElement;
import org.antlride.core.model.Statement;
import org.antlride.core.model.Statement.EbnfModifier;
import org.antlride.core.model.Statement.TreeModifier;
import org.antlride.core.model.builder.StatementBuilder;
import org.eclipse.core.runtime.Assert;

/**
 * The {@link CommonTree} implementation of {@link StatementBuilder}.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public class StatementBuilderImpl<D extends StatementBuilder<SourceElement>> implements StatementBuilder<CommonTree> {

    /**
   * The real {@link StatementBuilder}.
   */
    protected final D delegate;

    /**
   * Creates a new {@link StatementBuilderImpl}.
   * 
   * @param delegate The real {@link StatementBuilder}.
   */
    public StatementBuilderImpl(D delegate) {
        Assert.isNotNull(delegate);
        this.delegate = delegate;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public StatementBuilder<CommonTree> withModifier(EbnfModifier modifier) {
        delegate.withModifier(modifier);
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public StatementBuilder<CommonTree> withModifier(TreeModifier modifier) {
        delegate.withModifier(modifier);
        return this;
    }

    public D getDelegate() {
        return this.delegate;
    }

    @SuppressWarnings("unchecked")
    public static <D extends StatementBuilder<SourceElement>> D getDelegate(StatementBuilder<CommonTree> statementBuilder) {
        return (D) ((StatementBuilderImpl<?>) statementBuilder).getDelegate();
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Statement build() {
        return delegate.build();
    }
}
