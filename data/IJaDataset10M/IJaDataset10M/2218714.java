package org.opennms.protocols.snmp;

import java.lang.*;
import org.opennms.protocols.snmp.SnmpSMI;
import org.opennms.protocols.snmp.SnmpSyntax;
import org.opennms.protocols.snmp.SnmpV2Error;
import org.opennms.protocols.snmp.SnmpUtil;

/**
 * The SnmpNoSuchObject object is typically returned by an
 * SNMPv2 agent when there is no matching object
 * identifier for the agent. The object is an SNMPv2 error
 * condition. This condition can be returned to a manager 
 * on a variable by variable basis.
 *
 * @see SnmpVarBind
 *
 * @author	Brian Weaver <weave@opennms.org>
 * @version	$Revision: 1.8 $
 *
 */
public class SnmpNoSuchObject extends SnmpV2Error {

    /**
	 * Defines the serialization format version.
	 *
	 */
    static final long serialVersionUID = -6750389210834760320L;

    /**
	 * The ASN.1 value that defines this 
	 * variable.
	 *
	 */
    public static final byte ASNTYPE = SnmpSMI.SMI_NOSUCHOBJECT;

    /**
	 * The default class construtor.
	 */
    public SnmpNoSuchObject() {
        super();
    }

    /**
	 * The class copy constructor.
	 */
    public SnmpNoSuchObject(SnmpNoSuchObject second) {
        super(second);
    }

    /**
	 * Returns the ASN.1 type for this particular
	 * object.
	 *
	 * @return ASN.1 identifier
	 *
	 */
    public byte typeId() {
        return ASNTYPE;
    }

    /**
	 * Returns a duplicate object of self. 
	 *
	 * @return A duplicate of self
	 */
    public SnmpSyntax duplicate() {
        return new SnmpNoSuchObject(this);
    }

    /**
	 * Returns a duplicate object of self. 
	 *
	 * @return A duplicate of self
	 */
    public Object clone() {
        return new SnmpNoSuchObject(this);
    }

    /**
	 * Returns the string representation of the object.
	 *
	 */
    public String toString() {
        return "SNMP No-Such-Object";
    }
}
