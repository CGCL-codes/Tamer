package jmathlib.toolbox.io;

import jmathlib.core.tokens.Token;
import jmathlib.core.tokens.OperandToken;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import jmathlib.core.tokens.CharToken;
import jmathlib.core.functions.ExternalFunction;
import jmathlib.core.interpreter.ErrorLogger;
import jmathlib.core.interpreter.Errors;
import jmathlib.core.interpreter.GlobalValues;
import java.io.*;

/**An external function for loading a matrix from a csv file*/
public class csvwrite extends ExternalFunction {

    /** Check that the operand is a string then open the file                
	   referenced.                                                          
       @param operands[0] = string which specifies the csv file to write    
       @param operands[1] = the matrix to save    
       @param operands[2] = the start row (optional)
       @param operands[3] = the start column (optional)*/
    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        if (getNArgIn(operands) != 2) throwMathLibException("CSVWrite: number of arguments != 2");
        if (operands[0] instanceof CharToken) {
            if (operands[1] instanceof DoubleNumberToken) {
                double[][] values = ((DoubleNumberToken) operands[1]).getReValues();
                String fileName = ((CharToken) operands[0]).toString();
                File CSVFile = new File(globals.getWorkingDirectory(), fileName);
                ErrorLogger.debugLine("Writing CSV>" + fileName + "<");
                int startLine = 0;
                int startColumn = 0;
                if (operands.length > 2) {
                    if (operands[2] instanceof DoubleNumberToken) startLine = ((DoubleNumberToken) operands[2]).getIntValue(0, 0);
                    if (operands.length > 3) {
                        if (operands[3] instanceof DoubleNumberToken) startColumn = ((DoubleNumberToken) operands[3]).getIntValue(0, 0);
                    }
                }
                try {
                    BufferedWriter outWriter = new BufferedWriter(new FileWriter(CSVFile));
                    try {
                        for (int row = 0; row < startLine; row++) {
                            outWriter.newLine();
                        }
                        for (int row = 0; row < values.length; row++) {
                            String line = "";
                            for (int column = 0; column < startColumn; column++) {
                                line = line + " ";
                            }
                            for (int column = 0; column < values[row].length; column++) {
                                line = line + values[row][column] + ",";
                            }
                            outWriter.write(line, 0, line.length() - 1);
                            outWriter.newLine();
                        }
                    } catch (Exception e) {
                        ErrorLogger.debugLine("CSVRead: load function exception - " + e.getMessage());
                    }
                    outWriter.close();
                } catch (Exception e) {
                    ErrorLogger.debugLine("CSVRead: load function exception - " + e.getMessage());
                }
            } else Errors.throwMathLibException(ERR_INVALID_PARAMETER, new Object[] { "DoubleNumberToken", operands[1].getClass().getName() });
        } else Errors.throwMathLibException(ERR_INVALID_PARAMETER, new Object[] { "CharToken", operands[0].getClass().getName() });
        return null;
    }
}
