package org.opennms.netmgt.rrd;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.web.Util;

/**
 * @author brozow
 * 
 * FIXME To change the template for this generated type comment go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
public class JniRrdImpl implements RrdImpl {

    /**
     *  
     */
    public JniRrdImpl() {
    }

    public void initialize() throws RrdException {
        Category log = ThreadCategory.getInstance(getClass());
        try {
            org.opennms.netmgt.rrd.Interface.init();
        } catch (SecurityException se) {
            if (log.isEnabledFor(Priority.FATAL)) log.fatal("initialize: Failed to initialize JNI RRD interface", se);
            throw new UndeclaredThrowableException(se);
        } catch (UnsatisfiedLinkError ule) {
            if (log.isEnabledFor(Priority.FATAL)) log.fatal("initialize: Failed to initialize JNI RRD interface", ule);
            throw new UndeclaredThrowableException(ule);
        }
    }

    public boolean createRRD(String creator, String directory, String dsName, int step, String dsType, int dsHeartbeat, String dsMin, String dsMax, List rraList) throws RrdException {
        Category log = ThreadCategory.getInstance(getClass());
        String fileName = dsName + ".rrd";
        if (log.isDebugEnabled()) log.debug("createRRD: rrd path and file name to create: " + directory + File.separator + fileName);
        File f = new File(directory);
        if (!f.isDirectory()) if (!f.mkdirs()) throw new RuntimeException("Unable to create RRD repository directory: " + directory);
        String completePath = directory + File.separator + fileName;
        f = new File(completePath);
        if (f.exists()) {
            return false;
        } else {
            String cmdPrefix = "create " + completePath + " --step " + step;
            String cmdDS = " DS:" + dsName + ":" + dsType + ":" + dsHeartbeat + ":" + dsMin + ":" + dsMax;
            String cmdRRA = new String();
            Iterator j = rraList.iterator();
            while (j.hasNext()) {
                String rra = (String) j.next();
                cmdRRA = cmdRRA.concat(" " + rra);
            }
            String cmd = cmdPrefix + cmdDS + cmdRRA;
            if (log.isDebugEnabled()) log.debug("createRRD: issuing RRD create command: " + cmd);
            String[] results = Interface.launch(cmd);
            if (results == null) {
                if (log.isEnabledFor(Priority.ERROR)) {
                    log.error("createRRD: Unexpected failure calling native method launch() with command string: " + cmd);
                    log.error("createRRD: No error text available.");
                }
                throw new RuntimeException("RRD database 'create' failed for " + creator);
            }
            if (results[0] != null) {
                if (log.isEnabledFor(Priority.ERROR)) log.error("RRD database 'create' failed for " + creator + ", reason: " + results[0]);
                throw new RuntimeException("RRD database 'create' failed for " + creator + ", reason: " + results[0]);
            }
            return true;
        }
    }

    public void updateRRD(String owner, String repositoryDir, String dsName, String val) throws RrdException {
        Category log = ThreadCategory.getInstance(getClass());
        String rrdFile = repositoryDir + File.separator + dsName + ".rrd";
        String cmd = "update " + rrdFile + " N:" + val;
        if (log.isDebugEnabled()) log.debug("updateRRD: Issuing RRD update command: " + cmd);
        String[] results = Interface.launch(cmd);
        if (log.isDebugEnabled()) log.debug("updateRRD: RRD update command completed.");
        if (results == null) {
            throw new RrdException("Unexpected failure calling native method launch() with command string: " + cmd);
        }
        if (results[0] != null) {
            throw new RrdException("RRD database 'update' failed for " + owner + ", reason: " + results[0]);
        }
    }

    public Double fetchLastValue(String rrdFile, int interval) throws NumberFormatException, RrdException {
        Category log = ThreadCategory.getInstance(getClass());
        String fetchCmd = "fetch " + rrdFile + " AVERAGE -s now-" + interval / 1000 + " -e now-" + interval / 1000;
        if (log.isDebugEnabled()) log.debug("fetch: Issuing RRD command: " + fetchCmd);
        String[] fetchStrings = Interface.launch(fetchCmd);
        if (fetchStrings == null) {
            if (log.isEnabledFor(Priority.ERROR)) {
                log.error("fetch: Unexpected error issuing RRD 'fetch' command, no error text available.");
            }
            return null;
        }
        if (fetchStrings[0] != null) {
            if (log.isEnabledFor(Priority.ERROR)) {
                log.error("fetch: RRD database 'fetch' failed, reason: " + fetchStrings[0]);
            }
            return null;
        }
        if (fetchStrings[1] == null || fetchStrings[2] == null) {
            if (log.isEnabledFor(Priority.ERROR)) {
                log.error("fetch: RRD database 'fetch' failed, no data retrieved.");
            }
            return null;
        }
        String dsName = fetchStrings[1].trim();
        Double dsValue = null;
        if (fetchStrings[2].trim().equalsIgnoreCase("nan")) {
            dsValue = new Double(Double.NaN);
        } else {
            try {
                dsValue = new Double(fetchStrings[2].trim());
            } catch (NumberFormatException nfe) {
                if (log.isEnabledFor(Priority.WARN)) log.warn("fetch: Unable to convert fetched value (" + fetchStrings[2].trim() + ") to Double for data source " + dsName);
                throw nfe;
            }
        }
        if (log.isDebugEnabled()) log.debug("fetch: fetch successful: " + dsName + "= " + dsValue);
        return dsValue;
    }

    public InputStream createGraph(String command, File workDir) throws IOException, RrdException {
        InputStream tempIn;
        String[] commandArray = Util.createCommandArray(command, '@');
        Process process = Runtime.getRuntime().exec(commandArray, null, workDir);
        ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
        BufferedInputStream in = new BufferedInputStream(process.getInputStream());
        Util.streamToStream(in, tempOut);
        in.close();
        tempOut.close();
        BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line = err.readLine();
        StringBuffer buffer = new StringBuffer();
        while (line != null) {
            buffer.append(line);
            line = err.readLine();
        }
        if (buffer.length() > 0) {
            throw new RrdException(buffer.toString());
        }
        byte[] byteArray = tempOut.toByteArray();
        tempIn = new ByteArrayInputStream(byteArray);
        return tempIn;
    }
}
