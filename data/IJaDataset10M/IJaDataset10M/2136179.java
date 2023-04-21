package org.openlms.servlet;

import java.util.HashMap;

/**
 * ServletProperties - properties for entire LMS.
 *
 * This class holds properties endemic to the src/web servlets.
 * src/lms/org/openlms/lms/LMSProperties could be used, but we want a complete independence
 * between src/web and src/lms.  The end result is that similar properties get set in both
 * locations.  See src/web build script for how to build againts src/lms if wanted. 
 * @author Rich Andrews
 *
 * @version $Id: ServletProperties.java,v 1.1 2003/03/18 17:50:53 huntbryan Exp $
 */
public final class ServletProperties extends Object {

    private static HashMap importerProperties = null;

    private ServletProperties() {
    }

    public static HashMap properties() {
        if (importerProperties == null) {
            importerProperties = new HashMap();
            importerProperties.put("import_staging_root", "/tmp");
            importerProperties.put("INITIAL_CONTEXT_FACTORY", "org.jnp.interfaces.NamingContextFactory");
            importerProperties.put("db_name", "openlms");
            importerProperties.put("adl_import_dir", "/opt/jboss/server/default/deploy/OpenLMS.ear/OpenLMS.war/");
            importerProperties.put("adl_import_staging_root", "/home/ossidian");
        }
        return importerProperties;
    }
}
