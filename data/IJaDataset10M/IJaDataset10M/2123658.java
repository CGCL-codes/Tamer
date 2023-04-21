package nekicalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.joda.time.DateTime;
import resources.Messages;
import application.Neki;

/**
 * @author kreed
 * used by JCalendar (visible at the left side of the app)
 */
public class JMonthTable extends JPanel {

    JTable table;

    MonthTableModel model;

    JCalendar jCalendar;

    Vector<Integer> selDays = new Vector<Integer>();

    int selectedDay;

    public JMonthTable(JCalendar jcal) {
        this.jCalendar = jcal;
        initGui();
    }

    public int getYear() {
        return model.getYear();
    }

    public int getMonth() {
        return model.getMonth();
    }

    public void setMonthYear(int month, int year) {
        if (month == 0) {
            month = 12;
            year--;
        } else if (month == 13) {
            month = 1;
            year++;
        }
        model.setMonthYear(month, year);
    }

    /**
	 * Marks days at the calendar given by a vector
	 * @param sdVect
	 */
    public void setSelectedDays(Vector<Integer> sdVect) {
        if (sdVect == null) {
            this.selDays.clear();
        } else this.selDays = sdVect;
        repaint();
    }

    public int getSelectedDay() {
        return this.selectedDay;
    }

    public void setSelectedDay(int sd) {
        this.selectedDay = sd;
    }

    private void initGui() {
        model = new MonthTableModel();
        model.refreshData();
        model.setMonthYear(1, 2008);
        table = new JTable(model);
        JPanel tablePlusHeader;
        tablePlusHeader = new JPanel();
        tablePlusHeader.setLayout(new BorderLayout());
        table.setPreferredSize(new Dimension(190, 98));
        table.getTableHeader().setPreferredSize(new Dimension(190, 20));
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        tablePlusHeader.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePlusHeader.add(table, BorderLayout.SOUTH);
        add(tablePlusHeader);
        table.setCellSelectionEnabled(true);
        table.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent arg0) {
                int col = table.getSelectedColumn();
                int row = table.getSelectedRow();
                String dayStr = (String) table.getValueAt(row, col);
                if (dayStr != "") {
                    setSelectedDay(Integer.parseInt((String) table.getValueAt(row, col)));
                    jCalendar.fireNotification("day");
                }
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setCellRenderer(new MyTableCellRenderer());
        }
    }

    public class MyTableCellRenderer extends JLabel implements TableCellRenderer {

        /**	Days with a run  */
        Color markedColor = Neki.currentProfile.getColorScheme().calRunDay;

        /**	Days without a run */
        Color defaultColor = Color.white;

        /** Empty Date Field	*/
        Color emptyColor = new Color(242, 242, 242);

        public MyTableCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
            if (value instanceof String) {
                String s = (String) value;
                if (s != "") {
                    this.setBackground(defaultColor);
                    int cval = Integer.parseInt(s);
                    for (Integer day : selDays) {
                        if (cval == day) {
                            this.setBackground(markedColor);
                            break;
                        } else {
                            this.setBackground(defaultColor);
                        }
                    }
                    if (cval == getSelectedDay()) {
                        if (!(this.getBackground().equals(defaultColor) || this.getBackground().equals(emptyColor))) {
                            this.setBackground(Neki.currentProfile.getColorScheme().calRunDayActive);
                        }
                    }
                } else {
                    this.setBackground(emptyColor);
                }
            }
            if (hasFocus) {
            }
            setText(value.toString());
            setToolTipText((String) value);
            return this;
        }

        public void validate() {
        }

        public void revalidate() {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }
    }

    public class MonthTableModel extends AbstractTableModel {

        private String[] columnNames = { Messages.getString("JMonthTable.mo-nday"), Messages.getString("JMonthTable.tu-esday"), Messages.getString("JMonthTable.we-dnesday"), Messages.getString("JMonthTable.th-ursday"), Messages.getString("JMonthTable.fr-iday"), Messages.getString("JMonthTable.sa-turday"), Messages.getString("JMonthTable.su-nday") };

        DateTime dateTime = new DateTime(System.currentTimeMillis());

        String[][] data = new String[6][7];

        public void setMonthYear(int month, int year) {
            this.dateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
            refreshData();
            System.out.println(":: JMonthTable - setMonthYear (" + month + "," + year + ")");
        }

        public int getYear() {
            return this.dateTime.getYear();
        }

        public int getMonth() {
            return this.dateTime.getMonthOfYear();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public void refreshData() {
            System.out.println(":: JMonthTable - refresh");
            int firstday = this.dateTime.getDayOfWeek() + 1;
            int numberOfDays = this.dateTime.dayOfMonth().getMaximumValue();
            int cnt = 1;
            boolean start = false;
            for (int rows = 0; rows < 6; rows++) {
                for (int cols = 0; cols < 7; cols++) {
                    if (rows == 0) {
                        if (firstday == 1 && cols == 6) {
                            start = true;
                        } else {
                            if ((firstday - 2) % 7 == cols) {
                                start = true;
                            }
                        }
                    } else {
                        if (rows >= 4) {
                            if (cnt > numberOfDays) {
                                start = false;
                            }
                        }
                    }
                    if (start) {
                        data[rows][cols] = String.valueOf(cnt);
                        cnt++;
                    } else {
                        data[rows][cols] = "";
                    }
                }
            }
        }

        public int getColumnCount() {
            return 7;
        }

        public int getRowCount() {
            return 6;
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
    }
}
