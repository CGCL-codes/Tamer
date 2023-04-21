package de.mindcrimeilab.xsanalyzer.ui.support.mydoggy;

import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowType;
import org.springframework.richclient.application.support.DefaultViewDescriptor;

/**
 * Spring RCP adapter to the tool window description of mydoggy.
 * 
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class MyDoggyToolWindowViewDescriptor extends DefaultViewDescriptor {

    private ToolWindowAnchor anchor;

    private ToolWindowType type;

    private boolean available;

    private boolean active;

    /**
     * @return the anchor
     */
    public ToolWindowAnchor getAnchor() {
        return anchor;
    }

    /**
     * @param anchor
     *            the anchor to set
     */
    public void setAnchor(ToolWindowAnchor anchor) {
        this.anchor = anchor;
    }

    /**
     * @return the type
     */
    public ToolWindowType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(ToolWindowType type) {
        this.type = type;
    }

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @param available
     *            the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
