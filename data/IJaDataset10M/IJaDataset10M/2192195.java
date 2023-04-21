package org.arch.event.http;

import java.io.IOException;
import org.arch.buffer.Buffer;
import org.arch.buffer.BufferHelper;
import org.arch.event.Event;
import org.arch.event.EventType;
import org.arch.event.EventVersion;

/**
 * @author qiyingwang
 *
 */
@EventType(HTTPEventContants.HTTP_ERROR_EVENT_TYPE)
@EventVersion(1)
public class HTTPErrorEvent extends Event {

    public int errno;

    public String error;

    @Override
    protected boolean onDecode(Buffer buffer) {
        try {
            errno = BufferHelper.readVarInt(buffer);
            error = BufferHelper.readVarString(buffer);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean onEncode(Buffer buffer) {
        BufferHelper.writeVarInt(buffer, errno);
        if (null == error) {
            BufferHelper.writeVarInt(buffer, 0);
        } else {
            BufferHelper.writeVarString(buffer, error);
        }
        return true;
    }
}
