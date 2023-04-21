package gov.cca.ports;

import gov.cca.*;
import java.util.*;

/** KeyValuePort for use decorating components with strings external
 * to the implementation of the component.
 * The keys may not contain whitespace or =. The values may be
 * anything in a String.
 * Alternative, misleading names for this class would be
 * <pre>
 *   EnvironmentPort (which has too many global connotations)
 *   RegistryPort (which has way too many global connotations)
 *   PropertiesPort (which will be confused with port properties and java Properties...).
 *   ParameterPort (which is already taken in the CCAFFEINE framework.).
 * </pre>
 * This class is deliberately simpler than java Properties, for
 * compatibility with basic c++ interfaces.
 * In a reasonable framework, each component will just be
 * automatically decorated with one of these if that framework
 * exposes the component to external agents.
 * The following code fragment illustrates the use of the KeyValuePort
 * by a framework Services implementation to decorate 
 * every component with a properties for the frameworks use.</p>
 * <pre>
 *     String[] dummy = null;
 *     try {
 *       addProvidesPort(kvp,createPortInfo("cProps","gov.cca.ComponentProperties",dummy));
 *     } catch (Exception e) { // cannot fail, due to other framework design features }
 * </pre>
 * The following code fragment illustrates the use of the port as a service by
 * a component that wants to know/modify what the framework says about it.
 * <pre>
 *   setServices(Services s) {
 *     String[] dummy = null;
 *     try {
 *       s.registerUsesPort(kvp,createPortInfo("sProps","gov.cca.ComponentPropertiesService",dummy));
 *     } catch (Exception e) { // cannot fail, if we adopt it as a well known service. }
 *   KeyValuePort kvp = (KeyValuePort)getPort("sProps");
 *   kvp.setValue("gov.babel.guiApplet.URL",
 *                "http://z.ca.sandia.gov/cgi-bin/babel.asp?component=gov.sandia.viz");
 *   s.releasePort("sProps");
 *   s.unregisterUsesPort("sProps");
 * </pre>
 * @author Ben Allan, 8/5/2000, Sandia National Laboratories.
 * @version $Id: KeyValuePort.java,v 1.1.1.1 2002/05/15 18:46:39 rob Exp $
 */
public interface KeyValuePort extends Port {

    /** Return the value of the requested key.  
      If key unknown, returns null.  */
    public String getValue(String key);

    /** Store the value for the given key, possibly replacing previous.
      Keys cannot contain the whitespace or '='. Returns nonzero on
      bogus input or other error. */
    public int setValue(String key, String value);

    /** Delete a key and associated value. The deleted value is returned.*/
    public String remove(String key);

    /** Return enumeration of all known keys.  May be empty.  */
    public Enumeration getKeys();
}
