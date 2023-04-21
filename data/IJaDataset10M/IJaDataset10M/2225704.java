package mudcartographer;

import java.awt.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Represents a "drawable" "room"
 */
public class Room {

    public static final int BOX_WIDTH_HEIGHT = 20;

    public static final int ARC_WIDTH_HEIGHT = 7;

    public static final int BOX_SPACING = BOX_WIDTH_HEIGHT;

    public static final int TEXT_SPACING = 2;

    public static final int PADDING = BOX_SPACING;

    public static final int TEXT_HEIGHT = 10;

    public static final char DEFAULT_SYMBOL = ' ';

    public static int nextID;

    private Room[] rooms = new Room[10];

    private char symbol = 'a';

    private int currentOperationID;

    private int ID;

    private Rectangle rectangle;

    private Point point;

    private Color textColor = Color.BLACK;

    private Color backgroundColor = Color.WHITE;

    private boolean isChanged;

    private String description;

    public enum RoomProperty {

        SYMBOL, ID, RECTANGLE, POINT, TEXT_COLOR, BACKGROUND_COLOR, DESCRIPTION, DELETE, PAINT;

        public static Set<RoomProperty> getAll() {
            return new HashSet<RoomProperty>(Arrays.asList(RoomProperty.values()));
        }
    }

    public Room() {
        this(DEFAULT_SYMBOL);
    }

    public Room(char symbol) {
        this.symbol = symbol;
        this.ID = useID();
    }

    /**
     * Get a room attached to this room
     * @param d the direction of the attached room
     * @return the room in the specified direction or null if no room exists
     */
    public Room getRoom(MudMap.Direction d) {
        return rooms[d.ordinal()];
    }

    public void setRoom(Room r, MudMap.Direction d) {
        rooms[d.ordinal()] = r;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
        this.setChanged(true);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrentOperationID() {
        return currentOperationID;
    }

    public void setCurrentOperationID(int currentOperationID) {
        this.currentOperationID = currentOperationID;
    }

    private int useID() {
        return nextID++;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Get the point and dimensions for the current drawing of this room
     * @return pixel point and pixel dimensions of the room
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Set the point and dimensions for the current drawing of this room
     * @param rectangle pixel point and pixel dimensions for the current drawing of this room
     */
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Get the current coordinates (in rooms, not pixels) of this room
     * @return current room coordinates of this room
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Set the current room coordinates of this room
     * @param point coordinates of the current room (in rooms, not pixels)
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        this.setChanged(true);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.setChanged(true);
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    /**
     * Wrap the paint function to allow setting the rectangle
     *
     * @param g2 the graphics object to draw to
     * @param rectangle the rectangle to draw in
     * @param isCurrent whether or not to highlight the room
     */
    public void paint(Graphics2D g2, Rectangle rectangle, boolean isCurrent) {
        setRectangle(rectangle);
        paint(g2, isCurrent);
    }

    /**
     * Paint this room
     *
     * @param g2 the graphics object to paint to
     * @param isCurrent whether to highlight the current room
     */
    public void paint(Graphics2D g2, boolean isCurrent) {
        int x = (int) rectangle.getX();
        int y = (int) rectangle.getY();
        g2.setColor(getBackgroundColor());
        g2.fillRoundRect(x, y, (int) rectangle.getWidth(), (int) rectangle.getHeight(), ARC_WIDTH_HEIGHT, ARC_WIDTH_HEIGHT);
        g2.setColor(isCurrent ? Color.RED : Color.BLACK);
        g2.drawRoundRect(x, y, (int) rectangle.getWidth(), (int) rectangle.getHeight(), ARC_WIDTH_HEIGHT, ARC_WIDTH_HEIGHT);
        g2.setColor(getTextColor());
        g2.drawString(String.valueOf(getSymbol()), (int) rectangle.getX() + TEXT_SPACING, (int) rectangle.getY() + TEXT_SPACING + TEXT_HEIGHT);
    }
}
