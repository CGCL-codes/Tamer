package monitor.mib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import monitor.entities.ManagedInterface;
import monitor.snmp.L2SnmpResponseColl;
import monitor.utils.AbstractIpAddress;
import monitor.utils.Ip6Address;
import net.AddressException;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

/**
 * MAC �s IP  MIB entry-k defini�l�sa
 * @author gyuf
 *
 */
public class MacAndIpMIB extends Base {

    public static NamedOID dot1dTpFdbEntry = new NamedOID("1.3.6.1.2.1.17.4.3.1", "dot1dTpFdbEntry", true, OIDType.Private);

    public static NamedOID dot1dTpFdbAddress = new NamedOID("1.3.6.1.2.1.17.4.3.1.1", "dot1dTpFdbAddress", true, OIDType.Special);

    public static NamedOID dot1dTpFdbPort = new NamedOID("1.3.6.1.2.1.17.4.3.1.2", "dot1dTpFdbPort", true, OIDType.Special);

    public static NamedOID ipAdEntIfIndex = new NamedOID("1.3.6.1.2.1.4.20.1.2", "ipAdEntIfIndex", true, OIDType.Special);

    public static NamedOID cIpAddressIfIndex = new NamedOID("1.3.6.1.4.1.9.10.86.1.1.2.1.3", "cIpAddressIfIndex", true, OIDType.Special);

    public static NamedOID ipNetToMediaPhysAddress = new NamedOID("1.3.6.1.2.1.4.22.1.2", "ipNetToMediaPhysAddress", true, OIDType.Special);

    public MacAndIpMIB() {
        super();
        init();
    }

    @Override
    public void init() {
        oids = new Vector<NamedOID>();
        oids.add(dot1dTpFdbAddress);
        oids.add(dot1dTpFdbPort);
        oids.add(ipAdEntIfIndex);
        oids.add(cIpAddressIfIndex);
        oids.add(ipNetToMediaPhysAddress);
        super.oids = oids;
    }

    @Override
    public Vector<L2SnmpResponseColl> setProperties(Vector<L2SnmpResponseColl> nodes) {
        for (L2SnmpResponseColl node : nodes) {
            selectMac(node);
        }
        return nodes;
    }

    private void selectMac(L2SnmpResponseColl node) {
        HashMap<String, String> mac = new HashMap<String, String>();
        HashMap<String, String> interf = new HashMap<String, String>();
        String tmp;
        String oid;
        ManagedInterface ni;
        for (VariableBinding vb : node.getResults()) {
            oid = vb.getOid().toString();
            if (oid.indexOf(dot1dTpFdbPort.toString()) != -1) {
                int len = dot1dTpFdbPort.toString().length() + 1 > oid.length() ? oid.length() : dot1dTpFdbPort.toString().length() + 1;
                tmp = oid.substring(len);
                interf.put(tmp, vb.getVariable().toString());
            }
            if (oid.indexOf(dot1dTpFdbAddress.toString()) != -1) {
                int len = dot1dTpFdbAddress.toString().length() + 1 > oid.length() ? oid.length() : dot1dTpFdbAddress.toString().length() + 1;
                tmp = oid.substring(len);
                mac.put(tmp, vb.getVariable().toString());
            }
            NamedOID noid = findOID(vb.getOid());
            if (noid != null) {
                if (noid.getType() == OIDType.Special) {
                    handleSpecialCases(node, vb, noid);
                }
            }
        }
        Iterator it = interf.entrySet().iterator();
        String macTmp;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            ni = node.getInterfaceById(pairs.getValue().toString());
            if (ni != null) {
                macTmp = mac.get(pairs.getKey());
                if (macTmp != null) ni.addMac(macTmp);
            }
        }
    }

    @Override
    public void handleSpecialCases(L2SnmpResponseColl node, VariableBinding vb, NamedOID oid) {
        if (oid.getOID().compareToIgnoreCase(ipAdEntIfIndex.getOID().toString()) == 0) {
            addIPv4AddrForIf(node, vb, oid);
        } else if (oid.getOID().compareToIgnoreCase(cIpAddressIfIndex.getOID().toString()) == 0) {
            addIPv6AddrForIf(node, vb, oid);
        } else if (oid.getOID().contains(ipNetToMediaPhysAddress.toString())) {
            if (node.isRouter()) {
                addARPTableEntry(node, vb, oid);
            }
        }
    }

    private void addIPv4AddrForIf(L2SnmpResponseColl node, VariableBinding vb, NamedOID oid) {
        String origOID = vb.getOid().toString();
        String tmp = origOID.substring(ipAdEntIfIndex.toString().length());
        String ip = tmp.substring(tmp.indexOf(".") + 1);
        String ifId = getValue(vb);
        try {
            node.addIpForIf(ifId, AbstractIpAddress.createAddress(ip));
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }

    private void addIPv6AddrForIf(L2SnmpResponseColl node, VariableBinding vb, NamedOID oid) {
        String noid = vb.getOid().toString();
        String[] suffix = new NamedOID("1.3.6.1.4.1.9.10.86.1.1.2.1", "cIpAddressEntry", true, OIDType.Private).getSuffixArrayFrom(noid);
        String IfIndex;
        if (suffix.length != 0) {
            Ip6Address addressAddr = new Ip6Address(getIp6Address(suffix, 3));
            if (cIpAddressIfIndex.isPrefixOf(noid)) {
                IfIndex = getValue(vb);
                try {
                    if (new Integer(IfIndex).intValue() > 0) {
                        node.addIpForIf(IfIndex, addressAddr);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private byte[] getIp6Address(String[] s, int startidx) {
        byte[] addr = new byte[16];
        int j = 0;
        boolean success = true;
        for (int i = startidx; i < startidx + 16; i++, j++) {
            try {
                addr[j] = (byte) Short.parseShort(s[i]);
            } catch (IndexOutOfBoundsException e) {
                success = false;
                break;
            }
        }
        if (success) {
            return addr;
        } else {
            return null;
        }
    }

    private void addARPTableEntry(L2SnmpResponseColl node, VariableBinding vb, NamedOID oid) {
        String origOID = vb.getOid().toString();
        String tmp = origOID.substring(ipNetToMediaPhysAddress.toString().length());
        String ifId = tmp.substring(1, tmp.indexOf(".", 1));
        String ip = tmp.substring(tmp.indexOf(".", 1) + 1);
        OctetString mac = (OctetString) vb.getVariable();
        node.addArpEntry(ifId, ip, mac.toString());
    }

    @Override
    public String getVendor() {
        return null;
    }

    @Override
    public Vector<NamedOID> getOids(DiscoveryType dt) {
        Vector<NamedOID> ret = new Vector<NamedOID>();
        if (true) {
            for (NamedOID oid : oids) {
                if (oid.getType() != OIDType.Private) ret.add(oid);
            }
            return ret;
        } else return null;
    }
}
