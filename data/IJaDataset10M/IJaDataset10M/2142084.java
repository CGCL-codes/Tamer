package com.jcorporate.expresso.kernel;

import java.util.Map;

/**
 * ContainerImpl is the equivelant of a Service Provider Interface (SPI) for
 * the Expresso component containers.  By implementing your own ContinainerImpl
 * inteface, you can have its behavior 'duplicated' throughout all the other
 * containers.  This particular behavior is specified by the ContainerFactory
 * class.
 * <p>There is a one to one relationship between a <code>ComponentContainer</code>
 * and a Containable object.  The ComponentContainer always wraps the Containable
 * object and the Containable object can be retrieved via the getContainercomponent()
 * method.</p>
 * <p>The particular ComponentContainer implementation set for each Containable
 * object is determined by the SystemFactory.  Although it does not currently
 * have code to dynamically load other ComponentContainer implementations, it
 * would be rather simple to do so.</p>
 *
 * @author Michael Rimov
 * @version $Revision: 3 $ on  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @since Expresso 5.1
 */
public interface ComponentContainer {

    /**
     * Locates an Expresso Service for use by a client.
     * @param componentName the name of the service to locate.
     * @return ExpressoService.
     * @throws IllegalArgumentException if the service cannot be found.
     * @throws IllegalStateException if the service exists, but is not in a
     * 'runnable' state due to some configuration error or other unforeseen
     * issue.
     */
    public ExpressoComponent locateComponent(String componentName);

    /**
     * Query the container to see if a particular service name is installed
     * in the system
     * @param componentName the name of the component to query for.
     * @return true if the service is installed and running.
     */
    public boolean isComponentExists(String componentName);

    /**
     * To register the component for control by the Component Manager.  This will
     * in essense transfer the control of ther service to the Component Manager.
     * This will often be called by the Configuration Bootstrap system.
     * @param newComponent the component to install
     */
    public void addComponent(ExpressoComponent newComponent);

    /**
     * Removes a component from this container.
     * @param componentName The name of the component to remove.
     */
    public void removeComponent(String componentName);

    /**
     * Install a component into the system.  If newComponent implements <code>
     * installable</code> then it shall be installed.  After that, the component
     * is added.
     * @param newComponent An instance of the component to install.
     * @param log a Logger-like interface to a component tha records the process
     * of the installation including any errors, etc.
     * @param installOptions The Installation Options for the Component
     */
    public void installComponent(ExpressoComponent newComponent, InstallationOptions installOptions, InstallLog log);

    /**
     * Uninstalls the component.  If the component implements <code>
     * installable</code> then it shall be uninstalled.  After that, it shall
     * be removed.
     * @param componentName the name of the component to uninstall
     * @param log a Logger-like interface to a component tha records the process
     * of the installation including any errors, etc.
     * @param installOptions The 'Uninstallation' options for the component
     */
    public void uninstallComponent(String componentName, InstallationOptions installOptions, InstallLog log);

    /**
     * Retrieves a list of instances of all contained ExpressoComponents.  Use
     * this for iterating through the components of a current 'context'.  Do not
     * attempt to modify the map given.  Either add or remove a component through
     * the addComponent or removeComponent methods.
     * @return Read only map of the components.
     */
    public Map getChildComponents();

    /**
     * Return the parent container
     * @return ContainerImpl interface
     */
    public ComponentContainer getParentContainer();

    /**
     * Set the parent container of this container
     * @param newParent the new Parent Container
     */
    public void setParentContainer(ComponentContainer newParent);

    /**
     * Return the 'wrapped' container ExpressoComponent.
     * @return <code>Containable</code>
     */
    public Containable getContainerComponent();

    /**
     * Sets the nested component. This is usually called by the system
     * factory.
     * @param newComponent the component links to this component container
     * object.
     */
    public void setContainerComponent(Containable newComponent);

    /**
     * Called when the container is to be destroyed
     */
    public void destroyContainer();
}
