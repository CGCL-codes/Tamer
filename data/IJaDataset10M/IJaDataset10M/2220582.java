package gov.sns.xal.model.sync;

import gov.sns.xal.model.elem.IdealMagQuad;
import gov.sns.xal.model.elem.IdealRfGap;
import gov.sns.xal.model.elem.Marker;
import gov.sns.xal.model.scenario.Scenario;
import gov.sns.xal.smf.impl.BPM;
import gov.sns.xal.smf.impl.DTLTank;
import gov.sns.xal.smf.impl.PermQuadrupole;
import gov.sns.xal.smf.impl.Quadrupole;
import gov.sns.xal.smf.impl.RfGap;
import junit.framework.TestCase;

/**
 * Verifies behavior of SynchronizationManager.
 * 
 * @author Craig McChesney
 */
public class SynchronizationManagerTest extends TestCase {

    public void testSynchronize() {
        SynchronizationManager syncManager = new SynchronizationManager();
        Quadrupole quad = new Quadrupole("testQuad");
        IdealMagQuad xalQuad1 = new IdealMagQuad();
        syncManager.synchronize(xalQuad1, quad);
        IdealMagQuad xalQuad2 = new IdealMagQuad();
        syncManager.synchronize(xalQuad2, quad);
        syncManager.synchronize(new Marker("middle"), quad);
        PermQuadrupole permQuad = new PermQuadrupole("permQuad");
        IdealMagQuad xalPermQuad = new IdealMagQuad();
        syncManager.synchronize(xalPermQuad, permQuad);
        DTLTank tank = new DTLTank("testTank");
        RfGap gap = new RfGap("testGap");
        tank.addNode(gap);
        IdealRfGap xalGap = new IdealRfGap();
        syncManager.synchronize(xalGap, gap);
        BPM bpm = new BPM("testBPM");
        Marker xalMarker = new Marker();
        syncManager.synchronize(xalMarker, bpm);
        syncManager.debugPrint();
        syncManager.setSynchronizationMode(Scenario.SYNC_MODE_DESIGN);
        try {
            syncManager.resync();
        } catch (SynchronizationException e) {
            fail("SynchronizationException in design mode");
        }
        syncManager.setSynchronizationMode(Scenario.SYNC_MODE_LIVE);
        try {
            syncManager.resync();
        } catch (SynchronizationException e) {
            System.out.println("yup, live sync failed because there is no CA for these nodes");
        }
    }
}
