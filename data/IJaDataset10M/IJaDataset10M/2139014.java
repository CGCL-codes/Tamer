package org.scribble.protocol.model;

import org.scribble.extensions.*;
import org.scribble.model.ModelReference;

/**
 * This class represents the protocol notation.
 */
@RegistryInfo(extension = org.scribble.model.Notation.class)
public class ProtocolNotation extends org.scribble.model.AbstractNotation {

    public static final String NOTATION_CODE = "spr";

    /**
	 * This is the protocol notation constructor.
	 */
    public ProtocolNotation() {
        super(NOTATION_CODE, "Protocol", true);
    }

    /**
	 * This method returns the initial description associated
	 * with the supplied reference.
	 * 
	 * @param ref The reference
	 * @return The initial description, or null if no description
	 */
    public String getInitialDescription(ModelReference ref) {
        StringBuffer buf = new StringBuffer();
        buf.append("namespace ");
        buf.append(ref.getNamespace());
        buf.append(";\r\n\r\n");
        buf.append("protocol ");
        buf.append(ref.getLocalpart());
        if (ref.getLocatedRole() != null) {
            buf.append(ModelReference.LOCATED_REFERENCE_SEPARATOR);
            buf.append(ref.getLocatedRole());
            buf.append(" {\r\n\trole\tP;\r\n\r\n}\r\n");
        } else {
            buf.append(" {\r\n\trole\tP1, P2;\r\n\r\n}\r\n");
        }
        return (buf.toString());
    }
}
