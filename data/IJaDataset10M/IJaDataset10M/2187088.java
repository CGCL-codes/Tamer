package org.jdmp.gui.dataset.actions;

import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.jdmp.core.dataset.DataSet;
import org.jdmp.core.dataset.DataSetFactory;
import org.jdmp.core.dataset.HasDataSetList;
import org.ujmp.core.enums.FileFormat;
import org.ujmp.core.interfaces.GUIObject;
import org.ujmp.gui.actions.ObjectAction;

public class ImportDataSetFromClipboardAction extends ObjectAction {

    private static final long serialVersionUID = -4692993310442522430L;

    public ImportDataSetFromClipboardAction(JComponent c, GUIObject i) {
        super(c, i);
        putValue(Action.NAME, "from Clipboard...");
        putValue(Action.SHORT_DESCRIPTION, "Import a DataSet from clipboard");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
    }

    public Object call() {
        try {
            FileFormat fileFormat = FileFormat.values()[JOptionPane.showOptionDialog(getComponent(), "Select format", "Import DataSet", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, FileFormat.values(), FileFormat.CSV)];
            DataSet ds = DataSetFactory.importFromClipboard(fileFormat);
            if (getCoreObject() instanceof HasDataSetList) {
                try {
                    ((HasDataSetList) getCoreObject()).getDataSets().add(ds);
                } catch (Exception e) {
                    ds.showGUI();
                }
            } else {
                ds.showGUI();
            }
            return ds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
