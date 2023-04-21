package org.marcont.services.definitions.timeEntry;

/**
 * Implementations of this listener may be registered with instances of org.marcont.services.definitions.timeEntry.InstantEvent to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface InstantEventListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when a value of intFinishedBy has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void intFinishedByAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing newValue);

    /**
	 * Called when a value of intFinishedBy has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void intFinishedByRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing oldValue);

    /**
	 * Called when a value of intOverlappedBy has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void intOverlappedByAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing newValue);

    /**
	 * Called when a value of intOverlappedBy has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void intOverlappedByRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing oldValue);

    /**
	 * Called when a value of before has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void beforeAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, org.marcont.services.definitions.timeEntry.TemporalThing newValue);

    /**
	 * Called when a value of before has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void beforeRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, org.marcont.services.definitions.timeEntry.TemporalThing oldValue);

    /**
	 * Called when a value of intStartedBy has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void intStartedByAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing newValue);

    /**
	 * Called when a value of intStartedBy has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void intStartedByRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing oldValue);

    /**
	 * Called when begins has changed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 */
    public void beginsChanged(org.marcont.services.definitions.timeEntry.InstantEvent source);

    /**
	 * Called when a value of after has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void afterAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing newValue);

    /**
	 * Called when a value of after has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void afterRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing oldValue);

    /**
	 * Called when a value of intContains has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void intContainsAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing newValue);

    /**
	 * Called when a value of intContains has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void intContainsRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.ibm.adtech.jastor.Thing oldValue);

    /**
	 * Called when a value of durationDescriptionOf has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void durationDescriptionOfAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, org.marcont.services.definitions.timeEntry.DurationDescription newValue);

    /**
	 * Called when a value of durationDescriptionOf has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void durationDescriptionOfRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, org.marcont.services.definitions.timeEntry.DurationDescription oldValue);

    /**
	 * Called when a value of durationDescriptionDataType has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void durationDescriptionDataTypeAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.hp.hpl.jena.datatypes.xsd.XSDDuration newValue);

    /**
	 * Called when a value of durationDescriptionDataType has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void durationDescriptionDataTypeRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.hp.hpl.jena.datatypes.xsd.XSDDuration oldValue);

    /**
	 * Called when ends has changed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 */
    public void endsChanged(org.marcont.services.definitions.timeEntry.InstantEvent source);

    /**
	 * Called when a value of inCalendarClockDataType has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void inCalendarClockDataTypeAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, com.hp.hpl.jena.datatypes.xsd.XSDDateTime newValue);

    /**
	 * Called when a value of inCalendarClockDataType has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void inCalendarClockDataTypeRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, com.hp.hpl.jena.datatypes.xsd.XSDDateTime oldValue);

    /**
	 * Called when a value of inCalendarClock has been added
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param newValue the object representing the new value
	 */
    public void inCalendarClockAdded(org.marcont.services.definitions.timeEntry.InstantEvent source, org.marcont.services.definitions.timeEntry.CalendarClockDescription newValue);

    /**
	 * Called when a value of inCalendarClock has been removed
	 * @param source the affected instance of org.marcont.services.definitions.timeEntry.InstantEvent
	 * @param oldValue the object representing the removed value
	 */
    public void inCalendarClockRemoved(org.marcont.services.definitions.timeEntry.InstantEvent source, org.marcont.services.definitions.timeEntry.CalendarClockDescription oldValue);
}
