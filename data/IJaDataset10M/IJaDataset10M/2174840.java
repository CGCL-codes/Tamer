package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FilterPanel extends JPanel {

    private JSpinner daySpinner, yearSpinner;

    private JComboBox monthComboBox;

    private JCheckBox filterEnable;

    private JButton showSelection;

    private GregorianCalendar calendar;

    private Date currentDate = null;

    private boolean leapYear = false;

    public FilterPanel() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        filterEnable = new JCheckBox("Show only elements added after");
        filterEnable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (filterEnable.isSelected()) {
                    fireDateChange();
                } else {
                    fireDateRemoved();
                }
            }
        });
        calendar = new GregorianCalendar();
        leapYear = calendar.isLeapYear(calendar.get(GregorianCalendar.YEAR));
        daySpinner = new JSpinner(new SpinnerNumberModel());
        ((SpinnerNumberModel) daySpinner.getModel()).setMinimum(1);
        ((SpinnerNumberModel) daySpinner.getModel()).setMaximum(31);
        daySpinner.setValue(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        monthComboBox = new JComboBox();
        for (Month m : Month.values()) {
            monthComboBox.addItem(m);
        }
        monthComboBox.setSelectedIndex(calendar.get(GregorianCalendar.MONTH));
        monthComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();
                Month m = (Month) source.getSelectedItem();
                monthsChanged(m);
            }
        });
        yearSpinner = new JSpinner();
        yearSpinner.setValue(calendar.get(GregorianCalendar.YEAR));
        yearSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                yearsChanged(e);
            }
        });
        showSelection = new JButton("show");
        showSelection.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                filterEnable.setSelected(true);
                fireDateChange();
            }
        });
    }

    private void initLayout() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.insets = new Insets(0, 0, 40, 0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(filterEnable, c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.VERTICAL;
        this.add(daySpinner, c);
        c.gridx = 1;
        c.gridy = 3;
        this.add(monthComboBox, c);
        c.gridx = 2;
        c.gridy = 3;
        this.add(yearSpinner, c);
        c.gridx = 2;
        c.gridy = 4;
        c.insets = new Insets(40, 0, 0, 0);
        this.add(showSelection, c);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, 0);
    }

    private void fireDateChange() {
        GregorianCalendar c = new GregorianCalendar((Integer) yearSpinner.getValue(), ((Month) monthComboBox.getSelectedItem()).getIndex() - 1, (Integer) daySpinner.getValue());
        firePropertyChange("new_date", currentDate, c);
        currentDate = c.getTime();
    }

    private void fireDateRemoved() {
        firePropertyChange("new_date", currentDate, null);
        currentDate = null;
    }

    private void monthsChanged(Month m) {
        int max = m.getNumberOfDaysInMonth();
        if (leapYear && m.equals(Month.FEBRUARY)) {
            max++;
        }
        ((SpinnerNumberModel) daySpinner.getModel()).setMaximum(max);
        if ((Integer) daySpinner.getValue() > max) {
            daySpinner.setValue(max);
        }
    }

    private void yearsChanged(ChangeEvent e) {
        JSpinner source = (JSpinner) e.getSource();
        int year = (Integer) source.getValue();
        leapYear = calendar.isLeapYear(year);
        Month m = (Month) monthComboBox.getSelectedItem();
        monthsChanged(m);
    }
}
