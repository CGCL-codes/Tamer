package org.jowidgets.impl.widgets.common.wrapper;

import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.common.widgets.IToolBarButtonCommon;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.impl.base.delegate.ItemModelBindingDelegate;
import org.jowidgets.spi.widgets.IToolBarButtonSpi;

public class ToolBarButtonSpiWrapper extends ToolBarItemSpiWrapper implements IToolBarButtonCommon {

    public ToolBarButtonSpiWrapper(final IToolBarButtonSpi widget, final ItemModelBindingDelegate itemDelegate) {
        super(widget, itemDelegate);
        widget.addActionListener(new IActionListener() {

            @Override
            public void actionPerformed() {
                getModel().actionPerformed();
            }
        });
    }

    @Override
    public IToolBarButtonSpi getWidget() {
        return (IToolBarButtonSpi) super.getWidget();
    }

    public IActionItemModel getModel() {
        return (IActionItemModel) getItemModelBindingDelegate().getModel();
    }

    @Override
    public void addActionListener(final IActionListener actionListener) {
        getWidget().addActionListener(actionListener);
    }

    @Override
    public void removeActionListener(final IActionListener actionListener) {
        getWidget().removeActionListener(actionListener);
    }
}
