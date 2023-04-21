package org.gtdfree.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author ikesan
 *
 */
public class TimeInputPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String TIME_OF_DAY = "timeOfDay";

    public static void main(String[] args) {
        TimeInputPanel p = new TimeInputPanel();
        p.addPropertyChangeListener(TIME_OF_DAY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println(evt.getNewValue());
            }
        });
        JFrame f = new JFrame();
        f.setContentPane(p);
        f.pack();
        f.setVisible(true);
    }

    private JComboBox hour;

    private JComboBox minutes;

    private int timeOfDay;

    public TimeInputPanel() {
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        hour = new JComboBox(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29 });
        hour.setEditable(false);
        hour.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateTime();
                }
            }
        });
        minutes = new JComboBox(new Integer[] { 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55 });
        minutes.setEditable(false);
        minutes.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateTime();
                }
            }
        });
        add(hour, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(new JLabel(":"), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(minutes, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    private void updateTime() {
        setTimeOfDay(60 * ((Integer) hour.getSelectedItem()).intValue() + ((Integer) minutes.getSelectedItem()).intValue());
    }

    /**
	 * @return the timeOfDay
	 */
    public int getTimeOfDay() {
        return timeOfDay;
    }

    /**
	 * @param timeOfDay the timeOfDay to set
	 */
    public void setTimeOfDay(int timeOfDay) {
        if (this.timeOfDay == timeOfDay) {
            return;
        }
        int old = this.timeOfDay;
        this.timeOfDay = timeOfDay;
        hour.setSelectedItem(timeOfDay / 60);
        minutes.setSelectedItem(timeOfDay % 60);
        firePropertyChange(TIME_OF_DAY, old, timeOfDay);
    }
}
