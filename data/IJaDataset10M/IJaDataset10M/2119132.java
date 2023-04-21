package org.jumpmind.pos.service.transaction;

import org.jumpmind.pos.common.item.SellItem;
import org.jumpmind.pos.common.store.WorkstationInfo;
import org.jumpmind.pos.domain.transaction.RetailTransaction;
import org.jumpmind.pos.service.IService;

public interface ITransactionService extends IService {

    public RetailTransaction createRetailTransaction(WorkstationInfo workstation);

    public void sellItem(RetailTransaction transaction, SellItem sellItem);
}
