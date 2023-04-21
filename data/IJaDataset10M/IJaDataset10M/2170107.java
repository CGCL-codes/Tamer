package net.sourceforge.javautil.ui.command;

import net.sourceforge.javautil.ui.UIException;

/**
 * The base for exceptions thrown by {@link IUICommand}'s.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: UICommandException.java 2514 2010-11-04 03:38:34Z ponderator $
 */
public class UICommandException extends UIException {

    protected final IUICommand command;

    public UICommandException(IUICommand command) {
        this(command, null, null);
    }

    public UICommandException(IUICommand command, String message, Throwable cause) {
        super(message, cause);
        this.command = command;
    }

    public UICommandException(IUICommand command, String message) {
        this(command, message, null);
    }

    public UICommandException(IUICommand command, Throwable cause) {
        this(command, null, cause);
    }

    /**
	 * @return The command related to this exception
	 */
    public IUICommand getCommand() {
        return command;
    }
}
