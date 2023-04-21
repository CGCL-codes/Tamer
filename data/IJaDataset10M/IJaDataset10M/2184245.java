package com.google.gdata.data.projecthosting;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Issue mergedInto updates.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = ProjectHostingNamespace.ISSUES_ALIAS, nsUri = ProjectHostingNamespace.ISSUES, localName = MergedIntoUpdate.XML_NAME)
public class MergedIntoUpdate extends ValueConstruct {

    /** XML element name */
    static final String XML_NAME = "mergedIntoUpdate";

    /**
   * Default mutable constructor.
   */
    public MergedIntoUpdate() {
        this(null);
    }

    /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable value or <code>null</code> for a mutable value
   */
    public MergedIntoUpdate(String value) {
        super(ProjectHostingNamespace.ISSUES_NS, XML_NAME, null, value);
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
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(MergedIntoUpdate.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    public String toString() {
        return "{MergedIntoUpdate value=" + getValue() + "}";
    }
}
