package xmage.raster.codec;

import xmage.raster.Image;
import xmage.raster.Palette;
import xmage.raster.RGBImage;
import huf.io.ByteInputStream;
import huf.io.StreamCorruptedException;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Input codec for PCX images.
 */
public class PCXInputCodec implements ImageInputCodec {

    /** Create new PCX codec. */
    public PCXInputCodec() {
    }

    /**
	 * Read image from file and return it in default format.
	 *
	 * @param image file name to read image from
	 * @return image in default format
	 * @throws IOException when file is unreadable
	 */
    public Image read(String image) throws IOException {
        return read(new File(image));
    }

    /**
	 * Read image from file and return it in default format.
	 *
	 * @param image file to read image from
	 * @return image in default format
	 * @throws IOException when file is unreadable
	 */
    public Image read(File image) throws IOException {
        return read(new FileInputStream(image));
    }

    private static final int MANUFACTURER = 10;

    private static final int ENCODING_RLE = 1;

    private int pcxManufacturer = -1;

    private int pcxVersion = -1;

    private int pcxEncoding = -1;

    private int pcxBitsPerPixel = -1;

    private int pcxWindowXMin = -1;

    private int pcxWindowYMin = -1;

    private int pcxWindowXMax = -1;

    private int pcxWindowYMax = -1;

    private int pcxNPlanes = -1;

    private int pcxWidth = -1;

    private int pcxHeight = -1;

    /**
	 * Read image from stream and return it in requested format.
	 *
	 * <p>
	 * <b>NOTE</b>: requested format may not be {@link xmage.raster.Image#FORMAT_PALETTED}.
	 * </p>
	 *
	 * @param image stream to read image from
	 * @return image in requested format
	 */
    public Image read(InputStream image) throws StreamCorruptedException {
        ByteInputStream data = new ByteInputStream(new BufferedInputStream(image));
        try {
            pcxManufacturer = data.readByte();
            pcxVersion = data.readByte();
            pcxEncoding = data.readByte();
            pcxBitsPerPixel = data.readByte();
            pcxWindowXMin = data.readShorti();
            pcxWindowYMin = data.readShorti();
            pcxWindowXMax = data.readShorti();
            pcxWindowYMax = data.readShorti();
            data.readShorti();
            data.readShorti();
            if (data.skip(48) != 48) {
                throw new StreamCorruptedException("Truncated image data");
            }
            if (data.skip(1) != 1) {
                throw new StreamCorruptedException("Truncated image data");
            }
            pcxNPlanes = data.readByte();
            data.readShorti();
            data.readShorti();
            data.readShorti();
            data.readShorti();
            if (data.skip(54) != 54) {
                throw new StreamCorruptedException("Truncated image data");
            }
        } catch (EOFException eofe) {
            throw new StreamCorruptedException("Image truncated in header");
        } catch (IOException ioe) {
            throw new StreamCorruptedException("Image truncated in header");
        }
        pcxWidth = pcxWindowXMax - pcxWindowXMin + 1;
        pcxHeight = pcxWindowYMax - pcxWindowYMin + 1;
        if (pcxManufacturer != MANUFACTURER) {
            throw new StreamCorruptedException("Unknown manufacturer ID: " + pcxManufacturer);
        }
        if (pcxVersion != 5) {
            throw new StreamCorruptedException("Unsupported image version: " + pcxVersion);
        }
        if (pcxEncoding != ENCODING_RLE) {
            throw new StreamCorruptedException("Unsupported encoding type: " + pcxEncoding);
        }
        if (pcxBitsPerPixel != 8) {
            throw new StreamCorruptedException("Unsupported bit resolution: " + pcxBitsPerPixel);
        }
        int width = pcxWidth;
        int height = pcxHeight;
        int planes = pcxNPlanes;
        switch(pcxNPlanes) {
            case 1:
                {
                    RGBImage img = new RGBImage(pcxWidth, pcxHeight);
                    ByteBuffer buffer = img.getBuffer();
                    try {
                        byte b = 0;
                        byte fill = 0;
                        int counter = 0;
                        int yindex = 0;
                        for (int y = 0; y < height; y++) {
                            yindex = y * pcxWidth * 3;
                            counter = 0;
                            do {
                                b = (byte) (data.readByte() & 0xff);
                                if ((b & 0xc0) == 0xc0) {
                                    b = (byte) (b & 0x3f);
                                    fill = (byte) (data.readByte() & 0xff);
                                    for (int i = 0; i < b; i++) {
                                        buffer.put(yindex + (counter++ * 3), fill);
                                    }
                                } else {
                                    buffer.put(yindex + (counter++ * 3), b);
                                }
                            } while (counter < width);
                        }
                    } catch (IOException ioe) {
                        throw new StreamCorruptedException("Image truncated in pixel data");
                    }
                    Palette palette = readPalette(data);
                    byte[][] pal = palette.getPalette();
                    int counter = pcxHeight * pcxWidth;
                    int idx = 0;
                    int colorIdx = 0;
                    while (counter-- > 0) {
                        colorIdx = buffer.get(idx) & 0xff;
                        buffer.put(idx++, (byte) (pal[colorIdx][0] & 0xff));
                        buffer.put(idx++, (byte) (pal[colorIdx][1] & 0xff));
                        buffer.put(idx++, (byte) (pal[colorIdx][2] & 0xff));
                    }
                    return img;
                }
            case 3:
                {
                    RGBImage img = new RGBImage(pcxWidth, pcxHeight);
                    ByteBuffer buffer = img.getBuffer();
                    try {
                        byte b = 0;
                        byte fill = 0;
                        int counter = 0;
                        int yindex = 0;
                        int yindexp = 0;
                        for (int y = 0; y < height; y++) {
                            yindex = y * pcxWidth * 3;
                            for (int p = 0; p < planes; p++) {
                                yindexp = yindex + p;
                                counter = 0;
                                do {
                                    b = (byte) (data.readByte() & 0xff);
                                    if ((b & 0xc0) == 0xc0) {
                                        b = (byte) (b & 0x3f);
                                        fill = (byte) (data.readByte() & 0xff);
                                        for (int i = 0; i < b; i++) {
                                            System.out.println("counter: " + counter + "  index: " + (yindexp + (counter * 4)) + " " + i + " / " + b);
                                            buffer.put(yindexp + (counter++ * 3), fill);
                                        }
                                    } else {
                                        buffer.put(yindexp + (counter++ * 3), b);
                                    }
                                } while (counter < width);
                            }
                        }
                    } catch (IOException ioe) {
                        throw new StreamCorruptedException("Image truncated in pixel data");
                    }
                    return img;
                }
            default:
                throw new StreamCorruptedException("Unsupported number of bit planes: " + pcxNPlanes);
        }
    }

    private Palette readPalette(ByteInputStream data) throws StreamCorruptedException {
        Palette palette = new Palette(256);
        try {
            if (data.readByte() != 0x0c) {
                throw new StreamCorruptedException("Palette must be present immediately after pixel data");
            }
            for (int i = 0; i < 256; i++) {
                palette.setColor(i, data.readByte(), data.readByte(), data.readByte());
            }
        } catch (IOException ioe) {
            throw new StreamCorruptedException("Image truncated in palette");
        }
        return palette;
    }
}
