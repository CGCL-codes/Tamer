package bt747.j2se_view.command;

import sun.security.jca.GetInstance;
import bt747.j2se_view.J2SEController;

public class DefaultCommand extends AbstractCommand {

    /**
	 * Reference to controller to use for commands.
	 */
    private static J2SEController c;

    /**
	 * Set the controller to use in the commands.
	 * 
	 * @param c
	 *            Controller to use.
	 */
    public static final void setController(final J2SEController c) {
        DefaultCommand.c = c;
    }

    /**
	 * Get the controller to use in the commands.
	 * 
	 * @return The controller to use.
	 */
    public static final J2SEController getController() {
        return c;
    }

    /**
	 * Contructor - private to force the use of
	 * {@link #getInstance(bt747.j2se_view.command.Commands.commandType, Object[])}
	 * .
	 * 
	 * @param cmd
	 *            Command type.
	 * @param args
	 *            Argument list for command.
	 */
    private DefaultCommand(final Commands.commandType cmd, final Object[] args) {
        super(cmd, args);
    }

    /**
	 * Get an instance for the given command and arguments.
	 * 
	 * @param cmd
	 *            The command.
	 * @param args
	 *            The arguments for the commands.
	 * @return The instance that can do the command.
	 */
    public static final DefaultCommand getInstance(final Commands.commandType cmd, final Object[] args) {
        switch(cmd) {
            case SETEND:
            case SETSTART:
                return new DefaultCommand(cmd, args);
            default:
                return null;
        }
    }

    public int exec() {
        return 0;
    }
}
