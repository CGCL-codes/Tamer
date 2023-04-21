package org.universa.tcc.gemda.web.jquery.event;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.universa.tcc.gemda.web.jquery.JQueryComponent;

public final class Callback {

    private JQueryComponent rootComponent;

    private AjaxRequestTarget target;

    public Callback(JQueryComponent rootComponent, AjaxRequestTarget target) {
        super();
        this.target = target;
        this.rootComponent = rootComponent;
        this.rootComponent.setOutputMarkupId(true);
    }

    public JQueryComponent getRootComponent() {
        return rootComponent;
    }

    public final Callback appendJavascript(String javascript) {
        target.appendJavascript(javascript);
        return this;
    }

    public final Callback updateComponent(Component... components) {
        for (Component component : components) {
            target.addComponent(component);
        }
        return this;
    }

    public void execute() {
    }
}
