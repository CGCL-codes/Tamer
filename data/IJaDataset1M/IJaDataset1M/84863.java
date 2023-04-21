package org.jowidgets.impl.base.blueprint.proxy;

import java.lang.reflect.Proxy;
import org.jowidgets.api.widgets.blueprint.convenience.ISetupBuilderConvenienceRegistry;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultsInitializerRegistry;
import org.jowidgets.common.widgets.builder.ISetupBuilder;
import org.jowidgets.common.widgets.descriptor.IWidgetDescriptor;
import org.jowidgets.impl.base.blueprint.proxy.internal.BluePrintProxyInvocationHandler;
import org.jowidgets.util.Assert;

public class BluePrintProxyProvider<BLUE_PRINT_TYPE extends ISetupBuilder<?>> {

    private final BLUE_PRINT_TYPE proxy;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BluePrintProxyProvider(final Class<? extends IWidgetDescriptor> bluePrintType, final ISetupBuilderConvenienceRegistry convenienceRegistry, final IDefaultsInitializerRegistry defaultsRegistry) {
        Assert.paramNotNull(bluePrintType, "bluePrintType");
        final BluePrintProxyInvocationHandler invocationHandler = new BluePrintProxyInvocationHandler();
        proxy = (BLUE_PRINT_TYPE) Proxy.newProxyInstance(bluePrintType.getClassLoader(), new Class[] { bluePrintType }, invocationHandler);
        invocationHandler.initialize(proxy, bluePrintType, convenienceRegistry, defaultsRegistry);
    }

    public BLUE_PRINT_TYPE getBluePrint() {
        return proxy;
    }
}
