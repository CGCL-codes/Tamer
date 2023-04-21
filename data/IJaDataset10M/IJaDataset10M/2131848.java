package com.makotan.util.container.s2;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import com.makotan.util.container.GenericContainer;
import com.makotan.util.container.GenericContainerFactory;

public class S2ContainerFactoryAdapter implements GenericContainerFactory {

    private S2ContainerAdapter defaultContainer;

    private static String defaultPath = "app.dicon";

    public S2ContainerFactoryAdapter(String defaultPath) {
        SingletonS2ContainerFactory.setConfigPath(defaultPath);
        SingletonS2ContainerFactory.init();
        defaultContainer = new S2ContainerAdapter();
        defaultContainer.setContainer(SingletonS2ContainerFactory.getContainer());
    }

    public S2ContainerFactoryAdapter() {
        this(defaultPath);
    }

    public GenericContainer getContainer() {
        return defaultContainer;
    }
}
