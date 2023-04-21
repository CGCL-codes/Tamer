package mipt.data.store.event.sync;

import mipt.io.sync.ObjectEvent;
import mipt.io.sync.ObjectEventConverter;
import mipt.data.Data;
import mipt.data.event.DataModelEvent;
import mipt.data.store.event.DataStorageEvent;
import mipt.data.store.PersistentData;

/**
 * For synchronization between DataStorages and UI components.
 * Can be installed to DataModelEventTranslator or DataModelObjectListener that receives
 *   events from DataStorages, and not backward.
 * Is almost abstract because we don't know where to insert data in trees
 *   even if we assume that all data choice consist in data of incoming DataStorageEvent's source.
 * @author Evdokimov
 */
public class DataStorageEventConverter implements ObjectEventConverter {

    /**
 * DataModels can't fire events for DataStorages, only vice versae
 *  so DataModelEventTranslator become work in one direction with us
 * @see mipt.sync.ObjectEventConverter
 */
    public ObjectEvent convertToCommonForm(ObjectEvent event) {
        return null;
    }

    /**
 * @see mipt.sync.ObjectEventConverter
 */
    public java.util.EventObject convertToSpecificForm(Object someChoice, ObjectEvent event) {
        DataStorageEvent e = (DataStorageEvent) event;
        int type = storage2modelEventType(e.getEventType());
        if (type < 0) return null;
        return createModelEvent(someChoice, type, e.getData());
    }

    protected DataModelEvent createModelEvent(Object source, int id, PersistentData data) {
        return new DataModelEvent(source, id, data, getParent(data));
    }

    /**
 * Must be overriden for tree choices
 */
    protected Data getParent(PersistentData data) {
        return null;
    }

    /**
 * 
 * @return int
 * @param storageType int
 */
    protected int storage2modelEventType(int storageType) {
        int type = -1;
        switch(storageType) {
            case DataStorageEvent.NEW_DATA:
                type = DataModelEvent.ADD;
                break;
            case DataStorageEvent.CHANGE_DATA:
            case DataStorageEvent.CHANGE_FIELD:
                type = DataModelEvent.CHANGE;
                break;
            case DataStorageEvent.DELETE_DATA:
                type = DataModelEvent.REMOVE;
                break;
        }
        return type;
    }
}
