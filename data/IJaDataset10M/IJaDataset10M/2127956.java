package gui.forms;

import gui.environment.Universe;
import gui.SuperMouseAdapter;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import core.CompositionFrame;
import core.specification.compositionexpression.ExtendedCompositionExpression;
import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;
import automata.fsa.extended.Condition;
import automata.fsa.extended.Assignment;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Siamak Kolahi
 * Date: Jul 7, 2007
 * Time: 2:08:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedCompExpressionDialog extends JDialog {

    private boolean answer = false;

    private String[] operatorsList = Universe.getOperatorList();

    private JPanel myPanel = null;

    JPanel jPanel1 = new JPanel();

    JLabel conditionLabel = new JLabel();

    DefaultListModel inputAssignmentModel = new DefaultListModel();

    JList inputAssignmentList = new JList(inputAssignmentModel);

    JScrollPane inputAssignmentScrollPane;

    DefaultListModel outputAssignmentModel = new DefaultListModel();

    JList outputAssignmentList = new JList(outputAssignmentModel);

    JScrollPane outputAssignmentScrollPane;

    JTextField conditionEdit = new JTextField();

    JButton editCompCondButton = new JButton();

    JComboBox baseUCCombo1 = new JComboBox();

    JComboBox referredUCCombo = new JComboBox();

    JButton inputAssEditButton = new JButton();

    JButton inputAssAddButton = new JButton();

    JButton outputAssEditButton = new JButton();

    JButton outputAssAddButton = new JButton();

    JButton okButton = new JButton();

    JButton cancelButton = new JButton();

    CompositionFrame frame;

    ExtendedCompositionExpression extendedExpression;

    FiniteStateAutomaton baseUCA;

    Set condition;

    Set inputAssignments;

    Set outputAssignments;

    public ExtendedCompExpressionDialog(final CompositionFrame frame, Automaton automaton, ExtendedCompositionExpression extExpr) {
        super(frame, "Extended Compoisition Expression", true);
        this.frame = frame;
        this.baseUCA = (FiniteStateAutomaton) automaton;
        if (extExpr != null) {
            this.extendedExpression = extExpr;
            this.condition = extExpr.getCondition();
            this.inputAssignments = extExpr.getInputAssignments();
            this.outputAssignments = extExpr.getOutputAssignments();
        } else {
            this.condition = new LinkedHashSet();
            this.inputAssignments = new LinkedHashSet();
            this.outputAssignments = new LinkedHashSet();
        }
        setSize(300, 350);
        setLocationRelativeTo(frame);
        myPanel = new JPanel();
        getContentPane().add(myPanel);
        myPanel.setLayout(null);
        this.getContentPane().setLayout(null);
        jPanel1.setBounds(new Rectangle(1, 2, 300, 400));
        jPanel1.setLayout(null);
        conditionLabel.setBounds(new Rectangle(10, 30, 110, 16));
        conditionLabel.setText("Composition Condition: ");
        conditionEdit.setBounds(new Rectangle(120, 30, 100, 20));
        if (condition != null) conditionEdit.setText(condition.toString());
        conditionEdit.setEditable(false);
        editCompCondButton.setBounds(new Rectangle(230, 30, 55, 20));
        editCompCondButton.addActionListener(new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                ConditionForm cf = new ConditionForm(getThis(), getAutomaton(), null);
                if (cf.getAnswer()) {
                    condition.add(cf.getCondition());
                    conditionEdit.setText(Condition.setToString(condition));
                }
            }
        });
        editCompCondButton.setText("Edit");
        inputAssignmentList.addMouseListener(new SuperMouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Assignment assignment = (Assignment) inputAssignmentList.getSelectedValue();
                    AssignmentForm af = new AssignmentForm(getThis(), getAutomaton(), assignment);
                    updateInputAssList();
                }
            }
        });
        updateInputAssList();
        updateOutputAssList();
        inputAssignmentScrollPane = new JScrollPane(inputAssignmentList);
        inputAssignmentScrollPane.setBounds(10, 70, 130, 150);
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.lightGray);
        Border assBorder = new TitledBorder(border, "Input Assignmets");
        inputAssignmentScrollPane.setBorder(assBorder);
        outputAssignmentList.addMouseListener(new SuperMouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Assignment assignment = (Assignment) outputAssignmentList.getSelectedValue();
                    AssignmentForm cf = new AssignmentForm(getThis(), getAutomaton(), assignment);
                    updateOutputAssList();
                }
            }
        });
        outputAssignmentScrollPane = new JScrollPane(outputAssignmentList);
        outputAssignmentScrollPane.setBounds(150, 70, 130, 150);
        Border selectedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.lightGray);
        Border selectedStatesBorder = new TitledBorder(selectedBorder, "Assignments");
        outputAssignmentScrollPane.setBorder(selectedStatesBorder);
        inputAssEditButton.setBounds(new Rectangle(15, 230, 55, 20));
        inputAssEditButton.addActionListener(new AbstractAction("Edit") {

            public void actionPerformed(ActionEvent evt) {
                Assignment assignment = (Assignment) inputAssignmentList.getSelectedValue();
                AssignmentForm af = new AssignmentForm(getThis(), getAutomaton(), assignment);
                updateInputAssList();
            }
        });
        inputAssEditButton.setText("Edit");
        inputAssAddButton.setBounds(new Rectangle(75, 230, 57, 20));
        inputAssAddButton.addActionListener(new AbstractAction("Add") {

            public void actionPerformed(ActionEvent evt) {
                AssignmentForm af = new AssignmentForm(getThis(), getAutomaton(), null);
                if (af.getAnswer()) {
                    Assignment assignment = af.getAssignment();
                    inputAssignments.add(assignment);
                    updateInputAssList();
                }
            }
        });
        inputAssAddButton.setText("Add");
        outputAssEditButton.setBounds(new Rectangle(155, 230, 55, 20));
        outputAssEditButton.addActionListener(new AbstractAction("Edit") {

            public void actionPerformed(ActionEvent evt) {
                Assignment assignment = (Assignment) outputAssignmentList.getSelectedValue();
                AssignmentForm cf = new AssignmentForm(getThis(), getAutomaton(), assignment);
                updateOutputAssList();
            }
        });
        outputAssEditButton.setText("Edit");
        outputAssAddButton.setBounds(new Rectangle(215, 230, 57, 20));
        outputAssAddButton.addActionListener(new AbstractAction("Add") {

            public void actionPerformed(ActionEvent evt) {
                AssignmentForm cf = new AssignmentForm(getThis(), getAutomaton(), null);
                if (cf.getAnswer()) {
                    Assignment assignment = cf.getAssignment();
                    outputAssignments.add(assignment);
                    updateOutputAssList();
                }
            }
        });
        outputAssAddButton.setText("Add");
        okButton.setBounds(new Rectangle(65, 270, 67, 25));
        okButton.addActionListener(new AbstractAction("OK") {

            public void actionPerformed(ActionEvent evt) {
                answer = true;
                setVisible(false);
            }
        });
        okButton.setText("OK");
        cancelButton.setBounds(new Rectangle(153, 270, 73, 25));
        cancelButton.addActionListener(new AbstractAction("Cancel") {

            public void actionPerformed(ActionEvent evt) {
                answer = false;
                setVisible(false);
            }
        });
        cancelButton.setText("Cancel");
        this.getContentPane().add(jPanel1, null);
        jPanel1.add(conditionLabel);
        jPanel1.add(conditionEdit);
        jPanel1.add(editCompCondButton);
        jPanel1.add(referredUCCombo);
        jPanel1.add(inputAssignmentScrollPane);
        jPanel1.add(outputAssignmentScrollPane);
        jPanel1.add(inputAssEditButton);
        jPanel1.add(outputAssEditButton);
        jPanel1.add(inputAssAddButton);
        jPanel1.add(outputAssAddButton);
        jPanel1.add(okButton);
        jPanel1.add(cancelButton);
        setVisible(true);
    }

    private void updateInputAssList() {
        inputAssignmentModel.removeAllElements();
        Iterator it = inputAssignments.iterator();
        for (Object inputAssignment : inputAssignments) {
            Object ob = (Object) inputAssignment;
            int pos = inputAssignmentModel.getSize();
            inputAssignmentModel.add(pos, ob);
        }
        System.out.println("Updated!");
    }

    private void updateOutputAssList() {
        outputAssignmentModel.removeAllElements();
        Iterator it = outputAssignments.iterator();
        for (Object ob : outputAssignments) {
            int pos = outputAssignmentModel.getSize();
            outputAssignmentModel.add(pos, ob);
        }
        System.out.println("Updated!");
    }

    /**
     * Getters and setters
     */
    public boolean getAnswer() {
        return answer;
    }

    public CompositionFrame getFrame() {
        return frame;
    }

    public FiniteStateAutomaton getAutomaton() {
        return baseUCA;
    }

    public ExtendedCompositionExpression getExtendedExpression() {
        return extendedExpression;
    }

    public ExtendedCompExpressionDialog getThis() {
        return this;
    }

    public Set getOutputAssignments() {
        return outputAssignments;
    }

    public Set getInputAssignments() {
        return inputAssignments;
    }

    public Set getCondition() {
        return condition;
    }
}
