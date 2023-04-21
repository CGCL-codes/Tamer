package ch.unibas.snmp.snmptrapwrapper;

import java.util.HashMap;
import ch.unibas.jmeter.snmp.helper.SnmpJmeterOids;
import ch.unibas.jmeter.snmp.helper.SnmpTrapVariables;
import ch.unibas.snmp.snmptrapwrapper.helper.SnmpTrapHelper;

public class SnmpTrapWrapper {

    private String managementServerIp = "131.152.2.236";

    private SnmpTrapWrapper() {
        super();
    }

    public SnmpTrapWrapper(String managementServerIp) {
        this();
        this.managementServerIp = managementServerIp;
    }

    protected void sendTrap(int type, String ip, String name, String url, String comment) {
        HashMap vars = new HashMap();
        vars.put(SnmpJmeterOids.snmpJmeterTrapVariable + ".1", name);
        vars.put(SnmpJmeterOids.snmpJmeterTrapVariable + ".2", url != null ? url : "none");
        vars.put(SnmpJmeterOids.snmpJmeterTrapVariable + ".3", comment != null ? comment : "none");
        SnmpTrapHelper trap = new SnmpTrapHelper(SnmpJmeterOids.snmpJmeterTrap, getManagementServerIp(), ip, "public", type, vars);
        trap.sendSnmpTrap();
    }

    public void setManagementServerIp(String nms) {
        this.managementServerIp = nms;
    }

    public String getManagementServerIp() {
        return managementServerIp;
    }

    /**
	 * Send a trap indication that this step is OK<br>
	 * A step is defined by the combination of IP and name.<br>
	 * If one of the other traps {@link sendFatalTrap}, {@link sendErrorTrap}
	 * and {@link sendWarningTrap} is potentially sent in the case of an error,
	 * this trap must be send in the case everything is OK. <br>
	 * 
	 * @param ip
	 *            A imaginary IP that stands for the application (i.e.
	 *            192.168.X.Y)
	 * @param name
	 *            The name of the step (must be identical for failure and ok
	 *            condition)
	 * @param url
	 *            The URL where the problem happened
	 * @param comment
	 *            A comment
	 */
    public void sendOKTrap(String ip, String name, String url, String comment) {
        sendTrap(SnmpTrapVariables.CLEAR_TRAP, ip, name, url, comment);
    }

    /**
	 * Send a trap indication that this step produced a fatal error. A fatal
	 * error leads potentially to a SMS been sent.<br>
	 * A step is defined by the combination of IP and name.<br>
	 * Make sure to call {@link sendOKTrap} in case of no failure.<br>
	 * 
	 * @param ip
	 *            A imaginary IP that stands for the application (i.e.
	 *            192.168.X.Y)
	 * @param name
	 *            The name of the step (must be identical for failure and ok
	 *            condition)
	 * @param url
	 *            The URL where the problem happened
	 * @param comment
	 *            A comment
	 */
    public void sendFatalTrap(String ip, String name, String url, String comment) {
        sendTrap(SnmpTrapVariables.FATAL_TRAP, ip, name, url, comment);
    }

    /**
	 * Send a trap indication that this step produced a error. A error leads
	 * potentially to a e-mail been sent.<br>
	 * A step is defined by the combination of IP and name.<br>
	 * Make sure to call {@link sendOKTrap} in case of no failure.<br>
	 * 
	 * @param ip
	 *            A imaginary IP that stands for the application (i.e.
	 *            192.168.X.Y)
	 * @param name
	 *            The name of the step (must be identical for failure and ok
	 *            condition)
	 * @param url
	 *            The URL where the problem happened
	 * @param comment
	 *            A comment
	 */
    public void sendErrorTrap(String ip, String name, String url, String comment) {
        sendTrap(SnmpTrapVariables.ERROR_TRAP, ip, name, url, comment);
    }

    /**
	 * Send a trap indication that this step produced a warning.<br>
	 * A step is defined by the combination of IP and name.<br>
	 * Make sure to call {@link sendOKTrap} in case of no failure.<br>
	 * 
	 * @param ip
	 *            A imaginary IP that stands for the application (i.e.
	 *            192.168.X.Y)
	 * @param name
	 *            The name of the step (must be identical for failure and ok
	 *            condition)
	 * @param url
	 *            The URL where the problem happened
	 * @param comment
	 *            A comment
	 */
    public void sendWarningTrap(String ip, String name, String url, String comment) {
        sendTrap(SnmpTrapVariables.WARNING_TRAP, ip, name, url, comment);
    }
}
