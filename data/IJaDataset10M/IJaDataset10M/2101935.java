package datechooser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author  Przemek
 */
public class DateChooserDialog extends javax.swing.JDialog {

    private Date date;

    /** Creates new form DateChooserDialog */
    public DateChooserDialog(boolean modal) {
        super();
        setModal(modal);
        initComponents();
        initializeControls();
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jComboBoxDay = new javax.swing.JComboBox();
        jComboBoxMonth = new javax.swing.JComboBox();
        jComboBoxYear = new javax.swing.JComboBox();
        jButtonSet = new javax.swing.JButton();
        getContentPane().setLayout(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        jComboBoxDay.setMinimumSize(new java.awt.Dimension(36, 20));
        jComboBoxDay.setPreferredSize(new java.awt.Dimension(36, 22));
        jComboBoxDay.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxChanged(evt);
            }
        });
        getContentPane().add(jComboBoxDay);
        jComboBoxDay.setBounds(30, 20, 64, 22);
        jComboBoxMonth.setMinimumSize(new java.awt.Dimension(36, 20));
        jComboBoxMonth.setPreferredSize(new java.awt.Dimension(36, 22));
        jComboBoxMonth.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxChanged(evt);
            }
        });
        getContentPane().add(jComboBoxMonth);
        jComboBoxMonth.setBounds(110, 20, 64, 22);
        jComboBoxYear.setMinimumSize(new java.awt.Dimension(36, 20));
        jComboBoxYear.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxChanged(evt);
            }
        });
        getContentPane().add(jComboBoxYear);
        jComboBoxYear.setBounds(190, 20, 84, 22);
        jButtonSet.setText("Set");
        jButtonSet.setMinimumSize(new java.awt.Dimension(51, 20));
        jButtonSet.setPreferredSize(new java.awt.Dimension(64, 22));
        jButtonSet.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSetActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSet);
        jButtonSet.setBounds(300, 20, 64, 22);
        pack();
    }

    private void jButtonSetActionPerformed(java.awt.event.ActionEvent evt) {
        this.setModal(false);
        this.setVisible(false);
    }

    private void comboBoxChanged(java.awt.event.ActionEvent evt) {
        if (date == null || !isVisible()) return;
        if (!evt.getActionCommand().equals(jComboBoxDay.getActionCommand())) return;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(jComboBoxYear.getSelectedIndex() + 2000, jComboBoxMonth.getSelectedIndex(), jComboBoxDay.getSelectedIndex() + 1);
        date = c.getTime();
        setDaysControl();
    }

    private void initializeControls() {
        for (int i = 2000; i < 2100; i++) jComboBoxYear.addItem(Integer.toString(i));
        for (int i = 1; i <= 12; i++) jComboBoxMonth.addItem(Integer.toString(i));
        setDaysControl();
    }

    private void setDaysControl() {
        int selected = jComboBoxDay.getSelectedIndex();
        int daysNo = 31;
        if (date != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            daysNo = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        jComboBoxDay.removeAllItems();
        for (int i = 1; i <= daysNo; i++) jComboBoxDay.addItem(Integer.toString(i));
        jComboBoxDay.setSelectedIndex(Math.min(selected, daysNo - 1));
    }

    private void setControlsToDate() {
        Calendar c = Calendar.getInstance();
        c.setTime((Date) date.clone());
        jComboBoxYear.setSelectedIndex(Math.max(c.get(Calendar.YEAR) - 2000, 0));
        jComboBoxMonth.setSelectedIndex(c.get(Calendar.MONTH));
        setDaysControl();
        jComboBoxDay.setSelectedIndex(c.get(Calendar.DAY_OF_MONTH) - 1);
    }

    private javax.swing.JButton jButtonSet;

    private javax.swing.JComboBox jComboBoxDay;

    private javax.swing.JComboBox jComboBoxMonth;

    private javax.swing.JComboBox jComboBoxYear;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        setControlsToDate();
    }
}
