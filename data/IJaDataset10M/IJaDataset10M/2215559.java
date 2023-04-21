package org.dmd.mvw.client.gxt.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import org.dmd.dmc.DmcClassInfo;
import org.dmd.dmc.DmcHierarchicObjectName;
import org.dmd.dmc.DmcNameResolverIF;
import org.dmd.dmc.DmcNamedObjectIF;
import org.dmd.dmc.DmcObject;
import org.dmd.dmc.DmcObjectName;
import org.dmd.dmc.DmcOmni;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.DmcValueExceptionSet;
import org.dmd.dmp.shared.generated.dmo.DMPEventDMO;
import org.dmd.mvw.client.gxt.dmw.GxtWrapper;
import org.dmd.mvw.client.gxt.dmw.GxtWrapperFactoryIF;
import org.dmd.mvw.client.gxt.dmw.GxtWrapperFactoryManager;

/**
 * The Cache class provides a centralized cache of data retrieved from the server.
 */
public class GxtCache implements DmcNameResolverIF {

    HashMap<DmcObjectName, GxtWrapper> objMap;

    HashMap<DmcClassInfo, IndexInfo> indices;

    HashMap<DmcClassInfo, HierarchyInfo> hierarchies;

    GxtWrapperFactoryManager wrapperFactory;

    public GxtCache() {
        init();
    }

    void init() {
        objMap = new HashMap<DmcObjectName, GxtWrapper>();
        indices = new HashMap<DmcClassInfo, IndexInfo>();
        hierarchies = new HashMap<DmcClassInfo, HierarchyInfo>();
        wrapperFactory = new GxtWrapperFactoryManager();
    }

    /**
	 * Adds the specified factory that can create GxtWrappers for a particular schema.
	 * @param factory the factory to be added.
	 */
    public void addWrapperFactory(GxtWrapperFactoryIF factory) {
        wrapperFactory.addFactory(factory);
    }

    /**
	 * Resets the cache to be empty - generally called when we logout.
	 */
    public void reset() {
        objMap = null;
        indices = null;
        init();
    }

    public GxtWrapper find(DmcObjectName on) {
        return (objMap.get(on));
    }

    public void addIndex(DmcClassInfo dci) {
        if (indices.get(dci) == null) indices.put(dci, new IndexInfo(dci));
    }

    public HashMap<DmcObjectName, GxtWrapper> addIndexListener(DmcClassInfo dci, GxtCacheIndexListenerIF listener) {
        IndexInfo info = indices.get(dci);
        if (info == null) throw (new IllegalStateException("Tried to register listener for non-existent index: " + dci.name));
        info.listeners.add(listener);
        return (info.objects);
    }

    public void removeIndexListener(DmcClassInfo dci, GxtCacheIndexListenerIF listener) {
        IndexInfo info = indices.get(dci);
        if (info == null) throw (new IllegalStateException("Tried to register listener for non-existent index: " + dci.name));
        info.listeners.remove(listener);
    }

    public void addHierarchicIndex(DmcClassInfo dci) {
        if (hierarchies.get(dci) == null) {
            HierarchyInfo index = new HierarchyInfo(dci);
            hierarchies.put(dci, index);
        }
    }

    /**
	 * 
	 * @param dci The class of object that is the root of the hierarchy.
	 * @param listener the listener.
	 * @return The root of the hierarchy is it's available and null otherwise.
	 */
    public GxtWrapper addHierarchyListener(DmcClassInfo dci, GxtCacheHierarchyListenerIF listener) {
        HierarchyInfo info = hierarchies.get(dci);
        if (info == null) throw (new IllegalStateException("Tried to register hierarchy listener for non-existent index: " + dci.name));
        info.addListener(listener);
        return (info.root);
    }

    public void removeHierarchyListener(DmcClassInfo dci, GxtCacheHierarchyListenerIF listener) {
        HierarchyInfo info = hierarchies.get(dci);
        if (info == null) throw (new IllegalStateException("Tried to register listener for non-existent hierarchic index: " + dci.name));
        info.listeners.remove(listener);
    }

    public void hierarchyFetchComplete(DmcClassInfo dci, GxtWrapper root) {
        HierarchyInfo info = hierarchies.get(dci);
        if (info == null) throw (new IllegalStateException("Tried to mark hierarchy complete for non-existent index: " + dci.name));
        info.loadComplete(root);
    }

    public HashMap<DmcObjectName, GxtWrapper> getIndex(DmcClassInfo dci) {
        IndexInfo info = indices.get(dci);
        if (info == null) return (null);
        return (info.objects);
    }

    public GxtWrapper addObject(DmcObject obj) {
        GxtWrapper rc = addObjectInternal(obj);
        IndexInfo info = indices.get(obj.getConstructionClassInfo());
        if (info != null) {
            for (GxtCacheIndexListenerIF listener : info.listeners) listener.objectAdded(info.classInfo, rc, null);
        }
        return (rc);
    }

    private GxtWrapper addObjectInternal(DmcObject obj) {
        GxtWrapper rc = wrapperFactory.wrapObject(obj);
        DmcObjectName on = ((DmcNamedObjectIF) obj).getObjectName();
        if (objMap.get(on) != null) throw (new IllegalStateException("Objects should only be retrieved by one component. Duplicate retrieval of: " + on + " (" + obj.getConstructionClassName() + ")"));
        objMap.put(((DmcNamedObjectIF) obj).getObjectName(), rc);
        IndexInfo info = indices.get(obj.getConstructionClassInfo());
        if (info != null) info.objects.put(on, rc);
        return (rc);
    }

    public GxtWrapper handleHierarchyEvent(DmcClassInfo dci, DMPEventDMO event) {
        GxtWrapper rc = handleEvent(event);
        HierarchyInfo info = hierarchies.get(dci);
        if (info == null) throw (new IllegalStateException("Tried to mark hierarchy complete for non-existent index: " + dci.name));
        info.notifyListeners(rc, event);
        return (rc);
    }

    public GxtWrapper handleEvent(DMPEventDMO event) {
        GxtWrapper rc = null;
        IndexInfo info = null;
        switch(event.getEventTypeDMP()) {
            case CREATED:
                rc = addObjectInternal(event.getSourceObject());
                try {
                    rc.getDmcObject().resolveReferencesExceptClass(this);
                } catch (DmcValueExceptionSet e1) {
                    e1.printStackTrace();
                }
                info = indices.get(rc.getDmcObject().getConstructionClassInfo());
                if (info != null) {
                    for (GxtCacheIndexListenerIF listener : info.listeners) listener.objectAdded(info.classInfo, rc, event);
                }
                break;
            case DELETED:
                rc = deleteObjectInternal(event.getSource().getName());
                if (rc != null) {
                    info = indices.get(rc.getDmcObject().getConstructionClassInfo());
                    if (info != null) {
                        for (GxtCacheIndexListenerIF listener : info.listeners) listener.objectDeleted(info.classInfo, rc, event);
                    }
                }
                break;
            case MODIFIED:
                rc = objMap.get(event.getSource().getName());
                if (rc != null) {
                    try {
                        rc.applyModifierFromEvent(event);
                        rc.getDmcObject().resolveReferencesExceptClass(this);
                        info = indices.get(rc.getDmcObject().getConstructionClassInfo());
                        if (info != null) {
                            for (GxtCacheIndexListenerIF listener : info.listeners) listener.objectModified(info.classInfo, rc, event);
                        }
                    } catch (DmcValueExceptionSet e) {
                        e.printStackTrace();
                    } catch (DmcValueException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return (rc);
    }

    private GxtWrapper deleteObjectInternal(DmcObjectName on) {
        GxtWrapper rc = objMap.get(on);
        if (rc == null) {
        } else {
            rc.getDmcObject().youAreDeleted();
            objMap.remove(on);
            IndexInfo info = indices.get(rc.getDmcObject().getConstructionClassInfo());
            if (info != null) info.objects.remove(on);
        }
        return (rc);
    }

    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("Cache:\n");
        sb.append("  Objects: " + objMap.size() + "\n");
        sb.append("  Indices: " + indices.size() + "\n");
        if (indices.size() > 0) {
            for (DmcClassInfo dci : indices.keySet()) {
                IndexInfo info = indices.get(dci);
                sb.append("    " + info.classInfo.name + ": " + info.objects.size() + "\n");
            }
        }
        return (sb.toString());
    }

    class IndexInfo {

        DmcClassInfo classInfo;

        HashMap<DmcObjectName, GxtWrapper> objects;

        LinkedList<GxtCacheIndexListenerIF> listeners;

        public IndexInfo(DmcClassInfo ci) {
            classInfo = ci;
            objects = new HashMap<DmcObjectName, GxtWrapper>();
            listeners = new LinkedList<GxtCacheIndexListenerIF>();
        }
    }

    class HierarchyInfo {

        DmcClassInfo classInfo;

        HashMap<DmcObjectName, GxtWrapper> objects;

        ArrayList<GxtCacheHierarchyListenerIF> listeners;

        boolean loadComplete;

        GxtWrapper root;

        HierarchyInfo(DmcClassInfo ci) {
            classInfo = ci;
            objects = new HashMap<DmcObjectName, GxtWrapper>();
            listeners = new ArrayList<GxtCacheHierarchyListenerIF>();
            loadComplete = false;
        }

        void addListener(GxtCacheHierarchyListenerIF listener) {
            listeners.add(listener);
        }

        void loadComplete(GxtWrapper r) {
            loadComplete = true;
            root = r;
            for (GxtCacheHierarchyListenerIF listener : listeners) {
                listener.hierarchyAvailable(classInfo, root);
            }
        }

        void notifyListeners(GxtWrapper gxt, DMPEventDMO event) {
            for (GxtCacheHierarchyListenerIF listener : listeners) {
                switch(event.getEventTypeDMP()) {
                    case CREATED:
                        listener.hierarchyObjectAdded(classInfo, gxt, event);
                        break;
                    case DELETED:
                        listener.hierarchyObjectDeleted(classInfo, gxt, event);
                        break;
                    case MODIFIED:
                        listener.hierarchyObjectModified(classInfo, gxt, event);
                        break;
                }
            }
        }
    }

    public void performInitialResolution() {
        System.out.println("Objects to resolve: " + objMap.size());
        int i = 1;
        for (GxtWrapper wrapper : objMap.values()) {
            DmcNamedObjectIF obj = (DmcNamedObjectIF) wrapper.getDmcObject();
            try {
                System.out.println("Resolving (" + i + "): " + obj.getObjectName().getNameString() + " " + wrapper.getClass().getName());
                i++;
                if (wrapper.getDmcObject().getConstructionClassName().equals("PepConfigProfile")) System.out.println("HERE HERE");
                wrapper.getDmcObject().resolveReferencesExceptClass(this);
            } catch (DmcValueExceptionSet e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public DmcObject findNamedDMO(DmcObjectName name) {
        GxtWrapper wrapper = objMap.get(name);
        if (wrapper == null) {
            return (null);
        }
        return (wrapper.getDmcObject());
    }

    @Override
    public DmcNamedObjectIF findNamedObject(DmcObjectName name) {
        System.out.println("Finding: " + name.getNameString());
        GxtWrapper wrapper = objMap.get(name);
        if (wrapper == null) return (null);
        return (DmcNamedObjectIF) (wrapper.getDmcObject());
    }

    @Override
    public DmcNamedObjectIF findNamedObject(DmcObjectName name, int attributeID) {
        return (findNamedObject(name));
    }
}
