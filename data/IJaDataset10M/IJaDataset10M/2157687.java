package com.trazere.parser.monad;

/**
 * DOCME
 */
public class MonadParsers {

    public static <Token, Result> FailureParser<Token, Result> failure(final String description) {
        return new FailureParser<Token, Result>(description);
    }

    public static <Token, Result> SuccessParser<Token, Result> success(final Result result, final String description) {
        return new SuccessParser<Token, Result>(result, description);
    }

    private MonadParsers() {
    }
}
