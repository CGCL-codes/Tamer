package com.google.gdata.data.extensions;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.util.Namespaces;

/**
 * Used in work addresses.  Also for 'in care of' or 'c/o'.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = Namespaces.gAlias, nsUri = Namespaces.g, localName = Agent.XML_NAME)
public class Agent extends ValueConstruct {

    /** XML element name */
    static final String XML_NAME = "agent";

    /**
   * Default mutable constructor.
   */
    public Agent() {
        this(null);
    }

    /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable value or <code>null</code> for a mutable value
   */
    public Agent(String value) {
        super(Namespaces.gNs, XML_NAME, null, value);
    }

    /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
    public static ExtensionDescription getDefaultDescription(boolean required, boolean repeatable) {
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(Agent.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    public String toString() {
        return "{Agent value=" + getValue() + "}";
    }
}
