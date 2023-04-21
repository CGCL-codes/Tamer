package si.unimb.cot.mgbl.gameauth.actionbeans;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import si.unimb.cot.mgbl.gameauth.formbeans.MagobalAuthorForm;
import si.unimb.cot.mgbl.gamemgmt.actionbeans.TransactionFreeLocalizedAction;
import si.unimb.cot.mgbl.gamemgmt.dao.GameDao;
import si.unimb.cot.mgbl.gamemgmt.dao.MogabalGameDao;
import si.unimb.cot.mgbl.gamemgmt.datamodel.GameHiber;
import si.unimb.cot.mgbl.gamemgmt.formbeans.MessageBean;
import si.unimb.cot.mgbl.gamemgmt.mogabal.datamodel.MogabalEventHiber;
import si.unimb.cot.mgbl.gamemgmt.mogabal.datamodel.MogabalEventOptionHiber;
import si.unimb.cot.mgbl.gamemgmt.security.SecurityCenter;

/**
 * Magobal authoring handler - based on default one - prepared for developing AT 2
 * @author Luka Pavli� (luka.pavlic@uni-mb.si)
 */
public class MagobalAuthorAction extends TransactionFreeLocalizedAction {

    /**
	 * @see evolaris.framework.sys.web.action.AnonymousLocalizedAction#defaultMethod(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    public ActionForward defaultMethod(ActionMapping am, ActionForm af, HttpServletRequest req, HttpServletResponse res) {
        MagobalAuthorForm form = (MagobalAuthorForm) af;
        if (req.getParameter("startAuthoring") != null) form.reset();
        form.setMyUserId(webUser.getId());
        if (req.getParameter("cancelInsertSubmit") != null) {
            return am.findForward("ok");
        }
        if (req.getParameter("cancelChangesSubmit") != null) {
            form.reset();
            return am.findForward("cancel");
        }
        long id = Long.parseLong(req.getParameter("id"));
        GameHiber g = GameDao.findGameHiber(id, session);
        form.setGh(g);
        form.setG(GameDao.findGame(id, session));
        if (req.getParameter("hideEventContnt") != null) {
            form.getHidenEvents().add(req.getParameter("hideEventContnt"));
            return am.findForward("ok");
        }
        if (req.getParameter("showEventContnt") != null) {
            form.getHidenEvents().remove(req.getParameter("showEventContnt"));
            return am.findForward("ok");
        }
        if (req.getParameter("change") != null) {
            if (!SecurityCenter.canIEditGame(webUser.getId(), g.getId(), session)) {
                MessageBean.setMyMessage(req, ("Error: Insufficient privileges."));
                form.reset();
                return am.findForward("list");
            }
            g.setMaksimumScore(form.getMaxScores());
            String message = "Game content updated.";
            if (form.getJar().getFileName() != null) if (!form.getJar().getFileName().equals("")) {
                try {
                    Blob b = Hibernate.createBlob(form.getJar().getInputStream());
                    g.setJarContent(b);
                    message = message + " New JAR uploaded.";
                } catch (Exception e) {
                }
            }
            GameDao.updateGame(g, session);
            MessageBean.setMyMessage(req, (message));
        }
        if (req.getParameter("deleteEvent") != null) {
            try {
                ArrayList<MogabalEventHiber> events = MogabalGameDao.getGameEvents(id, session);
                for (MogabalEventHiber evt : events) {
                    if (evt.getForward() != null) if (evt.getForward().getId().equals(req.getParameter("deleteEvent"))) {
                        evt.setForward(null);
                        MogabalGameDao.updateMogabalGameEvent(evt, session);
                    }
                    for (MogabalEventOptionHiber oh : evt.getOptions()) {
                        if (oh.getForward() != null) if (oh.getForward().getId().equals(req.getParameter("deleteEvent"))) {
                            oh.setForward(null);
                            MogabalGameDao.updateMogabalGameEventOption(oh, session);
                        }
                    }
                }
                MogabalGameDao.deleteMogabalGameEvent(req.getParameter("deleteEvent"), session);
                MessageBean.setMyMessage(req, "Game event deleted.");
            } catch (Exception e) {
                MessageBean.setMyMessage(req, "Game event cannot be deleted.  ");
            }
        }
        if (req.getParameter("deleteOption") != null) {
            MogabalGameDao.deleteMogabalGameEventOption(Long.parseLong(req.getParameter("deleteOption")), session);
            MessageBean.setMyMessage(req, "Event option deleted.");
        }
        if (req.getParameter("insertQuiz") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyQuizEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyQuizEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_QUIZ);
            mh.setGame(g);
            if (req.getParameter("modifyQuizEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            Set<MogabalEventOptionHiber> opts = mh.getOptions();
            for (MogabalEventOptionHiber op : opts) {
                op.setText(req.getParameter("text" + op.getId()));
                try {
                    op.setEval(Integer.parseInt(req.getParameter("value" + op.getId())));
                } catch (Exception e) {
                }
                op.setHint(req.getParameter("hint" + op.getId()));
                op.setReward(req.getParameter("reward" + op.getId()));
                MogabalGameDao.updateMogabalGameEventOption(op, session);
            }
            if (req.getParameter("addOptionSubmit") != null) {
                MogabalEventOptionHiber opt = new MogabalEventOptionHiber();
                opt.setText("");
                opt.setEval(0);
                opt.setHint("");
                opt.setReward("");
                if (req.getParameter("modifyQuizEvent") == null) MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                MogabalGameDao.persistMogabalGameEventOption(opt, session);
                mh.getOptions().add(opt);
                MogabalGameDao.updateMogabalGameEvent(mh, session);
                try {
                    res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyQuestionEvent=" + mh.getId() + "&jsp_insertNewQEvent=1&jsp_insertNewEvent=1");
                } catch (Exception e) {
                }
                MessageBean.setMyMessage(req, "Game content updated, new option inserted.");
            } else {
                MessageBean.setMyMessage(req, "Game content updated.");
                if (req.getParameter("modifyQuizEvent") == null) {
                    MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                } else {
                    MogabalGameDao.updateMogabalGameEvent(mh, session);
                }
            }
            if (req.getParameter("modifyQuizEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertGameOver") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyGameOverEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyGameOverEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_GAME_OVER);
            mh.setGame(g);
            if (req.getParameter("modifyGameOverEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            MessageBean.setMyMessage(req, "Game content updated.");
            if (req.getParameter("modifyGameOverEvent") == null) {
                MogabalGameDao.persistMogabalGameEventHiber(mh, session);
            } else {
                MogabalGameDao.updateMogabalGameEvent(mh, session);
            }
            if (req.getParameter("modifyGameOverEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertSimple") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifySimpleEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifySimpleEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_SIMPLE);
            mh.setGame(g);
            if (req.getParameter("modifySimpleEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            mh.setReward(req.getParameter("qreward"));
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            MessageBean.setMyMessage(req, "Game content updated.");
            if (req.getParameter("modifySimpleEvent") == null) {
                MogabalGameDao.persistMogabalGameEventHiber(mh, session);
            } else {
                MogabalGameDao.updateMogabalGameEvent(mh, session);
            }
            if (req.getParameter("modifySimpleEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertNull") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyNullEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyNullEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_NULL);
            mh.setGame(g);
            if (req.getParameter("modifyNullEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setInPublicRepository(false);
            mh.setCellType(req.getParameter("qcelltype"));
            MessageBean.setMyMessage(req, "Game content updated.");
            if (req.getParameter("modifyNullEvent") == null) {
                MogabalGameDao.persistMogabalGameEventHiber(mh, session);
            } else {
                MogabalGameDao.updateMogabalGameEvent(mh, session);
            }
            if (req.getParameter("modifyNullEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertMultimedia") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyMMEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyMMEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_MULTIMEDIA);
            mh.setGame(g);
            if (req.getParameter("modifyMMEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            mh.setMultimediaResource(req.getParameter("qresource"));
            mh.setForward(null);
            try {
                mh.setForward(MogabalGameDao.findMagobalGameEvent(req.getParameter("qreward"), session));
            } catch (Exception e) {
            }
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            MessageBean.setMyMessage(req, "Game content updated.");
            if (req.getParameter("modifyMMEvent") == null) {
                MogabalGameDao.persistMogabalGameEventHiber(mh, session);
            } else {
                MogabalGameDao.updateMogabalGameEvent(mh, session);
            }
            if (req.getParameter("modifyMMEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertDecTree") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyDecTreeEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyDecTreeEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_DECISION_TREE);
            mh.setGame(g);
            if (req.getParameter("modifyDecTreeEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            Set<MogabalEventOptionHiber> opts = mh.getOptions();
            for (MogabalEventOptionHiber op : opts) {
                op.setText(req.getParameter("text" + op.getId()));
                try {
                    op.setEval(Integer.parseInt(req.getParameter("value" + op.getId())));
                } catch (Exception e) {
                }
                op.setHint(req.getParameter("hint" + op.getId()));
                op.setReward(req.getParameter("reward" + op.getId()));
                op.setForward(null);
                try {
                    op.setForward(MogabalGameDao.findMagobalGameEvent(req.getParameter("forward" + op.getId()), session));
                } catch (Exception e) {
                }
                MogabalGameDao.updateMogabalGameEventOption(op, session);
            }
            if (req.getParameter("addOptionSubmit") != null) {
                MogabalEventOptionHiber opt = new MogabalEventOptionHiber();
                opt.setText("");
                opt.setEval(0);
                opt.setHint("");
                opt.setReward("");
                opt.setForward(null);
                if (req.getParameter("modifyDecTreeEvent") == null) MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                MogabalGameDao.persistMogabalGameEventOption(opt, session);
                mh.getOptions().add(opt);
                MogabalGameDao.updateMogabalGameEvent(mh, session);
                try {
                    res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyDecTreeEvent=" + mh.getId() + "&jsp_insertNewDTEvent=1&jsp_insertNewEvent=1");
                } catch (Exception e) {
                }
                MessageBean.setMyMessage(req, "Game content updated, new option inserted.");
            } else {
                MessageBean.setMyMessage(req, "Game content updated.");
                if (req.getParameter("modifyDecTreeEvent") == null) {
                    MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                } else {
                    MogabalGameDao.updateMogabalGameEvent(mh, session);
                }
            }
            if (req.getParameter("modifyDecTreeEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertCondDecTree") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyCondDecTreeEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyCondDecTreeEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_CONDITIONAL_DECISION_TREE);
            mh.setGame(g);
            if (req.getParameter("modifyCondDecTreeEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            Set<MogabalEventOptionHiber> opts = mh.getOptions();
            for (MogabalEventOptionHiber op : opts) {
                op.setText(req.getParameter("text" + op.getId()));
                try {
                    op.setEval(Integer.parseInt(req.getParameter("value" + op.getId())));
                } catch (Exception e) {
                }
                op.setHint(req.getParameter("hint" + op.getId()));
                op.setReward(req.getParameter("reward" + op.getId()));
                op.setCondition(req.getParameter("condition" + op.getId()));
                op.setForward(null);
                try {
                    op.setForward(MogabalGameDao.findMagobalGameEvent(req.getParameter("forward" + op.getId()), session));
                } catch (Exception e) {
                }
                MogabalGameDao.updateMogabalGameEventOption(op, session);
            }
            if (req.getParameter("addOptionSubmit") != null) {
                MogabalEventOptionHiber opt = new MogabalEventOptionHiber();
                opt.setText("");
                opt.setEval(0);
                opt.setHint("");
                opt.setReward("");
                opt.setCondition("");
                opt.setForward(null);
                if (req.getParameter("modifyCondDecTreeEvent") == null) MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                MogabalGameDao.persistMogabalGameEventOption(opt, session);
                mh.getOptions().add(opt);
                MogabalGameDao.updateMogabalGameEvent(mh, session);
                try {
                    res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyCondDecTreeEvent=" + mh.getId() + "&jsp_insertNewCDTEvent=1&jsp_insertNewEvent=1");
                } catch (Exception e) {
                }
                MessageBean.setMyMessage(req, "Game content updated, new option inserted.");
            } else {
                MessageBean.setMyMessage(req, "Game content updated.");
                if (req.getParameter("modifyCondDecTreeEvent") == null) {
                    MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                } else {
                    MogabalGameDao.updateMogabalGameEvent(mh, session);
                }
            }
            if (req.getParameter("modifyCondDecTreeEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertMiniGame") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyMGameEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyMGameEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_MINI_GAME);
            mh.setGame(g);
            if (req.getParameter("modifyMGameEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            mh.setText(req.getParameter("qtext"));
            mh.setMiniGameName(req.getParameter("qgname"));
            mh.setMiniGameResultype(req.getParameter("qrestype"));
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            MessageBean.setMyMessage(req, "Game content updated.");
            if (req.getParameter("modifyMGameEvent") == null) {
                MogabalGameDao.persistMogabalGameEventHiber(mh, session);
            } else {
                MogabalGameDao.updateMogabalGameEvent(mh, session);
            }
            if (req.getParameter("modifyMGameEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertVarSet") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyVarSetEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyVarSetEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_VAR_SET);
            mh.setGame(g);
            if (req.getParameter("modifyVarSetEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            mh.setForward(null);
            try {
                mh.setForward(MogabalGameDao.findMagobalGameEvent(req.getParameter("qreward"), session));
            } catch (Exception e) {
            }
            Set<MogabalEventOptionHiber> opts = mh.getOptions();
            for (MogabalEventOptionHiber op : opts) {
                op.setText("0");
                try {
                    op.setText("" + Integer.parseInt(req.getParameter("text" + op.getId())));
                } catch (Exception e) {
                }
                op.setEval(0);
                try {
                    op.setEval(Integer.parseInt(req.getParameter("value" + op.getId())));
                } catch (Exception e) {
                }
                MogabalGameDao.updateMogabalGameEventOption(op, session);
            }
            if (req.getParameter("addOptionSubmit") != null) {
                MogabalEventOptionHiber opt = new MogabalEventOptionHiber();
                opt.setText("");
                opt.setEval(0);
                if (req.getParameter("modifyVarSetEvent") == null) MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                MogabalGameDao.persistMogabalGameEventOption(opt, session);
                mh.getOptions().add(opt);
                MogabalGameDao.updateMogabalGameEvent(mh, session);
                try {
                    res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyVarSetEvent=" + mh.getId() + "&jsp_insertNewVSEvent=1&jsp_insertNewEvent=1");
                } catch (Exception e) {
                }
                MessageBean.setMyMessage(req, "Game content updated, new variable set row inserted.");
            } else {
                MessageBean.setMyMessage(req, "Game content updated.");
                if (req.getParameter("modifyVarSetEvent") == null) {
                    MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                } else {
                    MogabalGameDao.updateMogabalGameEvent(mh, session);
                }
            }
            if (req.getParameter("modifyVarSetEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("insertCase") != null) {
            MogabalEventHiber mh = new MogabalEventHiber();
            if (req.getParameter("modifyCaseEvent") != null) {
                mh = MogabalGameDao.findMagobalGameEvent(req.getParameter("modifyCaseEvent"), session);
            }
            mh.setEventType(MogabalGameDao.EVENT_TYPE_CASE);
            mh.setGame(g);
            if (req.getParameter("modifyCaseEvent") == null) mh.setId(req.getParameter("qid"));
            mh.setPublicRepositoryName(req.getParameter("repname"));
            mh.setInPublicRepository(false);
            if (mh.getPublicRepositoryName() != null) if (!mh.getPublicRepositoryName().equals("")) if (!mh.getPublicRepositoryName().equals("null")) mh.setInPublicRepository(true);
            mh.setCellType(req.getParameter("qcelltype"));
            mh.setText("0");
            try {
                mh.setText("" + Integer.parseInt(req.getParameter("qtext")));
            } catch (Exception e) {
            }
            Set<MogabalEventOptionHiber> opts = mh.getOptions();
            for (MogabalEventOptionHiber op : opts) {
                op.setForward(null);
                try {
                    op.setForward(MogabalGameDao.findMagobalGameEvent(req.getParameter("forward" + op.getId()), session));
                } catch (Exception e) {
                }
                op.setEval(0);
                try {
                    op.setEval(Integer.parseInt(req.getParameter("value" + op.getId())));
                } catch (Exception e) {
                }
                MogabalGameDao.updateMogabalGameEventOption(op, session);
            }
            if (req.getParameter("addOptionSubmit") != null) {
                MogabalEventOptionHiber opt = new MogabalEventOptionHiber();
                opt.setForward(null);
                opt.setEval(0);
                if (req.getParameter("modifyCaseEvent") == null) MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                MogabalGameDao.persistMogabalGameEventOption(opt, session);
                mh.getOptions().add(opt);
                MogabalGameDao.updateMogabalGameEvent(mh, session);
                try {
                    res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyCaseEvent=" + mh.getId() + "&jsp_insertNewCASEEvent=1&jsp_insertNewEvent=1");
                } catch (Exception e) {
                }
                MessageBean.setMyMessage(req, "Game content updated, new case row inserted.");
            } else {
                MessageBean.setMyMessage(req, "Game content updated.");
                if (req.getParameter("modifyCaseEvent") == null) {
                    MogabalGameDao.persistMogabalGameEventHiber(mh, session);
                } else {
                    MogabalGameDao.updateMogabalGameEvent(mh, session);
                }
            }
            if (req.getParameter("modifyVarSetEvent") != null) if (!mh.getId().equals(req.getParameter("qid"))) changeEventId(mh, req.getParameter("qid"), session, req);
        }
        if (req.getParameter("copyEvent") != null) {
            MogabalEventHiber currentEvent = new MogabalEventHiber();
            currentEvent.setId("gm" + g.getId() + "_" + new Random().nextInt(1000) + "_copy");
            currentEvent.setInPublicRepository(false);
            currentEvent.setPublicRepositoryName("");
            MogabalEventHiber copyEvent = MogabalGameDao.findMagobalGameEvent(req.getParameter("copyEvent"), session);
            currentEvent.setForward(null);
            currentEvent.setMiniGameName(copyEvent.getMiniGameName());
            currentEvent.setMiniGameResultype(copyEvent.getMiniGameResultype());
            currentEvent.setMultimediaResource(copyEvent.getMultimediaResource());
            currentEvent.setReward(copyEvent.getReward());
            currentEvent.setText(copyEvent.getText());
            currentEvent.setVarSetString(copyEvent.getVarSetString());
            currentEvent.setEventType(copyEvent.getEventType());
            currentEvent.setGame(g);
            MogabalGameDao.persistMogabalGameEventHiber(currentEvent, session);
            Set<MogabalEventOptionHiber> optns = copyEvent.getOptions();
            for (MogabalEventOptionHiber op : optns) {
                MogabalEventOptionHiber newOne = new MogabalEventOptionHiber();
                newOne.setText(op.getText());
                newOne.setHint(op.getHint());
                newOne.setReward(op.getReward());
                newOne.setEval(op.getEval());
                newOne.setCondition(op.getCondition());
                newOne.setForward(null);
                MogabalGameDao.persistMogabalGameEventOption(newOne, session);
                currentEvent.getOptions().add(newOne);
                MogabalGameDao.updateMogabalGameEventOption(newOne, session);
            }
            MogabalGameDao.updateMogabalGameEvent(currentEvent, session);
            try {
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_QUIZ) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyQuestionEvent=" + currentEvent.getId() + "&jsp_insertNewQEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_GAME_OVER) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyGameOverEvent=" + currentEvent.getId() + "&jsp_insertNewGOEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_SIMPLE) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifySimpleEvent=" + currentEvent.getId() + "&jsp_insertNewSIEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_NULL) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyNullEvent=" + currentEvent.getId() + "&jsp_insertNewNULLEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_MULTIMEDIA) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyMMEvent=" + currentEvent.getId() + "&jsp_insertNewMMEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_DECISION_TREE) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyDecTreeEvent=" + currentEvent.getId() + "&jsp_insertNewDTEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_CONDITIONAL_DECISION_TREE) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyCondDecTreeEvent=" + currentEvent.getId() + "&jsp_insertNewCDTEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_MINI_GAME) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyMGameEvent=" + currentEvent.getId() + "&jsp_insertNewMGEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_VAR_SET) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyVarSetEvent=" + currentEvent.getId() + "&jsp_insertNewVSEvent=1&jsp_insertNewEvent=1");
                if (currentEvent.getEventType() == MogabalGameDao.EVENT_TYPE_CASE) res.sendRedirect("authMAGOBAL.do?id=" + g.getId() + "&modifyCaseEvent=" + currentEvent.getId() + "&jsp_insertNewCASEEvent=1&jsp_insertNewEvent=1");
            } catch (Exception e) {
            }
        }
        form.setMaxScores((int) g.getMaksimumScore());
        form.setOrderedEvents(MogabalGameDao.getGameEvents(g.getId(), session));
        return am.findForward("ok");
    }

    private MogabalEventHiber changeEventId(MogabalEventHiber mh, String newId, Session session, HttpServletRequest req) {
        MogabalEventHiber ret = new MogabalEventHiber();
        ret.setId(newId);
        ret.setGame(mh.getGame());
        ret.setInPublicRepository(mh.isInPublicRepository());
        ret.setPublicRepositoryName(mh.getPublicRepositoryName());
        ret.setEventType(mh.getEventType());
        ret.setCellType(mh.getCellType());
        ret.setText(mh.getText());
        ret.setReward(mh.getReward());
        ret.setMiniGameName(mh.getMiniGameName());
        ret.setMiniGameResultype(mh.getMiniGameResultype());
        ret.setMultimediaResource(mh.getMultimediaResource());
        ret.setVarSetString(mh.getVarSetString());
        ret.setForward(mh.getForward());
        ret.setOptions(new HashSet<MogabalEventOptionHiber>());
        try {
            MogabalGameDao.persistMogabalGameEventHiber(ret, session);
        } catch (Exception e) {
            MessageBean.appendMyMessage(req, "Game ID cannot be updated. Do you already have event with the same ID?");
            return null;
        }
        for (MogabalEventOptionHiber mo : mh.getOptions()) {
            MogabalEventOptionHiber newo = new MogabalEventOptionHiber();
            newo.setText(mo.getText());
            newo.setEval(mo.getEval());
            newo.setHint(mo.getHint());
            newo.setReward(mo.getReward());
            newo.setCondition(mo.getCondition());
            newo.setForward(mo.getForward());
            ret.getOptions().add(newo);
            MogabalGameDao.persistMogabalGameEventOption(newo, session);
        }
        MogabalGameDao.updateMogabalGameEvent(ret, session);
        MogabalGameDao.deleteMogabalGameEvent(mh.getId(), session);
        return ret;
    }
}
