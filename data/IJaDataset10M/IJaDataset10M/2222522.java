package de.mindmatters.faces.spring.context.actionlistener;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author andreas.kuhrwahl
 */
public class ActionListener1 implements ActionListener, BeanFactoryAware {

    private BeanFactory beanFactory;

    private final ActionListener delegate;

    private String actionProperty;

    public ActionListener1(final ActionListener delegate) {
        super();
        this.delegate = delegate;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ActionListener getDelegate() {
        return delegate;
    }

    public void processAction(ActionEvent event) throws AbortProcessingException {
        delegate.processAction(event);
    }

    public String getActionProperty() {
        return actionProperty;
    }

    public void setActionProperty(String actionProperty) {
        this.actionProperty = actionProperty;
    }
}
