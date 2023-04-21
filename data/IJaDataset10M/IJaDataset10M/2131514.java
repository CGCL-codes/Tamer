package com.i3sp.ant;

import com.mortbay.Util.Code;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Date;
import java.io.File;

public class ClientWar extends Task {

    private String name;

    public void setName(String name_) {
        name = name_;
    }

    private File src;

    public void setSrc(File src_) {
        src = src_;
    }

    private File gen;

    public void setGen(File gen_) {
        gen = gen_;
    }

    private File dest;

    public void setDest(File dest_) {
        dest = dest_;
    }

    private File lookAndFeel;

    public void setLookandfeel(File laf) {
        lookAndFeel = laf;
    }

    private String lookAndFeelRef = "LookAndFeel";

    public void setLookandfeelref(String ref) {
        lookAndFeelRef = ref;
    }

    private boolean expand = false;

    public void setExpand(boolean exp) {
        expand = exp;
    }

    private boolean forceCopy = false;

    public void setForcecopy(boolean forceCopy_) {
        forceCopy = forceCopy_;
    }

    public class ReplaceValue {

        private StringBuffer buf = new StringBuffer();

        public void addText(String val) {
            buf.append(val);
        }

        public String getText() {
            return buf.toString();
        }

        public String toString() {
            return buf.toString();
        }
    }

    private ReplaceValue displayName;

    public ReplaceValue createDisplayname() {
        displayName = new ReplaceValue();
        return displayName;
    }

    private ReplaceValue ssoURL;

    public ReplaceValue createSsourl() {
        ssoURL = new ReplaceValue();
        return ssoURL;
    }

    private ReplaceValue ssoLogout;

    public ReplaceValue createSsologout() {
        ssoLogout = new ReplaceValue();
        return ssoLogout;
    }

    private ReplaceValue authenticatedURL;

    public ReplaceValue createAuthenticatedurl() {
        authenticatedURL = new ReplaceValue();
        return authenticatedURL;
    }

    private ReplaceValue logOutURL;

    public ReplaceValue createLogouturl() {
        logOutURL = new ReplaceValue();
        return logOutURL;
    }

    private ReplaceValue targetHttpd;

    public ReplaceValue createTargethttpd() {
        targetHttpd = new ReplaceValue();
        return targetHttpd;
    }

    private ReplaceValue lookandfeelpackage;

    public ReplaceValue createLookandfeelpackage() {
        lookandfeelpackage = new ReplaceValue();
        return lookandfeelpackage;
    }

    private ReplaceValue loginurl;

    public ReplaceValue createLoginurl() {
        loginurl = new ReplaceValue();
        return loginurl;
    }

    public void execute() throws BuildException {
        Object args[] = { name, src, gen, dest };
        String names[] = { "name", "src", "gen", "dest" };
        Object filterArgs[] = { displayName, ssoURL, ssoLogout, authenticatedURL, logOutURL, targetHttpd, lookandfeelpackage, loginurl };
        String filterNames[] = { "displayname", "ssourl", "ssologout", "authenticatedurl", "logouturl", "targethttpd", "lookandfeelpackage", "loginurl" };
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                throw new BuildException(names[i] + " attribute must be set!", location);
            }
        }
        for (int i = 0; i < filterArgs.length; i++) {
            if (filterArgs[i] == null) {
                throw new BuildException("nested tag <" + filterNames[i] + "> must be present!", location);
            } else if (i != 0) project.addFilter(filterNames[i], filterArgs[i].toString());
        }
        try {
            Mkdir mkdir = (Mkdir) project.createTask("mkdir");
            File temp = new File(gen, name);
            mkdir.setDir(temp);
            mkdir.execute();
            File clientGateway = new File(temp, "client-gateway.xml");
            File clientWWW = new File(temp, "client-www.xml");
            Copy copy = (Copy) project.createTask("copy");
            copy.setOverwrite(forceCopy);
            copy.setFiltering(true);
            project.addFilter(filterNames[0], displayName.toString() + "-Gateway");
            copy.setTofile(clientGateway);
            copy.setFile(new File(src, "client-gateway.xml"));
            copy.execute();
            project.addFilter(filterNames[0], displayName.toString() + "-Client");
            copy.setTofile(clientWWW);
            copy.setFile(new File(src, "client-www.xml"));
            copy.execute();
            War war = (War) project.createTask("war");
            File gatewayWar = new File(dest, name + "-gateway.war");
            war.setWarfile(gatewayWar);
            war.setWebxml(clientGateway);
            ZipFileSet lib = new ZipFileSet();
            lib.setDir(new File(project.getProperty("jar")));
            lib.setExcludes("*munge.jar");
            war.addLib(lib);
            FileSet extras = new FileSet();
            extras.setDir(src);
            Reference ref = new Reference();
            ref.setRefId("clientfiles");
            extras.createPatternSet().setRefid(ref);
            war.addFileset(extras);
            war.execute();
            File clientWar = new File(dest, name + "-client.war");
            war.setWarfile(clientWar);
            war.setWebxml(clientWWW);
            if (lookAndFeel != null) {
                ZipFileSet webinf = new ZipFileSet();
                webinf.setDir(lookAndFeel);
                ref = new Reference();
                ref.setRefId(lookAndFeelRef);
                webinf.createPatternSet().setRefid(ref);
                war.addWebinf(webinf);
            }
            war.execute();
            if (expand) {
                Expand expand = (Expand) project.createTask("unwar");
                File to = new File(dest, name + "-gateway");
                if (gatewayWar.lastModified() > to.lastModified()) {
                    expand.setSrc(gatewayWar);
                    expand.setDest(to);
                    expand.execute();
                }
                to = new File(dest, name + "-client");
                if (clientWar.lastModified() > to.lastModified()) {
                    expand.setSrc(clientWar);
                    expand.setDest(to);
                    expand.execute();
                }
            }
        } catch (BuildException ex) {
            Code.setDebug(true);
            Code.debug(ex);
            throw ex;
        } finally {
            Hashtable filters = project.getFilters();
            for (int i = 0; i < filterNames.length; i++) filters.remove(filterNames[i]);
        }
    }
}
