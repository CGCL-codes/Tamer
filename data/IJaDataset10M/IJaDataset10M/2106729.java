package org.jowidgets.spi.impl.swt.common.widgets;

import org.eclipse.swt.widgets.Control;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.spi.widgets.IActionWidgetSpi;
import org.jowidgets.spi.widgets.IComponentSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;

public abstract class AbstractActionComponent extends AbstractActionWidget implements IActionWidgetSpi, IComponentSpi {

    private final SwtComponent swtComponentDelegate;

    public AbstractActionComponent(final Control control) {
        super(control);
        this.swtComponentDelegate = new SwtComponent(control);
    }

    @Override
    public void redraw() {
        swtComponentDelegate.redraw();
    }

    @Override
    public void setRedrawEnabled(final boolean enabled) {
        swtComponentDelegate.setRedrawEnabled(enabled);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        swtComponentDelegate.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        swtComponentDelegate.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return swtComponentDelegate.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return swtComponentDelegate.getBackgroundColor();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        swtComponentDelegate.setCursor(cursor);
    }

    @Override
    public void setVisible(final boolean visible) {
        swtComponentDelegate.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return swtComponentDelegate.isVisible();
    }

    @Override
    public Dimension getSize() {
        return swtComponentDelegate.getSize();
    }

    @Override
    public void setSize(final Dimension size) {
        swtComponentDelegate.setSize(size);
    }

    @Override
    public Position getPosition() {
        return swtComponentDelegate.getPosition();
    }

    @Override
    public void setPosition(final Position position) {
        swtComponentDelegate.setPosition(position);
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return swtComponentDelegate.createPopupMenu();
    }

    @Override
    public boolean requestFocus() {
        return swtComponentDelegate.requestFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        swtComponentDelegate.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        swtComponentDelegate.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        swtComponentDelegate.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        swtComponentDelegate.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        swtComponentDelegate.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        swtComponentDelegate.removeMouseListener(mouseListener);
    }

    @Override
    public void addComponentListener(final IComponentListener componentListener) {
        swtComponentDelegate.addComponentListener(componentListener);
    }

    @Override
    public void removeComponentListener(final IComponentListener componentListener) {
        swtComponentDelegate.removeComponentListener(componentListener);
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        swtComponentDelegate.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        swtComponentDelegate.removePopupDetectionListener(listener);
    }
}
