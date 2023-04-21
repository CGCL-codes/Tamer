package com.rooster.action.skill;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.action.common.DbUtil;
import com.rooster.utils.SkillUtil;

public class DeleteSkill extends Action {

    DataSource dataSource;

    private Logger logger = Logger.getLogger(DeleteSkill.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.info("com.rooster.action.skill.DeleteSkill.execute - Entry");
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        String sRecruiterId = String.valueOf(session.getAttribute("UserId"));
        dataSource = getDataSource(req);
        String sSkillId = new String();
        String[] sSkillIds = req.getParameterValues("skill_id");
        String sConfirmDelete = req.getParameter("delete_confirm");
        if ((sConfirmDelete == null) || (sConfirmDelete.equals(new String("")))) {
        } else {
            String sConfirmSkillIds = req.getParameter("skill_ids");
            deleteSkill(sConfirmSkillIds, sRecruiterId);
            ServletContext context = servlet.getServletContext();
            context.setAttribute("SKILL_LIST", SkillUtil.getSkillList(dataSource));
            req.setAttribute("Info", "Successfully Deleted");
            return map.findForward("show_delete_info");
        }
        Connection myConnection = null;
        Statement stmtSkill = null;
        ResultSet rsQuery = null;
        boolean bFound = false;
        Vector vJobIds = new Vector();
        try {
            for (int i = 0; i < sSkillIds.length; i++) {
                sSkillId += "'";
                sSkillId += sSkillIds[i];
                sSkillId += "'";
                if (i != sSkillIds.length - 1) {
                    sSkillId += ",";
                }
            }
            String sReqSkill = "select a.clrJobId from requirement a,velrec_requirement_skill_tbl b where b.visible=1 and a.visible=1 and a.archive='no' and a.postSkill=b.skill_name and b.skill_id in (" + sSkillId + ") order by a.rdDt desc";
            myConnection = dataSource.getConnection();
            stmtSkill = myConnection.createStatement();
            rsQuery = stmtSkill.executeQuery(sReqSkill);
            while (rsQuery.next()) {
                bFound = true;
                vJobIds.add(rsQuery.getString(1));
            }
        } catch (Exception e) {
            logger.debug("com.rooster.action.skill.DeleteSkill.execute - Exit" + e);
        } finally {
            try {
                DbUtil.closeResultSet(rsQuery);
                DbUtil.closeStatement(stmtSkill);
                DbUtil.closeConnection(myConnection);
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
        if (bFound) {
            req.setAttribute("vector_clrJobIds", vJobIds);
            req.setAttribute("skill_ids", sSkillId);
            return map.findForward("confirm_delete");
        } else {
            deleteSkill(sSkillId, sRecruiterId);
            ServletContext context = servlet.getServletContext();
            context.setAttribute("SKILL_LIST", SkillUtil.getSkillList(dataSource));
            req.setAttribute("Info", "Successfully Deleted");
        }
        logger.info("com.rooster.action.skill.DeleteSkill.execute - Exit");
        return map.findForward("show_delete_info");
    }

    private void deleteSkill(String sSkillId, String sRecruiterId) {
        Connection myConnection = null;
        Statement stmtSkill = null;
        try {
            String sSqlSkill = "update velrec_requirement_skill_tbl set visible=0,updated_by='" + sRecruiterId + "'  where skill_id in(" + sSkillId + ");";
            logger.debug("Skill Delete " + sSqlSkill);
            myConnection = dataSource.getConnection();
            stmtSkill = myConnection.createStatement();
            stmtSkill.executeUpdate(sSqlSkill);
        } catch (Exception e) {
            logger.debug("com.rooster.action.skill.DeleteSkill.execute - Exit" + e);
        } finally {
            try {
                DbUtil.closeStatement(stmtSkill);
                DbUtil.closeConnection(myConnection);
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
    }
}
