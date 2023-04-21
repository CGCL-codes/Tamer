package maltcms.experimental.operations;

import maltcms.datastructures.array.IFeatureVector;
import org.apache.commons.configuration.Configuration;
import ucar.ma2.Array;
import ucar.ma2.Index;
import cross.annotations.Configurable;
import cross.annotations.RequiresVariables;

/**
 * 
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 */
@RequiresVariables(names = { "var.binned_mass_values", "var.binned_intensity_values" })
public class WeightedCosine extends TwoFeatureVectorOperation {

    @Configurable(name = "var.binned_mass_values")
    private String binned_mass_values = "binned_mass_values";

    @Configurable(name = "var.binned_intensity_values")
    private String binned_intensity_values = "binned_intensity_values";

    @Override
    public double apply(IFeatureVector f1, IFeatureVector f2) {
        Array m1 = f1.getFeature(this.binned_mass_values);
        Array m2 = f2.getFeature(this.binned_mass_values);
        Array i1 = f1.getFeature(this.binned_intensity_values);
        Array i2 = f2.getFeature(this.binned_intensity_values);
        Index m1idx = m1.getIndex();
        Index m2idx = m2.getIndex();
        Index i1idx = i1.getIndex();
        Index i2idx = i2.getIndex();
        double s1 = 0, s2 = 0;
        double v = 0.0d;
        for (int i = 0; i < m1.getShape()[0]; i++) {
            s1 += (m1.getDouble(m1idx.set(i)) * i1.getDouble(i1idx.set(i)));
            s2 += (m2.getDouble(m2idx.set(i)) * i2.getDouble(i2idx.set(i)));
        }
        for (int i = 0; i < m1.getShape()[0]; i++) {
            v += (((m1.getDouble(m1idx.set(i)) * i1.getDouble(i1idx.set(i))) / s1) * ((m2.getDouble(m2idx.set(i)) * i2.getDouble(i2idx.set(i))) / s2));
        }
        return v;
    }

    @Override
    public boolean isMinimize() {
        return false;
    }

    @Override
    public void configure(Configuration cfg) {
        super.configure(cfg);
    }
}
