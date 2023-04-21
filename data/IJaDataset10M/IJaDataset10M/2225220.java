package gridrm.util;

import gridrm.core.GridException;

public class QueueLimitException extends GridException {

    public QueueLimitException(String msg) {
        super(msg);
    }
}
