package it.gashale.jacolib.shell;

import it.gashale.jacolib.console.ConsoleInterface;
import it.gashale.jacolib.console.StandardIOConsole;
import it.gashale.jacolib.core.JacolibError;

public abstract class LocalShell implements ShellInterface {

    public static ConsoleInterface default_console = new StandardIOConsole();

    private String m_language_name;

    private ConsoleInterface m_console;

    protected LocalShell(String language_name) throws JacolibError {
        m_language_name = language_name;
        m_console = default_console;
    }

    public String getLanguageName() {
        return m_language_name;
    }

    public ConsoleInterface getConsole() {
        return m_console;
    }

    public void setConsole(ConsoleInterface con) throws JacolibError {
        m_console = con;
        notify_new_console();
    }

    protected void notify_new_console() throws JacolibError {
    }

    public String hello() {
        return "Hello! I'm a " + getLanguageName() + "~A shell.";
    }
}
