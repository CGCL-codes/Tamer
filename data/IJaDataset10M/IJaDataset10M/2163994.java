package org.authorsite.bib.web.taglib;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import org.authorsite.bib.dto.*;
import org.apache.commons.beanutils.*;
import org.apache.struts.Globals;
import org.apache.struts.util.*;

/**
 *
 * @author  jejking
 * @version $Revision: 1.2 $
 */
public class PersonTag extends TagSupport {

    private BibTagUtils utils;

    private String detail;

    private String beanName;

    private boolean checkBoxFlag;

    /** Creates a new instance of PersonTag */
    public PersonTag() {
        utils = new BibTagUtils();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String newDetail) {
        detail = newDetail;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String newBeanName) {
        beanName = newBeanName;
    }

    public boolean getCheckBoxFlag() {
        return checkBoxFlag;
    }

    public void setCheckBoxFlag(boolean newCheckBoxFlag) {
        checkBoxFlag = newCheckBoxFlag;
    }

    public int doStartTag() throws JspException {
        String html = null;
        PersonDTO dto = null;
        String[] personIDs = null;
        if (beanName != null && !beanName.equals("")) {
            dto = (PersonDTO) pageContext.findAttribute(beanName);
        } else {
            dto = (PersonDTO) pageContext.getAttribute("CurrentPersonDTO");
        }
        if (dto == null) {
            dto = (PersonDTO) pageContext.findAttribute("CurrentPersonDTO");
        }
        if (dto == null) {
            return (SKIP_BODY);
        }
        personIDs = (String[]) pageContext.getAttribute("PersonIDs");
        if (detail != null && !detail.equals("full")) {
            html = utils.renderPersonConcise(pageContext, dto, personIDs, checkBoxFlag);
        } else {
            html = utils.renderPersonFull(pageContext, dto, personIDs, checkBoxFlag);
        }
        try {
            pageContext.getOut().write(html);
        } catch (IOException ioe) {
            throw new JspException(ioe);
        }
        return (SKIP_BODY);
    }
}
