package ossobook2010.gui.components.content.entry.section1;

import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OTextField;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblock;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class Horizontal1 {

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public Horizontal1(MainFrame mainFrame, Entry entry) {
        OTextField inputField = new OTextField(mainFrame, 1, 1, Messages.getString("HORIZONTALSTRATIGRAPHY1"), InputUnitManager.HORIZONTALSTRATIGRAPHY1, Messages.getString("TOOLTIP_ENTRY>HORIZONTALSTRATIGRAPHY1"), new SidebarSingleTextblock(Messages.getString("SIDEBAR_ENTRY>HORIZONTALSTRATIGRAPHY1")));
        entry.getSection(1).add(inputField);
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }
}
