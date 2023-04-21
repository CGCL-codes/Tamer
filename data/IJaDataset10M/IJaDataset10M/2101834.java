package net.community.chest.apache.ant.build;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 31, 2008 12:51:35 PM
 */
public class BuildFileExecutor {

    public static final BuildEventsHandler.EventHandleResult runAntBuild(final ClassLoader cl, final String... args) throws Exception {
        final Class<?> amClass = cl.loadClass("org.apache.tools.ant.Main");
        final Object antMain = amClass.newInstance();
        {
            final Method pa = amClass.getDeclaredMethod("processArgs", String[].class);
            if (!pa.isAccessible()) pa.setAccessible(true);
            pa.invoke(antMain, (Object) args);
        }
        {
            final Method rb = amClass.getDeclaredMethod("runBuild", ClassLoader.class);
            if (!rb.isAccessible()) rb.setAccessible(true);
            rb.invoke(antMain, cl);
        }
        return BuildEventsHandler.EventHandleResult.FINISHED;
    }

    public static final BuildEventsHandler.EventHandleResult runAntBuild(final String... args) throws Exception {
        final Thread t = Thread.currentThread();
        return runAntBuild(t.getContextClassLoader(), args);
    }

    public static final BuildEventsHandler.EventHandleResult executeScript(final String filePath, final String targetName, final Class<?> loggerClass, final Class<?> hndlrClass, final ClassLoader cl, final Collection<String> xtraArgs) throws Exception {
        if ((null == filePath) || (filePath.length() <= 0)) throw new IllegalArgumentException("executeScript() no ANT script file path supplied");
        if (null == loggerClass) throw new IllegalStateException("executeScript(" + filePath + "[" + targetName + "]) no " + ExecutorLogger.class.getSimpleName() + " instance");
        final String ihClsName = (null == hndlrClass) ? null : hndlrClass.getName();
        if ((null == ihClsName) || (ihClsName.length() <= 0)) throw new IllegalStateException("executeScript(" + filePath + "[" + targetName + "]) no input handler class specified");
        final List<String> argsList;
        {
            final Collection<String> antFixedArgs = Arrays.asList("-buildfile", filePath, "-inputhandler", ihClsName, "-logger", loggerClass.getName());
            final int numExtraArgs = (null == xtraArgs) ? 0 : xtraArgs.size();
            argsList = new ArrayList<String>(antFixedArgs.size() + Math.max(0, numExtraArgs));
            argsList.addAll(antFixedArgs);
            if (numExtraArgs > 0) argsList.addAll(xtraArgs);
        }
        if ((targetName != null) && (targetName.length() > 0)) argsList.add(targetName);
        final String[] execArgs = argsList.toArray(new String[argsList.size()]);
        return runAntBuild(cl, execArgs);
    }

    public static final BuildEventsHandler.EventHandleResult executeScript(final String filePath, final String targetName, final Class<?> loggerClass, final Class<?> hndlrClass, final Collection<String> xtraArgs) throws Exception {
        final Thread t = Thread.currentThread();
        return executeScript(filePath, targetName, loggerClass, hndlrClass, t.getContextClassLoader(), xtraArgs);
    }
}
