package net.dataforte.canyon.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.dataforte.canyon.CanyonRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.karora.cooee.app.table.DefaultTableModel;

/**
 * Simple TableModel which reads data from a CSV file. The first row is used for
 * the column titles
 * 
 * @author tst
 */
public class CSVTableModel extends DefaultTableModel {

    static final Log log = LogFactory.getLog(CSVTableModel.class);

    List<String[]> rows = new ArrayList<String[]>();

    String[] titles = new String[0];

    public void setFileName(String fileName) throws IOException {
        InputStream is = CanyonRegistry.getResource(fileName);
        if (is == null) {
            is = new FileInputStream(fileName);
        }
        rows.clear();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();
        titles = line.split(",");
        while ((line = br.readLine()) != null) {
            String items[] = line.split(",");
            rows.add(items);
        }
        if (log.isDebugEnabled()) {
            log.debug("Loaded CSV: " + fileName + " - Rows = " + getRowCount() + " Columns = " + getColumnCount());
        }
    }

    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    public int getColumnCount() {
        return titles.length;
    }

    public String getColumnName(int column) {
        return titles[column];
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int column) {
        try {
            return rows.get(column)[row];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
