package org.jnp.interfaces;

import javax.naming.NameParser;
import javax.naming.Name;
import javax.naming.CompoundName;
import javax.naming.NamingException;
import java.util.Properties;

/** The NamingParser for the jnp naming implementation
 * 
 * @author Rickard Oberg     
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57199 $
 */
public class NamingParser implements NameParser, java.io.Serializable {

    private static final long serialVersionUID = 2925203703371001031L;

    /** The unsynchronized syntax properties
    */
    static Properties syntax = new FastNamingProperties();

    public static Properties getSyntax() {
        return syntax;
    }

    public Name parse(String name) throws NamingException {
        return new CompoundName(name, syntax);
    }
}
