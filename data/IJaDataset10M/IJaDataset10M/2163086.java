package net.sf.wgfa.struts.action.crawler;

import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.exceptions.WgfaCrawlExistsException;
import net.sf.wgfa.exceptions.WgfaCrawlNotFoundException;
import net.sf.wgfa.exceptions.WgfaDatabaseException;
import net.sf.wgfa.search.crawler.Crawl;
import net.sf.wgfa.search.crawler.CrawlController;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StopCrawlAction extends Action {

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
	 * @throws InterruptedException 
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, WgfaCrawlExistsException, WgfaCrawlNotFoundException, InterruptedException, WgfaDatabaseException {
        request.setAttribute("crawlController", CrawlController.getSingleton());
        CrawlController cc = CrawlController.getSingleton();
        Crawl c = cc.getCrawl(request.getParameter("crawl"));
        c.stop();
        return mapping.findForward("success");
    }
}
