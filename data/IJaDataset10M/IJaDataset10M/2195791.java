package cm.util;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * Exporter of Swing table ({@link JTable} or {@link TableModel}) to HTML.
 */
public class TableModelHtmlExporter {

    /**
     * Returns HTML representation of supplied table.
     * @param table {@link JTable} object to get {@link TableModel} from.
     * @return HTML representation of table data
     */
    public static StringBuffer exportTable(JTable table) {
        TableModel model = table.getModel();
        return exportTable(model);
    }

    /**
     * Returns HTML representation of supplied table model.
     * @param model {@link TableModel} to export.
     * @return HTML representation of table data.
     */
    public static StringBuffer exportTable(TableModel model) {
        StringBuffer sb = new StringBuffer("<html>\n");
        sb.append("<head>\n");
        sb.append("<link href='style.css' rel='stylesheet' type='text/css'/>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<table>\n\t<tr>\n");
        for (int i = 0; i < model.getColumnCount(); i++) {
            sb.append("\t\t<th>").append(model.getColumnName(i)).append("</th>\n");
        }
        sb.append("\t</tr>\n");
        for (int i = 0; i < model.getRowCount(); i++) {
            sb.append("\t<tr>\n");
            for (int j = 0; j < model.getColumnCount(); j++) {
                sb.append("\t\t<td>").append(model.getValueAt(i, j).toString()).append("</td>\n");
            }
            sb.append("\t</tr>\n");
        }
        sb.append("</table>\n");
        sb.append("</body>\n");
        sb.append("</html>");
        return sb;
    }
}
