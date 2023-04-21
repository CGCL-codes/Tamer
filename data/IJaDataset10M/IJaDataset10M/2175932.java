package org.icenigrid.gridsam.client.cli;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icenigrid.gridsam.client.common.ClientSideJobManager;
import org.icenigrid.gridsam.core.ConfigurationException;
import org.icenigrid.gridsam.core.JobInstance;
import org.icenigrid.gridsam.core.JobManager;
import org.icenigrid.gridsam.core.JobStage;
import org.icenigrid.gridsam.core.JobState;
import org.icenigrid.schema.jsdl.y2005.m11.JobDefinitionDocument;
import EDU.oswego.cs.dl.util.concurrent.CountDown;
import EDU.oswego.cs.dl.util.concurrent.Latch;

/**
 * GridSAM Stress Test Client with multiple input JSDLs. This command can accept
 * two folders of JSDL files, one conatins good JSDLs and the other contains bad
 * JSDLs. Before submitting each job during the stress test, this command will
 * first decide to choose good or bad JSDLs with a given bad JSDL percentage,
 * then randomly choose a good or bad JSDL file from the selected fold. <br>
 * This is a improved version of stress test client to accept multiple JSDLs.
 * 
 * @author Yongqiang Zou. 2008.11.05.
 */
public class GridSAMMultiJSDLStressTest {

    /**
	 * logger
	 */
    private static Log sLog = LogFactory.getLog(GridSAMMultiJSDLStressTest.class);

    /**
	 * interface for receiving event from user
	 */
    public interface Reporter {

        /**
		 * the test is started
		 * 
		 * @param pConcurrentUsers
		 *            number of users
		 * @param pJobs
		 *            number of jobs per user
		 */
        public void start(int pConcurrentUsers, int pJobs);

        /**
		 * report before submitJob is invoked
		 * 
		 * @param pUser
		 *            the user submitted the job
		 * @param pJobNo
		 *            the job number
		 */
        public void preSubmitJob(SimulatedUser pUser, int pJobNo);

        /**
		 * report error after submitJob is invoked
		 * 
		 * @param pUser
		 *            the user submitted the job
		 * @param pJobNo
		 *            the job number
		 * @param pError
		 *            the error
		 */
        public void errorSubmitJob(SimulatedUser pUser, int pJobNo, Exception pError);

        /**
		 * report after submitJob is invoked
		 * 
		 * @param pUser
		 *            the user submitted the job
		 * @param pJobNo
		 *            the job number
		 * @param pJobID
		 *            the job identifier
		 */
        public void postSubmitJob(SimulatedUser pUser, int pJobNo, String pJobID);

        /**
		 * a job is marked as terminal
		 * 
		 * @param pCompleted
		 *            the completed job
		 */
        public void postJobTerminal(JobInstance pCompleted);

        /**
		 * the test is ended
		 */
        public void end();
    }

    /**
	 * class representing a simulated user
	 */
    public static class SimulatedUser implements Runnable {

        /**
		 * the job manager
		 */
        private JobManager oJobManager;

        /**
		 * number of jobs to be submitted
		 */
        private int oNumberOfJobs;

        /**
		 * status reporter
		 */
        private Reporter oReporter;

        /**
		 * JSDL strings
		 */
        private JobDefinitionDocument[] oJSDLs;

        /**
		 * wrong JSDL strings.
		 */
        private JobDefinitionDocument[] oWrongJSDLs;

        private int oWrongPercentage;

        private static Random oRand;

        /**
		 * submission period
		 */
        private long oPeriod;

        static {
            oRand = new Random(System.currentTimeMillis());
        }

        /**
		 * create a simulated user
		 * 
		 * @param pJobmanager
		 *            the jobmanager to use to submit job
		 * @param pNumberOfJobs
		 *            number of jobs to submit
		 */
        public SimulatedUser(JobManager pJobmanager, int pNumberOfJobs, Reporter pReporter, JobDefinitionDocument[] pJSDL, JobDefinitionDocument[] pWrongJSDL, int pWrongPercentage, long pPeriod) {
            oJobManager = pJobmanager;
            oNumberOfJobs = pNumberOfJobs;
            oReporter = pReporter;
            oJSDLs = pJSDL;
            oWrongJSDLs = pWrongJSDL;
            oWrongPercentage = pWrongPercentage;
            oPeriod = pPeriod;
        }

        /**
		 * When an object implementing interface <code>Runnable</code> is used
		 * to create a thread, starting the thread causes the object's
		 * <code>run</code> method to be called in that separately executing
		 * thread. <p/> The general contract of the method <code>run</code> is
		 * that it may take any action whatsoever.
		 * 
		 * @see Thread#run()
		 */
        public void run() {
            for (int i = 0; i < oNumberOfJobs; i++) {
                try {
                    JobDefinitionDocument jsdl = getJSDL();
                    oReporter.preSubmitJob(this, i);
                    String jobName = "";
                    try {
                        jobName = jsdl.getJobDefinition().getJobDescription().getJobIdentification().getJobName();
                    } catch (Exception e) {
                        sLog.error(e.getMessage());
                    }
                    sLog.info(hashCode() + " submitting job using " + jobName);
                    JobInstance xJob = oJobManager.submitJob(jsdl);
                    sLog.info(hashCode() + " submitted job '" + xJob.getID() + "'");
                    oReporter.postSubmitJob(this, i, xJob.getID());
                } catch (Exception xEx) {
                    sLog.warn(hashCode() + " submission failed '" + xEx.getMessage() + "'", xEx);
                    oReporter.errorSubmitJob(this, i, xEx);
                }
                delay();
            }
        }

        /**
		 * get the JSDL to be submitted
		 * 
		 * @return String JSDL string
		 */
        public JobDefinitionDocument getJSDL() {
            JobDefinitionDocument[] useJDSLs = null;
            int rand = oRand.nextInt(100) + 1;
            if (rand <= this.oWrongPercentage) {
                useJDSLs = this.oWrongJSDLs;
            } else {
                useJDSLs = this.oJSDLs;
            }
            int idx = oRand.nextInt(Integer.MAX_VALUE) % useJDSLs.length;
            return useJDSLs[idx];
        }

        /**
		 * delay for a period before the next run
		 */
        public void delay() {
            try {
                Thread.sleep(oPeriod);
            } catch (InterruptedException xEx) {
                xEx.printStackTrace();
            }
        }
    }

    /**
	 * run the command-line application
	 * 
	 * @param pArgs
	 *            command line arguments
	 * @param pOut
	 *            output stream
	 * @param pErr
	 *            error stream
	 * @return return value
	 */
    public int run(String pArgs[], PrintStream pOut, PrintStream pErr) {
        final String fArgs[] = pArgs;
        HelpFormatter xFormatter = new HelpFormatter();
        Options xOptions = ClientSideJobManager.getStandardOptions();
        OptionGroup xServiceOptionGroup = new OptionGroup();
        xServiceOptionGroup.setRequired(true);
        xOptions.addOption(OptionBuilder.withArgName("path-to-jsdl").hasArg().withDescription("path to the JSDL file").create("j"));
        xOptions.addOption(OptionBuilder.withArgName("path-to-wrong-jsdl").hasArg().withDescription("path to the Wrong JSDL file").create("wj"));
        xOptions.addOption(OptionBuilder.withArgName("wrong-percentage").hasArg().withDescription("wrong percentage, should be a value between 0 to 100").create("wrongPer"));
        xOptions.addOption(OptionBuilder.withArgName("no-of-users").isRequired(true).hasArg().withDescription("number of users").create("users"));
        xOptions.addOption(OptionBuilder.withArgName("repetitions").isRequired(true).hasArg().withDescription("number of jobs per user").create("jobs"));
        xOptions.addOption(OptionBuilder.withArgName("milliseconds").isRequired(true).hasArg().withDescription("delay between job submission").create("delay"));
        xOptions.addOption(OptionBuilder.withArgName("milliseconds").isRequired(false).hasArg().withDescription("delay of the main thread between check job status").create("maindelay"));
        CommandLine xCmd = null;
        try {
            CommandLineParser xParser = new GnuParser();
            xCmd = xParser.parse(xOptions, pArgs);
        } catch (ParseException xEx) {
            xFormatter.printHelp("gridsam-stress-test (-s service-url | -sn service-name) [-j path-to-jsdl] " + " [-users no-of-users] [-jobs repetition] [-delay milliseconds] [-maindelay milliseconds]", "GridSAM Stress Test Client", xOptions, "Invalid or missing command-line options - " + xEx.getMessage(), false);
            return 1;
        }
        File xJobDescFolder = null;
        if (xCmd.getOptionValue("j") == null) {
            if (xCmd.getArgs().length == 0) {
                sLog.fatal("-j or path-to-jsdl is not specified");
                return 1;
            }
            xJobDescFolder = new File(xCmd.getArgs()[0]);
        } else {
            xJobDescFolder = new File(xCmd.getOptionValue("j"));
        }
        File xWrongJobDescFolder = null;
        if (xCmd.getOptionValue("wj") == null) {
            if (xCmd.getArgs().length == 0) {
                sLog.fatal("-wj or path-to-wrong-jsdl is not specified");
                return 1;
            }
            xWrongJobDescFolder = new File(xCmd.getArgs()[0]);
        } else {
            xWrongJobDescFolder = new File(xCmd.getOptionValue("wj"));
        }
        final int fConcurrentUsers = Integer.parseInt(xCmd.getOptionValue("users"));
        final int fJobs = Integer.parseInt(xCmd.getOptionValue("jobs"));
        final int fDelay = Integer.parseInt(xCmd.getOptionValue("delay"));
        int mainDelay = 2000;
        if (xCmd.hasOption("maindelay")) {
            mainDelay = Integer.parseInt(xCmd.getOptionValue("maindelay"));
        }
        final int fMainDelay = mainDelay;
        int wrongPercentage = 0;
        if (xCmd.hasOption("wrongPer")) {
            wrongPercentage = Integer.parseInt(xCmd.getOptionValue("wrongPer"));
        }
        final int fWrongPercentage = wrongPercentage;
        sLog.info("use wrongPercentage " + wrongPercentage);
        final Latch fLatch = new Latch();
        final CountDown fCountDown = new CountDown(fConcurrentUsers);
        File[] xJSDLFiles = null;
        File[] xWrongJSDLFiles = null;
        try {
            if (xJobDescFolder.isDirectory()) {
                xJSDLFiles = xJobDescFolder.listFiles();
            } else {
                xJSDLFiles = new File[1];
                xJSDLFiles[0] = xJobDescFolder;
            }
            if (xWrongJobDescFolder.isDirectory()) {
                xWrongJSDLFiles = xWrongJobDescFolder.listFiles();
            } else {
                xWrongJSDLFiles = new File[1];
                xWrongJSDLFiles[0] = xWrongJobDescFolder;
            }
        } catch (Exception e) {
            sLog.fatal(e.getMessage(), e);
            return 1;
        }
        JobDefinitionDocument[] xJSDLArray = new JobDefinitionDocument[xJSDLFiles.length];
        JobDefinitionDocument[] xWrongJSDLArray = new JobDefinitionDocument[xWrongJSDLFiles.length];
        try {
            for (int i = 0; i < xJSDLFiles.length; ++i) {
                File xJobDescFile = xJSDLFiles[i];
                JobDefinitionDocument xJSDL = JobDefinitionDocument.Factory.parse(xJobDescFile);
                xJSDLArray[i] = xJSDL;
            }
            for (int i = 0; i < xWrongJSDLFiles.length; ++i) {
                File xJobDescFile = xWrongJSDLFiles[i];
                JobDefinitionDocument xJSDL = JobDefinitionDocument.Factory.parse(xJobDescFile);
                xWrongJSDLArray[i] = xJSDL;
            }
        } catch (Exception xEx) {
            sLog.fatal(xEx.getMessage(), xEx);
            return 1;
        }
        final JobDefinitionDocument[] fJSDLs = xJSDLArray;
        final JobDefinitionDocument[] fWrongJSDLs = xWrongJSDLArray;
        ClientSideJobManager xJobManager = null;
        try {
            xJobManager = new ClientSideJobManager(pArgs, xOptions);
        } catch (ConfigurationException xEx) {
            sLog.fatal(xEx.getMessage(), xEx);
            return 1;
        }
        final XMLReporter fReporter = new XMLReporter(new OutputStreamWriter(pOut));
        fReporter.start(fConcurrentUsers, fJobs);
        final ClientSideJobManager fJobManager = xJobManager;
        for (int i = 0; i < fConcurrentUsers; i++) {
            final int userid = i;
            Thread xThread = new Thread(new Runnable() {

                public void run() {
                    try {
                        try {
                            fLatch.acquire();
                        } catch (InterruptedException xEx) {
                            xEx.printStackTrace();
                            return;
                        }
                        SimulatedUser xUser = new SimulatedUser(fJobManager, fJobs, fReporter, fJSDLs, fWrongJSDLs, fWrongPercentage, fDelay);
                        xUser.run();
                    } finally {
                        fCountDown.release();
                    }
                }
            });
            xThread.start();
        }
        fLatch.release();
        try {
            fCountDown.acquire();
        } catch (Exception xEx) {
            xEx.printStackTrace();
        }
        pOut.flush();
        sLog.info("collecting all submitted job status");
        Set xSet = new HashSet(fReporter.getSubmittedJobIDs());
        while (!xSet.isEmpty()) {
            for (Iterator xIDs = xSet.iterator(); xIDs.hasNext(); ) {
                String xID = (String) xIDs.next();
                try {
                    JobInstance xJobInstance = (JobInstance) fJobManager.findJobInstance(xID);
                    if (xJobInstance.getLastKnownStage().getState().isTerminal()) {
                        fReporter.postJobTerminal(xJobInstance);
                        xIDs.remove();
                        continue;
                    }
                } catch (Exception xEx) {
                    xEx.printStackTrace();
                    sLog.fatal("unable to retrieve job status of " + xID + ": " + xEx.getMessage(), xEx);
                    xIDs.remove();
                }
            }
            try {
                Thread.sleep(fMainDelay);
                sLog.info("wake up to check job status after sleep " + fMainDelay + " ms.");
            } catch (InterruptedException xEx) {
                xEx.printStackTrace();
            }
        }
        fReporter.end();
        return 0;
    }

    /**
	 * a reporter that generates a XML report
	 */
    public class XMLReporter implements Reporter {

        protected PrintWriter oWriter;

        /**
		 * Map <user:jobno, submitted Date>,
		 */
        private Map oTempJobs = Collections.synchronizedMap(new HashMap());

        /**
		 * submitted jobs <jobid, Date>
		 */
        private Map oSubmittedJobs = Collections.synchronizedMap(new HashMap());

        /**
		 * failed submission count
		 */
        private int oFailedSubmissionCount = 0;

        /**
		 * failed job count
		 */
        private int oFailedJobCount = 0;

        /**
		 * total response time for submitJob()
		 */
        private long oTotalResponseTime = 0;

        /**
		 * turnaround time for the job
		 */
        private long oTotalTurnaroundTime = 0;

        /**
		 * total number of jobs attempted
		 */
        private int oTotalJobs;

        /**
		 * create an XMLReporter
		 * 
		 * @param pWriter
		 *            the writer to write the report to
		 */
        public XMLReporter(Writer pWriter) {
            oWriter = new PrintWriter(pWriter);
        }

        /**
		 * get the list of submitted jobs
		 * 
		 * @return List of job id
		 */
        public Set getSubmittedJobIDs() {
            return oSubmittedJobs.keySet();
        }

        /**
		 * the test is started
		 * 
		 * @param pConcurrentUsers
		 *            number of users
		 * @param pJobs
		 *            number of jobs per user
		 */
        public synchronized void start(int pConcurrentUsers, int pJobs) {
            oTotalJobs = pConcurrentUsers * pJobs;
            oWriter.println("<test concurrency=\"" + pConcurrentUsers + "\" jobs=\"" + pJobs + "\" >");
        }

        /**
		 * the test is ended
		 */
        public synchronized void end() {
            oWriter.println("\t<statistics>");
            oWriter.println("\t\t<totalFailedSubmissions>" + oFailedSubmissionCount + "</totalFailedSubmissions>");
            oWriter.println("\t\t<totalFailedJobs>" + oFailedJobCount + "</totalFailedJobs>");
            if ((oTotalJobs - oFailedSubmissionCount) > 0) {
                double xAverage = (double) oTotalResponseTime / (oTotalJobs - oFailedSubmissionCount);
                oWriter.println("\t\t<averageResponseTime samples=\"" + (oTotalJobs - oFailedSubmissionCount) + "\">" + xAverage + "</averageResponseTime>");
            }
            if ((oTotalJobs - oFailedSubmissionCount - oFailedJobCount) > 0) {
                double xAverage = (double) oTotalTurnaroundTime / (oTotalJobs - oFailedSubmissionCount - oFailedJobCount);
                oWriter.println("\t\t<averageTurnaroundTime samples=\"" + (oTotalJobs - oFailedSubmissionCount - oFailedJobCount) + "\">" + xAverage + "</averageTurnaroundTime>");
            }
            oWriter.println("\t</statistics>");
            oWriter.println("</test>");
            oWriter.flush();
        }

        /**
		 * report before submitJob is invoked
		 * 
		 * @param pUser
		 *            the user submitted the job
		 * @param pJobNo
		 *            the job number
		 */
        public synchronized void preSubmitJob(SimulatedUser pUser, int pJobNo) {
            synchronized (oTempJobs) {
                oTempJobs.put(pUser.hashCode() + ":" + pJobNo, new Date());
            }
        }

        /**
		 * report error after submitJob is invoked
		 * 
		 * @param pUser
		 *            the user submitted the job
		 * @param pJobNo
		 *            the job number
		 * @param pError
		 *            the error
		 */
        public synchronized void errorSubmitJob(SimulatedUser pUser, int pJobNo, Exception pError) {
            oFailedSubmissionCount++;
        }

        /**
		 * report after submitJob is invoked
		 * 
		 * @param pUser
		 *            the user submitted the job
		 * @param pJobNo
		 *            the job number
		 * @param pJobID
		 *            the job identifier
		 */
        public synchronized void postSubmitJob(SimulatedUser pUser, int pJobNo, String pJobID) {
            synchronized (oTempJobs) {
                Date xNow = new Date();
                Date xSubmitted = (Date) oTempJobs.remove(pUser.hashCode() + ":" + pJobNo);
                oSubmittedJobs.put(pJobID, xSubmitted);
                oTotalResponseTime = oTotalResponseTime + (xNow.getTime() - xSubmitted.getTime());
                oWriter.println("\t<submittedJob id=\"" + pJobID + "\" responseTime=\"" + (xNow.getTime() - xSubmitted.getTime()) + "\" />");
                oWriter.flush();
            }
        }

        /**
		 * a job is marked as terminal
		 * 
		 * @param pCompleted
		 *            the completed job
		 */
        public void postJobTerminal(JobInstance pCompleted) {
            synchronized (oSubmittedJobs) {
                Date xSubmitted = (Date) oSubmittedJobs.remove(pCompleted.getID());
                sLog.info("job '" + pCompleted.getID() + "' is " + pCompleted.getLastKnownStage().getState());
                if (pCompleted.getLastKnownStage().getState().equals(JobState.DONE)) {
                    oWriter.println("\t<completedJob id=\"" + pCompleted.getID() + "\" " + "submittedAt=\"" + xSubmitted.getTime() + "\" " + "enteredAt=\"" + ((JobStage) pCompleted.getJobStages().get(0)).getDate().getTime() + "\" " + "completedAt=\"" + pCompleted.getLastKnownStage().getDate().getTime() + "\" " + "/>");
                    oTotalTurnaroundTime = oTotalTurnaroundTime + (pCompleted.getLastKnownStage().getDate().getTime() - ((JobStage) pCompleted.getJobStages().get(0)).getDate().getTime());
                } else {
                    oWriter.println("\t<failedJob id=\"" + pCompleted.getID() + "\" " + "submittedAt=\"" + xSubmitted.getTime() + "\"" + "enteredAt=\"" + ((JobStage) pCompleted.getJobStages().get(0)).getDate().getTime() + "\"" + "failedAt=\"" + pCompleted.getLastKnownStage().getDate().getTime() + "\"" + ">");
                    oWriter.println("\t\t" + pCompleted.getLastKnownStage().getDescription());
                    oWriter.println("\t</failedJob>");
                    oFailedJobCount++;
                }
                oWriter.flush();
            }
        }
    }

    /**
	 * application entry point
	 * 
	 * @param pArgs
	 *            array of command-line arguments
	 */
    public static void main(String pArgs[]) {
        System.exit((new GridSAMMultiJSDLStressTest()).run(pArgs, System.out, System.err));
    }
}
