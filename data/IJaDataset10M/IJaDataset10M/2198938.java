package net.sf.graphiti.ui.actions;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

/**
 * This class is a tailor-made {@link Clipboard}.
 * 
 * @author Matthieu Wipliez
 */
public class GraphitiClipboard {

    /**
	 * The event name used for {@link GraphitiClipboard#fireContentsSet()}
	 */
    public static final String CONTENTS_SET_EVENT = "ContentsSet";

    private static Clipboard instance;

    /**
	 * Returns the default Clipboard.
	 * 
	 * @return The default Clipboard.
	 */
    public static Clipboard getInstance() {
        if (instance == null) {
            instance = new Clipboard(Display.getCurrent());
        }
        return instance;
    }
}
