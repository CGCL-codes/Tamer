package org.jowidgets.spi.impl.swing.common.widgets;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JToggleButton;
import org.jowidgets.common.widgets.controller.IItemStateListener;
import org.jowidgets.spi.impl.controller.ItemStateObservable;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;

public class ToolBarToggleButtonImpl extends ToolBarItemImpl implements IToolBarToggleButtonSpi {

    private final ItemStateObservable itemStateObservable;

    public ToolBarToggleButtonImpl(final JToggleButton button) {
        super(button);
        getUiReference().addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(final ItemEvent e) {
                itemStateObservable.fireItemStateChanged();
            }
        });
        this.itemStateObservable = new ItemStateObservable();
    }

    @Override
    public boolean isSelected() {
        return getUiReference().isSelected();
    }

    @Override
    public void setSelected(final boolean selected) {
        getUiReference().setSelected(selected);
    }

    @Override
    public void addItemListener(final IItemStateListener listener) {
        itemStateObservable.addItemListener(listener);
    }

    @Override
    public void removeItemListener(final IItemStateListener listener) {
        itemStateObservable.removeItemListener(listener);
    }
}
