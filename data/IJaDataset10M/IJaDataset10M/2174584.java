package myqueue.Core.StorageEngines;

import myqueue.Core.MessageManagers.HDMessageManager;

/**
 *
 * @author Nikos Siatras
 */
public class HDEngine extends StorageEngine {

    private HDMessageManager fNormalPriorityMessageManager;

    private HDMessageManager fAboveNormalPriorityMessageManager;

    private HDMessageManager fHighPriorityMessageManager;

    private final Object fSyncObject = new Object();

    private HDEngine fJournalEngine;

    public HDEngine(String location) {
        super(location);
        fNormalPriorityMessageManager = new HDMessageManager(location, "PN");
        fAboveNormalPriorityMessageManager = new HDMessageManager(location + "/AboveNormalPriority", "PA");
        fHighPriorityMessageManager = new HDMessageManager(location + "/HighPriority", "PH");
    }

    @Override
    public byte[] Dequeue() {
        synchronized (fSyncObject) {
            return fHighPriorityMessageManager.fMessageCount > 0 ? fHighPriorityMessageManager.Dequeue() : fAboveNormalPriorityMessageManager.fMessageCount > 0 ? fAboveNormalPriorityMessageManager.Dequeue() : fNormalPriorityMessageManager.Dequeue();
        }
    }

    @Override
    public byte[] Peek() {
        synchronized (fSyncObject) {
            return fHighPriorityMessageManager.fMessageCount > 0 ? fHighPriorityMessageManager.Peek() : fAboveNormalPriorityMessageManager.fMessageCount > 0 ? fAboveNormalPriorityMessageManager.Peek() : fNormalPriorityMessageManager.Peek();
        }
    }

    @Override
    public byte[] GetMessageByID(String messageID) {
        String priorityIdicator = messageID.substring(0, 2);
        byte[] bytesToReturn = null;
        bytesToReturn = priorityIdicator.equals("PH") ? fHighPriorityMessageManager.GetMessageByID(messageID) : priorityIdicator.equals("PA") ? fAboveNormalPriorityMessageManager.GetMessageByID(messageID) : fNormalPriorityMessageManager.GetMessageByID(messageID);
        return bytesToReturn == null && isJournalRecording() ? fJournalEngine.GetMessageByID(messageID) : bytesToReturn;
    }

    @Override
    public String Enqueue(byte[] bytes) {
        synchronized (fSyncObject) {
            if (isJournalRecording()) {
                fJournalEngine.Enqueue(bytes);
            }
            int messagePriority = Integer.parseInt(new String(bytes, 0, 1));
            switch(messagePriority) {
                case 0:
                    return fNormalPriorityMessageManager.Enqueue(bytes);
                case 1:
                    return fAboveNormalPriorityMessageManager.Enqueue(bytes);
                case 2:
                    return fHighPriorityMessageManager.Enqueue(bytes);
            }
            return null;
        }
    }

    @Override
    public void StartEngine() {
        fNormalPriorityMessageManager.Start();
        fAboveNormalPriorityMessageManager.Start();
        fHighPriorityMessageManager.Start();
        if (isJournalRecording()) {
            fJournalEngine = new HDEngine(getLocation() + "/Journal");
            fJournalEngine.StartEngine();
        }
    }

    @Override
    public String GetMessagesPack() {
        synchronized (fSyncObject) {
            return fNormalPriorityMessageManager.GetMessagesPack() + ":" + fAboveNormalPriorityMessageManager.GetMessagesPack() + ":" + fHighPriorityMessageManager.GetMessagesPack();
        }
    }

    @Override
    public void Clear() {
        synchronized (fSyncObject) {
            fNormalPriorityMessageManager.ClearMessages();
            fAboveNormalPriorityMessageManager.ClearMessages();
            fHighPriorityMessageManager.ClearMessages();
            if (fJournalEngine != null) {
                fJournalEngine.Clear();
            }
        }
    }

    @Override
    public long getMessageCount() {
        return fNormalPriorityMessageManager.getMessageCount() + fAboveNormalPriorityMessageManager.getMessageCount() + fHighPriorityMessageManager.getMessageCount();
    }

    @Override
    public long getNormalPriorityMessageCount() {
        return fNormalPriorityMessageManager.getMessageCount();
    }

    @Override
    public long getAboveNormalPriorityMessageCount() {
        return fAboveNormalPriorityMessageManager.getMessageCount();
    }

    @Override
    public long getHighPriorityMessageCount() {
        return fHighPriorityMessageManager.getMessageCount();
    }
}
