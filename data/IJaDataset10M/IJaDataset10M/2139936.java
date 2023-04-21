package jsclang.module.api.impl;

import jsclang.module.JSCLangCallback;
import jsclang.module.JSCLangMain;

/**
 * This class is the wrapper for the JSCLangModule DLL.
 * It utilizes the Java Native Interface to communicate with
 * the SuperCollider language (SCLang)
 * <p>
 * Clients should use {@link JSCLangMain} instead of calling this class directly.
 * 
 * @see JSCLangMain
 * @author Dieter Kleinrath (kleinrath@mur.at)
 * 
 */
public final class JSCLangModule {

    private static boolean libraryLoaded = false;

    public static boolean isLibraryLoaded() {
        return libraryLoaded;
    }

    static {
        try {
            System.loadLibrary("JSCLangModule");
            libraryLoaded = true;
        } catch (UnsatisfiedLinkError e) {
            libraryLoaded = false;
            String message = "Warning: The library JSCLangModule was not loaded.\n" + "A lot of features of JSC will not work without the library JSCLangModule.\n" + "Have a look at the JSC homepage at 'http://jsce.sourceforge.net/' for details about this problem.";
            System.out.println(message);
        }
    }

    /**
	 * This method returns true if the SCClassLibrary was compiled correctly.
	 * 
	 * In JNI native code this method returns the value of the bool field compiledOK in PyrLexer.cpp.
	 * 
	 * @return True if the SCClassLibrary has been compiled correctly
	 */
    public static native boolean compiledOK();

    /**
	 * starts JSCLangModule and compiles the SCClassLibrary
	 */
    public static native void start();

    /**
	 * Stops JSCLangModule and stops the callbackthread
	 */
    public static native void stop();

    /**
	 * Compiles the SCClassLibrary.
	 * 
	 * In JNI native code this method compiles the SCClassLibrary by calling PyrLexer.compileLibrary()
	 */
    public static native void compileLibrary();

    /**
	 * Sets the commandline for the SC interpreter.
	 */
    public static native void setCmdLine(String str);

    /**
	 * Sends a message to the SC Interpreter instance. I.e. sendMain("interpretCmdLine") interprets the command line.
	 */
    public static native void sendMain(String str);

    /**
	 * Interprets the given String str without posting it to the console.
	 * @param str The String that will get interpreted.
	 */
    public static native void interpret(String str);

    /**
	 * Sets the callback for JSCLang.
	 * This method initializes the Module and starts the Interpreter thread. 
	 * 
	 * @see JSCLangCallback
	 */
    public static native void setJSCLangCallback(Object callback);

    /**
	 * <p>
	 * Sets the SuperCollider working Directory. This must have the
	 * SCClassLibrary as a subdirectory.
	 * </p>
	 * It is important to set the working directory of SuperCollider before
	 * starting up the Language. If the working directory is not set
	 * SuperCollider assumes that the SuperCollider directory is in the same
	 * path as JSCLang. example: setWorkingDirectory("C:\\SuperCollider3");
	 * 
	 * In JNI native code this method simply changes the working dirctory of
	 * C++ by calling "chdir(dir)";
	 */
    public static native void setWorkingDirectory(String str);

    /**
	 * shuts the SCClassLibrary down
	 * 
	 * In JNI native code this method calls PyrLexer.shutdownLibrary()
	 */
    public static native void shutdownLibrary();

    /**
	 * shuts the SCClassLibrary down and afterwards recompiles the SCClassLibrary
	 * 
	 * In JNI native code this method calls PyrLexer.shutdownLibrary and PyrLexer.compileLibrary()
	 */
    public static native void recompileLibrary();

    public static native String getUserAppSupportDirectory();

    public static native void setUserAppSupportDirectory(String str);

    public static native String getSystemAppSupportDirectory();

    public static native void setSystemAppSupportDirectory(String str);

    public static native String getUserExtensionDirectory();

    public static native void setUserExtensionDirectory(String str);

    public static native String getSystemExtensionDirectory();

    public static native void setSystemExtensionDirectory(String str);

    /**
	 * Convenience method for testing purposes. Not intented
	 * to be used for anything else.
	 * In JNI native code this method does nothing by default.
	 */
    public static native void testJSCLang();
}
