package org.hexahedron.collision;

import org.hexahedron.cube.CubeGrid;
import org.hexahedron.geom.Vector3iDefault;
import org.hexahedron.geom.Vector3i;
import org.hexahedron.util.VectorUtils;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class OctoBox implements CollisionGeom {

    /**
	 * The half-sizes of the cube in each axis
	 */
    private Vector3f half = new Vector3f();

    private Vector3f position = new Vector3f();

    private Vector3f leading = new Vector3f();

    /**
	 * The next unit coords to be passed in each direction,
	 * above and below the octobox.
	 * 
	 * The first index is 0 for the bound below, 1 for above
	 * The second index is the axis
	 * 
	 * So for example, if bounds[0][0] is 5, that means that
	 * when moving in the negative x direction, the next plane
	 * to be passed through by the box boundary is the x=5 plane.
	 * No part of the box has moved to an x value strictly
	 * less than 5. This means that if we are moving in the 
	 * negative x direction, we look for collisions when the
	 * leading point moves through x=5. It also means that if
	 * we have zero x velocity, there is no need to check for
	 * collisions with any faces of cubes having x position 4 (since
	 * these are the cubes on the x-negative side of x=5)
	 * 
	 * Similarly, if bounds[1][1] is 0, this means that the
	 * octobox is entirely regions with negative or zero
	 * y value, etc.  
	 * 
	 * When movement causes a check for collision with a unit plane,
	 * this means that the box is considered to have moved at least
	 * some tiny distance past that unit plane, and so the
	 * bounds must be incremented (or decremented) on this axis - this
	 * means that the unit plane will not be checked again for movement
	 * on this axis, and also that it is not possible to hit cubes 
	 * past this plane.
	 * 
	 */
    private int[][] bounds = new int[2][3];

    /**
	 * Indicates whether the octobox is touching the cubegrid on each
	 * side of its bounds
	 * 
	 * The first index is 0 for the bound/face in negative axis direction, 1 for above
	 * The second index is the axis
	 * 
	 * So for example if bounds[0][2] is true, the box is "standing on" something below
	 * 
	 * This is very closely related to "aligned" - a face can only be touching
	 * if it is aligned, but may be aligned without touching - it depends whether
	 * there are actually any cubes on the far side of the aligned plane, within
	 * the integer bounds of the octobox
	 */
    private boolean[][] touching = new boolean[2][3];

    /**
	 * Indicates whether the octobox is EXACTLY with aligned the cubegrid on each
	 * side of its bounds
	 * 
	 * The first index is 0 for the bound/face in negative axis direction, 1 for above
	 * The second index is the axis
	 * 
	 * So for example if bounds[0][2] is true, the box has its bottom face EXACTLY
	 * aligned with a dividing plane of the cube grid
	 * 
	 * This is very closely related to "touching" - a face can only be touching
	 * if it is aligned, but may be aligned without touching - it depends whether
	 * there are actually any cubes on the far side of the aligned plane, within
	 * the integer bounds of the octobox
	 * 
	 */
    private boolean[][] aligned = new boolean[2][3];

    private Vector3i minSearch = new Vector3iDefault();

    private Vector3i maxSearch = new Vector3iDefault();

    private Vector3i search = new Vector3iDefault();

    private Vector3i nextBoundaries = new Vector3iDefault();

    private NextCollisionReceiver r = new NextCollisionReceiver();

    private Vector3f slideV = new Vector3f();

    private CubeGrid grid;

    /**
	 * Heading - this is a temp vector that gives the direction of
	 * motion in each axis - 1 for increasing, -1 for decreasing,
	 * 0 if velocity is exactly 0
	 */
    private Vector3i heading = new Vector3iDefault();

    public OctoBox(CubeGrid grid, Vector3f half, Vector3f position) {
        this.grid = grid;
        this.half.set(half);
        this.position.set(position);
        for (int axis = 0; axis < 3; axis++) {
            float pos = position.get(axis);
            float h = half.get(axis);
            bounds[0][axis] = (int) FastMath.floor(pos - h);
            bounds[1][axis] = (int) FastMath.ceil(pos + h);
            touching[0][axis] = false;
            touching[1][axis] = false;
            aligned[0][axis] = false;
            aligned[1][axis] = false;
        }
    }

    /**
	 * Get the box center position. Please note that if you change
	 * this directly, collisions will not occur - use {@link #slide(Vector3f, float, CubeGrid, CollisionReceiver)}
	 * to slide the box through a {@link CubeGrid} with collisions
	 * @return
	 * 		The {@link OctoBox} center position
	 */
    public Vector3f getPosition() {
        return position;
    }

    private void moveAndUpdate(Vector3f velocity, float time) {
        if (time == 0) return;
        for (int j = 0; j < 3; j++) {
            position.set(j, position.get(j) + time * velocity.get(j));
        }
        for (int j = 0; j < 3; j++) {
            float pos = position.get(j);
            float h = half.get(j);
            if (heading.get(j) != 0) {
                if (heading.get(j) == 1) {
                    bounds[0][j] = (int) FastMath.floor(pos - h);
                } else if (heading.get(j) == -1) {
                    bounds[1][j] = (int) FastMath.ceil(pos + h);
                }
                touching[0][j] = false;
                touching[1][j] = false;
                aligned[0][j] = false;
                aligned[1][j] = false;
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int direction = 0; direction < 1; direction++) {
                if (aligned[direction][j]) {
                    int boundary = bounds[direction][j];
                    if (direction == 0) boundary--;
                    touching[direction][j] = scanInPlane(j, boundary);
                }
            }
        }
    }

    /**
	 * Scan for any cubes in the specified plane bordering the octobox, within
	 * the bounds of the octobox
	 * @param grid
	 * 		The grid to scan
	 * @param axis
	 * 		The normal axis of the plane
	 * @param position
	 * 		The coordinate of the plane
	 * @return
	 * 		True iff there is a cube
	 */
    private boolean scanInPlane(int axis, int position) {
        for (int i = 0; i < 3; i++) {
            if (i == axis) {
                minSearch.set(i, position);
                maxSearch.set(i, position);
            } else {
                minSearch.set(i, bounds[0][i]);
                maxSearch.set(i, bounds[1][i] - 1);
            }
        }
        boolean collided = false;
        for (int x = minSearch.getX(); x <= maxSearch.getX(); x++) {
            for (int y = minSearch.getY(); y <= maxSearch.getY(); y++) {
                for (int z = minSearch.getZ(); z <= maxSearch.getZ(); z++) {
                    search.set(x, y, z);
                    if (grid.getPresence(search)) {
                        collided = true;
                    }
                }
            }
        }
        return collided;
    }

    public void slide(Vector3f velocity, float maxTime, CollisionReceiver receiver) {
        float elapsedTime = 0;
        updateHeading(velocity);
        updateLeading();
        for (int repeats = 0; repeats < 100000; repeats++) {
            if (VectorUtils.isZero(velocity)) return;
            for (int i = 0; i < 3; i++) {
                int h = heading.get(i);
                if (h == 1) {
                    nextBoundaries.set(i, bounds[1][i]);
                } else {
                    nextBoundaries.set(i, bounds[0][i]);
                }
            }
            float minTime = Float.MAX_VALUE;
            int minAxis = 0;
            for (int i = 0; i < 3; i++) {
                float vi = velocity.get(i);
                if (vi != 0) {
                    float t = (nextBoundaries.get(i) - leading.get(i)) / vi;
                    if (t < minTime) {
                        minTime = t;
                        minAxis = i;
                    }
                }
            }
            if (elapsedTime + minTime > maxTime) {
                moveAndUpdate(velocity, maxTime - elapsedTime);
                return;
            }
            moveAndUpdate(velocity, minTime);
            elapsedTime += minTime;
            updateLeading();
            int headingMinAxis = heading.get(minAxis);
            int collisionPlaneCubeIndex = nextBoundaries.get(minAxis);
            if (headingMinAxis < 0) collisionPlaneCubeIndex--;
            for (int i = 0; i < 3; i++) {
                if (i == minAxis) {
                    minSearch.set(i, collisionPlaneCubeIndex);
                    maxSearch.set(i, collisionPlaneCubeIndex);
                } else {
                    minSearch.set(i, bounds[0][i]);
                    maxSearch.set(i, bounds[1][i] - 1);
                }
            }
            boolean collided = false;
            for (int x = minSearch.getX(); x <= maxSearch.getX(); x++) {
                for (int y = minSearch.getY(); y <= maxSearch.getY(); y++) {
                    for (int z = minSearch.getZ(); z <= maxSearch.getZ(); z++) {
                        search.set(x, y, z);
                        if (grid.getPresence(search)) {
                            collided = true;
                        }
                    }
                }
            }
            if (collided) {
                touching[headingMinAxis > 0 ? 1 : 0][minAxis] = true;
                aligned[headingMinAxis > 0 ? 1 : 0][minAxis] = true;
                if (!receiver.acceptCollision(elapsedTime, position, minAxis, position)) {
                    return;
                }
            } else {
                if (headingMinAxis > 0) {
                    bounds[1][minAxis]++;
                } else {
                    bounds[0][minAxis]--;
                }
            }
        }
    }

    public int[][] getBounds() {
        return bounds;
    }

    public boolean[][] getTouching() {
        return touching;
    }

    public boolean[][] getAligned() {
        return aligned;
    }

    private void updateLeading() {
        leading.set(position);
        for (int i = 0; i < 3; i++) {
            leading.set(i, leading.get(i) + heading.get(i) * half.get(i));
        }
    }

    private void updateHeading(Vector3f velocity) {
        for (int i = 0; i < 3; i++) {
            int val = 0;
            double vComp = velocity.get(i);
            if (vComp > 0) {
                val = 1;
            } else if (vComp < 0) {
                val = -1;
            }
            heading.set(i, val);
        }
    }

    /**
	 * Slide the box through a grid, making sure we slide along
	 * any cubes we hit in the grid.
	 * @param v
	 * 		The velocity at which to move
	 * @param maxTime
	 * 		The maximum time for which to move
	 * @param grid
	 * 		The grid we are moving through
	 */
    public void slideAlong(Vector3f v, float maxTime) {
        float elapsedTime = 0;
        slideV.set(v);
        while ((elapsedTime < maxTime) && (!VectorUtils.isZero(slideV))) {
            r.reset();
            slide(slideV, maxTime - elapsedTime, r);
            if (!r.collided()) return;
            elapsedTime += r.getElapsedTime();
            slideV.set(r.getCollisionAxis(), 0);
        }
    }

    public CubeGrid getGrid() {
        return grid;
    }
}
