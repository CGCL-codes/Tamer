package org.orbeon.faces.components.demo.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.orbeon.faces.components.demo.components.PaneComponent;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * <p>Render our associated {@link PaneComponent} as a tabbed control, with
 * each of its immediate child {@link PaneComponent}s representing a single
 * tab.  Measures are taken to ensure that exactly one of the child tabs is
 * selected, and only the selected child pane's contents will be rendered.
 * </p>
 */
public class TabbedRenderer extends BaseRenderer {

    private static Log log = LogFactory.getLog(TabbedRenderer.class);

    public void decode(FacesContext context, UIComponent component) throws IOException {
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("encodeBegin(" + component.getComponentId() + ")");
        }
        String paneClass = (String) component.getAttribute("paneClass");
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<table");
        if (paneClass != null) {
            writer.write(" class=\"");
            writer.write(paneClass);
            writer.write("\"");
        }
        writer.write(">\n");
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("encodeChildren(" + component.getComponentId() + ")");
        }
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("encodeEnd(" + component.getComponentId() + ")");
        }
        Iterator kids = component.getChildren();
        PaneComponent firstPane = null;
        PaneComponent selectedPane = null;
        int n = 0;
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!(kid instanceof PaneComponent)) {
                continue;
            }
            PaneComponent pane = (PaneComponent) kid;
            n++;
            if (firstPane == null) {
                firstPane = pane;
            }
            if (pane.isSelected()) {
                if (selectedPane == null) {
                    selectedPane = pane;
                } else {
                    pane.setSelected(false);
                }
            }
        }
        if ((selectedPane == null) && (firstPane != null)) {
            firstPane.setSelected(true);
            selectedPane = firstPane;
        }
        String selectedClass = (String) component.getAttribute("selectedClass");
        String unselectedClass = (String) component.getAttribute("unselectedClass");
        ResponseWriter writer = context.getResponseWriter();
        writer.write("<tr>\n");
        int percent;
        if (n > 0) {
            percent = 100 / n;
        } else {
            percent = 100;
        }
        kids = component.getChildren();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (!(kid instanceof PaneComponent)) {
                continue;
            }
            PaneComponent pane = (PaneComponent) kid;
            writer.write("<td width=\"");
            writer.write("" + percent);
            writer.write("%\"");
            if (pane.isSelected() && (selectedClass != null)) {
                writer.write(" class=\"");
                writer.write(selectedClass);
                writer.write("\"");
            } else if (!pane.isSelected() && (unselectedClass != null)) {
                writer.write(" class=\"");
                writer.write(unselectedClass);
                writer.write("\"");
            }
            writer.write(">");
            UIComponent facet = (UIComponent) pane.getFacet("label");
            if (facet != null) {
                if (pane.isSelected() && (selectedClass != null)) {
                    facet.setAttribute("paneTabLabelClass", selectedClass);
                } else if (!pane.isSelected() && (unselectedClass != null)) {
                    facet.setAttribute("paneTabLabelClass", unselectedClass);
                }
                facet.encodeBegin(context);
            }
            writer.write("</td>\n");
        }
        writer.write("</tr>\n");
        String contentClass = (String) component.getAttribute("contentClass");
        writer.write("<tr><td width=\"100%\" colspan=\"");
        writer.write("" + n);
        writer.write("\"");
        if (contentClass != null) {
            writer.write(" class=\"");
            writer.write(contentClass);
            writer.write("\"");
        }
        writer.write(">\n");
        selectedPane.encodeBegin(context);
        if (selectedPane.getRendersChildren()) {
            selectedPane.encodeChildren(context);
        }
        selectedPane.encodeEnd(context);
        writer.write("\n</td></tr>\n");
        writer.write("</table>\n");
    }
}
