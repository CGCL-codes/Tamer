package com.thesett.maven.run;

import java.util.Iterator;

/**
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * </table></pre>
 *
 * @author                       Rupert Smith
 * @goal                         prod-script
 * @phase                        package
 * @requiresDependencyResolution test
 */
public class ProductionScriptGenMojo extends ScriptGenMojo {

    private static final String JAR_DIR_PREFIX_UNIX = "${assembly.jar.dir.unix}";

    private static final String JAR_DIR_PREFIX_WINDOWS = "${assembly.jar.dir.windows}";

    /** {@inheritDoc} */
    protected String appendClasspath(String commandLine, boolean unix) {
        String pathSeperator;
        String seperator;
        String classpathDirPrefix;
        if (unix) {
            pathSeperator = "/";
            seperator = ":";
            classpathDirPrefix = JAR_DIR_PREFIX_UNIX;
        } else {
            pathSeperator = "\\";
            seperator = ";";
            classpathDirPrefix = JAR_DIR_PREFIX_WINDOWS;
        }
        for (Iterator i = classpathElements.iterator(); i.hasNext(); ) {
            String cpPath = (String) i.next();
            int lastSlash = cpPath.lastIndexOf("/");
            int lastBackslash = cpPath.lastIndexOf("\\");
            int lastPathSeperator = (lastSlash > lastBackslash) ? lastSlash : lastBackslash;
            if (lastPathSeperator != -1) {
                cpPath = cpPath.substring(lastPathSeperator + 1);
            }
            if (cpPath.endsWith(".jar")) {
                commandLine += classpathDirPrefix + pathSeperator + cpPath + seperator;
            }
        }
        commandLine += classpathDirPrefix + pathSeperator + outputJar + ".jar";
        return commandLine;
    }
}
