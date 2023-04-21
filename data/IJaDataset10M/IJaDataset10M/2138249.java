package org.xmlcml.cml.tools.test;

import org.xmlcml.cml.element.test.AbstractTest;

/** superclass to manage resources etc.
 * 
 * @author pm286
 *
 */
public abstract class AbstractToolTest extends AbstractTest {

    /**
     * resource
     */
    public static final String TOOLS_RESOURCE = "org" + U_S + "xmlcml" + U_S + "cml" + U_S + "tools" + U_S + "test";

    /**
     * examples
     */
    public static final String TOOLS_EXAMPLES = TOOLS_RESOURCE + U_S + "examples";
}
