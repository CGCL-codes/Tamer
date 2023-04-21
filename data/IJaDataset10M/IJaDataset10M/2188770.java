package ossobook2010.gui.components.content.entry.section1;

import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OTextArea;
import ossobook2010.gui.components.content.input_fields.basic.OTextField;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblock;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class FeatureNote {

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public FeatureNote(MainFrame mainFrame, Entry entry) {
        OTextArea inputField = new OTextArea(mainFrame, 1, 2, Messages.getString("FEATURENOTE"), InputUnitManager.FEATURENOTE, Messages.getString("TOOLTIP_ENTRY>FEATURENOTE"), new SidebarSingleTextblock(Messages.getString("SIDEBAR_ENTRY>FEATURENOTE")));
        entry.getSection(1).add(inputField);
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }
}
