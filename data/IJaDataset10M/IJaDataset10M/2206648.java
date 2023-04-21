package org.antlride.internal.antlr.parser.model;

import java.util.List;
import org.antlr.runtime.tree.CommonTree;
import org.antlride.core.model.Rule;
import org.antlride.core.model.Rule.Visibility;
import org.antlride.core.model.SourceElement;
import org.antlride.core.model.builder.OptionBuilder;
import org.antlride.core.model.builder.RuleBuilder;
import org.eclipse.core.runtime.Assert;

/**
 * The {@link CommonTree} {@link RuleBuilder} implementation.
 * 
 * @author Edgar Espina
 * @since 2.1.10
 */
public class RuleBuilderImpl implements RuleBuilder<CommonTree> {

    /**
   * The real {@link RuleBuilder}.
   */
    private final RuleBuilder<SourceElement> delegate;

    /** The rule's name. */
    private final String ruleName;

    /**
   * Creates a new {@link RuleBuilderImpl} using a delegate.
   * 
   * @param ruleName
   * @param delegate The real {@link RuleBuilder}. Cannot be null.
   */
    public RuleBuilderImpl(String ruleName, RuleBuilder<SourceElement> delegate) {
        Assert.isNotNull(delegate);
        this.delegate = delegate;
        this.ruleName = ruleName;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withComment(CommonTree comment) {
        delegate.withComment(Fn.toSourceElement(comment));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withThrows(CommonTree throwsToken, List<CommonTree> exceptions) {
        delegate.withThrows(Fn.toSourceElement(throwsToken), Fn.toSourceElementList(exceptions));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withCatch(CommonTree catchClause, CommonTree catchBody) {
        delegate.withCatch(Fn.toSourceElement(Fn.stripFirstAndLastChars(catchClause), Fn.start(catchClause), Fn.end(catchClause)), Fn.toSourceElement(Fn.stripFirstAndLastChars(catchBody), Fn.start(catchBody), Fn.end(catchBody)));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withFinally(CommonTree finallyToken, CommonTree body) {
        delegate.withFinally(Fn.toSourceElement(finallyToken), Fn.toSourceElement(Fn.stripFirstAndLastChars(body), Fn.start(body), Fn.end(body)));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withScopeReferences(CommonTree scopeToken, List<CommonTree> scopeNames) {
        delegate.withScopeReferences(Fn.toSourceElement(scopeToken), Fn.toSourceElementList(scopeNames));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public OptionBuilder<CommonTree> withOptions(CommonTree options) {
        String text = Fn.text(options);
        String cleanText = text.substring(0, text.length() - 1).trim();
        int start = Fn.start(options);
        int end = Fn.end(options) - (text.length() - cleanText.length());
        return new OptionBuilderImpl(delegate.withOptions(Fn.toSourceElement(cleanText, start, end)));
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withParameters(CommonTree node) {
        String text = Fn.stripFirstAndLastChars(node);
        delegate.withParameters(Fn.toSourceElement(text, Fn.start(node), Fn.end(node)));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withScope(CommonTree scopeName, CommonTree body) {
        String text = Fn.stripFirstAndLastChars(body);
        delegate.withScope(Fn.toSourceElement(ruleName, Fn.start(scopeName), Fn.end(scopeName)), Fn.toSourceElement(text, Fn.start(body), Fn.end(body)));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withAction(CommonTree name, CommonTree body) {
        String text = Fn.stripFirstAndLastChars(body);
        delegate.withAction(Fn.toSourceElement(name), Fn.toSourceElement(text, Fn.start(body), Fn.end(body)));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBodyBuilderImpl withBody() {
        return new RuleBodyBuilderImpl(delegate.withBody());
    }

    @Override
    public RuleBuilder<CommonTree> withReturnValues(CommonTree returns, CommonTree node) {
        String text = Fn.stripFirstAndLastChars(node);
        delegate.withReturnValues(Fn.toSourceElement(returns), Fn.toSourceElement(text, Fn.start(node), Fn.end(node)));
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> withVisibility(Visibility modifier) {
        delegate.withVisibility(modifier);
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public RuleBuilder<CommonTree> excludeFromAST() {
        delegate.excludeFromAST();
        return this;
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public Rule build() {
        return delegate.build();
    }

    public RuleBuilder<SourceElement> getDelegate() {
        return delegate;
    }

    public String getRuleName() {
        return ruleName;
    }
}
