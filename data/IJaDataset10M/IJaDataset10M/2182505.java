package engine;

import java.util.ArrayList;
import engine.collision.CollisionContext;
import engine.shapes.AABox;
import engine.shapes.Body;

/**
 * A strategy that divides the space into 4 repeatedly until either the target
 * number of bodies is reached or the a given level of subdivisions is reached.
 * (fast)
 * 
 * @author Jeff Ahern
 * @author Matt DePortor
 * @author Keith Kowalski
 * @author Mcomber, Kevin
 */
public class Strategy {

    private ArrayList<Space> spaces = new ArrayList<Space>();

    private int maxLevels;

    private int maxInSpace;

    /**
	 * Create a new strategy
	 * 
	 * @param maxInSpace
	 *            The maximum number of bodies in a given space acceptable
	 * @param maxLevels
	 *            The number of sub divisions allows
	 */
    public Strategy(int maxInSpace, int maxLevels) {
        this.maxInSpace = maxInSpace;
        this.maxLevels = maxLevels;
    }

    /**
	 * @see engine.strategies.BroadCollisionStrategy#collideBodies(engine.collision.CollisionContext,
	 *      engine.BodyList, float)
	 */
    public void collideBodies(CollisionContext context, BodyList bodies, float dt) {
        spaces.clear();
        Space space = new Space(0, 0, 0, 0);
        for (int i = 0; i < bodies.size(); i++) {
            Body body = bodies.get(i);
            space.addAABox(body.getShape().getBounds(), body.getPosition().getX(), body.getPosition().getY());
            space.addBody(body);
        }
        splitSpace(space, 0, maxInSpace, spaces);
        for (int i = 0; i < spaces.size(); i++) {
            context.resolve(spaces.get(i), dt);
        }
    }

    /**
	 * Get the spaces dervied in the quad process
	 * 
	 * @return The list of spaces dervied (QuadSpaceStrategy.Space)
	 */
    public ArrayList<Space> getSpaces() {
        return spaces;
    }

    /**
	 * Considering splitting a space into 4 sub-spaces
	 * 
	 * @param space
	 *            The space to subdivide
	 * @param level
	 *            The number of levels of subdivision allowed
	 * @param target
	 *            The target number of bodies per space
	 * @param spaceList
	 *            The list of spaces to populate
	 * @return True if the target has found
	 */
    private boolean splitSpace(Space space, int level, int target, ArrayList<Space> spaceList) {
        if (space.size() <= target) {
            spaceList.add(space);
            return true;
        }
        if (level > maxLevels) {
            spaceList.add(space);
            return true;
        }
        Space[] spaces = space.getQuadSpaces();
        for (int j = 0; j < 4; j++) {
            splitSpace(spaces[j], level + 1, target, spaceList);
        }
        return false;
    }

    /**
	 * A single space within the quad-tree
	 * 
	 * @author Kevin Glass
	 */
    public class Space extends BodyList {

        public float x1;

        public float y1;

        public float x2;

        public float y2;

        /**
		 * Create a space within the quad tree
		 * 
		 * @param x
		 *            The x position of the space
		 * @param y
		 *            The y position of the space
		 * @param width
		 *            The width of the space
		 * @param height
		 *            The height of the space
		 */
        public Space(float x, float y, float width, float height) {
            this.x1 = x;
            this.y1 = y;
            this.x2 = x + width;
            this.y2 = y + height;
        }

        /**
		 * Add a pody to the space
		 * 
		 * @param body
		 *            The body to add to the space
		 */
        public void addBody(Body body) {
            add(body);
        }

        /**
		 * Sub-divide this space into four seperate sub-spaces dolling out the
		 * bodies into each space
		 * 
		 * @return The spaces created by the subdivision (always length 4)
		 */
        public Space[] getQuadSpaces() {
            Space[] spaces = new Space[4];
            float width = (this.x2 - this.x1) / 2;
            float height = (this.y2 - this.y1) / 2;
            spaces[0] = new Space(x1, y1, width, height);
            spaces[1] = new Space(x1, y1 + height, width, height);
            spaces[2] = new Space(x1 + width, y1, width, height);
            spaces[3] = new Space(x1 + width, y1 + height, width, height);
            for (int i = 0; i < size(); i++) {
                Body body = get(i);
                for (int j = 0; j < 4; j++) {
                    if (spaces[j].touches(body.getShape().getBounds(), body.getPosition().getX(), body.getPosition().getY())) {
                        spaces[j].add(body);
                    }
                }
            }
            return spaces;
        }

        /**
		 * Combine this space with another box
		 * 
		 * @param box
		 *            The box to include in this space
		 * @param xp
		 *            The x position of the box
		 * @param yp
		 *            The y position of the box
		 */
        public void addAABox(AABox box, float xp, float yp) {
            float x1 = xp - box.getWidth();
            float x2 = xp + box.getWidth();
            float y1 = yp - box.getHeight();
            float y2 = yp + box.getHeight();
            this.x1 = Math.min(x1, this.x1);
            this.y1 = Math.min(y1, this.y1);
            this.x2 = Math.max(x2, this.x2);
            this.y2 = Math.max(y2, this.y2);
        }

        /**
		 * Check if this space touches a box
		 * 
		 * @param box
		 *            The box to check against
		 * @param xp
		 *            The x position of the box to check
		 * @param yp
		 *            The y position of the box to check
		 * @return True if the box touches these space
		 */
        public boolean touches(AABox box, float xp, float yp) {
            float thisWidth = (this.x2 - this.x1) / 2;
            float thisHeight = (this.y2 - this.y1) / 2;
            float thisCx = this.x1 + thisWidth;
            float thisCy = this.y1 + thisHeight;
            float x1 = xp - (box.getWidth() / 2);
            float x2 = xp + (box.getWidth() / 2);
            float y1 = yp - (box.getHeight() / 2);
            float y2 = yp + (box.getHeight() / 2);
            float otherWidth = (x2 - x1) / 2;
            float otherHeight = (y2 - y1) / 2;
            float otherCx = xp;
            float otherCy = yp;
            float dx = Math.abs(thisCx - otherCx);
            float dy = Math.abs(thisCy - otherCy);
            float totalWidth = thisWidth + otherWidth;
            float totalHeight = thisHeight + otherHeight;
            return (totalWidth > dx) && (totalHeight > dy);
        }

        /**
		 * Get the top left x coordinate of this space
		 * 
		 * @return The top left x coordinate of this space
		 */
        public float getX1() {
            return x1;
        }

        /**
		 * Get the bottom right x coordinate of this space
		 * 
		 * @return The bottom right x coordinate of this space
		 */
        public float getX2() {
            return x2;
        }

        /**
		 * Get the top left y coordinate of this space
		 * 
		 * @return The top left y coordinate of this space
		 */
        public float getY1() {
            return y1;
        }

        /**
		 * Get the bottom right y coordinate of this space
		 * 
		 * @return The bottom right y coordinate of this space
		 */
        public float getY2() {
            return y2;
        }

        /**
		 * @see java.lang.Object#toString()
		 */
        public String toString() {
            return "[Space " + x1 + "," + y1 + " " + x2 + "," + y2 + " " + size() + " bodies]";
        }
    }
}
