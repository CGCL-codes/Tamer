package DE.FhG.IGD.semoa.audit;

import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.service.*;
import DE.FhG.IGD.util.*;
import java.security.cert.CertificateException;
import java.security.*;
import java.util.*;
import java.sql.*;
import java.lang.*;
import java.io.*;

/**
 * A filter that gives information about the departure of the current agent.
 * 
 * @author Elie Atallah, changes Roger Hartmann
 * @version "$Id: DepartureFilter.java 622 2002-04-18 19:11:55Z upinsdor $"
 */
public class DepartureFilter extends AbstractService implements AgentFilter.Out {

    private Properties props_ = null;

    private boolean propsSet_ = false;

    private DBCon dbc_ = null;

    private String tmpDir_ = null;

    private boolean tmpDirSet_ = false;

    private String rtmpDir_ = null;

    private boolean rtmpDirSet_ = false;

    /**
     * The directory where to store the zipped agent files
     * before they are stored in the database.
     *
     * @see #setRTmpDir
     */
    public void setTmpDir(String s) {
        if (tmpDirSet_ == true) {
            throw new IllegalStateException("TmpDir: set- or get- method already called!");
        }
        tmpDir_ = s;
        tmpDirSet_ = true;
    }

    /**
     * The directory on the database server where agent files
     * are stored. It must point to the same directory as setTmpDir().
     * The value, however might not be the same.
     * <p>
     * Lets say the semoa server runs on windoze and the user's home
     * directory can be accessed by <code>u:\</code>. You could
     * define <code>setTmpDir("u:\tmp")</code> and
     * <code>setRTmpDir("/home/user/tmp")</code> assuming that both
     * are actually the same directory.
     * <br>This is important because the agent files a stored using
     * the DB server's local filesystem.
     */
    public void setRTmpDir(String s) {
        if (rtmpDirSet_ == true) {
            throw new IllegalStateException("Remote TmpDir: set- or get- method already called!");
        }
        rtmpDir_ = s;
        rtmpDirSet_ = true;
    }

    protected Properties getProperties() {
        if (props_ == null) {
            throw new NullPointerException("Properties: not set! Ensure " + "that setter method is called before!");
        }
        propsSet_ = true;
        return props_;
    }

    /**
     * Example properties:<code><br>
     * #properties for the semoa audit package.
     * dbDrv=org.postgresql.Driver<br>
     * dbURL=jdbc:postgresql://aldebaran:5432/semoa<br>
     * dbUser=semoa<br>
     * dbPassWD=audit<br></code>
     * <br>
     * All these Properties <b>must</b> be defined, otherwise an
     * IOException is thrown.
     *
     * @exception IllegalArgumentException when
     *   the Properties file is incomplete.
     * @exception IOException when an I/O error occured.
     */
    public void setProperties(File f) throws IllegalArgumentException, IOException {
        if (propsSet_ == true) {
            throw new IllegalStateException("Properties: set- or get- method already called!");
        }
        FileInputStream fis;
        Properties p;
        String s;
        fis = new FileInputStream(f);
        p = new Properties();
        p.load(fis);
        dbc_ = new DBCon(p);
        props_ = p;
        propsSet_ = true;
    }

    public String info() {
        return "This filter disposes information " + "about the agent and its context.";
    }

    public String author() {
        return "Elie Atallah";
    }

    public String revision() {
        return "$Revision: 622 $/$Date: 2002-04-18 15:11:55 -0400 (Thu, 18 Apr 2002) $";
    }

    public DepartureFilter() {
    }

    public ErrorCode filter(AgentContext ctx) {
        if (props_ == null || tmpDir_ == null) {
            System.err.println("DepatureFilter not initialized! Ensure that setProperties " + "and setTmpDir are called! No Filtering done!");
            return ErrorCode.OK;
        }
        AgentCard card;
        String agentsSeq = null, timesSeq = null;
        Timestamp arrival, start, stop, departure, ts;
        int aseq = 0, tseq = 0;
        Resource res;
        File zipFile = null;
        Map map;
        FileOutputStream fos = null;
        card = (AgentCard) ctx.get(FieldType.CARD);
        ts = new Timestamp(System.currentTimeMillis());
        if (AgentCard.ROOT.equals(card)) {
            throw new SecurityException("Agent has the ROOT card!");
        }
        try {
            zipFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".zip", new File(tmpDir_));
            fos = new FileOutputStream(zipFile);
        } catch (IOException e) {
            System.out.println("ERROR: cannot open the FileOutputStream" + e);
            e.printStackTrace();
        }
        try {
            map = (Properties) ctx.get(FieldType.PROPERTIES);
            res = (Resource) ctx.get(FieldType.RESOURCE);
            Resources.zip(res, fos);
            agentsSeq = (String) map.get((String) "Agentskey");
            aseq = Integer.parseInt(agentsSeq);
            map.put((String) "departuretime", (Timestamp) ts);
            departure = (Timestamp) map.get((String) "departuretime");
            TimesTable timesTable = new TimesTable(dbc_);
            timesTable.update("departure", departure, aseq);
            CorpusTable corpusTable = new CorpusTable(dbc_);
            corpusTable.write(aseq, new File(rtmpDir_, zipFile.getName()).toString(), ts);
        } catch (Exception e) {
            System.out.println("Error at writing into the DataBase in Departure Filter: " + e.toString());
            String[] exception = getExceptionInfo(e);
            ExceptionsTable exTable = new ExceptionsTable(dbc_);
            exTable.write(exception[0], exception[1], exception[2], ts, aseq);
        }
        return ErrorCode.OK;
    }

    public static String[] getExceptionInfo(Throwable exi) {
        String[] exception = new String[3];
        exception[0] = exception[1] = exception[2] = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            exi.printStackTrace(ps);
            System.out.println("The baos: " + baos.toString());
            exception[2] = baos.toString();
        } catch (Exception e) {
            System.out.println("Cannot write the StackTrace into the printStream: " + e);
            e.printStackTrace();
        }
        exception[0] = exi.getClass().getName();
        exception[1] = exi.toString();
        return exception;
    }
}
