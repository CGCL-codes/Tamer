package org.hornetq.rest.queue;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class QueueDeployment extends DestinationSettings {

    private String name;

    public QueueDeployment() {
    }

    public QueueDeployment(String name, boolean duplicatesAllowed) {
        this.name = name;
        this.duplicatesAllowed = duplicatesAllowed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
