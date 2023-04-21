package de.wcondev.launch4l.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Launch4lConfig {

    public static enum Header {

        gui, console
    }

    private File jar;

    private File outfile;

    private Header headerType;

    private String cmdLine;

    private String chdir;

    private String errTitle;

    private String supportUrl;

    private String downloadUrl;

    private boolean stayAlive;

    private boolean dontWrapJar;

    private List<Variable> variables;

    private ClassPath classPath;

    private Jre jre;

    private VersionInfo versionInfo;

    public Launch4lConfig() {
        headerType = Header.gui;
    }

    public void add(final Variable var) {
        variables.add(var);
    }

    public List<Variable> getVariables() {
        if (variables == null) {
            variables = new ArrayList<Variable>();
        }
        return variables;
    }

    public String getChdir() {
        return chdir;
    }

    public void setChdir(String chdir) {
        this.chdir = chdir;
    }

    public String getCmdLine() {
        return cmdLine;
    }

    public void setCmdLine(String cmdLine) {
        this.cmdLine = cmdLine;
    }

    public boolean isDontWrapJar() {
        return dontWrapJar;
    }

    public void setDontWrapJar(boolean dontWrapJar) {
        this.dontWrapJar = dontWrapJar;
    }

    public Header getHeaderType() {
        return headerType;
    }

    public void setHeaderType(Header headerType) {
        this.headerType = headerType;
    }

    public File getJar() {
        return jar;
    }

    public void setJar(File jar) {
        this.jar = jar;
    }

    public File getOutfile() {
        return outfile;
    }

    public void setOutfile(File outfile) {
        this.outfile = outfile;
    }

    public boolean isStayAlive() {
        return stayAlive;
    }

    public void setStayAlive(boolean stayAlive) {
        this.stayAlive = stayAlive;
    }

    public String getErrTitle() {
        return errTitle;
    }

    public void setErrTitle(String errTitle) {
        this.errTitle = errTitle;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(String supportUrl) {
        this.supportUrl = supportUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public ClassPath getClassPath() {
        if (classPath == null) {
            classPath = new ClassPath();
        }
        return classPath;
    }

    public void setClassPath(ClassPath classPath) {
        this.classPath = classPath;
    }

    public Jre getJre() {
        return jre;
    }

    public void setJre(Jre jre) {
        this.jre = jre;
    }

    public VersionInfo getVersionInfo() {
        if (versionInfo == null) {
            versionInfo = new VersionInfo();
        }
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String toString() {
        final String nl = System.getProperty("line.separator");
        final StringBuffer sb = new StringBuffer();
        sb.append("jar = " + jar + (jar.isAbsolute() ? " (absolute)" : "") + nl);
        sb.append("outfile = " + outfile + (outfile.isAbsolute() ? " (absolute)" : "") + nl);
        sb.append("headerType = " + headerType + nl);
        sb.append("cmdLine = " + cmdLine + nl);
        sb.append("chdir = " + chdir + nl);
        sb.append("errTitle = " + errTitle + nl);
        sb.append("supportUrl = " + supportUrl + nl);
        sb.append("downloadUrl = " + downloadUrl + nl);
        sb.append("stayAlive = " + stayAlive + nl);
        sb.append("dontWrapJar = " + dontWrapJar + nl);
        sb.append("variables = " + variables + nl);
        sb.append("classPath = " + classPath + nl);
        sb.append("jre = " + jre + nl);
        sb.append("versionInfo = " + versionInfo + nl);
        return sb.toString();
    }
}
