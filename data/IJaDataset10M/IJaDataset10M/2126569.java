package com.gwtaf.core.client.layout;

import com.google.gwt.user.client.ui.Widget;
import com.gwtaf.core.client.util.Size;

public class SimpleLayout extends AbstractLayout<LayoutData> {

    private Size minSize;

    @Override
    public void onAddChild(Widget w) {
        super.onAddChild(w);
        if (getLayoutPanel().getWidgetCount() > 1) throw new IllegalStateException("Only one widget allowed");
    }

    @Override
    protected LayoutData createDefaultLayoutData() {
        return new LayoutData(100, 100);
    }

    @Override
    public void layout() {
        if (getLayoutPanel().getWidgetCount() > 0) {
            Size size = getClientSize();
            Widget widget = getLayoutPanel().getWidget(0);
            LayoutData widgetData = getWidgetData(widget);
            int width = Math.max(widgetData.effectiveMinWidth, size.getWidth());
            int height = Math.max(widgetData.effectiveMinHeight, size.getHeight());
            sizeWidget(widget, width, height);
        }
    }

    @Override
    public void measure() {
        if (getLayoutPanel().getWidgetCount() > 0) {
            Widget widget = getLayoutPanel().getWidget(0);
            minSize = updateEffectiveMinSize(widget);
        } else minSize = new Size(0, 0);
    }

    public Size getMinSize() {
        return minSize;
    }
}
