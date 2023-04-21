package org.jowidgets.impl.widgets.basic;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IItemModel;
import org.jowidgets.api.model.item.IItemModelListener;
import org.jowidgets.api.model.item.IMenuItemModel;
import org.jowidgets.api.widgets.IActionMenuItem;
import org.jowidgets.api.widgets.IMenu;
import org.jowidgets.api.widgets.descriptor.setup.IAccelerateableMenuItemSetup;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IMenuListener;
import org.jowidgets.impl.base.delegate.ItemModelBindingDelegate;
import org.jowidgets.impl.base.delegate.MenuItemDisposableDelegate;
import org.jowidgets.impl.command.ActionExecuter;
import org.jowidgets.impl.command.ActionWidgetSync;
import org.jowidgets.impl.command.IActionWidget;
import org.jowidgets.impl.model.item.ActionItemModelBuilder;
import org.jowidgets.impl.widgets.common.wrapper.ActionMenuItemSpiWrapper;
import org.jowidgets.impl.widgets.common.wrapper.invoker.ActionMenuItemSpiInvoker;
import org.jowidgets.spi.widgets.IActionMenuItemSpi;

public class ActionMenuItemImpl extends ActionMenuItemSpiWrapper implements IActionMenuItem, IActionWidget {

    private final IMenu parent;

    private final IItemModelListener modelListener;

    private final IMenuListener parentMenuListener;

    private final MenuItemDisposableDelegate disposableDelegate;

    private ActionWidgetSync actionWidgetSync;

    private ActionExecuter actionExecuter;

    private IAction action;

    private boolean onItemChange;

    public ActionMenuItemImpl(final IMenu parent, final IActionMenuItemSpi actionMenuItemSpi, final IAccelerateableMenuItemSetup setup) {
        super(actionMenuItemSpi, new ItemModelBindingDelegate(new ActionMenuItemSpiInvoker(actionMenuItemSpi), new ActionItemModelBuilder().build()));
        this.parent = parent;
        this.disposableDelegate = new MenuItemDisposableDelegate(this, getItemModelBindingDelegate());
        setText(setup.getText());
        setToolTipText(setup.getToolTipText());
        setIcon(setup.getIcon());
        if (setup.getAccelerator() != null) {
            setAccelerator(setup.getAccelerator());
        }
        if (setup.getMnemonic() != null) {
            setMnemonic(setup.getMnemonic().charValue());
        }
        this.modelListener = new IItemModelListener() {

            @Override
            public void itemChanged(final IItemModel item) {
                final IAction newAction = getModel().getAction();
                if (!onItemChange && newAction != action) {
                    onItemChange = true;
                    setActionValue(newAction);
                    onItemChange = false;
                }
            }
        };
        getModel().addItemModelListener(modelListener);
        this.parentMenuListener = new IMenuListener() {

            @Override
            public void menuActivated() {
                if (actionWidgetSync != null) {
                    actionWidgetSync.setActive(true);
                }
            }

            @Override
            public void menuDeactivated() {
                if (actionWidgetSync != null) {
                    actionWidgetSync.setActive(false);
                }
            }
        };
        parent.addMenuListener(parentMenuListener);
        addActionListener(new IActionListener() {

            @Override
            public void actionPerformed() {
                if (actionExecuter != null) {
                    actionExecuter.execute();
                }
            }
        });
    }

    @Override
    public IMenu getParent() {
        return parent;
    }

    @Override
    public void setAction(final IAction action) {
        setActionValue(action);
        getModel().removeItemModelListener(modelListener);
        getModel().setAction(action);
        getModel().addItemModelListener(modelListener);
    }

    private void setActionValue(final IAction action) {
        if (this.action != action) {
            if (action != null) {
                if (action.getAccelerator() != null) {
                    setAccelerator(action.getAccelerator());
                }
                if (action.getMnemonic() != null) {
                    setMnemonic(action.getMnemonic().charValue());
                }
            }
            disposeActionWidgetSync();
            if (action != null) {
                actionWidgetSync = new ActionWidgetSync(action, this);
                actionExecuter = new ActionExecuter(action, this);
            }
            this.action = action;
        }
    }

    @Override
    public void addDisposeListener(final IDisposeListener listener) {
        disposableDelegate.addDisposeListener(listener);
    }

    @Override
    public void removeDisposeListener(final IDisposeListener listener) {
        disposableDelegate.removeDisposeListener(listener);
    }

    @Override
    public boolean isDisposed() {
        return disposableDelegate.isDisposed();
    }

    @Override
    public void dispose() {
        if (!isDisposed()) {
            parent.removeMenuListener(parentMenuListener);
            getModel().removeItemModelListener(modelListener);
            disposeActionWidgetSync();
            disposableDelegate.dispose();
        }
    }

    private void disposeActionWidgetSync() {
        if (actionWidgetSync != null) {
            actionWidgetSync.dispose();
            actionWidgetSync = null;
        }
    }

    @Override
    public void setModel(final IActionItemModel model) {
        if (getModel() != null) {
            getModel().removeItemModelListener(modelListener);
        }
        getItemModelBindingDelegate().setModel(model);
        setActionValue(model.getAction());
        model.addItemModelListener(modelListener);
    }

    @Override
    public void setModel(final IMenuItemModel model) {
        if (model instanceof IActionItemModel) {
            setModel((IActionItemModel) model);
        } else {
            throw new IllegalArgumentException("Model type '" + IActionItemModel.class.getName() + "' expected");
        }
    }
}
