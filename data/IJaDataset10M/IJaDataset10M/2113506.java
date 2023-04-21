package org.jowidgets.spi.impl.dummy.widgets;

import org.jowidgets.common.types.Orientation;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.spi.impl.dummy.dummyui.AbstractUIDButton;
import org.jowidgets.spi.impl.dummy.dummyui.UIDComponent;
import org.jowidgets.spi.impl.dummy.dummyui.UIDContainer;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBar;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarButton;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarContainerItem;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarPopupButton;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarSeparator;
import org.jowidgets.spi.impl.dummy.dummyui.UIDToolBarToggleButton;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;
import org.jowidgets.spi.widgets.IToolBarContainerItemSpi;
import org.jowidgets.spi.widgets.IToolBarItemSpi;
import org.jowidgets.spi.widgets.IToolBarPopupButtonSpi;
import org.jowidgets.spi.widgets.IToolBarSpi;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;

public class ToolBarImpl extends DummyControl implements IToolBarSpi {

    private final IGenericWidgetFactory factory;

    public ToolBarImpl(final DummyImageRegistry registry, final IGenericWidgetFactory factory) {
        super(new UIDToolBar());
        this.factory = factory;
    }

    @Override
    public UIDToolBar getUiReference() {
        return (UIDToolBar) super.getUiReference();
    }

    @Override
    public void pack() {
    }

    @Override
    public void remove(final int index) {
        getUiReference().remove(index);
    }

    @Override
    public IToolBarButtonSpi addToolBarButton(final Integer index) {
        final UIDComponent button = addItem(new UIDToolBarButton(), index);
        return new ToolBarButtonImpl((AbstractUIDButton) button);
    }

    @Override
    public IToolBarToggleButtonSpi addToolBarToggleButton(final Integer index) {
        final UIDComponent button = addItem(new UIDToolBarToggleButton(), index);
        return new ToolBarToggleButtonImpl((UIDToolBarToggleButton) button);
    }

    @Override
    public IToolBarPopupButtonSpi addToolBarPopupButton(final Integer index) {
        final UIDComponent button = addItem(new UIDToolBarPopupButton(), index);
        return new ToolBarPopupButtonImpl((AbstractUIDButton) button);
    }

    @Override
    public IToolBarContainerItemSpi addToolBarContainer(final Integer index) {
        final UIDComponent container = addItem(new UIDToolBarContainerItem(), index);
        return new ToolBarContainerItemImpl(factory, (UIDContainer) container);
    }

    @Override
    public IToolBarItemSpi addSeparator(final Integer index) {
        final UIDComponent separator = addItem(new UIDToolBarSeparator(Orientation.VERTICAL), index);
        return new ToolBarSeparatorImpl((UIDToolBarSeparator) separator);
    }

    private UIDComponent addItem(final UIDComponent item, final Integer index) {
        if (index != null) {
            getUiReference().add(item, index.intValue());
        } else {
            getUiReference().add(item);
        }
        return item;
    }
}
