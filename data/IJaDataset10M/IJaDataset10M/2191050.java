package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

import java.util.EventObject;

/**
 * Used to notify components that the cca server
 * broke a connection between a Provides Port and a Uses Port.
 * A view entity might
 * respond by erasing the line that was drawn
 * between the two ports.
 * <p>
 * Possible Scenario <br>
 * The screen shows a line connecting a Provides Port and a Uses Port <br>
 * The end-user clicks on a connected Provides Port <br>
 * The cca server removes the connection <br>
 * The cca server sends a disconnect message to this client <br>
 * The client responds by removing the line <br>
 */
public class DisconnectEvent extends EventObject {

    static final long serialVersionUID = 1;

    /**
   * The name of the component that houses the source port.
   * The source port is no longer connected to the target port.
   * The name is usually the java class name of the component
   * (without the package name) concatenated with an index number.
   * Example:  "StartComponent0"
   */
    protected String sourceComponentName = null;

    /**
     * Get the name of the component that houses the source port.
     * The source port is no longer connected to the target port.
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     * @return The name of the component that houses the source port.
     */
    public String getSourceComponentName() {
        return (this.sourceComponentName);
    }

    /**
   * The name of the source port.  The source port is no
   * longer connected to the target port.
   * Example:  "out0"
   */
    protected String sourcePortName = null;

    /**
     * Get the name of the source port.
     * The source port is no longer connected
     * to the target port.
     * Example:  "out0"
     * @return the name of the source port
     */
    public String getSourcePortName() {
        return (this.sourcePortName);
    }

    /**
    * The name of the component that houses the target port.
    * The target port is no longer connected to the source port.
    * The name is usually the java class name of the component
    * (without the package name) concatenated with an index number.
    * Example:  "PrinterComponent0"
    */
    protected String targetComponentName = null;

    /**
     * Get the name of the component that houses the target port.
     * The target port is no longer connected to the source port.
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "Printer0"
     * @return The name of the component that houses the target port.
     */
    public String getTargetComponentName() {
        return (this.targetComponentName);
    }

    /**
   * This is the name of the target port.
   * The target port is one of the two ports
   * that are no longer connected.
   * Example:  "printer_port"
   */
    protected String targetPortName = null;

    /**
     * Get the name of the target port.
     * The target port is one of the two ports
     * that are no longer connected.
     * Example:  "out0"
     * @return the name of the source port
     */
    public String getTargetPortName() {
        return (this.targetPortName);
    }

    /**
     * Create a DisconnectEvent.
     * This event can be used to notify components that
     * the cca server
     * broke a connection between a Provides Port
     * and a Uses Port.
     * A view entity might
     * respond by erasing the line that was drawn
     * between the two ports.
     * @param source The entity that created this event.
     * @param sourceComponentName The name of the component
     * that houses the source port.
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "StartComponent0"
     * @param sourcePortName The name of the source port.
     * Example:  "out0"
     * @param targetComponentName The name of the component
     * that houses the target port.
     * The name is usually the java class name of the component
     * (without the package name) concatenated with an index number.
     * Example:  "PrinterComponent0"
     * @param targetPortName the name of the target port.
     * Example:  "out0"
     */
    public DisconnectEvent(Object source, String sourceComponentName, String sourcePortName, String targetComponentName, String targetPortName) {
        super(source);
        this.sourceComponentName = sourceComponentName;
        this.sourcePortName = sourcePortName;
        this.targetComponentName = targetComponentName;
        this.targetPortName = targetPortName;
    }
}
