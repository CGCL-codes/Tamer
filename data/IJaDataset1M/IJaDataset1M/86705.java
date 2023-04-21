package org.mftech.dawn.runtime.client.synchronization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.impl.DiffGroupImpl;
import org.eclipse.emf.compare.diff.metamodel.impl.UpdateAttributeImpl;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;

public class ResourceDiffHelper {

    List<EObject> newObjects;

    Map<EObject, EObject> changedObjects;

    List<EObject> deletedObjects;

    private static Logger logger = Logger.getLogger(ResourceDiffHelper.class);

    Resource newResource;

    Resource oldResource;

    public ResourceDiffHelper() {
        changedObjects = new HashMap<EObject, EObject>();
        newObjects = new ArrayList<EObject>();
        deletedObjects = new ArrayList<EObject>();
    }

    public void doDiff(Resource oResource, Resource nResource) throws InterruptedException {
        this.newResource = nResource;
        this.oldResource = oResource;
        Diagram newDiagram = ResourceHelper.getDiagramFromResource(newResource);
        Diagram oldDiagram = ResourceHelper.getDiagramFromResource(oldResource);
        parseList(newDiagram.getChildren(), oldDiagram.getChildren());
        parseList(newDiagram.getEdges(), oldDiagram.getEdges());
    }

    private void evaluateChanges(DiffModel diff) {
        for (DiffElement e : diff.getOwnedElements()) {
            logger.info(e + "(" + e.getClass() + ")");
            evaluateChanges(e, 0, null);
        }
    }

    /**
	 * searches for changes in View Elements
	 * 
	 * @param diff
	 * @param level
	 * @param parent
	 */
    private void evaluateChanges(DiffElement diff, int level, EObject parent) {
        for (DiffElement diffy : diff.getSubDiffElements()) {
            if (diffy instanceof DiffGroupImpl) {
                DiffGroupImpl dd = (DiffGroupImpl) diffy;
                EObject leftParent = ((DiffGroupImpl) diffy).getLeftParent();
                if (leftParent instanceof Edge || leftParent instanceof Node) {
                    parent = leftParent;
                }
            } else if (diffy instanceof UpdateAttributeImpl) {
                logger.info("ADD View Parent to changed " + parent);
                if (parent != null) {
                    getChangedObjects().put(parent, ResourceHelper.getSameEObjectFromOtherResource(parent, (XMLResource) newResource));
                }
            } else {
                logger.error("UNIDENTIFIED CLASS: " + diffy + "(" + diffy.getClass() + ")");
            }
            evaluateChanges(diffy, level + 1, parent);
        }
    }

    /**
	 * parses the lists and puts the diff into the concerning lists
	 * 
	 * @param newDiagramChildren
	 * @param oldDiagramChildren
	 */
    private void parseList(EList newDiagramChildren, EList oldDiagramChildren) {
        boolean foundExistingObject = false;
        for (Object newO : newDiagramChildren) {
            foundExistingObject = false;
            for (Object oldO : oldDiagramChildren) {
                if (ResourceHelper.areSameObjects((EObject) oldO, (EObject) newO)) {
                    logger.info("HasChanged: " + (ResourceHelper.objectsHaveChanged(oldO, newO)));
                    if ((ResourceHelper.objectsHaveChanged(oldO, newO))) {
                        getChangedObjects().put((EObject) oldO, (EObject) newO);
                        logger.info("Change Node :" + oldO);
                    }
                    foundExistingObject = true;
                    break;
                }
            }
            if (!foundExistingObject) {
                logger.info("Muss erstellt werden " + newO);
                newObjects.add((EObject) newO);
            }
        }
        for (Object oldO : oldDiagramChildren) {
            foundExistingObject = false;
            for (Object newO : newDiagramChildren) {
                if (ResourceHelper.areSameObjects((EObject) oldO, (EObject) newO)) {
                    foundExistingObject = true;
                    break;
                }
            }
            if (!foundExistingObject) {
                logger.info("Muss gel�scht werden " + oldO);
                deletedObjects.add((EObject) oldO);
            }
        }
    }

    public List<EObject> getNewObjects() {
        if (newObjects == null) {
            newObjects = new ArrayList<EObject>();
        }
        return newObjects;
    }

    public void setNewObjects(List<EObject> newObjects) {
        this.newObjects = newObjects;
    }

    public Map<EObject, EObject> getChangedObjects() {
        if (changedObjects == null) {
            changedObjects = new HashMap<EObject, EObject>();
        }
        return changedObjects;
    }

    public void setChangedObjects(HashMap<EObject, EObject> changedObjects) {
        this.changedObjects = changedObjects;
    }

    public List<EObject> getDeletedObjects() {
        if (deletedObjects == null) {
            deletedObjects = new ArrayList<EObject>();
        }
        return deletedObjects;
    }

    public void setDeletedObjects(List<EObject> deletedObjects) {
        this.deletedObjects = deletedObjects;
    }

    private static void printChanges(final DiffModel diff) {
        for (DiffElement e : diff.getOwnedElements()) {
            System.out.println(e + "(" + e.getClass() + ")");
            printChanges(e, 0);
        }
    }

    private static void printChanges(DiffElement diff, int level) {
        for (DiffElement diffy : diff.getSubDiffElements()) {
            if (diffy instanceof DiffGroupImpl) {
                DiffGroupImpl dd = (DiffGroupImpl) diffy;
            }
            UpdateAttributeImpl a;
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.println(diffy + "(" + diffy.getClass() + ")");
            printChanges(diffy, level + 1);
        }
    }

    public static void merge(XMLResource newResource, XMLResource oldResource) throws InterruptedException {
        final MatchModel match = MatchService.doResourceMatch(newResource, oldResource, Collections.<String, Object>emptyMap());
        final DiffModel diff = DiffService.doDiff(match, false);
        System.out.println("Left " + diff.getLeft());
        System.out.println("Right " + diff.getRight());
        printChanges(diff);
        List<DiffElement> differences = new ArrayList<DiffElement>(diff.getOwnedElements());
        MergeService.merge(differences, true);
    }
}
