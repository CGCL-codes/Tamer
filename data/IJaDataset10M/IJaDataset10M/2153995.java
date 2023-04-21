package org.starobjects.jpa.runtime.persistence.objectstore.execute.create;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import java.util.Collections;
import java.util.List;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.transaction.PersistenceCommand;
import org.starobjects.jpa.runtime.persistence.objectstore.JpaObjectStoreAbstractTestCase;
import org.starobjects.jpa.runtime.persistence.objectstore.execute.SimpleAutoAssignedObject;
import org.starobjects.jpa.runtime.persistence.objectstore.execute.SimpleAutoAssignedObjectRepository;
import org.starobjects.jpa.runtime.persistence.oid.JpaOid;

@RunWith(JMock.class)
public class GivenSingleObject extends JpaObjectStoreAbstractTestCase {

    private CreateObjectCommand command;

    private NakedObject adapter;

    private SimpleAutoAssignedObject object;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setUpServicesAndOpenSession(new SimpleAutoAssignedObjectRepository());
        setUpCreateCreateCommand();
    }

    private void setUpCreateCreateCommand() {
        adapter = createInstance(SimpleAutoAssignedObject.class);
        object = (SimpleAutoAssignedObject) adapter.getObject();
        object.setSomeString("some string");
        transactionManager.startTransaction();
        command = jpaObjectStore.createCreateObjectCommand(adapter);
    }

    @Test
    public void whenExecuteShouldSaveSingleObjectToDatabase() throws Exception {
        jpaObjectStore.execute(Collections.<PersistenceCommand>singletonList(command));
        List<?> query = querySql("select * from SimpleAutoAssignedObject");
        assertThat(query.size(), is(1));
    }

    @Test
    public void whenExecuteShouldChangeResolveStateFromTransientToResolved() throws Exception {
        assertThat(adapter.getResolveState(), is(ResolveState.TRANSIENT));
        jpaObjectStore.execute(Collections.<PersistenceCommand>singletonList(command));
        assertThat(adapter.getResolveState(), is(ResolveState.RESOLVED));
    }

    @Test
    public void whenExecuteShouldChangeOidFromTransientToPersistent() throws Exception {
        assertThat(adapter.getOid().isTransient(), is(true));
        jpaObjectStore.execute(Collections.<PersistenceCommand>singletonList(command));
        assertThat(adapter.getOid().isTransient(), is(false));
    }

    @Test
    public void whenExecuteShouldOidShouldHavePrevoius() throws Exception {
        assertThat(adapter.getOid().getPrevious(), is(nullValue()));
        jpaObjectStore.execute(Collections.<PersistenceCommand>singletonList(command));
        assertThat(adapter.getOid().getPrevious(), is(not(nullValue())));
    }

    @Test
    public void whenExecuteShouldOidShouldStorePrevoius() throws Exception {
        Oid previousOid = ((JpaOid) adapter.getOid()).clone();
        jpaObjectStore.execute(Collections.<PersistenceCommand>singletonList(command));
        Oid currentOidPrevious = adapter.getOid().getPrevious();
        assertThat(currentOidPrevious, is(equalTo(previousOid)));
    }
}
