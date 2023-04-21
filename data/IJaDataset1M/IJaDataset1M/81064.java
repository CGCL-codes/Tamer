package org.peaseplate.utils.exception;

import org.peaseplate.utils.Location;

public class CompileException extends PeasePlateException implements Location {

    private static final long serialVersionUID = 1L;

    private final int line;

    private final int column;

    public CompileException(final String message, final int line, final int column, final Object... values) {
        super(message, values);
        this.line = line;
        this.column = column;
    }

    public CompileException(final String message, final Throwable cause, final int line, final int column, final Object... values) {
        super(message, cause, values);
        this.line = line;
        this.column = column;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getLine() {
        return line;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getColumn() {
        return column;
    }
}
