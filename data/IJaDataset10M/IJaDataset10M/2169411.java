package org.jsens.editors.internal.variable;

import org.jsens.businesslogic.Variable;

public class BindingDelegateVariable {

    private Variable delegate = new Variable();

    public void setDelegate(Variable delegate) {
        if (delegate == null) {
            this.delegate = new Variable();
        } else {
            this.delegate = delegate;
        }
    }

    public void setName(String name) {
        delegate.setName(name);
    }

    public String getName() {
        return delegate.getName();
    }

    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    public String getDescription() {
        return delegate.getDescription();
    }
}
