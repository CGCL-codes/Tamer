package de.jakop.rugby.comm;

import gnu.io.CommPortIdentifier;
import java.util.Enumeration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author jakop
 * @deprecated in Entwicklung
 */
@Deprecated
public class ParallelPortCommunicator {

    /** Field log Logger for this class */
    private final transient Log log = LogFactory.getLog(this.getClass());

    /**
	 * listet alle verf�gbaren Ports auf
	 *
	 */
    public void listPorts() {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (this.log.isDebugEnabled()) {
                this.log.debug(id.getPortType() + "," + id.getName() + "," + id.getCurrentOwner());
            }
        }
    }
}
