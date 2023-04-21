package org.t2framework.lucy.lucyTestMocks;

import org.t2framework.commons.annotation.composite.PrototypeScope;
import org.t2framework.lucy.annotation.core.Inject;

@PrototypeScope
public class Client8 {

    protected Init1 init1;

    @Inject
    public void set(Init1 init1) {
        this.init1 = init1;
    }

    public String message() {
        return init1.getMessage();
    }
}
