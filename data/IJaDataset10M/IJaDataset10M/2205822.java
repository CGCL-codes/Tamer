package net.sf.jwisp.component;

import java.io.Serializable;
import java.util.Enumeration;

public interface View extends Serializable {

    public Object getPageController();

    public String getViewName();

    public Enumeration<ViewComponent> children();
}
