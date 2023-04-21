package org.jowidgets.impl.widgets.basic.factory.internal;

import java.util.LinkedList;
import java.util.List;
import org.jowidgets.api.convert.IStringObjectConverter;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.descriptor.IComboBoxDescriptor;
import org.jowidgets.common.verify.IInputVerifier;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import org.jowidgets.common.widgets.factory.IWidgetFactory;
import org.jowidgets.impl.spi.ISpiBluePrintFactory;
import org.jowidgets.impl.spi.blueprint.IComboBoxBluePrintSpi;
import org.jowidgets.impl.widgets.basic.ComboBoxImpl;
import org.jowidgets.impl.widgets.basic.factory.internal.util.ComboBoxBuilderConverter;
import org.jowidgets.spi.IWidgetsServiceProvider;
import org.jowidgets.spi.widgets.IComboBoxSpi;
import org.jowidgets.tools.verify.InputVerifierComposite;

public class ComboBoxFactory extends AbstractWidgetFactory implements IWidgetFactory<IComboBox<?>, IComboBoxDescriptor<?>> {

    public ComboBoxFactory(final IGenericWidgetFactory genericWidgetFactory, final IWidgetsServiceProvider widgetsServiceProvider, final ISpiBluePrintFactory bpF) {
        super(genericWidgetFactory, widgetsServiceProvider, bpF);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public IComboBox<?> create(final Object parentUiReference, final IComboBoxDescriptor<?> descriptor) {
        final IComboBoxBluePrintSpi bp = getSpiBluePrintFactory().comboBox().setSetup(descriptor);
        ComboBoxBuilderConverter.convert(bp, descriptor);
        final IStringObjectConverter<?> converter = descriptor.getStringObjectConverter();
        final IInputVerifier inputVerifier = descriptor.getInputVerifier();
        final IInputVerifier converterInputVerifier = converter.getInputVerifier();
        InputVerifierComposite spiInputVerifier = null;
        if (inputVerifier != null || converterInputVerifier != null) {
            spiInputVerifier = new InputVerifierComposite();
            if (inputVerifier != null) {
                spiInputVerifier.addVerifier(inputVerifier);
            }
            if (converterInputVerifier != null) {
                spiInputVerifier.addVerifier(converterInputVerifier);
            }
        }
        final List<String> regExps = descriptor.getAcceptingRegExps();
        final String converterRegExp = converter.getAcceptingRegExp();
        final List<String> spiRegExps = new LinkedList<String>();
        spiRegExps.addAll(regExps);
        if (converterRegExp != null) {
            spiRegExps.add(converterRegExp);
        }
        bp.setInputVerifier(spiInputVerifier);
        bp.setAcceptingRegExps(spiRegExps);
        bp.setMaxLength(descriptor.getMaxLength());
        bp.setMask(converter.getMask());
        final IComboBoxSpi widget = getSpiWidgetFactory().createComboBox(parentUiReference, bp);
        final IComboBox<?> result = new ComboBoxImpl(widget, descriptor);
        return result;
    }
}
