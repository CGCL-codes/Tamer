package org.antlride.core.model.builder;

import org.antlride.core.model.RuleBody;

/**
 * Creates a new {@link RuleBody}.
 * 
 * @author Edgar Espina
 */
public interface RuleBodyBuilder<Token> extends StatementBuilder<Token> {

    /**
   * Append the given statement.
   * 
   * @param statement The statement to be added. Cannot be null.
   * @return This builder
   */
    RuleBodyBuilder<Token> withStatement(AlternativeBuilder<Token> statement);

    /**
   * Build a new {@link RuleBody}.
   * 
   * @return A new {@link RuleBody}.
   */
    RuleBody build();
}
