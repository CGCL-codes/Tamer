package org.jowidgets.spi.impl.dummy;

import java.util.Collections;
import java.util.List;
import org.jowidgets.common.application.IApplicationRunner;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.common.threads.IUiThreadAccessCommon;
import org.jowidgets.common.types.Position;
import org.jowidgets.common.widgets.IComponentCommon;
import org.jowidgets.spi.IOptionalWidgetsFactorySpi;
import org.jowidgets.spi.IWidgetFactorySpi;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.image.IImageHandleFactorySpi;
import org.jowidgets.spi.impl.dummy.application.DummyApplicationRunner;
import org.jowidgets.spi.impl.dummy.image.DummyImageHandleFactory;
import org.jowidgets.spi.impl.dummy.image.DummyImageHandleFactorySpi;
import org.jowidgets.spi.impl.dummy.image.DummyImageRegistry;
import org.jowidgets.spi.impl.dummy.threads.DummyUiThreadAccess;

public class DummyWidgetsServiceProvider implements IWidgetsServiceProvider {

    private final DummyImageHandleFactorySpi imageHandleFactorySpi;

    private final DummyImageRegistry imageRegistry;

    private final DummyWidgetFactory widgetFactory;

    private final DummyOptionalWidgetsFactory optionalWidgetsFactory;

    public DummyWidgetsServiceProvider() {
        super();
        this.imageRegistry = new DummyImageRegistry(new DummyImageHandleFactory());
        this.imageHandleFactorySpi = new DummyImageHandleFactorySpi(imageRegistry);
        this.widgetFactory = new DummyWidgetFactory(imageRegistry);
        this.optionalWidgetsFactory = new DummyOptionalWidgetsFactory();
    }

    @Override
    public IImageRegistry getImageRegistry() {
        return imageRegistry;
    }

    @Override
    public IImageHandleFactorySpi getImageHandleFactory() {
        return imageHandleFactorySpi;
    }

    @Override
    public IWidgetFactorySpi getWidgetFactory() {
        return widgetFactory;
    }

    @Override
    public IOptionalWidgetsFactorySpi getOptionalWidgetFactory() {
        return optionalWidgetsFactory;
    }

    @Override
    public IUiThreadAccessCommon createUiThreadAccess() {
        return new DummyUiThreadAccess();
    }

    @Override
    public IApplicationRunner createApplicationRunner() {
        return new DummyApplicationRunner();
    }

    @Override
    public Object getActiveWindowUiReference() {
        return null;
    }

    @Override
    public List<Object> getAllWindowsUiReference() {
        return Collections.emptyList();
    }

    @Override
    public Position toScreen(final Position localPosition, final IComponentCommon component) {
        return null;
    }

    @Override
    public Position toLocal(final Position screenPosition, final IComponentCommon component) {
        return null;
    }
}
