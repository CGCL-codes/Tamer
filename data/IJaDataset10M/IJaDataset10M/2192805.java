package com.volantis.schema;

/**
 * Represents a validation error where an element is expecting one child
 * element.
 */
public class ContentNotCompleteOneChild extends ValidationError {

    private final String expected;

    /**
     * Initialise.
     *
     * @param element        The element whose content is not complete.
     * @param childNamespace The namespace of the missing child element.
     * @param child          The name of the missing child element.
     */
    public ContentNotCompleteOneChild(String element, String childNamespace, String child) {
        expected = "error - cvc-complex-type.2.4.b: The content of element '" + element + "' is not complete. One of '{\"" + childNamespace + "\":" + child + "}' is expected.\n";
    }

    public boolean matches(String message) {
        return message.equals(expected);
    }

    public String getDescription() {
        return expected;
    }
}
