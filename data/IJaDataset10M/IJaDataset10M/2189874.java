package org.primordion.user.app.database.school.classroom.server;

import javax.ejb.Remote;
import org.primordion.xholon.base.IMessage;

/**
 * EJB
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on July 6, 2009)
*/
@Remote
public interface Classroom {

    public IMessage processReceivedSyncMessage(IMessage msg);
}
