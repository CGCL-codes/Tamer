package org.nakedobjects.metamodel.adapter.oid.stringable.directly;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.encoding.Encodable;

/**
 * An alternative to {@link Encodable}, intended to be used for <tt>Oid</tt>s
 * that can be encoded/decoded from strings.
 * 
 * <p>
 * This is inspired by the DSFA's implementation that uses <tt>CUS|1234567A</tt> as the
 * string representation of their <tt>OStoreOid</tt>, representing a Customer.
 */
public interface DirectlyStringableOid extends Oid {

    String enString();
}
