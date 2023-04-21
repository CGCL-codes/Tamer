package net.sf.wgfa.struts.action.crawler;

import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.search.crawler.Crawl;
import net.sf.wgfa.search.crawler.CrawlController;
import net.sf.wgfa.struts.form.crawler.ExportCrawlForm;
import net.sf.wgfa.util.CompressedDownload;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author tobias
 */
public class ExportRDFAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExportCrawlForm ecf = (ExportCrawlForm) form;
        OutputStream os = CompressedDownload.createDownload(response, "application/xml", ecf.getZip(), ecf.getCrawl(), ecf.getSavetodisk());
        CrawlController cc = CrawlController.getSingleton();
        Crawl c = cc.getCrawl(ecf.getCrawl());
        c.getModel().write(os);
        os.close();
        return null;
    }
}
