package org.t2framework.confeito.action.impl;

import org.t2framework.confeito.action.ActionContext;
import org.t2framework.confeito.action.ActionFilter;
import org.t2framework.confeito.action.ActionFilterChain;
import org.t2framework.confeito.action.ActionInvokingContext;
import org.t2framework.confeito.action.ActionStatus;
import org.t2framework.confeito.action.ErrorInfo;
import org.t2framework.confeito.adapter.ContainerAdapter;
import org.t2framework.confeito.contexts.WebContext;
import org.t2framework.confeito.exception.ActionMethodInvocationException;
import org.t2framework.confeito.exception.ExceptionHandlerProcessingRuntimeException;
import org.t2framework.confeito.handler.ExceptionHandler;
import org.t2framework.confeito.handler.ExceptionHandlerChain;
import org.t2framework.confeito.model.PageComponent;
import org.t2framework.confeito.spi.Navigation;

/**
 * 
 * <#if locale="en">
 * <p>
 * An implementation of {@link ActionFilter} which invokes for error capturing
 * and invoking {@link ExceptionHandler} so that user is easy to handle
 * application exception.
 * </p>
 * <#else>
 * <p>
 * 例外処理用の{@link ActionFilter}実装です.例外をキャッチすると、 {@link ExceptionHandler}を呼び出します.
 * {@link ExceptionHandler}を使用すると、 アプリケーションで発生した例外処理を容易に行えます.
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class ExceptionHandlerActionFilterImpl implements ActionFilter {

    /**
	 * <#if locale="en">
	 * <p>
	 * Invoke this filter.{@link ErrorInfo} is created at this time to pass
	 * through by {@link ActionContext}.
	 * </p>
	 * <#else>
	 * <p>
	 * このフィルターを呼び出します.呼び出し時に{@link ErrorInfo}を生成し、{@link ActionContext}に渡します.
	 * </p>
	 * </#if>
	 * 
	 * @param invokingContext
	 * @param pageDesc
	 * @param actionChain
	 * @throws ExceptionHandlerProcessingRuntimeException
	 *             if {@link ExceptionHandler} or {@link ExceptionHandlerChain}
	 *             throws checked exception
	 */
    @Override
    public void invoke(ActionInvokingContext invokingContext, PageComponent pageDesc, ActionFilterChain actionChain) throws ExceptionHandlerProcessingRuntimeException, RuntimeException {
        invokingContext.getActionContext().setErrorInfo(createErrorInfo());
        try {
            actionChain.invokeChain(invokingContext, pageDesc);
        } catch (Throwable t) {
            invokingContext.setActionStatus(ActionStatus.ERROR);
            final ExceptionHandlerChain chain = createExceptionHandlerChain(invokingContext);
            final Throwable throwable = unwrap(t);
            final Navigation result = executeChain(invokingContext, chain, throwable);
            overrideResultNavigation(result, invokingContext, throwable);
        } finally {
            invokingContext.getActionContext().clearErrorInfo();
        }
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Override result {@link Navigation} if needed.
	 * </p>
	 * <#else>
	 * <p>
	 * </p>
	 * </#if>
	 * 
	 * @param result
	 * @param invokingContext
	 * @param throwable
	 * @throws RuntimeException
	 * @throws ExceptionHandlerProcessingRuntimeException
	 */
    protected void overrideResultNavigation(final Navigation result, final ActionInvokingContext invokingContext, final Throwable throwable) {
        if (result != null) {
            invokingContext.setResultNavigation(result);
        } else {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new ExceptionHandlerProcessingRuntimeException(throwable);
            }
        }
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * </p>
	 * <#else>
	 * <p>
	 * </p>
	 * </#if>
	 * 
	 * @param invokingContext
	 * @param chain
	 * @param throwable
	 * @return
	 * @throws ExceptionHandlerProcessingRuntimeException
	 */
    protected Navigation executeChain(final ActionInvokingContext invokingContext, final ExceptionHandlerChain chain, final Throwable throwable) {
        Navigation ret = null;
        try {
            final WebContext webContext = invokingContext.getWebContext();
            final Navigation result = invokingContext.getResultNavigation();
            ret = chain.doChain(throwable, webContext, result);
        } catch (ExceptionHandlerProcessingRuntimeException e) {
            throw e;
        }
        return ret;
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Unwrap {@link ActionMethodInvocationException}.
	 * </p>
	 * <#else>
	 * <p>
	 * </p>
	 * </#if>
	 * 
	 * @param t
	 * @return
	 */
    protected Throwable unwrap(Throwable t) {
        Throwable throwable = null;
        if (t instanceof ActionMethodInvocationException) {
            throwable = t.getCause();
        } else {
            throwable = t;
        }
        return throwable;
    }

    /**
	 * 
	 * <#if locale="en">
	 * <p>
	 * Create {@link ErrorInfo} instance.
	 * </p>
	 * <#else>
	 * <p>
	 * </p>
	 * </#if>
	 * 
	 * @return
	 */
    protected ErrorInfo createErrorInfo() {
        return new ErrorInfoImpl();
    }

    protected ExceptionHandlerChain createExceptionHandlerChain(ActionInvokingContext context) {
        final ContainerAdapter<?> containerAdapter = context.getContainerAdapter();
        return new ExceptionHandlerChain(containerAdapter.createExceptionHandlers());
    }
}
