package org.argetr.resim.proc;

import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.argetr.resim.proc.data.*;

public class HistogramMatchPR extends HistogramPR {

    protected HistogramMatchDA _histMatchDA;

    protected void setConnectionRules(ConnectionRule connRule) {
        connRule.addInType(ImageDA.class, 0);
        connRule.addOutType(ImageDA.class);
    }

    public void setParameters(HistogramMatchDA histMatchDA) {
        setParamHistogramMatchDA(histMatchDA);
    }

    public void setParamHistogramMatchDA(HistogramMatchDA histMatchDA) {
        _histMatchDA = histMatchDA;
    }

    public HistogramMatchDA getParamHistogramMatchDA() {
        return _histMatchDA;
    }

    public Data process() {
        super.process();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(getHistImage());
        int nBand = getHistImage().getNumBands();
        int nCDBand = _histMatchDA.getCD().length;
        if (nBand > nCDBand) {
            _histMatchDA.setCD(ConverterUT.get2DFloatArray(_histMatchDA.getCD()[0], nBand));
        }
        PlanarImage o = (PlanarImage) JAI.create("matchcdf", getHistImage(), _histMatchDA.getCD());
        ImageDA outData = new ImageDA(o);
        outData.addDataType(Data.HIST_IMAGE);
        setOutput(outData);
        return getOutput();
    }
}
