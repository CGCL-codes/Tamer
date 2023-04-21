package edu.whitman.halfway.jigs.gui.desque;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DateAdjusterDialog extends JDialog implements PropertyChangeListener, ChangeListener {

    private JOptionPane optionPane;

    private SpinnerNumberModel[] spinModel;

    private JTextField origDateField, modDateField;

    private static int YEAR = 0, MONTH = 1, DAY = 2, HOUR = 3, MIN = 4, SEC = 5;

    private boolean dateAdjusted;

    private Date date;

    public DateAdjusterDialog(Frame frame) {
        super(frame, "Adjust Dates", true);
        spinModel = new SpinnerNumberModel[6];
        for (int i = 0; i < spinModel.length; i++) {
            spinModel[i] = new SpinnerNumberModel(new Integer(0), null, null, new Integer(1));
        }
        JPanel p1 = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout gridbag = new GridBagLayout();
        p1.setLayout(gridbag);
        Border title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Adjust Date(s):");
        p1.setBorder(title);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.0;
        c.weightx = 0.5;
        c.insets = new Insets(5, 10, 5, 5);
        gridAdd(p1, buildSpinner(YEAR), 0, 0, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 0, 5, 15);
        gridAdd(p1, newLabel("Years", Font.PLAIN), 1, 0, 1, c, gridbag);
        c.insets = new Insets(5, 5, 5, 5);
        gridAdd(p1, buildSpinner(MONTH), 2, 0, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 0, 5, 15);
        gridAdd(p1, newLabel("Months", Font.PLAIN), 3, 0, 1, c, gridbag);
        c.insets = new Insets(5, 5, 5, 5);
        gridAdd(p1, buildSpinner(DAY), 4, 0, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 0, 5, 5);
        gridAdd(p1, newLabel("Days", Font.PLAIN), 5, 0, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 10, 5, 5);
        gridAdd(p1, buildSpinner(HOUR), 0, 1, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 0, 5, 15);
        gridAdd(p1, newLabel("Hours", Font.PLAIN), 1, 1, 1, c, gridbag);
        c.insets = new Insets(5, 5, 5, 5);
        gridAdd(p1, buildSpinner(MIN), 2, 1, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 0, 5, 15);
        gridAdd(p1, newLabel("Min", Font.PLAIN), 3, 1, 1, c, gridbag);
        c.insets = new Insets(5, 5, 5, 5);
        gridAdd(p1, buildSpinner(SEC), 4, 1, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 0, 5, 5);
        gridAdd(p1, newLabel("Sec", Font.PLAIN), 5, 1, 1, c, gridbag);
        JPanel p2 = new JPanel();
        gridbag = new GridBagLayout();
        p2.setLayout(gridbag);
        title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Example:");
        p2.setBorder(title);
        c.weighty = 0.5;
        c.weightx = 0.5;
        c.insets = new Insets(5, 10, 5, 5);
        origDateField = new JTextField(20);
        origDateField.setEditable(false);
        gridAdd(p2, origDateField, 0, 0, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(2, 5, 2, 5);
        gridAdd(p2, newLabel("becomes", Font.PLAIN), 0, 1, 1, c, gridbag);
        c.weightx = 0.5;
        c.insets = new Insets(5, 10, 5, 5);
        modDateField = new JTextField(20);
        modDateField.setEditable(false);
        gridAdd(p2, modDateField, 0, 2, 1, c, gridbag);
        JPanel rootPanel = new JPanel();
        gridbag = new GridBagLayout();
        rootPanel.setLayout(gridbag);
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 1;
        gridAdd(rootPanel, p1, 0, 0, 1, c, gridbag);
        gridAdd(rootPanel, p2, 0, 1, 1, c, gridbag);
        Object[] options = { "OK", "Cancel" };
        optionPane = new JOptionPane(rootPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
        optionPane.addPropertyChangeListener(this);
        setContentPane(optionPane);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
    }

    private JSpinner buildSpinner(int i) {
        JSpinner spin = new JSpinner(spinModel[i]);
        spin.setPreferredSize(new Dimension(60, 25));
        spin.addChangeListener(this);
        return spin;
    }

    private JLabel newLabel(String s, int fontStyle) {
        JLabel jl = new JLabel(s);
        jl.setVerticalAlignment(JLabel.TOP);
        jl.setForeground(Color.black);
        jl.setFont(new Font(jl.getFont().getName(), fontStyle, 12));
        return jl;
    }

    private void gridAdd(Container root, Component obj, int x, int y, int width, GridBagConstraints c, GridBagLayout gridbag) {
        c.weighty = 0.0;
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        gridbag.setConstraints(obj, c);
        root.add(obj);
    }

    public void show(Date d) {
        dateAdjusted = false;
        if (origDateField == null) return;
        for (int i = 0; i < spinModel.length; i++) {
            spinModel[i].setValue(new Integer(0));
        }
        date = d;
        origDateField.setText(date.toString());
        modDateField.setText(date.toString());
        this.setVisible(true);
    }

    public void stateChanged(ChangeEvent e) {
        modDateField.setText(updateDate(date).toString());
    }

    public Date updateDate(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(cal.YEAR, spinModel[YEAR].getNumber().intValue());
        cal.add(cal.MONTH, spinModel[MONTH].getNumber().intValue());
        cal.add(cal.DAY_OF_YEAR, spinModel[DAY].getNumber().intValue());
        cal.add(cal.HOUR_OF_DAY, spinModel[HOUR].getNumber().intValue());
        cal.add(cal.MINUTE, spinModel[MIN].getNumber().intValue());
        cal.add(cal.SECOND, spinModel[SEC].getNumber().intValue());
        return cal.getTime();
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
            Object value = optionPane.getValue();
            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                return;
            }
            optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
            if (value.equals("OK")) {
                dateAdjusted = true;
            }
            setVisible(false);
        }
    }

    public boolean hasChanged() {
        return dateAdjusted;
    }
}
