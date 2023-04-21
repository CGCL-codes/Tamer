package com.oktiva.mogno.additional;

import com.oktiva.mogno.Component;
import com.oktiva.mogno.Visual;
import com.oktiva.mogno.html.Input;
import com.oktiva.mogno.html.Table;
import com.oktiva.mogno.html.Td;
import com.oktiva.mogno.html.Th;
import com.oktiva.mogno.html.Tr;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * @version $Id: SimpleTable.java,v 1.2 2006/01/04 03:27:42 mosca Exp $
 */
public class SimpleTable extends Table {

    /** Vector of Vectors with the table data.
	 */
    protected Vector dataVector = null;

    private Vector removedRow = null;

    /** String with the comumn's names and widths, in the following format:<br>
	 * name1|attr1=val1|attr2=val2&amp;name2|attr3=val3...
	 */
    public String columnIdentifiers = "";

    public String getColumnIdentifiers() {
        return columnIdentifiers;
    }

    public void setColumnIdentifiers(String columnIdentifiers) {
        this.columnIdentifiers = columnIdentifiers;
    }

    public String editableColumns = "";

    public String hiddenColumns = "";

    /** Getter for property hiddenColumns.
	 * @return Value of property hiddenColumns.
	 */
    public String getHiddenColumns() {
        return hiddenColumns;
    }

    /** Setter for property hiddenColumns. String with the number of the
	 * columns which should not be displayed, but must have its data included
	 * Column numbers should be separated by &amp; as usual.
	 * @param hiddenColumns New value of property hiddenColumns.
	 */
    public void setHiddenColumns(String hiddenColumns) {
        this.hiddenColumns = hiddenColumns;
    }

    /** The title on top of the columns with the "remove" buttons.
	 */
    public String removeColumnTitle = "&nbsp;";

    public String getRemoveColumnTitle() {
        return removeColumnTitle;
    }

    public void setRemoveColumnTitle(String removeColumnTitle) {
        this.removeColumnTitle = removeColumnTitle;
    }

    /**
	 */
    public String evOnRemoveRow = "";

    public String getEvOnRemoveRow() {
        return evOnRemoveRow;
    }

    public void setEvOnRemoveRow(String evOnRemoveRow) {
        this.evOnRemoveRow = evOnRemoveRow;
    }

    /** Activate/deactivate this SimpleList.
	 * An inactive SimpleList don't submit any data and has no "del" button.
	 */
    public boolean active = true;

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /** The label to show in the "remove" buttons.
	 */
    public String removeButtonLabel = "X";

    public String getRemoveButtonLabel() {
        return removeButtonLabel;
    }

    public void setRemoveButtonLabel(String removeButtonLabel) {
        this.removeButtonLabel = removeButtonLabel;
    }

    /** The String that will be displayed in the first column of the total line
	 */
    public String totalLineLabel = "";

    /** The number of the columns taht will be totalized, separated by <code>&amp;</code>.<br>
	 */
    public String totalColumns = "";

    /** The max number of fractionary digists given to DecimalFormat
	 */
    public int totalMaximumFractionDigits = 2;

    /** The minimum number of fractionary digists given to DecimalFormat
	 */
    public int totalMinimumFractionDigits = 2;

    public Component selectParentComponent() {
        return this;
    }

    public String show() throws Exception {
        StringBuffer ret = new StringBuffer(4096);
        long isHidden = 0;
        long isEditable = 0;
        long isTotalized = 0;
        double[] totals = null;
        NumberFormat nf = null;
        String[] columns = columnIdentifiers.split("\\&");
        String[] hiddens = hiddenColumns.split("\\&");
        String[] editables = editableColumns.split("\\&");
        isHidden = colListToBitCache(hiddens);
        isEditable = colListToBitCache(editables);
        isTotalized = colListToBitCache(getTotalColumns().split("\\&"));
        if (isTotalized != 0) {
            nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(getTotalMaximumFractionDigits());
            nf.setMinimumFractionDigits(getTotalMinimumFractionDigits());
            totals = new double[columnIdentifiers.length()];
            for (int i = 0; i < totals.length; i++) {
                totals[i] = 0;
            }
        }
        ret.append("<TABLE").append(showHtmlAttributes()).append(">\n");
        ret.append("<TR>");
        int cols = columns.length;
        Vector colProperties = new Vector();
        for (int i = 0; i < columns.length; i++) {
            String[] info = columns[i].split("\\|");
            StringBuffer sb = new StringBuffer();
            for (int j = 1; j < info.length; j++) {
                String[] prop = info[j].split("=", 2);
                if (prop.length == 2 && prop[0] != null && !"".equals(prop[0]) && prop[1] != null && !"".equals(prop[1])) {
                    sb.append(" ").append(prop[0]).append("=\"").append(prop[1] + "\"");
                }
            }
            colProperties.add(sb.toString());
            if ((isHidden & (1 << i)) != 0) {
                continue;
            }
            ret.append("<TH>").append(info[0]).append("</TH>");
        }
        if (active) {
            ret.append("<TH>").append(removeColumnTitle).append("</TH>");
        }
        ret.append("</TR>\n");
        if (dataVector != null) {
            int rows = dataVector.size();
            ret.append("<INPUT type='hidden' name='").append(getName()).append("_ROWS' value='");
            ret.append(new Integer(rows).toString()).append("'>");
            ret.append("<INPUT type='hidden' name='").append(getName()).append("_COLS' value='");
            ret.append(new Integer(cols).toString()).append("'>");
            for (int row = 0; row < rows; row++) {
                ret.append("<TR>");
                for (int col = 0; col < cols; col++) {
                    String cellContent;
                    String hiddenValue;
                    try {
                        cellContent = (String) ((Vector) dataVector.get(row)).get(col);
                        hiddenValue = cellContent;
                        if ((isTotalized & (1 << col)) != 0) {
                            try {
                                totals[col] += nf.parse(cellContent).doubleValue();
                            } catch (NumberFormatException e) {
                            }
                        }
                    } catch (Exception e) {
                        cellContent = "&nbsp;";
                        hiddenValue = "";
                    }
                    String hiddenName = getName() + "_" + row + "_" + col;
                    String hiddenType = "hidden";
                    if ((isHidden & (1 << col)) == 0) {
                        ret.append("<TD");
                        ret.append((String) colProperties.get(col));
                        ret.append(">");
                        if ((isEditable & (1 << col)) != 0) {
                            hiddenType = "text";
                        } else {
                            ret.append(cellContent);
                        }
                    }
                    ret.append("<INPUT type='").append(hiddenType).append("' name='");
                    ret.append(hiddenName);
                    ret.append("' value='").append(hiddenValue).append("'>");
                    if ((isHidden & (1 << col)) == 0) {
                        ret.append("</TD>");
                    }
                }
                if (active) {
                    ret.append("<TD>");
                    ret.append("<INPUT type='submit' name='");
                    ret.append(getName() + "_DEL_" + row);
                    ret.append("' value='").append(removeButtonLabel).append("'>");
                    ret.append("</TD>");
                }
                ret.append("</TR>\n");
            }
            if (isTotalized != 0) {
                ret.append("<TR>");
                boolean first = true;
                for (int col = 0; col < columns.length; col++) {
                    if ((isHidden & (1 << col)) != 0) {
                        continue;
                    }
                    ret.append("<TD");
                    if (first) {
                        ret.append(">").append(getTotalLineLabel());
                        first = false;
                    } else {
                        ret.append((String) colProperties.get(col));
                        ret.append(">");
                        if ((isTotalized & (1 << col)) != 0) {
                            ret.append(nf.format(totals[col]));
                        } else {
                            ret.append("&nbsp;");
                        }
                    }
                    ret.append("</TD>");
                }
                if (active) {
                    ret.append("<TD>&nbsp;</TD>");
                }
                ret.append("</TR>\n");
            }
        }
        ret.append("</TABLE>\n");
        tag = null;
        ret.append(super.show());
        return ret.toString();
    }

    public String endContainer() {
        Enumeration e = listChilds();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            freeChild(s);
        }
        return super.endContainer();
    }

    public void receiveRequest(HttpServletRequest request) {
        int rows = 0, cols = 0;
        String rowsStr = request.getParameter(getName() + "_ROWS");
        if (rowsStr != null) {
            rows = Integer.valueOf(rowsStr).intValue();
        }
        String colsStr = request.getParameter(getName() + "_COLS");
        if (colsStr != null) {
            cols = Integer.valueOf(colsStr).intValue();
        }
        dataVector = new Vector();
        for (int row = 0; row < rows; row++) {
            Vector rowData = new Vector(cols);
            rowData.setSize(cols);
            for (int col = 0; col < cols; col++) {
                rowData.set(col, request.getParameter(getName() + "_" + row + "_" + col));
            }
            if (request.getParameter(getName() + "_DEL_" + row) == null) {
                dataVector.add(rowData);
            } else {
                removedRow = rowData;
                queue("evOnRemoveRow");
            }
        }
    }

    public void addRowData(Vector rowData) {
        if (dataVector == null) {
            dataVector = new Vector();
        }
        dataVector.add(rowData);
    }

    /** Getter for property dataVector.
	 * @return Value of property dataVector.
	 */
    public Vector getDataVector() {
        return dataVector;
    }

    /** Setter for property dataVector.
	 * @param dataVector New value of property dataVector.
	 */
    public void setDataVector(Vector dataVector) {
        this.dataVector = dataVector;
    }

    /** Getter for property editableColumns.
	 * @return Value of property editableColumns.
	 */
    public java.lang.String getEditableColumns() {
        return editableColumns;
    }

    /** Colum numbers wich should be turnned into text inputs, separated by
	 * &amp;
	 * @param editableColumns New value of property editableColumns.
	 */
    public void setEditableColumns(java.lang.String editableColumns) {
        this.editableColumns = editableColumns;
    }

    /** Returns the row that was clicked to be removed.
	 * @return Vector with the row data
	 */
    public Vector getRemovedRow() {
        return removedRow;
    }

    /** Getter for property totalLineLabel.
	 * @return Value of property totalLineLabel.
	 *
	 */
    public java.lang.String getTotalLineLabel() {
        return totalLineLabel;
    }

    /** Setter for property totalLineLabel.
	 * @param totalLineLabel New value of property totalLineLabel.
	 *
	 */
    public void setTotalLineLabel(java.lang.String totalLineLabel) {
        this.totalLineLabel = totalLineLabel;
    }

    /** Getter for property totalColumns.
	 * @return Value of property totalColumns.
	 *
	 */
    public java.lang.String getTotalColumns() {
        return totalColumns;
    }

    /** Setter for property totalColumns.
	 * @param totalColumns New value of property totalColumns.
	 *
	 */
    public void setTotalColumns(java.lang.String totalColumns) {
        this.totalColumns = totalColumns;
    }

    /** Getter for property totalMaximumFractionDigits.
	 * @return Value of property totalMaximumFractionDigits.
	 *
	 */
    public int getTotalMaximumFractionDigits() {
        return totalMaximumFractionDigits;
    }

    /** Setter for property totalMaximumFractionDigits.
	 * @param totalMaximumFractionDigits New value of property totalMaximumFractionDigits.
	 *
	 */
    public void setTotalMaximumFractionDigits(int totalMaximumFractionDigits) {
        this.totalMaximumFractionDigits = totalMaximumFractionDigits;
    }

    /** Getter for property totalMinimumFractionDigits.
	 * @return Value of property totalMinimumFractionDigits.
	 *
	 */
    public int getTotalMinimumFractionDigits() {
        return totalMinimumFractionDigits;
    }

    /** Setter for property totalMinimumFractionDigits.
	 * @param totalMinimumFractionDigits New value of property totalMinimumFractionDigits.
	 *
	 */
    public void setTotalMinimumFractionDigits(int totalMinimumFractionDigits) {
        this.totalMinimumFractionDigits = totalMinimumFractionDigits;
    }

    /**
	 * @param str
	 * @return
	 */
    private long strArrToBitCache(String[] str) {
        long ret = 0;
        for (int i = 0; i < str.length; i++) {
            if (!"".equals(str[i])) {
                ret |= (1 << i);
            }
        }
        return ret;
    }

    /**
	 * @param str
	 * @return
	 */
    private long colListToBitCache(String[] str) {
        long ret = 0;
        for (int i = 0; i < str.length; i++) {
            if (!"".equals(str[i])) {
                Integer col = new Integer(str[i]);
                ret |= (1 << (col.intValue() - 1));
            }
        }
        return ret;
    }
}
