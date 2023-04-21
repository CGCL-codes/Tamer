package preprocessing.methods.MissingData;

import game.utils.Exceptions.InvalidArgument;
import preprocessing.Parameters.Parameter;
import preprocessing.methods.BasePreprocessor;
import preprocessing.storage.BasePreprocessingStorage;
import preprocessing.storage.PreprocessingStorage;
import weka.core.FastVector;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 17.3.2007
 * Time: 17:50:43
 * To change this template use File | Settings | File Templates.
 */
public class MedianMissingDataImputer extends BasePreprocessor {

    private boolean classSpecific;

    private int[] attributeIdxs;

    public MedianMissingDataImputer() {
        super();
        methodName = "Median Missing Value Imputer";
        methodDescription = "Replaces missing value with dataset median.";
        methodTree = "Missing values.";
        baseConfig = new MedianMissingDataImputerConfig();
        type = BasePreprocessor.Type.LOCAL;
        classSpecific = false;
    }

    private boolean gatherConfigParameters() {
        attributeIdxs = baseConfig.getSeparatedIndices(store);
        Parameter p;
        String paramName = "";
        try {
            paramName = "Impute according to class?";
            p = baseConfig.getParameterObjByKey(paramName);
            String s = (String) p.getValue();
            if (s.compareToIgnoreCase("Yes") == 0) {
                classSpecific = true;
            } else {
                classSpecific = false;
            }
        } catch (NoSuchFieldException e) {
            logger.error("No parameter called \"" + paramName + "\" found in " + methodName + ".\n", e);
            System.err.flush();
            System.out.flush();
            System.exit(-1);
        }
        return true;
    }

    private boolean imputeMedian(FastVector classIndices, int processAttribute) {
        double sum = 0.0;
        int nonNaNNumbers = 0;
        Object[] idxs = classIndices.toArray();
        double[] itemCache = new double[idxs.length];
        for (int i = 0; i < idxs.length; i++) {
            Double d = (Double) store.getDataItem(processAttribute, (Integer) idxs[i]);
            itemCache[i] = d;
            if (!d.isNaN()) {
                sum = sum + d;
                nonNaNNumbers++;
            }
        }
        if (nonNaNNumbers == 0) {
            logger.warn("No non-missing values in attribute " + processAttribute + ".");
            return false;
        }
        if (nonNaNNumbers == idxs.length) {
            return true;
        }
        sum = sum / nonNaNNumbers;
        for (int i = 0; i < idxs.length; i++) {
            if (Double.isNaN(itemCache[i])) {
                store.setDataItem(processAttribute, (Integer) idxs[i], sum);
            }
        }
        return true;
    }

    public boolean run() {
        if (!gatherConfigParameters()) {
            return false;
        }
        FastVector[] indexes;
        if (classSpecific) {
            try {
                FastVector cls = null;
                cls = BasePreprocessingStorage.getClassesInStorage(store);
                indexes = new FastVector[cls.size()];
                for (int i = 0; i < cls.size(); i++) {
                    indexes[i] = BasePreprocessingStorage.getInstanceIndicesOfClass(store, (Integer) cls.elementAt(i));
                }
            } catch (InvalidArgument invalidArgument) {
                invalidArgument.printStackTrace();
                return false;
            }
        } else {
            try {
                indexes = new FastVector[1];
                indexes[0] = new FastVector(store.getAttributeLength(0));
                for (int i = 0; i < store.getAttributeLength(0); i++) {
                    indexes[0].addElement(i);
                }
            } catch (InvalidArgument invalidArgument) {
                invalidArgument.printStackTrace();
                logger.warn("Preprocessing storage appears to be empty");
                return false;
            }
        }
        for (int k = 0; k < attributeIdxs.length; k++) {
            if (store.getAttributeType(attributeIdxs[k]) != PreprocessingStorage.DataType.NUMERIC) {
                logger.error("Attribute " + attributeIdxs[k] + " is not numeric. Skipping...");
                continue;
            }
            for (int i = 0; i < indexes.length; i++) {
                imputeMedian(indexes[i], k);
            }
        }
        return true;
    }

    public void finish() {
    }

    public boolean isApplyOnTestingData() {
        return true;
    }
}
