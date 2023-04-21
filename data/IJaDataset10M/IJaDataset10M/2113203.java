package goldengate.snmp.utils;

import org.snmp4j.smi.Counter32;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Opaque;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.Variable;
import goldengate.snmp.interf.GgInterfaceVariableFactory;

/**
 * Default VariableFactory
 * 
 * @author Frederic Bregier
 * 
 */
public class GgDefaultVariableFactory implements GgInterfaceVariableFactory {

    @Override
    public Variable getVariable(OID oid, int type, int mibLevel, int entry) {
        Variable var;
        switch(type) {
            case SMIConstants.SYNTAX_INTEGER:
                var = new Integer32();
                break;
            case SMIConstants.SYNTAX_OCTET_STRING:
                var = new OctetString();
                break;
            case SMIConstants.SYNTAX_NULL:
                var = new Null();
                break;
            case SMIConstants.SYNTAX_OBJECT_IDENTIFIER:
                var = new OID();
                break;
            case SMIConstants.SYNTAX_IPADDRESS:
                var = new IpAddress();
                break;
            case SMIConstants.SYNTAX_COUNTER32:
                var = new Counter32();
                break;
            case SMIConstants.SYNTAX_GAUGE32:
                var = new Gauge32();
                break;
            case SMIConstants.SYNTAX_TIMETICKS:
                var = new TimeTicks();
                break;
            case SMIConstants.SYNTAX_OPAQUE:
                var = new Opaque();
                break;
            case SMIConstants.SYNTAX_COUNTER64:
                var = new Counter64();
                break;
            default:
                throw new IllegalArgumentException("Unmanaged Type: " + type);
        }
        return var;
    }
}
