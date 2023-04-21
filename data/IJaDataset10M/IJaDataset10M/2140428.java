package org.kwantu.persistence.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import org.kwantu.persistence.PersistentObject;
import org.kwantu.persistence.test.model.Composite;
import org.kwantu.persistence.test.model.Leaf;
import org.hibernate.Transaction;

public class InstantiationTest extends AbstractPersistenceTest {

    Random random = new Random(123);

    public void test() throws Exception {
        HashSet<String> identityIdsFirst = new HashSet<String>(), identityIdsSecond = new HashSet<String>();
        HashSet<PersistentObject> objectsFirst = new HashSet<PersistentObject>(), objectsSecond = new HashSet<PersistentObject>();
        Transaction transaction = getSession().beginTransaction();
        transaction.commit();
        transaction = getSession().beginTransaction();
        instantiateSome(identityIdsFirst, new Class[] { Composite.class, Leaf.class }, objectsFirst, 70);
        saveSomeObjectsRandomly(objectsFirst, 0.3);
        transaction.commit();
        transaction = getSession().beginTransaction();
        instantiateSome(identityIdsSecond, new Class[] { Composite.class, Leaf.class }, objectsSecond, 60);
        saveSomeObjectsRandomly(objectsSecond, 1);
        transaction.commit();
        assertEquals(true, Collections.disjoint(identityIdsFirst, identityIdsSecond));
    }

    private void saveSomeObjectsRandomly(HashSet<PersistentObject> objectsFirst, double probability) {
        for (PersistentObject o : objectsFirst) {
            if (random.nextDouble() < probability) {
                getSession().save(o);
            }
        }
    }

    private void instantiateSome(HashSet identityIds, Class[] classes, HashSet objects, int n) throws InstantiationException, IllegalAccessException {
        for (Class c : classes) {
            for (int i = 0; i < n; i++) {
                PersistentObject o = (PersistentObject) c.newInstance();
                objects.add(o);
                identityIds.add(o.getIdentityId());
            }
        }
    }
}
