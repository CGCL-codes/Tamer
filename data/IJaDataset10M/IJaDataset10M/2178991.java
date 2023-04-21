package systemobject.snmp;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsystem.framework.system.SystemObjectImpl;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import org.opennms.protocols.snmp.SnmpOctetString;
import org.opennms.protocols.snmp.SnmpPduPacket;
import org.opennms.protocols.snmp.SnmpPduRequest;
import org.opennms.protocols.snmp.SnmpPduTrap;
import org.opennms.protocols.snmp.SnmpTrapHandler;
import org.opennms.protocols.snmp.SnmpTrapSession;
import org.opennms.protocols.snmp.SnmpVarBind;
import systemobject.snmp.mibSymbolInfo.MibSymbolInfo;

public class SnmpTrap extends SystemObjectImpl implements SnmpTrapHandler, TestListener {

    /**
	 * Logger
	 */
    private static Logger log = Logger.getLogger(SnmpTrap.class.getName());

    private MibReader reader;

    public static SnmpTrap defaultSnmpTrap = null;

    SnmpTrapSession trapSession = null;

    TableFormater table = null;

    /**
	 * A list of trap listeners to receive trap indications
	 */
    ArrayList<SnmpTrapListener> trapListeners;

    public SnmpTrap() throws SocketException {
        trapSession = new SnmpTrapSession(this);
        log.fine("SnmpTrapSession init");
        initListeners();
    }

    public void init() throws Exception {
        initTable();
    }

    public static SnmpTrap getInstance() throws Exception {
        if (defaultSnmpTrap == null) {
            defaultSnmpTrap = new SnmpTrap();
            defaultSnmpTrap.init();
        }
        return defaultSnmpTrap;
    }

    public synchronized void waitForTrap(long timeout) throws Exception {
        report.report("waitForTrap: " + timeout);
        wait(timeout);
        getTrapTable();
    }

    public void close() {
        if (trapSession != null) {
            log.fine("SnmpTrapSession close");
            trapSession.close();
        }
    }

    /**
	 * Receives and prints information about SNMPv2c traps.
	 * 
	 * @param session
	 *            The Trap Session that received the PDU.
	 * @param agent
	 *            The address of the remote sender.
	 * @param port
	 *            The remote port where the pdu was transmitted from.
	 * @param community
	 *            The decoded community string.
	 * @param pdu
	 *            The decoded V2 trap pdu.
	 * 
	 */
    public synchronized void snmpReceivedTrap(SnmpTrapSession session, java.net.InetAddress agent, int port, SnmpOctetString community, SnmpPduPacket pdu) {
        String[] line = new String[11];
        line[0] = agent.toString();
        line[1] = "V2";
        line[2] = "" + pdu.getCommand();
        line[3] = "" + pdu.getRequestId();
        line[4] = "" + pdu.getLength();
        line[5] = "" + ((SnmpPduRequest) pdu).getErrorStatus();
        line[6] = "" + ((SnmpPduRequest) pdu).getErrorIndex();
        line[7] = "";
        line[8] = "";
        line[9] = "";
        StringBuffer buf = new StringBuffer();
        int k = pdu.getLength();
        for (int i = 0; i < k; i++) {
            SnmpVarBind vb = pdu.getVarBindAt(i);
            if (i != 0) {
                buf.append(", ");
            }
            buf.append("[" + i + "] := " + convertOID(vb.getName().toString()));
            buf.append(" --> " + convertOID(vb.getValue().toString()));
        }
        line[10] = buf.toString();
        table.log(line);
        notifyAll();
        for (SnmpTrapListener listener : trapListeners) {
            listener.v2PacketRecieved(session, agent, port, community, pdu);
        }
    }

    private String convertOID(String oid) {
        if (reader != null) {
            MibSymbolInfo info = reader.getMibByFullOid(oid);
            if (info != null) {
                return info.getMibName();
            }
        }
        return oid;
    }

    public void getTrapTable() {
        String trapTable = table.toString();
        setTestAgainstObject(trapTable);
        report.report("getTrapTable", trapTable, true);
    }

    public void resetTrapTable() {
        initTable();
    }

    /**
	 * Receives and prints information about SNMPv1 traps.
	 * 
	 * @param session
	 *            The Trap Session that received the PDU.
	 * @param agent
	 *            The address of the remote sender.
	 * @param port
	 *            The remote port where the pdu was transmitted from.
	 * @param community
	 *            The decoded community string.
	 * @param pdu
	 *            The decoded V1 trap pdu.
	 * 
	 */
    public synchronized void snmpReceivedTrap(SnmpTrapSession session, java.net.InetAddress agent, int port, SnmpOctetString community, SnmpPduTrap pdu) {
        String[] line = new String[11];
        line[0] = "" + pdu.getAgentAddress();
        line[1] = "V1";
        line[2] = "";
        line[3] = "" + pdu.getEnterprise();
        line[4] = "" + pdu.getLength();
        line[5] = "";
        line[6] = "";
        line[7] = "" + pdu.getGeneric();
        line[8] = "" + pdu.getSpecific();
        line[9] = "" + pdu.getTimeStamp();
        log.fine("V1 Trap from agent " + agent.toString() + " on port " + port);
        log.fine("Ip Address................. " + pdu.getAgentAddress());
        log.fine("Enterprise Id.............. " + pdu.getEnterprise());
        log.fine("Generic ................... " + pdu.getGeneric());
        log.fine("Specific .................. " + pdu.getSpecific());
        log.fine("TimeStamp ................. " + pdu.getTimeStamp());
        log.fine("Length..................... " + pdu.getLength());
        StringBuffer buf = new StringBuffer();
        int k = pdu.getLength();
        for (int i = 0; i < k; i++) {
            SnmpVarBind vb = pdu.getVarBindAt(i);
            if (i != 0) {
                buf.append(", ");
            }
            buf.append("[" + i + "] := " + convertOID(vb.getName().toString()));
            buf.append(" --> " + convertOID(vb.getValue().toString()));
        }
        line[10] = buf.toString();
        table.log(line);
        notifyAll();
        for (SnmpTrapListener listener : trapListeners) {
            listener.v1trapRecieved(session, agent, port, community, pdu);
        }
    }

    /**
	 * Process session errors.
	 * 
	 * @param session
	 *            The trap session in error.
	 * @param error
	 *            The error condition.
	 * @param ref
	 *            The reference object, if any.
	 * 
	 */
    public synchronized void snmpTrapSessionError(SnmpTrapSession session, int error, java.lang.Object ref) {
        log.fine("An error occured in the trap session");
        log.fine("Session error code = " + error);
        if (ref != null) {
            log.fine("Session error reference: " + ref.toString());
        }
        if (error == SnmpTrapSession.ERROR_EXCEPTION) {
            close();
            try {
                trapSession = new SnmpTrapSession(this);
            } catch (SocketException ex) {
                log.log(Level.WARNING, "Fail to init trap session after error", ex);
            }
        }
        notifyAll();
    }

    private void initTable() {
        String[] header = { "Agent", "Version", "Command", "Id", "Length", "Error status", "Error index", "Generic", "Specific", "Timestamp", "Varbind" };
        table = new TableFormater(header);
    }

    public void addError(Test arg0, Throwable arg1) {
    }

    public void addFailure(Test arg0, AssertionFailedError arg1) {
    }

    public void endTest(Test arg0) {
    }

    public void startTest(Test arg0) {
        initTable();
    }

    public MibReader getReader() {
        return reader;
    }

    public void setReader(MibReader reader) {
        this.reader = reader;
    }

    /**
	 * Add to listeners list
	 * 
	 * @param listener
	 */
    public void addListener(SnmpTrapListener listener) {
        trapListeners.add(listener);
    }

    /**
	 * Remove all listeners
	 */
    public void initListeners() {
        trapListeners = new ArrayList<SnmpTrapListener>();
    }
}
