package uk.co.nimp.scard.samplecode;

import com.atolsystems.atolutilities.AFileChooser;
import com.atolsystems.atolutilities.AFileUtilities;
import com.atolsystems.atolutilities.ATimeUtilities;
import com.atolsystems.atolutilities.CommandLine;
import com.atolsystems.atolutilities.CommandLine.Arg;
import com.atolsystems.atolutilities.CommandLine.ArgDef;
import com.atolsystems.atolutilities.CommandLine.ArgHandler;
import com.atolsystems.atolutilities.InvalidCommandLineException;
import com.atolsystems.atolutilities.PlugInLoader;
import com.atolsystems.atolutilities.SimpleArgHandler;
import com.atolsystems.atolutilities.StopRequestFromUserException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardNotPresentException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.co.nimp.scard.AbstractSmartCardTask;
import uk.co.nimp.scard.GenericTerminal;
import uk.co.nimp.scard.Iso7816;
import uk.co.nimp.scard.ScardException;
import uk.co.nimp.scard.ScardLogHandler;
import uk.co.nimp.scard.SmartCardTask;
import uk.co.nimp.smartcard.Apdu;
import uk.co.nimp.smartcard.UnexpectedCardResponseException;

public class GetRandomData extends AbstractSmartCardTask {

    public static final String LAST_DATA_FILE_PATH_KEY = "GetRandomDataLastDataFilePath";

    protected File outputFile;

    protected boolean outputFileClonned = false;

    protected Integer dataSize;

    protected int maxLeDataSize = 256;

    protected String getRandomDataCmd = "0084000000";

    protected ArrayList<String> AIS31Tests = new ArrayList<String>();

    protected boolean AIS31Verbose = false;

    protected boolean ignoreSw = false;

    protected boolean coldReset = false;

    OutputFileArgHandler outputFileArgHandler = new OutputFileArgHandler();

    GetRnDDataCmdArgHandler getRnDDataCmdArgHandler = new GetRnDDataCmdArgHandler();

    public GetRandomData() {
    }

    public GetRandomData(GetRandomData task) {
        super(task);
        outputFile = task.outputFile;
        outputFileClonned = true;
        task.outputFileClonned = true;
        dataSize = task.dataSize;
        maxLeDataSize = task.maxLeDataSize;
        AIS31Tests = task.AIS31Tests;
        AIS31Verbose = task.AIS31Verbose;
        ignoreSw = task.ignoreSw;
        coldReset = task.coldReset;
        getRandomDataCmd = task.getRandomDataCmd;
    }

    @Override
    public SmartCardTask clone() {
        GetRandomData copy = new GetRandomData(this);
        return copy;
    }

    File askForFile() {
        AFileChooser fileChooser;
        fileChooser = new AFileChooser((java.awt.Frame) null, true, LAST_DATA_FILE_PATH_KEY);
        fileChooser.setDialogTitle("Select a file to save random data");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("raw data file", "dat");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        long start = System.nanoTime();
        int option = fileChooser.showOpenDialog(null);
        deadTime += System.nanoTime() - start;
        fileChooser.dispose();
        if (option != JFileChooser.APPROVE_OPTION) {
            throw new StopRequestFromUserException();
        }
        fileChooser.saveCurrentPath();
        outputFile = fileChooser.getSelectedFile();
        outputFileClonned = false;
        return outputFile;
    }

    void askForDataSize() {
        String baseMessage = "Please input the size of random data to get from the card, in KB.";
        String message = "";
        do {
            try {
                long start = System.nanoTime();
                String inputValue = JOptionPane.showInputDialog(message + "\n" + baseMessage);
                deadTime += System.nanoTime() - start;
                dataSize = Integer.decode(inputValue);
                message = "Number must be positive.";
            } catch (NumberFormatException e) {
                message = "Number not recognized.";
            } catch (NullPointerException e) {
                throw new StopRequestFromUserException();
            }
        } while (dataSize < 0);
        dataSize = dataSize * 1024;
    }

    static final String ARG_SET_OUTPUT_FILE = "outputFile:";

    static final String ARG_SET_DATA_SIZE = "dataSize:";

    static final String ARG_SET_MAX_LE_DATA_SIZE = "maxLeDataSize:";

    static final String ARG_SET_GETRNDDATACMD = "getRndDataCmd:";

    static final String ARG_SET_AIS31_TEST = "ais31Test:";

    static final String ARG_SET_AIS31_VERBOSE = "ais31Verbose";

    static final String ARG_SET_IGNORE_SW = "ignoreSw";

    static final String ARG_SET_COLD_RESET = "coldReset";

    class OutputFileArgHandler implements ArgHandler {

        public boolean inspectArg(Arg arg, CommandLine cl) {
            outputFile = arg.getSingleFile(ARG_SET_OUTPUT_FILE);
            outputFileClonned = false;
            return true;
        }

        public void endOfCommandLineInspection(CommandLine cl) {
            if (null == outputFile) {
                askForFile();
            }
        }

        public boolean processArg(Arg arg, CommandLine cl) {
            return true;
        }

        public void endOfCommandLineProcessing(CommandLine cl) {
        }
    }

    class GetRnDDataCmdArgHandler implements ArgHandler {

        public boolean inspectArg(Arg arg, CommandLine cl) {
            getRandomDataCmd = arg.getString(ARG_SET_GETRNDDATACMD);
            Apdu getChallengeApdu = new Apdu(getRandomDataCmd);
            if (getChallengeApdu.getExpectedLe() != 256) {
                throw new InvalidCommandLineException("Command to get random data must have LE=256, other case are not supported");
            }
            return true;
        }

        public void endOfCommandLineInspection(CommandLine cl) {
        }

        public boolean processArg(Arg arg, CommandLine cl) {
            return true;
        }

        public void endOfCommandLineProcessing(CommandLine cl) {
        }
    }

    @Override
    public LinkedList<ArgDef> getArgDefs() {
        LinkedList<ArgDef> out = new LinkedList<ArgDef>();
        out.add(new ArgDef(ARG_SET_GETRNDDATACMD, getRnDDataCmdArgHandler));
        out.add(new ArgDef(ARG_SET_OUTPUT_FILE, outputFileArgHandler));
        out.add(new ArgDef(ARG_SET_DATA_SIZE, this));
        out.add(new ArgDef(ARG_SET_MAX_LE_DATA_SIZE, this));
        out.add(new ArgDef(ARG_SET_AIS31_TEST, this));
        out.add(new ArgDef(ARG_SET_AIS31_VERBOSE, this));
        out.add(new ArgDef(ARG_SET_IGNORE_SW, this));
        out.add(new ArgDef(ARG_SET_COLD_RESET, this));
        return out;
    }

    @Override
    public boolean inspectArg(Arg arg, CommandLine cl) {
        if (arg.value.startsWith(ARG_SET_DATA_SIZE)) {
            int unit;
            if (arg.value.endsWith("b")) {
                unit = 1;
            } else if (arg.value.endsWith("k")) {
                unit = 1024;
            } else if (arg.value.endsWith("m")) {
                unit = 1024 * 1024;
            } else {
                throw new InvalidCommandLineException("parameter must end by 'b', 'k' or 'm' to indicate byte, kilo-bytes or mega-bytes respectively.");
            }
            String num = arg.value.substring(ARG_SET_DATA_SIZE.length(), arg.value.length() - 1);
            dataSize = Integer.decode(num);
            if (null == dataSize) {
                return true;
            }
            if (dataSize < 0) {
                throw new InvalidCommandLineException("parameter must be positive.");
            }
            dataSize = dataSize * unit;
        } else if (arg.value.startsWith(ARG_SET_MAX_LE_DATA_SIZE)) {
            maxLeDataSize = arg.getInteger(ARG_SET_MAX_LE_DATA_SIZE);
        } else if (arg.value.startsWith(ARG_SET_AIS31_TEST)) {
            AIS31Tests.add(arg.getString(ARG_SET_AIS31_TEST));
        } else if (arg.value.startsWith(ARG_SET_AIS31_VERBOSE)) {
            AIS31Verbose = true;
        } else if (arg.value.startsWith(ARG_SET_IGNORE_SW)) {
            ignoreSw = true;
        } else if (arg.value.startsWith(ARG_SET_COLD_RESET)) {
            coldReset = true;
        }
        return true;
    }

    @Override
    public void endOfCommandLineInspection(CommandLine cl) {
        if (null == dataSize) {
            askForDataSize();
        }
    }

    @Override
    public boolean processArg(Arg arg, CommandLine cl) {
        return true;
    }

    public File getFileFromUser(int argId, boolean isOutputFile) {
        return askForFile();
    }

    protected static int coreFunction(GenericTerminal gt, File targetFile, int dataSize, int nRun, String getRandomDataCmd, int maxLeDataSize, List<String> AIS31Tests, boolean AIS31Verbose, boolean ignoreSw, boolean coldReset) throws ScardException {
        int status = SmartCardTask.ERROR;
        FileOutputStream fos = null;
        File baseFile = targetFile;
        int successCnt = 0;
        try {
            for (int runNumber = 0; runNumber < nRun; runNumber++) {
                if (nRun > 1) {
                    String fileName = baseFile.getCanonicalPath() + " - run" + runNumber;
                    targetFile = new File(fileName);
                }
                fos = new FileOutputStream(targetFile);
                try {
                    gt.logLine(ScardLogHandler.LOG_INFO, "targetFile:" + targetFile.getCanonicalPath());
                    gt.logLine(ScardLogHandler.LOG_INFO, "dataSize=" + dataSize + " bytes");
                    gt.logLine(ScardLogHandler.LOG_INFO, "maxLeDataSize=" + maxLeDataSize + " bytes");
                    Apdu getChallengeApdu = new Apdu(getRandomDataCmd);
                    getChallengeApdu.setExpectedLe(maxLeDataSize);
                    if (!ignoreSw) getChallengeApdu.setExpectedSw(Iso7816.SW_SUCCESS);
                    int nLoops = dataSize / maxLeDataSize;
                    for (int i = 0; i < nLoops; i++) {
                        if (coldReset) gt.coldConnect();
                        gt.sendApdu(getChallengeApdu);
                        fos.write(getChallengeApdu.getLeData());
                    }
                    int remaining = dataSize % maxLeDataSize;
                    if (remaining > 0) {
                        getChallengeApdu.setExpectedLe(remaining);
                        gt.sendApdu(getChallengeApdu);
                        fos.write(getChallengeApdu.getLeData());
                    }
                    fos.close();
                    for (String AIS31Test : AIS31Tests) {
                        gt.logLine(ScardLogHandler.LOG_INFO, "Launch AIS31 test condition '" + AIS31Test + "' on file " + targetFile);
                        execAis31Test(AIS31Test, AIS31Verbose, targetFile);
                    }
                    successCnt++;
                } catch (UnexpectedCardResponseException ex) {
                    Logger.getLogger(GetRandomData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CardNotPresentException ex) {
                    Logger.getLogger(GetRandomData.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GetRandomData.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        status = SmartCardTask.SYSTEM_ERROR;
                        Logger.getLogger(GetRandomData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetRandomData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetRandomData.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (nRun == successCnt) {
            status = SmartCardTask.SUCCESS;
        }
        return status;
    }

    public static void execAis31Test(String AIS31Test, boolean AIS31Verbose, File targetFile) {
        try {
            String[] args = new String[3];
            args[0] = targetFile.getCanonicalPath();
            args[1] = AIS31Test;
            args[2] = Boolean.toString(AIS31Verbose);
            String[] classes = { "Evaluator.class", "Tester.class" };
            String[] classeNames = { "Tester" };
            invokeMain(classes, "Evaluator", classeNames, args);
        } catch (Throwable e) {
            RuntimeException re = new RuntimeException("Exception occured in AIS31 Evaluator tool", e);
            throw re;
        }
    }

    public int execute(GenericTerminal gt) throws ScardException {
        try {
            if (outputFileClonned) {
                String path = outputFile.getParentFile().getCanonicalPath();
                String baseName = outputFile.getName();
                String tName = AFileUtilities.generateValidFileName(gt.getName());
                String ext = AFileUtilities.extractFileExtension(tName);
                if (false == ext.isEmpty()) {
                    tName = tName.substring(0, tName.length() - ext.length() - 1);
                }
                String pathSeparator = System.getProperty("file.separator");
                StringBuilder name = new StringBuilder();
                name.append(path);
                name.append(pathSeparator);
                name.append(tName);
                name.append("-");
                name.append(baseName);
                outputFile = new File(name.toString());
            }
            int status = coreFunction(gt, outputFile, dataSize, nRun, getRandomDataCmd, maxLeDataSize, AIS31Tests, AIS31Verbose, ignoreSw, coldReset);
            setErrorStatus(status);
            return status;
        } catch (IOException ex) {
            setErrorStatus(SmartCardTask.ERROR);
            throw new RuntimeException(ex);
        } catch (Throwable e) {
            setErrorStatus(SmartCardTask.ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAboutMsg() {
        return "NimpSmartCard sample code: GetRandomData";
    }

    public static void invokeMain(String[] classFiles, String mainClassName, String[] classNames, String[] args) throws Throwable {
        PlugInLoader pl = new PlugInLoader(GetRandomData.class, "packageLocator");
        String here = AFileUtilities.getPackageUrl(GetRandomData.class, "packageLocator");
        String jarPrefix = "jar:file:/";
        int lastMarkPos = here.lastIndexOf("!");
        if (lastMarkPos != -1) {
            String jarFilePath = here.substring(jarPrefix.length(), lastMarkPos);
            pl.addJarFile(new File(jarFilePath));
        } else {
        }
        URL[] classUrls = new URL[classFiles.length];
        for (int i = 0; i < classFiles.length; i++) {
            classUrls[i] = ClassLoader.getSystemResource(classFiles[i]);
        }
        URLClassLoader ucl = new URLClassLoader(classUrls);
        for (int i = 0; i < classNames.length; i++) {
            Class miscClass = null;
            try {
                miscClass = ucl.loadClass(classNames[i]);
            } catch (Throwable e) {
                miscClass = pl.getPlugInClass(classNames[i]);
            }
        }
        Class c = null;
        try {
            c = ucl.loadClass(mainClassName);
        } catch (Throwable e) {
            c = pl.getPlugInClass(mainClassName);
        }
        Method main = c.getDeclaredMethod("main", args.getClass());
        Object[] arguments = new Object[1];
        arguments[0] = args;
        main.invoke(null, arguments);
    }

    public static void main(String[] args) throws Exception {
        execAis31Test("0", true, new File("a.txt"));
    }
}
