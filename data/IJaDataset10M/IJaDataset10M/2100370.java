package org.jowidgets.spi.impl.swing.common.widgets;

import javax.swing.JSeparator;
import org.jowidgets.spi.impl.swing.common.util.OrientationConvert;
import org.jowidgets.spi.widgets.IControlSpi;
import org.jowidgets.spi.widgets.setup.ISeparatorSetupSpi;

public class SeparatorImpl extends SwingControl implements IControlSpi {

    public SeparatorImpl(final ISeparatorSetupSpi setup) {
        super(new JSeparator(OrientationConvert.convert(setup.getOrientation())));
    }

    @Override
    public JSeparator getUiReference() {
        return (JSeparator) super.getUiReference();
    }
}
