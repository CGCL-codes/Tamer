package org.gamenet.application.mm8leveleditor.data.mm6.indoor;

import java.util.List;
import org.gamenet.application.mm8leveleditor.data.mm6.Vertex;
import org.gamenet.util.ByteConversions;

public class ShortVertex implements Vertex {

    private static final int VERTEX_RECORD_LENGTH = 6;

    private static final int EAST_WEST_OFFSET = 0;

    private static final int NORTH_SOUTH_OFFSET = 2;

    private static final int HEIGHT_OFFSET = 4;

    private short x = 0;

    private short y = 0;

    private short z = 0;

    private long offset = 0;

    public ShortVertex(short x, short y, short z, long offset) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.offset = offset;
    }

    public ShortVertex(int x, int y, int z) {
        super();
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public static int populateObjects(byte[] dataSrc, int offset, List vertexList) {
        int vertexCount = ByteConversions.getIntegerInByteArrayAtPosition(dataSrc, offset);
        offset += 4;
        for (int vertexIndex = 0; vertexIndex < vertexCount; ++vertexIndex) {
            int vertexOffset = offset;
            short x = ByteConversions.getShortInByteArrayAtPosition(dataSrc, offset);
            offset += 2;
            short y = ByteConversions.getShortInByteArrayAtPosition(dataSrc, offset);
            offset += 2;
            short z = ByteConversions.getShortInByteArrayAtPosition(dataSrc, offset);
            offset += 2;
            ShortVertex vertex = new ShortVertex(x, y, z, vertexOffset);
            vertexList.add(vertex);
        }
        return offset;
    }

    public static int updateData(byte[] newData, int offset, List vertexList) {
        ByteConversions.setIntegerInByteArrayAtPosition(vertexList.size(), newData, offset);
        offset += 4;
        for (int vertexIndex = 0; vertexIndex < vertexList.size(); ++vertexIndex) {
            ShortVertex vertex = (ShortVertex) vertexList.get(vertexIndex);
            ByteConversions.setShortInByteArrayAtPosition((short) vertex.getX(), newData, offset);
            offset += 2;
            ByteConversions.setShortInByteArrayAtPosition((short) vertex.getY(), newData, offset);
            offset += 2;
            ByteConversions.setShortInByteArrayAtPosition((short) vertex.getZ(), newData, offset);
            offset += 2;
        }
        return offset;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = (short) x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = (short) y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = (short) z;
    }

    public long getOffset() {
        return this.offset;
    }

    public static int getRecordSize() {
        return VERTEX_RECORD_LENGTH;
    }
}
