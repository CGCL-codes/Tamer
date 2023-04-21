package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import java.nio.ByteBuffer;

/**
 * Describes the format of media access units in PDCF files.
 */
public final class OmaDrmAccessUnitFormatBox extends AbstractFullBox {

    public static final String TYPE = "odaf";

    private boolean selectiveEncryption;

    private byte allBits;

    private int keyIndicatorLength;

    private int initVectorLength;

    protected long getContentSize() {
        return 7;
    }

    public OmaDrmAccessUnitFormatBox() {
        super("odaf");
    }

    public boolean isSelectiveEncryption() {
        return selectiveEncryption;
    }

    public int getKeyIndicatorLength() {
        return keyIndicatorLength;
    }

    public int getInitVectorLength() {
        return initVectorLength;
    }

    public void setInitVectorLength(int initVectorLength) {
        this.initVectorLength = initVectorLength;
    }

    public void setKeyIndicatorLength(int keyIndicatorLength) {
        this.keyIndicatorLength = keyIndicatorLength;
    }

    public void setAllBits(byte allBits) {
        this.allBits = allBits;
        selectiveEncryption = (allBits & 0x80) == 0x80;
    }

    @Override
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        allBits = (byte) IsoTypeReader.readUInt8(content);
        selectiveEncryption = (allBits & 0x80) == 0x80;
        keyIndicatorLength = IsoTypeReader.readUInt8(content);
        initVectorLength = IsoTypeReader.readUInt8(content);
    }

    @Override
    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt8(byteBuffer, allBits);
        IsoTypeWriter.writeUInt8(byteBuffer, keyIndicatorLength);
        IsoTypeWriter.writeUInt8(byteBuffer, initVectorLength);
    }
}
