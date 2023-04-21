package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	User Roles Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MUserRoles.java,v 1.3 2006/07/30 00:58:37 jjanke Exp $
 */
public class MUserRoles extends X_AD_User_Roles {

    /**
	 * 	Get User Roles Of Role
	 *	@param ctx context
	 *	@param AD_Role_ID role
	 *	@return array of user roles
	 */
    public static MUserRoles[] getOfRole(Properties ctx, int AD_Role_ID) {
        String sql = "SELECT * FROM AD_User_Roles WHERE AD_Role_ID=?";
        ArrayList<MUserRoles> list = new ArrayList<MUserRoles>();
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Role_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MUserRoles(ctx, rs, null));
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, "getOfRole", e);
        }
        try {
            if (pstmt != null) pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MUserRoles[] retValue = new MUserRoles[list.size()];
        list.toArray(retValue);
        return retValue;
    }

    /**
	 * 	Get User Roles Of User
	 *	@param ctx context
	 *	@param AD_User_ID role
	 *	@return array of user roles
	 */
    public static MUserRoles[] getOfUser(Properties ctx, int AD_User_ID) {
        String sql = "SELECT * FROM AD_User_Roles WHERE AD_User_ID=?";
        ArrayList<MUserRoles> list = new ArrayList<MUserRoles>();
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_User_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MUserRoles(ctx, rs, null));
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, "getOfUser", e);
        }
        try {
            if (pstmt != null) pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        MUserRoles[] retValue = new MUserRoles[list.size()];
        list.toArray(retValue);
        return retValue;
    }

    /**	Static Logger	*/
    private static CLogger s_log = CLogger.getCLogger(MUserRoles.class);

    /**************************************************************************
	 * 	Persistence Constructor
	 *	@param ctx context
	 *	@param ignored invalid
	 *	@param trxName transaction
	 */
    public MUserRoles(Properties ctx, int ignored, String trxName) {
        super(ctx, ignored, trxName);
        if (ignored != 0) throw new IllegalArgumentException("Multi-Key");
    }

    /**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MUserRoles(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param AD_User_ID user
	 *	@param AD_Role_ID role
	 *	@param trxName transaction
	 */
    public MUserRoles(Properties ctx, int AD_User_ID, int AD_Role_ID, String trxName) {
        this(ctx, 0, trxName);
        setAD_User_ID(AD_User_ID);
        setAD_Role_ID(AD_Role_ID);
    }

    /** 
	 * 	Set User/Contact.
	 *	User within the system - Internal or Business Partner Contact
	 *	@param AD_User_ID user 
	 */
    public void setAD_User_ID(int AD_User_ID) {
        set_ValueNoCheck("AD_User_ID", new Integer(AD_User_ID));
    }

    /** 
	 * 	Set Role.
	 * 	Responsibility Role
	 * 	@param AD_Role_ID role 
	 **/
    public void setAD_Role_ID(int AD_Role_ID) {
        set_ValueNoCheck("AD_Role_ID", new Integer(AD_Role_ID));
    }
}
