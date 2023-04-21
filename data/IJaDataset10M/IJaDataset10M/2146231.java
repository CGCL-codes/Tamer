package KFramework30.Widgets.DataBrowser.UI;

import KFramework30.Widgets.DataBrowser.UI.customOrderClass;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import KFramework30.Base.*;
import KFramework30.Widgets.KDataBrowserBaseClass;

public class setOrderDialogClass extends javax.swing.JDialog implements ListSelectionListener {

    private KConfigurationClass configuration;

    private KLogClass log;

    private java.awt.Window parentWindow;

    private customOrderClass listFiller;

    /** Creates new form setOrderDialogClass */
    public setOrderDialogClass(KConfigurationClass configurationParam, KLogClass logParam, java.awt.Window parentWindowParam, KDataBrowserBaseClass tableFillerParam) throws KExceptionClass {
        super(parentWindowParam, java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        configuration = configurationParam;
        log = logParam;
        parentWindow = parentWindowParam;
        initComponents();
        pack();
        setSize(458, 293);
        KMetaUtilsClass.centerInScreen(this);
        listFiller = new customOrderClass(configuration, log, sourceList, destinationList, tableFillerParam);
        listFiller.fillList();
        destinationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sourceList.addListSelectionListener(this);
        destinationList.addListSelectionListener(this);
        if (sourceList.getModel().getSize() != 0) sourceList.setSelectedIndex(0);
        setButtonStates();
    }

    private void initComponents() {
        jLayeredPane1 = new javax.swing.JLayeredPane();
        setOrderPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        scrollerFrom = new javax.swing.JScrollPane();
        sourceList = new javax.swing.JList();
        scrollerTo = new javax.swing.JScrollPane();
        destinationList = new javax.swing.JList();
        ToRight = new javax.swing.JButton();
        ToLeft = new javax.swing.JButton();
        up = new javax.swing.JButton();
        down = new javax.swing.JButton();
        clearAll = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        OK = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sorting");
        setFont(new java.awt.Font("Dialog", 0, 10));
        setModal(true);
        setName("Set order");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        setOrderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sort", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10)));
        jLabel2.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel2.setText("Available Columns");
        jLabel3.setFont(new java.awt.Font("Arial", 0, 10));
        jLabel3.setText("Sort");
        sourceList.setFont(new java.awt.Font("Arial", 0, 10));
        scrollerFrom.setViewportView(sourceList);
        scrollerTo.setFont(new java.awt.Font("Arial", 0, 10));
        destinationList.setFont(new java.awt.Font("Arial", 0, 10));
        scrollerTo.setViewportView(destinationList);
        ToRight.setFont(new java.awt.Font("Arial", 0, 10));
        ToRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toright1.jpg")));
        ToRight.setText("Add");
        ToRight.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ToRight.setMargin(new java.awt.Insets(2, 2, 2, 2));
        ToRight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToRight.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToRightActionPerformed(evt);
            }
        });
        ToLeft.setFont(new java.awt.Font("Arial", 0, 10));
        ToLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/toleft1.jpg")));
        ToLeft.setText("Remove");
        ToLeft.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ToLeft.setMargin(new java.awt.Insets(2, 2, 2, 2));
        ToLeft.setMinimumSize(new java.awt.Dimension(50, 50));
        ToLeft.setPreferredSize(new java.awt.Dimension(50, 50));
        ToLeft.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToLeft.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToLeftActionPerformed(evt);
            }
        });
        up.setFont(new java.awt.Font("Arial", 0, 10));
        up.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/up1.jpg")));
        up.setText("Move Up");
        up.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        up.setMargin(new java.awt.Insets(2, 2, 2, 2));
        up.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        up.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upActionPerformed(evt);
            }
        });
        down.setFont(new java.awt.Font("Arial", 0, 10));
        down.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/down1.jpg")));
        down.setText("Move Down");
        down.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        down.setMargin(new java.awt.Insets(1, 1, 1, 1));
        down.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        down.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downActionPerformed(evt);
            }
        });
        clearAll.setFont(new java.awt.Font("Arial", 0, 10));
        clearAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/remove1.jpg")));
        clearAll.setText("Clear");
        clearAll.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        clearAll.setMargin(new java.awt.Insets(2, 2, 2, 2));
        clearAll.setMaximumSize(new java.awt.Dimension(50, 50));
        clearAll.setMinimumSize(new java.awt.Dimension(50, 50));
        clearAll.setPreferredSize(new java.awt.Dimension(50, 50));
        clearAll.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        clearAll.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout setOrderPanelLayout = new org.jdesktop.layout.GroupLayout(setOrderPanel);
        setOrderPanel.setLayout(setOrderPanelLayout);
        setOrderPanelLayout.setHorizontalGroup(setOrderPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(setOrderPanelLayout.createSequentialGroup().add(4, 4, 4).add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(100, 100, 100).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(setOrderPanelLayout.createSequentialGroup().add(4, 4, 4).add(scrollerFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(setOrderPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ToRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(ToLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(clearAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(3, 3, 3).add(scrollerTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(7, 7, 7).add(setOrderPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(up, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(down, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))));
        setOrderPanelLayout.setVerticalGroup(setOrderPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(setOrderPanelLayout.createSequentialGroup().add(7, 7, 7).add(setOrderPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(setOrderPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(scrollerFrom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(setOrderPanelLayout.createSequentialGroup().add(5, 5, 5).add(ToRight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(ToLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(clearAll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(scrollerTo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(setOrderPanelLayout.createSequentialGroup().add(25, 25, 25).add(up, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(down, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))));
        setOrderPanel.setBounds(5, 5, 440, 210);
        jLayeredPane1.add(setOrderPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Cancel.setFont(new java.awt.Font("Arial", 0, 10));
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });
        Cancel.setBounds(360, 220, 80, 20);
        jLayeredPane1.add(Cancel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        OK.setFont(new java.awt.Font("Arial", 0, 10));
        OK.setText("Ok");
        OK.setName("");
        OK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKActionPerformed(evt);
            }
        });
        OK.setBounds(270, 220, 80, 20);
        jLayeredPane1.add(OK, javax.swing.JLayeredPane.DEFAULT_LAYER);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 450, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
    }

    private void clearAllActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            DefaultListModel sourceListModel = (DefaultListModel) sourceList.getModel();
            DefaultListModel destinationListModel = (DefaultListModel) destinationList.getModel();
            int size = destinationListModel.size();
            for (int i = 0; i < size; i++) sourceListModel.addElement(destinationListModel.get(i));
            destinationListModel.clear();
            setButtonStates();
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
    }

    private void OKActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            listFiller.refreshTable();
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
        ;
        setVisible(false);
        dispose();
    }

    private void downActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            DefaultListModel destinationListModel = (DefaultListModel) destinationList.getModel();
            if (!destinationList.isSelectionEmpty()) {
                int index = destinationList.getSelectedIndex();
                if (index != destinationListModel.getSize() - 1) {
                    destinationListModel.insertElementAt(destinationList.getSelectedValue(), index + 2);
                    destinationList.setSelectedIndex(index + 2);
                    destinationListModel.remove(index);
                }
            }
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
    }

    private void setButtonStates() {
        if (sourceList.isSelectionEmpty()) ToRight.setEnabled(false); else ToRight.setEnabled(true);
        if (destinationList.isSelectionEmpty()) {
            ToLeft.setEnabled(false);
            up.setEnabled(false);
            down.setEnabled(false);
        } else {
            ToLeft.setEnabled(true);
            up.setEnabled(true);
            down.setEnabled(true);
        }
        if (((DefaultListModel) destinationList.getModel()).size() == 0) {
            clearAll.setEnabled(false);
        } else {
            clearAll.setEnabled(true);
        }
    }

    private void ToLeftActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int index = 0;
            DefaultListModel sourceListModel = (DefaultListModel) sourceList.getModel();
            DefaultListModel destinationListModel = (DefaultListModel) destinationList.getModel();
            while (!destinationList.isSelectionEmpty()) {
                index = destinationList.getSelectedIndex();
                sourceListModel.addElement(destinationList.getSelectedValue());
                int size = sourceListModel.getSize();
                sourceList.setSelectedIndex(size - 1);
                destinationListModel.remove(index);
            }
            int size = destinationListModel.getSize();
            if (size != 0) {
                if (index == size) index--;
                destinationList.setSelectedIndex(index);
            }
            setButtonStates();
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
    }

    private void upActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            DefaultListModel destinationListModel = (DefaultListModel) destinationList.getModel();
            if (!destinationList.isSelectionEmpty()) {
                int index = destinationList.getSelectedIndex();
                if (index != 0) {
                    destinationListModel.insertElementAt(destinationList.getSelectedValue(), index - 1);
                    destinationListModel.remove(index + 1);
                    destinationList.setSelectedIndex(index - 1);
                }
            }
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
    }

    private void ToRightActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int index = 0;
            DefaultListModel sourceListModel = (DefaultListModel) sourceList.getModel();
            DefaultListModel destinationListModel = (DefaultListModel) destinationList.getModel();
            while (!sourceList.isSelectionEmpty()) {
                index = sourceList.getSelectedIndex();
                destinationListModel.addElement(sourceList.getSelectedValue());
                int size = destinationListModel.getSize();
                destinationList.setSelectedIndex(size - 1);
                sourceListModel.remove(index);
            }
            int size = sourceListModel.getSize();
            if (size != 0) {
                if (index == size) index--;
                sourceList.setSelectedIndex(index);
            }
            setButtonStates();
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
    }

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        dispose();
    }

    private void toLeftActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void toRightActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        dispose();
    }

    private javax.swing.JButton Cancel;

    private javax.swing.JButton OK;

    private javax.swing.JButton ToLeft;

    private javax.swing.JButton ToRight;

    private javax.swing.JButton clearAll;

    private javax.swing.JList destinationList;

    private javax.swing.JButton down;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLayeredPane jLayeredPane1;

    private javax.swing.JScrollPane scrollerFrom;

    private javax.swing.JScrollPane scrollerTo;

    private javax.swing.JPanel setOrderPanel;

    private javax.swing.JList sourceList;

    private javax.swing.JButton up;

    /** Process for lists selectoin change */
    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        setButtonStates();
    }
}
