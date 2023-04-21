package com.mtgi.analytics.jmx;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.JmxUtils;
import com.mtgi.analytics.BehaviorEvent;
import com.mtgi.analytics.BehaviorEventPersister;

/**
 * <p>An event persister which aggregates statistics for each type of event in memory, exposing
 * those statistics as MBeans.</p>
 * 
 * <p>Configuration is very similar to that of the Spring Framework class MBeanExporter.
 * BehaviorEvent MBeans are named (and thus grouped for aggregation)
 * by an implementation of {@link #setNamingStrategy(ObjectNamingStrategy) ObjectNamingStrategy};
 * {@link EventTypeNamingStrategy} is used by default.  Each MBean is backed by an instance of
 * {@link StatisticsMBean}, which is converted into a ModelMBean by an instance of
 * {@link #setAssembler(MBeanInfoAssembler) MBeanInfoAssembler}; {@link MetadataMBeanInfoAssembler}
 * is used by default.  Statistics mbeans are registered in an instance of {@link #setServer(MBeanServer) MBeanServer};
 * {@link JmxUtils#locateMBeanServer()} is used to choose the default platform MBean server
 * if none is specified.</p>
 */
public class StatisticsMBeanEventPersisterImpl implements BehaviorEventPersister, InitializingBean, DisposableBean {

    private static final Log log = LogFactory.getLog(StatisticsMBeanEventPersisterImpl.class);

    private ObjectNamingStrategy namingStrategy;

    private MBeanInfoAssembler assembler;

    private MBeanServer server;

    private Map<ObjectName, StatisticsMBean> stats;

    public void setNamingStrategy(ObjectNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    public void setAssembler(MBeanInfoAssembler assembler) {
        this.assembler = assembler;
    }

    public void setServer(MBeanServer server) {
        this.server = server;
    }

    public void afterPropertiesSet() {
        if (assembler == null) {
            MetadataMBeanInfoAssembler assembler = new MetadataMBeanInfoAssembler();
            assembler.setAttributeSource(new AnnotationJmxAttributeSource());
            assembler.afterPropertiesSet();
            setAssembler(assembler);
        }
        if (namingStrategy == null) {
            EventTypeNamingStrategy strat = new EventTypeNamingStrategy();
            strat.afterPropertiesSet();
            setNamingStrategy(strat);
        }
        if (server == null) setServer(JmxUtils.locateMBeanServer());
        stats = new HashMap<ObjectName, StatisticsMBean>();
    }

    public void destroy() throws Exception {
        Set<ObjectName> names = stats.keySet();
        stats = null;
        for (ObjectName name : names) {
            try {
                server.unregisterMBean(name);
            } catch (Exception e) {
                log.error("Error unregistering statistics for " + name, e);
            }
        }
    }

    public void persist(Queue<BehaviorEvent> events) {
        for (BehaviorEvent event : events) {
            try {
                StatisticsMBean mbean = getStatistics(event);
                mbean.add(event);
            } catch (Exception e) {
                log.error("Error aggregating data for " + event, e);
            }
        }
    }

    /**
	 * Retrieve an aggregator for the given behavior event.  If no aggregator yet
	 * exists for the given event type, one is created and registered with the MBean
	 * server prior to being returned.
	 * @return the StatisticsMBean used to track aggregate data for <code>event</code>, never null
	 */
    protected StatisticsMBean getStatistics(BehaviorEvent event) throws RuntimeOperationsException, InvalidTargetObjectTypeException, JMException {
        ObjectName name = namingStrategy.getObjectName(event, null);
        StatisticsMBean ret = null;
        synchronized (stats) {
            ret = stats.get(name);
            if (ret == null) {
                ret = new StatisticsMBean();
                ModelMBeanInfo info = assembler.getMBeanInfo(ret, null);
                RequiredModelMBean mbean = new RequiredModelMBean(info);
                mbean.setManagedResource(ret, "ObjectReference");
                server.registerMBean(mbean, name);
                stats.put(name, ret);
            }
        }
        return ret;
    }
}
