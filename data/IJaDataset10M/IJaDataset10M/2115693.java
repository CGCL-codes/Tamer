package com.rooster.action.candidate.edit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.rooster.action.question.QuestionBean;
import com.rooster.utils.candidate.QuestionnaireUtil;

public class ShowEditQuestionnaireAction extends Action {

    Logger logger = Logger.getLogger(ShowEditQuestionnaireAction.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
                logger.debug(e);
            }
            return null;
        }
        String sEmailId = String.valueOf(session.getAttribute("UserId"));
        String sFrom = String.valueOf(req.getParameter("from"));
        logger.info("From: " + sFrom);
        QuestionnaireUtil oQUtil = new QuestionnaireUtil();
        ArrayList<QuestionBean> alQuestForm = new ArrayList<QuestionBean>();
        DataSource db;
        try {
            db = getDataSource(req);
            alQuestForm = oQUtil.getQuestionnaire(db, sEmailId);
        } catch (SQLException e) {
            logger.debug(e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        }
        req.setAttribute("from", sFrom);
        req.setAttribute("QuestInfo", alQuestForm);
        return map.findForward("success");
    }
}
