package org.curjent.test.agent.asmify;

import static org.curjent.agent.CallState.EXECUTING;
import org.curjent.impl.agent.CallInfo;
import org.curjent.impl.agent.Message;

final class CopyMessage_Count extends Message {

    int result;

    CopyMessage_Count(CallInfo info) {
        super(info);
    }

    @Override
    protected void dispatch(Object queuedTask) throws Throwable {
        CopyTask task = (CopyTask) queuedTask;
        int result = task.count();
        synchronized (this) {
            if (state != EXECUTING) return;
            this.result = result;
            cachedResult = this;
        }
    }

    @Override
    protected Object getResultValue() {
        return Integer.valueOf(result);
    }

    @Override
    protected void setResultValue(Object value) {
        if (value == null) {
            result = 0;
        } else {
            result = ((Integer) value).intValue();
        }
    }
}
