package org.jbrix.util;

import org.w3c.dom.*;

/**
 *   The Configurable interface allows any arbitrary implementing object
 *   to be "configured" with an XML element. What exactly this means is
 *   unrestricted and not specified here - it could be just about
 *   anything.
 *
 *   Commonly (though not by necessity) an XML file for some
 *   component or application would contain elements that allow specific
 *   properties to be defined and set, and use those to configure
 *   corresponding objects through this interface. An example is the
 *   Xybrix application configuration file, which contains elements
 *   defining the editors utilized by a given application. The application
 *   stores these elements upon startup, and later when an instance
 *   of one of these editors is created, the corresponding editor
 *   configuration element is passed as a parameter to the configure method.
 */
public interface Configurable {

    /**
	 *   This method provides a means of allowing an object to be
	 *   arbitrarily configured with an XML element.
	 */
    void configure(Element configuration);
}
