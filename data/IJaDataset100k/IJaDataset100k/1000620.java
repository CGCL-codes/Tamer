package com.rooster.action.search.groupbysearch.recruiter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.form.ReqDisplayForm;
import com.rooster.utils.CurrentDate;

public class SkillSearch extends Action {

    static Logger logger = Logger.getLogger(SkillSearch.class.getName());

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
        String sState_code = req.getParameter("state");
        String sCity_code = req.getParameter("city");
        String sState = "";
        String sCity = "";
        if (sState_code.indexOf("_") > -1) {
            sState = sState_code.substring(0, sState_code.indexOf("_"));
        }
        if (sCity_code.indexOf("_") > -1) {
            sCity = sCity_code.substring(0, sCity_code.indexOf("_"));
        }
        String sInput = req.getParameter("input");
        if ((sInput == null) || (sInput.equals(new String("")))) {
        } else if (sInput.equals(new String("tree"))) {
            getSkill(req, res, sState, sState_code, sCity, sCity_code);
            return null;
        }
        DataSource db = null;
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        ArrayList reqList = new ArrayList();
        Vector vValues = new Vector();
        vValues.add("state");
        vValues.add("location");
        ReqDisplayForm reqDisfrm = null;
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            String sQuery = "select reqId,rdDt,client,clrJobId,rsDt,location,state,zip,duration,temp2perm,priorComm,mbr,t1099,w2,h1accept,postSkill,description,exp,archive,opening,assigned from requirement where  archive='no' and state='" + sState + "' and location='" + sCity + "' order by rdDt desc;";
            rs = state.executeQuery(sQuery);
            while (rs.next()) {
                reqDisfrm = new ReqDisplayForm();
                reqDisfrm.setReqId(rs.getString("reqId"));
                reqDisfrm.setRdDt(CurrentDate.convertDBFormatToUS(String.valueOf(rs.getDate("rdDt"))));
                reqDisfrm.setClient(rs.getString("client"));
                reqDisfrm.setClrJobId(rs.getString("clrJobId"));
                reqDisfrm.setRsDt(CurrentDate.convertDBFormatToUS(String.valueOf(rs.getString("rsDt"))));
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
        req.setAttribute("SELECTED_VALUES", vValues);
        return map.findForward("success");
    }

    private void getSkill(HttpServletRequest req, HttpServletResponse res, String sState, String sStateCode, String sCity, String sCityCode) {
        String sSql = "select postSkill,count(postSkill) from requirement where state='" + sState + "' and location='" + sCity + "' and archive='no' group by postSkill;";
        DataSource db = null;
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        String sFinalString = sStateCode + "***" + sCityCode + "***skill$$$<ul>";
        boolean bFound = false;
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sSql);
            while (rs.next()) {
                bFound = true;
                String sSkill = rs.getString(1);
                String sSkill_Count = rs.getString(2);
                sFinalString += "<li node_name='skill' search_value1='" + sStateCode + "' search_value2='" + sCityCode + "' search_value3='" + sSkill + "_skill'>" + sSkill + "[" + sSkill_Count + "]";
                sFinalString += "<ul><img src='images/ajax-loader.gif'></img> Loading...Please Wait...</ul><div name='skill_end' style='display:none;'></div>";
                sFinalString += "</li>";
            }
            if (!bFound) {
                sFinalString += "No Data Found";
            }
            sFinalString += "</ul>";
        } catch (Exception e) {
            logger.warn(e);
            System.out.println(e);
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
        try {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            out.write(sFinalString);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
