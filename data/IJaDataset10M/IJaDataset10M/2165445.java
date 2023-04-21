package org.dbwiki.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;
import org.dbwiki.data.index.DatabaseContent;
import org.dbwiki.data.io.ExportNodeWriter;
import org.dbwiki.data.io.NodeWriter;
import org.dbwiki.data.resource.NodeIdentifier;
import org.dbwiki.data.wiki.DatabaseWikiPage;
import org.dbwiki.data.wiki.WikiPageDescription;
import org.dbwiki.exception.WikiException;
import org.dbwiki.web.server.DatabaseWiki;
import org.dbwiki.web.server.WikiServer;
import org.dbwiki.lib.XML;
import org.dbwiki.main.ImportPresentationFiles.PresentationFileType;

public class DatabasePackageExport {

    private static final String commandLine = "DatabasePackageExport <config-file> [<packageinfo>|<name> <title> <path> <xml-file> <html-template> <css> <layout>]";

    private static WikiServer _server;

    private static DatabaseWiki _wiki;

    private static String PackageInfoName = "NAME";

    private static String PackageInfoTitle = "TITLE";

    private static String PackageInfoPath = "PATH";

    private static String PackageInfoInputXML = "INPUT_XML";

    private static String PackageInfoTemplate = "TEMPLATE";

    private static String PackageInfoCSS = "CSS";

    private static String PackageInfoURLDecoding = "URLDECODING";

    private static String PackageInfoLayout = "LAYOUT";

    private static String PackageInfoWiki = "WIKI";

    public static void savePresentationFile(String filename, int version, PresentationFileType type) throws IOException, WikiException {
        File outputFile = new File(filename);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        assert (writer != null);
        String configFile = null;
        switch(type) {
            case CSS:
                configFile = _server.getStyleSheet(_wiki, version);
                break;
            case Layout:
                configFile = _server.getLayout(_wiki, version);
                break;
            case Template:
                configFile = _server.getTemplate(_wiki, version);
                break;
            case URLDecoding:
                configFile = _server.getURLDecoding(_wiki, version);
                break;
        }
        writer.write(configFile);
        writer.close();
    }

    public static class Args {

        File configFile = null;

        String name = null;

        String title = null;

        String path = null;

        String xmlFile = null;

        String htmlTemplate = null;

        String cssTemplate = null;

        String urldecoding = null;

        String layout = null;

        String wikiDir = null;

        public Args(String[] args) throws IOException {
            configFile = new File(args[0]);
            if (args.length == 10) {
                name = args[1];
                title = args[2];
                path = args[3];
                xmlFile = args[4];
                htmlTemplate = args[5];
                cssTemplate = args[6];
                layout = args[7];
                urldecoding = args[8];
                wikiDir = args[9];
            } else if (args.length == 2) {
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
                xmlFile = packageDir + File.separator + packageProperties.getProperty(PackageInfoInputXML, name + ".xml");
                htmlTemplate = packageDir + File.separator + packageProperties.getProperty(PackageInfoTemplate, File.separator + "presentation" + File.separator + name + ".html");
                cssTemplate = packageDir + File.separator + packageProperties.getProperty(PackageInfoCSS, File.separator + "presentation" + File.separator + name + ".css");
                urldecoding = packageDir + File.separator + packageProperties.getProperty(PackageInfoURLDecoding, File.separator + "presentation" + File.separator + name + ".urldecoding");
                layout = packageDir + File.separator + packageProperties.getProperty(PackageInfoLayout, File.separator + "presentation" + File.separator + name + ".layout");
                wikiDir = packageDir + File.separator + packageProperties.getProperty(PackageInfoWiki, "wiki");
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
            OutputStream out = null;
            File outputFile = new File(args.xmlFile);
            if (args.xmlFile.endsWith(".gz")) {
                out = new GZIPOutputStream(new FileOutputStream(outputFile));
            } else {
                out = new FileOutputStream(outputFile);
            }
            _wiki = _server.get(args.name);
            NodeWriter writer = new ExportNodeWriter();
            BufferedWriter outstr = new BufferedWriter(new OutputStreamWriter(out));
            writer.init(outstr);
            _wiki.database().export(new NodeIdentifier(), _wiki.database().versionIndex().getLastVersion().number(), writer);
            outstr.close();
            out.close();
            assert (_wiki != null);
            savePresentationFile(args.cssTemplate, _wiki.getCSSVersion(), PresentationFileType.CSS);
            savePresentationFile(args.htmlTemplate, _wiki.getTemplateVersion(), PresentationFileType.Template);
            savePresentationFile(args.layout, _wiki.getLayoutVersion(), PresentationFileType.Layout);
            savePresentationFile(args.urldecoding, _wiki.getURLDecodingVersion(), PresentationFileType.URLDecoding);
            assert (_wiki.wiki() != null);
            DatabaseContent wikiContent = _wiki.wiki().content();
            File wikiDirFile = new File(args.wikiDir);
            if (!wikiDirFile.exists()) {
                System.err.println("Creating wiki directory " + args.wikiDir);
                wikiDirFile.mkdir();
            }
            if (wikiDirFile.isDirectory()) {
                for (int i = 0; i < wikiContent.size(); i++) {
                    WikiPageDescription wikiEntry = (WikiPageDescription) wikiContent.get(i);
                    File wikiFile = new File(args.wikiDir + File.separator + "page_" + i + ".xml");
                    OutputStream wikioutstream = new FileOutputStream(wikiFile);
                    OutputStreamWriter wikiout = new OutputStreamWriter(wikioutstream);
                    DatabaseWikiPage content = _wiki.wiki().get(wikiEntry.identifier());
                    wikiout.write("<page id=\"" + content.getID() + "\" title=\"" + content.getName());
                    if (content.getUser() != null) {
                        wikiout.write("\" user=\"" + content.getUser().login());
                    }
                    wikiout.write("\" timestamp=\"" + content.getTimestamp() + "\" >\n");
                    wikiout.write(XML.maskText(content.getContent()));
                    wikiout.write("</page>");
                    wikiout.close();
                    wikioutstream.close();
                }
            } else {
                throw new Exception("Wiki directory path is not a directory");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }
}
