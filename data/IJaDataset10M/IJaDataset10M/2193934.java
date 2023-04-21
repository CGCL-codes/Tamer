package org.digitalcure.lunarcp.io.rxtx;

import gnu.io.CommPortIdentifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Serial port utility class implementation.
 * @author Stefan Diener
 * @version 1.0
 * @since LunaRCP 0.5, 09.12.2010
 * @lastChange $Date$ by $Author$
 */
public final class SerialPortUtil {

    /** The one and only instance of this class. */
    private static final SerialPortUtil INSTANCE = new SerialPortUtil();

    /** Private constructor to avoid instantiation from outside. */
    private SerialPortUtil() {
        super();
    }

    /**
     * Returns the one and only instance of this class.
     * @return <code>SerialPortUtil</code> instance
     */
    public static SerialPortUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Determine the list of all ports of the system. The method does not filter
     * for used and unused ports.
     * @return list of all ports, maybe empty, but never <code>null</code>
     */
    public List<CommPortIdentifier> getPorts() {
        @SuppressWarnings("unchecked") final Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        final List<CommPortIdentifier> portList = new ArrayList<CommPortIdentifier>();
        while (portEnum.hasMoreElements()) {
            portList.add(portEnum.nextElement());
        }
        return portList;
    }

    /**
     * Determine the list of all ports of the given type. The method does not
     * filter for used and unused ports.
     * @param type port type to search for
     * @return list of all ports of the given type, maybe empty, but never
     *  <code>null</code>
     */
    public List<CommPortIdentifier> getPorts(final RxtxPortType type) {
        final List<CommPortIdentifier> portList = getPorts();
        if (type == null) {
            return portList;
        }
        final Iterator<CommPortIdentifier> iter = portList.iterator();
        while (iter.hasNext()) {
            final CommPortIdentifier portIdentifier = iter.next();
            final RxtxPortType portType = RxtxPortType.getPortTypeEnum(portIdentifier.getPortType());
            if (!type.equals(portType)) {
                iter.remove();
            }
        }
        return portList;
    }
}
