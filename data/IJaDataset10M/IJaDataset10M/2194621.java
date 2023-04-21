package de.excrawler.distributed.SwingWorker;

import com.jadif.client.Diagrams.PiePainter;
import com.jadif.client.Diagrams.PiePopupShow;
import com.jadif.client.FileTools.FolderTools;
import com.jadif.client.FileTools.FormatFileSize;
import de.excrawler.distributed.Gui.clientView;
import de.excrawler.distributed.Logging.Log;
import de.excrawler.distributed.System.Configuration.ClientConfig;
import java.io.File;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Yves Hoppe <info at yves-hoppe.de>
 */
public class HDDUsage extends SwingWorker<Void, clientView> {

    private clientView _client;

    public HDDUsage(clientView client) {
        this._client = client;
    }

    @Override
    protected Void doInBackground() throws Exception {
        final double imageSize = FormatFileSize.formatSize(FolderTools.getFolderSize(new File(ClientConfig.IMAGEDIR)), FormatFileSize.MB);
        final double siteSize = FormatFileSize.formatSize(FolderTools.getFolderSize(new File(ClientConfig.SITEDIR)), FormatFileSize.MB);
        final double tmpSize = FormatFileSize.formatSize(FolderTools.getFolderSize(new File(ClientConfig.TMPDIR)), FormatFileSize.MB);
        final double logSize = FormatFileSize.formatSize(FolderTools.getFolderSize(new File(ClientConfig.LOGDIR)), FormatFileSize.MB);
        final double dbSize = FormatFileSize.formatSize(FolderTools.getFolderSize(new File("jadif")) + FolderTools.getFolderSize(new File("excrawler")), FormatFileSize.MB);
        double all = tmpSize + logSize + dbSize;
        this._client.field_hdd_database.setText(dbSize + " MB");
        this._client.field_hdd_logs.setText(logSize + " MB");
        this._client.field_hdd_projects.setText(tmpSize + " MB");
        this._client.field_hdd_total.setText(all + " MB");
        TableModel tm = new AbstractTableModel() {

            String data[][] = { { "Images", String.valueOf(imageSize) }, { "Sites", String.valueOf(siteSize) }, { "Logs", String.valueOf(logSize) }, { "Database", String.valueOf(dbSize) } };

            String headers[] = { "Directory", "Size" };

            public int getColumnCount() {
                return headers.length;
            }

            public int getRowCount() {
                return data.length;
            }

            public String getColumnName(int col) {
                return headers[col];
            }

            public Class getColumnClass(int col) {
                return (col == 0) ? String.class : Number.class;
            }

            public boolean isCellEditable(int row, int col) {
                return true;
            }

            public Object getValueAt(int row, int col) {
                return data[row][col];
            }

            public void setValueAt(Object value, int row, int col) {
                data[row][col] = (String) value;
                fireTableRowsUpdated(row, row);
            }
        };
        PiePainter pp = new PiePainter(tm);
        Log.logger.debug("Pie char width: " + this._client.panel_diagram.getWidth());
        Log.logger.debug("Pie chart height: " + this._client.panel_diagram.getHeight());
        if (this._client.panel_diagram.getWidth() == 0) {
            pp.setSize(478, 476);
        } else {
            pp.setSize(this._client.panel_diagram.getWidth(), this._client.panel_diagram.getHeight());
        }
        this._client.panel_diagram.add(pp);
        this._client.panel_diagram.validate();
        this._client.panel_diagram.repaint();
        return null;
    }
}
