package balmysundaycandy.more.low.level.operations.datastore.impl;

import java.util.concurrent.Future;
import balmysundaycandy.core.future.ProtocolMessageFuture;
import balmysundaycandy.core.operations.OperationNamespaces.datastore_v3;
import balmysundaycandy.more.low.level.operations.datastore.DatastoreOperation;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.DatastorePb.BeginTransactionRequest;
import com.google.apphosting.api.DatastorePb.Transaction;

/**
 * begin transaction operation.
 * 
 * @author marblejenka
 */
public final class BeginTransactionOperation extends DatastoreOperation<BeginTransactionRequest, Transaction> {

    @Override
    public Transaction call(BeginTransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.mergeFrom(ApiProxy.makeSyncCall(datastore_v3.packageName, datastore_v3.methodName.BeginTransaction, request.toByteArray()));
        return transaction;
    }

    @Override
    public Future<Transaction> callAsync(BeginTransactionRequest request, ApiConfig apiConfig) {
        Transaction response = new Transaction();
        return new ProtocolMessageFuture<Transaction>(ApiProxy.makeAsyncCall(datastore_v3.packageName, datastore_v3.methodName.BeginTransaction, request.toByteArray(), apiConfig), response);
    }
}
