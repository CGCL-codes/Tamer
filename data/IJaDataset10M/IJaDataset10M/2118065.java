package org.gromurph.javascore.actions;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.gromurph.javascore.JavaScore;
import org.gromurph.javascore.JavaScoreProperties;
import org.gromurph.javascore.gui.DialogImportEntries;
import org.gromurph.javascore.gui.DialogImportTable;

public class ActionImportEntries extends AbstractAction {

    static ResourceBundle res = JavaScoreProperties.getResources();

    public ActionImportEntries() {
        super(res.getString("MenuImportEntries"));
        putValue(Action.MNEMONIC_KEY, new Integer(res.getString("MenuImportEntriesMnemonic").charAt(0)));
    }

    public void actionPerformed(ActionEvent parm1) {
        DialogImportTable dialog = new DialogImportEntries(JavaScore.getInstance());
        dialog.setRegatta(JavaScoreProperties.getRegatta());
        dialog.setVisible(true);
    }
}
