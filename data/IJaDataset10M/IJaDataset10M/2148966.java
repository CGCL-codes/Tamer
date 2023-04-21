package org.jmol.viewer;

import org.jmol.g3d.Graphics3D;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3i;

class RibbonsRenderer extends MpsRenderer {

    Ribbons strands;

    final Point3f pointT = new Point3f();

    Point3i[] calcScreens(Point3f[] centers, Vector3f[] vectors, short[] mads, float offsetFraction) {
        Point3i[] screens = viewer.allocTempScreens(centers.length);
        if (offsetFraction == 0) {
            for (int i = centers.length; --i >= 0; ) viewer.transformPoint(centers[i], screens[i]);
        } else {
            offsetFraction /= 1000;
            for (int i = centers.length; --i >= 0; ) {
                pointT.set(vectors[i]);
                float scale = mads[i] * offsetFraction;
                pointT.scaleAdd(scale, centers[i]);
                if (Float.isNaN(pointT.x)) {
                    System.out.println(" vectors[" + i + "]=" + vectors[i] + " centers[" + i + "]=" + centers[i] + " mads[" + i + "]=" + mads[i] + " scale=" + scale + " --> " + pointT);
                }
                viewer.transformPoint(pointT, screens[i]);
            }
        }
        return screens;
    }

    boolean isNucleic;

    boolean ribbonBorder = false;

    void renderMpspolymer(Mps.Mpspolymer mpspolymer) {
        Ribbons.Schain strandsChain = (Ribbons.Schain) mpspolymer;
        if (strandsChain.wingVectors != null) {
            ribbonBorder = viewer.getRibbonBorder();
            isNucleic = strandsChain.polymer instanceof NucleicPolymer;
            render1Chain(strandsChain.monomerCount, strandsChain.monomers, strandsChain.leadMidpoints, strandsChain.wingVectors, strandsChain.mads, strandsChain.colixes);
        }
    }

    void render1Chain(int monomerCount, Monomer[] monomers, Point3f[] centers, Vector3f[] vectors, short[] mads, short[] colixes) {
        Point3i[] ribbonTopScreens;
        Point3i[] ribbonBottomScreens;
        ribbonTopScreens = calcScreens(centers, vectors, mads, isNucleic ? 1f : 0.5f);
        ribbonBottomScreens = calcScreens(centers, vectors, mads, isNucleic ? 0f : -0.5f);
        render2Strand(monomerCount, monomers, mads, colixes, ribbonTopScreens, ribbonBottomScreens);
        viewer.freeTempScreens(ribbonTopScreens);
        viewer.freeTempScreens(ribbonBottomScreens);
    }

    void render2Strand(int monomerCount, Monomer[] monomers, short[] mads, short[] colixes, Point3i[] ribbonTopScreens, Point3i[] ribbonBottomScreens) {
        for (int i = monomerCount; --i >= 0; ) if (mads[i] > 0) render2StrandSegment(monomerCount, monomers[i], colixes[i], mads, ribbonTopScreens, ribbonBottomScreens, i);
    }

    void render2StrandSegment(int monomerCount, Monomer monomer, short colix, short[] mads, Point3i[] ribbonTopScreens, Point3i[] ribbonBottomScreens, int i) {
        int iLast = monomerCount;
        int iPrev = i - 1;
        if (iPrev < 0) iPrev = 0;
        int iNext = i + 1;
        if (iNext > iLast) iNext = iLast;
        int iNext2 = i + 2;
        if (iNext2 > iLast) iNext2 = iLast;
        colix = Graphics3D.inheritColix(colix, monomer.getLeadAtom().colixAtom);
        g3d.drawHermite(true, ribbonBorder, colix, isNucleic ? 4 : 7, ribbonTopScreens[iPrev], ribbonTopScreens[i], ribbonTopScreens[iNext], ribbonTopScreens[iNext2], ribbonBottomScreens[iPrev], ribbonBottomScreens[i], ribbonBottomScreens[iNext], ribbonBottomScreens[iNext2]);
    }
}
