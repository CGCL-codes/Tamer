package org.jowidgets.spi.impl.bridge.awt.swt;

import org.jowidgets.spi.impl.bridge.swt.awt.common.application.BridgedSwtAwtApplicationRunnerFactory;
import org.jowidgets.spi.impl.swing.common.SwingWidgetsServiceProvider;

public class AwtSwtBridgeWidgetsServiceProvider extends SwingWidgetsServiceProvider {

    public AwtSwtBridgeWidgetsServiceProvider() {
        super(new BridgedSwtAwtApplicationRunnerFactory());
    }
}
