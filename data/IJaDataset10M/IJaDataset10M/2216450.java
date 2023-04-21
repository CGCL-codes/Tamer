package javax.help;

/**
 * An ID was attempted to be created with incorrect arguments
 *
 * @author Roger D. Brinkley
 * @author Eduardo Pelegri-Llopart
 * @version	1.15	10/30/06
 */
public class BadIDException extends IllegalArgumentException {

    private Map map;

    private String id;

    private HelpSet hs;

    /**
     * Create the exception. Null values are allowed for each parameter.
     * 
     * @param map The Map in which the ID wasn't found
     * @param msg A generic message
     * @param id The ID in Map that wasn't found
     * @see javax.help.Map
     */
    public BadIDException(String msg, Map map, String id, HelpSet hs) {
        super(msg);
        this.map = map;
        this.id = id;
        this.hs = hs;
    }

    /**
     * The HelpSet in which the ID wasn't found
     */
    public Map getMap() {
        return map;
    }

    /**
     * The ID that wasn't found in the Map
     */
    public String getID() {
        return id;
    }

    /**
     * The HelpSet that wasn't found in the Map
     */
    public HelpSet getHelpSet() {
        return hs;
    }
}
