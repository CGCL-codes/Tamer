package net.sourceforge.appgen.model;

import java.io.Serializable;

/**
 * @author Byeongkil Woo
 */
public class BaseService implements Serializable {

    private static final long serialVersionUID = 1L;

    private String packageName;

    public BaseService(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return "Service";
    }

    public String getFullPackageName() {
        return packageName + ".base";
    }
}
