package org.t2framework.lucy.lucyTestMocks;

import org.t2framework.commons.annotation.composite.SingletonScope;
import org.t2framework.lucy.annotation.core.Inject;

@SingletonScope
public abstract class InheritTargetParent {

    protected Greeting greeting;

    protected Greeting greeting2;

    @Inject
    public void setGreeting(Greeting greeting) {
        this.greeting = greeting;
    }

    @Inject
    public void hoge(Greeting greeting2) {
        this.greeting2 = greeting2;
    }

    public String hello2() {
        return greeting2.greet();
    }

    public abstract String hello();
}
