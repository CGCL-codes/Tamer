package org.dbwiki.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import org.dbwiki.data.io.SAXCallbackInputHandler;
import org.dbwiki.data.io.StructureParser;
import org.dbwiki.data.schema.DatabaseSchema;
import org.dbwiki.driver.rdbms.DatabaseConnector;
import org.dbwiki.driver.rdbms.DatabaseConnectorFactory;
import org.dbwiki.exception.WikiException;
import org.dbwiki.user.User;
import org.dbwiki.web.server.DatabaseWiki;
import org.dbwiki.web.server.WikiServer;
import org.dbwiki.main.ImportPresentationFiles.PresentationFileType;

/** Imports a database "package".  Arguments:
 * Config file
 * Name of database
 * Title of database
 * Path to entries
 * XML file
 * HTML template
 * CSS
 * Layout
 * URLDecoding 
 * User 
 * 
 * Alternative usage:
 * Config file
 * Path to a directory that contains a "package descriptor", which is a properties file providing the above arguments.
 * FIXME #import: Refactor this and ImportPresentationFiles.
 * TODO: make this robust if fields are missing (by using default presentation files for example)
 * TODO: Make this import wiki pages.
 * TODO: Support zip file import/export
 * TODO: Import full history, provenance, and annotations
 * @author jcheney
 *
 */
public class DatabasePackageImport {

    private static final String commandLine = "DatabasePackageImport <config-file> [<packageinfo>|<name> <title> <path> <xml-file> <html-template> <css> <layout>] <user>";

    private static WikiServer _server;

    private static DatabaseWiki _wiki;

    private static String PackageInfoName = "NAME";

    private static String PackageInfoTitle = "TITLE";

    private static String PackageInfoPath = "PATH";

    private static String PackageInfoInputXML = "INPUT_XML";

    private static String PackageInfoTemplate = "TEMPLATE";

    private static String PackageInfoCSS = "CSS";

    private static String PackageInfoLayout = "LAYOUT";

    private static String PackageInfoURLDecoding = "URLDECODING";

    public static void loadPresentationFile(String filename, PresentationFileType type, User user) throws IOException, WikiException {
        File inputFile = new File(filename);
        if (inputFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            char[] buf = new char[(int) inputFile.length()];
            reader.read(buf);
            String contents = new String(buf);
            _server.updateConfigFile(_wiki.id(), type.getNumber(), contents, user);
        } else {
            System.out.println("File not found: " + filename);
        }
    }

    public static class Args {

        File configFile = null;

        String name = null;

        String title = null;

        String path = null;

        String inputName = null;

        String htmlTemplate = null;

        String cssTemplate = null;

        String layout = null;

        String urldecoding = null;

        String username = null;

        public Args(String[] args) throws IOException {
            configFile = new File(args[0]);
            if (args.length == 10) {
                name = args[1];
                title = args[2];
                path = args[3];
                inputName = args[4];
                htmlTemplate = args[5];
                cssTemplate = args[6];
                layout = args[7];
                urldecoding = args[8];
                username = args[9];
            } else if (args.length == 3) {
                String packageInfo = args[1];
                File packageFile = new File(packageInfo);
                File packageDir;
                if (packageFile.isDirectory()) {
                    packageDir = packageFile;
                    packageFile = new File(packageInfo + "pkginfo");
                } else {
                    packageDir = new File(packageFile.getParent());
                }
                Properties packageProperties = org.dbwiki.lib.IO.loadProperties(packageFile);
                name = packageProperties.getProperty(PackageInfoName);
                title = packageProperties.getProperty(PackageInfoTitle, name);
                path = packageProperties.getProperty(PackageInfoPath, name);
                inputName = packageDir + File.separator + packageProperties.getProperty(PackageInfoInputXML, name + ".xml");
                htmlTemplate = packageDir + File.separator + packageProperties.getProperty(PackageInfoTemplate, File.separator + "presentation" + File.separator + name + ".html");
                cssTemplate = packageDir + File.separator + packageProperties.getProperty(PackageInfoCSS, File.separator + "presentation" + File.separator + name + ".css");
                layout = packageDir + File.separator + packageProperties.getProperty(PackageInfoLayout, File.separator + "presentation" + File.separator + name + ".layout");
                urldecoding = packageDir + File.separator + packageProperties.getProperty(PackageInfoURLDecoding, File.separator + "presentation" + File.separator + name + ".urldecoding");
                username = args[2];
            } else {
                System.out.println("Usage: " + commandLine);
                System.exit(0);
            }
        }
    }

    public static void main(String[] argv) {
        try {
            Args args = new Args(argv);
            Properties properties = org.dbwiki.lib.IO.loadProperties(args.configFile);
            _server = new WikiServer(properties);
            InputStream in = null;
            File inputFile = new File(args.inputName);
            if (args.inputName.endsWith(".gz")) {
                in = new GZIPInputStream(new FileInputStream(inputFile));
            } else {
                in = new FileInputStream(inputFile);
            }
            StructureParser structureParser = new StructureParser();
            new SAXCallbackInputHandler(structureParser, false).parse(in, false, false);
            in.close();
            if (structureParser.hasException()) {
                throw structureParser.getException();
            }
            DatabaseSchema databaseSchema = structureParser.getDatabaseSchema(args.path);
            User user = _server.users().get(args.username);
            _server.registerDatabase(args.name, args.title, args.path, inputFile.toURI().toURL(), databaseSchema, user, 1, 0);
            _wiki = _server.get(args.name);
            assert (_wiki != null);
            DatabaseConnector connector = new DatabaseConnectorFactory().getConnector(properties);
            Connection con = connector.getConnection();
            con.setAutoCommit(false);
            loadPresentationFile(args.cssTemplate, PresentationFileType.CSS, user);
            loadPresentationFile(args.htmlTemplate, PresentationFileType.Template, user);
            loadPresentationFile(args.layout, PresentationFileType.Layout, user);
            loadPresentationFile(args.urldecoding, PresentationFileType.URLDecoding, user);
            con.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }
}
