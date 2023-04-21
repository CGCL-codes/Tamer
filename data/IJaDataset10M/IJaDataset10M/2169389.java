package de.intarsys.pdf.filter;

import java.io.IOException;
import de.intarsys.pdf.cos.COSDictionary;

public class PNGOptimumPrediction extends PNGPrediction {

    private PNGPrediction average;

    private PNGPrediction none;

    private PNGPrediction paeth;

    private PNGPrediction sub;

    private PNGPrediction up;

    public PNGOptimumPrediction(COSDictionary options) {
        super(options);
        none = new PNGNonePrediction(options);
        sub = new PNGSubPrediction(options);
        up = new PNGUpPrediction(options);
        average = new PNGAveragePrediction(options);
        paeth = new PNGPaethPrediction(options);
    }

    public void decodeRow(byte[] source, int sourceOffset, byte[] result, int resultOffset) throws IOException {
        PNGPrediction prediction;
        switch(source[sourceOffset] + 10) {
            case PNGNone:
                prediction = none;
                break;
            case PNGSub:
                prediction = sub;
                break;
            case PNGUp:
                prediction = up;
                break;
            case PNGAverage:
                prediction = average;
                break;
            case PNGPaeth:
                prediction = paeth;
                break;
            default:
                throw new IOException("Unknown predictor function.");
        }
        prediction.decodeRow(source, sourceOffset, result, resultOffset);
    }
}
