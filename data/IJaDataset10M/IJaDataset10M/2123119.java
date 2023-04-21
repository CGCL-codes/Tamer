package org.exmaralda.partitureditor.deprecated;

import org.exmaralda.partitureditor.deprecated.ExportPraatDialog;
import org.exmaralda.partitureditor.partiture.*;
import org.exmaralda.partitureditor.jexmaralda.*;
import org.exmaralda.partitureditor.jexmaraldaswing.*;
import org.exmaralda.partitureditor.jexmaraldaswing.*;
import com.klg.jclass.table.*;

/**
 * Exports the current transcription as a Praat Text Grid
 * Menu: File --> Export --> Export Praat TextGrid
 * @author  thomas
 */
public class ExportPraatAction extends org.exmaralda.partitureditor.partiture.AbstractTableAction {

    /** Creates a new instance of ExportPraatAction */
    public ExportPraatAction(PartitureTableWithActions t, javax.swing.ImageIcon icon) {
        super("Praat TextGrid...", icon, t);
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        System.out.println("exportPraatAction!");
        table.commitEdit(true);
        exportPraat();
    }

    private void exportPraat() {
        ExportPraatDialog dialog = new ExportPraatDialog(table.homeDirectory, table.getModel().getTranscription().makeCopy());
        boolean success = dialog.exportPraat(table.parent);
    }
}
