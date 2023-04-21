package xhtmldoclet.pages;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import xhtmldoclet.XhtmlPageWriter;

/**
 * Creates "help-doc.html", a page for explaining the javadoc layout.
 */
public class Help extends XhtmlPageWriter {

    /**
	 * Generate "help-doc.html" and handle errors.
	 */
    public static void generatePage() {
        if (conf.nohelp) return;
        String filename = "help-doc" + conf.ext;
        try {
            new Help(filename);
        } catch (IOException exc) {
            throw XhtmlPageWriter.docletException(exc, filename);
        }
    }

    /**
	 * Initialize and create help page with the given filename. Copy input from
	 * either the default or user-specified help file. Reads data from the
	 * specified file and writes it directly to the help file, within the same
	 * document structure, inside #Content.
	 * 
	 * @param filename The desired name of the file (with extension).
	 * @throws IOException If creation of {@link FileOutputStream} fails.
	 */
    private Help(String filename) throws IOException {
        super(filename);
        this.filename = filename;
        pageType = PageType.HELP;
        windowTitle = (conf.windowtitle.length() > 0) ? conf.windowtitle : conf.getText("Help");
        printXhtmlHeader();
        InputStreamReader stream;
        if (conf.helpfile != "") stream = new InputStreamReader(new FileInputStream(conf.helpfile)); else stream = new InputStreamReader(XhtmlPageWriter.class.getResourceAsStream("resources/help" + conf.ext));
        char[] buf = new char[2048];
        int n;
        while ((n = stream.read(buf)) > 0) write(buf, 0, n);
        stream.close();
        println();
        printXhtmlFooter();
        this.close();
    }

    /** Highlight "Help" as current section, don't create link. */
    protected void navLinkHelp() {
        println(listItemCurrent(HELP));
    }
}
