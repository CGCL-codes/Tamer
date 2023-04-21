package ossobook2010.gui.components.content.entry.section2;

import java.util.ArrayList;
import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OComboBox;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblock;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class BodySide {

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public BodySide(MainFrame mainFrame, Entry entry) {
        ArrayList<String> data = new ArrayList<String>();
        data.add("dex.");
        data.add("sin.");
        data.add("dex.+sin.");
        data.add("indet.");
        OComboBox inputField = new OComboBox(mainFrame, 1, 1, Messages.getString("BODYSIDE"), InputUnitManager.BODYSIDE, data, Messages.getString("TOOLTIP_ENTRY>BODYSIDE"), new SidebarSingleTextblock(Messages.getString("SIDEBAR_ENTRY>BODYSIDE")));
        entry.getSection(2).add(inputField);
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }
}
