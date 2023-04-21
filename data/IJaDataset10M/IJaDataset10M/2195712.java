package com.android.email.mail;

import com.android.email.MessagingListener;
import com.android.email.provider.EmailContent;
import com.android.email.GroupMessagingListener;
import android.content.Context;
import java.util.Collection;

/**
 * This interface allows a store to define a completely different synchronizer algorithm,
 * as necessary.
 */
public interface StoreSynchronizer {

    /**
     * An object of this class is returned by SynchronizeMessagesSynchronous to report
     * the results of the sync run.
     */
    public static class SyncResults {

        /**
         * The total # of messages in the folder
         */
        public int mTotalMessages;

        /**
         * The # of new messages in the folder
         */
        public int mNewMessages;

        public SyncResults(int totalMessages, int newMessages) {
            mTotalMessages = totalMessages;
            mNewMessages = newMessages;
        }
    }

    /**
     * The job of this method is to synchronize messages between a remote folder and the
     * corresponding local folder.
     * 
     * The following callbacks should be called during this operation:
     *  {@link MessagingListener#synchronizeMailboxNewMessage(Account, String, Message)}
     *  {@link MessagingListener#synchronizeMailboxRemovedMessage(Account, String, Message)}
     *  
     * Callbacks (through listeners) *must* be synchronized on the listeners object, e.g.
     *   synchronized (listeners) {
     *       for(MessagingListener listener : listeners) {
     *           listener.synchronizeMailboxNewMessage(account, folder, message);
     *       }
     *   }
     *
     * @param account The account to synchronize
     * @param folder The folder to synchronize
     * @param listeners callbacks to make during sync operation
     * @param context if needed for making system calls
     * @return an object describing the sync results
     */
    public SyncResults SynchronizeMessagesSynchronous(EmailContent.Account account, EmailContent.Mailbox folder, GroupMessagingListener listeners, Context context) throws MessagingException;
}
