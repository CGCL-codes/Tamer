package org.sac.browse.datastore;

import org.sac.crosspather.common.util.Constants.REQUEST_RESPONSE_KEYS;
import org.sac.browse.queue.QueueHelper;
import org.sac.browse.queue.QueueItem;
import org.sac.browse.util.Logger;

public class ProgressListener {

    private static Logger logger = new Logger("StatusListener");

    private static final long PUBLISH_SIZE = 1000000;

    long currentBuffer;

    long currentSize;

    String key;

    public ProgressListener(String key) {
        this.key = key;
        currentBuffer = 0;
        currentSize = 0;
    }

    public void updateStatus(long completed) {
        currentSize += completed;
        currentBuffer += completed;
        if (currentBuffer > PUBLISH_SIZE) {
            currentBuffer = 0;
            if (key != null) {
                QueueItem item = new QueueItem(key, REQUEST_RESPONSE_KEYS.FILE_SERVER_UPLOAD_STATUS + REQUEST_RESPONSE_KEYS.VALUE_SEPERATOR + currentSize);
                logger.debugIt(REQUEST_RESPONSE_KEYS.FILE_SERVER_UPLOAD_STATUS + REQUEST_RESPONSE_KEYS.VALUE_SEPERATOR + currentSize);
                QueueHelper.getInstance().addToServerQ(item);
            }
        }
    }

    public void resetCurrentSize(long newCurrentSize) {
        currentSize = newCurrentSize;
    }

    public long getCurrent() {
        return currentSize;
    }
}
