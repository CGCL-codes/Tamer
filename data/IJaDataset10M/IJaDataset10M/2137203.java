package net.sourceforge.deco;

/**
 * A logging interface that must be injected by the user (for example ant task
 * or maven plugin).
 */
public interface Logger {

    public void debug(String msg);

    public void verbose(String msg);

    public void info(String msg);

    public void warning(String msg);

    public void error(String msg);
}
