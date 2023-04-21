package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import com.technoetic.xplanner.util.CollectionUtils;
import com.technoetic.xplanner.util.CollectionUtils.DoubleFilter;
import com.technoetic.xplanner.util.CollectionUtils.DoublePropertyFilter;

public class UserStory extends DomainObject implements Nameable, NoteAttachable, Describable {

    private String name;

    private String description;

    private int trackerId;

    private int iterationId;

    private Collection tasks = new HashSet();

    private Collection features = new HashSet();

    private Person customer;

    private double estimatedHours;

    private Double estimatedOriginalHours;

    private double actualHours;

    private double postponedHours;

    private double iterationStartEstimatedHours;

    private int priority;

    private int orderNo;

    private int previousOrderNo;

    private StoryDisposition disposition = StoryDisposition.PLANNED;

    private StoryStatus status = StoryStatus.DRAFT;

    public static final String ITERATION_START_ESTIMATED_HOURS = getValidProperty("iterationStartEstimatedHours");

    public static final String TASK_BASED_ESTIMATED_ORIGINAL_HOURS = getValidProperty("taskBasedEstimatedOriginalHours");

    public static final String TASK_BASED_COMPLETED_ORIGINAL_HOURS = getValidProperty("taskBasedCompletedOriginalHours");

    public static final String TASK_BASED_ADDED_ORIGINAL_HOURS = getValidProperty("taskBasedAddedOriginalHours");

    public static final String TASK_BASED_OVERESTIMATED_ORIGINAL_HOURS = getValidProperty("taskBasedOverestimatedOriginalHours");

    public static final String TASK_BASED_UNDERESTIMATED_ORIGINAL_HOURS = getValidProperty("taskBasedUnderestimatedOriginalHours");

    public static final String TASK_BASED_ESTIMATED_HOURS = getValidProperty("estimatedHours");

    public static final String TASK_BASED_ADJUSTED_ESTIMATED_HOURS = getValidProperty("adjustedEstimatedHours");

    public static final String TASK_BASED_COMPLETED_HOURS = getValidProperty("completedTaskHours");

    public static final String CACHED_TASK_BASED_ACTUAL_HOURS = getValidProperty("cachedActualHours");

    public static final String TASK_BASED_ACTUAL_HOURS = getValidProperty("actualHours");

    public static final String TASK_BASED_ADDED_HOURS = getValidProperty("estimatedHoursOfAddedTasks");

    public static final String TASK_BASED_POSTPONED_HOURS = getValidProperty("postponedHours");

    public static final String TASK_BASED_REMAINING_HOURS = getValidProperty("taskBasedRemainingHours");

    public static final String TASK_BASED_COMPLETED_REMAINING_HOURS = getValidProperty("taskBasedCompletedRemainingHours");

    public static final String TASK_BASED_OVERESTIMATED_HOURS = getValidProperty("overestimatedHours");

    public static final String TASK_BASED_UNDERESTIMATED_HOURS = getValidProperty("underestimatedHours");

    public static final String ADJUSTED_ESTIMATED_HOURS = getValidProperty("adjustedEstimatedHours");

    public static final String ESTIMATED_HOURS = getValidProperty("estimatedHours");

    public static final String STORY_ESTIMATED_HOURS = getValidProperty("totalHours");

    public static final String REMAINING_HOURS = getValidProperty("remainingHours");

    public static final String COMPLETED_HOURS = getValidProperty("completedHours");

    public static final String TOTAL_HOURS = getValidProperty("totalHours");

    public static final String TASK_BASED_TOTAL_HOURS = getValidProperty("taskBasedTotalHours");

    public static final String STORY_ESTIMATED_ORIGINAL_HOURS = getValidProperty("nonTaskBasedOriginalHours");

    public static final String TASK_ESTIMATED_ORIGINAL_HOURS = getValidProperty("taskEstimatedOriginalHours");

    public static final String TASK_BASED_ORIGINAL_HOURS = getValidProperty("taskBasedOriginalHours");

    public static final String TASK_ESTIMATED_HOURS_IF_STORY_ADDED = getValidProperty("taskEstimatedHoursIfStoryAdded");

    public static final String TASK_ESTIMATED_HOURS_IF_ORIGINAL_STORY = getValidProperty("taskEstimatedHoursIfOriginalStory");

    public static final String STORY_ESTIMATED_HOURS_IF_STORY_ADDED = getValidProperty("storyEstimatedHoursIfStoryAdded");

    public static final String POSTPONED_STORY_HOURS = getValidProperty("postponedStoryHours");

    private static String getValidProperty(String property) {
        return getValidProperty(UserStory.class, property);
    }

    public double getCachedActualHours() {
        if (actualHours == 0) {
            actualHours = getActualHours();
        }
        return actualHours;
    }

    public double getActualHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getActualHours();
            }
        });
    }

    public double getAdjustedEstimatedHours() {
        double adjustedEstimatedHours = 0;
        if (!tasks.isEmpty()) {
            Iterator itr = tasks.iterator();
            while (itr.hasNext()) {
                Task task = (Task) itr.next();
                adjustedEstimatedHours += task.getAdjustedEstimatedHours();
            }
            if (adjustedEstimatedHours == 0) {
                adjustedEstimatedHours = estimatedHours;
            }
        } else {
            adjustedEstimatedHours = getEstimatedHours();
        }
        return adjustedEstimatedHours;
    }

    public double getEstimatedHours() {
        if (tasks.size() > 0) return getTaskBasedEstimatedHours();
        return estimatedHours;
    }

    public StoryStatus getStatus() {
        return status;
    }

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StoryDisposition getDisposition() {
        return disposition;
    }

    public void setDisposition(StoryDisposition disposition) {
        this.disposition = disposition;
    }

    public Collection getFeatures() {
        return features;
    }

    public void setFeatures(Collection features) {
        this.features = features;
    }

    public int getIterationId() {
        return iterationId;
    }

    public void setIterationId(int iterationId) {
        this.iterationId = iterationId;
    }

    /**
    * @deprecated Has to be removed after the next release
    */
    public double getIterationStartEstimatedHours() {
        return iterationStartEstimatedHours;
    }

    /**
    * @deprecated Has to be removed after the next release
    */
    public void setIterationStartEstimatedHours(double tasksEstimateAtIterationStart) {
        this.iterationStartEstimatedHours = tasksEstimateAtIterationStart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEstimatedOriginalHours(Double newEstimatedOriginalHours) {
        this.estimatedOriginalHours = newEstimatedOriginalHours;
    }

    public double getEstimatedOriginalHours() {
        if (isStarted()) {
            return getEstimatedOriginalHoursField().doubleValue();
        } else {
            return getTaskBasedEstimatedOriginalHours();
        }
    }

    public double getTaskBasedEstimatedOriginalHours() {
        return CollectionUtils.sum(tasks, new DoublePropertyFilter(Task.ITERATION_START_ESTIMATED_HOURS));
    }

    public Double getEstimatedOriginalHoursField() {
        return estimatedOriginalHours;
    }

    public void setEstimatedOriginalHoursField(Double estimatedOriginalHours) {
        this.estimatedOriginalHours = estimatedOriginalHours;
    }

    public double getPostponedHours() {
        return postponedHours;
    }

    public void setPostponedHours(double postponedHours) {
        this.postponedHours = postponedHours;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getTaskBasedEstimatedHours() {
        double taskBasedEstimatedHours = 0;
        Iterator itr = tasks.iterator();
        while (itr.hasNext()) {
            Task task = (Task) itr.next();
            taskBasedEstimatedHours += task.getEstimatedHoursBasedOnActuals();
        }
        return taskBasedEstimatedHours;
    }

    public Collection getTasks() {
        return tasks;
    }

    public void setTasks(Collection tasks) {
        this.tasks = tasks;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public String toString() {
        return "UserStory(id=" + this.getId() + ", iterationId=" + iterationId + ", name=" + name + ", trackerId=" + this.getTrackerId() + ", name=" + this.getName() + ", title=" + this.getName() + ", lastUpdateTime=" + this.getLastUpdateTime() + ")";
    }

    public double getEstimatedHoursOfAddedTasks() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getAddedHours();
            }
        });
    }

    public double getCompletedTaskHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getCompletedHours();
            }
        });
    }

    public double getCompletedHours() {
        return isCompleted() ? getEstimatedHoursField() : 0.0;
    }

    public int getCustomerId() {
        if (getCustomer() == null) return 0;
        return getCustomer().getId();
    }

    public String getDispositionName() {
        return disposition != null ? disposition.getName() : null;
    }

    public double getTaskBasedAddedOriginalHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getAddedOriginalHours();
            }
        });
    }

    public double getTaskBasedTotalHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getEstimatedHours();
            }
        });
    }

    public double getTaskBasedCompletedOriginalHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getCompletedOriginalHours();
            }
        });
    }

    public double getTaskBasedOverestimatedOriginalHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getOverestimatedOriginalHours();
            }
        });
    }

    public double getTaskBasedUnderestimatedOriginalHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getUnderestimatedOriginalHours();
            }
        });
    }

    public double getOverestimatedHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getOverestimatedHours();
            }
        });
    }

    public double getTaskBasedCompletedRemainingHours() {
        if (tasks.size() == 0) {
            return 0.0;
        }
        double remainingHours = 0;
        Iterator itr = tasks.iterator();
        while (itr.hasNext()) {
            Task task = (Task) itr.next();
            remainingHours += task.getCompletedRemainingHours();
        }
        return remainingHours;
    }

    public double getTaskBasedRemainingHours() {
        if (tasks.size() == 0) {
            return estimatedHours;
        }
        double remainingHours = 0;
        boolean isTaskEstimatePresent = false;
        Iterator itr = tasks.iterator();
        while (itr.hasNext()) {
            Task task = (Task) itr.next();
            remainingHours += task.getRemainingHours();
            isTaskEstimatePresent |= (task.getEstimatedHours() > 0);
        }
        return isTaskEstimatePresent ? remainingHours : estimatedHours;
    }

    public double getRemainingHours() {
        return isCompleted() ? 0.0 : getEstimatedHoursField();
    }

    public double getUnderestimatedHours() {
        return CollectionUtils.sum(getTasks(), new DoubleFilter() {

            public double filter(Object o) {
                return ((Task) o).getUnderestimatedHours();
            }
        });
    }

    public String getStatusName() {
        return status != null ? status.getName() : null;
    }

    public void setStatusName(String statusName) {
        this.status = StoryStatus.fromName(statusName);
    }

    public boolean isCompleted() {
        if (tasks.size() > 0) {
            Iterator itr = tasks.iterator();
            while (itr.hasNext()) {
                Task task = (Task) itr.next();
                if (!task.isCompleted()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void setDispositionName(String dispositionName) {
        this.disposition = StoryDisposition.valueOf(dispositionName);
    }

    public double getTotalHours() {
        return getEstimatedHoursField();
    }

    protected double getEstimatedHoursField() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        setEstimatedHoursField(estimatedHours);
    }

    protected void setEstimatedHoursField(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public void start() {
        if (!isStarted()) {
            setEstimatedOriginalHours(new Double(getTaskBasedEstimatedOriginalHours()));
        }
        for (Iterator iterator = tasks.iterator(); iterator.hasNext(); ) {
            Task task = (Task) iterator.next();
            task.start();
        }
    }

    public double getNonTaskBasedOriginalHours() {
        return isAdded() ? 0.0 : getEstimatedHoursField();
    }

    public double getTaskEstimatedHoursIfStoryAdded() {
        if (isAdded()) {
            double result = getTaskBasedEstimatedOriginalHours();
            result += getSumOfTaskProperty(Task.ADDED_ORIGINAL_HOURS);
            return result;
        } else {
            return 0.0;
        }
    }

    boolean isAdded() {
        return StoryDisposition.ADDED.equals(getDisposition());
    }

    public double getTaskEstimatedHoursIfOriginalStory() {
        return isAdded() ? 0.0 : getTaskEstimatedOriginalHours();
    }

    public double getTaskBasedOriginalHours() {
        return isAdded() ? 0.0 : getTaskEstimatedOriginalHours();
    }

    public boolean isStarted() {
        return estimatedOriginalHours != null;
    }

    public double getTaskEstimatedOriginalHours() {
        return getEstimatedOriginalHours();
    }

    public double getStoryEstimatedHoursIfStoryAdded() {
        return isAdded() ? getEstimatedHoursField() : 0.0;
    }

    private double getSumOfTaskProperty(String name) {
        return CollectionUtils.sum(getTasks(), new DoublePropertyFilter(name));
    }

    public void postponeRemainingHours() {
        setPostponedHours(getPostponedHours() + getTaskBasedRemainingHours());
    }

    public double getPostponedStoryHours() {
        return getPostponedHours() > 0.0 ? getEstimatedHoursField() : 0.0;
    }

    public int getPreviousOrderNo() {
        return previousOrderNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.previousOrderNo = this.orderNo;
        this.orderNo = orderNo;
    }

    public void moveTo(Iteration targetIteration) {
        setIterationId(targetIteration.getId());
        targetIteration.getUserStories().add(this);
        if (targetIteration.isActive() && !isStarted()) {
            start();
        }
        if (targetIteration.isActive()) {
            setDisposition(StoryDisposition.ADDED);
            setTasksDisposition(TaskDisposition.ADDED);
        } else {
            setDisposition(StoryDisposition.PLANNED);
            setTasksDisposition(TaskDisposition.PLANNED);
        }
    }

    private void setTasksDisposition(TaskDisposition disposition) {
        Iterator iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = (Task) iterator.next();
            task.setDisposition(disposition);
        }
    }
}
