package org.vosao.business.impl.mq.subscriber;

import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.QueueSpeed;
import org.vosao.business.mq.Topic;
import org.vosao.business.mq.message.SimpleMessage;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class EntityRemove extends AbstractSubscriber {

    public void onMessage(Message message) {
        SimpleMessage msg = (SimpleMessage) message;
        String kind = msg.getMessage();
        DatastoreService datastore = getBusiness().getSystemService().getDatastore();
        Query query = new Query(kind);
        PreparedQuery results = datastore.prepare(query);
        int i = 0;
        boolean end = true;
        for (Entity entity : results.asIterable()) {
            datastore.delete(entity.getKey());
            i++;
            if (getBusiness().getSystemService().getRequestCPUTimeSeconds() > 25) {
                addEntityRemoveTask(kind);
                end = false;
                break;
            }
        }
        logger.info("Deleted " + i + " entities " + kind);
        if (end) {
            logger.info("Finished entity removing.");
        }
    }

    private void addEntityRemoveTask(String kind) {
        getMessageQueue().publish(new SimpleMessage(Topic.ENTITY_REMOVE, kind, QueueSpeed.LOW));
        logger.info("Added new entity remove task " + kind);
    }
}
