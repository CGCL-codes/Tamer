package KFramework30.Widgets;

import javax.swing.*;
import java.util.*;
import KFramework30.Base.*;
import KFramework30.Base.*;

public class selectDialogClass extends javax.swing.JDialog {

    private KConfigurationClass configuration;

    private KLogClass log;

    private KDataBrowserBaseClass browser = null;

    private java.awt.Window parentWindow;

    private long parent_id = -1;

    private Vector selectedKeys;

    /** Creates new form selectParentDialogClass */
    public selectDialogClass(KConfigurationClass configurationParam, KLogClass logParam, java.awt.Window parentWindowParam, KDataBrowserBaseClass browserParam, String title) throws KExceptionClass {
        super(parentWindowParam, java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        initComponents();
        pack();
        KMetaUtilsClass.centerInScreen(this);
        configuration = configurationParam;
        log = logParam;
        parentWindow = parentWindowParam;
        browser = browserParam;
        browser.setDoubleClickEnabled(false);
        selectedKeys = new Vector();
        setTitle(title);
        textLabel.setText(title);
        Panel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), title, 4, 2, new java.awt.Font("Arial", 0, 10), java.awt.Color.black));
        browser.getJTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(browser.getJTable());
        newButton.addActionListener(browser);
        editButton.addActionListener(browser);
        deleteButton.addActionListener(browser);
        sortButton.addActionListener(browser);
        filterButton.addActionListener(browser);
        printButton.addActionListener(browser);
        refreshButton.addActionListener(browser);
        log.log(this, "Select Parent Window opened sucessfully.");
    }

    public void setTableSingleSelection() {
        browser.getJTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void initComponents() {
        jLayeredPane1 = new javax.swing.JLayeredPane();
        textLabel = new javax.swing.JLabel();
        topLabel1 = new javax.swing.JLabel();
        Panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        newButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        sortButton = new javax.swing.JButton();
        filterButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        printButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        selectButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        textLabel.setFont(new java.awt.Font("Verdana", 2, 24));
        textLabel.setForeground(new java.awt.Color(255, 255, 255));
        textLabel.setBounds(20, 10, 940, 35);
        jLayeredPane1.add(textLabel, new Integer(5));
        topLabel1.setFont(new java.awt.Font("Dialog", 1, 24));
        topLabel1.setForeground(java.awt.Color.white);
        topLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/topBar.jpg")));
        topLabel1.setBounds(0, 0, 1024, 55);
        jLayeredPane1.add(topLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Panel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Selección", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10)));
        Panel.setFont(new java.awt.Font("Arial", 0, 10));
        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 10));
        jToolBar1.setFloatable(false);
        jToolBar1.setMinimumSize(new java.awt.Dimension(68, 41));
        jToolBar1.setPreferredSize(new java.awt.Dimension(18, 45));
        jToolBar1.add(jSeparator2);
        newButton.setFont(new java.awt.Font("Verdana", 0, 10));
        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sort1.jpg")));
        newButton.setText("new");
        newButton.setToolTipText("Sort table");
        newButton.setFocusable(false);
        newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newButton.setMaximumSize(new java.awt.Dimension(50, 50));
        newButton.setMinimumSize(new java.awt.Dimension(50, 50));
        newButton.setPreferredSize(new java.awt.Dimension(50, 50));
        newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(newButton);
        editButton.setFont(new java.awt.Font("Verdana", 0, 10));
        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sort1.jpg")));
        editButton.setText("edit");
        editButton.setToolTipText("Sort table");
        editButton.setFocusable(false);
        editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editButton.setMaximumSize(new java.awt.Dimension(50, 50));
        editButton.setMinimumSize(new java.awt.Dimension(50, 50));
        editButton.setPreferredSize(new java.awt.Dimension(50, 50));
        editButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(editButton);
        deleteButton.setFont(new java.awt.Font("Verdana", 0, 10));
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sort1.jpg")));
        deleteButton.setText("delete");
        deleteButton.setToolTipText("Sort table");
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setMaximumSize(new java.awt.Dimension(50, 50));
        deleteButton.setMinimumSize(new java.awt.Dimension(50, 50));
        deleteButton.setPreferredSize(new java.awt.Dimension(50, 50));
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(deleteButton);
        jToolBar1.add(jSeparator4);
        sortButton.setFont(new java.awt.Font("Verdana", 0, 10));
        sortButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sort1.jpg")));
        sortButton.setText("sort");
        sortButton.setToolTipText("Sort table");
        sortButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sortButton.setMaximumSize(new java.awt.Dimension(50, 50));
        sortButton.setMinimumSize(new java.awt.Dimension(50, 50));
        sortButton.setPreferredSize(new java.awt.Dimension(50, 50));
        sortButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(sortButton);
        filterButton.setFont(new java.awt.Font("Verdana", 0, 10));
        filterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/filter1.jpg")));
        filterButton.setText("filter");
        filterButton.setToolTipText("Filter table");
        filterButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        filterButton.setMaximumSize(new java.awt.Dimension(50, 50));
        filterButton.setMinimumSize(new java.awt.Dimension(50, 50));
        filterButton.setPreferredSize(new java.awt.Dimension(50, 50));
        filterButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(filterButton);
        jToolBar1.add(jSeparator1);
        printButton.setFont(new java.awt.Font("Verdana", 0, 10));
        printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/print1.jpg")));
        printButton.setText("Print");
        printButton.setActionCommand("print");
        printButton.setFocusable(false);
        printButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        printButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        printButton.setMaximumSize(new java.awt.Dimension(60, 50));
        printButton.setMinimumSize(new java.awt.Dimension(60, 50));
        printButton.setPreferredSize(new java.awt.Dimension(60, 50));
        printButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        printButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(printButton);
        refreshButton.setFont(new java.awt.Font("Verdana", 0, 10));
        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/refresh1.jpg")));
        refreshButton.setText("Refresh");
        refreshButton.setActionCommand("refresh");
        refreshButton.setFocusable(false);
        refreshButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        refreshButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        refreshButton.setMaximumSize(new java.awt.Dimension(60, 50));
        refreshButton.setMinimumSize(new java.awt.Dimension(60, 50));
        refreshButton.setPreferredSize(new java.awt.Dimension(60, 50));
        refreshButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(refreshButton);
        jToolBar1.add(jSeparator3);
        org.jdesktop.layout.GroupLayout PanelLayout = new org.jdesktop.layout.GroupLayout(Panel);
        Panel.setLayout(PanelLayout);
        PanelLayout.setHorizontalGroup(PanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(PanelLayout.createSequentialGroup().add(PanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        PanelLayout.setVerticalGroup(PanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(PanelLayout.createSequentialGroup().add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(5, 5, 5).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 330, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        Panel.setBounds(8, 55, 500, 410);
        jLayeredPane1.add(Panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        selectButton.setFont(new java.awt.Font("Arial", 0, 10));
        selectButton.setText("Select");
        selectButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        selectButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        selectButton.setBounds(300, 470, 100, 20);
        jLayeredPane1.add(selectButton, new Integer(4));
        cancelButton.setFont(new java.awt.Font("Arial", 0, 10));
        cancelButton.setText("Cancel");
        cancelButton.setMargin(new java.awt.Insets(2, 6, 2, 6));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        cancelButton.setBounds(400, 470, 100, 20);
        jLayeredPane1.add(cancelButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 515, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 500, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            parent_id = browser.getSelectedRowKey();
            selectedKeys = browser.getMultiSelectedRowKeys();
            setVisible(false);
            dispose();
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JPanel Panel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton deleteButton;

    private javax.swing.JButton editButton;

    private javax.swing.JButton filterButton;

    private javax.swing.JLayeredPane jLayeredPane1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JToolBar.Separator jSeparator1;

    private javax.swing.JToolBar.Separator jSeparator2;

    private javax.swing.JToolBar.Separator jSeparator3;

    private javax.swing.JToolBar.Separator jSeparator4;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JButton newButton;

    private javax.swing.JButton printButton;

    private javax.swing.JButton refreshButton;

    private javax.swing.JButton selectButton;

    private javax.swing.JButton sortButton;

    private javax.swing.JLabel textLabel;

    private javax.swing.JLabel topLabel1;

    public long showDialog() {
        show();
        return parent_id;
    }

    public Vector showMultiSelectionDialog() {
        show();
        return selectedKeys;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getFilterButton() {
        return filterButton;
    }

    public JButton getNewButton() {
        return newButton;
    }

    public JButton getPrintButton() {
        return printButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JButton getSortButton() {
        return sortButton;
    }
}
