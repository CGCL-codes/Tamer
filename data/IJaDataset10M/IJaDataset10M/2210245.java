package corina.cross;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import corina.Element;
import corina.Preview;
import corina.Previewable;
import corina.Range;
import corina.Sample;
import corina.core.App;
import corina.formats.WrongFiletypeException;
import corina.logging.CorinaLog;
import corina.prefs.Prefs;
import corina.ui.I18n;
import javax.swing.JLabel;

/**
   A crossdating grid.

   <p>All of the samples are listed down the left side, and also
   across the top.  If you go right from a sample on the left, and
   down from a different sample no the top, the cell where they meet
   contains their crossdate (t, trend, d, overlap).  If you trace
   right from a sample, and down from the same sample, that cell (on
   the diagonal) has the length of the sample, by convention.</p>

   <p>A Grid might look similar to this when printed:</p>

<blockquote class="paper">

   <table border="1" cellspacing="0">

	<tr>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	  <td>SPI2A.IND     </td>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	</tr>

	<tr>
	  <td>SPI2A.IND </td>
	  <td>n=54            </td>
	  <td>SPI3A.IND </td>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	</tr>

	<tr>
	  <td>SPI3A.IND       </td>
	  <td>t=0.00 <br> tr=47.2% <br> d=0.00 <br> n=54 </td>
	  <td>       n=170 </td>
	  <td>SPI4A.IND </td>
	</tr>

	<tr>
	  <td>SPI4A.IND </td>
	  <td>t=0.22 <br> tr=67.2% <br> d=0.03 <br> n=52 </td>
	  <td>t=1.63 <br> tr=55.6% <br> d=0.09 <br> n=55 </td>
	  <td>n=55     </td>
	</tr>

   </table>

</blockquote>

   <h2>Left to do</h2>
   <ul>
     <li>clean up this class: many many lines are longer than 80 characters
     <li>font handling code is sometimes inefficient (lots of "new Font(...)")
     <li>font handling code is sometimes incorrect (nudge factors
         instead of measuring ascents)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: Grid.java,v 1.15 2006/04/25 21:39:54 lucasmo Exp $
*/
public class Grid implements Runnable, Previewable {

    private static final CorinaLog log = new CorinaLog(Grid.class);

    private List files;

    private int num;

    private Cell cell[][];

    private Exception error = null;

    public List getFiles() {
        return files;
    }

    public interface Cell {

        public abstract void print(Graphics2D g2, int x, int y, int width, int height, float scale);

        public abstract String toXML();
    }

    public static class EmptyCell implements Cell {

        public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
        }

        public String toXML() {
            return "<empty/>";
        }
    }

    public class HeaderCell implements Cell {

        protected String name;

        public HeaderCell(String name) {
            int index = name.lastIndexOf(File.separatorChar);
            this.name = name.substring(index + 1);
        }

        Sample fixed;

        public Sample getFixed() {
            return fixed;
        }

        public HeaderCell(Sample fixed) {
            String name = (String) fixed.meta.get("filename");
            int index = name.lastIndexOf(File.separatorChar);
            this.name = name.substring(index + 1);
            this.fixed = fixed;
        }

        public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
            g2.setClip(x, y, width, height);
            g2.drawString(name, x + EPS, y + (int) ((getCellHeight() / 2 - getLineHeight() / 2) * scale));
        }

        public String toXML() {
            return "<header name=\"" + name + "\"/>";
        }
    }

    public class HeaderRangeCell extends HeaderCell {

        private Range range;

        public HeaderRangeCell(String name, Range range) {
            super(name);
            this.range = range;
        }

        public HeaderRangeCell(Sample fixed) {
            super(fixed);
            this.range = fixed.range;
        }

        public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
            g2.setClip(x, y, width, height);
            g2.drawString(name, x + EPS, y + (int) ((getCellHeight() / 2) * scale));
            g2.drawString(range.toString(), x + EPS, y + (int) ((getCellHeight() / 2 + getLineHeight()) * scale));
        }

        public String toXML() {
            return "<header name=\"" + name + "\" range=\"" + range + "\"/>";
        }
    }

    public static class CrossCell extends Single implements Cell {

        Sample fixed, moving;

        public CrossCell(Sample fixed, Sample moving) {
            super(fixed, moving);
            this.fixed = fixed;
            this.moving = moving;
        }

        public Sample getFixed() {
            return fixed;
        }

        public Sample getMoving() {
            return moving;
        }

        public CrossCell(float t, float tr, float d, float r, int n) {
            super(t, tr, d, r, n);
        }

        public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
            if (Boolean.valueOf(App.prefs.getPref(Prefs.GRID_HIGHLIGHT)).booleanValue() && isSignificant()) {
                Color oldColor = g2.getColor();
                g2.setColor(App.prefs.getColorPref(Prefs.GRID_HIGHLIGHTCOLOR, Color.green));
                g2.fillRect(x, y, (int) (getCellWidth() * scale), (int) (getCellHeight() * scale));
                g2.setColor(oldColor);
            }
            g2.drawRect(x, y, (int) (getCellWidth() * scale), (int) (getCellHeight() * scale));
            if (n < 10) {
                g2.drawString("n=" + n, x + EPS, y + (int) ((getCellHeight() / 2 - getLineHeight() / 2) * scale));
                return;
            }
            g2.drawString("t=" + formatT() + ", r=" + formatR(), x + EPS, y + (int) (getLineHeight() * scale) - EPS);
            g2.drawString("tr=" + formatTrend(), x + EPS, y + (int) (2 * getLineHeight() * scale) - EPS);
            g2.drawString("D=" + formatD(), x + EPS, y + (int) (3 * getLineHeight() * scale) - EPS);
            g2.drawString("n=" + String.valueOf(n), x + EPS, y + (int) (4 * getLineHeight() * scale) - EPS);
        }
    }

    public static class LengthCell implements Cell {

        private int length;

        LengthCell(int length) {
            this.length = length;
        }

        public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
            g2.drawString("n=" + length, x + EPS, y + (int) ((getCellHeight() / 2 - getLineHeight() / 2) * scale));
        }

        public String toXML() {
            return "<length n=\"" + length + "\"/>";
        }
    }

    private Cell makeCell(String name, Attributes atts) {
        if (name.equals("header")) {
            String r = atts.getValue("range");
            if (r != null) return new HeaderRangeCell(atts.getValue("name"), new Range(r)); else return new HeaderCell(atts.getValue("name"));
        } else if (name.equals("length")) {
            return new LengthCell(Integer.parseInt(atts.getValue("n")));
        } else if (name.equals("cross")) {
            return new CrossCell(Float.parseFloat(atts.getValue("t")), Float.parseFloat(atts.getValue("tr")), Float.parseFloat(atts.getValue("d")), Float.parseFloat(atts.getValue("r")), Integer.parseInt(atts.getValue("n")));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static class GridPage implements Printable {

        private Grid grid;

        private int startRow, endRow, startCol, endCol;

        public GridPage(Grid grid, int startRow, int endRow, int startCol, int endCol) {
            this.grid = grid;
            this.startRow = startRow;
            this.endRow = endRow;
            this.startCol = startCol;
            this.endCol = endCol;
        }

        public int print(Graphics g, PageFormat pf, int pageNr) throws PrinterException {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(0.1f));
            if (App.prefs.getPref("corina.grid.font") != null) g2.setFont(Font.decode(App.prefs.getPref("corina.grid.font")));
            int stopRow = Math.min(endRow, grid.cell.length - 1);
            int stopCol = Math.min(endCol, grid.cell[0].length - 1);
            for (int x = startCol; x <= stopCol; x++) {
                for (int y = startRow; y <= stopRow; y++) {
                    Cell c = grid.cell[y][x];
                    Shape oldclip = g2.getClip();
                    c.print(g2, ((int) pf.getImageableX()) + (x - startCol) * getCellWidth(), ((int) pf.getImageableY()) + (y - startRow) * getCellHeight(), getCellWidth(), getCellHeight(), 1.0f);
                    g2.setClip(oldclip);
                }
            }
            return PAGE_EXISTS;
        }
    }

    private static class GridPrinter implements Pageable {

        private Grid grid;

        private int size;

        private int rowsPerPage, colsPerPage, pagesWide, pagesTall, numPages;

        private PageFormat pf;

        public GridPrinter(Grid grid, PageFormat pf) {
            this.grid = grid;
            this.size = grid.size() + 1;
            this.pf = pf;
            rowsPerPage = ((int) pf.getImageableHeight()) / getCellHeight();
            colsPerPage = ((int) pf.getImageableWidth()) / getCellWidth();
            pagesWide = (int) Math.ceil((float) size / colsPerPage);
            pagesTall = (int) Math.ceil((float) size / rowsPerPage);
            numPages = pagesWide * pagesTall;
        }

        public int getNumberOfPages() {
            return numPages;
        }

        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            return pf;
        }

        public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
            if (pageIndex >= numPages) throw new IndexOutOfBoundsException();
            int x = pageIndex % pagesWide;
            int y = pageIndex / pagesWide;
            return new GridPage(grid, y * rowsPerPage, y * rowsPerPage + rowsPerPage - 1, x * colsPerPage, x * colsPerPage + colsPerPage - 1);
        }
    }

    public Pageable makeHardcopy(PageFormat pf) {
        return new GridPrinter(this, pf);
    }

    /**
	 Construct a Grid from a List of Elements.  Elements with
	 <code>active=false</code> are ignored.

	 @param elements the List of Elements to use
	 */
    public Grid(List elements) {
        files = elements;
        num = 0;
        for (int i = 0; i < files.size(); i++) if (((Element) files.get(i)).active) num++;
        cell = new Cell[num + 1][num + 1];
    }

    /**
	 Construct a Grid from an existing file.  Cells are loaded from
	 the previously-calculated values; the user must "refresh" the
	 display (<code>run()</code>) to update these values.

	 @param filename the file to load
	 @exception WrongFiletypeException if this file isn't a Grid
	 @exception FileNotFoundException if the file can't be found
	 @exception IOException if a low-level I/O exception occurs
	 */
    public Grid(String filename) throws WrongFiletypeException, FileNotFoundException, IOException {
        load(filename);
    }

    public Grid(Sequence seq) {
        files = new ArrayList();
        List fixed = seq.getAllFixed();
        for (int i = 0; i < fixed.size(); i++) files.add(new Element((String) fixed.get(i)));
        List moving = seq.getAllMoving();
        for (int i = 0; i < moving.size(); i++) if (!fixed.contains(moving.get(i))) files.add(new Element((String) moving.get(i)));
        num = files.size();
        cell = new Cell[num + 1][num + 1];
        run();
    }

    /**
	 The number of samples in this Grid.  Add one to this value to
	 get the number of cells high or wide the grid is.

	 @return the number of samples in this Grid
	 */
    public int size() {
        return num;
    }

    /**
	 Get a Cell from the grid.

	 @param row the row
	 @param column the column
	 @return the cell at (row, column)
	 */
    public Cell getCell(int row, int column) {
        return cell[row][column];
    }

    /** Compute the cells of this grid. */
    public void run() {
        AvgSingle averages = new AvgSingle();
        Sample buffer[] = new Sample[num];
        int read = 0;
        for (int i = 0; i < files.size(); i++) {
            Element e = (Element) files.get(i);
            if (!e.isActive()) continue;
            try {
                buffer[read] = e.load();
                read++;
            } catch (IOException ioe) {
                buffer[read] = null;
                read++;
                error = ioe;
                continue;
            }
        }
        for (int row = 0; row < num; row++) {
            Sample fixed = buffer[row];
            if (fixed == null || fixed.meta.get("filename") == null) continue;
            String filename = (String) fixed.meta.get("filename");
            cell[row + 1][0] = new HeaderCell(fixed);
            cell[row][row + 1] = new HeaderRangeCell(fixed);
            cell[row + 1][row + 1] = new LengthCell(fixed.data.size());
            for (int col = 0; col < row; col++) {
                Sample moving = buffer[col];
                if (moving == null || moving.meta.get("filename") == null) continue;
                Cell crosscell = new CrossCell(fixed, moving);
                cell[row + 1][col + 1] = crosscell;
                averages.addSingle((Single) crosscell);
            }
        }
        averages.calculateAverages();
        for (int col = 0; col < num + 1; col++) {
            if (cell[1][col] == null) {
                cell[1][col] = averages;
                break;
            }
        }
        EmptyCell e = new EmptyCell();
        for (int row = 0; row < num + 1; row++) for (int col = 0; col < num + 1; col++) if (cell[row][col] == null) cell[row][col] = e;
    }

    /**
	 Get the error that occurred while computing the grid.  The
	 run() method in Runnable can't throw any exceptions, so we just
	 store them here for later use.

	 @return an Exception, if one occurred, else null
	 */
    public Exception getError() {
        return error;
    }

    public static int getCellWidth() {
        return (int) (getCellHeight() * 1.4);
    }

    public static int getCellHeight() {
        int h;
        Font myFont = App.prefs.getFontPref("corina.grid.font", new Font("sansserif", Font.PLAIN, 12));
        h = myFont.getSize();
        return 4 * (h + 2 * EPS);
    }

    private static int getLineHeight() {
        return getCellHeight() / 4;
    }

    private static final int EPS = 2;

    /**
	 A short preview for file dialogs.  Displays "Crossdating Grid",
	 and lists the first few elements.

	 @return a preview component for this grid
	 */
    public Preview getPreview() {
        return new GridPreview(this);
    }

    private static class GridPreview extends Preview {

        GridPreview(Grid g) {
            title = I18n.getText("crossdating_grid");
            items = new ArrayList();
            items.add("(" + g.files.size() + " " + I18n.getText("total") + ")");
            for (int i = 0; i < g.files.size(); i++) {
                if (i == 4 && g.files.size() > 5) {
                    items.add("...");
                    break;
                }
                String filename = ((Element) g.files.get(i)).getFilename();
                items.add(new File(filename).getName());
            }
        }
    }

    /** A SAX2 handler for loading saved grid files. */
    private class GridHandler extends DefaultHandler {

        private boolean readAnything = false;

        private int row = 0, col = 0;

        private EmptyCell e = new EmptyCell();

        public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
            System.out.println("startElement");
            if (!readAnything) {
                if (name.equals("grid")) {
                    readAnything = true;
                    return;
                }
                throw new SAXException("Not a grid!");
            }
            if (name.equals("input")) {
                files = new ArrayList();
                return;
            }
            if (name.equals("sample")) {
                files.add(new Element(atts.getValue("filename")));
                return;
            }
            if (name.equals("output")) {
                cell = new Cell[num + 1][num + 1];
                return;
            }
            if (name.equals("empty")) {
                cell[row][col] = e;
                return;
            }
            try {
                Cell c = makeCell(name, atts);
                cell[row][col] = c;
            } catch (IllegalArgumentException iae) {
            }
        }

        public void endElement(String uri, String name, String qName) {
            System.out.println("endElement");
            if (name.equals("input")) {
                num = files.size();
                return;
            }
            if (name.equals("empty") || name.equals("header") || name.equals("length") || name.equals("cross")) {
                col++;
                return;
            }
            if (name.equals("row")) {
                col = 0;
                row++;
                return;
            }
        }
    }

    /**
	 Load a grid, saved in XML format.

	 @param filename the target to load
	 @exception WrongFiletypeException if this file isn't a Grid
	 @exception FileNotFoundException if there is no file by this name
	 @exception IOException if an I/O exception occurs while trying
	 to load
	 */
    public void load(String filename) throws WrongFiletypeException, FileNotFoundException, IOException {
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            GridHandler loader = new GridHandler();
            xr.setContentHandler(loader);
            xr.setErrorHandler(loader);
            System.out.println("reading " + filename + " as xml");
            FileReader r = new FileReader(filename);
            xr.parse(new InputSource(r));
            System.out.println("done parsing");
        } catch (SAXException se) {
            throw new WrongFiletypeException();
        }
    }

    /**
	 Save this grid in XML format.

	 @param filename the target to save to
	 @exception IOException if an I/O exception occurs while trying to save
	 */
    public void save(String filename) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));
        try {
            w.write("<?xml version=\"1.0\"?>\n");
            w.write("\n");
            w.write("<grid>\n");
            w.write("\n");
            w.write("  <input>\n");
            for (int i = 0; i < files.size(); i++) {
                w.write("    <sample filename=\"" + files.get(i) + "\"/>\n");
            }
            w.write("  </input>\n");
            w.write("\n");
            w.write("  <output>\n");
            for (int r = 0; r < cell.length; r++) {
                w.write("    <row>\n");
                for (int c = 0; c < cell[r].length; c++) w.write("      " + cell[r][c].toXML() + "\n");
                w.write("    </row>\n");
            }
            w.write("  </output>\n");
            w.write("\n");
            w.write("</grid>\n");
        } finally {
            try {
                w.close();
            } catch (IOException ioe) {
                log.error("Error closing writer", ioe);
            }
        }
    }

    private class AvgSingle extends Single implements Cell {

        private int numCrosses;

        private float cumt, cumtr, cumd, cumr;

        ;

        public AvgSingle() {
            super();
            numCrosses = 0;
            cumt = cumtr = cumd = cumr = 0.0f;
        }

        public void addSingle(Single cross) {
            if (cross.n < 10) return;
            cumt += cross.t;
            cumtr += cross.tr;
            cumd += cross.d;
            cumr += cross.r;
            numCrosses++;
        }

        public void calculateAverages() {
            t = cumt / numCrosses;
            tr = cumtr / numCrosses;
            d = cumd / numCrosses;
            r = cumr / numCrosses;
        }

        public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
            g2.drawRect(x, y, (int) (getCellWidth() * scale), (int) (getCellHeight() * scale));
            g2.drawString("t=" + formatT() + ", r=" + formatR(), x + EPS, y + (int) (2 * getLineHeight() * scale) - EPS);
            g2.drawString("tr=" + formatTrend(), x + EPS, y + (int) (3 * getLineHeight() * scale) - EPS);
            g2.drawString("D=" + formatD(), x + EPS, y + (int) (4 * getLineHeight() * scale) - EPS);
            g2.drawString("Averages n>=10", x + EPS, y + (int) (1 * getLineHeight() * scale) - EPS);
        }
    }
}
