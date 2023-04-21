package honeycrm.client.services;

import honeycrm.client.misc.ServiceCallStatistics;
import java.util.Collection;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("common")
public interface CommonService extends RemoteService {

    public void feedback(final String message);

    public Collection<ServiceCallStatistics> getServiceCallStatistics();

    public void bulkCreate();

    public void bulkRead();
}
