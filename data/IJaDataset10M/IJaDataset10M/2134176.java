package alt.viiigine.utils;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Dmitry S. Vorobiev
 */
public class CommandLineParameters {

    protected LinkedList<String> singleKeys = new LinkedList<String>();

    protected HashMap<String, String> pairKeys = new HashMap<String, String>();

    protected LinkedList<String> singleValues = new LinkedList<String>();

    public LinkedList<String> getSingleKeys() {
        return singleKeys;
    }

    public HashMap<String, String> getPairKeys() {
        return pairKeys;
    }

    public LinkedList<String> getSingleValues() {
        return singleValues;
    }
}
