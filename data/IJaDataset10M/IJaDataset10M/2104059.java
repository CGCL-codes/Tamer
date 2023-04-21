package com.rooster.action.submission.external_candidate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.action.common.DbUtil;
import com.rooster.utils.UserPassword;

public class CopyCandidate extends Action {

    DataSource db;

    Logger logger = Logger.getLogger(CopyCandidate.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        Connection myCon = null;
        Statement state = null;
        ResultSet rs = null;
        String sRecruiterId = String.valueOf(session.getAttribute("UserId"));
        String slogCat = String.valueOf(session.getAttribute("logCategory"));
        String sWebsite = String.valueOf(session.getAttribute("WEBSITE"));
        String sClrJobId = req.getParameter("clrJobId");
        String sEmail = req.getParameter("email_id");
        boolean isExist = false;
        int iInserted = 0;
        try {
            String sSql = "select email_id from rooster_candidate_info where visible=1 and email_id='" + sEmail + "'";
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sSql);
            while (rs.next()) {
                isExist = true;
                break;
            }
            if (isExist) {
            } else {
                iInserted = CopyCandidate(sEmail, req);
                UpdatePassword(sEmail, req);
            }
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (state != null) {
                    state.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
        return map.findForward("enter_call");
    }

    private int CopyCandidate(String sEmailId, HttpServletRequest req) {
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        int iInserted = 0;
        try {
            String sSql = "insert into rooster_candidate_info " + "(email_id,first_name,middle_name,last_name,basic_skill,ssn,address_1,address_2," + "city,state,country,zip_code,phone_no,phone_no_type,relocation,pref_location,resume," + "resume_link,actual_path,training,title,marketable,rate_opt,barred,refered_by,tax_term," + "vendor_id,own_w2,work_auth,eadtxt,paystub,rate,en_summary,certification,exp_level,desired_title) " + "select email_id,first_name,middle_name,last_name,basic_skill,ssn,address_1,address_2," + "city,state,country,zip_code,phone_no,phone_no_type,relocation,pref_location,resume," + "resume_link,actual_path,training,title,marketable,rate_opt,barred,refered_by,tax_term," + "vendor_id,own_w2,work_auth,eadtxt,paystub,rate,en_summary,certification,exp_level,desired_title" + " from rooster_new_candidate_info where visible=1 and email_id='" + sEmailId + "'";
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            iInserted = state.executeUpdate(sSql);
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (state != null) {
                    state.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
        return iInserted;
    }

    private void UpdatePassword(String sEmailId, HttpServletRequest req) {
        Connection myCon = null;
        PreparedStatement state = null;
        String sPassword = UserPassword.getPassword();
        try {
            String sSql = "update rooster_candidate_info set password=? where visible=1 and email_id=?";
            String sSqlCreateLogin = "insert into req_userlogin (user_name, user_pass, category) select email_id, password, tax_term from rooster_candidate_info where email_id='" + sEmailId + "'";
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.prepareStatement(sSql);
            state.setString(1, sPassword);
            state.setString(2, sEmailId);
            state.addBatch();
            state.addBatch(sSqlCreateLogin);
            int[] iUpdated = state.executeBatch();
            logger.info(DbUtil.checkUpdateCounts(iUpdated));
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        } finally {
            try {
                if (state != null) {
                    state.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
    }
}
