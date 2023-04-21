package org.jcvi.vics.compute.launcher.timeLogic;

import org.jboss.annotation.ejb.PoolClass;
import org.jcvi.vics.compute.engine.launcher.ejb.SeriesLauncherMDB;
import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Feb 4, 2010
 * Time: 3:29:47 PM
 * To change this template use File | Settings | File Templates.
 */
@MessageDriven(name = "TeraBlastLauncherMDB", activationConfig = { @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge "), @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener"), @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/TeraBlastLauncher"), @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "30"), @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "432000"), @ActivationConfigProperty(propertyName = "DLQMaxResent", propertyValue = "0") })
@PoolClass(value = org.jboss.ejb3.StrictMaxPool.class, maxSize = 30, timeout = 10000)
public class TeraBlastLauncherMDB extends SeriesLauncherMDB {
}
