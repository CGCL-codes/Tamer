package nuts.exts.struts2.views.freemarker;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.exts.struts2.util.StrutsContextUtils;
import org.apache.struts2.views.freemarker.FreemarkerResult;
import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.RequestConstants;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateModel;

/**
 * FreemarkerDecorateResult
 */
@SuppressWarnings("serial")
public class FreemarkerDecorateResult extends FreemarkerResult {

    /**
	 * Constructor 
	 */
    public FreemarkerDecorateResult() {
        super();
    }

    /**
	 * Constructor
	 * @param location location
	 */
    public FreemarkerDecorateResult(String location) {
        super(location);
    }

    /**
	 * @see org.apache.struts2.views.freemarker.FreemarkerResult#preTemplateProcess(freemarker.template.Template, freemarker.template.TemplateModel)
	 */
    @Override
    protected boolean preTemplateProcess(Template template, TemplateModel model) throws IOException {
        HttpServletResponse res = StrutsContextUtils.getServletResponse();
        res.setCharacterEncoding("UTF-8");
        if (model instanceof SimpleHash) {
            SimpleHash hash = (SimpleHash) model;
            HttpServletRequest req = StrutsContextUtils.getServletRequest();
            hash.put("base", req.getContextPath());
            Page page = (Page) req.getAttribute(RequestConstants.PAGE);
            if (page != null) {
                hash.put("page", page);
                if (page instanceof HTMLPage) {
                    HTMLPage htmlPage = ((HTMLPage) page);
                    hash.put("head", htmlPage.getHead());
                }
                hash.put("title", page.getTitle());
                hash.put("body", page.getBody());
                hash.put("page.properties", new SimpleHash(page.getProperties()));
            }
        }
        return super.preTemplateProcess(template, model);
    }
}
