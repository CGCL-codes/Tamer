package org.apache.directory.server.tools.listeners;

import java.io.Serializable;
import org.apache.directory.server.tools.ToolCommandListener;

public class ExceptionListener implements ToolCommandListener {

    public void notify(Serializable o) {
        if (o instanceof Exception) {
            Exception e = (Exception) o;
            System.err.println("An error has occurred. Apache DS Tools must quit." + "\n" + e.getMessage());
            System.exit(1);
        }
    }
}
