package org.tm4j.ant.dbtasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Topic;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;
import java.beans.PropertyVetoException;

/**
 * create a topic in a topicmap
 */
public class CreateTopic extends TopicTask {

    protected String name;

    protected String type;

    protected String subjectIndicator;

    public void setName(String name) {
        this.name = name;
    }

    public void setSubjectIndicator(String subjectIndicator) {
        this.subjectIndicator = subjectIndicator;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void execute() throws BuildException {
        super.execute();
        log("Creating topic " + topic + " in topicmap " + uri, Project.MSG_DEBUG);
        try {
            if (tm != null) {
                Topic t = tm.createTopic(topic);
                if (name != null) {
                    t.createName(null).setData(name);
                } else {
                    t.createName(null).setData(topic);
                }
                Locator loc = null;
                if (subjectIndicator != null) {
                    loc = tm.getLocatorFactory().createLocator("URI", subjectIndicator);
                    t.addSubjectIndicator(loc);
                } else {
                    if (name != null) {
                        loc = tm.getLocatorFactory().createLocator("URI", uri + "/#" + name);
                    } else {
                        loc = tm.getLocatorFactory().createLocator("URI", uri + "/#" + topic);
                    }
                    t.addSubjectIndicator(loc);
                }
                if (type != null) {
                    Topic tt = tm.getTopicByID(type);
                    if (tt != null) {
                        t.addType(tt);
                    }
                }
            }
            log("Topic created", Project.MSG_INFO);
        } catch (DuplicateObjectIDException e) {
            new BuildException("Duplicate topic", e);
        } catch (PropertyVetoException e) {
            new BuildException("Could not create topic", e);
        } catch (LocatorFactoryException e) {
            new BuildException("Could not get locator factory", e);
        }
    }
}
