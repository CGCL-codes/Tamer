package fvm.impl;

import java.io.*;

public class InputStreamConsumer extends InputStream {

    RefInvoker refInvoker;

    byte[] buffer = new byte[0];

    int bufferPos = 0;

    int bufferBytesLeft = 0;

    public InputStreamConsumer(RefInvoker refInvoker) {
        this.refInvoker = refInvoker;
    }

    public int read(byte[] buff, int offset, int len) throws IOException {
        if (buffer == null) {
            return -1;
        }
        int startOffset = offset;
        int bytesLeft = len;
        while (bytesLeft > 0) {
            if (bufferBytesLeft <= 0) {
                readMore();
                if (buffer == null) {
                    break;
                }
            }
            int bytesToRead = 0;
            if (bytesLeft > bufferBytesLeft) {
                bytesToRead = bufferBytesLeft;
            } else {
                bytesToRead = bytesLeft;
            }
            System.arraycopy(buffer, bufferPos, buff, offset, bytesToRead);
            bufferPos += bytesToRead;
            offset += bytesToRead;
            bytesLeft -= bytesToRead;
            bufferBytesLeft -= bytesToRead;
        }
        int bytesRead = offset - startOffset;
        if (buffer == null && bytesRead == 0) {
            return -1;
        }
        return bytesRead;
    }

    public int read() throws IOException {
        if (buffer == null) {
            return -1;
        }
        if (bufferPos >= buffer.length) {
            readMore();
            if (buffer == null) {
                return -1;
            }
        }
        byte b = buffer[bufferPos++];
        if (b >= 0) {
            return (int) b;
        }
        return 256 + b;
    }

    private void readMore() {
        MethodCall call = new MethodCall();
        call.setMethodName("read");
        call.setReturnType(byte[].class.getName());
        buffer = (byte[]) refInvoker.callRemoteMethod(call);
        bufferPos = 0;
        if (buffer == null) {
            call = new MethodCall();
            call.setMethodName("close");
            refInvoker.callRemoteMethod(call);
        } else {
            bufferBytesLeft = buffer.length;
        }
    }
}
