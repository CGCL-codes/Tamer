package org.claros.intouch.common.utility;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.dbutils.QueryRunner;
import org.claros.commons.db.DbConfigList;
import org.claros.intouch.webmail.models.HotlistClientTbl;
import org.claros.intouch.webmail.models.Req_UserLogin;
import org.claros.intouch.webmail.models.RoosterCandidateInfo;
import org.claros.intouch.webmail.models.RoosterWebMailClientConfig;
import org.claros.intouch.webmail.models.RoosterWebmailLogin;
import com.jenkov.mrpersister.itf.IGenericDao;
import com.jenkov.mrpersister.util.JdbcUtil;
import com.rooster.constants.LoginConstants;
import com.rooster.utils.CurrentDate;

public class MailUtil {

    public static String getType(String sCategory) {
        String sType = null;
        if (sCategory.equalsIgnoreCase(LoginConstants.W2_HOURLY) || sCategory.equalsIgnoreCase(LoginConstants.W2_ANNUAL) || sCategory.equalsIgnoreCase(LoginConstants._1099) || sCategory.equalsIgnoreCase(LoginConstants.UNIDENTIFIED) || sCategory.equalsIgnoreCase(LoginConstants.NP_C2C) || sCategory.equalsIgnoreCase(LoginConstants.C2C)) {
            sType = "candidate";
        }
        if (sCategory.equalsIgnoreCase(LoginConstants.CLIENT)) {
            sType = "client";
        }
        return sType;
    }

    public static String ClearHTMLTags(String strHTML, int intWorkFlow) {
        Pattern pattern = null;
        Matcher matcher = null;
        String regex;
        String strTagLess = null;
        strTagLess = strHTML;
        if (intWorkFlow == -1) {
            regex = "<[^>]*>";
            pattern = pattern.compile(regex);
            strTagLess = pattern.matcher(strTagLess).replaceAll(" ");
        }
        if (intWorkFlow > 0 && intWorkFlow < 3) {
            regex = "[<]";
            pattern = pattern.compile(regex);
            strTagLess = pattern.matcher(strTagLess).replaceAll("<");
            regex = "[>]";
            pattern = pattern.compile(regex);
            strTagLess = pattern.matcher(strTagLess).replaceAll(">");
        }
        return strTagLess;
    }

    public static Req_UserLogin getEmailType(String sEmailId) throws Exception {
        IGenericDao dao = null;
        Req_UserLogin type = null;
        try {
            dao = Utility.getDbConnection();
            String sql = "SELECT category FROM req_userlogin WHERE user_name=? limit 1";
            type = (Req_UserLogin) dao.read(Req_UserLogin.class, sql, new Object[] { sEmailId });
        } finally {
            JdbcUtil.close(dao);
            dao = null;
        }
        return type;
    }

    public static RoosterCandidateInfo getCandidate(String sEmailId) throws Exception {
        IGenericDao dao = null;
        RoosterCandidateInfo type = null;
        try {
            dao = Utility.getDbConnection();
            String sql = "SELECT first_name, middle_name, last_name, state, city, zip_code, email_id, phone_no, basic_skill, tax_term, work_auth, rate FROM rooster_candidate_info WHERE email_id=? limit 1";
            type = (RoosterCandidateInfo) dao.read(RoosterCandidateInfo.class, sql, new Object[] { sEmailId });
        } finally {
            JdbcUtil.close(dao);
            dao = null;
        }
        return type;
    }

    public static void saveCandCallLog(RoosterCandidateInfo candObj, String sDate, String sMessage, String sUserId) throws Exception {
        QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
        try {
            String sql = "INSERT velrec_call_log (clrJobId, first_name, middle_name, last_name, state, city, zip_code, email_id, phone_no, basic_skill, recruiter_id, cand_reference, tax_term, work_auth, rate, req_comments, status, call_date, log_from, received_date) " + " values ('',?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '', ?, ?, ?, ?, 'Only Remarks', '" + CurrentDate.getDateWithTime() + "', 'email', ?)";
            run.update(sql, new Object[] { candObj.getFirst_name(), candObj.getMiddle_name(), candObj.getLast_name(), candObj.getState(), candObj.getCity(), candObj.getZip_code(), candObj.getEmail_id(), candObj.getPhone_no(), candObj.getBasic_skill(), sUserId, candObj.getTax_term(), candObj.getWork_auth(), candObj.getRate(), MailUtil.ClearHTMLTags(sMessage, -1), sDate });
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void saveClientCallLog(HotlistClientTbl clientObject, String sDate, String sMessage, String sUserId) throws Exception {
        QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
        try {
            String sql = "INSERT rooster_client_call_log (recruiter_id, client_name, client_id, client_email_id, client_contact_name, recruiter_comment, log_from, received_date) " + " values (?, ?, ?, ?, ?, ?, 'email', ?)";
            run.update(sql, new Object[] { sUserId, clientObject.getClient_name(), clientObject.getClient_id(), clientObject.getClient_mailid(), clientObject.getFirst_name(), MailUtil.ClearHTMLTags(sMessage, -1), sDate });
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void saveMailLoginDetails(RoosterWebmailLogin loginObject) throws Exception {
        QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
        try {
            String sql = "INSERT rooster_webmail_login (email_id, mail_username, mail_password) " + " values (?, ?, ?)";
            run.update(sql, new Object[] { loginObject.getEmail_id(), loginObject.getMail_username(), loginObject.getMail_password() });
        } catch (SQLException e) {
            throw e;
        }
    }

    public static RoosterWebmailLogin getMailLoginDetails(String sEmailId) throws Exception {
        IGenericDao dao = null;
        RoosterWebmailLogin type = null;
        try {
            dao = Utility.getDbConnection();
            String sql = "SELECT * FROM rooster_webmail_login WHERE email_id=? limit 1";
            type = (RoosterWebmailLogin) dao.read(RoosterWebmailLogin.class, sql, new Object[] { sEmailId });
        } finally {
            JdbcUtil.close(dao);
            dao = null;
        }
        return type;
    }

    public static void deleteMailLoginDetails(String sEmailId) throws Exception {
        QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
        try {
            String sql = "DELETE FROM rooster_webmail_login WHERE email_id = ?";
            run.update(sql, new Object[] { sEmailId });
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void updateMailLoginDetails(RoosterWebmailLogin loginObject) throws Exception {
        QueryRunner run = new QueryRunner(DbConfigList.getDataSourceById("file"));
        try {
            String sql = "update rooster_webmail_login set mail_username= ?, mail_password=? WHERE email_id = ?";
            run.update(sql, new Object[] { loginObject.getMail_username(), loginObject.getMail_password(), loginObject.getEmail_id() });
        } catch (SQLException e) {
            throw e;
        }
    }

    public static HotlistClientTbl getClientInfo(String sEmailId) throws Exception {
        IGenericDao dao = null;
        HotlistClientTbl type = null;
        try {
            dao = Utility.getDbConnection();
            String sql = "SELECT client_id, client_name, client_mailid, first_name FROM hotlist_client_tbl WHERE client_mailid=? limit 1";
            type = (HotlistClientTbl) dao.read(HotlistClientTbl.class, sql, new Object[] { sEmailId });
        } finally {
            JdbcUtil.close(dao);
            dao = null;
        }
        return type;
    }

    public static ArrayList<String> stringToAddressList(String str) throws Exception {
        if (str == null) return null;
        str = org.claros.commons.utility.Utility.extendedTrim(str, ",");
        StringTokenizer token = new StringTokenizer(str, ",");
        if (token.countTokens() != 0) {
            ArrayList<String> list = new ArrayList<String>(token.countTokens());
            int counter = 0;
            while (token.hasMoreTokens()) {
                String addr = token.nextToken().trim();
                addr = org.claros.commons.utility.Utility.replaceAllOccurances(addr, "&lt;", "<");
                addr = org.claros.commons.utility.Utility.replaceAllOccurances(addr, "&gt;", ">");
                String email = "";
                int j;
                try {
                    if ((j = addr.indexOf("<")) > 0) {
                        email = org.claros.commons.utility.Utility.extendedTrim(org.claros.commons.utility.Utility.extendedTrim(addr.substring(j + 1), ">"), "\"").trim();
                        list.add(email);
                    } else {
                        list.add(addr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                counter++;
            }
            return list;
        } else {
            return null;
        }
    }

    public static RoosterWebMailClientConfig getServerConfig() throws Exception {
        IGenericDao dao = null;
        RoosterWebMailClientConfig type = null;
        try {
            dao = Utility.getDbConnection();
            String sql = "SELECT * FROM rooster_web_mail_client_config limit 1";
            type = (RoosterWebMailClientConfig) dao.read(RoosterWebMailClientConfig.class, sql, new Object[] {});
        } finally {
            JdbcUtil.close(dao);
            dao = null;
        }
        return type;
    }
}
