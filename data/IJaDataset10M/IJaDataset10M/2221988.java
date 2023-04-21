package com.jcorporate.expresso.ext.dbobj;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 *
 *
 * Created on March 20, 2001, 9:00 AM
 * @version     $Revision: 3 $      $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 * @author Michael Nash
 */
public class URLReader extends Thread {

    private BufferedReader dis = null;

    private String expectString = null;

    private boolean succeeded = false;

    private String failureReason = "Timed Out";

    private String sessionId = null;

    private static Logger log = Logger.getLogger(URLReader.class);

    private boolean printOutput = false;

    private boolean requireSession = false;

    /**
     * Creates new URLReader
     *
     * @param   newDis a Data Input Stream
     * @param   newExpect String that is expected
     * @param newPrint should output be printed
     */
    public URLReader(InputStreamReader newDis, String newExpect, boolean newPrint) {
        dis = new BufferedReader(newDis);
        expectString = newExpect;
        printOutput = newPrint;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setRequireSession(boolean newRequireSession) {
        requireSession = newRequireSession;
    }

    /**
     *
     *
     * @return The reason for failure as java.lang.String
     */
    public String getFailureReason() {
        return failureReason;
    }

    /**
     *
     *
     * @return true if the test had completed successfully
     */
    public boolean completedSuccessfully() {
        return succeeded;
    }

    /**
     * Read from the specified URL, looking for the "ExpectString". If you find it,
     * return true, if not - return false
     */
    public void run() {
        boolean returnVal = false;
        StringBuffer wholeReply = new StringBuffer();
        if (dis == null) {
            failureReason = "No connection to server:DataInputStream dis is null";
            return;
        }
        try {
            int linesRead = 0;
            String OneLine = dis.readLine();
            if (OneLine == null) {
                failureReason = "Server never sent EOF";
                return;
            }
            while (OneLine != null) {
                if (printOutput) {
                    System.out.println(OneLine);
                }
                wholeReply.append(OneLine + " ");
                if (!OneLine.equals("")) {
                    linesRead++;
                }
                OneLine = dis.readLine();
                if (OneLine != null) {
                    if (OneLine.indexOf("jsessionid|") > 0) {
                        log.info("Found line with jsessionid:'" + OneLine + "'");
                        StringTokenizer stk = new StringTokenizer(OneLine, "|");
                        stk.nextToken();
                        if (stk.hasMoreTokens()) {
                            sessionId = stk.nextToken();
                            System.out.println("Session assigned:'" + sessionId + "'");
                        } else {
                            log.error("No second token found! Session info not coded correctly");
                        }
                    }
                }
            }
            if (wholeReply.toString().indexOf(expectString) > 0) {
                returnVal = true;
            }
            dis.close();
            if ((requireSession) && (sessionId == null)) {
                System.out.println("Never got session established. Output was:");
                System.out.println("-----Output Begins----");
                System.out.println(wholeReply.toString());
                System.out.println("-----Output Ends----");
            }
            if (!returnVal) {
                System.out.println("Expect string:'" + expectString + "'");
                failureReason = "Never received the expected string '" + expectString + "'. Got '";
                failureReason = failureReason + wholeReply.toString();
                failureReason = failureReason + "' instead. (" + linesRead + " lines read)";
                return;
            }
        } catch (IOException ie) {
            log.error("I/O Exception during test:" + ie);
            failureReason = "I/O Exception reading URL:" + ie.getMessage();
            succeeded = false;
            System.gc();
            return;
        }
        succeeded = true;
    }
}
