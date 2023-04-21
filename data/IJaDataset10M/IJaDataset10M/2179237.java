package org.libreplan.business.planner.limiting.entities;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.joda.time.LocalDate;
import org.libreplan.business.planner.entities.DayAssignment;
import org.libreplan.business.planner.entities.GenericDayAssignment;
import org.libreplan.business.planner.entities.GenericResourceAllocation;
import org.libreplan.business.planner.entities.ResourceAllocation;
import org.libreplan.business.planner.entities.SpecificDayAssignment;
import org.libreplan.business.planner.entities.SpecificResourceAllocation;
import org.libreplan.business.planner.limiting.entities.Gap.GapOnQueue;
import org.libreplan.business.resources.entities.LimitingResourceQueue;
import org.libreplan.business.resources.entities.Resource;
import org.libreplan.business.workingday.EffortDuration;

public abstract class AllocationSpec {

    public static AllocationSpec invalidOn(GapOnQueue gap) {
        return new InvalidAllocationAttempt(gap);
    }

    public static AllocationSpec validOn(LimitingResourceQueueElement element, GapOnQueue gap, DateAndHour start, DateAndHour endExclusive, int[] assignableHours) {
        return new ValidAllocationAttempt(element, gap, start, endExclusive, assignableHours);
    }

    private final GapOnQueue originalGap;

    private List<LimitingResourceQueueElement> unscheduled = new ArrayList<LimitingResourceQueueElement>();

    protected AllocationSpec(GapOnQueue originalGap) {
        Validate.notNull(originalGap);
        this.originalGap = originalGap;
    }

    public abstract boolean isValid();

    public abstract List<DayAssignment> getAssignmentsFor(ResourceAllocation<?> allocation, Resource resource) throws IllegalStateException;

    public abstract LimitingResourceQueueElement getElement() throws IllegalStateException;

    public abstract DateAndHour getStartInclusive() throws IllegalStateException;

    public abstract DateAndHour getEndExclusive() throws IllegalStateException;

    public Gap getGap() {
        return originalGap.getGap();
    }

    public LimitingResourceQueue getQueue() {
        return originalGap.getOriginQueue();
    }

    public boolean isAppropriative() {
        return !unscheduled.isEmpty();
    }

    public void setUnscheduledElements(List<LimitingResourceQueueElement> queueElements) {
        unscheduled.addAll(queueElements);
    }

    public List<LimitingResourceQueueElement> getUnscheduledElements() {
        return unscheduled;
    }
}

class InvalidAllocationAttempt extends AllocationSpec {

    private static final String INVALID_ALLOCATION_ON_GAP = "invalid allocation on gap";

    InvalidAllocationAttempt(GapOnQueue originalGap) {
        super(originalGap);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public List<DayAssignment> getAssignmentsFor(ResourceAllocation<?> allocation, Resource resource) {
        throw new IllegalStateException(INVALID_ALLOCATION_ON_GAP);
    }

    @Override
    public LimitingResourceQueueElement getElement() throws IllegalStateException {
        throw new IllegalStateException(INVALID_ALLOCATION_ON_GAP);
    }

    @Override
    public DateAndHour getEndExclusive() throws IllegalStateException {
        throw new IllegalStateException(INVALID_ALLOCATION_ON_GAP);
    }

    @Override
    public DateAndHour getStartInclusive() throws IllegalStateException {
        throw new IllegalStateException(INVALID_ALLOCATION_ON_GAP);
    }
}

class ValidAllocationAttempt extends AllocationSpec {

    private final LimitingResourceQueueElement element;

    private final DateAndHour start;

    private final DateAndHour end;

    private final int[] assignableHours;

    public ValidAllocationAttempt(LimitingResourceQueueElement element, GapOnQueue gap, DateAndHour startInclusive, DateAndHour endExclusive, int[] assignableHours) {
        super(gap);
        Validate.notNull(element);
        Validate.notNull(startInclusive);
        Validate.notNull(endExclusive);
        Validate.notNull(assignableHours);
        Validate.isTrue(endExclusive.isAfter(startInclusive));
        this.element = element;
        this.start = startInclusive;
        this.end = endExclusive;
        Validate.isTrue(assignableHours.length == toFiniteList(start.daysUntil(end)).size());
        this.assignableHours = assignableHours.clone();
    }

    private List<LocalDate> toFiniteList(Iterable<LocalDate> daysUntil) {
        List<LocalDate> result = new ArrayList<LocalDate>();
        for (LocalDate each : daysUntil) {
            result.add(each);
        }
        return result;
    }

    @Override
    public List<DayAssignment> getAssignmentsFor(ResourceAllocation<?> allocation, Resource resource) throws IllegalStateException {
        List<LocalDate> days = toFiniteList(start.daysUntil(end));
        assert assignableHours.length == days.size();
        if (allocation instanceof SpecificResourceAllocation) {
            return createSpecific(days, (SpecificResourceAllocation) allocation, resource);
        } else {
            return createGeneric(days, (GenericResourceAllocation) allocation, resource);
        }
    }

    private List<DayAssignment> createSpecific(List<LocalDate> days, SpecificResourceAllocation allocation, Resource resource) {
        List<DayAssignment> result = new ArrayList<DayAssignment>();
        int i = 0;
        for (LocalDate each : days) {
            EffortDuration hours = EffortDuration.hours(assignableHours[i]);
            result.add(SpecificDayAssignment.create(each, hours, resource));
            i++;
        }
        return result;
    }

    private List<DayAssignment> createGeneric(List<LocalDate> days, GenericResourceAllocation allocation, Resource resource) {
        List<DayAssignment> result = new ArrayList<DayAssignment>();
        int i = 0;
        for (LocalDate each : days) {
            EffortDuration hours = EffortDuration.hours(assignableHours[i]);
            result.add(GenericDayAssignment.create(each, hours, resource));
            i++;
        }
        return result;
    }

    @Override
    public LimitingResourceQueueElement getElement() {
        return element;
    }

    @Override
    public DateAndHour getEndExclusive() throws IllegalStateException {
        return end;
    }

    @Override
    public DateAndHour getStartInclusive() throws IllegalStateException {
        return start;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
