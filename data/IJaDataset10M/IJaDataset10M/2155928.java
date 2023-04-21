package com.pentagaia.tb.tx.impl;

import com.sun.sgs.service.Transaction;

/**
 * A special transaction handler used to receive the underlying pds transaction
 *
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
public interface ITransactionHandler {

    /**
     * Returns the core pds transaction
     * 
     * @return pds transaction
     */
    Transaction getTxn();
}
