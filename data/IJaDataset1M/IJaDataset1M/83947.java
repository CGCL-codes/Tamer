package org.dancecues.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.dancecues.control.AbstractBusyAction;
import org.dancecues.data.CueSheet;
import org.dancecues.gui.util.EnableWhenNonBlankListener;
import org.dancecues.gui.util.GUIUtil;

public class CueSheetHeaderDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    protected JTextField nameField = new JTextField();

    protected JTextField sequenceField = new JTextField();

    protected JList attrList = new JList(new DefaultListModel());

    protected CueSheet sheet;

    public CueSheetHeaderDialog(Frame parent, CueSheet sheet) {
        super(parent, "Edit Cue Sheet Information", true);
        if (sheet == null) {
            throw new NullPointerException("Cue sheet cannot be null.");
        }
        this.sheet = sheet;
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        JPanel textPanel = new JPanel(new GridLayout());
        textPanel.add(makeNamePanel());
        textPanel.add(makeSequencePanel());
        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(makeDialogCenter(), BorderLayout.CENTER);
        mainPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, ks);
        mainPanel.getActionMap().put(ks, new CancelAction());
        pack();
        if (getSize().width < GUIUtil.DEFAULT_MIN_WIDTH) {
            setSize(GUIUtil.DEFAULT_MIN_WIDTH, getSize().height);
        }
        GUIUtil.sizeAndCenterDialog(parent, this);
    }

    protected JPanel makeNamePanel() {
        JPanel retVal = new JPanel(new BorderLayout());
        nameField.setText(sheet.getName());
        nameField.addActionListener(new OKListener());
        retVal.add(new JLabel("Dance Name: "), BorderLayout.WEST);
        retVal.add(nameField, BorderLayout.CENTER);
        return retVal;
    }

    protected JPanel makeSequencePanel() {
        JPanel retVal = new JPanel(new BorderLayout());
        sequenceField.setText(sheet.getSequence());
        retVal.add(new JLabel("Sequence: "), BorderLayout.WEST);
        retVal.add(sequenceField, BorderLayout.CENTER);
        return retVal;
    }

    protected JPanel makeDialogCenter() {
        JPanel retVal = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new BorderLayout());
        retVal.add(southPanel, BorderLayout.SOUTH);
        final JTextField textField = new JTextField();
        southPanel.add(textField, BorderLayout.CENTER);
        JButton addButton = new JButton("Add Text");
        ActionListener listener = new AddAttributeAction(textField);
        addButton.addActionListener(listener);
        textField.addActionListener(listener);
        southPanel.add(addButton, BorderLayout.WEST);
        JPanel listPanel = new JPanel(new BorderLayout());
        retVal.add(listPanel, BorderLayout.CENTER);
        for (String attr : sheet.getAttributes()) {
            ((DefaultListModel) attrList.getModel()).addElement(attr);
        }
        JScrollPane attrScroll = new JScrollPane(attrList);
        attrScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        attrScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listPanel.add(attrScroll, BorderLayout.CENTER);
        JPanel removePanel = new JPanel(new FlowLayout());
        listPanel.add(removePanel, BorderLayout.SOUTH);
        final JButton removeButton = new JButton("Remove Text");
        removeButton.addActionListener(new RemoveAttributesAction());
        removeButton.setEnabled(false);
        attrList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                removeButton.setEnabled(attrList.getSelectedIndex() >= 0);
            }
        });
        return retVal;
    }

    private JPanel makeButtonPanel() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.RIGHT);
        JPanel retVal = new JPanel(layout);
        JButton okButton = new JButton("OK");
        DocumentListener listener = new EnableWhenNonBlankListener(nameField, okButton);
        nameField.getDocument().addDocumentListener(listener);
        okButton.addActionListener(new OKListener());
        retVal.add(okButton);
        retVal.add(new JButton(new CancelAction()));
        return retVal;
    }

    private class AddAttributeAction extends AbstractBusyAction {

        private static final long serialVersionUID = 1L;

        private final JTextField textField;

        public AddAttributeAction(JTextField textField) {
            super(CueSheetHeaderDialog.this, "add attribute text to the header dialog");
            this.textField = textField;
        }

        public void internalActionPerformed(ActionEvent ae) {
            String text = textField.getText();
            if ((text != null) && (!text.equals(""))) {
                ((DefaultListModel) attrList.getModel()).addElement(text);
                textField.setText("");
            }
        }
    }

    private class RemoveAttributesAction extends AbstractBusyAction {

        private static final long serialVersionUID = 1L;

        public RemoveAttributesAction() {
            super(CueSheetHeaderDialog.this, "remove selected attributes from the sheet");
        }

        public void internalActionPerformed(ActionEvent ae) {
            int[] selection = attrList.getSelectedIndices();
            if (selection != null) {
                for (int x = (selection.length - 1); x >= 0; x--) {
                    sheet.removeAttribute(selection[x]);
                }
            }
        }
    }

    private class CancelAction extends AbstractBusyAction {

        private static final long serialVersionUID = -4162621721107865712L;

        public CancelAction() {
            super(CueSheetHeaderDialog.this, "cancel out of the cue sheet header dialog");
            putValue(NAME, "Cancel");
        }

        public void internalActionPerformed(ActionEvent arg0) {
            dispose();
        }
    }

    private class OKListener extends AbstractBusyAction {

        private static final long serialVersionUID = 1L;

        public OKListener() {
            super(CueSheetHeaderDialog.this, "set the header info from the dialog onto the sheet");
        }

        public void internalActionPerformed(ActionEvent arg0) {
            sheet.setName(nameField.getText());
            sheet.setSequence(sequenceField.getText());
            sheet.clearAttributes();
            ListModel model = attrList.getModel();
            for (int x = 0; x < model.getSize(); x++) {
                sheet.addAttribute(model.getElementAt(x).toString());
            }
            dispose();
        }
    }
}
