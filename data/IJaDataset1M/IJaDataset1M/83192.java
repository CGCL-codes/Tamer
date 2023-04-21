package de.sonivis.tool.textmining.operations.matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.CoreTooling;
import de.sonivis.tool.core.gnu.r.AbstractRException;
import de.sonivis.tool.core.gnu.r.RManager;
import de.sonivis.tool.textmining.representation.matrix.RDataMatrix;

/**
 * Class for feature selection. The matrix have to be created before.
 * 
 * @author Nette
 * @version $Revision$, $Date$
 */
public class RMatrixFeatureSelection {

    /**
	 * Logger at {@value} .
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(RMatrixFeatureSelection.class.getName());

    /**
	 * Saves the actual feature selection method. Possible methods are listed in
	 * {@link EMatrixOperationsGrouped#FEATURE_SELECTION}.
	 */
    private EMatrixOperation featureSelection = EMatrixOperation.NONE;

    /**
	 * Saves the feature selection threshold in percent.
	 */
    private Double featureSelectionThreshold = EMatrixOperation.FEATSEL_THRESHOLD_DEFAULT;

    /**
	 * Default Constructor.
	 */
    public RMatrixFeatureSelection() {
    }

    /**
	 * Initializing Constructor.
	 * 
	 * @param featureSelection
	 *            The feature selection method.
	 * @param featureSelectionThreshold
	 *            Feature selection threshold.
	 */
    public RMatrixFeatureSelection(final EMatrixOperation featureSelection, final Double featureSelectionThreshold) {
        this.featureSelection = featureSelection;
        this.featureSelectionThreshold = featureSelectionThreshold;
    }

    /**
	 * Returns the actual selected feature selection method.
	 * 
	 * @return The enumeration of the feature selection method.
	 */
    public final EMatrixOperation getFeatureSelection() {
        return featureSelection;
    }

    /**
	 * Returns the actual defined threshold.
	 * 
	 * @return Threshold value.
	 */
    public final Double getFeatureSelectionThreshold() {
        return featureSelectionThreshold;
    }

    /**
	 * Defines the feature selection configuration.
	 * 
	 * @param featureSelection
	 *            The feature selection method.
	 * @param featureSelectionThreshold
	 *            Feature selection threshold.
	 */
    public final void setFeatureSelection(final EMatrixOperation featureSelection, final Double featureSelectionThreshold) {
        this.featureSelection = featureSelection;
        this.featureSelectionThreshold = featureSelectionThreshold;
    }

    /**
	 * Executes a feature selection method on the given matrix.
	 * 
	 * @param rMatrixName
	 *            Name of the matrix on which feature selection should be
	 *            executed.
	 * @return True, if method was successful, false otherwise.
	 */
    public final boolean executeFeatureSelection(final String rMatrixName) {
        boolean succ = false;
        final long startTime = System.nanoTime();
        LOGGER.info("Execute feature selection with: " + featureSelection);
        try {
            switch(featureSelection) {
                case FEATSEL_WEIGHT_THRESHOLDING:
                    executeColumnBasedFeatureSelection(rMatrixName);
                    break;
                default:
                    break;
            }
            succ = true;
        } catch (AbstractRException e) {
            LOGGER.error("There where problems while executing the feature selection on the matrix " + rMatrixName + ". ", e);
        }
        final RDataMatrix rMatrix = new RDataMatrix(rMatrixName);
        LOGGER.info("Feature selection executed in " + String.valueOf((System.nanoTime() - startTime) * CoreTooling.NANO_TIME_FACTOR) + CoreTooling.NANO_TIME_IDENTIFIER + ". New matrix has " + rMatrix.getNumberOfColumns() + " columns and " + rMatrix.getNumberOfRows() + " rows. Success: " + succ);
        return succ;
    }

    /**
	 * Executes the value based feature selection. Only the highest values will
	 * be considered by further analysis. The smallest value is the value which
	 * cover at least {@link RMatrixFeatureSelection#featureSelectionThreshold}
	 * percent of the values.
	 * 
	 * @param rMatrixName
	 *            Name of the matrix on which feature selection should be
	 *            executed.
	 * @throws AbstractRException
	 *             Exception signals R errors during the feature selection.
	 */
    private void executeValueBasedFeatureSelection(final String rMatrixName) throws AbstractRException {
        if (featureSelectionThreshold < 100.0) {
            RManager.getInstance().evalWithoutResult("tdmValues <- sort(summary(" + rMatrixName + ")$x)");
            RManager.getInstance().evalWithoutResult("tdmValueMinIndex <- length(tdmValues) - trunc(length(tdmValues)*" + String.valueOf(featureSelectionThreshold) + "/100)");
            LOGGER.info("There are " + RManager.getInstance().evalAsInt("length(tdmValues)") + " values." + " The value at position " + RManager.getInstance().evalAsInt("as.integer(tdmValueMinIndex)") + " defines the minimum value." + " The value is " + RManager.getInstance().evalAsDouble("tdmValues[tdmValueMinIndex]") + ".");
            RMatrixUtilities.replaceValues(rMatrixName, " < tdmValues[tdmValueMinIndex]", "0");
            RMatrixUtilities.deleteEmptyColumns(rMatrixName);
            RManager.getInstance().evalWithoutResult(rMatrixName + " <- t(" + rMatrixName + ")");
            RMatrixUtilities.deleteEmptyColumns(rMatrixName);
            RManager.getInstance().evalWithoutResult(rMatrixName + " <- t(" + rMatrixName + ")");
            RManager.getInstance().evalWithoutResult("rm(tdmValues)");
            RManager.getInstance().evalWithoutResult("rm(tdmValueMinIndex)");
        }
    }

    /**
	 * Executes the column based feature selection. Values which occur in more
	 * objects than the given threshold allows will be deleted. The threshold 
	 * {@link RMatrixFeatureSelection#featureSelectionThreshold} is
	 * a percentage value corresponding to the total number of objects. 
	 * 
	 * @param rMatrixName
	 *            Name of the matrix on which feature selection should be
	 *            executed.
	 * @throws AbstractRException
	 *             Exception signals R errors during the feature selection.
	 */
    private void executeColumnBasedFeatureSelection(final String rMatrixName) throws AbstractRException {
        if (featureSelectionThreshold < 100.0) {
            final RDataMatrix rMatrix = new RDataMatrix(rMatrixName);
            final Integer numberOfObjects = rMatrix.getNumberOfRows();
            final int numberOfMaximalObjects = ((Double) (numberOfObjects * featureSelectionThreshold / 100)).intValue();
            LOGGER.info("There are " + numberOfObjects + " objects." + " Values which are used from more than " + numberOfMaximalObjects + " objects will be deleted.");
            final String colOpResName = RMatrixUtilities.executeColumnOperation(rMatrixName, "", "function(x){sum(x!=0, na.rm = FALSE)}");
            final int[] numberOfObjectsPerColumn = RManager.getInstance().evalAsIntArray(colOpResName);
            Integer cDeletes = 0;
            for (Integer i = 0; i < numberOfObjectsPerColumn.length; i++) {
                if (numberOfObjectsPerColumn[i] > numberOfMaximalObjects || numberOfObjectsPerColumn[i] < 2) {
                    RManager.getInstance().evalWithoutResult(rMatrixName + " <- " + rMatrixName + "[,-" + (i + 1) + "]");
                    cDeletes++;
                }
            }
            LOGGER.info(cDeletes + " values are deleted.");
        }
    }
}
