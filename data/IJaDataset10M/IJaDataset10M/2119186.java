package de.mindmatters.faces.spring.context.servlet.controller;

import javax.el.MethodExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.ActionSource2;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import de.mindmatters.faces.application.ActionEventContextHolder;

/**
 * Simple implementation of MethodNameResolver that maps JSF Action outcomes to
 * method names. This class is the default implementation used by the
 * {@link MultiActionEventController} class.
 * 
 * <p>
 * This class invokes the {@link javax.faces.el.MethodBinding} from the
 * {@link ActionSource} ({@link MethodExpression} from the
 * {@link ActionSource2} respectively) exposed from the current
 * {@link ActionEvent} and maps the evaluated outcome to a method name.
 * </p>
 * 
 * <p>
 * Examples:
 * <ul>
 * <li><code>&lt;h:commandButton action="foo".../&gt;</code> evaluates to
 * method name 'foo'</li>
 * <li><code>&lt;h:commandButton action="#{foo.bar}".../&gt;</code> invokes
 * method bar on object foo and returns an outcome (a String) which will be used
 * as method name</li>
 * </ul>
 * </p>
 * 
 * @author Andreas Kuhrwahl
 * 
 */
public final class ActionMethodNameResolver implements MethodNameResolver {

    /**
     * {@inheritDoc}
     */
    public String getHandlerMethodName(final HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
        ActionEvent event = ActionEventContextHolder.getActionEvent();
        if (event == null) {
            throw new NoSuchRequestHandlingMethodException(request);
        }
        String methodName = null;
        if (event.getComponent() instanceof ActionSource) {
            methodName = getHandlerMethodName((ActionSource) event.getComponent());
        } else if (event.getComponent() instanceof ActionSource2) {
            methodName = getHandlerMethodName((ActionSource2) event.getComponent());
        }
        if (methodName == null) {
            throw new NoSuchRequestHandlingMethodException(request);
        }
        return methodName;
    }

    /**
     * Evaluates the method name from an {@link ActionSource}.
     * 
     * @param actionSource
     *            the {@link ActionSource}
     * @return the evaluated method name
     * @deprecated
     */
    private String getHandlerMethodName(final ActionSource actionSource) {
        String methodName = null;
        javax.faces.el.MethodBinding mb = actionSource.getAction();
        if (mb != null) {
            try {
                methodName = (String) mb.invoke(FacesContext.getCurrentInstance(), null);
            } catch (Exception ex) {
                methodName = null;
            }
        }
        return methodName;
    }

    /**
     * Evaluates the method name from an {@link ActionSource2}.
     * 
     * @param actionSource
     *            the {@link ActionSource2}
     * @return the evaluated method name
     */
    private String getHandlerMethodName(final ActionSource2 actionSource) {
        String methodName = null;
        MethodExpression expression = actionSource.getActionExpression();
        if (expression != null) {
            try {
                methodName = (String) expression.invoke(FacesContext.getCurrentInstance().getELContext(), null);
            } catch (Exception ex) {
                methodName = null;
            }
        }
        return methodName;
    }
}
