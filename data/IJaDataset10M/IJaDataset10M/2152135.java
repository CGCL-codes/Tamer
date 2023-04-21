package refactorfit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import fit.Parse;

/**
 * The {@code FitPage} class represents a single <a href="http://fit.c2.com">FIT</a>
 * test page.
 *
 * @author Mark Hopkins
 * @version 13/10/2008
 * 
 */
public class FitPage {

    private Parse pageContents;

    /**
	 * Initialises a newly created {@code FitPage} object so that it
	 * represents the HTML page contents given.
	 * 
	 * @param pageContents
	 *        the HTML page contents which must include at least one table.
	 */
    public FitPage(String pageContents) throws Exception {
        this.pageContents = new Parse(pageContents, new String[] { "table", "tr", "td" });
    }

    /**
	 * Creates and returns a new {@code FitPage} object from the contents of the
	 * {@link java.io.File File} object provided.
	 * 
	 * @param pageFile
	 *        the {@link java.io.File File} object from which the details
	 *        should be read
	 * @return a newly created {@code FitPage} object 
	 */
    public static FitPage readFromFile(File pageFile) throws Exception {
        char chars[] = new char[(int) (pageFile.length())];
        FileReader in = new FileReader(pageFile);
        in.read(chars);
        in.close();
        return new FitPage(new String(chars));
    }

    /**
	 * Writes the contents of this {@code FitPage} object to the file
	 * represented by the {@link java.io.File File} object provided.
	 * 
	 * @param outputFile
	 *        the details of the file to write to.
	 */
    public void write(File outputFile) throws IOException {
        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
        pageContents.print(output);
        output.close();
    }

    /**
	 * Returns a list of {@link refactorfit.FitTable FitTable} objects that
	 * this {@code FitPage} contains.
	 * 
	 * @return the list of {@link refactorfit.FitTable FitTable} objects
	 * that this {@code FitPage} contains.
	 */
    public List<FitTable> getTables() {
        List<FitTable> listOfTables = new ArrayList<FitTable>();
        Parse nextTable = pageContents;
        while (nextTable != null) {
            listOfTables.add(new FitTable(nextTable));
            nextTable = nextTable.more;
        }
        return listOfTables;
    }
}
