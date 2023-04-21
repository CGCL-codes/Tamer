package org.equanda.persistence.xml;

/**
 * convert table and field names from given names to something acceptable for the specific database Default
 * implementation : no conversion
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class Convert {

    /**
     * initialise conversion routines, may read conversion tables
     */
    public void init() {
    }

    /**
     * finish off conversion routines, may be done to make conversions persistent
     */
    public void done() {
    }

    /**
     * convert a table name
     *
     * @param name
     * @return
     */
    public String convertTable(String name) {
        return name;
    }

    /**
     * convert a field name
     *
     * @param name
     * @return
     */
    public String convertField(String name) {
        return name;
    }
}
