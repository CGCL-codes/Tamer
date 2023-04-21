package org.jmol.viewer;

import org.jmol.g3d.Graphics3D;
import javax.vecmath.*;
import java.util.BitSet;

class SasNeighborFinder {

    final Frame frame;

    final Sasurface1 sas1;

    final Graphics3D g3d;

    final Atom[] atoms;

    private static final boolean LOG = false;

    SasNeighborFinder(Frame frame, Sasurface1 sas1, Graphics3D g3d) {
        this.sas1 = sas1;
        this.frame = frame;
        this.g3d = g3d;
        atoms = frame.atoms;
        maxVanderwaalsRadius = frame.getMaxVanderwaalsRadius();
    }

    float radiusP, diameterP;

    float maxVanderwaalsRadius;

    void setProbeRadius(float radiusP) {
        if (radiusP <= 0) throw new NullPointerException();
        this.radiusP = radiusP;
        diameterP = 2 * radiusP;
    }

    void findAbuttingNeighbors(int atomIndex, BitSet bsSelected) {
        setAtomI(atomIndex);
        getNeighbors(bsSelected);
        sortNeighborIndexes();
        calcCavitiesI();
    }

    int indexI, indexJ, indexK;

    Atom atomI, atomJ, atomK;

    Point3f centerI, centerJ, centerK;

    float radiusI, radiusJ, radiusK;

    float radiiIP, radiiJP, radiiKP;

    float radiiIP2, radiiJP2, radiiKP2;

    float distanceIJ, distanceIK, distanceJK;

    float distanceIJ2, distanceIK2, distanceJK2;

    int neighborCount;

    Atom[] neighborAtoms = new Atom[16];

    int[] neighborIndexes = new int[16];

    Point3f[] neighborCenters = new Point3f[16];

    float[] neighborPlusProbeRadii = new float[16];

    float[] neighborPlusProbeRadii2 = new float[16];

    int[] sortedNeighborIndexes = new int[16];

    void setAtomI(int indexI) {
        if (LOG) System.out.println("setAtomI:" + indexI);
        this.indexI = indexI;
        atomI = atoms[indexI];
        centerI = atomI.point3f;
        radiusI = atomI.getVanderwaalsRadiusFloat();
        radiiIP = radiusI + radiusP;
        radiiIP2 = radiiIP * radiiIP;
    }

    void setNeighborJ(int sortedNeighborIndex) {
        indexJ = neighborIndexes[sortedNeighborIndex];
        if (LOG) System.out.println(" setNeighborJ:" + indexJ);
        atomJ = neighborAtoms[sortedNeighborIndex];
        radiusJ = atomJ.getVanderwaalsRadiusFloat();
        radiiJP = neighborPlusProbeRadii[sortedNeighborIndex];
        radiiJP2 = neighborPlusProbeRadii2[sortedNeighborIndex];
        centerJ = neighborCenters[sortedNeighborIndex];
        distanceIJ2 = centerJ.distanceSquared(centerI);
        distanceIJ = (float) Math.sqrt(distanceIJ2);
    }

    void setNeighborK(int sortedNeighborIndex) {
        indexK = neighborIndexes[sortedNeighborIndex];
        if (LOG) System.out.println("  setNeighborK:" + indexK);
        atomK = neighborAtoms[sortedNeighborIndex];
        radiusK = atomK.getVanderwaalsRadiusFloat();
        radiiKP = neighborPlusProbeRadii[sortedNeighborIndex];
        radiiKP2 = neighborPlusProbeRadii2[sortedNeighborIndex];
        centerK = neighborCenters[sortedNeighborIndex];
        distanceIK2 = centerK.distanceSquared(centerI);
        distanceIK = (float) Math.sqrt(distanceIK2);
        distanceJK2 = centerK.distanceSquared(centerJ);
        distanceJK = (float) Math.sqrt(distanceJK2);
    }

    void getNeighbors(BitSet bsSelected) {
        if (LOG) System.out.println("Surface.getNeighbors radiusI=" + radiusI + " diameterP=" + diameterP + " maxVdw=" + maxVanderwaalsRadius);
        neighborCount = 0;
        AtomIterator iter = frame.getWithinModelIterator(atomI, radiusI + diameterP + maxVanderwaalsRadius);
        while (iter.hasNext()) {
            Atom neighbor = iter.next();
            if (neighbor == atomI) continue;
            if (!bsSelected.get(neighbor.atomIndex)) continue;
            float neighborRadius = neighbor.getVanderwaalsRadiusFloat();
            if (centerI.distance(neighbor.point3f) > radiusI + radiusP + radiusP + neighborRadius) continue;
            if (neighborCount == neighborAtoms.length) {
                neighborAtoms = (Atom[]) Util.doubleLength(neighborAtoms);
                neighborIndexes = Util.doubleLength(neighborIndexes);
                neighborCenters = (Point3f[]) Util.doubleLength(neighborCenters);
                neighborPlusProbeRadii = Util.doubleLength(neighborPlusProbeRadii);
                neighborPlusProbeRadii2 = Util.doubleLength(neighborPlusProbeRadii2);
            }
            neighborAtoms[neighborCount] = neighbor;
            neighborCenters[neighborCount] = neighbor.point3f;
            neighborIndexes[neighborCount] = neighbor.atomIndex;
            float radii = neighborRadius + radiusP;
            neighborPlusProbeRadii[neighborCount] = radii;
            neighborPlusProbeRadii2[neighborCount] = radii * radii;
            ++neighborCount;
        }
        if (LOG) {
            System.out.println("neighborsFound=" + neighborCount);
            System.out.println("radiusI=" + radiusI + " maxVdwRadius=" + maxVanderwaalsRadius + " distMax=" + (radiusI + maxVanderwaalsRadius));
        }
    }

    void sortNeighborIndexes() {
        sortedNeighborIndexes = Util.ensureLength(sortedNeighborIndexes, neighborCount);
        for (int i = neighborCount; --i >= 0; ) sortedNeighborIndexes[i] = i;
        for (int i = neighborCount; --i >= 0; ) for (int j = i; --j >= 0; ) if (neighborIndexes[sortedNeighborIndexes[i]] > neighborIndexes[sortedNeighborIndexes[j]]) {
            int t = sortedNeighborIndexes[i];
            sortedNeighborIndexes[i] = sortedNeighborIndexes[j];
            sortedNeighborIndexes[j] = t;
        }
    }

    private final Vector3f vectorIJ = new Vector3f();

    private final Vector3f vectorIK = new Vector3f();

    private final Vector3f normalIJK = new Vector3f();

    private final Point3f torusCenterIJ = new Point3f();

    private final Point3f torusCenterIK = new Point3f();

    private final Point3f torusCenterJK = new Point3f();

    private final Point3f probeBaseIJK = new Point3f();

    private final Point3f probeCenter = new Point3f();

    private final Vector3f vectorPI = new Vector3f();

    private final Vector3f vectorPJ = new Vector3f();

    private final Vector3f vectorPK = new Vector3f();

    void calcCavitiesI() {
        if (radiusP == 0) return;
        for (int iJ = neighborCount; --iJ >= 0; ) {
            int sortedIndexJ = sortedNeighborIndexes[iJ];
            if (neighborIndexes[sortedIndexJ] <= indexI) continue;
            setNeighborJ(sortedIndexJ);
            if (distanceIJ < 0.2) continue;
            if (radiusI + distanceIJ < radiusJ || radiusJ + distanceIJ < radiusI) {
                System.out.println("embedded atom:" + indexI + "<->" + indexJ);
                continue;
            }
            vectorIJ.sub(centerJ, centerI);
            calcTorusCenter(centerI, radiiIP2, centerJ, radiiJP2, distanceIJ2, torusCenterIJ);
            for (int iK = neighborCount; --iK >= 0; ) {
                int sortedIndexK = sortedNeighborIndexes[iK];
                if (neighborIndexes[sortedIndexK] <= indexJ) continue;
                setNeighborK(sortedIndexK);
                if (distanceIK < 0.1 || distanceJK < 0.1) continue;
                if (distanceJK >= radiiJP + radiiKP) continue;
                getCavitiesIJK();
            }
            checkFullTorusIJ();
        }
        if (neighborCount == 0) sas1.allocateConvexVertexBitmap(indexI);
    }

    void calcTorusCenter(Point3f centerA, float radiiAP2, Point3f centerB, float radiiBP2, float distanceAB2, Point3f torusCenter) {
        torusCenter.sub(centerB, centerA);
        torusCenter.scale((radiiAP2 - radiiBP2) / distanceAB2);
        torusCenter.add(centerA);
        torusCenter.add(centerB);
        torusCenter.scale(0.5f);
    }

    boolean checkProbeNotIJ(Point3f probeCenter) {
        for (int i = neighborCount; --i >= 0; ) {
            int neighborIndex = neighborIndexes[i];
            if (neighborIndex == indexI || neighborIndex == indexJ) continue;
            if (probeCenter.distanceSquared(neighborCenters[i]) < neighborPlusProbeRadii2[i]) return false;
        }
        return true;
    }

    boolean checkProbeAgainstNeighborsIJK(Point3f probeCenter) {
        for (int i = neighborCount; --i >= 0; ) {
            int neighborIndex = neighborIndexes[i];
            if (neighborIndex == indexI || neighborIndex == indexJ || neighborIndex == indexK) continue;
            if (probeCenter.distanceSquared(neighborCenters[i]) < neighborPlusProbeRadii2[i]) return false;
        }
        return true;
    }

    final Vector3f vectorT = new Vector3f();

    final Vector3f v2v3 = new Vector3f();

    final Vector3f v3v1 = new Vector3f();

    final Vector3f v1v2 = new Vector3f();

    boolean intersectPlanes(Vector3f v1, Point3f p1, Vector3f v2, Point3f p2, Vector3f v3, Point3f p3, Point3f intersection) {
        v2v3.cross(v2, v3);
        if (Float.isNaN(v2v3.x)) return false;
        v3v1.cross(v3, v1);
        if (Float.isNaN(v3v1.x)) return false;
        v1v2.cross(v1, v2);
        if (Float.isNaN(v1v2.x)) return false;
        float denominator = v1.dot(v2v3);
        if (denominator == 0) return false;
        vectorT.set(p1);
        intersection.scale(v1.dot(vectorT), v2v3);
        vectorT.set(p2);
        intersection.scaleAdd(v2.dot(vectorT), v3v1, intersection);
        vectorT.set(p3);
        intersection.scaleAdd(v3.dot(vectorT), v1v2, intersection);
        intersection.scale(1 / denominator);
        if (Float.isNaN(intersection.x)) return false;
        return true;
    }

    float calcProbeHeightIJK(Point3f probeBaseIJK) {
        float hypotenuse2 = radiiIP2;
        vectorT.sub(probeBaseIJK, centerI);
        float baseLength2 = vectorT.lengthSquared();
        float height2 = hypotenuse2 - baseLength2;
        if (height2 <= 0) return 0;
        return (float) Math.sqrt(height2);
    }

    void getCavitiesIJK() {
        if (LOG) System.out.println("getCavitiesIJK:" + indexI + "," + indexJ + "," + indexK);
        vectorIK.sub(centerK, centerI);
        normalIJK.cross(vectorIJ, vectorIK);
        if (Float.isNaN(normalIJK.x)) return;
        normalIJK.normalize();
        calcTorusCenter(centerI, radiiIP2, centerK, radiiKP2, distanceIK2, torusCenterIK);
        if (!intersectPlanes(vectorIJ, torusCenterIJ, vectorIK, torusCenterIK, normalIJK, centerI, probeBaseIJK)) return;
        float probeHeight = calcProbeHeightIJK(probeBaseIJK);
        if (probeHeight <= 0) return;
        Sasurface1.Torus torusIJ = null, torusIK = null, torusJK = null;
        for (int i = -1; i <= 1; i += 2) {
            probeCenter.scaleAdd(i * probeHeight, normalIJK, probeBaseIJK);
            if (checkProbeAgainstNeighborsIJK(probeCenter)) {
                SasCavity cavity = new SasCavity(centerI, centerJ, centerK, probeCenter, radiusP, probeBaseIJK, vectorPI, vectorPJ, vectorPK, vectorT, g3d);
                sas1.addCavity(indexI, indexJ, indexK, cavity);
                boolean rightHanded = (i == 1);
                if (LOG) System.out.println(" indexI=" + indexI + " indexJ=" + indexJ + " indexK=" + indexK);
                if (torusIJ == null && (torusIJ = sas1.getTorus(indexI, indexJ)) == null) torusIJ = sas1.createTorus(indexI, indexJ, torusCenterIJ, calcTorusRadius(radiusI, radiusJ, distanceIJ2), false);
                if (torusIJ != null) torusIJ.addCavity(cavity, rightHanded);
                if (torusIK == null && (torusIK = sas1.getTorus(indexI, indexK)) == null) torusIK = sas1.createTorus(indexI, indexK, torusCenterIK, calcTorusRadius(radiusI, radiusK, distanceIK2), false);
                if (torusIK != null) torusIK.addCavity(cavity, !rightHanded);
                if (torusJK == null && (torusJK = sas1.getTorus(indexJ, indexK)) == null) {
                    calcTorusCenter(centerJ, radiiJP2, centerK, radiiKP2, distanceJK2, torusCenterJK);
                    torusJK = sas1.createTorus(indexJ, indexK, torusCenterJK, calcTorusRadius(radiusJ, radiusK, distanceJK2), false);
                }
                if (torusJK != null) torusJK.addCavity(cavity, rightHanded);
            }
        }
    }

    float calcTorusRadius(float radiusA, float radiusB, float distanceAB2) {
        float t1 = radiusA + radiusB + diameterP;
        float t2 = t1 * t1 - distanceAB2;
        float diff = radiusA - radiusB;
        float t3 = distanceAB2 - diff * diff;
        if (t2 <= 0 || t3 <= 0 || distanceAB2 == 0) {
            System.out.println("calcTorusRadius\n" + " radiusA=" + radiusA + " radiusB=" + radiusB + " distanceAB2=" + distanceAB2);
            System.out.println("distanceAB=" + Math.sqrt(distanceAB2) + " t1=" + t1 + " t2=" + t2 + " diff=" + diff + " t3=" + t3);
            throw new NullPointerException();
        }
        return (float) (0.5 * Math.sqrt(t2) * Math.sqrt(t3) / Math.sqrt(distanceAB2));
    }

    final Vector3f unitRadialVectorT = new Vector3f();

    static final Vector3f vectorZ = new Vector3f(0, 0, 1);

    final Point3f pointT = new Point3f();

    void checkFullTorusIJ() {
        if (sas1.getTorus(indexI, indexJ) == null) {
            if (vectorIJ.z == 0) unitRadialVectorT.set(vectorZ); else {
                unitRadialVectorT.set(-vectorIJ.y, vectorIJ.x, 0);
                unitRadialVectorT.normalize();
            }
            float torusRadiusIJ = calcTorusRadius(radiusI, radiusJ, distanceIJ2);
            if (torusRadiusIJ > radiusP) {
                pointT.scaleAdd(torusRadiusIJ, unitRadialVectorT, torusCenterIJ);
                if (checkProbeNotIJ(pointT)) sas1.createTorus(indexI, indexJ, torusCenterIJ, torusRadiusIJ, true);
            }
        }
    }
}
