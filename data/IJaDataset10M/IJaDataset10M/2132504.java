package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Organization Info Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MOrgInfo.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 */
public class MOrgInfo extends X_AD_OrgInfo {

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param AD_Org_ID id
	 *	@return Org Info
	 */
    public static MOrgInfo get(Properties ctx, int AD_Org_ID) {
        MOrgInfo retValue = null;
        String sql = "SELECT * FROM AD_OrgInfo WHERE AD_Org_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Org_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) retValue = new MOrgInfo(ctx, rs, null);
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        return retValue;
    }

    /** Static Logger					*/
    private static CLogger s_log = CLogger.getCLogger(MOrgInfo.class);

    /**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MOrgInfo(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Organization constructor
	 *	@param org org
	 */
    public MOrgInfo(MOrg org) {
        super(org.getCtx(), 0, org.get_TrxName());
        setClientOrg(org);
        setDUNS("?");
        setTaxID("?");
    }
}
