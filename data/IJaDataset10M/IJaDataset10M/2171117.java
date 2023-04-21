package iwork.eheap;

import java.util.Vector;
import java.util.Enumeration;
import com.ibm.tspaces.Tuple;
import java.util.*;
import java.io.*;

/**
 * NOTE: This is a compatibility mode version of this class.  It
 * allows v1 clients to participate in the v2 Event Heap.  No new
 * applications should be programmed to use this class, but should use
 * iwork.eheap2.EventHeap instead. <p>
 *
 * This class provides an interface to an event heap in which the application
 * is participating.  An event heap is characterized by the server name for
 * the machine on which it is running and the port being used on that machine.
 */
public class EventHeap {

    /** The version of the Event Heap.  Note, that this changes only when
   * incompatibilities between versions are introduced. */
    private Integer EVENT_HEAP_VERSION = new Integer(1);

    /** A handle to the underlying Event Heap v2 object. 
   */
    iwork.eheap2.EventHeap eh2;

    /** See the description for {@link #EventHeap(int, String, String,
   * int, EventHeap)}. This constructor calls that one with
   * sourceID=0, heapName=null, machine=null, and port=-1.  oldHeap
   * must be non-null.  Use this method in a new thread to construct a
   * clone of some other Event Heap which was created in another
   * thread.  Sequencing will be the same for both the original and
   * new EventHeap objects (that is to say at most one of them will
   * receive an event from any given source, and events they generate
   * will be part of the same sequence).
   */
    public EventHeap(EventHeap oldHeap) throws EventHeapException {
        if (oldHeap == null) throw new EventHeapException("oldHeap must be non-null");
        eh2 = oldHeap.eh2;
    }

    /** See the description for {@link #EventHeap(int, String, String,
   * int, EventHeap)}. This constructor creates a new Event Heap with
   * heapName, machine and port the same as the passed in Event Heap,
   * oldHeap.  'sourceID' must be different than in 'oldHeap', and
   * sequencing of events from this Event Heap object will be
   * independent of the original.  It should be used to create a new
   * EventHeap object that is connected to the same Event Heap for use
   * in a different thread.  As with {@link #EventHeap(int, String, String,
   * int, EventHeap)}, setting sourceID to 0 will cause a random
   * value for sourceID to be chosen.
   */
    public EventHeap(EventHeap oldHeap, int sourceID) throws EventHeapException {
        this(sourceID, null, oldHeap.eh2.getMachine(), oldHeap.eh2.getPort(), null);
        if (oldHeap == null) throw new EventHeapException("oldHeap must be non-null");
        if (oldHeap.getSourceID() == new Integer(sourceID)) throw new EventHeapException("oldHeap.sourceID and sourceID must " + "be different.");
    }

    /** See the description for {@link #EventHeap(int, String, String, int, 
   * EventHeap)}. This constructor calls that one with port=-1 and 
   * oldHeap=null.
   */
    public EventHeap(int sourceID, String heapName, String machine) throws EventHeapException {
        this(sourceID, heapName, machine, -1, null);
    }

    /** See the description for {@link #EventHeap(int, String, String, int, 
   * EventHeap)}. This constructor calls that one with oldHeap=null.
   */
    public EventHeap(int sourceID, String heapName, String machine, int port) throws EventHeapException {
        this(sourceID, heapName, machine, port, null);
    }

    /** See the description for {@link #EventHeap(int, String, String, int, 
   * EventHeap)}. This constructor calls that one with sourceID=0, port=-1 and 
   * oldHeap=null.
   */
    public EventHeap(String heapName, String machine) throws EventHeapException {
        this(0, heapName, machine, -1, null);
    }

    /** See the description for {@link #EventHeap(int, String, String, int, 
   * EventHeap)}. This constructor calls that one with sourceID=0, and 
   * oldHeap=null.
   */
    public EventHeap(String heapName, String machine, int port) throws EventHeapException {
        this(0, heapName, machine, port, null);
    }

    /** See the description for {@link #EventHeap(int, String, String, int, 
   * EventHeap)}. This constructor calls that one with sourceID=0.
   */
    public EventHeap(String heapName, String machine, int port, EventHeap oldHeap) throws EventHeapException {
        this(0, heapName, machine, port, oldHeap);
    }

    /** Creates a new Event Heap interface to the specified event heap.
   * The Event Heap which is connected is determinde by the given
   * machine and port.  The SourceID specifies the source ID that will
   * be used in all events sent from this EventHeap object.  If
   * another Event Heap is passed in, the heapName, machine and port
   * parameters will be set to the values from it if they were passed
   * in as 'null' or '-1' (for port).  To use the Event Heap from
   * different threads you should create a new instance of the Event
   * Heap by passing in an old EventHeap object otherwise behavior
   * will be unpredictable.<p>
   *
   * <b>C++ API Note:</b> The C++ API includes one more parameter, 
   *       clientName. This parameter is ignored in the current
   *       implementation and is left in for legacy support. 
   *
   * @param sourceID The SourceID for all events generated by this
   * object.  it should be unique among all participants in the Event
   * Heap.  If it is left as '0' a random value will be chosen between
   * 0 and MAX_INT.  If oldHeap is non-null and sourceID=-1 the value
   * will be taken from oldHeap.
   *
   * @param heapName DEPRECATED.  The Event Heap v2 doesn't allow more
   * than one Event Heap per server, so this parameter is actually ignored.
   * It is maintained for compatibility reasons only.<p>
   *
   * @param machine The DNS name of the machine where the Event Heap
   * server is running.  For now this is the machine the TSpace server
   * is running on.  Leave as 'null' to use the value in oldHeap.<p>
   *
   * @param port The port on the machine being used by the Event Heap
   * server (TSpace server).  Leave as -1 for default port.<p>
   *
   * @param oldHeap Another Event Heap instance.  Values for the other
   * parameters are taken from here if this is non-null and they are
   * set to 'null' or -1.
   *
   * @throws EventHeapException An exception is thrown if a connection
   * cannot be made to the remote Event Heap.  
   */
    public EventHeap(int sourceID, String heapName, String machine, int port, EventHeap oldHeap) throws EventHeapException {
        if (sourceID == -1 && oldHeap == null) sourceID = 0;
        if (sourceID == -1) {
            eh2 = oldHeap.eh2;
            return;
        } else if (sourceID == 0) sourceID = (int) (Math.random() * (double) Integer.MAX_VALUE);
        String source = (new Integer(sourceID)).toString();
        eh2 = new iwork.eheap2.EventHeap(source, machine, port, (oldHeap == null ? null : oldHeap.eh2), null, null);
    }

    /** simple function to create an array of events from a single event
   * to allow the single template event retrieval versions to just
   * call into the multi-template event versions.
   */
    private Event[] createEventArray(Event event) {
        Event[] events = new Event[1];
        events[0] = event;
        return events;
    }

    /** Prints out a debug statement to the debug output strieam if the
   * current debugLevel is higher than the level of the statement.
   * See also {@link #setDebug(int)} and {@link
   * #setDebugStream(PrintStream)}.  
   */
    public static void debugPrintln(int level, String statement) {
        iwork.eheap2.EventHeap.debugPrintln(level, statement);
    }

    /** Permanently removes an event from the Event Heap.  The event passed
   * in should be one previously retrieved using one of the retrieval 
   * methods.
   *
   * @param event An event previously retrieved from the Event Heap.
   */
    public void deleteEvent(Event event) {
        try {
            eh2.deleteEvent(event.getCurrentTuple().wrappedEvent);
        } catch (iwork.eheap2.EventHeapException e) {
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event get(Event templateEvent) throws EventHeapException {
        Event[] retArray = getMany(createEventArray(templateEvent));
        if (retArray != null) return retArray[0]; else return null;
    }

    /** Gets an event matching one of the given template events from the
   * Event Heap represented by this object.  If no matching event is
   * available it returns immediately with 'null'.  In addition to the
   * matching event it returns which template or templates matched.
   *
   * @param templateEvents The templates to be used in matching events
   * in the Event Heap passed in as elements of an array.  Only the
   * field type, name and value of non-formal fields are used in the
   * match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return An array of events.  The first element of the array is
   * the next most recent event in the Event Heap not yet retrieved by
   * this object which matches one or more of the given template
   * events.  Subsequent events in the array are the template events
   * used for the match which matched the returned event.  'null' is
   * returned if there are no matching events currently present.  
   */
    public Event[] getEvent(Event[] templateEvents) throws EventHeapException {
        try {
            int i;
            for (i = 0; i < templateEvents.length; i++) templateEvents[i].getTemplateTuple();
            return getMany(templateEvents);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** Gets an event matching the given template event from the Event
   * Heap represented by this object.  If no matching event is
   * available it returns immediately with 'null'.  
   * 
   * @param templateEvent The template to be used in matching events
   * in the Event Heap.  Only the field type, name and value of non-formal
   * fields are used in the match.  Field order is irrelevant.
   * 
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   * 
   * @return The next most recent event in the Event Heap not yet
   * retrieved by this object which matches the given template event.
   * 'null' is returned if there are no matching events currently
   * present.  */
    public Event getEvent(Event templateEvent) throws EventHeapException {
        try {
            templateEvent.getTemplateTuple();
            return get(templateEvent);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event[] getMany(Event[] templateEvents) throws EventHeapException {
        try {
            return Event.createV1Array(eh2.getEvent(Event.createV2Array(templateEvents)));
        } catch (iwork.eheap2.EventHeapException e) {
            throw new EventHeapException(e);
        }
    }

    /** Returns the source ID for this Event Heap object.  This can be
   * used to match the target ID field to get events targeted at this
   * Event Heap object.
   * 
   * @return The source ID for this Event Heap object 
   */
    public Integer getSourceID() {
        String stringVal = eh2.getSourceName();
        int finalUnderscore = stringVal.lastIndexOf('_');
        int idVal;
        try {
            idVal = Integer.parseInt(stringVal.substring(0, finalUnderscore));
        } catch (Exception e) {
            idVal = Integer.parseInt(stringVal.substring(finalUnderscore + 1));
        }
        return new Integer(idVal);
    }

    /** Returns the version of the Event Heap. 
   * 
   * @return The version of this Event Heap.
   */
    public int getVersion() {
        return EVENT_HEAP_VERSION.intValue();
    }

    /** Indicates whether or not the given templateEvent is a match for the 
   * returned event.  
   *
   * @param returnedEvent An event returned from the Event Heap by one of
   * the get/remove/wait event retrieval methods.
   *
   * @param templateEvent One of the template events that was used in the event
   * retrieval method call which returned returnedEvent.
   *
   * @return 'true' if the returnedEvent is a valid match for this
   * templateEvent. 
   */
    public boolean matches(Event returnedEvent, Event templateEvent) {
        Tuple templateTuple = templateEvent.getCurrentTuple();
        Tuple returnedTuple = returnedEvent.getCurrentTuple();
        return templateTuple.matches(returnedTuple);
    }

    /**
   * @deprecated use {@link #putEvent(Event)} instead
   */
    public void put(Event event) throws EventHeapException {
        putEvent(event);
    }

    /** Puts the given event into the Event Heap.  Only non-formal
   * fields are included in the copy stored on the Event Heap.
   * 
   * @param event The event to place in the Event Heap
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   */
    public void putEvent(Event event) throws EventHeapException {
        try {
            event.getActualTuple();
            write(event);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event remove(Event templateEvent) throws EventHeapException {
        Event[] retArray = removeMany(createEventArray(templateEvent));
        if (retArray != null) return retArray[0]; else return null;
    }

    /** Removes an event matching the given template event from the Event
   * Heap represented by this object.  If no matching event is
   * available it returns immediately with 'null'.  
   * 
   * @param templateEvent The template to be used in matching events
   * in the Event Heap.  Only the field type, name and value of non-formal
   * fields are used in the match.  Field order is irrelevant.
   * 
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   * 
   * @return The next most recent event in the Event Heap not yet
   * retrieved by this object which matches the given template event.
   * After retrieval the event will no longer be available in the Event
   * Heap for others to retrieve.  'null' is returned if there are no
   * matching events currently present.  
   */
    public Event removeEvent(Event templateEvent) throws EventHeapException {
        try {
            templateEvent.getTemplateTuple();
            return remove(templateEvent);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** Removes an event matching one of the given template events from the
   * Event Heap represented by this object.  If no matching event is
   * available it returns immediately with 'null'.  In addition to the
   * matching event it returns which template or templates matched.
   *
   * @param templateEvents The templates to be used in matching events
   * in the Event Heap passed in as elements of an array.  Only the
   * field type, name and value of non-formal fields are used in the
   * match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return An array of events.  The first element of the array is
   * the next most recent event in the Event Heap not yet retrieved by
   * this object which matches one or more of the given template
   * events.  Subsequent events in the array are the template events
   * used for the match which matched the returned event.  After
   * retrieval the events will no longer be available in the Event
   * Heap for other to retrieve.  'null' is returned if there are no
   * matching events currently present.  
   */
    public Event[] removeEvent(Event[] templateEvents) throws EventHeapException {
        try {
            int i;
            for (i = 0; i < templateEvents.length; i++) templateEvents[i].getTemplateTuple();
            return removeMany(templateEvents);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event[] removeMany(Event[] templateEvents) throws EventHeapException {
        try {
            return Event.createV1Array(eh2.removeEvent(Event.createV2Array(templateEvents)));
        } catch (iwork.eheap2.EventHeapException e) {
            throw new EventHeapException(e);
        }
    }

    /** Sets the debug output level for the EventHeap for this process.
   * All debug messages with level less than or equal to the current
   * 'level' will be printed.  A level of 1 or higher will cause a
   * dump of all TSpaces calls made by the Event Heap to be printed.
   * 
   * @param level the current debug level.  Calls to {@link
   * #debugPrintln(int, String)} with level set to less than the debug
   * level will be output.  
   */
    public static void setDebug(int level) {
        iwork.eheap2.EventHeap.setDebug(level);
    }

    /** Sets the print stream to which to output debug information */
    public static void setDebugStream(PrintStream debugOutput) {
        iwork.eheap2.EventHeap.setDebugStream(debugOutput);
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event[] snoopMany(Event[] templateEvents) throws EventHeapException {
        try {
            return Event.createV1Array(eh2.snoopEvents(Event.createV2Array(templateEvents)));
        } catch (iwork.eheap2.EventHeapException e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event[] snoop(Event templateEvent) throws EventHeapException {
        return snoopMany(createEventArray(templateEvent));
    }

    /** Returns all events matching the template event without modifying
   * the contents of the Event Heap.  This can be used to look at what
   * is currently in the Event Heap without actually interacting.  The
   * events are returned in an array.  
   *
   * @param templateEvent The template to be used in matching events
   * in the Event Heap.  Only the field type, name and value of non-formal
   * fields are used in the match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return An array of all events currently in the Event Heap that
   * match the template event.  The array is a set- there are no
   * duplicate events- but the array is not guaranteed to be ordered.
   */
    public Event[] snoopEvents(Event templateEvent) throws EventHeapException {
        try {
            Tuple templatetuple = templateEvent.getTemplateTuple();
            return snoop(templateEvent);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** Returns all events matching one of the given template events
   * without modifying the contents of the Event Heap.  This can be
   * used to look at what is currently in the Event Heap without
   * actually interacting.  If no matching events are available it
   * returns immediately with 'null'.  The matching events are
   * returned in an array, and the {@link #matches(Event, Event)}
   * method can be used to determine which template events actually
   * match any given returned event.
   *
   * @param templateEvents The templates to be used in matching events
   * in the Event Heap passed in as elements of an array.  Only the
   * field type, name and value of non-formal fields are used in the
   * match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return An array of all events currently in the Event Heap that
   * match one of the template events.  The array is a set- there are no
   * duplicate events- but the array is not guaranteed to be ordered.  */
    public Event[] snoopEvents(Event[] templateEvents) throws EventHeapException {
        try {
            int i;
            for (i = 0; i < templateEvents.length; i++) templateEvents[i].getTemplateTuple();
            return snoopMany(templateEvents);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event waitToRemove(Event templateEvent) throws EventHeapException {
        Event[] retArray = waitToRemoveMany(createEventArray(templateEvent));
        if (retArray != null) return retArray[0]; else return null;
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event[] waitToRemoveMany(Event[] templateEvents) throws EventHeapException {
        try {
            return Event.createV1Array(eh2.waitToRemoveEvent(Event.createV2Array(templateEvents)));
        } catch (iwork.eheap2.EventHeapException e) {
            throw new EventHeapException(e);
        }
    }

    /** Removes an event matching the given template event from the Event
   * Heap represented by this object.  The call will block until a matching
   * event is found. 
   *
   * @param templateEvent The template to be used in matching events
   * in the Event Heap.  Only the field type, name and value of non-formal
   * fields are used in the match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return The next most recent event in the Event Heap not yet
   * retrieved by this object which matches the given template event.
   * The event will no longer be available in the Event Heap for others
   * to use.
   */
    public Event waitToRemoveEvent(Event templateEvent) throws EventHeapException {
        try {
            templateEvent.getTemplateTuple();
            return waitToRemove(templateEvent);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** Removes an event matching one of the given template events from
   * the Event Heap represented by this object.  The call will block
   * until a matching event is found.  In addition to the matching event
   * it returns which template or templates matched.<p>
   *
   * <b>C++ API Note:</b> The C++ API includes one more parameter, 
   *       int *size, which is a pointer to the size of the templateEvents
   *       array being passed in.  When the call returns the size parameter
   *       is changed to the size of the returned event array. 
   *
   * @param templateEvents The templates to be used in matching events
   * in the Event Heap passed in as elements of an array.  Only the
   * field type, name and value of non-formal fields are used in the
   * match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return An array of events.  The first element of the array is
   * the next most recent event in the Event Heap not yet retrieved by
   * this object which matches one or more of the given template
   * events.  Subsequent events in the array are the template events
   * used for the match which matched the returned event.  The event
   * retrieved will no longer be available in the Event Heap for
   * others to use. 
   */
    public Event[] waitToRemoveEvent(Event[] templateEvents) throws EventHeapException {
        try {
            int i;
            for (i = 0; i < templateEvents.length; i++) templateEvents[i].getTemplateTuple();
            return waitToRemoveMany(templateEvents);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event wait(Event templateEvent) throws EventHeapException {
        Event[] retArray = waitMany(createEventArray(templateEvent));
        if (retArray != null) return retArray[0]; else return null;
    }

    /** Retrieves an event matching the given template event from the Event
   * Heap represented by this object.  The call will block until a matching
   * event is found.
   *
   * @param templateEvent The template to be used in matching events
   * in the Event Heap.  Only the field type, name and value of non-formal
   * fields are used in the match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return The next most recent event in the Event Heap not yet
   * retrieved by this object which matches the given template event.
   */
    public Event waitForEvent(Event templateEvent) throws EventHeapException {
        try {
            templateEvent.getTemplateTuple();
            return wait(templateEvent);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** Retrieves an event matching one of the given template events from
   * the Event Heap represented by this object.  The call will block
   * until a matching event is found.  In addition to the matching event
   * it returns which template or templates matched.
   *
   * @param templateEvents The templates to be used in matching events
   * in the Event Heap passed in as elements of an array.  Only the
   * field type, name and value of non-formal fields are used in the
   * match.  Field order is irrelevant.
   *
   * @throws EventHeapException An exception is thrown if there was an
   * error communicating with the Event Heap.
   *
   * @return An array of events.  The first element of the array is
   * the next most recent event in the Event Heap not yet retrieved by
   * this object which matches one or more of the given template
   * events.  Subsequent events in the array are the template events
   * used for the match which matched the returned event.  
   */
    public Event[] waitForEvent(Event[] templateEvents) throws EventHeapException {
        try {
            int i;
            for (i = 0; i < templateEvents.length; i++) templateEvents[i].getTemplateTuple();
            return waitMany(templateEvents);
        } catch (Exception e) {
            throw new EventHeapException(e);
        }
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public Event[] waitMany(Event[] templateEvents) throws EventHeapException {
        try {
            return Event.createV1Array(eh2.waitForEvent(Event.createV2Array(templateEvents)));
        } catch (iwork.eheap2.EventHeapException e) {
            throw new EventHeapException(e);
        }
    }

    /**
   * @deprecated use {@link #waitForEvent(Event templateEvent)} instead 
   */
    public Event waitToTake(Event templateEvent) throws EventHeapException {
        return waitForEvent(templateEvent);
    }

    /** <b><i>Private Method</i></b>: This should be treated as a private
   *  method, and is only public to allow the C++ wrapper class to call
   *  the method through JNI.
   */
    public void write(Event event) throws EventHeapException {
        try {
            eh2.putEvent(event.getCurrentTuple().wrappedEvent);
        } catch (iwork.eheap2.EventHeapException e) {
        }
    }
}
