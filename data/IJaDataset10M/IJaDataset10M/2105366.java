package org.speech.asr.recognition.ann.joone;

import org.joone.engine.LinearLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.recognition.math.MathUtils;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 27, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class SoftmaxLayerExt extends LinearLayer {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(SoftmaxLayerExt.class.getName());

    public SoftmaxLayerExt() {
        super();
    }

    public void forward(double[] pattern) {
        int x;
        int n = getRows();
        double sum = 0;
        for (x = 0; x < n; ++x) {
            assert MathUtils.isReal(pattern[x]) : pattern[x];
            outs[x] = Math.exp(pattern[x]);
            if (Double.isInfinite(outs[x])) {
                outs[x] = Float.MAX_VALUE;
            }
            assert MathUtils.isReal(outs[x]) : "Out=" + outs[x] + ",pat=" + pattern[x];
            sum += outs[x];
            if (Double.isInfinite(sum)) {
                sum = Float.MAX_VALUE;
            }
        }
        assert sum != 0;
        for (x = 0; x < n; ++x) {
            double preOut = outs[x];
            outs[x] = outs[x] / sum;
            assert MathUtils.isReal(outs[x]) : "out=" + preOut + ",sum=" + sum;
        }
    }
}
