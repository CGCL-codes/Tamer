package org.tide.tomcataccess;

import org.tidelaget.core.*;
import org.tide.webapp.*;
import java.util.Collection;
import org.w3c.dom.*;

public interface IfcTomcat {

    /**
	 * Returns a Collection with Strings containing
	 * TomcatContext-objects
	 * 
	 * @return Collection	Collection with TomcatContexts
	 */
    public Collection getContexts() throws TIDEException;

    /**
	* Saves tomcat configuration file server.xml and each contexts web.xml file
	*/
    public void save() throws TIDEException;

    public TomcatContext createTomcatContext() throws TIDEException;

    public Element getRootElement();
}
