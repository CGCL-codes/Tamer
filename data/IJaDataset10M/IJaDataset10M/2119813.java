package net.sf.imca.taglibs;

import javax.servlet.jsp.JspException;
import net.sf.imca.model.AssociationBO;
import net.sf.imca.services.CommitteeService;

public class Association extends javax.servlet.jsp.tagext.TagSupport {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 8944804977828774091L;

    private String countryCode = "";

    private String area = "";

    public int doStartTag() throws JspException {
        String parameterCountryCode = this.pageContext.getRequest().getParameter("countrycode");
        if (parameterCountryCode != null) {
            countryCode = parameterCountryCode;
        }
        String parameterArea = this.pageContext.getRequest().getParameter("area");
        if (parameterArea != null) {
            area = parameterArea;
        }
        CommitteeService service = new CommitteeService();
        AssociationBO association = service.getAssociation(countryCode, area);
        pageContext.setAttribute("name", association.getName());
        return EVAL_BODY_INCLUDE;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
