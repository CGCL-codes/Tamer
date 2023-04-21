package com.rooster.action.vendor;

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
import com.rooster.form.vendor.VendorConsultantForm;

public class ConsultantInfoAction extends Action {

    DataSource db;

    Connection myCon = null;

    ResultSet rs = null;

    Statement state = null;

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            request.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                response.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        VendorConsultantForm consulUptFrm;
        String linkId = request.getParameter("consultant_id");
        ArrayList consulAList = new ArrayList();
        String resumeLink = "";
        String resumeLink2 = "";
        try {
            String sSql = "";
            sSql = "select first_name,middle_name,last_name,basic_skill,address_1,address_2,city,state,country,zip_code,phone_no,phone_no1,phone_no2,availability,relocation,rate,en_summary,certification,resume_link,resume_link2,extension,training,title,marketable,rate_opt,work_auth,ssn,tax_term,extension1,extension2 from rooster_candidate_info where visible =1 and  email_id ='" + linkId + "';";
            db = getDataSource(request);
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sSql);
            while (rs.next()) {
                consulUptFrm = new VendorConsultantForm();
                consulUptFrm.setEmail_id(linkId);
                consulUptFrm.setFirst_name(rs.getString("first_name"));
                consulUptFrm.setMiddle_name(rs.getString("middle_name"));
                consulUptFrm.setLast_name(rs.getString("last_name"));
                consulUptFrm.setBasic_skill(rs.getString("basic_skill"));
                consulUptFrm.setAddress_1(rs.getString("address_1"));
                consulUptFrm.setAddress_2(rs.getString("address_2"));
                consulUptFrm.setLocation(rs.getString("city"));
                consulUptFrm.setState(rs.getString("state"));
                consulUptFrm.setCountry(rs.getString("country"));
                consulUptFrm.setPin_code(rs.getString("zip_code"));
                consulUptFrm.setPhone_no(rs.getString("phone_no"));
                consulUptFrm.setPhone_no1(rs.getString("phone_no1"));
                consulUptFrm.setPhone_no2(rs.getString("phone_no2"));
                consulUptFrm.setAvailability(rs.getString("availability"));
                consulUptFrm.setRelocation(rs.getString("relocation"));
                consulUptFrm.setRate(rs.getString("rate"));
                consulUptFrm.setEnSummary(rs.getString("en_summary"));
                consulUptFrm.setCertify(rs.getString("certification"));
                resumeLink = rs.getString("resume_link");
                consulUptFrm.setFileName(rs.getString("resume_link"));
                resumeLink2 = rs.getString("resume_link2");
                consulUptFrm.setFileName2(rs.getString("resume_link2"));
                consulUptFrm.setExtension(rs.getString("extension"));
                consulUptFrm.setTraining(rs.getString("training"));
                consulUptFrm.setTitle(rs.getString("title"));
                consulUptFrm.setMarketable(rs.getString("marketable"));
                consulUptFrm.setRate_opt(rs.getString("rate_opt"));
                consulUptFrm.setVisa_status(rs.getString("work_auth"));
                consulUptFrm.setSsn(rs.getString("ssn"));
                consulUptFrm.setTaxTerm(rs.getString("tax_term"));
                consulUptFrm.setExtension1(rs.getString("extension1"));
                consulUptFrm.setExtension2(rs.getString("extension2"));
                consulAList.add(consulUptFrm);
            }
        } catch (Exception e) {
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
            }
        }
        request.setAttribute("resumeLink", resumeLink);
        request.setAttribute("resumeLink2", resumeLink2);
        request.setAttribute("consulAList", consulAList);
        return map.findForward("success");
    }
}
