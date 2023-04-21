package maltcms.commands.distances.dtwng;

import java.awt.Point;
import java.util.List;
import maltcms.datastructures.array.IArrayD2Double;
import maltcms.datastructures.array.IFeatureVector;
import maltcms.datastructures.IFileFragmentModifier;

/**
 * 
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 */
public interface IOptimizationFunction extends IFileFragmentModifier {

    public abstract void init(List<IFeatureVector> l, List<IFeatureVector> r, IArrayD2Double cumulatedScores, IArrayD2Double pwScores, TwoFeatureVectorOperation tfvo);

    public abstract void apply(int... is);

    public abstract List<Point> getTrace();

    public abstract String getOptimalOperationSequenceString();

    public abstract String[] getStates();

    public abstract void setWeight(String state, double d);

    public abstract double getWeight(String state);

    public abstract double getOptimalValue();
}
