package com.fluendo.jst;

public class Event {

    public static final int FLUSH_START = 1;

    public static final int FLUSH_STOP = 2;

    public static final int EOS = 3;

    public static final int NEWSEGMENT = 4;

    public static final int SEEK = 5;

    private static String typeNames[] = { "NULL", "FLUSH_START", "FLUSH_STOP", "EOS", "NEWSEGMENT", "SEEK" };

    private int type;

    private int format;

    private boolean update;

    private long start;

    private long stop;

    private long position;

    private Event(int type) {
        position = -1;
        this.type = type;
    }

    public String toString() {
        String typeName = typeNames[type];
        switch(type) {
            case SEEK:
                return "[Event] type: " + typeName + ", format: " + format + ", position: " + position;
            case NEWSEGMENT:
                return "[Event] type: " + typeName + (update ? ", update" : ", non-update") + ", format: " + format + ", start: " + start + ", stop: " + stop + ", position: " + position;
            default:
                return "[Event] type: " + typeName;
        }
    }

    public int getType() {
        return type;
    }

    public static Event newEOS() {
        return new Event(EOS);
    }

    public static Event newFlushStart() {
        return new Event(FLUSH_START);
    }

    public static Event newFlushStop() {
        return new Event(FLUSH_STOP);
    }

    public static Event newSeek(int format, long position) {
        Event e = new Event(SEEK);
        e.format = format;
        e.position = position;
        return e;
    }

    public long parseSeekPosition() {
        return position;
    }

    public int parseSeekFormat() {
        return format;
    }

    public static Event newNewsegment(boolean update, int format, long start, long stop, long position) {
        Event e = new Event(NEWSEGMENT);
        e.update = update;
        e.format = format;
        e.start = start;
        e.stop = stop;
        e.position = position;
        return e;
    }

    public boolean parseNewsegmentUpdate() {
        return update;
    }

    public int parseNewsegmentFormat() {
        return format;
    }

    public long parseNewsegmentStart() {
        return start;
    }

    public long parseNewsegmentStop() {
        return stop;
    }

    public long parseNewsegmentPosition() {
        return position;
    }
}
