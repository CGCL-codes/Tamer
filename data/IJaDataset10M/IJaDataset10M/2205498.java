package com.frinika.gui;

import com.frinika.project.gui.ProjectFrame;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Generic OptionsDialog for user-interaction. Content must be set via setPanel or via
 * constructor-parameter. The dialog provides OK/Cancel buttons and automatically
 * refreshes/updates OptionEditors.
 * 
 * (Created with NetBeans 5.5 gui-editor, see corresponding .form file.)
 * 
 * @author Jens Gulden
 */
public class OptionsDialog extends AbstractDialog {

    protected JComponent content;

    protected Dimension repackDelta = new Dimension(30, 20);

    /** 
     * Creates new form OptionsDialog.
     * @param content may implement 'OptionsEditor', in this case
     *                refresh() is called every time the Dialog is shown, 
     *                update() every time the Dialog is hidden.
     */
    public OptionsDialog(ProjectFrame parent, JComponent content, String title) {
        super(parent, title, true);
        initComponents();
        this.setContent(content);
        getContentPanel().add(content, BorderLayout.CENTER);
        this.getRootPane().setDefaultButton(okButton);
    }

    public void repack() {
        pack();
        Dimension size = getSize();
        setSize(size.width + repackDelta.width, size.height + repackDelta.height);
    }

    public void setRepackDelta(Dimension repackDelta) {
        this.repackDelta = repackDelta;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonsPanel = new javax.swing.JPanel();
        buttonsInnerPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        editorPanelOuter = new javax.swing.JPanel();
        contentPanel = new javax.swing.JPanel();
        setModal(true);
        buttonsInnerPanel.setLayout(new java.awt.GridLayout(1, 2, 10, 0));
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonsInnerPanel.add(okButton);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonsInnerPanel.add(cancelButton);
        buttonsPanel.add(buttonsInnerPanel);
        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);
        editorPanelOuter.setLayout(new java.awt.GridBagLayout());
        editorPanelOuter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        contentPanel.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 5, 3);
        editorPanelOuter.add(contentPanel, gridBagConstraints);
        getContentPane().add(editorPanelOuter, java.awt.BorderLayout.CENTER);
        pack();
    }

    private boolean firstTimeShow = true;

    public void show() {
        canceled = false;
        if (content instanceof OptionsEditor) {
            ((OptionsEditor) content).refresh();
        }
        if (firstTimeShow == true) {
            repack();
            centerOnScreen(this);
            firstTimeShow = false;
        }
        super.show();
    }

    public void hide() {
        if ((!isCanceled()) && (content instanceof OptionsEditor)) {
            ((OptionsEditor) content).update();
        }
        super.hide();
    }

    public JComponent getContent() {
        return content;
    }

    public void setContent(JComponent content) {
        this.content = content;
        getContentPanel().add(content, BorderLayout.CENTER);
    }

    public javax.swing.JPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(javax.swing.JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        cancel();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ok();
    }

    private javax.swing.JPanel buttonsInnerPanel;

    private javax.swing.JPanel buttonsPanel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JPanel contentPanel;

    private javax.swing.JPanel editorPanelOuter;

    private javax.swing.JButton okButton;
}
