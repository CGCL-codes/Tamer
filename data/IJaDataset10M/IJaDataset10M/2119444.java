package net.sf.gamine.util;

import net.sf.gamine.common.*;
import net.sf.gamine.control.*;
import net.sf.gamine.physics.*;
import net.sf.gamine.render.*;
import java.util.*;

/**
 * A Compound manages a set of objects (Controllers, BodySets, Forces, RenderTreeNodes, etc.) that are meant to work
 * together, and that should all be added or removed as a unit.  To use it, add the component objects to the
 * Compound by calling the appropriate add...() methods.  You can then call {@link #start()} to add all of them to the
 * Sequence, or {@link #stop()} to remove all of them.
 * <p>
 * The Compound class may be used directly for managing arbitrary sets of objects.  It also is the parent class of
 * various other classes that represent specific types of compound entities.
 */
public class Compound {

    private final ControlStage controlStage;

    private final PhysicsStage physicsStage;

    private final ArrayList<Controller> controllers;

    private final ArrayList<BodySet> bodySets;

    private final ArrayList<Force> forces;

    private final ArrayList<ContactSet> contactSets;

    private final ArrayList<RenderTreeNodeInfo> renderTreeNodes;

    private boolean isRunning;

    /**
   * Create a new Compound.
   *
   * @param controlStage    the ControlStage to which Controllers should be added
   * @param physicsStage    the PhysicsStage to which Forces, BodySets, and ContactSets should be added
   */
    public Compound(ControlStage controlStage, PhysicsStage physicsStage) {
        this.controlStage = controlStage;
        this.physicsStage = physicsStage;
        controllers = new ArrayList<Controller>();
        bodySets = new ArrayList<BodySet>();
        forces = new ArrayList<Force>();
        contactSets = new ArrayList<ContactSet>();
        renderTreeNodes = new ArrayList<RenderTreeNodeInfo>();
    }

    /**
   * Create a new Compound.
   *
   * @param sequence    a DefaultSequence the Compound's components should be added to.  Controllers will be added to
   *                    its control stage, while Forces, BodySets, and ContactSets will be added to its physics stage.
   */
    public Compound(DefaultSequence sequence) {
        this(sequence.getControlStage(), sequence.getPhysicsStage());
    }

    /**
   * Add a Controller to the Compound, which will be added to the ControlStage when {@link #start()} is called.
   */
    public void addController(Controller controller) {
        controllers.add(controller);
    }

    /**
   * Add a BodySet to the Compound, which will be added to the PhysicsStage when {@link #start()} is called.
   */
    public void addBodySet(BodySet bodySet) {
        bodySets.add(bodySet);
    }

    /**
   * Add a Force to the Compound, which will be added to the PhysicsStage when {@link #start()} is called.
   */
    public void addForce(Force force) {
        forces.add(force);
    }

    /**
   * Add a ContactSet to the Compound, which will be added to the PhysicsStage when {@link #start()} is called.
   */
    public void addContactSet(ContactSet contactSet) {
        contactSets.add(contactSet);
    }

    /**
   * Add a RenderTreeNode to the Compound, which will be added to the specified parent node when {@link #start()} is called.
   *
   * @param node     the RenderTreeNode to add
   * @param parent   the parent node to add it to
   */
    public void addRenderTreeNode(RenderTreeNode node, BranchNode parent) {
        renderTreeNodes.add(new RenderTreeNodeInfo(node, parent));
    }

    /**
   * Add all components of this Compound to their respective parents.
   */
    public void start() {
        for (int i = 0; i < controllers.size(); i++) controlStage.addController(controllers.get(i));
        for (int i = 0; i < bodySets.size(); i++) physicsStage.addBodySet(bodySets.get(i));
        for (int i = 0; i < forces.size(); i++) physicsStage.addForce(forces.get(i));
        for (int i = 0; i < contactSets.size(); i++) physicsStage.addContactSet(contactSets.get(i));
        for (int i = 0; i < renderTreeNodes.size(); i++) {
            RenderTreeNodeInfo info = renderTreeNodes.get(i);
            info.parent.addChild(info.node);
        }
        isRunning = true;
    }

    /**
   * Remove all components of this Compound from their respective parents.
   */
    public void stop() {
        for (int i = 0; i < controllers.size(); i++) controlStage.removeController(controllers.get(i));
        for (int i = 0; i < bodySets.size(); i++) physicsStage.removeBodySet(bodySets.get(i));
        for (int i = 0; i < forces.size(); i++) physicsStage.removeForce(forces.get(i));
        for (int i = 0; i < contactSets.size(); i++) physicsStage.removeContactSet(contactSets.get(i));
        for (int i = 0; i < renderTreeNodes.size(); i++) {
            RenderTreeNodeInfo info = renderTreeNodes.get(i);
            info.parent.removeChild(info.node);
        }
        isRunning = false;
    }

    /**
   * Get whether this Compound is running (that is, whether {@link #start()} has been called at least once,
   * and {@link #stop()} has not been called since the most recent call to start()).
   */
    public boolean isRunning() {
        return isRunning;
    }

    /**
   * This inner class stores information about a RenderTreeNode that has been added to the Compound.
   */
    private static class RenderTreeNodeInfo {

        RenderTreeNode node;

        BranchNode parent;

        RenderTreeNodeInfo(RenderTreeNode node, BranchNode parent) {
            this.node = node;
            this.parent = parent;
        }
    }
}
