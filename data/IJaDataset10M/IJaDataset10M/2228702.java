package org.objectstyle.cayenne.remote;

/**
 * A message sent to a remote service to request Cayenne mapping info.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class BootstrapMessage implements ClientMessage {

    public String toString() {
        return "Bootstrap";
    }
}
