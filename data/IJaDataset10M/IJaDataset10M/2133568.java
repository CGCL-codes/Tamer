package org.ji18n.core.container;

import java.net.URL;

/**
 * @version $Id: ContainerService.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public interface ContainerService {

    public String getName();

    public String[] getContainerNames();

    public String getConfigName();

    public void start();

    public void deploy(URL... urls);

    public String[] getObjectIds();

    public Object getObject(String id);

    public void stop();
}
