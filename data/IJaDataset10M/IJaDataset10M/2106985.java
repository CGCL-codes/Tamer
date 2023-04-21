package org.apache.sanselan.formats.bmp.pixelparsers;

import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.bmp.BmpHeaderInfo;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.awt.image.DataBuffer;

public class PixelParserRle extends PixelParser {

    public PixelParserRle(BmpHeaderInfo bhi, byte ColorTable[], byte ImageData[]) {
        super(bhi, ColorTable, ImageData);
    }

    private int getSamplesPerByte() throws ImageReadException, IOException {
        if (bhi.bitsPerPixel == 8) return 1; else if (bhi.bitsPerPixel == 4) return 2; else throw new ImageReadException("BMP RLE: bad BitsPerPixel: " + bhi.bitsPerPixel);
    }

    private int[] convertDataToSamples(int data) throws ImageReadException, IOException {
        int rgbs[];
        if (bhi.bitsPerPixel == 8) {
            rgbs = new int[1];
            rgbs[0] = getColorTableRGB(data);
        } else if (bhi.bitsPerPixel == 4) {
            rgbs = new int[2];
            int sample1 = data >> 4;
            int sample2 = 0x0f & data;
            rgbs[0] = getColorTableRGB(sample1);
            rgbs[1] = getColorTableRGB(sample2);
        } else throw new ImageReadException("BMP RLE: bad BitsPerPixel: " + bhi.bitsPerPixel);
        return rgbs;
    }

    private int processByteOfData(int rgbs[], int repeat, int x, int y, int width, int height, DataBuffer db, BufferedImage bi) throws ImageReadException {
        int pixels_written = 0;
        for (int i = 0; i < repeat; i++) {
            if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
                int rgb = rgbs[i % rgbs.length];
                db.setElem(y * bhi.width + x, rgb);
            } else {
                System.out.println("skipping bad pixel (" + x + "," + y + ")");
            }
            x++;
            pixels_written++;
        }
        return pixels_written;
    }

    public void processImage(BufferedImage bi) throws ImageReadException, IOException {
        DataBuffer db = bi.getRaster().getDataBuffer();
        int count = 0;
        int width = bhi.width;
        int height = bhi.height;
        int x = 0, y = height - 1;
        boolean done = false;
        while (!done) {
            count++;
            int a = 0xff & bfp.readByte("RLE (" + x + "," + y + ") a", is, "BMP: Bad RLE");
            int b = 0xff & bfp.readByte("RLE (" + x + "," + y + ")  b", is, "BMP: Bad RLE");
            if (a == 0) {
                switch(b) {
                    case 0:
                        {
                            y--;
                            x = 0;
                        }
                        break;
                    case 1:
                        done = true;
                        break;
                    case 2:
                        {
                            int c = 0xff & bfp.readByte("RLE c", is, "BMP: Bad RLE");
                            int d = 0xff & bfp.readByte("RLE d", is, "BMP: Bad RLE");
                        }
                        break;
                    default:
                        {
                            int SamplesPerByte = getSamplesPerByte();
                            int size = b / SamplesPerByte;
                            if ((b % SamplesPerByte) > 0) size++;
                            if ((size % 2) != 0) size++;
                            byte bytes[] = bfp.readByteArray("bytes", size, is, "RLE: Absolute Mode");
                            int remaining = b;
                            for (int i = 0; remaining > 0; i++) {
                                int samples[] = convertDataToSamples(0xff & bytes[i]);
                                int towrite = Math.min(remaining, SamplesPerByte);
                                int written = processByteOfData(samples, towrite, x, y, width, height, db, bi);
                                x += written;
                                remaining -= written;
                            }
                        }
                        break;
                }
            } else {
                int rgbs[] = convertDataToSamples(b);
                x += processByteOfData(rgbs, a, x, y, width, height, db, bi);
            }
        }
    }
}
