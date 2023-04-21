package org.tubs.epoc.SMFF.ImportExport.Pdf.Graph;

import java.awt.*;
import java.util.Random;

public class GraphFormating {

    public static Color CreateGraphCellColor(int AppId) {
        switch(AppId) {
            case 1:
                return Color.GREEN;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.RED;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.MAGENTA;
            case 6:
                return Color.PINK;
            default:
                return new Color(new Random(AppId).nextInt());
        }
    }
}
