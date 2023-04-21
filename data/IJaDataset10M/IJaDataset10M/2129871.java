package org.shestkov.timeseriestorage.serializator;

import org.shestkoff.nimium.common.struct.Bar;
import org.shestkoff.nimium.common.struct.Candle;

/**
 * Project: Maxifier
 * User: Vasily
 * Date: 01.12.2009 18:07:57
 * <p/>
 * Copyright (c) 1999-2006 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author Shestkov Vasily
 */
public class CandleWithTimeSerializator implements TimeSerieItemSerializator<Bar<Candle>> {

    private static final int BAR_CANDLE_OBJECT_WITH_TIME_SIZE = 32;

    public byte[] serialize(Bar<Candle> value) {
        final int open = Float.floatToRawIntBits(value.candle.open);
        final int high = Float.floatToRawIntBits(value.candle.high);
        final int low = Float.floatToRawIntBits(value.candle.low);
        final int close = Float.floatToRawIntBits(value.candle.close);
        return new byte[] { (byte) (value.timeStamp >>> 56), (byte) (value.timeStamp >>> 48), (byte) (value.timeStamp >>> 40), (byte) (value.timeStamp >>> 32), (byte) (value.timeStamp >>> 24), (byte) (value.timeStamp >>> 16), (byte) (value.timeStamp >>> 8), (byte) (value.timeStamp), (byte) (open >>> 24), (byte) (open >>> 16), (byte) (open >>> 8), (byte) (open), (byte) (high >>> 24), (byte) (high >>> 16), (byte) (high >>> 8), (byte) (high), (byte) (low >>> 24), (byte) (low >>> 16), (byte) (low >>> 8), (byte) (low), (byte) (close >>> 24), (byte) (close >>> 16), (byte) (close >>> 8), (byte) (close), (byte) (value.volume >>> 56), (byte) (value.volume >>> 48), (byte) (value.volume >>> 40), (byte) (value.volume >>> 32), (byte) (value.volume >>> 24), (byte) (value.volume >>> 16), (byte) (value.volume >>> 8), (byte) (value.volume) };
    }

    public Bar<Candle> deserialize(byte[] bytes, int offset, long time) {
        if (offset < 0 || bytes.length - offset < BAR_CANDLE_OBJECT_WITH_TIME_SIZE) {
            throw new IllegalArgumentException();
        }
        time = ((long) bytes[offset] << 56) + ((long) (bytes[1 + offset] & 0xFF) << 48) + ((long) (bytes[2 + offset] & 0xFF) << 40) + ((long) (bytes[3 + offset] & 0xFF) << 32) + ((long) (bytes[4 + offset] & 0xFF) << 24) + ((long) (bytes[5 + offset] & 0xFF) << 16) + ((long) (bytes[6 + offset] & 0xFF) << 8) + ((long) bytes[7 + offset] & 0xFF);
        final Candle candle = new Candle(Float.intBitsToFloat((bytes[8 + offset] << 24) + ((bytes[9 + offset] & 0xFF) << 16) + ((bytes[10 + offset] & 0xFF) << 8) + (bytes[11 + offset] & 0xFF)), Float.intBitsToFloat((bytes[12 + offset] << 24) + ((bytes[13 + offset] & 0xFF) << 16) + ((bytes[14 + offset] & 0xFF) << 8) + (bytes[15 + offset] & 0xFF)), Float.intBitsToFloat((bytes[16 + offset] << 24) + ((bytes[17 + offset] & 0xFF) << 16) + ((bytes[18 + offset] & 0xFF) << 8) + (bytes[19 + offset] & 0xFF)), Float.intBitsToFloat((bytes[20 + offset] << 24) + ((bytes[21 + offset] & 0xFF) << 16) + ((bytes[22 + offset] & 0xFF) << 8) + (bytes[23 + offset] & 0xFF)));
        return new Bar<Candle>(time, candle, ((long) bytes[24 + offset] << 56) + ((long) (bytes[25 + offset] & 0xFF) << 48) + ((long) (bytes[26 + offset] & 0xFF) << 40) + ((long) (bytes[27 + offset] & 0xFF) << 32) + ((long) (bytes[28 + offset] & 0xFF) << 24) + ((long) (bytes[29 + offset] & 0xFF) << 16) + ((long) (bytes[30 + offset] & 0xFF) << 8) + ((long) bytes[31 + offset] & 0xFF));
    }

    public boolean isEmpty(byte[] bytes, int offset) {
        final long objectSize = getSerializedBytesSize();
        for (int i = 0; i < objectSize; i++) {
            if (bytes[offset + i] != 0) {
                return false;
            }
        }
        return true;
    }

    public long getSerializedBytesSize() {
        return BAR_CANDLE_OBJECT_WITH_TIME_SIZE;
    }
}
