package com.ibm.webdav;

/**
 * A target selector can be a label name, version name, working resource id,
 * or an indicator that the VersionedResource itself should be selected.
 * A TargetSelector selects the indicated revision.
 */
public class LabelSelector extends TargetSelector {

    /**
 * Get the selector key for this TargetSelector.
 *
 * @return the Target-Selector key
 * @exception com.ibm.webdav.WebDAVException
 */
    public String getSelectorKey() throws WebDAVException {
        return "label " + targetSelector;
    }
}
