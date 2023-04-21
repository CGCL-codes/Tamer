package br.rmpestano.finantial.util;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FacesContextBuilder {

    public FacesContext getFacesContext(final ServletRequest request, final ServletResponse response) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            return facesContext;
        }
        FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        ServletContext servletContext = ((HttpServletRequest) request).getSession().getServletContext();
        facesContext = contextFactory.getFacesContext(servletContext, request, response, lifecycle);
        InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);
        if (null == facesContext.getViewRoot()) {
            facesContext.setViewRoot(new UIViewRoot());
        }
        return facesContext;
    }

    private abstract static class InnerFacesContext extends FacesContext {

        protected static void setFacesContextAsCurrentInstance(final FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }
}
