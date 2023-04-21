package org.jcvi.vics.web.gwt.common.client.panel;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;

/**
 * This is in invisible panel that supports child widgets being dragged around within the panel.
 * <p/>
 * To make an internal (child) widget draggable, call getDragController().makeDraggable(widget) or
 * getDragController().makeDraggable(widget, widget);
 *
 * @author Michael Press
 */
public class DraggableAreaPanel extends Composite {

    private PickupDragController _dragController;

    private AbsolutePanel _absolutePanel;

    public DraggableAreaPanel() {
        super();
        init();
    }

    private void init() {
        _dragController = new PickupDragController(null, true);
        _absolutePanel = new AbsolutePanel();
        _absolutePanel.setStyleName("dragBoundaryArea");
        _absolutePanel.add(HtmlUtils.getHtml("&nbsp;", "spacer"));
        initWidget(_absolutePanel);
    }

    public DragController getDragController() {
        return _dragController;
    }

    public void add(Widget widget) {
        _absolutePanel.add(widget);
    }

    public void clear() {
        _absolutePanel.clear();
    }

    public boolean remove(Widget widget) {
        return _absolutePanel.remove(widget);
    }
}
