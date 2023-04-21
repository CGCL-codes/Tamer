package org.codebistro.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildNumber {

    private File outputPath;

    private File buildDirectory;

    private File basedir;

    public String execute(boolean throwOnError) {
        String buildNumber = System.getenv("BUILD_NUMBER");
        boolean modified = true;
        if (buildNumber == null) {
            String svnVersionCommand = "svnversion -n -c " + basedir.getAbsolutePath();
            try {
                Process process = Runtime.getRuntime().exec(svnVersionCommand);
                buildNumber = readIntoString(process.getInputStream()).replaceFirst("^[0-9]*:", "");
                if (buildNumber.endsWith("M")) {
                    buildNumber = buildNumber.substring(0, buildNumber.length() - 1);
                    modified = true;
                }
                process.waitFor();
            } catch (IOException ex) {
                if (throwOnError) throw new RuntimeException("Running '" + svnVersionCommand + "'" + "\n-- Make sure you have command line subversion installed" + "\n-- Some links to try: http://subversion.apache.org/packages.html, http://www.sliksvn.com/en/download", ex);
            } catch (Exception ex) {
                if (throwOnError) throw new RuntimeException("Running '" + svnVersionCommand + "'", ex);
            }
        }
        if (buildNumber == null || buildNumber.equalsIgnoreCase("exported")) buildNumber = timeGenerated();
        FileWriter writer;
        try {
            outputPath.getParentFile().mkdirs();
            writer = new FileWriter(outputPath);
            writer.append("build.number=");
            writer.append(buildNumber);
            writer.append('\n');
            if (modified) {
                writer.append("build.modified=true");
                writer.append('\n');
                writer.append("build.timestamp=");
                writer.append(timeGenerated());
                writer.append('\n');
            }
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Writing to outputPath", ex);
        }
        return buildNumber;
    }

    static String readIntoString(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (true) {
            int next = in.read();
            if (next < 0) break;
            builder.append((char) next);
        }
        return builder.toString();
    }

    static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd-hhmm");

    static String timeGenerated() {
        return DATE_FORMAT.format(new Date());
    }

    public File getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(File outputPath) {
        this.outputPath = outputPath;
    }

    public File getBuildDirectory() {
        return buildDirectory;
    }

    public void setBuildDirectory(File buildDirectory) {
        this.buildDirectory = buildDirectory;
    }

    public File getBasedir() {
        return basedir;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }
}
