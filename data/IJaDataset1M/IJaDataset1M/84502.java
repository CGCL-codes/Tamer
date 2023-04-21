package com.director.struts;

import com.director.core.DirectEvent;

/**
 * Author: Simone Ricciardi
 * Date: 31-mag-2010
 * Time: 21.42.37
 */
public class ValidationAwareEvent extends DirectEvent {

    private String name;

    private Object data;

    public ValidationAwareEvent(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }
}
