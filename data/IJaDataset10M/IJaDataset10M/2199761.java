package xutools.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Stores all object types available.
 * 
 * @author Tobias Weigel
 * @date 09.10.2008
 * 
 */
public final class ObjectTypeLibrary {

    public static ObjectType SUN;

    public static ObjectType PLANET;

    public static ObjectType ASTEROID;

    public static ObjectType TRADING_STATION;

    public static ObjectType EQUIPMENT_DOCK;

    public static ObjectType GATE;

    public static ObjectType PIER;

    public static ObjectType SOLAR_POWER_PLANT;

    public static ObjectType SHIPYARD;

    public static ObjectType KHAAK_STATION;

    public static ObjectType PIRATE_BASE;

    public static ObjectType NEBULA;

    public static ObjectType SQUASH_MINE;

    /**
	 * Hashes object type id's to object types.
	 */
    private HashMap<String, ObjectType> types = new HashMap<String, ObjectType>();

    /**
	 * Creates a library from a given XML document containing definitions.
	 * 
	 * @param stationInfo
	 *            XML document
	 * @throws Exception
	 */
    public ObjectTypeLibrary(Document stationInfo) throws Exception {
        loadTypesFromXML(stationInfo);
        assignConstants();
    }

    /**
	 * Finds the object type with given id (i.e. internal id, not to be confused
	 * with x2id).
	 * 
	 * @param id
	 * @return ObjectType or null if no such object exists.
	 */
    public ObjectType getByID(String id) {
        return types.get(id.toUpperCase());
    }

    /**
	 * Returns a list of object types for given IDs.
	 * 
	 * @param ids
	 * @return List<ObjectType>
	 */
    public List<ObjectType> getByID(Collection<String> ids) {
        ArrayList<ObjectType> list = new ArrayList<ObjectType>(ids.size());
        for (String s : ids) {
            list.add(types.get(s.toUpperCase()));
        }
        return list;
    }

    /**
	 * Returns a list of object types for given IDs.
	 * 
	 * @param ids
	 * @return List<ObjectType>
	 */
    public List<ObjectType> getByID(String[] ids) {
        ArrayList<ObjectType> list = new ArrayList<ObjectType>(ids.length);
        for (String s : ids) {
            ObjectType ot = types.get(s.toUpperCase());
            if (ot == null) throw new IllegalArgumentException("Unknown ObjectType: '" + s + "'!");
            list.add(ot);
        }
        return list;
    }

    private void loadTypesFromXML(Document doc) throws Exception {
        NodeList rootList = doc.getElementsByTagName("objectTypes");
        for (int rootIndex = 0; rootIndex < rootList.getLength(); rootIndex++) {
            Node rootNode = rootList.item(rootIndex);
            Node elemNode = rootNode.getFirstChild();
            while (elemNode != null) {
                if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                    if (!elemNode.getNodeName().equalsIgnoreCase("type")) {
                        throw new Exception("Invalid element: " + elemNode.getNodeName());
                    }
                    ObjectType objectType = ObjectType.createFromXML(this, elemNode);
                    types.put(objectType.getID(), objectType);
                }
                elemNode = elemNode.getNextSibling();
            }
        }
    }

    private void assignConstants() throws Exception {
        SUN = getByID("sun");
        PLANET = getByID("planet");
        ASTEROID = getByID("asteroid");
        TRADING_STATION = getByID("trading_station");
        EQUIPMENT_DOCK = getByID("equipment_dock");
        GATE = getByID("gate");
        PIER = getByID("pier");
        SOLAR_POWER_PLANT = getByID("solar_power_plant");
        SHIPYARD = getByID("shipyard");
        KHAAK_STATION = getByID("khaak_station");
        PIRATE_BASE = getByID("pirate_base");
        NEBULA = getByID("nebula");
        SQUASH_MINE = getByID("squash_mine");
        if (SUN == null) throw new Exception("Object type for essential constant SUN not found!");
        if (PLANET == null) throw new Exception("Object type for essential constant PLANET not found!");
        if (ASTEROID == null) throw new Exception("Object type for essential constant ASTEROID not found!");
        if (TRADING_STATION == null) throw new Exception("Object type for essential constant TRADING_STATION not found!");
        if (EQUIPMENT_DOCK == null) throw new Exception("Object type for essential constant EQUIPMENT_DOCK not found!");
        if (GATE == null) throw new Exception("Object type for essential constant GATE not found!");
        if (PIER == null) throw new Exception("Object type for essential constant PIER not found!");
        if (SHIPYARD == null) throw new Exception("Object type for essential constant SHIPYARD not found!");
        if (SOLAR_POWER_PLANT == null) throw new Exception("Object type for essential constant SOLAR_POWER_PLANT not found!");
        if (KHAAK_STATION == null) throw new Exception("Object type for essential constant KHAAK_STATION not found!");
        if (PIRATE_BASE == null) throw new Exception("Object type for essential constant PIRATE_BASE not found!");
        if (NEBULA == null) throw new Exception("Object type for essential constant NEBULA not found!");
        if (SQUASH_MINE == null) throw new Exception("Object type for essential constant SQUASH_MINE not found!");
    }

    /**
	 * Returns all object types with given classification string (ignores case).
	 * 
	 * @param classification
	 * @return Collection<ObjectType>
	 */
    public Collection<ObjectType> getByClassification(String classification) {
        Vector<ObjectType> vec = new Vector<ObjectType>();
        for (ObjectType ot : types.values()) {
            if (ot.getClassification().equalsIgnoreCase(classification)) vec.add(ot);
        }
        return vec;
    }

    /**
	 * Returns an iterator over all registered object types.
	 * 
	 * @return iterator
	 */
    public Iterator<ObjectType> iterateAll() {
        return types.values().iterator();
    }
}
