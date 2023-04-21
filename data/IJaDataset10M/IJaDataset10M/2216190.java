package org.ikasan.exceptionResolver;

import java.util.List;
import java.util.Map;
import org.ikasan.exceptionResolver.action.ExceptionAction;
import org.ikasan.exceptionResolver.action.StopAction;

/**
 * Implementation of <code>IkasanExceptionHandler</code> that relies on a
 * configuration of <code>ExceptionGroup</code>s to match Throwables and apply
 * Exception Actions
 * 
 * Includes ability so set rules that are either specific to named components,
 * on non specific
 * 
 * @author Ikasan Development Team
 * 
 */
public class MatchingExceptionResolver implements ExceptionResolver {

    /**
     * Default Action if Throwable is not matched by any configured groups
     */
    private static final ExceptionAction defaultAction = StopAction.instance();

    /**
     * Non component specific exception groupings
     */
    private List<ExceptionGroup> exceptionGroupings;

    /**
     * Component specific exception groupings keyed by component name
     */
    private Map<String, List<ExceptionGroup>> componentExceptionGroupings;

    /**
     * Constructor
     * 
     * @param exceptionGroupings
     * @param componentExceptionGroupings
     */
    public MatchingExceptionResolver(List<ExceptionGroup> exceptionGroupings, Map<String, List<ExceptionGroup>> componentExceptionGroupings) {
        this.exceptionGroupings = exceptionGroupings;
        this.componentExceptionGroupings = componentExceptionGroupings;
    }

    /**
     * Constructor
     * 
     * @param exceptionGroupings
     */
    public MatchingExceptionResolver(List<ExceptionGroup> exceptionGroupings) {
        this(exceptionGroupings, null);
    }

    public ExceptionAction resolve(String componentName, Throwable throwable) {
        if (componentExceptionGroupings != null) {
            List<ExceptionGroup> thisComponentsGroupings = componentExceptionGroupings.get(componentName);
            if (thisComponentsGroupings != null) {
                for (ExceptionGroup exceptionGroup : thisComponentsGroupings) {
                    if (exceptionGroup.includes(throwable)) {
                        return exceptionGroup.getAction();
                    }
                }
            }
        }
        if (exceptionGroupings != null) {
            for (ExceptionGroup exceptionGroup : exceptionGroupings) {
                if (exceptionGroup.includes(throwable)) {
                    return exceptionGroup.getAction();
                }
            }
        }
        return defaultAction;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getName() + " [");
        sb.append("exceptionGroupings = [" + exceptionGroupings + "]");
        sb.append(", ");
        sb.append("componentExceptionGroupings = [" + componentExceptionGroupings + "]");
        sb.append(", ");
        sb.append("defaultAction = [" + defaultAction + "]");
        sb.append("]");
        return sb.toString();
    }
}
