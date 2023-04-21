package net.sf.wgfa.struts.action.crawler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.exceptions.WgfaCrawlNotFoundException;
import net.sf.wgfa.exceptions.WgfaDatabaseException;
import net.sf.wgfa.search.crawler.Crawl;
import net.sf.wgfa.search.crawler.CrawlController;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewCrawlAction extends Action {

    /**
     * Method execute
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws MalformedURLException
     * @throws WgfaCrawlExistsException
     * @throws WgfaCrawlNotFoundException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws WgfaCrawlNotFoundException, WgfaDatabaseException {
        CrawlController cc = CrawlController.getSingleton();
        Crawl crawl = cc.getCrawl(request.getParameter("crawl"));
        request.getSession().setAttribute("c", crawl);
        return mapping.findForward("success");
    }
}
