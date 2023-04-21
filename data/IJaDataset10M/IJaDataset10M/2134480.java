package com.rooster.action.candidate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.rooster.action.candidate.util.CandidateUtil;
import com.rooster.form.candidate.GetDataForm;
import com.rooster.form.candidate.RequirementInfoForm;
import com.rooster.utils.DoThings;
import java.util.StringTokenizer;
import java.util.Hashtable;

public class ShowNcaDocument extends Action {

    DataSource db;

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
        Connection myCon = null;
        Statement stmt = null;
        Statement state = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement prpstmtNca = null;
        ArrayList<RequirementInfoForm> searchResult = new ArrayList<RequirementInfoForm>();
        String sJobId = String.valueOf(req.getParameter("jobId"));
        String sFlowType = String.valueOf(req.getAttribute("flow_type"));
        sJobId = replaceChar(sJobId, ',');
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            String sQry = "select rdDt, client, clrJobId, rsDt, location, state, zip, duration, optSkill, reqSkill, mbr, t1099, h1accept, w2, postSkill, description, exp, archive, opening, assigned, exp from requirement where clrJobId in (" + sJobId + ");";
            stmt = myCon.createStatement();
            rs1 = stmt.executeQuery(sQry);
            while (rs1.next()) {
                RequirementInfoForm reqDisfrm = new RequirementInfoForm();
                reqDisfrm.setRdDt(rs1.getDate("rdDt").toString());
                reqDisfrm.setClient(rs1.getString("client"));
                reqDisfrm.setClrJobId(rs1.getString("clrJobId"));
                reqDisfrm.setRsDt(rs1.getString("rsDt"));
                reqDisfrm.setLocation(rs1.getString("location"));
                reqDisfrm.setState(DoThings.getState(rs1.getString("state")));
                reqDisfrm.setZip(rs1.getString("zip"));
                reqDisfrm.setDuration(rs1.getString("duration"));
                reqDisfrm.setMbr(rs1.getString("mbr"));
                reqDisfrm.setOptSkill(rs1.getString("optSkill"));
                reqDisfrm.setOptSkill(rs1.getString("reqSkill"));
                reqDisfrm.setPostSkill(rs1.getString("postSkill"));
                reqDisfrm.setDescription(rs1.getString("description"));
                searchResult.add(reqDisfrm);
            }
            req.setAttribute("JobInfo", searchResult);
        } catch (SQLException exp) {
            getServlet().log("Connection.process", exp);
            exp.printStackTrace();
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                getServlet().log("Connection.close", e);
            }
        }
        try {
            String sSqlDataConfirm = "select first_name, last_name, ssn, address_1, address_2, state, city, zip_code, phone_no from rooster_candidate_info where email_id = ?";
            db = getDataSource(req);
            myCon = db.getConnection();
            prpstmtNca = myCon.prepareStatement(sSqlDataConfirm);
            prpstmtNca.setString(1, String.valueOf(session.getAttribute("UserId")));
            String sUser = String.valueOf(session.getAttribute("UserId"));
            rs = prpstmtNca.executeQuery();
            GetDataForm fdf = new GetDataForm();
            ArrayList<GetDataForm> CandInfo = new ArrayList<GetDataForm>();
            rs.next();
            fdf.setConFname(rs.getString("first_name"));
            fdf.setConLname(rs.getString("last_name"));
            fdf.setConSSN(rs.getString("ssn"));
            fdf.setConAdd1(rs.getString("address_1"));
            fdf.setConAdd2(rs.getString("address_2"));
            fdf.setConCity(rs.getString("city"));
            fdf.setConState(DoThings.getState(rs.getString("state")));
            fdf.setConPhone1(rs.getString("phone_no"));
            fdf.setConZip(CandidateUtil.checkZipDigit(rs.getString("zip_code")));
            CandInfo.add(fdf);
            req.setAttribute("CandInfo", CandInfo);
            String qry = "select ndatext from rooster_ndasetting";
            state = myCon.createStatement();
            rs = state.executeQuery(qry);
            String userPass = null;
            String ncaText = "";
            while (rs.next()) {
                ncaText = rs.getString("ndatext");
            }
            StringBuffer headerText = new StringBuffer();
            headerText.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            headerText.append("For purpose of this agreement: ^=comanyname^ referred as ^=shortname^ and Contractor/Consultant/Employee</br></b>");
            headerText.append("I, ^=Consultantname^ with personal phone number : ^=phonenumber^ and last 4 digits of SSN as ^=SSN^ residing at ^=address^");
            headerText.append("agree to the submission of ^=requirementId^ </br>");
            headerText.append("<br><br>");
            headerText.append(ncaText);
            String clrJob = (String) session.getAttribute("clrJobId1");
            String finalText = replaceNDAText(headerText, CandInfo, searchResult, clrJob, req);
            finalText = finalText.replaceAll("\\n", "<br><br>");
            finalText = finalText.replaceAll("\\t", "&nbsp;");
            session.setAttribute("ndaText", finalText);
            req.setAttribute("ndaText", finalText);
        } catch (Exception e) {
            getServlet().log("Connection.close", e);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (prpstmtNca != null) {
                    prpstmtNca.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                getServlet().log("Connection.close", e);
            }
        }
        req.setAttribute("flow_type", sFlowType);
        return map.findForward("ShowNca");
    }

    public String replaceNDAText(StringBuffer ndaText, ArrayList<GetDataForm> CandInfo, ArrayList<RequirementInfoForm> searchResult, String clrJobId, HttpServletRequest req) {
        String companyName = com.rooster.utils.RoosterDefaults.getCOMAPANY_NAME();
        String companySortName = com.rooster.utils.RoosterDefaults.getCOMPANY_SH_NAME();
        DataSource db1;
        Connection Con = null;
        try {
            String sSqlDataConfirm = "select company_name,company_sh_name from admin_panel_info;";
            db1 = getDataSource(req);
            Con = db.getConnection();
            Statement state = state = Con.createStatement();
            ResultSet rs = state.executeQuery(sSqlDataConfirm);
            while (rs.next()) {
                companyName = rs.getString("company_name");
                companySortName = rs.getString("company_sh_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (Con != null) {
                    Con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                getServlet().log("Connection.close", e);
            }
        }
        GetDataForm candDetails = (GetDataForm) CandInfo.get(0);
        String nda = ndaText.toString();
        Hashtable ht = new Hashtable();
        ht.put("comanyname", companyName);
        ht.put("shortname", companySortName);
        ht.put("Consultantname", candDetails.getConLname() + " , " + candDetails.getConFname());
        ht.put("phonenumber", candDetails.getConPhone1());
        ht.put("SSN", candDetails.getConSSN());
        ht.put("address", candDetails.getConAdd1() + ", " + candDetails.getConCity() + ", " + candDetails.getConState() + " ");
        ht.put("requirementId", clrJobId);
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(nda, "^=");
        while (st.hasMoreElements()) {
            String st1 = st.nextToken();
            if (ht.containsKey(st1)) {
                st1 = (String) ht.get(st1);
            }
            sb.append(st1);
        }
        return sb.toString();
    }

    public String replaceChar(String sSource, char cReplace) {
        String sResult = "'";
        for (int i = 0; i < sSource.length(); i++) {
            if (sSource.charAt(i) != cReplace) {
                sResult += sSource.charAt(i);
            } else {
                sResult += "\',\'";
            }
        }
        sResult += "'";
        return sResult;
    }
}
