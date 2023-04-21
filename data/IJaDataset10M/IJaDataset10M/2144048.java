package com.tinywebgears.tuatara.framework.gui.window;

import com.tinywebgears.tuatara.framework.gui.component.ComponentId;
import com.tinywebgears.tuatara.framework.gui.container.WebComponentContainerIF;
import com.tinywebgears.tuatara.framework.gui.resource.WebResourceContext;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractDialog<CC extends ComponentContainer, WCC extends WebComponentContainerIF<CC>> extends AbstractPopupWindow<CC, WCC> {

    private AbstractOrderedLayout mainLayout;

    private AbstractOrderedLayout contentPanel;

    private AbstractOrderedLayout buttonLayout;

    protected AbstractOrderedLayout makeWindowLayout() {
        mainLayout = makeOrderedLayout(Orientation.VERTICAL);
        contentPanel = makeOrderedLayout(Orientation.VERTICAL);
        contentPanel.setSizeUndefined();
        buttonLayout = makeOrderedLayout(Orientation.HORIZONTAL);
        mainLayout.addComponent(contentPanel);
        VerticalLayout buttonLayoutWrapper = new VerticalLayout();
        buttonLayoutWrapper.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        buttonLayoutWrapper.addComponent(buttonLayout);
        buttonLayoutWrapper.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
        mainLayout.addComponent(buttonLayoutWrapper);
        return mainLayout;
    }

    public AbstractDialog(ComponentId id, WebResourceContext webResourceContext, String title, Boolean margin, Boolean spacing, WindowContainerIF windowContainer) {
        super(id, webResourceContext, title, margin, spacing, windowContainer);
    }

    @Override
    protected void setComponentContainerView(WCC componentContainer) {
        contentPanel.removeAllComponents();
        contentPanel.addComponent(componentContainer.getWebComponent());
    }

    protected Layout getButtonLayout() {
        return buttonLayout;
    }

    @Override
    protected void resize(Integer width, Integer widthUnits, Integer height, Integer heightUnits) {
        super.resize(width, widthUnits, height, heightUnits);
        if (width >= 0) {
            mainLayout.setWidth(100, Sizeable.UNITS_PERCENTAGE);
            contentPanel.setWidth(100, Sizeable.UNITS_PERCENTAGE);
        } else {
            mainLayout.setWidth(-1, Sizeable.UNITS_PIXELS);
            contentPanel.setWidth(-1, Sizeable.UNITS_PIXELS);
        }
        if (height >= 0) {
            mainLayout.setHeight(100, Sizeable.UNITS_PERCENTAGE);
            contentPanel.setHeight(100, Sizeable.UNITS_PERCENTAGE);
        } else {
            mainLayout.setHeight(-1, Sizeable.UNITS_PIXELS);
            contentPanel.setHeight(-1, Sizeable.UNITS_PIXELS);
        }
    }
}
