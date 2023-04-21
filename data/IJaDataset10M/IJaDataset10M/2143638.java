package org.gjt.sp.jedit.msg;

import org.gjt.sp.jedit.*;

/**
 * Message sent when a buffer-related change occurs.
 * @author Slava Pestov
 * @version $Id: BufferUpdate.java 12504 2008-04-22 23:12:43Z ezust $
 *
 * @since jEdit 2.2pre6
 */
public class BufferUpdate extends EBMessage {

    /**
	 * Buffer created.
	 */
    public static final Object CREATED = "CREATED";

    /**
	 * About to be closed
	 * @since jEdit 4.2pre3 
	 */
    public static final Object CLOSING = "CLOSING";

    /**
	 * Buffer load started.
	 * @since jEdit 2.6pre1
	 */
    public static final Object LOAD_STARTED = "LOAD_STARTED";

    /**
	 * Buffer loaded.
	 */
    public static final Object LOADED = "LOADED";

    /**
	 * Buffer closed.
	 */
    public static final Object CLOSED = "CLOSED";

    /**
	 * Buffer dirty changed.
	 */
    public static final Object DIRTY_CHANGED = "DIRTY_CHANGED";

    /**
	 * Buffer markers changed.
	 */
    public static final Object MARKERS_CHANGED = "MARKERS_CHANGED";

    /**
	 * Buffer saving.
	 */
    public static final Object SAVING = "SAVING";

    /**
	 * Buffer saved.
	 * @since jEdit 4.0pre4
	 */
    public static final Object SAVED = "SAVED";

    /**
	 * Properties changed.
	 * @since jEdit 4.1pre1
	 */
    public static final Object PROPERTIES_CHANGED = "PROPERTIES_CHANGED";

    /**
	 * Creates a new buffer update message.
	 * @param buffer The buffer
	 * @param what What happened
	 */
    public BufferUpdate(Buffer buffer, View view, Object what) {
        super(buffer);
        this.view = view;
        if (what == null) throw new NullPointerException("What must be non-null");
        this.what = what;
    }

    /**
	 * Returns what caused this buffer update.
	 */
    public Object getWhat() {
        return what;
    }

    /**
	 * Returns the buffer involved.
	 */
    public Buffer getBuffer() {
        return (Buffer) getSource();
    }

    /**
	 * Returns the view involved, which may be null.
	 */
    public View getView() {
        return view;
    }

    public String paramString() {
        return "what=" + what + ",view=" + view + "," + super.paramString();
    }

    private Object what;

    private View view;
}
