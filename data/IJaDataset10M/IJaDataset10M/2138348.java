package com.trapezium.edit;

import com.trapezium.vorlon.LintVisitor;
import com.trapezium.vorlon.ErrorInfo;
import com.trapezium.factory.FactoryResponseListener;
import com.trapezium.parse.TokenEnumerator;
import com.trapezium.parse.LineError;
import com.trapezium.vrml.node.Node;
import com.trapezium.vrml.node.DEFUSENode;
import com.trapezium.chisel.gui.Slider;
import com.trapezium.chisel.Chisel;
import java.util.BitSet;
import java.util.Vector;

/**
 *  Extends LintVisitor to track chisel specific information.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.1, 9 Jan 1998
 *
 *  @since           1.1
 */
public class EditLintVisitor extends LintVisitor implements LineError {

    int inlineCount = 0;

    public EditLintVisitor(TokenEnumerator dataSource) {
        super(dataSource);
    }

    /** Part of LineError interface, get the number of errors on the
     *  indicated line.
     */
    public int getErrorCount(int lineNo, boolean includeWarnings) {
        ErrorInfo ei = getErrorInfo(lineNo);
        if (ei == null) {
            return (0);
        } else {
            return (ei.getNumberErrors(includeWarnings));
        }
    }

    /** Part of LineError interface, get the line number of the next line
     *  with an error.
     *
     *  @param lineNo start searching after this line
     *
     *  @return the next line number with an error, -1 if none
     */
    public int getNextError(int lineNo) {
        TokenEnumerator lineSource = getLineSource();
        int numberLines = lineSource.getNumberLines();
        lineNo++;
        while (lineNo < numberLines) {
            if (getErrorCount(lineNo, Chisel.includeWarningsInSearch) > 0) {
                return (lineNo);
            }
            lineNo++;
        }
        return (-1);
    }

    public int getPrevError(int lineNo) {
        lineNo--;
        while (lineNo >= 0) {
            if (getErrorCount(lineNo, Chisel.includeWarningsInSearch) > 0) {
                return (lineNo);
            }
            lineNo--;
        }
        return (-1);
    }

    /** LineError interface, get an error string for display in viewer.
     *
     *  @param lineNo the line number associated with the error
     *  @param errorStringNo offset in list of errors associated with line
     */
    public String getErrorViewerString(int lineNo, int errorStringNo) {
        ErrorInfo actualError = getActualError(lineNo, errorStringNo);
        if (actualError == null) {
            return (null);
        } else {
            return (actualError.getDisplayString());
        }
    }

    /** LineError interface, get an error string for display on status line
     *
     *  @param lineNo the line number associated with the error
     *  @param errorStringNo offset in list of errors associated with line
     */
    public String getErrorStatusString(int lineNo, int errorStringNo) {
        ErrorInfo actualError = getActualError(lineNo, errorStringNo);
        if (actualError == null) {
            return (null);
        } else {
            return (actualError.getStatusString());
        }
    }

    ErrorInfo getActualError(int lineNo, int errorStringNo) {
        ErrorInfo ei = getErrorInfo(lineNo);
        if (ei == null) {
            return (null);
        } else {
            return (ei.getError(errorStringNo));
        }
    }

    public int getInlineCount() {
        return (inlineCount);
    }

    public void setInlineCount(int n) {
        inlineCount = n;
    }

    int firstErrorLine = -1;

    int lastErrorLine = -1;

    public void mergeErrors(FactoryResponseListener frl, BitSet errorBits, BitSet warningBits, BitSet nonconformanceBits) {
        TokenEditor editSource;
        if (dataSource instanceof TokenEditor) {
            editSource = (TokenEditor) dataSource;
        } else {
            return;
        }
        editSource.setLineError(this);
        int numberLines = dataSource.getNumberLines();
        if (numberLines < 25) {
            numberLines = 25;
        }
        int errorLine = getGreatestErrorLine(numberLines + 1);
        if (frl != null) {
            frl.setLinePercent(-10);
            frl.setAction("Merging errors ... ");
        }
        setErrorKeys();
        for (int i = errorLine; i > 0; i--) {
            if ((i % 100) == 0) {
                if (frl != null) {
                    frl.setLinePercent((numberLines - i) * 1000 / numberLines);
                }
            }
            if (i != errorLine) {
                continue;
            }
            ErrorInfo ei = getErrorInfo(i);
            if (ei != null) {
                if (lastErrorLine == -1) {
                    lastErrorLine = i;
                }
                firstErrorLine = i;
                while (ei != null) {
                    if ((errorBits != null) && (warningBits != null)) {
                        int newRange = (errorLine * Slider.ErrorMarkCount) / numberLines;
                        String estr = ei.getError();
                        if (estr.indexOf("Error") != -1) {
                            errorBits.set(newRange);
                        }
                        if (estr.indexOf("Warning") != -1) {
                            warningBits.set(newRange);
                        }
                        if (estr.indexOf("Nonconformance") != -1) {
                            nonconformanceBits.set(newRange);
                        }
                    }
                    ei = ei.getNextError();
                }
            }
            errorLine = getGreatestErrorLine(errorLine);
        }
        numberLines = dataSource.getNumberLines();
        if (frl != null) {
            frl.setLinePercent(-20);
        }
    }

    public int getFirstErrorLine() {
        return (firstErrorLine);
    }

    public int getLastErrorLine() {
        return (lastErrorLine);
    }

    public int getGreatestErrorLine(int lineNumber) {
        int greatestErrorLine = -1;
        if (errorList != null) {
            ErrorInfo scanner = errorList;
            while (scanner != null) {
                int scannerLineNumber = scanner.getLineNumber();
                if (scannerLineNumber > greatestErrorLine) {
                    if (scannerLineNumber < lineNumber) {
                        greatestErrorLine = scannerLineNumber;
                    }
                }
                scanner = scanner.getNextLink();
            }
        }
        return (greatestErrorLine);
    }
}
