package ossobook2010.gui.components.content.entry.section1;

import java.util.ArrayList;
import ossobook2010.Messages;
import ossobook2010.exceptions.NoRightException;
import ossobook2010.exceptions.NotConnectedException;
import ossobook2010.exceptions.NotLoadedException;
import ossobook2010.exceptions.StatementNotExecutedException;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.Content;
import ossobook2010.gui.components.content.entry.Entry;
import ossobook2010.gui.components.content.input_fields.basic.OComboTextField;
import ossobook2010.gui.components.sidebar.elements.SidebarSingleTextblock;
import ossobook2010.queries.InputUnitManager;

/**
 * 
 *
 * @author Daniel Kaltenthaler
 */
public class ExcavationMethod {

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param entry The basic Entry object.
	 */
    public ExcavationMethod(MainFrame mainFrame, Entry entry) {
        ArrayList<String> dataAvailableItems = new ArrayList<String>();
        try {
            dataAvailableItems = mainFrame.getController().getExcavationMethodInformation();
        } catch (StatementNotExecutedException e) {
            mainFrame.displayError(Messages.getString("SQL_ERROR_OCCURED_PLEASE_CONTACT_SUPPORT"));
        } catch (NotConnectedException e) {
            mainFrame.reloadGui(Content.Id.LOGIN);
        } catch (NotLoadedException e) {
            mainFrame.reloadGui(Content.Id.PROJECT);
        } catch (NoRightException e) {
            mainFrame.displayError(Messages.getString("NO_RIGHT_ERROR_OCCURED"));
        }
        OComboTextField inputField = new OComboTextField(mainFrame, 1, 1, Messages.getString("EXCAVATIONMETHOD"), InputUnitManager.EXCAVATIONMETHOD, dataAvailableItems, Messages.getString("TOOLTIP_ENTRY>EXCAVATIONMETHOD"), new SidebarSingleTextblock(Messages.getString("SIDEBAR_ENTRY>EXCAVATIONMETHOD")));
        entry.getSection(1).add(inputField);
        entry.fillValue(inputField);
        entry.disableInputElement(inputField);
    }
}
