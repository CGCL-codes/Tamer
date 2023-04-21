package sun.misc;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.File;
import java.util.StringTokenizer;

public final class MIDPLauncher {

    private static String suitePath[] = new String[0];

    public static String[] getMidletSuitePath() {
        return suitePath;
    }

    public static void main(String args[]) {
        int i, j, num;
        File midppath[] = new File[0];
        String midppathString = null;
        String suitepathString = null;
        StringTokenizer components;
        String mainArgs[];
        int numMainArgs = args.length;
        for (i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-midppath")) {
                midppathString = args[++i];
                components = new StringTokenizer(midppathString, System.getProperty("path.separator", ":"));
                num = components.countTokens();
                midppath = new File[num];
                for (j = 0; j < num; j++) {
                    midppath[j] = new File(components.nextToken());
                }
                args[i - 1] = args[i] = null;
                numMainArgs -= 2;
            } else if (arg.equals("-suitepath")) {
                suitepathString = args[++i];
                components = new StringTokenizer(suitepathString, System.getProperty("path.separator", ":"));
                num = components.countTokens();
                suitePath = new String[num];
                for (j = 0; j < num; j++) {
                    suitePath[j] = components.nextToken();
                }
                args[i - 1] = args[i] = null;
                numMainArgs -= 2;
            }
        }
        int k = 0;
        mainArgs = new String[numMainArgs];
        for (j = 0; j < args.length; j++) {
            if (args[j] != null) {
                mainArgs[k++] = args[j];
            }
        }
        MIDPImplementationClassLoader midpImplCL = MIDPConfig.newMIDPImplementationClassLoader(midppath);
        String loaderName = null;
        try {
            loaderName = System.getProperty("com.sun.midp.mainClass.name", "com.sun.midp.main.CdcMIDletSuiteLoader");
            Class suiteloader = midpImplCL.loadClass(loaderName);
            Class loaderArgs[] = { mainArgs.getClass() };
            Method mainMethod = suiteloader.getMethod("main", loaderArgs);
            Object args2[] = { mainArgs };
            mainMethod.invoke(null, args2);
        } catch (ClassNotFoundException ce) {
            System.err.println("Can't find " + loaderName);
        } catch (NoSuchMethodException ne) {
            System.err.println("Can't access MIDletSuiteLoader main()");
        } catch (IllegalAccessException ie) {
            System.err.println("Can't invoke MIDletSuiteLoader main()");
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
    }
}
