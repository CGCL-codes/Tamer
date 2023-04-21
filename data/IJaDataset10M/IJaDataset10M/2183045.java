package seismosurfer.event;

import java.awt.Cursor;
import com.bbn.openmap.event.CoordMouseMode;

/**
 * A CoordMouseMode mouse mode extension. Using this mode the 
 * user can get information for the map objects.
 *
 */
public class InfoMouseMode extends CoordMouseMode {

    private static final long serialVersionUID = -1989432992210543796L;

    /**
     * Mouse Mode identifier, which is "Gestures". This is returned on getID()
     */
    public static final transient String modeID = "Info";

    /**
     * Construct an InfoMouseMode. Default constructor. Sets the ID to the
     * modeID, and the consume mode to true.
     */
    public InfoMouseMode() {
        this(true);
    }

    /**
     * Construct an InfoMouseMode. The constructor that lets you set the consume
     * mode.
     * 
     * @param consumeEvents
     *            the consume mode setting.
     */
    public InfoMouseMode(boolean consumeEvents) {
        this(modeID, consumeEvents);
    }

    /**
     * Construct an InfoMouseMode. The constructor that lets you set the consume
     * mode.
     * 
     * @param id
     *            the id for the mouse mode.
     * @param consumeEvents
     *            the consume mode setting.
     */
    public InfoMouseMode(String id, boolean consumeEvents) {
        super(id, consumeEvents);
        setPrettyName("Display feature attributes");
        setModeCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
