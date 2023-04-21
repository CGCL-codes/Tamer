package org.curjent.test.agent.asmify;

import static org.curjent.agent.CallState.EXECUTING;
import org.curjent.impl.agent.CallInfo;
import org.curjent.impl.agent.Message;

final class CopyMessage_Copy extends Message {

    String arg0;

    String arg1;

    CopyMessage_Copy(CallInfo info) {
        super(info);
    }

    @Override
    protected void dispatch(Object queuedTask) throws Throwable {
        CopyTask task = (CopyTask) queuedTask;
        String arg0;
        String arg1;
        synchronized (this) {
            if (state != EXECUTING) return;
            arg0 = this.arg0;
            arg1 = this.arg1;
        }
        task.copy(arg0, arg1);
    }

    @Override
    protected Object getArgumentValue(int index) {
        Object value;
        if (index == 0) {
            value = Integer.valueOf(arg0);
        } else if (index == 1) {
            value = Integer.valueOf(arg1);
        } else {
            value = Integer.valueOf(arg1);
        }
        return value;
    }

    @Override
    protected void setArgumentValue(int index, Object value) {
        if (index == 0) {
            arg0 = (String) value;
        } else {
            arg1 = (String) value;
        }
    }
}
