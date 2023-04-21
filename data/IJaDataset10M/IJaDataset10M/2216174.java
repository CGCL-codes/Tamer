package jragonsoft.javautil.cmdtool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;

/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: MakeClassPath.java 2486 2005-11-28 20:52:33Z zemian.deng $
 * @deprecated These script are not flexable enough. Use res/template.sh
 *             instead.
 */
public class MakeClassPath {

    /** Description of the Field */
    public static final String JAR_EXT_PATTERN = "\\.jar$";

    /** Description of the Method */
    public static void printExitHelp() {
        String help = "USAGE:" + "\n  MakeClassPath [options] JarFile [JarDir ...]" + "\n  Utility to help you construct a Java CLASSPATH string for copy" + "\n  and paste. Program takes any jar files and/or dir that contains" + "\n  jars, then build the CLASPATH string with the proper path" + "\n  seperator. If given dir doesn't contain any jar file, then the " + "\n  dir itself will be added to CLASSPATH." + "\n" + "\nNOTES:" + "\n  In Windows prompts, the output of mutiple lines are tricky to copy." + "\n  You may either make the Windows big enough to show single line, or" + "\n  redirect output to file then copy and paste." + "\n" + "\n  [options]" + "\n    -h      Help and version." + "\n    -j      Only includes jar files, No directories." + "\n    -r      Do recursive search on given directorie." + "\n" + "\nEXAMPLES:" + "\n  $ # Assume using wrapper scripts and PATH is set" + "\n  $ makeclasspath /opt/tomcat/common/lib" + "\n  $ makeclasspath /opt/tomcat/common/lib > tmp.txt" + "\n" + "\nCREDITS:" + "\n  ZMan Java Utility. <zemiandeng@gmail.com>" + "\n  $Id: MakeClassPath.java 4 2006-03-16 15:27:19Z zemian $";
        System.out.println(help);
        System.exit(0);
    }

    /**
	 * The main program for the MakeClassPath class
	 * 
	 * @param args
	 *            The command line arguments
	 */
    public static void main(String[] args) {
        GetOpt opt = new GetOpt(args);
        if (opt.isOpt("h") || opt.getArgsCount() < 1) {
            printExitHelp();
        }
        boolean isRecursive = opt.isOpt("r");
        boolean isJarOnly = opt.isOpt("j");
        ArrayList classpathList = new ArrayList();
        for (int i = 0; i < opt.getArgsCount(); i++) {
            File inputFile = new File(opt.getArg(i));
            if (inputFile.isDirectory()) {
                ArrayList searchResult = new ArrayList(Arrays.asList(FileUtils.globFiles(inputFile, JAR_EXT_PATTERN, isRecursive)));
                if (searchResult.size() == 0) {
                    if (!isJarOnly) {
                        classpathList.add(inputFile);
                    }
                } else {
                    classpathList.addAll(searchResult);
                }
            } else if (inputFile.getName().endsWith(".jar")) {
                classpathList.add(inputFile);
            }
        }
        StringBuffer sb = new StringBuffer();
        if (classpathList.size() >= 1) {
            File jarFile = (File) classpathList.get(0);
            sb.append(jarFile.getAbsolutePath());
        }
        for (int i = 1; i < classpathList.size(); i++) {
            File jarFile = (File) classpathList.get(i);
            sb.append(System.getProperty("path.separator"));
            sb.append(jarFile.getAbsolutePath());
        }
        System.out.println(sb.toString());
    }
}
