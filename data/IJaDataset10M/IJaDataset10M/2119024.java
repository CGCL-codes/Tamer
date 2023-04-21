package ledestin.swing;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
The class provides a model for JComboBox allowing to select from a range of 
years. firstYear and lastYear properties can be set, confining the range of 
years.
*/
public class YearsComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private Calendar firstYear = Calendar.getInstance();

    private Calendar lastYear = Calendar.getInstance();

    private int selectedYear;

    public YearsComboBoxModel(int firstYear, int lastYear) {
        this.firstYear.set(Calendar.YEAR, firstYear);
        this.lastYear.set(Calendar.YEAR, lastYear);
        setSelectedYear(getLastYear());
    }

    public void setFirstYear(int firstYear) throws IllegalArgumentException {
        if (firstYear < getLastYear()) {
            int range = getFirstYear() - firstYear;
            this.firstYear.set(Calendar.YEAR, firstYear);
            int lastIndex = getSize() - 1;
            if (range < 0) {
                fireIntervalRemoved(this, lastIndex + 1, lastIndex - range);
            } else if (range > 0) {
                fireIntervalAdded(this, lastIndex - range + 1, lastIndex);
            }
        } else {
            throw new IllegalArgumentException("First year must be less than last year");
        }
    }

    public int getFirstYear() {
        return firstYear.get(Calendar.YEAR);
    }

    public void setLastYear(int lastYear) throws IllegalArgumentException {
        if (lastYear > getFirstYear()) {
            int range = lastYear - getLastYear();
            this.lastYear.set(Calendar.YEAR, lastYear);
            int lastIndex = getSize() - 1;
            if (range < 0) {
                fireIntervalRemoved(this, 0, Math.abs(range) - 1);
            } else if (range > 0) {
                fireIntervalAdded(this, 0, Math.abs(range) - 1);
            }
        } else {
            throw new IllegalArgumentException("Last year must be larger than first year");
        }
    }

    public int getLastYear() {
        return lastYear.get(Calendar.YEAR);
    }

    protected void setSelectedYear(int year) {
        selectedYear = year;
    }

    protected int getSelectedYear() {
        return selectedYear;
    }

    public Object getElementAt(int index) {
        return String.valueOf(getLastYear() - index);
    }

    public int getSize() {
        return getLastYear() - getFirstYear() + 1;
    }

    public void setSelectedItem(Object anItem) {
        setSelectedYear(Integer.parseInt((String) anItem));
    }

    public Object getSelectedItem() {
        return String.valueOf(getSelectedYear());
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("YearsComboBoxModel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final YearsComboBoxModel yearsModel = new YearsComboBoxModel(1997, 2000);
        JComboBox cb = new JComboBox(yearsModel);
        JLabel l1 = new JLabel("firstYear");
        final JTextField firstYear = new JTextField();
        firstYear.setText(String.valueOf(yearsModel.getFirstYear()));
        JLabel l2 = new JLabel("lastYear");
        final JTextField lastYear = new JTextField();
        lastYear.setText(String.valueOf(yearsModel.getLastYear()));
        JButton btn = new JButton("Apply changes");
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                yearsModel.setFirstYear(Integer.parseInt(firstYear.getText()));
                yearsModel.setLastYear(Integer.parseInt(lastYear.getText()));
            }
        });
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout());
        pane.add(cb);
        pane.add(l1);
        pane.add(firstYear);
        pane.add(l2);
        pane.add(lastYear);
        pane.add(btn);
        frame.pack();
        frame.show();
    }
}
