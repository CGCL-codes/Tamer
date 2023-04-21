package brgm.mapreader.operations;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Iterator;
import brgm.mapreader.Image;
import brgm.mapreader.Parameter;
import brgm.mapreader.Toolkit;

public class ParameteredConvolutionOperation extends ParameteredOperation {

    private KernelFactory kernelFactory;

    public ParameteredConvolutionOperation(String name, KernelFactory kf) {
        super(name);
        kernelFactory = kf;
        ArrayList<Parameter> pl = kf.getParameters();
        Iterator<Parameter> i = pl.iterator();
        while (i.hasNext()) {
            i.next().addObserver(this);
        }
    }

    public void doExecute(Image img) {
        ConvolveOp op = new ConvolveOp(kernelFactory.getKernel(), ConvolveOp.EDGE_NO_OP, null);
        BufferedImage bi = img.getBufferedImage();
        BufferedImage dest = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        op.filter(bi, dest);
        this.setImage(new Image(dest));
    }

    public final int getNbInputs() {
        return 1;
    }

    public final ArrayList<Parameter> getParameters() {
        return kernelFactory.getParameters();
    }
}
