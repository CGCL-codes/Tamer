package uk.ac.lkl.migen.system.cdst.model;

import java.util.*;
import uk.ac.lkl.migen.system.ai.analysis.Verification;
import uk.ac.lkl.migen.system.ai.feedback.intervention.InterventionShownOccurrence;
import uk.ac.lkl.migen.system.ai.feedback.strategy.FeedbackStrategyType;
import uk.ac.lkl.migen.system.ai.um.*;
import uk.ac.lkl.migen.system.ai.um.occurrence.IndicatorOccurrence;
import uk.ac.lkl.migen.system.ai.um.occurrence.StateIndicatorOccurrence;
import uk.ac.lkl.migen.system.cdst.model.event.*;
import uk.ac.lkl.migen.system.cdst.ui.goal.GoalStatus;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.task.TaskIdentifier;
import uk.ac.lkl.migen.system.task.goal.*;
import uk.ac.lkl.migen.system.util.MiGenUtilities;

/**
 * This class contains all the indicators that have happened to a 
 * student while interaction with one construction (i.e. ExpresserModel). 
 * 
 * @author $Author: sergut $
 * @version $Revision: 7176 $
 * @version $Date: 2010-10-15 09:00:35 +0100 (Fri, 15 Oct 2010) $
 * 
 */
public class StudentConstructionIndicatorModel implements SingleStudentGoalTrackingModel {

    /**
     * The UserSet this model belongs to
     */
    private UserSet userSet;

    /**
     * The ExpresserModel/construction this model refers to
     */
    private ExpresserModel expresserModel;

    /**
     * Timestamp of the first indicator added to this model for this student
     */
    private Long firstIndicatorStudentTime;

    /**
     * Timestamp of the first indicator added to any model of any student.
     * 
     * TODO: This should be session-dependent, probably implemented with a map
     */
    private static Long firstIndicatorGlobalTime = Long.MAX_VALUE;

    /**
     * Timestamp of the last indicator added to this model until now
     */
    private Long lastIndicatorTime;

    /**
     * Max possible timestamp of indicators added to this model. If
     * an indicator with a larger timestamp is attempted to be added
     * to this model, it will be discarded.  
     */
    private Long maxAcceptedTime;

    /**
     * A list of all the events (i.e. event-indicators) stored in this model. 
     */
    private List<EventIndicatorOccurrence> eventOccurrenceList;

    /**
     * A list of all the events (i.e. event-indicators) stored in this model
     * that fit into the time constraints. Is a sublist from the former one.
     */
    private List<EventIndicatorOccurrence> eventOccurrenceFilteredList;

    /**
     * A map from state-indicators to lists with all the state-indicators of that
     * type that have been stored in this model. 
     */
    private Map<StateIndicator, List<StateIndicatorOccurrence>> stateOccurrenceListMap;

    /**
     * A map from state-indicators to lists with all the state-indicators of that
     * type that have been stored in this model and fit into the time constraints. 
     * The values in this map are sublists of the the corresponding lists in the 
     * former one.   
     */
    private Map<StateIndicator, List<StateIndicatorOccurrence>> stateOccurrenceFilteredListMap;

    private List<IndicatorModelListener> listeners;

    private static final boolean showStatusMessages = false;

    public StudentConstructionIndicatorModel(UserSet userSet, ExpresserModel expresserModel, Long maxTime) {
        this.userSet = userSet;
        this.expresserModel = expresserModel;
        this.maxAcceptedTime = maxTime;
        this.eventOccurrenceList = new ArrayList<EventIndicatorOccurrence>();
        this.eventOccurrenceFilteredList = new ArrayList<EventIndicatorOccurrence>();
        this.stateOccurrenceListMap = new HashMap<StateIndicator, List<StateIndicatorOccurrence>>();
        this.stateOccurrenceFilteredListMap = new HashMap<StateIndicator, List<StateIndicatorOccurrence>>();
        this.listeners = new ArrayList<IndicatorModelListener>();
        this.firstIndicatorStudentTime = null;
        this.lastIndicatorTime = null;
    }

    /**
     * Returns this model's UserSet.
     * 
     * @return this model's UserSet.
     */
    public UserSet getUserSet() {
        return userSet;
    }

    /**
     * Returns this model's construction
     * 
     * @return this model's construction
     */
    public ExpresserModel getExpresserModel() {
        return expresserModel;
    }

    /**
     * Returns the timestamp of the first indicator added to this model
     * 
     * @return the timestamp of the first indicator added to this model
     */
    public Long getFirstIndicatorTime() {
        return firstIndicatorStudentTime;
    }

    /**
     * Returns the timestamp of the first indicator added to this model
     * 
     * @return the timestamp of the first indicator added to this model
     */
    public Long getFirstIndicatorTimeForAllStudents() {
        return firstIndicatorGlobalTime;
    }

    /**
     * Returns the timestamp of the last indicator added to this model until now
     * 
     * @return the timestamp of the last indicator added to this model until now
     */
    public Long getlastIndicatorTime() {
        return lastIndicatorTime;
    }

    /**
     * Sets the max time at which an indicator can be added to this model. Indicators
     * that happen after this time are discarded. 
     */
    public void setMaxTime(Long maxTime) {
        if (this.maxAcceptedTime == maxTime) return;
        updateFilteredIndicatorLists(maxTime);
        this.maxAcceptedTime = maxTime;
    }

    private void updateFilteredIndicatorLists(long maxTime) {
        for (IndicatorModelListener l : listeners) l.modelClean();
        eventOccurrenceFilteredList = new ArrayList<EventIndicatorOccurrence>();
        for (EventIndicatorOccurrence occurrence : eventOccurrenceList) copyFilteredEventOccurrence(occurrence, maxTime);
        this.stateOccurrenceFilteredListMap = new HashMap<StateIndicator, List<StateIndicatorOccurrence>>();
        for (List<StateIndicatorOccurrence> stateOccurrenceList : stateOccurrenceListMap.values()) for (StateIndicatorOccurrence occurrence : stateOccurrenceList) copyFilteredStateOccurrence(occurrence, maxTime);
    }

    private boolean copyFilteredEventOccurrence(EventIndicatorOccurrence occurrence, long maxTime) {
        if (occurrence.getTimestampLong() > maxTime) {
            return false;
        }
        eventOccurrenceFilteredList.add(occurrence);
        fireFilteredEventCopied(new EventOccurrenceEvent(this, occurrence));
        return true;
    }

    private boolean copyFilteredStateOccurrence(StateIndicatorOccurrence occurrence, long maxTime) {
        if (occurrence.getTimestampLong() > maxTime) return false;
        StateIndicator indicator = occurrence.getIndicator();
        List<StateIndicatorOccurrence> stateOccurrenceList = stateOccurrenceFilteredListMap.get(indicator);
        if (stateOccurrenceList == null) {
            stateOccurrenceList = new ArrayList<StateIndicatorOccurrence>();
            stateOccurrenceFilteredListMap.put(indicator, stateOccurrenceList);
        }
        stateOccurrenceList.add(occurrence);
        fireFilteredStateCopied(new StateOccurrenceEvent(this, occurrence));
        return true;
    }

    private void fireFilteredEventCopied(EventOccurrenceEvent e) {
        for (IndicatorModelListener l : listeners) l.eventIndicatorOccurrenceAdded(e);
    }

    private void fireFilteredStateCopied(StateOccurrenceEvent e) {
        for (IndicatorModelListener l : listeners) l.stateIndicatorOccurrenceAdded(e);
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void dropFutureIndicatorOccurrences() {
        if (lastIndicatorTime == null) return;
        int occurrencesCount = eventOccurrenceList.size();
        int eventCount = occurrencesCount;
        int dropEventCount = 0;
        for (int i = occurrencesCount - 1; i >= 0; i--) {
            IndicatorOccurrence<?> eventIndicator = eventOccurrenceList.get(i);
            if (isTooLate(eventIndicator)) {
                eventOccurrenceList.remove(i);
                dropEventCount++;
            }
        }
        int stateCount = 0;
        int dropStateCount = 0;
        for (List<StateIndicatorOccurrence> stateOccurrences : stateOccurrenceListMap.values()) {
            occurrencesCount = stateOccurrences.size();
            stateCount += occurrencesCount;
            for (int i = occurrencesCount - 1; i >= 0; i--) {
                IndicatorOccurrence<?> stateIndicator = stateOccurrences.get(i);
                if (isTooLate(stateIndicator)) {
                    stateOccurrences.remove(i);
                    dropStateCount++;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Deprecated
    private void dropFutureIndicatorsMoreEfficiently() {
        int occurrencesCount = eventOccurrenceList.size();
        int firstElementOutIdx = occurrencesCount;
        for (int i = 0; i < occurrencesCount; i++) {
            IndicatorOccurrence<?> eventIndicator = eventOccurrenceList.get(i);
            if (isTooLate(eventIndicator)) {
                firstElementOutIdx = i;
                break;
            }
        }
        for (int i = occurrencesCount - 1; i >= firstElementOutIdx; i--) {
            eventOccurrenceList.remove(i);
        }
        for (List<StateIndicatorOccurrence> stateOccurrences : stateOccurrenceListMap.values()) {
            occurrencesCount = stateOccurrences.size();
            firstElementOutIdx = occurrencesCount;
            for (int i = 0; i < occurrencesCount; i++) {
                IndicatorOccurrence<?> eventIndicator = stateOccurrences.get(i);
                if (isTooLate(eventIndicator)) {
                    firstElementOutIdx = i;
                    break;
                }
            }
            for (int i = occurrencesCount - 1; i >= firstElementOutIdx; i--) {
                stateOccurrences.remove(i);
            }
        }
    }

    /**
     * Returns true if the indicator (event or state) occurrence's 
     * timestamp is beyond the accepted max time, false otherwise 
     * 
     * @param indicatorOccurrence the indicator occurrence
     * 
     * @return true if the indicator (event or state) occurrence's 
     *   timestamp is beyond the accepted max time, false otherwise
     */
    @Deprecated
    private boolean isTooLate(IndicatorOccurrence<?> indicatorOccurrence) {
        Long indicatorOccurrenceTime = indicatorOccurrence.getTimestampLong();
        if (indicatorOccurrenceTime > this.maxAcceptedTime) return true; else return false;
    }

    /**
     * Add an event indicator occurrence to this model.
     * 
     * @param occurrence the indicator occurrence to add.
     */
    public void addEventIndicatorOccurrence(EventIndicatorOccurrence occurrence) {
        long eventTimestamp = occurrence.getTimestampLong();
        updateIndicatorTimeBounds(eventTimestamp);
        eventOccurrenceList.add(occurrence);
        copyFilteredEventOccurrence(occurrence, this.maxAcceptedTime);
    }

    /**
     * Add a state indicator occurrence to this model.
     * 
     * @param occurrence the indicator occurrence to add.
     *  
     */
    public void addStateIndicatorOccurrence(StateIndicatorOccurrence occurrence) {
        long stateTimestamp = occurrence.getTimestampLong();
        updateIndicatorTimeBounds(stateTimestamp);
        StateIndicator stateIndicator = occurrence.getIndicator();
        List<StateIndicatorOccurrence> stateOccurrences = stateOccurrenceListMap.get(stateIndicator);
        if (stateOccurrences == null) {
            stateOccurrences = new ArrayList<StateIndicatorOccurrence>();
            stateOccurrenceListMap.put(stateIndicator, stateOccurrences);
        }
        stateOccurrences.add(occurrence);
        copyFilteredStateOccurrence(occurrence, this.maxAcceptedTime);
    }

    /**
     * Updates the time of the first and last indicators if the given 
     * timestamp precedes or postcedes the current values.
     * 
     * The time of the first indicator for all students and models is also
     * updated when appropriate.  
     * 
     * @param timestamp a timestamp
     */
    private void updateIndicatorTimeBounds(long timestamp) {
        if (firstIndicatorStudentTime == null) {
            firstIndicatorStudentTime = timestamp;
        } else {
            if (firstIndicatorStudentTime > timestamp) {
                firstIndicatorStudentTime = timestamp;
                if (firstIndicatorGlobalTime > timestamp) {
                    firstIndicatorGlobalTime = timestamp;
                }
            }
        }
        if (lastIndicatorTime == null) {
            lastIndicatorTime = timestamp;
        } else {
            lastIndicatorTime = Math.max(lastIndicatorTime, timestamp);
        }
    }

    /**
     * Returns the number of occurrences of the given indicator. 
     * 
     * @param indicator the indicator 
     * 
     * @return the number of occurrences of the given indicator.
     */
    public int getCount(IndicatorClass indicator) {
        if (indicator instanceof EventIndicator) return getEventIndicatorCount((EventIndicator) indicator); else if (indicator instanceof StateIndicator) return getStateIndicatorCount((StateIndicator) indicator); else throw new IllegalArgumentException("Indicator is neither event nor state.");
    }

    public int getEventIndicatorCount(EventIndicator eventIndicator) {
        int result = 0;
        for (EventIndicatorOccurrence indicatorOccurrence : eventOccurrenceFilteredList) {
            IndicatorClass indicator = indicatorOccurrence.getIndicator();
            if (indicator == eventIndicator) result++;
        }
        return result;
    }

    public int getStateIndicatorCount(StateIndicator stateIndicator) {
        for (StateIndicator indicator : stateOccurrenceFilteredListMap.keySet()) {
            if (indicator == stateIndicator) return stateOccurrenceFilteredListMap.get(indicator).size();
        }
        return 0;
    }

    public int getTotalIndicatorCount() {
        int result = 0;
        result += eventOccurrenceFilteredList.size();
        for (Collection<StateIndicatorOccurrence> stateOccurrenceCollection : stateOccurrenceFilteredListMap.values()) result += stateOccurrenceCollection.size();
        return result;
    }

    public List<EventIndicatorOccurrence> getEventOccurrences() {
        return Collections.unmodifiableList(eventOccurrenceFilteredList);
    }

    public List<StateIndicatorOccurrence> getStateOccurrences(StateIndicator stateIndicator) {
        List<StateIndicatorOccurrence> occurrences;
        occurrences = stateOccurrenceFilteredListMap.get(stateIndicator);
        if (occurrences == null) return Collections.emptyList(); else return Collections.unmodifiableList(occurrences);
    }

    @SuppressWarnings("unused")
    private void fireEventOccurrenceAdded(EventIndicatorOccurrence occurrence) {
        EventOccurrenceEvent e = new EventOccurrenceEvent(this, occurrence);
        for (int i = 0; i < listeners.size(); i++) {
            IndicatorModelListener listener = listeners.get(i);
            listener.eventIndicatorOccurrenceAdded(e);
        }
    }

    @SuppressWarnings("unused")
    private void fireStateOccurrenceAdded(StateIndicatorOccurrence occurrence) {
        StateOccurrenceEvent e = new StateOccurrenceEvent(this, occurrence);
        for (int i = 0; i < listeners.size(); i++) {
            IndicatorModelListener listener = listeners.get(i);
            listener.stateIndicatorOccurrenceAdded(e);
        }
    }

    @SuppressWarnings("unused")
    private void fireEventOccurrenceRemoved(EventIndicatorOccurrence occurrence) {
        EventOccurrenceEvent e = new EventOccurrenceEvent(this, occurrence);
        for (int i = 0; i < listeners.size(); i++) {
            IndicatorModelListener listener = listeners.get(i);
            listener.eventIndicatorOccurrenceRemoved(e);
        }
    }

    @SuppressWarnings("unused")
    private void fireStateOccurrenceRemoved(StateIndicatorOccurrence occurrence) {
        StateOccurrenceEvent e = new StateOccurrenceEvent(this, occurrence);
        for (int i = 0; i < listeners.size(); i++) {
            IndicatorModelListener listener = listeners.get(i);
            listener.stateIndicatorOccurrenceRemoved(e);
        }
    }

    public void addIndicatorModelListener(IndicatorModelListener listener) {
        listeners.add(listener);
    }

    public void removeIndicatorModelListener(IndicatorModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the status of the student (e.g. inactive, working, waiting to be helped by teacher, etc). 
     * 
     * @return the status of the student 
     */
    public StudentStatus getStudentStatus() {
        List<EventIndicatorOccurrence> eventList = getEventOccurrences();
        if (eventList.isEmpty()) {
            if (showStatusMessages) System.out.println("Empty list of indicators: ABSENT.");
            return StudentStatus.ABSENT;
        }
        if (isWaitingForTeacher()) return StudentStatus.WAITING_FOR_TEACHER;
        if (isInactive()) return StudentStatus.INACTIVE;
        return StudentStatus.WORKING;
    }

    private boolean isWaitingForTeacher() {
        List<EventIndicatorOccurrence> eventList = getEventOccurrences();
        List<EventIndicatorOccurrence> sortedEventList = new ArrayList<EventIndicatorOccurrence>();
        for (EventIndicatorOccurrence occurrence : eventList) {
            if (sortedEventList.isEmpty()) sortedEventList.add(occurrence);
            for (int i = 0; i < sortedEventList.size(); i++) {
                if (occurrence.getTimestampLong() > sortedEventList.get(i).getTimestampLong()) {
                    sortedEventList.add(i, occurrence);
                    break;
                }
            }
        }
        int size = Math.min(10, sortedEventList.size());
        for (int i = 0; i < size; i++) {
            EventIndicatorOccurrence recentEventOccurrence = sortedEventList.get(i);
            if (recentEventOccurrence.getIndicator() == IndicatorClass.INTERVENTION_SHOWN) {
                InterventionShownOccurrence interventionShownOccurrence = (InterventionShownOccurrence) recentEventOccurrence;
                FeedbackStrategyType type = interventionShownOccurrence.getIntervention().getFeedbackStrategyType();
                if (type == FeedbackStrategyType.CALL_TEACHER) return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the 'inactive' state of the student is true, false otherwise. 
     *  
     * @return true if the 'inactive' state of the student is true, false otherwise.
     */
    private boolean isInactive() {
        List<StateIndicatorOccurrence> inactiveStateOccurrenceList = getStateOccurrences(IndicatorClass.INACTIVE_STUDENT);
        if (inactiveStateOccurrenceList.isEmpty()) return true;
        int lastChange = inactiveStateOccurrenceList.size() - 1;
        Verification currentState = (Verification) inactiveStateOccurrenceList.get(lastChange);
        return currentState.getValue();
    }

    /**
     * Returns true if the given indicator happened too long ago, i.e. more time ago than a 
     * lesson is long, false otherwise.  
     * 
     * @param indicatorOccurrence the indicator
     * 
     * @return true if the given indicator happened too long ago, i.e. more time ago than a 
     * lesson is long, false otherwise. 
     */
    @SuppressWarnings("unused")
    @Deprecated
    private boolean happenedTooLongAgo(IndicatorOccurrence<? extends IndicatorClass> indicatorOccurrence) {
        long now = System.currentTimeMillis();
        long timeDifference = now - indicatorOccurrence.getTimestampLong();
        if (timeDifference > MiGenUtilities.getLessonTimeLong()) return true; else return false;
    }

    /**
     * Returns the most recent time in which an indicator was added to the model.
     * 
     * @return the most recent time in which an indicator was added to the model.
     */
    public long getMostRecentIndicatorOccurrenceTimestamp() {
        long result = 0L;
        for (EventIndicatorOccurrence occurrence : eventOccurrenceFilteredList) result = Math.max(result, occurrence.getTimestampLong());
        for (List<StateIndicatorOccurrence> stateOccurrences : stateOccurrenceFilteredListMap.values()) {
            for (StateIndicatorOccurrence occurrence : stateOccurrences) result = Math.max(result, occurrence.getTimestampLong());
        }
        return result;
    }

    public Long getMaxAcceptedTime() {
        return maxAcceptedTime;
    }

    /**
     * Returns the status of the given goal for this student and this expresser model
     *  
     * @param goal the goal
     * 
     * @return the status of the given goal for this student and this expresser model
     */
    public GoalStatus getGoalStatus(Goal goal) {
        if (goal == null) return GoalStatus.NOT_ACHIEVED_YET;
        Long lastGoalTimestamp = 0L;
        GoalStatus lastGoalStatus = GoalStatus.NOT_ACHIEVED_YET;
        List<EventIndicatorOccurrence> eventOccurrenceList = getEventOccurrences();
        for (EventIndicatorOccurrence occurrence : eventOccurrenceList) {
            if (occurrence instanceof GoalEventOccurrence) {
                GoalEventOccurrence goalEventOccurrence = (GoalEventOccurrence) occurrence;
                if (goal.equals(goalEventOccurrence.getGoal())) {
                    if (goalEventOccurrence.getTimestampLong() > lastGoalTimestamp && goalEventOccurrence.isSystemDetected()) {
                        lastGoalStatus = fromInt2GoalStatus(goalEventOccurrence.getAchievementState());
                        lastGoalTimestamp = goalEventOccurrence.getTimestampLong();
                    }
                }
            }
        }
        return lastGoalStatus;
    }

    private GoalStatus fromInt2GoalStatus(int achievementState) {
        switch(achievementState) {
            case GoalEventOccurrence.ACHIEVED:
                return GoalStatus.ACHIEVED;
            case GoalEventOccurrence.NOT_ACHIEVED:
                return GoalStatus.UN_ACHIEVED;
            default:
                return GoalStatus.NOT_ACHIEVED_YET;
        }
    }

    /**
     * Returns the number of achieved goals up to know.
     * 
     * @return the number of achieved goals up to know.
     */
    public int getAccomplishedGoalCount() {
        Map<Goal, GoalStatus> statusMap = new HashMap<Goal, GoalStatus>();
        Map<Goal, Long> timestampMap = new HashMap<Goal, Long>();
        List<EventIndicatorOccurrence> eventOccurrenceList = getEventOccurrences();
        for (EventIndicatorOccurrence occurrence : eventOccurrenceList) {
            if (occurrence instanceof GoalEventOccurrence) {
                GoalEventOccurrence goalEventOccurrence = (GoalEventOccurrence) occurrence;
                Goal nextGoal = goalEventOccurrence.getGoal();
                Long nextTime = goalEventOccurrence.getTimestampLong();
                Long oldTime = timestampMap.get(nextGoal);
                if (oldTime == null || nextTime > oldTime) {
                    statusMap.put(nextGoal, fromInt2GoalStatus(goalEventOccurrence.getAchievementState()));
                    timestampMap.put(nextGoal, nextTime);
                }
            }
        }
        int result = 0;
        for (GoalStatus status : statusMap.values()) {
            if (status == GoalStatus.ACHIEVED) result++;
        }
        return result;
    }

    @Override
    public void addGoalListener(GoalListener l) {
    }

    @Override
    public void removeGoalListener(GoalListener l) {
    }

    /**
     * Returns the status of this goal for this student for this task (for 
     * the current instance, i.e. expresser model).
     * 
     * Returns null if the provided student is not the same as this model's student. 
     * 
     * @param student the student, should be the same as this model's student
     * @param task the task 
     * @param goal the goal
     * 
     * @return the status of this goal for this student (for the current task instance/expresser model).
     */
    @Override
    public GoalStatus getGoalStatus(UserSet student, TaskIdentifier task, Goal goal) {
        if (!getUserSet().equals(student)) return null;
        return this.getGoalStatus(goal);
    }

    @Override
    public List<Goal> getGoals(TaskIdentifier taskId) {
        return GoalRepository.getGoals();
    }
}
