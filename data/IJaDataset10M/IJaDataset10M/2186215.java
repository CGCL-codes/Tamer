package org.starobjects.jpa.runtime.persistence.objectstore.load.oneToManyEagerJoin;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.starobjects.jpa.runtime.persistence.objectstore.JpaObjectStoreAbstractTestCase;

public abstract class Fixture extends JpaObjectStoreAbstractTestCase {

    protected static final int NUMBER_REFERENCED_OBJECTS = 3;

    protected long referencingObjectPK;

    protected long referencedObjectPrefixPK;

    protected ReferencedObjectA[] seedReferencedObjects;

    protected ReferencingObject seedReferencingObject;

    protected NakedObjectSpecification referencingObjectSpec;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        setUpDatabase();
        clearAdapters();
    }

    private void setUpDatabase() throws Exception {
        setUpServicesAndOpenSession(new ReferencingObjectRepository());
        referencingObjectSpec = javaReflector.loadSpecification(ReferencingObject.class);
        referencingObjectPK = 1001L;
        referencedObjectPrefixPK = 2001L;
        seedReferencedObjects = new ReferencedObjectA[3];
        for (int i = 0; i < seedReferencedObjects.length; i++) {
            seedReferencedObjects[i] = new ReferencedObjectA();
            seedReferencedObjects[i].setId(referencedObjectPrefixPK + i);
            seedReferencedObjects[i].setDescription("referenced #" + i);
        }
        seedReferencingObject = new ReferencingObject();
        seedReferencingObject.setId(referencingObjectPK);
        seedReferencingObject.setDescription("referencing");
        for (int i = 0; i < seedReferencedObjects.length; i++) {
            seedReferencingObject.getReferencedObjectAs().add(seedReferencedObjects[i]);
        }
        List<Object> objectsToSave = new ArrayList<Object>();
        for (int i = 0; i < seedReferencedObjects.length; i++) {
            objectsToSave.add(seedReferencedObjects[i]);
        }
        objectsToSave.add(seedReferencingObject);
        save(objectsToSave.toArray());
        Session hibernateSession = jpaObjectStore.getHibernateSession();
        boolean dirty = hibernateSession.isDirty();
        Assert.assertEquals(false, dirty);
    }
}
