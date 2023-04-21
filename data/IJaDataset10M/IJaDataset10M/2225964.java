package org.nakedobjects;

import org.nakedobjects.object.ApplicationContext;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedCollection;

public class DefaultApplicationContext extends ApplicationContext {

    private String name;

    public void setUpUsers(NakedCollection users) {
    }

    public String name() {
        return name;
    }

    public NakedClass addClass(String className) {
        return super.addClass(className);
    }
}
