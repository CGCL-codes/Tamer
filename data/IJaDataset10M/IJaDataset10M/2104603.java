package ossobook2010.gui.components.content.entry.section4;

import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OIntegerField;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblock;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class M_207pb204pb {

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public M_207pb204pb(MainFrame mainFrame, Entry entry) {
        OIntegerField inputField = new OIntegerField(mainFrame, 1, 1, Messages.getString("207PB204PB"), InputUnitManager.M_207PB_204PB, Messages.getString("TOOLTIP_ENTRY>207PB_204PB"), new SidebarSingleTextblock(Messages.getString("SIDEBAR_ENTRY>207PB_204PB")));
        entry.getSection(4).add(inputField);
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }
}
