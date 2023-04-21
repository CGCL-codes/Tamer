package net.sf.jturingmachine.common.graph;

import java.awt.Point;
import java.util.Vector;
import net.sf.jturingmachine.common.GraphContainer;
import net.sf.jturingmachine.common.exceptions.CrossContainerLinkException;
import net.sf.jturingmachine.common.exceptions.FollowStateAlreadySetException;
import net.sf.jturingmachine.common.exceptions.PreviousStateAlreadySetException;
import net.sf.jturingmachine.common.exceptions.IncompatibleMachineTypeException;
import net.sf.jturingmachine.common.graph.Transition;

public abstract class State {

    private Point position;

    private String name;

    private Vector<Transition> endingTransitions;

    private Vector<Transition> startingTransitions;

    private GraphContainer parentContainer;

    /**
     * The constructor of this object will automatically set a unique name for each state. 
     * (s0, s1...)
     * @param GraphContainer: Needs the managing container of this object. 
     * The container is storing the reference of this state
     */
    public State(GraphContainer parentContainer) {
        this.parentContainer = parentContainer;
        position = new Point();
        endingTransitions = new Vector<Transition>();
        startingTransitions = new Vector<Transition>();
        String name_proposal;
        int iter = 0;
        do {
            name_proposal = "s" + iter++;
        } while (!this.setName(name_proposal));
    }

    /**
     * Returns the managing container of this object. 
     * The container is storing the reference of this state.
     * @return GraphContainer
     */
    public GraphContainer getParentContainer() {
        return parentContainer;
    }

    /**
     * Sets a new name for the state. A name of a state has to be unique.
     * @param name: a string which should be set.
     * @return A boolean to indicate if the give name was unique and could be set or not.
     */
    public boolean setName(String name) {
        if (this.parentContainer.getStateByName(name) == null) {
            this.name = name;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the unique name of the state.
     * @return the name as a string
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the position of the state. Only useful for canvas display.
     * @param position: a point containing the new coordinates of the state. 
     */
    public boolean setPosition(Point position) {
        if (position != null) {
            this.position = (Point) position.clone();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a Point containing the coordinates of the state on the canvas.
     * @return Point
     */
    public Point getPosition() {
        return (Point) this.position.clone();
    }

    /**
     * Removes a transition between two states, regardless whether it is ending or starting. 
     * None of the linked states will handle it anymore.
     * @param transition: the transition to remove
     * @throws An UnknownTransitionException is thrown when a transition shall be removed,
     * which is neither ending nor starting on this state.
     */
    public boolean removeTransition(Transition transition) {
        if (endingTransitions.contains(transition)) {
            return this.unlinkEndingTransition(transition) && ((transition.getPreviousState() == null) || transition.getPreviousState().unlinkStartingTransition(transition));
        } else {
            if (startingTransitions.contains(transition)) {
                return this.unlinkStartingTransition(transition) && ((transition.getFollowState() == null) || transition.getFollowState().unlinkEndingTransition(transition));
            } else {
                return false;
            }
        }
    }

    /**
     * Returns a list of all transitions that are pointing to this state.
     * @return vector<Transition>
     */
    public Vector<Transition> getEndingTransitions() {
        return this.endingTransitions;
    }

    /**
     * Get all transitions that are starting on this state and pointing to this or other states. 
     * @return vector<Transition> a list of all starting transitions
     */
    public Vector<Transition> getStartingTransitions() {
        return this.startingTransitions;
    }

    /**
     * Connects this states with a transition. The transition will end here.
     * @param transition: the transition to link
     * @return boolean: indicates if the operation was successfull or not
     * @throws FollowStateAlreadySetException
     * @throws CrossContainerLinkException
     * @throws IncompatibleMachineTypeException 
     */
    boolean linkTransitionAsEnding(Transition transition) throws FollowStateAlreadySetException, CrossContainerLinkException, IncompatibleMachineTypeException {
        if (transition == null) {
            return false;
        } else {
            if (transition.getFollowState() == null) {
                if (transition.getPreviousState() == null || transition.getPreviousState().getParentContainer() == this.getParentContainer()) {
                    transition.setFollowState(this);
                    return this.endingTransitions.add(transition);
                } else {
                    throw new CrossContainerLinkException("States in different Graphs cannot be linked!");
                }
            } else {
                throw new FollowStateAlreadySetException("The transition is still linked to " + transition.getFollowState().getName() + ". Unlink first!");
            }
        }
    }

    /**
     * Connects this states with a transition. The transition will start here.
     * @param transition: the transition to link
     * @return boolean: indicates if the operation was successfull or not
     * @throws PreviousStateAlreadySetException
     * @throws CrossContainerLinkException
     * @throws IncompatibleMachineTypeException 
     */
    boolean linkTransitionAsStarting(Transition transition) throws PreviousStateAlreadySetException, CrossContainerLinkException, IncompatibleMachineTypeException {
        if (transition == null) {
            return false;
        } else {
            if (transition.getPreviousState() == null) {
                if (transition.getFollowState() == null || transition.getFollowState().getParentContainer() == this.getParentContainer()) {
                    transition.setPreviousState(this);
                    return this.startingTransitions.add(transition);
                } else {
                    throw new CrossContainerLinkException("States in different graphs cannot be linked!");
                }
            } else {
                throw new PreviousStateAlreadySetException("The transition is still linked to " + transition.getPreviousState().getName() + ". Unlink first!");
            }
        }
    }

    /**
     * Disconnects a transition which is starting here from this state.
     * @param transition: the transition to unlink
     * @return true if the transition is completely unlinked now
     */
    boolean unlinkStartingTransition(Transition transition) {
        if (transition.getPreviousState() == this) {
            transition.setPreviousState(null);
            this.startingTransitions.remove(transition);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Disconnects a transition which is ending here from this state.
     * @param transition: the transition to unlink
     * @return true if the transition is completely unlinked now
     */
    boolean unlinkEndingTransition(Transition transition) {
        if (transition.getFollowState() == this) {
            transition.setFollowState(null);
            this.endingTransitions.remove(transition);
            return true;
        } else {
            return false;
        }
    }

    /**
     * With this method different graphs of the same time can be linked. Linking means that when
     * the runner reaches a "linked State" the runner will continue its run in the foreign graph.
     * When an endstate is reached the runner will continue in the "linked State"
     *
     * @param OperationoreignContainer: the foreign graph to link to
     * @throws An UnknownTransitionException 
     */
    public boolean linkToForeignContainer(GraphContainer foreignContainer) throws IncompatibleMachineTypeException {
        if (foreignContainer == null || foreignContainer == this.parentContainer) {
            return false;
        }
        if (foreignContainer.getMachineType() == this.parentContainer.getMachineType()) {
            this.parentContainer = foreignContainer;
            return true;
        } else {
            throw new IncompatibleMachineTypeException("A" + foreignContainer.getMachineType() + "cannot be linked to a" + this.parentContainer.getMachineType());
        }
    }
}
