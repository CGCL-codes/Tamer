package genomicMap.inputModel;

import genomicMap.data.RFLPImputation;
import genomicMap.inputModel.optOperations.ComGLHoodOptionalOperModel;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * the input model corresponding to the worker ComGLHood.
 * This will have all the parameters for GDataSource and some for GLikelihood. One should
 * be able to create an instance of GDataSource from this model.
 * @author Susanta Tewari
 * @version 1.0
 * @created 16-Nov-2007 11:23:09 AM
 */
@Entity
public class ComGLHoodIModel extends InputModel implements java.io.Serializable {

    private int iterationLimit = 150;

    private double convgEpsilon = 1.0E-4;

    @OneToOne(cascade = CascadeType.ALL)
    private RFLPImputation rflpImputation = new RFLPImputation();

    @OneToOne(cascade = CascadeType.ALL)
    private ComGLHoodOptionalOperModel optionalOperModel = new ComGLHoodOptionalOperModel();

    public ComGLHoodIModel() {
    }

    public static void main(String[] args) {
        ComGLHoodIModel inputModel = new ComGLHoodIModel();
        System.out.println(inputModel.getOptionalOperModel().isComputingSE());
    }

    @Override
    public int hashCode() {
        return super.hashCode() + new Integer(iterationLimit).hashCode() + new Double(convgEpsilon).hashCode() + getRflpImputation().hashCode();
    }

    /**
     * Note that <CODE>optionalOperModel</CODE> is not included in the <CODE>hashCode</CODE>
     * and <CODE>equals</CODE> implementation. This is to differentiate between the
     * core input setings that would require a fresh LHood computation from optional
     * operations which do not require a fresh LHood computation except for few
     * (SE computation if the accuracy wasn`t up to the required level).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComGLHoodIModel) {
            if (super.equals(obj)) {
                ComGLHoodIModel inputModel = (ComGLHoodIModel) obj;
                return this.getIterationLimit() == inputModel.getIterationLimit() && this.getConvgEpsilon() == inputModel.getConvgEpsilon() && this.getRflpImputation().equals(inputModel.getRflpImputation());
            }
            return false;
        }
        return false;
    }

    public int getIterationLimit() {
        return iterationLimit;
    }

    public void setIterationLimit(int iterationLimit) {
        this.iterationLimit = iterationLimit;
    }

    public double getConvgEpsilon() {
        return convgEpsilon;
    }

    public void setConvgEpsilon(double convgEpsilon) {
        this.convgEpsilon = convgEpsilon;
    }

    public ComGLHoodOptionalOperModel getOptionalOperModel() {
        return optionalOperModel;
    }

    public void setOptionalOperModel(ComGLHoodOptionalOperModel optionalOperModel) {
        this.optionalOperModel = optionalOperModel;
    }

    public RFLPImputation getRflpImputation() {
        return rflpImputation;
    }

    public void setRflpImputation(RFLPImputation rflpImputation) {
        this.rflpImputation = rflpImputation;
    }
}
