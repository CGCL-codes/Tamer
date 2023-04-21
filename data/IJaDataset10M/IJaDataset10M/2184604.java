package org.impalaframework.web.spring.module;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * Sets the context of the root module to that bound to {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE}
 * @author Phil Zoio
 */
public class RootWebModuleLoader extends WebModuleLoader {

    @Override
    protected void configureBeanFactoryAndApplicationContext(ModuleDefinition moduleDefinition, DefaultListableBeanFactory beanFactory, GenericWebApplicationContext context) {
        super.configureBeanFactoryAndApplicationContext(moduleDefinition, beanFactory, context);
        if (context.getParent() == null) {
            final Object attribute = getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            final WebApplicationContext wac = ObjectUtils.cast(attribute, WebApplicationContext.class);
            if (wac != null) {
                context.setParent(wac);
            }
        }
    }
}
