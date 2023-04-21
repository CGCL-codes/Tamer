package jmri.util.davidflanagan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.JWindow;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.JButton;
import jmri.util.JmriJFrame;

/**
 * This is from Chapter 12 of the O'Reilly Java book by
 * David Flanagan with the alligator on the front.
 *
 * @author		David Flanagan
 * @version             $Revision: 1.25 $
 */
public class HardcopyWriter extends Writer {

    protected PrintJob job;

    protected Graphics page;

    protected String jobname;

    protected String line;

    protected int fontsize;

    protected String time;

    protected Dimension pagesize = new Dimension(612, 792);

    protected int pagedpi = 72;

    protected Font font, headerfont;

    protected String fontName = "Monospaced";

    protected int fontStyle = Font.PLAIN;

    protected FontMetrics metrics;

    protected FontMetrics headermetrics;

    protected int x0, y0;

    protected int height, width;

    protected int headery;

    protected int charwidth;

    protected int lineheight;

    protected int lineascent;

    protected int chars_per_line;

    protected int lines_per_page;

    protected int charnum = 0, linenum = 0;

    protected int charoffset = 0;

    protected int pagenum = 0;

    protected Color color = Color.black;

    protected boolean isPreview;

    protected Graphics previewedPage;

    protected Image previewImage;

    protected Graphics previewImagegr;

    protected Vector<Image> pageImages = new Vector<Image>(3, 3);

    protected JmriJFrame previewFrame;

    protected JPanel previewPanel;

    protected ImageIcon previewIcon = new ImageIcon();

    protected JLabel previewLabel = new JLabel();

    protected JToolBar previewToolBar = new JToolBar();

    protected Frame frame;

    protected JButton nextButton;

    protected JButton previousButton;

    protected JButton closeButton;

    protected JLabel pageCount = new JLabel();

    protected JLabel totalPages = new JLabel();

    private boolean last_char_was_return = false;

    JobAttributes jobAttributes = new JobAttributes();

    PageAttributes pageAttributes = new PageAttributes();

    public HardcopyWriter(Frame frame, String jobname, int fontsize, double leftmargin, double rightmargin, double topmargin, double bottommargin, boolean preview) throws HardcopyWriter.PrintCanceledException {
        hardcopyWriter(frame, jobname, fontsize, leftmargin, rightmargin, topmargin, bottommargin, preview);
    }

    public HardcopyWriter(Frame frame, String jobname, int fontsize, double leftmargin, double rightmargin, double topmargin, double bottommargin, boolean preview, String printerName) throws HardcopyWriter.PrintCanceledException {
        jobAttributes.setPrinter(printerName);
        hardcopyWriter(frame, jobname, fontsize, leftmargin, rightmargin, topmargin, bottommargin, preview);
    }

    private void hardcopyWriter(Frame frame, String jobname, int fontsize, double leftmargin, double rightmargin, double topmargin, double bottommargin, boolean preview) throws HardcopyWriter.PrintCanceledException {
        isPreview = preview;
        this.frame = frame;
        pageAttributes.setColor(PageAttributes.ColorType.COLOR);
        if (!isPreview) {
            Toolkit toolkit = frame.getToolkit();
            job = toolkit.getPrintJob(frame, jobname, jobAttributes, pageAttributes);
            if (job == null) throw new PrintCanceledException("User cancelled print request");
            pagesize = job.getPageDimension();
            pagedpi = job.getPageResolution();
        }
        if (System.getProperty("os.name").regionMatches(true, 0, "windows", 0, 7)) {
        }
        x0 = (int) (leftmargin * pagedpi);
        y0 = (int) (topmargin * pagedpi);
        width = pagesize.width - (int) ((leftmargin + rightmargin) * pagedpi);
        height = pagesize.height - (int) ((topmargin + bottommargin) * pagedpi);
        font = new Font(fontName, fontStyle, fontsize);
        metrics = frame.getFontMetrics(font);
        lineheight = metrics.getHeight();
        lineascent = metrics.getAscent();
        charwidth = metrics.charWidth('m');
        chars_per_line = width / charwidth;
        lines_per_page = height / lineheight;
        headerfont = new Font("SansSerif", Font.ITALIC, fontsize);
        headermetrics = frame.getFontMetrics(headerfont);
        headery = y0 - (int) (0.125 * pagedpi) - headermetrics.getHeight() + headermetrics.getAscent();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        df.setTimeZone(TimeZone.getDefault());
        time = df.format(new Date());
        this.jobname = jobname;
        this.fontsize = fontsize;
        if (isPreview) {
            previewFrame = new JmriJFrame("Print Preview");
            previewFrame.getContentPane().setLayout(new BorderLayout());
            toolBarInit();
            previewToolBar.setFloatable(false);
            previewFrame.getContentPane().add(previewToolBar, BorderLayout.NORTH);
            previewPanel = new JPanel();
            previewPanel.setSize(pagesize.width, pagesize.height);
            previewFrame.getContentPane().add(new JScrollPane(previewPanel), BorderLayout.CENTER);
            previewFrame.setSize(660, Toolkit.getDefaultToolkit().getScreenSize().height);
            previewFrame.setVisible(true);
        }
    }

    /**
     * Creates a print preview toolbar 
     * added by Dennis Miller
     */
    protected void toolBarInit() {
        previousButton = new JButton("Previous Page");
        previewToolBar.add(previousButton);
        previousButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                pagenum--;
                displayPage();
            }
        });
        nextButton = new JButton("Next Page");
        previewToolBar.add(nextButton);
        nextButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                pagenum++;
                displayPage();
            }
        });
        previewToolBar.add(new JLabel("    Page "));
        previewToolBar.add(pageCount);
        previewToolBar.add(new JLabel(" of "));
        previewToolBar.add(totalPages);
        closeButton = new JButton(" Close ");
        previewToolBar.add(closeButton);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                if (page != null) page.dispose();
                previewFrame.dispose();
            }
        });
    }

    /**
     * Method to display a page image in the preview pane
     * Not in original class but added later by Dennis Miller
     */
    protected void displayPage() {
        if (pagenum > pageImages.size()) pagenum = pageImages.size();
        if (pagenum < 1) pagenum = 1;
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);
        if (pagenum == pageImages.size()) nextButton.setEnabled(false);
        if (pagenum == 1) previousButton.setEnabled(false);
        previewImage = pageImages.elementAt(pagenum - 1);
        previewFrame.setVisible(false);
        previewIcon.setImage(previewImage);
        previewLabel.setIcon(previewIcon);
        previewPanel.add(previewLabel);
        pageCount.setText("" + pagenum);
        totalPages.setText("" + pageImages.size() + "     ");
        previewFrame.invalidate();
        previewFrame.validate();
        previewFrame.setVisible(true);
    }

    /**
     * write method, implemented by all Write subclasses
     */
    public void write(char[] buffer, int index, int len) {
        synchronized (this.lock) {
            line = "";
            for (int i = index; i < index + len; i++) {
                if (page == null) newpage();
                if (buffer[i] == '\n') {
                    if (!last_char_was_return) {
                        newline();
                    }
                    continue;
                }
                if (buffer[i] == '\r') {
                    newline();
                    last_char_was_return = true;
                    continue;
                } else last_char_was_return = false;
                if (Character.isWhitespace(buffer[i]) && !Character.isSpaceChar(buffer[i]) && (buffer[i] != '\t')) continue;
                if (charoffset >= width) {
                    newline();
                    if (page == null) newpage();
                }
                if (buffer[i] == '\t') {
                    int tab = 8 - (charnum % 8);
                    charnum += tab;
                    charoffset = charnum * metrics.charWidth('m');
                    for (int t = 0; t < tab; t++) {
                        line += " ";
                    }
                } else {
                    line += buffer[i];
                    charnum++;
                    charoffset += metrics.charWidth(buffer[i]);
                }
            }
            page.drawString(line, x0, y0 + (linenum * lineheight) + lineascent);
        }
    }

    /**
     * Write the String with the desired color.  Returns the text color
     * back to the default after the string is written. 
     * @param c the color desired for this String
     * @param s the String
     * @throws IOException
     */
    public void write(Color c, String s) throws IOException {
        if (page != null) {
            page.setColor(c);
            write(s);
            page.setColor(color);
        }
    }

    public void flush() {
    }

    /** method modified by Dennis Miller to add preview capability */
    public void close() {
        synchronized (this.lock) {
            if (isPreview) {
                pageImages.addElement(previewImage);
                pagenum = 1;
                displayPage();
            }
            if (page != null) page.dispose();
            if (job != null) job.end();
        }
    }

    /** Dispose added so that a preview can be canceled */
    public void dispose() {
        synchronized (this.lock) {
            if (page != null) page.dispose();
            previewFrame.dispose();
            if (job != null) job.end();
        }
    }

    public void setFontStyle(int style) {
        synchronized (this.lock) {
            Font current = font;
            try {
                font = new Font(fontName, style, fontsize);
                fontStyle = style;
            } catch (Exception e) {
                font = current;
            }
            if (page != null) page.setFont(font);
        }
    }

    public int getLineHeight() {
        return this.lineheight;
    }

    public int getFontSize() {
        return this.fontsize;
    }

    public int getCharWidth() {
        return this.charwidth;
    }

    public int getLineAscent() {
        return this.lineascent;
    }

    public void setFontName(String name) {
        synchronized (this.lock) {
            Font current = font;
            try {
                font = new Font(name, fontStyle, fontsize);
                fontName = name;
                metrics = frame.getFontMetrics(font);
                lineheight = metrics.getHeight();
                lineascent = metrics.getAscent();
                charwidth = metrics.charWidth('m');
                chars_per_line = width / charwidth;
                lines_per_page = height / lineheight;
            } catch (Exception e) {
                font = current;
            }
            if (page != null) page.setFont(font);
        }
    }

    /**
     * sets the default text color 
     * @param c the new default text color
     */
    public void setTextColor(Color c) {
        color = c;
    }

    /** End the current page. Subsequent output will be on a new page */
    public void pageBreak() {
        synchronized (this.lock) {
            if (isPreview) pageImages.addElement(previewImage);
            page.dispose();
            page = null;
            newpage();
        }
    }

    /** Return the number of columns of characters that fit on a page */
    public int getCharactersPerLine() {
        return this.chars_per_line;
    }

    /** Return the number of lines that fit on a page */
    public int getLinesPerPage() {
        return this.lines_per_page;
    }

    /** Internal method begins a new line
     *  method modified by Dennis Miller to add preview capability
     */
    protected void newline() {
        page.drawString(line, x0, y0 + (linenum * lineheight) + lineascent);
        line = "";
        charnum = 0;
        charoffset = 0;
        linenum++;
        if (linenum >= lines_per_page) {
            if (isPreview) pageImages.addElement(previewImage);
            page.dispose();
            page = null;
            newpage();
        }
    }

    /** Internal method beings a new page and prints the header
     *  method modified by Dennis Miller to add preview capability
     */
    protected void newpage() {
        if (page == null) {
            if (!isPreview) {
                page = job.getGraphics();
            } else {
                previewImage = previewPanel.createImage(pagesize.width, pagesize.height);
                page = previewImage.getGraphics();
                page.setColor(Color.white);
                page.fillRect(0, 0, previewImage.getWidth(previewPanel), previewImage.getHeight(previewPanel));
                page.setColor(color);
            }
        }
        pagenum++;
        linenum = 0;
        charnum = 0;
        page.setFont(headerfont);
        page.drawString(jobname, x0, headery);
        String s = "- " + pagenum + " -";
        int w = headermetrics.stringWidth(s);
        page.drawString(s, x0 + (this.width - w) / 2, headery);
        w = headermetrics.stringWidth(time);
        page.drawString(time, x0 + width - w, headery);
        int y = headery + headermetrics.getDescent() + 1;
        page.drawLine(x0, y, x0 + width, y);
        page.setFont(font);
    }

    /**
     * Write a graphic to the printout.
     * <P>This was not in the
     * original class, but was added afterwards by Bob Jacobsen. Modified by D Miller.
     * <P> The image is positioned on the right side of the paper,
     * at the current height.
     */
    public void write(Image c, Component i) {
        if (page == null) newpage();
        int x = x0 + width - (c.getWidth(null) * 2 / 3 + charwidth);
        int y = y0 + (linenum * lineheight) + lineascent;
        page.drawImage(c, x, y, c.getWidth(null) * 2 / 3, c.getHeight(null) * 2 / 3, null);
    }

    /**
     * Write a graphic to the printout.
     * <P>This was not in the
     * original class, but was added afterwards by Kevin Dickerson.
     * it is a copy of the write, but without the scaling.
     * <P> The image is positioned on the right side of the paper,
     * at the current height.
     */
    public void writeNoScale(Image c, Component i) {
        if (page == null) newpage();
        int x = x0 + width - (c.getWidth(null) + charwidth);
        int y = y0 + (linenum * lineheight) + lineascent;
        page.drawImage(c, x, y, c.getWidth(null), c.getHeight(null), null);
    }

    /** A Method to allow a JWindow to print itself at the current line position
    * <P>This was not in the
    * original class, but was added afterwards by Dennis Miller.
    * <P>Intended to allow for a graphic printout of the speed table, but can be
    * used to print any window.  The JWindow is passed to the method and prints itself at the current
    * line and aligned at the left margin.  The calling method should check for sufficient
    * space left on the page and move it to the top of the next page if there
    * isn't enough space.
    */
    public void write(JWindow jW) {
        if (page == null) newpage();
        int x = x0;
        int y = y0 + (linenum * lineheight);
        page.translate(x, y);
        jW.setVisible(true);
        jW.printAll(page);
        jW.setVisible(false);
        jW.dispose();
        page.translate(-x, -y);
    }

    /**
     * Draw a line on the printout.
     * <P>This was not in the
     * original class, but was added afterwards by Dennis Miller.
     * <P>colStart and colEnd represent the horizontal character positions.  The
     * lines actually start in the middle of the character position to make it easy to
     * draw vertical lines and space them between printed characters.
     * <P>rowStart and rowEnd represent the vertical character positions.  Horizontal
     * lines are drawn underneath the row (line) number.  They are offset so they
     * appear evenly spaced, although they don't take into account any space needed
     * for descenders, so they look best with all caps text
     */
    public void write(int rowStart, int colStart, int rowEnd, int colEnd) {
        if (page == null) newpage();
        int xStart = x0 + (colStart - 1) * charwidth + charwidth / 2;
        int xEnd = x0 + (colEnd - 1) * charwidth + charwidth / 2;
        int yStart = y0 + rowStart * lineheight + (lineheight - lineascent) / 2;
        int yEnd = y0 + rowEnd * lineheight + (lineheight - lineascent) / 2;
        page.drawLine(xStart, yStart, xEnd, yEnd);
    }

    /**
     * Get the current linenumber.
     * <P>This was not in the
     * original class, but was added afterwards by Dennis Miller.
     */
    public int getCurrentLineNumber() {
        return this.linenum;
    }

    /**
     * Print vertical borders on the current line at the left and right sides
     * of the page at character positions 0 and chars_per_line + 1.
     * Border lines are one text line in height
     * <P>This was not in the
     * original class, but was added afterwards by Dennis Miller.
     */
    public void writeBorders() {
        write(this.linenum, 0, this.linenum + 1, 0);
        write(this.linenum, this.chars_per_line + 1, this.linenum + 1, this.chars_per_line + 1);
    }

    /**
     * Increase line spacing by a percentage 
	 * <P>
	 * This method should be invoked immediately after a new HardcopyWriter is created.
	 * <P>
	 * This method was added to improve appearance when printing tables
     * <P>
	 * This was not in the original class, added afterwards by DaveDuchamp.
     */
    public void increaseLineSpacing(int percent) {
        int delta = (lineheight * percent) / 100;
        lineheight = lineheight + delta;
        lineascent = lineascent + delta;
        lines_per_page = height / lineheight;
    }

    public static class PrintCanceledException extends Exception {

        public PrintCanceledException(String msg) {
            super(msg);
        }
    }
}
