package com.google.gdata.data.docs;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Composer of the track.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = DocsNamespace.DOCS_ALIAS, nsUri = DocsNamespace.DOCS, localName = Composer.XML_NAME)
public class Composer extends ValueConstruct {

    /** XML element name */
    static final String XML_NAME = "composer";

    /**
   * Default mutable constructor.
   */
    public Composer() {
        this(null);
    }

    /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable value or <code>null</code> for a mutable value
   */
    public Composer(String value) {
        super(DocsNamespace.DOCS_NS, XML_NAME, null, value);
        setRequired(false);
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
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(Composer.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    public String toString() {
        return "{Composer value=" + getValue() + "}";
    }
}
