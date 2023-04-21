package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import java.nio.ByteBuffer;

/**
 * aligned(8) class MovieExtendsHeaderBox extends FullBox('mehd', version, 0) {
 * if (version==1) {
 * unsigned int(64) fragment_duration;
 * } else { // version==0
 * unsigned int(32) fragment_duration;
 * }
 * }
 */
public class MovieExtendsHeaderBox extends AbstractFullBox {

    public static final String TYPE = "mehd";

    private long fragmentDuration;

    public MovieExtendsHeaderBox() {
        super(TYPE);
    }

    @Override
    protected long getContentSize() {
        return getVersion() == 1 ? 12 : 8;
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        fragmentDuration = getVersion() == 1 ? IsoTypeReader.readUInt64(content) : IsoTypeReader.readUInt32(content);
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, fragmentDuration);
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, fragmentDuration);
        }
    }

    public long getFragmentDuration() {
        return fragmentDuration;
    }
}
