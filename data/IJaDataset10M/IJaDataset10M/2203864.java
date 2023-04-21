package org.apache.myfaces.trinidadinternal.ui.laf.base.desktop;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.trinidadinternal.agent.TrinidadAgent;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.OutputUtils;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.laf.base.LafIconProvider;
import org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml.XhtmlLafRenderer;
import org.apache.myfaces.trinidad.skin.Icon;

/**
 * Base Rendering class for HTML renderers
 * <p>
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class HtmlLafRenderer extends XhtmlLafRenderer implements BaseDesktopConstants {

    /**
   * Returns true if the current user agent is a Netscape user agent.
   */
    public static boolean isNetscape(UIXRenderingContext context) {
        return (context.getAgent().getAgentApplication() == TrinidadAgent.APPLICATION_NETSCAPE);
    }

    /**
   * Returns true if the current user agent is a Safari user agent.
   */
    public static boolean isSafari(UIXRenderingContext context) {
        return (context.getAgent().getAgentApplication() == TrinidadAgent.APPLICATION_SAFARI);
    }

    /**
   * Returns true if the current user agent is a Gecko user agent.
   */
    public static boolean isGecko(UIXRenderingContext context) {
        return (context.getAgent().getAgentApplication() == TrinidadAgent.APPLICATION_GECKO);
    }

    /**
   * Returns true if the current user agent is an Internet Explorer user agent.
   */
    public static boolean isIE(UIXRenderingContext context) {
        return (context.getAgent().getAgentApplication() == TrinidadAgent.APPLICATION_IEXPLORER);
    }

    /**
   * Renders a transparent gif using a script to save space.
   */
    @Override
    protected void renderTransparent(UIXRenderingContext context, String width, String height, boolean needsQuoting) throws IOException {
        super.renderTransparent(context, width, height, needsQuoting);
    }

    protected void renderRepeatingImage(UIXRenderingContext context, String backgroundImageURL) throws IOException {
        renderRepeatingImage(context, backgroundImageURL, null);
    }

    protected void renderRepeatingImage(UIXRenderingContext context, String backgroundImageURL, Object height) throws IOException {
        if (backgroundImageURL != null) {
            ResponseWriter writer = context.getResponseWriter();
            renderLayoutTableHeader(context, ZERO, "100%");
            writeAbsoluteImageURI(context, "background", backgroundImageURL);
            if (height != null) {
                writer.writeAttribute("height", height, null);
            }
            writer.startElement("tr", null);
            writer.startElement("td", null);
            writer.writeAttribute("height", "1px", null);
            writer.endElement("td");
            writer.endElement("tr");
            writer.endElement("table");
        }
    }

    protected void renderRepeatingImage(UIXRenderingContext context, String backgroundImageURL, Object height, String contentImageURL, String contentHAlign, Object contentWidth, Object contentHeight) throws IOException {
        if (backgroundImageURL != null) {
            ResponseWriter writer = context.getResponseWriter();
            renderLayoutTableHeader(context, ZERO, "100%");
            writeAbsoluteImageURI(context, "background", backgroundImageURL);
            if (height != null) {
                writer.writeAttribute("height", height, null);
            }
            writer.startElement("tr", null);
            writer.startElement("td", null);
            if (contentHAlign != null) {
                writer.writeAttribute("align", contentHAlign, null);
            }
            renderIcon(context, contentImageURL, contentWidth, contentHeight);
            writer.endElement("td");
            writer.endElement("tr");
            writer.endElement("table");
        }
    }

    /**
   * Utility method for rendering an icon in a table data cell that is used
   * for trim, so the alt="".
   */
    protected void renderTableDataIcon(UIXRenderingContext context, Icon icon, String styleClass) throws IOException {
        if (icon != null) {
            RenderingContext arc = RenderingContext.getCurrentInstance();
            FacesContext fContext = context.getFacesContext();
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(TABLE_DATA_ELEMENT, null);
            if (styleClass != null) renderStyleClassAttribute(context, styleClass);
            writer.writeAttribute("width", icon.getImageWidth(arc), null);
            writer.writeAttribute("height", icon.getImageHeight(arc), null);
            OutputUtils.renderIcon(fContext, arc, icon, "", null);
            writer.endElement(TABLE_DATA_ELEMENT);
        }
    }

    protected static void writeCacheImageURI(UIXRenderingContext context, String attribute, String uri) throws IOException {
        if ((uri == null) || (uri.length() == 0)) return;
        ResponseWriter writer = context.getResponseWriter();
        String cachedImgURI = LafIconProvider.getCacheImageURI(context) + uri;
        FacesContext facesContext = context.getFacesContext();
        if (facesContext != null) cachedImgURI = facesContext.getExternalContext().encodeResourceURL(cachedImgURI);
        writer.writeURIAttribute(attribute, cachedImgURI, null);
    }
}
