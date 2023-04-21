package net.wsnware.gui;

/**
 * Interface for any GUI form, used to bind the form with a component's instance.
 * 
 * @see     GuiFactory
 *
 * @author  Alessandro Polo <contact@alessandropolo.name>
 * @version 1.0.0
 * @date    2011-05-17
 */
public interface ComponentGui {

    public void setInstance(Object obj);

    public Object getInstance();

    public Class[] getSupportedComponents();

    public String getPid();

    public void setPid(String pid);
}
