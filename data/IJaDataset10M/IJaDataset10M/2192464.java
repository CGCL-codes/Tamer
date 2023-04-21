package com.sri.emo.dbobj;

import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.registry.RequestRegistry;
import com.jcorporate.expresso.core.security.User;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * Factory for determing the parts of each type of node.
 */
public class PartsFactory {

    public static final String DELIMITER = "|";

    private static Logger slog = Logger.getLogger(PartsFactory.class);

    private static Hashtable<String, Part[]> sHashOfPartArrays = new Hashtable<String, Part[]>();

    /**
     * individual attributes hashed here
     */
    private static Hashtable<String, Part> sAttribHash = new Hashtable<String, Part>();

    private static Hashtable<String, NodeType> sNodeTypeHash = new Hashtable<String, NodeType>();

    private static Hashtable<String, NodeType> sFakeTypeHash = new Hashtable<String, NodeType>();

    /**
     * put node types in RAM cache
     */
    static {
        try {
            NodeType nodeType = new NodeType(User.getAdmin());
            List nodes = nodeType.searchAndRetrieveList();
            for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
                NodeType type = (NodeType) iter.next();
                addType(type);
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    /**
     * add a new type; typically, there will be no parts associated with new type,
     * but an empty array will be added to hash
     */
    public static void addType(NodeType type) throws DBException {
        sNodeTypeHash.put(type.getEntityName(), type);
        Part[] parts = type.getParts();
        sHashOfPartArrays.put(type.getEntityName(), parts);
        for (int i = 0; i < parts.length; i++) {
            Part part = parts[i];
            if (part.isOwnedAttribute() && (part.getPartType() != null)) {
                String key = getPartKey(type.getEntityName(), part.getPartType());
                if (sAttribHash.get(key) != null) {
                    getLogger().error("overwriting existing hashed Type with key: " + key);
                } else {
                    sAttribHash.put(key, part);
                }
            }
        }
    }

    /**
     * add a new fake type; fake types are just used for getting list of all types (for footer)
     */
    public static void addFakeType(NodeType type) throws DBException {
        sFakeTypeHash.put(type.getEntityName(), type);
    }

    private static String getPartKey(String parent, String partName) {
        StringBuffer buf = new StringBuffer();
        buf.append(parent);
        buf.append(DELIMITER);
        buf.append(partName);
        return buf.toString();
    }

    /**
     * @return a list of parts for paradigms, or empty array if not found; never null
     */
    public static Part[] getParts(String nodeType) {
        Part[] result;
        synchronized (sHashOfPartArrays) {
            Part[] resultsToClone = sHashOfPartArrays.get(nodeType);
            if (resultsToClone == null) {
                result = new Part[0];
            } else {
                result = new Part[resultsToClone.length];
                for (int i = 0; i < resultsToClone.length; i++) {
                    try {
                        result[i] = (Part) resultsToClone[i].clone();
                        result[i].setRequestingUser(RequestRegistry.getUser());
                    } catch (CloneNotSupportedException ex) {
                        throw new Error("Caught CloneNotSupportedException for a cloneable object: " + ex);
                    }
                }
            }
        }
        return result;
    }

    /**
     * necessary since the JSP doesn't know anything, but
     * must iterate to display attributes and send back edits with some discernible
     * connection between input text and attribute destination.
     *
     * @param type      type of node
     * @param attribNum number of attribute in parts list of node
     * @return name of attribute which corresponds to given type, num.  return null if cannot find
     */
    public static String getAttributeType(String type, int attribNum) throws DBException {
        Part[] parts = getParts(type);
        return parts[attribNum].getPartType();
    }

    /**
     * @return part corresponding to node, attrib params; null if not found
     */
    public static Part getAttribPart(String srcNodetype, String attribName) {
        Part part = null;
        String key = getPartKey(srcNodetype, attribName);
        if (key == null) {
            slog.error("cannot find key for object|attribute: " + srcNodetype + "|" + attribName);
        } else {
            part = sAttribHash.get(key);
        }
        if (part == null) {
            slog.error("cannot find object|attribute: " + key);
        }
        return part;
    }

    /**
     * @return array of parts that have picklists (i.e., the values for the part are chosen from a closed list, like the type of a MeasurementModel)
     */
    public static Part[] getPartsWithPicklists() throws DBException {
        LinkedList<Part> list = new LinkedList<Part>();
        Enumeration<String> enumeration = sAttribHash.keys();
        while (enumeration.hasMoreElements()) {
            String partkey = enumeration.nextElement();
            Part part = sAttribHash.get(partkey);
            if (part.hasPicklist()) {
                list.add(part);
            }
        }
        return (Part[]) list.toArray(new Part[list.size()]);
    }

    /**
     * @param relation relation for this part; pass in null for owned attributes
     * @return first part which matches partType, or null if not found
     */
    public static Part getPart(String parentType, String partType, String relation) throws DBException {
        Part result = null;
        Part[] parts = getParts(parentType);
        for (int i = 0; i < parts.length; i++) {
            Part part = parts[i];
            if (part.getPartType().equals(partType)) {
                if (relation == null) {
                    result = part;
                    break;
                } else {
                    if (part.getNodeRelation().equals(relation)) {
                        result = part;
                        break;
                    }
                }
            }
        }
        if (result == null) {
            getLogger().error("cannot find part with parent/type: " + parentType + "/" + partType);
        }
        return result;
    }

    /**
     * after adding or deleting parts, call this
     */
    public static void refreshCache(String parentType) throws DBException {
        String dbcontext = DBConnection.DEFAULT_DB_CONTEXT_NAME;
        Part[] oldparts = getParts(parentType);
        for (int i = 0; i < oldparts.length; i++) {
            Part oldpart = oldparts[i];
            dbcontext = oldpart.getDataContext();
            if (oldpart.isOwnedAttribute()) {
                String key = getPartKey(parentType, oldpart.getPartType());
                sAttribHash.remove(key);
            }
        }
        NodeType type = new NodeType(User.getAdmin(dbcontext));
        if (dbcontext != null) {
            type.setDBName(dbcontext);
        }
        type.setEntityName(parentType);
        if (!type.find()) {
            sNodeTypeHash.remove(parentType);
        } else {
            sNodeTypeHash.put(parentType, type);
            Part[] parts = type.getParts();
            sHashOfPartArrays.put(parentType, parts);
            for (int i = 0; i < parts.length; i++) {
                Part part = parts[i];
                if (part.isOwnedAttribute() && (part.getPartType() != null)) {
                    String key = getPartKey(parentType, part.getPartType());
                    if (sAttribHash.get(key) != null) {
                        getLogger().error("unexpectedly overwriting existing, hashed Part with key: " + key);
                    }
                    sAttribHash.put(key, part);
                }
            }
        }
    }

    /**
     * @return a nodetype for the given string, or null if not found
     */
    public static NodeType getEntity(String nodeType) {
        return sNodeTypeHash.get(nodeType);
    }

    /**
     * @return list of REGISTERED NodeType objects.  this may exclude actual DB types
     *         that are not registered with partFactory. this method is more efficent since it uses cache.
     * @see NodeType#getAllTypes for a complete list of DB entries, though it is less efficient than this method
     * @see #addType
     * @see #getFakeEntities
     */
    public static TreeSet<NodeType> getEntities() {
        return new TreeSet<NodeType>(sNodeTypeHash.values());
    }

    /**
     * @return array of fake node types.
     * @see #addFakeType
     */
    public static ArrayList<NodeType> getFakeEntities() {
        return new ArrayList<NodeType>(sFakeTypeHash.values());
    }

    /**
     * convenience for getting logger for current (sub) class
     *
     * @return Category for logging
     */
    public static Category getLogger() {
        if (slog == null) {
            slog = Logger.getLogger(PartsFactory.class.getName());
        }
        return slog;
    }

    /**
     * convenience method to get part from ID; hits db, so not as effective as other methods which retrieve from hash table.
     *
     * @return part with given ID, or null if not found.
     */
    public static Part getPart(int partId) throws DBException {
        Part part = new Part();
        part.setPartId(partId);
        part.find();
        return part;
    }

    /**
     * convenience method to get part from ID; hits db, so not as effective as other methods which retrieve from hash table.
     *
     * @return part with given ID, or null if not found.
     */
    public static Part getPart(String partId) throws DBException {
        Part part = new Part();
        part.setPartId(partId);
        part.find();
        return part;
    }
}
