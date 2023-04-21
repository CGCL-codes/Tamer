package com.gwtaf.core.client.layout;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.Widget;
import com.gwtaf.core.client.util.Size;

@SuppressWarnings("deprecation")
public class RootLayoutPanel extends LayoutPanel {

    private final WindowResizeListener handler = new WindowResizeListener() {

        @Override
        public void onWindowResized(int width, int height) {
            RootLayoutPanel.this.onResize();
        }
    };

    public RootLayoutPanel(Widget w) {
        setLayout(new RootLayout());
        add(w);
    }

    @Override
    public void setLayout(ILayout layout) {
        if (layout instanceof RootLayout) super.setLayout(layout); else throw new IllegalArgumentException("layout not accepted");
    }

    @Override
    protected void onLoad() {
        Window.enableScrolling(false);
        Window.setMargin("0");
        Window.addWindowResizeListener(handler);
        super.onLoad();
    }

    @Override
    protected void onUnload() {
        Window.removeWindowResizeListener(handler);
        super.onUnload();
    }

    @Override
    public void onResize() {
        Window.enableScrolling(false);
        super.onResize();
        Size minSize = getMinSize();
        if (minSize.getWidth() > Window.getClientWidth() || minSize.getHeight() > Window.getClientHeight()) Window.enableScrolling(true);
    }
}
