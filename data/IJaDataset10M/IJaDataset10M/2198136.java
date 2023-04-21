package org.jmol.viewer;

import org.jmol.util.Bmp;
import org.jmol.g3d.Graphics3D;
import javax.vecmath.*;

class SasurfaceRenderer extends ShapeRenderer {

    boolean perspectiveDepth;

    boolean hideSaddles;

    boolean hideConvex;

    boolean hideSeams;

    boolean showEdgeNumbers;

    int scalePixelsPerAngstrom;

    boolean bondSelectionModeOr;

    int geodesicVertexCount;

    int geodesicFaceCount;

    short[] geodesicFaceVertexes;

    short[] geodesicFaceNormixes;

    short[] geodesicNeighborVertexes;

    SasCache sasCache;

    float radiusP;

    Vector3f[] transformedProbeVertexes;

    Point3i[] probeScreens;

    int maxVertexCount;

    short formalChargeColixWhite;

    int[] bmpEdge;

    void initRenderer() {
        maxVertexCount = g3d.getGeodesicVertexCount(Sasurface.MAX_GEODESIC_RENDERING_LEVEL);
        sasCache = new SasCache(viewer, 6, maxVertexCount);
        formalChargeColixWhite = g3d.getChangableColix(JmolConstants.FORMAL_CHARGE_COLIX_WHITE, JmolConstants.argbsFormalCharge[JmolConstants.FORMAL_CHARGE_INDEX_WHITE]);
        System.out.println(" formalChargeColixWhite=" + Integer.toHexString(formalChargeColixWhite));
        for (int i = JmolConstants.argbsFormalCharge.length; --i >= 0; ) g3d.getChangableColix((short) (JmolConstants.FORMAL_CHARGE_COLIX_RED + i), JmolConstants.argbsFormalCharge[i]);
    }

    void render() {
        perspectiveDepth = viewer.getPerspectiveDepth();
        scalePixelsPerAngstrom = (int) viewer.getScalePixelsPerAngstrom();
        bondSelectionModeOr = viewer.getBondSelectionModeOr();
        Sasurface sasurface = (Sasurface) shape;
        if (sasurface == null) return;
        int surfaceCount = sasurface.surfaceCount;
        if (surfaceCount == 0) return;
        Sasurface1[] surfaces = sasurface.surfaces;
        hideConvex = viewer.getTestFlag1();
        hideSaddles = viewer.getTestFlag2();
        hideSeams = viewer.getTestFlag3();
        showEdgeNumbers = viewer.getTestFlag4();
        if (showEdgeNumbers) g3d.setFontOfSize(12);
        sasCache.clear();
        for (int i = surfaceCount; --i >= 0; ) renderSasurface1(surfaces[i]);
    }

    int[] atomsToRender;

    void renderSasurface1(Sasurface1 surface) {
        if (surface.hide) return;
        int renderingLevel = surface.geodesicRenderingLevel;
        radiusP = surface.radiusP;
        geodesicVertexCount = surface.geodesicVertexCount;
        geodesicFaceCount = g3d.getGeodesicFaceCount(renderingLevel);
        geodesicFaceVertexes = g3d.getGeodesicFaceVertexes(renderingLevel);
        geodesicFaceNormixes = g3d.getGeodesicFaceNormixes(renderingLevel);
        geodesicNeighborVertexes = g3d.getGeodesicNeighborVertexes(renderingLevel);
        if (transformedProbeVertexes == null || transformedProbeVertexes.length < geodesicVertexCount) allocTransformedProbeVertexes();
        Atom[] atoms = frame.atoms;
        int[][] convexVertexMaps = surface.convexVertexMaps;
        int[][] convexFaceMaps = surface.convexFaceMaps;
        short[] colixesConvex = surface.colixesConvex;
        int displayModelIndex = this.displayModelIndex;
        int convexCount = surface.surfaceConvexMax;
        if (bmpEdge == null) bmpEdge = Bmp.allocateBitmap(geodesicVertexCount); else bmpEdge = Bmp.growBitmap(bmpEdge, geodesicVertexCount);
        int[] atomsToRender = this.atomsToRender = Bmp.growBitmap(this.atomsToRender, convexCount);
        Bmp.setAllBits(atomsToRender, convexCount);
        Sasurface1.Torus[] toruses = surface.toruses;
        for (int i = surface.torusCount; --i >= 0; ) {
            Sasurface1.Torus torus = toruses[i];
            renderTorus(torus, atoms, colixesConvex, convexVertexMaps);
            renderSeams(torus, atoms, colixesConvex, convexVertexMaps);
            int ixA = torus.ixA;
            if (Bmp.getBit(atomsToRender, ixA)) {
                Bmp.clearBit(atomsToRender, ixA);
                int[] vertexMap = convexVertexMaps[ixA];
                if (vertexMap != null) renderConvex(surface, atoms[ixA], colixesConvex[ixA], vertexMap, convexFaceMaps[ixA]);
            }
            int ixB = torus.ixB;
            if (Bmp.getBit(atomsToRender, ixB)) {
                Bmp.clearBit(atomsToRender, ixB);
                int[] vertexMap = convexVertexMaps[ixB];
                if (vertexMap != null) renderConvex(surface, atoms[ixB], colixesConvex[ixB], vertexMap, convexFaceMaps[ixB]);
            }
        }
        for (int i = -1; (i = Bmp.nextSetBit(atomsToRender, i + 1)) >= 0; ) {
            int[] vertexMap = convexVertexMaps[i];
            if (vertexMap != null) {
                int[] faceMap = convexFaceMaps[i];
                Atom atom = atoms[i];
                if (displayModelIndex < 0 || displayModelIndex == atom.modelIndex) renderConvex(surface, atom, colixesConvex[i], vertexMap, faceMap);
            }
        }
    }

    void allocTransformedProbeVertexes() {
        transformedProbeVertexes = new Vector3f[geodesicVertexCount];
        probeScreens = new Point3i[geodesicVertexCount];
        Vector3f[] transformedGeodesicVertexes = viewer.g3d.getTransformedVertexVectors();
        for (int i = geodesicVertexCount; --i >= 0; ) {
            transformedProbeVertexes[i] = new Vector3f();
            transformedProbeVertexes[i].scale(radiusP, transformedGeodesicVertexes[i]);
            probeScreens[i] = new Point3i();
        }
    }

    void renderConvex(Sasurface1 surface, Atom atom, short colix, int[] vertexMap, int[] faceMap) {
        if (hideConvex) return;
        if (false) {
            short madAtom = (short) (atom.getVanderwaalsMar() * 2);
            int diameter = viewer.scaleToScreen(atom.screenZ, madAtom);
            g3d.fillSphereCentered(atom.colixAtom, diameter, atom.screenX, atom.screenY, atom.screenZ);
            return;
        }
        Point3i[] screens = sasCache.lookupAtomScreens(atom, vertexMap);
        colix = Graphics3D.inheritColix(colix, atom.colixAtom);
        for (int i = -1; (i = Bmp.nextSetBit(faceMap, i + 1)) >= 0; ) {
            int j = 3 * i;
            short vA = geodesicFaceVertexes[j];
            short vB = geodesicFaceVertexes[j + 1];
            short vC = geodesicFaceVertexes[j + 2];
            g3d.fillTriangle(colix, screens[vA], vA, screens[vB], vB, screens[vC], vC);
        }
        if (showEdgeNumbers) renderGeodesicEdgeNumbers(atom, vertexMap);
    }

    void renderGeodesicEdgeNumbers(Atom atom, int[] vertexMap) {
        Point3i[] screens = sasCache.lookupAtomScreens(atom, vertexMap);
        findActualEdge(vertexMap, bmpEdge);
        for (int v = -1; (v = Bmp.nextSetBit(bmpEdge, v + 1)) >= 0; ) {
            g3d.drawString("" + v, Graphics3D.CYAN, screens[v].x, screens[v].y, screens[v].z - 20);
        }
    }

    boolean findActualEdge(int[] visibleVertexMap, int[] actualEdgeMap) {
        int edgeVertexCount = 0;
        Bmp.clearBitmap(actualEdgeMap);
        for (int v = -1; (v = Bmp.nextSetBit(visibleVertexMap, v + 1)) >= 0; ) {
            int neighborsOffset = v * 6;
            for (int j = (v < Graphics3D.GEODESIC_START_VERTEX_COUNT) ? Graphics3D.GEODESIC_START_NEIGHBOR_COUNT : 6; --j >= 0; ) {
                int neighbor = geodesicNeighborVertexes[neighborsOffset + j];
                if (!Bmp.getBit(visibleVertexMap, neighbor)) {
                    Bmp.setBit(actualEdgeMap, v);
                    ++edgeVertexCount;
                    break;
                }
            }
        }
        return edgeVertexCount > 0;
    }

    void renderVertexDots(Atom atom, int[] vertexMap) {
        Point3i[] screens = sasCache.lookupAtomScreens(atom, vertexMap);
        for (int v = -1; (v = Bmp.nextSetBit(vertexMap, v + 1)) >= 0; ) g3d.fillSphereCentered(Graphics3D.LIME, 5, screens[v]);
    }

    void renderTorus(Sasurface1.Torus torus, Atom[] atoms, short[] convexColixes, int[][] convexVertexMaps) {
        if (hideSaddles) return;
        prepareTorusColixes(torus, convexColixes, atoms);
        renderTorus(torus, torusColixes);
        if (showEdgeNumbers) renderTorusEdgeNumbers(torus);
    }

    void renderTorusEdgeNumbers(Sasurface1.Torus torus) {
        Point3i[] screens = sasCache.lookupTorusScreens(torus);
        int outerPointCount = torus.outerPointCount;
        int totalPointCount = torus.totalPointCount;
        for (int i = 0; i < totalPointCount; i += outerPointCount) {
            g3d.drawString("" + i, Graphics3D.RED, screens[i].x, screens[i].y, screens[i].z - 20);
            int j = i + outerPointCount - 1;
            g3d.drawString("" + j, Graphics3D.MAGENTA, screens[j].x, screens[j].y, screens[j].z - 20);
        }
    }

    void renderSeams(Sasurface1.Torus torus, Atom[] atoms, short[] convexColixes, int[][] convexVertexMaps) {
        if (true) return;
        if (hideSeams) return;
        Point3i[] torusScreens = sasCache.lookupTorusScreens(torus);
        short[] torusNormixes = torus.normixes;
        int ixA = torus.ixA;
        Atom atomA = atoms[ixA];
        short colixA = Graphics3D.inheritColix(convexColixes[ixA], atomA.colixAtom);
        renderSeam(torusScreens, torusNormixes, sasCache.lookupAtomScreens(atomA, convexVertexMaps[ixA]), colixA, torus.seamA);
        int ixB = torus.ixB;
        Atom atomB = atoms[ixB];
        short colixB = Graphics3D.inheritColix(convexColixes[ixB], atomB.colixAtom);
        renderSeam(torusScreens, torusNormixes, sasCache.lookupAtomScreens(atomB, convexVertexMaps[ixB]), colixB, torus.seamB);
    }

    void renderSeam(Point3i[] torusScreens, short[] torusNormixes, Point3i[] geodesicScreens, short colix, short[] seam) {
        if (seam == null) return;
        boolean breakSeam = true;
        short prevTorus = -1;
        short prevGeodesic = -1;
        for (int i = 0; i < seam.length; ++i) {
            if (breakSeam) {
                prevTorus = seam[i++];
                prevGeodesic = (short) ~seam[i];
                breakSeam = false;
                continue;
            }
            short v = seam[i];
            if (v >= 0) {
                g3d.fillTriangle(colix, torusScreens[prevTorus], torusNormixes[prevTorus], torusScreens[v], torusNormixes[v], geodesicScreens[prevGeodesic], prevGeodesic);
                prevTorus = v;
            } else if (v == Short.MIN_VALUE) {
                breakSeam = true;
            } else {
                v = (short) ~v;
                g3d.fillTriangle(colix, torusScreens[prevTorus], torusNormixes[prevTorus], geodesicScreens[v], v, geodesicScreens[prevGeodesic], prevGeodesic);
                prevGeodesic = v;
            }
        }
        if (showEdgeNumbers) {
            renderSeamEdgeNumbers(torusScreens, geodesicScreens, seam);
        }
    }

    void renderSeamEdgeNumbers(Point3i[] torusScreens, Point3i[] geodesicScreens, short[] seam) {
        if (seam == null) return;
        boolean breakSeam = true;
        short prevTorus = -1;
        short prevGeodesic = -1;
        for (int i = 0; i < seam.length; ++i) {
            if (breakSeam) {
                prevTorus = seam[i++];
                g3d.drawString("" + prevTorus, Graphics3D.WHITE, torusScreens[prevTorus].x, torusScreens[prevTorus].y, torusScreens[prevTorus].z - 21);
                prevGeodesic = (short) ~seam[i];
                g3d.drawString("" + prevGeodesic, Graphics3D.WHITE, geodesicScreens[prevGeodesic].x, geodesicScreens[prevGeodesic].y, geodesicScreens[prevGeodesic].z - 21);
                breakSeam = false;
                continue;
            }
            short v = seam[i];
            if (v >= 0) {
                g3d.drawString("" + v, Graphics3D.WHITE, torusScreens[v].x, torusScreens[v].y, torusScreens[v].z - 21);
                prevTorus = v;
            } else if (v == Short.MIN_VALUE) {
                breakSeam = true;
            } else {
                v = (short) ~v;
                g3d.drawString("" + v, Graphics3D.WHITE, geodesicScreens[v].x, geodesicScreens[v].y, geodesicScreens[v].z - 21);
                prevGeodesic = v;
            }
        }
    }

    void dumpSeam(short[] seam) {
        System.out.println("dumpSeam:");
        for (int i = 0; i < seam.length; ++i) {
            short v = seam[i];
            System.out.print("  " + v + " ");
            if (v == Short.MIN_VALUE) System.out.println(" -- break"); else if (v < 0) System.out.println("(" + ~v + ")"); else System.out.println("");
        }
    }

    void renderEdgeBalls(Atom atom, int[] edgeVertexes) {
        Point3i[] screens = sasCache.lookupAtomScreens(atom, edgeVertexes);
        for (int v = -1; (v = Bmp.nextSetBit(edgeVertexes, v + 1)) >= 0; ) {
            g3d.fillSphereCentered(Graphics3D.BLUE, 10, screens[v]);
            g3d.drawString("" + v, Graphics3D.BLUE, screens[v].x + 10, screens[v].y + 10, screens[v].z - 10);
        }
    }

    short getColix(short colix, short[] colixes, Atom[] atoms, int index) {
        return Graphics3D.inheritColix(colix, atoms[index].colixAtom);
    }

    int getTorusOuterDotCount() {
        return 32;
    }

    int getTorusIncrement() {
        return 1;
    }

    static final int OUTER_TORUS_STEP_COUNT = Sasurface.OUTER_TORUS_STEP_COUNT;

    final AxisAngle4f aaT = new AxisAngle4f();

    final AxisAngle4f aaT1 = new AxisAngle4f();

    final Matrix3f matrixT = new Matrix3f();

    final Matrix3f matrixT1 = new Matrix3f();

    final Point3f pointT = new Point3f();

    final Point3f pointT1 = new Point3f();

    void renderTorus(Sasurface1.Torus torus, short[] colixes) {
        Point3i[] screens = sasCache.lookupTorusScreens(torus);
        short[] normixes = torus.normixes;
        int outerPointCount = torus.outerPointCount;
        Sasurface1.TorusCavity[] torusCavities = torus.torusCavities;
        int torusCavityIndex = 0;
        int ixP = 0;
        int torusSegmentCount = torus.torusSegmentCount;
        for (int i = 0; i < torusSegmentCount; ++i) {
            if (torusCavities != null) renderTorusCavityTriangle(screens, normixes, ixP, outerPointCount, colixes, torusCavities[torusCavityIndex++]);
            int stepCount = torus.torusSegments[i].stepCount;
            int ixQ = ixP + outerPointCount;
            for (int j = stepCount; --j > 0; ) {
                ++ixP;
                ++ixQ;
                for (int k = 1; k < outerPointCount; ++k) {
                    g3d.fillQuadrilateral(screens[ixP - 1], colixes[k - 1], normixes[ixP - 1], screens[ixP], colixes[k], normixes[ixP], screens[ixQ], colixes[k], normixes[ixQ], screens[ixQ - 1], colixes[k - 1], normixes[ixQ - 1]);
                    ++ixP;
                    ++ixQ;
                }
            }
            if (torusCavities != null) renderTorusCavityTriangle(screens, normixes, ixP, outerPointCount, colixes, torusCavities[torusCavityIndex++]);
            ixP = ixQ;
        }
    }

    final Point3i screenCavityBottom = new Point3i();

    void renderTorusCavityTriangle(Point3i[] torusScreens, short[] torusNormixes, int torusIndex, int torusPointCount, short[] colixes, Sasurface1.TorusCavity torusCavity) {
        SasCavity cavity = torusCavity.cavity;
        viewer.transformPoint(cavity.pointBottom, screenCavityBottom);
        short normixCavityBottom = cavity.normixBottom;
        Point3i torusScreenLast = torusScreens[torusIndex];
        short torusNormixLast = torusNormixes[torusIndex];
        short colixLast = colixes[0];
        ++torusIndex;
        for (int i = 1; i < torusPointCount; ++i) {
            Point3i torusScreen = torusScreens[torusIndex];
            short torusNormix = torusNormixes[torusIndex];
            ++torusIndex;
            short colix = colixes[i];
            short colixBottom = colix;
            if (colix != colixLast) colixBottom = g3d.getColixMix(colix, colixLast);
            g3d.fillTriangle(torusScreenLast, colixLast, torusNormixLast, torusScreen, colix, torusNormix, screenCavityBottom, colixBottom, normixCavityBottom);
            torusScreenLast = torusScreen;
            torusNormixLast = torusNormix;
            colixLast = colix;
        }
    }

    final short[] torusColixes = new short[OUTER_TORUS_STEP_COUNT];

    void prepareTorusColixes(Sasurface1.Torus torus, short[] convexColixes, Atom[] atoms) {
        int ixA = torus.ixA;
        int ixB = torus.ixB;
        int outerPointCount = torus.outerPointCount;
        short colixA = Graphics3D.inheritColix(torus.colixA, convexColixes[ixA], atoms[ixA].colixAtom);
        short colixB = Graphics3D.inheritColix(torus.colixB, convexColixes[ixB], atoms[ixB].colixAtom);
        if (colixA == colixB) {
            for (int i = outerPointCount; --i >= 0; ) torusColixes[i] = colixA;
            return;
        }
        if (colixA < 0 && colixB < 0) {
            short unmaskedA = Graphics3D.getChangableColixIndex(colixA);
            short unmaskedB = Graphics3D.getChangableColixIndex(colixB);
            if (unmaskedA >= JmolConstants.FORMAL_CHARGE_COLIX_RED && unmaskedA <= JmolConstants.FORMAL_CHARGE_COLIX_BLUE && unmaskedB >= JmolConstants.FORMAL_CHARGE_COLIX_RED && unmaskedB <= JmolConstants.FORMAL_CHARGE_COLIX_BLUE) {
                prepareFormalChargeTorusColixes(colixA, unmaskedA, colixB, unmaskedB, outerPointCount);
                return;
            }
        }
        int halfRoundedUp = (outerPointCount + 1) / 2;
        torusColixes[outerPointCount / 2] = colixA;
        for (int i = outerPointCount / 2; --i >= 0; ) {
            torusColixes[i] = colixA;
            torusColixes[i + halfRoundedUp] = colixB;
        }
    }

    void prepareFormalChargeTorusColixes(short colixA, short unmaskedA, short colixB, short unmaskedB, int outerPointCount) {
        int delta = unmaskedB - unmaskedA;
        int denominator = outerPointCount - 1;
        boolean crossesZero = ((unmaskedA > JmolConstants.FORMAL_CHARGE_COLIX_WHITE && unmaskedB < JmolConstants.FORMAL_CHARGE_COLIX_WHITE) || (unmaskedA < JmolConstants.FORMAL_CHARGE_COLIX_WHITE && unmaskedB > JmolConstants.FORMAL_CHARGE_COLIX_WHITE));
        if (!crossesZero) {
            for (int i = outerPointCount; --i >= 0; ) {
                torusColixes[i] = (short) (colixA + (i * delta / denominator));
            }
            return;
        }
        final short whiteColix = JmolConstants.FORMAL_CHARGE_COLIX_WHITE;
        for (int i = outerPointCount; --i >= 0; ) torusColixes[i] = Graphics3D.GREEN;
        int deltaA = whiteColix - unmaskedA;
        int indexWhiteA = (deltaA * outerPointCount / delta) - 1;
        torusColixes[indexWhiteA] = (short) (whiteColix | (colixA & 0xC000));
        for (int i = indexWhiteA; --i >= 0; ) torusColixes[i] = (short) (colixA + (i * deltaA / indexWhiteA));
        int indexWhiteB = indexWhiteA + 1;
        if (indexWhiteB < outerPointCount) {
            int deltaB = unmaskedB - whiteColix;
            int whiteB = whiteColix | (colixB & 0xC000);
            int denomB = outerPointCount - indexWhiteB - 1;
            torusColixes[indexWhiteB] = (short) whiteB;
            for (int i = outerPointCount; --i > indexWhiteB; ) torusColixes[i] = (short) (whiteB + ((i - indexWhiteB) * deltaB / denomB));
        }
    }
}
