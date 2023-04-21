package ossobook2010.gui.components.content.entry.section3;

import ossobook2010.exceptions.NotConnectedException;
import ossobook2010.exceptions.StatementNotExecutedException;
import java.util.ArrayList;
import ossobook2010.Messages;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OComboIdSearchBox;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblock;
import ossobook2010.helpers.metainfo.CodeTablesEntry;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class FractureEdge2 {

    private static ArrayList<CodeTablesEntry> data = new ArrayList<CodeTablesEntry>();

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public FractureEdge2(MainFrame mainFrame, Entry entry) {
        try {
            if (data.isEmpty() || Entry.AREVALUESUPDATED) {
                data = mainFrame.getController().getEntriesWithID(InputUnitManager.TABLENAME_FRACTUREEDGE2);
            }
            OComboIdSearchBox inputField = new OComboIdSearchBox(mainFrame, 1, 1, Messages.getString("FRACTUREEDGE2"), InputUnitManager.FRACTUREEDGE2, data, Messages.getString("TOOLTIP_ENTRY>FRACTUREEDGE2"), new SidebarSingleTextblock(Messages.getString("SIDEBAR_ENTRY>FRACTUREEDGE2")));
            entry.getSection(3).add(inputField);
            entry.fillValue(inputField);
            entry.disableInputElement(inputField);
        } catch (NotConnectedException ex) {
            mainFrame.displayError(Messages.getString("NO_RIGHT_ERROR_OCCURED"));
        } catch (StatementNotExecutedException ex) {
            mainFrame.displayError(Messages.getString("SQL_ERROR_OCCURED_PLEASE_CONTACT_SUPPORT"));
        }
    }
}
