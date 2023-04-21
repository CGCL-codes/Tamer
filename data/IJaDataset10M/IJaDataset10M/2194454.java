package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ToolItem;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.spi.impl.swt.common.image.SwtImageRegistry;
import org.jowidgets.spi.widgets.IToolBarItemSpi;

public class ToolBarItemImpl implements IToolBarItemSpi {

    private final ToolItem toolItem;

    public ToolBarItemImpl(final ToolItem item) {
        super();
        this.toolItem = item;
    }

    @Override
    public ToolItem getUiReference() {
        return toolItem;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        toolItem.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return toolItem.isEnabled();
    }

    @Override
    public void setText(final String text) {
        if (text != null) {
            toolItem.setText(text);
        } else {
            toolItem.setText("");
        }
    }

    @Override
    public void setToolTipText(final String text) {
        toolItem.setToolTipText(text);
    }

    @Override
    public void setIcon(final IImageConstant icon) {
        toolItem.setImage(SwtImageRegistry.getInstance().getImage(icon));
    }

    @Override
    public Position getPosition() {
        final Rectangle bounds = toolItem.getBounds();
        return new Position(bounds.x, bounds.y);
    }

    @Override
    public Dimension getSize() {
        final Rectangle bounds = toolItem.getBounds();
        return new Dimension(bounds.width, bounds.height);
    }
}
