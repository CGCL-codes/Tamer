package org.jcompany.jdoc.struts;

import java.io.Serializable;

public class PlcStrutsActionForward implements Serializable {

    private String className;

    private String contextRelative;

    private String name;

    private String path;

    private String redirect;

    private String description;

    public PlcStrutsActionForward() {
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String newClassName) {
        className = newClassName;
    }

    public String getContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(String newContextRelative) {
        contextRelative = newContextRelative;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String newPath) {
        path = newPath;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String newRedirect) {
        redirect = newRedirect;
    }
}
