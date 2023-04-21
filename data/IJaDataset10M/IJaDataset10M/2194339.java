package ossobook2010.gui.components.content.entry.section2;

import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OCheckBox;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class Fused {

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public Fused(MainFrame mainFrame, Entry entry) {
        OCheckBox inputField = new OCheckBox(mainFrame, 1, 1, Messages.getString("FUSED"), InputUnitManager.FUSED, Messages.getString("TOOLTIP_ENTRY>FUSED"));
        entry.getSection(2).add(inputField);
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }
}
