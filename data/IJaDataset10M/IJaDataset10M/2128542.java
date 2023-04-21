package net.sf.imca.taglibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import net.sf.imca.model.BoatForSaleBO;
import net.sf.imca.services.UiService;

public class BoatsForSale extends javax.servlet.jsp.tagext.TagSupport {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -1697708191497954575L;

    private int count = 0;

    private String countryCode = "";

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    BoatForSaleBO[] bfs;

    public int doStartTag() throws JspException {
        String parameterCountryCode = this.pageContext.getRequest().getParameter("countrycode");
        if (parameterCountryCode != null) {
            countryCode = parameterCountryCode;
        }
        UiService service = new UiService();
        bfs = service.getBoatsForSale(countryCode);
        count = 0;
        if (bfs.length > 0) {
            setPageAttributes();
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    public int doAfterBody() throws JspTagException {
        count++;
        if (count < bfs.length) {
            setPageAttributes();
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    private void setPageAttributes() {
        pageContext.setAttribute("boatForSale", bfs[count]);
        if (count % 2 == 0) {
            pageContext.setAttribute("rowClass", "even");
        } else {
            pageContext.setAttribute("rowClass", "odd");
        }
    }
}
