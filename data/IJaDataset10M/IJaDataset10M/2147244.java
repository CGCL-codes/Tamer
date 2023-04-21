package com.ericdaugherty.mail.server;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ericdaugherty.mail.server.configuration.*;
import com.ericdaugherty.mail.server.configuration.ConfigurationManager.TransferMode;
import com.ericdaugherty.mail.server.configuration.ConfigurationManager.RetrievalMode;
import com.ericdaugherty.mail.server.persistence.SMTPMessagePersistenceFactory;
import com.ericdaugherty.mail.server.security.JESSecurityManager;
import com.ericdaugherty.mail.server.services.general.ServiceListener;
import com.ericdaugherty.mail.server.services.pop3.Pop3Processor;
import com.ericdaugherty.mail.server.services.smtp.*;
import com.ericdaugherty.mail.server.services.smtp.support.VerifyIPFactory;

/**
 * This class is the entrypoint for the Mail Server application.  It creates
 * threads to listen for SMTP and POP3 connections.  It also handles the
 * configuration information and initialization of the User subsystem.
 *
 * @author Eric Daugherty
 * @author Andreas Kyrmegalos (2.x branch)
 */
public class Mail {

    /** Logger for this class. */
    private static Log log = LogFactory.getLog(Mail.class);

    private static Mail instance;

    private final ThreadGroup threadgroup = new ThreadGroup("JESThreadGroup");

    private final Status status;

    private final ServiceListener popListener;

    private final ServiceListener smtpListener;

    private final ServiceListener amavisSmtpListener;

    private final ServiceListener securepopListener;

    private final ServiceListener securesmtpListener;

    private final SMTPSender smtpSender, amavisSmtpSender;

    private final ConnectionBasedConfigurator connectionBasedConfigurator;

    private final ShutdownService shutdownService;

    /** The SMTP sender thread */
    private final Thread smtpSenderThread, amavisSmtpSenderThread;

    /** The ShutdownService Thread.  Started when the JVM is shutdown. */
    private final Thread shutdownServiceThread;

    /** A parameter to alert threads that the server is shutting down. */
    private volatile boolean shuttingDown;

    private static volatile boolean completedOperations;

    public static boolean testing;

    private Mail(String[] args) {
        if (args.length > 1) testing = args[1].toLowerCase().equals("testing") || (args.length > 2 ? args[2].toLowerCase().equals("testing") : false);
        String os = System.getProperty("os.name").toLowerCase();
        boolean isUNIX = os.indexOf("win") == -1 && os.indexOf("mac") == -1;
        String directory = getConfigurationDirectory(args);
        log.warn("JES Starting Up...");
        ConfigurationManager configurationManager = ConfigurationManager.initialize(directory);
        Class serviceListener = ServiceListener.class;
        Method method = null;
        try {
            method = serviceListener.getMethod("setTotalSL", int.class);
            Integer totalSL = new Integer(1 + (configurationManager.getRetrievalMode() == RetrievalMode.POP3 ? 1 : 0) + (configurationManager.isAmavisSupportActive() || testing ? 1 : 0) + (configurationManager.isSecureActive() ? 1 + ((configurationManager.getRetrievalMode() == RetrievalMode.POP3 ? 1 : 0)) : 0));
            log.debug("Total number of service listeners to be instanced " + totalSL);
            method.invoke(null, totalSL);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        int port;
        int executeThreads = configurationManager.getExecuteThreadCount();
        if (configurationManager.getRetrievalMode() == RetrievalMode.POP3) {
            port = configurationManager.getPOP3Port();
            if (log.isDebugEnabled()) log.debug("Starting POP3 Service on port: " + port);
            popListener = new ServiceListener(port, Pop3Processor.class, executeThreads, false);
            new Thread(threadgroup, popListener, "POP3").start();
        } else {
            popListener = null;
        }
        port = configurationManager.getSMTPPort();
        if (log.isDebugEnabled()) log.debug("Starting SMTP Service on port: " + port);
        smtpListener = new ServiceListener(port, SMTPProcessor.class, executeThreads, false);
        new Thread(threadgroup, smtpListener, "SMTP").start();
        if (configurationManager.isAmavisSupportActive() || testing) {
            port = !testing ? configurationManager.getAmavisFilteredSMTPPort() : (configurationManager.getSMTPPort() + 1);
            if (log.isDebugEnabled()) log.debug("Starting Transmiting MTA's SMTP Service on port: " + port);
            amavisSmtpListener = new ServiceListener(port, SMTPProcessorAmavis.class, executeThreads, false);
            new Thread(threadgroup, amavisSmtpListener, !testing ? "Amavis SMTP" : "Testing SMTP").start();
        } else {
            amavisSmtpListener = null;
        }
        if (configurationManager.isSecureActive()) {
            int secureexecuteThreads = configurationManager.getSecureExecuteThreadCount();
            if (configurationManager.getRetrievalMode() == RetrievalMode.POP3) {
                port = configurationManager.getSecurePOP3Port();
                if (log.isDebugEnabled()) log.debug("Starting secure POP3 Service on port: " + port);
                securepopListener = new ServiceListener(port, Pop3Processor.class, secureexecuteThreads, true);
                new Thread(threadgroup, securepopListener, "secure POP3").start();
            } else {
                securepopListener = null;
            }
            port = configurationManager.getSecureSMTPPort();
            if (log.isDebugEnabled()) log.debug("Starting secure SMTP Service on port: " + port);
            securesmtpListener = new ServiceListener(port, SMTPProcessor.class, secureexecuteThreads, true);
            new Thread(threadgroup, securesmtpListener, "secure SMTP").start();
        } else {
            securepopListener = null;
            securesmtpListener = null;
        }
        if (configurationManager.isAmavisSupportActive()) {
            smtpSenderThread = new Thread(threadgroup, smtpSender = new SMTPSenderAmavis(), "SMTPSender");
            smtpSenderThread.start();
            amavisSmtpSenderThread = new Thread(threadgroup, amavisSmtpSender = new SMTPSenderStandard(false), "SMTPSender2");
            amavisSmtpSenderThread.start();
        } else if (testing) {
            smtpSenderThread = new Thread(threadgroup, smtpSender = new SMTPSenderStandard(true), "SMTPSenderTest");
            smtpSenderThread.start();
            amavisSmtpSenderThread = new Thread(Thread.currentThread().getThreadGroup(), amavisSmtpSender = new SMTPSenderStandard(false), "SMTPSender");
            amavisSmtpSenderThread.start();
        } else {
            smtpSenderThread = new Thread(threadgroup, smtpSender = new SMTPSenderStandard(false), "SMTPSender");
            smtpSenderThread.start();
            amavisSmtpSender = null;
            amavisSmtpSenderThread = null;
        }
        if (configurationManager.getConfigurationAddress() != null) {
            new Thread(threadgroup, connectionBasedConfigurator = new ConnectionBasedConfigurator(), "Configuration").start();
        } else {
            connectionBasedConfigurator = null;
        }
        shutdownService = new ShutdownService();
        shutdownServiceThread = new Thread(shutdownService);
        Runtime.getRuntime().addShutdownHook(shutdownServiceThread);
        try {
            method = serviceListener.getMethod("getLock");
            Object lock = method.invoke(null);
            if (!ServiceListener.isSLsloadingComplete()) {
                log.debug("Entering lock state");
                synchronized (lock) {
                    try {
                        lock.wait();
                        log.debug("All ServiceListener instances completed execution");
                    } catch (InterruptedException ex) {
                        log.debug(ex.getMessage());
                    }
                }
            } else {
                log.debug("All ServiceListener instances completed execution");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (isUNIX) {
            try {
                int uid = -1;
                try {
                    uid = Integer.parseInt(args[1]);
                } catch (NumberFormatException nfe) {
                    try {
                        uid = Integer.parseInt(args[2]);
                    } catch (NumberFormatException nfe1) {
                        log.error("uid not passed through main method arguments");
                    }
                }
                if (uid != -1) {
                    com.ericdaugherty.mail.server.UnixUID.setUid(uid);
                }
            } catch (IndexOutOfBoundsException ioobe) {
            }
        }
        ModuleControl.initialize(directory);
        log.debug("Persisting user/realm/password updates");
        configurationManager.persistUpdates();
        completedOperations = true;
        log.debug("Bringing SMTPSender(s) out of stand by.");
        synchronized (smtpSender) {
            smtpSender.notify();
        }
        if (configurationManager.isAmavisSupportActive() || testing) {
            synchronized (amavisSmtpSender) {
                amavisSmtpSender.notify();
            }
        }
        if (ConfigurationManager.isSecurityManagerEnabled()) {
            System.setSecurityManager(new JESSecurityManager());
        }
        status = new Status(!isUNIX);
        status.start();
        log.warn("JES Started Successfully.");
    }

    public static void instantiate(String[] args) {
        if (instance == null) {
            try {
                instance = new Mail(args);
            } catch (RuntimeException runtimeException) {
                System.err.println("The application failed to initialize.");
                System.err.println(runtimeException.getMessage());
                runtimeException.printStackTrace();
                System.exit(0);
            }
        }
    }

    public static Mail getInstance() {
        return instance;
    }

    /**
     * If true a server shut down has been initiated.
     *
     * @return shuttingDown
     */
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    /**
     * If true all operations relating to system startup have completed
     *
     * @return completedOperations
     */
    public static boolean hasCompletedOperations() {
        return completedOperations;
    }

    /**
     * Provides a 'safe' way for the application to shut down.  This
     * method is provided to enable compatability with the NT Service
     * wrapper class.  It defers the call to the shutdown method.
     *
     * @param args
     */
    public static void shutdown(String[] args) {
        log.debug("NT Service requested application shutdown.");
        getInstance().shutdown();
    }

    /**
     * Provides a 'safe' way for the application to shut down.  It will attempt
     * to stop the running threads.
     */
    public void shutdown() {
        if (shuttingDown) return;
        log.warn("Shutting down Mail Server.");
        shuttingDown = true;
        if (ConfigurationManager.getInstance().getRetrievalMode() == RetrievalMode.POP3) {
            popListener.notifyshutdown();
        }
        smtpListener.notifyshutdown();
        if (ConfigurationManager.getInstance().isAmavisSupportActive() || testing) amavisSmtpListener.notifyshutdown();
        if (ConfigurationManager.getInstance().isSecureActive()) {
            if (ConfigurationManager.getInstance().getRetrievalMode() == RetrievalMode.POP3) {
                securepopListener.notifyshutdown();
            }
            securesmtpListener.notifyshutdown();
        }
        smtpSender.notifyshutdown();
        if (ConfigurationManager.getInstance().isAmavisSupportActive() || testing) amavisSmtpSender.notifyshutdown();
        SMTPMessagePersistenceFactory.shutdown();
        popListener.initiateshutdown();
        smtpListener.initiateshutdown();
        if (ConfigurationManager.getInstance().isAmavisSupportActive() || testing) amavisSmtpListener.initiateshutdown();
        if (ConfigurationManager.getInstance().isSecureActive()) {
            if (ConfigurationManager.getInstance().getRetrievalMode() == RetrievalMode.POP3) {
                securepopListener.initiateshutdown();
            }
            securesmtpListener.initiateshutdown();
        }
        try {
            smtpSenderThread.join();
        } catch (InterruptedException ie) {
        }
        if (ConfigurationManager.getInstance().isAmavisSupportActive() || testing) {
            try {
                amavisSmtpSenderThread.join();
            } catch (InterruptedException ie) {
            }
        }
        if (connectionBasedConfigurator != null) {
            connectionBasedConfigurator.shutdown();
        }
        ModuleControl.shutdown();
        ConfigurationManager.shutdown();
        completedOperations = false;
        shuttingDown = false;
        testing = false;
        log.warn("Server shutdown complete.");
        instance = null;
    }

    /**
     * This method is the entrypoint to the system and is responsible
     * for the initial configuration of the application and the creation
     * of all 'service' threads.
     */
    public static void main(String[] args) {
        instantiate(args);
    }

    /**
     * Parses the input parameter for the configuration directory, or defaults
     * to the local directory.
     *
     * @param args the commandline arguments.
     * @return the directory to use as the 'root'.
     */
    private static String getConfigurationDirectory(String[] args) {
        String directory = ".";
        File directoryFile;
        if (args.length > 0) {
            directory = args[0];
        } else if ((directoryFile = new File(directory)).exists()) {
            System.out.println("Configuration Directory not specified.  Using \"" + directoryFile.getAbsolutePath() + "\"");
        } else {
            System.out.println("Usage:  java com.ericdaugherty.mail.server.Mail <configuration directory>");
            throw new RuntimeException("Unable to load the configuration file.");
        }
        return directory;
    }

    public void notifyChange() {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        if (ConfigurationManager.getInstance().getRetrievalMode() == RetrievalMode.POP3) {
            if (configurationManager.getPOP3Port() != popListener.getPort()) {
                new Thread(popListener.updateServerSocket(configurationManager.getPOP3Port())).start();
            }
        }
        if (configurationManager.getSMTPPort() != smtpListener.getPort()) {
            new Thread(smtpListener.updateServerSocket(configurationManager.getSMTPPort())).start();
        }
        if (ConfigurationManager.getInstance().isSecureActive()) {
            if (ConfigurationManager.getInstance().getRetrievalMode() == RetrievalMode.POP3) {
                if (configurationManager.getSecurePOP3Port() != securepopListener.getPort()) {
                    new Thread(securepopListener.updateServerSocket(configurationManager.getSecurePOP3Port())).start();
                }
            }
            if (configurationManager.getSecureSMTPPort() != securesmtpListener.getPort()) {
                new Thread(securesmtpListener.updateServerSocket(configurationManager.getSecureSMTPPort())).start();
            }
        }
    }

    private class Status extends Thread {

        private boolean refresh;

        /**
       * Initialize the thread.
       */
        public Status(boolean refresh) {
            super("JES Status Monitor");
            setDaemon(true);
            this.refresh = refresh;
        }

        /**
       * Check the status of the application
       */
        public void run() {
            long sleepTime = 5 * 60 * 1000;
            int verifyIPReplaceCounter = 0;
            ConfigurationManager cm = ConfigurationManager.getInstance();
            int maxthreadcount = 5 + cm.getExecuteThreadCount() * (1 + (cm.getRetrievalMode() == RetrievalMode.POP3 ? 1 : 0)) + (cm.getConfigurationAddress() != null ? 1 : 0) + (cm.isSecureActive() ? cm.getSecureExecuteThreadCount() * (1 + (cm.getRetrievalMode() == RetrievalMode.POP3 ? 1 : 0)) : 0) + ((cm.isAmavisSupportActive() || testing) ? cm.getExecuteThreadCount() + 5 : 0);
            int maxstandardsmtp, maxstandardpop3, maxsecuresmtp, maxsecurepop3, maxamavissmtp;
            int curstandardsmtp, curstandardpop3, cursecuresmtp, cursecurepop3, curamavissmtp, curdeliversmtp, curamavisdeliversmtp;
            maxstandardpop3 = maxstandardsmtp = cm.getExecuteThreadCount();
            maxsecurepop3 = maxsecuresmtp = cm.isSecureActive() ? cm.getSecureExecuteThreadCount() : 0;
            maxamavissmtp = (cm.isAmavisSupportActive() || testing) ? cm.getExecuteThreadCount() : 0;
            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException ie) {
                interrupted();
                if (isShuttingDown()) return;
                log.error("Error in JES Status Monitor thread.  Thread will continue to execute. " + ie, ie);
            }
            long start = System.nanoTime();
            long active, days, hours, mins;
            int dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR), currentDay;
            Calendar calendar;
            while (!isShuttingDown()) {
                active = System.nanoTime() - start;
                days = active / (24L * 60L * 60L * 1000L * 1000000L);
                active -= days * (24L * 60L * 60L * 1000L * 1000000L);
                hours = active / (60L * 60L * 1000L * 1000000L);
                active -= hours * (60L * 60L * 1000L * 1000000L);
                mins = active / (60L * 1000L * 1000000L);
                active -= mins * (60L * 1000L * 1000000L);
                active /= 1000L * 1000000L;
                StringBuilder timeactive = new StringBuilder(" Time active: ");
                if (days > 0L) timeactive.append(days + " days, ");
                if (hours > 0L) timeactive.append(hours + " hours, ");
                if (mins > 0L) timeactive.append(mins + " minutes.");
                log.info(timeactive);
                curamavissmtp = curstandardsmtp = curstandardpop3 = cursecuresmtp = cursecurepop3 = curdeliversmtp = curamavisdeliversmtp = 0;
                Thread[] threadlist = new Thread[maxthreadcount];
                int threadcount;
                threadcount = threadgroup.enumerate(threadlist);
                if (threadcount > 0) {
                    StringBuilder jesstatus = new StringBuilder(" Active Modules: ");
                    for (int i = 0; i < threadcount; i++) {
                        if (threadlist[i].getName().equals("SMTPSender")) {
                            jesstatus.append("SMTPSender ");
                        } else if (threadlist[i].getName().startsWith("Deliver Standard")) {
                            curdeliversmtp++;
                        } else if (threadlist[i].getName().startsWith("Deliver Amavis")) {
                            curamavisdeliversmtp++;
                        } else if (threadlist[i].getName().startsWith("SMTP:")) {
                            curstandardsmtp++;
                        } else if (threadlist[i].getName().startsWith("POP3:")) {
                            curstandardpop3++;
                        } else if (threadlist[i].getName().startsWith("Amavis") || threadlist[i].getName().startsWith("Testing")) {
                            curamavissmtp++;
                        } else {
                            if (threadlist[i].getName().startsWith("secure S")) {
                                cursecuresmtp++;
                            } else if (threadlist[i].getName().startsWith("secure P")) {
                                cursecurepop3++;
                            }
                        }
                    }
                    jesstatus.append("Deliver Standard:" + curdeliversmtp + "/4 ");
                    if (cm.isAmavisSupportActive()) jesstatus.append("Deliver Amavis:" + curamavisdeliversmtp + "/4 ");
                    jesstatus.append("SMTP:" + curstandardsmtp + "/" + maxstandardsmtp + " ");
                    if (cm.isAmavisSupportActive()) jesstatus.append("SMTP Amavis:" + curamavissmtp + "/" + maxamavissmtp + " "); else if (testing) jesstatus.append("SMTP Testing:" + curamavissmtp + "/" + maxamavissmtp + " ");
                    if (cm.getRetrievalMode() == RetrievalMode.POP3) {
                        jesstatus.append("POP3: " + curstandardpop3 + "/" + maxstandardpop3 + " ");
                    }
                    if (cm.isSecureActive()) {
                        jesstatus.append("secure SMTP: " + cursecuresmtp + "/" + maxsecuresmtp + " ");
                        if (cm.getRetrievalMode() == RetrievalMode.POP3) {
                            jesstatus.append("secure POP3: " + cursecurepop3 + "/" + maxsecurepop3 + " ");
                        }
                    }
                    log.info(jesstatus);
                    jesstatus = null;
                } else {
                    log.fatal(" No active threads");
                    break;
                }
                threadlist = null;
                if (verifyIPReplaceCounter == 3) {
                    log.info("Updating the alternate verify IP");
                    verifyIPReplaceCounter = 0;
                    VerifyIPFactory.updateAlternateVerifyIP();
                }
                if (refresh) {
                    calendar = Calendar.getInstance();
                    currentDay = calendar.get(Calendar.DAY_OF_YEAR);
                    if (currentDay != dayOfYear && calendar.get(Calendar.HOUR_OF_DAY) >= 4) {
                        log.debug("Updating ServerSockets");
                        dayOfYear = currentDay;
                        new Thread(popListener.updateServerSocket(-1)).start();
                        new Thread(smtpListener.updateServerSocket(-1)).start();
                        if (ConfigurationManager.getInstance().isSecureActive()) {
                            new Thread(securepopListener.updateServerSocket(-1)).start();
                            new Thread(securesmtpListener.updateServerSocket(-1)).start();
                        }
                    }
                    calendar.clear();
                    calendar = null;
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ie) {
                    interrupted();
                    log.error("Error in JES Status Monitor thread.  Thread will continue to execute. " + ie, ie);
                }
                verifyIPReplaceCounter++;
            }
        }
    }
}
