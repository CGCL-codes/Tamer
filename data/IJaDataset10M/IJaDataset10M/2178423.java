package net.sf.table4gwt.client.renderer.cell.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.sf.table4gwt.client.renderer.cell.CellRenderer;
import net.sf.table4gwt.client.renderer.cell.WrapperCellRenderer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * Wrapper class that allows to add click listener  to widget created by the wrapped {@link CellRenderer} .
 * 
 * @author Vitaliy S aka Yilativs emailto:vitaliy.se@gmail.com
 * 
 */
public class ClickHandlerCellRenderer implements WrapperCellRenderer {

    private final CellRenderer cellRenderer;

    private final Set<ClickHandler> clickHandlerSet = new HashSet<ClickHandler>();

    public ClickHandlerCellRenderer(CellRenderer cellRenderer, ClickHandler... clickHandlers) {
        this.cellRenderer = cellRenderer;
        clickHandlerSet.addAll(Arrays.asList(clickHandlers));
    }

    public final Widget render(Object object) {
        Widget widget = cellRenderer.render(object);
        if (!(widget instanceof HasClickHandlers)) {
            widget = new ClickablePanel(widget);
        }
        for (ClickHandler clickHandler : clickHandlerSet) {
            ((HasClickHandlers) widget).addClickHandler(clickHandler);
        }
        return widget;
    }

    public CellRenderer getWrappedCellRenderer() {
        return cellRenderer;
    }

    static class ClickablePanel extends SimplePanel implements HasClickHandlers {

        public ClickablePanel(Widget widget) {
            add(widget);
            sinkEvents(DOM.getEventsSunk(this.getElement()) | Event.ONCLICK);
        }

        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return super.addHandler(handler, ClickEvent.getType());
        }
    }
}
