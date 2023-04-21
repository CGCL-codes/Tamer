package com.continuent.tungsten.commons.cluster.resource.notification;

import com.continuent.tungsten.commons.cluster.resource.ResourceState;
import com.continuent.tungsten.commons.cluster.resource.ResourceType;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.directory.Directory;

public class DirectoryNotification extends ClusterResourceNotification {

    /**
     * 
     */
    private static final long serialVersionUID = 6671252824107054673L;

    public DirectoryNotification(String clusterName, String memberName, String notificationSource, String resourceName, ResourceState resourceState, TungstenProperties resourceProps) {
        super(NotificationStreamID.MONITORING, clusterName, memberName, notificationSource, ResourceType.DIRECTORY, resourceName, resourceState, resourceProps);
        setResource((Directory) resourceProps.getObject("directory"));
    }

    public Directory getDirectory() {
        return (Directory) getResource();
    }
}
