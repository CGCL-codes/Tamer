package org.apache.myfaces.trinidadinternal.ui.laf.simple.desktop;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputUtils;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidad.skin.Skin;
import org.apache.myfaces.trinidad.skin.Icon;

/**
 * GlobalButtonBarRenderer for the desktop implementation of the
 * Simple Look And Feel.
 *
 * This is an extension of the Base Look And Feel global button bar which
 * adds the following customizable icon:
 *
 * <ul>
 * <li>globalButtonBarSeparator: The separator bewteen global buttons
 *                                    items.
 * </ul>
 *
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class GlobalButtonBarRenderer extends org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml.GlobalButtonBarRenderer implements SimpleDesktopConstants {

    /**
   * Override of renderBetweenIndexedChildren() which
   * renders the separator Icon.
   */
    @Override
    protected void renderBetweenNodes(UIXRenderingContext context, UINode node) throws IOException {
        Icon icon = _getSeparatorIcon(context);
        if (icon != null) {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(TABLE_DATA_ELEMENT, null);
            FacesContext fContext = context.getFacesContext();
            RenderingContext arc = RenderingContext.getCurrentInstance();
            OutputUtils.renderIcon(fContext, arc, icon, "", null);
            writer.endElement(TABLE_DATA_ELEMENT);
        }
    }

    private static Icon _getSeparatorIcon(UIXRenderingContext context) {
        Object localValue = context.getLocalProperty(0, _SEPARATOR_ICON_KEY, _NULL_ICON);
        if (localValue != _NULL_ICON) return (Icon) localValue;
        Skin skin = context.getSkin();
        Icon icon = skin.getIcon(AF_MENU_BUTTONS_SEPARATOR_ICON_NAME);
        context.setLocalProperty(_SEPARATOR_ICON_KEY, icon);
        return icon;
    }

    private static final Object _SEPARATOR_ICON_KEY = new Object();

    private static final Object _NULL_ICON = new Object();
}
