package com.rooster.action.hotlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.form.hotlist.HotListConsultantListForm;
import com.rooster.utils.RoosterDefaults;

public class HotListMailAction extends Action {

    static Logger logger = Logger.getLogger(HotListMailAction.class.getName());

    DataSource db;

    Connection myCon;

    ResultSet rs, rs1, rs2;

    Statement state, state1, state2;

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
        HotListMailAction hLMail = new HotListMailAction();
        com.rooster.mail.RoosterMailer sm = new com.rooster.mail.RoosterMailer(req);
        HotListConsultantListForm hcl = null;
        String[] selectedConsult = (String[]) session.getAttribute("selectedConsult");
        String[] selectedClient = (String[]) session.getAttribute("selectedClient");
        String content_1 = req.getParameter("content_1");
        String content_2 = req.getParameter("content_2");
        String content_3 = req.getParameter("content_3");
        String content_5 = req.getParameter("content_5");
        String content_6 = req.getParameter("content_6");
        if (content_1.length() == 0 || content_2.length() == 0 || content_3.length() == 0 || content_5.length() == 0 || content_6.length() == 0) {
            req.setAttribute("alert", "Please Fill all the Text Area");
            return map.findForward("contentEmpty");
        }
        ArrayList mailConsultantList = new ArrayList();
        HashSet currLocationList = new HashSet();
        String[] selectedConsultName = selectedConsult.clone();
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            String username = (String) session.getAttribute("UserId");
            state2 = myCon.createStatement();
            String qry1 = "select email_id,password,message from hotlist_mailsetting where visible=1 and user_name='" + username + "';";
            rs2 = state2.executeQuery(qry1);
            String setEmail = "";
            String setPass = "";
            while (rs2.next()) {
                setEmail = rs2.getString("email_id");
                setPass = rs2.getString("password");
            }
            logger.debug("setMsg : " + content_3);
            logger.debug("username : " + username);
            logger.debug("setEmail : " + setEmail);
            logger.debug("setPass : " + setPass);
            String[] clientEmailId = selectedClient.clone();
            String[] clientName = selectedClient.clone();
            state = myCon.createStatement();
            for (int i = 0; i < selectedClient.length; i++) {
                String sSql1 = "select client_name,client_mailid from hotlist_client_tbl where visible=1 and client_mailid='" + selectedClient[i] + "'";
                rs = state.executeQuery(sSql1);
                while (rs.next()) {
                    clientName[i] = rs.getString("client_name");
                    clientEmailId[i] = rs.getString("client_mailid");
                }
            }
            String sSql = "";
            state1 = myCon.createStatement();
            for (int i = 0; i < selectedConsult.length; i++) {
                sSql = "select first_name,basic_skill,current_location,relocation,rate,email_id,en_summary from rooster_candidate_info where visible=1 and own_employee=1 and marketable='Yes' and email_id='" + selectedConsult[i] + "'";
                rs1 = state1.executeQuery(sSql);
                while (rs1.next()) {
                    hcl = new HotListConsultantListForm();
                    hcl.setFirst_name(rs1.getString("first_name"));
                    selectedConsultName[i] = rs1.getString("first_name");
                    hcl.setBasic_skill(rs1.getString("basic_skill"));
                    hcl.setCurrentLocation(rs1.getString("current_location"));
                    currLocationList.add(rs1.getString("current_location"));
                    hcl.setRelocation(rs1.getString("relocation"));
                    hcl.setRate(rs1.getString("rate"));
                    hcl.setEmail_id(rs1.getString("email_id"));
                    hcl.setEnSummary(rs1.getString("en_summary"));
                    mailConsultantList.add(hcl);
                }
            }
            String subject = "Hot List";
            String sResult = "";
            for (int i = 0; i < clientEmailId.length; i++) {
                String hotlistMailTop = hLMail.getHotlistMailTop(content_1, content_2);
                String hotlistMailBottom = hLMail.getHotlistMailBottom(content_3, content_5, content_6);
                String getHotlistMailBody = getHotlistMailBody(currLocationList, mailConsultantList, clientName[i]);
                String allFunction = hotlistMailTop + getHotlistMailBody + hotlistMailBottom;
                if (sm == null) {
                    sm = new com.rooster.mail.RoosterMailer(req);
                }
                if (!setEmail.equals(new String("")) && !setPass.equals(new String(""))) {
                    sResult = sm.sendMail(setEmail, clientEmailId[i], subject.toUpperCase(), allFunction, new java.util.Vector(), "", "");
                } else {
                    sResult = sm.sendMail(RoosterDefaults.getVENDOR_EMAIL(), clientEmailId[i], subject.toUpperCase(), allFunction, null, null, null);
                }
            }
            if (sResult.equals(new String("Send OK"))) {
                String succMsg = "Success";
                req.setAttribute("succMsg", succMsg);
            } else {
                String succMsg = "Failure";
                req.setAttribute("succMsg", succMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("exception :  " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (state != null) {
                    state.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
                if (state2 != null) {
                    state2.close();
                }
                if (state1 != null) {
                    state1.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
            }
        }
        req.setAttribute("selectedConsult", selectedConsultName);
        req.setAttribute("selectedClient", selectedClient);
        return map.findForward("mailSuccess");
    }

    public String getHotlistMailBody(HashSet currLocationList, ArrayList mailConsultantList, String clientName) {
        String venReq = "";
        String a = "";
        if (clientName == null || clientName.equals(new String(""))) {
        } else {
            a = clientName.substring(0, 1);
            clientName = clientName.replaceFirst(a, a.toUpperCase());
        }
        venReq += "<br>";
        venReq += "<table  bgcolor=#FFFFFF cellpadding=1 cellspacing=0 with=800px><tr><td align=center>";
        venReq += "<table cellspacing=1 cellpadding=1 width=800px>";
        venReq += "<tr bgcolor='#7DA5E1'  colspan='2'><th colspan='4'><font color='#FFFFFF' face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'> HOT LIST of consultants </font></th></tr>";
        HotListConsultantListForm hcl = new HotListConsultantListForm();
        Iterator itr = currLocationList.iterator();
        while (itr.hasNext()) {
            String currLocation = itr.next().toString();
            venReq += "<tr> <td colspan='4' bgcolor='#f0f8ff'> <b>" + currLocation + "</b></td><tr>";
            venReq += "<tr>";
            venReq += "<th width='20%' bgcolor='#7DA5E1' ><font color='#FFFFFF' face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>Consultant Name</font></th>";
            venReq += "<th width='20%' bgcolor='#7DA5E1' ><font color='#FFFFFF' face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>Basic Skill </font></th>";
            venReq += "<th width='20%' bgcolor='#7DA5E1' ><font color='#FFFFFF' face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>Relocation</font></th>";
            venReq += "<th width='20%' bgcolor='#7DA5E1' ><font color='#FFFFFF' face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>Rate</font></th>";
            venReq += "</tr>";
            for (int i = 0; i < mailConsultantList.size(); i++) {
                hcl = (HotListConsultantListForm) mailConsultantList.get(i);
                if (hcl.getCurrentLocation().equals(currLocation)) {
                    String consulName = hcl.getFirst_name();
                    if (consulName == null || consulName.equals(new String(""))) {
                    } else {
                        a = consulName.substring(0, 1);
                        consulName = consulName.replaceFirst(a, a.toUpperCase());
                    }
                    venReq += "<tr>";
                    venReq += "<td bgcolor='#f0f8ff'><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'><a name='summary' href='#consult'>" + consulName + "</a></td>";
                    venReq += "<td bgcolor='#f0f8ff'><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>" + hcl.getBasic_skill() + "</font></td>";
                    venReq += "<td bgcolor='#f0f8ff'><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>" + hcl.getRelocation() + "</font></td>";
                    venReq += "<td bgcolor='#f0f8ff'><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>" + hcl.getRate() + "</font></td>";
                    venReq += "</tr>";
                }
            }
        }
        venReq += "</table>";
        venReq += "</td></tr></table>";
        venReq += "<br>";
        venReq += "<br>";
        venReq += "<table  bgcolor=#FFFFFF cellpadding=1 cellspacing=0 with=800px><tr><td align=center><tr><td>";
        venReq += "<table cellspacing=1 cellpadding=1 width=800px>";
        venReq += " <tr><td bgcolor='#f0f8ff'><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'><a name='consult' href='#summary'>Summary Environment </a></font></td></tr>";
        for (int i = 0; i < mailConsultantList.size(); i++) {
            hcl = (HotListConsultantListForm) mailConsultantList.get(i);
            String consulName = hcl.getFirst_name();
            a = consulName.substring(0, 1);
            consulName = consulName.replaceFirst(a, a.toUpperCase());
            venReq += "<tr bgcolor='#7DA5E1'><th><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'> <div align='left'><a href='mailto:" + RoosterDefaults.getADMIN_EMAIL() + "?subject=" + consulName + "&body=Hi,%0A%0A" + clientName + " has requested the resume of " + consulName + ".'>" + consulName + "</a></div></font></th></tr>";
            venReq += "<tr>";
            venReq += "<td align='left' bgcolor='#f0f8ff'><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>" + hcl.getEnSummary() + "</font></td>";
            venReq += " </tr>";
        }
        venReq += "</table>";
        venReq += "</td></tr></table>";
        return venReq;
    }

    public String getHotlistMailTop(String content_1, String content_2) {
        String venReq = "";
        venReq += "<table  bgcolor=#FFFFFF cellpadding=1 cellspacing=0 with=100%><tr><td align=center><tr><td>";
        venReq += "<table cellspacing=1 cellpadding=1 width=80%>";
        venReq += "<tr><td width='100%' height='88' > <font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>&nbsp;</font>";
        venReq += "<p><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold' >" + content_1 + "</font></p>";
        venReq += "<p><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold' >" + content_2 + "</font></p>";
        venReq += "</table>";
        venReq += "</td></tr></table>";
        return venReq;
    }

    public String getHotlistMailBottom(String content_3, String content_5, String content_6) {
        String venReq = "";
        venReq += "<table  bgcolor=#FFFFFF cellpadding=1 cellspacing=0 with=100%><tr><td align=center><tr><td>";
        venReq += "<table cellspacing=1 cellpadding=1 width=80%>";
        venReq += "<tr><td width='100%' height='88' > <font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold'>&nbsp;</font>";
        venReq += "<p><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold' >" + content_3 + "</font></p>";
        venReq += "<p><pre><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold' >" + content_6 + "</font></pre></p>";
        venReq += "<p><font face='Verdana, Arial, Helvetica, sans-serif' size='2px' style='bold' >" + content_5 + "</font></p>";
        venReq += "</table>";
        venReq += "</td></tr></table>";
        return venReq;
    }
}
