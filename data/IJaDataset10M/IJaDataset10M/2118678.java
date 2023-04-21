package org.peaseplate.utils.highlight;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import org.peaseplate.utils.Location;
import org.peaseplate.utils.exception.CompileException;

public class CharArrayHighlight extends AbstractExceptionHighlight {

    private final int initialLine;

    private final int initialColumn;

    private final char[] chars;

    private final int offset;

    private final int length;

    private final Location location;

    public CharArrayHighlight(final int initialLine, final int initialColumn, final char[] chars, final int offset, final int length, final CompileException exception) {
        this(initialLine, initialColumn, chars, offset, length, exception, exception);
    }

    public CharArrayHighlight(final int initialLine, final int initialColumn, final char[] chars, final int offset, final int length, final Location location, final Throwable exception) {
        super(exception);
        this.initialLine = initialLine;
        this.initialColumn = initialColumn;
        this.chars = chars;
        this.offset = offset;
        this.length = length;
        this.location = location;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getLine() {
        return location.getLine();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public int getColumn() {
        return location.getColumn();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getResource() {
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected int getInitialLine() {
        return initialLine;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected int getInitialColumn() {
        return initialColumn;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected Reader openReader() throws IOException {
        return new CharArrayReader(chars, offset, length);
    }
}
