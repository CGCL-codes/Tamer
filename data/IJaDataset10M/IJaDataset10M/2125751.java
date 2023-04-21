package com.jigen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.jigen.ant.Icon;
import com.jigen.ant.Java;
import com.jigen.ant.Jigen;
import com.jigen.ant.Resource;
import com.jigen.ant.RunnableResource;
import com.jigen.ant.Shortcut;

public class UnixPackageGenerator {

    private final File tmpFile = File.createTempFile("jigenpackage_", ".tmp");

    private final byte buffer[] = new byte[200 * 1024];

    private final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmpFile));

    private final Jigen jigen;

    private final LinkedList<String> textFiles = new LinkedList<String>();

    private final LinkedList<String> executableFiles = new LinkedList<String>();

    private final LinkedList<String> externalFiles = new LinkedList<String>();

    public UnixPackageGenerator(Jigen jigen) throws IOException {
        tmpFile.deleteOnExit();
        this.jigen = jigen;
    }

    private void appendFile(String resourceName, File file) throws IOException {
        FileInputStream fileStream = new FileInputStream(file);
        appendFile(resourceName, fileStream);
        fileStream.close();
    }

    private void appendFile(String resourceName, byte[] file) throws IOException {
        ByteArrayInputStream fileStream = new ByteArrayInputStream(file);
        appendFile(resourceName, fileStream);
        fileStream.close();
    }

    private void appendFile(String resourceName, InputStream inputStream) throws IOException {
        zos.putNextEntry(new ZipEntry(resourceName));
        int length;
        while ((length = inputStream.read(buffer)) != -1) zos.write(buffer, 0, length);
        zos.closeEntry();
    }

    public File closeFile() throws IOException {
        zos.close();
        return tmpFile;
    }

    public void dispose() {
        tmpFile.delete();
    }

    public File getFile() {
        return tmpFile;
    }

    public void appendJigenRunnables() throws IOException {
        for (RunnableResource runnable : jigen.getRunnables()) {
            switch(runnable.getType()) {
                case JAVA:
                    for (File classpathElem : runnable.getJava().getClasspath()) appendFile("lib/" + classpathElem.getName(), classpathElem);
                    break;
                default:
                    throw new AssertionError("Bad runnable type");
            }
        }
    }

    public void appendJigenRunnableScripts() throws IOException {
        for (RunnableResource runnable : jigen.getRunnables()) {
            StringBuffer sb = new StringBuffer();
            switch(runnable.getType()) {
                case JAVA:
                    Java java = runnable.getJava();
                    sb.append("#!/bin/bash\n");
                    sb.append("\n");
                    sb.append("BASEDIR=$(dirname $0)/..\n");
                    sb.append("\n");
                    sb.append("if [ \"$(echo $BASEDIR | cut -c1)\" != \"/\" ]; then BASEDIR=$PWD/$BASEDIR; fi\n");
                    sb.append("\n");
                    sb.append("JAVAHOME=:[JAVAHOME]\n");
                    sb.append("\n");
                    sb.append("JAVAOPTS=" + (java.getJavaOpts() == null ? "" : java.getJavaOpts()) + "\n");
                    sb.append("\n");
                    sb.append("DEFAULTPARAMS=" + (java.getParams() == null ? "" : java.getParams()) + "\n");
                    sb.append("\n");
                    if (java.getJar() == null) {
                        sb.append("CLASSPATH=\n");
                        for (File classpathElem : runnable.getJava().getClasspath()) sb.append("CLASSPATH=$CLASSPATH:$BASEDIR/lib/" + classpathElem.getName() + "\n");
                        sb.append("\n");
                        sb.append("MAINCLASS=" + java.getClass1() + "\n");
                        sb.append("\n");
                        sb.append("( cd $BASEDIR; $JAVAHOME/bin/java $JAVAOPTS -cp $CLASSPATH $MAINCLASS $DEFAULTPARAMS $* )\n");
                    } else {
                        sb.append("MAINJAR=" + java.getJar() + "\n");
                        sb.append("\n");
                        sb.append("( cd $BASEDIR; $JAVAHOME/bin/java $JAVAOPTS -jar $MAINJAR $DEFAULTPARAMS $* )\n");
                    }
                    sb.append("\n");
                    break;
                default:
                    throw new AssertionError("Bad runnable type");
            }
            String file = "bin/" + runnable.getName();
            textFiles.add(file);
            executableFiles.add(file);
            appendFile(file, sb.toString().getBytes());
        }
    }

    public void appendIcons() throws IOException {
        for (RunnableResource runnable : jigen.getRunnables()) {
            Icon icon = runnable.getIcon();
            if (icon != null) {
                File iconSource = runnable.getIcon().getSource();
                appendFile("icons/" + iconSource.getName(), iconSource);
            }
        }
        for (Resource resource : jigen.getResources()) {
            Icon icon = resource.getIcon();
            if (icon != null) {
                File iconSource = resource.getIcon().getSource();
                appendFile("icons/" + iconSource.getName(), iconSource);
            }
        }
    }

    public void appendShortcuts() throws IOException {
        for (RunnableResource runnable : jigen.getRunnables()) {
            switch(runnable.getType()) {
                case JAVA:
                    for (Shortcut desktopShortcuts : runnable.getShortcuts()) {
                        switch(desktopShortcuts.getLocation()) {
                            case DESKTOP:
                                {
                                    StringBuffer sb = new StringBuffer();
                                    sb.append("\n");
                                    sb.append("[Desktop Entry]\n");
                                    sb.append("Encoding=UTF-8\n");
                                    sb.append("Version=1.0\n");
                                    sb.append("Type=Application\n");
                                    sb.append("Terminal=" + runnable.getJava().getOpenConsole() + "\n");
                                    sb.append("Icon=:[INSTALLDIR]/icons/" + runnable.getIcon().getSource().getName() + "\n");
                                    sb.append("Name=" + runnable.getName() + "\n");
                                    sb.append("Exec=:[INSTALLDIR]/bin/" + runnable.getName() + "\n");
                                    sb.append("Comment=" + runnable.getName() + "\n");
                                    String file = "jigen-desktop/" + runnable.getName() + ".desktop";
                                    appendFile(file, sb.toString().getBytes());
                                    externalFiles.add(":[DESKTOPDIR]/" + runnable.getName() + ".desktop");
                                    textFiles.add(file);
                                    break;
                                }
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    throw new AssertionError("Bad runnable type");
            }
        }
    }

    public void appendUninstaller() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("#!/bin/bash\n");
        sb.append("\n");
        sb.append("BASEDIR=$(dirname $0)\n");
        sb.append("\n");
        sb.append("( cd $BASEDIR/.. ; rm -rf :[INSTALLDIR] )\n");
        sb.append("\n");
        for (String externalFile : externalFiles) {
            sb.append("if [ -f " + externalFile + " ]; then rm " + externalFile + "; fi\n");
            sb.append("\n");
        }
        String file = "uninstall";
        appendFile(file, sb.toString().getBytes());
        executableFiles.add(file);
        textFiles.add(file);
    }

    public byte[] getTextFilesList() {
        StringBuffer sb = new StringBuffer();
        for (String s : textFiles) sb.append(s + '\n');
        return sb.toString().getBytes();
    }

    public byte[] getExecutableFilesList() {
        StringBuffer sb = new StringBuffer();
        for (String s : executableFiles) sb.append(s + '\n');
        return sb.toString().getBytes();
    }

    public void appendJigenResources() throws IOException {
        for (Resource resource : jigen.getResources()) appendFile(resource.getDestination().substring(1), resource.getSource());
    }
}
