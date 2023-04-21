package com.matthewtavares.jipt.processing;

import java.awt.Container;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import com.matthewtavares.jipt.util.PixelExtractor;

/**
 * Blurs an Image by average 8 neighboring pixels of each
 * pixel
 * 
 * @author Matt Tavares mattTavares@gmail.com
 *
 */
public class Blurer extends ImageOperator {

    public Image transform(Image image) {
        int[][][] imgData = PixelExtractor.getPixelData(image);
        int numImgRows = imgData.length;
        int numImgCols = imgData[0].length;
        imgData = ImageOperator.padImageAllSides(imgData, 1);
        int[][][] blurredImgData = new int[numImgRows][numImgCols][4];
        for (int row = 0; row < numImgRows; row++) {
            for (int col = 0; col < numImgCols; col++) {
                blurredImgData[row][col][0] = imgData[row][col][0];
                for (int colorIndex = 1; colorIndex < 4; colorIndex++) {
                    int colorAverage = (int) (((imgData[row][col][colorIndex]) + (imgData[row][col + 1][colorIndex]) + (imgData[row + 1][col][colorIndex]) + (imgData[row + 1][col + 1][colorIndex]) + (imgData[row + 2][col][colorIndex]) + (imgData[row + 2][col + 1][colorIndex]) + (imgData[row + 2][col + 2][colorIndex]) + (imgData[row][col + 2][colorIndex]) + (imgData[row + 1][col + 2][colorIndex])) / 9.0);
                    blurredImgData[row][col][colorIndex] = colorAverage;
                }
            }
        }
        int imgDataOneD[] = PixelExtractor.toOneDImg(blurredImgData);
        return new Container().createImage(new MemoryImageSource(numImgCols, numImgRows, imgDataOneD, 0, numImgCols));
    }
}
