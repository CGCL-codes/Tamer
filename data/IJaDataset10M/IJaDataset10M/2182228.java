package eln.applets;

import java.io.*;
import java.util.*;
import netscape.security.PrivilegeManager;

/**
 *  Description of the Class
 *
 * @author     d3h252
 * @created    February 13, 2003
 */
public class PropertiesForNetscape extends Properties {

    /**
   *  Constructor for the PropertiesForNetscape object
   */
    public PropertiesForNetscape() {
        super();
    }

    /**
   *  Constructor for the PropertiesForNetscape object
   *
   * @param  defs  Description of Parameter
   */
    public PropertiesForNetscape(Properties defs) {
        super(defs);
    }

    /**
   *  Constructor for the PropertiesForNetscape object
   *
   * @param  filename  Description of Parameter
   */
    public PropertiesForNetscape(String filename) throws IOException {
        super();
        try {
            PrivilegeManager.enablePrivilege("UniversalFileAccess");
        } catch (netscape.security.ForbiddenTargetException fte) {
            throw new IOException("Netscape Priveleges denied");
        }
        File iniFile = new File(filename);
        FileInputStream iniStream = new FileInputStream(iniFile);
        super.load(iniStream);
    }

    /**
   *  Description of the Method
   *
   * @param  is               Description of Parameter
   * @exception  IOException  Description of Exception
   */
    public synchronized void load(InputStream is) throws IOException {
        try {
            PrivilegeManager.enablePrivilege("UniversalFileAccess");
        } catch (netscape.security.ForbiddenTargetException fte) {
            throw new IOException("Netscape Priveleges denied");
        }
        super.load(is);
    }

    /**
   *  Description of the Method
   *
   * @param  ps  Description of Parameter
   */
    public synchronized void list(PrintStream ps) {
        try {
            PrivilegeManager.enablePrivilege("UniversalFileAccess");
        } catch (netscape.security.ForbiddenTargetException fte) {
            System.err.println("Netscape Priveleges denied: " + fte);
        }
        super.list(ps);
    }

    /**
   *  Description of the Method
   *
   * @param  pw               Description of Parameter
   * @exception  IOException  Description of Exception
   */
    public synchronized void load(PrintWriter pw) throws IOException {
        try {
            PrivilegeManager.enablePrivilege("UniversalFileAccess");
        } catch (netscape.security.ForbiddenTargetException fte) {
            throw new IOException("Netscape Priveleges denied");
        }
        super.list(pw);
    }

    /**
   *  Description of the Method
   *
   * @param  os               Description of Parameter
   * @param  comment          Description of Parameter
   * @exception  IOException  Description of Exception
   */
    public synchronized void store(OutputStream os, String comment) throws IOException {
        try {
            PrivilegeManager.enablePrivilege("UniversalFileAccess");
        } catch (netscape.security.ForbiddenTargetException fte) {
            throw new IOException("Netscape Priveleges denied");
        }
        try {
            super.store(os, comment);
        } catch (java.lang.NoSuchMethodError nsm) {
            super.save(os, comment);
        }
    }

    /**
   *  Description of the Method
   *
   * @param  comment          Description of Parameter
   * @param  outfilename      Description of Parameter
   * @exception  IOException  Description of Exception
   */
    public synchronized void store(String outfilename, String comment) throws IOException {
        try {
            PrivilegeManager.enablePrivilege("UniversalFileAccess");
        } catch (netscape.security.ForbiddenTargetException fte) {
            throw new IOException("Netscape Priveleges denied");
        }
        FileOutputStream outStream = new FileOutputStream(outfilename);
        try {
            super.store(outStream, comment);
            outStream.close();
        } catch (java.lang.NoSuchMethodError nsm) {
            super.save(outStream, comment);
            outStream.close();
        }
    }
}
