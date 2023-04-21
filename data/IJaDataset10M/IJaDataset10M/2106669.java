package com.rooster.action.submission.external_candidate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.sun.rowset.CachedRowSetImpl;

public class ViewApplication extends Action {

    static Logger logger = Logger.getLogger(ViewApplication.class.getName());

    DataSource db;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        db = getDataSource(req);
        String logCat = (String) session.getAttribute("logCategory");
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sType = req.getParameter("type");
        String sSql = "";
        ArrayList list = null;
        if ((logCat.equals(new String("Admin"))) || (logCat.equals(new String("Lead")))) {
            if ((sType == null) || (sType.equals(new String("")))) {
            } else if (sType.equals(new String("TODAY"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and DATE(submitted_date) >= DATE(DATE_ADD(CURDATE(),INTERVAL -1 DAY)) ";
            } else if (sType.equals(new String("YESTERDAY"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and DATE(submitted_date) >= DATE(DATE_ADD(CURDATE(),INTERVAL -3 DAY)) ";
            } else if (sType.equals(new String("WEEK"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and DATE(submitted_date) >= DATE(DATE_ADD(CURDATE(),INTERVAL -7 DAY)) ";
            } else if (sType.equals(new String("TOTAL"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 ";
            }
        } else {
            if ((sType == null) || (sType.equals(new String("")))) {
            } else if (sType.equals(new String("TODAY"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and clrJobId in (select clrJobId from req_assigned where recruiterId=(select user_id from req_userinfo where email='" + sUserId + "')) and DATE(submitted_date) >= DATE(DATE_ADD(CURDATE(),INTERVAL -1 DAY)) ";
            } else if (sType.equals(new String("YESTERDAY"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and clrJobId in (select clrJobId from req_assigned where recruiterId=(select user_id from req_userinfo where email='" + sUserId + "')) and DATE(submitted_date) >= DATE(DATE_ADD(CURDATE(),INTERVAL -3 DAY)) ";
            } else if (sType.equals(new String("WEEK"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and clrJobId in (select clrJobId from req_assigned where recruiterId=(select user_id from req_userinfo where email='" + sUserId + "')) and DATE(submitted_date) >= DATE(DATE_ADD(CURDATE(),INTERVAL -7 DAY)) ";
            } else if (sType.equals(new String("TOTAL"))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from rooster_new_candidate_submission where visible=1 and clrJobId in (select clrJobId from req_assigned where recruiterId=(select user_id from req_userinfo where email='" + sUserId + "')) ";
            }
        }
        String sFinal = getHtml(sSql);
        req.setAttribute("list", sFinal);
        return map.findForward("view_app");
    }

    private String getHtml(String sSql) {
        CachedRowSet resumeRs = null;
        CachedRowSet rs = null;
        CachedRowSet baseRs1 = null;
        CachedRowSet baseRs2 = null;
        CachedRowSet innerRs = null;
        String sHtml = "";
        boolean bDataFound = false;
        boolean bSubmitted = false;
        ArrayList alEmail = new ArrayList();
        try {
            String sFirstSql1 = "select distinct clrJobId from " + sSql.substring(sSql.indexOf("rooster_new_candidate_submission")) + ";";
            baseRs1 = getRs(sFirstSql1);
            while (baseRs1.next()) {
                String sGroupByColumn1 = baseRs1.getString(1);
                if ((sGroupByColumn1 == null) || (sGroupByColumn1.equals(new String("")))) {
                    continue;
                }
                String sFirstSql2 = "select distinct req_state from " + sSql.substring(sSql.indexOf("rooster_new_candidate_submission")) + ";";
                baseRs2 = getRs(sFirstSql2);
                while (baseRs2.next()) {
                    String sGroupByColumn2 = baseRs2.getString(1);
                    if ((sGroupByColumn2 == null) || (sGroupByColumn2.equals(new String("")))) {
                        continue;
                    }
                    boolean bFound = false;
                    String sSecondary = "";
                    String sFinalSql = sSql + " and clrJobId = '" + sGroupByColumn1 + "' and req_state = '" + sGroupByColumn2 + "';";
                    rs = getRs(sFinalSql);
                    int iCount = 1;
                    while (rs.next()) {
                        bFound = true;
                        bDataFound = true;
                        bSubmitted = false;
                        String sEmailId = rs.getString("email_id");
                        String sInnerSql = "select email_id from rooster_candidate_info where visible=1 and email_id = '" + sEmailId + "';";
                        innerRs = getRs(sInnerSql);
                        while (innerRs.next()) {
                            bSubmitted = true;
                        }
                        String sResumeSql = "select resume_link from rooster_new_candidate_info where email_id='" + sEmailId + "';";
                        resumeRs = getRs(sResumeSql);
                        String sResumeLink = "";
                        while (resumeRs.next()) {
                            sResumeLink = resumeRs.getString(1);
                        }
                        if ((sResumeLink == null) || (sResumeLink.equals(new String("")))) {
                            sResumeLink = "";
                        }
                        if (iCount == 1) {
                            sSecondary += "<DL><DT><div class='smallbluetext'><a href='callinfo.do?clrJobId=" + sGroupByColumn1 + "' onmouseover=\"getRequirementDetailsWithJobId('" + sGroupByColumn1 + "')\">" + sGroupByColumn1 + "</a></div><DL><DT>";
                            sSecondary += "<DD><div class='smallbluetext'>" + sGroupByColumn2 + "</div><br><table align='center' cellpadding='1' cellspacing='0' class='tableBackground' width='800px'><tr><td><table id='demo1_table' cellpadding='3' cellspacing='1' border='0' width='800px' class='sortable'>";
                            sSecondary += "<th>Email Id</th><th>Req City</th><th>Req Zip</th><th>Received Date</th><th>Copy To Call Log</th>";
                            iCount++;
                        }
                        if (bSubmitted) {
                            sSecondary += "<tr><td><a href='external_consultant_info.do?consultant_id=" + rs.getString(1) + "&clrJobId=" + sGroupByColumn1 + "' onmouseover=\"getExternalCandidateDetails('" + rs.getString(1) + "')\">" + rs.getString(1) + "</a>";
                            if (!(sResumeLink.equals(new String("")))) {
                                sSecondary += "<img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Resume')\" onClick=\"showFile('" + sResumeLink + "');\" />";
                            }
                            sSecondary += "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td><a href=\"callinfo.do?clrJobId=" + sGroupByColumn1 + "&email_id=" + rs.getString(1) + "\">Enter Call Log</a></td></tr>";
                        } else {
                            sSecondary += "<tr><td><a href='external_consultant_info.do?consultant_id=" + rs.getString(1) + "&clrJobId=" + sGroupByColumn1 + "' onmouseover=\"getExternalCandidateDetails('" + rs.getString(1) + "')\">" + rs.getString(1) + "</a>";
                            if (!(sResumeLink.equals(new String("")))) {
                                sSecondary += "<img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Resume')\" onClick=\"showFile('" + sResumeLink + "');\" />";
                            }
                            sSecondary += "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td><td><a href=\"new_copy_to_calllog.do?clrJobId=" + sGroupByColumn1 + "&email_id=" + rs.getString(1) + "\">Copy To Call Log</a></td></tr>";
                        }
                    }
                    sSecondary += "</table></td></tr></table></DL></DL>";
                    if (bFound) {
                        sHtml += sSecondary;
                    } else {
                        sHtml += "<table id='demo1_table'></table>";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!(bDataFound)) {
            sHtml = "<table id='demo1_table'><tr><td align='center'><div class='smallbluetext'>No Data Found For Your Search Criteria.</div></td></tr></table>";
        }
        return sHtml;
    }

    private CachedRowSet getRs(String sSql) {
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        CachedRowSet crs = null;
        try {
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sSql);
            crs = new CachedRowSetImpl();
            crs.populate(rs);
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
            }
        }
        return crs;
    }
}
