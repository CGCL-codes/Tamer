package com.rooster.action;

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
import com.rooster.form.VendorReqModifyForm;

public class VendorReqSingleViewAction extends Action {

    static Logger logger = Logger.getLogger(VendorReqSingleViewAction.class.getName());

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
        VendorReqModifyForm venReqView = (VendorReqModifyForm) frm;
        String linkclientno = venReqView.getLinkclientno();
        logger.debug("linkclientno " + linkclientno);
        DataSource db;
        Connection myCon = null;
        ResultSet rs;
        Statement state;
        ArrayList vendorReqList = new ArrayList();
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            state = myCon.createStatement();
            StringBuffer sb = new StringBuffer("select distinct reqdate,clrjobid,location,state,zip,duration,opening,mbr,skill,description,reqskill,optskill,mailed_by,mailed_info,mailed_to from vendor_mail_info where clrjobid='" + linkclientno + "'");
            rs = state.executeQuery(sb.toString());
            while (rs.next()) {
                venReqView = new VendorReqModifyForm();
                venReqView.setRdDt(rs.getString("reqdate"));
                venReqView.setClrJobId(rs.getString("clrjobid"));
                venReqView.setLocation(rs.getString("location"));
                venReqView.setState(rs.getString("state"));
                venReqView.setZip(rs.getString("zip"));
                venReqView.setDuration(rs.getString("duration"));
                venReqView.setOpening(rs.getString("opening"));
                venReqView.setMbr(rs.getString("mbr"));
                venReqView.setPostSkill(rs.getString("skill"));
                venReqView.setDescription(rs.getString("description"));
                venReqView.setReqSkill(rs.getString("reqskill"));
                venReqView.setOptSkill(rs.getString("optskill"));
                venReqView.setMailBy(rs.getString("mailed_by"));
                venReqView.setMailInfo(rs.getString("mailed_info"));
                venReqView.setMailTo(rs.getString("mailed_to"));
                vendorReqList.add(venReqView);
            }
            session.setAttribute("vendorSingleReqList", vendorReqList);
        } catch (SQLException exp) {
            getServlet().log("Connection.process", exp);
            logger.debug(exp);
        } finally {
            try {
                myCon.close();
            } catch (SQLException e) {
                getServlet().log("Connection.close", e);
            }
        }
        return map.findForward("venReqSingleView");
    }
}
