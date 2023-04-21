package org.openXpertya.model;

import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

/**
 *      Cost Element Model
 *  @author Comunidad de Desarrollo openXpertya
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         * Jorg Janke
 *  @version $Id: MCostElement.java,v 1.2 2005/04/25 06:02:02 jjanke Exp $
 */
public class MCostElement extends X_M_CostElement {

    /** Logger */
    private static CLogger s_log = CLogger.getCLogger(MCostElement.class);

    /**
     *      Standard Constructor
     *      @param ctx context
     *      @param M_CostElement_ID id
     *      @param trxName trx
     */
    public MCostElement(Properties ctx, int M_CostElement_ID, String trxName) {
        super(ctx, M_CostElement_ID, trxName);
        if (M_CostElement_ID == 0) {
            setCostElementType(COSTELEMENTTYPE_Material);
            setIsCalculated(false);
        }
    }

    /**
     *      Load Constructor
     *      @param ctx context
     *      @param rs result set
     *      @param trxName trx
     */
    public MCostElement(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     *      Before Save
     *      @param newRecord new
     *      @return true
     */
    protected boolean beforeSave(boolean newRecord) {
        if (COSTELEMENTTYPE_Material.equals(getCostElementType())) {
            String cm = getCostingMethod();
            if ((cm == null) || (cm.length() == 0) || COSTINGMETHOD_StandardCosting.equals(cm)) {
                setIsCalculated(false);
            } else {
                setIsCalculated(true);
            }
        } else {
            setIsCalculated(false);
        }
        return true;
    }

    /**
     *      Get Material Cost Element or create it
     *      @param po parent
     *      @param CostingMethod method
     *      @return cost element
     */
    public static MCostElement getMaterialCostElement(PO po, String CostingMethod) {
        if ((CostingMethod == null) || (CostingMethod.length() == 0)) {
            s_log.severe("No CostingMethod");
            return null;
        }
        MCostElement retValue = null;
        String sql = "SELECT * FROM M_CostElement WHERE AD_Client_ID=? AND CostingMethod=? ORDER BY AD_Org_ID";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, po.getAD_Client_ID());
            pstmt.setString(2, CostingMethod);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = new MCostElement(po.getCtx(), rs, po.get_TrxName());
            }
            if (rs.next()) {
                s_log.warning("More then one Material Cost Element for CostingMethod=" + CostingMethod);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        if (retValue != null) {
            return retValue;
        }
        retValue = new MCostElement(po.getCtx(), 0, po.get_TrxName());
        retValue.setClientOrg(po.getAD_Client_ID(), 0);
        String name = MRefList.getListName(po.getCtx(), COSTINGMETHOD_AD_Reference_ID, CostingMethod);
        if ((name == null) || (name.length() == 0)) {
            name = CostingMethod;
        }
        retValue.setName(name);
        retValue.setCostElementType(COSTELEMENTTYPE_Material);
        retValue.setCostingMethod(CostingMethod);
        retValue.save();
        return retValue;
    }

    /**
     *      Get Material Cost Element or create it
     *      @param po parent
     *      @return cost element
     */
    public static MCostElement[] getMaterialCostElements(PO po) {
        ArrayList list = new ArrayList();
        String sql = "SELECT * FROM M_CostElement WHERE AD_Client_ID=? ORDER BY AD_Org_ID";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, po.getAD_Client_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MCostElement(po.getCtx(), rs, po.get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MCostElement[] retValue = new MCostElement[list.size()];
        list.toArray(retValue);
        return retValue;
    }
}
