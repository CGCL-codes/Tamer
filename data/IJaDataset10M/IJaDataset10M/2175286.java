package org.mitre.rt.client.synchronize.transactions.update;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.*;
import org.mitre.rt.common.*;
import org.mitre.rt.client.synchronize.transactions.AbsUpdateTransaction;
import org.mitre.rt.rtclient.*;
import org.mitre.rt.rtclient.RTDocument.RT;
import org.mitre.rt.rtclient.RTDocument.RT.*;

/**
 *
 * @author BWORRELL
 */
public class ImpactLevelsTypeUpdateTransaction extends AbsUpdateTransaction {

    private Logger logger = Logger.getLogger(ImpactLevelsTypeUpdateTransaction.class.getPackage().getName());

    public ImpactLevelsTypeUpdateTransaction(VersionedItemType transObject, RT container, UpdateResponse response, String name) {
        super(transObject, container, response, name);
    }

    @Override
    protected void handleNewTransaction() {
        ImpactLevelsType newData = ((RT) super.container).addNewImpactLevels();
        newData.set(super.response.getServerItem());
    }

    @Override
    protected void handleModifiedTransaction() {
        super.defaultModifiedTransaction();
    }

    @Override
    protected void handleDeletedTransaction() {
        ((UserType) super.transObject).setDeleted(true);
    }
}
