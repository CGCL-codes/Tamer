package org.jcvi.platetools.swing.main.menu;

import javax.swing.Action;
import org.jcvi.glk.Plate;
import org.jcvi.platetools.swing.db.DatabasePlateSelectDialog;
import org.jcvi.platetools.swing.db.ViewAction;
import org.jcvi.platetools.swing.main.PlateToolFrame;

/**
 * A <code>DatabasePlateViewDialog</code> is an implementation of the 
 * {@link DatabasePlateSelectDialog} which will view the {@link Plate} which is selected.
 *
 * @author jsitz@jcvi.org
 */
public class DatabasePlateViewDialog extends DatabasePlateSelectDialog {

    /** The Serial Version UID */
    private static final long serialVersionUID = 7141234314326317405L;

    /**
     * Creates a new <code>DatabasePlateViewDialog</code>.
     * 
     * @param toolFrame The {@link PlateToolFrame} which created the dialog.
     */
    public DatabasePlateViewDialog(PlateToolFrame toolFrame) {
        super(toolFrame);
    }

    @Override
    protected Action getSelectAction() {
        final ViewAction action = new ViewAction(this, this.getParentFrame());
        action.setSourceWindow(this);
        return action;
    }
}
