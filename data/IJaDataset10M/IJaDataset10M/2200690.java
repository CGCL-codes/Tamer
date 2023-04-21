package com.sun.org.apache.xml.internal.security.encryption;

/**
 * A container for <code>ds:Transform</code>s.
 * <p>
 * It is defined as follows:
 * <xmp>
 * <complexType name='TransformsType'>
 *     <sequence>
 *         <element ref='ds:Transform' maxOccurs='unbounded'/>
 *     </sequence>
 * </complexType>
 * </xmp>
 *
 * @author Axl Mattheus
 * @see com.sun.org.apache.xml.internal.security.encryption.CipherReference
 */
public interface Transforms {

    /**
	 * Temporary method to turn the XMLEncryption Transforms class
	 * into a DS class.  The main logic is currently implemented in the
	 * DS class, so we need to get to get the base class.
	 * <p>
	 * <b>Note</b> This will be removed in future versions
     * @return
	 */
    com.sun.org.apache.xml.internal.security.transforms.Transforms getDSTransforms();
}
