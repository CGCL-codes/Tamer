package be.lassi.web;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.xml.sax.SAXException;

/**
 * Builds the user manual pages.
 *
 */
public class ManualBuilder extends DocBuilder {

    /**
     * Constructs a new instance.
     *
     * @param targetDir the directory on which to store the generated web site pages
     */
    public ManualBuilder(final String sourceDir, final String targetDir) {
        super(new File(sourceDir + "/help/pages").getAbsolutePath(), targetDir);
    }

    @Override
    protected void processLine(final Page page, final String string) {
        if (string.startsWith("<!-- TOC -->")) {
            printTableOfContents(page);
        }
        super.processLine(page, string);
    }

    private void printTableOfContents(final Page page) {
        try {
            Parse parse = new Parse(page.getWriter());
            printTOC(parse, "ui/main.html");
            printTOC(parse, "ui/sheet.html");
            printTOC(parse, "ui/cues.html");
            printTOC(parse, "ui/functionKeys.html");
            printTOC(parse, "ui/groups.html");
            printTOC(parse, "ui/log.html");
            printTOC(parse, "ui/logOptions.html");
            printTOC(parse, "ui/midiControl.html");
            printTOC(parse, "ui/patch.html");
            printTOC(parse, "ui/sysexCommands.html");
            printTOC(parse, "ui/timing.html");
            printTOC(parse, "ui/tryout.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printTOC(final Parse parse, final String filename) throws IOException, SAXException {
        String name = getSourceDir() + "/manual/" + filename;
        Reader reader = new FileReader(name);
        try {
            parse.parse(filename, reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Processes the manual pages.
     *
     * @param args the command line arguments
     * @throws IOException if problem with reading or writing the files
     */
    public static void main(final String[] args) throws IOException {
        String sourceDir = "/lassi/workspace/lassi/src";
        String targetDir = "/lassi/web";
        if (args.length >= 2) {
            sourceDir = args[0];
            targetDir = args[1];
        }
        new ManualBuilder(sourceDir, targetDir).process();
        System.out.println("Ready");
    }
}
