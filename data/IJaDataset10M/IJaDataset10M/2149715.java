package org.apache.sanselan.formats.psd.dataparsers;

import org.apache.sanselan.formats.psd.ImageContents;

public class DataParserIndexed extends DataParser {

    private final int ColorTable[];

    public DataParserIndexed(byte ColorModeData[]) {
        ColorTable = new int[256];
        for (int i = 0; i < 256; i++) {
            int red = 0xff & ColorModeData[0 * 256 + i];
            int green = 0xff & ColorModeData[1 * 256 + i];
            int blue = 0xff & ColorModeData[2 * 256 + i];
            int alpha = 0xff;
            int rgb = ((0xff & alpha) << 24) | ((0xff & red) << 16) | ((0xff & green) << 8) | ((0xff & blue) << 0);
            ColorTable[i] = rgb;
        }
    }

    protected int getRGB(int data[][][], int x, int y, ImageContents imageContents) {
        int sample = 0xff & data[0][y][x];
        int rgb = ColorTable[sample];
        return rgb;
    }

    public int getBasicChannelsCount() {
        return 1;
    }
}
