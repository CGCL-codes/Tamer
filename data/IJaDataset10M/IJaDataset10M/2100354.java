package org.makumba.list.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import org.makumba.LogicException;
import org.makumba.ProgrammerError;
import org.makumba.analyser.PageCache;
import org.makumba.commons.MakumbaJspAnalyzer;
import org.makumba.list.engine.valuecomputer.ValueComputer;

/**
 * If tag will accept test="..." similar to value tag, and will show body only if OQL expression evaluates to true
 * (integer 1).
 * 
 * @author Frederik Habilis
 * @since makumba-0.5.9.11
 * @version $Id: IfTag.java 2266 2008-04-30 23:19:14Z cristian_bogdan $
 */
public class IfTag extends GenericListTag implements BodyTag {

    private static final long serialVersionUID = 1L;

    String testExpr;

    private static final Integer TRUE_INT = new Integer(1);

    public void setTest(String s) {
        this.testExpr = s;
    }

    public void setBodyContent(BodyContent bc) {
    }

    public void doInitBody() {
    }

    /**
     * Sets tagKey to uniquely identify this tag. Called at analysis time before doStartAnalyze() and at runtime before
     * doMakumbaStartTag()
     * @param pageCache the page cache of the current page
     */
    public void setTagKey(PageCache pageCache) {
        addToParentListKey(testExpr.trim());
    }

    /** 
     * Determines the ValueComputer and associates it with the tagKey .
     * If we use Hibernate we need to adjust the syntax
     * @param pageCache the page cache of the current page
     * 
     * FIXME QueryExecutionProvider should tell us the syntax for the if boolean test
     * 
     */
    public void doStartAnalyze(PageCache pageCache) {
        String te = testExpr;
        if (MakumbaJspAnalyzer.getQueryLanguage(pageCache).equals("hql")) te = "case when " + testExpr + " then 1 else 0 end";
        pageCache.cache(GenericListTag.VALUE_COMPUTERS, tagKey, ValueComputer.getValueComputerAtAnalysis(this, QueryTag.getParentListKey(this, pageCache), te, pageCache));
    }

    /** 
     * Tells the ValueComputer to finish analysis, check for proper test result type
     * @param pageCache the page cache of the current page
     */
    public void doEndAnalyze(PageCache pageCache) {
        ValueComputer vc = (ValueComputer) pageCache.retrieve(GenericListTag.VALUE_COMPUTERS, tagKey);
        vc.doEndAnalyze(pageCache);
        String type = vc.getType().getDataType();
        if (!"int".equals(type)) {
            throw new ProgrammerError("mak:if test expression must be of type 'int'. In this case [" + this + "], type is " + type);
        }
    }

    /** Asks the ValueComputer to calculate the expression, and SKIP_BODY if false.
     * @param pageCache the page cache of the current page
     * @throws JspException
     * @throws LogicException
     */
    public int doAnalyzedStartTag(PageCache pageCache) throws JspException, org.makumba.LogicException {
        Object exprvalue = ((ValueComputer) pageCache.retrieve(GenericListTag.VALUE_COMPUTERS, tagKey)).getValue(this.getPageContext());
        if (exprvalue instanceof Integer) {
            int i = ((Integer) exprvalue).intValue();
            if (i == 1) {
                return EVAL_BODY_INCLUDE;
            } else if (i == 0) {
                return SKIP_BODY;
            }
            throw new MakumbaJspException(this, "test expression in mak:if should result in 0 or 1; result is " + exprvalue);
        }
        if (exprvalue == null) return SKIP_BODY;
        throw new MakumbaJspException(this, "test expression in mak:if should result in an Integer, result is " + exprvalue);
    }

    public String toString() {
        return "IF test=" + testExpr + " parameters: " + params;
    }
}
