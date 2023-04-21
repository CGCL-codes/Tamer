package org.jowidgets.spi.impl.bridge.swt.awt;

import org.jowidgets.spi.impl.bridge.swt.awt.common.application.BridgedSwtAwtApplicationRunnerFactory;
import org.jowidgets.spi.impl.swt.common.SwtWidgetsServiceProvider;

public class SwtAwtBridgeWidgetsServiceProvider extends SwtWidgetsServiceProvider {

    public SwtAwtBridgeWidgetsServiceProvider() {
        super(new BridgedSwtAwtApplicationRunnerFactory());
    }
}
