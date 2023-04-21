package flex.messaging.services.remoting.adapters;

import flex.messaging.config.SecurityConstraint;

/**
 * Used to define included and excluded methods exposed by the <tt>JavaAdapter</tt>
 * for a remoting destination.
 * This class performs no internal synchronization.
 */
public class RemotingMethod {

    private String name;

    /**
     * Returns the method name.
     * Because mapping ActionScript data types to Java data types is indeterminate 
     * in some cases, explicit overloaded methods are not currently supported so no 
     * parameter signature property is defined.
     * 
     * @return method name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the method name.
     * Because mapping ActionScript data types to Java data types is indeterminate 
     * in some cases, explicit overloaded methods are not currently supported so no 
     * parameter signature property is defined.
     * 
     * @param value method name
     */
    public void setName(String value) {
        name = value;
    }

    private SecurityConstraint constraint;

    /**
     * Returns the <tt>SecurityConstraint</tt> that will be applied to invocations
     * of the remoting method.
     * 
     * @return <tt>SecurityConstraint</tt> that will be applied to invocations
     * of the remoting method.
     */
    public SecurityConstraint getSecurityConstraint() {
        return constraint;
    }

    /**
     * Sets the <tt>SecurityConstraint</tt> that will be applied to invocations of
     * the remoting method.
     * 
     * @param value the <tt>SecurityConstraint</tt> that will be applied to invocations of
     * the remoting method.
     */
    public void setSecurityConstraint(SecurityConstraint value) {
        constraint = value;
    }
}
