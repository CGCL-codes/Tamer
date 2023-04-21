package org.mobicents.slee.resource.sip11;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FireableEventType;

/**
 * @author martins
 * 
 */
public class SipMarshaler implements javax.slee.resource.Marshaler {

    public int getEstimatedEventSize(FireableEventType arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    public int getEstimatedHandleSize(ActivityHandle arg0) {
        return ((MarshableSipActivityHandle) arg0).getEstimatedHandleSize();
    }

    public ByteBuffer getEventBuffer(FireableEventType arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    public void marshalEvent(FireableEventType arg0, Object arg1, DataOutput arg2) throws IOException {
        throw new UnsupportedOperationException();
    }

    private static final Class<?> DialogWithIdActivityHandle_TYPE = DialogWithIdActivityHandle.class;

    private static final Class<?> DialogWithoutIdActivityHandle_TYPE = DialogWithoutIdActivityHandle.class;

    private static final byte dialogWithIdActivityHandle = 0;

    private static final byte dialogWithoutIdActivityHandle = 1;

    public void marshalHandle(ActivityHandle arg0, DataOutput arg1) throws IOException {
        final Class<?> handleType = arg0.getClass();
        if (handleType == DialogWithIdActivityHandle_TYPE) {
            arg1.writeByte(dialogWithIdActivityHandle);
            final DialogWithIdActivityHandle handle = (DialogWithIdActivityHandle) arg0;
            arg1.writeUTF(handle.getDialogId());
        } else if (handleType == DialogWithoutIdActivityHandle_TYPE) {
            arg1.writeByte(dialogWithoutIdActivityHandle);
            final DialogWithoutIdActivityHandle handle = (DialogWithoutIdActivityHandle) arg0;
            arg1.writeUTF(handle.getCallId());
            arg1.writeUTF(handle.getLocalTag());
        } else {
            throw new IOException("unknown activity handle type");
        }
    }

    public void releaseEventBuffer(FireableEventType arg0, Object arg1, ByteBuffer arg2) {
        throw new UnsupportedOperationException();
    }

    public Object unmarshalEvent(FireableEventType arg0, DataInput arg1) throws IOException {
        throw new UnsupportedOperationException();
    }

    public ActivityHandle unmarshalHandle(DataInput arg0) throws IOException {
        final byte handleType = arg0.readByte();
        if (handleType == dialogWithIdActivityHandle) {
            final String dialogId = arg0.readUTF();
            return new DialogWithIdActivityHandle(dialogId);
        } else {
            final String callId = arg0.readUTF();
            final String localTag = arg0.readUTF();
            return new DialogWithoutIdActivityHandle(callId, localTag);
        }
    }
}
