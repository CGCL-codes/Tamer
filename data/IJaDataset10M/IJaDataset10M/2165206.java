package org.phramer.v1.decoder.mert.reader.phramer;

import info.olteanu.utils.*;
import org.phramer.v1.decoder.mert.reader.*;

public class RescoreReaderPhramer implements RescoreReader {

    public double[] readLine(String line) {
        String x[] = StringTools.tokenize(line, " ,");
        double[] out = new double[x.length];
        for (int i = 0; i < out.length; i++) out[i] = Double.parseDouble(x[i]);
        return out;
    }
}
