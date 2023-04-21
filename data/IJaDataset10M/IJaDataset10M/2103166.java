package tethyspict.gui.browser.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import tethyspict.model.ImportablePicture;

/**
 * Adds the selected files to the database
 * 
 * @author Elias Gerber
 */
public class ActionListenerAddSelected extends AbstractActionListenerAddPictures {

    /**
         * A Tooltip for this action
         */
    public static final String TOOLTIP = "Adds the selected importable pictures to the database.";

    public void actionPerformed(ActionEvent e) {
        System.out.println("action for add selected");
        if (this.selectedIndexes.size() > 0) {
            ArrayList<ImportablePicture> selected = new ArrayList<ImportablePicture>();
            for (int i = 0; i < selectedIndexes.size(); i++) {
                selected.add(this.model.getPictureAtRow(this.selectedIndexes.get(i)));
                this.addFiles(selected);
            }
        }
    }
}
