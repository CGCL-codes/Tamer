package musicsequencer.designer;

import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a measure, but is just the RhythmEvents, no audio data.
 *
 * Can be named for handy library usage.
 *
 * @author Music Sequencer Group
 */
public class RhythmTrack implements Serializable, Transferable {

    private static final long serialVersionUID = RhythmTrack.class.getName().hashCode();

    /**
     * The data flavor used for drag n drop transfers.
     */
    public static final DataFlavor dataFlavor;

    static {
        try {
            dataFlavor = new DataFlavor(Class.forName("musicsequencer.designer.RhythmTrack"), "RhythmTrack");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not initialize DataFlavor");
        }
    }

    /**
     * Used if no name is specified.
     */
    public static final String defaultName = "Unnamed";

    private RhythmEvent[] rhythmEvents = new RhythmEvent[RiffEngine.BEAT_COUNT];

    private String name = defaultName;

    /**
     * Creates a blank RhythmTrack with the default name.
     */
    public RhythmTrack() {
        name = defaultName;
        for (int i = 0; i < RiffEngine.BEAT_COUNT; i++) {
            rhythmEvents[i] = new RhythmEvent();
        }
    }

    /**
     * Creates a blank RhythmTrack with the specified name.
     * @param name the name for this RhythmTrack, e.g. "HipHop KickDrum Pattern 1"
     */
    public RhythmTrack(String name) {
        this();
        this.name = name;
    }

    public RhythmEvent getEvent(int i) {
        return rhythmEvents[i];
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(dataFlavor);
    }

    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = { dataFlavor };
        return flavors;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
        return copy();
    }

    private RhythmTrack copy() {
        RhythmTrack copy = new RhythmTrack();
        for (int i = 0; i < rhythmEvents.length; i++) {
            copy.rhythmEvents[i].setVolume(rhythmEvents[i].getVolume());
            if (rhythmEvents[i].isReverseCued()) copy.rhythmEvents[i].toggleReversed();
        }
        return copy;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "RhythmTrack: " + getName();
    }

    /**
     * @param other any other object
     * @return whether or not the other object is a RhythmTrack with the same rhythm information
     */
    public boolean equals(Object other) {
        if (!(other instanceof RhythmTrack)) return false;
        RhythmTrack otherTrack = (RhythmTrack) other;
        for (int i = 0; i < rhythmEvents.length; i++) {
            if (otherTrack.rhythmEvents[i].getVolume() != rhythmEvents[i].getVolume()) return false;
            if (otherTrack.rhythmEvents[i].isReverseCued() != rhythmEvents[i].isReverseCued()) return false;
        }
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }
}
