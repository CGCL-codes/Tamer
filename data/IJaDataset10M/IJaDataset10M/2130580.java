package uk.co.q3c.deplan.domain.task;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jdom.Element;
import uk.co.q3c.deplan.dao.TaskDao;
import uk.co.q3c.deplan.domain.resource.IndividualResource;
import uk.co.q3c.deplan.domain.resource.Resource;
import uk.co.q3c.deplan.util.XML;
import uk.co.q3c.deplan.util.XMLImporter;
import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ArrayList4;

/**
 * @see SupportTask
 * @see ProjectTask
 * @author DSowerby 2 Oct 2008
 * 
 */
public abstract class ResourcedTask extends AbstractAtomicTask {

    protected Resource assignedResource;

    protected double assignedUnits;

    protected int workRemaining;

    protected int workAllocated;

    protected int workDone;

    /**
	 * The amount of work done, plus the amount remaining
	 */
    protected int totalWork;

    private List<TaskResourceProfile> taskResourceProfiles;

    public ResourcedTask() {
        super();
    }

    /**
	 * Assigns a resource to this task, at a given level of work
	 * 
	 * @param resource
	 *           the resource to do the work
	 * @param allocation
	 *           the amount of work to do expressed as a percentage of working
	 *           time
	 */
    public void assignResource(Resource resource, double units) {
        activate(ActivationPurpose.WRITE);
        setAssignedUnits(units);
        setAssignedResource(resource);
    }

    public Resource getAssignedResource() {
        activate(ActivationPurpose.READ);
        return assignedResource;
    }

    public double getAssignedUnits() {
        activate(ActivationPurpose.READ);
        return assignedUnits;
    }

    public void unassignResource() {
        activate(ActivationPurpose.WRITE);
        setAssignedResource(null);
    }

    public void setAssignedUnits(double assignedUnits) {
        activate(ActivationPurpose.WRITE);
        double oldValue = this.assignedUnits;
        this.assignedUnits = assignedUnits;
        firePropertyChange("assignedUnits", oldValue, this.assignedUnits);
    }

    /**
	 * Sets the assigned resource as specified
	 * 
	 * @param assignedResource
	 */
    public void setAssignedResource(Resource assignedResource) {
        activate(ActivationPurpose.WRITE);
        Resource oldValue = this.assignedResource;
        this.assignedResource = assignedResource;
        firePropertyChange("assignedResource", oldValue, this.assignedResource);
    }

    public String assignmentDescription() {
        activate(ActivationPurpose.READ);
        return " resource=" + getAssignedUnits() + " x " + getAssignedResource();
    }

    /**
	 * The amount of work to do that isn't yet allocated. workRemaining -
	 * workAllocated
	 * 
	 * @return (workRemaining - workAllocated)
	 */
    public int unallocatedWork() {
        activate(ActivationPurpose.READ);
        return (workRemaining - workAllocated);
    }

    /**
	 * Gets total work value in minutes, work done + work remaining
	 * 
	 * @return
	 */
    public int getTotalWork() {
        activate(ActivationPurpose.READ);
        return totalWork;
    }

    protected void setTotalWork(int totalWork) {
        activate(ActivationPurpose.WRITE);
        int oldValue = this.totalWork;
        this.totalWork = totalWork;
        firePropertyChange("workTotal", oldValue, this.totalWork);
    }

    private void calculateTotalWork() {
        setTotalWork(workDone + workRemaining);
    }

    public int getWorkRemaining() {
        activate(ActivationPurpose.READ);
        return workRemaining;
    }

    /**
	 * Sets workRemaining to the value given. The general calculation for
	 * workRemaining is to take the maximum of all the individual resource
	 * estimates, but there are occasions when there is a need to set the other
	 * way round - for example when a task is first created. <br>
	 * <br>
	 * The new value is passed to all assigned individual resources <br>
	 * <br>
	 * A value of less than zero is forced to zero <br>
	 * <br>
	 * 
	 * <br>{@link #calculateWorkRemaining()} is called to finish the update
	 * 
	 * @param workRemaining
	 *           in minutes
	 */
    public void setWorkRemaining(int workRemaining) {
        activate(ActivationPurpose.WRITE);
        int newValue = (workRemaining < 0 ? 0 : workRemaining);
        for (TaskResourceProfile trp : taskResourceProfiles) {
            trp.forceWorkLeft(newValue);
        }
        updateWorkRemaining(newValue);
    }

    public int getWorkDone() {
        activate(ActivationPurpose.READ);
        return workDone;
    }

    /**
	 * sets workDone to the value supplied. Called in response to changes in
	 * {@link TaskResourceProfile}<br>
	 * <br>{@link #calculateTotalWork()} is called to update total work
	 * 
	 * @param workDone
	 *           in minutes
	 */
    private void setWorkDone(int workDone) {
        activate(ActivationPurpose.WRITE);
        int oldValue = this.workDone;
        this.workDone = workDone;
        firePropertyChange("workDone", oldValue, this.workDone);
        calculateTotalWork();
    }

    /**
	 * Sets the work done by the specified resource on the specified date
	 * 
	 * @param workDone
	 * @param resource
	 * @param di
	 */
    public void setWorkDone(int workDone, IndividualResource resource, Calendar di) {
        activate(ActivationPurpose.WRITE);
        if (resource == null) {
            if (logger.isInfoEnabled()) {
                logger.info("setWorkDone called with null resource");
                return;
            }
        }
        TaskResourceProfile trp = getTaskResourceProfile(resource);
        if (trp != null) {
            trp.setWorkDone(workDone, di.getTime());
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("No TaskResourceProfile found for " + resource.getName());
                return;
            }
        }
    }

    public boolean isComplete() {
        activate(ActivationPurpose.READ);
        return (workRemaining <= 0);
    }

    @Override
    public boolean isFixedDuration() {
        activate(ActivationPurpose.READ);
        return fixedDuration;
    }

    @Override
    public void setFixedDuration(boolean fixedDuration) {
        activate(ActivationPurpose.WRITE);
        boolean oldValue = this.fixedDuration;
        this.fixedDuration = fixedDuration;
        firePropertyChange("fixedDuration", oldValue, this.fixedDuration);
    }

    public int getWorkAllocated() {
        activate(ActivationPurpose.READ);
        return workAllocated;
    }

    public void setWorkAllocated(int workAllocated) {
        activate(ActivationPurpose.WRITE);
        int oldValue = this.workAllocated;
        this.workAllocated = workAllocated;
        firePropertyChange("workAllocated", oldValue, this.workAllocated);
    }

    /**
	 * Allocates <code>workAllocated</code> to this task - adds this to the work
	 * already allocated as opposed to {@link #setWorkAllocated(int)} which just
	 * does the usual setter thing. If this is the first work to be allocated,
	 * call {@link #setStartRemaining(Date)} with new start date. If the finish
	 * of this allocation is later than the current finish date move the finish
	 * date.
	 * 
	 * @param date
	 *           and time date the work is being done
	 * @param workAllocated
	 *           the amount of work planned to be done (in minutes)
	 */
    public void allocateWork(int allocation, StartFinishTimes sft) {
        activate(ActivationPurpose.WRITE);
        this.workAllocated = this.workAllocated + allocation;
        if (startRemaining == null) {
            setStartRemaining(new Date(sft.start));
        }
        if (getFinish().getTime() < sft.finish) {
            setFinish(new Date(sft.finish));
        }
    }

    /**
	 * Signifies that sufficient resource has been allocated to complete the
	 * remaining work
	 * 
	 * @return true if all necessary work has been allocated
	 */
    @Override
    public boolean isFullyAllocated() {
        activate(ActivationPurpose.READ);
        return (workRemaining <= workAllocated);
    }

    /**
	 * returns true if all predecessors have been completed
	 * 
	 * @return
	 */
    public boolean predecessorsAllocated() {
        activate(ActivationPurpose.READ);
        boolean result = true;
        for (AtomicTask t : predecessors) {
            if (!t.isFullyAllocated()) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void clearLevelling() {
        super.clearLevelling();
        activate(ActivationPurpose.WRITE);
        setWorkAllocated(0);
        if (finish == null) {
            finish = new Date();
        }
        this.finish.setTime(0);
    }

    @Override
    public void whereUsed(List<TaskReference> references, Task target) {
        throw new RuntimeException("nyi");
    }

    @Override
    public void readXML(XMLImporter importer, Element element) {
        super.readXML(importer, element);
        activate(ActivationPurpose.WRITE);
        assignedResource = importer.resolveResource(XML.readElementString(element, "assignedResource"));
        assignedUnits = XML.readElementDouble(element, "assignedUnits");
        workRemaining = XML.readElementInteger(element, "workRemaining");
        workAllocated = XML.readElementInteger(element, "workAllocated");
        workDone = XML.readElementInteger(element, "workDone");
        importer.save(this);
    }

    @Override
    public void writeXML(Element parentElement) {
        super.writeXML(parentElement);
        activate(ActivationPurpose.READ);
        XML.createElementReference(parentElement, "assignedResource", assignedResource);
        XML.createElement(parentElement, "assignedUnits", assignedUnits);
        XML.createElement(parentElement, "workRemaining", workRemaining);
        XML.createElement(parentElement, "workAllocated", workAllocated);
        XML.createElement(parentElement, "workDone", workDone);
    }

    /**
	 * This should only be called from {@link Resource#bookEffort()} to ensure
	 * that the resource doesn't get over booked. The effort wanted is added to
	 * the profile for the specified resource on the specified date
	 * 
	 * @param resource
	 * @param cal
	 * @param effortWanted
	 */
    public void bookEffort(IndividualResource resource, Calendar cal, int effortWanted) {
        activate(ActivationPurpose.WRITE);
        TaskResourceProfile trp = getTaskResourceProfile(resource);
        trp.bookEffort(cal, effortWanted);
        firePropertyChange("workPlanned", Integer.toString(effortWanted), " booked on " + cal.toString());
    }

    /**
	 * Returns the profile for the given resource, or a new one if none exists.
	 * Any newly created profile is also added to {@link #taskResourceProfiles}
	 * 
	 * @param resource
	 * @return
	 */
    public TaskResourceProfile getTaskResourceProfile(IndividualResource resource) {
        activate(ActivationPurpose.READ);
        TaskResourceProfile trp = null;
        for (TaskResourceProfile t : taskResourceProfiles) {
            if (t.getResource() == resource) {
                trp = t;
                break;
            }
        }
        if (trp == null) {
            activate(ActivationPurpose.WRITE);
            trp = new TaskResourceProfile(this, resource, propertyChangeSupport.getPropertyChangeListeners());
            taskResourceProfiles.add(trp);
        }
        return trp;
    }

    /**
	 * Calls {@link #init()} for the assigned resource to prepare that first.<br>
	 * <br>
	 * Makes sure {@link #taskResourceProfiles} has been created, then calls
	 * {@link #checkProfiles()} to ensure that each {@link IndividualResource}
	 * assigned has an associated profile. May want to re-write this if dynamic
	 * activation is used
	 * 
	 * @see uk.co.q3c.deplan.domain.AbstractPersistedObject#init()
	 */
    @Override
    public void initialise() {
        activate(ActivationPurpose.READ);
        if (taskResourceProfiles == null) {
            taskResourceProfiles = new ArrayList4<TaskResourceProfile>();
        }
    }

    /**
	 * Returns a list of all individual resources which either are assigned to
	 * this task, or have been assigned before and booked some time to this task.
	 * No record is kept of resources which were assigned but didn't book time
	 * 
	 * @return
	 */
    public List<IndividualResource> getAssignedResourceHistory() {
        activate(ActivationPurpose.READ);
        List<IndividualResource> list = new ArrayList<IndividualResource>(getAssignedResource().resources());
        for (TaskResourceProfile trp : taskResourceProfiles) {
            if (!list.contains(trp.getResource())) {
                list.add(trp.getResource());
            }
        }
        return list;
    }

    public List<TaskResourceProfile> getTaskResourceProfiles() {
        activate(ActivationPurpose.READ);
        return taskResourceProfiles;
    }

    void workLeftChanged(TaskResourceProfile taskResourceProfile) {
        activate(ActivationPurpose.READ);
        if (taskResourceProfile.getWorkLeft() > workRemaining) {
            updateWorkRemaining(taskResourceProfile.getWorkLeft());
        }
    }

    /**
	 * Updates work remaining to the specified value. Called instead of the
	 * setter method because that pushes the value down, and this method is used
	 * in response to changes being passed up from {@link TaskResourceProfile} -
	 * and that would cause a loop if the setter were used
	 * 
	 * @param workRemaining
	 */
    private void updateWorkRemaining(int workRemaining) {
        if (this.workRemaining == workRemaining) {
            return;
        }
        activate(ActivationPurpose.WRITE);
        int oldValue = this.workRemaining;
        this.workRemaining = workRemaining;
        if (workRemaining > 0) {
            setActualFinish(null);
        }
        firePropertyChange("workRemaining", oldValue, workRemaining);
        calculateTotalWork();
    }

    public void workDoneChanged() {
        activate(ActivationPurpose.READ);
        int t = 0;
        for (TaskResourceProfile trp : taskResourceProfiles) {
            t = t + trp.getTotalWorkDone();
        }
        setWorkDone(t);
    }

    /**
	 * Returns the work planned for the specified resource on the specified day.
	 * 
	 * @see #getWorkPlanned(DateIndex)
	 * @param resource
	 * @param dateIndex
	 * @return
	 */
    public int getWorkPlanned(IndividualResource resource, Calendar cal) {
        activate(ActivationPurpose.READ);
        TaskResourceProfile trp = getTaskResourceProfile(resource);
        return trp.getPlanned(cal.getTime());
    }

    /**
	 * Returns the total work planned for all resources on the specified date
	 * 
	 * @see #getWorkPlanned(IndividualResource, DateIndex)
	 * @param cal
	 * @return
	 */
    public int getWorkPlanned(Calendar cal) {
        activate(ActivationPurpose.READ);
        int total = 0;
        for (TaskResourceProfile trp : taskResourceProfiles) {
            total = total + trp.getPlanned(cal.getTime());
        }
        return total;
    }

    public void addToWorkDone(int workDone, IndividualResource ir, Calendar cal) {
        activate(ActivationPurpose.READ);
        TaskResourceProfile trp = getTaskResourceProfile(ir);
        trp.addToWorkDone(workDone, cal.getTime());
    }

    /**
	 * Removes listener from this and all {@link TaskResourceProfile}s
	 * 
	 * @see uk.co.q3c.deplan.domain.AbstractPersistedObject#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener);
        activate(ActivationPurpose.READ);
        for (TaskResourceProfile trp : taskResourceProfiles) {
            trp.removePropertyChangeListener(listener);
        }
    }

    @Override
    public TaskType taskType() {
        return null;
    }

    /**
	 * Passes the revised plan range to {@link TaskResourceProfile}s
	 * 
	 * @see TaskResourceProfile#setPlanRange(Date, TaskDao)
	 * @param planEnd
	 * @param taskDao
	 * @return
	 */
    public void setPlanRange(Date startDate, Date endDate) {
        activate(ActivationPurpose.READ);
        for (TaskResourceProfile trp : taskResourceProfiles) {
            trp.setPlanRange(startDate, endDate);
        }
    }
}
