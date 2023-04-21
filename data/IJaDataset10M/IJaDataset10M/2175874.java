package org.proteusframework.test;

import org.proteusframework.platformservice.IServicePlugin;
import org.proteusframework.platformservice.ServiceClassification;

public class UnitTestAppConfigService extends AbstractTestAppConfigService {

    public UnitTestAppConfigService() {
        super("Message Registrar Test Project");
    }

    @Override
    protected void onPlatformServiceConfiguration(ServiceClassification serviceClassification, IServicePlugin configurableServicePlugin) {
    }
}
