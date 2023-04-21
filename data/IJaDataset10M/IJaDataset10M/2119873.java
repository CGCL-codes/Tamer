package org.makumba.list.functions;

import javax.servlet.jsp.PageContext;
import org.makumba.LogicException;
import org.makumba.ProgrammerError;
import org.makumba.analyser.AnalysableElement;
import org.makumba.analyser.AnalysableExpression;
import org.makumba.analyser.PageCache;
import org.makumba.commons.StringUtils;
import org.makumba.list.tags.QueryTag;

/**
 * Represents all types of mak:count() functions, and does analysis on them.<br/>
 * FIXME: maybe {@link QueryTag#count()} and others should move here.
 * 
 * @author Rudolf Mayer
 * @version $Id: CountFunctions.java 5190 2010-05-22 21:20:09Z manuelbernhardt $
 */
public class CountFunctions extends AnalysableExpression {

    private static final long serialVersionUID = 1L;

    @Override
    public void analyze(PageCache pageCache) {
        if (StringUtils.equalsAny(expression, "count", "maxCount")) {
            checkNumberOfArguments(0);
            if (findParentWithClass(QueryTag.class) == null) {
                throw new ProgrammerError("Function '" + expression + "' needs to be enclosed in a LIST or OBJECT tag");
            }
        } else if (expression.equals("lastCount")) {
            checkNumberOfArguments(0);
            if (AnalysableElement.getElementBefore(pageCache, elData, QueryTag.class) == null) {
                throw new ProgrammerError("Function '" + expression + "' on " + elData.getLocation() + " needs to be *after* a LIST or OBJECT tag");
            }
        } else if (expression.equals("nextCount")) {
            checkNumberOfArguments(0);
        } else if (expression.equals("lastCountById")) {
            checkNumberOfArguments(1);
            String id = StringUtils.removeSingleQuote(elData.getArguments().get(0));
            AnalysableElement.checkTagFound(pageCache, "id", id, QueryTag.class);
        }
    }

    @Override
    public void doEndAnalyze(PageCache pageCache) {
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public Object resolve(PageContext pc, PageCache pageCache) throws LogicException {
        return null;
    }

    @Override
    public void setKey(PageCache pageCache) {
    }

    @Override
    public String treatExpressionAtAnalysis(String expression) {
        return expression;
    }
}
