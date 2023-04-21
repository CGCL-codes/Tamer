package org.argeproje.resim.proc.math;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.argeproje.resim.proc.data.*;

public class LogPR extends MathPR {

    public Data process() {
        PlanarImage i = getInputAsPlanarImage();
        PlanarImage o = JAI.create("log", i);
        ImageDA out = new ImageDA(o);
        setOutput((Data) out);
        return getOutput();
    }
}
