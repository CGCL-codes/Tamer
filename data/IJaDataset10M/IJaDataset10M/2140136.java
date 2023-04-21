package simple.xml.util;

import simple.xml.Attribute;
import simple.xml.Root;

/**
 * This object is stored within a <code>Resolver</code> so that it 
 * can be retrieved using a string that matches its pattern. Any
 * object that extends this can be inserted into the resolver and
 * retrieved using a string that matches its pattern. For example
 * take the following pattern "*.html" this will match the string
 * "/index.html" or "readme.html". This object should be extended
 * to add more XML attributes and elements, which can be retrieved
 * when the <code>Match</code> object is retrieve from a resolver.
 *
 * @author Niall Gallagher
 */
@Root(name = "match")
public abstract class Match {

    /**
    * This is the pattern string that is used by the resolver.
    */
    @Attribute(name = "pattern")
    protected String pattern;
}
