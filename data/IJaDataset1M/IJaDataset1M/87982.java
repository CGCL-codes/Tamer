package com.rooster.action.call_log;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.utils.CurrentDate;
import com.rooster.utils.candidate.ReqDocAttach;
import com.sun.rowset.CachedRowSetImpl;

public class GetReadyForSubmission extends Action {

    static Logger logger = Logger.getLogger(GetReadyForSubmission.class.getName());

    DataSource db;

    int iReadyCount;

    int iDirectCount;

    int iC2CCount;

    int iHotlistCount;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        boolean bSessionTimeOut = false;
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            bSessionTimeOut = true;
        }
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sUserCat = String.valueOf(session.getAttribute("logCategory"));
        db = getDataSource(req);
        String sOutPut = "";
        String sEmailId = "";
        String sInput = req.getParameter("input");
        String sType = req.getParameter("type");
        String sClrJobId = "";
        if (!bSessionTimeOut) {
            if (sType.equals(new String("screen"))) {
                sClrJobId = sInput;
                String sDirectCandidate = getDirectCandidates(sClrJobId, sUserId, sUserCat);
                String sC2CCandidate = getC2CCandidates(sClrJobId, sUserId, sUserCat);
                String sHotlistCandidate = getHotlistCandidates(sClrJobId, sUserId, sUserCat);
                sOutPut += "<div id=\"DHTMLSuite_readyForSubmissionTab\">";
                sOutPut += "<div class=\"DHTMLSuite_aTab\" align=\"left\">";
                if (sDirectCandidate.equals(new String(""))) {
                    sOutPut += "<table id='" + sClrJobId + "_rfs_dir_table'><tr><td>";
                    sOutPut += "</td></tr></table><div id='pageNav" + sClrJobId + "_rfs_dir_table' align='center'></div>";
                } else {
                    sOutPut += "<table class='tableBackground'><tr><td><table cellspacing='1' cellpadding='1' class='innerTable' id='" + sClrJobId + "_rfs_dir_table' width='800px'><tr><th width='20%'>Review And Submit To Client</th><th width='30%'>Email Id</th><th width='15%'>Req City</th><th width='15%'>Req Zip</th><th width='20%'>Received Date</th></tr>";
                    sOutPut += sDirectCandidate;
                    sOutPut += "</table></td></tr></table><div id='pageNav" + sClrJobId + "_rfs_dir_table' align='left'></div>";
                }
                sOutPut += "</div><div class=\"DHTMLSuite_aTab\" align=\"left\">";
                if (sC2CCandidate.equals(new String(""))) {
                    sOutPut += "<table id='" + sClrJobId + "_rfs_c2c_table'><tr><td>";
                    sOutPut += "</td></tr></table><div id='pageNav" + sClrJobId + "_rfs_c2c_table' align='center'></div>";
                } else {
                    sOutPut += "<table class='tableBackground'><tr><td><table cellspacing='1' cellpadding='1' class='innerTable' id='" + sClrJobId + "_rfs_c2c_table' width='800px'><tr><th width='20%'>Review And Submit To Client</th><th width='30%'>Email Id</th><th width='15%'>Req City</th><th width='15%'>Req Zip</th><th width='20%'>Received Date</th></tr>";
                    sOutPut += sC2CCandidate;
                    sOutPut += "</table></td></tr></table><div id='pageNav" + sClrJobId + "_rfs_c2c_table' align='left'></div>";
                }
                sOutPut += "</div><div class=\"DHTMLSuite_aTab\" align=\"left\">";
                if (sHotlistCandidate.equals(new String(""))) {
                    sOutPut += "<table id='" + sClrJobId + "_rfs_hot_table'><tr><td>";
                    sOutPut += "</td></tr></table><div id='pageNav" + sClrJobId + "_rfs_hot_table' align='center'></div>";
                } else {
                    sOutPut += "<table class='tableBackground'><tr><td><table cellspacing='1' cellpadding='1' class='innerTable' id='" + sClrJobId + "_rfs_hot_table' width='800px'><tr><th width='20%'>Review And Submit To Client</th><th width='30%'>Email Id</th><th width='15%'>Req City</th><th width='15%'>Req Zip</th><th width='20%'>Received Date</th></tr>";
                    sOutPut += sHotlistCandidate;
                    sOutPut += "</table></td></tr></table><div id='pageNav" + sClrJobId + "_rfs_hot_table' align='left'></div>";
                }
                sOutPut += "</div>";
            } else {
                sEmailId = sInput;
            }
        } else {
            sOutPut = "TIMEOUT";
        }
        try {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            if (!bSessionTimeOut) {
                iReadyCount = iDirectCount + iC2CCount + iHotlistCount;
                if (iReadyCount == 0) {
                    if (sType.equals(new String("screen"))) {
                        sOutPut = "<span class='infotext'>No Candidate's Are Ready For Submission For This Requirement.</span>";
                    }
                } else if (sType.equals(new String("screen"))) {
                    sOutPut = iReadyCount + "***" + iDirectCount + "***" + iC2CCount + "***" + iHotlistCount + "***" + sOutPut;
                }
                out.write(sOutPut);
                out.flush();
                out.close();
            } else {
                out.write(sOutPut);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            logger.debug(e);
        }
        return null;
    }

    private String getDirectCandidates(String sClrJobId, String sUserId, String logCat) {
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        CachedRowSet rejectedRs = null;
        CachedRowSet submittedRs = null;
        CachedRowSet callLogRs = null;
        CachedRowSet resumeRs = null;
        String sSecondary = "";
        try {
            myCon = db.getConnection();
            state = myCon.createStatement();
            String sSql = "";
            if ((logCat.equals(new String("Admin"))) || (logCat.equals(new String("Lead")))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from velrec_candidate_submission_made where visible=1 and clrJobId='" + sClrJobId + "';";
            } else {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from velrec_candidate_submission_made where visible=1 and clrJobId in (select clrJobId from req_assigned where clrJobId='" + sClrJobId + "' and recruiterId=(select user_id from req_userinfo where email='" + sUserId + "'))";
            }
            System.out.println("sSql:::" + sSql);
            rs = state.executeQuery(sSql);
            String sDocLink = "";
            iDirectCount = 0;
            while (rs.next()) {
                String sResumeSql = "select resume_link from rooster_candidate_info where email_id='" + rs.getString(1) + "';";
                System.out.println("sResumeSql:::" + sResumeSql);
                resumeRs = getRs(sResumeSql);
                String sResumeLink = "";
                while (resumeRs.next()) {
                    sResumeLink = resumeRs.getString(1);
                }
                String sRejectedSql = "select recruiter_id from rooster_candidates_rejected_for_req where visible=1 and clrJobId = '" + sClrJobId + "' and email_id = '" + rs.getString(1) + "';";
                System.out.println("sRejectedSql:::" + sRejectedSql);
                rejectedRs = getRs(sRejectedSql);
                boolean bRejected = false;
                while (rejectedRs.next()) {
                    bRejected = true;
                }
                if (bRejected) {
                } else {
                    String sSubmittedSql = "select last_updated_at from rooster_final_client_submission where visible=1 and clrJobId = '" + sClrJobId + "' and candidate_email_id = '" + rs.getString(1) + "';";
                    System.out.println("sSubmittedSql:::" + sSubmittedSql);
                    submittedRs = getRs(sSubmittedSql);
                    String sUpdatedAt = "";
                    boolean bSubmitted = false;
                    while (submittedRs.next()) {
                        bSubmitted = true;
                        sUpdatedAt = submittedRs.getString(1);
                    }
                    String sCallStatus = "";
                    String sCheckCallLogSql = "select status from velrec_call_log where visible=1 and clrJobId = '" + sClrJobId + "' and email_id = '" + rs.getString(1) + "';";
                    System.out.println("sCheckCallLogSql:::" + sCheckCallLogSql);
                    callLogRs = getRs(sCheckCallLogSql);
                    while (callLogRs.next()) {
                        sCallStatus = callLogRs.getString(1);
                    }
                    if (bSubmitted) {
                    } else if ((!bSubmitted) && ((sCallStatus.equalsIgnoreCase(new String("Call Successfull"))) || (sCallStatus.equalsIgnoreCase(new String("Only Remarks"))))) {
                        iDirectCount++;
                        sSecondary += "<tr><td><a onclick='paneSplitter.addContent(\"center\", new DHTMLSuite.paneSplitterContentModel( { id:\"ViewCandidateApplication\",htmlElementId:\"ViewCandidateApplicationID\",contentUrl:\"rfs_show_cand_sub_page.do?clrJobId=" + sClrJobId + "&email_id=" + rs.getString(1) + "\",title:\"View Application\",tabTitle: \"Candidate Application\",closable:true } ) );paneSplitter.showContent(\"ViewCandidateApplication\");dojo.addOnLoad(init);' style=\"cursor: pointer;\">Submit</a> / <a style=\"{cursor:pointer}\" onclick=\"blackListCandidate('" + rs.getString(1) + "')\">Reject</a></td><td><a href='search_default_call_log.do?sp_search_email_id=" + rs.getString(1) + "' onmouseover=\"getCandidateDetails('" + rs.getString(1) + "')\">" + rs.getString(1) + "</a>" + "<a onmouseover=\"Tip('Attach Candidate To Requirement')\" style=\"{cursor:pointer}\" onclick=\"ATTACH_CANDIDATE_NS.show_attach_candidate_dialog('" + rs.getString(1) + "')\">[AC]</a><img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Resume')\" onClick=\"showFile('" + sResumeLink + "');\" />";
                        sDocLink = ReqDocAttach.getReqDocLink(db, sClrJobId, rs.getString(1), "direct");
                        if (!sDocLink.equals(new String(""))) {
                            sSecondary += "<img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Document')\" onClick=\"showFile('" + sDocLink + "');\" />";
                        }
                        sSecondary += "</td>" + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td></tr>";
                    }
                }
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
            }
        }
        return sSecondary;
    }

    private String getC2CCandidates(String sClrJobId, String sUserId, String logCat) {
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        CachedRowSet rejectedRs = null;
        CachedRowSet submittedRs = null;
        CachedRowSet callLogRs = null;
        CachedRowSet resumeRs = null;
        String sSecondary = "";
        try {
            myCon = db.getConnection();
            state = myCon.createStatement();
            String sSql = "";
            if ((logCat.equals(new String("Admin"))) || (logCat.equals(new String("Lead")))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from vendor_consultant_submission where visible=1 and candidate_approved=1 and vendor_approved=1 and clrJobId='" + sClrJobId + "';";
            } else {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from vendor_consultant_submission where visible=1 and candidate_approved=1 and vendor_approved=1 and clrJobId in (select clrJobId from req_assigned where recruiterId=(select user_id from req_userinfo where email='" + sUserId + "'))";
            }
            System.out.println("sSql:::" + sSql);
            rs = state.executeQuery(sSql);
            String sDocLink = "";
            iC2CCount = 0;
            while (rs.next()) {
                String sResumeSql = "select resume_link from rooster_candidate_info where email_id='" + rs.getString(1) + "';";
                System.out.println("sResumeSql:::" + sResumeSql);
                resumeRs = getRs(sResumeSql);
                String sResumeLink = "";
                while (resumeRs.next()) {
                    sResumeLink = resumeRs.getString(1);
                }
                String sRejectedSql = "select recruiter_id from rooster_candidates_rejected_for_req where visible=1 and clrJobId = '" + sClrJobId + "' and email_id = '" + rs.getString(1) + "';";
                System.out.println("sRejectedSql:::" + sRejectedSql);
                rejectedRs = getRs(sRejectedSql);
                boolean bRejected = false;
                while (rejectedRs.next()) {
                    bRejected = true;
                }
                if (bRejected) {
                } else {
                    String sSubmittedSql = "select last_updated_at from rooster_final_client_submission where visible=1 and clrJobId = '" + sClrJobId + "' and candidate_email_id = '" + rs.getString(1) + "';";
                    System.out.println("sSubmittedSql:::" + sSubmittedSql);
                    submittedRs = getRs(sSubmittedSql);
                    String sUpdatedAt = "";
                    boolean bSubmitted = false;
                    while (submittedRs.next()) {
                        bSubmitted = true;
                        sUpdatedAt = submittedRs.getString(1);
                    }
                    String sCallStatus = "";
                    String sCheckCallLogSql = "select status from velrec_call_log where visible=1 and clrJobId = '" + sClrJobId + "' and email_id = '" + rs.getString(1) + "';";
                    System.out.println("sCheckCallLogSql:::" + sCheckCallLogSql);
                    callLogRs = getRs(sCheckCallLogSql);
                    while (callLogRs.next()) {
                        sCallStatus = callLogRs.getString(1);
                    }
                    if (bSubmitted) {
                    } else if ((!bSubmitted) && ((sCallStatus.equalsIgnoreCase(new String("Call Successfull"))) || (sCallStatus.equalsIgnoreCase(new String("Only Remarks"))))) {
                        iC2CCount++;
                        sSecondary += "<tr><td><a onclick='paneSplitter.addContent(\"center\", new DHTMLSuite.paneSplitterContentModel( { id:\"ViewCandidateApplication\",htmlElementId:\"ViewCandidateApplicationID\",contentUrl:\"rfs_vendor_show_cand_sub_page.do?clrJobId=" + sClrJobId + "&email_id=" + rs.getString(1) + "\",title:\"View Application\",tabTitle: \"Candidate Application\",closable:true } ) );paneSplitter.showContent(\"ViewCandidateApplication\");dojo.addOnLoad(init);' style=\"cursor: pointer;\">Submit</a> / <a style=\"{cursor:pointer}\" onclick=\"blackListCandidate('" + rs.getString(1) + "')\">Reject</a></td><td><a href='search_default_call_log.do?sp_search_email_id=" + rs.getString(1) + "' onmouseover=\"getCandidateDetails('" + rs.getString(1) + "')\">" + rs.getString(1) + "</a>" + "<a onmouseover=\"Tip('Attach Candidate To Requirement')\" style=\"{cursor:pointer}\" onclick=\"ATTACH_CANDIDATE_NS.show_attach_candidate_dialog('" + rs.getString(1) + "')\">[AC]</a><img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Resume')\" onClick=\"showFile('" + sResumeLink + "');\" />";
                        sDocLink = ReqDocAttach.getReqDocLink(db, sClrJobId, rs.getString(1), "direct");
                        if (!sDocLink.equals(new String(""))) {
                            sSecondary += "<img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Document')\" onClick=\"showFile('" + sDocLink + "');\" />";
                        }
                        sSecondary += "</td>" + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td></tr>";
                    }
                }
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
            }
        }
        return sSecondary;
    }

    private String getHotlistCandidates(String sClrJobId, String sUserId, String logCat) {
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        CachedRowSet rejectedRs = null;
        CachedRowSet submittedRs = null;
        CachedRowSet callLogRs = null;
        CachedRowSet resumeRs = null;
        String sSecondary = "";
        try {
            myCon = db.getConnection();
            state = myCon.createStatement();
            String sSql = "";
            if ((logCat.equals(new String("Admin"))) || (logCat.equals(new String("Lead")))) {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from hotlist_consultant_submission where visible=1 and candidate_approved=1 and recruiter_approved=0 and clrJobId='" + sClrJobId + "';";
            } else {
                sSql = "select email_id,req_city,req_zip_code,submitted_date from hotlist_consultant_submission where visible=1 and candidate_approved=1 and recruiter_approved=0 and clrJobId in (select clrJobId from req_assigned where recruiterId=(select user_id from req_userinfo where email='" + sUserId + "'))";
            }
            System.out.println("sSql:::" + sSql);
            rs = state.executeQuery(sSql);
            String sDocLink = "";
            iHotlistCount = 0;
            while (rs.next()) {
                String sResumeSql = "select resume_link from rooster_candidate_info where email_id='" + rs.getString(1) + "';";
                System.out.println("sResumeSql:::" + sResumeSql);
                resumeRs = getRs(sResumeSql);
                String sResumeLink = "";
                while (resumeRs.next()) {
                    sResumeLink = resumeRs.getString(1);
                }
                String sRejectedSql = "select recruiter_id from rooster_candidates_rejected_for_req where visible=1 and clrJobId = '" + sClrJobId + "' and email_id = '" + rs.getString(1) + "';";
                System.out.println("sRejectedSql:::" + sRejectedSql);
                rejectedRs = getRs(sRejectedSql);
                boolean bRejected = false;
                while (rejectedRs.next()) {
                    bRejected = true;
                }
                if (bRejected) {
                } else {
                    String sSubmittedSql = "select last_updated_at from rooster_final_client_submission where visible=1 and clrJobId = '" + sClrJobId + "' and candidate_email_id = '" + rs.getString(1) + "';";
                    System.out.println("sSubmittedSql:::" + sSubmittedSql);
                    submittedRs = getRs(sSubmittedSql);
                    String sUpdatedAt = "";
                    boolean bSubmitted = false;
                    while (submittedRs.next()) {
                        bSubmitted = true;
                        sUpdatedAt = submittedRs.getString(1);
                    }
                    if (bSubmitted) {
                    } else {
                        iHotlistCount++;
                        sSecondary += "<tr><td><a href=\"hotlist_quick_mail_content.do?CLR_JOBID=" + sClrJobId + "&CONSULTANT_EMAIL=" + rs.getString(1) + "\">Submit</a> / <a onclick=\"blackListCandidate('" + rs.getString(1) + "')\">Reject</a></td><td><a href='search_default_call_log.do?sp_search_email_id=" + rs.getString(1) + "' onmouseover=\"getCandidateDetails('" + rs.getString(1) + "')\">" + rs.getString(1) + "</a>" + "<a onmouseover=\"Tip('Attach Candidate To Requirement')\" style=\"{cursor:pointer}\" onclick=\"ATTACH_CANDIDATE_NS.show_attach_candidate_dialog('" + rs.getString(1) + "')\">[AC]</a><img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Resume')\" onClick=\"showFile('" + sResumeLink + "');\" />";
                        sDocLink = ReqDocAttach.getReqDocLink(db, sClrJobId, rs.getString(1), "direct");
                        if (!sDocLink.equals(new String(""))) {
                            sSecondary += "<img src=\"images/paperclip.gif\"  style=\"{cursor:pointer}\" onmouseover=\"Tip('Click To View Document')\" onClick=\"showFile('" + sDocLink + "');\" />";
                        }
                        sSecondary += "</td>" + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td><td>" + rs.getString(4) + "</td></tr>";
                    }
                }
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
            }
        }
        return sSecondary;
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

    private String getMailInfo(String sClrJobId, String sEmailId) {
        Connection myCon = null;
        ResultSet rsMail = null;
        Statement stateMail = null;
        String sFW_RS = "";
        String sSql = "select id from rooster_final_client_submission where visible=1 and candidate_email_id='" + sEmailId + "' and clrJobId='" + sClrJobId + "' order by last_updated_at desc";
        try {
            myCon = db.getConnection();
            stateMail = myCon.createStatement();
            rsMail = stateMail.executeQuery(sSql);
            while (rsMail.next()) {
                int iId = rsMail.getInt(1);
                sFW_RS = "<div>" + "<a href=\"track_submission_fwresend.do?input=resend&slNo=" + iId + "\" onmouseover=\"Tip('Resend')\">[RS]</a>&nbsp;" + "<a href=\"followup_submission.do?slNo=" + iId + "\" onmouseover=\"Tip('Follow-Up')\" >[FU]</a>" + "</div>";
                break;
            }
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            try {
                if (rsMail != null) {
                    rsMail.close();
                }
                if (stateMail != null) {
                    stateMail.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
            }
        }
        return sFW_RS;
    }
}
