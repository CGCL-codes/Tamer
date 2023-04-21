package org.adempierelbr.form;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.adempierelbr.model.MDocPrint;
import org.adempierelbr.model.MMatrixPrinter;
import org.adempierelbr.model.MNotaFiscal;
import org.adempierelbr.process.ProcPrintNF;
import org.adempierelbr.util.AdempiereLBR;
import org.adempierelbr.util.TextUtil;
import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.StatusBar;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.MiniTable;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTabbedPane;
import org.compiere.swing.CTextField;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * FormNotaFiscal
 * 
 * Form to Print Nota Fiscal
 * 
 * FR [2343379] - Definição de Impressão de Documento NF na Org
 * 
 * @author Mario Grigioni, mgrigioni (Kenos, www.kenos.com.br) 
 * @version $Id: FormNotaFiscal.java, 01/03/2007 09:46:00 mgrigioni
 */
public class FormNotaFiscal extends CPanel implements FormPanel, ActionListener, VetoableChangeListener, ChangeListener, TableModelListener, ASyncProcess {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String interval = "7";

    private static final int LBR_DocPrint_ID = 2000000;

    /**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
    public void init(int WindowNo, FormFrame frame) {
        log.info("");
        m_WindowNo = WindowNo;
        m_frame = frame;
        Env.setContext(Env.getCtx(), m_WindowNo, "IsSOTrx", "Y");
        try {
            fillPicks();
            jbInit();
            dynInit();
            frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "init", ex);
        }
    }

    /**	Window No			*/
    private int m_WindowNo = 0;

    /**	FormFrame			*/
    private FormFrame m_frame;

    private boolean m_selectionActive = true;

    private String m_whereClause;

    private Object m_DateDoc = null;

    private Object m_C_BPartner_ID = null;

    private Object m_LBR_MatrixPrinter_ID = null;

    private boolean m_mark = true;

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(FormNotaFiscal.class);

    private CTabbedPane tabbedPane = new CTabbedPane();

    private CPanel selPanel = new CPanel();

    private CPanel selNorthPanel = new CPanel();

    private BorderLayout selPanelLayout = new BorderLayout();

    private CLabel lDateDoc = new CLabel();

    private VDate fDateDoc = new VDate("DateDoc", false, false, true, 15, "");

    private CLabel lBPartner = new CLabel();

    private VLookup fBPartner;

    private CLabel lDocumentNo = new CLabel();

    private CTextField fDocumentNo = new CTextField(15);

    private CLabel lMatrixPrinter = new CLabel();

    private VLookup fMatrixPrinter;

    private JCheckBox printedNF = new JCheckBox();

    private GridBagLayout northPanelLayout = new GridBagLayout();

    private ConfirmPanel confirmPanelSel = new ConfirmPanel(true, true);

    private StatusBar statusBar = new StatusBar();

    private JScrollPane scrollPane = new JScrollPane();

    private MiniTable miniTable = new MiniTable();

    private JButton markButton = new JButton();

    /**
	 *	Static Init.
	 *  <pre>
	 *  selPanel (tabbed)
	 *      fOrg, fBPartner
	 *      scrollPane & miniTable
	 *  genPanel
	 *      info
	 *  </pre>
	 *  @throws Exception
	 */
    void jbInit() throws Exception {
        CompiereColor.setBackground(this);
        confirmPanelSel.addButton(markButton);
        confirmPanelSel.add(printedNF);
        selPanel.setLayout(selPanelLayout);
        selPanel.setPreferredSize(new Dimension(800, 450));
        lDateDoc.setLabelFor(fDateDoc);
        lDateDoc.setText(Msg.translate(Env.getCtx(), "DateDoc"));
        lBPartner.setLabelFor(fBPartner);
        lBPartner.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
        lDocumentNo.setLabelFor(fDocumentNo);
        lDocumentNo.setText(Msg.translate(Env.getCtx(), "DocumentNo"));
        lMatrixPrinter.setLabelFor(fMatrixPrinter);
        lMatrixPrinter.setText(Msg.translate(Env.getCtx(), "LBR_MatrixPrinter_ID"));
        printedNF.setText(Msg.translate(Env.getCtx(), "IsPrinted"));
        printedNF.addActionListener(this);
        selNorthPanel.setLayout(northPanelLayout);
        tabbedPane.add(selPanel, Msg.getMsg(Env.getCtx(), "Select"));
        selPanel.add(selNorthPanel, BorderLayout.NORTH);
        selNorthPanel.add(lDateDoc, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        selNorthPanel.add(fDateDoc, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        selNorthPanel.add(lBPartner, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        selNorthPanel.add(fBPartner, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        selNorthPanel.add(lDocumentNo, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        selNorthPanel.add(fDocumentNo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        selNorthPanel.add(lMatrixPrinter, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        selNorthPanel.add(fMatrixPrinter, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        selPanel.setName("selPanel");
        selPanel.add(confirmPanelSel, BorderLayout.SOUTH);
        selPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().add(miniTable, null);
        confirmPanelSel.addActionListener(this);
        markButton.setText("Todos");
        markButton.addActionListener(this);
        fDateDoc.addVetoableChangeListener(this);
    }

    /**
	 *	Fill Picks
	 *		Column_ID from C_Order
	 *  @throws Exception if Lookups cannot be initialized
	 */
    private void fillPicks() throws Exception {
        MLookup PrinterL = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, 1000716, DisplayType.Table);
        fMatrixPrinter = new VLookup("LBR_MatrixPrinter_ID", true, false, true, PrinterL);
        fMatrixPrinter.addVetoableChangeListener(this);
        fMatrixPrinter.setValue(MMatrixPrinter.getDefaultPrinter());
        m_LBR_MatrixPrinter_ID = (Integer) fMatrixPrinter.getValue();
        MLookup BPartnerL = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, 2893, DisplayType.Search);
        fBPartner = new VLookup("C_BPartner_ID", false, false, true, BPartnerL);
        fBPartner.addVetoableChangeListener(this);
    }

    /**
	 *	Dynamic Init.
	 *	- Create GridController & Panel
	 *	- AD_Column_ID from C_Order
	 */
    private void dynInit() {
        miniTable.addColumn("LBR_NotaFiscal_ID");
        miniTable.addColumn("AD_Org_ID");
        miniTable.addColumn("DocumentNo");
        miniTable.addColumn("C_BPartner_ID");
        miniTable.addColumn("DateDoc");
        miniTable.setMultiSelection(true);
        miniTable.setRowSelectionAllowed(true);
        miniTable.setColumnClass(0, IDColumn.class, false, " ");
        miniTable.setColumnClass(1, String.class, true, Msg.translate(Env.getCtx(), "AD_Org_ID"));
        miniTable.setColumnClass(2, String.class, true, Msg.translate(Env.getCtx(), "DocumentNo"));
        miniTable.setColumnClass(3, String.class, true, Msg.translate(Env.getCtx(), "C_BPartner_ID"));
        miniTable.setColumnClass(4, Timestamp.class, true, Msg.translate(Env.getCtx(), "DateDoc"));
        miniTable.autoSize();
        miniTable.getModel().addTableModelListener(this);
        statusBar.setStatusLine(Msg.getMsg(Env.getCtx(), "InvGenerateSel"));
        statusBar.setStatusDB(" ");
        tabbedPane.addChangeListener(this);
        executeQuery();
    }

    /**
	 *  Query Info
	 */
    private void executeQuery() {
        log.info("");
        statusBar.setStatusLine("");
        int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
        int index = 0;
        boolean documentno = false;
        Timestamp startDate = Env.getContextAsDate(Env.getCtx(), "#Date");
        Timestamp actualDate = Env.getContextAsDate(Env.getCtx(), "#Date");
        StringBuffer sql = new StringBuffer("SELECT distinct nf.LBR_NotaFiscal_ID, o.Name, nf.DocumentNo, bp.Name, nf.DateDoc ");
        sql.append("FROM LBR_NotaFiscal nf ");
        sql.append("INNER JOIN AD_Org o ON nf.AD_Org_ID=o.AD_Org_ID ");
        sql.append("INNER JOIN C_BPartner bp ON nf.C_BPartner_ID = bp.C_BPartner_ID ");
        sql.append("WHERE nf.C_DocTypeTarget_ID IS NOT NULL ");
        sql.append("AND nf.IsCancelled='N' ");
        sql.append("AND nf.AD_Client_ID=? ");
        if (printedNF.isSelected()) {
            sql.append("AND nf.IsPrinted='Y' ");
        } else {
            sql.append("AND nf.IsPrinted='N' ");
        }
        if (m_DateDoc != null) {
            sql.append("AND nf.DateDoc=? ");
            index = index + 1;
        }
        if (m_C_BPartner_ID != null) {
            sql.append("AND nf.C_BPartner_ID=? ");
            index = index + 2;
        }
        String documentNo = fDocumentNo.getText().toUpperCase();
        if (documentNo != null && !(documentNo.equals(""))) {
            sql.append("AND UPPER(nf.DocumentNo) LIKE '%");
            sql.append(documentNo.toString());
            sql.append("%' ");
            documentno = true;
        }
        if (index == 0 && printedNF.isSelected() && !documentno) {
            startDate = AdempiereLBR.addDays(actualDate, Integer.parseInt(interval) * -1);
            sql.append("AND nf.DateDoc BETWEEN ? AND ?");
            index = 4;
            statusBar.setStatusLine("Intervalo definido entre " + TextUtil.timeToString(startDate, "dd/MM/yyyy") + " e " + TextUtil.timeToString(actualDate, "dd/MM/yyyy"));
        }
        sql.append("ORDER BY nf.DocumentNo, DateDoc, o.Name, bp.Name");
        int row = 0;
        miniTable.setRowCount(row);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), null);
            pstmt.setInt(1, AD_Client_ID);
            if (index == 1) pstmt.setTimestamp(2, (Timestamp) m_DateDoc); else if (index == 2) pstmt.setInt(2, (Integer) m_C_BPartner_ID); else if (index == 3) {
                pstmt.setTimestamp(2, (Timestamp) m_DateDoc);
                pstmt.setInt(3, (Integer) m_C_BPartner_ID);
            } else if (index == 4) {
                pstmt.setTimestamp(2, startDate);
                pstmt.setTimestamp(3, actualDate);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                miniTable.setRowCount(row + 1);
                miniTable.setValueAt(new IDColumn(rs.getInt(1)), row, 0);
                miniTable.setValueAt(rs.getString(2), row, 1);
                miniTable.setValueAt(rs.getString(3), row, 2);
                miniTable.setValueAt(rs.getString(4), row, 3);
                miniTable.setValueAt(rs.getTimestamp(5), row, 4);
                row++;
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql.toString(), e);
        } finally {
            DB.close(rs, pstmt);
        }
        miniTable.autoSize();
        statusBar.setStatusDB(String.valueOf(miniTable.getRowCount()));
    }

    /**
	 * 	Dispose
	 */
    public void dispose() {
        if (m_frame != null) m_frame.dispose();
        m_frame = null;
    }

    /**
	 *	Action Listener
	 *  @param e event
	 */
    public void actionPerformed(ActionEvent e) {
        log.info("Cmd=" + e.getActionCommand());
        if (e.getActionCommand().equals(ConfirmPanel.A_CANCEL)) {
            dispose();
            return;
        }
        if (e.getActionCommand().equals(ConfirmPanel.A_REFRESH)) {
            executeQuery();
            return;
        }
        if (e.getSource().equals(printedNF)) {
            executeQuery();
            return;
        }
        if (e.getSource().equals(markButton)) {
            markAll();
            return;
        }
        m_whereClause = saveSelection();
        if (m_whereClause.length() > 0 && m_selectionActive) {
            try {
                print();
            } catch (Exception ex) {
                log.log(Level.SEVERE, "", ex);
            }
            m_mark = true;
            executeQuery();
        } else {
            String msg = "Selecionar Notas Fiscais para Impressão";
            statusBar.setStatusLine(msg);
            ADialog.info(m_WindowNo, this, msg);
            return;
        }
    }

    private void print() {
        Properties ctx = Env.getCtx();
        if ((Integer) m_LBR_MatrixPrinter_ID == null) return;
        MMatrixPrinter MatrixPrinter = new MMatrixPrinter(ctx, (Integer) m_LBR_MatrixPrinter_ID, null);
        String PrinterType = MatrixPrinter.getlbr_PrinterType();
        String PrinterName = MatrixPrinter.getlbr_PrinterPath();
        String charSet = MatrixPrinter.getlbr_Characterset();
        int pitch = MatrixPrinter.getlbr_Pitch();
        boolean condensed = MatrixPrinter.islbr_IsCondensed();
        int orgDocPrint_ID = MDocPrint.getLBR_DocPrint_ID(ctx);
        if (orgDocPrint_ID == 0) orgDocPrint_ID = LBR_DocPrint_ID;
        MDocPrint DoctypePrint = new MDocPrint(ctx, orgDocPrint_ID, null);
        DoctypePrint.startJob(PrinterType, PrinterName, charSet, condensed, pitch);
        Integer[] selection = getSelection();
        for (int i = 0; i < selection.length; i++) {
            if (i != 0) DoctypePrint.newPage();
            MNotaFiscal NotaFiscal = new MNotaFiscal(ctx, selection[i], null);
            ProcPrintNF.print(ctx, selection[i], MatrixPrinter, DoctypePrint, null);
            NotaFiscal.setIsPrinted(true);
            NotaFiscal.setProcessed(true);
            NotaFiscal.save();
        }
        DoctypePrint.endJob();
        MDocPrint.unixPrint(MatrixPrinter);
    }

    /**
	 *	Vetoable Change Listener - requery
	 *  @param e event
	 */
    public void vetoableChange(PropertyChangeEvent e) {
        log.info(e.getPropertyName() + "=" + e.getNewValue());
        int i = 0;
        if (e.getPropertyName().equals("DateDoc")) {
            m_DateDoc = e.getNewValue();
            fDateDoc.setValue(m_DateDoc);
            if (m_DateDoc != null) i = 1;
        }
        if (e.getPropertyName().equals("C_BPartner_ID")) {
            m_C_BPartner_ID = e.getNewValue();
            fBPartner.setValue(m_C_BPartner_ID);
            if (m_C_BPartner_ID != null) i = 1;
        }
        if (e.getPropertyName().equals("LBR_MatrixPrinter_ID")) {
            m_LBR_MatrixPrinter_ID = e.getNewValue();
            fMatrixPrinter.setValue(m_LBR_MatrixPrinter_ID);
        }
        if (i != 0) executeQuery();
    }

    /**
	 *	Change Listener (Tab changed)
	 *  @param e event
	 */
    public void stateChanged(ChangeEvent e) {
        int index = tabbedPane.getSelectedIndex();
        m_selectionActive = (index == 0);
    }

    /**
	 *  Table Model Listener
	 *  @param e event
	 */
    public void tableChanged(TableModelEvent e) {
        int rowsSelected = 0;
        int rows = miniTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            IDColumn id = (IDColumn) miniTable.getValueAt(i, 0);
            if (id != null && id.isSelected()) rowsSelected++;
        }
        statusBar.setStatusDB(" " + rowsSelected + " ");
    }

    /**
	 *	markAll
	 */
    private void markAll() {
        log.info("");
        miniTable.editingStopped(new ChangeEvent(this));
        int rows = miniTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            IDColumn id = (IDColumn) miniTable.getValueAt(i, 0);
            id.setSelected(m_mark);
            miniTable.setValueAt(id, i, 0);
        }
        if (m_mark == false) m_mark = true; else if (m_mark == true) m_mark = false;
    }

    /**
	 *	Save Selection & return selecion Query or ""
	 *  @return where clause like C_Order_ID IN (...)
	 */
    private String saveSelection() {
        log.info("");
        miniTable.editingStopped(new ChangeEvent(this));
        ArrayList<Integer> results = new ArrayList<Integer>();
        int rows = miniTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            IDColumn id = (IDColumn) miniTable.getValueAt(i, 0);
            if (id != null && id.isSelected()) results.add(id.getRecord_ID());
        }
        if (results.size() == 0) return "";
        String keyColumn = "LBR_NotaFiscal_ID";
        StringBuffer sb = new StringBuffer(keyColumn);
        if (results.size() > 1) sb.append(" IN ("); else sb.append("=");
        for (int i = 0; i < results.size(); i++) {
            if (i > 0) sb.append(",");
            if (keyColumn.endsWith("_ID")) sb.append(results.get(i).toString()); else sb.append("'").append(results.get(i).toString());
        }
        if (results.size() > 1) sb.append(")");
        log.config(sb.toString());
        return sb.toString();
    }

    /**
	 *	Save Selection & return Array
	 *  @return Integer[]
	 */
    private Integer[] getSelection() {
        log.info("");
        miniTable.editingStopped(new ChangeEvent(this));
        ArrayList<Integer> results = new ArrayList<Integer>();
        int rows = miniTable.getRowCount();
        for (int i = 0; i < rows; i++) {
            IDColumn id = (IDColumn) miniTable.getValueAt(i, 0);
            if (id != null && id.isSelected()) results.add(id.getRecord_ID());
        }
        Integer[] lines = new Integer[results.size()];
        results.toArray(lines);
        return lines;
    }

    /**************************************************************************
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
    public void lockUI(ProcessInfo pi) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        this.setEnabled(false);
    }

    /**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi result of execute ASync call
	 */
    public void unlockUI(ProcessInfo pi) {
        this.setEnabled(true);
        this.setCursor(Cursor.getDefaultCursor());
    }

    /**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
    public boolean isUILocked() {
        return this.isEnabled();
    }

    /**
	 *  Method to be executed async.
	 *  Called from the Worker
	 *  @param pi ProcessInfo
	 */
    public void executeASync(ProcessInfo pi) {
    }
}
