package com.windsor.node.plugin.here.manifest;

import com.windsor.node.common.domain.NodeTransaction;
import com.windsor.node.common.domain.ProcessContentResult;
import com.windsor.node.common.domain.ServiceType;
import com.windsor.node.plugin.here.BaseHEREService;

public class ManifestService extends BaseHEREService {

    public ManifestService() {
        super();
        getSupportedPluginTypes().clear();
        getSupportedPluginTypes().add(ServiceType.QUERY);
    }

    public ProcessContentResult process(NodeTransaction transaction) {
        return process(transaction, MANIFEST_TEMPLATE_NAME, true);
    }
}
