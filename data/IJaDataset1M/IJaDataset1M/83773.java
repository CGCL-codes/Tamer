package jsynoptic.plugins.java3d;

import java.util.ArrayList;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphObject;
import javax.swing.event.UndoableEditListener;

/**
 * a SceneGraphObjectHolder holds a scene graph object and
 * provides a set of operations applicable to any kind of scene graph object
 * These operations are :
 * - remove or add a scene graph object to the scene graph
 * at the place this object defines
 * - get the node or the group to which belong this scene graph object
 * - get a listener to perform undo/redo operations
 * For instance a tree used to display the scene graph is composed of
 * nodes. Each node refers to one scene graph object. By implementing
 * these operations each tree node can provide a polymorphic add/remove
 * operation on all the elements part of the scene graph.
 */
public interface SceneGraphObjectHolder {

    /**
     * Checks if a scene graph object can be added
     * @param obj the object to add
     * @return true if it can be added
     */
    public boolean canAddSceneGraphObject(SceneGraphObject obj);

    /**
     * Add one scene graph object
     * According to the target, this new object can replace the previous
     * object or be added to a list 
     * @param obj the object to add
     * @return the replace object or null if simply added
     */
    public SceneGraphObject addSceneGraphObject(SceneGraphObject obj);

    /**
     * Checks if a scene graph object can be deleted
     * @param obj the object to remove
     * @param oldObj the optional object to be used in place of 
     * @return true if it can be removed
     */
    public boolean canRemoveSceneGraphObject(SceneGraphObject obj, SceneGraphObject oldObj);

    /**
     * Remove one scene graph object
     * @param obj the object to remove
     * @param oldObj the optional object to be used in place of 
     */
    public void removeSceneGraphObject(SceneGraphObject obj, SceneGraphObject oldObj);

    /**
     * Get the node to which belong the related scene graph object 
     * If the related graph object is a node then it is the returned value
     */
    public Node getSceneGraphNode();

    /**
     * Get the group to which belong the related scene graph object 
     * If the related graph object is a group then it is the returned value
     */
    public Group getSceneGraphGroup();

    /**
     * Get the related scene graph object
     */
    public SceneGraphObject getSceneGraphObject();

    /**
     * Get the parent holder if any
     */
    public SceneGraphObjectHolder getParentHolder();

    /**
     * Get the children list of holders
     * @param list the list to fill with holders
     */
    public void getChildrenHolders(ArrayList<SceneGraphObjectHolder> list);

    /**
     * Get the list of root holders for this holder
     * The root holders are holders with null parent holder
     * @param list the list to fill with holders
     */
    public void getRootHolders(ArrayList<SceneGraphObjectHolder> list);

    /**
     * Get the undo able event listener if any
     */
    public UndoableEditListener getUndoableEditListener();
}
