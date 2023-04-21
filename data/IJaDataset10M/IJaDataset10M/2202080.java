package kite.sample03.dao;

import java.util.List;
import kite.sample03.model.Message;

/**
 * @version $Id$
 * @author Willie Wheeler
 * @since 1.0
 */
public interface MessageDao {

    Message getMotd();

    List<Message> getImportantMessages();
}
