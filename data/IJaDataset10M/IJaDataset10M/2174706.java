package game.visualizations.scatter.adapter.models;

import java.util.Iterator;
import game.visualizations.scatter.adapter.AttributeSelectible;
import game.visualizations.scatter.adapter.ModelAdapterMultivarConfigurable;
import game.visualizations.tools.dataset.MultivarDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.xy.XYDataset;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.learner.PredictionModel;

/**
 * For visualization of RapidMiner regression model in a scatterplot matrix.
 *
 * @author janf
 */
public class PredictionModelMultivarDatasetAdapter extends PredictionModelXYDatasetAdapter implements MultivarDataset, AttributeSelectible<String>, ModelAdapterMultivarConfigurable {

    private static final long serialVersionUID = -4508983707684973579L;

    private int[] selectedAtts;

    private Attribute[] atts;

    private double[] lowerBounds;

    private double[] upperBounds;

    ExampleSet set;

    public void setBounds(double[] lower, double[] upper) {
        if (lower.length == atts.length && upper.length == atts.length) {
            lowerBounds = lower;
            upperBounds = upper;
            for (int i = 0; i < lowerBounds.length; i++) {
                lowerBounds[i] -= Math.abs(lowerBounds[i] / 10);
                upperBounds[i] += Math.abs(upperBounds[i] / 10);
            }
        } else {
            throw new IllegalArgumentException("Illegal array length.");
        }
    }

    public PredictionModelMultivarDatasetAdapter(PredictionModel model, ExampleSet set) {
        super(model, set);
        this.set = set;
        Attributes attributes = set.getAttributes();
        atts = new Attribute[attributes.allSize()];
        selectedAtts = new int[atts.length];
        Iterator<Attribute> iterator = attributes.allAttributes();
        for (int i = 0; iterator.hasNext(); i++) {
            atts[i] = iterator.next();
            selectedAtts[i] = i;
        }
        setXPrecision(DatasetAdapters.DEFAULT_1D_PRECISION / 3);
    }

    @Override
    public Number getInput(int series, int item, int vectorIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getInputValue(int series, int item, int vectorIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAttributeCount() {
        return selectedAtts.length;
    }

    @Override
    public XYDataset toXYDataset(int xIndex, int yIndex) {
        PredictionModelXYDatasetAdapter result = new PredictionModelXYDatasetAdapter(model, set);
        result.setX(atts[selectedAtts[xIndex]].getName());
        result.setY(atts[selectedAtts[yIndex]].getName());
        result.setXLower(lowerBounds[selectedAtts[xIndex]]);
        result.setXUpper(upperBounds[selectedAtts[xIndex]]);
        result.setXPrecision(getXPrecision());
        result.setReferencePoint(getReferencePoint());
        return (XYDataset) result;
    }

    @Override
    public String[] getAttributes() {
        String[] attributes = new String[atts.length];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = atts[i].getName();
        }
        return attributes;
    }

    @Override
    public int[] getSelectedAttributeIndices() {
        return selectedAtts;
    }

    @Override
    public String[] getSelectedAttributes() {
        String[] attributes = new String[selectedAtts.length];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = atts[selectedAtts[i]].getName();
        }
        return attributes;
    }

    @Override
    public void setSelectedAttributeIndices(int[] indices) {
        selectedAtts = indices;
        notifyListeners(new DatasetChangeEvent(this, this));
    }

    @Override
    public void setSelectedAttributes(String[] attributes) {
        selectedAtts = new int[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            for (int j = 0; j < atts.length; j++) {
                if (atts[j].equals(attributes[i])) {
                    selectedAtts[i] = j;
                    break;
                }
            }
        }
        notifyListeners(new DatasetChangeEvent(this, this));
    }
}
