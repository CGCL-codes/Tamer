package se.entitymanager.presentation.swing;

import java.util.Arrays;
import javax.swing.tree.DefaultMutableTreeNode;
import se.entitymanager.logic.LogicFacade;
import se.entitymanager.logic.EntityNotFolderishException;
import se.entitymanager.logic.EntityInterface;

/**
 * A <code>DefaultMutableTreeNode</code> for entities.<p>
 * 
 * @uml.stereotype name="tagged" isDefined="true" 
 */
public class EntityTreeNode extends DefaultMutableTreeNode {

    /**
     * The presentation facade object, this object belongs to.
     * 
     * @uml.property name="presentationFacade"
     * @uml.associationEnd 
     * @uml.property name="presentationFacade" multiplicity="(1 1)"
     */
    private SwingPresentationFacade presentationFacade;

    /**
     * Flag that indicates whether the children of this node have been loaded.
     * 
     * @uml.property name="childrenLoaded" 
     */
    private boolean childrenLoaded = false;

    /**
     * The logic facade to access the entities.
     * 
     * @uml.property name="logicFacade"
     * @uml.associationEnd 
     * @uml.property name="logicFacade" multiplicity="(1 1)"
     */
    private LogicFacade logicFacade;

    /**
	 * Constructs a tree node for <code>entity</code> for a <code>presentationFacade</code>. 
	 * @param presentationFacade the presentation facade 
	 * @param entity the entity
	 */
    public EntityTreeNode(SwingPresentationFacade presentationFacade, EntityInterface entity) {
        super(entity);
        this.presentationFacade = presentationFacade;
        this.logicFacade = this.presentationFacade.getLogicFacade();
        if (this.logicFacade.isFolderish(entity)) {
            this.setAllowsChildren(true);
        }
    }

    /**
	 * Loads the children of this entity.<p>
	 * This is known as "lazy loading" and improves performance.
	 */
    protected void loadChildren() {
        if (!childrenLoaded) {
            childrenLoaded = true;
            Object[] children = null;
            try {
                children = logicFacade.getAllChildren((EntityInterface) this.userObject).toArray();
            } catch (EntityNotFolderishException e) {
                e.printStackTrace();
                System.err.println("Fatal system error: Object says it is folderish, but it is not.");
                System.exit(-1);
            }
            Arrays.sort(children, logicFacade.getComparator());
            for (int childrenIndex = 0; childrenIndex < children.length; childrenIndex++) {
                EntityInterface child = (EntityInterface) children[childrenIndex];
                this.add(new EntityTreeNode(presentationFacade, child));
            }
        }
    }

    /**
	 * Returns whether this node is a leaf.
	 * @return true if this node is a leaf, false otherwise
	 */
    public boolean isLeaf() {
        if (logicFacade.isFolderish((EntityInterface) this.userObject)) {
            return false;
        } else {
            return true;
        }
    }

    /** Returns the entity the node is displaying
	 * @return entity the entity
	 */
    public EntityInterface getEntity() {
        return (EntityInterface) this.getUserObject();
    }
}
