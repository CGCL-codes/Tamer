package game.visualizations.scatter;

import game.models.ModelLearnable;
import game.visualizations.scatter.adapter.GameDatasetHelper;
import game.visualizations.scatter.adapter.ModelAdapterMultivarConfigurable;
import game.visualizations.scatter.adapter.data.AbstractMultivarToXYDatasetAdapter;
import game.visualizations.scatter.adapter.data.DefaultMultivarDatasetAdapter;
import game.visualizations.scatter.adapter.data.ModelLearnableTrainingDataAdapterStrategy;
import game.visualizations.scatter.adapter.models.AbstractMultivarModelToXYDatasetAdapter;
import game.visualizations.scatter.adapter.models.GameModelLearnableMultivarDatasetAdapter;

/**
 * Scatterplot matrix visualization
 * for GAME regression models.
 * 
 * @author janf
 *
 */
public class ModelLearnableScatterplotMatrixVisualization extends AbstractScatterplotMatrixVisualization<ModelLearnable> {

    protected String[] attributes;

    protected double[] defaultReference;

    protected double[] lower;

    protected double[] upper;

    public ModelLearnableScatterplotMatrixVisualization() {
        super();
        attributes = new String[0];
        lower = new double[0];
        upper = new double[0];
        defaultReference = new double[0];
    }

    public ModelLearnableScatterplotMatrixVisualization(ModelLearnable model) {
        super(model);
        attributes = new String[model.getLearningInputVectors().length + 1];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = Integer.toString(i);
        }
        lower = GameDatasetHelper.getLowerInputs(model);
        upper = GameDatasetHelper.getUpperInputs(model);
        defaultReference = GameDatasetHelper.getMedianInstance(model, new int[0]);
        if (this.model instanceof ModelAdapterMultivarConfigurable) ((ModelAdapterMultivarConfigurable) this.model).setBounds(lower, upper);
        setReferencePoint(defaultReference);
        setChanged();
        clearChanged();
        updateChartPanel();
    }

    @Override
    public String[] getAttributes() {
        return attributes;
    }

    @Override
    public double[] getLowerReferencePoint() {
        return lower;
    }

    @Override
    public double[] getUpperReferencePoint() {
        return upper;
    }

    @Override
    public void setDefaultReferencePoint() {
        setReferencePoint(defaultReference);
    }

    @Override
    protected String getClassAttr() {
        return null;
    }

    @Override
    protected boolean isSetClassEnabled() {
        return false;
    }

    @Override
    public void setClass(String cls) {
    }

    @Override
    protected void setClassEnabled(boolean enabled) {
    }

    @Override
    public String[] getNominalAttributes() {
        return new String[0];
    }

    @Override
    protected AbstractMultivarToXYDatasetAdapter getDataAdapter(ModelLearnable object) {
        return new DefaultMultivarDatasetAdapter(new ModelLearnableTrainingDataAdapterStrategy(object));
    }

    @Override
    protected AbstractMultivarModelToXYDatasetAdapter getModelAdapter(ModelLearnable object) {
        return new GameModelLearnableMultivarDatasetAdapter(object);
    }

    @Override
    protected double[] getLowers() {
        return lower;
    }

    @Override
    protected double[] getUppers() {
        return upper;
    }
}
