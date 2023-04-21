package org.nomadpim.core.internal.entity;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.test.FakeDataStore;
import org.nomadpim.core.util.threading.DirectExecutor;

public abstract class AbstractEntityContainerTest extends AbstractTypeTestCase {

    protected EntityContainer container;

    protected IEntityFactory factory;

    protected IDataStore<IEntity> datastore;

    protected void prepareCreate(IEntity expected) {
        expect(factory.create()).andReturn(expected);
    }

    protected void setUp() throws Exception {
        super.setUp();
        factory = createStrictMock(IEntityFactory.class);
        datastore = new FakeDataStore<IEntity>();
        container = new EntityContainer(factory, datastore, new DirectExecutor());
    }
}
