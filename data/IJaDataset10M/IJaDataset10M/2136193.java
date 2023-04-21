package org.antlride.core.model.builder;

import org.antlride.core.model.Import;

/**
 * A builder for {@link Import}.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 * @param <TokenType> The token element.
 */
public interface ImportBuilder<TokenType> {

    /**
   * Append the given import
   * 
   * @param alias The import's alias. Can be null.
   * @param name The import's name. Cannot be null.
   * @return This builder.
   */
    ImportBuilder<TokenType> withImport(TokenType alias, TokenType name);
}
