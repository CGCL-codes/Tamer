package debugger.gui.debugging;

import debugger.resources.ResourcesFactory;
import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Copyright (c) Ontos AG (http://www.ontosearch.com).
 * This class is part of JAPE Debugger component for
 * GATE (Copyright (c) "The University of Sheffield" see http://gate.ac.uk/) <br>
 * @author Andrey Shafirin, Oleg Mishenko
 */
public class JapeSourcePanel extends JComponent {

    private JEditorPane editorPane;

    private JButton saveButton;

    private JButton reinitButton;

    private String saveButtonString = "Save";

    private String reinitButtonString = "Reinitialize";

    public JapeSourcePanel() {
        initGui();
        editorPane.setEditorKit(new StyledEditorKit());
        editorPane.setDocument(new SyntaxDocument());
        upgradeTextPane();
        initListeners();
    }

    private void initGui() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        editorPane = new JTextPane();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4, 3, 3, 4);
        editorPane.setBorder(BorderFactory.createEtchedBorder());
        this.add(new JScrollPane(editorPane), c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(new JPanel(), c);
        saveButton = new JButton(saveButtonString);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        this.add(saveButton, c);
        reinitButton = new JButton(reinitButtonString);
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        saveButton.setPreferredSize(getButtonSize());
        saveButton.setMinimumSize(getButtonSize());
        saveButton.setMaximumSize(getButtonSize());
        reinitButton.setPreferredSize(getButtonSize());
        reinitButton.setMinimumSize(getButtonSize());
        reinitButton.setMaximumSize(getButtonSize());
        this.add(reinitButton, c);
    }

    public void upgradeTextPane() {
        if (ResourcesFactory.getCurrentJapeFile() == null) return;
        if (!editorPane.getText().equals(ResourcesFactory.getCurrentJapeText())) {
            editorPane.setText(ResourcesFactory.getCurrentJapeText());
        }
        try {
            int caretPosition = editorPane.getText().indexOf(ResourcesFactory.getCurrentRuleModel().getName());
            editorPane.setCaretPosition(caretPosition);
            editorPane.grabFocus();
            Rectangle caretRectangle = editorPane.modelToView(caretPosition);
            Rectangle visibleRect = editorPane.getVisibleRect();
            editorPane.scrollRectToVisible(new Rectangle(0, caretRectangle.y + visibleRect.height, 1, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ResourcesFactory.getCurrentJapeFile().canWrite()) {
            saveButton.setEnabled(true);
            editorPane.setEditable(true);
        } else {
            saveButton.setEnabled(false);
            editorPane.setEditable(false);
        }
    }

    private Dimension getButtonSize() {
        int saveButtonTextLength = saveButton.getFontMetrics(saveButton.getFont()).stringWidth(saveButtonString);
        int reinitButtonTextLength = reinitButton.getFontMetrics(reinitButton.getFont()).stringWidth(reinitButtonString) + 25;
        return new Dimension(Math.max(saveButtonTextLength, reinitButtonTextLength) + 21, 25);
    }

    private void initListeners() {
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        reinitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
    }
}
