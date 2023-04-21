package cn.vlabs.duckling.vwb.tags;

import java.io.IOException;
import java.security.ProviderException;
import java.util.Collection;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;
import cn.vlabs.duckling.dct.services.dpage.DPage;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.services.resource.Resource;

public class HistoryIteratorTag extends IteratorTag {

    private static final long serialVersionUID = 0L;

    static Logger log = Logger.getLogger(HistoryIteratorTag.class);

    private int m_start;

    private int m_pagesize;

    public final int doStartTag() {
        vwbcontext = (VWBContext) pageContext.getAttribute(VWBContext.CONTEXT_KEY, PageContext.REQUEST_SCOPE);
        DPage page = (DPage) vwbcontext.getResource();
        try {
            if (m_pagesize < 0) m_pagesize = -1;
            if (page != null && vwbcontext.resourceExists(page.getResourceId())) {
                Collection<DPage> versions = null;
                if (m_start < 0) {
                    versions = vwbcontext.getSite().getDpageService().getDpageVersionsByResourceId(page.getResourceId());
                } else {
                    versions = vwbcontext.getSite().getDpageService().getDpageVersionsByResourceIdD(page.getResourceId(), m_start - 1, m_pagesize);
                }
                if (versions == null) {
                    return SKIP_BODY;
                }
                m_iterator = versions.iterator();
                if (m_iterator.hasNext()) {
                    refreshContext((Resource) m_iterator.next());
                } else {
                    return SKIP_BODY;
                }
            }
            return EVAL_BODY_BUFFERED;
        } catch (ProviderException e) {
            log.fatal("Provider failed while trying to iterator through history", e);
        }
        return SKIP_BODY;
    }

    public final int doAfterBody() {
        if (bodyContent != null) {
            try {
                JspWriter out = getPreviousOut();
                out.print(bodyContent.getString());
                bodyContent.clearBody();
            } catch (IOException e) {
                log.error("Unable to get inner tag text", e);
            }
        }
        if (m_iterator != null && m_iterator.hasNext()) {
            refreshContext((Resource) m_iterator.next());
            return EVAL_BODY_BUFFERED;
        }
        return SKIP_BODY;
    }

    private void refreshContext(Resource resource) {
        vwbcontext.targetCommand(resource);
        pageContext.setAttribute(getId(), resource, PageContext.PAGE_SCOPE);
    }

    /**
	 * @param m_start the m_start to set
	 */
    public void setStart(int start) {
        this.m_start = start;
    }

    /**
	 * @param m_pagesize the m_pagesize to set
	 */
    public void setPagesize(int pagesize) {
        this.m_pagesize = pagesize;
    }
}
