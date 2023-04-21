package br.ufmg.ubicomp.decs.server.eventservice.base;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import br.ufmg.ubicomp.decs.client.eventservice.service.EventService;
import br.ufmg.ubicomp.decs.client.eventservice.statistics.StatisticsContainer;
import br.ufmg.ubicomp.decs.server.eventservice.entity.AbstractEntity;
import br.ufmg.ubicomp.decs.server.eventservice.entity.EventMessage;
import br.ufmg.ubicomp.decs.server.eventservice.entity.PropertyEntity;

public class EventDataManager {

    private static EventDataManager instance;

    private StatisticsContainer statisticsContainer = new StatisticsContainer();

    private EventDataManager() {
    }

    public static EventDataManager getInstance() {
        if (instance == null) {
            instance = new EventDataManager();
        }
        return instance;
    }

    public AbstractEntity getMessage(String key) {
        AbstractEntity evm = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(PropertyEntity.class);
        query.setFilter("entityKey == entityKeyParam");
        query.declareParameters("String entityKeyParam");
        try {
            List<PropertyEntity> results = (List<PropertyEntity>) query.execute(key);
            if (results.iterator().hasNext()) {
                evm = new EventMessage(key);
                for (PropertyEntity pe : results) {
                    evm.put(pe.getPropertyName(), pe.getPropertyValue());
                }
            } else {
            }
        } finally {
            query.closeAll();
        }
        return evm;
    }

    public void saveMessage(AbstractEntity message) {
        PersistenceManager persistenceManager = PMF.get().getPersistenceManager();
        AbstractEntity em2 = getMessage(message.getKey());
        for (String s : message.getPropertiesList()) {
            PropertyEntity e = new PropertyEntity();
            e.setEntityName(message.getEntityName());
            e.setPropertyName(s);
            e.setPropertyValue(message.get(s));
            e.setEntityKey(message.getKey());
            persistenceManager.makePersistent(e);
        }
        persistenceManager.close();
    }

    public EventMessage getEventFromName(String name) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(EventMessage.class);
        query.setFilter("name == nameParam");
        EventMessage msg = null;
        query.declareParameters("String nameParam");
        try {
            List<EventMessage> results = (List<EventMessage>) query.execute(name);
            if (results.iterator().hasNext()) {
                msg = results.iterator().next();
            }
        } finally {
            query.closeAll();
        }
        return msg;
    }

    public EventMessage getEventMessage(String key) {
        long start = System.currentTimeMillis();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        EventMessage msg = null;
        try {
            msg = pm.getObjectById(EventMessage.class, Double.parseDouble(key));
        } finally {
            pm.close();
        }
        long time = System.currentTimeMillis() - start;
        statisticsContainer.update("Query time", time);
        statisticsContainer.update("Queries executed");
        return msg;
    }

    public List<EventMessage> getEventMessagesPerTopic(String topic) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(EventMessage.class);
        query.setFilter("topic == topicParam");
        EventMessage msg = null;
        query.declareParameters("String topicParam");
        List<EventMessage> results = null;
        try {
            results = (List<EventMessage>) query.execute(topic);
        } finally {
            query.closeAll();
        }
        return results;
    }

    public List<EventMessage> getEventsByState(String state) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(EventMessage.class);
        query.setFilter("state == stateParam");
        EventMessage msg = null;
        query.declareParameters("String stateParam");
        List<EventMessage> results = null;
        try {
            results = (List<EventMessage>) query.execute(state);
        } finally {
            query.closeAll();
        }
        return results;
    }

    public List<EventMessage> getEventsByTopic(String topic) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(EventMessage.class);
        query.setFilter("topic == topicParam");
        EventMessage msg = null;
        query.declareParameters("String topicParam");
        List<EventMessage> results = null;
        try {
            results = (List<EventMessage>) query.execute(topic);
        } finally {
            query.closeAll();
        }
        return results;
    }

    public List<EventMessage> getEventsByConsumer(String consumer, String state) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(EventMessage.class);
        String q = "consumer == consumerParam";
        q = q + " and state == stateParam";
        query.declareParameters("String consumerParam");
        query.declareParameters("String stateParam");
        query.setFilter(q);
        List<EventMessage> results = null;
        try {
            results = (List<EventMessage>) query.execute(consumer);
        } finally {
            query.closeAll();
        }
        return results;
    }

    public List<EventMessage> getEventsByStateAndTopic(String state, String topic) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(EventMessage.class);
        query.setFilter("topic == topicParam and state == stateParam");
        EventMessage msg = null;
        query.declareParameters("String topicParam");
        query.declareParameters("String stateParam");
        List<EventMessage> results = null;
        try {
            results = (List<EventMessage>) query.execute(topic, state);
        } finally {
            query.closeAll();
        }
        return results;
    }

    public List<EventMessage> getEventsPublished() {
        return getEventsByState(EventService.STATE_CREATED);
    }
}
