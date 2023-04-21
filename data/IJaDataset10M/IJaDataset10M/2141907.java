package org.ideenmanufaktur.games.threedriving.data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * SQL Script to statements
 * 
 * @author Juergen
 */
public final class SQLScriptToStatement {

    private static final Logger log = Logger.getLogger(SQLScriptToStatement.class);

    /**
     * SQL Script to statements
     * 
     * @param scriptStream Input Stream of Script
     * @param delimiter delimiter to use
     * @return an array of statements
     */
    public static final String[] script2Statement(InputStream scriptStream, String delimiter) {
        String script = null;
        ArrayList<String> strings = new ArrayList<String>();
        try {
            if (null == scriptStream) {
                SQLScriptToStatement.log.fatal("InputStream creation failed!");
                return null;
            }
            BufferedInputStream sqlFIS = new BufferedInputStream(scriptStream);
            StringBuffer buffer = new StringBuffer();
            int b = 0;
            while ((b = sqlFIS.read()) != -1) {
                buffer.append((char) b);
            }
            buffer.trimToSize();
            script = buffer.toString();
            sqlFIS.close();
            scriptStream.close();
        } catch (NullPointerException e) {
            SQLScriptToStatement.log.fatal("Cannot read script file", e);
            return null;
        } catch (IOException e) {
            SQLScriptToStatement.log.fatal("Cannot I/O script file", e);
            return null;
        }
        String[] r = script.split(delimiter);
        for (int i = 0; i < r.length; i++) {
            strings.add(r[i].trim());
        }
        String[] sql = new String[strings.size()];
        int i = 0;
        for (Iterator<String> it = strings.iterator(); it.hasNext(); ) {
            sql[i++] = it.next();
        }
        return sql;
    }
}
