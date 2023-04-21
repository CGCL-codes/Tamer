package org.spockframework.runtime.condition;

import org.spockframework.util.GroovyRuntimeUtil;

public class DiffedObjectAsStringRenderer implements IObjectRenderer<Object> {

    public String render(Object object) {
        return GroovyRuntimeUtil.toString(object) + "\n";
    }
}
