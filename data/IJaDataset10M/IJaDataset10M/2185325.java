package com.agimatec.sql.meta.script;

import com.agimatec.commons.config.ConfigManager;
import com.agimatec.sql.meta.persistence.ObjectPersistencer;
import com.agimatec.sql.meta.persistence.SerializerPersistencer;
import com.agimatec.sql.meta.persistence.XStreamPersistencer;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: main() class to use the DDLScriptSqlMetaFactory<br/>
 * User: roman.stumm <br/>
 * Date: 04.05.2007 <br/>
 * Time: 10:43:11 <br/>
 * Copyright: Agimatec GmbH
 */
public class DDLParserTool {

    private String dbms;

    private List<String> scriptsToParse = new ArrayList();

    private String catalog;

    public static void main(String[] args) throws Exception {
        DDLParserTool tool = new DDLParserTool();
        if (!tool.parseArgs(args)) return;
        tool.start();
    }

    private void start() throws Exception {
        DDLExpressions expressions = DDLExpressions.forDbms(dbms);
        if (expressions == null) {
            throw new IllegalArgumentException("unsupported dbms: " + dbms);
        }
        DDLScriptSqlMetaFactory factory = new DDLScriptSqlMetaFactory(expressions);
        for (String script : scriptsToParse) {
            factory.fillCatalog(ConfigManager.toURL(script));
        }
        ObjectPersistencer persistencer;
        if (catalog.toLowerCase().endsWith(".xml")) {
            persistencer = new XStreamPersistencer();
        } else {
            persistencer = new SerializerPersistencer();
        }
        File p = new File(catalog).getParentFile();
        if (p != null) p.mkdirs();
        persistencer.save(factory.getCatalog(), new File(catalog));
    }

    private boolean parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-help".equalsIgnoreCase(arg)) {
                printUsage();
                return false;
            } else if ("-dbms".equalsIgnoreCase(arg)) {
                if (dbms != null) {
                    throw new IllegalArgumentException("exactly one -dbms option is required!");
                }
                dbms = args[++i];
            } else if ("-parse".equalsIgnoreCase(arg)) {
                scriptsToParse.add(args[++i]);
            } else if ("-catalog".equalsIgnoreCase(arg)) {
                if (catalog != null) {
                    throw new IllegalArgumentException("exactly one -catalog option is required!");
                }
                catalog = args[++i];
            }
        }
        checkValid();
        return true;
    }

    private void checkValid() {
        if (catalog == null) {
            throw new IllegalArgumentException("no catalog output file name given with option -catalog");
        }
        if (dbms == null) {
            throw new IllegalArgumentException("no database given with option -dbms");
        }
        if (scriptsToParse.isEmpty()) {
            throw new IllegalArgumentException("no scripts to parse given with option -parse");
        }
    }

    private static void printUsage() {
        System.out.println("usage: java " + DDLParserTool.class.getName() + " -catalog catalog.xml -dbms dbms -parse file:script-to-parse.sql");
        System.out.println("Options:\n\t-help \t (optional) print this help");
        System.out.println("\t-catalog \t (required) path of catalog file (xml or dmp) to write");
        System.out.println("\t-dbms \t (required) e.g. oracle, postgres. Determines the database syntax for the script parser.");
        System.out.println("\t-parse\t (required) can appear multiple times. a URL of a script to parse into the catalog, e.g. file:create-tables.sql - All scripts must belong to the same database schema catalog");
    }
}
