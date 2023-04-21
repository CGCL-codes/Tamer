package jcash.ui;

import java.awt.Font;
import java.awt.font.*;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ATable extends JTable {

    public ATable() {
        Font font = getFont();
        if (font != null) {
            FontRenderContext frc = new FontRenderContext(null, false, true);
            LineMetrics lm = font.getLineMetrics("Ay", frc);
            int rowHt = (int) (lm.getHeight() + 4);
            setRowHeight(rowHt);
        }
    }

    public ATable(TableModel m) {
        this();
        setModel(m);
    }
}
