package org.apache.axis2.clustering;

/**
 * 
 */
public interface MessageSender {

    public void sendToGroup(ClusteringCommand msg) throws ClusteringFault;

    public void sendToSelf(ClusteringCommand msg) throws ClusteringFault;
}
