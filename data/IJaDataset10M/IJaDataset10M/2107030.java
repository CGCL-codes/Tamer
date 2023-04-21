package org.antlride.core.model.builder;

import java.util.List;
import org.antlride.core.model.Rule;
import org.antlride.core.model.Rule.Visibility;

/**
 * A {@link Rule} builder.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public interface RuleBuilder<TokenType> extends OptionsBuilder<TokenType> {

    /**
   * Set the rule's parameters.
   * 
   * @param text The parameter as text.
   * @return This builder.
   */
    RuleBuilder<TokenType> withParameters(TokenType text);

    /**
   * Set the rule's return values.
   * 
   * @param returns The returns token. Cannot be null.
   * @param text The return values as text. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withReturnValues(TokenType returns, TokenType text);

    /**
   * Set the rule's comment.
   * 
   * @param name The rule's comment. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withComment(TokenType comment);

    /**
   * Creates a new {@link OptionBuilder}.
   * 
   * @param options The options token.
   * @return A new {@link OptionBuilder}.
   */
    @Override
    OptionBuilder<TokenType> withOptions(TokenType options);

    /**
   * Set the default scope for this rule.
   * 
   * @param scopeName The scope name.
   * @param body The scope's body.
   * @return This builder.
   */
    RuleBuilder<TokenType> withScope(TokenType scopeName, TokenType body);

    /**
   * Set the scope references for this rules.
   * 
   * @param scopeToken The scope token. Cannot be null.
   * @param scopeNames The scope list. Cannot be empty.
   * @return This builder.
   */
    RuleBuilder<TokenType> withScopeReferences(TokenType scopeToken, List<TokenType> scopeNames);

    /**
   * Creates a body builder.
   * 
   * @return The rule body builder.
   */
    RuleBodyBuilder<TokenType> withBody();

    /**
   * Append an action rule.
   * 
   * @param name The action name. Cannot be null.
   * @param body The action body. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withAction(TokenType name, TokenType body);

    /**
   * Set the rule's visibility.
   * 
   * @param name The rule's visibility. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withVisibility(Visibility visibility);

    /**
   * Set the rule throws element.
   * 
   * @param throwsToken The throws token. Cannot be null.
   * @param exceptions The exception list. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withThrows(TokenType throwsToken, List<TokenType> exceptions);

    /**
   * Append the rule catch element.
   * 
   * @param catchClause The catch clause. Cannot be null.
   * @param catchBody The catch body. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withCatch(TokenType catchClause, TokenType catchBody);

    /**
   * Set the rule finally element.
   * 
   * @param finallyToken The finally token. Cannot be null.
   * @param body The finally body. Cannot be null.
   * @return This builder.
   */
    RuleBuilder<TokenType> withFinally(TokenType finallyToken, TokenType body);

    /**
   * Mark the rule as ignored by the AST nodes creation.
   * 
   * @return This builder.
   */
    RuleBuilder<TokenType> excludeFromAST();

    /**
   * Construct and returns a rule.
   * 
   * @return Construct and returns a rule.
   */
    Rule build();
}
