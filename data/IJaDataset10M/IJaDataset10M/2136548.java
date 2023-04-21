package org.mitre.rt.client.ui.platform.specification.logicaltest;

import javax.swing.JTable;
import org.mitre.cpe.language.x20.*;
import org.mitre.rt.client.ui.AbsColorTableTextRenderer;
import org.mitre.rt.client.xml.FileTypeHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.FileType;

/**
 *
 * @author BWORRELL
 */
public class LogicalTestTableCellRenderer extends AbsColorTableTextRenderer {

    private static final FileTypeHelper helper = new FileTypeHelper();

    private ApplicationType application = null;

    public LogicalTestTableCellRenderer(final JTable table, final ApplicationType application) {
        super(table);
        this.setDoubleBuffered(true);
        this.application = application;
    }

    @Override
    public void setData(Object value, int row, int column) {
        if (value instanceof LogicalTestType) {
            if (column == LogicalTestTableModel.INT_COLUMN_NAME) {
                this.setText("<logical test: edit to view>");
            }
        }
        if (value instanceof CPEFactRefType) {
            CPEFactRefType factRef = (CPEFactRefType) value;
            if (column == LogicalTestTableModel.INT_COLUMN_NAME) {
                this.setText("fact: " + factRef.getName());
            }
        }
        if (value instanceof CheckFactRefType) {
            CheckFactRefType checkFactRef = (CheckFactRefType) value;
            if (column == LogicalTestTableModel.INT_COLUMN_NAME) {
                FileType file = this.helper.getItem(this.application.getFiles().getFileList(), checkFactRef.getHref());
                this.setText("check-fact: href: " + file.getOrigFileName() + " id: " + checkFactRef.getIdRef());
            }
        }
    }
}
