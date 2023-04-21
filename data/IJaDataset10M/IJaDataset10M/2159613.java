package org.jowidgets.spi.impl.swt.common.widgets;

import java.util.List;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.swt.MigLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.controller.IComponentListener;
import org.jowidgets.common.widgets.controller.IFocusListener;
import org.jowidgets.common.widgets.controller.IKeyListener;
import org.jowidgets.common.widgets.controller.IMouseListener;
import org.jowidgets.common.widgets.controller.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetCreator;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.spi.impl.swt.common.util.BorderToComposite;
import org.jowidgets.spi.impl.swt.common.util.ScrollBarSettingsConvert;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.spi.widgets.IScrollCompositeSpi;
import org.jowidgets.spi.widgets.setup.IScrollCompositeSetupSpi;

public class ScrollCompositeImpl implements IScrollCompositeSpi {

    private final SwtComposite outerContainer;

    private final SwtContainer innerContainer;

    public ScrollCompositeImpl(final IGenericWidgetFactory factory, final Object parentUiReference, final IScrollCompositeSetupSpi setup) {
        final MigLayout growingMigLayout = new MigLayout("", "0[grow, 0::]0", "0[grow, 0::]0");
        final String growingCellConstraints = "grow, w 0::,h 0::";
        final Composite outerComposite = BorderToComposite.convert((Composite) parentUiReference, setup.getBorder());
        outerComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        this.outerContainer = new SwtComposite(factory, outerComposite);
        outerComposite.setLayout(growingMigLayout);
        final ScrolledComposite scrolledComposite = new ScrolledComposite(outerComposite, ScrollBarSettingsConvert.convert(setup));
        final SwtContainer scrolledWidget = new SwtContainer(factory, scrolledComposite);
        scrolledComposite.setLayoutData(growingCellConstraints);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setAlwaysShowScrollBars(setup.isAlwaysShowBars());
        scrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        final Composite innerComposite = new Composite(scrolledWidget.getUiReference(), SWT.NONE);
        innerComposite.setBackgroundMode(SWT.INHERIT_DEFAULT);
        this.innerContainer = new SwtContainer(factory, innerComposite);
        scrolledComposite.setContent(innerComposite);
        innerComposite.setLayoutData(growingCellConstraints);
        growingMigLayout.addLayoutCallback(new LayoutCallback() {

            @Override
            public void correctBounds(final ComponentWrapper comp) {
                scrolledComposite.setMinSize(innerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, false));
                super.correctBounds(comp);
            }
        });
    }

    @Override
    public final void setLayout(final ILayoutDescriptor layout) {
        innerContainer.setLayout(layout);
    }

    @Override
    public Composite getUiReference() {
        return outerContainer.getUiReference();
    }

    @Override
    public Rectangle getClientArea() {
        return innerContainer.getClientArea();
    }

    @Override
    public Dimension computeDecoratedSize(final Dimension clientAreaSize) {
        return outerContainer.computeDecoratedSize(clientAreaSize);
    }

    @Override
    public Dimension getMinSize() {
        return outerContainer.getMinSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return outerContainer.getPreferredSize();
    }

    @Override
    public Dimension getMaxSize() {
        return outerContainer.getMaxSize();
    }

    @Override
    public void setLayoutConstraints(final Object layoutConstraints) {
        outerContainer.setLayoutConstraints(layoutConstraints);
    }

    @Override
    public Object getLayoutConstraints() {
        return outerContainer.getLayoutConstraints();
    }

    @Override
    public void setVisible(final boolean visible) {
        outerContainer.setVisible(visible);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        outerContainer.setEnabled(enabled);
        innerContainer.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return outerContainer.isEnabled();
    }

    @Override
    public boolean isVisible() {
        return outerContainer.isVisible();
    }

    @Override
    public Dimension getSize() {
        return outerContainer.getSize();
    }

    @Override
    public void setSize(final Dimension size) {
        outerContainer.setSize(size);
    }

    @Override
    public Position getPosition() {
        return outerContainer.getPosition();
    }

    @Override
    public void setPosition(final Position position) {
        outerContainer.setPosition(position);
    }

    @Override
    public void redraw() {
        outerContainer.redraw();
    }

    @Override
    public void setRedrawEnabled(final boolean enabled) {
        outerContainer.setRedrawEnabled(enabled);
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        outerContainer.setForegroundColor(colorValue);
        innerContainer.setForegroundColor(colorValue);
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        outerContainer.setBackgroundColor(colorValue);
        innerContainer.setBackgroundColor(colorValue);
    }

    @Override
    public void setCursor(final Cursor cursor) {
        outerContainer.setCursor(cursor);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return innerContainer.getForegroundColor();
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return innerContainer.getBackgroundColor();
    }

    @Override
    public boolean requestFocus() {
        return innerContainer.requestFocus();
    }

    @Override
    public void addFocusListener(final IFocusListener listener) {
        innerContainer.addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(final IFocusListener listener) {
        innerContainer.removeFocusListener(listener);
    }

    @Override
    public void addKeyListener(final IKeyListener listener) {
        innerContainer.addKeyListener(listener);
    }

    @Override
    public void removeKeyListener(final IKeyListener listener) {
        innerContainer.removeKeyListener(listener);
    }

    @Override
    public void addMouseListener(final IMouseListener mouseListener) {
        innerContainer.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final IMouseListener mouseListener) {
        innerContainer.removeMouseListener(mouseListener);
    }

    @Override
    public void addComponentListener(final IComponentListener componentListener) {
        innerContainer.addComponentListener(componentListener);
    }

    @Override
    public void removeComponentListener(final IComponentListener componentListener) {
        innerContainer.removeComponentListener(componentListener);
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return innerContainer.createPopupMenu();
    }

    @Override
    public void addPopupDetectionListener(final IPopupDetectionListener listener) {
        innerContainer.addPopupDetectionListener(listener);
    }

    @Override
    public void removePopupDetectionListener(final IPopupDetectionListener listener) {
        innerContainer.removePopupDetectionListener(listener);
    }

    @Override
    public void setTabOrder(final List<? extends IControlCommon> tabOrder) {
        innerContainer.setTabOrder(tabOrder);
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(final Integer index, final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor, final Object cellConstraints) {
        final WIDGET_TYPE result = innerContainer.add(index, descriptor, cellConstraints);
        return result;
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(final Integer index, final ICustomWidgetCreator<WIDGET_TYPE> creator, final Object cellConstraints) {
        return innerContainer.add(index, creator, cellConstraints);
    }

    @Override
    public boolean remove(final IControlCommon control) {
        return innerContainer.remove(control);
    }

    @Override
    public void layoutBegin() {
        outerContainer.layoutBegin();
    }

    @Override
    public void layoutEnd() {
        outerContainer.layoutEnd();
    }

    @Override
    public void removeAll() {
        innerContainer.removeAll();
    }

    @Override
    public void setToolTipText(final String toolTip) {
        innerContainer.setToolTipText(toolTip);
    }
}
