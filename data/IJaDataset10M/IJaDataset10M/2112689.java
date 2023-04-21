package soma.rest.training.jerseySpringJDO.dao;

import java.util.Collection;
import java.util.Date;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import soma.rest.training.jerseySpringJDO.model.Verb;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Repository
public class VerbDAO {

    @Autowired
    private PersistenceManagerFactory pmf;

    public static final String TAG = "VerbDAO";

    final Logger logger = LoggerFactory.getLogger(VerbDAO.class);

    public Verb get(String word) {
        logger.info(TAG + "   ### get = Verb text  // " + word.toString());
        Key key = KeyFactory.createKey(Verb.class.getSimpleName(), word);
        logger.info(TAG + "   ### make key ");
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            Verb verb = pm.getObjectById(Verb.class, key);
            logger.info(TAG + "   ### try before :  " + verb.toString());
            return pm.detachCopy(verb);
        } catch (JDOObjectNotFoundException e) {
            logger.warn(TAG + "   ### text get error text ");
            throw e;
        } finally {
            pm.close();
        }
    }

    public void create(Verb verb) {
        logger.info(TAG + "   ### create = Verb text  // " + verb.toString());
        Key key = KeyFactory.createKey(Verb.class.getSimpleName(), verb.getText());
        logger.info(TAG + "   ### make key ");
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            verb.setKey(key);
            logger.info(TAG + "   ### verb.setKey(key)");
            verb.setTag("msges");
            logger.info(TAG + "   ### setTag(msges)");
            pm.makePersistent(verb);
        } catch (JDOObjectNotFoundException e) {
            logger.warn(TAG + "    ### text create error id");
            throw e;
        } finally {
            pm.close();
        }
    }

    public Verb save(Verb verb) {
        logger.info(TAG + "    ### save start  // " + verb.toString());
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            logger.info(TAG + "    ### make pm ");
            Key key = KeyFactory.createKey(Verb.class.getSimpleName(), verb.getText());
            verb.setKey(key);
            verb.setTag("msges");
            Verb returnText = pm.makePersistent(verb);
            logger.info(TAG + "    ### save = returnText   // " + returnText.toString());
            return pm.detachCopy(returnText);
        } catch (JDOObjectNotFoundException e) {
            logger.warn(TAG + "    ### text save error id");
        } finally {
            logger.warn(TAG + "    ### text save ok ");
            pm.close();
        }
        return verb;
    }

    public void delete(Verb text) {
        PersistenceManager pm = null;
        logger.info(TAG + "   ### deleteMsg = Verb id  // " + text.toString());
        Key key = KeyFactory.createKey(Verb.class.getSimpleName(), text.getText());
        logger.info(TAG + "   ### make key ");
        try {
            pm = pmf.getPersistenceManager();
            Verb deletetext = pm.getObjectById(Verb.class, key);
            logger.info(TAG + "   ### deleteText =  id  // " + deletetext.toString());
            pm.deletePersistent(deletetext);
            logger.info(TAG + "   ###    pm.deletePersistent(deletetext)");
        } finally {
            pm.close();
        }
    }

    public boolean exist(String text) {
        logger.info(TAG + "   ### exist start = Text id  // " + text);
        PersistenceManager pm = null;
        Key key = KeyFactory.createKey(Verb.class.getSimpleName(), text);
        logger.info(TAG + "   ### make key ");
        try {
            pm = pmf.getPersistenceManager();
            logger.info(TAG + "   ### exist = Text text  // " + text);
            Verb existtext = pm.getObjectById(Verb.class, key);
            if (existtext == null) {
                logger.info(TAG + "   ### exist = Text text = " + text + " existtext == null  ");
                return false;
            } else {
                logger.info(TAG + "   ### exist = Text text = " + text + " existtext  ok!! ");
                return true;
            }
        } catch (DataAccessException e) {
            return false;
        } finally {
            pm.close();
        }
    }

    public Collection<Verb> search(String word) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Verb.class, "tag == input");
        query.declareParameters("String input");
        Collection<Verb> msgs = (Collection<Verb>) query.execute(word);
        return pm.detachCopyAll(msgs);
    }

    public void log(Date date, String... args) {
        System.out.println(date);
        for (String string : args) {
            System.out.println(string);
        }
    }
}
