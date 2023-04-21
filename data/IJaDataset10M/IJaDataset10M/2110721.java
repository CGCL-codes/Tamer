package com.rooster.action.e_reader.copy_req;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.log4j.Logger;
import com.rooster.action.requirement.FileAttachFunction;
import com.rooster.form.requirement.ReqirementEntryForm;
import com.rooster.utils.RoosterConstants;
import com.rooster.general.MyValidations;
import com.rooster.action.search.groupbysearch.recruiter.TreeDefaultDisplay;

public class AddRequirement extends Action {

    static Logger logger = Logger.getLogger(AddRequirement.class.getName());

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        String sMBRPercentage = String.valueOf(session.getAttribute(RoosterConstants.MBR_PERCENTAGE));
        String s1099Percentage = String.valueOf(session.getAttribute(RoosterConstants.S1099_PERCENTAGE));
        String sW2Percentage = String.valueOf(session.getAttribute(RoosterConstants.W2_PERCENTAGE));
        DataSource dataSource = getDataSource(req);
        Connection myConnection = null;
        PreparedStatement preState = null;
        String sClrJobId = String.valueOf(req.getAttribute("CLR_JOBID"));
        if (sClrJobId.equals(new String("")) || sClrJobId.equals(new String("null"))) {
            sClrJobId = String.valueOf(req.getParameter("CLR_JOBID"));
        }
        String sError = "";
        String client = req.getParameter("client");
        String clrJobId = req.getParameter("clrJobId").toUpperCase().trim();
        String jobTitle = req.getParameter("jobTitle");
        String rdDt = com.rooster.utils.CurrentDate.getFormattedDate(req.getParameter("rdDt"));
        String rsDt = com.rooster.utils.CurrentDate.getFormattedDate(req.getParameter("rsDt"));
        if ((rdDt == null) || (rdDt.equals(new String(""))) || (rdDt.equals(new String("0000-00-00")))) {
            req.setAttribute("Alert", "Problem In Parsing Req. Entry Date.");
            return map.findForward("failure");
        }
        if ((rsDt == null) || (rsDt.equals(new String(""))) || (rsDt.equals(new String("0000-00-00")))) {
            req.setAttribute("Alert", "Problem In Parsing Resume Due Date.");
            return map.findForward("failure");
        }
        if ((clrJobId == null) || (clrJobId.equals(new String("")))) {
            req.setAttribute("Alert", "Clr/Job No Can Not Be Empty.");
            return map.findForward("failure");
        }
        String location = req.getParameter("location");
        String state = req.getParameter("state");
        String zip = req.getParameter("zip");
        String duration = req.getParameter("duration");
        String temp2perm = req.getParameter("temp2perm");
        String sPermanent = req.getParameter("permanent");
        String priorComm = req.getParameter("priorComm");
        String mbr = req.getParameter("mbr");
        String openiing = req.getParameter("opening");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        MyValidations myValidation = new MyValidations();
        float fmbr = Float.parseFloat(mbr.replaceAll(",", ""));
        mbr = mbr.replaceAll(",", "");
        String sCalculatedMBR = new String();
        String t1099 = new String();
        String w2 = new String();
        if (sPermanent.equals(new String("Yes"))) {
            sCalculatedMBR = t1099 = w2 = mbr;
        } else {
            sCalculatedMBR = myValidation.deductPercentage(mbr, sMBRPercentage);
            t1099 = myValidation.deductPercentage(mbr, s1099Percentage);
            w2 = myValidation.deductPercentage(mbr, sW2Percentage);
        }
        String postSkill = req.getParameter("postSkill");
        String description = req.getParameter("description");
        String exp = req.getParameter("exp");
        String h1accept = req.getParameter("h1accept");
        String archive = "no";
        String assigned = "no";
        String req_skill = req.getParameter("req_skill").trim();
        String preOpt_skill = req.getParameter("preOpt_skill").trim();
        String sRegular = req.getParameter("regular");
        int iResult = 0;
        try {
            myConnection = dataSource.getConnection();
            String str = "";
            String sCat = "";
            int iDuration = 0;
            boolean bYear = false;
            str = duration;
            str = str.toLowerCase();
            if (str.indexOf(" ") > -1) {
                if (str.indexOf("m") > -1) {
                    bYear = false;
                    if (str.indexOf("+") > -1) {
                        sCat = str.substring(0, str.indexOf("+")).trim();
                    } else if (str.indexOf("-") > -1) {
                        sCat = str.substring(0, str.indexOf("-")).trim();
                    } else {
                        sCat = str.substring(0, str.indexOf("m")).trim();
                    }
                } else if (str.indexOf("y") > -1) {
                    bYear = true;
                    if (str.indexOf("+") > -1) {
                        sCat = str.substring(0, str.indexOf("+")).trim();
                    } else if (str.indexOf("-") > -1) {
                        sCat = str.substring(0, str.indexOf("-")).trim();
                    } else {
                        sCat = str.substring(0, str.indexOf("y")).trim();
                    }
                } else {
                    sCat = str.substring(0, str.indexOf(" ")).trim();
                }
            } else {
                sCat = str;
            }
            int iNum = 0;
            int iMonth = 0;
            try {
                iNum = Integer.parseInt(sCat);
            } catch (Exception e) {
            }
            if (bYear) {
                iMonth = iNum * 12;
            } else {
                iMonth = iNum;
            }
            try {
                iDuration = iMonth;
                logger.debug("iduration  : " + iDuration);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sVirtualPath = "";
            String sActualPath = "";
            if (sRegular.equals(new String("Yes")) || sRegular.equals(new String(""))) {
                preState = myConnection.prepareStatement("insert into requirement (rdDt,client,clrJobId,rsDt,location,state,Zip,duration,sort_requirement,temp2perm,priorComm,mbr,calculated_mbr,t1099,w2,h1accept,postSkill,exp,description,archive,opening,assigned,reqSkill,optSkill,jobtitle,actual_path,virtual_path,permanent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                req.setAttribute("IS_HOTLIST_REQUIREMENT", new String("No"));
                preState.setString(1, rdDt);
                preState.setString(2, client);
                preState.setString(3, clrJobId);
                preState.setString(4, rsDt);
                preState.setString(5, location);
                preState.setString(6, state);
                preState.setString(7, zip);
                preState.setString(8, duration);
                preState.setInt(9, iDuration);
                preState.setString(10, temp2perm);
                preState.setString(11, priorComm);
                preState.setString(12, mbr);
                preState.setString(13, sCalculatedMBR);
                preState.setString(14, t1099);
                preState.setString(15, w2);
                preState.setString(16, h1accept);
                preState.setString(17, postSkill);
                preState.setString(18, exp);
                preState.setString(19, description);
                preState.setString(20, archive);
                preState.setString(21, openiing);
                preState.setString(22, assigned);
                preState.setString(23, req_skill);
                preState.setString(24, preOpt_skill);
                preState.setString(25, jobTitle);
                preState.setString(26, sActualPath);
                preState.setString(27, sVirtualPath);
                preState.setString(28, sPermanent);
                iResult = preState.executeUpdate();
            } else {
                preState = myConnection.prepareStatement("insert into rooster_hotlist_requirement (rdDt,client,clrJobId,rsDt,location,state,Zip,duration,sort_requirement,temp2perm,priorComm,mbr,calculated_mbr,t1099,w2,h1accept,postSkill,exp,description,archive,opening,assigned,reqSkill,optSkill,jobtitle,actual_path,virtual_path,permanent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                req.setAttribute("IS_HOTLIST_REQUIREMENT", new String("Yes"));
                preState.setString(1, rdDt);
                preState.setString(2, client);
                preState.setString(3, clrJobId);
                preState.setString(4, rsDt);
                preState.setString(5, location);
                preState.setString(6, state);
                preState.setString(7, zip);
                preState.setString(8, duration);
                preState.setInt(9, iDuration);
                preState.setString(10, temp2perm);
                preState.setString(11, priorComm);
                preState.setString(12, mbr);
                preState.setString(13, sCalculatedMBR);
                preState.setString(14, t1099);
                preState.setString(15, w2);
                preState.setString(16, h1accept);
                preState.setString(17, postSkill);
                preState.setString(18, exp);
                preState.setString(19, description);
                preState.setString(20, archive);
                preState.setString(21, openiing);
                preState.setString(22, assigned);
                preState.setString(23, req_skill);
                preState.setString(24, preOpt_skill);
                preState.setString(25, jobTitle);
                preState.setString(26, sActualPath);
                preState.setString(27, sVirtualPath);
                preState.setString(28, sPermanent);
                iResult = preState.executeUpdate();
            }
        } catch (Exception e) {
            logger.warn(e);
            sError = e.toString();
        } finally {
            try {
                myConnection.close();
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        Connection cCon = null;
        Statement sStatement = null;
        ResultSet rs = null;
        int iReqId = 0;
        try {
            cCon = dataSource.getConnection();
            sStatement = cCon.createStatement();
            String sSql = "select max(reqId) from requirement;";
            rs = sStatement.executeQuery(sSql);
            while (rs.next()) {
                iReqId = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.warn(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (sStatement != null) {
                    sStatement.close();
                }
                if (cCon != null) {
                    cCon.close();
                }
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        req.setAttribute("JobId", clrJobId);
        if (iResult > 0) {
            TreeDefaultDisplay treeDisplay = new TreeDefaultDisplay();
            treeDisplay.setRecruiterDefaultTree(req, dataSource);
            req.setAttribute("RESULT", "Requirement Successfully Added.");
            req.setAttribute("ReqId", iReqId);
            if (fmbr > 39) {
                req.setAttribute("mbr", fmbr);
            }
        } else {
            req.setAttribute("RESULT", "Problem In Adding Requirement.<br>" + sError);
        }
        return map.findForward("success");
    }

    private Vector<String> attachDocument(ReqirementEntryForm reqEntFrm, HttpServletRequest req, String sClrJobId) {
        java.util.Vector vec = new Vector<String>();
        FormFile file = reqEntFrm.getRequirement_file();
        if (file.getFileName().equals(new String(""))) {
        } else {
            try {
                FileAttachFunction fileAttach = new FileAttachFunction();
                vec = fileAttach.attachDocument(req, file, sClrJobId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vec;
    }

    private int UpdateFinalClientSubmission(HttpServletRequest req, String sNewClrJobId, String sOldClrJobId) {
        DataSource ds = null;
        Connection myConnection = null;
        PreparedStatement pstmtUpdate = null;
        ResultSet rsUpdate = null;
        int iUpdated = 0;
        int iId = 0;
        HttpSession session = req.getSession(false);
        String sId = String.valueOf(session.getAttribute("FINAL_SUBMISSION_ID"));
        if (sId.equals(new String("null")) || sId.equals(new String(""))) {
        } else {
            iId = Integer.valueOf(sId);
            session.setAttribute("FINAL_SUBMISSION_ID", "");
        }
        try {
            String sSql = "update rooster_final_client_submission set clrJobId = ? where clrJobId = ? and id=?;";
            ds = getDataSource(req);
            myConnection = ds.getConnection();
            pstmtUpdate = myConnection.prepareStatement(sSql);
            pstmtUpdate.setString(1, sNewClrJobId);
            pstmtUpdate.setString(2, sOldClrJobId);
            pstmtUpdate.setInt(3, iId);
            iUpdated = pstmtUpdate.executeUpdate();
        } catch (SQLException sqle) {
            logger.debug(sqle);
        } finally {
            try {
                if (rsUpdate != null) {
                    rsUpdate.close();
                }
                if (pstmtUpdate != null) {
                    pstmtUpdate.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
        return iUpdated;
    }
}
