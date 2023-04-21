package com.emental.mindraider.model.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.emental.mindraider.MindRaiderConstants;
import com.emental.mindraider.gfx.Gfx;
import com.emental.mindraider.graph.spiders.SpidersGraph;
import com.emental.mindraider.kernel.MindRaider;
import com.emental.mindraider.ui.ProgramIconJDialog;

/**
 * New RDF Model.
 * 
 * @author Martin.Dvorak
 */
public class NewRdfModelJDialog extends ProgramIconJDialog implements MindRaiderConstants {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = 7979982777537952504L;

    private JTextField subjectNs, subjectLocalName;

    private String nodeNameString = null;

    /**
     * Constructor.
     * 
     * @param union made union of models.
     */
    public NewRdfModelJDialog() {
        super("New Model");
        JPanel framePanel = new JPanel();
        framePanel.setLayout(new GridLayout(3, 1));
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        p.add(new JLabel(" Subject: "));
        subjectNs = new JTextField(30);
        subjectNs.setText(MR_RDF_PREDICATE_NS);
        p.add(subjectNs);
        p.add(new JLabel("#"));
        subjectLocalName = new JTextField(15);
        p.add(subjectLocalName);
        framePanel.add(p);
        p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        final JCheckBox literalCheckBox = new JCheckBox("literal", false);
        p.add(literalCheckBox);
        framePanel.add(p);
        p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Create");
        p.add(addButton);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                createModel(literalCheckBox);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        p.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NewRdfModelJDialog.this.dispose();
            }
        });
        framePanel.add(p);
        subjectLocalName.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    createModel(literalCheckBox);
                }
            }

            public void keyReleased(KeyEvent keyEvent) {
            }

            public void keyTyped(KeyEvent keyEvent) {
            }
        });
        getContentPane().add(framePanel, BorderLayout.CENTER);
        pack();
        Gfx.centerAndShowWindow(this);
        addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                subjectLocalName.requestFocusInWindow();
            }
        });
    }

    /**
     * Create model.
     * 
     * @param literalCheckBox
     */
    private void createModel(final JCheckBox literalCheckBox) {
        if (subjectNs.getText() == null || "".equals(subjectNs.getText())) {
            subjectNs.setText(MR_RDF_NS);
        }
        MindRaider.spidersGraph.newResource(subjectNs.getText() + subjectLocalName.getText(), literalCheckBox.isSelected());
        MindRaider.masterToolBar.setModelLocation(SpidersGraph.MINDRAIDER_NEW_MODEL);
        dispose();
    }
}
