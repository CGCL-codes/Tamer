package org.opennms.netmgt.dao.jdbc;

import java.util.Collection;
import java.util.Set;
import javax.sql.DataSource;
import org.opennms.netmgt.dao.EventDao;
import org.opennms.netmgt.dao.jdbc.event.EventDelete;
import org.opennms.netmgt.dao.jdbc.event.EventSave;
import org.opennms.netmgt.dao.jdbc.event.EventUpdate;
import org.opennms.netmgt.dao.jdbc.event.FindAll;
import org.opennms.netmgt.dao.jdbc.event.FindByEventId;
import org.opennms.netmgt.dao.jdbc.event.LazyEvent;
import org.opennms.netmgt.model.OnmsEvent;

public class EventDaoJdbc extends AbstractDaoJdbc implements EventDao {

    public EventDaoJdbc() {
        super();
    }

    public EventDaoJdbc(DataSource ds) {
        super(ds);
    }

    public int countAll() {
        return getJdbcTemplate().queryForInt("select count(*) from event");
    }

    public void delete(OnmsEvent event) {
        if (event.getId() == null) throw new IllegalArgumentException("cannot delete null event");
        getEventDeleter().doDelete(event);
    }

    public Collection findAll() {
        return new FindAll(getDataSource()).findSet();
    }

    public void flush() {
    }

    public OnmsEvent get(int id) {
        return get(new Integer(id));
    }

    public OnmsEvent get(Integer id) {
        if (Cache.retrieve(OnmsEvent.class, id) == null) return new FindByEventId(getDataSource()).findUnique(id); else return (OnmsEvent) Cache.retrieve(OnmsEvent.class, id);
    }

    public OnmsEvent load(int id) {
        return load(new Integer(id));
    }

    public OnmsEvent load(Integer id) {
        OnmsEvent event = get(id);
        if (event == null) throw new IllegalArgumentException("unable to load event with id " + id);
        return event;
    }

    public void save(OnmsEvent event) {
        if (event.getId() != null) throw new IllegalArgumentException("Cannot save an event that already has a eventid");
        event.setId(allocateId());
        getEventSaver().doInsert(event);
        cascadeSaveAssociations(event);
    }

    public void saveOrUpdate(OnmsEvent event) {
        if (event.getId() == null) save(event); else update(event);
    }

    public void update(OnmsEvent event) {
        if (event.getId() == null) throw new IllegalArgumentException("Cannot update an event without a null event");
        if (isDirty(event)) getEventUpdater().doUpdate(event);
        cascadeUpdateAssociations(event);
    }

    private boolean isDirty(OnmsEvent event) {
        if (event instanceof LazyEvent) {
            LazyEvent lazyEvent = (LazyEvent) event;
            return lazyEvent.isDirty();
        }
        return true;
    }

    private Integer allocateId() {
        return new Integer(getJdbcTemplate().queryForInt("SELECT nextval('eventsNxtId')"));
    }

    private void cascadeSaveAssociations(OnmsEvent event) {
    }

    private void cascadeUpdateAssociations(OnmsEvent event) {
    }

    private EventDelete getEventDeleter() {
        return new EventDelete(getDataSource());
    }

    private EventSave getEventSaver() {
        return new EventSave(getDataSource());
    }

    private EventUpdate getEventUpdater() {
        return new EventUpdate(getDataSource());
    }

    public Set findEvents(OnmsEvent event) {
        return null;
    }
}
