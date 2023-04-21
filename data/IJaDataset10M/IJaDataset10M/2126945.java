package org.wings.plaf.css;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.LowLevelEventListener;
import org.wings.SComponent;
import org.wings.SPopupMenu;
import org.wings.util.SStringBuilder;
import org.wings.border.SBorder;
import org.wings.border.STitledBorder;
import org.wings.dnd.DragSource;
import org.wings.io.Device;
import org.wings.plaf.ComponentCG;
import org.wings.script.ScriptListener;
import org.wings.style.Style;
import java.io.IOException;

/**
 * @author ole
 */
public class PrefixAndSuffixDecorator implements CGDecorator {

    private static final long serialVersionUID = 1L;

    protected static final transient Log log = LogFactory.getLog(PrefixAndSuffixDecorator.class);

    protected ComponentCG delegate;

    public PrefixAndSuffixDecorator() {
    }

    public PrefixAndSuffixDecorator(ComponentCG delegate) {
        this.delegate = delegate;
    }

    public ComponentCG getDelegate() {
        return delegate;
    }

    public void setDelegate(ComponentCG delegate) {
        this.delegate = delegate;
    }

    public void installCG(SComponent c) {
        delegate.installCG(c);
    }

    public void uninstallCG(SComponent c) {
        delegate.uninstallCG(c);
    }

    public void componentChanged(SComponent c) {
        delegate.componentChanged(c);
    }

    public void write(Device device, SComponent component) throws IOException {
        boolean wantsPrefixAndSuffix = delegate.wantsPrefixAndSuffix(component);
        if (wantsPrefixAndSuffix) writePrefix(device, component);
        delegate.write(device, component);
        if (wantsPrefixAndSuffix) writeSuffix(device, component);
    }

    public boolean wantsPrefixAndSuffix(SComponent component) {
        return false;
    }

    public void writePrefix(Device device, SComponent component) throws IOException {
        final boolean isTitleBorder = component.getBorder() instanceof STitledBorder;
        Utils.printDebugNewline(device, component);
        Utils.printDebug(device, "<!-- ").print(component.getName()).print(" -->");
        device.print("<div");
        final String classname = component.getStyle();
        Utils.optAttribute(device, "class", isTitleBorder ? classname + " STitledBorder" : classname);
        Utils.optAttribute(device, "id", component.getName());
        Utils.optAttribute(device, "style", getInlineStyles(component));
        if (component instanceof LowLevelEventListener) {
            Utils.optAttribute(device, "eid", component.getEncodedLowLevelEventId());
        }
        writeTooltipMouseOver(device, component);
        writeContextMenu(device, component);
        device.print(">");
        if (isTitleBorder) {
            STitledBorder titledBorder = (STitledBorder) component.getBorder();
            device.print("<div class=\"STitledBorderLegend\">");
            device.print(titledBorder.getTitle());
            device.print("</div>");
        }
        component.fireRenderEvent(SComponent.START_RENDERING);
    }

    public void writeSuffix(Device device, SComponent component) throws IOException {
        component.fireRenderEvent(SComponent.DONE_RENDERING);
        writeInlineScripts(device, component);
        device.print("</div>");
        Utils.printDebug(device, "<!-- /").print(component.getName()).print(" -->");
    }

    protected String getInlineStyles(SComponent component) {
        final SStringBuilder builder = new SStringBuilder();
        if (component instanceof DragSource) builder.append("position:relative;");
        Utils.appendCSSInlineSize(builder, component.getPreferredSize());
        final Style allStyle = component.getDynamicStyle(SComponent.SELECTOR_ALL);
        if (allStyle != null) builder.append(allStyle.toString());
        final SBorder border = component.getBorder();
        if (border != null && border.getAttributes() != null) builder.append(border.getAttributes().toString());
        return builder.toString();
    }

    protected void writeInlineScripts(Device device, SComponent component) throws IOException {
        boolean scriptTagOpen = false;
        for (int i = 0; i < component.getScriptListeners().length; i++) {
            ScriptListener scriptListener = component.getScriptListeners()[i];
            String script = scriptListener.getScript();
            if (script != null) {
                if (!scriptTagOpen) {
                    device.print("<script type=\"text/javascript\">");
                    scriptTagOpen = true;
                }
                device.print(script);
            }
        }
        if (scriptTagOpen) device.print("</script>");
    }

    /**
     * Write JS code for context menus. Common implementaton for MSIE and gecko.
     */
    protected static void writeContextMenu(Device device, SComponent component) throws IOException {
        final SPopupMenu menu = component.getComponentPopupMenu();
        if (menu != null) {
            final String componentId = menu.getName();
            final String popupId = componentId + "_pop";
            device.print(" onContextMenu=\"return wpm_menuPopup(event, '");
            device.print(popupId);
            device.print("');\" onMouseDown=\"return wpm_menuPopup(event, '");
            device.print(popupId);
            device.print("');\"");
        }
    }

    /**
     * Write DomTT Tooltip code. Common handler for MSIE and Gecko PLAF.
     */
    protected static void writeTooltipMouseOver(Device device, SComponent component) throws IOException {
        final String toolTipText = component.getToolTipText();
        if (toolTipText != null) {
            device.print(" onmouseover=\"return makeTrue(domTT_activate(this, event, 'content', '");
            Utils.quote(device, toolTipText.replaceAll("\'", "\\\\'"), true, true, true);
            device.print("', 'predefined', 'default'));\"");
        }
    }
}
