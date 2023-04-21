package org.openXpertya.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.ecs.storage.Array;
import org.openXpertya.util.CCache;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Msg;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MPOS extends X_C_POS {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param C_POS_ID
     *
     * @return
     */
    public static MPOS get(Properties ctx, int C_POS_ID) {
        Integer key = new Integer(C_POS_ID);
        MPOS retValue = (MPOS) s_cache.get(key);
        if (retValue != null) {
            return retValue;
        }
        retValue = new MPOS(ctx, C_POS_ID, null);
        if (retValue.getID() != 0) {
            s_cache.put(key, retValue);
        }
        return retValue;
    }

    /**
	 * Obtengo las configuraciones de puntos de ventas para el usuario y
	 * organización parámetro
	 * 
	 * @param ctx
	 *            contexto
	 * @param orgID
	 *            id de organización
	 * @param userID
	 *            id de usuario
	 * @param trxName
	 *            nombre de la transacción
	 * @return lista con todas las configuraciones de punto de venta
	 */
    public static List<MPOS> get(Properties ctx, Integer orgID, Integer userID, String trxName) {
        if (orgID == null) {
            orgID = Env.getAD_Org_ID(ctx);
        }
        if (userID == null) {
            userID = Env.getAD_User_ID(ctx);
        }
        List<MPOS> poss = new ArrayList<MPOS>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM c_pos WHERE ad_org_id = ? AND salesrep_id = ? AND isactive = 'Y'";
        try {
            ps = DB.prepareStatement(sql, trxName);
            ps.setInt(1, orgID);
            ps.setInt(2, userID);
            rs = ps.executeQuery();
            while (rs.next()) {
                poss.add(new MPOS(ctx, rs, trxName));
            }
        } catch (Exception e) {
            s_log.severe("Error getting pos configurations. Get Query: " + ps.toString());
        } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (Exception e2) {
                s_log.severe("Error getting pos configurations. " + e2.getMessage());
            }
        }
        return poss;
    }

    /** Descripción de Campos */
    private static CCache s_cache = new CCache("C_POS", 20);

    private static CLogger s_log = CLogger.getCLogger(MPOS.class);

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param C_POS_ID
     * @param trxName
     */
    public MPOS(Properties ctx, int C_POS_ID, String trxName) {
        super(ctx, C_POS_ID, trxName);
        if (C_POS_ID == 0) {
            setIsModifyPrice(false);
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MPOS(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Descripción de Campos */
    private MBPartner m_template = null;

    /** Get AD_PrintLabel_ID.
    *
    * @return
   AD_PrintLabel_ID */
    public int getAD_PrintLabel_ID() {
        Integer ii = (Integer) get_Value("AD_PrintLabel_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /** Get AD_Window_ID.
    *
    * @return
   AD_Window_ID */
    public int getAD_Window_ID() {
        Integer ii = (Integer) get_Value("AD_Window_ID");
        if (ii == null) {
            return 0;
        }
        return ii.intValue();
    }

    /**
     * Descripción de Método
     *
     *
     * @param newRecord
     *
     * @return
     */
    protected boolean beforeSave(boolean newRecord) {
        if (newRecord || is_ValueChanged("C_CashBook_ID") || is_ValueChanged("M_Warehouse_ID")) {
            MCashBook cb = MCashBook.get(getCtx(), getC_CashBook_ID(), get_TrxName());
            if (cb.getAD_Org_ID() != getAD_Org_ID()) {
                log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@: @C_CashBook_ID@"));
                return false;
            }
            MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
            if (wh.getAD_Org_ID() != getAD_Org_ID()) {
                log.saveError("Error", Msg.parseTranslation(getCtx(), "@AD_Org_ID@: @M_Warehouse_ID@"));
                return false;
            }
        }
        if (CalloutInvoiceExt.ComprobantesFiscalesActivos() && !(getPOSNumber() > 0 && getPOSNumber() < 10000)) {
            log.saveError("SaveError", Msg.getMsg(getCtx(), "FieldValueOutOfRange", new Object[] { Msg.translate(getCtx(), "POSNumber"), 1, 9999 }));
            return false;
        }
        MBPartner bPartner = new MBPartner(getCtx(), getC_BPartnerCashTrx_ID(), get_TrxName());
        MBPartnerLocation[] locs = bPartner.getLocations(false);
        if (locs.length == 0) {
            log.saveError("SaveError", Msg.translate(getCtx(), "NoBPartnerLocationError"));
            return false;
        }
        if (!isSearchByName() && !isSearchByNameLike() && !isSearchByUPC() && !isSearchByUPCLike() && !isSearchByValue() && !isSearchByValueLike()) {
            log.saveError("SaveError", Msg.translate(getCtx(), "NeedPOSProductSearchOption"));
            return false;
        }
        if (!isCreateInvoice()) {
            set_ValueNoCheck("C_InvoiceDocType_ID", null);
        }
        if (getC_InoutDocType_ID() == 0) {
            setIsDeliverOrderInWarehouse(false);
        }
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public MBPartner getBPartner() {
        if (m_template == null) {
            if (getC_BPartnerCashTrx_ID() == 0) {
                m_template = MBPartner.getBPartnerCashTrx(getCtx(), getAD_Client_ID());
            } else {
                m_template = new MBPartner(getCtx(), getC_BPartnerCashTrx_ID(), get_TrxName());
            }
            log.fine("getBPartner - " + m_template);
        }
        return m_template;
    }
}
