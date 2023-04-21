package spring.jeuditechno.db.hsqldb;

import spring.jeuditechno.db.DBDictionary;

/**
 * @author rozange
 *
 */
public class HsqlDBDictionary implements DBDictionary {

    public String getIdentityQuery() {
        return "call identity()";
    }
}
