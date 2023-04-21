package org.apache.jetspeed.portal.portlets;

import org.apache.jetspeed.portal.PortletException;

/**
 * A Velocity based portlet implementation
 *    <p> 
 *   <strong>NOTE:</strong>This supports the pre-MVC style of template 
 *    based portlet development and is supplied for backward compatibility.
 *    The prefered method is to define template-based portlets is to use    
 *    @see org.apache.jetspeed.portal.portlets.GenericMVCPortlet
 *    or a sub-class there of.  The GenericMVCPortlet javadoc provides
 *    instructions for using using the MVC portlet model.
 *   </p>
 * @author <a href="mailto:re_carrasco@bco011.sonda.cl">Roberto Carrasco</a>
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @author <a href="mailto:paulsp@apache.org">Paul Spencer</a>
 * @author <a href="mailto:sweaver@rippe.com">Scott Weaver</a>u
 */
public class VelocityPortlet extends GenericMVCPortlet {

    /**
    * STW: Backward compatibility: set the viewType to "Velocity".
    */
    public void init() throws PortletException {
        setCacheable(true);
        setViewType("Velocity");
        super.init();
    }
}
