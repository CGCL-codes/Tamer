package org.apache.myfaces.trinidadinternal.ui.laf.base.desktop;

import java.io.IOException;
import javax.faces.context.ResponseWriter;
import org.apache.myfaces.trinidad.component.UIXHierarchy;
import org.apache.myfaces.trinidad.component.UIXNavigationLevel;
import org.apache.myfaces.trinidadinternal.style.Style;
import org.apache.myfaces.trinidadinternal.style.StyleContext;
import org.apache.myfaces.trinidadinternal.style.StyleMap;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml.LinkUtils;
import org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml.ModelRendererUtils;

/**
 * Renderer for tab bars
 *
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class TabBarRenderer extends HtmlLafRenderer {

    @Override
    protected String getElementName(UIXRenderingContext context, UINode node) {
        return TABLE_ELEMENT;
    }

    @Override
    protected void renderAttributes(UIXRenderingContext context, UINode node) throws IOException {
        super.renderAttributes(context, node);
        renderLayoutTableAttributes(context, "0", null);
    }

    /**
   *
   */
    @Override
    protected void prerender(UIXRenderingContext context, UINode node) throws IOException {
        renderRelatedLinksBlockStart(context, "af_menuTabs.BLOCK_TITLE");
        super.prerender(context, node);
        LinkUtils.startDefaultStyleClassDisabled(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(TABLE_ROW_ELEMENT, null);
    }

    protected UIXHierarchy getHierarchyBase(UIXRenderingContext context, UINode node) {
        return (UIXHierarchy) node.getUIComponent();
    }

    protected UINode getStamp(UIXRenderingContext context, UINode node) {
        return node.getNamedChild(context, NODE_STAMP_CHILD);
    }

    protected boolean setNewPath(UIXRenderingContext context, UINode node, UIXHierarchy component) {
        int startDepth = getIntAttributeValue(context, node, LEVEL_ATTR, 0);
        return ModelRendererUtils.setNewPath(component, startDepth, ((UIXNavigationLevel) component).getFocusRowKey());
    }

    /**
   * @todo - deal with rendered=false on model nodes
   */
    @Override
    protected void renderContent(UIXRenderingContext context, UINode node) throws IOException {
        UIXHierarchy component = getHierarchyBase(context, node);
        UINode stamp = getStamp(context, node);
        if (stamp != null) {
            Object oldPath = component.getRowKey();
            boolean isNewPath = setNewPath(context, node, component);
            if (isNewPath) {
                int size = component.getRowCount();
                int rowIndex = component.getRowIndex();
                boolean needsSeparator = false;
                boolean isSelected = false;
                boolean isFirst = false;
                boolean isLast = false;
                boolean isNextSelected = false;
                int lastIndex = size - 1;
                for (int i = size - 1; i >= 0; i--) {
                    component.setRowIndex(i);
                    boolean isRendered = isRendered(context, stamp);
                    if (isRendered == true) {
                        lastIndex = i;
                        break;
                    }
                }
                int firstIndex = 0;
                for (int i = 0; i < size; i++) {
                    component.setRowIndex(i);
                    boolean isRendered = isRendered(context, stamp);
                    if (isRendered == true) {
                        firstIndex = i;
                        break;
                    }
                }
                for (int i = 0; i < size; i++) {
                    component.setRowIndex(i);
                    boolean isRendered = isRendered(context, stamp);
                    if (isRendered) {
                        if (needsSeparator) renderBetweenNodes(context, node); else needsSeparator = true;
                        isSelected = i == rowIndex;
                        isFirst = i == firstIndex;
                        isLast = i == lastIndex;
                        isNextSelected = i + 1 == rowIndex;
                        renderNode(context, stamp, isSelected, isFirst, isLast, isNextSelected);
                    }
                }
                component.setRowKey(oldPath);
            }
            if (getVisibleIndexedChildCount(context, node) > 0) renderBetweenNodes(context, node);
        }
        super.renderContent(context, node);
    }

    protected boolean isRendered(UIXRenderingContext context, UINode stamp) {
        return getBooleanAttributeValue(context, stamp, RENDERED_ATTR, true);
    }

    /**
   *
   */
    @Override
    protected void postrender(UIXRenderingContext context, UINode node) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement(TABLE_ROW_ELEMENT);
        LinkUtils.endDefaultStyleClassDisabled(context);
        super.postrender(context, node);
        renderRelatedLinksBlockEnd(context);
    }

    protected void renderNode(UIXRenderingContext context, UINode child, boolean selected, boolean isFirst, boolean isLast, boolean isNextSelected) throws IOException {
        renderNode(context, child, selected);
    }

    protected void renderNode(UIXRenderingContext context, UINode child, boolean selected) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(TABLE_DATA_ELEMENT, null);
        boolean disabled = _isDisabled(context, child);
        renderTabStyleAttrs(context, child, selected, disabled);
        renderChild(context, child);
        writer.endElement(TABLE_DATA_ELEMENT);
    }

    @Override
    protected void renderIndexedChild(UIXRenderingContext context, UINode node, int currVisChildIndex, int prevVisChildIndex, int nextVisChildIndex, int ithRenderedChild) throws IOException {
        int selectedIndex = getResolvedSelectedIndex(context, node);
        boolean selected = (currVisChildIndex == selectedIndex);
        UINode child = node.getIndexedChild(context, currVisChildIndex);
        renderNode(context, child, selected, prevVisChildIndex == NO_CHILD_INDEX, nextVisChildIndex == NO_CHILD_INDEX, selectedIndex == nextVisChildIndex);
    }

    /**
   * Renders a separator between tabs.
   */
    @Override
    protected void renderBetweenIndexedChildren(UIXRenderingContext context, UINode node) throws IOException {
        renderBetweenNodes(context, node);
    }

    /**
   * Renders a separator between tabs.
   */
    protected void renderBetweenNodes(UIXRenderingContext context, UINode node) throws IOException {
        if (_doRenderSeparator(context)) {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(TABLE_DATA_ELEMENT, null);
            renderStyleClassAttribute(context, AF_MENU_TABS_SEPARATOR_STYLE_CLASS);
            writer.endElement(TABLE_DATA_ELEMENT);
        }
    }

    /**
   * Renders the style attributes for menuTabs items
   */
    protected void renderTabStyleAttrs(UIXRenderingContext context, UINode node, boolean selected, boolean disabled) throws IOException {
        String styleClass = null;
        if (disabled) styleClass = AF_MENU_TABS_DISABLED_STYLE_CLASS; else if (selected) styleClass = AF_MENU_TABS_SELECTED_STYLE_CLASS; else styleClass = AF_MENU_TABS_ENABLED_STYLE_CLASS;
        renderStyleClassAttribute(context, styleClass);
    }

    /**
   * Override of getStyleClass() which forces style class
   * to menuTabs.
   */
    @Override
    protected Object getStyleClass(UIXRenderingContext context, UINode node) {
        return AF_MENU_TABS_STYLE_CLASS;
    }

    protected boolean isSelected(UIXRenderingContext context, UINode node, int index) {
        return (getResolvedSelectedIndexFromCache(context, node) == index);
    }

    private boolean _isDisabled(UIXRenderingContext context, UINode child) {
        return getBooleanAttributeValue(context, child, DISABLED_ATTR, Boolean.FALSE);
    }

    private boolean _doRenderSeparator(UIXRenderingContext context) {
        Object value = context.getLocalProperty(0, _SEPARATOR_KEY, null);
        if (value != null) return ((Boolean) value).booleanValue();
        boolean doRenderSep = false;
        StyleMap styleMap = context.getStyleContext().getStyleMap();
        if (styleMap != null) {
            StyleContext styleContext = context.getStyleContext();
            Style style = styleMap.getStyleByClass(styleContext, AF_MENU_TABS_SEPARATOR_STYLE_CLASS);
            if (style != null) {
                String width = style.getProperty("width");
                if (!"0px".equals(width)) doRenderSep = true;
            }
        }
        context.setLocalProperty(_SEPARATOR_KEY, (doRenderSep ? Boolean.TRUE : Boolean.FALSE));
        return doRenderSep;
    }

    private static final Object _SEPARATOR_KEY = new Object();
}
