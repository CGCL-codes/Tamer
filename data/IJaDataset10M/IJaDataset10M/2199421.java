package org.progeeks.extract.filter;

import java.util.*;
import java.util.regex.*;
import org.progeeks.extract.*;
import org.progeeks.util.TemplateExpressionProcessor.TemplateExpression;
import org.progeeks.util.TemplateExpressionProcessor.ReferenceElement;
import org.progeeks.util.TemplateExpressionProcessor.NestedExpressionElement;
import org.progeeks.util.log.Log;

/**
 *  Selects one or more records from a source list
 *  given a list of relative field regexes.
 *
 *  @version   $Revision: 4303 $
 *  @author    Paul Speed
 */
public class SelectFilter extends AbstractFilter {

    static Log log = Log.getLog();

    private List<FieldRegex> expressions;

    private boolean matchAnyExpression;

    public SelectFilter() {
    }

    public SelectFilter(List<FieldRegex> fieldExpressions) {
        setFieldExpressions(fieldExpressions);
    }

    public void setMatchAnyExpression(boolean flag) {
        this.matchAnyExpression = flag;
    }

    public boolean getMatchAnyExpression() {
        return matchAnyExpression;
    }

    public void setFieldExpressions(List<FieldRegex> fieldExpressions) {
        if (this.expressions == fieldExpressions) return;
        this.expressions = new ArrayList<FieldRegex>(fieldExpressions);
    }

    public List<FieldRegex> getFieldExpressions() {
        return expressions;
    }

    public Object filter(ExecutionContext context, DataElement container, Object o) {
        if (expressions == null || expressions.size() == 0) return null;
        if (log.isTraceEnabled()) log.trace("filter(" + o + ")");
        for (FieldRegex exp : getFieldExpressions()) {
            Object source = context.evaluate(o, exp.fieldExpression());
            if (log.isTraceEnabled()) log.trace("   checking:" + source);
            Object result = FilterList.filterObject(context, container, source, exp.filter());
            boolean empty = result == null || (result instanceof Collection && ((Collection) result).isEmpty());
            if (log.isTraceEnabled()) log.trace("   result:" + result);
            if (empty && !matchAnyExpression) return null;
            if (!empty && matchAnyExpression) return o;
        }
        if (!matchAnyExpression) {
            return o;
        } else {
            return null;
        }
    }

    public void applySources(Rule container, Dependencies deps) {
        for (FieldRegex exp : getFieldExpressions()) {
            applyElements(exp.fieldExpression().getElements(), container, deps);
            exp.filter().applySources(container, deps);
        }
    }

    protected void applyElements(List elements, Rule container, Dependencies deps) {
        for (Iterator i = elements.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof ReferenceElement) {
                ReferenceElement re = (ReferenceElement) o;
                String exp = re.getExpression();
                if (exp.charAt(0) != ExecutionContext.ROOT_PREFIX) continue;
                deps.setSourceLink(container, exp);
            } else if (o instanceof NestedExpressionElement) {
                applyElements(((NestedExpressionElement) elements).getElements(), container, deps);
            }
        }
    }

    public static class FieldRegex {

        private String field;

        private String expression;

        private TemplateExpression fieldExp;

        private RegexFilter filter;

        public FieldRegex() {
        }

        public FieldRegex(String field, String exp) {
            setField(field);
            setExpression(exp);
        }

        public void setField(String field) {
            this.field = field;
            fieldExp = null;
        }

        public String getField() {
            return field;
        }

        public void setExpression(String exp) {
            this.expression = exp;
            filter = null;
        }

        public String getExpression() {
            return expression;
        }

        public TemplateExpression fieldExpression() {
            if (fieldExp == null) {
                if (field.indexOf('$') < 0) {
                    fieldExp = new TemplateExpression("${" + field + "}");
                } else {
                    fieldExp = new TemplateExpression(field);
                }
            }
            return fieldExp;
        }

        public RegexFilter filter() {
            if (filter == null) {
                filter = new RegexFilter(expression);
            }
            return filter;
        }

        public String toString() {
            return field + " = " + expression;
        }
    }
}
