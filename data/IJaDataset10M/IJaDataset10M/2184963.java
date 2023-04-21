package ossobook2010.gui.components.content.entry.section2;

import java.util.ArrayList;
import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.specific.Age2BaselField;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblockAndOpenFile;
import ossobook2010.gui.stylesheet.Files;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class Age2Basel {

    private Age2BaselField inputField;

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public Age2Basel(MainFrame mainFrame, Entry entry) {
        inputField = new Age2BaselField(mainFrame, 1, 1, Messages.getString("AGE2BASEL"), InputUnitManager.AGE2BASEL, Messages.getString("TOOLTIP_ENTRY>AGE2BASEL"), new SidebarSingleTextblockAndOpenFile(Messages.getString("SIDEBAR_ENTRY>AGE2BASEL"), Files.PDF_AGE_X_BASEL));
        entry.getSection(2).add(inputField);
        if (entry.getAnimalIds() != null) {
            updateAge2BaselField(entry.getAnimalIds());
        }
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }

    public void updateAge2BaselField(ArrayList<Integer> animalIds) {
        inputField.updateData(animalIds);
    }
}
