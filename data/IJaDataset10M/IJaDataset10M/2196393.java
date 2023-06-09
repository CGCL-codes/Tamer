package com.rapidminer.tools.math;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rapidminer.operator.OperatorException;

/**
 * This class provides functions to convert a matrix given in single
 * line Matlab format to standard matrix representation and vice versa. 
 * 
 * @author Helge Homburg
 */
public class StringToMatrixConverter {

    private static final String ROW_DELIMITER = ";";

    private static final String VALUE_DELIMITER = " ";

    /** creates a Matlab string from a given double matrix  */
    public static String createMatlabString(double[][] matrix) {
        StringBuffer matrixString = new StringBuffer();
        if (matrix == null) {
            return "";
        }
        matrixString.append("[");
        int numberOfColumns = matrix.length;
        int numberOfRows = matrix[0].length;
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns - 1; j++) {
                matrixString.append(matrix[i][j] + VALUE_DELIMITER);
            }
            matrixString.append(matrix[i][numberOfColumns - 1] + ROW_DELIMITER);
        }
        ;
        matrixString.deleteCharAt(matrixString.length() - 1);
        matrixString.append("]");
        return matrixString.toString();
    }

    /** parses a Matlab string to create a double matrix  */
    public static double[][] parseMatlabString(String matrixString) throws OperatorException {
        if (matrixString == null || matrixString.trim().length() == 0) return null;
        double[][] matrix;
        Pattern possibleRedundantWhitespace = Pattern.compile("\\s+");
        Matcher whitespaceMatcher = possibleRedundantWhitespace.matcher(matrixString);
        matrixString = whitespaceMatcher.replaceAll(" ");
        Pattern lineFeed = Pattern.compile("\\n");
        Matcher lineFeedMatcher = lineFeed.matcher(matrixString);
        matrixString = lineFeedMatcher.replaceAll("");
        matrixString = matrixString.trim();
        Pattern illegalChar = Pattern.compile("[^0-9\\-\\+\\.\\,\\; \\[\\]]");
        Matcher findIllegalChar = illegalChar.matcher(matrixString);
        if (findIllegalChar.find()) {
            throw new OperatorException("StringToMatrixConverter: Matlab String contains illegal characters, parsing failed.");
        }
        Pattern squareBrackets = Pattern.compile("[\\[\\]]");
        Matcher removeSquareBrackets = squareBrackets.matcher(matrixString);
        matrixString = removeSquareBrackets.replaceAll("");
        String usedDelimiter = VALUE_DELIMITER;
        if (matrixString.indexOf(",") < 0) {
            usedDelimiter = " ";
            matrixString = matrixString.trim();
        } else {
            Pattern space = Pattern.compile("\\s+");
            Matcher spaceMatcher = space.matcher(matrixString);
            matrixString = spaceMatcher.replaceAll("");
        }
        String[] matrixRows;
        try {
            matrixRows = matrixString.split(ROW_DELIMITER);
        } catch (Exception e) {
            throw new OperatorException("StringToMatrixConverter: Matlab String does not provide correct row separation, parsing failed.");
        }
        int numberOfRows = matrixRows.length;
        int numberOfValues = matrixRows[0].split(usedDelimiter).length;
        String[][] stringMatrix = new String[numberOfRows][];
        try {
            for (int i = 0; i < numberOfRows; i++) {
                matrixRows[i] = matrixRows[i].trim();
                String[] currentRow = matrixRows[i].split(usedDelimiter);
                if (currentRow.length != numberOfValues) {
                    throw new OperatorException("StringToMatrixConverter: Matlab String contains data rows of different length, parsing failed.");
                }
                stringMatrix[i] = currentRow;
            }
        } catch (Exception e) {
            if (e instanceof OperatorException) {
                throw (OperatorException) e;
            } else {
                throw new OperatorException("StringToMatrixConverter: Matlab String does not provide correct value separation, parsing failed.");
            }
        }
        matrix = new double[numberOfRows][numberOfValues];
        try {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfValues; j++) {
                    matrix[i][j] = Double.parseDouble(stringMatrix[i][j]);
                }
            }
        } catch (RuntimeException e) {
            throw new OperatorException("StringToMatrixConverter: Matlab String contains irregular values, all values must be integer or double literals. Parsing failed.");
        }
        return matrix;
    }
}
