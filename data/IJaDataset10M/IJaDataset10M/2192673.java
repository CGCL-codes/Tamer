package org.curjent.test.agent.asmify;

import static org.curjent.agent.CallState.EXECUTING;
import java.util.concurrent.Future;
import org.curjent.impl.agent.CallInfo;
import org.curjent.impl.agent.Message;

final class CopyMessage_File extends Message {

    String arg0;

    Object result;

    CopyMessage_File(CallInfo info) {
        super(info);
    }

    @Override
    protected void dispatch(Object queuedTask) throws Throwable {
        CopyTask task = (CopyTask) queuedTask;
        String arg0;
        synchronized (this) {
            if (state != EXECUTING) return;
            arg0 = this.arg0;
        }
        Object result = task.file(arg0);
        if (result != null) result = ((Future<?>) result).get();
        synchronized (this) {
            if (state != EXECUTING) return;
            this.result = result;
            cachedResult = result;
        }
    }

    @Override
    protected Object getArgumentValue(int index) {
        return arg0;
    }

    @Override
    protected void setArgumentValue(int index, Object value) {
        arg0 = (String) value;
    }

    @Override
    protected void setResultValue(Object value) {
        result = value;
    }
}
