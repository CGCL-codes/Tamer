package org.hsqldb;

import java.io.IOException;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.IntKeyHashMap;
import org.hsqldb.lib.StopWatch;
import org.hsqldb.scriptio.ScriptReaderBase;

/**
 * Restores the state of a Database instance from an SQL script. <p>
 *
 * The script file may be in one of several allowed encodings, currently
 * including plain text, binary format and compressed binary format.
 *
 * @author fredt@users
 * @version 1.7.2
 */
public class ScriptRunner {

    /**
     *  This is used to read the *.log file and manage any necessary
     *  transaction rollback.
     *
     * @throws  HsqlException
     */
    public static void runScript(Database database, String scriptFilename, int logType) throws HsqlException {
        try {
            if (database.isFilesInJar()) {
                if (ScriptRunner.class.getClassLoader().getResource(scriptFilename) == null) {
                    return;
                }
            } else if (!FileUtil.exists(scriptFilename)) {
                return;
            }
        } catch (IOException e) {
        }
        IntKeyHashMap sessionMap = new IntKeyHashMap();
        Session sysSession = database.getSessionManager().getSysSession();
        Session current = sysSession;
        database.setReferentialIntegrity(false);
        try {
            StopWatch sw = new StopWatch();
            ScriptReaderBase scr = ScriptReaderBase.newScriptReader(database, scriptFilename, logType);
            while (true) {
                String s = scr.readLoggedStatement();
                if (s == null) {
                    break;
                }
                if (s.startsWith("/*C")) {
                    int id = Integer.parseInt(s.substring(3, s.indexOf('*', 4)));
                    current = (Session) sessionMap.get(id);
                    if (current == null) {
                        current = database.getSessionManager().newSession(database, sysSession.getUser(), false);
                        sessionMap.put(id, current);
                    }
                    s = s.substring(s.indexOf('/', 1) + 1);
                }
                if (s.length() != 0 && !current.isClosed()) {
                    Result result = current.sqlExecuteDirectNoPreChecks(s);
                    if (result != null && result.iMode == ResultConstants.ERROR) {
                        Trace.printSystemOut("error in " + scriptFilename + " line: " + scr.getLineNumber());
                        Trace.printSystemOut(result.getMainString());
                    }
                }
            }
            scr.close();
            database.getSessionManager().closeAllSessions();
        } catch (IOException e) {
            throw Trace.error(Trace.FILE_IO_ERROR, Trace.Generic_reading_file_error, new Object[] { scriptFilename, e });
        }
        database.setReferentialIntegrity(true);
    }
}
