package com.google.gdt.eclipse.designer.model.widgets.generic;

import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.core.model.JavaInfo;
import org.eclipse.wb.core.model.ObjectInfo;
import org.eclipse.wb.core.model.broadcast.JavaEventListener;

/**
 * Some panels, such as <code>SimplePanel</code> require "technical" size <code>"100%"</code> for
 * child widget(s) to fill this panel client area.
 * <p>
 * Supports for following parameters:
 * <ul>
 * <li><b>onChildAdd.setWidth</b> specifies width to set automatically when {@link WidgetInfo} is
 * added to this container.</li>
 * <li><b>onChildAdd.setHeight</b> specifies height to set automatically when {@link WidgetInfo} is
 * added to this container.</li>
 * </ul>
 * 
 * @author scheglov_ke
 * @coverage gwt.model
 */
public final class UpdateSizeOnChildAddSupport extends JavaEventListener {

    private final WidgetInfo m_panel;

    public UpdateSizeOnChildAddSupport(WidgetInfo panel) {
        m_panel = panel;
        m_panel.addBroadcastListener(this);
    }

    @Override
    public void addAfter(JavaInfo parent, JavaInfo child) throws Exception {
        setSize(child);
    }

    @Override
    public void moveAfter(JavaInfo child, ObjectInfo oldParent, JavaInfo newParent) throws Exception {
        if (newParent != oldParent) {
            setSize(child);
        }
    }

    private void setSize(JavaInfo child) throws Exception {
        if (child instanceof WidgetInfo && child.getParent() == m_panel) {
            WidgetInfo widget = (WidgetInfo) child;
            String width = getSizeString(widget, "onChildAdd.setWidth");
            String height = getSizeString(widget, "onChildAdd.setHeight");
            widget.getSizeSupport().setSize(width, height);
        }
    }

    private String getSizeString(WidgetInfo widget, String key) {
        String size = m_panel.getDescription().getParameter(key);
        if ("null".equals(size)) {
            size = null;
        }
        return size;
    }
}
