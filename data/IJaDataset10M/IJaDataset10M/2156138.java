package org.nexopenframework.web.mvc.struts.support;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nexopenframework.web.bind.ServletRequestDataBinderBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Extension of the Spring {@link org.springframework.web.struts.DelegatingRequestProcessor}
 * in order to process {@link SpringBindingActionForm} for binding from the form
 * to the corresponding model</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class DelegatingRequestProcessor extends org.springframework.web.struts.DelegatingRequestProcessor {

    /**Spring bean name of the {@link ServletRequestDataBinderBuilder}*/
    public static final String SERVLET_REQUEST_DATA_BINDER_BUILDER = "openfrwk.web.servletRequestDataBinderBuilder";

    /**
	 * <p>Executes a given {@link Action} before to check if the the
	 * given {@link ActionForm} it is an instance of {@link SpringBindingActionForm}
	 * for binding the form to the corresponding model</p>
	 * 
	 * @see #processActionPerformInternal(HttpServletRequest, HttpServletResponse, Action, SpringBindingActionForm, ActionMapping)
	 * @see org.apache.struts.action.RequestProcessor#processActionPerform(HttpServletRequest, HttpServletResponse, Action, ActionForm, ActionMapping)
	 */
    @Override
    protected final ActionForward processActionPerform(HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping) throws IOException, ServletException {
        if (form instanceof SpringBindingActionForm) {
            processActionPerformInternal(request, response, action, (SpringBindingActionForm) form, mapping);
        }
        return super.processActionPerform(request, response, action, form, mapping);
    }

    /**
	 * @param request
	 * @param response
	 * @param action
	 * @param form
	 * @param mapping
	 */
    protected void processActionPerformInternal(HttpServletRequest request, HttpServletResponse response, Action action, SpringBindingActionForm form, ActionMapping mapping) {
        ServletRequestDataBinder binder = null;
        try {
            Object target = form.getTarget();
            if (target == null) {
                Object obj = processActionAnnotation(action.getClass());
                target = obj;
            }
            if (target != null) {
                binder = newServletRequestDataBinder(target);
                binder.bind(request);
            }
        } finally {
            if (binder != null) {
                form.expose(binder.getBindingResult(), request);
            }
        }
    }

    /**
	 * @param action
	 * @return
	 */
    protected Object processActionAnnotation(Class<? extends Action> action) {
        if (action.isAnnotationPresent(org.nexopenframework.web.mvc.struts.annotation.Action.class)) {
            org.nexopenframework.web.mvc.struts.annotation.Action _action = action.getAnnotation(org.nexopenframework.web.mvc.struts.annotation.Action.class);
            Class modelObject = _action.form();
            Object obj = BeanUtils.instantiateClass(modelObject);
            return obj;
        }
        return null;
    }

    /**
	 * <p></p>
	 * 
	 * @see ServletRequestDataBinderBuilder#createServletRequestDataBinder(Object)
	 * @param target
	 * @return
	 */
    protected ServletRequestDataBinder newServletRequestDataBinder(Object target) {
        try {
            WebApplicationContext context = this.getWebApplicationContext();
            ServletRequestDataBinderBuilder srdbb = (ServletRequestDataBinderBuilder) context.getBean(SERVLET_REQUEST_DATA_BINDER_BUILDER);
            ServletRequestDataBinder srdb = srdbb.createServletRequestDataBinder(target);
            return srdb;
        } catch (NoSuchBeanDefinitionException e) {
            if (log.isDebugEnabled()) {
                log.debug("No ServletRequestDataBinder registered. Just returing a new instance");
            }
            return new ServletRequestDataBinder(target);
        }
    }
}
