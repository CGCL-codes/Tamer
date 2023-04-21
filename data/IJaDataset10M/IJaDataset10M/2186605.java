package com.teletalk.jadmin.gui.log;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import com.teletalk.jserver.log.LogFilter;
import com.teletalk.jserver.util.ComparableConstraint;
import com.teletalk.jserver.util.Constraint;
import com.teletalk.jserver.util.StringConstraint;

/**
 * Class handling the dialog box when log message filter rules can be created.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Alpha
 */
public final class FilterDialog extends JDialog implements ActionListener {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    private final JComboBox field;

    private final JComboBox operator;

    private final DefaultComboBoxModel operatorModel;

    private final JTextField constraint;

    private final JPanel constraintPanel;

    private final JComboBox logLevel;

    private boolean logLevelSelected = false;

    private final JButton ok;

    private final JButton cancel;

    private Date parsedDate = null;

    private Integer parsedInt = null;

    private boolean endOk = false;

    private final SimpleDateFormat dateFormat;

    private final String[] logLevelNames = { "Debug", "Info", "Warning", "Error", "Critical error" };

    /**
	 * Creates a new FilterDialog.
	 */
    public FilterDialog(final Frame owner) {
        super(owner, "Add new filter constraint", true);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.setLayout(new BorderLayout());
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        JPanel upper = new JPanel();
        String[] fieldNames = LogFilter.getFields();
        field = new JComboBox(fieldNames);
        field.setMaximumRowCount(3);
        field.setSelectedIndex(0);
        field.addActionListener(this);
        upper.add(new JLabel("Select field: "));
        upper.add(field);
        JPanel middle = new JPanel();
        operatorModel = new DefaultComboBoxModel();
        operator = new JComboBox(operatorModel);
        operator.setMaximumRowCount(3);
        middle.add(new JLabel("Select operator: "));
        middle.add(operator);
        logLevel = new JComboBox(logLevelNames);
        logLevel.setMaximumRowCount(3);
        logLevel.setSelectedIndex(0);
        logLevel.addActionListener(this);
        logLevel.setEditable(true);
        constraintPanel = new JPanel();
        constraint = new JTextField(15);
        constraintPanel.add(new JLabel("Filter constraint: "));
        constraintPanel.add(constraint);
        filterPanel.add(upper);
        filterPanel.add(middle);
        filterPanel.add(constraintPanel);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        ok = new JButton("Ok");
        ok.addActionListener(this);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        southPanel.add(ok);
        southPanel.add(cancel);
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        updateFieldSelection();
    }

    /**
	 * Updates the GUI depending on which field is selected.
	 */
    private void updateFieldSelection() {
        String selField = (String) field.getSelectedItem();
        if (selField.equals(LogFilter.LEVEL_FIELD)) {
            if (!logLevelSelected) {
                constraintPanel.removeAll();
                constraintPanel.add(new JLabel("Filter constraint: "));
                constraintPanel.add(logLevel);
                logLevelSelected = true;
                pack();
            }
        } else {
            if (logLevelSelected) {
                constraintPanel.removeAll();
                constraintPanel.add(new JLabel("Filter constraint: "));
                constraintPanel.add(constraint);
                logLevelSelected = false;
                pack();
            }
            if (selField.equals(LogFilter.TIME_FIELD)) constraint.setText(dateFormat.format(new Date())); else constraint.setText("");
        }
        updateOperatorCombo();
    }

    /**
	 * Updates the operator combo box depending on which field is selected.
	 */
    private void updateOperatorCombo() {
        String selObj = (String) field.getSelectedItem();
        Object[] ops;
        if (selObj.equals(LogFilter.LEVEL_FIELD) || selObj.equals(LogFilter.TIME_FIELD)) {
            ((Class) ComparableConstraint.class).getClass();
            ops = ComparableConstraint.getConstraintOperators();
        } else {
            ((Class) StringConstraint.class).getClass();
            ops = StringConstraint.getConstraintOperators();
        }
        if (operatorModel.getSize() > 0) operatorModel.removeAllElements();
        for (int i = 0; i < ops.length; i++) {
            operatorModel.addElement(((Constraint.ConstraintOperator) ops[i]).toString(true));
            operatorModel.addElement(((Constraint.ConstraintOperator) ops[i]).toString(false));
        }
        pack();
    }

    /**
	 * Checks if the dialog box was closed ok.
	 */
    public boolean closedOk() {
        return endOk;
    }

    /**
	 * Gets the field for which a filter rule (constraint) should be added.
	 */
    public String getField() {
        return (String) field.getSelectedItem();
    }

    /**
	 * Gets the filter rule (constraint).
	 */
    public Constraint getConstraint() {
        String selField = (String) field.getSelectedItem();
        String selOperator = (String) operator.getSelectedItem();
        Constraint.ConstraintOperator constraintOp;
        boolean not = true;
        if (selField.equals(LogFilter.LEVEL_FIELD)) {
            if (parsedInt != null) {
                if ((constraintOp = ComparableConstraint.getConstraintOperator(selOperator, false)) != null) not = false; else constraintOp = ComparableConstraint.getConstraintOperator(selOperator, true);
                return new ComparableConstraint(constraintOp, parsedInt, not);
            } else return null;
        } else if (selField.equals(LogFilter.TIME_FIELD)) {
            if (parsedDate != null) {
                if ((constraintOp = ComparableConstraint.getConstraintOperator(selOperator, false)) != null) not = false; else constraintOp = ComparableConstraint.getConstraintOperator(selOperator, true);
                return new ComparableConstraint(constraintOp, parsedDate, not);
            } else return null;
        } else {
            if ((constraintOp = StringConstraint.getConstraintOperator(selOperator, false)) != null) not = false; else constraintOp = StringConstraint.getConstraintOperator(selOperator, true);
            return new StringConstraint(constraintOp, constraint.getText(), not);
        }
    }

    /**
	 * Gets a string describing the filter rule and the field for which it was added.
	 */
    public String getFilterString() {
        String selField = (String) field.getSelectedItem();
        if (selField.equals(LogFilter.LEVEL_FIELD)) {
            ComparableConstraint cons = (ComparableConstraint) getConstraint();
            String operandName;
            if (parsedInt.intValue() >= logLevelNames.length) operandName = parsedInt.toString(); else operandName = logLevelNames[parsedInt.intValue()];
            return "...field '" + getField() + "' " + cons.getConstraintOperator().toString(cons.isNot()) + " '" + operandName + "'";
        } else return "...field '" + getField() + "' " + getConstraint();
    }

    /**
	 * Action event handler method.
	 */
    public void actionPerformed(final ActionEvent event) {
        String selField = (String) field.getSelectedItem();
        if (event.getSource() == ok) {
            boolean ok = true;
            String errStr = "?";
            if (selField.equals(LogFilter.LEVEL_FIELD)) {
                try {
                    int selIndex = logLevel.getSelectedIndex();
                    if (selIndex >= 0) parsedInt = new Integer(selIndex); else parsedInt = new Integer(logLevel.getEditor().getItem().toString());
                } catch (NumberFormatException e) {
                    ok = false;
                    errStr = "'" + constraint.getText() + "' isn't a valid integer value.";
                }
            } else if (!constraint.getText().equals("")) {
                if (selField.equals(LogFilter.TIME_FIELD)) {
                    try {
                        parsedDate = dateFormat.parse(constraint.getText());
                    } catch (ParseException e) {
                        ok = false;
                        errStr = "'" + constraint.getText() + "' isn't a valid date/time value.";
                    }
                }
            } else {
                ok = false;
                errStr = "No constraint specified.";
            }
            if (ok) {
                endOk = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, errStr, "Inputformat error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (event.getSource() == cancel) {
            endOk = false;
            dispose();
        } else if (event.getSource() == field) {
            updateFieldSelection();
        }
    }
}
