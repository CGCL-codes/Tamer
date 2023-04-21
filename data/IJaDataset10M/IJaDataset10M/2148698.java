package com.rooster.action.requirement;

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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.form.requirement.ReqirementEntryForm;
import com.rooster.utils.CurrentDate;
import com.rooster.utils.DoThings;

public class EditRequirementInit extends Action {

    static Logger logger = Logger.getLogger(EditRequirementInit.class.getName());

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
        String logId = (String) session.getAttribute("logId");
        String logCat = (String) session.getAttribute("logCategory");
        String UserId = String.valueOf(session.getAttribute("UserId"));
        String sPage = req.getParameter("page");
        if (sPage != null) {
            sPage = sPage.replaceAll("amp;", "&");
            session.setAttribute("FOLLOW_TO", sPage);
        }
        String linkclientno = req.getParameter("linkclientno");
        logger.debug("linkclientno " + linkclientno);
        String crljobid = "";
        DataSource db;
        Connection myCon = null;
        ResultSet rs, rs1;
        Statement state, state1;
        ArrayList reqList = new ArrayList();
        ArrayList assignedNotesList = new ArrayList();
        try {
            String assignedVal = "";
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            StringBuffer sb = new StringBuffer("select * from requirement where reqId='" + linkclientno + "'");
            rs = state.executeQuery(sb.toString());
            ReqirementEntryForm reqDis = null;
            while (rs.next()) {
                reqDis = new ReqirementEntryForm();
                reqDis.setReqId(rs.getString("reqId"));
                reqDis.setRdDate(CurrentDate.convertDBFormatToUS(rs.getDate("rdDt").toString()));
                logger.debug(rs.getDate("rdDt"));
                reqDis.setClient(rs.getString("client"));
                reqDis.setClrJobId(rs.getString("clrJobId"));
                crljobid = rs.getString("clrJobId");
                reqDis.setRsDate(CurrentDate.convertDBFormatToUS(rs.getString("rsDt")));
                reqDis.setLocation(rs.getString("location"));
                reqDis.setState(rs.getString("state"));
                reqDis.setState_name(DoThings.getState(rs.getString("state")));
                reqDis.setZip(rs.getString("zip"));
                reqDis.setDuration(rs.getString("duration"));
                reqDis.setTemp2perm(rs.getString("temp2perm"));
                reqDis.setPermanent(rs.getString("permanent"));
                reqDis.setPriorComm(rs.getString("priorComm"));
                reqDis.setMbr(rs.getString("mbr"));
                reqDis.setT1099(rs.getString("t1099"));
                reqDis.setW2(rs.getString("w2"));
                reqDis.setH1accept(rs.getString("h1accept"));
                reqDis.setPostSkill(rs.getString("postSkill"));
                reqDis.setDescription(rs.getString("description"));
                reqDis.setExp(rs.getString("exp"));
                reqDis.setArchive(rs.getString("archive"));
                assignedVal = rs.getString("assigned");
                reqDis.setOpening(rs.getString("opening"));
                reqDis.setReq_skill(rs.getString("reqSkill"));
                reqDis.setPreOpt_skill(rs.getString("optSkill"));
                reqDis.setJobTitle(rs.getString("jobtitle"));
                String sVirtualPath = rs.getString("virtual_path");
                String sFileName = "";
                if ((sVirtualPath == null) || (sVirtualPath.equals(new String("")))) {
                } else {
                    try {
                        if (sVirtualPath.indexOf("/") > -1) {
                            sFileName = sVirtualPath.substring((sVirtualPath.lastIndexOf("/") + 1));
                        }
                    } catch (Exception e) {
                    }
                }
                if (!(sFileName.equals(new String("")))) {
                    reqDis.setVirtual_path(sVirtualPath);
                    reqDis.setReq_attachment_file_name(sFileName);
                }
                reqList.add(reqDis);
            }
            req.setAttribute("singleinfo", reqList);
        } catch (Exception exp) {
            logger.debug(exp);
        } finally {
            try {
                myCon.close();
            } catch (SQLException e) {
                getServlet().log("Connection.close", e);
            }
        }
        return map.findForward("edit_requirement.show");
    }
}
