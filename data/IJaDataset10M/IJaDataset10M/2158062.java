package org.hironico.dbtool2.dbcopy;

import org.hironico.database.dbcopy.DbLink;
import java.awt.Window;
import java.io.File;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.hironico.database.driver.ConnectionPool;
import org.hironico.database.driver.ConnectionPoolManager;
import org.hironico.gui.image.ImageCache;
import org.jdesktop.swingx.JXPanel;
import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hironico.dbtool2.config.OpenNewConnectionAction;
import org.hironico.dbtool2.config.DbToolConfiguration;
import org.hironico.util.threadmonitor.MonitorThread;

/**
 * Panel permettant de modéliser la GUI pour le db copy tool.
 * Il 'agit de créer des objets de lien qui contiennent le setup
 * de la source vers la destination. Cela comporte les noms des colonnes
 * pour les bases de données et le numéro de colonne pour les fichiers.
 * Il est ainsi possible d'adapter totalement le format
 * @author hironico
 */
public class UserDefinedDbCopyPanel extends JXPanel {

    protected static final Logger logger = Logger.getLogger("org.hironico.dbtool2.dbcopy");

    private SQLTableColumnCellEditor tableLinkCellEditor = new SQLTableColumnCellEditor(new ArrayList<String>());

    /** Creates new form UserDefinedDbCopyPanel */
    public UserDefinedDbCopyPanel() {
        initComponents();
        taskPaneSetupSource.add(pnlSourceSetup);
        taskPaneDestinationSetup.add(pnlDestSetup);
        taskPaneDefinedLinks.add(pnlLinkSetup);
        loadIcons();
        taskPaneDestinationSetup.setCollapsed(true);
        taskPaneSetupSource.setCollapsed(false);
        taskPaneDefinedLinks.setCollapsed(false);
        mainToolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        mainToolBar.putClientProperty(Plastic3DLookAndFeel.BORDER_STYLE_KEY, BorderStyle.SEPARATOR);
        DbToolConfiguration config = DbToolConfiguration.getInstance();
        listDefinedLinks.addHighlighter(config.getZebraHighlighter());
        tableLinkEditor.addHighlighter(config.getZebraHighlighter());
        tableLinkEditor.setDefaultEditor(String.class, tableLinkCellEditor);
    }

    public void addDbLink(DbLink dbLink) {
        DefaultListModel model = (DefaultListModel) listDefinedLinks.getModel();
        if (!model.contains(dbLink)) {
            model.addElement(dbLink);
        }
        listDefinedLinks.setSelectedValue(dbLink, true);
    }

    /**
     * Permet d'afficher le paramétrage pour un lien dont la description est passée en paramétre.
     * Cette méthode mets à jour l'affichage des sources et destination ainsi que du mapping.
     * @param dbLink le lien à afficher dans la fenetre.
     * @since 2.0.0
     */
    public void showDbLink(DbLink dbLink) {
        showLinkOptions(dbLink);
        showSourceSetup(dbLink);
        showDestinationSetup(dbLink);
        showMappingSetup(dbLink);
    }

    protected void showLinkOptions(DbLink dbLink) {
        chkCreateStructures.setEnabled(dbLink != null);
        chkCopyData.setEnabled(dbLink != null);
        if (dbLink != null) {
            chkCreateStructures.setSelected(dbLink.isCreateStructures());
            chkCopyData.setSelected(dbLink.isCopyData());
            chkIncludeDependencies.setSelected(dbLink.isIncludeDependencies());
        }
    }

    protected void showSourceSetup(DbLink dbLink) {
        if (dbLink == null) {
            return;
        }
        String srcConnection = dbLink.getSourceDbName();
        String tableOrFile = dbLink.getSourceTableOrFile();
        if (srcConnection != null) {
            radioSrcTable.setSelected(true);
            if (srcConnection.equals((String) cmbSrcConnection.getSelectedItem())) return;
            ConnectionPool pool = ConnectionPoolManager.getInstance().getConnectionPool(srcConnection);
            if (pool == null) {
                OpenNewConnectionAction newConAction = new OpenNewConnectionAction(srcConnection);
                newConAction.actionPerformed(null);
            }
            showActiveConnections();
            cmbSrcConnection.setSelectedItem(srcConnection);
            cmbSrcTable.setSelectedItem(tableOrFile);
        } else {
            radioSrcFileName.setSelected(true);
            txtSrcFileName.setText(tableOrFile);
            chkSrcFileDropHeaders.setSelected(dbLink.getSourceFileFormat().dropHeaders);
            if (dbLink.getSourceFileFormat().fieldSeparatorChar == '\t') {
                radioSrcSeparatorTab.setSelected(true);
            } else {
                radioSrcSeparatorUserDef.setSelected(true);
                txtSrcSeparatorUserDef.setText("" + dbLink.getSourceFileFormat().fieldSeparatorChar);
            }
        }
    }

    protected void showDestinationSetup(DbLink dbLink) {
        if (dbLink == null) {
            return;
        }
        String destConnection = dbLink.getDestinationDbNameOrFile();
        if (destConnection != null) {
            radioDestTable.setSelected(true);
            ConnectionPool pool = ConnectionPoolManager.getInstance().getConnectionPool(destConnection);
            if (pool == null) {
                OpenNewConnectionAction newConAction = new OpenNewConnectionAction(destConnection);
                newConAction.actionPerformed(null);
                boolean found = false;
                for (int cpt = 0; cpt < cmbDestConnection.getItemCount(); cpt++) {
                    String myCon = (String) cmbDestConnection.getItemAt(cpt);
                    if (destConnection.equals(myCon)) {
                        found = true;
                        break;
                    }
                }
                if (!found) cmbDestConnection.addItem(destConnection);
            }
            cmbDestConnection.setSelectedItem(destConnection);
        } else {
            radioDestFileName.setSelected(true);
            txtDestFileName.setText(destConnection);
            chkDestFileDropHeaders.setSelected(dbLink.getDestinationFileFormat().dropHeaders);
            if (dbLink.getDestinationFileFormat().fieldSeparatorChar == '\t') {
                radioDestSeparatorTab.setSelected(true);
            } else {
                radioDestSepatatorUserDef.setSelected(true);
                txtDestSeparatorUserDef.setText("" + dbLink.getDestinationFileFormat().fieldSeparatorChar);
            }
        }
    }

    protected void showMappingSetup(DbLink dbLink) {
        DefaultTableModel model = (DefaultTableModel) tableLinkEditor.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        if (dbLink == null) {
            return;
        }
        tableLinkCellEditor.cancelCellEditing();
        Map<String, String> mapping = dbLink.getDestinationMapping();
        JComboBox cmb = tableLinkCellEditor.getJComponent();
        cmb.removeAllItems();
        for (String col : mapping.values()) {
            cmb.addItem(col);
        }
        for (String destColumn : mapping.keySet()) {
            String row[] = new String[model.getColumnCount()];
            row[0] = mapping.get(destColumn);
            row[1] = destColumn;
            model.addRow(row);
        }
    }

    /**
     * Normalement cette méthode est appelée (entre autres) depuis la OpenDbCopyDialogAction qui
     * fait le setup lors d'appels sucessifs pour afficher la dialog.
     * @since 2.0.0
     */
    public void showActiveConnections() {
        logger.debug("Show active connections...");
        cmbSrcConnection.setEnabled(false);
        cmbDestConnection.setEnabled(false);
        cmbSrcTable.setEnabled(false);
        Vector<String> connections = ConnectionPoolManager.getInstance().getDatabaseNames();
        cmbSrcConnection.removeAllItems();
        cmbDestConnection.removeAllItems();
        for (String conName : connections) {
            cmbDestConnection.addItem(conName);
            cmbSrcConnection.addItem(conName);
        }
        cmbSrcConnection.setEnabled(true);
        cmbDestConnection.setEnabled(true);
        cmbSrcTable.setEnabled(true);
        if (connections.size() > 0) {
            cmbSrcConnectionItemStateChanged(null);
        }
    }

    /**
     * Permet de charger les icones.
     * @since 2.0.0
     */
    protected void loadIcons() {
        ImageIcon icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/data_up.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/data_up.png");
        } else {
            taskPaneSetupSource.setIcon(icon);
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/data_down.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/data_down.png");
        } else {
            taskPaneDestinationSetup.setIcon(icon);
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/link.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/link.png");
        } else {
            taskPaneDefinedLinks.setIcon(icon);
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/link_add.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/link_add.png");
        } else {
            btnNewLink.setIcon(icon);
            btnNewLink.setText("");
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/link_delete.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/link_delete.png");
        } else {
            btnRemoveLink.setIcon(icon);
            btnRemoveLink.setText("");
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/folder_document.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/folder_document.png");
        } else {
            btnLoadSetup.setIcon(icon);
            btnLoadSetup.setText("");
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/disk_blue.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/disk_blue.png");
        } else {
            btnSaveSetup.setIcon(icon);
            btnSaveSetup.setText("");
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/row_add.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/row_add.png");
        } else {
            btnNewMapping.setIcon(icon);
            btnNewMapping.setText("");
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/row_delete.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/row_delete.png");
        } else {
            btnRemoveMapping.setIcon(icon);
            btnRemoveMapping.setText("");
        }
        icon = ImageCache.getInstance().loadImageIcon("org/hironico/resource/icons/inconexperience/small/shadow/data_copy.png");
        if (icon == null) {
            logger.error("Cannot load icon : org/hironico/resource/icons/inconexperience/small/shadow/data_copy.png");
        } else {
            btnCopyFullDatabase.setIcon(icon);
            btnCopyFullDatabase.setText("");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        pnlSourceSetup = new org.jdesktop.swingx.JXPanel();
        radioSrcTable = new javax.swing.JRadioButton();
        radioSrcFileName = new javax.swing.JRadioButton();
        txtSrcFileName = new javax.swing.JTextField();
        lblSrcFieldSeparator = new javax.swing.JLabel();
        pnlSrcFieldSeparator = new org.jdesktop.swingx.JXPanel();
        radioSrcSeparatorTab = new javax.swing.JRadioButton();
        radioSrcSeparatorUserDef = new javax.swing.JRadioButton();
        txtSrcSeparatorUserDef = new javax.swing.JTextField();
        chkSrcFileDropHeaders = new javax.swing.JCheckBox();
        btnBrowseSrcFile = new javax.swing.JButton();
        pnlDatabaseTable = new org.jdesktop.swingx.JXPanel();
        cmbSrcConnection = new javax.swing.JComboBox();
        cmbSrcTable = new javax.swing.JComboBox();
        pnlDestSetup = new org.jdesktop.swingx.JXPanel();
        radioDestTable = new javax.swing.JRadioButton();
        radioDestFileName = new javax.swing.JRadioButton();
        txtDestFileName = new javax.swing.JTextField();
        lblDestFieldSeparator = new javax.swing.JLabel();
        pnlDestFieldSeparator = new org.jdesktop.swingx.JXPanel();
        radioDestSeparatorTab = new javax.swing.JRadioButton();
        radioDestSepatatorUserDef = new javax.swing.JRadioButton();
        txtDestSeparatorUserDef = new javax.swing.JTextField();
        btnBrowseDestFile = new javax.swing.JButton();
        cmbDestConnection = new javax.swing.JComboBox();
        pnlDestFileFormat = new org.jdesktop.swingx.JXPanel();
        chkDestFileDropHeaders = new javax.swing.JCheckBox();
        chkDestFileAppendMode = new javax.swing.JCheckBox();
        btnGrpRadioSrc = new javax.swing.ButtonGroup();
        btnGrpRadioDest = new javax.swing.ButtonGroup();
        btnGrpRadioSrcSeparator = new javax.swing.ButtonGroup();
        btnGrpRadioDestSeparator = new javax.swing.ButtonGroup();
        pnlLinkSetup = new org.jdesktop.swingx.JXPanel();
        scrolldefinedLinks = new javax.swing.JScrollPane();
        listDefinedLinks = new org.jdesktop.swingx.JXList();
        pnlLinkOptions = new org.jdesktop.swingx.JXPanel();
        chkCreateStructures = new javax.swing.JCheckBox();
        chkCopyData = new javax.swing.JCheckBox();
        chkIncludeDependencies = new javax.swing.JCheckBox();
        txtExplain = new javax.swing.JTextPane();
        pnlMain = new org.jdesktop.swingx.JXPanel();
        mainToolBar = new javax.swing.JToolBar();
        btnLoadSetup = new javax.swing.JButton();
        btnSaveSetup = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnNewLink = new javax.swing.JButton();
        btnRemoveLink = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnNewMapping = new javax.swing.JButton();
        btnRemoveMapping = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnCopyFullDatabase = new javax.swing.JButton();
        splitMain = new javax.swing.JSplitPane();
        scrollTaskpaneContainer = new javax.swing.JScrollPane();
        taskpaneContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        taskPaneDefinedLinks = new org.jdesktop.swingx.JXTaskPane();
        taskPaneSetupSource = new org.jdesktop.swingx.JXTaskPane();
        taskPaneDestinationSetup = new org.jdesktop.swingx.JXTaskPane();
        scrollLinkEditor = new javax.swing.JScrollPane();
        tableLinkEditor = new org.jdesktop.swingx.JXTable();
        mainStatusBar = new org.jdesktop.swingx.JXStatusBar();
        progressbar = new javax.swing.JProgressBar();
        pnlCommands = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        btnProceed = new javax.swing.JButton();
        pnlSourceSetup.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pnlSourceSetup.setLayout(new java.awt.GridBagLayout());
        btnGrpRadioSrc.add(radioSrcTable);
        radioSrcTable.setSelected(true);
        radioSrcTable.setText("Database & table:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSourceSetup.add(radioSrcTable, gridBagConstraints);
        btnGrpRadioSrc.add(radioSrcFileName);
        radioSrcFileName.setText("File name:");
        radioSrcFileName.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioSrcFileNameItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSourceSetup.add(radioSrcFileName, gridBagConstraints);
        txtSrcFileName.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlSourceSetup.add(txtSrcFileName, gridBagConstraints);
        lblSrcFieldSeparator.setText("Field separator:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlSourceSetup.add(lblSrcFieldSeparator, gridBagConstraints);
        pnlSrcFieldSeparator.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        btnGrpRadioSrcSeparator.add(radioSrcSeparatorTab);
        radioSrcSeparatorTab.setText("Tab");
        radioSrcSeparatorTab.setEnabled(false);
        pnlSrcFieldSeparator.add(radioSrcSeparatorTab);
        btnGrpRadioSrcSeparator.add(radioSrcSeparatorUserDef);
        radioSrcSeparatorUserDef.setSelected(true);
        radioSrcSeparatorUserDef.setText("User defined:");
        radioSrcSeparatorUserDef.setEnabled(false);
        pnlSrcFieldSeparator.add(radioSrcSeparatorUserDef);
        txtSrcSeparatorUserDef.setText(",");
        txtSrcSeparatorUserDef.setEnabled(false);
        txtSrcSeparatorUserDef.setPreferredSize(new java.awt.Dimension(25, 20));
        pnlSrcFieldSeparator.add(txtSrcSeparatorUserDef);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSourceSetup.add(pnlSrcFieldSeparator, gridBagConstraints);
        chkSrcFileDropHeaders.setText("Drop headers");
        chkSrcFileDropHeaders.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        pnlSourceSetup.add(chkSrcFileDropHeaders, gridBagConstraints);
        btnBrowseSrcFile.setText("...");
        btnBrowseSrcFile.setEnabled(false);
        btnBrowseSrcFile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseSrcFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlSourceSetup.add(btnBrowseSrcFile, gridBagConstraints);
        pnlDatabaseTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnlDatabaseTable.setLayout(new java.awt.GridBagLayout());
        cmbSrcConnection.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSrcConnectionItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlDatabaseTable.add(cmbSrcConnection, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlDatabaseTable.add(cmbSrcTable, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlSourceSetup.add(pnlDatabaseTable, gridBagConstraints);
        pnlDestSetup.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pnlDestSetup.setLayout(new java.awt.GridBagLayout());
        btnGrpRadioDest.add(radioDestTable);
        radioDestTable.setSelected(true);
        radioDestTable.setText("Database:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDestSetup.add(radioDestTable, gridBagConstraints);
        btnGrpRadioDest.add(radioDestFileName);
        radioDestFileName.setText("File name:");
        radioDestFileName.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioDestFileNameItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDestSetup.add(radioDestFileName, gridBagConstraints);
        txtDestFileName.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlDestSetup.add(txtDestFileName, gridBagConstraints);
        lblDestFieldSeparator.setText("Field separator:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlDestSetup.add(lblDestFieldSeparator, gridBagConstraints);
        pnlDestFieldSeparator.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        btnGrpRadioDestSeparator.add(radioDestSeparatorTab);
        radioDestSeparatorTab.setText("Tab");
        radioDestSeparatorTab.setEnabled(false);
        pnlDestFieldSeparator.add(radioDestSeparatorTab);
        btnGrpRadioDestSeparator.add(radioDestSepatatorUserDef);
        radioDestSepatatorUserDef.setSelected(true);
        radioDestSepatatorUserDef.setText("User defined:");
        radioDestSepatatorUserDef.setEnabled(false);
        pnlDestFieldSeparator.add(radioDestSepatatorUserDef);
        txtDestSeparatorUserDef.setText(",");
        txtDestSeparatorUserDef.setEnabled(false);
        txtDestSeparatorUserDef.setPreferredSize(new java.awt.Dimension(25, 20));
        pnlDestFieldSeparator.add(txtDestSeparatorUserDef);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlDestSetup.add(pnlDestFieldSeparator, gridBagConstraints);
        btnBrowseDestFile.setText("...");
        btnBrowseDestFile.setEnabled(false);
        btnBrowseDestFile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseDestFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlDestSetup.add(btnBrowseDestFile, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlDestSetup.add(cmbDestConnection, gridBagConstraints);
        pnlDestFileFormat.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        chkDestFileDropHeaders.setText("Drop headers");
        chkDestFileDropHeaders.setEnabled(false);
        pnlDestFileFormat.add(chkDestFileDropHeaders);
        chkDestFileAppendMode.setText("Append mode");
        chkDestFileAppendMode.setEnabled(false);
        pnlDestFileFormat.add(chkDestFileAppendMode);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        pnlDestSetup.add(pnlDestFileFormat, gridBagConstraints);
        pnlLinkSetup.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pnlLinkSetup.setLayout(new java.awt.GridBagLayout());
        scrolldefinedLinks.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        listDefinedLinks.setModel(new DefaultListModel());
        listDefinedLinks.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listDefinedLinksValueChanged(evt);
            }
        });
        scrolldefinedLinks.setViewportView(listDefinedLinks);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlLinkSetup.add(scrolldefinedLinks, gridBagConstraints);
        pnlLinkOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Link options:"));
        pnlLinkOptions.setLayout(new java.awt.GridBagLayout());
        chkCreateStructures.setText("Try to create structures on destination");
        chkCreateStructures.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        pnlLinkOptions.add(chkCreateStructures, gridBagConstraints);
        chkCopyData.setText("Copy data");
        chkCopyData.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlLinkOptions.add(chkCopyData, gridBagConstraints);
        chkIncludeDependencies.setText("Include dependencies");
        chkIncludeDependencies.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        pnlLinkOptions.add(chkIncludeDependencies, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlLinkSetup.add(pnlLinkOptions, gridBagConstraints);
        setMinimumSize(new java.awt.Dimension(550, 550));
        setPreferredSize(new java.awt.Dimension(550, 550));
        setLayout(new java.awt.GridBagLayout());
        txtExplain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtExplain.setContentType("text/html");
        txtExplain.setEditable(false);
        txtExplain.setText("<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <h2>Db copy tool...</h2>\n    <p style=\"margin-top: 0\">\r\nThis tool allows you to import/export and copy database structure and data to various targets.<br/>\nYou create links between the source and destination, then you can save the setup for future use.      \r\n    </p>\r\n  </body>\r\n</html>\r\n");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(txtExplain, gridBagConstraints);
        pnlMain.setLayout(new java.awt.GridBagLayout());
        mainToolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainToolBar.setFloatable(false);
        mainToolBar.setRollover(true);
        btnLoadSetup.setText("Load setup");
        btnLoadSetup.setToolTipText("Loads a previously saved global copy setup.");
        btnLoadSetup.setFocusable(false);
        btnLoadSetup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLoadSetup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mainToolBar.add(btnLoadSetup);
        btnSaveSetup.setText("Save setup");
        btnSaveSetup.setToolTipText("Saves the full set of defined links into a global setup.");
        btnSaveSetup.setFocusable(false);
        btnSaveSetup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveSetup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mainToolBar.add(btnSaveSetup);
        mainToolBar.add(jSeparator1);
        btnNewLink.setText("New link");
        btnNewLink.setToolTipText("Creates a new link from source and destination setttings.");
        btnNewLink.setFocusable(false);
        btnNewLink.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewLink.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewLink.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewLinkActionPerformed(evt);
            }
        });
        mainToolBar.add(btnNewLink);
        btnRemoveLink.setText("Remove link");
        btnRemoveLink.setToolTipText("Removes the selected link from the global setup.");
        btnRemoveLink.setFocusable(false);
        btnRemoveLink.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveLink.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveLink.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveLinkActionPerformed(evt);
            }
        });
        mainToolBar.add(btnRemoveLink);
        mainToolBar.add(jSeparator2);
        btnNewMapping.setText("New mapping");
        btnNewMapping.setFocusable(false);
        btnNewMapping.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNewMapping.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNewMapping.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewMappingActionPerformed(evt);
            }
        });
        mainToolBar.add(btnNewMapping);
        btnRemoveMapping.setText("Remove mapping");
        btnRemoveMapping.setFocusable(false);
        btnRemoveMapping.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveMapping.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveMapping.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveMappingActionPerformed(evt);
            }
        });
        mainToolBar.add(btnRemoveMapping);
        mainToolBar.add(jSeparator3);
        btnCopyFullDatabase.setText("Full database");
        btnCopyFullDatabase.setFocusable(false);
        btnCopyFullDatabase.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCopyFullDatabase.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCopyFullDatabase.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyFullDatabaseActionPerformed(evt);
            }
        });
        mainToolBar.add(btnCopyFullDatabase);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlMain.add(mainToolBar, gridBagConstraints);
        splitMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitMain.setDividerLocation(350);
        splitMain.setDoubleBuffered(true);
        splitMain.setOneTouchExpandable(true);
        scrollTaskpaneContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollTaskpaneContainer.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTaskpaneContainer.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTaskpaneContainer.setDoubleBuffered(true);
        taskpaneContainer.setScrollableTracksViewportHeight(true);
        taskPaneDefinedLinks.setTitle("Defined links");
        taskpaneContainer.add(taskPaneDefinedLinks);
        taskPaneSetupSource.setTitle("Source setup");
        taskpaneContainer.add(taskPaneSetupSource);
        taskPaneDestinationSetup.setTitle("Destination setup");
        taskpaneContainer.add(taskPaneDestinationSetup);
        scrollTaskpaneContainer.setViewportView(taskpaneContainer);
        splitMain.setLeftComponent(scrollTaskpaneContainer);
        scrollLinkEditor.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollLinkEditor.setPreferredSize(new java.awt.Dimension(230, 200));
        tableLinkEditor.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Source column", "Destination column" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { true, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        scrollLinkEditor.setViewportView(tableLinkEditor);
        splitMain.setRightComponent(scrollLinkEditor);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlMain.add(splitMain, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(pnlMain, gridBagConstraints);
        mainStatusBar.setDoubleBuffered(true);
        mainStatusBar.setResizeHandleEnabled(true);
        mainStatusBar.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        mainStatusBar.add(progressbar, gridBagConstraints);
        pnlCommands.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pnlCommands.setMinimumSize(new java.awt.Dimension(165, 23));
        pnlCommands.setOpaque(false);
        pnlCommands.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(75, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        pnlCommands.add(btnClose);
        btnProceed.setText("Proceed");
        btnProceed.setToolTipText("Executes the copy according to the setup currently loaded");
        btnProceed.setPreferredSize(new java.awt.Dimension(75, 23));
        pnlCommands.add(btnProceed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        mainStatusBar.add(pnlCommands, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(mainStatusBar, gridBagConstraints);
    }

    /**
     * Permet de choisir un fichier source.
     * @param evt
     */
    private void btnBrowseSrcFileActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        int resp = chooser.showOpenDialog(UserDefinedDbCopyPanel.this);
        if (resp != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selectedFile = chooser.getSelectedFile();
        txtSrcFileName.setText(selectedFile.getAbsolutePath());
    }

    /**
     * Active ou pas les controles pour le selecteur de séparateur dans le fichier source.
     * @param evt
     */
    private void radioSrcFileNameItemStateChanged(java.awt.event.ItemEvent evt) {
        txtSrcFileName.setEnabled(radioSrcFileName.isSelected());
        btnBrowseSrcFile.setEnabled(radioSrcFileName.isSelected());
        radioSrcSeparatorTab.setEnabled(radioSrcFileName.isSelected());
        radioSrcSeparatorUserDef.setEnabled(radioSrcFileName.isSelected());
        txtSrcSeparatorUserDef.setEnabled(radioSrcFileName.isSelected());
        chkSrcFileDropHeaders.setEnabled(radioSrcFileName.isSelected());
        cmbSrcConnection.setEnabled(!radioSrcFileName.isSelected());
        cmbSrcTable.setEnabled(!radioSrcFileName.isSelected());
    }

    /**
     * Active ou pas les controles pour le selecteur de séparateur dans le fichier destination.
     * @param evt
     */
    private void radioDestFileNameItemStateChanged(java.awt.event.ItemEvent evt) {
        txtDestFileName.setEnabled(radioDestFileName.isSelected());
        btnBrowseDestFile.setEnabled(radioDestFileName.isSelected());
        radioDestSeparatorTab.setEnabled(radioDestFileName.isSelected());
        radioDestSepatatorUserDef.setEnabled(radioDestFileName.isSelected());
        txtDestSeparatorUserDef.setEnabled(radioDestFileName.isSelected());
        chkDestFileDropHeaders.setEnabled(radioDestFileName.isSelected());
        chkDestFileAppendMode.setEnabled(radioDestFileName.isSelected());
        cmbDestConnection.setEnabled(!radioDestFileName.isSelected());
    }

    /**
     * Permet de fermer la fenétre parent de ce panel si elle existe.
     * @param evt
     * @since 2.0.0
     */
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        Window win = SwingUtilities.getWindowAncestor(UserDefinedDbCopyPanel.this);
        if (win != null) {
            win.setVisible(false);
            win.dispose();
        }
    }

    /**
     * Il faut charger la liste des tables sauf si on est en train de mettre à jour
     * la liste des connexions (combo disabled)
     * @param evt
     */
    private void cmbSrcConnectionItemStateChanged(java.awt.event.ItemEvent evt) {
        if (!cmbSrcConnection.isEnabled()) {
            return;
        }
        if (!cmbSrcTable.isEnabled()) {
            return;
        }
        final String conName = (String) cmbSrcConnection.getSelectedItem();
        logger.debug("Loading tables from source connection " + conName);
        cmbSrcTable.setEnabled(false);
        cmbSrcTable.removeAllItems();
        final LoadTablesThread loadTableThread = new LoadTablesThread(conName);
        MonitorThread monitorThread = new MonitorThread(loadTableThread) {

            @Override
            public void initialize() {
                Runnable myRun = new Runnable() {

                    @Override
                    public void run() {
                        progressbar.setValue(0);
                        progressbar.setMaximum(1);
                        progressbar.setStringPainted(true);
                        progressbar.setString("Loading tables for connection: " + conName);
                    }
                };
                try {
                    SwingUtilities.invokeAndWait(myRun);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void update(int taskCount, final int taskNum, String generalDescription, String taskDescription) {
                Runnable myRun = new Runnable() {

                    @Override
                    public void run() {
                        progressbar.setValue(taskNum);
                    }
                };
                try {
                    SwingUtilities.invokeAndWait(myRun);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void cleanup() {
                Runnable runDisplayTables = new Runnable() {

                    @Override
                    public void run() {
                        progressbar.setString("Finished loading tables for connection: " + conName);
                        List<String> tableList = loadTableThread.getTableList();
                        cmbSrcTable.removeAllItems();
                        if (tableList == null) {
                            return;
                        }
                        for (String tableName : tableList) {
                            cmbSrcTable.addItem(tableName);
                        }
                        cmbSrcTable.setEnabled(true);
                    }
                };
                try {
                    SwingUtilities.invokeAndWait(runDisplayTables);
                } catch (Exception ignored) {
                }
            }
        };
        monitorThread.start();
        loadTableThread.start();
    }

    /**
     * Permet de créer un nouveau mapping et de l'ajouter dans la liste
     * des mapping prédéfinis.
     * @param evt ActionEvent qui a déclenché cette méthode.
     * @since 2.0.0
     */
    private void btnNewLinkActionPerformed(java.awt.event.ActionEvent evt) {
        DbLink link = SetupFullDbCopyThread.createDbLink((String) cmbSrcConnection.getSelectedItem(), (String) cmbSrcTable.getSelectedItem(), (String) cmbDestConnection.getSelectedItem());
        if (link == null) {
            return;
        }
        addDbLink(link);
    }

    private void btnRemoveLinkActionPerformed(java.awt.event.ActionEvent evt) {
        int selection = listDefinedLinks.getSelectedIndex();
        if (selection < 0) {
            return;
        }
        DbLink link = (DbLink) listDefinedLinks.getSelectedValue();
        int confirm = JOptionPane.showConfirmDialog(UserDefinedDbCopyPanel.this, "Are you sure you want to remove the link named:\n'" + link.getName() + "'?", "Please confirm...", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        DefaultListModel model = (DefaultListModel) listDefinedLinks.getModel();
        model.removeElement(link);
        if (!model.isEmpty()) {
            if (selection > 0) {
                selection--;
            }
            listDefinedLinks.setSelectionInterval(selection, selection);
        }
    }

    /**
     * Provoque l'affichage du lien actuellement sélectionné.
     * @param evt ListSelectionEvent qui a déclenché cette méthode.
     * @since 2.0.0
     */
    private void listDefinedLinksValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) return;
        DbLink link = (DbLink) listDefinedLinks.getSelectedValue();
        showDbLink(link);
    }

    /**
     * Ajoute une nouvelle correspondance de colonne dans le DbLink actuellement affiché.
     * @param evt ActionEvent qui a déclenché cette méthode.
     * @since 2.0.0
     */
    private void btnNewMappingActionPerformed(java.awt.event.ActionEvent evt) {
        DbLink currentDbLink = (DbLink) listDefinedLinks.getSelectedValue();
        if (currentDbLink == null) {
            return;
        }
        String newDestColName = JOptionPane.showInputDialog(UserDefinedDbCopyPanel.this, "Please enter the name of the DESTINATION column:", "New destination...", JOptionPane.QUESTION_MESSAGE);
        if ((newDestColName == null) || "".equals(newDestColName)) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableLinkEditor.getModel();
        for (int cpt = 0; cpt < model.getRowCount(); cpt++) {
            String destName = (String) model.getValueAt(cpt, 1);
            if (destName.equals(newDestColName)) {
                JOptionPane.showMessageDialog(UserDefinedDbCopyPanel.this, "The destination column name '" + newDestColName + "' already exists!", "Please choose another name...", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        currentDbLink.getDestinationMapping().put(newDestColName, (String) tableLinkCellEditor.box.getItemAt(0));
        showDbLink(currentDbLink);
    }

    /**
     * Permet de retirer une ligne du mapping actuellement affiché à l'écran.
     * @param evt ActionEvent qui a déclenché cette méthode.
     * @since 2.0.0
     */
    private void btnRemoveMappingActionPerformed(java.awt.event.ActionEvent evt) {
        tableLinkCellEditor.cancelCellEditing();
        DbLink link = (DbLink) listDefinedLinks.getSelectedValue();
        if (link == null) {
            return;
        }
        int selection = tableLinkEditor.getSelectedRow();
        if (selection < 0) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tableLinkEditor.getModel();
        String srcCol = (String) model.getValueAt(selection, 0);
        String destCol = (String) model.getValueAt(selection, 1);
        int confirm = JOptionPane.showConfirmDialog(UserDefinedDbCopyPanel.this, "Please confirm that you want to remove mapping for:\n" + srcCol + " to " + destCol + " ?", "Please confirm...", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        model.removeRow(selection);
        link.getDestinationMapping().remove(destCol);
    }

    private void btnCopyFullDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
        if (!radioSrcTable.isSelected()) {
            JOptionPane.showMessageDialog(UserDefinedDbCopyPanel.this, "You should select a source that is a database.", "Hey!!!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!radioDestTable.isSelected()) {
            int confirm = JOptionPane.showConfirmDialog(UserDefinedDbCopyPanel.this, "Are you sure you want to export the full database into a file as a destination?\n" + "This could produce a HUGE file !", "Warning...", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }
        String srcCon = (String) cmbSrcConnection.getSelectedItem();
        String destCon = (String) cmbDestConnection.getSelectedItem();
        if (srcCon.equals(destCon)) {
            JOptionPane.showMessageDialog(UserDefinedDbCopyPanel.this, "Source and destination connections are the same !", "Cannot copy...", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(UserDefinedDbCopyPanel.this, "Are you sure you wan to setup the FULL DB copy ?\n" + "From " + srcCon + " to " + destCon + "\n" + "This operation can be very long on slow dataservers.", "Please confirm...", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        List<String> srcTables = new ArrayList<String>();
        for (int cpt = 0; cpt < cmbSrcTable.getItemCount(); cpt++) {
            srcTables.add((String) cmbSrcTable.getItemAt(cpt));
        }
        final SetupFullDbCopyThread setupThread = new SetupFullDbCopyThread(srcCon, srcTables, destCon);
        MonitorThread monitorThread = new MonitorThread(setupThread) {

            @Override
            public void initialize() {
                Runnable run = new Runnable() {

                    @Override
                    public void run() {
                        progressbar.setValue(0);
                        progressbar.setMaximum(cmbSrcTable.getItemCount());
                        progressbar.setStringPainted(true);
                        progressbar.setString("init...");
                    }
                };
                SwingUtilities.invokeLater(run);
            }

            @Override
            public void update(final int taskCount, final int taskNum, final String generalDescription, final String taskDescription) {
                Runnable run = new Runnable() {

                    @Override
                    public void run() {
                        progressbar.setValue(taskNum);
                        progressbar.setMaximum(taskCount);
                        progressbar.setString(generalDescription + " / " + taskDescription);
                    }
                };
                SwingUtilities.invokeLater(run);
            }

            @Override
            public void cleanup() {
                Runnable displayRun = new Runnable() {

                    @Override
                    public void run() {
                        progressbar.setString("Finished creating full db copy setup.");
                        List<DbLink> links = ((SetupFullDbCopyThread) monitoredThread).getLinks();
                        if (links.size() > 0) {
                            DefaultListModel model = (DefaultListModel) listDefinedLinks.getModel();
                            for (DbLink myLink : links) {
                                model.addElement(myLink);
                            }
                        }
                    }
                };
                SwingUtilities.invokeLater(displayRun);
            }
        };
        monitorThread.start();
        setupThread.start();
    }

    private void btnBrowseDestFileActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showOpenDialog(UserDefinedDbCopyPanel.this);
        if (ret != JFileChooser.APPROVE_OPTION) return;
        File selectedFile = chooser.getSelectedFile();
        txtDestFileName.setText(selectedFile.getPath());
    }

    public JComboBox getCmbSrcTable() {
        return cmbSrcTable;
    }

    private javax.swing.JButton btnBrowseDestFile;

    private javax.swing.JButton btnBrowseSrcFile;

    private javax.swing.JButton btnClose;

    private javax.swing.JButton btnCopyFullDatabase;

    private javax.swing.ButtonGroup btnGrpRadioDest;

    private javax.swing.ButtonGroup btnGrpRadioDestSeparator;

    private javax.swing.ButtonGroup btnGrpRadioSrc;

    private javax.swing.ButtonGroup btnGrpRadioSrcSeparator;

    private javax.swing.JButton btnLoadSetup;

    private javax.swing.JButton btnNewLink;

    private javax.swing.JButton btnNewMapping;

    private javax.swing.JButton btnProceed;

    private javax.swing.JButton btnRemoveLink;

    private javax.swing.JButton btnRemoveMapping;

    private javax.swing.JButton btnSaveSetup;

    private javax.swing.JCheckBox chkCopyData;

    private javax.swing.JCheckBox chkCreateStructures;

    private javax.swing.JCheckBox chkDestFileAppendMode;

    private javax.swing.JCheckBox chkDestFileDropHeaders;

    private javax.swing.JCheckBox chkIncludeDependencies;

    private javax.swing.JCheckBox chkSrcFileDropHeaders;

    private javax.swing.JComboBox cmbDestConnection;

    private javax.swing.JComboBox cmbSrcConnection;

    private javax.swing.JComboBox cmbSrcTable;

    private javax.swing.JToolBar.Separator jSeparator1;

    private javax.swing.JToolBar.Separator jSeparator2;

    private javax.swing.JToolBar.Separator jSeparator3;

    private javax.swing.JLabel lblDestFieldSeparator;

    private javax.swing.JLabel lblSrcFieldSeparator;

    private org.jdesktop.swingx.JXList listDefinedLinks;

    private org.jdesktop.swingx.JXStatusBar mainStatusBar;

    private javax.swing.JToolBar mainToolBar;

    private javax.swing.JPanel pnlCommands;

    private org.jdesktop.swingx.JXPanel pnlDatabaseTable;

    private org.jdesktop.swingx.JXPanel pnlDestFieldSeparator;

    private org.jdesktop.swingx.JXPanel pnlDestFileFormat;

    private org.jdesktop.swingx.JXPanel pnlDestSetup;

    private org.jdesktop.swingx.JXPanel pnlLinkOptions;

    private org.jdesktop.swingx.JXPanel pnlLinkSetup;

    private org.jdesktop.swingx.JXPanel pnlMain;

    private org.jdesktop.swingx.JXPanel pnlSourceSetup;

    private org.jdesktop.swingx.JXPanel pnlSrcFieldSeparator;

    private javax.swing.JProgressBar progressbar;

    private javax.swing.JRadioButton radioDestFileName;

    private javax.swing.JRadioButton radioDestSeparatorTab;

    private javax.swing.JRadioButton radioDestSepatatorUserDef;

    private javax.swing.JRadioButton radioDestTable;

    private javax.swing.JRadioButton radioSrcFileName;

    private javax.swing.JRadioButton radioSrcSeparatorTab;

    private javax.swing.JRadioButton radioSrcSeparatorUserDef;

    private javax.swing.JRadioButton radioSrcTable;

    private javax.swing.JScrollPane scrollLinkEditor;

    private javax.swing.JScrollPane scrollTaskpaneContainer;

    private javax.swing.JScrollPane scrolldefinedLinks;

    private javax.swing.JSplitPane splitMain;

    private org.jdesktop.swingx.JXTable tableLinkEditor;

    private org.jdesktop.swingx.JXTaskPane taskPaneDefinedLinks;

    private org.jdesktop.swingx.JXTaskPane taskPaneDestinationSetup;

    private org.jdesktop.swingx.JXTaskPane taskPaneSetupSource;

    private org.jdesktop.swingx.JXTaskPaneContainer taskpaneContainer;

    private javax.swing.JTextField txtDestFileName;

    private javax.swing.JTextField txtDestSeparatorUserDef;

    private javax.swing.JTextPane txtExplain;

    private javax.swing.JTextField txtSrcFileName;

    private javax.swing.JTextField txtSrcSeparatorUserDef;
}
