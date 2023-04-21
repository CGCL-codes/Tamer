package com.orientechnologies.odbms.tools;

import java.io.PrintStream;
import java.util.Iterator;
import com.orientechnologies.jdo.oConstants;
import com.orientechnologies.jdo.oOID;
import com.orientechnologies.jdo.oPersistenceManager;
import com.orientechnologies.jdo.oPersistenceManagerFactory;
import com.orientechnologies.jdo.oUtility;
import com.orientechnologies.jdo.directory.oDirectoryException;
import com.orientechnologies.jdo.directory.oDirectoryManager;
import com.orientechnologies.jdo.system.oSysFolder;
import com.orientechnologies.jdo.system.oSysReference;
import com.orientechnologies.jdo.types.d_Dictionary;
import com.orientechnologies.jdo.utils.oFormatOutput;

/**
 * Works with database directory to simulate a virtual file system. Copyright
 * (c) 2001-2004 Orient Technologies (www.orientechnologies.com)
 * 
 * @author Orient Staff (staff@orientechnologies.com)
 * @version 2.3
 */
public class DbDirectory extends GenericTool {

    /**
	 * Constructor for interactive execution
	 */
    public DbDirectory() {
        parameters.put("force", "true");
    }

    public void start(String[] iArgs) throws Exception {
        loadArgs(iArgs);
        translate();
    }

    private void translate() {
        String database = (String) parameters.get("database");
        String command = (String) parameters.get("command");
        String path = (String) parameters.get("path");
        String value = (String) parameters.get("value");
        oPersistenceManagerFactory factory = null;
        try {
            factory = new oPersistenceManagerFactory();
            mgr = DbUtils.openDatabase(factory, database, this);
            command = command.toUpperCase();
            if (command.equals("LIST")) list(path, value); else if (command.equals("GET")) get(path); else if (command.equals("ADDREF")) addRef(path, value); else if (command.equals("ADDFOLDER")) addFolder(path); else if (command.equals("UPDATE")) update(path, value); else if (command.equals("DELREF")) delRef(path); else if (command.equals("DELFOLDER")) delFolder(path);
            writeOutput("\n");
        } finally {
            if (mgr != null) mgr.close();
            if (factory != null) factory.close();
        }
    }

    public void list(String iPath, String iValue) {
        int[] counts = new int[2];
        list(iPath, iValue, counts);
        writeOutput("\n\n " + counts[0] + " folder(s) and " + counts[1] + " reference(s) found");
    }

    public void list(String iPath, String iValue, int[] iCounts) {
        writeOutput("\n\nListing folder " + iPath + ":\n");
        try {
            oSysFolder folder = oDirectoryManager.getInstance().findFolder(mgr.getDirectory(), iPath);
            String refName;
            oSysReference ref;
            String className;
            oOID oid;
            String creationDate;
            String lastUpdateDate;
            d_Dictionary refs = folder.getReferences();
            if (refs != null) for (Iterator keys = refs.keySet().iterator(); keys.hasNext(); ) {
                refName = (String) keys.next();
                ref = (oSysReference) refs.get(refName);
                oid = ref.getOid();
                className = oUtility.getClassFromOid(mgr, oid);
                lastUpdateDate = mgr.getDateFormat().format(ref.getLastUpdate());
                writeOutput("\n " + oFormatOutput.print(refName, 30, oFormatOutput.ALIGN_LEFT) + oFormatOutput.print(oid, 13, oFormatOutput.ALIGN_LEFT) + oFormatOutput.print(className, 20, oFormatOutput.ALIGN_LEFT) + oFormatOutput.print(lastUpdateDate, 15, oFormatOutput.ALIGN_LEFT));
                ++iCounts[1];
            }
            String folderName;
            d_Dictionary folders = folder.getFolders();
            if (folders != null) for (Iterator keys = folders.keySet().iterator(); keys.hasNext(); ) {
                folderName = (String) keys.next();
                writeOutput("\n " + oFormatOutput.print(folderName, 30, oFormatOutput.ALIGN_LEFT) + oFormatOutput.print("<FOLDER>", 13, oFormatOutput.ALIGN_LEFT));
                ++iCounts[0];
                if (iValue != null && iValue.equalsIgnoreCase("-R")) list(((oSysFolder) folders.get(folderName)).getFullPath(), iValue, iCounts);
            }
        } catch (oDirectoryException e) {
            writeOutput(e.getMessage());
            return;
        }
    }

    public void get(String iPath) {
    }

    public void addRef(String iPath, String iValue) {
        try {
            writeOutput("\nCreating reference " + iPath + "=" + iValue + "...");
            if (iValue == null) {
                writeOutput("error: reference NULL");
                return;
            }
            oOID oid = null;
            try {
                oid = new oOID(iValue);
                if (!oid.isValid()) oid = null;
            } catch (Exception e) {
            }
            if (oid == null) {
                writeOutput("error: reference " + iValue + " is not valid");
                return;
            }
            String[] names = oDirectoryManager.getInstance().splitPath(iPath);
            oSysFolder folder = oDirectoryManager.getInstance().findFolder(mgr.getDirectory(), names[0]);
            mgr.currentTransaction().begin();
            folder.addReference(names[1], oid);
            mgr.currentTransaction().commit();
            writeOutput("ok.");
        } catch (oDirectoryException e) {
            writeOutput(e.getMessage());
            return;
        }
    }

    public void addFolder(String iPath) {
        try {
            writeOutput("\nCreating folder " + iPath + "...");
            String[] names = oDirectoryManager.getInstance().splitPath(iPath);
            oSysFolder folder = oDirectoryManager.getInstance().findFolder(mgr.getDirectory(), names[0]);
            mgr.currentTransaction().begin();
            folder.addFolder(names[1]);
            mgr.currentTransaction().commit();
            writeOutput("ok.");
        } catch (oDirectoryException e) {
            writeOutput(e.getMessage());
            return;
        }
    }

    public void update(String iPath, String iValue) {
    }

    public void delFolder(String iPath) {
        try {
            writeOutput("\nDeleting folder " + iPath + "...");
            oSysFolder folder = oDirectoryManager.getInstance().findFolder(mgr.getDirectory(), iPath);
            mgr.currentTransaction().begin();
            folder.getParent().delFolder(folder.getName());
            mgr.currentTransaction().commit();
            writeOutput("ok.");
        } catch (oDirectoryException e) {
            writeOutput(e.getMessage());
            return;
        }
    }

    public void delRef(String iPath) {
        try {
            writeOutput("\nDeleting reference " + iPath + "...");
            oSysReference ref = oDirectoryManager.getInstance().findReference(mgr.getDirectory(), iPath);
            mgr.currentTransaction().begin();
            ref.getParent().delRef(ref.getName());
            mgr.currentTransaction().commit();
            writeOutput("ok.");
        } catch (oDirectoryException e) {
            writeOutput(e.getMessage());
            return;
        }
    }

    private void loadArgs(String[] iArgs) {
        if (iArgs == null) syntaxError("Missed <command> parameter");
        parameters.put("command", iArgs[0]);
        if (iArgs.length < 2) syntaxError("Missed <database> parameter");
        parameters.put("database", iArgs[1]);
        if (iArgs.length < 3) syntaxError("Missed <path> parameter");
        parameters.put("path", iArgs[2]);
        if (iArgs.length > 3) parameters.put("value", iArgs[3]);
    }

    public void setFileOutput(PrintStream iOutput) {
        fileOutput = iOutput;
    }

    protected void printRealTitle() {
        writeOutput("\nOrient ODBMS oDbDir v. " + oConstants.PRODUCT_VERSION + " - " + oConstants.PRODUCT_COPYRIGHTS + " (" + oConstants.PRODUCT_WWW + ")\n");
    }

    protected void printTitle() {
    }

    protected void printFormat() {
        writeOutput("\nFormat: oDbDir <command> <database> <path> [<value>]");
        writeOutput("\n where: <command>  = GET to get the reference OID for a path");
        writeOutput("\n                     LIST to list entries in folder path");
        writeOutput("\n                     ADDREF to add a reference");
        writeOutput("\n                     ADDFOLDER to add a sub-folder");
        writeOutput("\n                     UPDATE to update a reference with a new OID");
        writeOutput("\n                     DELREF to remove a reference");
        writeOutput("\n                     DELFOLDER to remove a folder and all inside of it");
        writeOutput("\n        <database> = database name");
        writeOutput("\n        <path>     = path to find");
        writeOutput("\n        <value>    = Optional value based on command type");
    }

    private oPersistenceManager mgr = null;

    private StringBuffer buffer = null;

    private PrintStream fileOutput = System.out;

    private static final int BUFFER_SIZE = 100000;
}
