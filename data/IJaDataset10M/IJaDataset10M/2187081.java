package org.posterita.model;

import org.compiere.model.MTransaction;

public class UDIMTransaction extends UDIPO {

    public UDIMTransaction(MTransaction mtransaction) {
        super(mtransaction);
    }

    public MTransaction getMTransaction() {
        return (MTransaction) getPO();
    }
}
