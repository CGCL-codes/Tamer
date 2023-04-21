package edu.ucla.stat.SOCR.analyses.model;

import edu.ucla.stat.SOCR.analyses.result.*;
import edu.ucla.stat.SOCR.analyses.data.*;
import edu.ucla.stat.SOCR.analyses.exception.*;
import edu.ucla.stat.SOCR.distributions.StudentDistribution;
import edu.ucla.stat.SOCR.util.Matrix;
import edu.ucla.stat.SOCR.util.AnalysisUtility;
import java.util.*;
import edu.ucla.stat.SOCR.analyses.data.DataType;

public class LogisticRegression implements Analysis {

    private static final int NUMBER_OF_Y_VAR = 1;

    private static final String INTERCEPT = "INTERCEPT";

    private static final String X_DATA_TYPE = DataType.QUANTITATIVE;

    private static final String Y_DATA_TYPE = DataType.QUANTITATIVE;

    private String type = "LogisticRegression";

    private ArrayList<String> varNameList = new ArrayList<String>();

    private String[] varList = null;

    public String getAnalysisType() {
        return type;
    }

    public Result analyze(Data data, short analysisType) throws WrongAnalysisException, DataIsEmptyException {
        if (analysisType != AnalysisType.LOGISTIC_REGRESSION) throw new WrongAnalysisException();
        HashMap<String, Object> xMap = data.getMapX();
        HashMap<String, Object> yMap = data.getMapY();
        if (xMap == null || yMap == null) throw new WrongAnalysisException();
        Set<String> keySet = xMap.keySet();
        Iterator<String> iterator = keySet.iterator();
        String keys = "";
        ArrayList<Object> x = new ArrayList<Object>();
        double y[] = null;
        int xIndex = 0;
        varNameList.add(0, INTERCEPT);
        Column xColumn = null;
        while (iterator.hasNext()) {
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
            double xVector[] = xColumn.getDoubleArray();
            for (int i = 0; i < xVector.length; i++) {
            }
            x.add(xIndex, xVector);
            xIndex++;
            varNameList.add(xIndex, keys);
        }
        keySet = yMap.keySet();
        iterator = keySet.iterator();
        while (iterator.hasNext()) {
            keys = (String) iterator.next();
            try {
                Class cls = keys.getClass();
            } catch (Exception e) {
            }
        }
        Column yColumn = (Column) yMap.get(keys);
        String yDataType = yColumn.getDataType();
        if (!yDataType.equalsIgnoreCase(Y_DATA_TYPE)) {
            throw new WrongAnalysisException("\ny data type MUST be QUANTITATIVE but the input is of type " + yDataType);
        }
        y = yColumn.getDoubleArray();
        for (int i = 0; i < y.length; i++) {
        }
        varList = new String[varNameList.size()];
        for (int i = 0; i < varNameList.size(); i++) {
            varList[i] = (String) varNameList.get(i);
        }
        return regression(x, y);
    }

    private LogisticRegressionResult regression(ArrayList<Object> x, double[] y) throws DataIsEmptyException {
        HashMap<String, Object> texture = new HashMap<String, Object>();
        LogisticRegressionResult result = new LogisticRegressionResult(texture);
        int sampleSize = ((double[]) (x.get(0))).length;
        int numberOfX = x.size();
        int numberColumns = numberOfX + 1;
        double[][] varX = new double[sampleSize][numberColumns];
        double[][] vectorY = new double[sampleSize][NUMBER_OF_Y_VAR];
        for (int i = 0; i < sampleSize; i++) {
            varX[i][0] = 1;
            vectorY[i][0] = y[i];
        }
        double[] meanX = new double[numberColumns];
        meanX[0] = 1;
        for (int j = 1; j < numberColumns; j++) {
            double[] rowX = (double[]) (x.get(j - 1));
            for (int i = 0; i < sampleSize; i++) {
                double entryX = rowX[i];
                varX[i][j] = entryX;
            }
            meanX[j] = AnalysisUtility.mean(rowX);
        }
        Matrix matrixY = new Matrix(sampleSize, 1, vectorY);
        Matrix matrixX = new Matrix(sampleSize, numberColumns, varX);
        Matrix xTranspose = matrixX.transpose();
        Matrix identityMatrix = new Matrix(numberColumns, numberColumns, 'I');
        for (int i = 0; i < numberColumns; i++) {
            for (int j = 0; j < numberColumns; j++) {
                if (i == j) identityMatrix.element[i][j] = 1;
            }
        }
        Matrix beta = new Matrix(numberColumns, 1);
        Matrix productXX = new Matrix(numberColumns, numberColumns);
        productXX = Matrix.multiply(xTranspose, matrixX);
        for (int i = 0; i < numberColumns; i++) {
            for (int j = 0; j < numberColumns; j++) {
            }
        }
        Matrix inverseProductXX = new Matrix(numberColumns, numberColumns);
        inverseProductXX = AnalysisUtility.inverse(productXX);
        double temp = 0;
        for (int i = 0; i < numberColumns; i++) {
            for (int j = 0; j < numberColumns; j++) {
                temp = inverseProductXX.element[i][j];
            }
        }
        beta = Matrix.multiply(inverseProductXX, Matrix.multiply(xTranspose, matrixY));
        double betaEntry[][] = new double[numberColumns][1];
        double betaArray[] = new double[numberColumns];
        betaEntry = beta.element;
        for (int i = 0; i < numberColumns; i++) {
            betaArray[i] = beta.element[i][0];
        }
        Matrix hat = Matrix.multiply(Matrix.multiply(matrixX, inverseProductXX), xTranspose);
        Matrix predictedY = Matrix.multiply(hat, matrixY);
        Matrix residual = Matrix.subtract(matrixY, predictedY);
        double[] predicted = new double[sampleSize];
        double[] residuals = new double[sampleSize];
        double rss = 0;
        for (int i = 0; i < sampleSize; i++) {
            predicted[i] = predictedY.element[i][0];
            residuals[i] = residual.element[i][0];
            rss += residuals[i] * residuals[i];
        }
        double estVar = rss / (sampleSize - numberColumns);
        Matrix estCovBeta = Matrix.multiply(estVar, inverseProductXX);
        double[] estVarBeta = new double[estCovBeta.rows];
        double[] seBeta = new double[estCovBeta.rows];
        for (int i = 0; i < estCovBeta.rows; i++) {
            for (int j = 0; j < estCovBeta.columns; j++) {
                if (i == j) {
                    estVarBeta[i] = estCovBeta.element[i][j];
                    seBeta[i] = Math.sqrt(estVarBeta[i]);
                }
            }
        }
        texture.put(LogisticRegressionResult.VARIABLE_LIST, varList);
        texture.put(LogisticRegressionResult.BETA, betaArray);
        texture.put(LogisticRegressionResult.BETA_SE, seBeta);
        int betaLength = varNameList.size();
        StudentDistribution tDistribution = new StudentDistribution(sampleSize - numberOfX - 1);
        double tStatBeta[] = new double[betaLength];
        double pValue[] = new double[betaLength];
        for (int i = 0; i < betaLength; i++) {
        }
        for (int i = 0; i < betaLength; i++) {
            tStatBeta[i] = betaArray[i] / seBeta[i];
            pValue[i] = 2 * (1 - tDistribution.getCDF(Math.abs(tStatBeta[i])));
        }
        HashMap<String, Object> residualMap = AnalysisUtility.getResidualNormalQuantiles(residuals, residuals.length - betaArray.length);
        double[] sortedResiduals = (double[]) residualMap.get(LogisticRegressionResult.SORTED_RESIDUALS);
        int[] sortedResidualsIndex = (int[]) residualMap.get(LogisticRegressionResult.SORTED_RESIDUALS_INDEX);
        double[] sortedNormalQuantiles = (double[]) residualMap.get(LogisticRegressionResult.SORTED_NORMAL_QUANTILES);
        for (int i = 0; i < sortedResiduals.length; i++) {
        }
        double varPredicted = AnalysisUtility.sampleVariance(predicted);
        double varResiduals = AnalysisUtility.sampleVariance(residuals);
        double rSquare = varPredicted / (varResiduals + varPredicted);
        int dfError = residuals.length - betaArray.length;
        texture.put(LogisticRegressionResult.DF_ERROR, new Integer(dfError));
        texture.put(LogisticRegressionResult.R_SQUARE, new Double(rSquare));
        texture.put(LogisticRegressionResult.BETA_T_STAT, tStatBeta);
        texture.put(LogisticRegressionResult.BETA_P_VALUE, pValue);
        texture.put(LogisticRegressionResult.PREDICTED, predicted);
        texture.put(LogisticRegressionResult.RESIDUALS, residuals);
        texture.put(LogisticRegressionResult.SORTED_RESIDUALS, sortedResiduals);
        texture.put(LogisticRegressionResult.SORTED_RESIDUALS_INDEX, sortedResidualsIndex);
        texture.put(LogisticRegressionResult.SORTED_NORMAL_QUANTILES, sortedNormalQuantiles);
        return result;
    }
}
