package jp.ne.nifty.iga.midori.file.xml;

import java.io.IOException;
import java.io.OutputStream;

public final class JMdBase64OutputStream extends OutputStream {

    private OutputStream outStream = null;

    private JMdBase64Util mdshellbase64util = new JMdBase64Util();

    private int iBufferCount = 0;

    private int iBufferLength = 0;

    private int[] bufferBase64 = new int[3];

    public JMdBase64OutputStream(OutputStream outStream) {
        this.outStream = outStream;
    }

    public final void write(int bArg) throws IOException {
        bufferBase64[iBufferCount++] = bArg;
        iBufferLength++;
        if (iBufferCount == 3 || iBufferLength > 56) internalDo();
    }

    public final void flush() throws IOException {
        internalDo();
        outStream.flush();
    }

    public final void close() throws IOException {
        internalDo();
        outStream.close();
    }

    private final void internalDo() throws IOException {
        if (iBufferCount == 0) return;
        byte[] byteOutWork = mdshellbase64util.encodeBuf(bufferBase64, iBufferCount);
        outStream.write(byteOutWork[0]);
        outStream.write(byteOutWork[1]);
        outStream.write(byteOutWork[2]);
        outStream.write(byteOutWork[3]);
        if (iBufferLength > 56) {
            iBufferLength = 0;
            outStream.write(0x0a);
        }
        iBufferCount = 0;
    }
}
