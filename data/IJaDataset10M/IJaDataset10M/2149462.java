package org.jowidgets.impl.widgets.common.wrapper.invoker;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;
import org.jowidgets.spi.widgets.IToolBarToggleButtonSpi;
import org.jowidgets.util.EmptyCheck;

public class ToolBarToggleButtonSpiInvoker implements ISelectableItemSpiInvoker {

    private final IToolBarToggleButtonSpi item;

    private String text;

    private String toolTipText;

    private IImageConstant icon;

    public ToolBarToggleButtonSpiInvoker(final IToolBarToggleButtonSpi item) {
        this.item = item;
    }

    public IToolBarToggleButtonSpi getItem() {
        return item;
    }

    @Override
    public void setSelected(final boolean selected) {
        getItem().setSelected(selected);
    }

    @Override
    public boolean isSelected() {
        return getItem().isSelected();
    }

    @Override
    public void setText(final String text) {
        this.text = text;
        setValues();
    }

    @Override
    public void setToolTipText(final String toolTipText) {
        this.toolTipText = toolTipText;
        setValues();
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        this.icon = icon;
        setValues();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        getItem().setEnabled(enabled);
    }

    @Override
    public void setAccelerator(final Accelerator accelerator) {
    }

    @Override
    public void setMnemonic(final Character mnemonic) {
    }

    private void setValues() {
        if (icon == null) {
            getItem().setText(text);
            getItem().setToolTipText(toolTipText);
        } else {
            if (!EmptyCheck.isEmpty(text) && !EmptyCheck.isEmpty(toolTipText)) {
                getItem().setText(null);
                getItem().setToolTipText(text + " : " + toolTipText);
            } else if (!EmptyCheck.isEmpty(text)) {
                getItem().setText(null);
                getItem().setToolTipText(text);
            } else if (!EmptyCheck.isEmpty(toolTipText)) {
                getItem().setText(null);
                getItem().setToolTipText(toolTipText);
            }
        }
        getItem().setIcon(icon);
    }
}
