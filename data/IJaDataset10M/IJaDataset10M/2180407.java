package seneca.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.Vector;
import seneca.structgen.*;
import seneca.judges.*;
import seneca.core.*;
import compchem.*;

/**
 * This class provides a gui for configuring a 2D-spectrum Judges
 */
abstract class TwoDSpectrumJudgeConfigurator extends JudgeConfigurator {

    protected transient SpectrumAssigner sa;

    protected TwoDSpectrumJudge twodsse;

    TwoDSpectrumJudgeConfigurator(SenecaDataset sd, TwoDSpectrumJudge tJ, SpectrumAssigner sa, String title) {
        super(sd, tJ);
        this.twodsse = tJ;
        this.sa = sa;
    }

    protected Box contructCenterBox() {
        table = new JTable(new TwoDSpectrumAssignmentTableModel());
        Box centerBox = new Box(BoxLayout.X_AXIS);
        scrollpane = new JScrollPane(table);
        scrollpane.setBorder(getTitledBorder());
        scrollpane.setPreferredSize(new Dimension(200, 400));
        centerBox.add(scrollpane);
        return centerBox;
    }

    class TwoDSpectrumAssignmentTableModel extends AbstractTableModel {

        TwoDSpectrumAssignmentTableModel() {
        }

        public int getColumnCount() {
            if (twodsse == null || twodsse.assignment == null) {
                return 0;
            }
            return twodsse.assignment.length + 1;
        }

        public String getColumnName(int col) {
            if (col == 0) return "No.";
            return new Integer(col).toString();
        }

        /**
         *
         **/
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /**
         *
         **/
        public boolean isCellEditable(int row, int col) {
            if (col == 0) return false;
            return true;
        }

        public int getRowCount() {
            int rc = 0;
            if (twodsse == null || twodsse.assignment == null) {
                return 0;
            } else {
                for (int i = 0; i < twodsse.assignment.length; i++) {
                    for (int j = 0; j < twodsse.assignment.length; j++) {
                        if (twodsse.assignment[i][j][j]) {
                            rc++;
                        }
                    }
                }
            }
            return rc;
        }

        public Object getValueAt(int row, int column) {
            int rc = 0;
            for (int i = 0; i < twodsse.assignment.length; i++) {
                for (int j = 0; j < twodsse.assignment.length; j++) {
                    if (twodsse.assignment[i][j][j]) rc++;
                    if (rc - 1 == row) {
                        if (column == 0) return new Integer(i + 1);
                        if (twodsse.assignment[i][j][column - 1]) return new Boolean(true); else return new Boolean(false);
                    }
                }
            }
            return null;
        }

        public void setValueAt(Object aValue, int row, int column) {
        }
    }

    protected void report() {
        JFrame frame = new JFrame();
        frame.setTitle("Judge Configuration Report");
        JTextPane text = new JTextPane();
        text.setPreferredSize(new Dimension(400, 400));
        text.setText(judge.toString());
        frame.getContentPane().add(text);
        frame.pack();
        frame.setVisible(true);
    }

    public void autoconfigure() {
        sa.setSenecaDataset(sd);
        if (!sa.assign()) {
            System.out.println("Assignment failed.");
            twodsse.setEnabled(false);
            return;
        }
        System.out.println("Assignment successful.");
        System.out.println(twodsse.assignment);
        twodsse.setEnabled(true);
        table.setModel(new TwoDSpectrumAssignmentTableModel());
        scrollpane.revalidate();
    }
}
