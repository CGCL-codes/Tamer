package edu.ucsd.ncmir.tiff;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author spl
 */
class ConverterShort extends Converter {

    public ConverterShort(int width, int height, int channels) {
        super(width, height, channels);
    }

    @Override
    Number[][] convert(byte[] byte_data, boolean little_endian) {
        ByteBuffer bb = super.getBuffer(byte_data, little_endian);
        ShortBuffer sb = bb.asShortBuffer();
        short[] data = new short[byte_data.length / (Short.SIZE / Byte.SIZE)];
        sb.get(data);
        Short[][] buffer = new Short[super._height][super._width];
        switch(super._channels) {
            case 1:
                {
                    for (int j = 0, o = 0; j < super._height; j++) for (int i = 0; i < super._width; i++, o++) buffer[j][i] = new Short(data[o]);
                    break;
                }
            case 3:
                {
                    for (int j = 0, o = 0; j < super._height; j++) for (int i = 0; i < super._width; i++, o += 3) buffer[j][i] = new Short((short) super.rgbToGrey(data[o + 0], data[o + 1], data[o + 2]));
                    break;
                }
            case 4:
                {
                    for (int j = 0, o = 0; j < super._height; j++) for (int i = 0; i < super._width; i++, o += 4) buffer[j][i] = new Short((short) super.rgbToGrey(data[o + 0], data[o + 1], data[o + 2]));
                    break;
                }
        }
        return buffer;
    }
}
