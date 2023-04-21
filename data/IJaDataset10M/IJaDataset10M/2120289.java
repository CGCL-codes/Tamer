package joelib2.feature.types.atompair;

import joelib2.feature.DistanceMetricValue;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureResult;
import joelib2.io.IOType;
import joelib2.math.similarity.DistanceMetric;
import joelib2.math.similarity.DistanceMetricHelper;
import joelib2.molecule.types.BasicPairData;
import joelib2.molecule.types.PairData;
import joelib2.util.HelperMethods;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.log4j.Category;

/**
 * Atom pair descriptor (depends on atom properties used).
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.8 $, $Date: 2005/02/17 16:48:32 $
 */
public class AtomPairResult extends BasicPairData implements Cloneable, FeatureResult, DistanceMetricValue {

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib2.feature.types.atompair.AtomPairResult");

    private static final String DEFAULT_DISTANCE_METRIC_NAME = "BasicAPDistanceMetric";

    private static final String lineFormat = "numberOfAtomPairs p\n" + "numberOfAtomProperties n\n" + "<atom property 1>\n" + "...\n" + "<atom property n>\n" + "<atom property 1 of atom pair 1_a>\n" + "...\n" + "<atom property n of atom pair 1_a>\n" + "<distance of atom pair 1>\n" + "<atom property 1 of atom pair 1_b>\n" + "...\n" + "<atom property n of atom pair 1_b>\n" + "<occurence of atom pair 1>\n" + "...\n" + "...\n" + "<atom property 1 of atom pair p_a>\n" + "...\n" + "<atom property n of atom pair p_a>\n" + "<distance of atom pair 1>\n" + "<atom property 1 of atom pair p_b>\n" + "...\n" + "<atom property n of atom pair p_b>\n" + "<occurence of atom pair p>";

    public Hashtable atomPairs;

    public String[] nominalAP;

    public String[] numericAP;

    private DistanceMetric metric;

    /**
     *  Constructor for the IntResult object
     */
    public AtomPairResult() {
        this.setKey(this.getClass().getName());
        this.setKeyValue(this);
    }

    public Object clone() {
        AtomPairResult newObj = new AtomPairResult();
        return clone(newObj);
    }

    public AtomPairResult clone(AtomPairResult _target) {
        _target.atomPairs = (Hashtable) atomPairs.clone();
        _target.nominalAP = new String[nominalAP.length];
        System.arraycopy(nominalAP, 0, _target.nominalAP, 0, nominalAP.length);
        _target.numericAP = new String[numericAP.length];
        System.arraycopy(numericAP, 0, _target.numericAP, 0, numericAP.length);
        _target.metric = metric;
        return _target;
    }

    /**
     *  Description of the Method
     *
     * @param  ioType  Description of the Parameter
     * @return         Description of the Return Value
     */
    public String formatDescription(IOType ioType) {
        return lineFormat;
    }

    /**
     *  Description of the Method
     *
     * @param  pairData  Description of the Parameter
     * @return           Description of the Return Value
     */
    public boolean fromPairData(IOType ioType, PairData pairData) {
        this.setKey(pairData.getKey());
        Object value = pairData.getKeyValue();
        boolean success = false;
        if ((value != null) && (value instanceof String)) {
            success = fromString(ioType, (String) value);
        }
        return success;
    }

    /**
     *  Description of the Method
     *
     * @param  sValue  Description of the Parameter
     * @return         Description of the Return Value
     */
    public boolean fromString(IOType ioType, String sValue) {
        StringReader sr = new StringReader(sValue);
        LineNumberReader lnr = new LineNumberReader(sr);
        int numAtomPairs = 0;
        int numNominalAP = 0;
        int numNumericAP = 0;
        try {
            numAtomPairs = Integer.parseInt(lnr.readLine());
            numNominalAP = Integer.parseInt(lnr.readLine());
            numNumericAP = Integer.parseInt(lnr.readLine());
            nominalAP = new String[numNominalAP];
            for (int i = 0; i < numNominalAP; i++) {
                nominalAP[i] = lnr.readLine();
            }
            numericAP = new String[numNumericAP];
            for (int i = 0; i < numNumericAP; i++) {
                numericAP[i] = lnr.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        atomPairs = new Hashtable(numAtomPairs);
        AtomPair tmp;
        int occurence;
        for (int i = 0; i < numAtomPairs; i++) {
            tmp = AtomPair.fromString(lnr, numNominalAP, numNumericAP);
            if (tmp == null) {
                logger.error("Atom pair was not parsed successfully.");
                return false;
            } else {
                try {
                    occurence = Integer.parseInt(lnr.readLine());
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    return false;
                }
                atomPairs.put(tmp, new int[] { occurence });
            }
        }
        return true;
    }

    public double getDistance(Object target) {
        if (metric == null) {
            try {
                loadDefaultMetric();
            } catch (FeatureException e) {
                logger.error(e.getMessage());
                return Double.NaN;
            }
        }
        return metric.getDistance(this, target);
    }

    public boolean init(String _descName) {
        this.setKey(_descName);
        return true;
    }

    public void loadDefaultMetric() throws FeatureException {
        loadMetric(DEFAULT_DISTANCE_METRIC_NAME);
    }

    public void loadMetric(String represenation) throws FeatureException {
        metric = DistanceMetricHelper.getDistanceMetric(represenation);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public String toString(IOType ioType) {
        StringBuffer sb = new StringBuffer();
        if (atomPairs == null) {
            logger.error("Contains no data");
            return null;
        }
        sb.append(atomPairs.size());
        sb.append(HelperMethods.eol);
        sb.append(nominalAP.length);
        sb.append(HelperMethods.eol);
        sb.append(numericAP.length);
        sb.append(HelperMethods.eol);
        for (int i = 0; i < this.nominalAP.length; i++) {
            sb.append(nominalAP[i]);
            sb.append(HelperMethods.eol);
        }
        for (int i = 0; i < this.numericAP.length; i++) {
            sb.append(numericAP[i]);
            sb.append(HelperMethods.eol);
        }
        AtomPair key;
        for (Enumeration e = atomPairs.keys(); e.hasMoreElements(); ) {
            key = (AtomPair) e.nextElement();
            sb.append(key.toString(ioType));
            sb.append(HelperMethods.eol);
            sb.append((((int[]) atomPairs.get(key))[0]));
            if (e.hasMoreElements()) {
                sb.append(HelperMethods.eol);
            }
        }
        return sb.toString();
    }
}
