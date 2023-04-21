package org.mitre.rt.client.ui.recommendations;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.client.xml.ApplicationHelper;
import org.mitre.rt.client.ui.groups.ManageGroups;
import org.mitre.rt.rtclient.GroupType;
import org.mitre.rt.rtclient.RecommendationType;
import org.mitre.rt.client.ui.transfer.ListTransferHandler;
import org.mitre.rt.client.util.GlobalUITools;

public class AddEditRecommendationsPanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(AddEditRecommendationsPanel.class.getPackage().getName());

    private GroupType myGroup;

    private org.mitre.rt.client.ui.groups.ManageGroups parentPanel;

    public AddEditRecommendationsPanel(ManageGroups myPanel) {
        this.parentPanel = myPanel;
        initComponents();
        populateList();
        recommendationList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                int[] selectedCells = recommendationList.getSelectedIndices();
                if (selectedCells.length == 1) {
                    Object selectedObj = recommendationList.getModel().getElementAt(selectedCells[0]);
                    assert selectedObj instanceof RecommendationType;
                    displayRecommendation((RecommendationType) selectedObj);
                }
            }
        });
        recommendationList.setTransferHandler(new ListTransferHandler());
        recommendationList.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                return;
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                return;
            }

            public void mouseEntered(MouseEvent e) {
                return;
            }

            public void mouseExited(MouseEvent e) {
                return;
            }
        });
    }

    public void putRecommendationInList(RecommendationType toDelete) {
        ((DefaultListModel) recommendationList.getModel()).addElement(toDelete);
    }

    private void populateList() {
        RecommendationType[] recList = ApplicationHelper.getUngroupedRecommendations(MetaManager.getMainWindow().getApplicationBar().getApplication());
        try {
            RecommendationTypeListModel recModel = (RecommendationTypeListModel) recommendationList.getModel();
            recModel.removeAllElements();
            for (RecommendationType recRef : recList) {
                recModel.addElement(recRef);
            }
        } catch (ClassCastException ex) {
            logger.error("Unable to populate the rule list!", ex);
        }
    }

    private void displayRecommendation(RecommendationType toDisplay) {
        recommendationTitleLabel.setText(toDisplay.getTitle());
        descriptionField.setText(toDisplay.getDescription());
        rationaleField.setText(toDisplay.getRationale().getText());
    }

    /**
     * 
     */
    public class RecommendationTypeListModel extends DefaultListModel {

        public RecommendationTypeListModel() {
            super();
        }

        @Override
        public RecommendationType elementAt(int index) {
            Object element = super.elementAt(index);
            return (RecommendationType) element;
        }

        @Override
        public RecommendationType get(int index) {
            Object element = super.get(index);
            return (RecommendationType) element;
        }
    }

    private class RecommendationTypeListRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            assert value instanceof RecommendationType;
            RecommendationType thisRec = (RecommendationType) value;
            this.setText(thisRec.getTitle());
            GlobalUITools.setupListCellRendererUI(this, list, index, isSelected);
            return this;
        }
    }

    private void initComponents() {
        jLabel8 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        modifiedLabel = new javax.swing.JLabel();
        createdLabel = new javax.swing.JLabel();
        recommendationTitleLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionField = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        rationaleField = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        recListScrollPane = new javax.swing.JScrollPane();
        recommendationList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        goToMetadataButton = new javax.swing.JButton();
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText("Rationale:");
        setPreferredSize(new java.awt.Dimension(600, 500));
        modifiedLabel.setText("Last modified by <user> on <date>");
        createdLabel.setText("Created by <user> on <date>");
        createdLabel.setMinimumSize(new java.awt.Dimension(50, 14));
        createdLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        recommendationTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        recommendationTitleLabel.setText("titleHere");
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Rationale:");
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Description:");
        descriptionField.setColumns(20);
        descriptionField.setLineWrap(true);
        descriptionField.setRows(5);
        descriptionField.setWrapStyleWord(true);
        descriptionField.setEnabled(false);
        descriptionField.setPreferredSize(new java.awt.Dimension(164, 50));
        jScrollPane1.setViewportView(descriptionField);
        rationaleField.setColumns(20);
        rationaleField.setLineWrap(true);
        rationaleField.setRows(5);
        rationaleField.setWrapStyleWord(true);
        rationaleField.setEnabled(false);
        rationaleField.setPreferredSize(new java.awt.Dimension(164, 50));
        jScrollPane2.setViewportView(rationaleField);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(30, 30, 30).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(30, 30, 30).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(recommendationTitleLabel).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(modifiedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE).addComponent(jLabel2).addComponent(jLabel3))))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(createdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(recommendationTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(createdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(modifiedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE).addContainerGap()));
        recommendationList.setModel(new RecommendationTypeListModel());
        recommendationList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recommendationList.setCellRenderer(new RecommendationTypeListRenderer());
        recommendationList.setDragEnabled(true);
        recListScrollPane.setViewportView(recommendationList);
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText("Available Rules");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jLabel1).addComponent(recListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(recListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE).addContainerGap()));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel4.setText("Add/Edit Rules");
        goToMetadataButton.setText("Edit Group Information");
        goToMetadataButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToMetadataButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 315, Short.MAX_VALUE).addComponent(goToMetadataButton).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(goToMetadataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel4)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
    }

    private void goToMetadataButtonActionPerformed(java.awt.event.ActionEvent evt) {
        parentPanel.displayGroupMetadataPanel(null);
    }

    private javax.swing.JLabel createdLabel;

    private javax.swing.JTextArea descriptionField;

    private javax.swing.JButton goToMetadataButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JLabel modifiedLabel;

    private javax.swing.JTextArea rationaleField;

    private javax.swing.JScrollPane recListScrollPane;

    private javax.swing.JList recommendationList;

    private javax.swing.JLabel recommendationTitleLabel;
}
