package org.nomadpim.core.internal.entity.io;

import org.nomadpim.core.entity.IType;
import org.nomadpim.core.entity.io.ILocalResolver;
import org.nomadpim.core.internal.entity.EntityContainer;
import org.nomadpim.core.internal.entity.IEntityFactory;
import org.nomadpim.core.internal.entity.IEntityTypeConfiguration;
import org.nomadpim.core.util.test.TestersOnly;
import org.nomadpim.core.util.threading.NewThreadExecutor;

public final class EntityTypeConfiguration implements IEntityTypeConfiguration {

    private final IXMLConverter converter;

    private final IDOMContainer domContainer;

    private NewThreadExecutor executor;

    private final IEntityFactory factory;

    private final ILocalResolver resolver;

    public EntityTypeConfiguration(IEntityFactory factory, IDOMContainer container, ILocalResolver resolver, IXMLConverter converter) {
        assert factory != null;
        this.factory = factory;
        this.domContainer = container;
        this.converter = converter;
        this.resolver = resolver;
        this.executor = new NewThreadExecutor();
    }

    public EntityContainer createContainer() {
        DOMDataStore dataStore = new DOMDataStore(converter, domContainer, factory, resolver);
        return new EntityContainer(factory, dataStore, executor);
    }

    @TestersOnly
    public IXMLConverter getConverter() {
        return converter;
    }

    @TestersOnly
    public IDOMContainer getDomContainer() {
        return domContainer;
    }

    @TestersOnly
    public IEntityFactory getFactory() {
        return factory;
    }

    @TestersOnly
    public ILocalResolver getResolver() {
        return resolver;
    }

    public IType getType() {
        return factory.getType();
    }
}
