package aurora.hwc.control;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Panel for editing traffic responsive controller properties.
 * @author Alex Kurzhanskiy
 * @version $Id: PanelControllerTR.java 38 2010-02-08 22:59:00Z akurzhan $
 */
public final class PanelControllerTR extends AbstractPanelSimpleController {

    private static final long serialVersionUID = 3757750722401395160L;

    private Vector<Double> t_dns = new Vector<Double>();

    private Vector<Double> t_flw = new Vector<Double>();

    private Vector<Double> t_spd = new Vector<Double>();

    private Vector<Double> rate = new Vector<Double>();

    private JButton buttonAdd = new JButton("Add");

    private JButton buttonDelete = new JButton("Delete");

    private JTable trtable;

    private TRTableModel trtablemodel = new TRTableModel();

    /**
	 * Fills the panel with traffic responsive controller specific fields.
	 */
    protected void fillPanel() {
        if (controller != null) {
            Vector<Double> dns = ((ControllerTR) controller).getDensityThresholds();
            Vector<Double> flw = ((ControllerTR) controller).getFlowThresholds();
            Vector<Double> spd = ((ControllerTR) controller).getSpeedThresholds();
            Vector<Double> rt = ((ControllerTR) controller).getRates();
            for (int i = 0; i < dns.size(); i++) {
                t_dns.add(dns.get(i));
                t_flw.add(flw.get(i));
                t_spd.add(spd.get(i));
                rate.add(rt.get(i));
            }
        }
        JPanel tabpanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        tabpanel.setBorder(BorderFactory.createTitledBorder("Thresholds and Rates"));
        trtable = new JTable(trtablemodel);
        trtable.setPreferredScrollableViewportSize(new Dimension(200, 50));
        trtable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = trtable.rowAtPoint(new Point(e.getX(), e.getY()));
                    if ((row > t_dns.size() - 1) || (row < 0)) return;
                    try {
                        WindowEdit winEdit = new WindowEdit(null, row, t_dns.get(row), t_flw.get(row), t_spd.get(row), rate.get(row));
                        winEdit.setVisible(true);
                        trtablemodel.deleterow(row);
                        trtablemodel.addrow(winEdit.getID(), winEdit.getDensity(), winEdit.getFlow(), winEdit.getSpeed(), winEdit.getRate());
                    } catch (Exception excp) {
                    }
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        tabpanel.add(new JScrollPane(trtable), c);
        c.ipady = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        tabpanel.add(buttonAdd, c);
        c.gridx = 1;
        tabpanel.add(buttonDelete, c);
        add(tabpanel);
        buttonAdd.setEnabled(true);
        buttonAdd.addActionListener(new ButtonAddListener());
        buttonDelete.setEnabled(true);
        buttonDelete.addActionListener(new ButtonDeleteListener());
        return;
    }

    /**
	 * Saves controller properties.
	 */
    public synchronized void save() {
        ((ControllerTR) controller).setData(t_dns, t_flw, t_spd, rate);
        super.save();
        return;
    }

    /**
	 * Class needed for displaying TOD table.
	 */
    private class TRTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -8032577329195548779L;

        public String getColumnName(int col) {
            switch(col) {
                case 0:
                    return "Level";
                case 1:
                    return "Density (vpml)";
                case 2:
                    return "Flow (vphl)";
                case 3:
                    return "Speed (mph)";
                default:
                    return "Rate (vph)";
            }
        }

        public int getColumnCount() {
            return 5;
        }

        public int getRowCount() {
            return t_dns.size();
        }

        public void addrow(int id, double dns, double flw, double spd, double rt) {
            if ((id < 0) || (dns < 0) || (flw < 0) || (spd < 0) || (rt < 0)) return;
            int idx = t_dns.size();
            if (id < idx) idx = id;
            t_dns.insertElementAt(dns, idx);
            t_flw.insertElementAt(flw, idx);
            t_spd.insertElementAt(spd, idx);
            rate.insertElementAt(rt, idx);
            fireTableStructureChanged();
        }

        public void deleterow(int i) {
            t_dns.removeElementAt(i);
            t_flw.removeElementAt(i);
            t_spd.removeElementAt(i);
            rate.removeElementAt(i);
            fireTableStructureChanged();
        }

        public Object getValueAt(int row, int column) {
            if ((row < 0) || (row > t_dns.size() - 1) || (column < 0) || (column > 4)) return null;
            NumberFormat form = NumberFormat.getInstance();
            form.setMaximumFractionDigits(2);
            form.setMinimumFractionDigits(0);
            String s;
            switch(column) {
                case 0:
                    s = "" + row;
                    break;
                case 1:
                    s = form.format(t_dns.get(row));
                    break;
                case 2:
                    s = form.format(t_flw.get(row));
                    break;
                case 3:
                    s = form.format(t_spd.get(row));
                    break;
                default:
                    s = form.format(rate.get(row));
                    break;
            }
            return s;
        }

        public boolean isCellEditable(int row, int column) {
            if (column > 0) return true;
            return false;
        }

        public void setValueAt(Object value, int row, int column) {
            if ((row < 0) || (row >= t_dns.size()) || (column == 0)) return;
            String buf = (String) value;
            try {
                double v = Double.parseDouble(buf);
                if ((v >= 0.0) && (v <= 99999.99)) switch(column) {
                    case 1:
                        t_dns.set(row, v);
                        break;
                    case 2:
                        t_flw.set(row, v);
                        break;
                    case 3:
                        t_spd.set(row, v);
                        break;
                    default:
                        rate.set(row, v);
                        break;
                }
            } catch (Exception e) {
            }
            fireTableRowsUpdated(row, row);
            return;
        }
    }

    /**
	 * This class is needed to react to "Add" button pressed.
	 */
    private class ButtonAddListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            try {
                WindowAdd winAdd = new WindowAdd(null);
                winAdd.setVisible(true);
            } catch (Exception e) {
            }
            return;
        }
    }

    /**
	 * This class is needed to react to "Delete" button pressed.
	 */
    private class ButtonDeleteListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            try {
                int[] selected = trtable.getSelectedRows();
                if ((selected != null) && (selected.length > 0)) for (int i = 0; i < selected.length; i++) {
                    System.err.println(selected[i]);
                    int idx = selected[i] - i;
                    if ((idx >= 0) && (idx < t_dns.size())) trtablemodel.deleterow(idx);
                }
            } catch (Exception e) {
            }
            return;
        }
    }

    /**
	 * This class implements an editor widow for an entry to a threshold table.
	 */
    private abstract class WindowTR extends JDialog {

        private static final long serialVersionUID = 4112302361380265274L;

        private JSpinner lid;

        private JSpinner density;

        private JSpinner flow;

        private JSpinner speed;

        private JSpinner rate;

        protected int l_id = 0;

        protected double l_dns = 0;

        protected double l_flw = 0;

        protected double l_spd = 0;

        protected double l_rate = 0;

        protected boolean toAdd = true;

        private static final String cmdOK = "pressedOK";

        private static final String cmdCancel = "pressedCancel";

        public WindowTR(JFrame parent, String title) {
            super(parent, title);
            setSize(300, 420);
            setLocationRelativeTo(parent);
            setModal(true);
        }

        public int getID() {
            return l_id;
        }

        public double getDensity() {
            return l_dns;
        }

        public double getFlow() {
            return l_flw;
        }

        public double getSpeed() {
            return l_spd;
        }

        public double getRate() {
            return l_rate;
        }

        protected JPanel createForm() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
            JPanel pID = new JPanel(new BorderLayout());
            pID.setBorder(BorderFactory.createTitledBorder("Level"));
            lid = new JSpinner(new SpinnerNumberModel(l_id, 0, 999, 1));
            lid.setEditor(new JSpinner.NumberEditor(lid, "##0"));
            pID.add(lid);
            panel.add(pID);
            JPanel pD = new JPanel(new BorderLayout());
            pD.setBorder(BorderFactory.createTitledBorder("Density Threshold (vpml)"));
            density = new JSpinner(new SpinnerNumberModel(l_dns, 0.0, 99999.99, 1));
            density.setEditor(new JSpinner.NumberEditor(density, "####0.##"));
            pD.add(density);
            panel.add(pD);
            JPanel pF = new JPanel(new BorderLayout());
            pF.setBorder(BorderFactory.createTitledBorder("Flow Threshold (vphl)"));
            flow = new JSpinner(new SpinnerNumberModel(l_flw, 0.0, 99999.99, 10));
            flow.setEditor(new JSpinner.NumberEditor(flow, "####0.##"));
            pF.add(flow);
            panel.add(pF);
            JPanel pS = new JPanel(new BorderLayout());
            pS.setBorder(BorderFactory.createTitledBorder("Speed Threshold (mph)"));
            speed = new JSpinner(new SpinnerNumberModel(l_spd, 0.0, 99999.99, 1));
            speed.setEditor(new JSpinner.NumberEditor(speed, "####0.##"));
            pS.add(speed);
            panel.add(pS);
            JPanel pR = new JPanel(new BorderLayout());
            pR.setBorder(BorderFactory.createTitledBorder("Rate (vph)"));
            rate = new JSpinner(new SpinnerNumberModel(l_rate, 0.0, 99999.99, 10));
            rate.setEditor(new JSpinner.NumberEditor(rate, "####0.##"));
            pR.add(rate);
            panel.add(pR);
            JPanel bp = new JPanel(new FlowLayout());
            JButton bOK = new JButton("    OK    ");
            bOK.setActionCommand(cmdOK);
            bOK.addActionListener(new ButtonEventsListener());
            JButton bCancel = new JButton("Cancel");
            bCancel.setActionCommand(cmdCancel);
            bCancel.addActionListener(new ButtonEventsListener());
            bp.add(bOK);
            bp.add(bCancel);
            panel.add(bp);
            return panel;
        }

        private class ButtonEventsListener implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (cmdOK.equals(cmd)) {
                    l_id = ((Integer) lid.getValue());
                    l_dns = ((Double) density.getValue());
                    l_flw = ((Double) flow.getValue());
                    l_spd = ((Double) speed.getValue());
                    l_rate = ((Double) rate.getValue());
                    if (toAdd) trtablemodel.addrow(l_id, l_dns, l_flw, l_spd, l_rate); else trtablemodel.fireTableDataChanged();
                }
                setVisible(false);
                dispose();
                return;
            }
        }
    }

    private final class WindowAdd extends WindowTR {

        private static final long serialVersionUID = 96083476285596430L;

        public WindowAdd(JFrame parent) {
            super(parent, "New");
            setContentPane(createForm());
        }
    }

    private final class WindowEdit extends WindowTR {

        private static final long serialVersionUID = 1229903996266521378L;

        public WindowEdit(JFrame parent, int id, double dns, double flw, double spd, double rt) {
            super(parent, "Edit");
            l_id = id;
            l_dns = dns;
            l_flw = flw;
            l_spd = spd;
            l_rate = rt;
            toAdd = false;
            setContentPane(createForm());
        }
    }
}
