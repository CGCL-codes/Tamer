package com.javapathfinder.vjp.verify;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.report.Publisher;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import com.javapathfinder.vjp.DefaultProperties;
import com.javapathfinder.vjp.VJP;
import com.javapathfinder.vjp.config.editors.ModePropertyConfiguration;
import com.javapathfinder.vjp.verify.topics.Topic;
import com.javapathfinder.vjp.verify.topics.TopicPublisher;
import com.javapathfinder.vjp.verify.topics.TopicView;
import com.javapathfinder.vjp.verify.view.VJPView;

/**
 * This Job serves to perform the JPF verification. 
 * The Verify view is created, the publisher is grabbed and verification is
 * executed all in this job.
 * @author Sandro Badame
 */
public class VerifyJob extends Job {

    /**
   * The name of this job
   */
    private static final String JOB_NAME = "Verify...";

    /**
   * The property that vjp looks for to add a delay to each transition in the 
   * run.
   */
    private static final String delay_key = "vjp.rundelay";

    /**
   * The property that vjp reads to determine whether to display the trace view
   */
    private static final String trace_listener_key = "vjp.tracelistener";

    private static boolean isRunning = false;

    /**
   * @return true if there is currently a VerifyJob running
   */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
   * Automatically creates and schedules a verification
   * @param file the configuration file used to define the user defined properties
   *             for this verification
   * @param project the project to be used to determine the VJP defined values.
   * @param step if true then verification will be done step by step
   */
    public static void verify(IFile file, IProject project, boolean step) {
        VJP.logInfo(project.toString());
        verify(file, JavaCore.create(project), step);
    }

    /**
   * Automatically creates and schedules a verification
   * @param file the configuration file used to define the user defined properties
   *             for this verification
   * @param project the project to be used to determine the VJP defined values.
   * @param step if true then verification will be done step by step
   */
    public static void verify(IFile file, IJavaProject project, boolean step) {
        VerifyJob job = new VerifyJob(file, project);
        job.schedule();
    }

    private IFile file;

    private Config config;

    private boolean step = false;

    private PrintStream out, err;

    /**
   * @param modePropertyFilePath This absolute path to the Mode Property File
   *                             to be used for this verification.
   * @param project 
   */
    public VerifyJob(IFile modePropertyFile, IJavaProject project) {
        super(JOB_NAME);
        this.file = modePropertyFile;
        this.config = createConfig(modePropertyFile.getLocation().toOSString(), project);
    }

    private Config createConfig(String path, IJavaProject project) {
        Config config = JPF.createConfig(new String[] { path });
        HashMap<String, String> vjp = DefaultProperties.getDefaultProperties(project);
        HashMap<String, String> userDefined = ModePropertyConfiguration.getConfigFileProperties(file);
        for (String key : vjp.keySet()) {
            if (!userDefined.containsKey(key)) config.setProperty(key, vjp.get(key));
        }
        String publisher = userDefined.get("jpf.report.publisher");
        if (publisher == null || publisher.contains("topic")) {
            config.setProperty("jpf.report.publisher", "topic");
            config.setProperty("jpf.report.topic.class", "com.javapathfinder.vjp.verify.topics.TopicPublisher");
        }
        return config;
    }

    protected IStatus run(IProgressMonitor monitor) {
        monitor.beginTask("JPF Verify", 4);
        grabLogging();
        if (monitor.isCanceled()) return cancelJob();
        monitor.worked(1);
        monitor.subTask("Creating JPF");
        JPF jpf = new JPF(config);
        if (monitor.isCanceled()) return cancelJob();
        monitor.worked(1);
        if (config.getBoolean(trace_listener_key, false)) {
            monitor.subTask("Adding VJP listeners");
            addListeners(jpf);
            if (monitor.isCanceled()) return cancelJob();
            monitor.worked(1);
        }
        monitor.subTask("Running verification");
        try {
            jpf.run();
        } catch (Exception e) {
            VJP.logError("Exception while running JPF", e);
        }
        checkForTopicPublisher(jpf);
        monitor.subTask("Cleaning up...");
        finishJob();
        monitor.done();
        done(Status.OK_STATUS);
        return Status.OK_STATUS;
    }

    private IStatus cancelJob() {
        finishJob();
        done(Status.CANCEL_STATUS);
        return Status.CANCEL_STATUS;
    }

    private void finishJob() {
        releaseLogging();
        isRunning = false;
    }

    private void grabLogging() {
        MessageConsole console = new MessageConsole("Verify: " + config.getProperty("target"), null);
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
        MessageConsoleStream stream = console.newMessageStream();
        out = System.out;
        err = System.err;
        System.setOut(new PrintStream(stream));
        System.setErr(new PrintStream(stream));
    }

    private void addListeners(JPF jpf) {
        setStepRun(true);
        VJPView view = VJPView.getView();
        view.setVerifyJob(this);
        VJPListener listener = new VJPListener(view, step);
        listener.setRunDelayMillis(config.getInt(delay_key, 0));
        jpf.addSearchListener(listener);
        jpf.addVMListener(listener);
    }

    private void checkForTopicPublisher(JPF jpf) {
        for (Publisher publisher : jpf.getReporter().getPublishers()) {
            if (publisher instanceof TopicPublisher) {
                TopicView view = TopicView.getView();
                Map<String, Topic> results = ((TopicPublisher) publisher).getResults();
                view.showResults(results);
                return;
            }
        }
    }

    private void releaseLogging() {
        System.setOut(out);
        System.setErr(err);
    }

    /**
   * Sets whether the verification should just run or step.
   * @param step if true, the verification will step through each state
   */
    public void setStepRun(boolean step) {
        this.step = step;
    }
}
