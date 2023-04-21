package org.vramework.vow.test;

import org.vramework.vow.IVOW;
import org.vramework.vow.IVOWListener;
import org.vramework.vow.ObjectState;
import org.vramework.vow.test.objectmodel.Customer;
import org.vramework.vow.test.objectmodel.DatabaseMock;
import org.vramework.vow.test.objectmodel.IdentityCustomer;

/**
 * @author thomas.mahringer
 * 
 */
public class VOWDeleteTest extends VOWTestCase {

    /**
   * @see org.vramework.commons.junit.VTestCase#setUp()
   */
    @Override
    public void firstRunOfTestCaseClass() {
        log("----------------------------------------------------------------------");
        log("Start " + getClass().getSimpleName() + " ------------------------------------");
        log("----------------------------------------------------------------------");
    }

    /**
   * Object without Identity added => New => Delete => Listener does not vetoe =>
   * Must be removed from VOW.
   */
    public void testNoIdentityAddDeleteListenerNoVetoe() {
        doTestNoIdentityAddDelete(true, false, ObjectState.Detached);
    }

    /**
   * Object without Identity added => New => Delete => No listener => Must be
   * removed from VOW.
   */
    public void testNoIdentityAddDeleteNoListener() {
        doTestNoIdentityAddDelete(false, false, ObjectState.Detached);
    }

    /**
   * Object without Identity added => New => Delete => Listener vetoes => Must
   * not be removed from VOW => Stay new.
   */
    public void testNoIdentityAddDeleteListenerVetoe() {
        doTestNoIdentityAddDelete(true, true, ObjectState.New);
    }

    /**
   * Internal, called by others change testers. Adds an object without identity
   * and deletes it.
   * 
   * @param useListener
   * @param vetoeChange
   * @param expectedState
   */
    public void doTestNoIdentityAddDelete(boolean useListener, boolean vetoeChange, ObjectState expectedState) {
        IVOW uow = VOWTest.createVOW();
        Customer customer = DatabaseMock.getNewMock().queryFirstCustomer();
        uow.register(customer);
        assertTrue("Customer should be new", uow.isNew(customer));
        uow.setProperty(customer, "_lastName", "LastName set by testCase");
        assertTrue("Customer should be new", uow.isNew(customer));
        assertTrue("Regsitered count should be '1' here", uow.registeredCount() == 1);
        if (useListener) {
            IVOWListener listener = new VOWTestListener(vetoeChange);
            uow.registerDeleteListener(listener);
        }
        uow.delete(customer);
        if (expectedState == ObjectState.New) {
            assertTrue("Customer should be new", uow.isNew(customer));
        } else if (expectedState == ObjectState.Detached) {
            assertTrue("Customer should be detached", !uow.isRegistered(customer));
        } else {
            assertTrue("Should not get here, illegal state", false);
        }
        assertTrue("Deleted count should be '0' here", uow.deletedCount() == 0);
    }

    /**
   * Object with Identity added => Initial => Delete => Listener does not vetoe =>
   * Deleted.
   */
    public void testIdentityAddDeleteListenerNoVetoe() {
        doTestIdentityAddDelete(true, false, ObjectState.Deleted);
    }

    /**
   * Object with Identity added => Initial => Delete => No listener => Must be
   * deleted.
   */
    public void testIdentityAddDeleteNoListener() {
        doTestIdentityAddDelete(false, false, ObjectState.Deleted);
    }

    /**
   * Object with Identity added => Initial => Delete => Listener vetoes => Must
   * stay initial.
   */
    public void testIdentityAddDeleteListenerVetoe() {
        doTestIdentityAddDelete(true, true, ObjectState.Initial);
    }

    /**
   * Internal, called by other testers.
   * 
   * @param useListener
   * @param vetoeChange
   * @param expectedState
   */
    public void doTestIdentityAddDelete(boolean useListener, boolean vetoeChange, ObjectState expectedState) {
        IVOW uow = VOWTest.createVOW();
        IdentityCustomer customer = DatabaseMock.getNewMock().queryFirstIdentityCustomer();
        uow.register(customer);
        assertTrue("Customer should be new", uow.isInitial(customer));
        assertTrue("Regsitered count should be '1' here", uow.registeredCount() == 1);
        if (useListener) {
            IVOWListener listener = new VOWTestListener(vetoeChange);
            uow.registerDeleteListener(listener);
        }
        uow.delete(customer);
        if (expectedState == ObjectState.Initial) {
            assertTrue("Customer should be initial", uow.isInitial(customer));
            assertTrue("Deleted count should be '1' here", uow.deletedCount() == 0);
        } else if (expectedState == ObjectState.Deleted) {
            assertTrue("Customer should be deleted", uow.isDeleted(customer));
            assertTrue("Deleted count should be '1' here", uow.deletedCount() == 1);
        } else {
            assertTrue("Should not get here, illegal state", false);
        }
    }

    /**
   * Object with Identity => Delete => Listener does not vetoe => Deleted.
   */
    public void testIdentityDeleteListenerNoVetoe() {
        doTestIdentityDelete(true, false, ObjectState.Deleted, false);
    }

    /**
   * Object with Identity => Delete => No listener => Must be deleted.
   */
    public void testIdentityDeleteNoListener() {
        doTestIdentityDelete(false, false, ObjectState.Deleted, false);
    }

    /**
   * Object with Identity => Delete => Listener vetoes => Must stay detached.
   */
    public void testIdentityDeleteListenerVetoe() {
        doTestIdentityDelete(true, true, ObjectState.Detached, false);
    }

    /**
   * Object with null Identity => Delete => Listener does not vetoe => Detached.
   */
    public void testNullIdentityDeleteListenerNoVetoe() {
        doTestIdentityDelete(true, false, ObjectState.Detached, true);
    }

    /**
   * Object with null Identity => Delete => No listener => Detached.
   */
    public void testNullIdentityDeleteNoListener() {
        doTestIdentityDelete(false, false, ObjectState.Detached, true);
    }

    /**
   * Object with null Identity => Delete => Listener vetoes => Must stay detached.
   */
    public void testNullIdentityDeleteListenerVetoe() {
        doTestIdentityDelete(true, true, ObjectState.Detached, true);
    }

    /**
   * Internal, called by others change testers.
   * 
   * @param useListener
   * @param vetoeChange
   * @param expectedState
   * @param setIdentityToNull - true: The identity of the customer will be set to null
   */
    public void doTestIdentityDelete(boolean useListener, boolean vetoeChange, ObjectState expectedState, boolean setIdentityToNull) {
        IVOW uow = VOWTest.createVOW();
        IdentityCustomer customer = DatabaseMock.getNewMock().queryFirstIdentityCustomer();
        if (setIdentityToNull) {
            customer.setId(null);
        }
        if (useListener) {
            IVOWListener listener = new VOWTestListener(vetoeChange);
            uow.registerDeleteListener(listener);
        }
        uow.delete(customer);
        if (expectedState == ObjectState.Detached) {
            assertFalse("Customer should be detached", uow.isRegistered(customer));
            assertTrue("Deleted count should be '1' here", uow.deletedCount() == 0);
            assertTrue("Customer must not have any identity listeners", customer.listenerIterator().hasNext() == false);
        } else if (expectedState == ObjectState.Deleted) {
            assertTrue("Customer should be deleted", uow.isDeleted(customer));
            assertTrue("Deleted count should be '1' here", uow.deletedCount() == 1);
        } else {
            assertTrue("Should not get here, illegal state", false);
        }
    }
}
