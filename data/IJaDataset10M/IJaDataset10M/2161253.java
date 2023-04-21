package session;

import binds.Logs;
import javax.ejb.EJBLocalObject;
import ru.dreamjteam.mdb.Change;

/**
 *
 * @author диман
 */
public interface PutToLogLocal extends EJBLocalObject {

    void Put(Change message);

    Logs getHistory(String entity);
}
