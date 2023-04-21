package com.jrefinery.ibd;

import java.util.*;
import com.jrefinery.finance.*;
import com.jrefinery.finance.data.reference.*;

/**
 * A simple implementation of the JFinance interfaces for storing and retrieving business day
 * calendars.  The standard interfaces allow for a multi-state, time-dependent model...but this
 * simple implementation has only one state and ignores time.
 * <P>
 * An important property of the model is that its contents can only be modified using the defined
 * interfaces.  We have to be careful not to store objects in the model that the caller has a
 * reference to (and could modify directly).  Cloning is one way of ensuring this doesn't happen,
 * and making use of immutable objects (e.g. java.lang.String) is another.
 */
public class SimpleBusinessDayCalendarModel implements BusinessDayCalendarsToModel, BusinessDayCalendarsFromModel {

    /** Storage for the calendars; */
    private List calendars;

    /** Flag that indicates whether or not the model has been modified (the flag is reset whenever
      the data is saved); */
    private boolean dirty;

    /** Keeps track of the last id number allocated by the model; */
    private long lastId;

    /**
   * Default constructor - builds a new and empty model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   */
    public SimpleBusinessDayCalendarModel() {
        this.calendars = new ArrayList();
        this.dirty = false;
        this.lastId = 0;
    }

    /**
   * Returns true if the model has been modified (since the last save), and false otherwise.
   * @return A boolean that indicates whether or not the data in the model has been changed since
   *         it was last saved;
   */
    public boolean isDirty() {
        return this.dirty;
    }

    /**
   * Sets a flag that indicates whether or not the model has been modified since it was last
   * saved.
   * @param dirty The flag;
   */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
   * Returns an identification number that hasn't been used in the model yet.
   * @return An unused id number;
   */
    public long getUnusedId() {
        return ++lastId;
    }

    /**
   * Returns the number of business day calendars in the model.
   * @return The number of business day calendars in the model;
   */
    public int getBusinessDayCalendarCount() {
        return calendars.size();
    }

    /**
   * Returns the details of the calendar with the specified name - throws a
   * NoMatchingEntityException if there is no calendar with that name in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param name The name of the calendar;
   * @return The business day calendar with the specified name;
   */
    public BusinessDayCalendar getBusinessDayCalendar(String name) throws ModelException {
        Iterator iterator = calendars.iterator();
        while (iterator.hasNext()) {
            BusinessDayCalendar current = (BusinessDayCalendar) iterator.next();
            if (current.getName().equals(name)) {
                return (BusinessDayCalendar) current.clone();
            }
        }
        throw new EntityNotFoundException("getBusinessDayCalendar(String) : No matching calendar");
    }

    /**
   * Returns the (latest) details of the calendar with the specified entity id, from (the default)
   * state zero.  Throws a NoMatchingEntityException if there is no calendar with the specified
   * id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to retrieve from the model;
   * @return The calendar with the specified id;
   */
    public BusinessDayCalendar getBusinessDayCalendar(long entityId) throws ModelException {
        return this.getBusinessDayCalendar(entityId, 0, null);
    }

    /**
   * Returns the (latest) details of the calendar with the specified entity id, from the specified
   * state.  Throws a NoMatchingEntityException if there is no calendar with the specified id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to retrieve from the model;
   * @param state The state from which the calendar should be retrieved;
   * @return The calendar with the specified id, from the requested state;
   */
    public BusinessDayCalendar getBusinessDayCalendar(long entityId, int state) throws ModelException {
        return this.getBusinessDayCalendar(entityId, state, null);
    }

    /**
   * Returns the details of the calendar with the specified entity id, as at the specified instant,
   * from (the default) state zero.  Throws a NoMatchingEntityException if there is no calendar
   * with the specified id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to retrieve from the model;
   * @param instant The instant in time for the calendar 'snapshot';
   * @return The calendar with the specified id, from state zero, as-at the specified instant;
   */
    public BusinessDayCalendar getBusinessDayCalendar(long entityId, Date instant) throws ModelException {
        return this.getBusinessDayCalendar(entityId, 0, instant);
    }

    /**
   * Returns the details of the calendar with the specified entity id, as at the specified instant,
   * from the specified state.  Throws a NoMatchingEntityException if there is no calendar with the
   * specified id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to retrieve from the model;
   * @param state The state from which to retrieve the calendar details;
   * @param instant The instant in time for the calendar 'snapshot';
   */
    public BusinessDayCalendar getBusinessDayCalendar(long entityId, int state, Date instant) throws ModelException {
        Iterator iterator = calendars.iterator();
        while (iterator.hasNext()) {
            BusinessDayCalendar item = (BusinessDayCalendar) iterator.next();
            if (item.getEntityId() == entityId) {
                return (BusinessDayCalendar) item.clone();
            }
        }
        throw new EntityNotFoundException("getBusinessDayCalendar(...) : No matching calendar");
    }

    /**
   * Returns the (complete and latest) details of all the business day calendars in (the default)
   * state zero.  This may require a lot of memory.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @return All the business day calendars in the model;
   */
    public Collection getBusinessDayCalendars() {
        return this.getBusinessDayCalendars(BusinessDayCalendar.COMPLETE, 0, null);
    }

    /**
   * Returns the (latest) details of all the business day calendars in (the default) state zero
   *  - the caller can request COMPLETE details, or REFERENCE details (requires less memory, but
   * includes only the name and entity id for each calendar).
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param scope ;
   */
    public Collection getBusinessDayCalendars(int scope) {
        return this.getBusinessDayCalendars(scope, 0, null);
    }

    /**
   * Returns the (latest) details of all the business day calendars in the specified state
   *  - the caller can request COMPLETE details, or REFERENCE details (requires less memory, but
   * includes only the name and entity id for each calendar).
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param scope ;
   * @param state The model state from which the calendars should be retrieved;
   */
    public Collection getBusinessDayCalendars(int scope, int state) {
        return this.getBusinessDayCalendars(scope, state, null);
    }

    /**
   * Returns the details of all the business day calendars in (the default) state zero - the caller
   * can request COMPLETE details, or REFERENCE details (requires less memory, but includes only
   * the name and entity id for each calendar).
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param scope ;
   * @param instant The instant in time for the calendars 'snapshot';
   */
    public Collection getBusinessDayCalendars(int scope, Date instant) {
        return this.getBusinessDayCalendars(scope, 0, instant);
    }

    /**
   * Returns the details of all the business day calendars in the specified state - the caller
   * can request COMPLETE details, or REFERENCE details (requires less memory, but includes only
   * the name and entity id for each calendar).
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param scope ;
   * @param state The model state from which the calendars should be retrieved;
   * @param instant The instant in time for the calendars 'snapshot';
   */
    public Collection getBusinessDayCalendars(int scope, int state, Date instant) {
        if (scope == BusinessDayCalendar.COMPLETE) {
            ArrayList cloneList = new ArrayList();
            Iterator iterator = calendars.iterator();
            while (iterator.hasNext()) {
                BusinessDayCalendar calendar = (BusinessDayCalendar) iterator.next();
                BusinessDayCalendar duplicate = (BusinessDayCalendar) calendar.clone();
                cloneList.add(duplicate);
            }
            return cloneList;
        } else if (scope == BusinessDayCalendar.REFERENCE) {
            ArrayList referenceList = new ArrayList();
            Iterator iterator = calendars.iterator();
            while (iterator.hasNext()) {
                BusinessDayCalendar calendar = (BusinessDayCalendar) iterator.next();
                BusinessDayCalendarReference reference = new BusinessDayCalendarReference(calendar.getName(), calendar.getEntityId());
                referenceList.add(reference);
            }
            return referenceList;
        } else throw new IllegalArgumentException("getBusinessDayCalendars(...) - Unknown scope");
    }

    /**
   * Adds the details of the specified calendar to (the default) state zero of the model, 'as at'
   * the latest instant in time - returns the entity id for the calendar in the model.
   * <P>
   * You can assign your own entity id if you want to.  If you leave it as zero, the model will
   * assign a unique id automatically.
   * <P>
   * An exception will be thrown if the name or entity id for the specified calendar are not
   * unique in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The calendar to be added to the model;
   * @return long The entity id for the calendar in the model;
   */
    public long addBusinessDayCalendar(BusinessDayCalendar calendar) throws ModelException {
        return this.addBusinessDayCalendar(calendar, 0);
    }

    /**
   * Adds the details of the specified calendar to the specified state of the model, 'as at' the
   * latest instant in time - returns the entity id for the calendar in the model.
   * <P>
   * You can assign your own entity id if you want to.  If you leave it as zero, the model will
   * assign a unique id automatically.
   * <P>
   * An exception will be thrown if the name or entity id for the specified calendar are not
   * unique in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The calendar to be added to the model;
   * @param state The model state into which the calendar should be added;
   * @return The id of the calendar within the model;
   */
    public long addBusinessDayCalendar(BusinessDayCalendar calendar, int state) throws ModelException {
        return this.addBusinessDayCalendar(calendar, state, null);
    }

    /**
   * Adds the details of the specified calendar to (the default) state of the model, 'as at' the
   * specified instant in time - returns the entity id for the calendar in the model.
   * <P>
   * You can assign your own entity id if you want to.  If you leave it as zero, the model will
   * assign a unique id automatically.
   * <P>
   * An exception will be thrown if the name or entity id for the specified calendar are not
   * unique in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The calendar to be added to the model;
   * @param instant The instant in time at which the calendar should be recorded as added to the
   *                model;
   * @return The id used by the model for this calendar;
   */
    public long addBusinessDayCalendar(BusinessDayCalendar calendar, Date instant) throws ModelException {
        return this.addBusinessDayCalendar(calendar, 0, instant);
    }

    /**
   * Adds the details of the specified calendar to the specified state of the model, 'as at' the
   * specified instant in time - returns the entity id for the calendar in the model.
   * <P>
   * You can assign your own entity id if you want to.  If you leave it as zero, the model will
   * assign a unique id automatically.
   * <P>
   * An exception will be thrown if the name or entity id for the specified calendar are not
   * unique in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The calendar to be added to the model;
   * @param state The state into which the calendar should be added;
   * @param instant The instant in time at which the calendar should be recorded as added to the
   *                model;
   * @return The id used by the model for this calendar;
   */
    public long addBusinessDayCalendar(BusinessDayCalendar calendar, int state, Date instant) throws ModelException {
        String name = calendar.getName();
        if (name == null) {
            throw new InvalidNameException("addBusinessDayCalendar(...) : Name is null");
        } else if (name.length() == 0) {
            throw new InvalidNameException("addBusinessDayCalendar(...) : Name is empty");
        } else if (!isUniqueName(name)) {
            throw new DuplicateNameException("addBusinessDayCalendar(...) : Duplicate name");
        }
        long id = calendar.getEntityId();
        if (id != 0) {
            if (this.getBusinessDayCalendar(id) != null) {
                throw new DuplicateEntityIdException("Calendar already exists");
            }
        } else {
            id = getUnusedId();
        }
        BusinessDayCalendar duplicate = (BusinessDayCalendar) calendar.clone();
        duplicate.setEntityId(id);
        calendars.add(duplicate);
        return id;
    }

    /**
   * Modifies the details of the specified calendar (identified by the entity id) in (the default)
   * state zero of the model, 'as at' the latest instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id, or if the
   * name of the calendar is empty or duplicated elsewhere in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The details of the calendar to be updated in the model;
   */
    public void updateBusinessDayCalendar(BusinessDayCalendar calendar) throws ModelException {
        this.updateBusinessDayCalendar(calendar, 0, null);
    }

    /**
   * Modifies the details of the specified calendar (identified by the entity id) in the specified
   * state of the model, 'as at' the latest instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id, or if the
   * name of the calendar is empty or duplicated elsewhere in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The details of the calendar to be updated in the model;
   * @param state The state in which the calendar should be updated;
   */
    public void updateBusinessDayCalendar(BusinessDayCalendar calendar, int state) throws ModelException {
        this.updateBusinessDayCalendar(calendar, state, null);
    }

    /**
   * Modifies the details of the specified calendar (identified by the entity id) in (the default)
   * state zero of the model, 'as at' the specified instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id, or if the
   * name of the calendar is empty or duplicated elsewhere in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The details of the calendar to be updated in the model;
   * @param instant The date and time at which the update is effective;
   */
    public void updateBusinessDayCalendar(BusinessDayCalendar calendar, Date instant) throws ModelException {
        this.updateBusinessDayCalendar(calendar, 0, instant);
    }

    /**
   * Modifies the details of the specified calendar (identified by the entity id) in the specified
   * state of the model, 'as at' the specified instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id, or if the
   * name of the calendar is empty or duplicated elsewhere in the model.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param calendar The details of the calendar to be updated in the model;
   * @param state The state in which the calendar is to be updated;
   * @param instant The date and time at which the update is effective;
   */
    public void updateBusinessDayCalendar(BusinessDayCalendar calendar, int state, Date instant) throws ModelException {
        boolean unique = this.isUniqueNameExceptFor(calendar.getName(), calendar.getEntityId());
        if (unique) {
            Iterator iterator = this.calendars.iterator();
            while (iterator.hasNext()) {
                BusinessDayCalendar item = (BusinessDayCalendar) iterator.next();
                if (item.getEntityId() == calendar.getEntityId()) {
                    BusinessDayCalendar duplicate = (BusinessDayCalendar) calendar.clone();
                    calendars.set(calendars.indexOf(item), duplicate);
                }
            }
        } else throw new DuplicateNameException("updateBusinessDayCalendar(...) : Duplicate name");
    }

    /**
   * Deletes the calendar with the specified entity id in (the default) state zero of the model,
   * 'as at' the latest instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to delete from the model;
   */
    public void deleteBusinessDayCalendar(long entityId) throws ModelException {
        this.deleteBusinessDayCalendar(entityId, 0, null);
    }

    /**
   * Deletes the calendar with the specified entity id in the specified state of the model, 'as at'
   * the latest instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to delete from the model;
   * @param state The state from which the calendar is to be deleted;
   */
    public void deleteBusinessDayCalendar(long entityId, int state) throws ModelException {
        this.deleteBusinessDayCalendar(entityId, state, null);
    }

    /**
   * Deletes the calendar with the specified entity id in (the default) state zero of the model,
   * 'as at' the specified instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to delete from the model;
   * @param instant The date and time from which the deletion is effective;
   */
    public void deleteBusinessDayCalendar(long entityId, Date instant) throws ModelException {
        this.deleteBusinessDayCalendar(entityId, 0, instant);
    }

    /**
   * Deletes the calendar with the specified entity id in the specified state of the model, 'as at'
   * the specified instant in time.
   * <P>
   * An exception will be thrown if there is no calendar with the specified entity id.
   * <P>
   * Keep in mind that this class implements a simple version of the model that has only one state
   * and ignores the time dimension.
   * @param entityId The id of the calendar to delete from the model;
   * @param state The state from which the calendar is to be deleted;
   * @param instant The date and time from which the deletion is effective;
   */
    public void deleteBusinessDayCalendar(long entityId, int state, Date instant) throws ModelException {
        Iterator iterator = calendars.iterator();
        while (iterator.hasNext()) {
            BusinessDayCalendar item = (BusinessDayCalendar) iterator.next();
            if (item.getEntityId() == entityId) {
                calendars.remove(item);
                this.dirty = true;
                return;
            }
        }
        throw new ModelException("No calendar with that id to delete");
    }

    /**
   * Returns true if the specified name is unique in the model, and false otherwise.
   * @param name The name to check for uniqueness;
   * @return A boolean that indicates whether or not the specified name is unique in the model;
   */
    private boolean isUniqueName(String name) {
        boolean unique = true;
        Iterator iterator = calendars.iterator();
        while (iterator.hasNext()) {
            BusinessDayCalendar calendar = (BusinessDayCalendar) iterator.next();
            if (calendar.getName().equals(name)) unique = false;
        }
        return unique;
    }

    /**
   * Returns true if the specified name is unique in the model, and false otherwise.  One calendar
   * (with the specified entity id) is ignored for this test (useful when updating a calendar).
   * @param name The name to check for uniqueness;
   * @param ignoreEntityId The id of the calendar in the model that should be ignored for this test;
   * @param A boolean that indicates whether or not the specified name is unique in the model;
   */
    private boolean isUniqueNameExceptFor(String name, long ignoreEntityId) {
        boolean unique = true;
        Iterator iterator = calendars.iterator();
        while (iterator.hasNext()) {
            BusinessDayCalendar calendar = (BusinessDayCalendar) iterator.next();
            if (!(ignoreEntityId == calendar.getEntityId())) {
                if (calendar.getName().equals(name)) unique = false;
            }
        }
        return unique;
    }
}
