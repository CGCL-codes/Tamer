package net.sf.beezle.mork.semantics;

import net.sf.beezle.mork.scanner.Position;

public class SemanticError extends Exception {

    public final Position position;

    public final Exception exception;

    public SemanticError(Position position, Exception exception) {
        this.position = position;
        this.exception = exception;
    }
}
