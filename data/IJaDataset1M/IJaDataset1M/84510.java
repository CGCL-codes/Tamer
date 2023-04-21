package com.rooster.action.hotlist_candidate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.form.candidate.GetDataForm;
import com.rooster.utils.DoThings;

public class candiDataAction extends Action {

    DataSource dataSource;

    Connection myConnection = null;

    ResultSet rs;

    Statement state;

    ResultSet rs1;

    ResultSet rsExist;

    Statement state1;

    Statement stmtExist;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        GetDataForm DataForm = (GetDataForm) frm;
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        String username = (String) session.getAttribute("UserId");
        String req_first_name = "";
        String clrJobId = "";
        String recId = "";
        Hashtable htReqInfo = new Hashtable();
        ArrayList info = new ArrayList();
        ArrayList<GetDataForm> CandInfo = new ArrayList<GetDataForm>();
        String iExist = "";
        try {
            dataSource = getDataSource(req);
            myConnection = dataSource.getConnection();
            state = myConnection.createStatement();
            stmtExist = myConnection.createStatement();
            String qry = "select a.clrJobId ,b.location," + "c.first_name,a.mailed_by from rooster_final_client_submission a,requirement b," + " req_userinfo c where a.mailed_by=c.email and a.candidate_email_id='" + username + "' " + "and a.clrJobId=b.clrJobId and candidate_approved=0 order by a.clrJobId desc;";
            rs = state.executeQuery(qry);
            int i = 0;
            String id = "";
            while (rs.next()) {
                clrJobId = rs.getString("clrJobId");
                if (!htReqInfo.contains(clrJobId)) {
                    req_first_name = rs.getString("first_name");
                    id = "fir_" + i;
                    htReqInfo.put(id, req_first_name);
                    recId = rs.getString("mailed_by");
                    id = "recId_" + i;
                    htReqInfo.put(id, recId);
                    info.add(htReqInfo);
                    i++;
                }
            }
            req.setAttribute("recruiter_name", req_first_name);
            req.setAttribute("recruiter_id", recId);
            if (info.size() > 0) {
                req.setAttribute("info", info);
            } else {
                String candReqStr = "There is no job";
                req.setAttribute("candReqStr", candReqStr);
            }
            state1 = myConnection.createStatement();
            String qry1 = "select alternate_mail_id,first_name,middle_name,last_name,basic_skill,address_1,address_2," + "city, state, country, zip_code, phone_no, phone_no_type, email_id, tax_term, work_auth, eadtxt, paystub, ssn, " + "rate, relocation, pref_location from rooster_candidate_info where email_id='" + username + "' and visible=1;";
            rs1 = state1.executeQuery(qry1);
            while (rs1.next()) {
                DataForm.setAlternate_mail_id(rs1.getString("alternate_mail_id"));
                DataForm.setConFname(rs1.getString("first_name"));
                DataForm.setConLname(rs1.getString("last_name"));
                DataForm.setConEmail(rs1.getString("email_id"));
                DataForm.setConSSN(rs1.getString("ssn"));
                DataForm.setMbrrate(rs1.getString("rate"));
                DataForm.setConPhone1(rs1.getString("phone_no"));
                DataForm.setPhoneType1(rs1.getString("phone_no_type"));
                DataForm.setConAdd1(rs1.getString("address_1"));
                DataForm.setConAdd2(rs1.getString("address_2"));
                DataForm.setConCity(rs1.getString("city"));
                DataForm.setConState(rs1.getString("state"));
                DataForm.setConZip(rs1.getString("zip_code"));
                DataForm.setWorkauth(rs1.getString("work_auth"));
                DataForm.setEadtxt(rs1.getString("eadtxt"));
                DataForm.setPaystub(rs1.getString("paystub"));
                DataForm.setTaxterm(rs1.getString("tax_term"));
                DataForm.setConRelo(rs1.getString("relocation"));
                DataForm.setConPreStateList(rs1.getString("pref_location"));
                DataForm.setConCoreSkillSet(rs1.getString("basic_skill"));
                DataForm.setState_name(DoThings.getState(rs1.getString("state")));
            }
            CandInfo.add(DataForm);
            String sSqlExist = "select login_status from rooster_candidate_info where email_id='" + String.valueOf(session.getAttribute("UserId")) + "'";
            rsExist = stmtExist.executeQuery(sSqlExist);
            while (rsExist.next()) {
                iExist = rsExist.getString("login_status");
            }
        } catch (SQLException sqle) {
            getServlet().log("Connection.process", sqle);
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (state1 != null) {
                    state1.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (SQLException e) {
                getServlet().log("Connection.close", e);
                e.printStackTrace();
            }
        }
        String sForward = new String("");
        if (iExist.equalsIgnoreCase("Y")) {
            sForward = "reqdetailAction";
        } else if (iExist.equalsIgnoreCase("N")) {
            req.setAttribute("CandInfo", CandInfo);
            sForward = "candidetail";
        }
        return map.findForward(sForward);
    }
}
