package se.mafro.sesam.net.smtp;

import se.mafro.sesam.net.Command;

/**
 * @author mange.froberg
 *
 */
public abstract class SMTPCommand implements Command {

    public static final String CRLF = "\r\n";

    public static final String SPACE = " ";

    public String getCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        if (getParameters() != null) {
            sb.append(SPACE);
            sb.append(getParameters());
        }
        sb.append(CRLF);
        return sb.toString();
    }

    public abstract String getName();

    public abstract String getParameters();

    @Override
    public String toString() {
        return getCommand();
    }
}
