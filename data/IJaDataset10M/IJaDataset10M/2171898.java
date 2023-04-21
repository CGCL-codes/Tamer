package org.openXpertya.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.openXpertya.plugin.common.PluginUtils;
import org.openXpertya.util.CCache;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.TimeUtil;
import org.openXpertya.util.Util;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MUser extends X_AD_User {

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param C_BPartner_ID
     *
     * @return
     */
    public static MUser[] getOfBPartner(Properties ctx, int C_BPartner_ID) {
        ArrayList list = new ArrayList();
        String sql = "SELECT * FROM AD_User WHERE C_BPartner_ID=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, C_BPartner_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MUser(ctx, rs, null));
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
        MUser[] retValue = new MUser[list.size()];
        list.toArray(retValue);
        return retValue;
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param AD_User_ID
     *
     * @return
     */
    public static MUser get(Properties ctx, int AD_User_ID) {
        Integer key = new Integer(AD_User_ID);
        MUser retValue = (MUser) s_cache.get(key);
        if (retValue == null) {
            retValue = new MUser(ctx, AD_User_ID, null);
            if (AD_User_ID == 0) {
                String trxName = null;
                retValue.load(trxName);
            }
            s_cache.put(key, retValue);
        }
        return retValue;
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     *
     * @return
     */
    public static MUser get(Properties ctx) {
        return get(ctx, Env.getAD_User_ID(ctx));
    }

    /**
     * Descripción de Método
     *
     *
     * @param ctx
     * @param name
     * @param password
     *
     * @return
     */
    public static MUser get(Properties ctx, String name, String password) {
        return get(ctx, name, password, true);
    }

    public static MUser get(Properties ctx, String name, String password, boolean controlEmpty) {
        return get(ctx, name, password, controlEmpty, true);
    }

    /**
	 * Obtiene un usuario determinado por nombre de usuario y password
	 * 
	 * @param ctx
	 *            contexto
	 * @param name
	 *            nombre de usuario o null caso que se obtenga solo por clave
	 * @param password
	 *            clave de usuario
	 * @param controlEmpty
	 *            true si se debe controlar que los valores de usuario y clave
	 *            no se pasen vacíos, false si no hay que verificar
	 * @param controlPasswordWithUser
	 *            true si se debe controlar que el password venga
	 *            obligatoriamente acompañado de la clave, false si no hay que
	 *            verificar
	 * @return usuario correspondiente a los datos parámetro, null caso que no
	 *         exista o no pase alguna de las validaciones descritas
	 */
    public static MUser get(Properties ctx, String name, String password, boolean controlEmpty, boolean controlPasswordWithUser) {
        boolean isUserName = !Util.isEmpty(name, true);
        boolean isPassword = !Util.isEmpty(password, true);
        if (controlEmpty && (!isUserName || !isPassword)) {
            s_log.severe("Invalid Name/Password = " + name + "/" + password);
            return null;
        }
        if (controlPasswordWithUser && (isUserName && !isPassword)) {
            s_log.severe("Invalid Name/Password = " + name + "/" + password);
            return null;
        }
        int AD_Client_ID = Env.getAD_Client_ID(ctx);
        MUser retValue = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM AD_User WHERE IsActive='Y' AND AD_Client_ID=?");
        if (isUserName) {
            sql.append(" AND Name='").append(name).append("'");
        }
        if (isPassword) {
            sql.append(" AND Password='").append(password).append("'");
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql.toString());
            int i = 1;
            pstmt.setInt(i++, AD_Client_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                retValue = new MUser(ctx, rs, null);
                if (rs.next()) {
                    s_log.warning("More then one user with Name/Password = " + name);
                }
            } else {
                s_log.fine("No record");
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            s_log.log(Level.SEVERE, sql.toString(), e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        return retValue;
    }

    /**
     * Get users from client id
     * @param C_Client_ID client id
     * @return array of users of that client
     */
    public static MUser[] getOfClient(Properties ctx, String trxName) {
        ArrayList users = new ArrayList();
        String sql = "SELECT * FROM ad_user WHERE ad_client_id = ?";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt(1, Env.getAD_Client_ID(ctx));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new MUser(ctx, rs, trxName));
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
        MUser[] retValue = new MUser[users.size()];
        users.toArray(retValue);
        return retValue;
    }

    /**
	 * @return true si la clave expiró, false caso contrario
	 */
    public static boolean isPasswordExpired(Properties ctx, Integer userID, String trxName) {
        MClientInfo clientInfo = MClientInfo.get(ctx, Env.getAD_Client_ID(ctx), trxName);
        if (!clientInfo.isPasswordExpirationActive()) {
            return false;
        }
        Object lastDate = DB.getSQLObject(PluginUtils.getPluginInstallerTrxName(), "SELECT lastpasswordchangedate FROM ad_user WHERE ad_user_id = ?", new Object[] { userID });
        Timestamp expirationDate = TimeUtil.addDays(new Timestamp(((Date) lastDate).getTime()), clientInfo.getPasswordExpirationDays());
        Timestamp today = Env.getDate();
        if (today.compareTo(expirationDate) >= 0) {
            return true;
        }
        return false;
    }

    /**
	 * Retorna la contraseña actual del usuario parámetro
	 * 
	 * @param ctx
	 *            contexto
	 * @param userID
	 *            id de usuario
	 * @param trxName
	 *            transacción actual
	 * @return el password del usuario actual
	 */
    public static String getCurrentPassword(Properties ctx, Integer userID, String trxName) {
        return DB.getSQLValueString(trxName, "SELECT password FROM ad_user WHERE ad_user_id = ?", userID);
    }

    /** Descripción de Campos */
    private static CCache s_cache = new CCache("AD_User", 30, 60);

    /** Descripción de Campos */
    private static CLogger s_log = CLogger.getCLogger(MUser.class);

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param AD_User_ID
     * @param trxName
     */
    public MUser(Properties ctx, int AD_User_ID, String trxName) {
        super(ctx, AD_User_ID, trxName);
        if (AD_User_ID == 0) {
            setIsLDAPAuthorized(false);
            setNotificationType(NOTIFICATIONTYPE_EMail);
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param partner
     */
    public MUser(MBPartner partner) {
        this(partner.getCtx(), 0, partner.get_TrxName());
        setClientOrg(partner);
        setC_BPartner_ID(partner.getC_BPartner_ID());
        setName(partner.getName());
        setPassword(null);
        setDescription(null);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MUser(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Descripción de Campos */
    private MRole[] m_roles = null;

    /** Descripción de Campos */
    private int m_rolesAD_Org_ID = -1;

    /** Descripción de Campos */
    private Boolean m_isAdministrator = null;

    /**
     * Descripción de Método
     *
     *
     * @param description
     */
    public void addDescription(String description) {
        if ((description == null) || (description.length() == 0)) {
            return;
        }
        String descr = getDescription();
        if ((descr == null) || (descr.length() == 0)) {
            setDescription(description);
        } else {
            setDescription(descr + " - " + description);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("MUser[").append(getID()).append(",Name=").append(getName()).append(",EMailUserID=").append(getEMailUser()).append("]");
        return sb.toString();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isOnline() {
        if ((getEMail() == null) || (getPassword() == null)) {
            return false;
        }
        return true;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public InternetAddress getInternetAddress() {
        String email = getEMail();
        if ((email == null) || (email.length() == 0)) {
            return null;
        }
        try {
            InternetAddress ia = new InternetAddress(email, true);
            if (ia != null) {
                ia.validate();
            }
            return ia;
        } catch (AddressException ex) {
            log.warning(email + " - " + ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Descripción de Método
     *
     *
     * @param ia
     *
     * @return
     */
    private String validateEmail(InternetAddress ia) {
        if (ia == null) {
            return "NoEmail";
        }
        if (true) {
            return null;
        }
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
        try {
            DirContext ctx = new InitialDirContext(env);
            Attributes atts = ctx.getAttributes("dns://admin.openXpertya.org", new String[] { "ES" });
            NamingEnumeration en = atts.getAll();
            while (en.hasMore()) {
                System.out.println(en.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return null;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isEMailValid() {
        return validateEmail(getInternetAddress()) == null;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String getEMailVerifyCode() {
        long code = getAD_User_ID() + getName().hashCode();
        return "C" + String.valueOf(Math.abs(code)) + "C";
    }

    /**
     * Descripción de Método
     *
     *
     * @param code
     * @param info
     *
     * @return
     */
    public boolean setEMailVerifyCode(String code, String info) {
        boolean ok = (code != null) && code.equals(getEMailVerifyCode());
        if (ok) {
            setEMailVerifyDate(new Timestamp(System.currentTimeMillis()));
        } else {
            setEMailVerifyDate(null);
        }
        setEMailVerify(info);
        return ok;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isEMailVerified() {
        return (getEMailVerifyDate() != null) && (getEMailVerify() != null) && (getEMailVerify().length() > 0);
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isNotificationEMail() {
        String s = getNotificationType();
        return (s == null) || NOTIFICATIONTYPE_EMail.equals(s);
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isNotificationNote() {
        String s = getNotificationType();
        return (s != null) && NOTIFICATIONTYPE_Notice.equals(s);
    }

    /**
     * Descripción de Método
     *
     *
     * @param AD_Org_ID
     *
     * @return
     */
    public MRole[] getRoles(int AD_Org_ID) {
        if ((m_roles != null) && (m_rolesAD_Org_ID == AD_Org_ID)) {
            return m_roles;
        }
        ArrayList list = new ArrayList();
        String sql = "SELECT * FROM AD_Role r " + "WHERE r.IsActive='Y'" + " AND EXISTS (SELECT * FROM AD_Role_OrgAccess ro" + " WHERE r.AD_Role_ID=ro.AD_Role_ID AND ro.IsActive='Y' AND ro.AD_Org_ID=?)" + " AND EXISTS (SELECT * FROM AD_User_Roles ur" + " WHERE r.AD_Role_ID=ur.AD_Role_ID AND ur.IsActive='Y' AND ur.AD_User_ID=?) " + "ORDER BY AD_Role_ID";
        PreparedStatement pstmt = null;
        try {
            pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, AD_Org_ID);
            pstmt.setInt(2, getAD_User_ID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MRole(getCtx(), rs, get_TrxName()));
            }
            rs.close();
            pstmt.close();
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, sql, e);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            pstmt = null;
        }
        m_rolesAD_Org_ID = AD_Org_ID;
        m_roles = new MRole[list.size()];
        list.toArray(m_roles);
        return m_roles;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isAdministrator() {
        if (m_isAdministrator == null) {
            m_isAdministrator = Boolean.FALSE;
            MRole[] roles = getRoles(0);
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].getAD_Role_ID() == 0) {
                    m_isAdministrator = Boolean.TRUE;
                    break;
                }
            }
        }
        return m_isAdministrator.booleanValue();
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
        if (!newRecord && is_ValueChanged("EMail")) {
            setEMailVerifyDate(null);
        }
        StringBuffer sql = new StringBuffer("SELECT ad_user_id FROM ad_user WHERE name = ?");
        if (!newRecord) {
            sql.append(" AND ad_user_id <> " + getID());
        }
        int sameUser = DB.getSQLValue(get_TrxName(), sql.toString(), getName());
        if (sameUser >= 0) {
            log.saveError("UserNameAlreadyInUse", "");
            return false;
        }
        if (Util.isEmpty(getPassword(), true)) {
            log.saveError("PasswordInvalidError", "");
            return false;
        }
        sql = new StringBuffer("SELECT ad_user_id FROM ad_user WHERE password = ?");
        if (!newRecord) {
            sql.append(" AND ad_user_id <> " + getID());
        }
        int samePass = DB.getSQLValue(get_TrxName(), sql.toString(), getPassword());
        if (samePass >= 0) {
            log.saveError("PasswordInvalidError", "");
            return false;
        }
        return true;
    }

    @Override
    protected boolean afterSave(boolean newRecord, boolean success) {
        String expirationActive = Env.getContext(getCtx(), "#PasswordExpirationActive");
        if (expirationActive.equals("Y") && is_ValueChanged("Password")) {
            DB.executeUpdate("UPDATE ad_user SET lastpasswordchangedate = current_date WHERE ad_user_id = " + getID(), get_TrxName());
        }
        return true;
    }

    public static void clearCache() {
        s_cache.clear();
    }
}
