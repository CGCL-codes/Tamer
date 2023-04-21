package edu.vt.middleware.ldap.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.vt.middleware.ldap.LdapUtil;
import edu.vt.middleware.ldap.asn1.ContextType;
import edu.vt.middleware.ldap.asn1.DEREncoder;
import edu.vt.middleware.ldap.asn1.OctetStringType;
import edu.vt.middleware.ldap.asn1.SequenceEncoder;

/**
 * Request control for server side sorting. See RFC 2891. Control is defined as:
 *
 * <pre>
   SortKeyList ::= SEQUENCE OF SEQUENCE {
      attributeType   AttributeDescription,
      orderingRule    [0] MatchingRuleId OPTIONAL,
      reverseOrder    [1] BOOLEAN DEFAULT FALSE }
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision: 2197 $ $Date: 2012-01-01 22:40:30 -0500 (Sun, 01 Jan 2012) $
 */
public class SortRequestControl extends AbstractControl implements RequestControl {

    /** OID of this control. */
    public static final String OID = "1.2.840.113556.1.4.473";

    /** hash code seed. */
    private static final int HASH_CODE_SEED = 727;

    /** sort keys. */
    private SortKey[] sortKeys;

    /** Default constructor. */
    public SortRequestControl() {
        super(OID);
    }

    /**
   * Creates a new sort request control.
   *
   * @param  keys  sort keys
   */
    public SortRequestControl(final SortKey[] keys) {
        super(OID);
        setSortKeys(keys);
    }

    /**
   * Creates a new sort request control.
   *
   * @param  keys  sort keys
   * @param  critical  whether this control is critical
   */
    public SortRequestControl(final SortKey[] keys, final boolean critical) {
        super(OID, critical);
        setSortKeys(keys);
    }

    /**
   * Returns the sort keys.
   *
   * @return  sort keys
   */
    public SortKey[] getSortKeys() {
        return sortKeys;
    }

    /**
   * Sets the sort keys.
   *
   * @param  keys  sort keys
   */
    public void setSortKeys(final SortKey[] keys) {
        sortKeys = keys;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return LdapUtil.computeHashCode(HASH_CODE_SEED, getOID(), getCriticality(), sortKeys);
    }

    /**
   * Provides a descriptive string representation of this instance.
   *
   * @return  string representation
   */
    @Override
    public String toString() {
        return String.format("[%s@%d::criticality=%s, sortKeys=%s]", getClass().getName(), hashCode(), getCriticality(), Arrays.toString(sortKeys));
    }

    /** {@inheritDoc} */
    @Override
    public byte[] encode() {
        final DEREncoder[] keyEncoders = new DEREncoder[sortKeys.length];
        for (int i = 0; i < sortKeys.length; i++) {
            final List<DEREncoder> l = new ArrayList<DEREncoder>();
            l.add(new OctetStringType(sortKeys[i].getAttributeDescription()));
            if (sortKeys[i].getMatchingRuleId() != null) {
                l.add(new ContextType(0, sortKeys[i].getMatchingRuleId()));
            }
            if (sortKeys[i].getReverseOrder()) {
                l.add(new ContextType(1, sortKeys[i].getReverseOrder()));
            }
            keyEncoders[i] = new SequenceEncoder(l.toArray(new DEREncoder[l.size()]));
        }
        final SequenceEncoder se = new SequenceEncoder(keyEncoders);
        return se.encode();
    }
}
