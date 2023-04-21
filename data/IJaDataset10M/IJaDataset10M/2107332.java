package filter;

import java.awt.image.BufferedImage;
import utils.BasicOperations;

/**
 *
 * @author Michał
 */
public class FilterStandard {

    int maskSize;

    String inputPath;

    String formatName;

    public FilterStandard(int maskSize, String inputPath, String formatName) {
        this.maskSize = maskSize;
        this.inputPath = inputPath;
        this.formatName = formatName;
    }

    private void go() {
        try {
            System.out.println("Pelny filtr zaczyna prace.");
            BufferedImage img = BasicOperations.readImage(inputPath);
            BufferedImage resultImg = BasicOperations.filterAll(img, maskSize);
            String outputPath = inputPath.substring(0, inputPath.indexOf('.')) + "_standard" + inputPath.substring(inputPath.indexOf('.'));
            BasicOperations.writeImage(resultImg, outputPath, formatName);
            System.out.println("Pelny filtr konczy prace.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        go();
        long endTime = System.currentTimeMillis();
        System.out.println("Czas pracy filtra: " + (endTime - startTime) + " ms.");
    }
}
