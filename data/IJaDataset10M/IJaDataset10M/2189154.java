package org.apache.naming;

import javax.naming.NameParser;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.CompositeName;

/**
 * Parses names.
 *
 * @author Remy Maucherat
 * @version $Revision: 467222 $ $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public class NameParserImpl implements NameParser {

    /**
     * Parses a name into its components.
     * 
     * @param name The non-null string name to parse
     * @return A non-null parsed form of the name using the naming convention 
     * of this parser.
     */
    public Name parse(String name) throws NamingException {
        return new CompositeName(name);
    }
}
