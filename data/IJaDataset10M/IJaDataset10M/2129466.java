package com.rooster.action.requirement;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.utils.CurrentDate;

public class GetRequirementDetails extends Action {

    static Logger logger = Logger.getLogger(GetRequirementDetails.class.getName());

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
        String sInput = req.getParameter("input");
        sInput = sInput.toLowerCase();
        if (sInput.equals(new String("full"))) {
            String sSql = "";
            String req_id = req.getParameter("req_id");
            String sClrJobId = "";
            if ((req_id == null) || (req_id.equals(new String("")))) {
                sClrJobId = req.getParameter("clrJobId");
                sSql = "select rdDt, client, clrJobId, rsDt, location, state, Zip, duration, sort_requirement, temp2perm, " + "priorComm, mbr, t1099, w2, h1accept, postSkill, exp, description, archive, opening, " + "assigned,reqId,jobtitle  from requirement where visible=1 and clrJobId ='" + sClrJobId + "';";
            } else {
                sSql = "select rdDt, client, clrJobId, rsDt, location, state, Zip, duration, sort_requirement, temp2perm, " + "priorComm, mbr, t1099, w2, h1accept, postSkill, exp, description, archive, opening, " + "assigned,reqId,jobtitle  from requirement where visible=1 and reqId ='" + req_id + "';";
            }
            try {
                String sOutPut = "";
                db = getDataSource(req);
                Connection myCon = db.getConnection();
                Statement stateCheck = myCon.createStatement();
                ResultSet rsCheck = stateCheck.executeQuery(sSql);
                rsCheck.last();
                int iRow = rsCheck.getRow();
                if (iRow > 0) {
                } else {
                    if ((req_id == null) || (req_id.equals(new String("")))) {
                        sSql = "select rdDt, client, clrJobId, rsDt, location, state, Zip, duration, sort_requirement, temp2perm, " + "priorComm, mbr, t1099, w2, h1accept, postSkill, exp, description, archive, opening, " + "assigned,reqId,jobtitle  from rooster_hotlist_requirement where visible=1 and clrJobId ='" + sClrJobId + "';";
                    }
                }
                Statement state = myCon.createStatement();
                ResultSet rs = state.executeQuery(sSql);
                String sRdDate = "";
                String sClient = "";
                String sRsDate = "";
                String sLocation = "";
                String sState = "";
                String sZip = "";
                String sDuration = "";
                String sSortReq = "";
                String sTemp2Perm = "";
                String sPriority = "";
                String sMbr = "";
                String sT1099 = "";
                String sW2 = "";
                String sH1Accept = "";
                String sPostSkill = "";
                String sExp = "";
                String sDesc = "";
                String sArchive = "";
                String sOpening = "";
                String sAssigned = "";
                String sRecruiterId = "";
                String sLeadId = "";
                String sJobTitle = "";
                try {
                    while (rs.next()) {
                        sRdDate = rs.getString(1);
                        sClient = rs.getString(2);
                        sClrJobId = rs.getString(3);
                        sRsDate = rs.getString(4);
                        sLocation = rs.getString(5);
                        sState = rs.getString(6);
                        sZip = rs.getString(7);
                        sDuration = rs.getString(8);
                        sSortReq = rs.getString(9);
                        sTemp2Perm = rs.getString(10);
                        sPriority = rs.getString(11);
                        sMbr = rs.getString(12);
                        sT1099 = rs.getString(13);
                        sW2 = rs.getString(14);
                        sH1Accept = rs.getString(15);
                        sPostSkill = rs.getString(16);
                        sExp = rs.getString(17);
                        sDesc = rs.getString(18);
                        sArchive = rs.getString(19);
                        sOpening = rs.getString(20);
                        sAssigned = rs.getString(21);
                        req_id = rs.getString(22);
                        sJobTitle = rs.getString(23);
                    }
                } catch (Exception e) {
                } finally {
                    try {
                        if (rsCheck != null) {
                            rsCheck.close();
                        }
                        if (stateCheck != null) {
                            stateCheck.close();
                        }
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
                if (sAssigned.equalsIgnoreCase(new String("yes"))) {
                    try {
                        sSql = "select first_name, last_name, email from req_userinfo where user_id in (select recruiterId from req_assigned where reqId ='" + req_id + "');";
                        myCon = db.getConnection();
                        state = myCon.createStatement();
                        rs = state.executeQuery(sSql);
                        while (rs.next()) {
                            sRecruiterId += rs.getString(1) + " ";
                            sRecruiterId += rs.getString(2) + " [";
                            sRecruiterId += rs.getString(3) + "]<br>";
                        }
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
                    if (sRecruiterId.equals(new String(""))) {
                        sRecruiterId = "Not Available.";
                    }
                    try {
                        sSql = "select first_name, last_name, email from req_userinfo where user_id in (select leadId from req_assigned where reqId ='" + req_id + "');";
                        myCon = db.getConnection();
                        state = myCon.createStatement();
                        rs = state.executeQuery(sSql);
                        while (rs.next()) {
                            sLeadId += rs.getString(1) + " ";
                            sLeadId += rs.getString(2) + " [";
                            sLeadId += rs.getString(3) + "]<br>";
                        }
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
                    if (sLeadId.equals(new String(""))) {
                        sLeadId = "Not Available.";
                    }
                }
                sOutPut = getRequirementDetails(sRdDate, sClient, sClrJobId, sRsDate, sLocation, sState, sZip, sDuration, sSortReq, sTemp2Perm, sPriority, sMbr, sT1099, sW2, sH1Accept, sPostSkill, sExp, sDesc, sArchive, sOpening, sAssigned, sRecruiterId, sLeadId, sJobTitle);
                res.setContentType("text/xml");
                PrintWriter out = res.getWriter();
                out.write(sOutPut);
                out.flush();
                out.close();
                rs.close();
                state.close();
            } catch (Exception e) {
                logger.debug(e);
            }
        }
        return (null);
    }

    public String getRequirementDetails(String sRdDate, String sClient, String sClrJobId, String sRsDate, String sLocation, String sState, String sZip, String sDuration, String sSortReq, String sTemp2Perm, String sPriority, String sMbr, String sT1099, String sW2, String sH1Accept, String sPostSkill, String sExp, String sDesc, String sArchive, String sOpening, String sAssigned, String sRecruiterId, String sLeadId, String sJobTitle) {
        String sFinal = "";
        String sContent = "";
        try {
            if (sAssigned.equalsIgnoreCase(new String("yes"))) {
                sContent += "<tr>";
                sContent += "<td width='400px' id='label'>Assigned To</td>";
                sContent += "<td class='alerttext' width='400px'>" + sRecruiterId + "</td>";
                sContent += "<td width='600px' id='label'>Assigned By </td>";
                sContent += "<td class='alerttext' width='600px' >" + sLeadId + "</td>";
                sContent += "</tr>";
            }
            sContent += "<tr>";
            sContent += "<td width='400px' id='label'>Req. Title</td>";
            sContent += "<td colspan='3'>" + sJobTitle + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td width='400px' id='label'>Req. Entry Date</td>";
            sContent += "<td>" + CurrentDate.convertDBFormatToUS(sRdDate) + "</td>";
            sContent += "<td width='600px' id='label'>Client Name</td>";
            sContent += "<td>" + sClient + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>Job Id</td>";
            sContent += "<td>" + sClrJobId + "</td>";
            sContent += "<td id='label'>Req. Date</td>";
            sContent += "<td>" + CurrentDate.convertDBFormatToUS(sRsDate) + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>Location</td>";
            sContent += "<td>" + sLocation + "</td>";
            sContent += "<td id='label'>State</td>";
            sContent += "<td>" + sState + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>Zip</td>";
            sContent += "<td>" + sZip + "</td>";
            sContent += "<td id='label'>Duration</td>";
            sContent += "<td>" + sDuration + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>Temp To Perm</td>";
            sContent += "<td>" + sTemp2Perm + "</td>";
            sContent += "<td id='label'>Priority</td>";
            sContent += "<td>" + sPriority + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>MBR</td>";
            sContent += "<td>" + sMbr + "</td>";
            sContent += "<td id='label'>1099</td>";
            sContent += "<td>" + sT1099 + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>W2</td>";
            sContent += "<td>" + sW2 + "</td>";
            sContent += "<td id='label'>H1 Accepted</td>";
            sContent += "<td>" + sH1Accept + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>Skill</td>";
            sContent += "<td>" + sPostSkill + "</td>";
            sContent += "<td id='label'>Experience </td>";
            sContent += "<td>" + sExp + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label'>Assigned </td>";
            sContent += "<td >" + sAssigned + "</td>";
            sContent += "<td id='label'>No. Of Openings</td>";
            sContent += "<td>" + sOpening + "</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td id='label' colspan='3'>Description</td>";
            sContent += "<td>&nbsp;</td>";
            sContent += "</tr>";
            sContent += "<tr>";
            sContent += "<td colspan='4' align='justify'>" + sDesc + "</td>";
            sContent += "</tr>";
        } catch (Exception e) {
        }
        sFinal += "<span id='" + sClrJobId + "'><center><b>Requirement Details</b></center><table align='center' cellpadding='1' cellspacing='0' class='tableBackground' width='500px'><tr><td><table id='demo1_table' cellpadding='3' cellspacing='1' border='0' width='500px' class='innertable' >" + sContent + "</table></td></tr></table></span>";
        return sFinal;
    }
}
