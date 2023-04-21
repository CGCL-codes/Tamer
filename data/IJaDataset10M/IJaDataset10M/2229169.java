package shellkk.qiq.jdm.common.kernel;

import java.util.Map;
import shellkk.qiq.math.kernel.Kernel;

public class HermiteKernelFactory implements KernelFactory {

    protected String kernelDescription;

    public String getKernelDescription() {
        return kernelDescription;
    }

    public void setKernelDescription(String kernelDescription) {
        this.kernelDescription = kernelDescription;
    }

    public Kernel createKernel(Map<String, String> kernelProps) throws Exception {
        HermiteKernel kernel = new HermiteKernel();
        if (kernelProps != null) {
            String QStrategy = kernelProps.get("QStrategy");
            if (QStrategy != null) {
                kernel.QStrategy = Double.parseDouble(QStrategy);
            }
        }
        return kernel;
    }

    public String[] getKernelPropertyNames() {
        return new String[] { "QStrategy" };
    }
}
