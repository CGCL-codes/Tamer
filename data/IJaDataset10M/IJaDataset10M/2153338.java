package com.rooster.action.search;

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
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.form.ReqDisplayForm;
import com.rooster.utils.CurrentDate;

public class OpenReqs extends Action {

    static Logger logger = Logger.getLogger(OpenReqs.class.getName());

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
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sPeriod = req.getParameter("period");
        String sQuery = "";
        String sSearchTitle = "";
        if ((sPeriod == null) || (sPeriod.equals(new String(""))) || (sPeriod.equals(new String("TOTAL")))) {
            sQuery = "select reqId,rdDt,client,clrJobId,rsDt,location,state,zip,duration,temp2perm,priorComm,mbr,t1099,w2,h1accept,postSkill,description,exp,archive,opening,assigned from requirement where visible=1 and archive='no' ORDER BY rdDt DESC;";
            sSearchTitle = "Total Reqs Found.";
        } else if (sPeriod.equals(new String("TODAY"))) {
            sQuery = "select reqId,rdDt,client,clrJobId,rsDt,location,state,zip,duration,temp2perm,priorComm,mbr,t1099,w2,h1accept,postSkill,description,exp,archive,opening,assigned from requirement where visible=1 and archive='no' and DATE(rdDt) >= DATE(DATE_ADD(CURDATE(),INTERVAL -1 DAY)) ORDER BY rdDt DESC;";
            sSearchTitle = "Reqs Created Today.";
        } else if (sPeriod.equals(new String("YESTERDAY"))) {
            sQuery = "select reqId,rdDt,client,clrJobId,rsDt,location,state,zip,duration,temp2perm,priorComm,mbr,t1099,w2,h1accept,postSkill,description,exp,archive,opening,assigned from requirement where visible=1 and archive='no' and DATE(rdDt) >= DATE(DATE_ADD(CURDATE(),INTERVAL -3 DAY)) ORDER BY rdDt DESC;";
            sSearchTitle = "Reqs Created In The Past Three Days.";
        } else if (sPeriod.equals(new String("LAST_WEEK"))) {
            sSearchTitle = "Reqs Created In The Past One Week.";
            sQuery = "select reqId,rdDt,client,clrJobId,rsDt,location,state,zip,duration,temp2perm,priorComm,mbr,t1099,w2,h1accept,postSkill,description,exp,archive,opening,assigned from requirement where visible=1 and archive='no' and DATE(rdDt) >= DATE(DATE_ADD(CURDATE(),INTERVAL -7 DAY)) ORDER BY rdDt DESC;";
        } else {
            sSearchTitle = "Total Reqs Found.";
            sQuery = "select reqId,rdDt,client,clrJobId,rsDt,location,state,zip,duration,temp2perm,priorComm,mbr,t1099,w2,h1accept,postSkill,description,exp,archive,opening,assigned from assigned_job_view where visible=1 and archive='no' ORDER BY rdDt DESC;";
        }
        DataSource db = null;
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        ArrayList reqList = new ArrayList();
        ReqDisplayForm reqDisfrm = null;
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sQuery);
            while (rs.next()) {
                reqDisfrm = new ReqDisplayForm();
                reqDisfrm.setReqId(rs.getString("reqId"));
                reqDisfrm.setRdDt(CurrentDate.convertDBFormatToUS(rs.getDate("rdDt").toString()));
                reqDisfrm.setClient(rs.getString("client"));
                reqDisfrm.setClrJobId(rs.getString("clrJobId"));
                reqDisfrm.setRsDt(CurrentDate.convertDBFormatToUS(rs.getString("rsDt").toString()));
                reqDisfrm.setLocation(rs.getString("location"));
                reqDisfrm.setState(rs.getString("state"));
                reqDisfrm.setZip(rs.getString("zip"));
                reqDisfrm.setDuration(rs.getString("duration"));
                reqDisfrm.setTemp2perm(rs.getString("temp2perm"));
                reqDisfrm.setPriorComm(rs.getString("priorComm"));
                reqDisfrm.setMbr(rs.getString("mbr"));
                reqDisfrm.setT1099(rs.getString("t1099"));
                reqDisfrm.setW2(rs.getString("w2"));
                reqDisfrm.setH1accept(rs.getString("h1accept"));
                reqDisfrm.setPostSkill(rs.getString("postSkill"));
                reqDisfrm.setDescription(rs.getString("description"));
                reqDisfrm.setExp(rs.getString("exp"));
                reqDisfrm.setArchive(rs.getString("archive"));
                reqDisfrm.setOpening(rs.getString("opening"));
                reqDisfrm.setAssigned(rs.getString("assigned"));
                reqList.add(reqDisfrm);
            }
            req.setAttribute("requirements", reqList);
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
        req.setAttribute("SEARCH_TITLE", sSearchTitle);
        return map.findForward("success");
    }
}
