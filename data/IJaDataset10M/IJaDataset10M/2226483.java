package org.dueam.ui.web.spring.common;

/**
 * @author Anemone lgh@onhonest.cn
 */
public class SimpleExtModel implements ExtModel {

    private Object value;

    public Object getObject(Object command, Object um) {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
