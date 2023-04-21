package de.schlund.pfixcore.beans.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mleidig@schlund.de
 */
public class Beans {

    Map<String, Bean> beans;

    public Beans() {
        beans = new HashMap<String, Bean>();
    }

    public Bean getBean(String className) {
        return beans.get(className);
    }

    public void setBean(Bean bean) {
        beans.put(bean.getClassName(), bean);
    }
}
