package tr.view.action;

import java.awt.Dialog;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodMonth.OnTheDay;
import tr.model.action.PeriodMonth.OnTheNth;
import tr.model.action.PeriodYear;

/**
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class PeriodYearlyPanel extends JPanel {

    /** Creates new instance. */
    public PeriodYearlyPanel(Dialog parent, PeriodYear periodYear) {
        this.parent = parent;
        this.periodYear = periodYear;
        initComponents();
        onTheCheckBox.setSelected(periodYear.isOnTheSelected());
        dayComboBox.setModel(getDayComboModel());
        dayComboBox.setSelectedItem(periodYear.getOnTheDay());
        nthComboBox.setModel(getNthComboModel());
        nthComboBox.setSelectedItem(periodYear.getOnTheNth());
        setEnabled(true);
    }

    private ComboBoxModel getDayComboModel() {
        return new DefaultComboBoxModel(PeriodMonth.OnTheDay.values());
    }

    private ComboBoxModel getNthComboModel() {
        return new DefaultComboBoxModel(PeriodMonth.OnTheNth.values());
    }

    private String getSelectedMonthsText() {
        return periodYear.getSelectedMonthsText();
    }

    /**
     * Receives notification of the start date so that a default month can
     * be set if necessary (i.e. if there is none already set).
     * @startDate The start date
     */
    public void notifyStartDate(Date startDate) {
        if (startDate == null) {
            return;
        }
        if (periodYear.getSelectedMonths().size() > 0) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        periodYear.select(calendar.get(Calendar.MONTH));
    }

    private void initComponents() {
        inLabel = new javax.swing.JLabel();
        monthsButton = new javax.swing.JButton();
        onTheCheckBox = new javax.swing.JCheckBox();
        nthComboBox = new javax.swing.JComboBox();
        dayComboBox = new javax.swing.JComboBox();
        setMaximumSize(new java.awt.Dimension(528, 26));
        setMinimumSize(new java.awt.Dimension(528, 26));
        setPreferredSize(new java.awt.Dimension(528, 26));
        inLabel.setText(NbBundle.getMessage(PeriodYearlyPanel.class, "YearsPanel.in"));
        inLabel.setMaximumSize(new java.awt.Dimension(20, 26));
        inLabel.setMinimumSize(new java.awt.Dimension(20, 26));
        inLabel.setPreferredSize(new java.awt.Dimension(20, 26));
        monthsButton.setText(getSelectedMonthsText());
        monthsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        monthsButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        monthsButton.setMaximumSize(new java.awt.Dimension(120, 26));
        monthsButton.setMinimumSize(new java.awt.Dimension(120, 26));
        monthsButton.setPreferredSize(new java.awt.Dimension(120, 26));
        monthsButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthsButtonAction(evt);
            }
        });
        onTheCheckBox.setText(NbBundle.getMessage(PeriodYearlyPanel.class, "months.on.the"));
        onTheCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        onTheCheckBox.setMaximumSize(new java.awt.Dimension(74, 26));
        onTheCheckBox.setMinimumSize(new java.awt.Dimension(74, 26));
        onTheCheckBox.setPreferredSize(new java.awt.Dimension(74, 26));
        onTheCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onTheCheckBoxAction(evt);
            }
        });
        nthComboBox.setMaximumSize(new java.awt.Dimension(120, 26));
        nthComboBox.setMinimumSize(new java.awt.Dimension(120, 26));
        nthComboBox.setPreferredSize(new java.awt.Dimension(120, 26));
        nthComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NthComboAction(evt);
            }
        });
        dayComboBox.setMaximumSize(new java.awt.Dimension(120, 26));
        dayComboBox.setMinimumSize(new java.awt.Dimension(120, 26));
        dayComboBox.setPreferredSize(new java.awt.Dimension(120, 26));
        dayComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayComboAction(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(inLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(monthsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(onTheCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(nthComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(dayComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, onTheCheckBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, inLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, monthsButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, nthComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, dayComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
    }

    private void dayComboAction(java.awt.event.ActionEvent evt) {
        periodYear.setOnTheDay((OnTheDay) dayComboBox.getSelectedItem());
    }

    private void NthComboAction(java.awt.event.ActionEvent evt) {
        periodYear.setOnTheNth((OnTheNth) nthComboBox.getSelectedItem());
    }

    private void onTheCheckBoxAction(java.awt.event.ActionEvent evt) {
        periodYear.setOnTheSelected(onTheCheckBox.isSelected());
        setEnabled(true);
    }

    private void monthsButtonAction(java.awt.event.ActionEvent evt) {
        MonthsOfYearDialog dialog = new MonthsOfYearDialog(parent, monthsButton);
        dialog.setPeriodYear(periodYear);
        dialog.setVisible(true);
        if (!dialog.cancelled()) {
            periodYear.setSelectedMonths(dialog.getSelectedMonths());
            monthsButton.setText(periodYear.getSelectedMonthsText());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        nthComboBox.setEnabled(enabled && onTheCheckBox.isSelected());
        dayComboBox.setEnabled(enabled && onTheCheckBox.isSelected());
        monthsButton.setEnabled(enabled);
        onTheCheckBox.setEnabled(enabled);
    }

    private javax.swing.JComboBox dayComboBox;

    private javax.swing.JLabel inLabel;

    private javax.swing.JButton monthsButton;

    private javax.swing.JComboBox nthComboBox;

    private javax.swing.JCheckBox onTheCheckBox;

    private final Dialog parent;

    private final PeriodYear periodYear;
}
