package edu.ucsd.ncmir.tiff;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author spl
 */
class ConverterInt extends Converter {

    public ConverterInt(int width, int height, int channels) {
        super(width, height, channels);
    }

    @Override
    Number[][] convert(byte[] byte_data, boolean little_endian) {
        ByteBuffer bb = super.getBuffer(byte_data, little_endian);
        IntBuffer ib = bb.asIntBuffer();
        int[] data = new int[byte_data.length / (Integer.SIZE / Byte.SIZE)];
        ib.get(data);
        Integer[][] buffer = new Integer[super._height][super._width];
        switch(super._channels) {
            case 1:
                {
                    for (int j = 0, o = 0; j < super._height; j++) for (int i = 0; i < super._width; i++, o++) buffer[j][i] = new Integer(data[o]);
                    break;
                }
            case 3:
                {
                    for (int j = 0, o = 0; j < super._height; j++) for (int i = 0; i < super._width; i++, o += 3) buffer[j][i] = new Integer((int) super.rgbToGrey(data[o + 0], data[o + 1], data[o + 2]));
                    break;
                }
            case 4:
                {
                    for (int j = 0, o = 0; j < super._height; j++) for (int i = 0; i < super._width; i++, o += 4) buffer[j][i] = new Integer((int) super.rgbToGrey(data[o + 0], data[o + 1], data[o + 2]));
                    break;
                }
        }
        return buffer;
    }
}
