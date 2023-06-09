package org.compiere.pos;

import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import org.compiere.swing.*;
import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Point of Sales Main Window.
 *
 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright (c) Jorg Janke
 *  @version $Id: PosPanel.java,v 1.10 2004/07/12 04:10:04 jjanke Exp $
 */
public class PosPanel extends CPanel implements FormPanel {

    /**
	 * 	Constructor - see init 
	 */
    public PosPanel() {
        super(new GridBagLayout());
        originalKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        m_focusMgr = new PosKeyboardFocusManager();
        KeyboardFocusManager.setCurrentKeyboardFocusManager(m_focusMgr);
    }

    /**	Window No			*/
    private int m_WindowNo = 0;

    /**	FormFrame			*/
    private FormFrame m_frame;

    /**	Logger				*/
    private CLogger log = CLogger.getCLogger(getClass());

    /** Context				*/
    private Properties m_ctx = Env.getCtx();

    /** Sales Rep 			*/
    private int m_SalesRep_ID = 0;

    /** POS Model			*/
    protected MPOS p_pos = null;

    /** Keyoard Focus Manager		*/
    private PosKeyboardFocusManager m_focusMgr = null;

    /**	Status Bar					*/
    protected StatusBar f_status = new StatusBar();

    /** Customer Panel				*/
    protected SubBPartner f_bpartner = null;

    /** Sales Rep Panel				*/
    protected SubSalesRep f_salesRep = null;

    /** Current Line				*/
    protected SubCurrentLine f_curLine = null;

    /** Product	Selection			*/
    protected SubProduct f_product = null;

    /** All Lines					*/
    protected SubLines f_lines = null;

    /** Function Keys				*/
    protected SubFunctionKeys f_functionKeys = null;

    /** Checkout					*/
    protected SubCheckout f_checkout = null;

    /**	Product Query Window		*/
    protected QueryProduct f_queryProduct = null;

    /**	BPartner Query Window		*/
    protected QueryBPartner f_queryBPartner = null;

    /** Ticket Query Window			*/
    protected QueryTicket f_queryTicket = null;

    protected CashSubFunctions f_cashfunctions;

    private Timestamp m_today = Env.getContextAsDate(m_ctx, "#Date");

    private KeyboardFocusManager originalKeyboardFocusManager;

    /**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame parent frame
	 */
    public void init(int WindowNo, FormFrame frame) {
        frame.setMaximize(true);
        m_SalesRep_ID = Env.getAD_User_ID(m_ctx);
        log.info("init - SalesRep_ID=" + m_SalesRep_ID);
        m_WindowNo = WindowNo;
        m_frame = frame;
        try {
            if (!dynInit()) {
                dispose();
                frame.dispose();
                return;
            }
            frame.getContentPane().add(this, BorderLayout.CENTER);
            frame.getContentPane().add(f_status, BorderLayout.SOUTH);
        } catch (Exception e) {
            log.log(Level.SEVERE, "init", e);
        }
        log.config("PosPanel.init - " + getPreferredSize());
        m_focusMgr.start();
    }

    /**
	 * 	Dispose - Free Resources
	 */
    public void dispose() {
        if (m_focusMgr != null) m_focusMgr.stop();
        m_focusMgr = null;
        KeyboardFocusManager.setCurrentKeyboardFocusManager(originalKeyboardFocusManager);
        if (f_bpartner != null) f_bpartner.dispose();
        f_bpartner = null;
        if (f_salesRep != null) f_salesRep.dispose();
        f_salesRep = null;
        if (f_curLine != null) {
            f_curLine.deleteOrder();
            f_curLine.dispose();
        }
        f_curLine = null;
        if (f_product != null) f_product.dispose();
        f_product = null;
        if (f_lines != null) f_lines.dispose();
        f_lines = null;
        if (f_functionKeys != null) f_functionKeys.dispose();
        f_functionKeys = null;
        if (f_checkout != null) f_checkout.dispose();
        f_checkout = null;
        if (f_queryProduct != null) f_queryProduct.dispose();
        f_queryProduct = null;
        if (f_queryBPartner != null) f_queryBPartner.dispose();
        f_queryBPartner = null;
        if (f_queryTicket != null) f_queryTicket.dispose();
        f_queryTicket = null;
        if (f_cashfunctions != null) f_cashfunctions.dispose();
        f_cashfunctions = null;
        if (m_frame != null) m_frame.dispose();
        m_frame = null;
        m_ctx = null;
    }

    /**************************************************************************
	 * 	Dynamic Init.
	 * 	PosPanel has a GridBagLayout.
	 * 	The Sub Panels return their position
	 */
    private boolean dynInit() {
        if (!setMPOS()) return false;
        f_bpartner = new SubBPartner(this);
        add(f_bpartner, f_bpartner.getGridBagConstraints());
        f_salesRep = new SubSalesRep(this);
        add(f_salesRep, f_salesRep.getGridBagConstraints());
        f_curLine = new SubCurrentLine(this);
        add(f_curLine, f_curLine.getGridBagConstraints());
        f_product = new SubProduct(this);
        add(f_product, f_product.getGridBagConstraints());
        f_lines = new SubLines(this);
        add(f_lines, f_lines.getGridBagConstraints());
        f_functionKeys = new SubFunctionKeys(this);
        add(f_functionKeys, f_functionKeys.getGridBagConstraints());
        f_checkout = new SubCheckout(this);
        add(f_checkout, f_checkout.getGridBagConstraints());
        f_queryProduct = new QueryProduct(this);
        add(f_queryProduct, f_queryProduct.getGridBagConstraints());
        f_queryBPartner = new QueryBPartner(this);
        add(f_queryBPartner, f_queryBPartner.getGridBagConstraints());
        f_queryTicket = new QueryTicket(this);
        add(f_queryTicket, f_queryTicket.getGridBagConstraints());
        f_cashfunctions = new CashSubFunctions(this);
        add(f_cashfunctions, f_cashfunctions.getGridBagConstraints());
        newOrder();
        return true;
    }

    /**
	 * 	Set MPOS
	 *	@return true if found/set
	 */
    private boolean setMPOS() {
        MPOS[] poss = null;
        if (m_SalesRep_ID == 100) poss = getPOSs(0); else poss = getPOSs(m_SalesRep_ID);
        if (poss.length == 0) {
            ADialog.error(m_WindowNo, m_frame, "NoPOSForUser");
            return false;
        } else if (poss.length == 1) {
            p_pos = poss[0];
            return true;
        }
        String msg = Msg.getMsg(m_ctx, "SelectPOS");
        String title = Env.getHeader(m_ctx, m_WindowNo);
        Object selection = JOptionPane.showInputDialog(m_frame, msg, title, JOptionPane.QUESTION_MESSAGE, null, poss, poss[0]);
        if (selection != null) {
            p_pos = (MPOS) selection;
            ;
            return true;
        }
        return false;
    }

    /**
	 * 	Get POSs for specific Sales Rep or all
	 *	@param SalesRep_ID
	 *	@return array of POS
	 */
    private MPOS[] getPOSs(int SalesRep_ID) {
        ArrayList<MPOS> list = new ArrayList<MPOS>();
        String sql = "SELECT * FROM C_POS WHERE SalesRep_ID=?";
        if (SalesRep_ID == 0) sql = "SELECT * FROM C_POS WHERE AD_Client_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            if (SalesRep_ID != 0) pstmt.setInt(1, m_SalesRep_ID); else pstmt.setInt(1, Env.getAD_Client_ID(m_ctx));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MPOS(m_ctx, rs, null));
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MPOS[] retValue = new MPOS[list.size()];
        list.toArray(retValue);
        return retValue;
    }

    /**
	 * 	Set Visible
	 *	@param aFlag visible
	 */
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        f_product.f_name.requestFocus();
    }

    /**
	 * 	Open Query Window
	 *	@param panel
	 */
    public void openQuery(CPanel panel) {
        if (panel.equals(f_cashfunctions)) {
            f_bpartner.setVisible(false);
            f_salesRep.setVisible(false);
            f_curLine.setVisible(false);
            f_product.setVisible(false);
        }
        f_checkout.setVisible(false);
        f_lines.setVisible(false);
        f_functionKeys.setVisible(false);
        panel.setVisible(true);
    }

    /**
	 * 	Close Query Window
	 *	@param panel
	 */
    public void closeQuery(CPanel panel) {
        panel.setVisible(false);
        f_bpartner.setVisible(true);
        f_salesRep.setVisible(true);
        f_curLine.setVisible(true);
        f_product.setVisible(true);
        f_lines.setVisible(true);
        f_functionKeys.setVisible(true);
        f_checkout.setVisible(true);
    }

    /**************************************************************************
	 * 	Get Today's date
	 *	@return date
	 */
    public Timestamp getToday() {
        return m_today;
    }

    /**
	 * 	New Order
	 *   
	 */
    public void newOrder() {
        log.info("PosPabel.newOrder");
        f_bpartner.setC_BPartner_ID(0);
        f_curLine.newOrder();
        f_curLine.newLine();
        f_product.f_name.requestFocus();
        updateInfo();
    }

    /**
	 * Get the number of the window for the function calls that it needs 
	 * 
	 * @return the window number
	 */
    public int getWindowNo() {
        return m_WindowNo;
    }

    /**
	 * Get the properties for the process calls that it needs
	 * 
	 * @return las Propiedades m_ctx
	 */
    public Properties getPropiedades() {
        return m_ctx;
    }

    public void updateInfo() {
        if (f_lines != null) f_lines.updateTable(f_curLine.getOrder());
        if (f_checkout != null) f_checkout.displayReturn();
    }
}
