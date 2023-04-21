package org.openscada.opc.lib.ae.browser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.openscada.opc.lib.da.browser.TreeBrowser;

/**
 *
 */
public class Area {

    private Area _parent = null;

    private String _name = null;

    private String _qualifiedName = null;

    private final List<Area> _areas = new ArrayList<Area>();

    private final List<Source> _sources = new ArrayList<Source>();

    /**
     * Create a source to the virtual root folder
     */
    public Area() {
        super();
    }

    /**
     * Create an area with a parent area and a name of this area.
     * @param parent The parent of this area
     * @param name The name of this area
     */
    public Area(Area parent, String name) {
        super();
        _name = name;
        _parent = parent;
    }

    public Area(Area parent, String name, String qualifiedName) {
        super();
        _name = name;
        _qualifiedName = qualifiedName;
        _parent = parent;
    }

    /**
     * Get all areas.
     * <br/>
     * They must be filled first with a fill method from the {@link TreeBrowser}
     * @return The list of sources
     */
    public List<Area> getAreas() {
        return _areas;
    }

    public void setAreas(List<Area> areas) {
        _areas.clear();
        _areas.addAll(areas);
    }

    /**
     * Get all sources.
     * <br/>
     * @return The list of sources
     */
    public List<Source> getSources() {
        return _sources;
    }

    public void setSources(List<Source> sources) {
        _sources.clear();
        _sources.addAll(sources);
    }

    public String getName() {
        return _name;
    }

    public String getQualifiedName() {
        return _qualifiedName;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setQualifiedName(String qualifiedName) {
        _qualifiedName = qualifiedName;
    }

    public Area getParent() {
        return _parent;
    }

    /**
     * Get the list of names from the parent up to this source
     * @return The stack of source names from the parent up this one
     */
    public Collection<String> getAreaStack() {
        LinkedList<String> areas = new LinkedList<String>();
        Area currentArea = this;
        while (currentArea.getParent() != null) {
            areas.add(currentArea.getName());
            currentArea = currentArea.getParent();
        }
        Collections.reverse(areas);
        return areas;
    }
}
