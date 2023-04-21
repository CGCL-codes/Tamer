package net.sf.grotag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import net.sf.grotag.common.AmigaPathList;
import net.sf.grotag.guide.DomWriter;
import net.sf.grotag.guide.ExportTools;
import net.sf.grotag.guide.Guide;
import net.sf.grotag.guide.GuidePile;
import net.sf.grotag.view.GrotagFrame;
import org.xml.sax.SAXException;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

/**
 * Grotag - View, convert, validate and pretty print Amigaguide documents.
 * 
 * @author Thomas Aglassinger
 */
public class Grotag {

    private GrotagFrame viewer;

    private GrotagJsap jsap;

    private AmigaPathList amigaPaths;

    private Grotag() throws JSAPException {
        jsap = new GrotagJsap();
    }

    private void setAmigaPaths() throws SAXException, IOException, ParserConfigurationException {
        amigaPaths = new AmigaPathList();
        try {
            amigaPaths.read(new File("grotag.xml"));
        } catch (FileNotFoundException errorToIgnore) {
            Logger.getLogger(Grotag.class.getName()).fine("ignored missing grotag.xml");
        }
    }

    @SuppressWarnings("unchecked")
    private void work(String[] arguments) throws IOException, ParserConfigurationException, TransformerException, SAXException {
        JSAPResult options = jsap.parse(arguments);
        if (!options.success()) {
            Iterator<String> errorRider = options.getErrorMessageIterator();
            String errorMessage;
            if (errorRider.hasNext()) {
                errorMessage = errorRider.next();
            } else {
                errorMessage = null;
            }
            throw new IllegalArgumentException(errorMessage);
        }
        boolean isDocBook = options.getBoolean(GrotagJsap.ARG_DOCBOOK);
        boolean isXhtml = options.getBoolean(GrotagJsap.ARG_XHTML);
        boolean isHtml = options.getBoolean(GrotagJsap.ARG_HTML);
        boolean isPretty = options.getBoolean(GrotagJsap.ARG_PRETTY);
        boolean isValidate = options.getBoolean(GrotagJsap.ARG_VALIDATE);
        if (isDocBook || isHtml || isPretty || isValidate || isXhtml) {
            File files[] = options.getFileArray(GrotagJsap.ARG_FILE);
            assert files != null;
            if (files.length == 0) {
                throw new IllegalArgumentException("Amigaguide input file must be specified");
            }
            setAmigaPaths();
            if (isDocBook) {
                docBook(files);
            } else if (isHtml || isXhtml) {
                html(files, isXhtml);
            } else if (isPretty) {
                pretty(files);
            } else if (isValidate) {
                validate(files);
            } else {
                assert false;
            }
        } else if (options.getBoolean(GrotagJsap.ARG_HELP)) {
            jsap.printHelp(System.err);
        } else if (options.getBoolean(GrotagJsap.ARG_LICENSE)) {
            jsap.printLicense(System.out);
        } else if (options.getBoolean(GrotagJsap.ARG_VERSION)) {
            jsap.printVersion(System.out);
        } else {
            File files[] = options.getFileArray(GrotagJsap.ARG_FILE);
            assert files != null;
            if (files.length > 1) {
                throw new IllegalArgumentException("only one Amigaguide input file must be specified for viewing");
            }
            setAmigaPaths();
            viewer = new GrotagFrame();
            viewer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            viewer.pack();
            viewer.setVisible(true);
            if (files.length != 0) {
                viewer.read(files[0], amigaPaths);
            }
        }
    }

    private void docBook(File[] files) throws IOException, ParserConfigurationException, TransformerException {
        ExportTools exportTools = ExportTools.getInstance();
        int fileCount = files.length;
        File inputFile;
        File outputFile;
        if (fileCount == 0) {
            throw new IllegalArgumentException("Amigaguide input file must be specified");
        } else if (fileCount == 1) {
            inputFile = files[0];
            File outputFileFolder = inputFile.getParentFile();
            outputFile = exportTools.targetFileFor(inputFile, outputFileFolder, "xml");
        } else if (fileCount == 2) {
            inputFile = files[0];
            outputFile = files[1];
        } else {
            throw new IllegalArgumentException("with --" + GrotagJsap.ARG_DOCBOOK + " only 2 files must be specified instead of " + fileCount);
        }
        GuidePile pile = GuidePile.createGuidePile(inputFile, amigaPaths);
        exportTools.exportAsDocBookXml(pile, outputFile);
    }

    private void html(File[] files, boolean isXhtml) throws IOException, ParserConfigurationException, TransformerException {
        assert files != null;
        String argumentName;
        DomWriter.Dtd dtd;
        if (isXhtml) {
            argumentName = GrotagJsap.ARG_XHTML;
            dtd = DomWriter.Dtd.XHTML;
        } else {
            argumentName = GrotagJsap.ARG_HTML;
            dtd = DomWriter.Dtd.HTML;
        }
        int fileCount = files.length;
        File inputFile;
        File outputFolder;
        if (fileCount == 0) {
            throw new IllegalArgumentException("Amigaguide input file must be specified");
        } else if (fileCount == 1) {
            inputFile = files[0];
            outputFolder = new File(System.getProperty("user.dir"));
        } else if (fileCount == 2) {
            inputFile = files[0];
            outputFolder = files[1];
        } else {
            throw new IllegalArgumentException("with --" + argumentName + " only 2 files must be specified instead of " + fileCount);
        }
        GuidePile pile = GuidePile.createGuidePile(inputFile, amigaPaths);
        ExportTools.getInstance().exportAsHtml(pile, outputFolder, dtd);
    }

    private void pretty(File[] files) throws IOException {
        for (File guideFile : files) {
            Guide guide = Guide.createGuide(guideFile, amigaPaths);
            guide.writePretty(guideFile);
        }
    }

    private void validate(File[] files) throws IOException {
        for (File guideFile : files) {
            GuidePile.createGuidePile(guideFile, amigaPaths);
        }
    }

    public static void main(final String[] arguments) throws Exception {
        Logger mainLog = Logger.getLogger(Grotag.class.getName());
        int exitCode = 1;
        try {
            Grotag grotag = new Grotag();
            grotag.work(arguments);
            exitCode = 0;
        } catch (IllegalArgumentException error) {
            mainLog.log(Level.FINE, "cannot process command line options: " + error.getMessage(), error);
            System.err.println("cannot process command line options: " + error.getMessage());
            error.printStackTrace();
        } catch (Exception error) {
            mainLog.log(Level.SEVERE, "cannot run Grotag: " + error.getMessage(), error);
        }
        if (exitCode > 0) {
            System.exit(exitCode);
        }
    }
}
