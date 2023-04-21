package org.apache.catalina.ssi;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

/**
 * Implements the Server-side #printenv command
 * 
 * @author Dan Sandberg
 * @author David Becker
 * @version $Revision: 531303 $, $Date: 2007-04-23 02:24:01 +0200 (Mon, 23 Apr 2007) $
 */
public class SSIPrintenv implements SSICommand {

    /**
     * @see SSICommand
     */
    public long process(SSIMediator ssiMediator, String commandName, String[] paramNames, String[] paramValues, PrintWriter writer) {
        long lastModified = 0;
        if (paramNames.length > 0) {
            String errorMessage = ssiMediator.getConfigErrMsg();
            writer.write(errorMessage);
        } else {
            Collection variableNames = ssiMediator.getVariableNames();
            Iterator iter = variableNames.iterator();
            while (iter.hasNext()) {
                String variableName = (String) iter.next();
                String variableValue = ssiMediator.getVariableValue(variableName);
                if (variableValue == null) {
                    variableValue = "(none)";
                }
                writer.write(variableName);
                writer.write('=');
                writer.write(variableValue);
                writer.write('\n');
                lastModified = System.currentTimeMillis();
            }
        }
        return lastModified;
    }
}
