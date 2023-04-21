package game.visualizations.roc;

import game.visualizations.ChartIOObject;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.PredictionModel;

/**
 * ROC curve generator for RapidMiner classifiers (objects of the {@link PredictionModel} class
 * with nominal output attribute).
 * 
 * @author janf
 *
 */
public class RapidROCCurveGenerator extends AbstractROCCurveGenerator {

    private static final long serialVersionUID = 7176709480635235540L;

    private int[] displayedClasses;

    private ExampleSet set;

    private PredictionModel model;

    public RapidROCCurveGenerator(ChartIOObject object) {
        this.set = object.getData();
        this.model = object.getModel();
        if (set.getAttributes().getLabel().isNominal()) {
            displayedClasses = new int[set.getAttributes().getLabel().getMapping().size()];
            for (int i = 0; i < displayedClasses.length; i++) displayedClasses[i] = i;
        } else {
            displayedClasses = null;
        }
        updateResults();
    }

    protected String getClassName(int classIndex) {
        return set.getAttributes().getLabel().getMapping().mapIndex(classIndex);
    }

    protected struct[] getPole(int classIndex) throws OperatorException {
        ExampleSet setWithPrediction = model.apply((ExampleSet) set.clone());
        struct[] pole = new struct[setWithPrediction.size()];
        for (int j = 0; j < setWithPrediction.size(); j++) {
            pole[j] = new struct();
            pole[j].response = setWithPrediction.getExample(j).getConfidence(getClassName(classIndex));
            double expectedOutput = (setWithPrediction.getExample(j).getLabel() == classIndex) ? 1 : 0;
            assert expectedOutput == 0 || expectedOutput == 1;
            pole[j].expected = expectedOutput;
        }
        return pole;
    }

    public String[] getClasses() {
        if (!set.getAttributes().getLabel().isNominal()) return new String[0];
        String[] classes = new String[set.getAttributes().getLabel().getMapping().size()];
        List<String> list = set.getAttributes().getLabel().getMapping().getValues();
        for (int i = 0; i < set.getAttributes().getLabel().getMapping().size(); i++) {
            classes[i] = list.get(i);
        }
        return classes;
    }

    public String[] getSelectedClasses() {
        String[] result = new String[displayedClasses.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.toString(displayedClasses[i]);
        }
        return result;
    }

    public Color getSeriesColor(int series) {
        if (set.getAttributes().getLabel().getMapping().size() == 0) return Color.RED;
        return getColorProvider().getPointColor((double) displayedClasses[series] / (double) (set.getAttributes().getLabel().getMapping().size() - 1));
    }

    public void setSelectedClasses(String[] classes) {
        displayedClasses = new int[classes.length];
        for (int i = 0; i < displayedClasses.length; i++) {
            displayedClasses[i] = Integer.parseInt(classes[i]);
        }
        Arrays.sort(displayedClasses);
        updateResults();
    }

    @Override
    public int[] getSelectedClassIndices() {
        return displayedClasses;
    }

    @Override
    public void setSelectedClassIndices(int[] indices) {
        displayedClasses = indices;
        updateResults();
    }
}
