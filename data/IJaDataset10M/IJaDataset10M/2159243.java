package net.sf.spooler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

public class Main {

    protected final Logger log = Logger.getLogger(getClass().getName());

    private List<Folder> folders = new ArrayList<Folder>();

    private List<ServiceParam> services = new ArrayList<ServiceParam>();

    private List<String> extensions = new ArrayList<String>();

    private Map<String, Job> jobImpls = new HashMap<String, Job>();

    private Map<String, RemoteLocation> remoteConf = new HashMap<String, RemoteLocation>();

    private Map<String, String> extenXservice = new HashMap<String, String>();

    private void execute() {
        services = Configuration.loadServices();
        if (services.size() == 0) {
            JOptionPane.showMessageDialog(null, "No services configured!");
            System.exit(0);
        }
        for (ServiceParam s : services) {
            String extension = s.getExtension().toLowerCase();
            extenXservice.put(extension, s.getType());
            if (!extensions.contains(extension)) extensions.add(extension);
            if ("windows.remote".equals(s.getType())) {
                jobImpls.put("windows.remote" + "." + s.getAlias(), new WindowsRemoteJob());
                remoteConf.put(s.getAlias(), new RemoteLocation(s.getIp(), s.getPort()));
            } else if ("windows".equals(s.getType())) {
                jobImpls.put("windows", new WindowsJob());
            } else if ("netware".equals(s.getType())) {
                jobImpls.put("netware", new NetwareJob());
            } else if ("pdf.generator".equals(s.getType())) {
                jobImpls.put("pdf.generator", new PdfConverterJob());
            } else if ("solic.coleta".equals(s.getType())) {
                jobImpls.put("solic.coleta", new SolicitacaoColetaJob());
            }
        }
        folders = Configuration.loadFolders();
        if (folders.size() == 0) {
            JOptionPane.showMessageDialog(null, "No folders configured");
            System.exit(0);
        }
        serve();
    }

    private void serve() {
        int timeWaitFolders = Configuration.getTimeWaitFolders();
        int timeWaitJobs = Configuration.getTimeWaitJobs();
        boolean debug = Configuration.isDebugEnabled();
        boolean multiThreaded = Configuration.isMultiThreaded();
        String customTimestamp = Configuration.getCustomTimestamp();
        List<JobFile> filesFound = new ArrayList<JobFile>();
        while (true) {
            for (Folder fld : folders) {
                log.info("Checking folder " + fld.getName());
                filesFound = getFiles(fld, customTimestamp);
                Map<String, JobExecutor> executors = new HashMap<String, JobExecutor>();
                for (JobFile jf : filesFound) {
                    JobParam jobParam = extractParams(jf);
                    jobParam.setFolder(fld);
                    String extension = jf.getName().substring((jf.getName().lastIndexOf(".") + 1)).toLowerCase();
                    String serviceType = extenXservice.get(extension);
                    if ("windows.remote".equals(serviceType)) {
                        serviceType = serviceType + "." + jobParam.getServer();
                    }
                    Job impl = jobImpls.get(serviceType);
                    JobExecutor je = null;
                    if (!executors.containsKey(serviceType)) {
                        je = new JobExecutor(timeWaitJobs);
                        je.setImpl(impl);
                        je.setDebug(debug);
                        executors.put(serviceType, je);
                    } else {
                        je = executors.get(serviceType);
                    }
                    je.enqueue(jobParam);
                    if (debug) {
                        log(jf);
                    }
                    File f = new File(jf.getName());
                    boolean success = false;
                    while (!success) {
                        success = f.delete();
                        if (!success) {
                            log.warn("Error deleting " + jf.getName());
                            Util.pause(100);
                        }
                    }
                }
                Iterator iterator = executors.keySet().iterator();
                while (iterator.hasNext()) {
                    String st = (String) iterator.next();
                    JobExecutor je = executors.get(st);
                    if ("windows.remote".equals(st) || "pdf-generator".equals(st) || multiThreaded) {
                        Thread thread = new Thread(je);
                        thread.start();
                    } else {
                        je.run();
                    }
                }
                filesFound.clear();
                Util.pause(timeWaitFolders);
            }
        }
    }

    private void log(JobFile jf) {
        SimpleDateFormat dfFile = new SimpleDateFormat("yyyy-MM-dd");
        File logFolder = new File("logs");
        if (!logFolder.exists() || !logFolder.isDirectory()) logFolder.mkdir();
        String logFile = "logs/queue" + dfFile.format(new Date()) + ".txt";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(logFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.println("File: " + jf.getName() + "  " + jf.getDate());
        pw.flush();
        pw.close();
    }

    private List<JobFile> getFiles(Folder fld, String customTimestamp) {
        File folder = new File(fld.getName());
        File[] files = folder.listFiles(new ExtensionFilter(extensions));
        List<JobFile> filesFound = new ArrayList<JobFile>();
        String timestamp = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssS");
        for (File file : files) {
            if (customTimestamp == null) {
                timestamp = df.format(new Date(file.lastModified()));
            } else {
                timestamp = getCustomTimestamp(file, customTimestamp);
            }
            JobFile jf = new JobFile(file.getPath(), timestamp, fld);
            filesFound.add(jf);
        }
        Collections.sort(filesFound, new Comparator<JobFile>() {

            public int compare(JobFile jf1, JobFile jf2) {
                return jf1.getDate().compareTo(jf2.getDate());
            }
        });
        return filesFound;
    }

    private String getCustomTimestamp(File file, String customTimestamp) {
        String timestamp = "";
        String command = customTimestamp + " " + file.getPath();
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            InputStreamReader isr = new InputStreamReader(pr.getInputStream());
            BufferedReader r = new BufferedReader(isr);
            timestamp = r.readLine();
            log.info("File timestamp: " + timestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    private JobParam extractParams(JobFile jf) {
        Properties p = Util.lerPropriedades(jf.getName());
        String server = "";
        String user = "";
        String copies = "";
        String printQueue = "";
        String reportFile = "";
        String pdfFileName = "";
        String emailFrom = null;
        String emailAddress = "";
        String emailSubject = "";
        String emailMsgBody = "";
        if (p.containsKey("servidor")) server = p.getProperty("servidor").trim();
        if (p.containsKey("usuario")) user = p.getProperty("usuario").trim();
        if (p.containsKey("copias")) copies = p.getProperty("copias").trim();
        if (p.containsKey("fila")) printQueue = p.getProperty("fila").trim();
        if (p.containsKey("arquivo")) {
            reportFile = p.getProperty("arquivo").trim();
            reportFile = fixedReportName(reportFile, jf.getName());
        }
        if (p.containsKey("nome.pdf")) pdfFileName = p.getProperty("nome.pdf").trim();
        if (p.containsKey("email.de")) emailFrom = p.getProperty("email.de").trim();
        if (p.containsKey("email.para")) emailAddress = p.getProperty("email.para").trim();
        if (p.containsKey("email.assunto")) emailSubject = p.getProperty("email.assunto").trim();
        if (p.containsKey("email.mensagem")) emailMsgBody = p.getProperty("email.mensagem").trim();
        log.info("processing " + reportFile);
        RemoteLocation remoteLocation = remoteConf.get(server);
        JobParam jp = new JobParam();
        jp.setCopies(copies);
        jp.setPrintQueue(printQueue);
        jp.setReportFile(reportFile);
        jp.setServer(server);
        jp.setUser(user);
        jp.setPdfFileName(pdfFileName);
        jp.setEmailAddress(emailAddress);
        jp.setEmailFrom(emailFrom);
        jp.setEmailSubject(emailSubject);
        jp.setEmailMsgBody(emailMsgBody);
        jp.setRemoteLocation(remoteLocation);
        return jp;
    }

    private String fixedReportName(String reportName, String propFileName) {
        if (propFileName.charAt(1) == ':' && reportName.indexOf(":") == -1) return propFileName.substring(0, 2) + reportName;
        return reportName;
    }

    public static void main(String[] args) {
        new Main().execute();
    }
}

class ExtensionFilter implements FileFilter {

    private List<String> extensions = new ArrayList<String>();

    public ExtensionFilter(List<String> extensions) {
        super();
        this.extensions = extensions;
    }

    public boolean accept(File file) {
        for (String ext : extensions) {
            String name = file.getName().toLowerCase();
            if (name.endsWith(ext)) return true;
        }
        return false;
    }
}
