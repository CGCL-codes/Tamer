package org.primordion.user.app.database.school.employee.server;

import javax.ejb.Stateless;
import org.primordion.user.app.database.school.employee.common.EmployeeDao_03;
import org.primordion.xholon.base.IMessage;

/**
 * EJB
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8 (Created on July 6, 2009)
*/
@Stateless
public class EmployeeBean implements Employee {

    public IMessage processReceivedSyncMessage(IMessage msg) {
        EmployeeDao_03 dao = new EmployeeDao_03();
        return dao.processReceivedSyncMessage(msg);
    }
}
