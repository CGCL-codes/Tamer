package org.ncgr.cmtv.isys;

import org.ncgr.cmtv.*;
import org.ncgr.cmtv.datamodel.*;
import org.ncgr.cmtv.datamodel.impl.DefaultMapModel;
import org.ncgr.cmtv.datamodel.impl.ConsensusMap;
import org.ncgr.cmtv.datamodel.impl.InversePWA;
import org.ncgr.cmtv.geometry.*;
import org.ncgr.cmtv.geometry.impl.InterMapCoordinateConversionManager;
import org.ncgr.cmtv.geometry.impl.MapCombinabilityManager;
import org.ncgr.isys.system.*;
import org.ncgr.isys.system.event.*;
import org.ncgr.isys.objectmodel.LinearObject;
import org.ncgr.isys.objectmodel.LinearlyLocatedObject;
import org.ncgr.isys.objectmodel.PairwiseAlignment;
import org.ncgr.isys.service.RetrieveAnnotatedLinearObjectService;
import org.ncgr.isys.service.AnnotatedLinearObject;
import org.ncgr.isys.service.TableViewerService;
import org.ncgr.isys.service.MapListing;
import org.ncgr.isys.objectmodel.LinearObjectDistribution;
import org.ncgr.isys.util.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MapInternalFrameClient implements Client, MapModelListener {

    static final Isys isys = Isys.getInstance();

    MapModel mapModel;

    MapInternalFrame viewer;

    CompMapViewerWrapper wrapper;

    public MapInternalFrameClient(MapInternalFrame mif, CompMapViewerWrapper wrapper) {
        this.viewer = mif;
        this.wrapper = wrapper;
        mapModel = mif.getMapModel();
        mapModel.addMapModelListener(this);
        addAttributesToLinearObjectDistributions();
        GeneralItemEventListener generalEventHandler = new GeneralItemEventListener();
        isys.addEventListener(ItemSelectedEvent.class, generalEventHandler, this);
        isys.addEventListener(ItemDeselectedEvent.class, generalEventHandler, this);
        isys.addEventListener(ItemShownEvent.class, generalEventHandler, this);
        isys.addEventListener(ItemHiddenEvent.class, generalEventHandler, this);
        DeselectAllListener deselectAllHandler = new DeselectAllListener();
        isys.addEventListener(DeselectAllEvent.class, deselectAllHandler, this);
        ItemAddedListener itemAddedListener = new ItemAddedListener();
        isys.addEventListener(ItemAddedEvent.class, itemAddedListener, this);
        JPopupMenu popup = viewer.getPopupMenu(Collections.EMPTY_SET);
        popup.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                java.util.Collection data = new java.util.ArrayList(mapModel.getSelectedObjects().size());
                for (Iterator itr = mapModel.getSelectedObjects().iterator(); itr.hasNext(); ) {
                    Object o = itr.next();
                    Collection objreps = null;
                    if (o instanceof LinearObject) objreps = mapModel.getObjectRepresentationsForMap((LinearObject) o); else if (o instanceof LinearlyLocatedObject) objreps = mapModel.getObjectRepresentationsForMappedObject((LinearlyLocatedObject) o);
                    if (objreps == null) continue;
                    for (Iterator itr2 = objreps.iterator(); itr2.hasNext(); ) {
                        Object obj = itr2.next();
                        if (obj instanceof IsysObject) data.add(obj);
                    }
                }
                Action[] isysActions = IsysActionFactory.getAllIsysActions(new DefaultIsysObjectCollection(data), new Client[] { MapInternalFrameClient.this }, null);
                if (isysActions.length == 0) return;
                JPopupMenu popup = (JPopupMenu) e.getSource();
                popup.addSeparator();
                for (int i = 0; i < isysActions.length; i++) popup.add(isysActions[i]);
                popup.pack();
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        installMenuItems();
    }

    private Set lodsAugmented = new HashSet();

    private boolean isLODAugmented(LinearObjectDistribution lod) {
        if (lodsAugmented.contains(lod)) {
            return true;
        }
        return false;
    }

    private void setLODAugmented(LinearObjectDistribution lod, boolean augmented) {
        if (augmented) {
            lodsAugmented.add(lod);
        } else {
            lodsAugmented.remove(lod);
        }
    }

    private void addAttributesToLinearObjectDistributions() {
        MutableIsysObject mio = null;
        LinearObjectDistribution lod = null;
        Object mappedObj = null;
        Collection mapObjs = null;
        Object obj = null;
        for (Iterator itr = mapModel.getAllMappedObjects().iterator(); itr.hasNext(); ) {
            mappedObj = itr.next();
            if (mappedObj instanceof LinearObjectDistribution) {
                lod = (LinearObjectDistribution) mappedObj;
                if (isLODAugmented(lod)) {
                    continue;
                }
                mapObjs = mapModel.getObjectRepresentationsForMappedObject(lod);
                for (Iterator itr2 = mapObjs.iterator(); itr2.hasNext(); ) {
                    obj = itr2.next();
                    if (obj instanceof IsysObject) {
                        IsysObject localIO = new AugmentedIsysObject((IsysObject) obj, new IsysAttribute[] { new MinMaxValueInRangeAttribute(viewer, lod) });
                        mapModel.addObjectRepresentationForMappedObject(lod, localIO);
                        setLODAugmented(lod, true);
                        break;
                    }
                }
            }
        }
    }

    private void addAttributesToLinearObjectDistributions(Collection mappedObjects) {
        addAttributesToLinearObjectDistributions();
    }

    public static final String FIND_COMPARABLE_MAPS_REPOSITORY_MENU_NAME = "Find comparable maps from repository";

    public static final String FIND_COMBINABLE_MAPS_MENU_NAME = "Find combinable maps in memory";

    public static final String FIND_COMBINABLE_MAPS_REPOSITORY_MENU_NAME = "Find combinable maps from repository";

    protected void installMenuItems() {
        JMenuBar menu = viewer.getJMenuBar();
        JMenu mapCompMenu = null;
        for (int i = 0; i < menu.getMenuCount(); ++i) {
            JMenu topLevelMenuItem = menu.getMenu(i);
            if (topLevelMenuItem.getText().equals(MapInternalFrame.MAP_COMPARISON_MENU_NAME)) {
                mapCompMenu = topLevelMenuItem;
            }
        }
        if (mapCompMenu == null) {
            JOptionPane.showMessageDialog(viewer, "", "Could not find " + MapInternalFrame.MAP_COMPARISON_MENU_NAME + " menu item. " + FIND_COMBINABLE_MAPS_MENU_NAME + " menu option will not be available", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JMenuItem mi = new JMenuItem(FIND_COMBINABLE_MAPS_MENU_NAME);
        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MapModel givenModel = wrapper.getGlobalMapModel();
                if (givenModel.isReference(viewer.getPrimaryMap())) {
                    JOptionPane.showMessageDialog(viewer, "Combine maps in memory functionality not yet implemented for reference maps; try Combine maps from repository instead");
                    return;
                }
                MapModel combinableModel = getCombinableMaps(viewer.getPrimaryMap(), givenModel);
                combineMaps(null, combinableModel);
            }
        });
        mapCompMenu.add(mi);
        java.util.Collection services;
        services = isys.getAllServices(MapListing.class);
        if (services != null && services.size() > 0) {
            JMenu compareToRepositoryMenu = new JMenu(FIND_COMPARABLE_MAPS_REPOSITORY_MENU_NAME);
            for (Iterator itr = services.iterator(); itr.hasNext(); ) {
                final MapListing svc = (MapListing) itr.next();
                mi = new JMenuItem(svc.getDisplayName());
                mi.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {

                            public void run() {
                                org.ncgr.cmtv.services.MapComparisonAcceptanceStrategy acceptanceStrategy = wrapper.getViewer().promptForMapComparisonAcceptanceStrategy();
                                if (acceptanceStrategy == null) {
                                    return;
                                }
                                MapComparisonModel[] comparisons = org.ncgr.cmtv.CompareToRepositoryMaps.makeComparisonsForMap(wrapper.getViewer(), viewer.getPrimaryMap(), svc, acceptanceStrategy);
                                if (comparisons == null) {
                                    return;
                                }
                                for (int i = 0; i < comparisons.length; i++) {
                                    wrapper.handleRetrievedMaps(comparisons[i].getMapsCompared(), svc, false);
                                    wrapper.getViewer().addMapComparisonModel(comparisons[i]);
                                }
                            }
                        }).start();
                    }
                });
                compareToRepositoryMenu.add(mi);
            }
            if (compareToRepositoryMenu.getItemCount() > 0) mapCompMenu.add(compareToRepositoryMenu);
            JMenu repositoryMenu = new JMenu(FIND_COMBINABLE_MAPS_REPOSITORY_MENU_NAME);
            for (Iterator itr = services.iterator(); itr.hasNext(); ) {
                final MapListing svc = (MapListing) itr.next();
                mi = new JMenuItem(svc.getDisplayName());
                mi.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {

                            public void run() {
                                MapModel givenModel = getMapMetadataFromRepository(svc);
                                if (givenModel == null) {
                                    return;
                                }
                                givenModel.addMaps(Collections.singletonMap(viewer.getPrimaryMap(), wrapper.getGlobalMapModel().getObjectRepresentationsForMap(viewer.getPrimaryMap())));
                                MapModel combinableModel = getCombinableMaps(viewer.getPrimaryMap(), givenModel);
                                combineMaps(svc, combinableModel);
                            }
                        }).start();
                    }
                });
                repositoryMenu.add(mi);
            }
            if (repositoryMenu.getItemCount() > 0) mapCompMenu.add(repositoryMenu);
        }
    }

    public void mapsAdded(Collection maps) {
    }

    public void mapsRemoved(Collection maps) {
    }

    public void mappedObjectsAdded(Collection mappedObjects) {
        addAttributesToLinearObjectDistributions(mappedObjects);
    }

    public void mappedObjectsRemoved(Collection mappedObjects) {
    }

    public void selectionStateChanged(Collection changedObjects, boolean selected) {
        java.util.Collection ios = new java.util.ArrayList(changedObjects.size());
        for (Iterator itr = changedObjects.iterator(); itr.hasNext(); ) {
            Object obj = itr.next();
            Collection objreps = null;
            if (obj instanceof LinearlyLocatedObject) objreps = mapModel.getObjectRepresentationsForMappedObject((LinearlyLocatedObject) obj); else objreps = mapModel.getObjectRepresentationsForMap((LinearObject) obj);
            for (Iterator itr2 = objreps.iterator(); itr2.hasNext(); ) ios.add(itr2.next());
        }
        IsysObjectCollection eventData = new DefaultIsysObjectCollection(ios);
        if (selected) isys.fireEvent(new ItemSelectedEvent(eventData), this); else isys.fireEvent(new ItemDeselectedEvent(eventData), this);
    }

    public void selectionCleared() {
        isys.fireEvent(new DeselectAllEvent(), this);
    }

    public void visibilityStateChanged(Collection changedObjects, boolean visible) {
        java.util.Collection ios = new java.util.ArrayList(changedObjects.size());
        for (Iterator itr = changedObjects.iterator(); itr.hasNext(); ) {
            Object obj = itr.next();
            Collection objreps = null;
            if (obj instanceof LinearlyLocatedObject) objreps = mapModel.getObjectRepresentationsForMappedObject((LinearlyLocatedObject) obj); else objreps = mapModel.getObjectRepresentationsForMap((LinearObject) obj);
            for (Iterator itr2 = objreps.iterator(); itr2.hasNext(); ) ios.add(itr2.next());
        }
        IsysObjectCollection eventData = new DefaultIsysObjectCollection(ios);
        if (visible) isys.fireEvent(new ItemShownEvent(eventData), this); else isys.fireEvent(new ItemHiddenEvent(eventData), this);
    }

    public void setVisible(boolean b) {
        viewer.setVisible(b);
    }

    class GeneralItemEventListener implements IsysEventListener {

        public void handleEvent(IsysEvent e) {
            IsysObjectCollection eventData = e.getData();
            Collection myEquivalentObjs = new HashSet(eventData.size());
            for (Iterator itr = wrapper.equiAttrHashes.keySet().iterator(); itr.hasNext(); ) {
                Class equiAttrClass = (Class) itr.next();
                java.util.Collection incomingAttrs = eventData.getAttributeInstances(equiAttrClass);
                for (java.util.Iterator itr2 = incomingAttrs.iterator(); itr2.hasNext(); ) {
                    IsysAttribute incomingAttr = (IsysAttribute) itr2.next();
                    java.util.Collection myMatches = org.ncgr.isys.system.DataModelRegistry.getByAttributeValue((java.util.Map) wrapper.equiAttrHashes.get(equiAttrClass), equiAttrClass, incomingAttr);
                    for (java.util.Iterator itr3 = myMatches.iterator(); itr3.hasNext(); ) {
                        IsysObject io = (IsysObject) itr3.next();
                        for (java.util.Iterator itr4 = io.getAttribute(LinearObject.class).iterator(); itr4.hasNext(); ) myEquivalentObjs.add(itr4.next());
                        for (java.util.Iterator itr4 = io.getAttribute(LinearlyLocatedObject.class).iterator(); itr4.hasNext(); ) myEquivalentObjs.add(itr4.next());
                    }
                }
            }
            for (java.util.Iterator itr = eventData.iterator(); itr.hasNext(); ) {
                IsysObject io = (IsysObject) itr.next();
                if (wrapper.allIOs.contains(io)) {
                    for (java.util.Iterator itr4 = io.getAttribute(LinearObject.class).iterator(); itr4.hasNext(); ) myEquivalentObjs.add(itr4.next());
                    for (java.util.Iterator itr4 = io.getAttribute(LinearlyLocatedObject.class).iterator(); itr4.hasNext(); ) {
                        Object o = itr4.next();
                        myEquivalentObjs.add(o);
                        if (o instanceof PairwiseAlignment) myEquivalentObjs.add(new InversePWA((PairwiseAlignment) o));
                    }
                }
            }
            if (e instanceof ItemSelectedEvent) mapModel.setSelectionState(myEquivalentObjs, true); else if (e instanceof ItemDeselectedEvent) mapModel.setSelectionState(myEquivalentObjs, false); else if (e instanceof ItemShownEvent) mapModel.setVisibilityState(myEquivalentObjs, true); else if (e instanceof ItemHiddenEvent) mapModel.setVisibilityState(myEquivalentObjs, false);
        }
    }

    class DeselectAllListener implements IsysEventListener {

        public void handleEvent(IsysEvent e) {
            mapModel.clearSelection();
        }
    }

    class ItemAddedListener implements IsysEventListener {

        public void handleEvent(IsysEvent e) {
            IsysObjectCollection eventData = e.getData();
            java.util.Collection iosToAdd = eventData.getObjectsWithAttributes(new Class[] { LinearObject.class });
            wrapper.addLinearObjects(iosToAdd, true);
            iosToAdd = eventData.getObjectsWithAttributes(new Class[] { LinearlyLocatedObject.class });
            wrapper.addLinearlyLocatedObjects(iosToAdd, true);
        }
    }

    class ItemRemovedListener implements IsysEventListener {

        public void handleEvent(IsysEvent e) {
            IsysObjectCollection eventData = e.getData();
            Collection remove = new ArrayList(eventData.size());
            for (java.util.Iterator itr = eventData.iterator(); itr.hasNext(); ) {
                IsysObject io = (IsysObject) itr.next();
                if (wrapper.addedIOs.contains(io)) {
                    for (java.util.Iterator itr2 = io.getAttribute(LinearlyLocatedObject.class).iterator(); itr2.hasNext(); ) remove.add(itr2.next());
                    for (java.util.Iterator itr2 = io.getAttribute(LinearObject.class).iterator(); itr2.hasNext(); ) remove.add(itr2.next());
                }
            }
            wrapper.mapModel.removeMappedObjects(remove);
            wrapper.mapModel.removeMaps(remove);
        }
    }

    private void combineMaps(final MapListing mapListingSvc, MapModel givenMapModel) {
        Collection maps = givenMapModel.getMapsWithMappedObjects().keySet();
        if (maps == null || maps.size() == 0) {
            JOptionPane.showMessageDialog(viewer, "No combinable maps found");
            return;
        }
        Collection goodMaps = new ArrayList(maps.size());
        Collection badMaps = new ArrayList(maps.size());
        for (Iterator itr = maps.iterator(); itr.hasNext(); ) {
            LinearObject map = (LinearObject) itr.next();
            boolean isGood = true;
            Collection mappedObjects = givenMapModel.getMappedObjectsForMap(map);
            for (Iterator itr2 = mappedObjects.iterator(); itr2.hasNext(); ) {
                LinearlyLocatedObject mappedObject = (LinearlyLocatedObject) itr2.next();
                if (mapModel.containsMappedObject(mappedObject)) {
                    isGood = false;
                    break;
                }
            }
            if (isGood) {
                goodMaps.add(map);
            } else {
                badMaps.add(map);
            }
        }
        if (goodMaps.size() == 0) {
            JOptionPane.showMessageDialog(viewer, "No combinable maps found");
            return;
        }
        givenMapModel.removeMaps(badMaps);
        badMaps.clear();
        goodMaps.clear();
        DistributionThresholdFilter thresholdFilter = viewer.createDistributionThresholdFilter();
        if (thresholdFilter != null) {
            if (mapListingSvc != null) {
                givenMapModel = doFullRetrieve(getIsysObjectsForMaps(givenMapModel), mapListingSvc);
                maps = givenMapModel.getMapsWithMappedObjects().keySet();
            }
            for (Iterator itr = maps.iterator(); itr.hasNext(); ) {
                LinearObject map = (LinearObject) itr.next();
                boolean isGood = false;
                Collection mappedObjects = givenMapModel.getMappedObjectsForMap(map);
                for (Iterator itr2 = mappedObjects.iterator(); itr2.hasNext(); ) {
                    LinearlyLocatedObject mappedObject = (LinearlyLocatedObject) itr2.next();
                    if (thresholdFilter.isIncluded(mappedObject)) {
                        isGood = true;
                        break;
                    }
                }
                if (isGood) {
                    goodMaps.add(map);
                } else {
                    badMaps.add(map);
                }
            }
            maps = goodMaps;
            givenMapModel.removeMaps(badMaps);
        }
        TableViewerService tv = (TableViewerService) Isys.getInstance().getService(TableViewerService.class);
        if (tv == null) {
            JOptionPane.showMessageDialog(viewer, "No TableViewer service available");
            return;
        }
        class TVResultHandler implements AsynchronousCallback {

            private MapModel givenMapModel;

            TVResultHandler(MapModel givenMapModel) {
                this.givenMapModel = givenMapModel;
            }

            public void returnAsynchronousResult(Object data, AsynchronousService svc) {
                if (data == null) {
                    return;
                }
                IsysObjectCollection ioc = (IsysObjectCollection) data;
                Collection maps;
                if (mapListingSvc != null) {
                    givenMapModel = doFullRetrieve(ioc, mapListingSvc);
                    maps = givenMapModel.getMapsWithMappedObjects().keySet();
                } else {
                    maps = ioc.getAttributeInstances(LinearObject.class);
                }
                Map allMos = new HashMap();
                Iterator it = maps.iterator();
                while (it.hasNext()) {
                    LinearObject map = (LinearObject) it.next();
                    Collection mos = givenMapModel.getMappedObjectsForMap(map);
                    for (Iterator itr2 = mos.iterator(); itr2.hasNext(); ) {
                        LinearlyLocatedObject mo = (LinearlyLocatedObject) itr2.next();
                        allMos.put(mo, givenMapModel.getObjectRepresentationsForMappedObject(mo));
                    }
                    if (mapModel.isReference(viewer.getPrimaryMap())) {
                        LinearObject referenceMap = viewer.getPrimaryMap();
                        InterMapCoordinateConversionManager mgr = wrapper.viewer.getInterMapCoordinateConversionManager();
                        MapToMapCoordinateConverter m2mcc = mgr.getCoordinateConverterFromSourceToTarget(map, referenceMap);
                        Collection basisMappedObjects = mgr.getBasisMappedObjectsForCoordinateConverter(m2mcc);
                        if (basisMappedObjects != null) mapModel.addMappedObjects(basisMappedObjects);
                    }
                }
                mapModel.addMappedObjects(allMos);
            }
        }
        ;
        IsysObjectCollection ioc = new DefaultIsysObjectCollection(maps);
        if (tv != null) {
            tv.executeModal("Listing of combinable maps", "Select maps for full retrieval", ioc, null, new TVResultHandler(givenMapModel));
        }
    }

    private MapModel getCombinableMaps(LinearObject targetMap, MapModel givenModel) {
        MapModel retval = new DefaultMapModel();
        Collection combinable;
        MapCombinabilityManager mapCombiner = wrapper.getMapCombinabilityManager();
        if (targetMap instanceof ConsensusMap) {
            LinearObject[] inputMaps = ((ConsensusMap) targetMap).getInputMaps();
            Collection removeAfter = new ArrayList(inputMaps.length);
            for (int i = 0; i < inputMaps.length; i++) {
                if (!givenModel.containsMap(inputMaps[i])) {
                    givenModel.addMaps(Collections.singletonMap(inputMaps[i], wrapper.getGlobalMapModel().getObjectRepresentationsForMap(inputMaps[i])));
                    removeAfter.add(inputMaps[i]);
                }
            }
            Map mapping = mapCombiner.findCombinableMaps((ConsensusMap) targetMap, givenModel);
            combinable = mapping.keySet();
            combinable.removeAll(removeAfter);
        } else if (wrapper.mapModel.isReference(targetMap)) {
            combinable = mapCombiner.findCombinableMapsForReferenceMap(targetMap, givenModel, wrapper.viewer.getMapComparisonModels(), wrapper.getGlobalMapModel());
        } else {
            combinable = mapCombiner.findCombinableMaps(targetMap, givenModel);
        }
        for (Iterator itr = combinable.iterator(); itr.hasNext(); ) {
            LinearObject map = (LinearObject) itr.next();
            Collection objReps = givenModel.getObjectRepresentationsForMap(map);
            retval.addMaps(Collections.singletonMap(map, objReps));
            Collection mappedObjects = givenModel.getMappedObjectsForMap(map);
            for (Iterator itr2 = mappedObjects.iterator(); itr2.hasNext(); ) {
                LinearlyLocatedObject mappedObject = (LinearlyLocatedObject) itr2.next();
                retval.addMappedObjects(Collections.singletonMap(mappedObject, givenModel.getObjectRepresentationsForMappedObject(mappedObject)));
            }
        }
        return retval;
    }

    /**
         * This method constructs a MapModel that contains only the metadata 
         * needed to determine "combinability" (assuming that the MapListing
         * service it invokes includes this information)
         */
    private MapModel getMapMetadataFromRepository(MapListing mapListingSvc) {
        final MapModel mapMetadata = new DefaultMapModel();
        IsysObjectCollection ioc = mapListingSvc.getListOfAvailableData(null);
        if (ioc == null) {
            return null;
        }
        populateMapModelFromIsysObjects(mapMetadata, ioc.getObjectsWithAttributes(new Class[] { IsysAttribute.class }));
        return mapMetadata;
    }

    private IsysObjectCollection getIsysObjectsForMaps(MapModel givenModel) {
        Collection toRetrieveMaps = givenModel.getMapsWithMappedObjects().keySet();
        java.util.Collection toRetrieveIsysObjects = new java.util.ArrayList(toRetrieveMaps.size());
        for (Iterator itr = toRetrieveMaps.iterator(); itr.hasNext(); ) {
            Collection objReps = givenModel.getObjectRepresentationsForMap((LinearObject) itr.next());
            for (Iterator itr2 = objReps.iterator(); itr2.hasNext(); ) {
                Object o = itr2.next();
                if (o instanceof IsysObject) toRetrieveIsysObjects.add(o);
            }
        }
        return new DefaultIsysObjectCollection(toRetrieveIsysObjects);
    }

    private MapModel doFullRetrieve(IsysObjectCollection ioData, Service fromSvc) {
        final MapModel retval = new DefaultMapModel();
        final Boolean[] notBack = new Boolean[] { Boolean.TRUE };
        java.util.Collection c = ioData.getObjectsWithAttributes(new Class[] { IsysAttribute.class });
        Collection c2 = new ArrayList(c.size());
        for (java.util.Iterator itr = c.iterator(); itr.hasNext(); ) {
            c2.add(itr.next());
        }
        wrapper.retrieveSelected(c2, fromSvc, new AsynchronousCallback() {

            public void returnAsynchronousResult(Object result, AsynchronousService svc) {
                java.util.Collection isysObjects = (java.util.Collection) result;
                populateMapModelFromIsysObjects(retval, isysObjects);
                notBack[0] = Boolean.FALSE;
            }
        });
        while (notBack[0].equals(Boolean.TRUE)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
        return retval;
    }

    private void populateMapModelFromIsysObjects(MapModel mapModel, java.util.Collection isysObjects) {
        for (java.util.Iterator itr = isysObjects.iterator(); itr.hasNext(); ) {
            IsysObject io = (IsysObject) itr.next();
            AnnotatedLinearObject map = (AnnotatedLinearObject) io.getAttribute(LinearObject.class).iterator().next();
            mapModel.addMaps(Collections.singletonMap(map, io));
            IsysObjectCollection mappedObjects = map.getAnnotations();
            Map mappedObjects2ObjectRepresentations = new HashMap();
            for (java.util.Iterator itr2 = mappedObjects.iterator(); itr2.hasNext(); ) {
                io = (IsysObject) itr2.next();
                LinearlyLocatedObject mappedObject = (LinearlyLocatedObject) io.getAttribute(LinearlyLocatedObject.class).iterator().next();
                mappedObjects2ObjectRepresentations.put(mappedObject, io);
            }
            mapModel.addMappedObjects(mappedObjects2ObjectRepresentations);
        }
    }
}
