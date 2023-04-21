package com.google.zxing.qrcode;

import java.util.Hashtable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class QRCodeWriter implements Writer {

    private static final int QUIET_ZONE_SIZE = 4;

    public BitMatrix encode(String contents, BarcodeFormat format, int multiple) throws WriterException {
        return encode(contents, format, multiple, null);
    }

    public BitMatrix encode(String contents, BarcodeFormat format, int multiple, Hashtable hints) throws WriterException {
        if (contents == null || contents.length() == 0) {
            throw new IllegalArgumentException("Found empty contents");
        }
        if (format != BarcodeFormat.QR_CODE) {
            throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
        }
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        if (hints != null) {
            ErrorCorrectionLevel requestedECLevel = (ErrorCorrectionLevel) hints.get(EncodeHintType.ERROR_CORRECTION);
            if (requestedECLevel != null) {
                errorCorrectionLevel = requestedECLevel;
            }
        }
        QRCode code = new QRCode();
        Encoder.encode(contents, errorCorrectionLevel, hints, code);
        return renderResult(code, multiple);
    }

    private static BitMatrix renderResult(QRCode code, int multiple) {
        ByteMatrix input = code.getMatrix();
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (QUIET_ZONE_SIZE << 1);
        int qrHeight = inputHeight + (QUIET_ZONE_SIZE << 1);
        int leftPadding = QUIET_ZONE_SIZE * multiple;
        int topPadding = QUIET_ZONE_SIZE * multiple;
        BitMatrix output = new BitMatrix(qrWidth * multiple, qrHeight * multiple);
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }
        return output;
    }
}
