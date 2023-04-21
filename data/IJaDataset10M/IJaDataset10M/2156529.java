package com.teletalk.jserver.queue;

import java.util.ArrayList;
import com.teletalk.jserver.comm.EndPointIdentifier;
import com.teletalk.jserver.queue.command.MultiQueueItemTransferRequest;
import com.teletalk.jserver.queue.command.QueueItemCancellationRequest;
import com.teletalk.jserver.queue.command.QueueItemCompletionResponse;
import com.teletalk.jserver.queue.command.QueueItemQuery;
import com.teletalk.jserver.queue.command.QueueItemRelocationRequest;
import com.teletalk.jserver.queue.command.QueueItemTransferRequest;
import com.teletalk.jserver.queue.command.QueueSystemSynchronizationRequest;
import com.teletalk.jserver.queue.command.QueueSystemSynchronizationResponse;

/**
 * Interface for classes that implement internal mode dependent implementation of methods in QueueManager.
 * 
 * @author Tobias L�fstrand
 * 
 * @since 2.1.2 (20060224)
 */
public interface QueueManagerImpl {

    /**
    * performInQueueCheck
    */
    public ArrayList performInQueueCheck();

    /**
    * performOutQueueCheck
    */
    public ArrayList performOutQueueCheck(ArrayList responsesToExecute);

    /**
    * initiateQueueSystemSynchronization
    */
    public QueueSystemSynchronizationRequest initiateQueueSystemSynchronization(final RemoteQueueSystem rqs);

    /**
    * Synchronize in queue with remote out queue
    */
    public QueueSystemSynchronizationResponse queueSystemSynchronizationRequestReceived(final RemoteQueueSystem rqs, final QueueSystemSynchronizationRequest synchRequest);

    /**
    * Synchronize out queue with remote in queue
    */
    public void queueSystemSynchronizationResponseReceived(RemoteQueueSystem rqs, QueueSystemSynchronizationResponse synchResponse);

    /**
    * recoveredItemsInInQueue
    */
    public void recoveredItemsInInQueue();

    /**
    * recoveredItemsInOutQueue
    */
    public void recoveredItemsInOutQueue();

    /**
    * createOutgoingQueueItems
    */
    public QueueItem[] createOutgoingQueueItems(final QueueItemData[] itemData, final QueueItem parentItem, boolean add) throws QueueStorageException;

    /**
    * createOutgoingQueueItem
    */
    public QueueItem createOutgoingQueueItem(final QueueItemData itemData, final QueueItem parentItem, boolean add) throws QueueStorageException;

    /**
    * dispatchQueueItem
    */
    public void dispatchQueueItem(QueueItem qItem, final EndPointIdentifier address) throws QueueStorageException;

    /**
    * dispatchQueueItems
    */
    public void dispatchQueueItems(QueueItem[] qItems, final EndPointIdentifier address) throws QueueStorageException;

    /**
    * dispatchQueueItemQuery
    */
    public void dispatchQueueItemQuery(QueueItem outQueueItem);

    /**
    * isInItemCompleted
    */
    public boolean isInItemCompleted(final QueueItem inItem);

    /**
    * inItemDone
    */
    public void inItemDone(final QueueItem inItem, final byte responseType, final Object responseData);

    /**
    * cancelInItem
    */
    public void cancelInItem(final QueueItem inItem);

    /**
    * cancelOutItem
    */
    public void cancelOutItem(final QueueItem outItem);

    /**
    * relocateInItem
    */
    public void relocateInItem(final QueueItem inItem);

    /**
    * relocateOutItem
    */
    public void relocateOutItem(QueueItem outItem);

    /**
    * forceOutItemDone
    */
    public void forceOutItemDone(QueueItem outItem, byte responseType, Object responseData);

    /**
    * queueItemTransferRequestReceived
    */
    public void queueItemTransferRequestReceived(final QueueItemTransferRequest request);

    /**
    * multiQueueItemTransferRequestReceived
    */
    public void multiQueueItemTransferRequestReceived(final MultiQueueItemTransferRequest request);

    /**
    * queueItemTransferRequestAborted
    */
    public void queueItemTransferRequestAborted(final QueueItem item);

    /**
    * queueItemTransferred
    */
    public void queueItemTransferred(final String outItemId, final QueueItem item);

    /**
    * queueItemTransferFailure
    */
    public void queueItemTransferFailure(final String outItemId, final QueueItem item);

    /**
    * queueItemTransferFailureQueueFull
    */
    public void queueItemTransferFailureQueueFull(final String outItemId, final QueueItem item);

    /**
    * queueItemTransferResponseAborted
    */
    public void queueItemTransferResponseAborted(final String outItemId, final QueueItem item);

    /**
    */
    public void queueItemQueryReceived(QueueItemQuery query);

    /**
    * queueItemCompletedSuccess
    */
    public QueueItem queueItemCompletedSuccess(final QueueItemCompletionResponse response);

    /**
    * queueItemCompletedFailure
    */
    public QueueItem queueItemCompletedFailure(final QueueItemCompletionResponse response);

    /**
    * queueItemCompletedCancelled
    */
    public QueueItem queueItemCompletedCancelled(final QueueItemCompletionResponse response);

    /**
    * queueItemRelocationRequiredResponseReceived
    */
    public void queueItemRelocationRequiredResponseReceived(final QueueItemCompletionResponse response);

    /**
    * queueItemCompletionResponseAborted
    */
    public void queueItemCompletionResponseAborted(final QueueItemCompletionResponse command);

    /**
    * queueItemCancellationRequest
    */
    public void queueItemCancellationRequest(final QueueItemCancellationRequest request);

    /**
    * queueItemRelocationRequest
    */
    public void queueItemRelocationRequest(QueueItemRelocationRequest request);

    /**
    * outItemAgeWarning
    */
    public void outItemAgeWarning(final QueueItem outItem);

    /**
    * performOutItemCheck
    */
    public void performOutItemCheck();

    /**
    * performQueueSystemCheck
    */
    public boolean performQueueSystemCheck();
}
