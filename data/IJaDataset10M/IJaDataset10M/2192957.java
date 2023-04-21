package edu.ucsd.ncmir.jibber.events;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import java.net.URI;

/**
 *
 * @author spl
 */
public class PromptEvent extends AsynchronousEvent {

    /**
     * Creates a new instance of PromptEvent
     */
    public PromptEvent() {
    }

    private String label = "";

    private String[] types = { "*" };

    private String type_description = "Any";

    private boolean open = true;

    private URI uri = null;

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String[] getTypes() {
        return this.types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getTypeDescription() {
        return this.type_description;
    }

    public void setTypeDescription(String type_description) {
        this.type_description = type_description;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public URI getURI() {
        return this.uri;
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }
}
