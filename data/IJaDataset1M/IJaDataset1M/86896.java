package com.rooster.action.requirement;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.utils.RoosterConstants;
import com.rooster.utils.StringPurifier;

public class CreateClrJobId extends Action {

    static Logger logger = Logger.getLogger(CreateClrJobId.class.getName());

    Connection myConnection = null;

    Statement clrJobStmt = null;

    ResultSet clrRs = null;

    DataSource dataSource = null;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.setContentType("text/xml");
                PrintWriter out = res.getWriter();
                out.write("SESSION_TIMEOUT");
                out.flush();
                out.close();
            } catch (Exception e) {
            }
            return null;
        }
        String clrJobId = req.getParameter("partial_job_id");
        clrJobId = StringPurifier.getReplacedContent(clrJobId);
        String sPostSkill = req.getParameter("skill");
        sPostSkill = StringPurifier.getReplacedContent(sPostSkill);
        String sState = req.getParameter("state");
        sState = StringPurifier.getReplacedContent(sState);
        dataSource = getDataSource(req);
        String sSequence = "";
        if ((clrJobId == null) || (clrJobId.equals(new String("")))) {
            sSequence = getNextSequence();
            clrJobId = sSequence;
        } else {
            clrJobId = clrJobId.trim();
        }
        String newclrjobid = getNewClrJobId(req, sPostSkill, sState, clrJobId);
        boolean bFound = checkClrJobId(newclrjobid);
        logger.debug("newclrjobid	:" + newclrjobid + "...." + bFound);
        if (clrJobId.equals(new String(""))) {
            if (bFound) {
                while (true) {
                    sSequence = getNextSequence();
                    clrJobId = sSequence;
                    newclrjobid = getNewClrJobId(req, sPostSkill, sState, clrJobId);
                    bFound = checkClrJobId(newclrjobid);
                    if (bFound == false) {
                        break;
                    }
                }
            }
        } else {
            if (bFound) {
                try {
                    res.setContentType("text/xml");
                    PrintWriter out = res.getWriter();
                    out.write("ALREADY_AVAILABLE");
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            out.write(newclrjobid);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkClrJobId(String newclrjobid) {
        boolean bFound = false;
        try {
            myConnection = dataSource.getConnection();
            clrJobStmt = myConnection.createStatement();
            clrRs = clrJobStmt.executeQuery("select clrJobId from requirement where clrJobId='" + newclrjobid + "';");
            while (clrRs.next()) {
                bFound = true;
            }
        } catch (Exception e) {
            logger.warn(e);
        } finally {
            try {
                if (clrRs != null) {
                    clrRs.close();
                }
                if (clrJobStmt != null) {
                    clrJobStmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
            }
        }
        return bFound;
    }

    private String getNextSequence() {
        int iLastGenerated = 0;
        String clrJobId = "";
        try {
            myConnection = dataSource.getConnection();
            clrJobStmt = myConnection.createStatement();
            clrRs = clrJobStmt.executeQuery("select val from bonsai_sequences where table_name='requirement'");
            while (clrRs.next()) {
                iLastGenerated = clrRs.getInt("val");
            }
            iLastGenerated++;
            clrJobId = String.valueOf(iLastGenerated);
            String zeroValue = "0";
            if (clrJobId.length() < 4) {
                for (int i = clrJobId.length(); i < 3; i++) {
                    zeroValue += "0";
                }
            }
            clrJobId = "AA" + zeroValue + clrJobId;
        } catch (Exception e) {
            logger.warn(e);
        } finally {
            try {
                if (clrRs != null) {
                    clrRs.close();
                }
                if (clrJobStmt != null) {
                    clrJobStmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
            }
        }
        try {
            myConnection = dataSource.getConnection();
            clrJobStmt = myConnection.createStatement();
            clrJobStmt.executeUpdate("update bonsai_sequences set val=" + iLastGenerated + " where table_name='requirement'");
        } catch (Exception e) {
            logger.warn(e);
        } finally {
            try {
                if (clrJobStmt != null) {
                    clrJobStmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
            }
        }
        return clrJobId;
    }

    public String getNewClrJobId(HttpServletRequest req1, String sSkillName, String sState, String sClrJobId) {
        String sNewClrJobId = new String();
        String sSkillCode = new String();
        HttpSession session = req1.getSession(false);
        String sShortName = String.valueOf(session.getAttribute(RoosterConstants.COMPANY_SHORT_NAME));
        DataSource dsJob = null;
        Connection dbConJob = null;
        Statement stmtJob = null;
        ResultSet rsJob = null;
        try {
            String sSql = "select skill_id from velrec_requirement_skill_tbl where skill_name='" + sSkillName + "'";
            dsJob = getDataSource(req1);
            dbConJob = dsJob.getConnection();
            stmtJob = dbConJob.createStatement();
            rsJob = stmtJob.executeQuery(sSql);
            while (rsJob.next()) {
                sSkillCode = rsJob.getString(1);
            }
        } catch (Exception e) {
            getServlet().log("Connection.process", e);
        } finally {
            try {
                if (rsJob != null) {
                    rsJob.close();
                }
                if (stmtJob != null) {
                    stmtJob.close();
                }
                if (dbConJob != null) {
                    dbConJob.close();
                }
            } catch (SQLException e) {
                getServlet().log("Connection.close", e);
            }
        }
        sClrJobId = sClrJobId.toUpperCase();
        sNewClrJobId = sShortName + "_" + sState + "_" + sSkillCode + "_" + sClrJobId;
        return sNewClrJobId;
    }
}
