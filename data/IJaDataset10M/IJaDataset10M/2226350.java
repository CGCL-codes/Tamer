package com.ivis.xprocess.core.impl;

import java.util.HashSet;
import java.util.Set;
import com.ivis.xprocess.core.ArtifactReferenceGroup;
import com.ivis.xprocess.core.Category;
import com.ivis.xprocess.core.ChargeRecord;
import com.ivis.xprocess.core.Consumable;
import com.ivis.xprocess.core.Consumption;
import com.ivis.xprocess.core.Folder;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.core.Pricing;
import com.ivis.xprocess.core.ProjectForecast;
import com.ivis.xprocess.core.WorkPackageContainer;
import com.ivis.xprocess.core.Xprocess;
import com.ivis.xprocess.core.Xproject;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.impl.XchangeElementContainerImpl;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.util.Day;
import com.ivis.xprocess.util.IMoney;

/**
 * WorkPackage container is a transient object and is only ever created in
 * memory. It is a place to store a collection of Tasks and can be given to the
 * Gantt.
 */
public class WorkPackageContainerImpl extends XchangeElementContainerImpl implements WorkPackageContainer {

    private HashSet<Xtask> tasks = new HashSet<Xtask>();

    private Set<Xelement> rootElements = new HashSet<Xelement>();

    private Set<Xproject> projects = new HashSet<Xproject>();

    private String name;

    private boolean closed = false;

    public WorkPackageContainerImpl(IPersistenceHelper ph) {
        super(ph);
    }

    public void addTask(Xtask task) {
        if (task.getProject() != null) {
            projects.add(task.getProject());
        }
        tasks.add(task);
    }

    public void removeTask(Xtask task) {
        tasks.remove(task);
    }

    public Day getTargetStart() {
        return null;
    }

    public void setTargetStart(Day targetStartDay) {
    }

    public Day getTargetEnd() {
        return null;
    }

    public void setTargetEnd(Day day) {
    }

    public Day getStart() {
        return null;
    }

    public Day getEnd50() {
        return null;
    }

    public Day getEnd75() {
        return null;
    }

    public Day getEnd95() {
        return null;
    }

    public Day getStart(ProjectForecast projectForecast) {
        return null;
    }

    public Day getEnd50(ProjectForecast projectForecast) {
        return null;
    }

    public Day getEnd75(ProjectForecast projectForecast) {
        return null;
    }

    public Day getEnd95(ProjectForecast projectForecast) {
        return null;
    }

    public double getVariance() {
        return 0;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void close() {
    }

    public Xtask createTask(String name) {
        return null;
    }

    public Set<Xtask> getTasks() {
        return getTasksIncluding(true, true);
    }

    public Set<Xtask> getTasksIncluding(boolean includeClosed, boolean includeNotToBeScheduled) {
        return getAllTasksIncluding(true, true);
    }

    public Set<Xtask> getAllTasks() {
        return getAllTasksIncluding(true, true);
    }

    public Set<Xtask> getAllTasksIncluding(boolean includeClosed, boolean includeNotToBeScheduled) {
        Set<Xtask> allTasks = new HashSet<Xtask>();
        for (Xtask task : tasks) {
            if ((includeClosed || !task.isClosed()) && (includeNotToBeScheduled || !task.isNotToBeScheduled())) {
                allTasks.add(task);
            }
        }
        return allTasks;
    }

    public boolean hasAsMember(Xtask task) {
        return tasks.contains(task);
    }

    public Xproject getProject() {
        return null;
    }

    public Pricing getPricing() {
        return null;
    }

    public ChargeRecord[] getResourceChargeRecords(Day from, Day to) {
        return null;
    }

    public String getResourceCostCSV(Day from, Day to) {
        return null;
    }

    public ChargeRecord[] getConsumablesChargeRecords(Day from, Day to) {
        return null;
    }

    public String getConsumablesCostCSV(Day from, Day to) {
        return null;
    }

    public ChargeRecord[] getTotalCompletionValue(Day from, Day to) {
        return null;
    }

    public String getTotalCompletionValueCSV(Day from, Day to) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Day getFinishedDate() {
        return null;
    }

    public Consumption createConsumption(String name, Consumable consumable, Day day, int quantity) {
        return null;
    }

    public void removeConsumption(Consumption consumption) {
    }

    public Set<Consumption> getConsumptions() {
        return null;
    }

    public Xtask getTaskByName(String name) {
        return null;
    }

    public Day getClosedDate() {
        return null;
    }

    public void unClose() {
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public IMoney getResourceCost() {
        return null;
    }

    public IMoney getResourceCostToDate() {
        return null;
    }

    public ArtifactReferenceGroup createArtifactReferenceGroup(String artifactReferenceName, boolean multiple) {
        return null;
    }

    public ArtifactReferenceGroup getArtifactReferenceGroupByName(String name) {
        return null;
    }

    public Set<ArtifactReferenceGroup> getArtifactReferenceGroups() {
        return null;
    }

    public void removeArtifactReferenceGroup(ArtifactReferenceGroup artifactReference) {
    }

    public Set<Set<ChargeRecord>> getConsumablesCostDetails(Day from, Day to) {
        return null;
    }

    public Set<ChargeRecord> getConsumablesCostSummary(Day from, Day to) {
        return null;
    }

    public Set<Set<ChargeRecord>> getExecutionCostDetails(Day from, Day to) {
        return null;
    }

    public ChargeRecord[] getExecutionCostSummary(Day from, Day to) {
        return null;
    }

    public IMoney getConsumptionCost() {
        return null;
    }

    public IMoney getConsumptionCostToDate() {
        return null;
    }

    public IMoney getConsumptionCost(Day from, Day to) {
        return null;
    }

    public IMoney getResourceCost(Day from, Day to) {
        return null;
    }

    public IMoney getConsumptionCost(Day day) {
        return null;
    }

    public IMoney getResourceCost(Day day) {
        return null;
    }

    public void addRoot(Xelement element) {
        rootElements.add(element);
    }

    public boolean hasAsRoot(Xelement element) {
        for (Xelement rootElement : rootElements) {
            if ((rootElement != null) && (rootElement.getUuid() != null) && (element != null)) {
                if (rootElement.getUuid().equals(element.getUuid())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasProject(Xproject project) {
        return projects.contains(project);
    }

    public void refresh() {
        tasks = new HashSet<Xtask>();
        for (Xelement rootElement : rootElements) {
            if (rootElement instanceof Person) {
            }
            if (rootElement instanceof Category) {
                Category category = (Category) rootElement;
                for (Xelement element : category.getMembersOfCategoryIn(this.getPersistenceHelper().getRootExchangeElementContainer(), Xtask.class)) {
                    if (element instanceof Xtask) {
                        Xtask task = (Xtask) element;
                        if (!(task.getParent() instanceof Xproject)) {
                            addTask(task);
                        }
                    }
                }
            }
            if (rootElement instanceof Folder) {
                Folder folder = (Folder) rootElement;
                for (Xtask task : folder.getTaskMembers()) {
                    addTask(task);
                }
            }
            if (rootElement instanceof Xproject) {
                Xproject project = (Xproject) rootElement;
                for (Xtask task : project.getTasks()) {
                    addTask(task);
                }
            } else if (rootElement instanceof Xtask) {
                Xtask task = (Xtask) rootElement;
                addTask(task);
            }
        }
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public Xprocess getProcess() {
        if (getProject() != null) {
            return getProject().getProcess();
        }
        return null;
    }
}
