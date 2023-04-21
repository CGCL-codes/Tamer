package org.fudaa.ctulu.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import com.memoire.bu.BuResource;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluUI;

/**
 * @author deniger
 *
 */
@SuppressWarnings("serial")
public class CtuluTableExportAction extends AbstractAction {

    private final CtuluUI ui;

    private final JTable table;

    private char separator = '\t';

    /**
   * @param ui
   * @param table
   */
    public CtuluTableExportAction(CtuluUI ui, JTable table) {
        super(CtuluLib.getS("Exporter le tableau"), BuResource.BU.getMenuIcon("exporter"));
        putValue(Action.ACTION_COMMAND_KEY, "EXPORT_CSV_EXCEL");
        putValue(Action.SHORT_DESCRIPTION, CtuluLib.getS("Exporter le tableau au format csv ou excel"));
        this.ui = ui;
        this.table = table;
    }

    public void actionPerformed(ActionEvent e) {
        CtuluTableExportPanel.doExport(separator, table, ui, CtuluLibSwing.getActiveWindow());
    }

    /**
   * @return the separator
   */
    public char getSeparator() {
        return separator;
    }

    /**
   * @param separator the separator to set
   */
    public void setSeparator(char separator) {
        this.separator = separator;
    }
}
