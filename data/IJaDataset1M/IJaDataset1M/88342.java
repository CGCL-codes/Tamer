package de.swm.commons.mobile.client.theme.components;

import com.google.gwt.resources.client.CssResource;

/**
 * Represents a css resource for a decoratedListItem.
 * 
 */
public interface DecoratedListItemCss extends CssResource {

    /** CSS Style name. @return style name. **/
    @ClassName("decoratedListItemHPanel")
    public String decoratedListItemHPanel();

    /** CSS Style name. @return style name. **/
    @ClassName("decoratedListItemVPanel")
    public String decoratedListItemVPanel();

    /** CSS Style name. @return style name. **/
    @ClassName("decoratedListItemTitle")
    public String decoratedListItemTitle();

    /** CSS Style name. @return style name. **/
    @ClassName("decoratedListItemSubtitle")
    public String decoratedListItemSubtitle();
}
