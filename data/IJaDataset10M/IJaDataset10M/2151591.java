package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Wrapper to access dex.jar through reflection.
 * <p/>Since there is no proper api to call the method in the dex library, this wrapper is going
 * to access it through reflection.
 */
public final class DexWrapper {

    private static final String DEX_MAIN = "com.android.dx.command.dexer.Main";

    private static final String DEX_CONSOLE = "com.android.dx.command.DxConsole";

    private static final String DEX_ARGS = "com.android.dx.command.dexer.Main$Arguments";

    private static final String MAIN_RUN = "run";

    private Method mRunMethod;

    private Constructor<?> mArgConstructor;

    private Field mArgOutName;

    private Field mArgVerbose;

    private Field mArgJarOutput;

    private Field mArgFileNames;

    private Field mConsoleOut;

    private Field mConsoleErr;

    /**
     * Loads the dex library from a file path.
     * 
     * The loaded library can be used via
     * {@link DexWrapper#run(String, String[], boolean, PrintStream, PrintStream)}.
     * 
     * @param osFilepath the location of the dex.jar file.
     * @return an IStatus indicating the result of the load.
     */
    public synchronized IStatus loadDex(String osFilepath) {
        try {
            File f = new File(osFilepath);
            if (f.isFile() == false) {
                return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, String.format(Messages.DexWrapper_s_does_not_exists, osFilepath));
            }
            URL url = f.toURL();
            URLClassLoader loader = new URLClassLoader(new URL[] { url }, DexWrapper.class.getClassLoader());
            Class<?> mainClass = loader.loadClass(DEX_MAIN);
            Class<?> consoleClass = loader.loadClass(DEX_CONSOLE);
            Class<?> argClass = loader.loadClass(DEX_ARGS);
            try {
                mRunMethod = mainClass.getMethod(MAIN_RUN, argClass);
                mArgConstructor = argClass.getConstructor();
                mArgOutName = argClass.getField("outName");
                mArgJarOutput = argClass.getField("jarOutput");
                mArgFileNames = argClass.getField("fileNames");
                mArgVerbose = argClass.getField("verbose");
                mConsoleOut = consoleClass.getField("out");
                mConsoleErr = consoleClass.getField("err");
            } catch (SecurityException e) {
                return createErrorStatus(Messages.DexWrapper_SecuryEx_Unable_To_Find_API, e);
            } catch (NoSuchMethodException e) {
                return createErrorStatus(Messages.DexWrapper_SecuryEx_Unable_To_Find_Method, e);
            } catch (NoSuchFieldException e) {
                return createErrorStatus(Messages.DexWrapper_SecuryEx_Unable_To_Find_Field, e);
            }
            return Status.OK_STATUS;
        } catch (MalformedURLException e) {
            return createErrorStatus(String.format(Messages.DexWrapper_Failed_to_load_s, osFilepath), e);
        } catch (ClassNotFoundException e) {
            return createErrorStatus(String.format(Messages.DexWrapper_Failed_to_load_s, osFilepath), e);
        }
    }

    /**
     * Runs the dex command.
     * @param osOutFilePath the OS path to the outputfile (classes.dex
     * @param osFilenames list of input source files (.class and .jar files)
     * @param verbose verbose mode.
     * @param outStream the stdout console
     * @param errStream the stderr console
     * @return the integer return code of com.android.dx.command.dexer.Main.run()
     * @throws CoreException
     */
    public synchronized int run(String osOutFilePath, String[] osFilenames, boolean verbose, PrintStream outStream, PrintStream errStream) throws CoreException {
        try {
            mConsoleErr.set(null, errStream);
            mConsoleOut.set(null, outStream);
            Object args = mArgConstructor.newInstance();
            mArgOutName.set(args, osOutFilePath);
            mArgFileNames.set(args, osFilenames);
            mArgJarOutput.set(args, false);
            mArgVerbose.set(args, verbose);
            Object res = mRunMethod.invoke(null, args);
            if (res instanceof Integer) {
                return ((Integer) res).intValue();
            }
            return -1;
        } catch (IllegalAccessException e) {
            throw new CoreException(createErrorStatus(String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, e.getMessage()), e));
        } catch (InstantiationException e) {
            throw new CoreException(createErrorStatus(String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, e.getMessage()), e));
        } catch (InvocationTargetException e) {
            throw new CoreException(createErrorStatus(String.format(Messages.DexWrapper_Unable_To_Execute_Dex_s, e.getMessage()), e));
        }
    }

    private static IStatus createErrorStatus(String message, Exception e) {
        AdtPlugin.log(e, message);
        AdtPlugin.printErrorToConsole(Messages.DexWrapper_Dex_Loader, message);
        return new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID, message, e);
    }
}
