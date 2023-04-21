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
public class ImpactTypesTypeUpdateTransaction extends AbsUpdateTransaction {

    private Logger logger = Logger.getLogger(ImpactTypesTypeUpdateTransaction.class.getPackage().getName());

    public ImpactTypesTypeUpdateTransaction(VersionedItemType transObject, RT container, UpdateResponse response, String name) {
        super(transObject, container, response, name);
    }

    @Override
    protected void handleNewTransaction() {
        ImpactTypesType newData = ((RT) super.container).addNewImpactTypes();
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
