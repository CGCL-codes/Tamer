package org.ofbiz.widget.menu;

import java.util.Map;

/**
 * Widget Library - Form String Renderer interface
 */
public interface MenuStringRenderer {

    public void renderMenuItem(StringBuffer buffer, Map context, ModelMenuItem menuItem);

    public void renderMenuOpen(StringBuffer buffer, Map context, ModelMenu menu);

    public void renderMenuClose(StringBuffer buffer, Map context, ModelMenu menu);

    public void renderFormatSimpleWrapperOpen(StringBuffer buffer, Map context, ModelMenu menu);

    public void renderFormatSimpleWrapperClose(StringBuffer buffer, Map context, ModelMenu menu);

    public void renderFormatSimpleWrapperRows(StringBuffer buffer, Map context, Object menu);

    public void setUserLoginIdHasChanged(boolean b);

    public void renderLink(StringBuffer buffer, Map context, ModelMenuItem.Link link);

    public void renderImage(StringBuffer buffer, Map context, ModelMenuItem.Image image);
}
