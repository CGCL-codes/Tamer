package com.rooster.action.hotlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.action.HomeAction;
import com.rooster.form.hotlist.ConsultantSearchForm;
import com.rooster.form.hotlist.HotListConsultantListForm;

public class ConsultantSearchAfterUpdatedAction extends Action {

    static Logger logger = Logger.getLogger(ConsultantSearchAfterUpdatedAction.class.getName());

    DataSource db;

    Connection myCon;

    Statement statement;

    ResultSet rs;

    HotListConsultantListForm consulListFrm;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        ArrayList consulList = new ArrayList();
        ConsultantSearchForm cSearchFrm = (ConsultantSearchForm) frm;
        String sSql = (String) session.getAttribute("searcgQry");
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            statement = myCon.createStatement();
            rs = statement.executeQuery(sSql);
            while (rs.next()) {
                consulListFrm = new HotListConsultantListForm();
                consulListFrm.setEmail_id(rs.getString("email_id"));
                consulListFrm.setFirst_name(rs.getString("first_name"));
                String f = rs.getString("first_name");
                String l = rs.getString("last_name");
                l = l.substring(0, 1).toUpperCase();
                l = l + f;
                consulListFrm.setFirst_name(l);
                consulListFrm.setPhone_no(rs.getString("phone_no"));
                consulListFrm.setPhone_no1(rs.getString("phone_no1"));
                consulListFrm.setPhone_no2(rs.getString("phone_no2"));
                consulListFrm.setBasic_skill(rs.getString("basic_skill"));
                consulListFrm.setCurrentLocation(rs.getString("current_location"));
                consulListFrm.setRelocation(rs.getString("relocation"));
                consulListFrm.setRate(rs.getString("rate"));
                consulListFrm.setAvailability(rs.getString("availability"));
                consulListFrm.setEnSummary(rs.getString("en_summary"));
                consulListFrm.setCertify(rs.getString("certification"));
                consulListFrm.setFileName(rs.getString("resume_link"));
                consulListFrm.setActual_path(rs.getString("actual_path"));
                consulListFrm.setFileName2(rs.getString("resume_link2"));
                consulListFrm.setActual_path2(rs.getString("actual_path2"));
                consulList.add(consulListFrm);
            }
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
            }
        }
        if (consulList.size() == 1) {
            req.setAttribute("linkId", consulListFrm.getEmail_id());
            return map.findForward("SingleResultView");
        }
        if (consulList.size() > 0) {
            req.setAttribute("consulList", consulList);
        } else {
            String str = "No data found for your search criteria";
            req.setAttribute("str", str);
        }
        return map.findForward("SearchResult");
    }
}
