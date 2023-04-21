package com.jmex.buoyancy;

import java.util.ArrayList;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cone;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jmex.buoyancy.util.Quadrilateral;
import com.jmex.buoyancy.util.Tetrahedron;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsCollisionGeometry;

/**
 * This is an <code>OrientedBoundingBox</code> buoyant object. It will apply physically accurate friction for boxes, but can also be used as an approximation for
 * similar shapes objects.
 * @author Sam
 *
 */
public class BuoyantBox extends BoundedBuoyantObject<OrientedBoundingBox> {

    private static final boolean debugMode = false;

    private FrictionEncapsulation friction;

    private VolumeAndCentroidEncapsulation buoyantForce;

    private float dragCoefficient = 1.28f;

    /**
	 * This models low velocity energy loss due to waves. 
	 */
    public static final float MINIMUM_FRICTION_LOSS = 0.1f;

    private Node rotationNode = null;

    private Node linearNode = null;

    private Node debugNode = null;

    private Node quadNode = null;

    /**
	 * 
	 * @param parent
	 * @param collisionGeometry
	 * @param spatial
	 * @param physicsNode
	 * @param useSpatialBound Whether to use the bounding volume of the spatial, or to apply a custom world bound that will be independently updated at each step.
	 * It is more efficient to use a bounding volume that is already calculated for the spatial, however there is a caveat: the spatial must use an OrientedBoundingBox bound.
	 */
    public BuoyantBox(Buoyancy parent, PhysicsCollisionGeometry collisionGeometry, Spatial spatial, DynamicPhysicsNode physicsNode, boolean useSpatialBound) throws BoundClassException {
        super(parent, collisionGeometry, spatial, physicsNode, new OrientedBoundingBox(), useSpatialBound);
        if (useSpatialBound & !(spatial.getWorldBound() instanceof OrientedBoundingBox)) throw new BoundClassException();
        friction = new FrictionEncapsulation(this);
        buoyantForce = new VolumeAndCentroidEncapsulation(this);
        if (debugMode) {
            debugNode = new Node();
            collisionGeometry.getParent().getParent().attachChild(debugNode);
        }
        rotationNode = new Node();
        collisionGeometry.getParent().getParent().attachChild(rotationNode);
        linearNode = new Node();
        collisionGeometry.getParent().getParent().attachChild(linearNode);
        quadNode = new Node();
        collisionGeometry.getParent().getParent().attachChild(quadNode);
        quadNode.updateRenderState();
    }

    @Override
    public void applyFriction(IFluidRegion region, Vector3f gravityUnit, float tpf) {
        friction.applyFriction(region, gravityUnit, tpf);
    }

    @Override
    public float getVolumeAndCentroid(IFluidRegion region, Vector3f gravityUnit, Vector3f store) {
        return buoyantForce.getVolumeRatioAndCentroid(region, gravityUnit, store) * super.getVolume();
    }

    /**
	 * This is just an encapsulation to provide a name space and enclosure for the messy calculations that follow.
	 * @author Sam Bayless
	 *
	 */
    private static class FrictionEncapsulation {

        BuoyantBox object;

        @SuppressWarnings("unchecked")
        private static final ArrayList<LinePair>[] _returnSegments = new ArrayList[6];

        @SuppressWarnings("unchecked")
        private static final ArrayList<LinePair>[] _segments = new ArrayList[6];

        static {
            for (int i = 0; i < _segments.length; i++) _segments[i] = new ArrayList<LinePair>();
        }

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _temp = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _corner1a = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _corner1b = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _corner2a = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _corner2b = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _storeCenter = new Vector3f();

        /** Temporary plane for calculations (to avoid garbage collection)*/
        private static final Plane _planeStore = new Plane();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _inersection1 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _intersection2 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _unit1positive = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _unit1negative = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _unit2positive = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _unit2negative = new Vector3f();

        /**
		 * A pair of lines, one positive, one negative, which together define an area.
		 * The pair share common axis vectors, and are to be integrated from the same start and stop positions.
		 * @author Sam
		 *
		 */
        private static class LinePair {

            /**
			 * Rise over run of the line segment
			 */
            private float slope1;

            /**
			 * Rise over run of the line segment
			 */
            private float slope2;

            /**
			 * The y-intercept of the line segment.
			 */
            private float yIntercept1;

            /**
			 * The y-intercept of the line segment.
			 */
            private float yIntercept2;

            /**
			 * The start of both line segments
			 */
            private float start;

            /**
			 * The end of both line segments
			 */
            private float end;

            /**
			 * Whether the first segment is negative, or the second segment is.
			 */
            private boolean firstNegative = false;

            private Vector3f translation = null;

            private Vector3f normal = null;

            private Vector3f yAxis = null;

            private Vector3f xAxis = null;

            public LinePair() {
                super();
                normal = new Vector3f();
                translation = new Vector3f();
                xAxis = new Vector3f();
                yAxis = new Vector3f();
            }

            /**
			 * Constructs a line pair with the same axis as the provided. No other values are copied.
			 * @param copyAxisFrom
			 */
            public LinePair(LinePair copyAxisFrom) {
                normal = new Vector3f(copyAxisFrom.normal);
                translation = new Vector3f(copyAxisFrom.translation);
                xAxis = new Vector3f(copyAxisFrom.xAxis);
                yAxis = new Vector3f(copyAxisFrom.yAxis);
            }

            public float getSlope1() {
                return slope1;
            }

            public void setSlope1(float slope1) {
                this.slope1 = slope1;
            }

            public float getSlope2() {
                return slope2;
            }

            public void setSlope2(float slope2) {
                this.slope2 = slope2;
            }

            public float getYIntercept1() {
                return yIntercept1;
            }

            public void setYIntercept1(float intercept1) {
                yIntercept1 = intercept1;
            }

            public float getYIntercept2() {
                return yIntercept2;
            }

            public void setYIntercept2(float intercept2) {
                yIntercept2 = intercept2;
            }

            public float getStart() {
                return start;
            }

            public void setStart(float start) {
                this.start = start;
            }

            public float getEnd() {
                return end;
            }

            public void setEnd(float end) {
                this.end = end;
            }

            /**
			 * Returns whether the first segment should be treated as negative (true) or the second segment (false).
			 * @return
			 */
            public boolean isFirstIsNegative() {
                return firstNegative;
            }

            public void setFirstIsNegative(boolean firstIsNegative) {
                this.firstNegative = firstIsNegative;
            }

            public Vector3f getTranslation() {
                return translation;
            }

            public Vector3f getNormal() {
                return normal;
            }

            public Vector3f getYAxis() {
                return yAxis;
            }

            public Vector3f getXAxis() {
                return xAxis;
            }

            /**
			 * Compute the first line function for the provided value of x
			 * @param x
			 * @return
			 */
            public float valueOf1(float x) {
                return x * this.getSlope1() + this.getYIntercept1();
            }

            /**
			 * Compute the second line function for the provided value of x
			 * @param x
			 * @return
			 */
            public float valueOf2(float x) {
                return x * this.getSlope2() + this.getYIntercept2();
            }

            public float getArea() {
                float area = 0;
                area += slope1 / 2f * ((end * end - start * start)) + yIntercept1 * (end - start);
                area -= slope2 / 2f * ((end * end) - (start * start)) + yIntercept2 * (end - start);
                if (isFirstIsNegative()) return area = -area;
                return area;
            }
        }

        public FrictionEncapsulation(BuoyantBox object) {
            super();
            this.object = object;
        }

        public void applyFriction(IFluidRegion region, Vector3f gravityUnit, float tpf) {
            object.quadNode.detachAllChildren();
            ArrayList<LinePair>[] segments = buildSegments(region, gravityUnit);
            applyLinearFriction(segments, region, gravityUnit, tpf);
            applyRotationalFriction(segments, region, gravityUnit, tpf);
        }

        private void drawLineSegments(ArrayList<LinePair> segments, Vector3f center) {
            if (segments == null) return;
            int i = -1;
            for (LinePair segment : segments) {
                i++;
                if (segment == null) continue;
                drawLinePair(segment, center);
            }
        }

        private void drawLinePair(LinePair segment, Vector3f center) {
            Vector3f position = new Vector3f(center).addLocal(segment.getTranslation());
            Vector3f x = new Vector3f();
            Vector3f y = new Vector3f();
            x.set(segment.getXAxis()).multLocal(segment.start);
            y.set(segment.getYAxis()).multLocal(segment.valueOf1(segment.start));
            Vector3f topLeft = new Vector3f(position).addLocal(x).addLocal(y);
            x.set(segment.getXAxis()).multLocal(segment.end);
            y.set(segment.getYAxis()).multLocal(segment.valueOf1(segment.end));
            Vector3f topRight = new Vector3f(position).addLocal(x).addLocal(y);
            x.set(segment.getXAxis()).multLocal(segment.start);
            y.set(segment.getYAxis()).multLocal(segment.valueOf2(segment.start));
            Vector3f bottomLeft = new Vector3f(position).addLocal(x).addLocal(y);
            x.set(segment.getXAxis()).multLocal(segment.end);
            y.set(segment.getYAxis()).multLocal(segment.valueOf2(segment.end));
            Vector3f bottomRight = new Vector3f(position).addLocal(x).addLocal(y);
            Quadrilateral quad = new Quadrilateral("", bottomLeft, topLeft, topRight, bottomRight);
            object.quadNode.attachChild(quad);
            quad.updateRenderState();
        }

        private static final Vector3f _heightTest = new Vector3f();

        private static final int X_AXIS = 4;

        private static final int Y_AXIS = 2;

        private static final int Z_AXIS = 1;

        private static final int[] AXIS = { X_AXIS, Y_AXIS, Z_AXIS };

        float t = 0;

        /**
		 * Build trapezoids for each face of the object, representing the portion that is submerged
		 * @param region
		 * @param gravityUnit
		 * @return
		 */
        private ArrayList<LinePair>[] buildSegments(IFluidRegion region, Vector3f gravityUnit) {
            ArrayList<LinePair>[] linePairs = _segments;
            ArrayList<LinePair>[] segments = _returnSegments;
            OrientedBoundingBox bound = object.getCustomWorldBound();
            object.getPhysicsNode().updateWorldVectors();
            Vector3f physicsNodeCenter = object.getPhysicsNode().getWorldTranslation();
            boolean flip = false;
            for (int i = 0; i < 6; i++) {
                int axis = AXIS[i / 2];
                if (getSegmentFromRegionAndBound(region, bound, axis, flip, physicsNodeCenter, gravityUnit, linePairs[i])) {
                    segments[i] = linePairs[i];
                } else {
                    LinePair segment = linePairs[i].get(0);
                    Vector3f height = _heightTest;
                    height.set(segment.getXAxis()).multLocal(segment.start).addLocal(segment.getTranslation()).addLocal(object.getPhysicsNode().getWorldTranslation());
                    if (-height.dot(gravityUnit) <= region.getFluidHeight()) {
                        segments[i] = linePairs[i];
                    } else segments[i] = null;
                }
                flip = !flip;
            }
            return segments;
        }

        private static final Vector3f _centerStore = new Vector3f();

        /**
		 * 
		 * @param region
		 * @param bound
		 * @param axis Index of the axis of the bound to use as normal 
		 * @param physicsNodeTranslation
		 * @param store
		 * @return true if the line was formed from an intersection with the water;
		 * false if the water doesn't intersect anywhere within the face of the cube defined by this normal (which can indicated two possibilities: either the face is fully submerged, or fully out of the water)
		 * If true, then store is set to the line segment formed by the intersection, otherwise store is set to the face of the cube the has the supplied normal.
		 * In all cases, store will have a rotation relative to the world, and a translation relative to the physics node.
		 * The translation of the line segment is, in terms relative to the physics node, the offset of the normal of the plane from the physics node center.
		 * Note: if index is not valid, the results are undefined;
		 * 
		 */
        private boolean getSegmentFromRegionAndBound(IFluidRegion region, OrientedBoundingBox bound, int normalAxis, boolean invertNormal, Vector3f physicsNodeTranslation, Vector3f gravityUnit, ArrayList<LinePair> store) {
            Vector3f unit1 = new Vector3f();
            Vector3f unit2 = new Vector3f();
            Vector3f normal = new Vector3f();
            float extent1;
            float extent2;
            float normalExtent;
            Plane plane = _planeStore;
            plane.getNormal().set(gravityUnit);
            plane.normal.multLocal(-1f);
            plane.constant = region.getFluidHeight();
            store.clear();
            LinePair mainPair = new LinePair();
            store.add(mainPair);
            LinePair secondaryPair = null;
            switch(normalAxis) {
                case (X_AXIS):
                    {
                        normal.set(bound.getXAxis());
                        unit1.set(bound.getZAxis());
                        unit2.set(bound.getYAxis());
                        extent1 = bound.getExtent().z;
                        extent2 = bound.getExtent().y;
                        normalExtent = bound.getExtent().x;
                        break;
                    }
                case (Y_AXIS):
                    {
                        normal.set(bound.getYAxis());
                        unit1.set(bound.getXAxis());
                        unit2.set(bound.getZAxis());
                        extent1 = bound.getExtent().x;
                        extent2 = bound.getExtent().z;
                        normalExtent = bound.getExtent().y;
                        break;
                    }
                case (Z_AXIS):
                    {
                        normal.set(bound.getZAxis());
                        unit1.set(bound.getXAxis());
                        unit2.set(bound.getYAxis());
                        extent1 = bound.getExtent().x;
                        extent2 = bound.getExtent().y;
                        normalExtent = bound.getExtent().z;
                        break;
                    }
                default:
                    return false;
            }
            if (invertNormal) normal.multLocal(-1f);
            if (unit1.dot(gravityUnit) > 0) {
                unit1.multLocal(-1f);
            }
            if (unit2.dot(gravityUnit) > 0) {
                unit2.multLocal(-1f);
            }
            Vector3f center = _storeCenter;
            center.set(normal).multLocal(normalExtent);
            center.addLocal(bound.getCenter());
            Vector3f unit1positive;
            Vector3f unit1negative;
            Vector3f unit2positive;
            Vector3f unit2negative;
            unit1positive = _unit1positive.set(unit1).multLocal(extent1);
            unit1negative = _unit1negative.set(unit1).multLocal(-extent1);
            unit2positive = _unit2positive.set(unit2).multLocal(extent2);
            unit2negative = _unit2negative.set(unit2).multLocal(-extent2);
            Vector3f cornerA = _corner1a;
            cornerA.set(center).addLocal(unit1positive).addLocal(unit2positive);
            Vector3f cornerB = _corner1b;
            cornerB.set(center).addLocal(unit1positive).addLocal(unit2negative);
            Vector3f cornerC = _corner2a;
            cornerC.set(center).addLocal(unit1negative).addLocal(unit2positive);
            Vector3f cornerD = _corner2b;
            cornerD.set(center).addLocal(unit1negative).addLocal(unit2negative);
            Vector3f intersection1 = _inersection1;
            Vector3f intersection2 = _intersection2;
            mainPair.getNormal().set(normal);
            mainPair.getTranslation().set(normal).multLocal(normalExtent + normal.dot(bound.getCenter(_centerStore).subtractLocal(object.getPhysicsNode().getWorldTranslation())));
            mainPair.setFirstIsNegative(false);
            Vector3f leftCorner = null;
            Vector3f rightCorner = null;
            Vector3f bottomCorner = null;
            Vector3f topCorner = null;
            Vector3f leftIntersection = null;
            Vector3f rightIntersection = null;
            Vector3f firstParallel = null;
            Vector3f secondParallel = null;
            boolean firstIntersection = false;
            boolean secondIntersection = false;
            boolean lowerIntersection = false;
            if (BuoyantObject.intersectPairWithPlane(plane, cornerA, cornerC, intersection1, false)) {
                firstIntersection = true;
                mainPair.getYAxis().set(unit1);
                mainPair.getXAxis().set(unit2);
                leftCorner = cornerA;
                rightCorner = cornerB;
                bottomCorner = cornerD;
                topCorner = cornerA;
                if (mainPair.getXAxis().dot(cornerA) > mainPair.getXAxis().dot(cornerB)) {
                    leftCorner = cornerB;
                    rightCorner = cornerA;
                } else {
                    leftCorner = cornerA;
                    rightCorner = cornerB;
                }
                if (BuoyantObject.intersectPairWithPlane(plane, cornerB, cornerD, intersection2, false)) {
                    if (mainPair.getXAxis().dot(intersection1) > mainPair.getXAxis().dot(intersection2)) {
                        leftIntersection = intersection2;
                        rightIntersection = intersection1;
                    } else {
                        leftIntersection = intersection1;
                        rightIntersection = intersection2;
                    }
                } else if (BuoyantObject.intersectPairWithPlane(plane, cornerA, cornerB, intersection2, false)) {
                    secondIntersection = true;
                    leftIntersection = intersection2;
                    rightIntersection = intersection1;
                    firstParallel = cornerB;
                    secondParallel = intersection2;
                } else {
                    firstIntersection = false;
                }
            } else if (BuoyantObject.intersectPairWithPlane(plane, cornerA, cornerB, intersection1, false)) {
                firstIntersection = true;
                mainPair.getYAxis().set(unit2);
                mainPair.getXAxis().set(unit1);
                if (mainPair.getXAxis().dot(cornerA) > mainPair.getXAxis().dot(cornerC)) {
                    leftCorner = cornerC;
                    rightCorner = cornerA;
                } else {
                    leftCorner = cornerA;
                    rightCorner = cornerC;
                }
                bottomCorner = cornerB;
                if (BuoyantObject.intersectPairWithPlane(plane, cornerC, cornerD, intersection2, false)) {
                    if (mainPair.getXAxis().dot(intersection1) > mainPair.getXAxis().dot(intersection2)) {
                        leftIntersection = intersection2;
                        rightIntersection = intersection1;
                    } else {
                        leftIntersection = intersection1;
                        rightIntersection = intersection2;
                    }
                } else if (BuoyantObject.intersectPairWithPlane(plane, cornerD, cornerB, intersection2, false)) {
                    leftIntersection = intersection2;
                    rightIntersection = intersection1;
                    firstParallel = intersection2;
                    secondParallel = cornerD;
                    secondIntersection = true;
                } else firstIntersection = false;
            } else if (BuoyantObject.intersectPairWithPlane(plane, cornerD, cornerC, intersection1, false)) {
                mainPair.getYAxis().set(unit1);
                mainPair.getXAxis().set(unit2);
                if (mainPair.getXAxis().dot(cornerD) > mainPair.getXAxis().dot(cornerC)) {
                    leftCorner = cornerC;
                    rightCorner = cornerD;
                } else {
                    leftCorner = cornerD;
                    rightCorner = cornerC;
                }
                bottomCorner = cornerD;
                if (BuoyantObject.intersectPairWithPlane(plane, cornerD, cornerB, intersection2, false)) {
                    if (mainPair.getXAxis().dot(intersection1) > mainPair.getXAxis().dot(intersection2)) {
                        leftIntersection = intersection2;
                        rightIntersection = intersection1;
                    } else {
                        leftIntersection = intersection1;
                        rightIntersection = intersection2;
                    }
                    lowerIntersection = true;
                }
            } else if (BuoyantObject.intersectPairWithPlane(plane, cornerC, cornerB, intersection1, false)) {
                mainPair.getYAxis().set(unit1);
                mainPair.getXAxis().set(unit2);
                bottomCorner = cornerC;
                if (mainPair.getXAxis().dot(cornerB) > mainPair.getXAxis().dot(cornerC)) {
                    leftCorner = cornerC;
                    rightCorner = cornerB;
                } else {
                    leftCorner = cornerB;
                    rightCorner = cornerC;
                }
                if (BuoyantObject.intersectPairWithPlane(plane, cornerC, cornerD, intersection2, false)) {
                    if (mainPair.getXAxis().dot(intersection1) > mainPair.getXAxis().dot(intersection2)) {
                        leftIntersection = intersection2;
                        rightIntersection = intersection1;
                    } else {
                        leftIntersection = intersection1;
                        rightIntersection = intersection2;
                    }
                    lowerIntersection = true;
                }
            } else if (BuoyantObject.intersectPairWithPlane(plane, cornerC, cornerD, intersection1, false)) {
                mainPair.getYAxis().set(unit1);
                mainPair.getXAxis().set(unit2);
                bottomCorner = cornerC;
                if (mainPair.getXAxis().dot(cornerD) > mainPair.getXAxis().dot(cornerC)) {
                    leftCorner = cornerC;
                    rightCorner = cornerD;
                } else {
                    leftCorner = cornerD;
                    rightCorner = cornerC;
                }
                if (BuoyantObject.intersectPairWithPlane(plane, cornerC, cornerB, intersection2, false)) {
                    if (mainPair.getXAxis().dot(intersection1) > mainPair.getXAxis().dot(intersection2)) {
                        leftIntersection = intersection2;
                        rightIntersection = intersection1;
                    } else {
                        leftIntersection = intersection1;
                        rightIntersection = intersection2;
                    }
                    lowerIntersection = true;
                }
            }
            cornerA.subtractLocal(physicsNodeTranslation).subtractLocal(mainPair.getTranslation());
            cornerB.subtractLocal(physicsNodeTranslation).subtractLocal(mainPair.getTranslation());
            cornerC.subtractLocal(physicsNodeTranslation).subtractLocal(mainPair.getTranslation());
            cornerD.subtractLocal(physicsNodeTranslation).subtractLocal(mainPair.getTranslation());
            intersection1.subtractLocal(physicsNodeTranslation).subtractLocal(mainPair.getTranslation());
            intersection2.subtractLocal(physicsNodeTranslation).subtractLocal(mainPair.getTranslation());
            if (secondIntersection) {
                mainPair.start = mainPair.getXAxis().dot(leftIntersection);
                mainPair.end = mainPair.getXAxis().dot(rightIntersection);
                if (mainPair.start > mainPair.end) {
                    float t = mainPair.start;
                    mainPair.start = mainPair.end;
                    mainPair.end = t;
                }
                float rise = mainPair.getYAxis().dot(rightIntersection) - mainPair.getYAxis().dot(leftIntersection);
                mainPair.slope1 = rise / (mainPair.end - mainPair.start);
                mainPair.yIntercept1 = mainPair.getYAxis().dot(rightIntersection) - mainPair.slope1 * mainPair.getXAxis().dot(rightIntersection);
                secondaryPair = new LinePair(mainPair);
                secondaryPair.slope1 = 0;
                secondaryPair.slope2 = 0;
                secondaryPair.firstNegative = false;
                secondaryPair.yIntercept1 = mainPair.yAxis.dot(topCorner);
                secondaryPair.yIntercept2 = mainPair.yAxis.dot(bottomCorner);
                if (secondaryPair.yIntercept1 < secondaryPair.yIntercept2) secondaryPair.firstNegative = true;
                secondaryPair.start = mainPair.getXAxis().dot(firstParallel);
                secondaryPair.end = mainPair.getXAxis().dot(secondParallel);
                if (secondaryPair.start > secondaryPair.end) {
                    float t = secondaryPair.start;
                    secondaryPair.start = secondaryPair.end;
                    secondaryPair.end = t;
                }
                store.add(secondaryPair);
            } else if (firstIntersection) {
                mainPair.start = mainPair.getXAxis().dot(leftIntersection);
                mainPair.end = mainPair.getXAxis().dot(rightIntersection);
                float rise = mainPair.getYAxis().dot(rightIntersection) - mainPair.getYAxis().dot(leftIntersection);
                mainPair.slope1 = rise / (mainPair.end - mainPair.start);
                mainPair.yIntercept1 = mainPair.getYAxis().dot(leftIntersection) - mainPair.slope1 * mainPair.getXAxis().dot(leftIntersection);
            } else if (lowerIntersection) {
                mainPair.start = mainPair.getXAxis().dot(leftIntersection);
                mainPair.end = mainPair.getXAxis().dot(rightIntersection);
                float rise = mainPair.getYAxis().dot(rightIntersection) - mainPair.getYAxis().dot(leftIntersection);
                mainPair.slope1 = rise / (mainPair.end - mainPair.start);
                mainPair.yIntercept1 = mainPair.getYAxis().dot(rightIntersection) - mainPair.slope1 * mainPair.getXAxis().dot(rightIntersection);
            } else {
                mainPair.getXAxis().set(unit1);
                mainPair.getYAxis().set(unit2);
                bottomCorner = cornerB;
                if (mainPair.getXAxis().dot(cornerC) < mainPair.getXAxis().dot(cornerA)) {
                    mainPair.start = mainPair.getXAxis().dot(cornerC);
                    mainPair.end = mainPair.getXAxis().dot(cornerA);
                } else {
                    mainPair.end = mainPair.getXAxis().dot(cornerC);
                    mainPair.start = mainPair.getXAxis().dot(cornerA);
                }
                mainPair.slope1 = 0;
                mainPair.yIntercept1 = mainPair.getYAxis().dot(cornerC) - mainPair.slope1 * mainPair.getXAxis().dot(cornerC);
            }
            mainPair.yIntercept2 = mainPair.getYAxis().dot(bottomCorner);
            mainPair.slope2 = 0;
            if (mainPair.getStart() > mainPair.getEnd()) System.out.println("error2");
            if (Float.isNaN(mainPair.slope1)) System.out.println("nan");
            if (mainPair.getArea() < 0) {
                mainPair.setFirstIsNegative(true);
            }
            if (secondaryPair != null && secondaryPair.getArea() < 0) {
                secondaryPair.setFirstIsNegative(true);
            }
            if (mainPair != null && (mainPair.getYIntercept1() < -10000 || mainPair.getYIntercept1() > 10000)) {
                System.out.println("  ");
            }
            return firstIntersection || secondIntersection || lowerIntersection;
        }

        private void applyLinearFriction(ArrayList<LinePair>[] segments, IFluidRegion region, Vector3f gravityUnit, float tpf) {
            Vector3f velocity = _temp;
            object.linearNode.detachAllChildren();
            object.getPhysicsNode().getLinearVelocity(velocity);
            object.getPhysicsNode().updateWorldVectors();
            object.getCollisionGeometry().updateWorldVectors();
            for (ArrayList<LinePair> segmentSet : segments) {
                if (segmentSet == null || segmentSet.isEmpty()) continue;
                applyLinearFrictionToSegments(segmentSet, region, velocity, object.dragCoefficient, tpf);
                applyLinearSkinDragToSegments(segmentSet, region, velocity, object.dragCoefficient, tpf);
            }
        }

        private static final Vector3f _skinDrag = new Vector3f();

        private void applyLinearSkinDragToSegments(ArrayList<LinePair> segments, IFluidRegion region, Vector3f velocity, float dragCoefficient, float tpf) {
            float area = 0;
            for (LinePair pair : segments) {
                area += pair.getArea();
            }
            if (area < FastMath.FLT_EPSILON) return;
            Vector3f skinDrag = _skinDrag;
            skinDrag.set(segments.get(0).getXAxis());
            skinDrag.addLocal(segments.get(0).getYAxis());
            skinDrag.normalizeLocal();
            float v = skinDrag.dot(velocity);
            float skinD = v * region.getFluidViscosity();
            skinD *= -area;
            skinDrag.multLocal(skinD);
            if (FastMath.abs(skinD) < FastMath.FLT_EPSILON) return;
            float centroidX = 0;
            for (LinePair pair : segments) {
                centroidX += integrateLinearFrictionCentroidX(pair, false);
                centroidX += integrateLinearFrictionCentroidX(pair, true);
            }
            centroidX /= area;
            float centroidY = 0;
            for (LinePair pair : segments) {
                centroidY += integrateLinearFrictionCentroidY(pair, false);
                centroidY += integrateLinearFrictionCentroidY(pair, true);
            }
            centroidY /= (2f * area);
            if (centroidY < FastMath.FLT_EPSILON && centroidY > -FastMath.FLT_EPSILON) centroidY = 0;
            if (centroidX < FastMath.FLT_EPSILON && centroidX > -FastMath.FLT_EPSILON) centroidX = 0;
            Vector3f position = _centroidStore.set(segments.get(0).getTranslation());
            Vector3f posX = _posStore;
            posX.set(segments.get(0).getXAxis()).multLocal(centroidX);
            position.addLocal(posX);
            Vector3f posY = _posStore;
            posY.set(segments.get(0).getYAxis()).multLocal(centroidY);
            position.addLocal(posY);
            object.getPhysicsNode().getWorldRotation().inverse().multLocal(position);
            object.getPhysicsNode().addForce(skinDrag, position);
        }

        private static final Vector3f _centroidStore = new Vector3f();

        private static final Vector3f _frictionStore = new Vector3f();

        private void applyLinearFrictionToSegments(ArrayList<LinePair> segments, IFluidRegion region, Vector3f velocity, float dragCoefficient, float tpf) {
            Vector3f normal = segments.get(0).getNormal();
            float v = velocity.dot(normal);
            if (v <= 0) return;
            float area = 0;
            for (LinePair pair : segments) {
                area += pair.getArea();
            }
            if (area < FastMath.FLT_EPSILON) return;
            float frictionForce;
            float lowestStart = Float.POSITIVE_INFINITY;
            float highestEnd = Float.NEGATIVE_INFINITY;
            for (LinePair pair : segments) {
                if (pair.start < lowestStart) lowestStart = pair.start;
                if (pair.end > highestEnd) highestEnd = pair.end;
            }
            float characteristicLength = highestEnd - lowestStart;
            if (Buoyancy.calculateReynoldsNumber(region, (float) v, characteristicLength) > 10f) {
                frictionForce = -0.5f * region.getFluidDensity() * v * v * area * dragCoefficient;
            } else {
                frictionForce = (region.getFluidViscosity() * (v)) + FastMath.sign(v) * MINIMUM_FRICTION_LOSS;
                frictionForce *= -area;
            }
            Vector3f friction = _frictionStore;
            friction.set(normal).multLocal((float) frictionForce);
            float centroidX = 0;
            for (LinePair pair : segments) {
                centroidX += integrateLinearFrictionCentroidX(pair, false);
                centroidX += integrateLinearFrictionCentroidX(pair, true);
            }
            centroidX /= area;
            float centroidY = 0;
            for (LinePair pair : segments) {
                centroidY += integrateLinearFrictionCentroidY(pair, false);
                centroidY += integrateLinearFrictionCentroidY(pair, true);
            }
            centroidY /= (2f * area);
            if (centroidY < FastMath.FLT_EPSILON && centroidY > -FastMath.FLT_EPSILON) centroidY = 0;
            if (centroidX < FastMath.FLT_EPSILON && centroidX > -FastMath.FLT_EPSILON) centroidX = 0;
            Vector3f position = _centroidStore.set(segments.get(0).getTranslation());
            Vector3f posX = _posStore;
            posX.set(segments.get(0).getXAxis()).multLocal(centroidX);
            position.addLocal(posX);
            Vector3f posY = _posStore;
            posY.set(segments.get(0).getYAxis()).multLocal(centroidY);
            position.addLocal(posY);
            object.getPhysicsNode().getWorldRotation().inverse().multLocal(position);
            object.getPhysicsNode().addForce(friction, position);
            if (debugMode) {
                Cone b = new Cone("", 5, 5, 0.5f, 1f);
                object.linearNode.getLocalTranslation().set(object.getPhysicsNode().getWorldTranslation());
                b.getLocalScale().multLocal(friction.length() / 10 + 0.01f);
                b.getLocalTranslation().set((object.getPhysicsNode().getWorldRotation().multLocal(position)));
                if (segments.get(0).yAxis.cross(segments.get(0).xAxis).dot(friction) <= 0) {
                    b.getLocalRotation().fromAxes(segments.get(0).xAxis, segments.get(0).yAxis, friction.normalize());
                    b.getLocalRotation().multLocal(new Quaternion().fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_X));
                } else {
                    b.getLocalRotation().fromAxes(segments.get(0).yAxis, segments.get(0).xAxis, friction.normalize());
                    b.getLocalRotation().multLocal(new Quaternion().fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_X));
                }
                b.updateWorldVectors();
                object.linearNode.attachChild(b);
                b.updateRenderState();
            }
        }

        private double integrateLinearFrictionCentroidX(LinePair segment, boolean useSecondLine) {
            double slope;
            double yIntercept;
            boolean negative;
            double start = segment.start;
            double end = segment.end;
            if (!useSecondLine) {
                slope = segment.slope1;
                yIntercept = segment.yIntercept1;
                negative = segment.isFirstIsNegative();
            } else {
                slope = segment.slope2;
                yIntercept = segment.yIntercept2;
                negative = !segment.isFirstIsNegative();
            }
            double centroidX = slope / 3.0 * (Math.pow(end, 3) - Math.pow(start, 3));
            centroidX += yIntercept / 2.0 * (Math.pow(end, 2) - Math.pow(start, 2));
            if (negative) return -centroidX; else return centroidX;
        }

        private double integrateLinearFrictionCentroidY(LinePair segment, boolean useSecondLine) {
            double slope;
            double yIntercept;
            boolean negative;
            double start = segment.start;
            double end = segment.end;
            if (!useSecondLine) {
                slope = segment.slope1;
                yIntercept = segment.yIntercept1;
                negative = segment.isFirstIsNegative();
            } else {
                slope = segment.slope2;
                yIntercept = segment.yIntercept2;
                negative = !segment.isFirstIsNegative();
            }
            double centroidY = slope * slope / 3f * (Math.pow(end, 3) - Math.pow(start, 3));
            centroidY += yIntercept * slope * (Math.pow(end, 2) - Math.pow(start, 2));
            centroidY += yIntercept * yIntercept * (end - start);
            if (negative) return -centroidY; else return centroidY;
        }

        private void applyRotationalFriction(ArrayList<LinePair>[] segments, IFluidRegion region, Vector3f gravityUnit, float tpf) {
            object.rotationNode.detachAllChildren();
            Vector3f omega = _temp;
            omega = (object.getPhysicsNode().getAngularVelocity(omega));
            object.linearNode.detachAllChildren();
            for (ArrayList<LinePair> segmentSet : segments) {
                if (segmentSet == null) continue;
                float omegaX = omega.dot(segmentSet.get(0).getYAxis());
                float omegaY = omega.dot(segmentSet.get(0).getXAxis());
                applyRotationalFrictionToSegments(segmentSet, region, omegaX, object.dragCoefficient, tpf);
                ArrayList<LinePair> inverseSet = invertLinePairSet(segmentSet);
                applyRotationalFrictionToSegments(inverseSet, region, omegaY, object.dragCoefficient, tpf);
                applyRotationalSkinDragToSegments(inverseSet, region, omega.dot(segmentSet.get(0).getNormal()), tpf);
            }
        }

        private ArrayList<LinePair> invertLinePairSet(ArrayList<LinePair> segments) {
            ArrayList<LinePair> inverseSegments = new ArrayList<LinePair>();
            for (LinePair pair : segments) {
                ArrayList<LinePair> inverse = invertLinePair(pair);
                for (LinePair inverted : inverse) inverseSegments.add(inverted);
            }
            return inverseSegments;
        }

        /**
		 * Inverts a pair of line segments.
		 * The segments must have identical start and end points for this to be accurate.
		 * @param store
		 * @return
		 */
        public ArrayList<LinePair> invertLinePair(LinePair segment) {
            ArrayList<LinePair> pairs = new ArrayList<LinePair>();
            LinePair centralPair = new LinePair();
            centralPair.xAxis.set(segment.yAxis);
            centralPair.yAxis.set(segment.xAxis);
            centralPair.normal.set(segment.normal);
            centralPair.translation.set(segment.translation);
            centralPair.slope1 = 0;
            centralPair.slope2 = 0;
            centralPair.yIntercept1 = segment.end;
            centralPair.yIntercept2 = segment.start;
            centralPair.setFirstIsNegative(false);
            centralPair.start = segment.valueOf2(segment.end);
            centralPair.end = segment.valueOf1(segment.end);
            if (centralPair.start > centralPair.end) {
                float t = centralPair.start;
                centralPair.start = centralPair.end;
                centralPair.end = t;
            }
            pairs.add(centralPair);
            {
                float pSlope = (segment.firstNegative ? segment.slope2 : segment.slope1);
                float nSlope = (segment.firstNegative ? segment.slope1 : segment.slope2);
                float pIntercept = (segment.firstNegative ? segment.yIntercept2 : segment.yIntercept1);
                float nIntercept = (segment.firstNegative ? segment.yIntercept1 : segment.yIntercept2);
                if (pSlope > FastMath.FLT_EPSILON || pSlope < -FastMath.FLT_EPSILON) {
                    LinePair positivePair = new LinePair(centralPair);
                    positivePair.setFirstIsNegative(pSlope > 0);
                    positivePair.slope1 = 1f / pSlope;
                    positivePair.yIntercept1 = -pIntercept / pSlope;
                    positivePair.slope2 = 0;
                    float startPos;
                    float endPos;
                    if (segment.firstNegative) {
                        startPos = segment.valueOf2(segment.start);
                        endPos = segment.valueOf2(segment.end);
                    } else {
                        startPos = segment.valueOf1(segment.start);
                        endPos = segment.valueOf1(segment.end);
                    }
                    if (startPos > endPos) {
                        float t = startPos;
                        startPos = endPos;
                        endPos = t;
                    }
                    positivePair.start = startPos;
                    positivePair.end = endPos;
                    float yIntA = positivePair.valueOf1(positivePair.end);
                    float yIntB = positivePair.valueOf1(positivePair.start);
                    positivePair.yIntercept2 = (yIntA < yIntB ? yIntA : yIntB);
                    pairs.add(positivePair);
                }
                if (nSlope > FastMath.FLT_EPSILON || nSlope < -FastMath.FLT_EPSILON) {
                    LinePair negativePair = new LinePair(centralPair);
                    negativePair.setFirstIsNegative(nSlope < 0);
                    negativePair.slope1 = 1f / nSlope;
                    negativePair.yIntercept1 = -nIntercept / nSlope;
                    negativePair.slope2 = 0;
                    float startPos;
                    float endPos;
                    if (!segment.firstNegative) {
                        startPos = segment.valueOf2(segment.start);
                        endPos = segment.valueOf2(segment.end);
                    } else {
                        startPos = segment.valueOf1(segment.start);
                        endPos = segment.valueOf1(segment.end);
                    }
                    if (startPos > endPos) {
                        float t = startPos;
                        startPos = endPos;
                        endPos = t;
                    }
                    negativePair.start = startPos;
                    negativePair.end = endPos;
                    float yIntA = negativePair.valueOf1(negativePair.end);
                    float yIntB = negativePair.valueOf1(negativePair.start);
                    negativePair.yIntercept2 = (yIntA < yIntB ? yIntA : yIntB);
                    pairs.add(negativePair);
                }
            }
            return pairs;
        }

        private static final Vector3f _skinTorque = new Vector3f();

        private static final Vector3f _skinPosition = new Vector3f();

        private void applyRotationalSkinDragToSegments(ArrayList<LinePair> segments, IFluidRegion fluid, float omega, float tpf) {
            if (omega < FastMath.FLT_EPSILON && omega > -FastMath.FLT_EPSILON) return;
            double torque = 0;
            float xForce = 0;
            float yForce = 0;
            for (LinePair pair : segments) {
                if (pair.firstNegative) {
                    float t = 0;
                    t -= (rotationalSkinDragIntegratorHelper(pair.slope1, pair.yIntercept1, pair.end) - rotationalSkinDragIntegratorHelper(pair.slope1, pair.yIntercept1, pair.start));
                    t += (rotationalSkinDragIntegratorHelper(pair.slope2, pair.yIntercept2, pair.end) - rotationalSkinDragIntegratorHelper(pair.slope2, pair.yIntercept2, pair.start));
                    if (Float.isNaN(t)) {
                        t = 0;
                    }
                    if (Float.isInfinite(t)) {
                        t = 0;
                    }
                    torque += t;
                    xForce -= (rotationalSkinDragIntegratorX(pair.slope1, pair.yIntercept1, pair.end) - rotationalSkinDragIntegratorX(pair.slope1, pair.yIntercept1, pair.start));
                    xForce += (rotationalSkinDragIntegratorX(pair.slope2, pair.yIntercept2, pair.end) - rotationalSkinDragIntegratorX(pair.slope2, pair.yIntercept2, pair.start));
                    yForce -= (rotationalSkinDragIntegratorY(pair.slope1, pair.yIntercept1, pair.end) - rotationalSkinDragIntegratorY(pair.slope1, pair.yIntercept1, pair.start));
                    yForce += (rotationalSkinDragIntegratorY(pair.slope2, pair.yIntercept2, pair.end) - rotationalSkinDragIntegratorY(pair.slope2, pair.yIntercept2, pair.start));
                } else {
                    float t = 0;
                    t += (rotationalSkinDragIntegratorHelper(pair.slope1, pair.yIntercept1, pair.end) - rotationalSkinDragIntegratorHelper(pair.slope1, pair.yIntercept1, pair.start));
                    t -= (rotationalSkinDragIntegratorHelper(pair.slope2, pair.yIntercept2, pair.end) - rotationalSkinDragIntegratorHelper(pair.slope2, pair.yIntercept2, pair.start));
                    if (Float.isNaN(t)) {
                        t = 0;
                    }
                    if (Float.isInfinite(t)) {
                        t = 0;
                    }
                    torque += t;
                    xForce += (rotationalSkinDragIntegratorX(pair.slope1, pair.yIntercept1, pair.end) - rotationalSkinDragIntegratorX(pair.slope1, pair.yIntercept1, pair.start));
                    xForce -= (rotationalSkinDragIntegratorX(pair.slope2, pair.yIntercept2, pair.end) - rotationalSkinDragIntegratorX(pair.slope2, pair.yIntercept2, pair.start));
                    yForce += (rotationalSkinDragIntegratorY(pair.slope1, pair.yIntercept1, pair.end) - rotationalSkinDragIntegratorY(pair.slope1, pair.yIntercept1, pair.start));
                    yForce -= (rotationalSkinDragIntegratorY(pair.slope2, pair.yIntercept2, pair.end) - rotationalSkinDragIntegratorY(pair.slope2, pair.yIntercept2, pair.start));
                }
            }
            Vector3f torqueV = _skinTorque;
            torqueV.set(segments.get(0).getNormal());
            float totalTorque = -(float) torque * omega * fluid.getFluidViscosity();
            if (Float.isNaN(totalTorque)) {
                totalTorque = 0;
            }
            if (Float.isInfinite(totalTorque)) {
                totalTorque = Math.signum(totalTorque) * (Float.MAX_VALUE);
            }
            torqueV.multLocal(totalTorque);
            object.getPhysicsNode().addTorque(torqueV);
            Vector3f pos = _skinPosition;
            pos.set(segments.get(0).getTranslation());
            Vector3f force = _skinTorque;
            force.set(segments.get(0).getXAxis());
            force.multLocal(-omega * xForce * fluid.getFluidViscosity());
            force.set(segments.get(0).getYAxis());
            force.multLocal(-omega * yForce * fluid.getFluidViscosity());
        }

        private float rotationalSkinDragIntegratorX(float m, float b, float x) {
            float v = (m * m * FastMath.pow(x, 3) / 3f + m * x * x * b + b * b * x) / 2f;
            return v;
        }

        private float rotationalSkinDragIntegratorY(float m, float b, float x) {
            return (m * FastMath.pow(x, 3) / 3f + b * x * x / 2f);
        }

        private double rotationalSkinDragIntegratorHelper(double m, double b, double x) {
            double b_2 = b * b;
            double b_3 = b_2 * b;
            double m_2 = m * m;
            double x_2 = x * x;
            double x_3 = x_2 * x;
            return (Math.sqrt(1.0 + m_2) * (-((1.0 + m_2) * x_3) + 3.0 * b_2 * m * Math.sqrt(b_2 + 2.0 * b * m * x + (1.0 + m_2) * x_2) + 6.0 * b * (1.0 + m_2) * x * Math.sqrt(b_2 + 2.0 * b * m * x + (1.0 + m_2) * x_2) + 3.0 * m * (1.0 + m_2) * x_2 * Math.sqrt(b_2 + 2.0 * b * m * x + (1.0 + m_2) * x_2)) + 3 * Math.pow(1.0 + m_2, 3.0 / 2.0) * x_3 * Math.log(b + m * x + Math.sqrt(b_2 + 2.0 * b * m * x + (1 + m_2) * x_2)) + 3.0 * b_3 * Math.log(b * m + x + m_2 * x + Math.sqrt(1.0 + m_2) * Math.sqrt(b_2 + 2.0 * b * m * x + (1.0 + m_2) * x_2))) / Math.pow(18.0 * (1.0 + m_2), (3.0 / 2.0));
        }

        private static final Vector3f _posStore = new Vector3f();

        private static final Vector3f _testStore = new Vector3f();

        private void applyRotationalFrictionToSegments(ArrayList<LinePair> segments, IFluidRegion region, float omega, float dragCoefficient, float tpf) {
            float minimumStart;
            float maximumEnd;
            float relativeSignOfNormal = FastMath.sign(segments.get(0).getYAxis().cross(segments.get(0).getXAxis(), _testStore).dot(segments.get(0).getNormal()));
            if ((omega) * relativeSignOfNormal > 0) {
                maximumEnd = Float.POSITIVE_INFINITY;
                minimumStart = 0;
            } else {
                maximumEnd = 0;
                minimumStart = Float.NEGATIVE_INFINITY;
            }
            double friction = 0;
            for (LinePair pair : segments) {
                friction += rotationalFrictionIntegrateSegment(pair, minimumStart, maximumEnd, false);
                friction += rotationalFrictionIntegrateSegment(pair, minimumStart, maximumEnd, true);
            }
            if (friction < FastMath.FLT_EPSILON) return;
            double centroidX = 0;
            for (LinePair pair : segments) {
                centroidX += rotationalCentroidXIntegrateSegment(pair, minimumStart, maximumEnd, false);
                centroidX += rotationalCentroidXIntegrateSegment(pair, minimumStart, maximumEnd, true);
            }
            centroidX /= friction;
            double centroidY = 0;
            for (LinePair pair : segments) {
                centroidY += rotationalCentroidYIntegrateSegment(pair, minimumStart, maximumEnd, false);
                centroidY += rotationalCentroidYIntegrateSegment(pair, minimumStart, maximumEnd, true);
            }
            centroidY /= (2.0 * friction);
            friction *= (-0.5 * omega * omega * dragCoefficient * region.getFluidDensity());
            Vector3f frictionForce = _frictionStore.set(segments.get(0).getNormal());
            frictionForce.multLocal((float) friction);
            Vector3f position = _centroidStore.set(segments.get(0).getTranslation());
            Vector3f posX = _posStore;
            posX.set(segments.get(0).getXAxis()).multLocal((float) centroidX);
            position.addLocal(posX);
            Vector3f posY = _posStore;
            posY.set(segments.get(0).getYAxis()).multLocal((float) centroidY);
            position.addLocal(posY);
            object.getPhysicsNode().getWorldRotation().inverse().multLocal(position);
            object.getPhysicsNode().addForce(frictionForce, position);
            if (debugMode) {
                Cone b = new Cone("", 5, 5, 0.5f, 1f);
                object.linearNode.getLocalTranslation().set(object.getPhysicsNode().getWorldTranslation());
                b.getLocalScale().multLocal(frictionForce.length() / 10f + 0.01f);
                b.getLocalTranslation().set((object.getPhysicsNode().getWorldRotation().multLocal(position)));
                if (segments.get(0).yAxis.cross(segments.get(0).xAxis).dot(frictionForce) <= 0) {
                    b.getLocalRotation().fromAxes(segments.get(0).xAxis, segments.get(0).yAxis, frictionForce.normalize());
                    b.getLocalRotation().multLocal(new Quaternion().fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_X));
                } else {
                    b.getLocalRotation().fromAxes(segments.get(0).yAxis, segments.get(0).xAxis, frictionForce.normalize());
                    b.getLocalRotation().multLocal(new Quaternion().fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_X));
                }
                b.updateWorldVectors();
                object.linearNode.attachChild(b);
                b.updateRenderState();
            }
        }

        private double rotationalFrictionIntegrateSegment(LinePair segment, float minStart, float maxEnd, boolean useSecondLine) {
            float slope;
            float yIntercept;
            boolean negative;
            if (!useSecondLine) {
                slope = segment.slope1;
                yIntercept = segment.yIntercept1;
                negative = segment.isFirstIsNegative();
            } else {
                slope = segment.slope2;
                yIntercept = segment.yIntercept2;
                negative = !segment.isFirstIsNegative();
            }
            float start = segment.start;
            float end = segment.end;
            if (segment.end > maxEnd) end = maxEnd;
            if (segment.start < minStart) start = minStart;
            if (segment.start > maxEnd) return 0;
            if (segment.end < minStart) return 0;
            double force = ((double) slope) / 4.0 * (Math.pow(end, 4) - Math.pow(start, 4));
            force += ((double) yIntercept) / 3.0 * (Math.pow(end, 3) - Math.pow(start, 3));
            if (negative) force = -force;
            return force;
        }

        private double rotationalCentroidXIntegrateSegment(LinePair segment, float minStart, float maxEnd, boolean useSecondLine) {
            float slope;
            float yIntercept;
            boolean negative;
            if (!useSecondLine) {
                slope = segment.slope1;
                yIntercept = segment.yIntercept1;
                negative = segment.isFirstIsNegative();
            } else {
                slope = segment.slope2;
                yIntercept = segment.yIntercept2;
                negative = !segment.isFirstIsNegative();
            }
            float start = segment.start;
            float end = segment.end;
            if (segment.end > maxEnd) end = maxEnd;
            if (segment.start < minStart) start = minStart;
            if (segment.start > maxEnd) return 0;
            if (segment.end < minStart) return 0;
            double centroidX = ((double) slope) / 5.0 * (Math.pow(end, 5) - Math.pow(start, 5));
            centroidX += ((double) yIntercept) / 4.0 * (Math.pow(end, 4) - Math.pow(start, 4));
            if (negative) centroidX = -centroidX;
            return centroidX;
        }

        private double rotationalCentroidYIntegrateSegment(LinePair segment, float minStart, float maxEnd, boolean useSecondLine) {
            float slope;
            float yIntercept;
            boolean negative;
            if (!useSecondLine) {
                slope = segment.slope1;
                yIntercept = segment.yIntercept1;
                negative = segment.isFirstIsNegative();
            } else {
                slope = segment.slope2;
                yIntercept = segment.yIntercept2;
                negative = !segment.isFirstIsNegative();
            }
            float start = segment.start;
            float end = segment.end;
            if (segment.end > maxEnd) end = maxEnd;
            if (segment.start < minStart) start = minStart;
            if (segment.start > maxEnd) return 0;
            if (segment.end < minStart) return 0;
            double centroidY = ((double) slope) * ((double) slope) / 5.0 * (Math.pow(end, 5) - Math.pow(start, 5));
            centroidY += ((double) slope) * ((double) yIntercept) / 2.0 * (Math.pow(end, 4) - Math.pow(start, 4));
            centroidY += ((double) yIntercept) * ((double) yIntercept) / 3.0 * (Math.pow(end, 3) - Math.pow(start, 3));
            if (negative) centroidY = -centroidY;
            return centroidY;
        }
    }

    /**
	 * This is just an encapsulation to provide a name space and enclosure for the messy calculations that follow.
	 * These methods perform an optimized, hard-coded Tetrahedralization on the portion of the box that is below surface level,
	 * to determine the exact volume and centroid of that volume.
	 * This was largely determined by modeling different orientations of the box and plane with plasticine, and then cutting it into tetrahedrons.
	 * It is ugly, but fast.
	 * 
	 * @author Sam Bayless
	 *
	 */
    private static class VolumeAndCentroidEncapsulation {

        private BuoyantBox object;

        private static final int axisZ = 1;

        private static final int axisY = 2;

        private static final int axisX = 4;

        private static final int MASK = 7;

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp0 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp1 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp2 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp3 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp4 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp5 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _temp6 = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _yTemp = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _xTemp = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _zTemp = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static final Vector3f _tempVector = new Vector3f();

        /** Temporary vectors for calculations (to avoid garbage collection)*/
        private static final Vector3f[] _vertices = new Vector3f[8];

        static {
            for (int i = 0; i < _vertices.length; i++) _vertices[i] = new Vector3f();
        }

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _aMinusD = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _bMinusD = new Vector3f();

        /** Temporary vector for calculations (to avoid garbage collection)*/
        private static Vector3f _cMinusD = new Vector3f();

        /** Temporary plane for calculations (to avoid garbage collection)*/
        private static final Plane _plane = new Plane();

        public VolumeAndCentroidEncapsulation(BuoyantBox object) {
            this.object = object;
        }

        private Vector3f getNeighbour(int axis, int index, Vector3f[] vertices) {
            return vertices[index ^ axis];
        }

        private int getNeighbourIndex(int axis, int index) {
            return index ^ axis;
        }

        private int getDifferentAxis(int index1, int index2) {
            return (index1 ^ index2);
        }

        /**
		 * Get the axis that these indices share in common
		 * @param index1
		 * @param index2
		 * @return
		 */
        private int getSharedAxis(int index1, int index2) {
            return (~(index1 ^ index2)) & MASK;
        }

        /**
		 * Get the first non-zero bit of this axis
		 * @param axis
		 * @return
		 */
        private int getFirstComponent(int axis) {
            if ((axis & axisZ) > 0) return axisZ;
            if ((axis & axisY) > 0) return axisY;
            if ((axis & axisX) > 0) return axisX;
            return axis;
        }

        /**
		 * Get the first non-zero bit of this axis
		 * @param axis
		 * @return
		 */
        private int getSecondComponent(int axis) {
            if ((axis & axisZ) > 0) {
                if ((axis & axisY) > 0) return axisY;
                if ((axis & axisX) > 0) return axisX;
            } else if ((axis & axisY) > 0) {
                if ((axis & axisX) > 0) return axisX;
            }
            return axis;
        }

        /**
	 * Gets the ratio of the volume that is submerged, and its centroid
	 * @param region
	 * @param gravityUnit
	 * @param store
	 * @return
	 */
        public float getVolumeRatioAndCentroid(IFluidRegion region, Vector3f gravityUnit, Vector3f store) {
            object.getSpatial().getParent().updateWorldVectors();
            object.getSpatial().updateWorldVectors();
            object.updateCustomWorldBound();
            OrientedBoundingBox worldbound;
            try {
                worldbound = object.getCustomWorldBound();
            } catch (ClassCastException e) {
                throw new BoundClassException(e.getMessage());
            }
            if (debugMode) object.debugNode.detachAllChildren();
            Vector3f center = worldbound.getCenter();
            Vector3f extent = worldbound.extent;
            Plane waterPlane = _plane;
            waterPlane.getNormal().set(gravityUnit);
            waterPlane.setConstant(region.getFluidHeight());
            boolean[] isUnderwater = new boolean[8];
            int numVertices = 0;
            int numUnderwater = 0;
            Vector3f[] vertices = _vertices;
            for (float z = -1; z <= 1; z += 2) {
                _zTemp.set(worldbound.getZAxis()).multLocal(extent.z * z);
                _zTemp.addLocal(center);
                for (float y = -1; y <= 1; y += 2) {
                    _yTemp.set(worldbound.getYAxis()).multLocal(extent.y * y);
                    for (float x = -1; x <= 1; x += 2) {
                        _xTemp.set(worldbound.getXAxis()).multLocal(extent.x * x);
                        Vector3f curVector = vertices[numVertices].set(_zTemp).addLocal(_yTemp).addLocal(_xTemp);
                        if ((isBelowWater(region, curVector, gravityUnit)) < 0) {
                            numUnderwater++;
                            isUnderwater[numVertices] = true;
                        } else {
                            isUnderwater[numVertices] = false;
                        }
                        numVertices++;
                    }
                }
            }
            if (numUnderwater == 0) return 0;
            boolean invert = false;
            if (numUnderwater > 4) {
                invert = true;
                for (int i = 0; i < isUnderwater.length; i++) isUnderwater[i] = !isUnderwater[i];
                numUnderwater = 8 - numUnderwater;
            }
            int[] underWaterIndices = new int[numUnderwater];
            {
                int e = 0;
                for (int i = 0; i < numVertices; i++) {
                    if (isUnderwater[i]) {
                        underWaterIndices[e] = i;
                        e++;
                    }
                }
            }
            float volumeOfSegment = 0;
            if (numUnderwater == 1) {
                volumeOfSegment = resolveUnderwaterVolume_1(underWaterIndices, vertices, waterPlane, store);
            } else if (numUnderwater == 2) {
                volumeOfSegment = resolveUnderwaterVolume_2(underWaterIndices, vertices, waterPlane, store);
            } else if (numUnderwater == 3) {
                volumeOfSegment = resolveUnderwaterVolume_3(underWaterIndices, isUnderwater, vertices, waterPlane, store);
            } else if (numUnderwater == 4) {
                int similar = 7;
                for (int i = 1; i < underWaterIndices.length; i++) {
                    similar &= underWaterIndices[i - 1] ^ (~underWaterIndices[i]);
                }
                if (similar > 0) {
                    volumeOfSegment = resolveUnderwaterVolume_4Similar(underWaterIndices, vertices, waterPlane, store);
                } else {
                    volumeOfSegment = resolveUnderwaterVolume_4Odd(underWaterIndices, isUnderwater, vertices, waterPlane, store);
                }
            }
            if (invert) {
                float totalVolume = object.getCustomWorldBound().getVolume();
                float belowWaterVolume = totalVolume - volumeOfSegment;
                Vector3f centroidOfWholeObject = new Vector3f().set(object.getCustomWorldBound().getCenter());
                centroidOfWholeObject.multLocal(totalVolume);
                store.multLocal(volumeOfSegment);
                centroidOfWholeObject.subtractLocal(store);
                store.set(centroidOfWholeObject);
                store.divideLocal(belowWaterVolume);
                volumeOfSegment = belowWaterVolume;
            }
            return volumeOfSegment / getTotalVolume();
        }

        private float getTotalVolume() {
            return object.getCustomWorldBound().getVolume();
        }

        /**
	 *  Helper method to perform an efficient tetrahedralization of the volume under the water, to determine exactly its volume and centroid.
	 * @param underWaterIndices
	 * @param isUnderwater
	 * @param vertices
	 * @param waterPlane
	 * @param store
	 * @return
	 */
        private float resolveUnderwaterVolume_4Odd(int[] underWaterIndices, boolean[] isUnderwater, Vector3f[] vertices, Plane waterPlane, Vector3f store) {
            int centerIndex = -1;
            int[] outerVertices = new int[3];
            int position = 0;
            for (int vertex : underWaterIndices) {
                if (countUnderwaterNeighbours(vertex, isUnderwater) == 3) {
                    centerIndex = vertex;
                } else {
                    outerVertices[position] = vertex;
                    position++;
                }
            }
            outerVertices[0] = getNeighbourIndex(axisX, centerIndex);
            outerVertices[1] = getNeighbourIndex(axisY, centerIndex);
            outerVertices[2] = getNeighbourIndex(axisZ, centerIndex);
            Vector3f center = vertices[centerIndex];
            Vector3f outer0 = vertices[outerVertices[0]];
            Vector3f outer1 = vertices[outerVertices[1]];
            Vector3f outer2 = vertices[outerVertices[2]];
            int axis = getSharedAxis(outerVertices[0], centerIndex);
            Vector3f outer0A = _temp1.set(getNeighbour(getFirstComponent(axis), outerVertices[0], vertices));
            Vector3f outer0B = _temp2.set(getNeighbour(getSecondComponent(axis), outerVertices[0], vertices));
            axis = getSharedAxis(outerVertices[1], centerIndex);
            Vector3f outer1A = _temp3.set(getNeighbour(getFirstComponent(axis), outerVertices[1], vertices));
            Vector3f outer1B = _temp4.set(getNeighbour(getSecondComponent(axis), outerVertices[1], vertices));
            axis = getSharedAxis(outerVertices[2], centerIndex);
            Vector3f outer2A = _temp5.set(getNeighbour(getFirstComponent(axis), outerVertices[2], vertices));
            Vector3f outer2B = _temp6.set(getNeighbour(getSecondComponent(axis), outerVertices[2], vertices));
            boolean result = true;
            result &= intersectPairWithPlane(waterPlane, outer0, outer0A, outer0A, true);
            result &= intersectPairWithPlane(waterPlane, outer0, outer0B, outer0B, true);
            result &= intersectPairWithPlane(waterPlane, outer1, outer1A, outer1A, true);
            result &= intersectPairWithPlane(waterPlane, outer1, outer1B, outer1B, true);
            result &= intersectPairWithPlane(waterPlane, outer2, outer2A, outer2A, true);
            result &= intersectPairWithPlane(waterPlane, outer2, outer2B, outer2B, true);
            if (!result) return 0;
            Vector3f curPos = _tempVector;
            float volume = getTetrahedronProperties(center, outer0, outer1, outer2, curPos);
            store.set(curPos.multLocal(volume));
            float totalVolume = volume;
            volume = getTetrahedronProperties(outer2, outer1, outer2A, outer0, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer1, outer1B, outer2A, outer0, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer1, outer1B, outer2A, outer1A, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer0, outer1B, outer2A, outer0B, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer0, outer2A, outer0A, outer0B, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer0, outer2, outer0A, outer2A, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            store.divideLocal(totalVolume);
            return totalVolume;
        }

        /**
	 * Helper method to perform an efficient tetrahedralization of the volume under the water, to determine exactly its volume and centroid.
	 * @param underWaterIndices
	 * @param vertices
	 * @param waterPlane
	 * @param store
	 * @return
	 */
        private float resolveUnderwaterVolume_4Similar(int[] underWaterIndices, Vector3f[] vertices, Plane waterPlane, Vector3f store) {
            boolean result = true;
            int axis = getSharedAxis(underWaterIndices[0], underWaterIndices[1]);
            axis &= getSharedAxis(underWaterIndices[1], underWaterIndices[2]);
            axis &= getSharedAxis(underWaterIndices[2], underWaterIndices[3]);
            Vector3f[] pairs = new Vector3f[4];
            Vector3f[] originals = new Vector3f[4];
            pairs[0] = _temp0;
            pairs[1] = _temp1;
            pairs[2] = _temp2;
            pairs[3] = _temp3;
            for (int i = 0; i < underWaterIndices.length; i++) {
                int currentVertex = underWaterIndices[i];
                originals[i] = vertices[currentVertex];
                pairs[i].set(getNeighbour(axis, currentVertex, vertices));
                result &= intersectPairWithPlane(waterPlane, originals[i], pairs[i], pairs[i], true);
            }
            if (!result) return -1;
            Vector3f curPos = _tempVector;
            float volume = getTetrahedronProperties(originals[0], originals[1], originals[2], pairs[2], curPos);
            store.set(curPos.multLocal(volume));
            float totalVolume = volume;
            volume = getTetrahedronProperties(originals[2], originals[3], originals[1], pairs[2], curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(originals[1], originals[3], pairs[3], pairs[2], curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(originals[1], originals[0], pairs[3], pairs[1], curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(originals[0], pairs[2], pairs[3], pairs[1], curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(originals[0], pairs[2], pairs[1], pairs[0], curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            store.divideLocal(totalVolume);
            return totalVolume;
        }

        /**
	 *  Helper method to perform an efficient tetrahedralization of the volume under the water, to determine exactly its volume and centroid.
	 * @param underWaterIndices
	 * @param isUnderwater
	 * @param vertices
	 * @param waterPlane
	 * @param store
	 * @return
	 */
        private float resolveUnderwaterVolume_3(int[] underWaterIndices, boolean[] isUnderwater, Vector3f[] vertices, Plane waterPlane, Vector3f store) {
            int centerIndex = -1;
            int[] outerVertices = new int[2];
            int position = 0;
            for (int vertex : underWaterIndices) {
                if (countUnderwaterNeighbours(vertex, isUnderwater) == 2) {
                    centerIndex = vertex;
                } else {
                    outerVertices[position] = vertex;
                    position++;
                }
            }
            int axisA = getDifferentAxis(centerIndex, outerVertices[0]);
            int axisB = getDifferentAxis(centerIndex, outerVertices[1]);
            if (axisA == axisY && axisB == axisX) {
                int temp = outerVertices[0];
                outerVertices[0] = outerVertices[1];
                outerVertices[1] = temp;
            } else if (axisA == axisZ && axisB == axisY) {
                int temp = outerVertices[0];
                outerVertices[0] = outerVertices[1];
                outerVertices[1] = temp;
            } else if (axisA == axisX && axisB == axisZ) {
                int temp = outerVertices[0];
                outerVertices[0] = outerVertices[1];
                outerVertices[1] = temp;
            }
            Vector3f center = vertices[centerIndex];
            Vector3f outer0 = vertices[outerVertices[0]];
            Vector3f outer1 = vertices[outerVertices[1]];
            int axisForCenterToIntersectOn = getSharedAxis(centerIndex, outerVertices[0]);
            axisForCenterToIntersectOn &= getSharedAxis(centerIndex, outerVertices[1]);
            Vector3f centerIntersection = _temp0.set(getNeighbour(centerIndex, axisForCenterToIntersectOn, vertices));
            int axis = getSharedAxis(outerVertices[0], centerIndex);
            Vector3f outer0A = _temp1.set(getNeighbour(getFirstComponent(axis), outerVertices[0], vertices));
            Vector3f outer0B = _temp2.set(getNeighbour(getSecondComponent(axis), outerVertices[0], vertices));
            axis = getSharedAxis(outerVertices[1], centerIndex);
            Vector3f outer1A = _temp3.set(getNeighbour(getFirstComponent(axis), outerVertices[1], vertices));
            Vector3f outer1B = _temp4.set(getNeighbour(getSecondComponent(axis), outerVertices[1], vertices));
            boolean result = true;
            result &= intersectPairWithPlane(waterPlane, center, centerIntersection, centerIntersection, true);
            result &= intersectPairWithPlane(waterPlane, outer0, outer0A, outer0A, true);
            result &= intersectPairWithPlane(waterPlane, outer0, outer0B, outer0B, true);
            result &= intersectPairWithPlane(waterPlane, outer1, outer1A, outer1A, true);
            result &= intersectPairWithPlane(waterPlane, outer1, outer1B, outer1B, true);
            if (!result) return 0;
            Vector3f curPos = _tempVector;
            float volume = getTetrahedronProperties(center, centerIntersection, outer0, outer1, curPos);
            store.set(curPos.multLocal(volume));
            float totalVolume = volume;
            volume = getTetrahedronProperties(outer1, outer1A, outer1B, outer0, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer1, outer1B, centerIntersection, outer0, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer0, outer0A, outer0B, outer1A, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(outer0, outer0A, centerIntersection, outer1A, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            store.divideLocal(totalVolume);
            return totalVolume;
        }

        /**
	 * Helper method to perform an efficient tetrahedralization of the volume under the water, to determine exactly its volume and centroid.
	 * @param underWaterIndices
	 * @param vertices
	 * @param waterPlane
	 * @param store
	 * @return
	 */
        private float resolveUnderwaterVolume_2(int[] underWaterIndices, Vector3f[] vertices, Plane waterPlane, Vector3f store) {
            Vector3f v1 = _temp0;
            Vector3f v2 = _temp1;
            Vector3f v3 = _temp2;
            Vector3f v4 = _temp3;
            Vector3f v5 = _temp4;
            Vector3f v6 = _temp5;
            int v1Index = underWaterIndices[0];
            int v2Index = underWaterIndices[1];
            v1.set(vertices[v1Index]);
            v2.set(vertices[v2Index]);
            int axis = getSharedAxis(v1Index, v2Index);
            int axis1 = getFirstComponent(axis);
            int axis2 = getSecondComponent(axis);
            v3.set(getNeighbour(axis1, v1Index, vertices));
            v4.set(getNeighbour(axis1, v2Index, vertices));
            v5.set(getNeighbour(axis2, v1Index, vertices));
            v6.set(getNeighbour(axis2, v2Index, vertices));
            boolean result = true;
            result &= intersectPairWithPlane(waterPlane, v1, v3, v3, true);
            result &= intersectPairWithPlane(waterPlane, v1, v5, v5, true);
            result &= intersectPairWithPlane(waterPlane, v2, v4, v4, true);
            result &= intersectPairWithPlane(waterPlane, v2, v6, v6, true);
            if (!result) return -1;
            Vector3f curPos = _tempVector;
            float volume = getTetrahedronProperties(v1, v2, v3, v5, curPos);
            store.set(curPos.multLocal(volume));
            float totalVolume = volume;
            volume = getTetrahedronProperties(v4, v2, v3, v6, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            volume = getTetrahedronProperties(v3, v2, v5, v6, curPos);
            store.addLocal(curPos.multLocal(volume));
            totalVolume += volume;
            store.divideLocal(totalVolume);
            return totalVolume;
        }

        /**
	 * Helper method to perform an efficient tetrahedralization of the volume under the water, to determine exactly its volume and centroid.
	 * @param underWaterIndices
	 * @param vertices
	 * @param waterPlane
	 * @param store
	 * @return
	 */
        private float resolveUnderwaterVolume_1(int[] underWaterIndices, Vector3f[] vertices, Plane waterPlane, Vector3f store) {
            int vertexIndex = underWaterIndices[0];
            int zNeighbour = getNeighbourIndex(axisZ, vertexIndex);
            int yNeighbour = vertexIndex ^ axisY;
            int xNeighbour = vertexIndex ^ axisX;
            boolean result = true;
            result &= intersectPairWithPlane(waterPlane, vertices[vertexIndex], vertices[zNeighbour], vertices[zNeighbour], true);
            result &= intersectPairWithPlane(waterPlane, vertices[vertexIndex], vertices[yNeighbour], vertices[yNeighbour], true);
            result &= intersectPairWithPlane(waterPlane, vertices[vertexIndex], vertices[xNeighbour], vertices[xNeighbour], true);
            if (!result) return -1;
            return (getTetrahedronProperties(vertices[vertexIndex], vertices[zNeighbour], vertices[yNeighbour], vertices[xNeighbour], store));
        }

        /**
     * For debugging. Note: The reason why the tetrahedron appears slightly off as compared to the shape is likely because the shape has its physics updated before rendering, but after this method is called.
     * @param point1
     * @param point2
     * @param point3
     * @param point4
     */
        private void drawTetrahedron(Vector3f point1, Vector3f point2, Vector3f point3, Vector3f point4) {
            Tetrahedron tet = new Tetrahedron("", point1, point2, point3, point4);
            MaterialState m = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
            m.setAmbient(ColorRGBA.randomColor());
            m.setDiffuse(ColorRGBA.randomColor());
            tet.setRenderState(m);
            tet.updateRenderState();
            tet.getLocalTranslation().set(object.getSpatial().getWorldTranslation());
            if (debugMode) {
                object.debugNode.attachChild(tet);
                object.debugNode.updateRenderState();
            }
        }

        /**
     * The centroid of a tetrahedron (or any other simplex) is just the average of each point.
     * The volume of the tetrahedron is given by this formula, where a, b, c and d are the four vertices, in any order: 
     * V = abs[(a-d) dot ((b-d)cross(c-d))]/6
     * 
     * See:
     * http://en.wikipedia.org/wiki/Tetrahedron
     * http://en.wikipedia.org/wiki/Centroid
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @param store
     * @return
     */
        private float getTetrahedronProperties(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Vector3f store) {
            _aMinusD.set(v1).subtractLocal(v4);
            _bMinusD.set(v2).subtractLocal(v4);
            _cMinusD.set(v3).subtractLocal(v4);
            Vector3f crossProduct = _bMinusD.crossLocal(_cMinusD);
            float volume = FastMath.abs(_aMinusD.dot(crossProduct)) / 6f;
            store.set(v1);
            store.addLocal(v2);
            store.addLocal(v3);
            store.addLocal(v4);
            store.divideLocal(4);
            if (debugMode) drawTetrahedron(v1.subtract(object.getSpatial().getWorldTranslation()), v2.subtract(object.getSpatial().getWorldTranslation()), v3.subtract(object.getSpatial().getWorldTranslation()), v4.subtract(object.getSpatial().getWorldTranslation()));
            return volume;
        }

        private int countUnderwaterNeighbours(int index, boolean[] isUnderwater) {
            int zNeighbour = index ^ axisZ;
            int yNeighbour = index ^ axisY;
            int xNeighbour = index ^ axisX;
            int count = 0;
            if (isUnderwater[zNeighbour]) count++;
            if (isUnderwater[yNeighbour]) count++;
            if (isUnderwater[xNeighbour]) count++;
            return count;
        }
    }

    public float getDragCoefficient() {
        return dragCoefficient;
    }

    public void setDragCoefficient(float dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
    }
}
