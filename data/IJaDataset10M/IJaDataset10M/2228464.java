package org.tagunit.tagext.display;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.tagunit.TestContext;

/**
 * Evaluates its body content if the current test failed.
 *
 * @author    Simon Brown
 */
public class ErrorTag extends TagSupport {

    /**
   * Called when the starting tag is encountered.
   */
    public int doStartTag() throws JspException {
        TestContextProvider prov = (TestContextProvider) findAncestorWithClass(this, TestContextProvider.class);
        if (prov.getTestContext().getStatus() == TestContext.ERROR) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }
}
