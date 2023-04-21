package chequeredflag.gui.track;

import chequeredflag.data.track.*;
import chequeredflag.gui.*;
import chequeredflag.gui.table.StandardTableModel;

/**
 *
 * @author barrie
 */
public class CmdParamTableModel extends StandardTableModel {

    private Command currentCommand;

    private String[] paramText;

    /** F1GP/WC track command parameters */
    public CmdParamTableModel(Command selectedCommand, String[] selectedParamText) {
        currentCommand = selectedCommand;
        paramText = selectedParamText;
        tableDimension(paramText.length, 2);
        populateTable();
    }

    public void updateTrackData() {
        for (int x = 0; x < paramText.length; x++) {
            currentCommand.setParam(x, new Integer((String) getValueAt(x, 1)).intValue());
        }
    }

    public void populateTable() {
        for (int x = 0; x < paramText.length; x++) {
            setValueAt(paramText[x], x, 0);
            setValueAt(new String(new Integer(currentCommand.getParam(x)).toString()), x, 1);
        }
    }
}
