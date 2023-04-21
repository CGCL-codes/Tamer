package ch.isbiel.oois.common.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBHome;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * TODO
 *
 *
 * @author $Author$
 * @version $Revision$
 */
public final class ServiceLocator {

    private Context _initialContext;

    private Map _cache;

    public ServiceLocator(String providerURL) throws ServiceLocatorException {
        try {
            _initialContext = new InitialContext();
            _cache = Collections.synchronizedMap(new HashMap());
        } catch (NamingException ne) {
            throw new ServiceLocatorException("Could not create InitialContext.", ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    /**
   * Get the EJB remote home factory.
   *
   * @param jndiHomeName TODO
   * @param className TODO
   * @return the EJB home corresponding to the homeName
   * @throws ServiceLocatorException TODO
  */
    public EJBHome getRemoteHome(String jndiHomeName, Class className) throws ServiceLocatorException {
        EJBHome home = null;
        try {
            if (_cache.containsKey(jndiHomeName)) {
                home = (EJBHome) _cache.get(jndiHomeName);
            } else {
                Object objref = _initialContext.lookup(jndiHomeName);
                Object obj = PortableRemoteObject.narrow(objref, className);
                _cache.put(jndiHomeName, obj);
                home = (EJBHome) obj;
            }
        } catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return home;
    }

    /**
   * Get the remote EJB.
   *
   * @param clazz TODO
   * @return the remote EJB
   * @throws ServiceLocatorException TODO
  */
    @SuppressWarnings("unchecked")
    public <T> T getRemoteEJB(Class<T> clazz) throws ServiceLocatorException {
        try {
            final String appName = "infocore";
            final String moduleName = "ejb";
            final String distinctName = "";
            String beanName = clazz.getSimpleName().replace("Remote", "Bean");
            String viewClassName = clazz.getName();
            return (T) _initialContext.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
    }

    /**
   * Get a queue connection factory
   * 
   * @param qcfName TODO
   * @return the queue connection factory
   * @throws ServiceLocatorException TODO
  */
    public QueueConnectionFactory getQueueConnectionFactory(String qcfName) throws ServiceLocatorException {
        QueueConnectionFactory factory = null;
        try {
            if (_cache.containsKey(qcfName)) {
                factory = (QueueConnectionFactory) _cache.get(qcfName);
            } else {
                factory = (QueueConnectionFactory) _initialContext.lookup(qcfName);
                _cache.put(qcfName, factory);
            }
        } catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return factory;
    }

    /**
   * Get a queue
   * 
   * @param queueName TODO
   * @return the queue
   * @throws ServiceLocatorException TODO
  */
    public Queue getQueue(String queueName) throws ServiceLocatorException {
        Queue queue = null;
        try {
            if (_cache.containsKey(queueName)) {
                queue = (Queue) _cache.get(queueName);
            } else {
                queue = (Queue) _initialContext.lookup(queueName);
                _cache.put(queueName, queue);
            }
        } catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return queue;
    }

    /**
   * Get a topic conncetion factory
   *
   * @param tcfName TODO
   * @return the topic connection factory
   * @throws ServiceLocatorException TODO
  */
    public TopicConnectionFactory getTopicConnectionFactory(String tcfName) throws ServiceLocatorException {
        TopicConnectionFactory factory = null;
        try {
            if (_cache.containsKey(tcfName)) {
                factory = (TopicConnectionFactory) _cache.get(tcfName);
            } else {
                factory = (TopicConnectionFactory) _initialContext.lookup(tcfName);
                _cache.put(tcfName, factory);
            }
        } catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return factory;
    }

    /**
   * Get a topic
   * 
   * @param topicName TODO
   * @return the topic
   * @throws ServiceLocatorException TODO
  */
    public Topic getTopic(String topicName) throws ServiceLocatorException {
        Topic topic = null;
        try {
            if (_cache.containsKey(topicName)) {
                topic = (Topic) _cache.get(topicName);
            } else {
                topic = (Topic) _initialContext.lookup(topicName);
                _cache.put(topicName, topic);
            }
        } catch (NamingException ne) {
            throw new ServiceLocatorException(ne);
        } catch (Exception e) {
            throw new ServiceLocatorException(e);
        }
        return topic;
    }
}
