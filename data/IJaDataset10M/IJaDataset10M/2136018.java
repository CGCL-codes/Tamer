package org.lwjgl.d3d;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class D3DVertexBufferDesc {

    public int Format;

    public int Type;

    public long Usage;

    public int Pool;

    public int Size;

    public long FVF;

    private static final int D3D_VERTEX_BUFFER_DESC_BYTE_SIZE = 24;

    private ByteBuffer buffer;

    public D3DVertexBufferDesc() {
        buffer = ByteBuffer.allocateDirect(D3D_VERTEX_BUFFER_DESC_BYTE_SIZE);
        buffer.order(ByteOrder.nativeOrder());
    }

    public ByteBuffer getEmptyBuffer() {
        buffer.rewind();
        return buffer;
    }

    public ByteBuffer getBuffer() {
        buffer.rewind();
        buffer.putInt(Format);
        buffer.putInt(Type);
        buffer.putInt((int) Usage);
        buffer.putInt(Pool);
        buffer.putInt(Size);
        buffer.putInt((int) FVF);
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        buffer.rewind();
        Format = buffer.getInt();
        Type = buffer.getInt();
        Usage = buffer.getInt();
        Pool = buffer.getInt();
        Size = buffer.getInt();
        FVF = buffer.getInt();
    }
}
