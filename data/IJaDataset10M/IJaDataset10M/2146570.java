package org.jowidgets.impl.widgets.basic.factory.internal;

import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.api.widgets.descriptor.ISeparatorDescriptor;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.ISeparatorBluePrintSpi;
import org.jowidgets.impl.widgets.basic.ControlImpl;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ColorSettingsInvoker;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.widgets.IControlSpi;

public class SeparatorFactory extends AbstractWidgetFactory implements IWidgetFactory<IWidget, ISeparatorDescriptor> {

    public SeparatorFactory(final IGenericWidgetFactory genericWidgetFactory, final IWidgetsServiceProvider widgetsServiceProvider, final ISpiBluePrintFactory bpF) {
        super(genericWidgetFactory, widgetsServiceProvider, bpF);
    }

    @Override
    public IWidget create(final Object parentUiReference, final ISeparatorDescriptor descriptor) {
        final ISeparatorBluePrintSpi bp = getSpiBluePrintFactory().separator().setSetup(descriptor);
        final IControlSpi widget = getSpiWidgetFactory().createSeparator(parentUiReference, bp);
        ColorSettingsInvoker.setColors(descriptor, widget);
        return new ControlImpl(widget);
    }
}
