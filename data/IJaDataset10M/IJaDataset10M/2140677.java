package uqdsd.infosec;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import uqdsd.infosec.model.StandardComponent;
import uqdsd.infosec.model.faultset.FaultMode;

/**
 * @author InfoSec Project (c) 2008 UQ Editor UI to enter new fault modes.
 */
public class FaultModesEditor extends JPanel implements ActionListener, ListSelectionListener, KeyListener {

    static final long serialVersionUID = 0;

    private static String ADD_ACTION = "add";

    private static String REMOVE_ACTION = "remove";

    private static String EDIT_ACTION = "edit";

    private static String NORMAL_ACTION = "normal";

    private static String FAULT_ACTION = "fault";

    private DefaultListModel faultModeModel;

    private JList faultModeList;

    private JTextField abbreviationInput = new JTextField();

    private JTextField descriptionInput = new JTextField();

    private JRadioButton normRadio = new JRadioButton("Normal");

    private JRadioButton faultRadio = new JRadioButton("Fault");

    private StandardComponent theComponent;

    private JButton removeButton, addButton, editButton;

    private ButtonGroup bg;

    private JFormattedTextField probInput = new JFormattedTextField();

    public FaultModesEditor(StandardComponent aSystemComponent) {
        theComponent = aSystemComponent;
        faultModeModel = theComponent.getFaultModes();
        faultModeList = new JList(faultModeModel);
        faultModeList.setCellRenderer(new FaultModeCellRenderer());
        faultModeList.addListSelectionListener(this);
        abbreviationInput.addKeyListener(this);
        descriptionInput.addKeyListener(this);
        descriptionInput.addActionListener(this);
        setLayout(new BorderLayout());
        add(new JLabel("Operation Modes"), BorderLayout.NORTH);
        add(new JScrollPane(faultModeList), BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        JPanel inputPanel1 = new JPanel(new BorderLayout());
        inputPanel1.add(new JLabel("Abbreviation: "), BorderLayout.WEST);
        abbreviationInput.setMaximumSize(abbreviationInput.getPreferredSize());
        inputPanel1.add(abbreviationInput, BorderLayout.CENTER);
        southPanel.add(inputPanel1);
        JPanel inputPanel2 = new JPanel(new BorderLayout());
        inputPanel2.add(new JLabel("Description: "), BorderLayout.WEST);
        inputPanel2.add(descriptionInput, BorderLayout.CENTER);
        southPanel.add(inputPanel2);
        normRadio.setActionCommand(NORMAL_ACTION);
        normRadio.addActionListener(this);
        faultRadio.setActionCommand(FAULT_ACTION);
        faultRadio.addActionListener(this);
        JPanel inputPanel3 = new JPanel(new GridLayout(1, 2));
        inputPanel3.add(normRadio);
        inputPanel3.add(faultRadio);
        bg = new ButtonGroup();
        bg.add(normRadio);
        bg.add(faultRadio);
        bg.setSelected(normRadio.getModel(), true);
        southPanel.add(inputPanel3);
        JPanel inputPanel4 = new JPanel(new BorderLayout());
        probInput.setInputVerifier(new FormattedTextFieldVerifier());
        probInput.setValue(100.0);
        probInput.setEnabled(false);
        inputPanel4.add(new JLabel("Probability (%): "), BorderLayout.WEST);
        inputPanel4.add(probInput, BorderLayout.CENTER);
        southPanel.add(inputPanel4);
        addButton = new JButton("Add");
        addButton.setActionCommand(ADD_ACTION);
        addButton.addActionListener(this);
        addButton.setEnabled(false);
        removeButton = new JButton("Remove");
        removeButton.setActionCommand(REMOVE_ACTION);
        removeButton.addActionListener(this);
        removeButton.setEnabled(false);
        editButton = new JButton("Edit");
        editButton.setActionCommand(EDIT_ACTION);
        editButton.addActionListener(this);
        editButton.setEnabled(false);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        southPanel.add(buttonPanel);
        add(southPanel, BorderLayout.SOUTH);
    }

    private FaultMode newFaultMode() {
        String abbreviation = abbreviationInput.getText().trim();
        String description = descriptionInput.getText().trim();
        double prob;
        try {
            prob = ((Double) probInput.getValue()) / 100;
        } catch (NumberFormatException e) {
            prob = 1.0;
            JOptionPane.showMessageDialog(this, "Probability input is invalid, assuming 100%!");
        }
        if (abbreviation.equals("") || description.equals("")) {
            JOptionPane.showMessageDialog(this, "Cannot have an empty abbreviation or description");
            return null;
        }
        return new FaultMode(theComponent.toString(), null, abbreviation, description, normRadio.isSelected(), prob);
    }

    private void addAction() {
        String abbreviation = abbreviationInput.getText().trim();
        FaultMode newFaultMode = newFaultMode();
        theComponent.addFaultMode(newFaultMode);
        if (!abbreviation.equals(newFaultMode.getAbbreviation())) {
            JOptionPane.showMessageDialog(this, "The abbreviation " + abbreviation + " is taken, so\n" + "the abbreviation " + newFaultMode.getAbbreviation() + " was used.");
        } else {
            abbreviationInput.setText("");
            descriptionInput.setText("");
            bg.setSelected(normRadio.getModel(), true);
            probInput.setEnabled(false);
        }
        updateAddButton();
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals(ADD_ACTION)) {
            addAction();
            InfoSec.getInstance().setDirty(true);
            return;
        }
        if (actionCommand.equals(NORMAL_ACTION)) {
            probInput.setValue(100.0);
            probInput.setEnabled(false);
            InfoSec.getInstance().setDirty(true);
            return;
        }
        if (actionCommand.equals(FAULT_ACTION)) {
            probInput.setValue(0.0);
            probInput.setEnabled(true);
            InfoSec.getInstance().setDirty(true);
            return;
        }
        if (actionCommand.equals(REMOVE_ACTION) || actionCommand.equals(EDIT_ACTION)) {
            int selectedIndex = faultModeList.getSelectedIndex();
            if (selectedIndex >= 0) {
                FaultMode fm = (FaultMode) faultModeList.getSelectedValue();
                if (actionCommand.equals(REMOVE_ACTION)) {
                    theComponent.removeFaultMode(fm);
                } else {
                    theComponent.replaceFaultMode(fm, newFaultMode());
                }
                InfoSec.getInstance().setDirty(true);
            }
            return;
        }
        if (e.getSource() == descriptionInput) {
            addAction();
            InfoSec.getInstance().setDirty(true);
            return;
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (faultModeList.getSelectedIndex() < 0) {
            removeButton.setEnabled(false);
            editButton.setEnabled(false);
            abbreviationInput.setText("");
            descriptionInput.setText("");
            bg.setSelected(normRadio.getModel(), true);
            probInput.setValue(100.0);
        } else {
            removeButton.setEnabled(true);
            editButton.setEnabled(true);
            FaultMode fm = (FaultMode) faultModeList.getSelectedValue();
            abbreviationInput.setText(fm.getAbbreviation());
            descriptionInput.setText(fm.getDescription());
            if (fm.isNormal()) {
                bg.setSelected(normRadio.getModel(), true);
                probInput.setEnabled(false);
            } else {
                bg.setSelected(faultRadio.getModel(), true);
                probInput.setEnabled(true);
            }
            probInput.setValue(fm.getProbability() * 100.0);
        }
    }

    private void updateAddButton() {
        if (abbreviationInput.getText().trim().length() > 0 && descriptionInput.getText().trim().length() > 0) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public void keyPressed(KeyEvent e) {
        updateAddButton();
    }

    public void keyReleased(KeyEvent e) {
        updateAddButton();
    }

    public void keyTyped(KeyEvent e) {
        updateAddButton();
    }
}

class FormattedTextFieldVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        if (input instanceof JFormattedTextField) {
            JFormattedTextField ftf = (JFormattedTextField) input;
            AbstractFormatter formatter = ftf.getFormatter();
            if (formatter != null) {
                String text = ftf.getText();
                try {
                    formatter.stringToValue(text);
                    return (Double.parseDouble(text) > 0 && Double.parseDouble(text) < 100);
                } catch (ParseException pe) {
                    return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        return verify(input);
    }
}
