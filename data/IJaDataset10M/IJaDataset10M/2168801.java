package com.volantis.mcs.dom2wbsax;

import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbdom.EmptyElementType;

/**
 * This class defines properties of the WBSAX processor
 */
public interface WBSAXProcessorConfiguration {

    /**
     * Getter for the version code.
     *
     * @return The VersionCode for this processor.
     */
    VersionCode getVersionCode();

    /**
     * Getter for the public ID code.
     *
     * @return the public ID code for this processor.
     */
    PublicIdCode getPublicIdCode();

    /**
     * Get the empty {@link com.volantis.mcs.wbdom.EmptyElementType} using the element name.
     *
     * @param elementName
     *         the name of the element.
     * @return the empty {@link com.volantis.mcs.wbdom.EmptyElementType} using the element name.
     */
    EmptyElementType getEmptyElementType(String elementName);

    /**
     * Determine whether element and attribute is part of the element attribute
     * mapping that my contain asset URLs.
     * @param elementName
     *         the name of the element.
     * @param attributeName
     *         the name of the attribute.
     * @return
     */
    boolean isURLAttribute(String elementName, String attributeName);
}
