package com.wrupple.muba.widget.client.widgets.panels;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.wrupple.muba.util.client.layout.LayoutPositioner;
import com.wrupple.muba.widget.client.view.HasToolbars;
import com.wrupple.muba.widget.client.widgets.toolbar.Toolbar;

public class PanelWithToolbar extends ResizeComposite implements HasToolbars {

    class Resizer implements ResizeHandler {

        @Override
        public void onResize(ResizeEvent event) {
            redraw();
        }
    }

    private final List<Toolbar> toolbars;

    private final TransitionPanel container;

    private final LayoutPanel main;

    private final ResizeHandler handler;

    protected final LayoutPositioner layout;

    public PanelWithToolbar() {
        container = new TransitionPanel();
        main = new LayoutPanel();
        handler = new Resizer();
        toolbars = new ArrayList<Toolbar>();
        layout = new PanelWithToolbarLayoutDelegate();
        initWidget(main);
        redraw();
    }

    @Override
    public void addToolbar(Toolbar toolbar) {
        addToolbar(toolbar, true);
    }

    @Override
    public TransitionPanel getContentContainer() {
        return container;
    }

    public void hideToolbar(int i) {
        if (i < toolbars.size() && i >= 0) {
            this.toolbars.get(i).asWidget().setVisible(false);
            redraw();
        }
    }

    public void showToolbar(int i) {
        if (i < toolbars.size() && i >= 0) {
            this.toolbars.get(i).asWidget().setVisible(true);
            redraw();
        }
    }

    public void hideToolbar(Toolbar step) {
        int i = findToolbarIndex(step);
        hideToolbar(i);
    }

    public void showToolbar(Toolbar step) {
        int i = findToolbarIndex(step);
        if (i < 0) {
            addToolbar(step);
        }
        showToolbar(i);
    }

    private int findToolbarIndex(Toolbar toolbar) {
        return toolbars.indexOf(toolbar);
    }

    protected void addToolbar(Toolbar toolbar, boolean redraw) {
        if (toolbar == null) {
            return;
        }
        toolbars.add(toolbar);
        toolbar.addResizeHandler(handler);
        if (redraw) {
            redraw();
        }
    }

    protected void redraw() {
        int size;
        layout.initialize(main, container);
        for (Toolbar toolbar : toolbars) {
            if (toolbar.asWidget().isVisible()) {
                size = toolbar.getSize();
                layout.addAtPosition(toolbar.asWidget(), toolbar.getDirection(), size);
            }
        }
        layout.animate(500);
    }

    @Override
    public boolean isToolbarVisible(Toolbar toolbar) {
        if (toolbars.contains(toolbar)) {
            return toolbar.asWidget().isVisible();
        } else {
            return false;
        }
    }

    public Widget getCurrentWidget() {
        return container.getCurrentWidget();
    }
}
