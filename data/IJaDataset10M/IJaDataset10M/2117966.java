package be.lassi.ui.fixtures;

import java.awt.Rectangle;

public class UniversePanelLayout {

    public static final int COLUMN_COUNT = 25;

    public static final int ROW_COUNT = (512 / COLUMN_COUNT) + 1;

    private static final int BORDER_GAP = 10;

    int width;

    int height;

    int boxWidth;

    int boxHeight;

    int totalWidth;

    int totalHeight;

    int xOffset;

    int yOffset;

    public void setBounds(final Rectangle bounds) {
        width = bounds.width - BORDER_GAP - BORDER_GAP;
        height = bounds.height - BORDER_GAP - BORDER_GAP;
        boxWidth = width / COLUMN_COUNT;
        boxHeight = height / ROW_COUNT;
        totalWidth = boxWidth * COLUMN_COUNT;
        totalHeight = boxHeight * ROW_COUNT;
        xOffset = (width - totalWidth) / 2 + BORDER_GAP;
        yOffset = (height - totalHeight) / 2 + BORDER_GAP;
    }

    public int getChannel(final int x, final int y) {
        int xx = x - xOffset;
        int yy = y - yOffset;
        int channel = (yy / boxHeight) * COLUMN_COUNT + (xx / boxWidth) + 1;
        return channel;
    }

    public int xChannel(final int channelId) {
        int column = channelId % COLUMN_COUNT;
        int x = xOffset + column * boxWidth;
        return x;
    }

    public int yChannel(final int channelId) {
        int row = channelId / COLUMN_COUNT;
        int y = yOffset + row * boxHeight;
        return y;
    }

    public boolean isOutside(final int x, final int y) {
        int channel = getChannel(x, y);
        return channel > 512 || x < xOffset || x > xOffset + totalWidth || y < yOffset || y > yOffset + totalHeight;
    }
}
