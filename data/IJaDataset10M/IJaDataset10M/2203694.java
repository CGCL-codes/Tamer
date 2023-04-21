package sk.naive.talker;

import sk.naive.talker.persistence.*;
import java.util.Set;

/**
 *
 * @author <a href="mailto:virgo@naive.deepblue.sk">Richard "Virgo" Richter</a>
 * @version $Revision: 1.2 $ $Date: 2003/12/29 21:33:23 $
 */
public interface BoardPersistence extends PropertiesObjectPersistence {

    Set boardNames() throws PersistenceException;

    Integer boardId(String name) throws PersistenceException;
}
