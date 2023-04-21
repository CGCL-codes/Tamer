package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.Matrix;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeSet;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class AnovaTwoWay implements Analysis {

    private static final int NUMBER_OF_Y_VAR = 1;

    private static final int PRESET_NUMBER_GROUPS = 9;

    private static final String X_DATA_TYPE = DataType.FACTOR;

    private static final String Y_DATA_TYPE = DataType.QUANTITATIVE;

    private static final int NUMBER_OF_DUMMY_DIGITS = AnalysisUtility.NUMBER_OF_DUMMY_DIGITS;

    private static final String INTERACTION_SWITCH = "INTERACTION";

    private boolean interactionOn = false;

    private String type = "AnovaTwoWay";

    private HashMap resultMap = null;

    private String[] varNames = null;

    ArrayList<String> varNameList = new ArrayList<String>();

    String[] varNameArray = null;

    private int varLength = 0;

    int[] dfModelGroup = new int[PRESET_NUMBER_GROUPS];

    int[] dfErrorGroup = new int[PRESET_NUMBER_GROUPS];

    int dfErrorTwoWay = 0;

    double[] rssGroup = new double[PRESET_NUMBER_GROUPS];

    double[] mseModelGroup = new double[PRESET_NUMBER_GROUPS];

    double[] mseErrorGroup = new double[PRESET_NUMBER_GROUPS];

    double[] fValueGroup = new double[PRESET_NUMBER_GROUPS];

    double[] pValueGroup = new double[PRESET_NUMBER_GROUPS];

    int boxPlotFactorSize = 0;

    int boxPlotRowSize = 0;

    int boxPlotColSize = 1;

    double[][][] boxPlotYValue = null;

    String[] boxPlotFactorName = null;

    String[] boxPlotRowFactorName = null;

    String[] boxPlotColFactorName = null;

    double estVarModel;

    double estVarError;

    double grandAvg = 0;

    int sampleSize;

    double[][] predictedBetweenArray;

    int varCount = 0;

    HashMap<String, Object> texture = new HashMap<String, Object>();

    double rssError;

    double rssModel;

    double[] predicted;

    double[] residuals;

    public String getAnalysisType() {
        return type;
    }

    public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
        Result result = null;
        interactionOn = ((Boolean) data.getParameter(analysisType, INTERACTION_SWITCH)).booleanValue();
        if (analysisType != AnalysisType.ANOVA_TWO_WAY) throw new WrongAnalysisException();
        HashMap<String, Object> xMap = data.getMapX();
        HashMap<String, Object> yMap = data.getMapY();
        if (xMap == null || yMap == null) throw new WrongAnalysisException();
        Set<String> keySet = null;
        Result resultBetween = null;
        keySet = yMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        double y[] = null;
        String keys = null;
        while (iterator.hasNext()) {
            keys = (String) iterator.next();
            try {
                Class cls = keys.getClass();
            } catch (Exception e) {
            }
        }
        Column yColumn = (Column) yMap.get(keys);
        y = yColumn.getDoubleArray();
        String yDataType = yColumn.getDataType();
        if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
            throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
        }
        double[] predictedBetween = null;
        predictedBetweenArray = new double[2][];
        varCount = 0;
        keySet = xMap.keySet();
        iterator = keySet.iterator();
        ArrayList<Object> x = new ArrayList<Object>();
        int xIndex = 0;
        boolean useColumn = false;
        Column xColumn = null;
        int dfCTotal = 0, dfError = 0, dfModel = 0;
        double mssModel = 0;
        double mssError = 0;
        double rssTotal = 0, fValue = 0;
        double pValue = 0;
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            if (xIndex == 0) {
                useColumn = true;
            } else {
                useColumn = false;
            }
            keys = (String) iterator.next();
            try {
                Class cls = keys.getClass();
            } catch (Exception e) {
            }
            xColumn = (Column) xMap.get(keys);
            String xDataType = xColumn.getDataType();
            if (!xDataType.equalsIgnoreCase(X_DATA_TYPE)) {
                throw new WrongAnalysisException(WrongAnalysisException.ERROR_MESSAGE);
            }
            String xVector[] = xColumn.getStringArray();
            data = new Data();
            data.appendX(keys, xVector, DataType.FACTOR);
            data.appendY("Y", y, DataType.QUANTITATIVE);
            try {
                resultBetween = data.getAnalysis(AnalysisType.ANOVA_ONE_WAY);
                predictedBetween = (double[]) (resultBetween.getTexture().get(AnovaTwoWayResult.PREDICTED));
                predictedBetweenArray[varCount] = predictedBetween;
                varCount++;
            } catch (Exception e) {
            }
            try {
                dfCTotal = ((Integer) (resultBetween.getTexture().get(AnovaTwoWayResult.DF_TOTAL))).intValue();
            } catch (NullPointerException e) {
            }
            try {
                dfError = ((Integer) (resultBetween.getTexture().get(AnovaTwoWayResult.DF_ERROR))).intValue();
                dfErrorGroup[xIndex] = dfError;
            } catch (NullPointerException e) {
            }
            try {
                dfModel = ((Integer) (resultBetween.getTexture().get(AnovaTwoWayResult.DF_MODEL))).intValue();
                dfModelGroup[xIndex] = dfModel;
            } catch (NullPointerException e) {
            }
            try {
                rssModel = ((Double) resultBetween.getTexture().get(AnovaTwoWayResult.RSS_MODEL)).doubleValue();
                rssGroup[xIndex] = rssModel;
            } catch (NullPointerException e) {
            }
            try {
                rssError = ((Double) resultBetween.getTexture().get(AnovaTwoWayResult.RSS_ERROR)).doubleValue();
            } catch (NullPointerException e) {
            }
            try {
                mssModel = ((Double) resultBetween.getTexture().get(AnovaTwoWayResult.MSS_MODEL)).doubleValue();
                mseModelGroup[xIndex] = mssModel;
            } catch (NullPointerException e) {
            }
            try {
                mssError = ((Double) resultBetween.getTexture().get(AnovaTwoWayResult.MSS_ERROR)).doubleValue();
                mseErrorGroup[xIndex] = mssError;
            } catch (NullPointerException e) {
            }
            try {
                rssTotal = ((Double) resultBetween.getTexture().get(AnovaTwoWayResult.RSS_TOTAL)).doubleValue();
            } catch (NullPointerException e) {
            }
            try {
                boxPlotFactorSize = ((Integer) (resultBetween.getTexture().get(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE))).intValue();
                if (xIndex == 0) {
                    boxPlotColSize = boxPlotFactorSize;
                } else {
                    boxPlotRowSize = boxPlotFactorSize;
                }
            } catch (NullPointerException e) {
            }
            try {
                boxPlotFactorName = ((String[]) resultBetween.getTexture().get(AnovaOneWayResult.BOX_PLOT_FACTOR_NAME));
                if (xIndex == 0) {
                    boxPlotColFactorName = boxPlotFactorName;
                    for (int i = 0; i < boxPlotFactorName.length; i++) {
                    }
                } else {
                    boxPlotRowFactorName = boxPlotFactorName;
                    for (int i = 0; i < boxPlotFactorName.length; i++) {
                    }
                }
            } catch (NullPointerException e) {
            }
            if (useColumn) {
                try {
                    boxPlotYValue = ((double[][][]) resultBetween.getTexture().get(AnovaTwoWayResult.BOX_PLOT_RESPONSE_VALUE));
                } catch (NullPointerException e) {
                }
            }
            double rss = AnalysisUtility.sumOfSquares(predictedBetween);
            x.add(xIndex, xVector);
            varNameList.add(xIndex, keys);
            xIndex++;
            data = null;
            resultBetween = null;
        }
        varLength = varNameList.size();
        varNameArray = new String[varLength];
        if (interactionOn) {
            varNameArray = new String[varLength + 1];
            for (int i = 0; i < varLength; i++) {
                varNameArray[i] = (String) varNameList.get(i);
            }
            varNameArray[varLength] = "Interaction " + varNameArray[0] + ":" + varNameArray[1];
        } else {
            for (int i = 0; i < varLength; i++) {
                varNameArray[i] = (String) varNameList.get(i);
            }
        }
        AnovaTwoWayResult anovaTwoWayResult = this.regression(x, y);
        for (int i = 0; i < count; i++) {
            try {
                fValue = mseModelGroup[i] / estVarError;
                fValueGroup[i] = fValue;
            } catch (NullPointerException e) {
            }
            try {
                dfModel = dfModelGroup[i];
                dfError = dfErrorGroup[i];
                FisherDistribution fDistribution = new FisherDistribution(dfModel, dfErrorTwoWay);
                pValue = (1 - fDistribution.getCDF(fValueGroup[i]));
                pValueGroup[i] = pValue;
            } catch (NullPointerException e) {
            }
        }
        texture.put(AnovaTwoWayResult.F_VALUE_GROUP, fValueGroup);
        texture.put(AnovaTwoWayResult.P_VALUE_GROUP, pValueGroup);
        return anovaTwoWayResult;
    }

    private AnovaTwoWayResult regression(ArrayList<Object> x, double[] y) throws DataIsEmptyException {
        grandAvg = AnalysisUtility.mean(y);
        AnovaTwoWayResult result = new AnovaTwoWayResult(texture);
        sampleSize = y.length;
        int numberOfX = x.size();
        int xLength = 0;
        String[][] xString = new String[numberOfX][sampleSize];
        int xIndex = 0;
        int totalNumCol = 0;
        ArrayList listVarX = new ArrayList();
        xLength = ((String[]) (x.get(xIndex))).length;
        if (sampleSize != xLength) {
            return null;
        }
        byte[][] dummy = null;
        byte[][] dummyTemp = new byte[sampleSize][NUMBER_OF_DUMMY_DIGITS * numberOfX + 1];
        int increment = 0;
        int numberColumns = 0;
        int dummyLength1 = 1, dummyLength2 = 1;
        boolean varIsColumn = true;
        for (xIndex = 0; xIndex < varLength; xIndex++) {
            xString[xIndex] = (String[]) x.get(xIndex);
            for (int i = 0; i < xString[0].length; i++) {
            }
            dummy = AnalysisUtility.getDummyMatrix(xString[xIndex]);
            int dummyLength = dummy[0].length;
            for (int j = 0; j < dummy[0].length; j++) {
                for (int i = 0; i < sampleSize; i++) {
                    dummyTemp[i][1 + increment + j] = dummy[i][j];
                }
            }
            increment = increment + dummyLength;
            if (xIndex == 0) dummyLength1 = dummyLength;
            if (xIndex == 1) dummyLength2 = dummyLength;
        }
        numberColumns = increment + 1;
        int[][] dummyInteraction = null;
        if (interactionOn) {
            dummyInteraction = new int[sampleSize][dummyLength1 * dummyLength2];
            for (int k = 0; k < dummyLength2; k++) for (int j = 0; j < dummyLength1; j++) for (int i = 0; i < sampleSize; i++) {
                dummyInteraction[i][j * dummyLength2 + k] = (int) dummyTemp[i][j + 1] * (int) dummyTemp[i][dummyLength1 + k + 1];
            }
        }
        double[][] varX = new double[sampleSize][numberColumns];
        for (int i = 0; i < sampleSize; i++) {
            varX[i][0] = 1;
        }
        for (int j = 0; j < varX[0].length; j++) {
            for (int i = 0; i < sampleSize; i++) {
                if (j != 0) varX[i][j] = dummyTemp[i][j];
            }
        }
        numberColumns = varX[xIndex].length;
        totalNumCol = numberColumns;
        predicted = new double[sampleSize];
        residuals = new double[sampleSize];
        double rssErrorWithoutInteraction = 0;
        double rssErrorInteraction = 0;
        double rssResiduals = 0;
        doRssError(totalNumCol, varX, y);
        rssErrorWithoutInteraction = rssError;
        rssResiduals = rssError;
        if (interactionOn) {
            varX = new double[sampleSize][numberColumns + dummyInteraction[0].length];
            for (int i = 0; i < sampleSize; i++) {
                varX[i][0] = 1;
            }
            for (int j = 0; j < varX[0].length; j++) {
                for (int i = 0; i < sampleSize; i++) {
                    if (j != 0) varX[i][j] = dummyTemp[i][j];
                }
            }
            for (int j = 0; j < dummyInteraction[0].length; j++) {
                for (int i = 0; i < sampleSize; i++) {
                    varX[i][j + numberColumns] = dummyInteraction[i][j];
                }
            }
            totalNumCol = varX[0].length;
            rssError = 0;
            doRssErrorInteraction(totalNumCol, varX, y);
            rssResiduals = rssError;
            rssErrorInteraction = rssErrorWithoutInteraction - rssError;
            dfModelGroup[2] = dfModelGroup[0] * dfModelGroup[1];
            rssGroup[2] = rssErrorInteraction;
            mseModelGroup[2] = rssErrorInteraction / dfModelGroup[2];
            int dfError = sampleSize - totalNumCol;
            int dfModel = totalNumCol - 1;
            estVarModel = rssModel / dfModel;
            estVarError = rssErrorInteraction / dfError;
            double fValue = estVarModel / estVarError;
            FisherDistribution fDistribution = new FisherDistribution(dfModel, dfError);
            double pValue = (1 - fDistribution.getCDF(fValue));
            pValueGroup[2] = pValue;
            fValueGroup[2] = fValue;
        }
        int numberGroups = totalNumCol;
        int dfCorrectedTotal = sampleSize - 1;
        int dfError = sampleSize - numberGroups;
        int dfModel = numberGroups - 1;
        this.estVarModel = rssModel / dfModel;
        this.estVarError = rssResiduals / dfError;
        double rssTotal = rssModel + rssResiduals;
        dfErrorTwoWay = dfError;
        FisherDistribution fDistribution = new FisherDistribution(dfModel, dfError);
        double fValue = estVarModel / estVarError;
        double pValue = (1 - fDistribution.getCDF(fValue));
        HashMap residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, dfError);
        double[] sortedResiduals = (double[]) residualMap.get(LinearModelResult.SORTED_RESIDUALS);
        int[] sortedResidualsIndex = (int[]) residualMap.get(LinearModelResult.SORTED_RESIDUALS_INDEX);
        double[] sortedNormalQuantiles = (double[]) residualMap.get(LinearModelResult.SORTED_NORMAL_QUANTILES);
        double varPredicted = AnalysisUtility.sampleVariance(predicted);
        double varResiduals = AnalysisUtility.sampleVariance(residuals);
        double rSquare = varPredicted / (varResiduals + varPredicted);
        texture.put(AnovaTwoWayResult.VARIABLE_LIST, varNameArray);
        texture.put(AnovaTwoWayResult.DF_MODEL_GROUP, dfModelGroup);
        texture.put(AnovaTwoWayResult.RSS_GROUP, rssGroup);
        texture.put(AnovaTwoWayResult.MSE_GROUP, mseModelGroup);
        texture.put(AnovaTwoWayResult.PREDICTED, predicted);
        texture.put(AnovaTwoWayResult.RESIDUALS, residuals);
        texture.put(AnovaTwoWayResult.DF_TOTAL, new Integer(dfCorrectedTotal));
        texture.put(AnovaTwoWayResult.DF_ERROR, new Integer(dfError));
        texture.put(AnovaTwoWayResult.DF_MODEL, new Integer(dfModel));
        texture.put(AnovaTwoWayResult.RSS_ERROR, new Double(rssResiduals));
        texture.put(AnovaTwoWayResult.RSS_MODEL, new Double(rssModel));
        texture.put(AnovaTwoWayResult.MSS_ERROR, new Double(estVarError));
        texture.put(AnovaTwoWayResult.MSS_MODEL, new Double(estVarModel));
        texture.put(AnovaTwoWayResult.RSS_TOTAL, new Double(rssTotal));
        texture.put(AnovaTwoWayResult.F_VALUE, new Double(fValue));
        texture.put(AnovaTwoWayResult.P_VALUE, new Double(pValue));
        texture.put(AnovaTwoWayResult.R_SQUARE, new Double(rSquare));
        texture.put(AnovaTwoWayResult.BOX_PLOT_ROW_SIZE, new Integer(boxPlotRowSize));
        texture.put(AnovaTwoWayResult.BOX_PLOT_COLUMN_SIZE, new Integer(boxPlotColSize));
        texture.put(AnovaTwoWayResult.BOX_PLOT_ROW_NAME, boxPlotRowFactorName);
        texture.put(AnovaTwoWayResult.BOX_PLOT_COLUMN_NAME, boxPlotColFactorName);
        texture.put(AnovaTwoWayResult.BOX_PLOT_RESPONSE_VALUE, boxPlotYValue);
        texture.put(AnovaTwoWayResult.SORTED_RESIDUALS, sortedResiduals);
        texture.put(AnovaTwoWayResult.SORTED_RESIDUALS_INDEX, sortedResidualsIndex);
        texture.put(AnovaTwoWayResult.SORTED_NORMAL_QUANTILES, sortedNormalQuantiles);
        return result;
    }

    protected void doRssError(int totalNumCol, double[][] varX, double[] y) {
        double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];
        for (int i = 0; i < sampleSize; i++) {
            vectorY[i][0] = y[i];
        }
        double meanY = 0;
        try {
            meanY = AnalysisUtility.mean(y);
        } catch (Exception e) {
        }
        Matrix matrixY = new Matrix(sampleSize, 1, vectorY);
        Matrix matrixX = new Matrix(sampleSize, totalNumCol, varX);
        Matrix xTranspose = matrixX.transpose();
        Matrix beta = new Matrix(totalNumCol, 1);
        Matrix productXX = new Matrix(totalNumCol, totalNumCol);
        productXX = Matrix.multiply(xTranspose, matrixX);
        Matrix inverseProductXX = new Matrix(totalNumCol, totalNumCol);
        inverseProductXX = AnalysisUtility.inverse(productXX);
        beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));
        double betaEntry[][] = new double[totalNumCol][1];
        betaEntry = beta.element;
        Matrix hat = Matrix.multiply(Matrix.multiply(matrixX, inverseProductXX), xTranspose);
        Matrix predictedY = Matrix.multiply(hat, matrixY);
        double[][] predictedYEntry = new double[sampleSize][1];
        predictedYEntry = predictedY.element;
        for (int i = 0; i < sampleSize; i++) {
            if (varLength == 2) {
                predicted[i] = predictedBetweenArray[0][i] + predictedBetweenArray[1][i] - grandAvg;
            } else if (varLength == 1) {
                predicted[i] = predictedBetweenArray[0][i] - grandAvg;
            }
            residuals[i] = y[i] - predicted[i];
        }
        Matrix yAvg = new Matrix(sampleSize, 1, meanY);
        Matrix residualModel = Matrix.subtract(predictedY, yAvg);
        rssModel = 0;
        for (int i = 0; i < sampleSize; i++) {
            rssModel += residualModel.element[i][0] * residualModel.element[i][0];
        }
        rssError = 0;
        for (int i = 0; i < sampleSize; i++) {
            rssError += residuals[i] * residuals[i];
        }
    }

    protected void doRssErrorInteraction(int totalNumCol, double[][] varX, double[] y) {
        double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];
        for (int i = 0; i < sampleSize; i++) {
            vectorY[i][0] = y[i];
        }
        double meanY = 0;
        try {
            meanY = AnalysisUtility.mean(y);
        } catch (Exception e) {
        }
        Matrix matrixY = new Matrix(sampleSize, 1, vectorY);
        Matrix matrixX = new Matrix(sampleSize, totalNumCol, varX);
        Matrix xTranspose = matrixX.transpose();
        Matrix beta = new Matrix(totalNumCol, 1);
        Matrix productXX = new Matrix(totalNumCol, totalNumCol);
        productXX = Matrix.multiply(xTranspose, matrixX);
        Matrix inverseProductXX = new Matrix(totalNumCol, totalNumCol);
        inverseProductXX = AnalysisUtility.inverse(productXX);
        beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));
        Matrix predictedInteraction = Matrix.multiply(matrixX, beta);
        double rssResiduals = 0;
        double[][] predictedInteractionEntry = predictedInteraction.element;
        for (int i = 0; i < predictedInteraction.rows; i++) rssResiduals += (y[i] - predictedInteractionEntry[i][0]) * (y[i] - predictedInteractionEntry[i][0]);
        rssError = rssResiduals;
        Matrix yAvg = new Matrix(sampleSize, 1, meanY);
        Matrix residualModel = Matrix.subtract(predictedInteraction, yAvg);
        rssModel = 0;
        for (int i = 0; i < sampleSize; i++) {
            rssModel += residualModel.element[i][0] * residualModel.element[i][0];
        }
    }
}
