package com.sivoh.hssftemplates.tags;

import org.apache.commons.el.ExpressionEvaluatorImpl;
import javax.servlet.jsp.el.ELException;
import javax.servlet.ServletException;
import java.util.*;
import java.util.logging.Logger;
import com.sivoh.hssftemplates.HssfTemplateContext;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public abstract class HssfBaseTag {

    protected static final Logger log = Logger.getLogger(HssfBaseTag.class.getName());

    List childTags = new ArrayList();

    ExpressionEvaluatorImpl evaluator = new ExpressionEvaluatorImpl();

    public abstract String getTagName();

    public abstract void render(HssfTemplateContext context) throws ServletException;

    protected void renderChildren(HssfTemplateContext context) throws ServletException {
        renderChildren(context, childTags);
    }

    protected void renderChildren(HssfTemplateContext context, Collection children) throws ServletException {
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            HssfBaseTag tag = (HssfBaseTag) it.next();
            tag.render(context);
        }
    }

    public void addHssfTag(HssfBaseTag tag) {
        childTags.add(tag);
    }

    public List getChildTags() {
        return childTags;
    }

    public Object parseExpression(String expression, Class expectedType, HssfTemplateContext context) throws ServletException {
        try {
            log.fine("Evaluating expression " + expression);
            return evaluator.evaluate(expression, expectedType, context, null);
        } catch (ELException ele) {
            throw new ServletException("Error parsing expression " + expression, ele);
        }
    }
}
