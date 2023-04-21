package vaspgui.eigenvalFiles;

import java.util.ArrayList;
import java.util.List;
import vaspgui.IO;

public class EigenvalFormater {

    private static int numKpoints;

    private static int numBands;

    private static List<List<Double>> bands;

    public static void refreshFile() {
        final int startLine = 5;
        ArrayList<String> file = IO.getFile(IO.getCurDir(), "EIGENVAL");
        numKpoints = 0;
        numBands = 0;
        bands = new ArrayList<List<Double>>();
        List<Double> list;
        if (file == null) {
            return;
        }
        try {
            numKpoints = Integer.parseInt(file.get(startLine).trim().split("\\s+")[1]);
            numBands = Integer.parseInt(file.get(startLine).trim().split("\\s+")[2]);
            for (int i = 0; i < numBands; i++) {
                list = new ArrayList<Double>();
                for (int j = 0; j < numKpoints; j++) {
                    list.add(Double.parseDouble(file.get((numBands + 2) * j + 8 + i).trim().split("\\s+")[1]));
                }
                bands.add(list);
            }
            return;
        } catch (NumberFormatException nfe) {
            return;
        }
    }

    public static int getKpoints() {
        return numKpoints;
    }

    public static int getNumBands() {
        return numBands;
    }

    public static List<List<Double>> getBands() {
        return bands;
    }
}
