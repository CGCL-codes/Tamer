package org.ochan.service.remote.webservice;

import org.ochan.service.remote.model.RemoteGroup;
import org.ochan.service.remote.model.RemoteStatistics;

public interface InstanceStatistics {

    public RemoteGroup getImages();

    public RemoteStatistics getStatistics();
}
