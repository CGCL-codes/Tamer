package gui.forms;

import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.extended.ExtendedFSA;
import core.CompositionFrame;
import core.specification.compositionexpression.CompositionExpression;
import core.specification.compositionexpression.ExtendedCompositionExpression;
import core.specification.compositionexpression.extensionpoint.ExtensionPoint;
import core.specification.operator.*;
import gui.SuperAbstractAction;
import gui.environment.Universe;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Siamak Kolahi
 * Date: Jul 7, 2007
 * Time: 3:56:06 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CompositionExpressionDialog extends AbstractDialog {

    protected CompositionFrame frame;

    private String[] operatorsList = Universe.getOperatorList();

    JPanel jPanel1 = new JPanel();

    JPanel extensionPointPanel = new JPanel();

    JTextField newUCIDEdit = new JTextField();

    JLabel newIDLabel = new JLabel();

    JLabel operatorLabel = new JLabel();

    JLabel baseLabel = new JLabel();

    JLabel referredLabel = new JLabel();

    JLabel extensionPointLabel = new JLabel();

    JComboBox baseUCCombo = new JComboBox();

    JComboBox referredUCCombo = new JComboBox();

    JComboBox operatorCombo = new JComboBox(operatorsList);

    JCheckBox minimalCheckBox = new JCheckBox();

    JTextField previewEdit = new JTextField();

    JButton okButton = new JButton();

    JButton cancelButton = new JButton();

    JButton extendedButton = new JButton();

    JButton previewButton = new JButton();

    Set condition;

    Set inputAssignments;

    Set outputAssignments;

    public CompositionExpressionDialog(final CompositionFrame frame) {
        super(frame, "Expression", true);
        this.frame = frame;
        condition = new LinkedHashSet();
        inputAssignments = new LinkedHashSet();
        outputAssignments = new LinkedHashSet();
        setLocationRelativeTo(frame);
        this.getContentPane().setLayout(null);
        jPanel1.setLayout(null);
        extensionPointPanel.setLayout(null);
        newIDLabel.setBounds(new Rectangle(15, 30, 110, 16));
        newIDLabel.setText("New Use Case ID:");
        operatorLabel.setText("Operator: ");
        operatorLabel.setBounds(new Rectangle(15, 60, 110, 14));
        baseLabel.setText("Base UseCase:");
        baseLabel.setBounds(new Rectangle(15, 90, 110, 15));
        referredLabel.setText("Referred UseCase:");
        referredLabel.setBounds(new Rectangle(15, 120, 120, 15));
        extensionPointLabel.setText("Extension Point: ");
        extensionPointLabel.setBounds(new Rectangle(5, 5, 120, 15));
        EtchedBorder etchedBorder = (EtchedBorder) BorderFactory.createEtchedBorder();
        extensionPointPanel.setBorder(etchedBorder);
        extensionPointPanel.setBounds(15, 150, 260, 15);
        extensionPointPanel.add(extensionPointLabel);
        extensionPointStartY = ((int) extensionPointLabel.getLocation().getY()) + extensionPointLabel.getHeight() + 10;
        newUCIDEdit.setBounds(new Rectangle(145, 30, 170, 20));
        operatorCombo.setBounds(new Rectangle(145, 60, 170, 20));
        Object[] usecases = frame.getSpecification().getUseCases();
        baseUCCombo.removeAllItems();
        for (Object uc : usecases) baseUCCombo.addItem(uc);
        baseUCCombo.setBounds(new Rectangle(145, 90, 170, 20));
        baseUCCombo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                FiniteStateAutomaton automaton = (FiniteStateAutomaton) evt.getItem();
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    updateExtensionPoint(automaton);
                } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                }
            }
        });
        referredUCCombo.removeAllItems();
        for (Object uc : usecases) referredUCCombo.addItem(uc);
        referredUCCombo.setBounds(new Rectangle(145, 120, 170, 20));
        minimalCheckBox.setText("Minimal UCA Result");
        okButton.addActionListener(new SuperAbstractAction(this, true));
        okButton.setText("OK");
        cancelButton.addActionListener(new SuperAbstractAction(this, false));
        cancelButton.setText("Cancel");
        extendedButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent evt) {
                Automaton automaton = getBaseUsecase();
                if (automaton instanceof ExtendedFSA) {
                    ExtendedCompExpressionDialog xced = new ExtendedCompExpressionDialog(getFrame(), automaton, null);
                    condition = xced.getCondition();
                    inputAssignments = xced.getInputAssignments();
                    outputAssignments = xced.getOutputAssignments();
                }
            }
        });
        extendedButton.setText("Extend");
        previewButton.setText("Preview");
        this.getContentPane().add(jPanel1, null);
        jPanel1.add(newIDLabel);
        jPanel1.add(operatorLabel);
        jPanel1.add(baseLabel);
        jPanel1.add(referredLabel);
        jPanel1.add(extensionPointPanel);
        jPanel1.add(newUCIDEdit);
        jPanel1.add(operatorCombo);
        jPanel1.add(baseUCCombo);
        jPanel1.add(referredUCCombo);
        jPanel1.add(minimalCheckBox);
        jPanel1.add(previewEdit);
        jPanel1.add(okButton);
        jPanel1.add(cancelButton);
        jPanel1.add(extendedButton);
        jPanel1.add(previewButton);
    }

    /**
     * Sets the size of the dialog relative to the given components
     * for the extension point in the derived dialogs
     */
    public void setDialogSize() {
        int buttonPos = ((int) extensionPointPanel.getLocation().getY()) + extensionPointPanel.getHeight();
        minimalCheckBox.setBounds(15, buttonPos + 10, 200, 20);
        previewEdit.setBounds(15, buttonPos + 40, 200, 20);
        previewButton.setBounds(new Rectangle(230, buttonPos + 40, 80, 20));
        buttonPos = ((int) previewEdit.getLocation().getY()) + previewEdit.getHeight() + 30;
        okButton.setBounds(new Rectangle(40, buttonPos, 75, 25));
        cancelButton.setBounds(new Rectangle(125, buttonPos, 75, 25));
        extendedButton.setBounds(new Rectangle(210, buttonPos, 75, 25));
        int heigth = ((int) okButton.getLocation().getY()) + okButton.getHeight();
        jPanel1.setBounds(new Rectangle(1, 2, dialogWidth, heigth));
        setSize(dialogWidth, heigth + 60);
        setLocationRelativeTo(frame);
    }

    /**
     * Abstract method for updating the extension point in the dialog.
     * To be overriden in the derived dialogs.
     *
     * @param automaton The usecase to update the availbale extension points for
     */
    protected abstract void updateExtensionPoint(FiniteStateAutomaton automaton);

    /**
     * Abstract getter and setter of the extension point in the dialog.
     * To be overriden in the derived dialogs.
     */
    protected abstract ExtensionPoint getFirstExtensionPoint();

    protected abstract ExtensionPoint getSecondExtensionPoint();

    /**
     * Setting the extension point with the given one;
     * For editing an existing core expression.
     * To be overriden in the derived dialogs.
     */
    protected abstract void setExtensionPoint(Operator operator);

    /**
     * Getters and setters
     */
    public boolean getAnswer() {
        return answer;
    }

    public CompositionFrame getFrame() {
        return frame;
    }

    public Operator getOperator(FiniteStateAutomaton baseUCA, FiniteStateAutomaton referredUCA) {
        String opStr = (String) operatorCombo.getSelectedItem();
        if (opStr.equals("Include")) {
            ExtensionPoint extPoint = getFirstExtensionPoint();
            return new Include(baseUCA, referredUCA, extPoint);
        } else if (opStr.equals("Extend")) {
            ExtensionPoint extPoint = getFirstExtensionPoint();
            return new Extend(baseUCA, referredUCA, extPoint);
        } else if (opStr.equals("Alternative")) {
            ExtensionPoint extPoint = getFirstExtensionPoint();
            return new Alternative(baseUCA, referredUCA, extPoint);
        } else if (opStr.equals("Graft")) {
            ExtensionPoint extPointBegin = getFirstExtensionPoint();
            ExtensionPoint extPointEnd = getSecondExtensionPoint();
            return new Graft(baseUCA, referredUCA, extPointBegin, extPointEnd);
        } else if (opStr.equals("Refine")) {
            ExtensionPoint extPoint = getFirstExtensionPoint();
            return new Refine(baseUCA, referredUCA, extPoint);
        } else if (opStr.equals("Interrupt")) {
            return new Interrupt(baseUCA, referredUCA);
        } else return null;
    }

    public String getNewUCID() {
        return newUCIDEdit.getText();
    }

    public FiniteStateAutomaton getBaseUsecase() {
        return (FiniteStateAutomaton) baseUCCombo.getSelectedItem();
    }

    public FiniteStateAutomaton getReferredUsecase() {
        return (FiniteStateAutomaton) referredUCCombo.getSelectedItem();
    }

    /**
     * Sets the dialog with the given core expression
     */
    public void setExpression(CompositionExpression expression) {
        newUCIDEdit.setText(expression.getNewUseCaseID());
        baseUCCombo.setSelectedItem(expression.getBaseUseCase());
        referredUCCombo.setSelectedItem(expression.getReferredUseCase());
        operatorCombo.setSelectedItem(expression.getOperator().toString());
        if (expression instanceof ExtendedCompositionExpression) {
            ExtendedCompositionExpression extExpr = (ExtendedCompositionExpression) expression;
            this.condition = extExpr.getCondition();
            this.inputAssignments = extExpr.getInputAssignments();
            this.outputAssignments = extExpr.getOutputAssignments();
        }
        setExtensionPoint(expression.getOperator());
        minimalCheckBox.setSelected(expression.getMinimal());
    }

    /**
     * Loops until the result of the expression is fine and error free;
     * Then returns the core expression of the dialog;
     */
    public CompositionExpression getCompositionExpression() {
        CompositionExpression expression = null;
        boolean fine = false;
        do {
            setVisible(true);
            if (getAnswer()) {
                FiniteStateAutomaton baseFSA = getBaseUsecase();
                FiniteStateAutomaton referredFSA = getReferredUsecase();
                if ((baseFSA == null) || (referredFSA == null)) {
                    JOptionPane.showMessageDialog(frame, "No such use case exist!");
                    continue;
                }
                FiniteStateAutomaton newFSA = frame.getSpecification().getUseCaseByID(getNewUCID());
                if (newFSA != null) {
                    JOptionPane.showMessageDialog(frame, "Use case " + getNewUCID() + " already exist. \n Choose another ID!");
                    continue;
                }
                if (getNewUCID().equals("")) {
                    JOptionPane.showMessageDialog(frame, "Choose an ID for the new use case!");
                    continue;
                }
                newFSA = new FiniteStateAutomaton(getNewUCID());
                Operator operator = getOperator(baseFSA, referredFSA);
                boolean minimal = minimalCheckBox.isSelected();
                expression = createCompositionExpression(newFSA, operator, minimal);
                if (expression == null) {
                    JOptionPane.showMessageDialog(frame, "Base and referred usecases should be of the same type!");
                    continue;
                }
                operator.setExpression(expression);
                answer = true;
                fine = true;
            }
        } while ((!fine) && getAnswer());
        return expression;
    }

    private CompositionExpression createCompositionExpression(FiniteStateAutomaton newFSA, Operator operator, boolean minimal) {
        if (operator.getBaseUseCase() instanceof ExtendedFSA) {
            if (operator.getReferredUseCase() instanceof ExtendedFSA) return new ExtendedCompositionExpression(newFSA, operator, minimal, condition, inputAssignments, outputAssignments); else return null;
        } else {
            if (operator.getReferredUseCase() instanceof ExtendedFSA) return null; else return new CompositionExpression(newFSA, operator, minimal);
        }
    }

    protected static final int dialogWidth = 340;

    protected static int extensionPointStartY;
}
