package monitor.mib;

import java.util.Vector;
import monitor.snmp.L2SnmpResponseColl;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.VariableBinding;

public abstract class Base implements L2MIBHandler {

    protected Vector<NamedOID> oids;

    public abstract void init();

    public abstract void handleSpecialCases(L2SnmpResponseColl node, VariableBinding vb, NamedOID oid);

    public abstract String getVendor();

    protected String getValue(VariableBinding vb) {
        String res = "";
        if (vb.getSyntax() == SMIConstants.SYNTAX_OCTET_STRING) {
            OctetString tmpStr = (OctetString) vb.getVariable();
            res = new String(tmpStr.getValue());
        } else if (vb.getSyntax() == SMIConstants.SYNTAX_INTEGER32) {
            Integer32 tmpStr = (Integer32) vb.getVariable();
            res = tmpStr.toString();
        } else if (vb.getSyntax() == SMIConstants.SYNTAX_OBJECT_IDENTIFIER) {
            OID tmpStr = (OID) vb.getVariable();
            res = tmpStr.toString();
        } else if (vb.getSyntax() == SMIConstants.SYNTAX_GAUGE32) {
            res = vb.getVariable().toString();
        } else if (vb.getSyntax() == SMIConstants.SYNTAX_COUNTER32) {
            res = vb.getVariable().toString();
        } else {
        }
        return res;
    }

    protected NamedOID findOID(OID oid) {
        for (NamedOID oidT : oids) {
            if (oidT.getOID().compareTo(oid.toString()) == 0) return oidT; else if (oidT.isBulk() && oid.toString().indexOf(oidT.getOID()) != -1) return oidT;
        }
        return null;
    }

    public Vector<L2SnmpResponseColl> setProperties(Vector<L2SnmpResponseColl> nodes) {
        NamedOID tmp;
        String value;
        for (L2SnmpResponseColl node : nodes) {
            if (node.getResults() != null) {
                for (VariableBinding vb : node.getResults()) {
                    tmp = findOID(vb.getOid());
                    if (tmp != null) {
                        if (tmp.getType() != OIDType.Special && !tmp.isBulk()) {
                            value = getValue(vb);
                            node.setAttribute(tmp.getName(), value);
                        } else if (tmp.getType() == OIDType.Special) {
                            handleSpecialCases(node, vb, tmp);
                        }
                    }
                }
            }
        }
        return nodes;
    }

    public Vector<NamedOID> getOids(DiscoveryType dt) {
        Vector<NamedOID> resOID = new Vector<NamedOID>();
        for (NamedOID oid : oids) {
            if (oid.getType() == OIDType.Public || oid.getType() == OIDType.Special) {
                resOID.add(oid);
            }
        }
        return resOID;
    }
}
