package org.mbari.vars.knowledgebase.ui;

import org.mbari.vars.knowledgebase.model.ConceptName;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * <p>
 * Panel for editing <code>Concept</code> name related information and
 * <code>Concept</code> taxonomy and lithology information.
 * </p>
 *
 * TODO 20050307 brian: Need to implement changing the primary name
 *
 * @author $Author: hohonuuli $
 * @version $Revision: 3 $
 */
public class NamesEditPanel extends AbstractEditPanel {

    private static final int TEXT_FIELD_WIDTH = 20;

    private static final String UNKNOWN_AUTHOR = "unknown";

    private static final ConceptName EMPTY_CONCEPT_NAME = new ConceptName("", ConceptName.NAMETYPE_COMMON, "");

    private JButton addButton;

    private JButton clearButton;

    private JButton deleteButton;

    private KnowledgeBaseMngr knowledgeBaseMngr;

    private JTextField primaryAuthorTextField;

    private PrimaryConceptNamePanel primaryConceptNamePanel;

    private JButton replaceButton;

    private JTextField secondaryNameAuthorTextField;

    private JTextField secondaryNameTextField;

    private JRadioButton secondaryNameTypeCommonRadioButton;

    private JRadioButton secondaryNameTypeSynonymRadioButton;

    private JList secondaryNamesList;

    /**
     * Constructs a <code>NamesEditPanel</code> tied to the specified
     * <code>MaintGui</code>.
     *
     * @param maintGui
     *            The containing <code>MaintGui</code> for this
     *            <code>NamesEditPanel</code>.
     */
    public NamesEditPanel(MaintGui maintGui) {
        super(maintGui);
        this.knowledgeBaseMngr = maintGui.getKnowledgeBaseMngr();
        createGuiComponents();
        layoutGuiComponents();
        createListeners();
    }

    /**
     * Adds a <code>ConceptName</code> object to the current
     * <code>GuiConcept</code> using the current values of the GUI.
     */
    private void addConceptName() {
        String name = secondaryNameTextField.getText().trim();
        if (knowledgeBaseMngr.containsConcept(name)) {
            JOptionPane.showMessageDialog(null, "ConceptName '" + name + "' already exists.", "Add ConceptName Error", JOptionPane.WARNING_MESSAGE);
        } else {
            ConceptName currentInput = createConceptNameFromInputFields();
            if (knowledgeBaseMngr.addConceptName(currentInput) == true) {
                maintGui.rebindConcept();
                secondaryNamesList.setSelectedValue(currentInput, true);
            } else {
                JOptionPane.showMessageDialog(null, "New Concept name rejected.", "Reject Modification", JOptionPane.WARNING_MESSAGE);
                clearInput();
            }
        }
    }

    /**
     * Clears GUI input fields and disables action buttons.
     */
    void clearInput() {
        secondaryNameTextField.setText("");
        secondaryNameAuthorTextField.setText(UNKNOWN_AUTHOR);
        disableButtons();
    }

    /**
     * Creates a Container holding the action buttons for modifying
     * <code>ConceptName</code> objects of the current <code>GuiConcept</code>.
     *
     * @return
     */
    private Container createButtonRow() {
        Box container = Box.createVerticalBox();
        Box buttonContainer = Box.createHorizontalBox();
        int buttonGap = 10;
        buttonContainer.add(addButton);
        buttonContainer.add(Box.createHorizontalStrut(buttonGap));
        buttonContainer.add(replaceButton);
        buttonContainer.add(Box.createHorizontalStrut(buttonGap));
        buttonContainer.add(deleteButton);
        buttonContainer.add(Box.createHorizontalStrut(buttonGap));
        buttonContainer.add(clearButton);
        container.add(Box.createVerticalStrut(5));
        container.add(buttonContainer);
        disableButtons();
        return container;
    }

    /**
     * Uses the current GUI input to create a <code>ConceptName</code>.
     *
     * @returns A <code>ConceptName</code> constructed using the current GUI
     *          input.
     *
     * @return
     */
    private ConceptName createConceptNameFromInputFields() {
        ConceptName conceptName = null;
        String type = null;
        if (secondaryNameTypeCommonRadioButton.isSelected()) {
            type = ConceptName.NAMETYPE_COMMON;
        } else if (secondaryNameTypeSynonymRadioButton.isSelected()) {
            type = ConceptName.NAMETYPE_SYNONYM;
        }
        String name = secondaryNameTextField.getText().trim();
        String author = secondaryNameAuthorTextField.getText().trim();
        if (author.equals("")) {
            conceptName = new ConceptName(name, type);
        } else {
            conceptName = new ConceptName(name, type, author);
        }
        conceptName.setConcept(guiConcept.getConcept());
        return conceptName;
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void createGuiComponents() {
        primaryAuthorTextField = new JTextField(TEXT_FIELD_WIDTH);
        primaryAuthorTextField.setToolTipText("Author of primary Concept name");
        secondaryNamesList = new JList();
        secondaryNamesList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        secondaryNamesList.setCellRenderer(new ConceptNameListCellRenderer());
        secondaryNameTextField = new JTextField(TEXT_FIELD_WIDTH);
        secondaryNameAuthorTextField = new JTextField(TEXT_FIELD_WIDTH);
        secondaryNameAuthorTextField.setToolTipText("Author of seconday Concept name");
        secondaryNameTypeCommonRadioButton = new JRadioButton("Common");
        secondaryNameTypeSynonymRadioButton = new JRadioButton("Synonym");
        secondaryNameTypeCommonRadioButton.setSelected(true);
        addButton = new JButton("Add");
        replaceButton = new JButton("Replace");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void createListeners() {
        primaryAuthorTextField.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent event) {
                guiConcept.setPrimaryAuthor(primaryAuthorTextField.getText().trim());
            }
        });
        secondaryNamesList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    ConceptName selected = (ConceptName) secondaryNamesList.getSelectedValue();
                    if (selected != null) {
                        secondaryNameTextField.setText(selected.getName());
                        secondaryNameAuthorTextField.setText(selected.getAuthor());
                        String nameType = selected.getNameType();
                        if (nameType.equals(ConceptName.NAMETYPE_COMMON)) {
                            secondaryNameTypeCommonRadioButton.setSelected(true);
                        } else if (nameType.equals(ConceptName.NAMETYPE_COMMON)) {
                            secondaryNameTypeSynonymRadioButton.setSelected(true);
                        }
                        enableButtons();
                    }
                }
            }
        });
        secondaryNameTextField.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent event) {
                enableButtons();
            }
        });
        secondaryNameAuthorTextField.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent event) {
                enableButtons();
            }
        });
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                addConceptName();
            }
        });
        replaceButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                replaceConceptName();
            }
        });
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                removeConceptName();
                enableButtons();
            }
        });
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                clearInput();
                secondaryNamesList.clearSelection();
            }
        });
    }

    /**
     * Creates the GUI for editing the author of the primary
     * <code>ConceptName</code> of the current <code>GuiConcept</code>.
     *
     * @return
     */
    private JPanel createPrimaryAuthorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JLabel primaryAuthorLabel = new JLabel("Author");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.top = 2;
        gbc.insets.left = 5;
        gbc.insets.bottom = 2;
        panel.add(primaryAuthorLabel, gbc);
        gbc.insets.right = 5;
        panel.add(primaryAuthorTextField, gbc);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets.right = 0;
        panel.add(Box.createHorizontalGlue(), gbc);
        return panel;
    }

    /**
     * Creates the GUI for editing a secondary <code>ConceptName</code> object
     * for the current <code>GuiConcept</code>.
     *
     * @return
     */
    private JPanel createSecondaryNameEntryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel secondaryNameLabel = new JLabel("Name");
        JLabel secondaryNameAuthorLabel = new JLabel("Author");
        JLabel secondaryNameTypeLabel = new JLabel("Type");
        ButtonGroup secondaryNameTypeButtonGroup = new ButtonGroup();
        secondaryNameTypeButtonGroup.add(secondaryNameTypeCommonRadioButton);
        secondaryNameTypeButtonGroup.add(secondaryNameTypeSynonymRadioButton);
        JPanel buttonGroupPanel = new JPanel();
        buttonGroupPanel.add(secondaryNameTypeCommonRadioButton);
        buttonGroupPanel.add(secondaryNameTypeSynonymRadioButton);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets.right = 5;
        panel.add(secondaryNameLabel, gbc);
        panel.add(secondaryNameAuthorLabel, gbc);
        gbc.insets.right = 0;
        panel.add(secondaryNameTypeLabel, gbc);
        gbc.gridx++;
        panel.add(secondaryNameTextField, gbc);
        panel.add(secondaryNameAuthorTextField, gbc);
        panel.add(buttonGroupPanel, gbc);
        return panel;
    }

    /**
     * Creates the GUI for editing secondary <code>ConceptName</code> objects
     * for the current <code>GuiConcept</code>.
     *
     * @return
     */
    private JPanel createSecondaryNamesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Secondary Names"));
        JScrollPane secondaryNamesScrollPane = new JScrollPane(secondaryNamesList);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets.left = 2;
        panel.add(createSecondaryNameEntryPanel(), gbc);
        panel.add(createButtonRow(), gbc);
        panel.add(new JLabel("Entries"), gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.top = 3;
        panel.add(secondaryNamesScrollPane, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(Box.createHorizontalGlue(), gbc);
        return panel;
    }

    /**
     * Diables all action buttons.
     */
    void disableButtons() {
        addButton.setEnabled(false);
        replaceButton.setEnabled(false);
        deleteButton.setEnabled(false);
        clearButton.setEnabled(false);
    }

    /**
     * Enables action buttons. Buttons are conditionally enabled.
     */
    void enableButtons() {
        String name = secondaryNameTextField.getText().trim();
        boolean emptyName = name.equals("");
        String author = secondaryNameAuthorTextField.getText().trim();
        boolean emptyAuthor = author.equals("");
        addButton.setEnabled(!emptyName && !emptyAuthor);
        if (!emptyName && !emptyAuthor && isItemSelected()) {
            ConceptName selected = (ConceptName) secondaryNamesList.getSelectedValue();
            String selectedName = selected.getName();
            boolean sameName = selectedName.equals(name);
            replaceButton.setEnabled(sameName);
        }
        deleteButton.setEnabled(isItemSelected());
        clearButton.setEnabled(!(emptyName && emptyAuthor) || isItemSelected());
    }

    /**
     * Determines whether a <code>ConceptName</code> is currently selected in
     * the list.
     *
     * @return
     */
    boolean isItemSelected() {
        return !secondaryNamesList.isSelectionEmpty();
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void layoutGuiComponents() {
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(createPrimaryAuthorPanel(), gbc);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(createSecondaryNamesPanel(), gbc);
    }

    /**
     * Removes a <code>ConceptName</code> object to the current
     * <code>GuiConcept</code> using the current values of the GUI.
     */
    private void removeConceptName() {
        String name = secondaryNameTextField.getText().trim();
        knowledgeBaseMngr.removeConceptName(name);
        secondaryNamesListChanged();
    }

    /**
     * Replaces a <code>ConceptName</code> object to the current
     * <code>GuiConcept</code> using the current values of the GUI.
     */
    private void replaceConceptName() {
        ConceptName currentInput = createConceptNameFromInputFields();
        ConceptName selected = (ConceptName) secondaryNamesList.getSelectedValue();
        if (currentInput.equals(selected)) {
            JOptionPane.showMessageDialog(null, "No difference in input Secondary Name.", "Replace ConceptName Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        knowledgeBaseMngr.replaceConceptName(selected, currentInput);
        secondaryNamesListChanged();
        secondaryNamesList.setSelectedValue(currentInput, false);
        maintGui.rebindConcept();
    }

    /**
     * Updates the GUI whenever a change has occurred in the list of secondary
     * <code>ConceptName</code> objects of the current <code>GuiConcept</code>.
     */
    private void secondaryNamesListChanged() {
        ConceptName[] secondaryNames = guiConcept.getSecondaryConceptNames();
        Arrays.sort(secondaryNames);
        secondaryNamesList.setListData(secondaryNames);
    }

    /**
     * <p><!-- Method description --></p>
     *
     */
    void useGuiConcept() {
        primaryAuthorTextField.setText(guiConcept.getPrimaryAuthor());
        clearInput();
        secondaryNamesListChanged();
    }
}
