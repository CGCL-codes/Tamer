package com.itstherules.tags;

import com.itstherules.io.BufferHelper;
import com.itstherules.io.IOHelper;

public class VideoTag extends FlvTag {

    public VideoTag() {
        bh = new BufferHelper();
        pos = 0;
        bits = null;
        width = 0;
        height = 0;
        byteOffset = 0L;
        codecIdFrameType = 0;
        frameType = 0;
        codecId = 0;
    }

    public VideoTag(IOHelper ioh) {
        super(ioh);
        bh = ioh.getBufferHelper();
        pos = 0;
        codecIdFrameType = bh.readInt(super.getData(), pos, 1);
        pos++;
        frameType = codecIdFrameType >> 4;
        codecId = codecIdFrameType & 15;
        bits = padBitSequence(bh.readBinaryString(super.getData(), pos, 9));
        super.clearData();
        pos += 9;
        if (codecId == 2) {
            int hwCheck = bh.bit2uint(bits.substring(30, 33).toCharArray());
            width = findWidth(hwCheck);
            height = findHeight(hwCheck);
        } else if (codecId == 3) {
            width = bh.bit2uint(bits.substring(4, 16).toCharArray());
            height = bh.bit2uint(bits.substring(16, 28).toCharArray());
        }
    }

    private String padBitSequence(String bitSrc) {
        String bitSeq = bitSrc;
        int pad = 72 - bitSeq.length();
        if (pad > 0) {
            for (int i = 0; i < pad; i++) bitSeq = (new StringBuilder()).append("0").append(bitSeq).toString();
        }
        return bitSeq;
    }

    private int findWidth(int hwCheck) {
        int width = 0;
        switch(hwCheck) {
            case 0:
                width = bh.bit2uint(bits.substring(33, 41).toCharArray());
                break;
            case 1:
                width = bh.bit2uint(bits.substring(33, 49).toCharArray());
                break;
            case 2:
                width = 352;
                break;
            case 3:
                width = 176;
                break;
            case 4:
                width = 128;
                break;
            case 5:
                width = 320;
                break;
            case 6:
                width = 160;
                break;
        }
        return width;
    }

    private int findHeight(int hwCheck) {
        int height = 0;
        switch(hwCheck) {
            case 0:
                height = bh.bit2uint(bits.substring(41, 49).toCharArray());
                break;
            case 1:
                height = bh.bit2uint(bits.substring(49, 65).toCharArray());
                break;
            case 2:
                height = 288;
                break;
            case 3:
                height = 144;
                break;
            case 4:
                height = 96;
                break;
            case 5:
                height = 240;
                break;
            case 6:
                height = 120;
                break;
        }
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCodecIdFrameType() {
        return codecIdFrameType;
    }

    public int getFrameType() {
        return frameType;
    }

    public int getCodecId() {
        return codecId;
    }

    public long getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(long byteOffset) {
        this.byteOffset = byteOffset;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCodecIdFrameType(int codecIdFrameType) {
        this.codecIdFrameType = codecIdFrameType;
    }

    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }

    public void setCodecId(int codecId) {
        this.codecId = codecId;
    }

    public static final int H263VIDEOPACKET = 2;

    public static final int SCREENVIDEOPACKET = 3;

    public static final int ON2VP6 = 4;

    public static final int KEYFRAME = 1;

    public static final int INTERFRAME = 2;

    public static final int DISPOSABLEINTERFRAME = 3;

    private BufferHelper bh;

    private int pos;

    private String bits;

    private int width;

    private int height;

    private long byteOffset;

    private int codecIdFrameType;

    private int frameType;

    private int codecId;
}
