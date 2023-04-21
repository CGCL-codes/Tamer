package com.c2b2.ipoint.management;

import com.c2b2.ipoint.model.HibernateUtil;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
  * This class initialises the Management MBeans required by iPoint and
  * provides a mechanism to retrieve the key MBeans.<br>
  * All the MBeans are placed into the default JDK 5.0 platform MBean Server.
  * <p>
  * $Date: 2007/07/03 16:57:46 $
  * 
  * $Id: ManagementMBeans.java,v 1.4 2007/07/03 16:57:46 steve Exp $<br>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please refer to your license agreement for terms and conditions.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.4 $
  */
public class ManagementMBeans {

    public static final String OBJECT_NAME_PREFIX = "com.c2b2.ipoint.";

    public static final String ATTRIBUTE_NAME = "ManagementMBeans";

    public enum StatisticsType {

        PageTotal, PagePreProcess, PageRender, PortletPreProcess, PortletRender, PortletResourceServe, MediaDownload, DocumentDownload, iPointController, RESTRequest
    }

    private String myDomain;

    private List<ObjectName> myRegisteredMBeans;

    private Map<String, AccessStatisticsMBean> myPageMBeans;

    private Logger myLogger;

    private ObjectName myHibernateServicesMBeanName;

    private MBeanServer myMBS;

    /**
   * Default Constructor
   * The Discriminator is added to the OBJECT_NAME_PREFIX to produce the fill domain
   * name for the ObjectName of the MBeans
   * @param discriminator The name of this specific management mbeans interface
   */
    public ManagementMBeans(String discriminator) {
        myDomain = OBJECT_NAME_PREFIX + discriminator;
        myRegisteredMBeans = new ArrayList<ObjectName>(1024);
        myLogger = Logger.getLogger(this.getClass().getName());
        myMBS = ManagementFactory.getPlatformMBeanServer();
        myPageMBeans = new HashMap<String, AccessStatisticsMBean>(100);
    }

    /**
   * Initialises all the static MBeans into the MBean Server
   */
    public void initialise() {
        try {
            ObjectName on = new ObjectName(myDomain + ":type=HibernateStatistics,name=HibernateStatistics");
            myMBS.registerMBean(HibernateUtil.getStatisticsService(), on);
            myLogger.fine("Registered Hibernate MBean with Object Name " + on.toString());
            myRegisteredMBeans.add(on);
            myHibernateServicesMBeanName = on;
        } catch (Exception e) {
            myLogger.log(Level.WARNING, "There was a problem registering the Hibernate Statistics MBean. Statistics will not be available", e);
        }
        myLogger.info("iPoint Management MBeans Initialised");
    }

    /**
   * Removes all the MBeans from the MBean Server
   */
    public void shutdown() {
        for (ObjectName on : myRegisteredMBeans) {
            try {
                myMBS.unregisterMBean(on);
            } catch (Exception e) {
                myLogger.log(Level.WARNING, "There was a problem removing the MBean " + on.toString(), e);
            }
        }
        myLogger.info("iPoint Management MBeans Shutdown");
    }

    /**
   * Accessor for the Hibernate JMX Statistics service object name
   * @return The Statistics Service object name
   */
    public ObjectName getHibernateStatistics() {
        return myHibernateServicesMBeanName;
    }

    /**
   * Increments the Statistics for an AccessStatistics MBean
   * @param type The type of access statistics to record
   */
    public void incrementStatistics(ManagementMBeans.StatisticsType type, String name, long accessTime, boolean cached) {
        try {
            String objectName = myDomain + ":type=AccessStatistics,StatType=" + type.toString() + ",Name=" + name;
            ObjectName on = new ObjectName(objectName);
            if (!myMBS.isRegistered(on)) {
                AccessStatistics pageMBean = new AccessStatistics();
                myMBS.registerMBean(pageMBean, on);
                myPageMBeans.put(objectName, pageMBean);
                myRegisteredMBeans.add(on);
            }
            AccessStatisticsMBean pageMBean = myPageMBeans.get(objectName);
            pageMBean.accessed(accessTime, cached);
        } catch (Exception e) {
            myLogger.log(Level.WARNING, "There was a problem incrementing a Statistics MBean. Statistics will not be available", e);
        }
    }

    /**
   * Registers a Portal Session MBean
   * @param psm The mbean to register
   */
    public void registerSessionMBean(PortalSession psm) {
        try {
            String objectName = myDomain + ":type=PortalSession,Name=" + psm.getId();
            ObjectName on = new ObjectName(objectName);
            if (!myMBS.isRegistered(on)) {
                myMBS.registerMBean(psm, on);
                myRegisteredMBeans.add(on);
            }
        } catch (Exception e) {
            myLogger.log(Level.WARNING, "There was a problem registering a Session MBean. Session information will not be available", e);
        }
    }

    /**
   * Removes a Portal Session MBean
   * @param psm
   */
    public void removeSessionMBean(PortalSession psm) {
        try {
            String objectName = myDomain + ":type=PortalSession,Name=" + psm.getId();
            ObjectName on = new ObjectName(objectName);
            if (myMBS.isRegistered(on)) {
                myMBS.unregisterMBean(on);
                myRegisteredMBeans.remove(on);
            }
        } catch (Exception e) {
            myLogger.log(Level.WARNING, "There was a problem removing a Session MBean.", e);
        }
    }
}
