package soma.rest.training.jerseySpringJDO.dao;

import java.util.Collection;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import soma.rest.training.jerseySpringJDO.model.Message;

@Repository("messageDAO")
public class MessageDAOImpl {

    @Autowired
    private PersistenceManagerFactory pmf;

    public Message get(Long id) {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Message message = pm.getObjectById(Message.class, id);
            return pm.detachCopy(message);
        } finally {
            pm.close();
        }
    }

    public Message save(Message message) {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Message returnMessage = pm.makePersistent(message);
            return pm.detachCopy(returnMessage);
        } finally {
            pm.close();
        }
    }

    public void delete(Long id) {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Message message = get(id);
            pm.deletePersistent(message);
        } finally {
            pm.close();
        }
    }

    public Collection<Message> search(String word) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Message.class, "text == input");
        query.declareParameters("String input");
        Collection<Message> msgs = (Collection<Message>) query.execute(word);
        return pm.detachCopyAll(msgs);
    }

    public void log(Date date, String... args) {
        System.out.println(date);
        for (String string : args) {
            System.out.println(string);
        }
    }
}
