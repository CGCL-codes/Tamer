package tm.confTester;

import java.applet.AppletStub;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;
import tm.ArgPackage;
import tm.TMMainFrame;
import tm.TMMainFrameAppletStub;
import tm.interfaces.ExternalCommandInterface;
import tm.interfaces.StatusProducer;
import tm.utilities.Assert;

/** Represent the TM to the conformance tester.
 * @author theo
 *
 */
class TMCompilerAdapter implements CompilerAdapter {

    static final Pattern pattern = Pattern.compile(".*\\.java|.*\\.jav|.*\\.cpp|.*\\.c++|.*\\.cxx|.*\\.c");

    public static final int LIMIT = 100000;

    TMMainFrame theMainFrame = null;

    int state = UNINIT;

    String compilerErrorString = null;

    String executeErrorString = null;

    String executeOutputString = null;

    /** 
     * @see tm.confTester.CompilerAdapter#init()
     */
    public void init() {
        if (theMainFrame != null) {
            theMainFrame.dispose();
            theMainFrame = null;
        }
        try {
            File cwd = new File(".");
            String cwd_string = cwd.getAbsolutePath() + "/";
            URL codeDir = new File(cwd_string).toURL();
            URL docDir = codeDir;
            AppletStub stub = null;
            stub = new TMMainFrameAppletStub(codeDir, docDir);
            ActionListener exitListener = new DoNothing();
            ArgPackage argPackage = new ArgPackage();
            theMainFrame = new TMMainFrame(stub, exitListener, argPackage, true);
            theMainFrame.setTestMode(true);
            state = INIT;
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            state = UNINIT;
        }
        compilerErrorString = null;
        executeErrorString = null;
        executeOutputString = null;
    }

    private class DoNothing implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            theMainFrame.setVisible(false);
        }
    }

    /** 
     * @see tm.confTester.CompilerAdapter#getState()
     */
    public int getState() {
        return state;
    }

    /** 
     * @see tm.confTester.CompilerAdapter#compile(java.io.File, java.lang.String)
     */
    public void compile(File directory, String fileName) {
        Assert.check(state == INIT);
        try {
            theMainFrame.loadLocalFile(directory, fileName);
            if (theMainFrame.getStatusCode() == StatusProducer.FILE_LOADED) {
                state = READY_TO_RUN;
            } else {
                state = COMPILE_FAILED;
                compilerErrorString = theMainFrame.getStatusMessage();
            }
        } catch (Throwable e) {
            state = COMPILE_FAILED;
            compilerErrorString = "Unexpected tm exception: " + e.getMessage();
        }
    }

    /** 
     * @see tm.confTester.CompilerAdapter#run(String)
     */
    public void run(String input) {
        Assert.check(state == READY_TO_RUN);
        try {
            theMainFrame.addInputString(input + (char) 0);
            int count = 0;
            int tmStatus;
            do {
                theMainFrame.intoSub();
                tmStatus = theMainFrame.getStatusCode();
                count++;
            } while ((tmStatus == StatusProducer.FILE_LOADED || tmStatus == StatusProducer.READY) && count < LIMIT);
            if (count == LIMIT) {
                state = RUN_FAILED;
                executeErrorString = "Infinite loop?";
            } else if (tmStatus == StatusProducer.EXECUTION_COMPLETE) {
                state = RUN_SUCCEEDED;
                executeOutputString = theMainFrame.getOutputString();
            } else if (tmStatus == StatusProducer.EXECUTION_FAILED) {
                state = RUN_FAILED;
                executeErrorString = theMainFrame.getStatusMessage();
            } else {
                state = RUN_FAILED;
                executeErrorString = "Unexpected tm status " + tmStatus;
            }
        } catch (Throwable e) {
            state = RUN_FAILED;
            executeErrorString = "Unexpected tm exeption " + e.getMessage();
        }
    }

    /** 
     * @see tm.confTester.CompilerAdapter#reinit()
     */
    public void reinit() {
        Assert.check(state == RUN_FAILED || state == RUN_SUCCEEDED);
        try {
            theMainFrame.reStart();
            if (theMainFrame.getStatusCode() == StatusProducer.FILE_LOADED) {
                state = READY_TO_RUN;
            } else {
                Assert.check(false);
            }
        } catch (Throwable e) {
            Assert.check(false);
        }
    }

    /** 
     * @see tm.confTester.CompilerAdapter#getCompilerErrorOutput()
     */
    public String getCompilerErrorOutput() {
        return compilerErrorString;
    }

    /** 
     * @see tm.confTester.CompilerAdapter#getExecuteErrorString()
     */
    public String getExecuteErrorString() {
        return executeErrorString;
    }

    /** 
     * @see tm.confTester.CompilerAdapter#getExecuteOutputString()
     */
    public String getExecuteOutputString() {
        return executeOutputString;
    }

    /** 
     * @see tm.confTester.CompilerAdapter#isFileNameSuitable(java.lang.String)
     */
    public boolean isFileNameSuitable(String fileName) {
        return pattern.matcher(fileName).matches();
    }
}
