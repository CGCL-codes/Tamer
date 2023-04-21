package com.volantis.osgi.cm;

import com.volantis.osgi.cm.dispatcher.DelayedReferenceContainer;
import com.volantis.osgi.cm.dispatcher.DispatcherMock;
import com.volantis.osgi.cm.plugin.PluginManagerMock;
import com.volantis.osgi.cm.store.ConfigurationStoreMock;
import mock.org.osgi.framework.BundleContextMock;
import mock.org.osgi.service.log.LogServiceMock;
import java.io.IOException;

public abstract class AdminStoreMockBasedTestAbstract extends AdminBasedTestAbstract {

    protected DispatcherMock dispatcherMock;

    protected ConfigurationStoreMock storeMock;

    protected BundleContextMock contextMock;

    private PluginManagerMock pluginManagerMock;

    protected LogServiceMock logServiceMock;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected ConfigurationManager createAdminManager() throws IOException {
        dispatcherMock = new DispatcherMock("dispatcherMock", expectations);
        storeMock = new ConfigurationStoreMock("storeMock", expectations);
        storeMock.expects.load().returns(null).any();
        contextMock = new BundleContextMock("contextMock", expectations);
        pluginManagerMock = new PluginManagerMock("pluginManagerMock", expectations);
        logServiceMock = new LogServiceMock("logServiceMock", expectations);
        DelayedReferenceContainer referenceContainer = new DelayedReferenceContainer();
        referenceContainer.setReference(null);
        return new ConfigurationAdminManager(contextMock, logServiceMock, dispatcherMock, storeMock);
    }
}
