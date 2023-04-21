package com.orientechnologies.odbms.tools.cpp;

import com.orientechnologies.jdo.oConstants;
import com.orientechnologies.jdo.oPersistenceManager;
import com.orientechnologies.jdo.oPersistenceManagerFactory;
import com.orientechnologies.jdo.system.schema.oSysClass;
import com.orientechnologies.jdo.system.schema.oSysDatabase;
import com.orientechnologies.odbms.tools.DbUtils;
import com.orientechnologies.odbms.tools.GenericTool;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * CppCompiler for the generation of source files giving a custom translator that
 * implements the Translator interface.
 * Copyright (c) 2001-2002
 * Orient Technologies (www.orientechnologies.com)
 *
 * @author Orient Staff (staff@orientechnologies.com)
 * @version 1.0
 */
public class CppCompiler extends GenericTool {

    public CppCompiler() {
        if (instance == null) instance = this;
    }

    public void start(String[] iArgs) {
        loadArgs(iArgs);
        translate();
    }

    public void setPersistenceManager(oPersistenceManager iDb) {
        persistenceManager = iDb;
        pMgrExternal = true;
    }

    public void setOutput(PrintStream output) {
        this.output = output;
    }

    private void translate() {
        String database = (String) parameters.get("database");
        String pack = (String) parameters.get("package");
        if (pack == null) parameters.put("package", database);
        String visibility = (String) parameters.get("visibility");
        if (visibility == null) parameters.put("visibility", "private");
        oPersistenceManagerFactory factory = new oPersistenceManagerFactory();
        if (persistenceManager == null) {
            output.print("Opening database <" + database + ">...");
            persistenceManager = DbUtils.openDatabase(factory, database, this);
        }
        oSysDatabase sysDb = persistenceManager.getSchema();
        output.println("ok.");
        oSysClass sysClass;
        StringBuffer dbHeaderBuffer = new StringBuffer();
        StringBuffer headerBuffer = new StringBuffer();
        StringBuffer sourceBuffer = new StringBuffer();
        try {
            CppDatabase.writeDatabase(sysDb, dbHeaderBuffer);
            Hashtable classes = (Hashtable) parameters.get("classes");
            int classParsed = 0;
            for (Enumeration eClasses = sysDb.getClasses().elements(); eClasses.hasMoreElements(); ) {
                sysClass = (oSysClass) eClasses.nextElement();
                if (classes != null) {
                    if (!classes.containsKey(sysClass.getName())) continue;
                }
                CppClass.writeClass(database, sysClass, headerBuffer, sourceBuffer);
                ++classParsed;
            }
            output.println("Classes generated: " + classParsed);
        } catch (Exception e) {
            output.println("Error: " + e.toString());
        } finally {
            if (!pMgrExternal) persistenceManager.close();
        }
    }

    private void loadArgs(String[] iArgs) {
        parameters.clear();
        if (iArgs == null) syntaxError("Missed <database> parameter");
        parameters.put("database", iArgs[0]);
        if (iArgs.length == 1) return;
        for (int i = 1; i < iArgs.length; ++i) {
            if (iArgs[i].startsWith("-c")) {
                String classesPar = iArgs[i].substring(2);
                if (classesPar.length() == 0) syntaxError("Missed <classes> parameter after -c");
                StringTokenizer classTokenizer = new StringTokenizer(classesPar, ":");
                Hashtable classes = new Hashtable();
                String token;
                while (classTokenizer.hasMoreTokens()) {
                    token = classTokenizer.nextToken();
                    if (token != null & token.length() > 0) classes.put(token, "");
                }
                if (classes.size() == 0) syntaxError("Wrong format of <classes> parameter after -c. Remember to delimit each class with ':' character");
                parameters.put("classes", classes);
            } else if (iArgs[i].startsWith("-o")) parameters.put("out_dir", iArgs[i].substring(2)); else if (iArgs[i].startsWith("-n")) parameters.put("namespace", iArgs[i].substring(2)); else if (iArgs[i].startsWith("-l")) parameters.put("library", "true"); else if (iArgs[i].startsWith("-f")) parameters.put("force", "true"); else if (iArgs[i].startsWith("-u")) parameters.put("uniqueFile", "true"); else if (iArgs[i].startsWith("-i")) parameters.put("inlineAccessors", "true"); else if (iArgs[i].startsWith("-a")) parameters.put("virtualAccessors", "true"); else if (iArgs[i].startsWith("-v")) parameters.put("visibility", iArgs[i].substring(2)); else if (iArgs[i].startsWith("-t")) parameters.put("triggers", "true");
        }
    }

    protected void printTitle() {
        writeOutput("\nOrient ODBMS CppCompiler v. " + oConstants.PRODUCT_VERSION + " - " + oConstants.PRODUCT_COPYRIGHTS + " (" + oConstants.PRODUCT_WWW + ")");
    }

    protected void printFormat() {
        output.println("Format: CppCompiler <database> [-c<classes>] [-o<out_dir>] [-n<namespace>] [<more options>]");
        output.println(" where: <database> is the alias of source database");
        output.println("        -c         Specify classes to be generated (optional)");
        output.println("                    Default value is all classes");
        output.println("                    <classes> may contains one or more classes separated by ':'");
        output.println("        -o         Specify output directory (optional)");
        output.println("        -n         Specify namespace of classes (optional).");
        output.println("                    Default classes has no namespace");
        output.println("       more options:");
        output.println("        -l         Generate library (DLL) information");
        output.println("        -f         Force overwrite of files");
        output.println("        -u         Generate just one file with class declaration and definition");
        output.println("        -i         Inline accessor methods");
        output.println("        -a         Virtual accessor methods");
        output.println("        -v         Change the visibility of attributes");
        output.println("                    Values are u=public, o=protected, i=private");
        output.println("                    Default value is private");
        output.println("        -t         Generate triggers");
    }

    public String getParameter(String iParName) {
        return (String) parameters.get(iParName);
    }

    public void setParameter(String iParName, String iValue) {
        parameters.put(iParName, iValue);
    }

    public static CppCompiler getInstance() {
        if (instance == null) synchronized (instance) {
            if (instance == null) new CppCompiler();
        }
        return instance;
    }

    public PrintStream getOutput() {
        return output;
    }

    private oPersistenceManager persistenceManager = null;

    private boolean pMgrExternal = false;

    private PrintStream output = System.out;

    private static CppCompiler instance = null;
}
