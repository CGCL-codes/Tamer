package org.softvfc.tablehandler.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.table.TableModel;
import org.openide.util.Exceptions;

/**
 *
 * @author Diego Schmaedech Martins (schmaedech@gmail.com)
 * @version 29/07/2010
 */
public class TableExporter {

    public TableExporter() {
    }

    public void exportTable(TableModel model, File file) {
        FileWriter out = null;
        try {
            out = new FileWriter(file);
            for (int i = 0; i < model.getColumnCount(); i++) {
                out.write(model.getColumnName(i) + "\t");
            }
            out.write("\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    out.write(model.getValueAt(i, j) + "\t");
                }
                out.write("\n");
            }
            out.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
