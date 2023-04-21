package backupit.core;

import java.io.Serializable;

/**
 *
 * @author dbotelho
 */
public abstract class Backup implements Serializable {

    private String name;

    private ResourceIdentifier source;

    private ResourceIdentifier destination;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceIdentifier getSource() {
        return source;
    }

    public void setSource(ResourceIdentifier source) {
        this.source = source;
    }

    public ResourceIdentifier getDestination() {
        return destination;
    }

    public void setDestination(ResourceIdentifier destination) {
        this.destination = destination;
    }
}
