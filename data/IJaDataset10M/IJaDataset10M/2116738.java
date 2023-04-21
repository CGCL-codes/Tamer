package org.ombu.client;

import javax.ejb.Local;

@Local
public interface CompletionCoordinatorControllerLocal {

    public AtomicTransactionOperationResult commit(Long participantId, String secret);

    public AtomicTransactionOperationResult rollback(Long participantId, String secret);
}
