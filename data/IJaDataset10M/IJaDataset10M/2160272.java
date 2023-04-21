package srcp.client;

import java.io.*;
import java.lang.*;
import java.util.*;

/** Well formatted output of validation test results is done by this class.
  * Data is presented in XML style.
  *
  * @author  Olaf Schlachter
  * @version $Revision: 123 $
  */
public class ResultOutput extends Object {

    protected PrintStream objOutput;

    /** Uses System.out as the default output stream. */
    public ResultOutput() {
        objOutput = System.out;
    }

    /** Write a begin tag with the given name. */
    public void beginTag(String strTag) {
        objOutput.println("<" + strTag + ">");
    }

    /** Write an end tag with the given name. */
    public void endTag(String strTag) {
        objOutput.println("</" + strTag + ">");
    }

    /** Write the result PASSED or FAILED */
    public void displayResult(boolean blnResult) {
        beginTag("Result");
        if (blnResult) {
            objOutput.println("PASSED");
        } else {
            objOutput.println("FAILED");
        }
        endTag("Result");
    }

    public void writeHeader() {
        beginTag("?xml version=\"1.0\"?");
        beginTag("!--Created by srcp.client.ResultOutput--");
    }
}
