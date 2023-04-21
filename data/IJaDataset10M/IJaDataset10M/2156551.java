package org.tei.comparator.web.client.rpc;

import org.tei.comparator.web.client.db.TCLink;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ManageMatch")
public interface ManageMatchService extends RemoteService {

    public boolean removeMatch(TCLink link);

    public boolean confirmMatch(TCLink link);
}
