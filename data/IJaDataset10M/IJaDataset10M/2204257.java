package org.jalgo.module.ebnf.gui.ebnf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.gui.EbnfFont;
import org.jalgo.module.ebnf.gui.FontNotInitializedException;
import org.jalgo.module.ebnf.gui.GUIConstants;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import org.jalgo.module.ebnf.model.ebnf.Rule;
import org.jdesktop.layout.GroupLayout;

/**
 * 
 * @author Tom
 *
 */
public class InputPanel extends JPanel implements Observer {

    private static final long serialVersionUID = -2227389692887606280L;

    private Font ebnfFont;

    private GuiController guiController;

    private javax.swing.JPanel inputPanel;

    private javax.swing.JLabel StartvariableLabel;

    private javax.swing.JButton addBarButton;

    private javax.swing.JButton addBraceLeftButton;

    private javax.swing.JButton addBraceRightButton;

    private javax.swing.JButton addBracketLeftButton;

    private javax.swing.JButton addBracketRightButton;

    private javax.swing.JButton addParenthLeftButton;

    private javax.swing.JButton addParenthRightButton;

    private javax.swing.JLabel addRuleAssignSymbolLabel;

    javax.swing.JButton addRuleButton;

    javax.swing.JComboBox addRuleLeftSideComboBox;

    javax.swing.JTextField addRuleRightSideTextbox;

    javax.swing.JButton addTerminalButton;

    javax.swing.JTextField addTerminalTextField;

    javax.swing.JButton addVariableButton;

    javax.swing.JTextField addVariableTextField;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton checkDefinitionButton;

    private javax.swing.JButton finishInputButton;

    private javax.swing.JPanel rulePanel;

    javax.swing.JComboBox startVariableComboBox;

    private javax.swing.JPanel terminalSymbolPanel;

    private javax.swing.JPanel variableSymbolPanel;

    private JButton changeVariableButton;

    private JButton cancelVariableButton;

    private JButton changeTerminalButton;

    private JButton cancelTerminalButton;

    private JButton changeRuleButton;

    private JButton cancelRuleButton;

    private JLabel infoIcon;

    private JLabel infoText;

    private ImageIcon warningIcon;

    private ImageIcon hintIcon;

    static MyOwnFocusTraversalPolicy newPolicy;

    public InputPanel(GuiController guicontroller) {
        this.guiController = guicontroller;
        try {
            ebnfFont = EbnfFont.getFont();
        } catch (FontNotInitializedException e) {
            e.printStackTrace();
        }
        initComponents();
        initActionListeners();
    }

    /** This method is called from within the constructor to
     * initialize the form and its components.
     */
    private void initComponents() {
        inputPanel = new javax.swing.JPanel();
        warningIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Msg_error"));
        hintIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Advice"));
        infoText = new JLabel();
        infoIcon = new JLabel();
        newPolicy = new MyOwnFocusTraversalPolicy();
        inputPanel.setFocusTraversalPolicy(newPolicy);
        inputPanel.setFocusTraversalPolicyProvider(true);
        variableSymbolPanel = new javax.swing.JPanel();
        addVariableButton = new javax.swing.JButton();
        addVariableTextField = new javax.swing.JTextField();
        changeVariableButton = new javax.swing.JButton();
        cancelVariableButton = new javax.swing.JButton();
        StartvariableLabel = new javax.swing.JLabel();
        startVariableComboBox = new javax.swing.JComboBox();
        terminalSymbolPanel = new javax.swing.JPanel();
        addTerminalButton = new javax.swing.JButton();
        changeTerminalButton = new javax.swing.JButton();
        cancelTerminalButton = new javax.swing.JButton();
        addTerminalTextField = new javax.swing.JTextField();
        rulePanel = new javax.swing.JPanel();
        addParenthLeftButton = new EbnfButton();
        addParenthRightButton = new EbnfButton();
        addBarButton = new EbnfButton();
        addBraceLeftButton = new EbnfButton();
        addBraceRightButton = new EbnfButton();
        addBracketLeftButton = new EbnfButton();
        addBracketRightButton = new EbnfButton();
        addRuleLeftSideComboBox = new javax.swing.JComboBox();
        addRuleAssignSymbolLabel = new javax.swing.JLabel();
        addRuleRightSideTextbox = new EbnfTextField();
        addRuleButton = new javax.swing.JButton();
        changeRuleButton = new javax.swing.JButton();
        cancelRuleButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        checkDefinitionButton = new javax.swing.JButton();
        finishInputButton = new javax.swing.JButton();
        addRuleRightSideTextbox.setBackground(Color.WHITE);
        createAddVariablePanel();
        createAddTerminalPanel();
        createAddRulePanel();
        createButtonPanel();
        createInputPanel();
        addVariableTextField.requestFocusInWindow();
    }

    /**This method puts all previously layouted sub-panels of the inputPanel
	 * in one GroupLayout
	 * 
	 */
    private void createInputPanel() {
        org.jdesktop.layout.GroupLayout inputPanelLayout = new org.jdesktop.layout.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(inputPanelLayout.createParallelGroup(GroupLayout.LEADING).add(inputPanelLayout.createSequentialGroup().addContainerGap().add(inputPanelLayout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.TRAILING, inputPanelLayout.createSequentialGroup().add(variableSymbolPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.LayoutStyle.RELATED).add(terminalSymbolPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(rulePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(inputPanelLayout.createSequentialGroup().addContainerGap().add(infoIcon).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(infoText)));
        inputPanelLayout.setVerticalGroup(inputPanelLayout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.TRAILING, inputPanelLayout.createSequentialGroup().addContainerGap().add(inputPanelLayout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.LEADING, inputPanelLayout.createSequentialGroup().add(inputPanelLayout.createParallelGroup(GroupLayout.TRAILING).add(variableSymbolPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(terminalSymbolPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(rulePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).add(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(inputPanelLayout.createParallelGroup(GroupLayout.LEADING).add(infoIcon).add(infoText))));
        inputPanelLayout.linkSize(new java.awt.Component[] { rulePanel, terminalSymbolPanel, variableSymbolPanel }, GroupLayout.VERTICAL);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().add(inputPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.LEADING).add(inputPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
    }

    /**This method does the layout for the buttonPanel / frame on the right.
	 *
	 */
    private void createButtonPanel() {
        buttonPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Messages.getString("ebnf", "InputPanel.next"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12)));
        checkDefinitionButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        checkDefinitionButton.setText(Messages.getString("ebnf", "Ebnf.Input_CheckDefinitionButton"));
        finishInputButton.setFont(new java.awt.Font("Tahoma", 1, 14));
        finishInputButton.setText(Messages.getString("ebnf", "Ebnf.Input_InputFinishedButton"));
        org.jdesktop.layout.GroupLayout buttonPanelLayout = new org.jdesktop.layout.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setAutocreateGaps(true);
        buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createParallelGroup(GroupLayout.LEADING).add(checkDefinitionButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(finishInputButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createSequentialGroup().addContainerGap(0, Short.MAX_VALUE).add(checkDefinitionButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(finishInputButton));
    }

    /**This method does the layout for the addRulePanel / frame.
	 *
	 */
    private void createAddRulePanel() {
        Insets buttonInsets = new java.awt.Insets(2, 4, 1, 4);
        rulePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Messages.getString("ebnf", "Ebnf.Input_RulesBorder"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12)));
        addParenthLeftButton.setFont(ebnfFont);
        addParenthLeftButton.setText("】");
        addParenthLeftButton.setMargin(buttonInsets);
        addParenthRightButton.setFont(ebnfFont);
        addParenthRightButton.setText("〒");
        addParenthRightButton.setMargin(buttonInsets);
        addBarButton.setFont(ebnfFont);
        addBarButton.setText("『");
        addBarButton.setMargin(buttonInsets);
        addBraceLeftButton.setFont(ebnfFont);
        addBraceLeftButton.setText("』");
        addBraceLeftButton.setMargin(buttonInsets);
        addBraceRightButton.setFont(ebnfFont);
        addBraceRightButton.setText("【");
        addBraceRightButton.setMargin(buttonInsets);
        addBracketLeftButton.setFont(ebnfFont);
        addBracketLeftButton.setText("「");
        addBracketLeftButton.setMargin(buttonInsets);
        addBracketRightButton.setFont(ebnfFont);
        addBracketRightButton.setText("」");
        addBracketRightButton.setMargin(buttonInsets);
        addRuleLeftSideComboBox.setFont(new java.awt.Font("Tahoma", 0, 16));
        addRuleLeftSideComboBox.setModel(new javax.swing.DefaultComboBoxModel());
        addRuleAssignSymbolLabel.setFont(new java.awt.Font("Tahoma", 1, 16));
        addRuleAssignSymbolLabel.setText("::=");
        addRuleRightSideTextbox.setFont(new java.awt.Font("Tahoma", 0, 18));
        addRuleButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        addRuleButton.setText(Messages.getString("ebnf", "Ebnf.Add"));
        addRuleButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        changeRuleButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        changeRuleButton.setText(Messages.getString("ebnf", "Ebnf.Change"));
        changeRuleButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cancelRuleButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        cancelRuleButton.setText(Messages.getString("ebnf", "Ebnf.Cancel"));
        cancelRuleButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        changeRuleButton.setVisible(false);
        cancelRuleButton.setVisible(false);
        org.jdesktop.layout.GroupLayout rulePanelLayout = new org.jdesktop.layout.GroupLayout(rulePanel);
        rulePanel.setLayout(rulePanelLayout);
        rulePanelLayout.setHorizontalGroup(rulePanelLayout.createParallelGroup(GroupLayout.LEADING).add(rulePanelLayout.createSequentialGroup().addContainerGap().add(addRuleLeftSideComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addRuleAssignSymbolLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(rulePanelLayout.createParallelGroup(GroupLayout.LEADING).add(rulePanelLayout.createSequentialGroup().add(addParenthLeftButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addBarButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addParenthRightButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addBraceLeftButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addBraceRightButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addBracketLeftButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addBracketRightButton).addContainerGap()).add(rulePanelLayout.createSequentialGroup().add(addRuleRightSideTextbox, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addRuleButton).add(changeRuleButton).add(cancelRuleButton).add(0, 0, 0)))));
        rulePanelLayout.setVerticalGroup(rulePanelLayout.createParallelGroup(GroupLayout.LEADING).add(rulePanelLayout.createSequentialGroup().add(rulePanelLayout.createParallelGroup(GroupLayout.BASELINE).add(addBarButton).add(addParenthRightButton).add(addBraceLeftButton).add(addBraceRightButton).add(addBracketLeftButton).add(addBracketRightButton).add(addParenthLeftButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(rulePanelLayout.createParallelGroup(GroupLayout.BASELINE).add(rulePanelLayout.createParallelGroup(GroupLayout.BASELINE).add(addRuleRightSideTextbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(addRuleAssignSymbolLabel).add(addRuleButton).add(changeRuleButton).add(cancelRuleButton)).add(addRuleLeftSideComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));
    }

    /**This method does the layout for the addTerminalPanel / frame.
	 *
	 */
    private void createAddTerminalPanel() {
        terminalSymbolPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Messages.getString("ebnf", "Ebnf.Input_TerminalBorder"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12)));
        terminalSymbolPanel.setMinimumSize(new java.awt.Dimension(22, 36));
        addTerminalButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        addTerminalButton.setText(Messages.getString("ebnf", "Ebnf.Add"));
        addTerminalButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        changeTerminalButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        changeTerminalButton.setText(Messages.getString("ebnf", "Ebnf.Change"));
        changeTerminalButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cancelTerminalButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        cancelTerminalButton.setText(Messages.getString("ebnf", "Ebnf.Cancel"));
        cancelTerminalButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        changeTerminalButton.setVisible(false);
        cancelTerminalButton.setVisible(false);
        addTerminalTextField.setFont(new java.awt.Font("Tahoma", 0, 14));
        org.jdesktop.layout.GroupLayout terminalSymbolPanelLayout = new org.jdesktop.layout.GroupLayout(terminalSymbolPanel);
        terminalSymbolPanel.setLayout(terminalSymbolPanelLayout);
        terminalSymbolPanelLayout.setHorizontalGroup(terminalSymbolPanelLayout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.TRAILING, terminalSymbolPanelLayout.createSequentialGroup().add(addTerminalTextField, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addTerminalButton).add(changeTerminalButton).add(cancelTerminalButton)));
        terminalSymbolPanelLayout.setVerticalGroup(terminalSymbolPanelLayout.createParallelGroup(GroupLayout.LEADING).add(terminalSymbolPanelLayout.createSequentialGroup().add(terminalSymbolPanelLayout.createParallelGroup(GroupLayout.BASELINE).add(addTerminalButton).add(changeTerminalButton).add(cancelTerminalButton).add(addTerminalTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(41, Short.MAX_VALUE)));
    }

    /**This method does the layout for the addVariablePanel / frame.
	 *
	 */
    private void createAddVariablePanel() {
        variableSymbolPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Messages.getString("ebnf", "InputPanel.Variables"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12)));
        addVariableButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        addVariableButton.setText(Messages.getString("ebnf", "Ebnf.Add"));
        addVariableButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        changeVariableButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        changeVariableButton.setText(Messages.getString("ebnf", "Ebnf.Change"));
        changeVariableButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        cancelVariableButton.setFont(new java.awt.Font("Tahoma", 0, 16));
        cancelVariableButton.setText(Messages.getString("ebnf", "Ebnf.Cancel"));
        cancelVariableButton.setMargin(new java.awt.Insets(2, 4, 2, 4));
        changeVariableButton.setVisible(false);
        cancelVariableButton.setVisible(false);
        addVariableTextField.setFont(new java.awt.Font("Tahoma", 0, 14));
        StartvariableLabel.setFont(new java.awt.Font("Tahoma", 0, 12));
        StartvariableLabel.setText(Messages.getString("ebnf", "Ebnf.Input_StartVar") + ":");
        startVariableComboBox.setFont(new java.awt.Font("Tahoma", 0, 12));
        startVariableComboBox.setModel(new javax.swing.DefaultComboBoxModel());
        org.jdesktop.layout.GroupLayout variableSymbolPanelLayout = new org.jdesktop.layout.GroupLayout(variableSymbolPanel);
        variableSymbolPanel.setLayout(variableSymbolPanelLayout);
        variableSymbolPanelLayout.setHorizontalGroup(variableSymbolPanelLayout.createParallelGroup(GroupLayout.LEADING).add(GroupLayout.TRAILING, variableSymbolPanelLayout.createSequentialGroup().add(addVariableTextField, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addVariableButton).add(changeVariableButton).add(cancelVariableButton)).add(GroupLayout.TRAILING, variableSymbolPanelLayout.createSequentialGroup().add(StartvariableLabel, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(startVariableComboBox, GroupLayout.PREFERRED_SIZE, 140, Short.MAX_VALUE)));
        variableSymbolPanelLayout.setVerticalGroup(variableSymbolPanelLayout.createParallelGroup(GroupLayout.LEADING).add(variableSymbolPanelLayout.createSequentialGroup().add(variableSymbolPanelLayout.createParallelGroup(GroupLayout.BASELINE).add(addVariableTextField, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).add(addVariableButton).add(changeVariableButton).add(cancelVariableButton)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(variableSymbolPanelLayout.createParallelGroup(GroupLayout.BASELINE).add(StartvariableLabel).add(startVariableComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(12, Short.MAX_VALUE)));
    }

    public Font getEbnfFont() {
        return ebnfFont;
    }

    public void setEbnfFont(Font ebnfFont) {
        this.ebnfFont = ebnfFont;
        addRuleRightSideTextbox.setFont(this.ebnfFont);
    }

    public void addSpecialChar(String specialChar) {
        if (addRuleRightSideTextbox.isEnabled()) {
            int i, h;
            String newcontent;
            i = addRuleRightSideTextbox.getCaretPosition();
            h = addRuleRightSideTextbox.getText().length();
            newcontent = addRuleRightSideTextbox.getText().substring(0, i) + specialChar + addRuleRightSideTextbox.getText().substring(i, h);
            addRuleRightSideTextbox.setText(newcontent);
            addRuleRightSideTextbox.setCaretPosition(i + 1);
            addRuleRightSideTextbox.requestFocus(true);
        }
    }

    /**Initialize the actionlisteners for the components of the inputPanel
	 *
	 */
    private void initActionListeners() {
        addParenthLeftButton.addActionListener(guiController.editRuleActionListener);
        addParenthLeftButton.setActionCommand("ebnfParentLeft");
        addParenthRightButton.addActionListener(guiController.editRuleActionListener);
        addParenthRightButton.setActionCommand("ebnfParentRight");
        addBarButton.addActionListener(guiController.editRuleActionListener);
        addBarButton.setActionCommand("ebnfBar");
        addBraceLeftButton.addActionListener(guiController.editRuleActionListener);
        addBraceLeftButton.setActionCommand("ebnfBraceLeft");
        addBraceRightButton.addActionListener(guiController.editRuleActionListener);
        addBraceRightButton.setActionCommand("ebnfBraceRight");
        addBracketLeftButton.addActionListener(guiController.editRuleActionListener);
        addBracketLeftButton.setActionCommand("ebnfBracketLeft");
        addBracketRightButton.addActionListener(guiController.editRuleActionListener);
        addBracketRightButton.setActionCommand("ebnfBracketRight");
        addParenthLeftButton.setMnemonic(KeyEvent.VK_U);
        addParenthLeftButton.setToolTipText(Messages.getString("ebnf", "InputPanel.insertlpar"));
        addParenthRightButton.setMnemonic(KeyEvent.VK_O);
        addBraceLeftButton.setMnemonic(KeyEvent.VK_7);
        addBraceRightButton.setMnemonic(KeyEvent.VK_0);
        addBracketLeftButton.setMnemonic(KeyEvent.VK_8);
        addBracketRightButton.setMnemonic(KeyEvent.VK_9);
        addBarButton.setMnemonic(KeyEvent.VK_I);
        finishInputButton.addActionListener(guiController.editRuleActionListener);
        finishInputButton.setActionCommand("inputfinished");
        checkDefinitionButton.addActionListener(guiController.editRuleActionListener);
        checkDefinitionButton.setActionCommand("checkdefinition");
        addVariableTextField.addActionListener(guiController.modifyDefinitionActionListener);
        addVariableButton.addActionListener(guiController.modifyDefinitionActionListener);
        addVariableButton.setActionCommand("addVariable");
        changeVariableButton.addActionListener(guiController.modifyDefinitionActionListener);
        changeVariableButton.setActionCommand("changeVariable");
        cancelVariableButton.addActionListener(guiController.modifyDefinitionActionListener);
        cancelVariableButton.setActionCommand("calcelVariable");
        addVariableTextField.setActionCommand("addVariable");
        addTerminalTextField.addActionListener(guiController.modifyDefinitionActionListener);
        addTerminalButton.addActionListener(guiController.modifyDefinitionActionListener);
        addTerminalButton.setActionCommand("addTerminal");
        changeTerminalButton.addActionListener(guiController.modifyDefinitionActionListener);
        changeTerminalButton.setActionCommand("changeTerminal");
        cancelTerminalButton.addActionListener(guiController.modifyDefinitionActionListener);
        cancelTerminalButton.setActionCommand("cancelTerminal");
        addTerminalTextField.setActionCommand("addTerminal");
        addRuleButton.addActionListener(guiController.modifyDefinitionActionListener);
        addRuleButton.setActionCommand("addRule");
        changeRuleButton.addActionListener(guiController.modifyDefinitionActionListener);
        changeRuleButton.setActionCommand("changeRule");
        cancelRuleButton.addActionListener(guiController.modifyDefinitionActionListener);
        cancelRuleButton.setActionCommand("cancelRule");
        addRuleRightSideTextbox.addActionListener(guiController.modifyDefinitionActionListener);
        addRuleRightSideTextbox.setActionCommand("addRule");
        startVariableComboBox.addActionListener(guiController.modifyDefinitionActionListener);
        startVariableComboBox.setActionCommand("setStartVariable");
        infoIcon.addMouseListener(guiController.modifyDefinitionActionListener);
        infoText.addMouseListener(guiController.modifyDefinitionActionListener);
    }

    public void setEditVariableActionCommand(boolean editMode) {
        if (editMode) addVariableTextField.setActionCommand("changeVariable"); else addVariableTextField.setActionCommand("addVariable");
    }

    public void setEditRuleActionCommand(boolean editMode) {
        if (editMode) addRuleRightSideTextbox.setActionCommand("changeRule"); else addRuleRightSideTextbox.setActionCommand("addRule");
    }

    public void setEditTerminalActionCommand(boolean editMode) {
        if (editMode) addTerminalTextField.setActionCommand("changeTerminal"); else addTerminalTextField.setActionCommand("addTerminal");
    }

    public void update(Observable o, Object arg) {
        startVariableComboBox.removeActionListener(guiController.modifyDefinitionActionListener);
        startVariableComboBox.removeAllItems();
        for (EVariable var : guiController.getEbnfController().getDefinition().getVariables()) startVariableComboBox.addItem(var);
        if (guiController.getEbnfController().getDefinition().getStartVariable() == null) startVariableComboBox.addItem(null);
        startVariableComboBox.setSelectedItem(guiController.getEbnfController().getDefinition().getStartVariable());
        startVariableComboBox.addActionListener(guiController.modifyDefinitionActionListener);
        Object currentItem = addRuleLeftSideComboBox.getSelectedItem();
        addRuleLeftSideComboBox.removeAllItems();
        for (EVariable var : guiController.getEbnfController().getDefinition().getVariables()) if (guiController.getEbnfController().getDefinition().getRule(var) == null) addRuleLeftSideComboBox.addItem(var);
        if (currentItem != null) addRuleLeftSideComboBox.setSelectedItem(currentItem);
        addRuleRightSideTextbox.setEnabled(addRuleLeftSideComboBox.getItemCount() != 0);
        addRuleButton.setEnabled(addRuleLeftSideComboBox.getItemCount() != 0);
        addRuleLeftSideComboBox.setEnabled(addRuleLeftSideComboBox.getItemCount() != 0);
    }

    public void editRule(Rule rule) {
        addRuleLeftSideComboBox.removeAllItems();
        addRuleLeftSideComboBox.addItem(rule.getName());
        addRuleLeftSideComboBox.setSelectedItem(rule.getName());
        addRuleRightSideTextbox.setText(EbnfRenderer.toRenderString(rule.getTerm(), false, ""));
        addRuleLeftSideComboBox.setEnabled(false);
        addRuleRightSideTextbox.setEnabled(true);
        addRuleButton.setVisible(false);
        changeRuleButton.setVisible(true);
        cancelRuleButton.setVisible(true);
    }

    public void cancelRule() {
        addRuleRightSideTextbox.setText("");
        addRuleLeftSideComboBox.removeAllItems();
        changeRuleButton.setVisible(false);
        cancelRuleButton.setVisible(false);
        addRuleButton.setVisible(true);
    }

    public void editTerminal(ETerminalSymbol terminal) {
        addTerminalTextField.setText(terminal.getName());
        changeTerminalButton.setVisible(true);
        cancelTerminalButton.setVisible(true);
        addTerminalButton.setVisible(false);
    }

    public void cancelTerminal() {
        addTerminalTextField.setText("");
        changeTerminalButton.setVisible(false);
        cancelTerminalButton.setVisible(false);
        addTerminalButton.setVisible(true);
    }

    public void editVariable(EVariable variable) {
        addVariableTextField.setText(variable.getName());
        changeVariableButton.setVisible(true);
        cancelVariableButton.setVisible(true);
        addVariableButton.setVisible(false);
    }

    public void cancelVariable() {
        addVariableTextField.setText("");
        changeVariableButton.setVisible(false);
        cancelVariableButton.setVisible(false);
        addVariableButton.setVisible(true);
    }

    /**
	 * Shows the information panel
	 * @param infoText the text to be shown
	 * @param isWarning decides if a warning or a hint icon is shown
	 */
    public void showInfo(String infoText, boolean isWarning) {
        this.infoText.setText(infoText);
        if (isWarning) {
            this.infoIcon.setIcon(warningIcon);
            this.infoText.setForeground(GUIConstants.ERROR_COLOR);
        } else {
            this.infoIcon.setIcon(hintIcon);
            this.infoText.setForeground(GUIConstants.TEXT_COLOR);
        }
        this.infoText.setVisible(true);
        this.infoIcon.setVisible(true);
        this.validate();
        javax.swing.Timer t = new javax.swing.Timer(4000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                hideInfo();
            }
        });
        t.setRepeats(false);
        t.start();
    }

    /**
	 * Hides the the information panel 
	 */
    public void hideInfo() {
        infoText.setVisible(false);
        infoIcon.setVisible(false);
        this.validate();
    }

    public class MyOwnFocusTraversalPolicy extends FocusTraversalPolicy {

        @Override
        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            if (aComponent.equals(addVariableTextField)) {
                return addTerminalTextField;
            } else if (aComponent.equals(addTerminalTextField)) {
                return addRuleRightSideTextbox;
            } else if (aComponent.equals(addRuleRightSideTextbox)) {
                return addVariableTextField;
            }
            return addVariableTextField;
        }

        @Override
        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            if (aComponent.equals(addVariableTextField)) {
                return addRuleRightSideTextbox;
            } else if (aComponent.equals(addRuleRightSideTextbox)) {
                return addTerminalTextField;
            } else if (aComponent.equals(addTerminalTextField)) {
                return addVariableTextField;
            }
            return addVariableTextField;
        }

        @Override
        public Component getDefaultComponent(Container focusCycleRoot) {
            return addVariableTextField;
        }

        @Override
        public Component getLastComponent(Container focusCycleRoot) {
            return addRuleRightSideTextbox;
        }

        @Override
        public Component getFirstComponent(Container focusCycleRoot) {
            return addVariableTextField;
        }
    }
}
