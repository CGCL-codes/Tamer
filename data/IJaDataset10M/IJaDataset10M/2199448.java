package com.google.gdata.data.photos;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * An opaque client checksum.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = Namespaces.PHOTOS_ALIAS, nsUri = Namespaces.PHOTOS, localName = GphotoChecksum.XML_NAME)
public class GphotoChecksum extends ValueConstruct {

    /** XML element name */
    static final String XML_NAME = "checksum";

    /**
   * Default mutable constructor.
   */
    public GphotoChecksum() {
        this(null);
    }

    /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable checksum or <code>null</code> for a mutable checksum
   */
    public GphotoChecksum(String value) {
        super(Namespaces.PHOTOS_NAMESPACE, XML_NAME, null, value);
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
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(GphotoChecksum.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    public String toString() {
        return "{GphotoChecksum value=" + getValue() + "}";
    }
}
