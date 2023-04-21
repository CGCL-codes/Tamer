package fi.hiit.cutehip.packet;

import fi.hiit.framework.utils.Helpers;

public class HostIDParameter extends HipParameter {

    public static final short TYPE_HOST_ID = 0x2C1;

    public static final short HI_LENGTH_OFFSET = 0x4;

    public static final short DI_TYPE_OFFSET = 0x6;

    public static final short DI_LENGTH_OFFSET = 0x6;

    public static final short HI_OFFSET = 0x8;

    public static final short DI_OFFSET = 0x8;

    public static final short HI_COMMON_LENGTH = 0x4;

    public HostIDParameter(int size) {
        super((short) HI_OFFSET);
        __setType();
        __setLength((short) 0);
        setDIType((byte) 0);
        setDILength(0);
        setHostIDLength(0);
    }

    public void setHostIDLength(int length) {
        __data[HI_LENGTH_OFFSET] = (byte) ((length >> 8) & 0xFF);
        __data[HI_LENGTH_OFFSET + 1] = (byte) (length & 0xFF);
    }

    public short getHostIDLength() {
        return (short) (((__data[HI_LENGTH_OFFSET] & 0xFF) << 8) | (__data[HI_LENGTH_OFFSET + 1] & 0xFF));
    }

    public void setDIType(byte type) {
        __data[DI_TYPE_OFFSET] |= (byte) ((type & 0xF) << 0x4);
    }

    public byte getDITypeLength() {
        return (byte) ((__data[DI_TYPE_OFFSET] >> 0x4) & 0xF);
    }

    public void setDILength(int length) {
        __data[DI_LENGTH_OFFSET] |= (byte) ((length >> 8) & 0xF);
        __data[DI_LENGTH_OFFSET + 1] = (byte) (length & 0xFF);
    }

    public short getDILength() {
        return (short) (((__data[HI_LENGTH_OFFSET] & 0xF) << 8) | (__data[HI_LENGTH_OFFSET + 1] & 0xFF));
    }

    public void setHI(byte[] value) {
        if (value == null) return;
        int __diLen = getDILength();
        int __hiLen = getHostIDLength();
        int __paramLen = getLength();
        int __padding = 0;
        int __length = 0;
        byte[] __buf = null;
        if (__diLen == 0 && __hiLen == 0) {
            if (value.length % MULTIPLE != 0) __padding = (short) (MULTIPLE - (HI_COMMON_LENGTH + value.length + HipParameter.PARAM_COMMON_HDR_LEN) % MULTIPLE);
            __buf = new byte[HipParameter.PARAM_COMMON_HDR_LEN + HI_COMMON_LENGTH + value.length + __padding];
            __length = value.length + HI_COMMON_LENGTH;
            System.arraycopy(__data, 0, __buf, 0, __data.length);
            System.arraycopy(value, 0, __buf, HipParameter.PARAM_COMMON_HDR_LEN + HI_COMMON_LENGTH, value.length);
        } else if ((__diLen > 0 && __hiLen == 0) || (__diLen > 0 && __hiLen > 0)) {
            if ((value.length + __diLen) % MULTIPLE != 0) __padding = (value.length + __diLen) % HipParameter.MULTIPLE;
            __buf = new byte[PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + value.length + __diLen + __padding];
            __length = HI_COMMON_LENGTH + value.length + __diLen;
            System.arraycopy(__data, 0, __buf, 0, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH);
            System.arraycopy(value, 0, __buf, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH, value.length);
            System.arraycopy(__data, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + __hiLen, __buf, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + value.length, __diLen);
        } else if (__diLen == 0 && __hiLen > 0) {
            __padding = value.length % HipParameter.MULTIPLE;
            __buf = new byte[PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + value.length + __padding];
            __length = HI_COMMON_LENGTH + value.length;
            System.arraycopy(__data, 0, __buf, 0, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH);
            System.arraycopy(value, 0, __buf, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH, value.length);
        }
        setData(__buf);
        __setLength((short) __length);
        setHostIDLength(value.length);
    }

    public void getHI(byte[] hi) {
        if (hi == null) return;
        int __hiLen = getHostIDLength();
        if (__hiLen == 0 || hi.length < __hiLen) return;
        System.arraycopy(__data, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH, hi, 0, __hiLen);
    }

    public void setDI(byte[] value) {
        if (value == null) return;
        int __diLen = getDILength();
        int __hiLen = getHostIDLength();
        int __paramLen = getLength();
        int __padding = (value.length + __hiLen) % HipParameter.MULTIPLE;
        ;
        int __length = value.length + HI_COMMON_LENGTH + __hiLen;
        byte[] __buf = new byte[PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + __hiLen + value.length + __padding];
        System.arraycopy(__data, 0, __buf, 0, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + __hiLen);
        System.arraycopy(value, 0, __buf, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + __hiLen, value.length);
        setData(__buf);
        __setLength((short) __length);
        setHostIDLength(value.length);
    }

    public void getDI(byte[] di) {
        if (di == null) return;
        int __diLen = getDILength();
        int __hiLen = getHostIDLength();
        if (__diLen == 0 || di.length < __diLen) return;
        System.arraycopy(__data, PARAM_HEADER_OFFSET + HI_COMMON_LENGTH + __hiLen, di, 0, __diLen);
    }

    private void __setType() {
        __data[TYPE_OFFSET] = (byte) ((TYPE_HOST_ID >> 8) & 0xFF);
        __data[TYPE_OFFSET + 1] = (byte) (TYPE_HOST_ID & 0xFF);
    }

    private void __setLength(short length) {
        __data[LENGTH_OFFSET] = (byte) ((length >> 8) & 0xFF);
        __data[LENGTH_OFFSET + 1] = (byte) (length & 0xFF);
    }
}
