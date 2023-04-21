package org.argetr.resim.proc.filter;

import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.argetr.resim.proc.data.*;

public class AveragePR extends SpatialPR {

    protected int _kernelWidth = 3;

    public void setParameters(int kernelWidth) {
        setParamKernelWidth(kernelWidth);
    }

    public void setParamKernelWidth(int kernelWidth) {
        _kernelWidth = kernelWidth;
    }

    public int getParamKernelWidth() {
        return _kernelWidth;
    }

    public Data process() {
        int kernelWidth = getParamKernelWidth();
        PlanarImage i = getInputAsPlanarImage();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(i);
        pb.add(kernelWidth);
        PlanarImage o = JAI.create("boxfilter", pb);
        ImageDA out = new ImageDA(o);
        setOutput((Data) out);
        return getOutput();
    }
}
