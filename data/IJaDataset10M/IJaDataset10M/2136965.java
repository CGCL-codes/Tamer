package org.wfmc.wapi;

/**
 * Indicates that an attribute does not exist, or is read-only.
 *
 * @author Adrian Price
 */
public class WMInvalidAttributeException extends WMInvalidObjectException {

    public WMInvalidAttributeException(String attributeName) {
        super(WMError.WM_INVALID_ATTRIBUTE, attributeName);
    }
}
