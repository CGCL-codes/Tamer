package com.controltier.ctl.types;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.BuildException;
import java.util.ArrayList;

/**
 * Action type is a TaskContainer, and a child element of HandlerActions.
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision: 1079 $
 * @ant.type name="action"
 */
public class Action implements TaskContainer {

    private String name;

    private ArrayList tasks = new ArrayList();

    public void addTask(Task task) {
        getTasks().add(task);
    }

    public ArrayList getTasks() {
        return tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void validate() {
        if (null == getName()) {
            throw new BuildException("Action tag requires name attribute");
        }
    }
}
