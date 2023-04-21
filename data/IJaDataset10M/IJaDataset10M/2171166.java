package org.kisst.cordys.caas.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SetupCommand extends CompositeCommand {

    public SetupCommand() {
        super("caas setup", "", "all");
        commands.put("all", new CommandBase("", "download all jar files") {

            @Override
            public void run(String args[]) {
                downloadBase();
                downloadGroovy();
                downloadJython();
            }
        });
        commands.put("groovy", new CommandBase("", "download all jar files needed to run groovy") {

            @Override
            public void run(String args[]) {
                downloadBase();
                downloadGroovy();
            }
        });
        commands.put("jython", new CommandBase("", "download all jar files needed to run jython") {

            @Override
            public void run(String args[]) {
                downloadBase();
                downloadJython();
            }
        });
        commands.put("pm", new CommandBase("", "download all jar files needed to run pm") {

            @Override
            public void run(String args[]) {
                downloadBase();
            }
        });
    }

    public void runold(String args[]) {
        String libdir = getLibDir();
        System.setProperty("java.net.useSystemProxies", "true");
        System.out.println("Downloading necessary jar files to " + libdir);
        File d = new File(libdir);
        if (!d.exists()) {
            System.out.println("Making new directory " + libdir);
            d.mkdir();
        }
    }

    public void downloadBase() {
        System.setProperty("java.net.useSystemProxies", "true");
        String libdir = getLibDir();
        download(libdir + "commons-httpclient-3.1.jar", "http://repo2.maven.org/maven2/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar");
        download(libdir + "commons-logging-1.0.4.jar", "http://repo2.maven.org/maven2/commons-logging/commons-logging/1.0.4/commons-logging-1.0.4.jar");
        download(libdir + "commons-codec-1.2.jar", "http://repo2.maven.org/maven2/commons-codec/commons-codec/1.2/commons-codec-1.2.jar");
        download(libdir + "log4j-1.2.13.jar", "http://repo2.maven.org/maven2/log4j/log4j/1.2.13/log4j-1.2.13.jar");
        download(libdir + "jdom-1.0.jar", "http://repo2.maven.org/maven2/jdom/jdom/1.0/jdom-1.0.jar");
    }

    public void downloadGroovy() {
        String libdir = getLibDir();
        download(libdir + "groovy-all-1.6.5.jar", "http://repo1.maven.org/maven2/org/codehaus/groovy/groovy-all/1.6.5/groovy-all-1.6.5.jar");
        download(libdir + "commons-cli-1.2.jar", "http://repo1.maven.org/maven2/commons-cli/commons-cli/1.2/commons-cli-1.2.jar");
        download(libdir + "jline-0.9.94.jar", "http://repo1.maven.org/maven2/jline/jline/0.9.94/jline-0.9.94.jar");
    }

    public void downloadJython() {
    }

    private static String getLibDir() {
        String dir = System.getProperty("java.class.path");
        dir = dir.replace('\\', '/');
        int pos = dir.lastIndexOf("/");
        if (pos >= 0) return dir.substring(0, pos + 1) + "lib/"; else return "lib/";
    }

    private static void download(String filename, String url) {
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("File " + filename + " allready exists, will not download this");
            return;
        }
        System.out.println("Downloading " + filename + " from " + url);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL u = new URL(url);
            in = u.openStream();
            out = new FileOutputStream(f);
            byte[] buf = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) out.write(buf, 0, bytesRead);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
            }
        }
    }
}
