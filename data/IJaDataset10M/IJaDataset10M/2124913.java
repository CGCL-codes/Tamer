package org.tubs.epoc.SMFF.ModelElements.Application;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import org.tubs.epoc.SMFF.ModelElements.ExtendibleModelElement;
import org.tubs.epoc.SMFF.ModelElements.Application.SchedElemChangeEvent.EventType;
import org.tubs.epoc.SMFF.ModelElements.Platform.AbstractResource;
import org.tubs.epoc.SMFF.ModelElements.Scheduling.AbstractSchedulingParameter;
import org.tubs.epoc.SMFF.ModelElements.Scheduling.SchedulingPriority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract class description for a schedulable element.
 * <p>
 * A schedulable element such as {@link Task Task} must extend this class. A schedulable element belongs to an
 * application model and has certain constraints. In case to be notified on changes, listeners can be added to an
 * element instance as well.
 * 
 */
public abstract class SchedulableElement extends ExtendibleModelElement<AbstractSchedElemData> implements Cloneable {

    private static Log logger = LogFactory.getLog(SchedulableElement.class);

    /**
	 * The application this task belongs to.
	 */
    protected ApplicationModel application;

    /**
	 * A user-definable short name (may be null).
	 */
    protected String shortName;

    /**
	 * Current scheduling paramter
	 */
    protected AbstractSchedulingParameter schedParam;

    /**
	 * Resource this scheduled elem is mapped to.
	 */
    protected AbstractResource mappedTo = null;

    /**
	 * Local element constraints.
	 */
    protected Hashtable<String, ElementLatencyConstraint> constraints = new Hashtable<String, ElementLatencyConstraint>();

    /**
	 * Response time for this task.
	 */
    protected double responseTime;

    /**
	 * Output timing behavior for tis sechuled element.
	 */
    protected AbstractTimingBehavior outputBehavior = null;

    /**
   * List of possible resources.
   */
    protected LinkedList<Profile> profileList = new LinkedList<Profile>();

    /**
   * Profile to use.
   */
    protected Profile activeProfile;

    /**
	 * Buffer size of this task.
	 */
    protected int bufferSize = 0;

    private LinkedList<SchedElemChangeListener> changeListener = new LinkedList<SchedElemChangeListener>();

    /**
	 * Returns a clone of the schedulable element.
	 * <p>
	 * Cloned object consists of:
	 * <ul>
	 * <li>deep clone of cloneable data extensions,
	 * <li>clone of all lists and hashtables (-> list is duplicated, referenced objects are linked),
	 * <li>empty list of change listeners.
	 * 
	 * @return the copy of this schedulable element
	 */
    @Override
    public Object clone() {
        SchedulableElement clone = (SchedulableElement) super.clone();
        clone.constraints = new Hashtable<String, ElementLatencyConstraint>();
        clone.constraints.putAll(constraints);
        clone.outputBehavior = (AbstractTimingBehavior) outputBehavior.clone();
        clone.changeListener = new LinkedList<SchedElemChangeListener>();
        return clone;
    }

    /**
	 * Returns the user supplied short name of this object. In case none was supplied, an empty string is returned.
	 * <p>
	 * This method can be used e.g. for GUIs with limited space.
	 * 
	 * @return a short string representing this object
	 */
    public final String getShortName() {
        return (shortName == null) ? "" : shortName;
    }

    /**
	 * Returns a unique name for the schedulable element.
	 * 
	 * Two elements of the same type with equal identifiers may have the same unique name.
	 * 
	 * @return a unique name that can be used in e.g. SymTA/S
	 */
    public abstract String getUniqueName();

    /**
	 * Gets the element ID of the implementing class.
	 * 
	 * @return element ID
	 */
    public final int getElemId() {
        return this.getIdent().getElemId();
    }

    /**
	 * Gets the Identifier of the Class.
	 * 
	 * @return the identifier
	 */
    public abstract SchedElemIdentifier getIdent();

    /**
	 * returns the associated scheduling paramter
	 * @return scheduling parameter
	 */
    public final AbstractSchedulingParameter getSchedulingParameter() {
        return this.schedParam;
    }

    /**
	 * sets the scheduling parameter for this schedulable element
	 * @param schedParam
	 */
    public final void setSchedulingParameter(AbstractSchedulingParameter schedParam) {
        if (this.getMappedTo() == null) {
            logger.error("Trying to set scheduling parameter although element is not yet mapped");
            return;
        }
        if (this.getMappedTo().getScheduler().getRequiredSchedParamClass().isInstance(schedParam)) {
            this.schedParam = schedParam;
        } else {
            logger.error("Trying to associate scheduling parameter that is not suitable for corresponding resource");
        }
    }

    /**
	 * Gets the current priority of this element.
	 * 
	 * @return the element's priority
	 * @deprecated this function is deprecated as abstract scheduling parameters should be used instead
	 * only remaining for compatibility reasons
	 */
    public final int getPrio() {
        if (this.schedParam instanceof SchedulingPriority) {
            return ((SchedulingPriority) this.schedParam).getPriority();
        } else {
            return -1;
        }
    }

    /**
	 * Sets the priority of this element
	 * 
	 * @param prio prority value to set
   * @deprecated this function is deprecated as abstract scheduling parameters should be used instead
   * only remaining for compatibility reasons
	 */
    public final void setPrio(int prio) {
        if (this.schedParam instanceof SchedulingPriority) {
            ((SchedulingPriority) this.schedParam).setPriority(prio);
        } else {
            return;
        }
        notifyListeners(new SchedElemChangeEvent(this, EventType.PRIORITY, new Integer(prio)));
    }

    /**
	 * Returns the largest violation of an end-to-end latency constraint. A violation is the difference between the path
	 * latency and the constraint assigned element on a path.
	 * 
	 * @return the largest violation
	 */
    public final double getLargestViolation() {
        double largestViolation = Integer.MIN_VALUE;
        Collection<ElementLatencyConstraint> constrCollect = constraints.values();
        for (ElementLatencyConstraint currentConstr : constrCollect) {
            largestViolation = Math.max(largestViolation, currentConstr.getPathLatency() - currentConstr.getLatencyConstr());
        }
        return largestViolation;
    }

    /**
	 * Returns the current response time of this schedulable element.
	 * 
	 * @return response time of this task
	 */
    public final double getResponseTime() {
        return responseTime;
    }

    /**
	 * Sets the response time for this element.
	 * 
	 * @param responseTime
	 *          the response time to set
	 */
    public final void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    /**
	 * Getter method for the timing behavior of this element.
	 * 
	 * @return timing behavior at the output of this element
	 */
    public final AbstractTimingBehavior getOutputBehavior() {
        return outputBehavior;
    }

    /**
	 * Setter method for the timing behavior of this element.
	 * 
	 * @param outputBehavior
	 *          timing behavior at the output of this element
	 */
    public final void setOutputBehavior(AbstractTimingBehavior outputBehavior) {
        this.outputBehavior = outputBehavior;
    }

    /**
	 * Getter method for the buffer size of this element
	 * 
	 * @return the buffer size
	 */
    public final int getBufferSize() {
        return bufferSize;
    }

    /**
	 * Setter method for the buffer size of this element.
	 * 
	 * @param bufferSize
	 *          buffer size value to be set to this value
	 */
    public final void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
	 * Implementation of a Listener concept. Adds a listener to this resource. The implementing class will be notified of
	 * changes to this object using the callback function defined in the listener class.
	 * <p>
	 * 
	 * For a List of events that cause a call back see the SchedElemChangeEvent class.
	 * 
	 * @param listener
	 *          Listener class to attach to this element
	 * 
	 * @see SchedElemChangeEvent
	 */
    public final void addListener(SchedElemChangeListener listener) {
        changeListener.add(listener);
    }

    /**
	 * Notify all registered listeners of an event.
	 * 
	 * @param e
	 *          the event to hand to all listeners
	 */
    protected final void notifyListeners(SchedElemChangeEvent e) {
        for (SchedElemChangeListener l : changeListener) {
            l.taskDescriptionChanged(e);
        }
    }

    /**
	 * Removes the given listener if it exists in the list of listeners.
	 * 
	 * @param listener
	 *          listener to remove
	 * 
	 * @return true if a listener was removed
	 */
    public final boolean removeListener(SchedElemChangeListener listener) {
        return changeListener.remove(listener);
    }

    /**
   * Getter method for the worst case execution time of the currently active profile.
   * @return the worst case execution time of the currently active profile
   */
    public int getWCET() {
        return this.getActiveProfile().getWCET();
    }

    /**
   * Setter method for the worst case execution time of the currently active profile.
   * @param WCET the worst case execution time to be set for the currently active profile
   */
    public void setWCET(int WCET) {
        this.getActiveProfile().setWCET(WCET);
    }

    /**
   * Getter method for the best case execution time of the currently active profile.
   * @return best case execution time of the currently active profile. 
   */
    public int getBCET() {
        return this.getActiveProfile().getBCET();
    }

    /**
   * Setter method for the best case execution time of the currently active profile.
   * @param BCET best case execution time to be set for the currently active profile. 
   */
    public void setBCET(int BCET) {
        this.getActiveProfile().setWCET(BCET);
    }

    /**
	 * Returns the resource this schedulable element is mapped to.
	 * 
	 * @return the resource this element is mapped to
	 */
    public final AbstractResource getMappedTo() {
        return mappedTo;
    }

    /**
	 * Maps this schedulable element to the specified resource.
	 * 
	 * @param mappedTo
	 *          the resource this element is mapped to.
	 */
    public final void setMappedTo(AbstractResource mappedTo) {
        this.mappedTo = mappedTo;
    }

    /**
	 * Getter method for the application ID of this element.
	 * 
	 * @return the application ID of this element
	 */
    public final int getAppId() {
        return this.getIdent().getAppId();
    }

    /**
	 * Getter method for the application version of this element.
	 * 
	 * @return the application version of this element
	 */
    public final int getAppV() {
        return this.getIdent().getAppV();
    }

    /**
	 * Getter method for the application model this element is part of.
	 * 
	 * @return the application to which this element is part of.
	 */
    public ApplicationModel getApplication() {
        return application;
    }

    /**
	 * Returns the latency constraint of the specified path hash.
	 * 
	 * @param pathHash
	 *          hash key of the latency constraint
	 * @return latency constraint (null if not found)
	 */
    public final ElementLatencyConstraint getLatencyConstraint(String pathHash) {
        return constraints.get(pathHash);
    }

    /**
	 * Returns a collection of all latency constraints.
	 * 
	 * @return latency constraints
	 */
    public final Collection<ElementLatencyConstraint> getLatencyConstraints() {
        return constraints.values();
    }

    /**
	 * Returns the largest violated constraint.
	 * 
	 * @return constraint with maximum difference between constraint and latency
	 */
    public final ElementLatencyConstraint getLargestViolator() {
        double largestViolation = Double.NEGATIVE_INFINITY;
        ElementLatencyConstraint violator = null;
        Collection<ElementLatencyConstraint> constrCollect = constraints.values();
        for (ElementLatencyConstraint currentConstr : constrCollect) {
            if (currentConstr.getPathLatency() - currentConstr.getLatencyConstr() > largestViolation) {
                largestViolation = currentConstr.getPathLatency() - currentConstr.getLatencyConstr();
                violator = currentConstr;
            }
        }
        return violator;
    }

    /**
	 * Checks whether the constraints are feasible. Returns null if all constraints are feasible, else the first violated
	 * constraint.
	 * 
	 * @return null if all constraints are feasible, the first violated constraint otherwise.
	 */
    public final ElementLatencyConstraint isFeasible() {
        Collection<ElementLatencyConstraint> constrCollect;
        constrCollect = this.getLatencyConstraints();
        for (ElementLatencyConstraint currentConstr : constrCollect) {
            if (currentConstr.getPathLatency() > currentConstr.getLatencyConstr()) {
                return currentConstr;
            }
        }
        return null;
    }

    /**
	 * Adds a path constraint to this task. Creates NO new LatencyConstraint object but links the given one.
	 * 
	 * @param constraint
	 *          Task Latency Constraint object to link
	 * @return the parameter passed <tt>constraint</tt>
	 */
    private ElementLatencyConstraint addConstraint(ElementLatencyConstraint constraint) {
        if (constraint == null) {
            logger.error("Error on adding a constraint. Constraint is null.");
            throw new NullPointerException("Constraint to be added is null!");
        }
        constraints.put(constraint.getPath().getSymtaName(), constraint);
        return constraint;
    }

    /**
	 * adds a path constraint to this tasklink. Creates a new LatencyConstraint object.
	 * 
	 * @param predecessor
	 *          previous task in path
	 * @param successor
	 *          following task in path
	 * @param latency
	 *          maximum latency of this path
	 * @param sysLatency
	 *          system latency constraint
	 * @param path
	 *          path of this element.
	 */
    public ElementLatencyConstraint addConstraint(SchedulableElement predecessor, SchedulableElement successor, int latency, SysLatencyConstraint sysLatency, Path path) {
        ElementLatencyConstraint Constraint = new ElementLatencyConstraint(this, predecessor, successor, latency, sysLatency, path);
        return this.addConstraint(Constraint);
    }

    /**
   * Adds a profile to the list in the task.
   *
   * @param  profile  to add
   */
    public void addProfile(Profile profile) {
        try {
            if (profile == null) throw new NullPointerException("Profile does not exist");
            profileList.add(profile);
            if (this.getActiveProfile() == null) this.setActiveProfile(profile);
        } catch (Exception e) {
            logger.error("Error adding a possible res", e);
        }
    }

    /**
   * Returns the profile in which this task is currently executing.
   * 
   * @return this task's current profile, <null> if there is no profile.
   */
    public Profile getActiveProfile() {
        return this.activeProfile;
    }

    /**
   * sets the currently active profile
   * @param activeProfile
   */
    public void setActiveProfile(Profile activeProfile) {
        this.activeProfile = activeProfile;
    }

    /**
   * returns the list of all profiles of this schedulable element
   * @return
   */
    public final LinkedList<Profile> getProfileList() {
        return profileList;
    }
}
