package com.patientis.framework.controls;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JToolTip;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.html.HTMLEditorKit;
import com.patientis.framework.controls.table.IGetTooltip;
import com.patientis.framework.controls.table.ISTable;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.reference.ActionReference;

/**
 * @author gcaulton
 *
 */
public class ISTableCellLabel extends DefaultTableCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private boolean rendererSetup = false;

    /**
	 * Called multiple times
	 * 
	 * @param table
	 * @param defaultCellRenderer
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 */
    public void setupRenderer(final ISTable table, final Object value, final boolean isSelected, final boolean hasFocus, int setupRow, int setupColumn) {
        if (table != null) {
            if (!rendererSetup) {
                rendererSetup = true;
                if (!table.isCellMouseListenerSetup()) {
                    table.setCellMouseListenerSetup(true);
                    setupClick(table);
                    setupTooltip(table);
                }
            }
        }
    }

    /**
	 * 
	 * @param table
	 */
    public void setupClick(final ISTable table) {
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                try {
                    int clickRow = table.rowAtPoint(e.getPoint());
                    if (clickRow > -1) {
                        table.executeLeftClick(e, ActionReference.SYSTEMLEFTCLICK, false);
                    }
                } catch (Exception ex) {
                    Log.exception(ex);
                }
            }
        });
    }

    /**
	 * 
	 * @param table
	 * @param defaultCellRenderer
	 * @param value
	 * @param isSelected
	 * @param hasFocus
	 * @param row
	 * @param column
	 */
    public void setupTooltip(final ISTable table) {
        table.setTooltipListener(new IGetTooltip() {

            @Override
            public JToolTip getCustomToolTip() {
                return null;
            }

            @Override
            public String getToolTip(int row, int col, MouseEvent me) {
                if (row > -1 && col > -1) {
                    IBaseModel m = table.getModelAtRow(row);
                    if (m != null) {
                        return m.getTooltipHtml();
                    } else {
                        Object o = table.getValueAt(row, col);
                        if (o != null && o instanceof IRenderCell) {
                            return ((IRenderCell) o).getTooltipHtml();
                        } else {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        });
    }
}
