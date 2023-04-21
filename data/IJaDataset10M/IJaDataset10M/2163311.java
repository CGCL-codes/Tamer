package org.jpos.apps.qsp.config;

import javax.management.NotCompliantMBeanException;
import org.jpos.apps.qsp.QSP;
import org.jpos.apps.qsp.QSPConfigurator;
import org.jpos.core.ConfigurationException;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;
import org.jpos.util.NameRegistrar;
import org.jpos.util.ThreadPool;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Configure ThreadPool
 * @author <A href=mailto:alcarraz@fing.edu.uy>Andr&eacute;s Alcarraz</A>
 * @version $Revision: 1745 $ $Date: 2003-10-13 07:04:20 -0400 (Mon, 13 Oct 2003) $
 */
public class ConfigThreadPool implements QSPConfigurator {

    public void config(QSP qsp, Node node) throws ConfigurationException {
        LogEvent evt = new LogEvent(qsp, "config-thread-pool");
        try {
            NamedNodeMap atts = node.getAttributes();
            int initialSize = Integer.parseInt(atts.getNamedItem("initial-size").getNodeValue());
            int maxSize = Integer.parseInt(atts.getNamedItem("max-size").getNodeValue());
            ThreadPool threadPool = new ThreadPool(initialSize, maxSize);
            threadPool.setLogger(ConfigLogger.getLogger(node), ConfigLogger.getRealm(node));
            evt.addMessage(threadPool);
            String name = atts.getNamedItem("name").getNodeValue();
            NameRegistrar.register("thread.pool." + name, threadPool);
            try {
                qsp.registerMBean(threadPool, "type=thread-pool,name=" + name);
            } catch (NotCompliantMBeanException e) {
                evt.addMessage(e.getMessage());
            }
        } catch (Exception e) {
            evt.addMessage(e);
        } finally {
            Logger.log(evt);
        }
    }

    public static ThreadPool getThreadPool(Node node) {
        Node n = node.getAttributes().getNamedItem("thread-pool");
        if (n != null) {
            try {
                return ThreadPool.getThreadPool(n.getNodeValue());
            } catch (NotFoundException e) {
            }
        }
        return null;
    }
}
