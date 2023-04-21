package org.apache.directory.server.tools;

import java.io.Serializable;

/**
 * This abstract class defines a ToolCommand, it must be extended by every type
 * of command
 */
public abstract class BaseToolCommand implements ToolCommand {

    private final String name;

    protected int port;

    protected String host;

    protected String password;

    protected String user;

    protected String auth;

    private boolean debugEnabled = false;

    private boolean verboseEnabled = false;

    private boolean quietEnabled = false;

    private String version;

    protected ToolCommandListener outputListener;

    protected ToolCommandListener errorListener;

    protected ToolCommandListener exceptionListener;

    public BaseToolCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setVerboseEnabled(boolean verboseEnabled) {
        this.verboseEnabled = verboseEnabled;
    }

    public boolean isVerboseEnabled() {
        return verboseEnabled;
    }

    public void setQuietEnabled(boolean quietEnabled) {
        this.quietEnabled = quietEnabled;
    }

    public boolean isQuietEnabled() {
        return quietEnabled;
    }

    /**
	 * Notifies the Output Listener
	 * 
	 * @param o
	 */
    protected void notifyOutputListener(Serializable o) {
        if (this.outputListener != null) {
            this.outputListener.notify(o);
        }
    }

    /**
	 * Notifies the Error Listener
	 * 
	 * @param o
	 */
    protected void notifyErrorListener(Serializable o) {
        if (this.errorListener != null) {
            this.errorListener.notify(o);
        }
    }

    /**
	 * Notifies the Exception Listener
	 * 
	 * @param o
	 */
    protected void notifyExceptionListener(Serializable o) {
        if (this.exceptionListener != null) {
            this.exceptionListener.notify(o);
        }
    }
}
