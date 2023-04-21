package org.plazmaforge.framework.config.configurer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.plazmaforge.framework.config.object.IObjectConfig;

public class ConfigurerInfo implements Serializable {

    private String name;

    private String type;

    private String className;

    private String fileName;

    private List objects;

    public ConfigurerInfo() {
        super();
    }

    public ConfigurerInfo(String name, String type, String className, String fileName) {
        super();
        this.name = name;
        this.type = type;
        this.className = className;
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }

    public String getFileName() {
        return fileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List getObjects() {
        return objects;
    }

    public void setObjects(List objects) {
        this.objects = objects;
    }

    public void addObject(IObjectConfig c) {
        if (objects == null) {
            objects = new ArrayList();
        }
        objects.add(c);
    }

    public String toString() {
        return "" + name + " " + className;
    }
}
