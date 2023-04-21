package com.centraview.note;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveEditNoteHandler extends org.apache.struts.action.Action {

    private static Logger logger = Logger.getLogger(SaveEditNoteHandler.class);

    public static final String GLOBAL_FORWARD_failure = "failure";

    private static final String FORWARD_saveeditnote = ".view.notes.saveeditnote";

    private static final String FORWARD_notelist = ".view.notes.notelist";

    private static String FORWARD_final = GLOBAL_FORWARD_failure;

    public SaveEditNoteHandler() {
    }

    /**
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        try {
            String bottomFrame = request.getParameter("bottomFrame");
            if (request.getParameter("saveType").equals("close")) {
                request.setAttribute("bodycontent", null);
                FORWARD_final = FORWARD_notelist;
                if (bottomFrame != null && bottomFrame.equals("true")) {
                    FORWARD_final = ".view.notes.relatednote";
                }
                return mapping.findForward(FORWARD_final);
            }
            HttpSession session = request.getSession();
            int individualId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
            NoteVO noteVO = new NoteVO();
            noteVO.setTitle(((NoteForm) form).getTitle());
            noteVO.setDetail(((NoteForm) form).getDetail());
            int indId = 0;
            if (((NoteForm) form).getIndividualid() != null && ((NoteForm) form).getIndividualid().length() > 0) {
                indId = Integer.parseInt(((NoteForm) form).getIndividualid());
            }
            noteVO.setRelateIndividual(indId);
            int entId = 0;
            if (((NoteForm) form).getEntityid() != null && ((NoteForm) form).getEntityid().length() > 0) {
                entId = Integer.parseInt(((NoteForm) form).getEntityid());
            }
            noteVO.setRelateEntity(entId);
            int noteID = 0;
            noteVO.setNoteId(noteID);
            noteVO.setCreatedBy(individualId);
            noteVO.setOwner(individualId);
            NoteHome noteHome = (NoteHome) CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
            Note noteRemote = noteHome.create();
            noteRemote.setDataSource(dataSource);
            noteRemote.updateNote(individualId, noteVO);
            if (request.getParameter("saveType").equals("save")) {
                request.setAttribute("bodycontent", "editnote");
                FORWARD_final = FORWARD_saveeditnote;
            } else if (request.getParameter("saveType").equals("saveclose")) {
                request.setAttribute("bodycontent", null);
                FORWARD_final = FORWARD_notelist;
            }
            if (bottomFrame != null && bottomFrame.equals("true")) {
                FORWARD_final = ".view.notes.relatednote";
            }
        } catch (Exception e) {
            logger.error("[execute]: Exception", e);
            FORWARD_final = GLOBAL_FORWARD_failure;
        }
        return mapping.findForward(FORWARD_final);
    }
}
