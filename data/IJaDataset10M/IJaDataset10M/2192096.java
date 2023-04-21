package org.jcvi.vics.compute.launcher.rnaSeq;

import org.jboss.annotation.ejb.PoolClass;
import org.jcvi.vics.compute.engine.launcher.ejb.SeriesLauncherMDB;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Apr 2, 2010
 * Time: 1:57:12 PM
 */
@MessageDriven(name = "RnaSeqPipelineLauncherMDB", activationConfig = { @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge "), @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener"), @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/rnaSeqPipelineLauncher"), @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "5"), @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "432000"), @ActivationConfigProperty(propertyName = "DLQMaxResent", propertyValue = "0") })
@PoolClass(value = org.jboss.ejb3.StrictMaxPool.class, maxSize = 5, timeout = 10000)
public class RnaSeqPipelineLauncherMDB extends SeriesLauncherMDB {
}
