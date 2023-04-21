package at.pollaknet.api.facile.header.cli.stream;

import java.util.ArrayList;
import at.pollaknet.api.facile.exception.UnexpectedHeaderDataException;
import at.pollaknet.api.facile.header.IDataHeader;
import at.pollaknet.api.facile.util.ArrayUtils;
import at.pollaknet.api.facile.util.ByteReader;

public class GuidStream implements IDataHeader {

    private int byteSize;

    private ArrayList<byte[]> guidHeap;

    public static final int GUID_BYTE_SIZE = 16;

    public GuidStream(int size) {
        byteSize = size;
        guidHeap = new ArrayList<byte[]>();
    }

    @Override
    public int getSize() {
        return byteSize;
    }

    @Override
    public int read(byte[] data, int offset) throws UnexpectedHeaderDataException {
        int index = offset;
        while (index < offset + byteSize) {
            guidHeap.add(ByteReader.getBytes(data, index, GUID_BYTE_SIZE));
            index += GUID_BYTE_SIZE;
        }
        byteSize = index - offset;
        return byteSize;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("#GUID Stream (Globally Unique Identifier Heap):");
        int index = 0;
        for (byte[] guid : guidHeap) {
            buffer.append("\n ");
            buffer.append(index);
            buffer.append(":\t");
            buffer.append(ArrayUtils.formatByteArray(guid));
            index++;
        }
        return buffer.toString();
    }

    public byte[] getGuid(int index) {
        index--;
        if (index < 0 || index >= guidHeap.size()) return null;
        return guidHeap.get(index);
    }

    public String getName() {
        return "#GUID";
    }
}
