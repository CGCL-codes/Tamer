package de.swm.commons.mobile.client.theme.components;

import com.google.gwt.resources.client.CssResource;
import de.swm.commons.mobile.client.widgets.CommandPanel;

/**
 * Represents a css resource for a {@link CommandPanel}. <br>
 * 
 */
public interface CommandPanelCss extends CssResource {

    /** CSS Style name. @return style name. **/
    @ClassName("commandPanel")
    public String commandPanel();

    /** CSS Style name. @return style name. **/
    @ClassName("pressed")
    public String pressed();

    /** CSS Style name. @return style name. **/
    @ClassName("disabled")
    public String disabled();
}
