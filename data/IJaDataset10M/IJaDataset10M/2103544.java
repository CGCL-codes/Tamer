package siouxsie.mvc.test;

import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Map.Entry;
import siouxsie.mvc.ActionTriggerDescription;
import siouxsie.mvc.IActionTrigger;

/**
 * Print action info to console.
 * @author Arnaud Cogoluegnes
 * @version $Id: ConsoleActionTrigger.java 143 2008-06-03 20:37:37Z acogo $
 */
public class ConsoleActionTrigger implements IActionTrigger {

    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    public ActionTriggerDescription getActionTriggerDescription() {
        return null;
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    public void triggerActionLaunch(String actionName, String nameSpace, Map<?, ?> parameters) {
        String ls = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer();
        buffer.append("Action triggered: " + nameSpace + actionName + ls);
        if (parameters != null && parameters.size() > 0) {
            buffer.append("Parameters:" + ls);
            for (Entry<?, ?> entry : parameters.entrySet()) {
                buffer.append(entry);
                buffer.append(" [" + entry.getValue().getClass() + "]");
                buffer.append(ls);
            }
        } else {
            buffer.append("No parameters" + ls);
        }
        System.out.println(buffer);
    }
}
