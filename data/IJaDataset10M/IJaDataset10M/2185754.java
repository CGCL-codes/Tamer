package net.mchaplin.ioc.xml.model;

import java.util.ArrayList;
import net.mchaplin.ioc.ContainerI;
import net.mchaplin.ioc.component.ComponentI;

/**
 * @author mchaplin@users.sourceforge.net
 * 
 * $Header: 
 * $Revision: 
 * $Date: 
 */
public class Container extends Component implements ComponentI {

    private String name;

    private String classname;

    private String loadOnStartup;

    private ArrayList components;

    /**
     * @return Returns the components.
     */
    public ArrayList getComponents() {
        return components;
    }

    /**
     * @param components The components to set.
     */
    public void setComponents(ArrayList components) {
        this.components = components;
    }

    public ContainerI getContainer() {
        return null;
    }

    public void reset() {
    }

    public void setContainer(ContainerI container) {
    }

    /**
     * @return Returns the classname.
     */
    public String getClassname() {
        return classname;
    }

    /**
     * @param classname The classname to set.
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * @return Returns the loadOnStartup.
     */
    public String getLoadOnStartup() {
        return loadOnStartup;
    }

    /**
     * @param loadOnStartup The loadOnStartup to set.
     */
    public void setLoadOnStartup(String loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
