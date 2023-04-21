package org.genxdm.processor.w3c.xs.validation.impl;

/**
 * The status of a streaming XPath.
 */
final class IdentityXPathStatus {

    public int currentStep = 0;

    /**
	 * Refers to whether the type of the element is simple. This determines whether the text can be used as a key.
	 */
    public boolean isSimple = false;

    /**
	 * This is synonymous with the status not being the first in the list. All status entries after the first were
	 * dynamically added as a result of evaluating the XPath expression in a streaming fashion with a relocation (//).
	 */
    public final boolean removable;

    public IdentityXPathStatus(final boolean removable) {
        this.removable = removable;
    }
}
