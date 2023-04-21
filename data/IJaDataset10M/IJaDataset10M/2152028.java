package org.openXpertya.apps.form;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.compiere.plaf.CompiereColor;
import org.compiere.plaf.CompierePLAF;
import org.compiere.swing.CCheckBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTabbedPane;
import org.compiere.swing.CTextPane;
import org.openXpertya.apps.AEnv;
import org.openXpertya.apps.ConfirmPanel;
import org.openXpertya.apps.ProcessCtl;
import org.openXpertya.apps.StatusBar;
import org.openXpertya.grid.ed.VDate;
import org.openXpertya.grid.ed.VLookup;
import org.openXpertya.minigrid.IDColumn;
import org.openXpertya.minigrid.MiniTable;
import org.openXpertya.model.MLookup;
import org.openXpertya.model.MLookupFactory;
import org.openXpertya.model.MOrder;
import org.openXpertya.model.MPInstance;
import org.openXpertya.model.MPInstancePara;
import org.openXpertya.model.MProduct;
import org.openXpertya.model.MInvoice;
import org.openXpertya.model.MQuery;
import org.openXpertya.model.MTab;
import org.openXpertya.process.*;
import org.openXpertya.util.ASyncProcess;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.DisplayType;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;
import org.openXpertya.process.C_RemesaGenerate;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class VInvoiceRemGen extends CPanel implements FormPanel, ActionListener, VetoableChangeListener, ChangeListener, TableModelListener, ASyncProcess {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor de la clase ...
     *
     *
     * @param mTab
     */
    public VInvoiceRemGen(MTab mTab) {
        m_tab = mTab;
        m_UpdatedBy = 0;
        m_PriceList_Version_Base_ID = 0;
        Remesa = ((Integer) m_tab.getValue("C_Remesa_ID")).intValue();
        salir = false;
        tablatemporal = false;
    }

    /** Descripción de Campos */
    private Object m_AD_Org_ID = null;

    private Object m_C_BPartner_ID = null;

    private static CLogger log = CLogger.getCLogger(VInvoiceRemGen.class);

    /** Descripción de Campos */
    private MTab m_tab;

    /** Descripción de Campos */
    private int m_PriceList_Version_ID;

    /** Descripción de Campos */
    private int m_Client_ID;

    /** Descripción de Campos */
    private int m_Org_ID;

    /** Descripción de Campos */
    private int m_UpdatedBy;

    /** Descripción de Campos */
    private int m_DiscountSchema_ID;

    /** Descripción de Campos */
    private int m_PriceList_Version_Base_ID;

    /** Descripción de Campos */
    private int no;

    /** Descripción de Campos */
    private boolean creandoTabla;

    /** Descripción de Campos */
    private boolean salir;

    /** Descripción de Campos */
    private boolean tablatemporal;

    /** Descripción de Campos */
    private final int CPA = 3;

    /** Descripción de Campos */
    private final int CPN = 4;

    /** Descripción de Campos */
    private final int CS = 5;

    /** Descripción de Campos */
    private final int CA = 6;

    /** Descripción de Campos */
    private final int CC = 7;

    /** Descripción de Campos */
    private final int CDE = 8;

    /** Descripción de Campos */
    private final int CPS = 9;

    private int Remesa = 0;

    private MQuery m_query = null;

    /**
     * Descripción de Método
     *
     *
     * @param WindowNo
     * @param frame
     */
    public void init(int WindowNo, FormFrame frame) {
        log.info("currupio VInvoiceRemGen.init");
        m_WindowNo = WindowNo;
        m_frame = frame;
        try {
            fillPicks();
            jbInit();
            dynInit();
            m_frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            m_frame.getContentPane().add(allselectPane, BorderLayout.LINE_END);
            m_frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
            doIt();
        } catch (Exception ex) {
            log.log(Level.SEVERE, "VInvoiceRemGen,init " + ex);
        }
    }

    /** Descripción de Campos */
    private int m_WindowNo = 0;

    /** Descripción de Campos */
    private FormFrame m_frame;

    /** Descripción de Campos */
    private CTabbedPane tabbedPane = new CTabbedPane();

    private CTabbedPane allselectPane = new CTabbedPane();

    /** Descripción de Campos */
    private CPanel selPanel = new CPanel();

    /** Descripción de Campos */
    private BorderLayout selPanelLayout = new BorderLayout();

    /** Descripción de Campos */
    private FlowLayout northPanelLayout = new FlowLayout();

    private ConfirmPanel confirmPanelSel = new ConfirmPanel(true, false, false, false, false, false, true, true);

    /** Descripción de Campos */
    private ConfirmPanel confirmPanelGen = new ConfirmPanel(false, false, false, false, false, false, true);

    /** Descripción de Campos */
    private StatusBar statusBar = new StatusBar();

    /** Descripción de Campos */
    private CPanel genPanel = new CPanel();

    /** Descripción de Campos */
    private BorderLayout genLayout = new BorderLayout();

    /** Descripción de Campos */
    private CTextPane info = new CTextPane();

    /** Descripción de Campos */
    private JScrollPane scrollPane = new JScrollPane();

    /** Descripción de Campos */
    private MiniTable miniTable = new MiniTable();

    private CCheckBox automatico = new CCheckBox();

    private CPanel selNorthPanel_aux = new CPanel();

    private CLabel lOrg = new CLabel();

    private VLookup fOrg;

    private CLabel lBPartner = new CLabel();

    private VLookup fBPartner;

    private CLabel lFrom = new CLabel();

    private VDate fFrom = new VDate();

    private CLabel lTo = new CLabel();

    private VDate fTo = new VDate();

    private CPanel selNorthPanel = new CPanel();

    /** Descripción de Campos */
    private int m_keyColumnIndex = -1;

    /**
     * Descripción de Método
     *
     *
     * @throws Exception
     */
    private void fillPicks() throws Exception {
        MLookup orgL = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, 2163, DisplayType.TableDir);
        fOrg = new VLookup("AD_Org_ID", false, false, true, orgL);
        fOrg.addVetoableChangeListener(this);
        MLookup bpL = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, 2762, DisplayType.Search);
        fBPartner = new VLookup("C_BPartner_ID", false, false, true, bpL);
        fBPartner.addVetoableChangeListener(this);
    }

    /**
     * Descripción de Método
     *
     *
     * @throws Exception
     */
    void jbInit() throws Exception {
        log.info("currupio VInvoiceRemGen.jbInit");
        CompiereColor.setBackground(this);
        selNorthPanel_aux.setLayout(new BorderLayout());
        selPanel.setLayout(selPanelLayout);
        lOrg.setLabelFor(fOrg);
        lOrg.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
        lBPartner.setLabelFor(fBPartner);
        lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
        lFrom.setLabelFor(fFrom);
        lFrom.setText("Desde");
        lTo.setLabelFor(fTo);
        lTo.setText("Hasta");
        selNorthPanel.setLayout(northPanelLayout);
        northPanelLayout.setAlignment(FlowLayout.LEFT);
        tabbedPane.add(selPanel, "Select");
        selPanel.add(selNorthPanel, BorderLayout.NORTH);
        selNorthPanel_aux.add(selNorthPanel, BorderLayout.SOUTH);
        selPanel.add(selNorthPanel_aux, BorderLayout.NORTH);
        selNorthPanel.add(lOrg, null);
        selNorthPanel.add(fOrg, null);
        selNorthPanel.add(lBPartner, null);
        selNorthPanel.add(fBPartner, null);
        selNorthPanel.add(lFrom, null);
        selNorthPanel.add(fFrom, null);
        selNorthPanel.add(lTo, null);
        selNorthPanel.add(fTo, null);
        selPanel.setName("selPanel");
        selPanel.add(confirmPanelSel, BorderLayout.SOUTH);
        automatico.setText("Seleccionar Todos");
        automatico.setSelected(true);
        automatico.addActionListener(this);
        automatico.setEnabled(true);
        selNorthPanel_aux.add(automatico, BorderLayout.NORTH);
        selPanel.add(selNorthPanel_aux, BorderLayout.NORTH);
        selPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().add(miniTable, null);
        confirmPanelSel.addActionListener(this);
        tabbedPane.add(genPanel, "Generate");
        genPanel.setLayout(genLayout);
        genPanel.add(info, BorderLayout.CENTER);
        genPanel.setEnabled(false);
        info.setBackground(CompierePLAF.getFieldBackground_Inactive());
        info.setEditable(false);
        genPanel.add(confirmPanelGen, BorderLayout.SOUTH);
        confirmPanelGen.addActionListener(this);
    }

    /**
     * Descripción de Método
     *
     */
    private void dynInit() {
        log.info(" currupio VInvoiceRemGen.dynInit");
        miniTable.addColumn("C_InvoicePaySchedule_ID");
        miniTable.addColumn("DocumentNo");
        miniTable.addColumn("C_BPartner");
        miniTable.addColumn("DateAcct");
        miniTable.addColumn("IsPaid");
        miniTable.addColumn("TotalLines");
        miniTable.addColumn("GrandTotal");
        miniTable.addColumn("DateInvoiced");
        miniTable.addColumn("IsIndispute");
        miniTable.setColorColumn(8);
        miniTable.setColorCompare(new BigDecimal(0.01));
        miniTable.setMultiSelection(true);
        miniTable.setRowSelectionAllowed(true);
        miniTable.setColumnClass(0, IDColumn.class, false, " ");
        m_keyColumnIndex = 0;
        miniTable.setColumnClass(1, Integer.class, true, Msg.translate(Env.getCtx(), "DocumentNo"));
        miniTable.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
        miniTable.setColumnClass(3, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateAcct"));
        miniTable.setColumnClass(4, String.class, true, Msg.translate(Env.getCtx(), "IsPaid"));
        miniTable.setColumnClass(5, BigDecimal.class, true, Msg.translate(Env.getCtx(), "TotalLines"));
        miniTable.setColumnClass(6, BigDecimal.class, true, Msg.translate(Env.getCtx(), "GrandTotal"));
        miniTable.setColumnClass(7, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateInvoiced"));
        miniTable.setColumnClass(8, String.class, true, Msg.translate(Env.getCtx(), "IsIndispute"));
        miniTable.autoSize();
        miniTable.getModel().addTableModelListener(this);
        statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "MInvoice"));
        statusBar.setStatusDB(" ");
        fFrom.setMandatory(true);
        fTo.setMandatory(true);
        tabbedPane.addChangeListener(this);
    }

    /**
     * Descripción de Método
     *
     */
    private void executeQuery() {
        creandoTabla = true;
        log.info(" currupio VInvoiceRemGen.executeQuery");
        int row = 0;
        miniTable.setRowCount(row);
        int norma_id = 0;
        String sql2 = "SELECT c_norma_id from C_Remesa where c_remesa_id=" + Remesa + "";
        PreparedStatement pstmt2 = null;
        try {
            pstmt2 = DB.prepareStatement(sql2);
            ResultSet rs2 = pstmt2.executeQuery();
            if (rs2.next()) {
                norma_id = rs2.getInt(1);
            }
            rs2.close();
            pstmt2.close();
            pstmt2 = null;
        } catch (Exception e) {
            log.saveError("C_RemesaGenerate - Prepare", e);
        }
        StringBuffer sql = new StringBuffer("SELECT ips.c_invoicepayschedule_id, inv.documentno, cb.name, duedate, ispaid, dueamt, grandtotal, ispayschedulevalid, dateprinted, dateinvoiced, isindispute FROM c_invoicepayschedule ips, c_invoice inv, c_bpartner cb WHERE ips.c_invoice_id=inv.c_invoice_id AND cb.c_bpartner_id=inv.c_bpartner_id AND ((DueDate<(SELECT rem.ExecuteDate FROM C_Remesa rem WHERE rem.C_Remesa_ID=" + Remesa + ") AND ispaid='N')OR aux='Y') AND ips.Processed='N' " + " AND  c_remesa_id is null AND inv.issotrx=(Select issotrx from c_norma where c_norma_id=" + norma_id + ") AND inv.paymentrule=(Select paymentrule from c_norma where c_norma_id=" + norma_id + ")");
        Date FromDate = fFrom.getTimestamp();
        if (FromDate != null) {
            log.fine("Entro aqui1");
            sql.append(" AND ips.created > '" + FromDate.toString().substring(0, FromDate.toString().indexOf(" ")) + "'");
        }
        Date ToDate = fTo.getTimestamp();
        if (ToDate != null) {
            log.fine("Entro aqui2");
            sql.append(" AND ips.created < '" + ToDate.toString().substring(0, ToDate.toString().indexOf(" ")) + "'");
        }
        if (m_AD_Org_ID != null) {
            sql.append(" AND inv.AD_Org_ID=").append(m_AD_Org_ID);
        }
        if (m_C_BPartner_ID != null) {
            sql.append(" AND inv.C_BPartner_ID=").append(m_C_BPartner_ID);
        }
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                miniTable.setRowCount(row + 1);
                IDColumn m_idColumn;
                m_idColumn = new IDColumn(rs.getInt(1));
                log.fine("LA ID en executeQuery=" + m_idColumn);
                automatico.setSelected(false);
                m_idColumn.setSelected(false);
                miniTable.setValueAt(m_idColumn, row, 0);
                miniTable.setValueAt(new Integer(rs.getInt(2)), row, 1);
                miniTable.setValueAt(rs.getString(3), row, 2);
                miniTable.setValueAt(rs.getDate(4), row, 3);
                miniTable.setValueAt(rs.getString(5), row, 4);
                miniTable.setValueAt(rs.getBigDecimal(6), row, 5);
                miniTable.setValueAt(rs.getBigDecimal(7), row, 6);
                miniTable.setValueAt(rs.getDate(10), row, 7);
                miniTable.setValueAt(rs.getString(11), row, 8);
                row++;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "VInvoiceRemGen.executeQuery " + sql.toString(), e);
        }
        miniTable.autoSize();
        statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
        creandoTabla = false;
    }

    /**
     * Descripción de Método
     *
     */
    public void dispose() {
        log.info(" currupio VInvoiceRemGen.dispose");
        if (m_frame != null) {
            m_frame.dispose();
        }
        m_frame = null;
    }

    public void seleccionarTodos() {
        for (int i = 0; i < miniTable.getRowCount(); i++) {
            IDColumn id = (IDColumn) miniTable.getModel().getValueAt(i, 0);
            id.setSelected(automatico.isSelected());
            miniTable.getModel().setValueAt(id, i, 0);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        log.info(" currupio VInvoiceRemGen.actionPerformed - " + e.getActionCommand());
        if (e.getSource().equals(automatico)) seleccionarTodos();
        if (e.getActionCommand().equals(ConfirmPanel.A_ZOOM)) {
            zoom();
        } else if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {
            dispose();
            return;
        } else if (e.getActionCommand().equals(ConfirmPanel.A_OK)) {
            updateIt();
            remesagenerate_process();
            dispose();
        } else if (e.getActionCommand().equals(ConfirmPanel.A_NEW)) {
            new_invoice();
        } else if (e.getActionCommand().equals(ConfirmPanel.A_REFRESH)) {
            executeQuery();
        }
    }

    /**
     * Descripción de Método
     *
     */
    public void remesagenerate_process() {
        int proceso = 0;
        String norma = "";
        String subnorma = "";
        String nombre_proceso = "";
        String sql = "SELECT name,subnorma from C_Norma where c_norma_id=(SELECT c_norma_id from C_Remesa where c_remesa_id=" + Remesa + ")";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                norma = rs.getString(1);
                subnorma = rs.getString(2);
                if (norma.contains("68")) {
                    nombre_proceso = "RemesaGenerate68";
                } else if (norma.contains("34")) {
                    nombre_proceso = "RemesaGenerate34";
                } else {
                    nombre_proceso = "RemesaGenerate";
                }
                log.fine("En la eleccion de norma y subnorma, norma=" + norma + ", y subnorma=" + subnorma);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.saveError("C_RemesaGenerate - Prepare", e);
        }
        String sql2 = "SELECT ad_process_id from ad_process where value like '" + nombre_proceso + "'";
        PreparedStatement pstmt2 = null;
        try {
            pstmt2 = DB.prepareStatement(sql2);
            ResultSet rs2 = pstmt2.executeQuery();
            if (rs2.next()) {
                proceso = rs2.getInt(1);
                log.fine("entra aqui dentro,rs2.next" + proceso);
            }
            rs2.close();
            pstmt2.close();
            pstmt2 = null;
        } catch (Exception e) {
            log.saveError("VInvoiceRemGen - remesagenerateprocess", e);
        }
        if (norma.contains("68")) {
            log.fine("Entra aqui68");
            C_RemesaGenerate68 a = new C_RemesaGenerate68();
            a.set_c_remesa_id(Remesa);
        } else if (norma.contains("34")) {
            log.fine("Entra aqui34");
            C_RemesaGenerate34 a = new C_RemesaGenerate34();
            a.set_c_remesa_id(Remesa);
        } else {
            log.fine("Entra aqui19-58");
            C_RemesaGenerate a = new C_RemesaGenerate();
            a.set_c_remesa_id(Remesa);
        }
        int AD_Process_ID = proceso;
        MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, 0, null);
        if (!instance.save()) {
            info.setText(Msg.getMsg(Env.getCtx(), "ProcessNoInstance"));
            return;
        }
        ProcessInfo pi = new ProcessInfo("", AD_Process_ID);
        pi.setAD_PInstance_ID(instance.getAD_PInstance_ID());
        MPInstancePara para = new MPInstancePara(instance, 10);
        para.setParameter("Selection", "Y");
        if (!para.save()) {
            String msg = "No Selection Parameter added";
            info.setText(msg);
            log.log(Level.SEVERE, msg);
            return;
        }
        para = new MPInstancePara(instance, 20);
        para.setParameter("DocAction", "CO");
        if (!para.save()) {
            String msg = "No DocAction Parameter added";
            info.setText(msg);
            log.log(Level.SEVERE, msg);
            return;
        }
        ProcessCtl worker = new ProcessCtl(this, pi, null);
        worker.start();
    }

    void zoom() {
        log.info("VInvoiceRemGen.zoom");
        Integer C_Invoice_ID = getSelectedRowKey();
        if (C_Invoice_ID == null) {
            return;
        }
        AEnv.zoom(MInvoice.Table_ID, C_Invoice_ID.intValue());
    }

    void new_invoice() {
        log.info("VInvoiceRemGen.new_invoice");
        FormPanel m_panel = null;
        FormFrame ff = new FormFrame();
        try {
            m_panel = new VInvoiceRemGen_aux(m_tab);
            ff.setFormPanel(m_panel);
            m_panel.init(ff.getWindowNo(), ff);
            ff.pack();
            AEnv.showCenterScreen(ff);
        } catch (Exception e) {
            log.log(Level.SEVERE, "VInvoiceRemGen_aux.openForm =" + e);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    boolean hasZoom() {
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    protected Integer getSelectedRowKey() {
        int row = miniTable.getSelectedRow();
        if ((row != -1) && (m_keyColumnIndex != -1)) {
            Object data = miniTable.getModel().getValueAt(row, m_keyColumnIndex);
            if (data instanceof IDColumn) {
                data = ((IDColumn) data).getRecord_ID();
                log.fine("en getSelectedRowKey con data =" + data);
            }
            if (data instanceof Integer) {
                return (Integer) data;
            }
        }
        return null;
    }

    /**
     * Descripción de Método
     *
     *
     * @param e
     */
    public void vetoableChange(PropertyChangeEvent e) {
        log.info("VProdPricGen.vetoableChange - " + e.getPropertyName() + "=" + e.getNewValue());
        if (e.getPropertyName().equals("AD_Org_ID")) {
            m_AD_Org_ID = e.getNewValue();
        }
        if (e.getPropertyName().equals("C_BPartner_ID")) {
            m_C_BPartner_ID = e.getNewValue();
            fBPartner.setValue(m_C_BPartner_ID);
        }
        executeQuery();
    }

    /**
     * Descripción de Método
     *
     *
     * @param e
     */
    public void stateChanged(ChangeEvent e) {
        log.fine("Estoy en stateChanged y lo que llega es e=" + e.toString());
        int index = tabbedPane.getSelectedIndex();
    }

    /**
     * Descripción de Método
     *
     *
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        if (!creandoTabla) {
            creandoTabla = true;
            log.fine("Estoy en tableChanged y lo que llega es e=" + e.toString());
            creandoTabla = false;
        }
    }

    /**
     * Descripción de Método
     *
     */
    public void changeSelectedPrice() {
        log.info(" currupio VInvoiceRemGen.changeSelecedPrice");
    }

    /**
     * Descripción de Método
     *
     *
     * @param pi
     */
    private void afterCreateProductPrices(ProcessInfo pi) {
        log.info(" currupio VInvoiceRemGen.afterCreateProductPrice");
        confirmPanelGen.getOKButton().setEnabled(true);
        salir = true;
    }

    /**
     * Descripción de Método
     *
     *
     * @param pi
     */
    public void lockUI(ProcessInfo pi) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.setEnabled(false);
    }

    /**
     * Descripción de Método
     *
     *
     * @param pi
     */
    public void unlockUI(ProcessInfo pi) {
        this.setEnabled(true);
        this.setCursor(Cursor.getDefaultCursor());
        if (tablatemporal) {
            afterCreateProductPrices(pi);
        } else {
            tablatemporal = true;
            executeQuery();
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isUILocked() {
        return this.isEnabled();
    }

    /**
     * Descripción de Método
     *
     *
     * @param pi
     */
    public void executeASync(ProcessInfo pi) {
    }

    /**
     * Descripción de Método
     *
     */
    private void doIt() {
        log.info(" currupio VInvoiceRemGen.doIT");
    }

    /**
     * Descripción de Método
     *
     */
    private void updateIt() {
        log.info(" currupio VInvoiceRemGen.updateIt");
        int rows = miniTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            IDColumn id = (IDColumn) miniTable.getValueAt(i, 0);
            if (id != null && id.isSelected()) {
                StringBuffer sql = new StringBuffer("UPDATE C_InvoicePaySchedule SET C_Remesa_ID=" + Remesa + ",aux='N' WHERE C_Invoicepayschedule_ID=" + id.getRecord_ID());
                DB.executeUpdate(sql.toString());
            }
        }
    }
}
