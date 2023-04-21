package net.assimilator.qa.core;

import com.sun.jini.qa.harness.TestException;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class provides the capability of expanding variables in OpStrings.
 */
public class VariableExpander {

    /**
     * The logger used by this class.
     */
    private static Logger logger = Logger.getLogger("net.assimilator.qa.core");

    /**
     * Line separator string. This is the value of the line.separator
     * property at the moment that the SimpleFormatter was created.
     */
    private static final String lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    /**
     * The map of variables to be expanded.
     */
    private Map<String, String> variables = new HashMap<String, String>();

    /**
     * Constructs a <code>VariableExpander</code>.
     */
    public VariableExpander() {
    }

    /**
     * Adds a variable to be expanded.
     *
     * @param name  the variable name
     * @param value the value to expand to
     */
    public void addVariable(String name, String value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        variables.put(name, value);
    }

    /**
     * Expands variables in a given OpString. The method expands variables
     * added to this object with the <code>addVariable</code> method.
     * The result is stored in a temporary downloadable file created with
     * the <code>FileUtils.createDownloadableTempFile()</code> method.
     * It is the caller's repsonsibility to delete that file.
     *
     * @param opStringUrl the URL of a file containing an OpString
     *                    definition
     * @return the temporary downloadable file containing the result.
     *         It is the caller's repsonsibility to delete this file.
     * @throws IOException   if an I/O exception occurs
     * @throws TestException if the
     *                       <code>${net.assimilator.qa.core.tmpdldir}</code>
     *                       property is not specified
     */
    public FileUtils.DownloadableFile expand(URL opStringUrl) throws IOException, TestException {
        return expand(opStringUrl, variables);
    }

    /**
     * Expands variables in a given OpString. The method expands variables
     * specified by the <code>variables</code> parameter.
     * The result is stored in a temporary downloadable file created with
     * the <code>FileUtils.createDownloadableTempFile()</code> method.
     * It is the caller's repsonsibility to delete that file.
     *
     * @param opStringUrl the URL of a file containing an OpString
     *                    definition
     * @param variables   the map of variable names and values
     * @return the temporary downloadable file containing the result.
     *         It is the caller's repsonsibility to delete this file.
     * @throws IOException   if an I/O exception occurs
     * @throws TestException if the
     *                       <code>${net.assimilator.qa.core.tmpdldir}</code>
     *                       property is not specified
     */
    public static FileUtils.DownloadableFile expand(URL opStringUrl, Map<String, String> variables) throws IOException, TestException {
        StringBuffer in = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(opStringUrl.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            in.append(line).append(lineSeparator);
        }
        reader.close();
        StringBuffer out = new StringBuffer();
        Pattern p = Pattern.compile("\\$\\{(\\w+)}");
        Matcher m = p.matcher(in);
        while (m.find()) {
            String varName = m.group(1);
            String varValue = variables.get(varName);
            if (varValue != null) {
                m.appendReplacement(out, varValue);
            } else {
                logger.warning("No expansion for [" + varName + "] found");
                m.appendReplacement(out, "$0");
            }
        }
        m.appendTail(out);
        FileUtils.DownloadableFile df = FileUtils.createDownloadableTempFile();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(df.file)));
        writer.println(out);
        writer.close();
        return df;
    }
}
