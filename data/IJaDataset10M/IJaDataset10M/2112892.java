package org.starobjects.jpa.runtime.persistence.objectstore.update.oneToManyBidir;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;

@RunWith(JMock.class)
public class GivenTransactionWhenAddObjectToCollectionTest extends Fixture {

    protected NakedObject referencingAdapter;

    protected ReferencingObject referencingObject;

    protected NakedObject referencedAdapter;

    protected ReferencedObjectB referencedObjectOther;

    protected NakedObjectTransactionManager transactionManager;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setUpAddObjectToCollection();
        setUpRetrieveComponents();
    }

    private void setUpAddObjectToCollection() {
        flushEntityEvents.clear();
        assertThat(flushEntityEvents.size(), is(0));
        referencingAdapter = retrieveObject(ReferencingObject.class, referencingObjectPK);
        referencingObject = (ReferencingObject) referencingAdapter.getObject();
        referencedAdapter = retrieveObject(ReferencedObjectB.class, referencedObjectBOtherPK);
        referencedObjectOther = (ReferencedObjectB) referencedAdapter.getObject();
        referencingObject.addToReferencedObjectBs(referencedObjectOther);
    }

    private void setUpRetrieveComponents() {
        transactionManager = nakedObjectSession.getPersistenceSession().getTransactionManager();
    }

    /**
	 * Unlike the 1->M, for a 1<->M it seems that only the two objects (ReferencingObject and
	 * the new ReferencedObjectB) are flushed.  Jolly D.
	 */
    @Test
    public void thenFlushesHibernateSession() {
        assertThat(flushEntityEvents.size(), is(0));
        transactionManager.endTransaction();
        assertThat(flushEntityEvents.size(), is(2));
    }

    @Test
    public void thenFlushesDirtiedObject() {
        transactionManager.endTransaction();
        Object pojo = this.flushEntityEvents.get(0).getEntity();
        assertThat(pojo, is(sameInstance((Object) referencingObject)));
        pojo = this.flushEntityEvents.get(1).getEntity();
        assertThat(pojo, is(sameInstance((Object) referencedObjectOther)));
    }

    /**
     * Same number of objects added as flushed, see {@link #thenFlushesHibernateSession()}.
     */
    @Test
    public void thenCommitAddsToUpdateNotifier() {
        transactionManager.endTransaction();
        List<NakedObject> changes = transactionManager.getTransaction().getUpdateNotifier().getChangedObjects();
        assertThat(changes.size(), is(2));
    }
}
