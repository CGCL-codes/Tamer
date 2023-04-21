package org.xith3d.render.prerender;

import java.util.Comparator;
import javax.vecmath.Point3f;
import org.xith3d.scenegraph.Node;
import org.xith3d.utility.comparator.Sorter;

/**
 * A collection of transparent RenderAtoms that will be prioritized and sorted
 * for rendering.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class TransparentRenderBin extends RenderBin {

    /**
     * No sorting is done
     */
    public static final int SORT_NONE = 0;

    /**
     * Transparent shapes are sorted front-to-back
     */
    public static final int SORT_FRONT_TO_BACK = 1;

    /**
     * Transparent shapes are sorted by bounding spheres and eye
     * ray intersection
     */
    public static final int SORT_BOUNDING_SPHERE_AND_EYE_RAY_INTERSECTION = 2;

    /**
     * Transparent shapes are sorted by z-value
     */
    public static final int SORT_BY_Z_VALUE = 3;

    private Comparator<RenderBucket> atomComparator = ComparatorFactory.newAtomComparator();

    private Comparator<RenderBucket> orderedBackToFrontComparator = ComparatorFactory.newOrderedBackToFrontComparator();

    private Comparator<RenderBucket> backToFrontComparator = ComparatorFactory.newBackToFrontComparator();

    private Comparator<RenderBucket> frontToBackComparator = ComparatorFactory.newFrontToBackComparator();

    private Comparator<RenderBucket> zValueComparator = ComparatorFactory.newZValueComparator();

    private StatePriorities priorities;

    private DynamicBucketArray buckets;

    private int iterationPointer = 0;

    private Point3f tmpPos = new Point3f();

    public TransparentRenderBin(int initialCapacity, StatePriorities priorities, String name) {
        super(name);
        this.priorities = priorities;
        this.buckets = new DynamicBucketArray(initialCapacity);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return (buckets.size());
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        buckets.clear();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void swapData(boolean isBeforeRendered) {
        buckets.get(iterationPointer - 1).swapData(isBeforeRendered);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public RenderAtom<? extends Node> getAtom(int index) {
        return (buckets.get(index).getAtom());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void resetIterationPointer() {
        iterationPointer = 0;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public RenderAtom<? extends Node> getNextAtom() {
        if (iterationPointer >= buckets.size()) return (null);
        return (buckets.get(iterationPointer++).getAtom());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void addAtom(RenderAtom<? extends Node> atom, long frameId, boolean isShared) {
        final RenderBucket bucket = buckets.append(atom.getRenderBucket(frameId));
        bucket.updateTransform(isShared);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public void dump() {
        System.out.println("");
        RenderAtom<? extends Node> atom;
        for (int j = 0; j < buckets.size(); j++) {
            System.out.print("Atom " + j + ": ");
            atom = buckets.get(j).getAtom();
            for (int i = 0; i < priorities.numStatePriorities; i++) {
                StateSortable ss1 = atom.getSortableStates().map[priorities.statePriorities[i]];
                if (ss1 == null) System.out.print(" " + priorities.statePriorities[i] + ":-1"); else {
                    long bid1 = atom.getSortableStates().map[priorities.statePriorities[i]].getStateId();
                    System.out.print(" " + priorities.statePriorities[i] + ":" + bid1);
                }
            }
            System.out.println("");
        }
    }

    private final void updateDistancesToView(Point3f viewPosition) {
        for (int i = 0; i < buckets.size(); i++) {
            buckets.get(i).getAtom().getPosition(tmpPos);
            buckets.get(i).setDistanceToView((viewPosition != null ? tmpPos.distanceSquared(viewPosition) : 0.0f), tmpPos.z);
        }
    }

    /**
     * Sorts the render bin from closest to furthest using bounding shpere center
     * as reference point.
     * 
     * @param viewPosition the current View's position
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private final void sortBackToFront(Point3f viewPosition) {
        updateDistancesToView(viewPosition);
        Sorter.quickSort(buckets.getRawArray(), 0, buckets.size() - 1, backToFrontComparator);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    private final void sortOrderedBackToFront(Point3f viewPosition) {
        updateDistancesToView(viewPosition);
        Sorter.quickSort(buckets.getRawArray(), 0, buckets.size() - 1, orderedBackToFrontComparator);
    }

    /**
     * Performs a sort based on shader, transform, geometry and other criteria.
     * third by shader.
     * 
     * @param viewPosition the current View's position
     */
    @SuppressWarnings({ "unchecked", "unused" })
    private final void sortByAtom(Point3f viewPosition) {
        Sorter.quickSort(buckets.getRawArray(), 0, buckets.size() - 1, atomComparator);
    }

    /**
     * Sorts the render bin from closest to furthest.
     * 
     * @param viewPosition the current View's position
     */
    @SuppressWarnings("unchecked")
    private final void sortFrontToBack(Point3f viewPosition) {
        updateDistancesToView(viewPosition);
        Sorter.quickSort(buckets.getRawArray(), 0, buckets.size() - 1, frontToBackComparator);
    }

    /**
     * Sorts the render bin from closest to furthest using bounding shpere - eye
     * ray intersection as reference point.
     * 
     * @param viewPosition the current View's position
     */
    @SuppressWarnings("unchecked")
    private final void sortBackToFrontByBoundingSphereAndEyeRayIntersection(Point3f viewPosition) {
        for (int i = 0; i < buckets.size(); i++) {
            final RenderBucket bucket = buckets.get(i);
            bucket.getAtom().getPosition(tmpPos);
            bucket.setDistanceToView(tmpPos.distanceSquared(viewPosition), tmpPos.z);
            bucket.setDistanceToView(bucket.getDistanceToView() - bucket.getAtom().getNode().getVworldBounds().getRadius(), tmpPos.z);
        }
        Sorter.quickSort(buckets.getRawArray(), 0, buckets.size() - 1, backToFrontComparator);
    }

    /**
     * Sorts the render bin from closest to furthest.
     */
    @SuppressWarnings("unchecked")
    public final void sortByZValue() {
        updateDistancesToView(null);
        Sorter.quickSort(buckets.getRawArray(), 0, buckets.size() - 1, zValueComparator);
    }

    /**
     * {@inheritDoc}
     */
    public void sort(int sortingPolicy, Point3f viewPosition) {
        if ((sortingPolicy == OpaqueRenderBin.SORT_NONE) || (buckets.size() == 0)) return;
        switch(sortingPolicy) {
            case TransparentRenderBin.SORT_FRONT_TO_BACK:
                sortFrontToBack(viewPosition);
                break;
            case TransparentRenderBin.SORT_BOUNDING_SPHERE_AND_EYE_RAY_INTERSECTION:
                sortBackToFrontByBoundingSphereAndEyeRayIntersection(viewPosition);
                break;
            case TransparentRenderBin.SORT_BY_Z_VALUE:
                sortByZValue();
                break;
        }
    }
}
