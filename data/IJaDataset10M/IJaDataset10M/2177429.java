package net.sf.crsx.generator.java.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Term dictionary
 * 
 * @author  <a href="mailto:villard@us.ibm.com">Lionel Villard</a>
 */
public class Dictionary {

    public static final Dictionary SINGLETON = new Dictionary();

    /** Term names indexed by their ID */
    protected final List<String> names;

    /** Term ids indexed by their name */
    protected final Map<String, Integer> ids;

    Dictionary() {
        names = new ArrayList<String>();
        ids = new HashMap<String, Integer>();
    }

    public String getName(int id) {
        return names.get(id);
    }

    /**
     * Gets the unique ID corresponding to the given name
     * @param name
     * @return an ID > 0 
     */
    public int getNonNullID(String name) {
        final Integer i = ids.get(name);
        if (i == null) {
            int id = names.size();
            add(name, id);
            return id;
        }
        return i;
    }

    /**
     * Gets the unique ID corresponding to the given name
     * @param name
     * @return an ID > 0 or -1 if does not exist.
     */
    public int getID(String name) {
        final Integer i = ids.get(name);
        return i == null ? -1 : i;
    }

    /**
     * Creates constructor of the given name.
     * 
     * An ID is automatically attached to the name if none already exists
     * @param name
     * @param properties  
     * @param subbinders
     * @param args
     * @return
     */
    public Term createConstructor(String name, Properties properties, Variable[][] subbinders, Term... args) {
        return createConstructor(getNonNullID(name), properties, subbinders, args);
    }

    /**
     * Creates constructor of the given id.
     *  
     * @param id
     * @param properties  
     * @param subbinders
     * @param args
     * @return
     */
    public Term createConstructor(int id, Properties properties, Variable[][] subbinders, Term... args) {
        return new FunctionConstruction(id, properties, subbinders, args);
    }

    private final void add(String name, int id) {
        while (names.size() <= id) names.add(null);
        names.set(id, name);
        ids.put(name, id);
    }
}
