package org.apache.avalon.cornerstone.blocks.security.realms.test;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractUsernamePasswordAttributeTestCase extends AbstractUsernamePasswordBaseTestCase implements AttributeSupportTestInterface {

    public AbstractUsernamePasswordAttributeTestCase(String name) {
        super(name);
    }

    /**
     *  @return the Principal that is to be used by testAttribute() and
     *          testAttributesForPrincipal()
     */
    protected abstract Principal getPrincipal();

    /**
     *  @return the name of the attribute that is to be used by testAttribute() and
     */
    protected abstract String getAttributeName();

    /**
     *  @return the String that should be returned by a call to
     *          GroupSupport.getAttribute() for the Principal returned by
     *          getPrincipal() and the attribute name returned by
     *          getAttributeName()
     */
    protected abstract String getExpectedAttributeValue();

    /**
     *  @return Set of attribute Principals that should be returned by a call to
     *          GroupSupport.getAttributeForPrinciple() for the Principal
     *          returned by getPrincipal()
     */
    protected abstract Set getExpectedAttributeSet();

    public void testAttribute() {
        performTestAttribute(getPrincipal(), getAttributeName(), getExpectedAttributeValue());
    }

    public void testAttributesForPrincipal() {
        performTestAttributesForPrincipal(getPrincipal(), getExpectedAttributeSet());
    }
}
