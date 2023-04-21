package com.narirelays.ems.model;

import org.apache.commons.beanutils.LazyDynaBean;

public class MyJavaBean {

    public MyJavaBean() {
        bean = new LazyDynaBean();
    }

    private LazyDynaBean bean;

    public LazyDynaBean getBean() {
        return bean;
    }

    public void setBean(LazyDynaBean bean) {
        this.bean = bean;
    }

    public Object get(String name) {
        return bean.get(name);
    }

    public void set(String name, Object value) {
        bean.set(name, value);
    }
}
