package org.jamwiki.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.jamwiki.utils.Utilities;

/**
 *
 */
public class SeparateWordsTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(SeparateWordsTag.class);

    private String value;

    /**
	 *
	 */
    public int doEndTag() throws JspException {
        evaluateExpressions();
        JspWriter out = pageContext.getOut();
        try {
            out.print(Utilities.separateWords(value));
        } catch (IOException e) {
            logger.warn(e);
        }
        return EVAL_PAGE;
    }

    /**
	 *
	 */
    public String getValue() {
        return value;
    }

    /**
	 *
	 */
    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * Evaluates expressions as necessary
	 */
    private void evaluateExpressions() throws JspException {
        value = (String) ExpressionUtil.evalNotNull("out", "value", value, String.class, this, pageContext);
    }
}
