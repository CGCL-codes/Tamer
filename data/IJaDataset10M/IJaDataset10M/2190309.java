package wabclient;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.awt.datatransfer.*;

public class WABTable extends JTable implements MouseListener {

    SortedTableModel model = new SortedTableModel();

    JPopupMenu rmbmenu = new JPopupMenu();

    int direction = 0;

    String lasterror = "";

    WABGlobal global;

    WABWindow win = null;

    public WABTable(WABGlobal global, Container cnt) {
        this.global = global;
        if (cnt instanceof WABWindow) win = (WABWindow) cnt;
        setModel(model);
        addMouseListener(this);
        getTableHeader().addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                JTableHeader th = (JTableHeader) e.getComponent();
                int c = th.columnAtPoint(e.getPoint());
                if (direction == 0) direction = 1; else direction = 0;
                editingStopped(null);
                model.sort(c, direction);
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        });
    }

    public void editingStopped(ChangeEvent e) {
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            System.out.println("editing stopped");
            Object value = editor.getCellEditorValue();
            setValueAt(value, editingRow, editingColumn);
            removeEditor();
        }
    }

    public int getSelectedRow(int start) {
        return getSelectedRow();
    }

    public boolean isModified() {
        return model.isModified();
    }

    public boolean isModified(int rowIndex) {
        return model.isModified(rowIndex);
    }

    public void setModified(int rowIndex, boolean modified) {
        model.setModified(rowIndex, modified);
    }

    public void setCellColor(int row, int column, String color) {
        model.setCellColor(row, column, color);
    }

    public void setCellColor(int row, String column, String color) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equals(column)) {
                setCellColor(row, i, color);
            }
        }
    }

    public int rowCount() {
        return model.getRowCount();
    }

    public void deleteRow(int row) {
        model.removeRow(row);
    }

    public void setItem(int row, int column, String value) {
        model.setValueAt(value, row, column);
    }

    public void setItem(int row, String column, String value) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equals(column)) {
                setItem(row, i, value);
                return;
            }
        }
    }

    public Object getItem(int row, String column) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equals(column)) return model.getValueAt(row, i);
        }
        return null;
    }

    public Object getItem(int row, int column) {
        return model.getValueAt(row, column);
    }

    public String getItemString(int row, String column) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            if (model.getColumnName(i).equals(column)) return (String) model.getValueAt(row, i);
        }
        return null;
    }

    public String getItemString(int row, int column) {
        return (String) model.getValueAt(row, column);
    }

    public int retrieve(String url) {
        StringTokenizer st;
        String value;
        String line;
        URL ur;
        BufferedReader br;
        int row = 0;
        int rc = 0;
        short col = 0;
        boolean valueSet;
        try {
            setCursor(new Cursor(3));
            model.removeRows();
            ur = new URL(url);
            br = new BufferedReader(new InputStreamReader(ur.openStream()));
            line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    br.close();
                    lasterror = line.substring(1);
                    setCursor(new Cursor(0));
                    return -3;
                }
                row = model.addRow();
                rc++;
                col = 1;
                st = new StringTokenizer(line, "" + (char) 127, true);
                valueSet = false;
                while (st.hasMoreTokens()) {
                    value = st.nextToken();
                    if (value.equals("" + (char) 127)) {
                        if (!valueSet) {
                            model.setValueAt("", row, col - 1, false);
                        }
                        col++;
                        valueSet = false;
                        continue;
                    }
                    model.setValueAt(value, row, col - 1, false);
                    valueSet = true;
                }
                if (win != null) {
                    Object args[] = new Object[1];
                    args[0] = new Integer(rc - 1);
                    win.fireEvent("retrieverow", args);
                }
            }
            br.close();
            setCursor(new Cursor(0));
            return rc;
        } catch (MalformedURLException mue) {
            return -1;
        } catch (IOException ioe) {
            return -2;
        }
    }

    public void copy() {
        String buffer = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        int rows = getSelectedRowCount();
        int columns = getColumnCount();
        int firstrow = getSelectedRow();
        String tmp = "";
        for (int c = 0; c < columns; c++) {
            tmp += getColumnName(c) + (char) 9;
        }
        buffer += tmp + "\n";
        for (int r = firstrow; r < rows + firstrow; r++) {
            tmp = "";
            for (int c = 0; c < columns; c++) {
                Object value = getValueAt(r, c);
                if (value instanceof String) tmp += (String) value + (char) 9; else if (value instanceof Number) {
                    NumberFormat nf = NumberFormat.getInstance();
                    tmp += "" + (Number) value + (char) 9;
                } else if (value instanceof Date) tmp += new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value) + (char) 9; else tmp += (char) 9;
            }
            buffer += tmp + "\n";
        }
        StringSelection contents = new StringSelection(buffer);
        clipboard.setContents(contents, contents);
    }

    static final int CSV = 1;

    static final int EXCEL = 2;

    public void saveas(String filename, int type) {
        if (type == CSV) saveas(filename, type); else if (type == EXCEL) saveas(filename, type, "Table");
    }

    public void saveas(String filename, int type, String sheetname) {
        if (type == CSV) saveCSV(filename); else if (type == EXCEL) saveEXCEL(filename, sheetname);
    }

    private void saveEXCEL(String filename, String sheetname) {
        try {
            jxl.write.WritableWorkbook workbook = jxl.Workbook.createWorkbook(new File(filename));
            jxl.write.WritableSheet sheet = workbook.createSheet(sheetname, 0);
            int rows = getSelectedRowCount();
            int columns = getColumnCount();
            int firstrow = getSelectedRow();
            int colno = 0;
            int rowno = 0;
            for (int c = 0; c < columns; c++) {
                TableColumn column = getColumnModel().getColumn(c);
                int w = column.getPreferredWidth();
                if (w > 0) {
                    jxl.write.Label label = new jxl.write.Label(colno++, 0, getColumnName(c));
                    sheet.addCell(label);
                }
            }
            for (int r = firstrow; r < rows + firstrow; r++) {
                colno = 0;
                rowno++;
                for (int c = 0; c < columns; c++) {
                    TableColumn column = getColumnModel().getColumn(c);
                    int w = column.getPreferredWidth();
                    if (w > 0) {
                        Object value = getValueAt(r, c);
                        if (value instanceof String) {
                            jxl.write.Label label = new jxl.write.Label(colno++, rowno, (String) value);
                            sheet.addCell(label);
                        } else if (value instanceof Number) {
                            jxl.write.Number number = new jxl.write.Number(colno++, rowno, ((Number) value).doubleValue());
                            sheet.addCell(number);
                        } else if (value instanceof Date) {
                            java.util.Date d = (Date) value;
                            long time = d.getTime() + 1000 * 60 * 60 * 2;
                            jxl.write.DateTime date = new jxl.write.DateTime(colno++, rowno, new java.util.Date(time));
                            sheet.addCell(date);
                        } else colno++;
                    }
                }
            }
            workbook.write();
            workbook.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured during file creation.", global.appname, JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured during file writing.", global.appname, JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured during file writing.", global.appname, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCSV(String filename) {
        String buffer = "";
        int rows = getSelectedRowCount();
        int columns = getColumnCount();
        int firstrow = getSelectedRow();
        String tmp = "";
        for (int c = 0; c < columns; c++) {
            TableColumn column = getColumnModel().getColumn(c);
            int w = column.getPreferredWidth();
            if (w > 0) tmp += "\"" + getColumnName(c) + "\",";
        }
        buffer += tmp + "\n";
        for (int r = firstrow; r < rows + firstrow; r++) {
            tmp = "";
            for (int c = 0; c < columns; c++) {
                TableColumn column = getColumnModel().getColumn(c);
                int w = column.getPreferredWidth();
                if (w > 0) {
                    Object value = getValueAt(r, c);
                    if (value instanceof String) tmp += "\"" + (String) value + "\","; else if (value instanceof Number) {
                        NumberFormat nf = NumberFormat.getInstance();
                        tmp += "\"" + (Number) value + "\",";
                    } else if (value instanceof Date) tmp += "\"" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value) + "\","; else tmp += "\"\",";
                }
            }
            buffer += tmp + "\n";
        }
        if (buffer.length() > 0) {
            try {
                FileOutputStream fos = new FileOutputStream(new File(filename));
                fos.write(buffer.getBytes());
                fos.close();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occured during file creation.", global.appname, JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occured during file writing.", global.appname, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String getLastError() {
        return lasterror;
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getModifiers() == 4) {
            if (rmbmenu.getComponentCount() > 0) {
                rmbmenu.setVisible(true);
                rmbmenu.show(this, e.getX(), e.getY());
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
