package com.ivis.xprocess.web.client.pages;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import com.ivis.xprocess.web.client.ui.ClientManager;
import com.ivis.xprocess.web.client.util.ServicesWarehouse;
import com.ivis.xprocess.web.service.ProjectServiceStub;
import com.ivis.xprocess.web.service.RemoteExceptionException0;
import com.ivis.xprocess.web.service.ProjectServiceStub.FolderDAO;
import com.ivis.xprocess.web.service.ProjectServiceStub.GetFolder;
import com.ivis.xprocess.web.service.ProjectServiceStub.GetFolderResponse;
import com.toedter.calendar.JDateChooser;

public class FolderEditorPage extends EditorPage {

    private static final long serialVersionUID = 1L;

    private JPanel mainPanel;

    private JTextField nameField;

    private JTextArea descriptionField;

    private JDateChooser targetStartChooser;

    private JDateChooser targetEndChooser;

    public FolderEditorPage(ClientManager panelManager, String uuid, String pageName) {
        super(panelManager, uuid, pageName);
        setLayout(new BorderLayout());
    }

    protected void createPage() {
        System.out.println("[1]");
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        JLabel label = new JLabel();
        label.setText("Folder editing page...");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(label, c);
        JLabel nameLabel = new JLabel();
        nameLabel.setText("Name:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(nameLabel, c);
        nameField = new JTextField();
        nameField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (!internalUpdate) {
                    propertyWrapperWarehouse.put("String", "NAME", nameField.getText());
                }
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 2;
        c.gridy = 1;
        mainPanel.add(nameField, c);
        JPanel targetDatesPanel = new JPanel();
        targetDatesPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 0);
        c.weightx = 1;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 2;
        mainPanel.add(targetDatesPanel, c);
        JLabel targetStartLabel = new JLabel("Target Start:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        targetDatesPanel.add(targetStartLabel, c);
        targetStartChooser = new JDateChooser();
        targetStartChooser.addPropertyChangeListener("date", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!internalUpdate) {
                    propertyWrapperWarehouse.put("DAY", "TARGET_START", targetStartChooser.getDate().getTime());
                }
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 1;
        c.gridy = 0;
        targetDatesPanel.add(targetStartChooser, c);
        JLabel targetEndLabel = new JLabel("End:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        targetDatesPanel.add(targetEndLabel, c);
        targetEndChooser = new JDateChooser();
        targetEndChooser.addPropertyChangeListener("date", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!internalUpdate) {
                    propertyWrapperWarehouse.put("DAY", "TARGET_END", targetEndChooser.getDate().getTime());
                }
            }
        });
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 3;
        c.gridy = 0;
        targetDatesPanel.add(targetEndChooser, c);
        JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText("Description:");
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        mainPanel.add(descriptionLabel, c);
        descriptionField = new JTextArea();
        descriptionField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (!internalUpdate) {
                    String richTextRepresentation = "\\ENCODING\\standard\\TEXT\\" + descriptionField.getText();
                    propertyWrapperWarehouse.put("Rich", "RICH_DESCRIPTION", richTextRepresentation);
                }
            }
        });
        descriptionField.setLineWrap(true);
        JScrollPane scrollPanel = new JScrollPane(descriptionField);
        scrollPanel.setBorder(new EtchedBorder());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 1;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        mainPanel.add(scrollPanel, c);
        add(mainPanel, BorderLayout.CENTER);
        createButtonBar();
        showData();
    }

    @Override
    protected void showData() {
        internalUpdate = true;
        ProjectServiceStub prss = ServicesWarehouse.getProjectServiceStub();
        GetFolder getFolder = new GetFolder();
        getFolder.setSessionToken(ServicesWarehouse.getSessionToken());
        System.out.println("uuid: " + uuid);
        getFolder.setFolderUuid(uuid);
        try {
            GetFolderResponse gfr = prss.getFolder(getFolder);
            FolderDAO folderDAO = gfr.get_return();
            if (folderDAO != null) {
                nameField.setText(folderDAO.getLabel());
                descriptionField.setText(folderDAO.getDescription());
                long targetStart = folderDAO.getTargetStart();
                if (targetStart != 0) {
                    targetStartChooser.setDate(new Date(targetStart));
                } else {
                    targetStartChooser.setDate(null);
                }
                long targetEnd = folderDAO.getTargetEnd();
                if (targetEnd != 0) {
                    targetEndChooser.setDate(new Date(targetEnd));
                } else {
                    targetEndChooser.setDate(null);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RemoteExceptionException0 e) {
            e.printStackTrace();
        } finally {
            internalUpdate = false;
        }
    }
}
