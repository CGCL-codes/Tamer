package org.granite.generator.ant;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.granite.generator.Input;
import org.granite.generator.Listener;
import org.granite.generator.Output;

/**
 * @author Franck WOLFF
 */
public class AntListener implements Listener {

    private final Task task;

    public AntListener(Task task) {
        this.task = task;
    }

    public void generating(Input<?> input, Output<?> output) {
        log("  Generating: " + output.getDescription() + " (" + output.getMessage() + ")", Project.MSG_INFO, null);
    }

    public void generating(String file, String message) {
        log("  Generating: " + file + " (" + message + ")", Project.MSG_INFO, null);
    }

    public void skipping(Input<?> input, Output<?> output) {
        log("  Skipping: " + output.getDescription() + " (" + output.getMessage() + ")", Project.MSG_DEBUG, null);
    }

    public void skipping(String file, String message) {
        log("  Skipping: " + file + " (" + message + ")", Project.MSG_DEBUG, null);
    }

    public void debug(String message) {
        log(message, Project.MSG_DEBUG, null);
    }

    public void debug(String message, Throwable t) {
        log(message, Project.MSG_DEBUG, t);
    }

    public void info(String message) {
        log(message, Project.MSG_INFO, null);
    }

    public void info(String message, Throwable t) {
        log(message, Project.MSG_INFO, t);
    }

    public void warn(String message) {
        log(message, Project.MSG_WARN, null);
    }

    public void warn(String message, Throwable t) {
        log(message, Project.MSG_WARN, t);
    }

    public void error(String message) {
        log(message, Project.MSG_ERR, null);
    }

    public void error(String message, Throwable t) {
        log(message, Project.MSG_ERR, t);
    }

    private void log(String message, int level, Throwable t) {
        if (t != null) message += "\n" + getStackTrace(t);
        task.log(message, level);
    }

    private static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
