package org.simpleframework.http.load;

import org.simpleframework.util.parse.URIParser;
import org.simpleframework.http.serve.Context;
import org.simpleframework.http.serve.Locator;
import org.simpleframework.util.net.Path;
import org.simpleframework.util.net.URI;

/**
 * The <code>PatternMapper</code> provides a mapper that is used 
 * to perform mapping using patterns. This provides a scheme like
 * the Servlet wild card mapping scheme. This <code>Mapper</code> 
 * allows arbitrary path configurations to be mapped directly to
 * a service name, which can be autoloaded to serve content.
 * <p>
 * This uses the wild card characters '*' and '?' to specify the
 * patterns used for URI path to service name matching. For example
 * take the pattern "*.html", this will match any URI path that 
 * ends with ".html", such as "/index.html" or "/example.html". If
 * the '?' character is used with '*' in the formation '*?' then
 * the match will be all characters up to the first occurance of 
 * the following character. For instance "/*?/*.html" will match
 * the path "/pub/index.html" but not "/pub/bin/index.html".
 * <p>
 * The specification of wild card patterns to service names is
 * done using the XML configuration file <code>Mapper.xml</code>.
 * This must be used by a <code>MapperEngine</code> to resolve
 * services to be loaded by resolving the service instance name.
 * <pre>
 *
 *    &lt;resolve&gt;
 *       &lt;match path="/*" name="file"/&gt;
 *       &lt;match path="/*?/*.txt" name="text"/&gt;
 *       &lt;match path="/???/*" name="three"/&gt;
 *    &lt;/resolve&gt;
 *
 * </pre>
 * Taking the above example mappings as entries within the XML
 * configuration file loaded, matches for all URI paths can 
 * match the pattern "/*", which signifies anything. However the
 * matches for the "/*?/*.txt" pattern will only match URI paths 
 * that end in ".txt" and have only a single path segment, like
 * "/path/README.txt". Any number of wild card characters can be 
 * specified and the order they appear in the configuration file 
 * is signifigant. The first pattern has the lowest priority and 
 * the last has the highest. So taking the above specification, a
 * URI path such as "/1234/README.txt" will match the "/*?/*.txt" 
 * pattern, as it is resolved before "/*" pattern. Also "/???/*"
 * matches an initial path segment with only three characters.
 *
 * @see org.simpleframework.http.load.MapperEngine
 * @see org.simpleframework.util.Resolver
 */
public class PatternMapper implements Mapper {

    /**
    * This determines the prefix for arbitrary URI paths.
    */
    private PatternResolver resolver;

    /**
    * Constructor for the <code>PatternMapper</code>. This is used
    * to create a <code>Mapper</code> that can be used to resolve
    * service instance names given an arbitrary URI path. This uses
    * a configuration file located using the <code>Context</code> 
    * object supplied with the context. This configuration file is
    * used to acquire the mappings for URI path to service names.
    *
    * @param context used to find the mapping configuration file
    */
    public PatternMapper(Context context) {
        this(context.getLocator());
    }

    /**
    * Constructor for the <code>PatternMapper</code>. This is used
    * to create a <code>Mapper</code> that can be used to resolve
    * service instance names given an arbitrary URI path. This uses
    * a configuration file located using the <code>Locator</code> 
    * object supplied with the context. This configuration file is
    * used to acquire the mappings for URI path to service names.
    *
    * @param lookup used to find the mapping configuration file
    */
    private PatternMapper(Locator lookup) {
        this.resolver = new PatternResolver(lookup);
    }

    /**
    * This method is used to acquire a path given the unmodified
    * URI path. One of the most confusing things about mapping 
    * within the Servlet Specification is the combination of
    * pattern mapping such as <code>*.jsp</code> and context
    * mapping such as <code>/context/</code>. When these are
    * used together determining the <code>getPathInfo</code> is
    * not intuitive. So this will simply return the path as is.
    *
    * @param path the URI path to extract a relative path with
    *
    * @return the specified URI path normalized and escaped
    */
    public String getPath(String path) {
        return path;
    }

    public String getName(String path) {
        return resolver.getName(path);
    }

    /** 
    * This method is used retrieve properties for a service by
    * using the service name. This will acquire the properties
    * if any for the named service instance. The properties will
    * contain zero or more name value pairs. If no properties
    * are associated with the service the instance returned
    * will be an empty map rather than a null object.
    *
    * @param name this is the name of the service instance
    *
    * @return returns a properties object for configuration
    */
    public Configuration getConfiguration(String name) {
        return resolver.getConfiguration(name);
    }

    /**
    * This method is used to retrieve the fully qualified class
    * name from the service instance name. This is provided such
    * that if a service has not been loaded, the class name can 
    * be retrieved from the service instance name, which will
    * enable the service to be loaded into the system from a URI
    * path provided with a HTTP request. If the service instance
    * name does not exist this method will return null.
    *
    * @param name this is the service instance being queried
    *
    * @return the fully qualified class name for the service
    */
    public String getClass(String name) {
        return resolver.getClass(name);
    }
}
