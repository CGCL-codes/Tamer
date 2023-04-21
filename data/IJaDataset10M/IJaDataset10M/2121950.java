package org.fbc.shogiserver.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.fbc.shogiserver.entities.Message;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class MessagesDAO {

    private static final Logger log = Logger.getLogger(MessagesDAO.class.getName());

    private static final MessagesDAO instance = new MessagesDAO();

    private MessagesDAO() {
    }

    public static MessagesDAO get() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public List<Message> getUserMesages(String nick) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Message.class);
        query.setFilter("addressee == nick");
        query.declareParameters("String nick");
        List<Message> result = new ArrayList<Message>();
        try {
            result = (List<Message>) query.execute(nick);
        } catch (Exception e) {
            log.warning(e.toString());
        } finally {
            query.closeAll();
        }
        return result;
    }

    public void postMessage(User user, String addressee, String subject, String message) {
        log.info("postMessage: " + user + ", " + addressee + ", " + subject + ", " + message);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Message m = null;
        if (addressee != null) {
            m = new Message(user, addressee, new Date(), null, subject, new Text(message));
            try {
                pm.makePersistent(m);
                log.info("new message persisted!");
            } finally {
                pm.close();
            }
        }
    }
}
