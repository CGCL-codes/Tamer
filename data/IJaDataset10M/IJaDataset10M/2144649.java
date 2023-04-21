package org.skunk.dav.client.gui.editor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.skunk.dav.client.gui.ResourceManager;

public abstract class AbstractSearchPanel extends JPanel implements ISearchPanel {

    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private ArrayList actionListeners;

    public static final int ENTRY_FIELD_LENGTH = 20;

    protected JTextField entryField, replaceField;

    protected JCheckBox reverseButton, incrementalButton, wrappedButton;

    protected JRadioButton literalButton, caseButton, regexpButton;

    private int searchMode;

    public AbstractSearchPanel() {
        super();
        actionListeners = new ArrayList();
        initComponents();
    }

    protected abstract void initComponents();

    protected void createReverseCheckBox() {
        reverseButton = new JCheckBox(ResourceManager.getMessage(ResourceManager.REVERSE_OPTION), false);
    }

    protected void createIncrementalCheckBox() {
        incrementalButton = new JCheckBox(ResourceManager.getMessage(ResourceManager.INCREMENTAL_OPTION), true);
    }

    protected void createWrappedCheckBox() {
        wrappedButton = new JCheckBox(ResourceManager.getMessage(ResourceManager.WRAPPED_OPTION), false);
    }

    protected void createSearchModeButtonGroup() {
        literalButton = new JRadioButton(ResourceManager.getMessage(ResourceManager.LITERAL_OPTION));
        literalButton.setActionCommand(ResourceManager.LITERAL_OPTION);
        caseButton = new JRadioButton(ResourceManager.getMessage(ResourceManager.CASE_OPTION));
        caseButton.setActionCommand(ResourceManager.CASE_OPTION);
        regexpButton = new JRadioButton(ResourceManager.getMessage(ResourceManager.REGEXP_OPTION));
        regexpButton.setActionCommand(ResourceManager.REGEXP_OPTION);
        final ButtonGroup group = new ButtonGroup();
        group.add(literalButton);
        group.add(caseButton);
        group.add(regexpButton);
        ActionListener groupListener = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                int mode = getSearchModeForActionCommand(group.getSelection().getActionCommand());
                AbstractSearchPanel.this.searchMode = mode;
            }
        };
        for (Enumeration eenum = group.getElements(); eenum.hasMoreElements(); ) {
            ((AbstractButton) eenum.nextElement()).addActionListener(groupListener);
        }
        group.setSelected(literalButton.getModel(), true);
    }

    protected void createEntryField() {
        entryField = new JTextField(ENTRY_FIELD_LENGTH);
        entryField.setFont(new Font("Serif", Font.PLAIN, 10));
        entryField.setMaximumSize(entryField.getPreferredSize());
        entryField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent donkey) {
            }

            public void insertUpdate(DocumentEvent donkey) {
                if (isIncremental()) fireActionEvent(isReverse() ? SEARCH_FROM_SELECTION : SEARCH_FROM_CARET);
            }

            public void removeUpdate(DocumentEvent donkey) {
                if (isIncremental()) fireActionEvent(SEARCH_FROM_START_OR_END);
            }
        });
        entryField.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) fireActionEvent(END_SEARCH);
            }
        });
        entryField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                fireActionEvent(isReverse() ? SEARCH_FROM_CARET : SEARCH_FROM_SELECTION);
            }
        });
    }

    public void createReplaceField() {
        replaceField = new JTextField(ENTRY_FIELD_LENGTH);
        replaceField.setFont(new Font("Serif", Font.PLAIN, 10));
        replaceField.setMaximumSize(replaceField.getPreferredSize());
        replaceField.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) fireActionEvent(END_SEARCH);
            }
        });
        replaceField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                fireActionEvent(isReverse() ? SEARCH_FROM_CARET : SEARCH_FROM_SELECTION);
            }
        });
    }

    public JComponent getComponent() {
        return this;
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    public void fireActionEvent(String command) {
        log.trace("in fireActionEvent()");
        ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        Object[] listenerArray = actionListeners.toArray();
        for (int i = 0; i < listenerArray.length; i++) {
            ((ActionListener) listenerArray[i]).actionPerformed(ae);
        }
    }

    protected int getSearchModeForActionCommand(String actionCommand) {
        if (ResourceManager.LITERAL_OPTION.equals(actionCommand)) return LITERAL_MODE; else if (ResourceManager.CASE_OPTION.equals(actionCommand)) return CASE_MODE; else if (ResourceManager.REGEXP_OPTION.equals(actionCommand)) return REGEXP_MODE;
        throw new IllegalArgumentException("unrecognized action command");
    }

    public boolean isReverse() {
        if (reverseButton != null) return reverseButton.isSelected(); else return false;
    }

    public boolean isIncremental() {
        if (incrementalButton != null) return incrementalButton.isSelected(); else return false;
    }

    public boolean isWrapped() {
        if (wrappedButton != null) return wrappedButton.isSelected(); else return false;
    }

    public int getSearchMode() {
        return this.searchMode;
    }

    public String getSearchText() {
        if (entryField != null) return entryField.getText(); else return null;
    }

    public String getReplaceText() {
        if (replaceField != null) return replaceField.getText(); else return null;
    }

    public void focus() {
        entryField.requestFocus();
    }
}
