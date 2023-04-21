package ch.enterag.utils.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import ch.enterag.utils.logging.*;

/** This class parses the command line and makes the arguments
 * accessible as named options by name and unnamed arguments by
 * position.
 * In the name of simplicity this class does support switches that 
 * indicate a boolean value just by their presence.
 * @author Hartwig Thomas
 */
public class Arguments {

    /** Logger */
    private static IndentLogger m_il = IndentLogger.getIndentLogger(Arguments.class.getName());

    /** container of named options */
    private Map<String, String> m_mapOptions = null;

    /** get value of parsed option. 
   * @param sName name of option 
   * @return option value */
    public String getOption(String sName) {
        return m_mapOptions.get(sName);
    }

    /** container of unnamed arguments */
    private String[] m_asArgument = null;

    /** get value of unnamed argument. 
   * @param iPosition position of argument (0 based) 
   * @return argument value */
    public String getArgument(int iPosition) {
        return m_asArgument[iPosition];
    }

    /** @return number of unnamed arguments. */
    public int getArguments() {
        return m_asArgument.length;
    }

    /** error string */
    private String m_sError = null;

    /** @return syntax error on command line. */
    public String getError() {
        return m_sError;
    }

    /** constructor parses the command-line arguments
   * @param args command-line arguments.
   */
    public Arguments(String[] args) {
        m_il.enter((Object[]) args);
        m_il.fine("Arguments:");
        for (int i = 0; i < args.length; i++) m_il.fine("  " + String.valueOf(i) + ": " + args[i]);
        List<String> listArgument = new ArrayList<String>();
        m_mapOptions = new HashMap<String, String>();
        int iArgument = 0;
        while (iArgument < args.length) {
            String sArgument = args[iArgument];
            if (sArgument.startsWith("-") || (!File.separator.equals("/") && sArgument.startsWith("/"))) {
                int iPosition = 1;
                for (; ((iPosition < sArgument.length() && Character.isLetterOrDigit(sArgument.charAt(iPosition)))); iPosition++) {
                }
                if (iPosition > 1) {
                    String sName = sArgument.substring(1, iPosition);
                    String sValue = "";
                    if (iPosition < sArgument.length()) {
                        if ((sArgument.charAt(iPosition) == ':') || (sArgument.charAt(iPosition) == '=')) sValue = sArgument.substring(iPosition + 1); else m_sError = "Option " + sName + " must be terminated by colon, equals or blank!";
                    }
                    m_mapOptions.put(sName, sValue);
                } else m_sError = "Empty option encountered!";
            } else listArgument.add(args[iArgument]);
            iArgument++;
        }
        m_asArgument = listArgument.toArray(new String[] {});
        m_il.fine("Named arguments (options):");
        for (Iterator<String> iterOption = m_mapOptions.keySet().iterator(); iterOption.hasNext(); ) {
            String sKey = iterOption.next();
            String sValue = m_mapOptions.get(sKey);
            m_il.fine("  " + sKey + ": " + sValue);
        }
        m_il.fine("Unnamed arguments:");
        for (int i = 0; i < m_asArgument.length; i++) m_il.fine("  " + String.valueOf(i) + ": " + m_asArgument[i]);
        m_il.exit();
    }
}
