package org.skins.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class XmlInterceptorElement implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4689011084996845960L;

    private String name = null;

    private String className = null;

    private Collection methods = new ArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Collection getMethods() {
        return methods;
    }

    public void setMethods(Collection methods) {
        this.methods = methods;
    }

    public void addMethods(XmlMethodElement method) {
        this.methods.add(method);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("methods", this.methods).append("className", this.className).append("name", this.name).toString();
    }
}
