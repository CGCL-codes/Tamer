package com.nokia.ats4.appmodel.main.event;

import com.nokia.ats4.appmodel.event.KendoEvent;
import java.io.File;

/**
 * OpenProjectEvent is sent to event queue when user wants to open an existing
 * Kendo project by loading it from the a local file.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class OpenProjectEvent extends KendoEvent {

    /** Project file to be opened, null to let user specify the file. */
    private File file = null;

    /**
     * Creates a new instance of OpenProjectEvent
     */
    public OpenProjectEvent(Object source) {
        super(source);
        this.file = null;
    }

    /**
     * Creates a new instance of OpenProjectEvent with project file.
     *
     * @param file Project file to be opened, or <code>null</code> to let user select the file.
     */
    public OpenProjectEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    /**
     * Get the project file to open.
     *
     * @return File to open, or <code>null</code> if not specified.
     */
    public File getProjectFile() {
        return this.file;
    }
}
