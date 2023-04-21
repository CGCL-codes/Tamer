package de.igdr;

import java.io.File;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import de.igdr.schemaprof.HTMLAction;

/**
 * Class with commandline interface for XMLPrPrPr (XML Profile Pretty Printer).
 * @author ebert
 * @since 1.5
 *
 */
public final class XMLProfilePrettyPrinter {

    public static final String ApplicationName = "XML Profile Pretty Printer" + " " + HTMLAction.serialVersionUID;

    public static final String AppJarName = "XMLPrPrPr.jar";

    public static final String CopyrightNotice = "\nCopyright 2006 Fraunhofer IGD Rostock\nThis product includes software developed by\nThe Apache Software Foundation (http://www.apache.org/).\n";

    private static final Log LOG = LogFactory.getLog(XMLProfilePrettyPrinter.class);

    /**
	 * Only for Testing in CommandLineTest.java
	 *
	 */
    protected XMLProfilePrettyPrinter() {
        super();
    }

    /**
	 * Function to start Transforming with the Options.
	 * 
	 * @param profil	<code>String</code> with the full path of the SchemaProf-Profile.
	 * @param output	<code>String</code> with the full path of the Output-File
	 * @param root		<code>String</code> with an optional root-Element
	 * @param control	<code>int</code>    for output (IMS_SIMPLE, IMS_PDF,SIMPLE_HTML,SIMPLE_PDF)<br> @see de.igdr.schemaprof.HTMLAction  
	 * @param language	<code>String</code> with the language of the annotations in <a href="http://www.iso.org/iso/en/prods-services/iso3166ma/02iso-3166-code-lists/index.html">ISO 3166 Code</a>
	 * @throws Exception
	 */
    private XMLProfilePrettyPrinter(String profil, String output, String root, int control, String language) throws Exception {
        checkFile(profil, "Profil");
        checkFile(output, "Output");
        if ((root == null) || (root.length() == 0)) {
            root = "all Elements";
            LOG.info("Use no specific element as root.");
        }
        File Jar_File_Path;
        Jar_File_Path = JarSearcher.getJarDirectory(this);
        if (Jar_File_Path != null) {
            LOG.debug("Jar-File Path JarSearcher: " + JarSearcher.getJarDirectory(this));
        } else {
            Jar_File_Path = new File(FilenameUtils.getFullPath(new File(XMLProfilePrettyPrinter.AppJarName).getAbsolutePath()));
            LOG.debug("Jar-File Path FilenamUtil: " + Jar_File_Path.getAbsolutePath());
        }
        try {
            String schema = getSchemaFileName(profil);
            (new HTMLAction(null)).generateOutput(schema, profil, Jar_File_Path, "en", root, output, control, profil);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
	 * Function to test the this Class with CommandLineTest-Class. This is called from CommandLineTest.
	 * 
	 * @param arg <code>ArgumentsClassForTesting</code>-Object with	arguments for the Commandline-Test.<br> @see de.igdr.ArgumentsClassForTesting
	 * @throws Exception
	 */
    protected void print(final ArgumentsClassForTesting arg) throws Exception {
        new XMLProfilePrettyPrinter(arg.getProfile(), arg.getOutput(), arg.getRoot(), arg.getControl(), arg.getLanguage());
    }

    /**
	 * This Class Checks if a file exist or not exist.
	 * 
	 * @param filename <code>String</code>-Object with relative path or full path to the file.
	 * @param label    <code>String</code>-Object with the Label for the File, only for Logging. e.g. Profile, Output
	 */
    private void checkFile(String filename, String label) {
        File f;
        f = new File(filename);
        String logstr = label + "(" + filename + "): ";
        if (!f.exists()) {
            logstr = logstr + "nicht ";
        }
        logstr = logstr + ("vorhanden");
        LOG.info(logstr);
    }

    /**
     *  Extract the name of the XML schema from the profile file and
     *  add the file name to the path of the profile.
     *  
     * @param profile	<code>String</code> with the filepath to the profile
     * @return 			<code>String</code>with the filepath of the baseschema			
     * @throws Exception 
     */
    private String getSchemaFileName(String profile) throws Exception {
        LOG.debug(profile);
        String result = null;
        final File file = new File(profile);
        org.jdom.Document doc = null;
        final SAXBuilder builder = new SAXBuilder();
        doc = builder.build(file);
        final Object obj = XPath.selectSingleNode(doc, "/sp:schema_mod");
        if (obj instanceof Element) {
            final Element element = (Element) obj;
            result = element.getAttributeValue("baseSchema");
        } else {
            throw new Exception("Error in XMP profile.");
        }
        String path = FilenameUtils.getFullPath(profile);
        result = FilenameUtils.concat(path, result);
        LOG.debug(result);
        return result;
    }

    /** 
     * standalone application with an command line interface<br>
	 * schema "C:\\Entwicklung\\sp-plugin\\SPP\\Beispiele\\Profil aus Schemaprof\\imsmd_rootv1p2p1.xsd"<br>
	 * profil "C:\\Entwicklung\\sp-plugin\\SPP\\Beispiele\\Profil aus Schemaprof\\testprofil3.xml"<br>
	 * output "C:\\testPr3.html"
	 * @param args
	 */
    public static void main(String[] args) {
        long start_time = System.nanoTime();
        System.out.println(ApplicationName);
        System.out.println(CopyrightNotice);
        CommandLine cmd = null;
        final Options options = new Options();
        options.addOption("h", false, "Print help for this Application");
        options.addOption("r", true, "Set a Root element");
        Option op;
        op = new Option("p", true, "XML Schema Profile *.xml");
        op.setRequired(true);
        options.addOption(op);
        op = new Option("o", true, "HTML Output file *.html or PDF Output file *.pdf");
        op.setRequired(true);
        options.addOption(op);
        options.addOption("s", true, "Style of output file (SIMPLE|IMS). Default is SIMPLE");
        options.addOption("f", true, "File format of output file (HTML|PDF (only in IMS-Style)). Default is HTML.");
        options.addOption("l", true, "Set a language of the Descriptions of the Schema File (en, de, it, fr, es). Default is en (English)");
        BasicParser parser = new BasicParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println("Error when parsing the Arguments: " + e.getLocalizedMessage());
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("java -jar " + AppJarName + " -p profile.xml -o output.html", options);
        }
        if ((cmd != null)) {
            if (cmd.hasOption("h")) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp(AppJarName + " -p profile.xml -x xmlschema.xsd -o output.html", options);
            } else {
                String profile = null;
                String output = null;
                String root = null;
                String style = null;
                String fformat = null;
                String language = null;
                profile = cmd.getOptionValue("p");
                output = cmd.getOptionValue("o");
                root = cmd.getOptionValue("r");
                style = cmd.getOptionValue("s");
                fformat = cmd.getOptionValue("f");
                language = cmd.getOptionValue("l");
                if (style == null) {
                    style = "SIMPLE";
                }
                if (fformat == null) {
                    fformat = "HTML";
                }
                if (style.equalsIgnoreCase("Simple") && fformat.equalsIgnoreCase("PDF")) {
                    System.out.println("Wrong Input. You cannot choose 'Simple Style' and PDF-Output");
                    LOG.warn("Wrong Input. You cannot choose 'Simple Style' and PDF-Output");
                    System.exit(1);
                }
                int control = HTMLAction.HTML_SIMPLE;
                if (fformat.equalsIgnoreCase("HTML")) {
                    if (style.equalsIgnoreCase("IMS")) {
                        control = HTMLAction.HTML_IMS;
                    } else if (style.equalsIgnoreCase("SIMPLE")) {
                        control = HTMLAction.HTML_SIMPLE;
                    } else {
                        LOG.warn("Unknown argument in style option.");
                    }
                } else if (fformat.equalsIgnoreCase("PDF")) {
                    if (style.equalsIgnoreCase("IMS")) {
                        control = HTMLAction.PDF_IMS;
                    } else if (style.equalsIgnoreCase("SIMPLE")) {
                        control = HTMLAction.PDF_SIMPLE;
                    } else {
                        LOG.warn("Unknown argument in style option.");
                    }
                } else {
                    LOG.warn("Unknown argument in file format option.");
                }
                try {
                    new XMLProfilePrettyPrinter(profile, output, root, control, language);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LOG.info("Time: " + (System.nanoTime() - start_time) + " ns");
            }
        }
    }
}
