package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.googlecode.mp4parser.AbstractFullBox;
import java.nio.ByteBuffer;

/**
 * Only used within the DataReferenceBox. Find more information there.
 *
 * @see com.coremedia.iso.boxes.DataReferenceBox
 */
public class DataEntryUrnBox extends AbstractFullBox {

    private String name;

    private String location;

    public static final String TYPE = "urn ";

    public DataEntryUrnBox() {
        super(TYPE);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    protected long getContentSize() {
        return Utf8.utf8StringLengthInBytes(name) + 1 + Utf8.utf8StringLengthInBytes(location) + 1;
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        name = IsoTypeReader.readString(content);
        location = IsoTypeReader.readString(content);
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(Utf8.convert(name));
        byteBuffer.put((byte) 0);
        byteBuffer.put(Utf8.convert(location));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        return "DataEntryUrlBox[name=" + getName() + ";location=" + getLocation() + "]";
    }
}
