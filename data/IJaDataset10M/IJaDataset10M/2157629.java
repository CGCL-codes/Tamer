package org.peaseplate.utils.command;

import org.peaseplate.utils.exception.ExecuteException;
import org.peaseplate.utils.exception.NotAssignableExecuteException;

/**
 * The abstract implementation of a command
 * 
 * @author Manfred HANTSCHEL
 */
public abstract class AbstractCommand implements Command {

    private final int line;

    private final int column;

    /**
	 * Creates the command
	 * 
	 * @param locator the locator
	 * @param line the line
	 * @param column the column
	 */
    public AbstractCommand(final int line, final int column) {
        super();
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

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isAssignable() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object assign(final Scope scope, final Object value) throws ExecuteException {
        throw new NotAssignableExecuteException(this);
    }
}
