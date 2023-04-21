package xdev.mxdev;

import java.util.UUID;
import java.nio.channels.*;
import java.nio.*;
import mpjdev.*;
import mpjbuf.NIOBuffer;
import xdev.*;

public class MXRecvRequest extends MXRequest {

    MXDevice device;

    int[] ctrlMsg = new int[2];

    MXRecvRequest(MXDevice device) {
        this.device = device;
    }

    protected long matchRecvHandle, matchRecvMaskHandle, sBufLengthHandle, dBufLengthHandle, bufferAddressHandle, ctrlMsgHandle;

    public Status iwait() {
        nativeIwait(status);
        status.srcID = device.pids[status.source].uuid();
        MXDevice.requestMap.remove(new Long(this.handle));
        try {
            bufferHandle.commit();
            status.type = bufferHandle.getSectionHeader();
            status.numEls = bufferHandle.getSectionSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        complete(status);
        testCalled = 1;
        return status;
    }

    public Status itest() {
        if (testCalled == 1) {
            return status;
        }
        int isCompleted = nativeItest(status);
        if (isCompleted == 1) {
            status.srcID = device.pids[status.source].uuid();
            testCalled = 1;
        }
        return ((isCompleted == 1) ? status : null);
    }

    native void nativeIwait(Status status);

    native int nativeItest(Status status);

    private static native void nativeRequestInit();

    static {
        nativeRequestInit();
    }
}
