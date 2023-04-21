package org.datanucleus.tests;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.datanucleus.tests.JDOPersistenceTestCase;
import org.jpox.samples.persistenceaware.AccessPublicFields;
import org.jpox.samples.persistenceaware.PublicFields;

/**
 * Tests of persistence-aware capabilities.
 *
 * @version $Revision: 1.4 $
 */
public class PersistenceAwareTest extends JDOPersistenceTestCase {

    /**
     * @param name
     */
    public PersistenceAwareTest(String name) {
        super(name);
    }

    /**
     * Tests wether persistent public fields accessed from another
     * class than the owning class are managed, that is if the
     * accessing class is enhanced correctly.
     */
    public void testPersistenceAware() {
        Object idAlpha = null;
        Object idBeta = null;
        try {
            PersistenceManager pm = pmf.getPersistenceManager();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                PublicFields pfAlpha = new PublicFields();
                PublicFields pfBeta = new PublicFields();
                AccessPublicFields.setStringField(pfAlpha, "alpha");
                AccessPublicFields.setIntField(pfAlpha, 1);
                AccessPublicFields.setObjectField(pfAlpha, pfBeta);
                AccessPublicFields.setStringField(pfBeta, "beta");
                AccessPublicFields.setIntField(pfBeta, 2);
                pm.makePersistent(pfAlpha);
                idAlpha = JDOHelper.getObjectId(pfAlpha);
                idBeta = JDOHelper.getObjectId(pfBeta);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
                pm.close();
            }
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            try {
                tx.begin();
                PublicFields checkAlpha = (PublicFields) pm.getObjectById(idAlpha, true);
                PublicFields checkBeta = (PublicFields) pm.getObjectById(idBeta, true);
                assertEquals("alpha", AccessPublicFields.getStringField(checkAlpha));
                assertEquals(1, AccessPublicFields.getIntField(checkAlpha));
                assertTrue(checkBeta.equals(AccessPublicFields.getObjectField(checkAlpha)));
                assertEquals("beta", AccessPublicFields.getStringField(checkBeta));
                assertEquals(2, AccessPublicFields.getIntField(checkBeta));
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
                pm.close();
            }
        } finally {
            PersistenceManager pm = pmf.getPersistenceManager();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                PublicFields alpha = (PublicFields) pm.getObjectById(idAlpha);
                AccessPublicFields.setObjectField(alpha, null);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            clean(PublicFields.class);
        }
    }
}
