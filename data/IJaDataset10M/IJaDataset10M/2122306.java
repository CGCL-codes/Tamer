package com.emental.mindraider.concept.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Category;
import com.emental.mindraider.MindRaiderConstants;
import com.emental.mindraider.folder.ResourceDescriptor;
import com.emental.mindraider.gfx.Gfx;
import com.emental.mindraider.kernel.MindRaider;
import com.emental.mindraider.outline.NotebookOutlineJPanel;
import com.emental.mindraider.ui.ProgramIconJDialog;

/**
 * Open notebook.
 * 
 * @author Martin.Dvorak
 */
public class OpenConceptJDialog extends ProgramIconJDialog implements MindRaiderConstants {

    /**
     * serial version UID
     */
    private static final long serialVersionUID = -6989778812859123304L;

    private static final Category cat = Category.getInstance("com.emental.mindraider.notebook.ui.OpenConceptJDialog");

    private static final int TEXTFIELD_WIDTH = 30;

    ResourceDescriptor[] resourceDescriptors;

    ArrayList shownDescriptors = new ArrayList();

    private JList list;

    private DefaultListModel defaultListModel;

    /**
     * Constructor.
     * 
     * @throws java.awt.HeadlessException
     */
    public OpenConceptJDialog() {
        super("Open Concept");
        resourceDescriptors = MindRaider.notebookCustodian.getConceptDescriptors();
        if (resourceDescriptors == null) {
            resourceDescriptors = new ResourceDescriptor[0];
        }
        cat.debug("Concepts to open: " + resourceDescriptors.length);
        JPanel framePanel = new JPanel();
        framePanel.setBorder(new EmptyBorder(5, 10, 0, 10));
        framePanel.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(new JLabel(" Select Concept to open: "), BorderLayout.NORTH);
        final JTextField conceptLabel = new JTextField(TEXTFIELD_WIDTH);
        if (resourceDescriptors == null) {
            conceptLabel.setEnabled(false);
        }
        conceptLabel.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    cat.debug("Open selected concept...");
                    try {
                        openConcept();
                    } catch (Exception e1) {
                        cat.debug("Unable to open concept!", e1);
                    }
                }
            }

            public void keyReleased(KeyEvent keyEvent) {
                getListModel().clear();
                shownDescriptors.clear();
                if (conceptLabel.getText().length() > 0) {
                    for (int i = 0; i < resourceDescriptors.length; i++) {
                        if (resourceDescriptors[i].label.toLowerCase().startsWith(conceptLabel.getText().toLowerCase())) {
                            getListModel().addElement(resourceDescriptors[i].label);
                            shownDescriptors.add(resourceDescriptors[i]);
                        }
                    }
                }
            }

            public void keyTyped(KeyEvent keyEvent) {
            }
        });
        northPanel.add(conceptLabel, BorderLayout.SOUTH);
        framePanel.add(northPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JLabel(" Matching concepts: "), BorderLayout.NORTH);
        defaultListModel = new DefaultListModel();
        list = new JList(defaultListModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(15);
        JScrollPane listScrollPane = new JScrollPane(list);
        centerPanel.add(listScrollPane, BorderLayout.SOUTH);
        framePanel.add(centerPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton openButton = new JButton("Open");
        southPanel.add(openButton);
        openButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    openConcept();
                } catch (Exception e1) {
                    cat.debug("Unable to open concept!", e1);
                }
            }
        });
        JButton cancelButton = new JButton("Cancel");
        southPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OpenConceptJDialog.this.dispose();
            }
        });
        framePanel.add(southPanel, BorderLayout.SOUTH);
        getContentPane().add(framePanel, BorderLayout.CENTER);
        pack();
        Gfx.centerAndShowWindow(this);
    }

    /**
     * List model helper.
     * 
     * @return list model.
     */
    private DefaultListModel getListModel() {
        return defaultListModel;
    }

    /**
     * Open selected notebook.
     */
    private void openConcept() throws Exception {
        String conceptToShow = null;
        int selectedIndex = list.getSelectedIndex();
        cat.debug("Selected index: " + selectedIndex);
        if (selectedIndex >= 0 && selectedIndex < shownDescriptors.size()) {
            ResourceDescriptor resourceDescriptor = (ResourceDescriptor) shownDescriptors.get(selectedIndex);
            if (resourceDescriptor != null) {
                conceptToShow = resourceDescriptor.uri;
            }
        } else {
            if (shownDescriptors.size() > 0) {
                conceptToShow = ((ResourceDescriptor) shownDescriptors.get(0)).uri;
            }
        }
        if (conceptToShow != null) {
            cat.debug("Going to open concept: " + conceptToShow);
            try {
                NotebookOutlineJPanel.getInstance().conceptJPanel.refresh(MindRaider.conceptCustodian.get(MindRaider.profile.activeNotebookUri.toString(), conceptToShow));
                NotebookOutlineJPanel.getInstance().setSelectedTreeNodeConcept(conceptToShow);
            } catch (URISyntaxException e1) {
                cat.debug("Unable to load notebook!", e1);
                JOptionPane.showMessageDialog(OpenConceptJDialog.this, "Concept Load Error", "Unable to load notebook: " + e1.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
            OpenConceptJDialog.this.dispose();
        }
    }
}
