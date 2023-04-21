package es.aeat.eett.rubik.export.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.table.TableModel;
import org.jfree.chart.ChartPanel;
import com.tonbeller.jpivot.core.ModelChangeEvent;
import com.tonbeller.jpivot.core.ModelChangeListener;
import es.aeat.eett.rubik.core.OlapModelManager;
import es.aeat.eett.rubik.core.cell.RubikCell;
import es.aeat.eett.rubik.tableRubik.MultiHeader;
import es.aeat.eett.rubik.tableRubik.TableRubik;

/**
 * <p>
 * en: Copy TableRubik, TableModel or ChartPanel to the clipboard. 
 * </p>
 * <p>
 * es: Copia TableRubik, TableModel o ChartPanel al portapapeles
 * </p>
 *
 * @author f00992
 */
class CopyToClipboard implements ModelChangeListener {

    private TableRubik tableRubik = null;

    private TableModel dataModel = null;

    private TableModel fixedModel = null;

    private MultiHeader fixedHeader = null;

    private MultiHeader headerData = null;

    private boolean dirty = false;

    /**
	 * @throws Exception
	 *
	 */
    CopyToClipboard() throws Exception {
        OlapModelManager.getInstance().getDefaultModel().addModelChangeListener(this);
    }

    void copyChart(ChartPanel chartPanel) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ImageSelection imageSelection = new ImageSelection();
        imageSelection.createTransferable(chartPanel);
        clipboard.setContents(imageSelection, null);
    }

    void copyTable(TableModel t_model) {
        tableRubik = null;
        int nRows = t_model.getRowCount();
        int nRcols = t_model.getColumnCount();
        StringBuffer sb = new StringBuffer();
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        final String newLin = "\n";
        final String tab = "\t";
        dirty = false;
        for (short j = 0; j < nRcols; j++) {
            String s = "";
            if (t_model.getColumnName(j) != null) s = t_model.getColumnName(j);
            sb.append(s + tab);
        }
        sb.append(newLin);
        for (short i = 0; i < nRows; i++) {
            for (short j = 0; j < nRcols; j++) {
                if (dirty) return;
                if (t_model.getValueAt(i, j) != null) {
                    sb.append(t_model.getValueAt(i, j).toString() + tab);
                } else {
                    sb.append(tab);
                }
            }
            sb.append(newLin);
        }
        StringSelection strIn = new StringSelection(sb.toString());
        cb.setContents(strIn, strIn);
        if (sb.length() > 0) sb.delete(0, sb.length() - 1);
        sb = null;
        System.gc();
    }

    void copyTableRubik(TableRubik tablaRubik) {
        this.tableRubik = tablaRubik;
        initialize();
        int nRows = (fixedHeader.getRowCount() + dataModel.getRowCount());
        int nRowsHeader = headerData.getRowCount();
        int nRcols = (fixedHeader.getColumnCount() + dataModel.getColumnCount());
        int nRcolsHeaderRows = fixedModel.getColumnCount();
        StringBuffer sb = new StringBuffer();
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        final String newLin = "\n";
        final String tab = "\t";
        RubikCell c = null;
        for (short i = 0; i < nRowsHeader; i++) {
            for (short j = 0; j < nRcols; j++) {
                if (dirty) return;
                if (j < nRcolsHeaderRows) {
                    c = (RubikCell) fixedHeader.getValueAt(i, j);
                    if (c != null) {
                        if (fixedHeader.isVisibleValueAt(i, j)) {
                            sb.append(c.getCaption() + tab);
                        } else {
                            sb.append(tab);
                        }
                    } else {
                        sb.append(tab);
                    }
                } else {
                    c = (RubikCell) headerData.getValueAt(i, j - nRcolsHeaderRows);
                    if (c != null) {
                        if (headerData.isVisibleValueAt(i, j - nRcolsHeaderRows)) {
                            sb.append(c.getCaption() + tab);
                        } else {
                            sb.append(tab);
                        }
                    } else {
                        sb.append(tab);
                    }
                }
            }
            sb.append(newLin);
        }
        for (short i = 0; i < nRows - nRowsHeader; i++) {
            for (short j = 0; j < nRcols; j++) {
                if (dirty) return;
                if (j < nRcolsHeaderRows) {
                    c = (RubikCell) fixedModel.getValueAt(i, j);
                    if (c != null) {
                        sb.append(c.getCaption() + tab);
                    } else {
                        sb.append(tab);
                    }
                } else {
                    c = (RubikCell) dataModel.getValueAt(i, j - nRcolsHeaderRows);
                    if (c != null) {
                        sb.append(c.getCaption() + tab);
                    } else {
                        sb.append(tab);
                    }
                }
            }
            sb.append(newLin);
        }
        StringSelection strIn = new StringSelection(sb.toString());
        cb.setContents(strIn, strIn);
        if (sb.length() > 0) sb.delete(0, sb.length() - 1);
        sb = null;
        System.gc();
        clearAll();
    }

    private void clearAll() {
        tableRubik = null;
        dataModel = null;
        fixedModel = null;
        fixedHeader = null;
        headerData = null;
    }

    private void initialize() {
        dirty = false;
        dataModel = tableRubik.getDataTable().getModel();
        fixedModel = tableRubik.getFixedTable().getModel();
        fixedHeader = (MultiHeader) tableRubik.getFixedTable().getTableHeader();
        headerData = (MultiHeader) tableRubik.getDataTable().getTableHeader();
    }

    public void modelChanged(ModelChangeEvent arg0) {
        dirty = true;
    }

    public void structureChanged(ModelChangeEvent arg0) {
        dirty = true;
    }
}
