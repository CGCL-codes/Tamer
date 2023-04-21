package iclab.filtering.discretization;

import iclab.core.ICData;
import iclab.core.ICAttribute.ICAttType;
import iclab.exceptions.ICParameterException;

public class ICWidthDiscretizer extends ICDiscretizerEngine {

    protected int[] _numBins = null;

    public ICWidthDiscretizer(ICData data) {
        super(data);
    }

    /**
	 * This is the method that actually performs the discretization algorithm. In this class it does nothing, and
	 * thus it should be overaided in other discretization classes
	 * @return - a double matrix where the first index is the attribute and the second the discretization policy. 
	 * A null value for this policy indicates that the attribute should not be discretized
	 */
    public double[][] estimateCutPoints() throws ICParameterException {
        if (_numBins == null) throw new ICParameterException("Before you can discretize, set the number of bins for each attribute");
        double[] max = new double[_numBins.length];
        double[] min = new double[_numBins.length];
        for (int j = 0; j < max.length; j++) max[j] = min[j] = Double.NaN;
        for (int i = 0; i < _data.numInstances(); i++) {
            double[] instanceValues = _data.instance(i).getInstanceValues();
            for (int a = 0; a < _numBins.length; a++) {
                if (_numBins[a] < 0) {
                    throw new ICParameterException("Negative number of bins not allowed!!!");
                } else if (_numBins[a] > 0) {
                    double val = instanceValues[a];
                    if (val < min[a]) min[a] = val;
                    if (val > max[a]) max[a] = val;
                }
            }
        }
        for (int c = 0; c < _cutPoints.length; c++) {
            double delta = (max[c] - min[c]) / _numBins[c];
            _cutPoints[c] = new double[_numBins[c] - 1];
            for (int b = 1; b < _numBins[c]; b++) _cutPoints[c][b] = min[c] + b * delta;
        }
        return _cutPoints;
    }

    /**
	 * This method is used to specify the number of bins into which each attribute has to be discretized
	 * Attributes with value "0" will not be discretized
	 * @param bins - Number of bins into which each attribute is discretized
	 * @throws ICParameterException
	 */
    public void setNumBins(int[] bins) throws ICParameterException {
        if (bins.length != _data.numAttributes()) throw new ICParameterException("The number of positions in the array of number of bins has to be equal to " + "the number of attributes");
        for (int a = 0; a < bins.length; a++) if ((bins[a] != 0) & (_data.attribute(a).getType() == ICAttType.categorical)) throw new ICParameterException("You are trying to discretize a categorical attribute!!!");
        _numBins = bins;
    }

    /**
	 * This method can be used to set the number of bins when we want to discretize some features and all of them
	 * have the same number of bins
	 * @param indexes - indexes of attribute to discretize
	 * @param bins - number of bins into which attributes will be discretized
	 * @param invert - when true the list of indexes is inverted (that is, all attributes except those provided
	 * are discretized)
	 * @throws ICParameterException
	 */
    public void setNumBins(int[] indexes, int bins, boolean invert) throws ICParameterException {
        int[] nbins = new int[_data.numAttributes()];
        for (int i = 0; i < nbins.length; i++) if (invert) nbins[i] = bins; else nbins[i] = 0;
        for (int i = 0; i < indexes.length; i++) if (invert) nbins[indexes[i]] = 0; else nbins[indexes[i]] = bins;
        setNumBins(nbins);
    }

    /**
	 * Method to set the same number of bins for all the attributes in the dataset
	 * @param bins - number of bins
	 * @throws ICParameterException
	 */
    public void setNumBins(int bins) throws ICParameterException {
        int[] nbins = new int[_data.numAttributes()];
        for (int i = 0; i < nbins.length; i++) nbins[i] = bins;
        setNumBins(nbins);
    }
}
