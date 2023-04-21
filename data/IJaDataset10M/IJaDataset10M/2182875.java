package hu.sztaki.lpds.dcibridge.service;

import hu.sztaki.lpds.dcibridge.config.Conf;
import hu.sztaki.lpds.submitter.grids.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.ggf.schemas.bes._2006._08.bes_factory.ActivityStateEnumeration;

/**
 * @author krisztian karoczkai
 */
public class Base extends Thread {

    public static final String MIDDLEWARE_LOCAL = "local";

    public static final String MIDDLEWARE_GLITE = "glite";

    public static final String MIDDLEWARE_GT2 = "gt2";

    public static final String MIDDLEWARE_GT4 = "gt4";

    public static final String MIDDLEWARE_UNICORE = "unicore";

    public static final String MIDDLEWARE_ARC = "arc";

    public static final String MIDDLEWARE_LSF = "lsf";

    public static final String MIDDLEWARE_PBS = "pbs";

    public static final String MIDDLEWARE_SERVICE = "service";

    public static final String MIDDLEWARE_BOINC = "boinc";

    public static final String MIDDLEWARE_GEMLCA = "gemlca";

    public static final String MIDDLEWARE_GAE = "gae";

    public static final String MIDDLEWARE_SYSTEM = "system";

    public static final String MIDDLEWARE_GBAC = "gbac";

    public static final String MIDDLEWARE_EDGI = "edgi";

    public static final String MIDDLEWARE_CLOUDBROKER = "cloudbroker";

    public static final String[] dcis = new String[] { MIDDLEWARE_GLITE, MIDDLEWARE_GT2, MIDDLEWARE_GT4, MIDDLEWARE_UNICORE, MIDDLEWARE_ARC, MIDDLEWARE_LSF, MIDDLEWARE_PBS, MIDDLEWARE_SERVICE, MIDDLEWARE_BOINC, MIDDLEWARE_GEMLCA, MIDDLEWARE_GAE, MIDDLEWARE_LOCAL, MIDDLEWARE_GBAC, MIDDLEWARE_EDGI, MIDDLEWARE_CLOUDBROKER };

    public String path;

    private static Base instance = new Base();

    private ConcurrentHashMap<String, Job> jobs = new ConcurrentHashMap<String, Job>();

    private DownloadingThread dwlThread = new DownloadingThread();

    private UploadingThread uplThread = new UploadingThread();

    private ConcurrentHashMap<String, List<Middleware>> middlewares = new ConcurrentHashMap<String, List<Middleware>>();

    private boolean status = false;

    private ConcurrentHashMap<String, List<LB>> loggs = new ConcurrentHashMap<String, List<LB>>();

    private static ConcurrentHashMap<String, LB> jobloggs = new ConcurrentHashMap<String, LB>();

    /**
 * Constructor, creating and starting the threads, creating the working directory
 */
    public Base() {
        start();
    }

    @Override
    public void run() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            Base.writeLogg(Base.MIDDLEWARE_SYSTEM, new LB(e));
        }
        initWorkDir();
        dwlThread.start();
        uplThread.start();
        for (String t : dcis) {
            System.out.println(t);
            loggs.put(t, new CopyOnWriteArrayList<LB>());
        }
        loggs.put(MIDDLEWARE_SYSTEM, new CopyOnWriteArrayList<LB>());
        for (dci.data.Middleware t : Conf.getM().getMiddleware()) initMiddleware(t);
    }

    /**
 * Starting of the configured middleware plugin 
 * @param pMiddleware Middleware descriptor
 */
    public void initMiddleware(dci.data.Middleware pMiddleware) {
        int activeThreadCount = 0;
        if (pMiddleware.isEnabled()) {
            if (Base.getI().getMiddlewares(pMiddleware.getType()) != null) {
                activeThreadCount = Base.getI().getMiddlewares(pMiddleware.getType()).size();
                try {
                    Base.getI().getMiddlewares(pMiddleware.getType()).get(0).setConfiguration();
                } catch (Exception e) {
                    writeLogg(Base.getI().getLogg(pMiddleware.getType()), new LB(e));
                }
            }
            for (int i = activeThreadCount; i < pMiddleware.getThreads(); i++) {
                try {
                    createNewMiddlewarePlugininstance(pMiddleware.getType(), pMiddleware.getPlugin());
                } catch (Exception e) {
                    writeLogg(loggs.get(MIDDLEWARE_SYSTEM), new LB(e));
                }
            }
            writeLogg(loggs.get(MIDDLEWARE_SYSTEM), new LB("Enabled " + pMiddleware.getType() + " plugin"));
        } else writeLogg(loggs.get(MIDDLEWARE_SYSTEM), new LB("Disabled " + pMiddleware.getType() + " plugin"));
    }

    /**
 * Creation of a new instance of the middleware plugin 
 * @param pType
 * @param pClassName
 * @throws Exception: plugin instance can not be created
 */
    public void createNewMiddlewarePlugininstance(String pType, String pClassName) throws Exception {
        if (middlewares.get(pType) == null) middlewares.put(pType, new java.util.concurrent.CopyOnWriteArrayList<Middleware>());
        Middleware tmp = (Middleware) Class.forName(pClassName).newInstance();
        if (middlewares.get(pType).size() == 0) {
            try {
                tmp.setConfiguration();
            } catch (Exception e) {
                writeLogg(Base.getI().getLogg(pType), new LB(e));
            }
        }
        tmp.start();
        middlewares.get(pType).add(tmp);
        writeLogg(pType, new LB("created middleware plugin thread(" + tmp.getName() + ")"));
    }

    /**
 * Initialisation of the working directory
 */
    public void initWorkDir() {
        path = System.getProperty("java.io.tmpdir") + "/dci_bridge/";
        if (Conf.getS().getPath() == null) ; else if ("".equals(Conf.getS().getPath())) ; else path = Conf.getS().getPath();
        File f = new File(path);
        if (!f.exists()) f.mkdir();
    }

    /**
 * Query of DCI-Bridge state
 * @return true= ready to receive jobs
 */
    public boolean isStatus() {
        return status;
    }

    /**
 * Set of DCI-Bridge state 
 * @param status  true = ready to receive jobs
 */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
 * query of the threads handling the midleware plugins
 * @return The thread having the shortest queue of waiting job instances
 */
    public Middleware getMiddleware(String pValue) {
        Middleware res = null;
        long min = Long.MAX_VALUE;
        List<Middleware> tmp = middlewares.get(pValue);
        for (byte b = 0; b < middlewares.get(pValue).size(); b++) if (tmp.get(b).isState() && tmp.get(b).getSize() < min) {
            min = tmp.get(b).getSize();
            res = tmp.get(b);
        }
        return res;
    }

    /**
 * threads handling the middleware
 * @param pValue name of middleware
 * @return list of middleware handling thread instances
 */
    public List<Middleware> getMiddlewares(String pValue) {
        return middlewares.get(pValue);
    }

    /**
 * Service:  query of path of temporary files 
 * @return path
 */
    public String getPath() {
        return path;
    }

    /**
 * Service:   set of path of temporary files 
 * @param pValue  path of temporary files
 */
    public synchronized void setPath(String pValue) {
        path = pValue;
    }

    /**
 * Query of singleton object
 * @return singleton instance
 */
    public static Base getI() {
        return instance;
    }

    public boolean isDestroyedJob(String pID) {
        return jobs.get(pID) == null;
    }

    /**
 * booking of new job instance to be executed
 * @param pID generated unique internal job identifier
 * @param pValue Job descriptor
 */
    public void newJob(String pID, Job pValue) {
        jobs.put(pID, pValue);
        try {
            dwlThread.addJob(pValue);
        } catch (Exception e) {
            Base.writeLogg(Base.MIDDLEWARE_SYSTEM, new LB(e));
            pValue.setStatus(ActivityStateEnumeration.FAILED);
        }
    }

    /**
 * query of the thread for downloading input files 
 * @return instance thread for downloading input files 
 */
    public DownloadingThread getDWLThread() {
        return dwlThread;
    }

    /**
 * Uploading the result of a terminated job instance 
 * @param pValue Job descriptor
 */
    public void finishJob(Job pValue) {
        try {
            uplThread.addJob(pValue);
        } catch (Exception e) {
            pValue.setPubStatus(ActivityStateEnumeration.FAILED);
        }
    }

    private void deleteWorkDir(Job pJob) {
        File f = new File(Base.getI().getJobDirectory(pJob.getId()));
        if (f.exists()) deleteFile(f);
    }

    private void deleteFile(File pFile) {
        if (pFile.isDirectory()) {
            File[] i = pFile.listFiles();
            for (File f : i) deleteFile(f);
        }
        pFile.delete();
    }

    /**
 * Deleting job from the repository
 * @param pID generated unique internal job identifier
 */
    public void removeJob(String pID) {
        if (0 == Conf.getP().getDebug()) deleteWorkDir(getJob(pID));
        jobs.remove(pID);
    }

    /**
 * query of numbers of managed jobs
 * @return  number of jobs
 */
    public int getRunningJobs() {
        return jobs.size();
    }

    /**
 * query of job descriptor based of job identifier
 * @param pValue ID
 * @return Job descriptor
 */
    public Job getJob(String pValue) {
        return jobs.get(pValue);
    }

    /**
 * query of the working directory of the job
 * @param pValue generated unique internal job identifier
 * @return working directory
 */
    public String getJobDirectory(String pValue) {
        return path + pValue + "/";
    }

    public static synchronized void writeLogg(List<LB> pLogList, LB pLogg) {
        if (pLogList.size() > 100) pLogList.remove(0);
        pLogList.add(pLogg);
    }

    public static void writeLogg(String pLogListName, LB pLogg) {
        writeLogg(getI().loggs.get(pLogListName), pLogg);
    }

    public List<LB> getLogg(String pName) {
        if (loggs.get(pName) == null) loggs.put(pName, new CopyOnWriteArrayList<LB>());
        return loggs.get(pName);
    }

    public static void initLogg(String pKey, String pMsg) {
        LB tmp = new LB(pMsg);
        jobloggs.put(pKey, tmp);
    }

    public static void endJobLogg(Job pJob, short pLevel, String pInfo) {
        writeJobLogg(pJob.getId(), pLevel, pInfo);
    }

    public static void endPluginLogg(String pMiddleware, String pKey, short pLevel) {
        LB tmp;
        if (jobloggs.get(pKey) == null) tmp = new LB(); else tmp = jobloggs.remove(pKey);
        tmp.setTs1(System.currentTimeMillis());
        writeLogg(pMiddleware, tmp);
    }

    public static void writeJobLogg(String pKey, short pLevel, String pName, String pMSG) {
        initLogg(pKey, pName);
        writeJobLogg(pKey, pLevel, pMSG);
    }

    public static void writeJobLogg(String pKey, short pLevel, String pMSG) {
        LB tmp;
        if (jobloggs.get(pKey) == null) tmp = new LB(pMSG); else tmp = jobloggs.remove(pKey);
        tmp.setTs1(System.currentTimeMillis());
        try {
            FileWriter fw = new FileWriter(Base.getI().getJobDirectory(pKey) + "outputs/guse.logg", true);
            fw.write("<item>\n");
            fw.write("\t<name>" + tmp.getInfo() + "</name>\n");
            fw.write("\t<time>" + (new Timestamp(tmp.getTs0())).toString() + "</time>\n");
            fw.write("\t<etc>" + (tmp.getTs1() - tmp.getTs0()) + "</etc>\n");
            fw.write("\t<level>" + tmp.getLevel() + "</level>\n");
            fw.write("\t<info>" + pMSG + "</info>\n");
            fw.write("</item>\n");
            fw.close();
        } catch (IOException ex) {
            writeLogg(getI().loggs.get(MIDDLEWARE_SYSTEM), new LB(ex));
        }
    }

    public static void writeJobLogg(Job pJob, Exception e, String pMessage) {
        try {
            FileWriter fw = new FileWriter(Base.getI().getJobDirectory(pJob.getId()) + "outputs/guse.logg", true);
            fw.write("<item>\n");
            fw.write("\t<name>" + pMessage + "</name>\n");
            fw.write("\t<time>" + (new Timestamp(System.currentTimeMillis())).toString() + "</time>\n");
            fw.write("\t<etc></etc>\n");
            fw.write("\t<level>" + LB.EXCEPTION + "</level>\n");
            fw.write("\t<info>\n");
            fw.write("\t\t" + e.getMessage() + "\n");
            for (StackTraceElement t : e.getStackTrace()) fw.write("\t\t" + t.getClassName() + "." + t.getMethodName() + "(" + t.getFileName() + ":" + t.getLineNumber() + ")" + "\n");
            fw.write("\t</info>\n");
            fw.write("</item>\n");
            fw.close();
        } catch (IOException ex) {
            writeLogg(getI().loggs.get(MIDDLEWARE_SYSTEM), new LB(ex));
        }
    }
}
