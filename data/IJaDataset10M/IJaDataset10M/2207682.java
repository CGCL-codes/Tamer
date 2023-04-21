package it.gentlewebsite.portal.actions;

import it.gentlewebsite.portal.dao.managers.PageMain;
import it.gentlewebsite.portal.dao.Page;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.hibernate.NonUniqueObjectException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public final class SaveSection extends SaveWebObject {

    private static final String LOGNAME = "it.gentlewebsite.portal.actions.SaveSection";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Logger.getLogger(LOGNAME).debug(" execute called");
        final String id = request.getParameter("id");
        final String idsubpage = request.getParameter("idsubpage");
        final String type = request.getParameter("type");
        Logger.getLogger(LOGNAME).debug("id page: " + id + ", idsubpage: " + idsubpage + ", type: " + type);
        if (id == null || id.trim().equals("") || type == null || !type.equals("Section") || idsubpage == null || idsubpage.trim().equals("")) {
            Logger.getLogger(LOGNAME).debug(" idpage or idcontent a null or type != Section : no Action Form set");
            return mapping.findForward("success");
        }
        PageMain pmain = new PageMain();
        Page superpage = pmain.load(getServlet().getServletContext(), new Long(id));
        Page subpage = pmain.load(getServlet().getServletContext(), new Long(idsubpage));
        ActionMessages actionMsg = new ActionMessages();
        if (superpage.isPublished() || subpage.isPublished()) {
            actionMsg.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("section.published.notsaved"));
            saveMessages(request, actionMsg);
            Logger.getLogger(LOGNAME).error("salvataggio della sottopagina impossibile perche padre o figlio pubblicati");
            return mapping.findForward("success");
        }
        try {
            if (!superpage.getSubpages().contains(subpage) && !superpage.equals(subpage)) {
                superpage.getSubpages().add(subpage);
                setModifiedby(superpage, request);
                pmain.saveOrUpdate(getServlet().getServletContext(), superpage);
                actionMsg.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("section.saved"));
            } else {
                actionMsg.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("section.notsaved"));
            }
        } catch (NonUniqueObjectException e) {
            actionMsg.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("section.notsaved"));
            Logger.getLogger(LOGNAME).error("salvataggio della sottopagina impossibile", e);
        }
        saveMessages(request, actionMsg);
        return mapping.findForward("success");
    }
}
