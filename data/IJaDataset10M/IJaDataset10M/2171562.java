package com.csam.tokenizer;

/**
 * Top-most exception class for atomizer-specific error conditions.
 *
 * @author Nathan Crause <ncrause at clarkesolomou.com>
 */
public class TokenizerException extends Exception {

    public TokenizerException(Throwable cause) {
        super(cause);
    }

    public TokenizerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenizerException(String message) {
        super(message);
    }

    public TokenizerException() {
    }
}
