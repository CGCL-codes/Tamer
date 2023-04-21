package configuration.classifiers.single.rapidMiner;

import configuration.Slider;
import configuration.classifiers.ClassifierConfigBase;
import game.classifiers.single.rapidMiner.RapidKNNClassifier;
import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.annotations.SelectionSet;
import org.ytoh.configurations.ui.CheckBox;
import org.ytoh.configurations.ui.SelectionSetModel;
import java.util.Collection;

/**
 * Configuration for rapid miner KNN classification algorithm.
 */
public class RapidKNNConfig extends ClassifierConfigBase {

    @Property(name = "K", description = "K in the K-Nearest neighbours.")
    @Slider(value = 1, min = 1, max = 5, multiplicity = 1, name = "K:")
    protected int nearestNeighbours;

    @Property(name = "Weighted vote", description = "Indicates if the votes should be weighted by similarity.")
    @CheckBox
    protected boolean weightedVote;

    @Property(name = "Measure type", description = "Measure type for computing distance.")
    @SelectionSet(key = "measureType", type = SelectionSetModel.class)
    protected SelectionSetModel<String> measureType;

    public RapidKNNConfig() {
        super();
        String[] measureTypes = new String[] { "MixedMeasures.MixedEuclideanDistance", "NumericalMeasures.EuclideanDistance", "NumericalMeasures.CamberraDistance", "NumericalMeasures.ChebychevDistance", "NumericalMeasures.CorrelationSimilarity", "NumericalMeasures.CosineSimilarity", "NumericalMeasures.DiceSimilarity", "NumericalMeasures.DynamicTimeWarpingDistance", "NumericalMeasures.InnerProductSimilarity", "NumericalMeasures.JaccardSimilarity", "NumericalMeasures.ManhattanDistance", "NumericalMeasures.MaxProductSimilarity", "NumericalMeasures.OverlapSimilarity" };
        measureType = new SelectionSetModel<String>(measureTypes);
        measureType.disableAllElements();
        measureType.enableElement(0);
        nearestNeighbours = 1;
        weightedVote = false;
        classRef = RapidKNNClassifier.class;
    }

    public RapidKNNConfig clone() {
        RapidKNNConfig newObject = (RapidKNNConfig) super.clone();
        String[] measureTypes = measureType.getAllElements();
        String[] copiedMeasureTypes = new String[measureTypes.length];
        System.arraycopy(measureTypes, 0, copiedMeasureTypes, 0, measureTypes.length);
        SelectionSetModel<String> copiedMeasureType = new SelectionSetModel<String>(copiedMeasureTypes);
        copiedMeasureType.disableAllElements();
        copiedMeasureType.enableElement(measureType.getEnableElementIndices()[0]);
        newObject.setMeasureType(copiedMeasureType);
        return newObject;
    }

    protected String variablesToString() {
        String measure = measureType.getEnabledElements(String.class)[0];
        measure = measure.substring(measure.lastIndexOf(".") + 1);
        return "(k=" + nearestNeighbours + ",vote=" + weightedVote + ",measure=" + measure + ")";
    }

    public SelectionSetModel<String> getMeasureType() {
        return measureType;
    }

    public void setMeasureType(SelectionSetModel<String> measureType) {
        this.measureType = measureType;
    }

    public boolean getWeightedVote() {
        return weightedVote;
    }

    public void setWeightedVote(boolean weightedVote) {
        this.weightedVote = weightedVote;
    }

    public int getNearestNeighbours() {
        return nearestNeighbours;
    }

    public void setNearestNeighbours(int nearestNeighbours) {
        this.nearestNeighbours = nearestNeighbours;
    }
}
