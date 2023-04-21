package mudcartographer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Manages a collection of Rooms that, together, form a map.
 */
public class MudMap {

    private static Map<Room, java.util.List<Room>> drawnLines = new HashMap<Room, java.util.List<Room>>();

    private Point[] maxPoints = { new Point(0, 0), new Point(0, 0) };

    private Graphics2D graphics2D;

    private BufferedImage bufferedImage;

    private Room initialRoom;

    private Room currentRoom;

    private Room previousRoom;

    private Point initialPoint = new Point(0, 0);

    private int currentOperationID;

    private boolean isChanged;

    private boolean isCurrentRoomChanged;

    private boolean hasNewConnection;

    private static Map<Integer, Direction> keyEventDirections = new HashMap<Integer, Direction>();

    public MudMap() {
    }

    static {
        keyEventDirections.put(KeyEvent.VK_8, Direction.NORTH);
        keyEventDirections.put(KeyEvent.VK_UP, Direction.NORTH);
        keyEventDirections.put(KeyEvent.VK_NUMPAD8, Direction.NORTH);
        keyEventDirections.put(KeyEvent.VK_2, Direction.SOUTH);
        keyEventDirections.put(KeyEvent.VK_DOWN, Direction.SOUTH);
        keyEventDirections.put(KeyEvent.VK_NUMPAD2, Direction.SOUTH);
        keyEventDirections.put(KeyEvent.VK_4, Direction.WEST);
        keyEventDirections.put(KeyEvent.VK_LEFT, Direction.WEST);
        keyEventDirections.put(KeyEvent.VK_NUMPAD4, Direction.WEST);
        keyEventDirections.put(KeyEvent.VK_6, Direction.EAST);
        keyEventDirections.put(KeyEvent.VK_RIGHT, Direction.EAST);
        keyEventDirections.put(KeyEvent.VK_NUMPAD6, Direction.EAST);
        keyEventDirections.put(KeyEvent.VK_7, Direction.NW);
        keyEventDirections.put(KeyEvent.VK_NUMPAD7, Direction.NW);
        keyEventDirections.put(KeyEvent.VK_9, Direction.NE);
        keyEventDirections.put(KeyEvent.VK_NUMPAD9, Direction.NE);
        keyEventDirections.put(KeyEvent.VK_1, Direction.SW);
        keyEventDirections.put(KeyEvent.VK_NUMPAD1, Direction.SW);
        keyEventDirections.put(KeyEvent.VK_3, Direction.SE);
        keyEventDirections.put(KeyEvent.VK_NUMPAD3, Direction.SE);
    }

    public Room getInitialRoom() {
        return initialRoom;
    }

    public void setInitialRoom(Room initialRoom) {
        this.initialRoom = initialRoom;
        setCurrentRoom(initialRoom);
    }

    public Point getInitialPoint() {
        return initialPoint;
    }

    public void setInitialPoint(Point initialPoint) {
        this.initialPoint = initialPoint;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Set the current room (for highlighting), mark the current rooms as changed
     * and set the previous room to, wonder of wonders, the previous room
     *
     * @param currentRoom the new current room
     */
    public void setCurrentRoom(Room currentRoom) {
        if (this.currentRoom == null || !this.currentRoom.equals(currentRoom)) {
            setPreviousRoom(this.currentRoom);
            setCurrentRoomChanged(true);
        }
        this.currentRoom = currentRoom;
    }

    public Room getPreviousRoom() {
        return previousRoom;
    }

    public void setPreviousRoom(Room previousRoom) {
        this.previousRoom = previousRoom;
    }

    public int getNewCurrentOperationID() {
        return currentOperationID += 1;
    }

    /**
     * Has the map changed?
     *
     * @return whether the map has changed
     */
    public boolean isChanged() {
        return isChanged;
    }

    /**
     * Flag the map as changed (i.e. we've added a room)
     *
     * @param changed flag stating if the map has changed
     */
    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    /**
     * Is the current room now a different room?
     *
     * Note: this is manual
     *
     * @return true if the current room is not a different room
     */
    public boolean isCurrentRoomChanged() {
        return isCurrentRoomChanged;
    }

    public boolean hasNewConnection() {
        return hasNewConnection;
    }

    public void setHasNewConnection(boolean hasNewConnection) {
        this.hasNewConnection = hasNewConnection;
    }

    /**
     * Mark the current room as changed or unchanged
     *
     * @param currentRoomChanged flag saying whether the room has been changed or not
     */
    public void setCurrentRoomChanged(boolean currentRoomChanged) {
        isCurrentRoomChanged = currentRoomChanged;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Graphics2D getGraphics2D() {
        return graphics2D;
    }

    /**
     * Create a new room in the map in the direction specified, relative to the current room
     * @param d the direction, relative to the current room, to create the new room in
     * @return the new room that was created
     */
    public Room createRoom(Direction d) {
        return createRoom(d, Room.DEFAULT_SYMBOL);
    }

    /**
     * Create a new room in the map in the direction specified, relative to the current room
     * @param d the direction, relative to the current room, to create the new room in
     * @param symbol the symbol to use for the new room (i.e. it's "icon")
     * @return the new room that was created
     */
    public Room createRoom(Direction d, char symbol) {
        Room newRoom = new Room(symbol);
        currentRoom.setRoom(newRoom, d);
        newRoom.setRoom(currentRoom, d.getOpposite());
        setCurrentRoom(newRoom);
        setChanged(true);
        return newRoom;
    }

    /**
     * Wrapper for selectOrCreateRoom
     * @param e key event specifying direction to move
     * @return the new current room with focus
     */
    public Room selectOrCreateRoom(KeyEvent e) {
        Direction d = getKeyDirection(e);
        if (d != null) {
            return selectOrCreateRoom(d);
        }
        return currentRoom;
    }

    /**
     * Move the focus to a new room; create the room if it doesn't exist
     * @param d the direction to move
     * @return the new current room with focus
     */
    public Room selectOrCreateRoom(Direction d) {
        Room newCurrentRoom;
        Point origin = new Point(0, 0);
        currentOperationID = getNewCurrentOperationID();
        if ((newCurrentRoom = currentRoom.getRoom(d)) != null) {
            setCurrentRoom(newCurrentRoom);
        } else if ((newCurrentRoom = getRoomAtPoint(currentRoom, origin, d.translatePoint(origin))) != null) {
            currentRoom.setRoom(newCurrentRoom, d);
            newCurrentRoom.setRoom(currentRoom, d.getOpposite());
            setCurrentRoom(newCurrentRoom);
            setHasNewConnection(true);
        } else {
            setCurrentRoom(createRoom(d));
        }
        return currentRoom;
    }

    /**
     * Walk through each room in the map and check if any room matches the
     * coordinates of the direction specified
     *
     * @param r the room to check to see if it is at the target coordinates
     * @param p the coordinates of the room to check
     * @param targetPoint the target coordinates to check for a room
     * @return the adjacent room if one exists or null
     */
    private Room getRoomAtPoint(Room r, Point p, Point targetPoint) {
        Room nextRoom;
        Room adjacentRoom;
        if (p.equals(targetPoint) || r == null) {
            return r;
        }
        r.setCurrentOperationID(currentOperationID);
        for (Direction d : MudMap.Direction.values()) {
            nextRoom = r.getRoom(d);
            if (nextRoom != null && nextRoom.getCurrentOperationID() < currentOperationID && (adjacentRoom = getRoomAtPoint(nextRoom, d.translatePoint(p), targetPoint)) != null) {
                return adjacentRoom;
            }
        }
        return null;
    }

    /**
     * Return a point that represents the farthest possible room to the north and west.
     * This will tell us how far from the top of the screen and from the left of the screen
     * we need to draw the initial room in order to have enough space to draw rooms to the west
     * and north of that room.
     *
     * @return a point representing the farthest north and farthest west that any rooms are (not, however, a
     *         point representing a single room)
     */
    public Point[] getMaxPoints() {
        Point[] maxPoints = new Point[] { new Point(0, 0), new Point(0, 0) };
        currentOperationID = getNewCurrentOperationID();
        return getNorthWesternmostPoint(initialRoom, initialPoint, maxPoints);
    }

    public void clearDrawnLines() {
        drawnLines.clear();
    }

    /**
     * Simple constants for keeping track of which point
     * in an array of "max" points is NW and which is SE
     */
    public enum Points {

        NW, SE
    }

    /**
     * Recursively travel through all rooms comparing each rooms point, relative to a
     * starting point, to a "max" point and seeing if the room is farther north, or
     * farther west, than the current "max". It does not have to be both, all we're
     * looking for is how much space to leave on the screen to the north and west of
     * the initial room when we draw the map.
     *
     * @param r the room to check against the current "max" coordinate
     * @param p the point the room is at, relative to some origin room
     * @param maxPoints an array of the two current "max" coordinates to compare this room's coordinates to
     * @return the "max" coordinate after we've made any necessary updates to it
     */
    private Point[] getNorthWesternmostPoint(final Room r, final Point p, Point[] maxPoints) {
        Room nextRoom;
        Point maxNW = maxPoints[Points.NW.ordinal()];
        Point maxSE = maxPoints[Points.SE.ordinal()];
        r.setCurrentOperationID(currentOperationID);
        if (p.getX() > maxNW.getX()) {
            maxNW.move((int) p.getX(), (int) maxNW.getY());
        }
        if (p.getY() > maxNW.getY()) {
            maxNW.move((int) maxNW.getX(), (int) p.getY());
        }
        if (p.getX() < maxSE.getX()) {
            maxSE.move((int) p.getX(), (int) maxSE.getY());
        }
        if (p.getY() < maxSE.getY()) {
            maxSE.move((int) maxSE.getX(), (int) p.getY());
        }
        for (MudMap.Direction d : MudMap.Direction.values()) {
            nextRoom = r.getRoom(d);
            if (nextRoom != null && nextRoom.getCurrentOperationID() < currentOperationID) {
                maxPoints = getNorthWesternmostPoint(nextRoom, d.translatePoint(p, Direction.CoordinateOrigin.ROOM), maxPoints);
            }
        }
        return maxPoints;
    }

    /**
     * Return the Direction corresponding to a keyboard key
     * @param e the key event to get the direction for (e.g. KeyEvent.VK_UP)
     * @return the direction corresponding to the supplied key event key
     */
    public Direction getKeyDirection(KeyEvent e) {
        return keyEventDirections.get(e.getKeyCode());
    }

    /**
     * Figure out what size is needed to paint this map
     * @return necessary dimension to paint this map
     */
    public Dimension getPreferredSize() {
        Point maxNW = maxPoints[MudMap.Points.NW.ordinal()];
        Point maxSE = maxPoints[MudMap.Points.SE.ordinal()];
        int heightPlusSpacing = Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING;
        return new Dimension(((int) maxNW.getX() - (int) maxSE.getX() + 2) * heightPlusSpacing + Room.PADDING, ((int) maxNW.getY() - (int) maxSE.getY() + 2) * heightPlusSpacing + Room.PADDING);
    }

    /**
     * Enum to represent directions, their opposite directions and how to translate a point
     * to represent "movement" in that direction
     */
    public enum Direction {

        NORTH {

            public Point getTranslation() {
                return new Point(0, 1);
            }

            public Direction getOpposite() {
                return SOUTH;
            }
        }
        , SOUTH {

            public Point getTranslation() {
                return new Point(0, -1);
            }

            public Direction getOpposite() {
                return NORTH;
            }
        }
        , EAST {

            public Point getTranslation() {
                return new Point(1, 0);
            }

            public Direction getOpposite() {
                return WEST;
            }
        }
        , WEST {

            public Point getTranslation() {
                return new Point(-1, 0);
            }

            public Direction getOpposite() {
                return EAST;
            }
        }
        , NE {

            public Point getTranslation() {
                return new Point(1, 1);
            }

            public Direction getOpposite() {
                return SW;
            }
        }
        , NW {

            public Point getTranslation() {
                return new Point(-1, 1);
            }

            public Direction getOpposite() {
                return SE;
            }
        }
        , SE {

            public Point getTranslation() {
                return new Point(1, -1);
            }

            public Direction getOpposite() {
                return NW;
            }
        }
        , SW {

            public Point getTranslation() {
                return new Point(-1, -1);
            }

            public Direction getOpposite() {
                return NE;
            }
        }
        , UP {

            public Point getTranslation() {
                return new Point(0, 0);
            }

            public Direction getOpposite() {
                return DOWN;
            }
        }
        , DOWN {

            public Point getTranslation() {
                return new Point(0, 0);
            }

            public Direction getOpposite() {
                return UP;
            }
        }
        ;

        /**
         * Map relative points to directions; i.e. if a target room is at a point relative a source room,
         * this map will tell what direction the target room is in relative to the source room
         */
        private static Map<Point, Direction> pointDirections = new HashMap<Point, Direction>();

        static {
            pointDirections.put(new Point(0, 1), NORTH);
            pointDirections.put(new Point(0, -1), SOUTH);
            pointDirections.put(new Point(1, 0), EAST);
            pointDirections.put(new Point(-1, 0), WEST);
            pointDirections.put(new Point(1, 1), NE);
            pointDirections.put(new Point(1, -1), NW);
            pointDirections.put(new Point(-1, 1), SE);
            pointDirections.put(new Point(-1, -1), SW);
        }

        /**
         * Wrapper for translatePoint with CoordinateOrigin defaulted to ROOM
         * @param p the point to translate
         * @return the point translated according to the ROOM coordinate origin
         */
        public Point translatePoint(Point p) {
            return translatePoint(p, CoordinateOrigin.ROOM);
        }

        /**
         * Translate a point according to a direction and a translation mode. The direction determines where
         * the point will end up based on direction (e.g. North should be x, y+1), the translation mode determines
         * what the coordinate should look like based on what you're doing with it (Mapping relies on
         * coordinates being relative to a starting room, drawing relies on coordinates being relative to the
         * top left of the screen)
         *
         * For example, if I'm drawing rooms and I want to go north, I need my Y coordinate to *decrease* because
         * my distance from the top of the screen should be *less*.
         *
         * @param p the point to translate
         * @param mode the translation mode to use
         * @return a point translated according to the specified direction and translation mode
         */
        public Point translatePoint(Point p, CoordinateOrigin mode) {
            Point newPoint = new Point(p);
            Point translation = mode.getTranslation(getTranslation());
            newPoint.translate((int) translation.getX(), (int) translation.getY());
            return newPoint;
        }

        /**
         * get the translation necessary to go in the specified direction
         * @return a point representing the delta-x and delta-y required to translate in the specified direction
         */
        protected abstract Point getTranslation();

        /**
         * Based on a direction, get its opposite direction
         * @return the opposite direction
         */
        public abstract Direction getOpposite();

        public static Direction getDirection(Point p) {
            return pointDirections.get(p);
        }

        /**
         * Drawing and finding the max coordinates of a map require different
         * coordinate "strategies":
         *
         * Mapping: going North is positive, as usual, but going west is positive since
         * rooms to the west of the origin room mean _more_ distance from the origin room
         * to the edge of the screen
         *
         * Drawing: going West is negative, as usual, because we're going closer to the
         * left edge of the screen which means there is _less_ distance from the edge; going
         * North is negative as well, again because we're going closer to the top of the screen
         * meaning there is _less_ distance from the top of the screen
         *
         * (For the max possible coordinates, think of drawing a box around all the rooms
         *  and then finding where the top-left (or northwest) corner would be)
         */
        public enum CoordinateOrigin {

            ROOM {

                public Point getTranslation(Point p) {
                    return new Point(-(int) p.getX(), (int) p.getY());
                }
            }
            , SCREEN {

                public Point getTranslation(Point p) {
                    return new Point((int) p.getX(), -(int) p.getY());
                }
            }
            ;

            public abstract Point getTranslation(Point p);
        }
    }

    /**
     * Paint the map onto its image
     */
    public void paint() {
        Point maxSE;
        if (!isChanged() && bufferedImage != null) {
            Room currentRoom = getCurrentRoom();
            if (!isCurrentRoomChanged() && !hasNewConnection() && !currentRoom.isChanged()) {
                return;
            }
            if (isCurrentRoomChanged()) {
                currentRoom.paint(graphics2D, true);
                getPreviousRoom().paint(graphics2D, false);
                setCurrentRoomChanged(false);
            }
            if (currentRoom.isChanged()) {
                currentRoom.paint(graphics2D, true);
                currentRoom.setChanged(false);
            }
            if (hasNewConnection()) {
                drawLine(getCurrentRoom().getPoint(), getPreviousRoom().getPoint());
                setHasNewConnection(false);
            }
        } else {
            initialRoom = getInitialRoom();
            maxPoints = getMaxPoints();
            initialPoint = maxPoints[MudMap.Points.NW.ordinal()];
            maxSE = maxPoints[MudMap.Points.SE.ordinal()];
            int width = (((int) initialPoint.getX() + 1) - (int) maxSE.getX()) * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
            int height = (((int) initialPoint.getY() + 1) - (int) maxSE.getY()) * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            this.graphics2D = bufferedImage.createGraphics();
            currentOperationID = getNewCurrentOperationID();
            drawRoom(initialRoom, initialPoint);
            clearDrawnLines();
            setChanged(false);
            setCurrentRoomChanged(false);
        }
    }

    /**
     * Draw a room and it's connections to other rooms
     *
     * @param r            the room to draw
     * @param currentGridCoordinate the coordinates to draw this room at
     */
    private void drawRoom(Room r, Point currentGridCoordinate) {
        Room nextRoom;
        Point drawToPoint;
        java.util.List<Room> regdRooms1;
        java.util.List<Room> regdRooms2;
        int x = (int) currentGridCoordinate.getX() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int y = (int) currentGridCoordinate.getY() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        r.paint(graphics2D, new Rectangle(x, y, Room.BOX_WIDTH_HEIGHT, Room.BOX_WIDTH_HEIGHT), r.equals(getCurrentRoom()));
        r.setCurrentOperationID(currentOperationID);
        r.setPoint(currentGridCoordinate);
        for (MudMap.Direction d : MudMap.Direction.values()) {
            if ((nextRoom = r.getRoom(d)) == null) {
                continue;
            }
            regdRooms1 = drawnLines.get(r);
            regdRooms2 = drawnLines.get(nextRoom);
            drawToPoint = d.translatePoint(currentGridCoordinate, MudMap.Direction.CoordinateOrigin.SCREEN);
            if ((regdRooms1 == null || !regdRooms1.contains(nextRoom)) && (regdRooms2 == null || !regdRooms2.contains(r))) {
                drawLine(currentGridCoordinate, drawToPoint);
                if (regdRooms1 == null) {
                    drawnLines.put(r, Arrays.asList(nextRoom));
                } else {
                    regdRooms1 = new ArrayList<Room>(regdRooms1);
                    regdRooms1.add(nextRoom);
                    drawnLines.put(r, regdRooms1);
                }
            }
        }
        for (MudMap.Direction d : MudMap.Direction.values()) {
            nextRoom = r.getRoom(d);
            if (nextRoom != null && nextRoom.getCurrentOperationID() < currentOperationID) {
                drawRoom(r.getRoom(d), d.translatePoint(currentGridCoordinate, MudMap.Direction.CoordinateOrigin.SCREEN));
            }
        }
    }

    /**
     * Draw a line from a room at one point to a room at another point
     *
     * @param from the room to draw the line from
     * @param to   the room to draw the line to
     */
    private void drawLine(Point from, Point to) {
        int xFrom = (int) from.getX() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int yFrom = (int) from.getY() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int xTo = (int) to.getX() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int yTo = (int) to.getY() * (Room.BOX_WIDTH_HEIGHT + Room.BOX_SPACING) + Room.PADDING;
        int dx = new Integer((int) to.getX()).compareTo((int) from.getX());
        int dy = new Integer((int) from.getY()).compareTo((int) to.getY());
        int dxFrom = dx * Room.BOX_WIDTH_HEIGHT / 2 + Room.BOX_WIDTH_HEIGHT / 2;
        int dyFrom = -dy * Room.BOX_WIDTH_HEIGHT / 2 + Room.BOX_WIDTH_HEIGHT / 2;
        int dxTo = (-dx + 1) * Room.BOX_WIDTH_HEIGHT / 2;
        int dyTo = dy * Room.BOX_WIDTH_HEIGHT / 2 + Room.BOX_WIDTH_HEIGHT / 2;
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawLine(xFrom + dxFrom, yFrom + dyFrom, xTo + dxTo, yTo + dyTo);
    }
}
