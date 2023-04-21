package org.hyperimage.client.gui.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import org.hyperimage.client.Messages;
import org.hyperimage.client.gui.HISimpleTextFieldControl;
import org.hyperimage.connector.fedora3.ws.HiMetadataRecord;
import org.hyperimage.connector.fedora3.ws.HiMetadataSchema;
import org.jdesktop.layout.GroupLayout;

/**
 * @author Jens-Martin Loebel
 */
public class RepositoryMetadataView extends GUIView {

    private static final long serialVersionUID = 5187274828278004546L;

    private JPanel metadataPanel;

    private JPanel repositoryFieldsPanel;

    private GridBagConstraints gridBagConstraints = new GridBagConstraints();

    private JScrollPane fieldsScroll;

    private JLabel infoLabel;

    public RepositoryMetadataView() {
        super(Messages.getString("RepositoryMetadataView.0"));
        initComponents();
        setDisplayPanel(metadataPanel);
    }

    public void setRepositoryFields(HiMetadataSchema repositorySchema) {
    }

    public void setMetadataForElements(int elementCount, List<HiMetadataRecord> repositoryRecords) {
        repositoryFieldsPanel.removeAll();
        repositoryFieldsPanel.doLayout();
        repositoryFieldsPanel.repaint();
        fieldsScroll.doLayout();
        fieldsScroll.repaint();
        if (elementCount != 1) {
            gridBagConstraints.gridy = 0;
            if (elementCount == 0) infoLabel.setText(Messages.getString("RepositoryMetadataView.1")); else infoLabel.setText(elementCount + " " + Messages.getString("RepositoryMetadataView.3"));
            repositoryFieldsPanel.add(infoLabel, gridBagConstraints);
        } else {
            for (int index = 0; index < repositoryRecords.size(); index++) {
                HiMetadataRecord record = repositoryRecords.get(index);
                HISimpleTextFieldControl control = addSingleLineField(record.getKey(), index, repositoryFieldsPanel);
                control.setText(record.getValue());
                control.setEditable(false);
            }
        }
        repositoryFieldsPanel.setVisible(false);
        repositoryFieldsPanel.doLayout();
        repositoryFieldsPanel.repaint();
        fieldsScroll.doLayout();
        fieldsScroll.repaint();
        repositoryFieldsPanel.setVisible(true);
        fieldsScroll.doLayout();
        fieldsScroll.repaint();
    }

    private HISimpleTextFieldControl addSingleLineField(String title, int index, JPanel panel) {
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 1.0d;
        gridBagConstraints.weighty = 0.0d;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = index;
        HISimpleTextFieldControl slPanel = new HISimpleTextFieldControl(title);
        panel.add(slPanel, gridBagConstraints);
        return slPanel;
    }

    private void initComponents() {
        metadataPanel = new JPanel();
        repositoryFieldsPanel = new JPanel();
        fieldsScroll = new JScrollPane(repositoryFieldsPanel);
        repositoryFieldsPanel.setBorder(BorderFactory.createTitledBorder(null, Messages.getString("RepositoryMetadataView.4"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 0, 13), java.awt.Color.blue));
        GroupLayout metadataPanelLayout = new GroupLayout(metadataPanel);
        metadataPanel.setLayout(metadataPanelLayout);
        metadataPanelLayout.setHorizontalGroup(metadataPanelLayout.createParallelGroup(GroupLayout.LEADING).add(metadataPanelLayout.createSequentialGroup().addContainerGap().add(fieldsScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        metadataPanelLayout.setVerticalGroup(metadataPanelLayout.createParallelGroup(GroupLayout.LEADING).add(metadataPanelLayout.createSequentialGroup().addContainerGap().add(fieldsScroll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        infoLabel = new JLabel();
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        repositoryFieldsPanel.setLayout(new GridBagLayout());
        fieldsScroll.setPreferredSize(new Dimension(400, 500));
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 1.0d;
        gridBagConstraints.weighty = 0.0d;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
    }
}
