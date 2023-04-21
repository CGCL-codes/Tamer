package net.sf.jncu.cdil.mnp;

import java.util.ArrayList;
import java.util.List;
import jssc.SerialPort;
import jssc.SerialPortList;

/**
 * NCU communication ports.
 * 
 * @author moshew
 * @see CommTrace CommTrace for setting library paths.
 */
public class CommPorts {

    /**
	 * Creates a new port helper.
	 */
    public CommPorts() {
        super();
    }

    /**
	 * Get the list of communication port identifiers.
	 * 
	 * @param portType
	 *            the port type.
	 * @return the list of ports.
	 */
    public List<String> getPortNames() {
        List<String> portIdentifiers = new ArrayList<String>();
        String[] names = SerialPortList.getPortNames();
        for (String name : names) portIdentifiers.add(name);
        return portIdentifiers;
    }

    /**
	 * Get the list of communication ports.
	 * 
	 * @param portType
	 *            the port type.
	 * @return the list of ports.
	 */
    public List<SerialPort> getPorts() {
        List<SerialPort> ports = new ArrayList<SerialPort>();
        String[] names = SerialPortList.getPortNames();
        SerialPort port;
        for (String name : names) {
            port = new SerialPort(name);
            ports.add(port);
        }
        return ports;
    }

    public static void main(String[] args) {
        CommPorts ports = new CommPorts();
        try {
            for (String name : ports.getPortNames()) {
                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
