package com.bluemarsh.jswat.command;

import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.*;

/**
 * Utility class for the CommandParser unit tests.
 *
 * @author Nathan Fiedler
 */
public class ParserHelper {

    /**
     * Runs the given command parser tests.
     *
     * @param  parser  command parser to which input is sent.
     * @param  datum   array of test data.
     */
    public void performTest(CommandParser parser, TestData[] datum) {
        for (TestData data : datum) {
            StringWriter sw = new StringWriter(80);
            PrintWriter pw = new PrintWriter(sw);
            parser.setOutput(pw);
            String result = null;
            try {
                parser.parseInput(data.getInput());
                result = sw.toString();
                if (data.shouldFail()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append(data.getInput());
                    buf.append(" <<should have failed -- result>> ");
                    buf.append(result);
                    fail(buf.toString());
                }
            } catch (CommandException ce) {
                if (!data.shouldFail()) {
                    StringBuilder buf = new StringBuilder();
                    buf.append(data.getInput());
                    buf.append(" <<should not have failed>> ");
                    sw = new StringWriter(256);
                    pw = new PrintWriter(sw);
                    ce.printStackTrace(pw);
                    buf.append(sw.toString());
                    fail(buf.toString());
                }
            } catch (Exception e) {
                StringBuilder buf = new StringBuilder();
                buf.append(data.getInput());
                buf.append(" <<unexpected exception>> ");
                sw = new StringWriter(256);
                pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                buf.append(sw.toString());
                fail(buf.toString());
            }
            boolean equals;
            if (result == null && data.getResult() == null) {
                equals = true;
            } else if (result == null || data.getResult() == null) {
                equals = false;
            } else {
                equals = result.equals(data.getResult());
            }
            if (!equals) {
                if (data.getMessage() == null) {
                    StringBuilder buf = new StringBuilder();
                    buf.append(data.getInput());
                    buf.append(" <<should have been>> ");
                    buf.append(data.getResult());
                    buf.append(" <<but got>> ");
                    buf.append(result);
                    fail(buf.toString());
                } else {
                    StringBuilder buf = new StringBuilder();
                    buf.append(data.getMessage());
                    buf.append(" <<expected>> ");
                    buf.append(data.getResult());
                    buf.append(" <<but got>> ");
                    buf.append(result);
                    fail(buf.toString());
                }
            }
        }
    }
}
