package net.walend.somnifugi;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

/**
SomniQueueContextFactory is a ContextFactory for shortcutting JNDI. Use it where you want to be able to reconfigure to use JNDI without thinking too hard about it, then configure.

<code>
<pre>
                //set things up in JNDI
                Hashtable env = new Hashtable(3);

                env.put(Context.INITIAL_CONTEXT_FACTORY,"net.walend.somnifugi.SomniQueueContextFactory");
                env.put(Context.PROVIDER_URL,"http://www.walend.net");

                // Create the initial context
                Context ctx = new InitialContext(env);
                
                //get the parts
                QueueConnection connection = (QueueConnection)ctx.lookup("Connection");
                Queue queue = (Queue)ctx.lookup("hotStartTest");

                ctx.close();
</pre>
</code>

@author @pklauser@
@author @dwalend@
*/
public class SomniQueueContextFactory implements InitialContextFactory {

    public SomniQueueContextFactory() {
    }

    public Context getInitialContext(Hashtable<?, ?> environment) {
        String contextName = (String) environment.get(SomniProperties.QUEUECONTEXTNAMEKEY);
        if (contextName == null) {
            contextName = SomniProperties.QUEUEDEFAULTCONTEXTNAME;
        }
        return new SomniQueueContext(contextName, environment);
    }
}
