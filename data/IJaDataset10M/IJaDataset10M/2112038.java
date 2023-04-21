package net.sf.dropboxmq.workflow.persistence.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.dropboxmq.workflow.data.EventType;
import net.sf.dropboxmq.workflow.data.ProcessType;
import net.sf.dropboxmq.workflow.data.Processor;
import net.sf.dropboxmq.workflow.persistence.EventTypePersistence;

/**
 * Created: 22 Aug 2010
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision$, $Date$
 */
public class EventTypePersistenceImpl implements EventTypePersistence {

    private final ProcessorPersistenceImpl processorPersistence;

    private final ProcessTypePersistenceImpl processTypePersistence;

    private final Map<Integer, EventType> eventTypesById = new HashMap<Integer, EventType>();

    private final Map<Integer, Map<String, EventType>> eventTypesByNameByProcessTypeId = new HashMap<Integer, Map<String, EventType>>();

    private int nextEventTypeId = 1000;

    public EventTypePersistenceImpl(final ProcessorPersistenceImpl processorPersistence, final ProcessTypePersistenceImpl processTypePersistence) {
        this.processorPersistence = processorPersistence;
        this.processTypePersistence = processTypePersistence;
    }

    @Override
    public void storeEventType(final EventType eventType) {
        final EventType newEventType = eventType.clone();
        if (eventTypesById.containsKey(nextEventTypeId)) {
            throw new RuntimeException("Event type id already exists, id = " + nextEventTypeId);
        }
        newEventType.setId(nextEventTypeId);
        nextEventTypeId++;
        eventTypesById.put(newEventType.getId(), newEventType);
        checkProcessTypes(newEventType);
        processTypePersistence.getExistingProcessTypeById(newEventType.getProcessTypeId());
        final Map<String, EventType> eventTypesByName = getEventTypesByName(newEventType.getProcessTypeId());
        if (eventTypesByName.containsKey(newEventType.getName())) {
            throw new RuntimeException("Process type already contains event type of given name, use update," + " event type = " + newEventType.getName());
        }
        eventTypesByName.put(newEventType.getName(), newEventType);
        eventType.setId(newEventType.getId());
    }

    private void checkProcessTypes(final EventType eventType) {
        final ProcessType processType = processTypePersistence.getExistingProcessTypeById(eventType.getProcessTypeId());
        eventType.setProcessTypeName(processType.getName());
        if (eventType.getParentProcessTypeId() != null) {
            final ProcessType parentProcessType = processTypePersistence.getExistingProcessTypeById(eventType.getParentProcessTypeId());
            eventType.setParentProcessTypeName(parentProcessType.getName());
        }
    }

    private Map<String, EventType> getEventTypesByName(final Integer processTypeId) {
        Map<String, EventType> eventTypesByName = eventTypesByNameByProcessTypeId.get(processTypeId);
        if (eventTypesByName == null) {
            eventTypesByName = new HashMap<String, EventType>();
            eventTypesByNameByProcessTypeId.put(processTypeId, eventTypesByName);
        }
        return eventTypesByName;
    }

    @Override
    public void updateEventType(final int id, final EventType eventType) {
        final EventType newEventType = eventType.clone();
        newEventType.setId(id);
        getExistingEventTypeById(id);
        checkProcessTypes(newEventType);
        eventTypesById.put(id, newEventType);
        final Map<String, EventType> eventTypesByName = getEventTypesByName(newEventType.getProcessTypeId());
        eventTypesByName.put(newEventType.getName(), newEventType);
        eventType.setId(id);
    }

    @Override
    public void disableEventType(final int id) {
        getExistingEventTypeById(id).setEnabled(true);
    }

    @Override
    public Collection<EventType> getEventTypesByProcessTypeId(final int processTypeId) {
        final Map<String, EventType> eventTypes = getEventTypesByName(processTypeId);
        final List<EventType> eventTypeList = new ArrayList<EventType>();
        for (final EventType eventType : eventTypes.values()) {
            eventTypeList.add(eventType.clone());
        }
        return eventTypeList;
    }

    @Override
    public Collection<EventType> getEnabledEventTypesByProcessor(final String processorName) {
        final List<EventType> enabledEventTypes = new ArrayList<EventType>();
        final Processor processor = processorPersistence.getProcessorByName(processorName);
        if (processor == null) {
            throw new RuntimeException("Could not find processor by name " + processorName);
        }
        for (final ProcessType processType : processTypePersistence.getExistingProcessTypesByProcessor(processor.getName())) {
            final Collection<EventType> existingEventTypes = getEventTypesByProcessTypeId(processType.getId());
            for (final EventType existingEventType : existingEventTypes) {
                if (existingEventType.isEnabled()) {
                    enabledEventTypes.add(existingEventType.clone());
                }
            }
        }
        return enabledEventTypes;
    }

    EventType getExistingEventTypeById(final int id) {
        final EventType existingEventType = eventTypesById.get(id);
        if (existingEventType == null) {
            throw new RuntimeException("Could not find event type, id = " + id);
        }
        return existingEventType;
    }
}
