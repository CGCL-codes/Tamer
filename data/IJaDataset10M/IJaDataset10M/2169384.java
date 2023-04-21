package org.dml.level030;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.dml.error.AssumptionError;
import org.dml.error.BadCallError;
import org.dml.level010.JavaID;
import org.dml.level010.Symbol;
import org.dml.tools.RunTime;
import org.dml.tracking.Factory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.references.method.MethodParams;
import org.references.method.PossibleParams;

/**
 * 
 *
 */
public class Level030_DMLEnvironmentTest {

    Level030_DMLEnvironment l3;

    @Before
    public void setUp() {
        MethodParams params = MethodParams.getNew();
        params.set(PossibleParams.jUnit_wipeDB, true);
        params.set(PossibleParams.jUnit_wipeDBWhenDone, true);
        l3 = Factory.getNewInstanceAndInit(Level030_DMLEnvironment.class, params);
    }

    @After
    public void tearDown() {
        Factory.deInit(l3);
    }

    @Test
    public void testPointer() {
        JavaID name = JavaID.ensureJavaIDFor("Ptr1");
        Symbol name2 = l3.createSymbol(name);
        Pointer p1 = l3.getExistingPointer(name2, true);
        p1.assumedValid();
        assertNull(p1.getPointee());
        Pointer p2 = l3.getNewNullPointer();
        assertNull(p2.getPointee());
        Pointer p1_1 = l3.getExistingPointer(name2, true);
        assertNull(p1_1.getPointee());
        Symbol uni1 = l3.newUniqueSymbol();
        assertNull(p1.pointTo(uni1));
        assertTrue(p1_1.getPointee() == uni1);
        assertTrue(p1.getPointee() == uni1);
        assertTrue(p1_1.pointTo(null) == uni1);
        Symbol pointsTo = l3.newUniqueSymbol();
        Pointer p3 = l3.getNewNonNullPointer(pointsTo);
        Pointer p3_3 = l3.getExistingPointer(p3.getAsSymbol(), false);
        assertTrue(p3 == p3_3);
        assertTrue(p3_3.getPointee() == pointsTo);
        assertTrue(p3.getPointee() == pointsTo);
        boolean threw = false;
        try {
            p3.pointTo(null);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
        Pointer p4 = l3.getNewNullPointer();
        assertTrue(p4 != p3);
        assertTrue(p4.getPointee() == null);
        assertTrue(p4.pointTo(null) == null);
        threw = false;
        try {
            p3.assumedValid();
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertFalse(threw);
        threw = false;
        try {
            p3.getPointee();
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertFalse(threw);
        threw = false;
        try {
            p3.pointTo(pointsTo);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertFalse(threw);
        threw = false;
        try {
            p3.getAsSymbol();
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertFalse(threw);
    }

    @Test
    public void testDomainPointer() {
        Symbol domain = l3.ensureSymbol(JavaID.ensureJavaIDFor("domain"));
        Symbol pointTo = l3.newUniqueSymbol();
        assertFalse(l3.ensureVector(domain, pointTo));
        DomainPointer dp1 = l3.getNewNonNullDomainPointer(domain, pointTo);
        DomainPointer dp1_1 = l3.getExistingDomainPointer(dp1.getAsSymbol(), domain, false);
        boolean must = false;
        try {
            @SuppressWarnings("unused") DomainPointer diffDom = l3.getExistingDomainPointer(dp1.getAsSymbol(), pointTo, false);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                must = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(must);
        must = false;
        try {
            @SuppressWarnings("unused") DomainPointer diffDom = l3.getExistingDomainPointer(dp1.getAsSymbol(), domain, true);
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, BadCallError.class)) {
                must = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(must);
        assertTrue(dp1_1.getPointee() == pointTo);
        assertTrue(dp1_1.getDomain() == domain);
        assertTrue(dp1 == dp1_1);
        DomainPointer dp2 = l3.getNewNullDomainPointer(domain);
        DomainPointer dp2_2 = l3.getExistingDomainPointer(dp2.getAsSymbol(), domain, true);
        assertTrue(dp2.getDomain() == domain);
        assertTrue(dp2_2.getDomain() == dp2.getDomain());
        assertTrue(dp2 == dp2_2);
        boolean threw = false;
        try {
            dp2.setDomain(dp2.getAsSymbol());
        } catch (Throwable t) {
            if (RunTime.isThisWrappedException_of_thisType(t, AssumptionError.class)) {
                threw = true;
                RunTime.clearLastThrown_andAllItsWraps();
            }
        }
        assertTrue(threw);
    }
}
