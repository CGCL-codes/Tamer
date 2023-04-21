package it.gentlewebsite.portal.actions;

import java.util.Enumeration;
import it.gentlewebsite.portal.dao.managers.ContentMain;
import it.gentlewebsite.portal.dao.managers.PubblicationMain;
import it.gentlewebsite.portal.dao.News;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class SaveNews extends SaveWebObject {

    private static final String LOGNAME = "it.gentlewebsite.portal.actions.SaveNews";

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Logger.getLogger(LOGNAME).debug(" execute called");
        Enumeration enume = request.getAttributeNames();
        while (enume.hasMoreElements()) {
            String name = (String) enume.nextElement();
            System.out.println(name);
            System.out.println(request.getAttribute(name));
            System.out.println((request.getAttribute(name)).getClass());
        }
        NewsForm aform = (NewsForm) form;
        Logger.getLogger(LOGNAME).debug("News with title: " + aform.getNews().getTitle() + " and body: " + aform.getNews().getBody() + " saved");
        final ContentMain cmain = new ContentMain();
        final PubblicationMain pubbMain = new PubblicationMain();
        final News news = aform.getNews();
        final boolean published = pubbMain.existsPubblicationPubblished(getServlet().getServletContext(), news);
        if (news.getId() != null && published) {
            request.setAttribute("contentMessage", "news.published.notsaved");
            request.setAttribute("existsMessage", new Boolean(true));
        } else {
            setModifiedby(news, request);
            cmain.saveOrUpdate(getServlet().getServletContext(), news);
            request.setAttribute("contentMessage", "news.saved");
            request.setAttribute("existsMessage", new Boolean(true));
        }
        request.getSession().removeAttribute("NewsForm");
        return mapping.findForward("success");
    }
}
