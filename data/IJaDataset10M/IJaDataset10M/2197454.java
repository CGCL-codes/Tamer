package net.sf.oxygen.client.helper;

import net.sf.oxygen.core.component.ComponentIdentifier;
import net.sf.oxygen.core.component.ComponentState;
import net.sf.oxygen.core.component.IllegalComponentStateException;

/**
 * A class that records data about a component such as the ID, JAR file, install
 * location and current state.
 * @author <A HREF="mailto:seniorc@users.sourceforge.net?subject=net.sf.oxygen.client.ComponentRecord">Chris Senior</A>
 */
public class ComponentRecord {

    /**
   * The components identifier
   */
    private ComponentIdentifier id;

    /**
   * The name of the LOCAL JAR file that was loaded to load the component 
   */
    private String jarFileName;

    /**
   * An install location (which may be remote) wherer the component JAR was 
   * downloaded from (if it wasn't local to start with).
   */
    private String installLocation;

    /**
   * The current state of the component
   */
    private ComponentState state;

    /**
   * Create a component record
   * @param id The component ID
   * @param jar The LOCAL JAR name 
   * @param install The install location (maybe remote)
   * @param state The initial state
   */
    public ComponentRecord(ComponentIdentifier id, String jar, String install, ComponentState state) {
        this.id = id;
        this.jarFileName = jar;
        this.installLocation = install;
        this.state = state;
    }

    /**
   * Get the component identifier for this record
   * @return The component identifier
   */
    public ComponentIdentifier getId() {
        return id;
    }

    /**
   * The install location of the component. If this component was downloaded from
   * some remote site - this will point to that site. If the component was 
   * installed from a local file system - this points there. This location is
   * used when Updating the component
   * @return The String location of the original install location
   */
    public String getInstallLocation() {
        return installLocation;
    }

    /**
   * The name of the LOCAL JAR file where this component is loaded from
   * @return The local JAR file name
   */
    public String getJarFileName() {
        return jarFileName;
    }

    /**
   * The current state of the component with the framework
   * @return The components current state
   */
    public ComponentState getState() {
        return state;
    }

    /**
   * Change this components state - if possible.
   * @param state A new state for the component
   * @throws IllegalComponentStateException If the state transition requested is not legal
   */
    public void setState(ComponentState newState) throws IllegalComponentStateException {
        if (state.equals(ComponentState.UNINSTALLED) && newState.equals(ComponentState.INSTALLED)) {
            state = newState;
        } else if (state.equals(ComponentState.RUNNING) && newState.equals(ComponentState.INSTALLED)) {
            state = newState;
        } else if (state.equals(ComponentState.INSTALLED) && (newState.equals(ComponentState.RUNNING) || newState.equals(ComponentState.UNINSTALLED))) {
            state = newState;
        } else throw new IllegalStateException("Cannot change to state " + newState + " from state " + state);
    }

    /**
   * Records are equal if their component ID is equal
   * @see java.lang.Object#equals(Object)
   */
    public boolean equals(Object o) {
        if (o instanceof ComponentRecord) {
            ComponentRecord other = (ComponentRecord) o;
            return other.id.equals(this.id);
        }
        return false;
    }

    /**
   * Simply dump the contents of the record
   * @see java.lang.Object#toString()
   */
    public String toString() {
        return "id=" + id + "; installLocation=" + installLocation + "; jar=" + jarFileName + "; state=" + state;
    }
}
