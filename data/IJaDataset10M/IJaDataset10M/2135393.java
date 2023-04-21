package org.openXpertya.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import org.openXpertya.util.CCache;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MRegistrationAttribute extends X_A_RegistrationAttribute {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     *
     * @return
     */
    public static MRegistrationAttribute[] getAll(Properties ctx) {
        ArrayList list = new ArrayList();
        String sql = "SELECT * FROM A_RegistrationAttribute " + "WHERE AD_Client_ID=? " + "ORDER BY SeqNo";
        int AD_Client_ID = Env.getAD_Client_ID(ctx);
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, AD_Client_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MRegistrationAttribute value = new MRegistrationAttribute(ctx, rs, null);
                Integer key = new Integer(value.getA_RegistrationAttribute_ID());
                s_cache.put(key, value);
                list.add(value);
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, "getAll", e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MRegistrationAttribute[] retValue = new MRegistrationAttribute[list.size()];
        list.toArray(retValue);
        return retValue;
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param A_RegistrationAttribute_ID
     * @param trxName
     *
     * @return
     */
    public static MRegistrationAttribute get(Properties ctx, int A_RegistrationAttribute_ID, String trxName) {
        Integer key = new Integer(A_RegistrationAttribute_ID);
        MRegistrationAttribute retValue = (MRegistrationAttribute) s_cache.get(key);
        if (retValue == null) {
            retValue = new MRegistrationAttribute(ctx, A_RegistrationAttribute_ID, trxName);
            s_cache.put(key, retValue);
        }
        return retValue;
    }

    /** Descripción de Campos */
    private static CLogger s_log = CLogger.getCLogger(MRegistrationAttribute.class);

    /** Descripción de Campos */
    private static CCache s_cache = new CCache("A_RegistrationAttribute", 20);

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param A_RegistrationAttribute_ID
     * @param trxName
     */
    public MRegistrationAttribute(Properties ctx, int A_RegistrationAttribute_ID, String trxName) {
        super(ctx, A_RegistrationAttribute_ID, trxName);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MRegistrationAttribute(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}
