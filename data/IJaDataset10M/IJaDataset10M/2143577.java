package org.argetr.resim.proc.filter;

import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;
import org.argetr.resim.proc.data.*;

public class GradientMagnitudePR extends SpatialPR {

    protected KernelDA _horizontalKernel;

    protected KernelDA _verticalKernel;

    public void setParameters(KernelDA horizontalKernel, KernelDA verticalKernel) {
        setParamHorizontalKernel(horizontalKernel);
        setParamVerticalKernel(verticalKernel);
    }

    public void setParamHorizontalKernel(KernelDA kernel) {
        _horizontalKernel = kernel;
    }

    public KernelDA getParamHorizontalKernel() {
        return _horizontalKernel;
    }

    public void setParamVerticalKernel(KernelDA kernel) {
        _verticalKernel = kernel;
    }

    public KernelDA getParamVerticalKernel() {
        return _verticalKernel;
    }

    public Data process() {
        KernelDA kernelH = getParamHorizontalKernel();
        KernelDA kernelV = getParamVerticalKernel();
        KernelJAI kernelJAIH = new KernelJAI(kernelH.getNCol(), kernelH.getNCol(), ConverterUT.get1DFloatArray(kernelH.getKernel()));
        KernelJAI kernelJAIV = new KernelJAI(kernelV.getNCol(), kernelV.getNCol(), ConverterUT.get1DFloatArray(kernelV.getKernel()));
        PlanarImage i = getInputAsPlanarImage();
        PlanarImage o = JAI.create("gradientmagnitude", i, kernelJAIH, kernelJAIV);
        ImageDA out = new ImageDA(o);
        setOutput((Data) out);
        return getOutput();
    }
}
