package net.walend.somnifugi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NameParser;
import javax.naming.OperationNotSupportedException;
import javax.naming.InvalidNameException;
import javax.naming.CompositeName;

/**
SomniQueueContext is used by SomniQueueContextFactory as a workhorse Context. Use it to look up a SomniQueueConnection by asking for a "Connection",, a SomniQueueConnectionFactory by asking for a "ConnectionFactory", or to look up a Queue, by asking for anything else. (In general, you should let JNDI hanlde the ConnectionFactory for you.)

@author @dwalend@
@author @pklauser@
*/
public class SomniQueueContext extends SomniContext {

    SomniQueueContext(String name, Hashtable environment) {
        super(name, environment);
    }

    public Object lookup(String name) throws NamingException {
        try {
            if (name.equalsIgnoreCase(SomniProperties.CONNECTION)) {
                return new SomniQueueConnectionFactory().createQueueConnection(this);
            } else if (name.equalsIgnoreCase(SomniProperties.CONNECTIONFACTORY)) {
                return new SomniQueueConnectionFactory();
            } else {
                return SomniQueueCache.IT.getQueue(name, this);
            }
        } catch (SomniNamingException sne) {
            NamingException ne = new NamingException();
            ne.initCause(sne);
            throw ne;
        }
    }
}
