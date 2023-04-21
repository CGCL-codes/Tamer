package net.sourceforge.mapcraft.map;

import java.util.*;
import net.sourceforge.mapcraft.map.elements.Area;
import net.sourceforge.mapcraft.map.elements.Terrain;

/**
 * A set of areas for a map. An area is a named region on the map. Each
 * tile can belong to zero or one regions. Borders between regions are
 * displayed on the map as red lines.
 * <br/>
 * A map will have exactly one AreaSet. All TileSets within a map share
 * the same AreaSet.
 */
public class AreaSet implements Cloneable {

    protected ArrayList list;

    /**
	 * Create a new, empty, AreaSet.
	 */
    public AreaSet() {
        list = new ArrayList();
    }

    /**
	 * Create a clone of this AreaSet.
	 */
    public Object clone() throws CloneNotSupportedException {
        AreaSet a = new AreaSet();
        a.list = (ArrayList) list.clone();
        return (Object) a;
    }

    /**
	 * The number of defined areas.
	 * @return	Size of the area set.
	 */
    public int size() {
        return list.size();
    }

    /**
	 * Obtain an iterator over all the areas in the AreaSet.
	 * 
	 * @return	Return an iterator over Area objects.
	 */
    public Iterator iterator() {
        return list.iterator();
    }

    /**
     * Get the next unused id for a new area. Start at 1, and search until
     * an id is found which does not exist.
     * 
     * @return  Next available id to be used.
     */
    private int getNextId() {
        int id = 0;
        for (id = 1; id < 64; id++) {
            Area a = getArea(id);
            if (a == null) {
                break;
            }
        }
        return id;
    }

    /**
	 * Add a new Area to the AreaSet. The Area will be appended to the end
	 * of the list. If the id of the new area is zero or less, then a new
     * (unique) id will be automatically assigned. If the id matches an
     * existing area, then the old area is replaced with the new one.
     * 
	 * @param area		Area to be added.
	 */
    public void add(Area area) {
        int id = area.getId();
        Area a = getArea(id);
        if (id < 1) {
            id = getNextId();
            area.setId(id);
            list.add(area);
        } else if (a != null) {
            a.setName(area.getName());
            a.setUri(area.getUri());
            a.setParent(area.getParent());
        } else {
            list.add(area);
        }
    }

    public void add(int id, String name, String uri) {
        Area a = new Area(id, name, uri);
        list.add(a);
    }

    public void add(int id, String name, String uri, Area parent) {
        Area a = new Area(id, name, uri, parent);
        list.add(a);
    }

    public Area getArea(String name) {
        Iterator it = list.iterator();
        Area a = null;
        boolean found = false;
        while (it.hasNext()) {
            a = (Area) it.next();
            if (a.getName().equalsIgnoreCase(name)) {
                found = true;
                break;
            }
        }
        if (!found) {
            a = null;
        }
        return a;
    }

    public Area getArea(int id) {
        Iterator it = list.iterator();
        Area a = null;
        boolean found = false;
        while (it.hasNext()) {
            a = (Area) it.next();
            if (a.getId() == id) {
                found = true;
                break;
            }
        }
        if (!found) {
            a = null;
        }
        return a;
    }

    public Area[] toArray() {
        Area[] array = new Area[list.size()];
        Iterator iter = iterator();
        int i = 0;
        while (iter.hasNext()) {
            Area a = (Area) iter.next();
            array[i++] = a;
        }
        return array;
    }

    public String[] toNameArray() {
        String[] names = new String[list.size()];
        Iterator iter = iterator();
        int i = 0;
        while (iter.hasNext()) {
            Area a = (Area) iter.next();
            names[i++] = a.getName();
        }
        return names;
    }

    /**
     * Needed so that we can use the general Pane class for displaying the
     * areas. Just pretend to be terrain.
     */
    public Terrain[] toTerrainArray() {
        Terrain[] array = new Terrain[list.size() + 1];
        Iterator iter = iterator();
        int i = 0;
        array[i++] = new Terrain((short) 0, "Undefined", "Undefined", null);
        while (iter.hasNext()) {
            Area a = (Area) iter.next();
            array[i++] = new Terrain((short) a.getId(), a.getName(), a.getName(), null);
        }
        return array;
    }

    /**
     * Delete the specified area from the list of areas.
     * 
     * @param id        Id of area to be removed.
     */
    public void deleteArea(Area area) {
        for (int i = 0; i < list.size(); i++) {
            Area a = (Area) list.get(i);
            if (area.equals(a)) {
                list.remove(i);
                break;
            }
        }
    }
}
