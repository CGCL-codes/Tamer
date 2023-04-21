package com.continuent.tungsten.commons.cluster.resource.notification;

import com.continuent.tungsten.commons.cluster.resource.ResourceState;
import com.continuent.tungsten.commons.cluster.resource.ResourceType;
import com.continuent.tungsten.commons.cluster.resource.SQLRouter;
import com.continuent.tungsten.commons.config.TungstenProperties;

public class SQLRouterNotification extends ClusterResourceNotification {

    /**
     * 
     */
    private static final long serialVersionUID = -7101382528522639737L;

    /**
     * @param clusterName
     * @param memberName TODO
     * @param notificationSource
     * @param resourceName
     * @param resourceState
     * @param resourceProps
     */
    public SQLRouterNotification(String clusterName, String memberName, String notificationSource, String resourceName, ResourceState resourceState, TungstenProperties resourceProps) {
        super(NotificationStreamID.MONITORING, clusterName, memberName, notificationSource, ResourceType.SQLROUTER, resourceName, resourceState, resourceProps);
        setResource(new SQLRouter(resourceProps));
    }

    public SQLRouter getReplicator() {
        return (SQLRouter) getResource();
    }
}
