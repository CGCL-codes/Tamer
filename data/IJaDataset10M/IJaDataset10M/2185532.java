package net.sourceforge.cruisecontrol.builders;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.Project;

/**
 * Ant Logger impl used to send progress messages back to an AntBuilder.
 *
 * Ideas adapted from contrib/XmlLoggerWithStatus, written by IgorSemenko (igor@semenko.com).
 *
 * @author Dan Rollo
 * Date: Aug 8, 2007
 * Time: 10:21:52 PM
 */
public class AntProgressLogger extends DefaultLogger {

    /**
     * Prefix prepended to system out messages to be detected by AntScript as progress messages.
     * NOTE: Must be the exact same string as that defined in AntScript constant, but kept separate
     * to avoid dependence on Ant Builder classes in AntScript.
     */
    static final String MSG_PREFIX_ANT_PROGRESS = "ccAntProgress -- ";

    public void targetStarted(BuildEvent event) {
        if (Project.MSG_INFO <= msgOutputLevel && !event.getTarget().getName().equals("")) {
            final String name = event.getTarget().getName();
            final String msg = MSG_PREFIX_ANT_PROGRESS + name;
            printMessage(msg, out, event.getPriority());
            log(msg);
        }
    }
}
