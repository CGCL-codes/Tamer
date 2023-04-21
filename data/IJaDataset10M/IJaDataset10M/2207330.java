package net.sf.ediknight;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/**
 *
 */
public final class Process {

    /** */
    private static final String TRANSFORMER_CLASS_STX = "net.sf.joost.trax.TransformerFactoryImpl";

    /**
     * No instantiation.
     */
    private Process() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ClassLoader classLoader = getProcessClassLoader();
            Thread thread = Thread.currentThread();
            thread.setContextClassLoader(classLoader);
            CommandLine cmd = parseCommandLine(args);
            ConverterFactory factory = ConverterFactory.newInstance();
            Converter converter = factory.createConverter("default");
            InputStream in = null;
            String optionIn = cmd.getOptionValue("in");
            if (optionIn == null) {
                in = System.in;
            } else {
                in = new FileInputStream(optionIn);
            }
            in = converter.recognize(in);
            Transformer transformer = null;
            boolean indent = true;
            String optionXsl = cmd.getOptionValue("xsl");
            if (optionXsl == null) {
                optionXsl = cmd.getOptionValue("stx");
                if (optionXsl != null) {
                    indent = false;
                    System.setProperty("javax.xml.transform.TransformerFactory", TRANSFORMER_CLASS_STX);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            if (optionXsl == null) {
                transformer = transformerFactory.newTransformer();
            } else {
                InputStream xsl = new BufferedInputStream(new FileInputStream(optionXsl));
                InputSource input = new InputSource(xsl);
                Source source = new SAXSource(input);
                transformer = transformerFactory.newTransformer(source);
            }
            if (indent) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            }
            if (cmd.hasOption("text") || cmd.hasOption("html")) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }
            Parser<Format> parser = converter.getParser();
            XMLReader reader = parser.getXMLReader();
            InputSource input = new InputSource(in);
            Source source = new SAXSource(reader, input);
            OutputStream out = null;
            String optionOut = cmd.getOptionValue("out");
            if (optionOut == null) {
                out = System.out;
            } else {
                out = new BufferedOutputStream(new FileOutputStream(optionOut));
            }
            String optionFormat = cmd.getOptionValue("format");
            ContentHandler contentHandler = converter.getContentHandler(optionFormat, out);
            try {
                Result result = null;
                if (contentHandler != null) {
                    result = new SAXResult(contentHandler);
                    if (contentHandler instanceof LexicalHandler) {
                        ((SAXResult) result).setLexicalHandler((LexicalHandler) contentHandler);
                    }
                } else {
                    result = new StreamResult(out);
                }
                transformer.transform(source, result);
            } finally {
                out.close();
            }
            System.exit(0);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (TransformerException ex) {
            Throwable contained = ex.getException();
            if (contained instanceof SAXException) {
                SAXException saxException = (SAXException) contained;
                contained = saxException.getException();
                if (contained instanceof ParseException) {
                    ParseException parseException = (ParseException) contained;
                    System.err.println(parseException.getMessage());
                    System.exit(-1);
                }
            }
            System.err.println(ex.getMessage());
        } catch (ConfigurationException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.exit(-1);
    }

    /**
     * @param args the command line arguments as a string array
     * @return the command line
     */
    private static CommandLine parseCommandLine(String[] args) {
        Options options = new Options();
        options.addOption("in", true, "input edifact url");
        options.addOption("out", true, "output file");
        OptionGroup trafosGroup = new OptionGroup();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            classLoader.loadClass(TRANSFORMER_CLASS_STX);
            Option stxOption = new Option("stx", true, "stx transformation script");
            trafosGroup.addOption(stxOption);
        } catch (ClassNotFoundException ex) {
        }
        Option xslOption = new Option("xsl", true, "xsl transformation script");
        trafosGroup.addOption(xslOption);
        options.addOptionGroup(trafosGroup);
        Option formatOption = new Option("format", true, "the output formatter");
        options.addOption(formatOption);
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ediknight", options);
            System.exit(1);
        }
        return cmd;
    }

    /**
     * Creates a classloader that scans the <it>lib</it>
     * directory for JAR files.
     *
     * @return the classloader
     */
    private static ClassLoader getProcessClassLoader() {
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        String ediknightHomePath = System.getProperty("net.sf.ediknight.home");
        if (ediknightHomePath == null || ediknightHomePath.trim().length() == 0) {
            return parent;
        }
        File ediknightHome = new File(ediknightHomePath);
        if (!ediknightHome.isDirectory()) {
            return parent;
        }
        File ediknightLib = new File(ediknightHome, "lib");
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };
        File[] jars = ediknightLib.listFiles(filter);
        URL[] urls = new URL[jars.length];
        for (int k = 0; k < urls.length; ++k) {
            try {
                urls[k] = jars[k].toURI().toURL();
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex.toString(), ex);
            }
        }
        return new URLClassLoader(urls, parent);
    }
}
