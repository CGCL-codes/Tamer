package de.swm.commons.mobile.client.theme.components;

import com.google.gwt.resources.client.CssResource;

/**
 * Represents a css resource for an Header.
 * 
 */
public interface HeaderCss extends CssResource {

    /** CSS Style name. @return style name. **/
    @ClassName("header-Panel")
    public String headerPanel();

    /** CSS Style name. @return style name. **/
    @ClassName("gwt-Label")
    public String gwtLabel();
}
