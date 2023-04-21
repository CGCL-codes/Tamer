package com.erinors.tapestry.tapdoc.maven;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;

/**
 *
 * @author andyhot
 */
public class DirectJavadocExecutor implements JavadocExecutor {

    private String javadocExecutable;

    public void runJavadoc(Collection<CharSequence> classpaths, CharSequence docletpath, CharSequence doclet, CharSequence outputDirectory, Collection<CharSequence> sourcepaths, CharSequence packages) {
        StringBuilder formattedClasspath = new StringBuilder();
        for (CharSequence file : classpaths) {
            if (formattedClasspath.length() > 0) {
                formattedClasspath.append(File.pathSeparatorChar);
            }
            formattedClasspath.append('\"');
            formattedClasspath.append(file);
            formattedClasspath.append('\"');
        }
        StringBuilder formattedSourcePath = new StringBuilder();
        for (CharSequence file : sourcepaths) {
            if (formattedSourcePath.length() > 0) {
                formattedSourcePath.append(File.pathSeparatorChar);
            }
            formattedSourcePath.append('\"');
            formattedSourcePath.append(file);
            formattedSourcePath.append('\"');
        }
        StringBuilder command = new StringBuilder();
        try {
            command.append(getJavadocExecutable());
        } catch (IOException e) {
            command.append("java ");
        }
        command.append(" -cp ");
        command.append(formattedClasspath);
        command.append(" -docletpath \"");
        command.append(docletpath);
        command.append("\" -doclet ");
        command.append(doclet);
        command.append(" -d \"");
        command.append(outputDirectory);
        command.append("\" -sourcepath \"");
        command.append(formattedSourcePath);
        command.append("\" -subpackages ");
        command.append(packages);
        System.out.println(command);
        try {
            Runtime.getRuntime().exec(command.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJavadocExecutable() throws IOException {
        String javadocCommand = "javadoc" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");
        File javadocExe;
        if (StringUtils.isNotEmpty(javadocExecutable)) {
            javadocExe = new File(javadocExecutable);
            if (!javadocExe.exists() || !javadocExe.isFile()) {
                throw new IOException("The javadoc executable '" + javadocExe + "' doesn't exist or is not a file. " + "Verify the <javadocExecutable/> parameter.");
            }
            return javadocExe.getAbsolutePath();
        }
        if (SystemUtils.IS_OS_AIX) {
            javadocExe = new File(SystemUtils.getJavaHome() + File.separator + ".." + File.separator + "sh", javadocCommand);
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            javadocExe = new File(SystemUtils.getJavaHome() + File.separator + "bin", javadocCommand);
        } else {
            javadocExe = new File(SystemUtils.getJavaHome() + File.separator + ".." + File.separator + "bin", javadocCommand);
        }
        if (!javadocExe.exists() || !javadocExe.isFile()) {
            Properties env = CommandLineUtils.getSystemEnvVars();
            String javaHome = env.getProperty("JAVA_HOME");
            if (StringUtils.isEmpty(javaHome)) {
                throw new IOException("The environment variable JAVA_HOME is not correctly set.");
            }
            if ((!new File(javaHome).exists()) || (!new File(javaHome).isDirectory())) {
                throw new IOException("The environment variable JAVA_HOME=" + javaHome + " doesn't exist or is " + "not a valid directory.");
            }
            javadocExe = new File(env.getProperty("JAVA_HOME") + File.separator + "bin", javadocCommand);
        }
        if (!javadocExe.exists() || !javadocExe.isFile()) {
            throw new IOException("The javadoc executable '" + javadocExe + "' doesn't exist or is not a file. " + "Verify the JAVA_HOME environment variable.");
        }
        return javadocExe.getAbsolutePath();
    }
}
