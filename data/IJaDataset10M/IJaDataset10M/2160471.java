package com.roncemer.barcode;

import java.util.*;
import com.roncemer.util.*;

/**
  * Class to scan an image for bar codes and return an array of all decoded
  * bar codes.
  *
  * @author Ronald B. Cemer
  */
public class ImageBarCodeScanner {

    private static final int thresholdGranularitySpacing = 16;

    private BarCodeDecoder[] decoders;

    private int scanLineSpacing = 4;

    private static boolean enableHorizontalScanning = true;

    private static boolean enableVerticalScanning = true;

    private static boolean enableDiagonalScanning = true;

    /**
      * Construct a new <code>ImageBarCodeScanner</code> object with all of
      * the default decoders.
      */
    public ImageBarCodeScanner() {
        decoders = new BarCodeDecoder[] { new UPCABarCodeDecoder(), new Code39BarCodeDecoder() };
    }

    /**
      * Construct a new <code>ImageBarCodeScanner</code> object with a specific
      * set of decoders.
      * @param decoders An array of <code>BarCodeDecoder</code> objects to use
      * as bar code decoders when scanning the image for bar codes.
      */
    public ImageBarCodeScanner(BarCodeDecoder[] decoders) {
        this.decoders = decoders;
    }

    /**
      * Set the scan line spacing.  This is how many pixels apart the scans
      * of the image will be.  The wider this is, the fewer scans will be done
      * of the image, and the faster the processing will occur.  However, the
      * side effect of increasing this setting is that more bar codes may be
      * missed.
      * @param scanLineSpacing The new scan line spacing.
      */
    public void setScanLineSpacing(int scanLineSpacing) {
        this.scanLineSpacing = scanLineSpacing;
    }

    /**
      * @return The current scan line spacing.
      */
    public int getScanLineSpacing() {
        return scanLineSpacing;
    }

    /**
      * Enable or disable horizontal scanning.
      * @param enable <code>true</code> to enable; <code>false</code> to
      * disable.
      */
    public void setEnableHorizontalScanning(boolean enable) {
        enableHorizontalScanning = enable;
    }

    /**
      * @return <code>true</code> if horizontal scanning is enabled;
      * <code>false</code> if it is disabled.
      */
    public boolean getEnableHorizontalScanning() {
        return enableHorizontalScanning;
    }

    /**
      * Enable or disable vertical scanning.
      * @param enable <code>true</code> to enable; <code>false</code> to
      * disable.
      */
    public void setEnableVerticalScanning(boolean enable) {
        enableVerticalScanning = enable;
    }

    /**
      * @return <code>true</code> if vertical scanning is enabled;
      * <code>false</code> if it is disabled.
      */
    public boolean getEnableVerticalScanning() {
        return enableVerticalScanning;
    }

    /**
      * Enable or disable diagonal scanning.
      * @param enable <code>true</code> to enable; <code>false</code> to
      * disable.
      */
    public void setEnableDiagonalScanning(boolean enable) {
        enableDiagonalScanning = enable;
    }

    /**
      * @return <code>true</code> if diagonal scanning is enabled;
      * <code>false</code> if it is disabled.
      */
    public boolean getEnableDiagonalScanning() {
        return enableDiagonalScanning;
    }

    /**
      * Scan an image for bar codes and return an array of all decoded
      * bar codes.
      * @param pixels An array containing the monochrome source pixels.  Each
      * element in this array should be in the range of 0-255.
      * @param w The width of the image, in pixels.
      * @param h The height of the image, in pixels.
      * @param includeCheckDigits <code>true</code> to return check digits;
      * <code>false</code> to strip them off.
      * @param listener The <code>BarCodeDecoderListener</code> to be notified
      * each time a bar code is decoded, or <code>null</code> if none.
      * @return An array of <code>String</code> objects containing the scanned
      * bar codes.
      */
    public String[] decodeBarCodesFromImage(int[] pixels, int w, int h, boolean includeCheckDigits, BarCodeDecoderListener listener) {
        int npix = w * h;
        int[] scanLine = new int[Math.max(w, h) * 2];
        int[] barWidths = new int[scanLine.length];
        ArrayList foundBarCodes = new ArrayList();
        int[] enhPixels = new int[npix];
        boolean firstThresh = true;
        boolean aborted = false;
        for (int thresh = 64; ((thresh <= 128) && (!aborted)); thresh += 64, firstThresh = false) {
            for (int intens = (firstThresh ? 0 : 64); intens <= 128; intens += 64) {
                if (intens == 0) {
                    System.arraycopy(pixels, 0, enhPixels, 0, npix);
                } else {
                    ImageUtils.sobelEnhance(pixels, enhPixels, w, h, thresh, intens);
                }
                if (enableHorizontalScanning) {
                    for (int y = 0; y < h; y += scanLineSpacing) {
                        int pixIdx = w * y;
                        for (int x = 0; x < w; x++, pixIdx++) scanLine[x] = enhPixels[pixIdx];
                        if (!scanLineForBarCodes(scanLine, w, barWidths, foundBarCodes, includeCheckDigits, listener)) {
                            aborted = true;
                            break;
                        }
                    }
                    if (aborted) break;
                }
                if (enableVerticalScanning) {
                    for (int x = 0; x < w; x += scanLineSpacing) {
                        int pixIdx = x;
                        for (int y = 0; y < h; y++, pixIdx += w) scanLine[y] = enhPixels[pixIdx];
                        if (!scanLineForBarCodes(scanLine, h, barWidths, foundBarCodes, includeCheckDigits, listener)) {
                            aborted = true;
                            break;
                        }
                    }
                    if (aborted) break;
                }
                if (enableDiagonalScanning) {
                    int wPlus1 = w + 1;
                    for (int startX = 0; startX < w; startX += scanLineSpacing) {
                        int pixIdx = startX;
                        int nPix = 0;
                        for (int x = startX, y = 0; ((x < w) && (y < h)); x++, y++, pixIdx += wPlus1, nPix++) {
                            scanLine[nPix] = enhPixels[pixIdx];
                        }
                        if (!scanLineForBarCodes(scanLine, nPix, barWidths, foundBarCodes, includeCheckDigits, listener)) {
                            aborted = true;
                            break;
                        }
                    }
                    if (aborted) break;
                    for (int startY = scanLineSpacing; startY < h; startY += scanLineSpacing) {
                        int pixIdx = (startY * w);
                        int nPix = 0;
                        for (int x = 0, y = startY; ((x < w) && (y < h)); x++, y++, pixIdx += wPlus1, nPix++) {
                            scanLine[nPix] = enhPixels[pixIdx];
                        }
                        if (!scanLineForBarCodes(scanLine, nPix, barWidths, foundBarCodes, includeCheckDigits, listener)) {
                            aborted = true;
                            break;
                        }
                    }
                    if (aborted) break;
                    int wMinus1 = w - 1;
                    for (int startY = 0; startY < h; startY += scanLineSpacing) {
                        int pixIdx = startY * w;
                        int nPix = 0;
                        for (int y = startY, x = 0; ((y >= 0) && (x < w)); y--, x++, pixIdx -= wMinus1) {
                            scanLine[nPix++] = enhPixels[pixIdx];
                        }
                        if (!scanLineForBarCodes(scanLine, nPix, barWidths, foundBarCodes, includeCheckDigits, listener)) {
                            aborted = true;
                            break;
                        }
                    }
                    if (aborted) break;
                    for (int startX = scanLineSpacing; startX < w; startX += scanLineSpacing) {
                        int pixIdx = ((h - 1) * w) + startX;
                        int nPix = 0;
                        for (int x = startX, y = h - 1; ((y >= 0) && (x < w)); y--, x++, pixIdx -= wMinus1) {
                            scanLine[nPix++] = enhPixels[pixIdx];
                        }
                        if (!scanLineForBarCodes(scanLine, nPix, barWidths, foundBarCodes, includeCheckDigits, listener)) {
                            aborted = true;
                            break;
                        }
                    }
                    if (aborted) break;
                }
            }
        }
        String[] result = new String[foundBarCodes.size()];
        foundBarCodes.toArray(result);
        return result;
    }

    private final boolean scanLineForBarCodes(int[] pixels, int nPixels, int[] barWidths, ArrayList foundBarCodes, boolean includeCheckDigits, BarCodeDecoderListener listener) {
        if (nPixels < 2) return true;
        int min = pixels[0];
        int max = min;
        for (int i = 1; i < nPixels; i++) {
            int sample = pixels[i];
            if (sample < min) min = sample; else if (sample > max) max = sample;
        }
        int thresholdGranularity = (max - min) / thresholdGranularitySpacing;
        if (thresholdGranularity < 1) thresholdGranularity = 1;
        for (int spaceThreshold = min + thresholdGranularity; spaceThreshold < max; spaceThreshold += thresholdGranularity) {
            int widIdx = 0;
            int wid = 1;
            boolean bar = (pixels[0] < spaceThreshold);
            for (int x = 1; x < nPixels; x++) {
                int pix = pixels[x];
                if (bar == (pix < spaceThreshold)) {
                    wid++;
                } else {
                    if ((bar) || (widIdx > 0)) barWidths[widIdx++] = wid;
                    wid = 1;
                    bar = !bar;
                }
            }
            for (int i = 0; (i < decoders.length); i++) {
                String[] bcs = decoders[i].decode(barWidths, widIdx, includeCheckDigits, listener);
                for (int j = 0; j < bcs.length; j++) {
                    String bc = bcs[j];
                    if (foundBarCodes.indexOf(bc) < 0) foundBarCodes.add(bc);
                }
                if (decoders[i].getAbortedByListener()) return false;
            }
        }
        return true;
    }
}
