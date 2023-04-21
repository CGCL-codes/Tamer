package com.intel.gpe.client2.common.requests;

import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.client2.requests.BaseRequest;
import com.intel.gpe.clients.api.TargetSystemClient;
import com.intel.gpe.client2.common.i18n.Messages;
import com.intel.gpe.client2.common.i18n.MessagesKeys;
import java.text.MessageFormat;

/**
 * 
 * @author Alexander Lukichev
 * @version $Id: ListApplicationsRequest.java,v 1.7 2006/10/19 13:40:48 dizhigul Exp $
 *
 */
public class ListApplicationsRequest extends BaseRequest {

    private ClientWrapper<TargetSystemClient, ?> targetSystem;

    public ListApplicationsRequest(ClientWrapper<TargetSystemClient, ?> targetSystem) {
        super(MessageFormat.format(Messages.getString(MessagesKeys.common_requests_ListApplicationsRequest_List_applications_at), targetSystem));
        this.targetSystem = targetSystem;
    }

    public Object perform() throws Throwable {
        return targetSystem.getClient().getApplications();
    }

    public ClientWrapper<TargetSystemClient, ?> getTargetSystem() {
        return targetSystem;
    }
}
