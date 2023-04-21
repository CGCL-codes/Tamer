package si.unimb.cot.mgbl.gamemgmt.actionbeans.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import si.unimb.cot.mgbl.gamemgmt.actionbeans.TransactionFreeLocalizedAction;
import si.unimb.cot.mgbl.gamemgmt.dao.GameDao;
import si.unimb.cot.mgbl.gamemgmt.dao.GameTemplateDao;
import si.unimb.cot.mgbl.gamemgmt.datamodel.GameHiber;
import si.unimb.cot.mgbl.gamemgmt.formbeans.AddNewGameForm;
import si.unimb.cot.mgbl.gamemgmt.formbeans.MessageBean;
import si.unimb.cot.mgbl.gamemgmt.security.SecurityCenter;
import evolaris.framework.database.util.HibernateSessions;

/**
 * Prepare form for adding / modifying game
 * @author Luka Pavli� (luka.pavlic@uni-mb.si)
 */
public class AddNewGamePrepareAction extends TransactionFreeLocalizedAction {

    /**
	 * @see evolaris.framework.sys.web.action.AnonymousLocalizedAction#defaultMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    public ActionForward defaultMethod(ActionMapping am, ActionForm af, HttpServletRequest req, HttpServletResponse res) {
        super.defaultMethod(am, af, req, res);
        this.session = HibernateSessions.getSessionFactory().openSession();
        try {
            AddNewGameForm form = (AddNewGameForm) af;
            form.setMyUserId(webUser.getId());
            form.reset();
            form.setTemplates(GameTemplateDao.getAllGameTemplates(session));
            if (req.getParameter("template") != null) {
                form.setTemplateToUse(Long.parseLong(req.getParameter("template")));
            }
            if (req.getParameter("id") != null) {
                long id = Long.parseLong(req.getParameter("id"));
                GameHiber g = GameDao.findGameHiber(id, session);
                if (SecurityCenter.canIEditGame(webUser.getId(), g.getId(), session)) {
                    form.setName(g.getName());
                    form.setDescription(g.getDescription());
                    form.setTemplateToUse(g.getBasedOnTemplate().getId());
                    form.setModifyMe(true);
                    form.setModifyId(id);
                    form.setInstructions(g.getInstructions());
                    form.setDC_contributor(g.getDC_contributor());
                    form.setDC_coverage(g.getDC_coverage());
                    form.setDC_creator(g.getDC_creator());
                    form.setDC_date(g.getDC_date());
                    form.setDC_description(g.getDC_description());
                    form.setDC_format(g.getDC_format());
                    form.setDC_identifier(g.getDC_identifier());
                    form.setDC_language(g.getDC_language());
                    form.setDC_publisher(g.getDC_publisher());
                    form.setDC_relation(g.getDC_relation());
                    form.setDC_rights(g.getDC_rights());
                    form.setDC_source(g.getDC_source());
                    form.setDC_subject(g.getDC_subject());
                    form.setDC_title(g.getDC_title());
                    form.setDC_type(g.getDC_type());
                    form.setDC_contributor_manual(g.getDC_contributor_manual());
                    form.setDC_coverage_manual(g.getDC_coverage_manual());
                    form.setDC_creator_manual(g.getDC_creator_manual());
                    form.setDC_date_manual(g.getDC_date_manual());
                    form.setDC_description_manual(g.getDC_description_manual());
                    form.setDC_format_manual(g.getDC_format_manual());
                    form.setDC_identifier_manual(g.getDC_identifier_manual());
                    form.setDC_language_manual(g.getDC_language_manual());
                    form.setDC_publisher_manual(g.getDC_publisher_manual());
                    form.setDC_relation_manual(g.getDC_relation_manual());
                    form.setDC_rights_manual(g.getDC_rights_manual());
                    form.setDC_source_manual(g.getDC_source_manual());
                    form.setDC_subject_manual(g.getDC_subject_manual());
                    form.setDC_title_manual(g.getDC_title_manual());
                    form.setDC_type_manual(g.getDC_type_manual());
                } else {
                    MessageBean.setMyMessage(req, ("Error: Insufficient privileges."));
                }
            }
            if (req.getParameter("like") != null) {
                long id = Long.parseLong(req.getParameter("like"));
                GameHiber g = GameDao.findGameHiber(id, session);
                if (SecurityCenter.canICopyGame(webUser.getId(), g)) {
                    form.setName(g.getName());
                    form.setDescription(g.getDescription());
                    form.setTemplateToUse(g.getBasedOnTemplate().getId());
                    form.setLikeMe(true);
                    form.setLikeId(id);
                    form.setInstructions(g.getInstructions());
                } else {
                    MessageBean.setMyMessage(req, ("Error: Insufficient privileges."));
                }
            }
            return am.findForward("ok");
        } finally {
            this.session.close();
        }
    }
}
