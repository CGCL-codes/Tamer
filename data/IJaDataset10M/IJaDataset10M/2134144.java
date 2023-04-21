package org.objectwiz.plugin.jee.appserver;

/**
 * Listener of {@link DeploymentNotification}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public interface DeploymentNotificationListener {

    public void onDeploymentNotification(DeploymentNotification dn);
}
