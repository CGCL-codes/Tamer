package br.gov.frameworkdemoiselle.template;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;

public abstract class AbstractPageBean implements PageBean {

    private static final long serialVersionUID = 1L;

    @Inject
    private FacesContext facesContext;

    private String nextView;

    private String previousView;

    @Override
    public String getCurrentView() {
        return facesContext.getViewRoot().getViewId();
    }

    @Override
    public String getNextView() {
        if (nextView == null) {
            NextView annotation = this.getClass().getAnnotation(NextView.class);
            if (annotation != null) {
                nextView = annotation.value();
            } else {
            }
        }
        return nextView;
    }

    @Override
    public String getPreviousView() {
        if (previousView == null) {
            PreviousView annotation = this.getClass().getAnnotation(PreviousView.class);
            if (annotation != null) {
                previousView = annotation.value();
            } else {
            }
        }
        return previousView;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
