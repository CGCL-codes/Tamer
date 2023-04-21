package in.raster.mayam.form;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.delegate.DicomServerDelegate;
import in.raster.mayam.delegate.EchoService;
import in.raster.mayam.delegate.QueryService;
import in.raster.mayam.delegate.MoveDelegate;
import in.raster.mayam.form.dialog.EchoStatus;
import in.raster.mayam.form.dialog.StudyAvailabilityStatus;
import in.raster.mayam.form.display.Display;
import in.raster.mayam.model.AEModel;
import in.raster.mayam.model.StudyModel;
import in.raster.mayam.model.table.ServerTableModel;
import in.raster.mayam.model.table.StudyListModel;
import in.raster.mayam.param.QueryParam;
import in.raster.mayam.util.core.MoveScu;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import org.dcm4che.data.Dataset;
import org.dcm4che.util.DcmURL;
import in.raster.mayam.model.table.renderer.CellRenderer;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class QueryRetrive extends javax.swing.JFrame implements ServerChangeListener, ListSelectionListener {

    /** Creates new form QueryRetrive */
    public QueryRetrive() {
        initComponents();
        refreshModels();
        addSearchDateitemListener();
        addModalityitemListener();
    }

    private void addSearchDateitemListener() {
        SearchDaysHandler searchDaysHandler = new SearchDaysHandler();
        betweenRadio.addItemListener(searchDaysHandler);
        lastmonthRadio.addItemListener(searchDaysHandler);
        lastweekRadio.addItemListener(searchDaysHandler);
        yesterdayRadio.addItemListener(searchDaysHandler);
        todayRadio.addItemListener(searchDaysHandler);
        anydateRadio.addItemListener(searchDaysHandler);
    }

    private void addModalityitemListener() {
        ModalityHandler modalityHandler = new ModalityHandler();
        ctCheckBox.addItemListener(modalityHandler);
        mrCheckBox.addItemListener(modalityHandler);
        xaCheckBox.addItemListener(modalityHandler);
        crCheckBox.addItemListener(modalityHandler);
        scCheckBox.addItemListener(modalityHandler);
        nmCheckBox.addItemListener(modalityHandler);
        rfCheckBox.addItemListener(modalityHandler);
        dxCheckBox.addItemListener(modalityHandler);
        pxCheckBox.addItemListener(modalityHandler);
        usCheckBox.addItemListener(modalityHandler);
        otCheckBox.addItemListener(modalityHandler);
        drCheckBox.addItemListener(modalityHandler);
        srCheckBox.addItemListener(modalityHandler);
        mgCheckBox.addItemListener(modalityHandler);
        rgCheckBox.addItemListener(modalityHandler);
    }

    public void refreshModels() {
        setServerTableModel();
        setServerName();
        setSpinnerDateModel();
    }

    private void setServerTableModel() {
        ServerTableModel serverTableModel = new ServerTableModel();
        serverTableModel.setEditable(false);
        serverTableModel.setData(ApplicationContext.databaseRef.getServerList());
        serverListTable.setModel(serverTableModel);
        serverListTable.getSelectionModel().addListSelectionListener(this);
        serverListTable.getColumnModel().getSelectionModel().addListSelectionListener(this);
        if (this.serverListTable.getRowCount() > 0) {
            serverListTable.setRowSelectionInterval(0, 0);
        }
    }

    private void setSpinnerDateModel() {
        SpinnerDateModel spm1 = new SpinnerDateModel();
        SpinnerDateModel spm2 = new SpinnerDateModel();
        SpinnerDateModel spm3 = new SpinnerDateModel();
        fromSpinner.setModel(spm1);
        fromSpinner.setEditor(new JSpinner.DateEditor(fromSpinner, "dd/MM/yyyy"));
        toSpinner.setModel(spm2);
        toSpinner.setEditor(new JSpinner.DateEditor(toSpinner, "dd/MM/yyyy"));
        birthDateSpinner.setModel(spm3);
        birthDateSpinner.setEditor(new JSpinner.DateEditor(birthDateSpinner, "dd/MM/yyyy"));
    }

    private void initComponents() {
        searchDaysGroup = new javax.swing.ButtonGroup();
        modalityGroup = new javax.swing.ButtonGroup();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        anydateRadio = new javax.swing.JRadioButton();
        todayRadio = new javax.swing.JRadioButton();
        yesterdayRadio = new javax.swing.JRadioButton();
        lastweekRadio = new javax.swing.JRadioButton();
        lastmonthRadio = new javax.swing.JRadioButton();
        betweenRadio = new javax.swing.JRadioButton();
        fromSpinner = new javax.swing.JSpinner();
        toSpinner = new javax.swing.JSpinner();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        patientNameText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        patientIDText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        accessionNoText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        birthDateSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        ctCheckBox = new javax.swing.JCheckBox();
        mrCheckBox = new javax.swing.JCheckBox();
        xaCheckBox = new javax.swing.JCheckBox();
        crCheckBox = new javax.swing.JCheckBox();
        scCheckBox = new javax.swing.JCheckBox();
        nmCheckBox = new javax.swing.JCheckBox();
        rfCheckBox = new javax.swing.JCheckBox();
        dxCheckBox = new javax.swing.JCheckBox();
        pxCheckBox = new javax.swing.JCheckBox();
        usCheckBox = new javax.swing.JCheckBox();
        otCheckBox = new javax.swing.JCheckBox();
        drCheckBox = new javax.swing.JCheckBox();
        srCheckBox = new javax.swing.JCheckBox();
        mgCheckBox = new javax.swing.JCheckBox();
        rgCheckBox = new javax.swing.JCheckBox();
        modalityText = new javax.swing.JTextField();
        serverlistScroll = new javax.swing.JScrollPane();
        serverListTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        verifyButton = new javax.swing.JButton();
        queryButton = new javax.swing.JButton();
        retrieveButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        studyListTable = new javax.swing.JTable();
        serverNameLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        setTitle("Query/Retrieve");
        jPanel9.setMaximumSize(new java.awt.Dimension(1200, 1400));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        searchDaysGroup.add(anydateRadio);
        anydateRadio.setSelected(true);
        anydateRadio.setText("Any date");
        searchDaysGroup.add(todayRadio);
        todayRadio.setText("Today");
        searchDaysGroup.add(yesterdayRadio);
        yesterdayRadio.setText("Yesterday");
        searchDaysGroup.add(lastweekRadio);
        lastweekRadio.setText("Last week");
        searchDaysGroup.add(lastmonthRadio);
        lastmonthRadio.setText("Last month");
        searchDaysGroup.add(betweenRadio);
        betweenRadio.setText("Between");
        fromSpinner.setEnabled(false);
        toSpinner.setEnabled(false);
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(todayRadio).add(anydateRadio).add(yesterdayRadio).add(lastweekRadio).add(lastmonthRadio).add(betweenRadio).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(25, 25, 25).add(fromSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE).add(toSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(anydateRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(todayRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(yesterdayRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lastweekRadio).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lastmonthRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(betweenRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(fromSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(toSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(26, 26, 26)));
        jPanel1Layout.linkSize(new java.awt.Component[] { betweenRadio, yesterdayRadio }, org.jdesktop.layout.GroupLayout.VERTICAL);
        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel2.setText("Patient Name");
        jLabel3.setText("Patient ID");
        jLabel4.setText("Accession #");
        jLabel5.setText("Date Of Birth");
        birthDateSpinner.setEnabled(false);
        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel10Layout.createSequentialGroup().addContainerGap().add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel2).add(jLabel3).add(jLabel4).add(jLabel5)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 23, Short.MAX_VALUE).add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(patientNameText).add(patientIDText).add(accessionNoText).add(birthDateSpinner, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)).addContainerGap()));
        jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel10Layout.createSequentialGroup().addContainerGap(20, Short.MAX_VALUE).add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(patientNameText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(patientIDText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(accessionNoText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel5).add(birthDateSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ctCheckBox.setText("CT");
        mrCheckBox.setText("MR");
        xaCheckBox.setText("XA");
        crCheckBox.setText("CR");
        scCheckBox.setText("SC");
        nmCheckBox.setText("NM");
        rfCheckBox.setText("RF");
        dxCheckBox.setText("DX");
        pxCheckBox.setText("PX");
        usCheckBox.setText("US");
        otCheckBox.setText("OT");
        drCheckBox.setText("DR");
        srCheckBox.setText("SR");
        mgCheckBox.setText("MG");
        rgCheckBox.setText("RG");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(ctCheckBox).add(mrCheckBox).add(xaCheckBox).add(crCheckBox).add(scCheckBox)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(nmCheckBox).add(rfCheckBox).add(dxCheckBox).add(pxCheckBox).add(usCheckBox)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(otCheckBox).add(drCheckBox).add(srCheckBox).add(mgCheckBox).add(rgCheckBox))).add(jPanel2Layout.createSequentialGroup().add(6, 6, 6).add(modalityText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jPanel2Layout.createSequentialGroup().add(otCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(drCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(srCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(mgCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(rgCheckBox)).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(nmCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(rfCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(dxCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(pxCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(usCheckBox).add(scCheckBox))).add(jPanel2Layout.createSequentialGroup().add(ctCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(mrCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(xaCheckBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(crCheckBox)))).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(modalityText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(55, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel7Layout.createSequentialGroup().addContainerGap().add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        serverListTable.setModel(new ServerTableModel());
        serverListTable.setDefaultRenderer(Object.class, new CellRenderer());
        serverlistScroll.setViewportView(serverListTable);
        verifyButton.setText("Verify");
        verifyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyButtonActionPerformed(evt);
            }
        });
        queryButton.setText("Query");
        queryButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryButtonActionPerformed(evt);
            }
        });
        retrieveButton.setText("Retrieve");
        retrieveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retrieveButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel8Layout.createSequentialGroup().add(verifyButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(queryButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(retrieveButton).addContainerGap(382, Short.MAX_VALUE)));
        jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(verifyButton).add(queryButton).add(retrieveButton)));
        studyListTable.setModel(new StudyListModel());
        studyListTable.setDefaultRenderer(Object.class, new CellRenderer());
        studyListTable.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studyListTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(studyListTable);
        serverNameLabel.setBackground(new java.awt.Color(117, 113, 113));
        serverNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        serverNameLabel.setForeground(new java.awt.Color(0, 0, 104));
        serverNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serverNameLabel.setText(" Server Name");
        serverNameLabel.setOpaque(true);
        headerLabel.setBackground(new java.awt.Color(117, 113, 113));
        headerLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        headerLabel.setForeground(new java.awt.Color(0, 0, 104));
        headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabel.setText(" DICOM Nodes ");
        headerLabel.setOpaque(true);
        jLabel1.setBackground(new java.awt.Color(117, 113, 113));
        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        jLabel1.setForeground(new java.awt.Color(0, 0, 104));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Query Filter");
        jLabel1.setOpaque(true);
        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup().addContainerGap().add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1242, Short.MAX_VALUE).add(jPanel9Layout.createSequentialGroup().add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, headerLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, serverlistScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE).add(serverNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).add(20, 20, 20)));
        jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel9Layout.createSequentialGroup().add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(headerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE).add(serverlistScroll, 0, 236, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(serverNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE).addContainerGap()));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE));
        pack();
    }

    private void setPatientInfoToQueryParam() {
        queryParam.setPatientId(patientIDText.getText());
        queryParam.setPatientName(patientNameText.getText());
        queryParam.setAccessionNo(accessionNoText.getText());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = (Date) birthDateSpinner.getModel().getValue();
        String dateOfBirth = sdf.format(d1);
        queryParam.setBirthDate(dateOfBirth);
        if (!queryParam.getSearchDays().equalsIgnoreCase("Between")) {
            resetFromAndToDate();
        } else {
            setFromToDate();
        }
    }

    private boolean startSearch = false;

    private void queryButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
        try {
            startSearch = true;
            int noFilterQuery = 0;
            AEModel ae = ApplicationContext.databaseRef.getServerDetail(serverName);
            DcmURL url = new DcmURL("dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort());
            QueryService qs = new QueryService();
            setPatientInfoToQueryParam();
            if (queryParam.getPatientId().equalsIgnoreCase("") && queryParam.getPatientName().equalsIgnoreCase("") && queryParam.getSearchDate().equalsIgnoreCase("") && modalityText.getText().equalsIgnoreCase("") && queryParam.getAccessionNo().equalsIgnoreCase("")) {
                noFilterQuery = JOptionPane.showConfirmDialog(null, "No filters have been selected. It will take long time to query and display result...!");
            }
            if (noFilterQuery == 0) {
                qs.callFindWithQuery(queryParam.getPatientId(), queryParam.getPatientName(), "", queryParam.getSearchDate(), modalityText.getText(), queryParam.getAccessionNo(), url);
                Vector studyList = new Vector();
                for (int dataSetCount = 0; dataSetCount < qs.getDatasetVector().size(); dataSetCount++) {
                    try {
                        Dataset dataSet = (Dataset) qs.getDatasetVector().elementAt(dataSetCount);
                        StudyModel studyModel = new StudyModel(dataSet);
                        studyList.addElement(studyModel);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                StudyListModel studyListModel = new StudyListModel();
                studyListModel.setData(studyList);
                studyListTable.setModel(studyListModel);
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(studyListModel);
                studyListTable.setRowSorter(sorter);
                boolean dicomServerDetailAlreadyPresentInArray = false;
                if (dicomServerArray != null) {
                    for (int i = 0; i < dicomServerArray.size(); i++) {
                        if (dicomServerArray.get(i).getName().equalsIgnoreCase(ae.getServerName())) {
                            dicomServerDetailAlreadyPresentInArray = true;
                            dicomServerArray.get(i).setAe(ae);
                            dicomServerArray.get(i).setStudyListModel(studyListModel);
                        }
                    }
                }
                if (!dicomServerDetailAlreadyPresentInArray) {
                    DicomServerDelegate dsd = new DicomServerDelegate(ae.getServerName());
                    dsd.setAe(ae);
                    dsd.setStudyListModel(studyListModel);
                    dicomServerArray.add(dsd);
                }
            }
        } catch (Exception e) {
            System.out.println("Select a Server");
            e.printStackTrace();
        }
        startSearch = false;
    }

    /**
     * This routine is the handler for retrieve button.
     * @param evt
     */
    private void retrieveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
        String[] s = ApplicationContext.databaseRef.getListenerDetails();
        if (dicomServerArray != null) {
            for (int i = 0; i < dicomServerArray.size(); i++) {
                if (dicomServerArray.get(i).getName().equalsIgnoreCase(serverName)) {
                    int index[] = studyListTable.getSelectedRows();
                    for (int j = 0; j < index.length; j++) {
                        index[j] = studyListTable.convertRowIndexToModel(index[j]);
                    }
                    for (int tempI = 0; tempI < index.length; tempI++) {
                        String tem[] = new String[] { "dicom" + "://" + dicomServerArray.get(i).getAe().getAeTitle() + "@" + dicomServerArray.get(i).getAe().getHostName() + ":" + dicomServerArray.get(i).getAe().getPort(), "--dest", s[0], "--pid", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 0), "--suid", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8) };
                        try {
                            if (!ApplicationContext.databaseRef.checkRecordExists("study", "StudyInstanceUID", dicomServerArray.get(i).getStudyListModel().getValueAt(index[tempI], 8))) {
                                MainScreen.sndRcvFrm.setVisible(true);
                                MoveDelegate moveDelegate = new MoveDelegate(tem);
                            } else {
                                MainScreen.sndRcvFrm.setVisible(true);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * This routine used to set the server name
     */
    private void setServerName() {
        if (serverListTable.getSelectedRow() == -1) {
            serverNameLabel.setText(((ServerTableModel) serverListTable.getModel()).getValueAt(0, 0));
        } else {
            serverNameLabel.setText(((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0));
        }
    }

    private void verifyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
            AEModel ae = ApplicationContext.databaseRef.getServerDetail(serverName);
            DcmURL url = new DcmURL("dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort());
            EchoService echo = new EchoService();
            EchoStatus echoStatus = new EchoStatus(this, true);
            Display.alignScreen(echoStatus);
            echo.checkEcho(url);
            echoStatus.setTitle("Echo Status");
            try {
                if (echo.getStatus().trim().equalsIgnoreCase("EchoSuccess")) {
                    echoStatus.status.setText("Echo dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort() + " successfully!");
                    echoStatus.setVisible(true);
                } else {
                    echoStatus.status.setText("Echo dicom://" + ae.getAeTitle() + "@" + ae.getHostName() + ":" + ae.getPort() + " not successfully!");
                    echoStatus.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Select a Server");
        }
    }

    private void serverSelectionPerformed() {
        setServerName();
        StudyListModel studyList = new StudyListModel();
        studyListTable.setModel(studyList);
        if (serverListTable.getSelectedRow() > -1) {
            if (dicomServerArray != null) {
                for (int i = 0; i < dicomServerArray.size(); i++) {
                    if (dicomServerArray.get(i).getName().equalsIgnoreCase(((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0))) {
                        studyListTable.setModel(dicomServerArray.get(i).getStudyListModel());
                        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(studyList);
                        studyListTable.setRowSorter(sorter);
                    }
                }
            }
        }
    }

    private void studyListTableMouseClicked(java.awt.event.MouseEvent evt) {
        String serverName = ((ServerTableModel) serverListTable.getModel()).getValueAt(serverListTable.getSelectedRow(), 0);
        if (evt.getClickCount() == 2) {
            if (dicomServerArray != null) {
                for (int i = 0; i < dicomServerArray.size(); i++) {
                    if (dicomServerArray.get(i).getName().equalsIgnoreCase(serverName)) {
                        String tem[] = new String[] { "dicom" + "://" + dicomServerArray.get(i).getAe().getAeTitle() + "@" + dicomServerArray.get(i).getAe().getHostName() + ":" + dicomServerArray.get(i).getAe().getPort(), "--dest", "MAYAM", "--pid", dicomServerArray.get(i).getStudyListModel().getValueAt(studyListTable.getSelectedRow(), 0), "--suid", dicomServerArray.get(i).getStudyListModel().getValueAt(studyListTable.getSelectedRow(), 8) };
                        try {
                            if (ApplicationContext.databaseRef.checkRecordExists("study", "StudyInstanceUID", dicomServerArray.get(i).getStudyListModel().getValueAt(studyListTable.getSelectedRow(), 8))) {
                                StudyAvailabilityStatus studyStatus = new StudyAvailabilityStatus(this, true);
                                Display.alignScreen(studyStatus);
                                studyStatus.setVisible(true);
                            } else {
                                MainScreen.sndRcvFrm.setVisible(true);
                                MoveScu.main(tem);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(MainScreen.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    private class SearchDaysHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (searchDaysGroup.getSelection() == ((JRadioButton) e.getItem()).getModel()) {
                    if (((JRadioButton) e.getItem()).getActionCommand().equalsIgnoreCase("Between")) {
                        fromSpinner.setEnabled(true);
                        toSpinner.setEnabled(true);
                    } else {
                        fromSpinner.setEnabled(false);
                        toSpinner.setEnabled(false);
                    }
                    queryParam.setSearchDays(((JRadioButton) e.getItem()).getActionCommand());
                }
            }
        }
    }

    private class ModalityHandler implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            String selectedModality = getModality();
            if (selectedModality.startsWith("\\")) {
                selectedModality = selectedModality.substring(1);
            }
            modalityText.setText(selectedModality);
        }
    }

    private String getModality() {
        String modalityString = "";
        if (ctCheckBox.isSelected()) {
            modalityString = ctCheckBox.getActionCommand();
        }
        if (mrCheckBox.isSelected()) {
            modalityString += "\\" + mrCheckBox.getActionCommand();
        }
        if (xaCheckBox.isSelected()) {
            modalityString += "\\" + xaCheckBox.getActionCommand();
        }
        if (crCheckBox.isSelected()) {
            modalityString += "\\" + crCheckBox.getActionCommand();
        }
        if (scCheckBox.isSelected()) {
            modalityString += "\\" + scCheckBox.getActionCommand();
        }
        if (nmCheckBox.isSelected()) {
            modalityString += "\\" + nmCheckBox.getActionCommand();
        }
        if (rfCheckBox.isSelected()) {
            modalityString += "\\" + rfCheckBox.getActionCommand();
        }
        if (dxCheckBox.isSelected()) {
            modalityString += "\\" + dxCheckBox.getActionCommand();
        }
        if (pxCheckBox.isSelected()) {
            modalityString += "\\" + pxCheckBox.getActionCommand();
        }
        if (usCheckBox.isSelected()) {
            modalityString += "\\" + usCheckBox.getActionCommand();
        }
        if (otCheckBox.isSelected()) {
            modalityString += "\\" + otCheckBox.getActionCommand();
        }
        if (drCheckBox.isSelected()) {
            modalityString += "\\" + drCheckBox.getActionCommand();
        }
        if (srCheckBox.isSelected()) {
            modalityString += "\\" + srCheckBox.getActionCommand();
        }
        if (mgCheckBox.isSelected()) {
            modalityString += "\\" + mgCheckBox.getActionCommand();
        }
        if (rgCheckBox.isSelected()) {
            modalityString += "\\" + rgCheckBox.getActionCommand();
        }
        return modalityString;
    }

    private void osSpecifics() {
        this.setSize(1030, 750);
        if (System.getProperty("os.name").startsWith("Mac")) {
            Border border = UIManager.getBorder("InsetBorder.aquaVariant");
            if (border == null) {
                border = new BevelBorder(1);
            }
            jPanel1.setBorder(border);
            jPanel2.setBorder(border);
            jPanel9.setBackground(new Color(216, 216, 216));
            jPanel1.setBackground(new Color(216, 216, 216));
            jPanel2.setBackground(new Color(216, 216, 216));
            jPanel7.setBackground(new Color(216, 216, 216));
            jPanel8.setBackground(new Color(216, 216, 216));
            serverlistScroll.setBackground(new Color(216, 216, 216));
        }
    }

    /**
     * This is implemented handler method for ServerChangeListener.
     */
    public void onServerChange() {
        setServerTableModel();
        setServerName();
    }

    public void valueChanged(ListSelectionEvent e) {
        serverSelectionPerformed();
    }

    public void setFromToDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = (Date) fromSpinner.getModel().getValue();
        Date d2 = (Date) toSpinner.getModel().getValue();
        String from = sdf.format(d1);
        String to = sdf.format(d2);
        queryParam.setFrom(from);
        queryParam.setTo(to);
    }

    /**
     *This routine used to reset the from and to date.
     */
    public void resetFromAndToDate() {
        queryParam.setFrom(null);
        queryParam.setTo(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new QueryRetrive().setVisible(true);
            }
        });
    }

    private javax.swing.JTextField accessionNoText;

    private javax.swing.JRadioButton anydateRadio;

    private javax.swing.JRadioButton betweenRadio;

    private javax.swing.JSpinner birthDateSpinner;

    private javax.swing.JCheckBox crCheckBox;

    private javax.swing.JCheckBox ctCheckBox;

    private javax.swing.JCheckBox drCheckBox;

    private javax.swing.JCheckBox dxCheckBox;

    private javax.swing.JSpinner fromSpinner;

    private javax.swing.JLabel headerLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel10;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JRadioButton lastmonthRadio;

    private javax.swing.JRadioButton lastweekRadio;

    private javax.swing.JCheckBox mgCheckBox;

    private javax.swing.ButtonGroup modalityGroup;

    private javax.swing.JTextField modalityText;

    private javax.swing.JCheckBox mrCheckBox;

    private javax.swing.JCheckBox nmCheckBox;

    private javax.swing.JCheckBox otCheckBox;

    private javax.swing.JTextField patientIDText;

    private javax.swing.JTextField patientNameText;

    private javax.swing.JCheckBox pxCheckBox;

    private javax.swing.JButton queryButton;

    private javax.swing.JButton retrieveButton;

    private javax.swing.JCheckBox rfCheckBox;

    private javax.swing.JCheckBox rgCheckBox;

    private javax.swing.JCheckBox scCheckBox;

    private javax.swing.ButtonGroup searchDaysGroup;

    private javax.swing.JTable serverListTable;

    private javax.swing.JLabel serverNameLabel;

    private javax.swing.JScrollPane serverlistScroll;

    private javax.swing.JCheckBox srCheckBox;

    private javax.swing.JTable studyListTable;

    private javax.swing.JSpinner toSpinner;

    private javax.swing.JRadioButton todayRadio;

    private javax.swing.JCheckBox usCheckBox;

    private javax.swing.JButton verifyButton;

    private javax.swing.JCheckBox xaCheckBox;

    private javax.swing.JRadioButton yesterdayRadio;

    public ArrayList<DicomServerDelegate> dicomServerArray = new ArrayList<DicomServerDelegate>();

    private QueryParam queryParam = new QueryParam();
}
