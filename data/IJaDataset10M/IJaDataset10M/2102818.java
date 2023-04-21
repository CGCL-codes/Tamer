package de.clonejo.jshelldrawer;

/**
 *
 * @author clonejo
 */
public class View implements Runnable {

    private short height, width;

    private long refreshRate;

    private char[][] data;

    boolean waitForChange;

    boolean viewChanged;

    public View(short width, short height, long refreshRate, boolean waitForChange) {
        this.height = (short) (height + 1);
        this.width = (short) (width + 1);
        this.refreshRate = refreshRate;
        this.waitForChange = waitForChange;
        data = new char[getWidth()][getHeight()];
        for (short x = 0; x < getWidth(); x++) {
            for (short y = 0; y < getWidth(); y++) {
                setChar(x, y, ' ');
            }
        }
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(long refreshRate) {
        this.refreshRate = refreshRate;
    }

    public short getHeight() {
        return height;
    }

    public short getWidth() {
        return width;
    }

    public char getChar(short x, short y) {
        return data[x][y];
    }

    public void setChar(short x, short y, char c) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            data[x][y] = c;
        }
        viewChanged = true;
    }

    public void putText(short x, short y, String text) {
        char[] chars = text.toCharArray();
        for (short i = 0; i < chars.length; i++) {
            setChar((short) (x + i), y, chars[i]);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                if ((waitForChange && viewChanged) || !waitForChange) {
                    viewChanged = false;
                    StringBuilder output = new StringBuilder();
                    for (short y = 0; y < height; y++) {
                        for (short x = 0; x < width; x++) {
                            output.append(getChar(x, y));
                        }
                        if (y != width - 1) {
                            output.append('\n');
                        }
                    }
                    System.out.print(output.toString());
                }
                Thread.sleep(refreshRate);
            }
        } catch (InterruptedException ex) {
        }
    }
}
