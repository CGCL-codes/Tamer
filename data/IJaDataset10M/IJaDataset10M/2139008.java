package com.genia.toolbox.web.taglib.head.js;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This tag generate a new javascript function name, and register the function
 * so that it is called at the initialization of the page.
 */
@SuppressWarnings("serial")
public class SpringJsAddFunction extends TagSupport {

    /**
   * the name of the javascript function.
   */
    private String functionName = null;

    /**
   * the order in which the function must be called.
   */
    private int order = 0;

    /**
   * the {@link JsFunctionContainer} to use.
   */
    private JsFunctionContainer jsFunctionContainer;

    /**
   * Processing of the start tag.
   * 
   * @throws JspException
   *           when an error occurred
   * @return SKIP_BODY
   */
    @Override
    public int doStartTag() throws JspException {
        final JsFunctionContainer jsFunctionContainer = getJsFunctionContainer();
        String functionName = getFunctionName();
        if (functionName == null) {
            functionName = jsFunctionContainer.getFunctionsBaseName() + jsFunctionContainer.getNextFunctionIdentifier();
        }
        try {
            pageContext.getOut().write(functionName);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        jsFunctionContainer.addFunction(functionName, getOrder());
        return SKIP_BODY;
    }

    /**
   * getter for the functionName property.
   * 
   * @return the functionName
   */
    public String getFunctionName() {
        return functionName;
    }

    /**
   * getter for the jsFunctionContainer property.
   * 
   * @return the jsFunctionContainer
   */
    public JsFunctionContainer getJsFunctionContainer() {
        return jsFunctionContainer;
    }

    /**
   * getter for the order property.
   * 
   * @return the order
   */
    public int getOrder() {
        return order;
    }

    /**
   * Release state.
   */
    @Override
    public void release() {
        super.release();
        functionName = null;
        order = 0;
    }

    /**
   * setter for the functionName property.
   * 
   * @param functionName
   *          the functionName to set
   */
    public void setFunctionName(final String functionName) {
        this.functionName = functionName;
    }

    /**
   * setter for the jsFunctionContainer property.
   * 
   * @param jsFunctionContainer
   *          the jsFunctionContainer to set
   */
    public void setJsFunctionContainer(JsFunctionContainer jsFunctionContainer) {
        this.jsFunctionContainer = jsFunctionContainer;
    }

    /**
   * setter for the order property.
   * 
   * @param order
   *          the order to set
   */
    public void setOrder(final int order) {
        this.order = order;
    }
}
