package com.bitgate.util.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import com.bitgate.server.Server;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.node.NodeUtil;

/**
 * This class allows for variable persistence based on Scope ID data.  Similar to JDO, but does not persist large
 * objects, and does not add hooks to allow for complex reading, writing, or recreation of objects from the database.
 * This may be a Phase 2 object design, but it was not implemented in this design, as it is not necessary.
 * <p/>
 * This class serves as a front-end for container objects, which are not used specifically by a programmer.  These are,
 * instead, handled internally within this class.  So, this class allows for getters, setters, and deleters of object data.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/persistence/GlobalPersistence.java#6 $
 */
public class GlobalPersistence {

    private String scopeId;

    private String sessionDb;

    private boolean databasePersist;

    private ConcurrentHashMap updateTimesVariables;

    private ConcurrentHashMap updateTimesHashmaps;

    private ConcurrentHashMap updateTimesStacks;

    private ConcurrentHashMap updateTimesLists;

    private ConcurrentHashMap updateTimesArrayLists;

    private GlobalPersistenceContainer pContainer;

    private static final int PTYPE_VARIABLE = 1;

    private static final int PTYPE_HASHMAP = 2;

    private static final int PTYPE_ARRAY = 3;

    private static final int PTYPE_STACK = 4;

    private static final int PTYPE_LIST = 5;

    /**
     * Constructor.
     *
     * @param sessionId Session ID of the currently assigned client.
     * @param sessionDb Session Database.
     */
    public GlobalPersistence(String scopeId, String sessionDb) {
        this.scopeId = scopeId;
        this.sessionDb = sessionDb;
        this.updateTimesVariables = new ConcurrentHashMap();
        this.updateTimesHashmaps = new ConcurrentHashMap();
        this.updateTimesStacks = new ConcurrentHashMap();
        this.updateTimesLists = new ConcurrentHashMap();
        this.updateTimesArrayLists = new ConcurrentHashMap();
        String persistDb = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.default']/property[@type='engine.globalpersistdatabase']/@value");
        if (persistDb != null && persistDb.equalsIgnoreCase("true")) {
            Debug.debug("Using database persistence.");
            databasePersist = true;
        } else {
            Debug.debug("Not using database persistence.");
            databasePersist = false;
        }
        this.pContainer = new GlobalPersistenceContainer(databasePersist, scopeId, sessionDb);
    }

    /**
     * Restarts and clears the persistence container.
     */
    public void shutdown() {
        pContainer.shutdown();
        this.updateTimesVariables = null;
        this.updateTimesHashmaps = null;
        this.updateTimesStacks = null;
        this.updateTimesLists = null;
        this.updateTimesArrayLists = null;
        Debug.inform("Persistence object for '" + scopeId + "' db '" + sessionDb + "' shutdown.");
    }

    /**
     * Retrieves a persistent variable by name.
     *
     * @param var The variable name.
     * @return <code>String</code> containing the contents of the variable.
     */
    public String getVariable(String var) {
        return pContainer.getVariable(var);
    }

    /**
     * Assigns a variable with a specified value.
     *
     * @param var The variable name.
     * @param value The value to assign to said variable.
     */
    public void setVariable(String var, String value) {
        pContainer.setVariable(var, value);
    }

    /**
     * Assigns the variable expiration time.
     *
     * @param var The variable name.
     * @param value The value to assign to the expire time.
     */
    public void setVariableExpire(String var, long value) {
        pContainer.setVariableExpire(var, value);
    }

    /**
     * Retrieves the expiration time of the variable.
     *
     * @param var The variable name.
     * @return <code>long</code> value containing the number of minutes the variable will expire in.
     */
    public long getVariableExpire(String var) {
        return pContainer.getVariableExpire(var);
    }

    /**
     * Retrieves a list of all known persistent variables.
     *
     * @return <code>ArrayList</code> of known persistent variables.
     */
    public ArrayList getVariableList() {
        return pContainer.getVariableList();
    }

    /**
     * Returns the last update time of the variable in question.
     *
     * @param var The variable to look up.
     * @return <code>String</code> containing the last known update time.
     */
    public String getVariableLastUpdate(String var) {
        Object varObject = updateTimesVariables.get(var);
        if (varObject != null) {
            return (String) varObject;
        }
        return "0";
    }

    /**
     * Deletes a variable assignment.
     *
     * @param var The variable name.
     */
    public void deleteVariable(String var) {
        pContainer.deleteVariable(var);
    }

    /**
     * Retrieves a HashMap by variable name.
     *
     * @param var The variable name to look up.
     * @return <code>HashMap</code> containing the result.
     */
    public HashMap getHashMap(String var) {
        return pContainer.getHashMap(var);
    }

    /**
     * Assigns a variable to a HashMap by key and value.
     *
     * @param var The variable name.
     * @param key The variable keyname.
     * @param value The value to assign to the key.
     */
    public void setHashMap(String var, String key, String value) {
        pContainer.setHashMap(var, key, value);
    }

    /**
     * Assigns the variable expiration time.
     *
     * @param var The variable name.
     * @param value The value to assign to the expire time.
     */
    public void setHashMapExpire(String var, long value) {
        pContainer.setHashMapExpire(var, value);
    }

    /**
     * Retrieves the expiration time of the variable.
     *
     * @param var The variable name.
     * @return <code>long</code> value containing the number of minutes the variable will expire in.
     */
    public long getHashMapExpire(String var) {
        return pContainer.getHashMapExpire(var);
    }

    /**
     * Assigns an entire HashMap to a variable.
     *
     * @param var The variable name.
     * @param hash The HashMap object to assign.
     */
    public void setHashMap(String var, HashMap hash) {
        pContainer.setHashMap(var, hash);
    }

    /**
     * Retrieves a list of all known persistent HashMap variables.
     *
     * @return <code>ArrayList</code> of known persistent HashMap variables.
     */
    public ArrayList getHashmapList() {
        return pContainer.getHashMapList();
    }

    /**
     * Returns the last update time of the variable in question.
     *
     * @param var The variable to look up.
     * @return <code>String</code> containing the last known update time.
     */
    public String getHashmapLastUpdate(String var) {
        Object varObject = updateTimesHashmaps.get(var);
        if (varObject != null) {
            return (String) varObject;
        }
        return "0";
    }

    /**
     * Deletes an entire hashmap by variable name.
     *
     * @param var The variable name to delete.
     */
    public void deleteHashMap(String var) {
        pContainer.deleteHashMap(var);
    }

    /**
     * Retrieves a stack object by variable name.
     *
     * @param var The variable name to retrieve.
     * @return <code>Stack</code> containing the retrieved data.
     */
    public Stack getStack(String var) {
        return pContainer.getStack(var);
    }

    /**
     * Adds an object to a Stack.
     *
     * @param var The variable name to assign.
     * @param value The variable to add.
     */
    public void setStack(String var, String value) {
        pContainer.setStack(var, value);
    }

    /**
     * Assigns the variable expiration time.
     *
     * @param var The variable name.
     * @param value The value to assign to the expire time.
     */
    public void setStackExpire(String var, long value) {
        pContainer.setStackExpire(var, value);
    }

    /**
     * Retrieves the expiration time of the variable.
     *
     * @param var The variable name.
     * @return <code>long</code> value containing the number of minutes the variable will expire in.
     */
    public long getStackExpire(String var) {
        return pContainer.getStackExpire(var);
    }

    /**
     * Retrieves a list of all known persistent Stack variables.
     *
     * @return <code>ArrayList</code> of known persistent HashMap variables.
     */
    public ArrayList getStackList() {
        return pContainer.getStackList();
    }

    /**
     * Returns the last update time of the variable in question.
     *
     * @param var The variable to look up.
     * @return <code>String</code> containing the last known update time.
     */
    public String getStackLastUpdate(String var) {
        Object varObject = updateTimesStacks.get(var);
        if (varObject != null) {
            return (String) varObject;
        }
        return "0";
    }

    /**
     * Deletes a stack object by variable.
     *
     * @param var The variable name to delete.
     */
    public void deleteStack(String var) {
        pContainer.deleteStack(var);
    }

    /**
     * Retrieves the LinkedList object by variable.
     *
     * @param var The variable name to look up.
     * @return <code>LinkedList</code> containing the looked up object.
     */
    public LinkedList getList(String var) {
        return pContainer.getList(var);
    }

    /**
     * Adds an object to a list.
     *
     * @param var The variable name.
     * @param value The value to add to the list.
     */
    public void setList(String var, String value) {
        pContainer.setList(var, value);
    }

    /**
     * Assigns the variable expiration time.
     *
     * @param var The variable name.
     * @param value The value to assign to the expire time.
     */
    public void setListExpire(String var, long value) {
        pContainer.setListExpire(var, value);
    }

    /**
     * Retrieves the expiration time of the variable.
     *
     * @param var The variable name.
     * @return <code>long</code> value containing the number of minutes the variable will expire in.
     */
    public long getListExpire(String var) {
        return pContainer.getListExpire(var);
    }

    /**
     * Adds an object to a list.
     *
     * @param var The variable name.
     * @param value The value to add to the list.
     */
    public void setList(String var, LinkedList value) {
        pContainer.setList(var, value);
    }

    /**
     * Retrieves a list of all known persistent Stack variables.
     *
     * @return <code>ArrayList</code> of known persistent HashMap variables.
     */
    public ArrayList getListsList() {
        return pContainer.getListsList();
    }

    /**
     * Returns the last update time of the variable in question.
     *
     * @param var The variable to look up.
     * @return <code>String</code> containing the last known update time.
     */
    public String getListLastUpdate(String var) {
        Object varObject = updateTimesLists.get(var);
        if (varObject != null) {
            return (String) varObject;
        }
        return "0";
    }

    /**
     * Deletes a list object by variable name.
     *
     * @param var The variable name to delete.
     */
    public void deleteList(String var) {
        pContainer.deleteList(var);
    }

    /**
     * Retrieves an array list object by variable name.
     *
     * @param var The variable name to look up.
     * @return <code>ArrayList</code> object.
     */
    public ArrayList getArray(String var) {
        return pContainer.getArray(var);
    }

    /**
     * Adds an object to the end of an array.
     *
     * @param var The variable name.
     * @param value The value to add to the end of the array.
     */
    public void setArray(String var, String value) {
        pContainer.setArray(var, value);
    }

    /**
     * Assigns the variable expiration time.
     *
     * @param var The variable name.
     * @param value The value to assign to the expire time.
     */
    public void setArrayExpire(String var, long value) {
        pContainer.setArrayExpire(var, value);
    }

    /**
     * Retrieves the expiration time of the variable.
     *
     * @param var The variable name.
     * @return <code>long</code> value containing the number of minutes the variable will expire in.
     */
    public long getArrayExpire(String var) {
        return pContainer.getArrayExpire(var);
    }

    /**
     * Adds an object to the end of an array.
     *
     * @param var The variable name.
     * @param value The value to add.
     */
    public void setArray(String var, ArrayList value) {
        pContainer.setArray(var, value);
    }

    /**
     * Retrieves a list of all known persistent Stack variables.
     *
     * @return <code>ArrayList</code> of known persistent HashMap variables.
     */
    public ArrayList getArrayListsList() {
        return pContainer.getArrayListsList();
    }

    /**
     * Returns the last update time of the variable in question.
     *
     * @param var The variable to look up.
     * @return <code>String</code> containing the last known update time.
     */
    public String getArrayListLastUpdate(String var) {
        Object varObject = updateTimesArrayLists.get(var);
        if (varObject != null) {
            return (String) varObject;
        }
        return "0";
    }

    /**
     * Deletes the specified array name.
     *
     * @param var The variable name to delete.
     */
    public void deleteArray(String var) {
        pContainer.deleteArray(var);
    }
}
