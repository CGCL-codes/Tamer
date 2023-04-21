package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author dswitkin@google.com (Daniel Switkin) - ported from C++
 */
public final class QRCode {

    public static final int NUM_MASK_PATTERNS = 8;

    private Mode mode;

    private ErrorCorrectionLevel ecLevel;

    private int version;

    private int matrixWidth;

    private int maskPattern;

    private int numTotalBytes;

    private int numDataBytes;

    private int numECBytes;

    private int numRSBlocks;

    private ByteMatrix matrix;

    public QRCode() {
        mode = null;
        ecLevel = null;
        version = -1;
        matrixWidth = -1;
        maskPattern = -1;
        numTotalBytes = -1;
        numDataBytes = -1;
        numECBytes = -1;
        numRSBlocks = -1;
        matrix = null;
    }

    public Mode getMode() {
        return mode;
    }

    public ErrorCorrectionLevel getECLevel() {
        return ecLevel;
    }

    public int getVersion() {
        return version;
    }

    public int getMatrixWidth() {
        return matrixWidth;
    }

    public int getMaskPattern() {
        return maskPattern;
    }

    public int getNumTotalBytes() {
        return numTotalBytes;
    }

    public int getNumDataBytes() {
        return numDataBytes;
    }

    public int getNumECBytes() {
        return numECBytes;
    }

    public int getNumRSBlocks() {
        return numRSBlocks;
    }

    public ByteMatrix getMatrix() {
        return matrix;
    }

    public int at(int x, int y) {
        int value = matrix.get(x, y);
        if (!(value == 0 || value == 1)) {
            throw new RuntimeException("Bad value");
        }
        return value;
    }

    public boolean isValid() {
        return mode != null && ecLevel != null && version != -1 && matrixWidth != -1 && maskPattern != -1 && numTotalBytes != -1 && numDataBytes != -1 && numECBytes != -1 && numRSBlocks != -1 && isValidMaskPattern(maskPattern) && numTotalBytes == numDataBytes + numECBytes && matrix != null && matrixWidth == matrix.getWidth() && matrix.getWidth() == matrix.getHeight();
    }

    public String toString() {
        StringBuffer result = new StringBuffer(200);
        result.append("<<\n");
        result.append(" mode: ");
        result.append(mode);
        result.append("\n ecLevel: ");
        result.append(ecLevel);
        result.append("\n version: ");
        result.append(version);
        result.append("\n matrixWidth: ");
        result.append(matrixWidth);
        result.append("\n maskPattern: ");
        result.append(maskPattern);
        result.append("\n numTotalBytes: ");
        result.append(numTotalBytes);
        result.append("\n numDataBytes: ");
        result.append(numDataBytes);
        result.append("\n numECBytes: ");
        result.append(numECBytes);
        result.append("\n numRSBlocks: ");
        result.append(numRSBlocks);
        if (matrix == null) {
            result.append("\n matrix: null\n");
        } else {
            result.append("\n matrix:\n");
            result.append(matrix.toString());
        }
        result.append(">>\n");
        return result.toString();
    }

    public void setMode(Mode value) {
        mode = value;
    }

    public void setECLevel(ErrorCorrectionLevel value) {
        ecLevel = value;
    }

    public void setVersion(int value) {
        version = value;
    }

    public void setMatrixWidth(int value) {
        matrixWidth = value;
    }

    public void setMaskPattern(int value) {
        maskPattern = value;
    }

    public void setNumTotalBytes(int value) {
        numTotalBytes = value;
    }

    public void setNumDataBytes(int value) {
        numDataBytes = value;
    }

    public void setNumECBytes(int value) {
        numECBytes = value;
    }

    public void setNumRSBlocks(int value) {
        numRSBlocks = value;
    }

    public void setMatrix(ByteMatrix value) {
        matrix = value;
    }

    public static boolean isValidMaskPattern(int maskPattern) {
        return maskPattern >= 0 && maskPattern < NUM_MASK_PATTERNS;
    }
}
