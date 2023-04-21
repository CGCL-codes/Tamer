package com.pegaa.uploader.ui.fileselection;

import com.pegaa.uploader.config.ConfigHolder;
import com.pegaa.uploader.lang.Lang;
import com.pegaa.uploader.ui.selectedfilelist.SelectedFileListModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author  tayfun
 */
public class FileSelectionContainer extends javax.swing.JPanel implements ActionListener {

    private ConfigHolder configHolder = null;

    private ArrayList<ActionListener> listeners = null;

    private SelectedFileListModel selectedFileListmodel = null;

    /** Creates new form FileSelectionContainer2 */
    public FileSelectionContainer() {
        initComponents();
        this.listeners = new ArrayList<ActionListener>(2);
    }

    public void setConfigHolder(ConfigHolder configHolder) {
        this.configHolder = configHolder;
        updateComponents();
        updateStrings();
    }

    /**
     *  Sets the texts of components to the selected language
     */
    private void updateStrings() {
        Lang lang = (Lang) this.configHolder.getObject("global.lang");
        this.jTabbedPane1.setTitleAt(0, lang.get("fileselection.tab1"));
        this.jTabbedPane1.setTitleAt(1, lang.get("fileselection.tab2"));
    }

    private void updateComponents() {
        initFileListModel();
        this.bottomToolbar1.setConfigHolder(configHolder);
        this.bottomToolbar1.addActionListener(this);
        this.selectedFileList1.setConfigHolder(configHolder);
        this.fileSelector1.setConfigHolder(configHolder);
        this.selectedFileList1.setListModel(selectedFileListmodel);
    }

    private void initFileListModel() {
        selectedFileListmodel = new SelectedFileListModel();
        this.configHolder.add("global.selected-file-list-model", selectedFileListmodel);
        String max = (String) this.configHolder.getObject("global.fileUploadLimit");
        if (max != null) {
            selectedFileListmodel.setMaxFileCount(Integer.parseInt(max));
        }
    }

    /**
     *  Returns internal model created by this class
     * 
     * @return internal model
     */
    public SelectedFileListModel getModel() {
        return (SelectedFileListModel) this.configHolder.getObject("global.selected-file-list-model");
    }

    /**
     *  adds BottomToolbar listener
     * 
     * @param l
     */
    public void addActionListener(java.awt.event.ActionListener l) {
        this.listeners.add(l);
    }

    /**
     *  Notifies BottomToolbar listeners
     * 
     * @param e
     */
    private void notifyListeners(ActionEvent e) {
        int len = this.listeners.size();
        for (int i = 0; i < len; i++) {
            this.listeners.get(i).actionPerformed(e);
        }
    }

    /**
     *  Event handler of BottomToolbar Event
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (selectedFileListmodel.getSize() == 0) {
            Lang lang = (Lang) this.configHolder.getObject("global.lang");
            javax.swing.JOptionPane.showMessageDialog(this, lang.get("fileselection.selectfile"));
            return;
        }
        this.notifyListeners(e);
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        fileSelector1 = new com.pegaa.uploader.ui.filelist.FileSelector();
        jPanel2 = new javax.swing.JPanel();
        selectedFileList1 = new com.pegaa.uploader.ui.selectedfilelist.SelectedFileList();
        bottomToolbar1 = new com.pegaa.uploader.ui.fileselection.BottomToolbar();
        setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(fileSelector1);
        jTabbedPane1.addTab("", jPanel1);
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));
        jPanel2.add(selectedFileList1);
        jTabbedPane1.addTab("", jPanel2);
        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        bottomToolbar1.setMinimumSize(new java.awt.Dimension(120, 30));
        bottomToolbar1.setPreferredSize(new java.awt.Dimension(120, 40));
        add(bottomToolbar1, java.awt.BorderLayout.SOUTH);
    }

    private com.pegaa.uploader.ui.fileselection.BottomToolbar bottomToolbar1;

    private com.pegaa.uploader.ui.filelist.FileSelector fileSelector1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTabbedPane jTabbedPane1;

    private com.pegaa.uploader.ui.selectedfilelist.SelectedFileList selectedFileList1;
}
