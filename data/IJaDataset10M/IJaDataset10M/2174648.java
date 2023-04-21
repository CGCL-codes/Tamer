package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import org.compiere.util.DB;

/**
 *	Alert Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAlert.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 * 
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 			<li>FR [ 1894573 ] Alert Processor Improvements
 */
public class MAlert extends X_AD_Alert {

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Alert_ID id
	 *	@param trxName transaction
	 */
    public MAlert(Properties ctx, int AD_Alert_ID, String trxName) {
        super(ctx, AD_Alert_ID, trxName);
        if (AD_Alert_ID == 0) {
            setEnforceClientSecurity(true);
            setEnforceRoleSecurity(true);
            setIsValid(true);
        }
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MAlert(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**	The Rules						*/
    private MAlertRule[] m_rules = null;

    /**	The Recipients					*/
    private MAlertRecipient[] m_recipients = null;

    /**
	 * 	Get Rules
	 *	@param reload reload data
	 *	@return array of rules
	 */
    public MAlertRule[] getRules(boolean reload) {
        if (m_rules != null && !reload) return m_rules;
        String sql = "SELECT * FROM AD_AlertRule " + "WHERE AD_Alert_ID=?";
        ArrayList<MAlertRule> list = new ArrayList<MAlertRule>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, getAD_Alert_ID());
            rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MAlertRule(getCtx(), rs, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
        m_rules = new MAlertRule[list.size()];
        list.toArray(m_rules);
        return m_rules;
    }

    /**
	 * 	Get Recipients
	 *	@param reload reload data
	 *	@return array of recipients
	 */
    public MAlertRecipient[] getRecipients(boolean reload) {
        if (m_recipients != null && !reload) return m_recipients;
        String sql = "SELECT * FROM AD_AlertRecipient " + "WHERE AD_Alert_ID=?";
        ArrayList<MAlertRecipient> list = new ArrayList<MAlertRecipient>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, getAD_Alert_ID());
            rs = pstmt.executeQuery();
            while (rs.next()) list.add(new MAlertRecipient(getCtx(), rs, null));
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
        m_recipients = new MAlertRecipient[list.size()];
        list.toArray(m_recipients);
        return m_recipients;
    }

    /**
	 * 	Get First Role if exist
	 *	@return AD_Role_ID or -1
	 */
    public int getFirstAD_Role_ID() {
        getRecipients(false);
        for (int i = 0; i < m_recipients.length; i++) {
            if (m_recipients[i].getAD_Role_ID() != -1) return m_recipients[i].getAD_Role_ID();
        }
        return -1;
    }

    /**
	 * 	Get First User Role if exist
	 *	@return AD_Role_ID or -1
	 */
    public int getFirstUserAD_Role_ID() {
        getRecipients(false);
        int AD_User_ID = getFirstAD_User_ID();
        if (AD_User_ID != -1) {
            MUserRoles[] urs = MUserRoles.getOfUser(getCtx(), AD_User_ID);
            for (int i = 0; i < urs.length; i++) {
                if (urs[i].isActive()) return urs[i].getAD_Role_ID();
            }
        }
        return -1;
    }

    /**
	 * 	Get First User if exist
	 *	@return AD_User_ID or -1
	 */
    public int getFirstAD_User_ID() {
        getRecipients(false);
        for (int i = 0; i < m_recipients.length; i++) {
            if (m_recipients[i].getAD_User_ID() != -1) return m_recipients[i].getAD_User_ID();
        }
        return -1;
    }

    /**
	 * @return unique list of recipient users
	 */
    public Collection<Integer> getRecipientUsers() {
        MAlertRecipient[] recipients = getRecipients(false);
        TreeSet<Integer> users = new TreeSet<Integer>();
        for (int i = 0; i < recipients.length; i++) {
            MAlertRecipient recipient = recipients[i];
            if (recipient.getAD_User_ID() >= 0) users.add(recipient.getAD_User_ID());
            if (recipient.getAD_Role_ID() >= 0) {
                MUserRoles[] urs = MUserRoles.getOfRole(getCtx(), recipient.getAD_Role_ID());
                for (int j = 0; j < urs.length; j++) {
                    MUserRoles ur = urs[j];
                    if (!ur.isActive()) continue;
                    users.add(ur.getAD_User_ID());
                }
            }
        }
        return users;
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("MAlert[");
        sb.append(get_ID()).append("-").append(getName()).append(",Valid=").append(isValid());
        if (m_rules != null) sb.append(",Rules=").append(m_rules.length);
        if (m_recipients != null) sb.append(",Recipients=").append(m_recipients.length);
        sb.append("]");
        return sb.toString();
    }
}
