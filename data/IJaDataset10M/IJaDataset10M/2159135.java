package sh;

public class DoorState {

    public static final DoorState NONE = new DoorState(-1, '-');

    public static final DoorState CLOSED = new DoorState(0, '1');

    public static final DoorState PARTIAL = new DoorState(1, '2');

    public static final DoorState OPEN = new DoorState(2, '3');

    public static final DoorState BLASTED = new DoorState(3, 'b');

    public static final DoorState[] ordered = { CLOSED, PARTIAL, OPEN };

    public static final DoorState[] all = { CLOSED, PARTIAL, OPEN, BLASTED };

    private int value_;

    private char char_;

    private DoorState(int value, char c) {
        value_ = value;
        char_ = c;
    }

    public int getValue() {
        return value_;
    }

    public char getChar() {
        return char_;
    }

    public static int getTile(int type, DoorState s, Face f) {
        return type | (f.getValue() * all.length + s.getValue());
    }

    ;

    public static int getTile(int type, DoorState s) {
        return type | s.getValue();
    }

    ;

    public static DoorState getState(int type, int tile) {
        if ((tile & TileType.TYPE_MASK) == type) return all[(tile & TileType.TILE_MASK) % all.length]; else return NONE;
    }

    public static Face getFace(int tile) {
        return Face.ordered[(tile & TileType.TILE_MASK) / Face.ordered.length];
    }
}
