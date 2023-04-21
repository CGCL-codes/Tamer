package joodin.impl.widgets;

import java.util.Iterator;
import java.util.Map;
import joodin.impl.application.util.ApplicationWrapper;
import joodin.impl.widgets.internal.PopupMenuImpl;
import joodin.impl.widgets.internal.util.ChildRemover;
import my.stuff.vaadin.miglayout.MigLayout;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.common.types.Cursor;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.IControlCommon;
import org.jowidgets.common.widgets.IWidgetCommon;
import org.jowidgets.common.widgets.controler.IPopupDetectionListener;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.common.widgets.factory.ICustomWidgetFactory;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.common.widgets.layout.ILayoutDescriptor;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.spi.widgets.IContainerSpi;
import org.jowidgets.spi.widgets.IPopupMenuSpi;
import org.jowidgets.util.Assert;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;

public class VaadinContainer implements IContainerSpi {

    private final IGenericWidgetFactory factory;

    private final Panel container;

    private final VaadinComponent qtWidgetDelegate;

    protected boolean doLayout = true;

    public VaadinContainer(final IGenericWidgetFactory factory, final Panel container) {
        Assert.paramNotNull(factory, "factory");
        this.factory = factory;
        this.container = container;
        this.qtWidgetDelegate = new VaadinComponent(container);
    }

    @SuppressWarnings("serial")
    @Override
    public void setLayout(final ILayoutDescriptor layoutManager) {
        Assert.paramNotNull(layoutManager, "layoutManager");
        if (layoutManager instanceof MigLayoutDescriptor) {
            final MigLayoutDescriptor migLayoutManager = (MigLayoutDescriptor) layoutManager;
            MenuBar menubar = null;
            MigLayout layout = new MigLayout(migLayoutManager.getLayoutConstraints(), migLayoutManager.getColumnConstraints(), migLayoutManager.getRowConstraints()) {

                @Override
                public void changeVariables(Object source, Map variables) {
                    super.changeVariables(source, variables);
                    ApplicationWrapper.getPusher().push();
                }

                @Override
                public String toString() {
                    return "MigLayout@" + this.hashCode();
                }
            };
            if (getUiReference().getContent() instanceof MigLayout) {
                Iterator<Component> it = getUiReference().getContent().getComponentIterator();
                while (it.hasNext()) {
                    Component child = it.next();
                    if (child instanceof MenuBar) {
                        menubar = (MenuBar) child;
                        menubar.setParent(null);
                    }
                }
                container.setContent(null);
                container.setContent(layout);
                if (menubar != null) {
                    layout.addComponent("dock north, height 23::", menubar);
                }
                return;
            }
            container.setContent(layout);
        } else {
            throw new IllegalArgumentException("Layout Manager of type '" + layoutManager.getClass().getName() + "' is not supported");
        }
    }

    @Override
    public Panel getUiReference() {
        return container;
    }

    @Override
    public void redraw() {
        qtWidgetDelegate.redraw();
    }

    @Override
    public void setForegroundColor(final IColorConstant colorValue) {
        qtWidgetDelegate.setForegroundColor(colorValue);
    }

    @Override
    public IColorConstant getForegroundColor() {
        return qtWidgetDelegate.getForegroundColor();
    }

    @Override
    public void setBackgroundColor(final IColorConstant colorValue) {
        qtWidgetDelegate.setBackgroundColor(colorValue);
    }

    @Override
    public IColorConstant getBackgroundColor() {
        return qtWidgetDelegate.getBackgroundColor();
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor, final Object cellConstraints) {
        final WIDGET_TYPE result = factory.create(getUiReference(), descriptor);
        addToContainer(result, cellConstraints);
        return result;
    }

    @Override
    public final <WIDGET_TYPE extends IControlCommon> WIDGET_TYPE add(final ICustomWidgetFactory<WIDGET_TYPE> customFactory, final Object cellConstraints) {
        final IWidgetFactory<WIDGET_TYPE, IWidgetDescriptor<? extends WIDGET_TYPE>> widgetFactory = new IWidgetFactory<WIDGET_TYPE, IWidgetDescriptor<? extends WIDGET_TYPE>>() {

            @Override
            public WIDGET_TYPE create(final Object parentUiReference, final IWidgetDescriptor<? extends WIDGET_TYPE> descriptor) {
                return factory.create(parentUiReference, descriptor);
            }
        };
        final WIDGET_TYPE result = customFactory.create(getUiReference(), widgetFactory);
        addToContainer(result, cellConstraints);
        return result;
    }

    @Override
    public boolean remove(final IControlCommon control) {
        container.removeComponent((Component) control.getUiReference());
        return ChildRemover.removeChild(container, (Component) control.getUiReference());
    }

    @Override
    public void removeAll() {
        if (!(getUiReference().getContent() instanceof MigLayout)) {
            throw new IllegalArgumentException("Layout Manager of type '" + getUiReference().getContent().getClass().getName() + "' is not supported");
        }
        final MigLayout layout = (MigLayout) getUiReference().getContent();
        layout.removeAllComponents();
    }

    @Override
    public void layoutBegin() {
        doLayout = true;
    }

    @Override
    public void layoutEnd() {
        doLayout = false;
    }

    protected IGenericWidgetFactory getGenericWidgetFactory() {
        return factory;
    }

    protected void addToContainer(final IWidgetCommon widget, Object cellConstraints) {
        if (!(getUiReference().getContent() instanceof MigLayout)) {
            throw new IllegalArgumentException("Layout Manager of type '" + getUiReference().getContent().getClass().getName() + "' is not supported");
        }
        if (widget.getUiReference().toString() != null && widget.getUiReference().toString().contains("ToolBarImpl")) {
            cellConstraints = "dock north";
        }
        final MigLayout layout = (MigLayout) getUiReference().getContent();
        if (((Component) widget.getUiReference()).getParent() != null) {
            ((ComponentContainer) ((Component) widget.getUiReference()).getParent()).removeComponent(((Component) widget.getUiReference()));
        }
        ((Component) widget.getUiReference()).setParent(null);
        if (cellConstraints != null) {
            layout.addComponent((String) cellConstraints, (AbstractComponent) widget.getUiReference());
        } else {
            layout.addComponent("", (AbstractComponent) widget.getUiReference());
        }
    }

    @Override
    public void setVisible(final boolean visible) {
        qtWidgetDelegate.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return qtWidgetDelegate.isVisible();
    }

    @Override
    public Dimension getSize() {
        return qtWidgetDelegate.getSize();
    }

    @Override
    public void setCursor(final Cursor cursor) {
        qtWidgetDelegate.setCursor(cursor);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        qtWidgetDelegate.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return qtWidgetDelegate.isEnabled();
    }

    @Override
    public IPopupMenuSpi createPopupMenu() {
        return new PopupMenuImpl(getUiReference());
    }

    @Override
    public void addPopupDetectionListener(IPopupDetectionListener arg0) {
    }

    @Override
    public void removePopupDetectionListener(IPopupDetectionListener arg0) {
    }
}
