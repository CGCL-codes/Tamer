package org.jplate.tmplate.directive;

/**
 *
 * This interface defines the API for parsed character directives.
 *
 */
public interface PCDataDirectiveIfc extends DirectiveIfc {

    /**
     *
     * This method will return the parsed character data.
     *
     * @return the parsed character data.
     *
     */
    public String getPCData();

    /**
     *
     * This method will set the parsed character data.
     *
     * @param pcData represents the parsed character data.
     *
     */
    public void setPCData(String pcData);
}
