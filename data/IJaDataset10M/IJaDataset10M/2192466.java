package net.sf.mavenizer.cli.exceptions;

import net.sf.mavenizer.MavenizerException;

/**
 * @author <a href="mailto:cedric-vidal@users.sourceforge.net">C&eacute;dric Vidal</a>
 *
 */
public class ProjectSavingException extends MavenizerException {

    private static final long serialVersionUID = 4392574778554105760L;

    public ProjectSavingException() {
        super();
    }

    public ProjectSavingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectSavingException(String message) {
        super(message);
    }

    public ProjectSavingException(Throwable cause) {
        super(cause);
    }
}
