package org.jowidgets.spi.widgets.setup;

import org.jowidgets.common.mask.ITextMask;
import org.jowidgets.common.widgets.descriptor.setup.IComboBoxSetupCommon;

public interface IComboBoxSetupSpi extends ITextComponentSetupSpi, IComboBoxSelectionSetupSpi, IComboBoxSetupCommon {

    ITextMask getMask();
}
