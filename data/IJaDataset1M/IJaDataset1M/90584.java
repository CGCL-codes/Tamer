package physis.visualisation.util;

import java.awt.Color;

public class ColorRange {

    private Color[] colors;

    /**
     *  Creates the colorrange with the spektrum from BLUE to RED.
     */
    public ColorRange(int size) {
        int j = 0;
        try {
            colors = new Color[size];
            Color[] cols = { new Color(0, 0, 96), Color.blue, Color.cyan, Color.green, Color.yellow, Color.red };
            int s = size / (cols.length);
            j = 0;
            for (int i = 0; i < cols.length - 1; i++) {
                Color[] tmp = getColorShades(cols[i], cols[i + 1], s);
                for (int k = 0; k < tmp.length; k++) {
                    colors[j] = tmp[k];
                    j++;
                }
            }
            s = colors.length - j - 1;
            Color[] tmp = getColorShades(cols[cols.length - 1], new Color(255, 128, 128), s);
            for (int i = 0; i < tmp.length; i++) {
                colors[j] = tmp[i];
                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + j);
        }
    }

    public ColorRange(Color start, Color end, int size) {
        colors = getColorShades(start, end, size);
    }

    public int getSize() {
        return colors.length;
    }

    public Color getColor(int n) {
        return colors[n];
    }

    public Color getDeadColor() {
        return Color.black;
    }

    public Color getNewBornColor() {
        return Color.darkGray;
    }

    private static Color[] getColorShades(Color start, Color end, int size) {
        size++;
        float red_incrementum = (end.getRed() - start.getRed()) / size;
        float green_incrementum = (end.getGreen() - start.getGreen()) / size;
        float blue_incrementum = (end.getBlue() - start.getBlue()) / size;
        float red = start.getRed() + red_incrementum;
        float green = start.getGreen() + green_incrementum;
        float blue = start.getBlue() + blue_incrementum;
        Color[] colors;
        colors = new Color[size];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color((int) red, (int) green, (int) blue);
            red += red_incrementum;
            green += green_incrementum;
            blue += blue_incrementum;
        }
        return colors;
    }
}
