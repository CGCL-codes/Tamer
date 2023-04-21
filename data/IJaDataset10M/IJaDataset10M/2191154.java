package jsclang.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import jsclang.module.api.ISCCommunicationModule;
import jsclang.module.api.InitializationException;
import jsclang.module.api.impl.JSCLangOSCModule;

/**
 * Default implementation of JSCLangCallback.
 * 
 * @author Dieter Kleinrath (kleinrath@mur.at)
 * 
 */
class CallbackThread extends JSCLangCallback {

    public CallbackThread() {
        super();
        this.setName("JSCLang Main");
        this.setDaemon(true);
    }

    @Override
    public synchronized void openTextFile(String path, int rangeStart, int rangeSize) {
        if (JSCLangMain.getInstance().getOpenTextFile() != null) {
            JSCLangMain.getInstance().getOpenTextFile().openTextFile(path, rangeStart, rangeSize);
        } else {
            writeInLogWindow("ERROR in JSCLangCallback : No IOpenTextFile set.");
        }
    }

    @Override
    public synchronized void writeInLogWindow(String message) {
        JSCLangMain.getInstance().postInLog(message);
    }
}

/**
 * This singleton class is responsible for all communications with
 * the JSCLangModule.
 * 
 * @author Dieter Kleinrath (kleinrath@mur.at)
 * 
 */
public final class JSCLangMain {

    /**
	 * The singleton instance
	 */
    private static JSCLangMain singleMain;

    private CallbackThread singleThread;

    private List<IJSCLogsink> logSinks = Collections.synchronizedList(new ArrayList<IJSCLogsink>());

    private String workingDirectory = null;

    private IJSCOpenTextFile openTextFile;

    private Boolean compiledOK = false;

    private IJSCErrorLog errorlog = null;

    /**
	 * The communication module used to communicate with SC
	 */
    private ISCCommunicationModule communicationModule;

    private boolean printAllInterpreterMessages = false;

    /**
	 * Returns the singleton of {@link JSCLangMain}
	 * @return Singleton instance
	 */
    public static synchronized JSCLangMain getInstance() {
        if (JSCLangMain.singleMain == null) {
            JSCLangMain.singleMain = new JSCLangMain();
        }
        return JSCLangMain.singleMain;
    }

    /**
	 * Constructs an instance with the default communication module.
	 */
    private JSCLangMain() {
        this(new JSCLangOSCModule());
    }

    /**
	 * Constructs an instance with the given communication module
	 * @param communicationModule Module to use to communicate with SuperCollider
	 */
    private JSCLangMain(ISCCommunicationModule communicationModule) {
        this.communicationModule = communicationModule;
    }

    /**
	 * Returns the current communication module
	 * @return Communication module used to communicate with SuperCollider
	 */
    public ISCCommunicationModule getCommunicationModule() {
        return communicationModule;
    }

    /**
	 * Replaces the communication module with the given one.
	 * @param communicationModule Communication module to use
	 */
    public void setCommunicationModule(ISCCommunicationModule communicationModule) {
        this.communicationModule = communicationModule;
    }

    public boolean isPrintAllInterpreterMessages() {
        return printAllInterpreterMessages;
    }

    public void setPrintAllInterpreterMessages(boolean printAllInterpreterMessages) {
        this.printAllInterpreterMessages = printAllInterpreterMessages;
    }

    /**
	 * This starts up SuperCollider. Make sure you have set the
	 * WorkingDirectory correctly before calling this method.
	 */
    public void initJSCLang() {
        if (singleThread == null) {
            this.compiledOK = false;
            singleThread = new CallbackThread();
            singleThread.start();
        }
        return;
    }

    public boolean isInitJSCLang() {
        if (singleThread != null) {
            return singleThread.isInit();
        }
        return false;
    }

    /**
	 *  Stops JSCLang. This method is only intended to be used when the
	 *  main thread stops using JSCLang.
	 */
    public void stopJSCLang() {
        if (getCompiledOK() && isInitJSCLang()) {
            communicationModule.sendMain("stop");
            communicationModule.sendMain("shutdown");
            this.compiledOK = false;
        }
        if (singleThread != null) {
            if (singleThread.isInit()) {
            }
            singleThread = null;
        }
    }

    public void addLogsink(IJSCLogsink logsink) {
        logSinks.add(logsink);
    }

    public void removeLogsink(IJSCLogsink logsink) {
        logSinks.remove(logsink);
    }

    public IJSCLogsink[] getLogSinks() {
        return logSinks.toArray(new IJSCLogsink[logSinks.size()]);
    }

    public synchronized void setOpenTextFile(IJSCOpenTextFile openTextFile) {
        this.openTextFile = openTextFile;
    }

    public IJSCOpenTextFile getOpenTextFile() {
        return openTextFile;
    }

    public CallbackThread getSingleThread() {
        return singleThread;
    }

    public synchronized void postInLog(String message) {
        if ((this.logSinks != null) && (this.logSinks.size() != 0)) {
            for (Iterator<IJSCLogsink> all = this.logSinks.iterator(); all.hasNext(); ) {
                IJSCLogsink each = all.next();
                each.post(message);
            }
        } else {
            System.out.println("ERROR in JSCLangMain: No Logsink set. Message was:");
            System.out.println(message);
        }
    }

    public synchronized void postInLog(String message, int style) {
        if ((this.logSinks != null) && (this.logSinks.size() != 0)) {
            for (Iterator<IJSCLogsink> all = this.logSinks.iterator(); all.hasNext(); ) {
                IJSCLogsink each = all.next();
                each.post(message, style);
            }
        } else {
            System.out.println("ERROR in JSCLangMain: No Logsink set. Message was:");
            System.out.println(message);
        }
    }

    public synchronized void setWorkingDirectory(String dir) {
        communicationModule.setWorkingDirectory(dir);
        this.workingDirectory = dir;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void compileLibrary() {
        if (isInitJSCLang()) {
            compiledOK = false;
            communicationModule.compileLibrary();
            if (!communicationModule.compiledOK()) {
                JSCCommunicationManager.getDefault().fireCompilationChangedEvent(false);
            }
        }
    }

    public Boolean getCompiledOK() {
        return (compiledOK && communicationModule.compiledOK());
    }

    public void setCompiledOK(Boolean compiledOK) {
        this.compiledOK = compiledOK;
    }

    /**
	 * Sends the String to SC and interprets it.
	 */
    public synchronized void interpret(final String cmdLine) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.interpret(cmdLine);
        }
    }

    /**
	 * Sends a String to be interpreted by SuperCollider and posts the command line to the console.
	 */
    public synchronized void interpretPrint(String cmdLine) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.sendMain(cmdLine, "interpretPrintCmdLine");
        }
    }

    public void stopSwingOSC() {
        interpret("g.quit");
    }

    public void startSwingOSC() {
        interpret("g.waitForBoot({ s.makeWindow(); });");
    }

    public void startServer() {
        interpret("Server.default.boot");
    }

    public void stopServer() {
        interpret("s.sendMsg(\"/quit\");");
    }

    public void setErrorlog(IJSCErrorLog errorlog) {
        this.errorlog = errorlog;
    }

    public IJSCErrorLog getErrorlog() {
        return errorlog;
    }

    public void logError(Throwable error) {
        if (errorlog != null) {
            errorlog.log(error);
        } else {
            error.printStackTrace();
        }
    }

    public void logError(Throwable error, String message) {
        if (errorlog != null) {
            errorlog.log(error, message);
        } else {
            error.printStackTrace();
            System.err.println(message);
        }
    }

    public boolean compiledOK() {
        return communicationModule.compiledOK();
    }

    public void initialize(JSCLangCallback callback) throws InitializationException {
        communicationModule.initialize(callback);
    }

    public String getUserAppSupportDirectory() {
        return communicationModule.getUserAppSupportDirectory();
    }

    public void setUserAppSupportDirectory(String str) {
        communicationModule.setUserAppSupportDirectory(str);
    }

    public String getSystemAppSupportDirectory() {
        return communicationModule.getSystemAppSupportDirectory();
    }

    public void setSystemAppSupportDirectory(String str) {
        communicationModule.setSystemAppSupportDirectory(str);
    }

    public String getUserExtensionDirectory() {
        return communicationModule.getUserExtensionDirectory();
    }

    public void setUserExtensionDirectory(String str) {
        communicationModule.setUserExtensionDirectory(str);
    }

    public String getSystemExtensionDirectory() {
        return communicationModule.getSystemExtensionDirectory();
    }

    public void setSystemExtensionDirectory(String str) {
        communicationModule.setSystemExtensionDirectory(str);
    }

    /**
	 * Sends a command to the GUI that a new window should be opened, where
	 * the implementations of the given SC-element (Class, method or field)
	 * are posted. The implementation depends on the client.
	 * 
	 * If only one implementation is found, the source file of the implemented
	 * element should be opened at the correct position.
	 * 
	 * @param element
	 */
    public void implementationsOf(String element) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.sendMain(element, "methodTemplates");
        }
    }

    /**
	 * Sends a command to the GUI that a new window should be opened, where
	 * the definition of the given SC-Class
	 * is posted. The implementation depends on the client.
	 * 
	 * TODO: In the new JSCEclipse source code, this information is get directly from the AST
	 * so this method will most likely be unnecessary
	 * 
	 * @param element
	 */
    public void openClassDefinition(String element) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.sendMain(element, "openWinCodeFile");
        }
    }

    /**
	 * Sends a command to the GUI that a new window should be opened, where
	 * all references to the given SC-element (Class, method or field)
	 * are posted. The implementation depends on the client.
	 * 
	 * If only one reference is found, the source file of the referring
	 * element should be opened at the correct position.
	 * 
	 * TODO: In the new JSCEclipse source code, this information is get directly from the AST
	 * so this method will most likely be unnecessary
	 * @param element
	 */
    public void referencesTo(String element) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.sendMain(element, "methodReferences");
        }
    }

    /**
	 * Stops the Interpreter
	 */
    public void stopSound() {
        if (isInitJSCLang() && getCompiledOK()) {
            sendMainInThread("stop");
        }
    }

    public void sendMainInThread(final String methodName) {
        if (isInitJSCLang() && getCompiledOK()) {
            Thread t = new Thread(new Runnable() {

                public void run() {
                    communicationModule.sendMain(methodName);
                }
            });
            t.setName("send Main");
            t.run();
        }
    }

    public void sendMain(String methodName) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.sendMain(methodName);
        }
    }

    public void sendMain(String cmdLine, String methodName) {
        if (isInitJSCLang() && getCompiledOK()) {
            communicationModule.sendMain(cmdLine, methodName);
        }
    }
}
