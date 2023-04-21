package org.dyndns.fichtner.jarrunner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.security.Permission;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * JarRunner supports the start of jar files identical to calling
 * "java -jar yourjar.jar". It supports an isolation mode (load an already
 * loaded class, see {@link #setIsolated(boolean)}) and is able to prevent
 * System#exit/Runtime#exit calls from inside the loaded jar (see
 * {@link #setPreventSystemExits(boolean)}).
 * 
 * @author Peter Fichtner
 */
public class JarRunner {

    public static class MethodSignature {

        private final String name;

        private final String[] parameterTypes;

        public MethodSignature(final String name, final Class<?>... parameterTypes) {
            this(name, extractClassNames(parameterTypes));
        }

        public MethodSignature(final String name, final String... parameterTypes) {
            this.name = name;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            final MethodSignature other = (MethodSignature) obj;
            return this.name.equals(other.getName()) && Arrays.equals(getParameterTypes(), other.getParameterTypes());
        }

        public String getName() {
            return this.name;
        }

        public String[] getParameterTypes() {
            return this.parameterTypes;
        }

        @Override
        public int hashCode() {
            return 51 + this.name.hashCode() + 17 * Arrays.hashCode(this.parameterTypes);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.name).append('(');
            for (int i = 0; ; i++) {
                builder.append(String.valueOf(this.parameterTypes[i]));
                if (i == (this.parameterTypes.length - 1)) return builder.append(')').toString();
                builder.append(", ");
            }
        }
    }

    private static class NoExitSecurityException extends SecurityException {

        private static final long serialVersionUID = 3970436780065880157L;

        private final int exitStatus;

        /**
		 * Creates a new NoExitSecurityException with the passed exitStatus.
		 * 
		 * @param exitStatus
		 */
        public NoExitSecurityException(final int exitStatus) {
            this.exitStatus = exitStatus;
        }

        /**
		 * Return the exit status that was passed to the
		 * {@link System#exit(int)} / {@link Runtime#exit(int)} call.
		 * 
		 * @return exit status
		 */
        public int getExitStatus() {
            return this.exitStatus;
        }
    }

    private static final String KEY_MAINCLASS = "Main-Class";

    private static final String KEY_CLASSPATH = "Class-Path";

    private static URL[] createURLArray(final File jarFile, final File[] classPath) {
        if (classPath == null || classPath.length == 0) {
            return new URL[] { FileUtil.toURL(jarFile) };
        }
        final URL[] urls = FileUtil.toURLs(jarFile.getAbsolutePath(), classPath);
        final URL[] result = new URL[urls.length + 1];
        result[0] = FileUtil.toURL(jarFile);
        System.arraycopy(urls, 0, result, 1, urls.length);
        return result;
    }

    private static String[] extractClassNames(final Class<?>[] argTypes) {
        if (argTypes == null || argTypes.length == 0) return new String[0];
        final String[] result = new String[argTypes.length];
        for (int i = 0; i < argTypes.length; i++) {
            result[i] = ClassUtil.getClassname(argTypes[i]);
        }
        return result;
    }

    private static String getClassPath(final JarFile jarFile) throws IOException {
        return getManifest(jarFile).getValue(KEY_CLASSPATH);
    }

    private static String getMainClass(final JarFile jarFile) throws IOException {
        return getManifest(jarFile).getValue(KEY_MAINCLASS);
    }

    private static Attributes getManifest(final JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes();
    }

    private static File[] splitClassPath(final String classPath) {
        if (classPath == null || classPath.length() == 0) {
            return new File[0];
        }
        final String[] split = classPath.split(File.separator);
        final File[] result = new File[split.length];
        for (int i = 0; i < split.length; i++) {
            result[i] = new File(split[i]);
        }
        return result;
    }

    private final File jarFile;

    private final String mainClass;

    private final File[] classPath;

    private MethodSignature method;

    private boolean isIsolated = true;

    private boolean preventSystemExits;

    /**
	 * Creates a new JarRunner using the passed jarFile and mainClass. Main
	 * class and classpath are determined by reading the jar file manifest.
	 * 
	 * @param jarFile jarFile containing the mainClass
	 * @throws IOException on errors reading the manifest
	 */
    public JarRunner(final File jarFile) throws IOException {
        this(jarFile, getMainClass(new JarFile(jarFile)));
    }

    /**
	 * Creates a new JarRunner using the passed jarFile and mainClass. The
	 * classpath is determined by reading the jar file manifest.
	 * 
	 * @param jarFile jarFile containing the mainClass
	 * @param mainClass mainClass to load
	 * @throws IOException on errors reading the manifest
	 */
    public JarRunner(final File jarFile, final String mainClass) throws IOException {
        this(jarFile, mainClass, getClassPath(new JarFile(jarFile)));
    }

    /**
	 * Creates a new JarRunner using the passed jarFile, mainClass and classpath
	 * (additional jars).
	 * 
	 * @param jarFile jarFile containing the mainClass
	 * @param mainClass mainClass to load
	 * @param classPath additional jars
	 */
    public JarRunner(final File jarFile, final String mainClass, final File[] classPath) {
        this(jarFile, mainClass, classPath, new MethodSignature("main", "java.lang.String[]"));
    }

    /**
	 * Creates a new JarRunner using the passed jarFile, mainClass, classpath
	 * (additional jars) and method to execute.
	 * 
	 * @param jarFile jarFile containing the mainClass
	 * @param mainClass mainClass to load
	 * @param classPath additional jars
	 * @param method method to execute
	 */
    public JarRunner(final File jarFile, final String mainClass, final File[] classPath, final MethodSignature method) {
        this.jarFile = jarFile;
        this.mainClass = mainClass;
        this.classPath = classPath;
        setMethod(method);
    }

    /**
	 * Creates a new JarRunner using the passed jarFile, mainClass and classpath
	 * (additional jars).
	 * 
	 * @param jarFile jarFile containing the mainClass
	 * @param mainClass mainClass to load
	 * @param classPath additional jars separated by {@link File#separator}
	 */
    public JarRunner(final File jarFile, final String mainClass, final String classPath) {
        this(jarFile, mainClass, splitClassPath(classPath));
    }

    /**
	 * Creates a new JarRunner using the passed jarFile and mainClass. Main
	 * class and classpath are determined by reading the jar file manifest.
	 * 
	 * @param filename jarFile containing the mainClass
	 * @throws IOException on errors reading the manifest
	 */
    public JarRunner(final String filename) throws IOException {
        this(new File(filename));
    }

    private URLClassLoader createClassLoader(final File jarFile, final File[] classPath) {
        final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        return new URLClassLoader(createURLArray(jarFile, classPath), this.isIsolated ? systemClassLoader.getParent() : systemClassLoader);
    }

    /**
	 * Execute the main class set and return the exit value of the exit call
	 * (see {@link #setPreventSystemExits(boolean)}). If no exit call is done
	 * the method will return <code>0</code>.
	 * 
	 * @param args arguments that should be passed to the main method
	 * @return exit value of <code>0</code> if no exit call is done
	 * @throws Exception Exception thrown my main method
	 */
    public int execute(final String... args) throws Exception {
        SecurityManager oldSecurityManager = null;
        if (this.preventSystemExits) {
            oldSecurityManager = System.getSecurityManager();
            installSecurityManager(oldSecurityManager);
        }
        try {
            loadMethod(createClassLoader(this.jarFile, this.classPath).loadClass(this.mainClass), this.method).invoke(null, new Object[] { args });
            return 0;
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            if (cause instanceof NoExitSecurityException) {
                return ((NoExitSecurityException) cause).getExitStatus();
            } else if (cause instanceof Exception) {
                throw (Exception) cause;
            } else if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new Exception(e);
            }
        } finally {
            if (this.preventSystemExits) {
                restoreSecurityManager(oldSecurityManager);
            }
        }
    }

    private void installSecurityManager(final SecurityManager securityManager) {
        System.setSecurityManager(new SecurityManager() {

            public void checkExit(final int status) {
                throw new NoExitSecurityException(status);
            }

            public void checkPermission(final Permission perm) {
                if (securityManager != null) {
                    securityManager.checkPermission(perm);
                }
            }

            public void checkPermission(final Permission perm, final Object context) {
                if (securityManager != null) {
                    securityManager.checkPermission(perm, context);
                }
            }
        });
    }

    /**
	 * Returns the isolation mode.
	 * 
	 * @return isolation mode
	 * @see #setIsolated(boolean)
	 */
    public boolean isIsolated() {
        return this.isIsolated;
    }

    /**
	 * If exit calls are caught <code>true</code> will be returned.
	 * 
	 * @return <code>true</code> if exit calls are caught
	 * @see #setPreventSystemExits(boolean)
	 */
    public boolean isPreventSystemExits() {
        return this.preventSystemExits;
    }

    private Method loadMethod(final Class<?> loadClass, final MethodSignature meth) throws NoSuchMethodException {
        for (final Method method : loadClass.getDeclaredMethods()) {
            if (matches(method, meth)) {
                return method;
            }
        }
        throw new NoSuchMethodException(loadClass.getName() + '#' + meth.toString());
    }

    private boolean matches(final Method method, final MethodSignature meth) {
        return method.getName().equals(meth.getName()) && Arrays.equals(extractClassNames(method.getParameterTypes()), meth.getParameterTypes());
    }

    private void restoreSecurityManager(final SecurityManager securityManager) {
        System.setSecurityManager(securityManager);
    }

    /**
	 * Set isolation mode. In isolation only the bootstrapped classes (classes
	 * found in the JRE like java.lang.String, java.lang.Object, java.util.List
	 * and so on) are reused. All other classes classes are searched inside the
	 * passed jar files.
	 * 
	 * @param isIsolated <code>true</code> for isolation mode otherwise
	 *            <code>false</code>
	 */
    public void setIsolated(final boolean isIsolated) {
        this.isIsolated = isIsolated;
    }

    /**
	 * Set the method that should be executed.
	 * 
	 * @param method the method that should be executed
	 */
    public void setMethod(final MethodSignature method) {
        this.method = method;
    }

    /**
	 * If set, {@link System#exit(int)} (or better said
	 * {@link Runtime#exit(int)}) calls are caught and the exit status will be
	 * returned.
	 * 
	 * @param preventSystemExits <code>true</code> if exit calls should be
	 *            caught
	 */
    public void setPreventSystemExits(final boolean preventSystemExits) {
        this.preventSystemExits = preventSystemExits;
    }
}
