package org.fudaa.ctulu.interpolation.profile;

import junit.framework.TestCase;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.gis.GISGeometryFactory;
import org.fudaa.ctulu.gis.GISPoint;

/**
 * Test de l'interpolateur de profil. A partir d'un nuage de points et d'une trace 2D.
 * @author Bertrand Marchand
 * @version $Id: TestJInterpolationProfile.java 6184 2011-03-30 08:57:28Z bmarchan $
 */
public class TestJInterpolationProfile extends TestCase {

    static double[][] ctrace = { { 427238.0, 245099.0, 0 }, { 427229.6, 245019.8, 0 }, { 427223.0, 244852.0, 0 } };

    static double[][] ccloud = { { 427209.348, 245104.277, 25.0 }, { 427269.064, 245095.43, 25.0 }, { 427212.297, 245076.262, 25.0 }, { 427183.545, 245077.736, 25.0 }, { 427292.655, 245077.736, 25.0 }, { 427191.654, 245031.291, 25.0 }, { 427288.969, 245020.232, 25.0 }, { 427174.698, 244956.093, 23.0 }, { 427276.436, 244963.465, 23.0 }, { 427283.071, 244925.129, 23.0 }, { 427173.961, 244913.333, 24.0 }, { 427221.144, 244889.005, 24.0 }, { 427209.348, 244868.362, 24.0 }, { 427257.268, 244880.895, 24.0 }, { 427265.378, 244851.406, 24.0 }, { 427289.706, 244858.041, 24.0 }, { 427157.741, 244851.406, 24.0 }, { 427198.289, 244833.712, 24.0 } };

    static double[][] cwin = { { 427134.887, 244823.391, 0 }, { 427324.356, 244823.391, 0 }, { 427324.356, 245116.81, 0 }, { 427134.887, 245116.81, 0 } };

    /**
   * Lancement du test de calcul de profil.
   * @param args
   */
    public void testInterp() {
        GISPoint[] ptscloud = new GISPoint[ccloud.length];
        for (int i = 0; i < ptscloud.length; i++) {
            ptscloud[i] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(ccloud[i][0], ccloud[i][1], ccloud[i][2]);
        }
        PointCloudPointsAdapter cloud = new PointCloudPointsAdapter(ptscloud);
        GISPoint[] ptswin = new GISPoint[cwin.length];
        for (int i = 0; i < ptswin.length; i++) {
            ptswin[i] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(cwin[i][0], cwin[i][1], cwin[i][2]);
        }
        ProfileCalculatorWindow win = new ProfileCalculatorWindow(ptswin);
        ProfileCalculator pc = new ProfileCalculator();
        pc.setWindow(win);
        pc.setCloud(cloud);
        GISPoint[] trace;
        GISPoint[] prof;
        trace = new GISPoint[ctrace.length];
        for (int i = 0; i < trace.length; i++) {
            trace[i] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(ctrace[i][0], ctrace[i][1], ctrace[i][2]);
        }
        System.out.println("* Trace fixee:");
        for (int i = 0; i < trace.length; i++) {
            System.out.println("Pt " + i + ": X=" + trace[i].getX() + " Y=" + trace[i].getY() + " Z=" + trace[i].getZ());
        }
        pc.setTrace(trace);
        prof = pc.extractProfile(0, new CtuluAnalyze());
        System.out.println("* Profil obtenu:");
        for (int i = 0; i < prof.length; i++) {
            System.out.println("Pt " + i + ": X=" + prof[i].getX() + " Y=" + prof[i].getY() + " Z=" + prof[i].getZ());
            assertTrue(prof[i].getZ() >= 23 - 1.E6 && prof[i].getZ() <= 25 + 1.0E6);
        }
        System.out.println("* Trace calcul�e:");
        pc.computeDefaultTrace();
        trace = pc.getTrace();
        for (int i = 0; i < trace.length; i++) {
            System.out.println("Pt " + i + ": X=" + trace[i].getX() + " Y=" + trace[i].getY() + " Z=" + trace[i].getZ());
        }
        prof = pc.extractProfile(1, new CtuluAnalyze());
        System.out.println("* Profil obtenu:");
        for (int i = 0; i < prof.length; i++) {
            System.out.println("Pt " + i + ": X=" + prof[i].getX() + " Y=" + prof[i].getY() + " Z=" + prof[i].getZ());
            assertTrue(prof[i].getZ() >= 23 - 1.E6 && prof[i].getZ() <= 25 + 1.0E6);
        }
    }
}
