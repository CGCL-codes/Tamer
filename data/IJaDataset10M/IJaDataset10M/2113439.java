package org.ttalbott.mytelly;

import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author  Tom Talbott
 * @version 
 */
public class PrintListing extends HTMLEditorKit.ParserCallback {

    private Programs m_programs;

    private org.ttalbott.mytelly.Config m_config;

    private boolean m_bold = false;

    private boolean m_italic = false;

    private boolean m_largeFont = false;

    private Font m_normalFont = new Font("serif", Font.PLAIN, 10);

    private Font m_boldFont = new Font("serif", Font.BOLD, 10);

    private Font m_boldItalicFont = new Font("serif", Font.BOLD | Font.ITALIC, 10);

    private Font m_italicFont = new Font("serif", Font.BOLD | Font.ITALIC, 10);

    private Font m_largeBoldFont = new Font("serif", Font.BOLD, 12);

    private Font m_headerFont = new Font("SansSerif", Font.BOLD, 16);

    private Font m_footerFont = m_normalFont;

    private HTMLEditorKit.Parser m_parser = null;

    private int m_columnWidth = 0;

    private Graphics m_gBuffer = null;

    private int m_normFontHeight = 0;

    private int m_normFontAscent = 0;

    private int m_horizPos = 0;

    private int m_vertPos = 0;

    private boolean m_measureOnly = true;

    private int m_indent = 0;

    private int m_indentsize = 0;

    private boolean m_blockquote = false;

    /** Creates new PrintListing */
    public PrintListing(Programs programs, Config config) {
        m_programs = programs;
        m_config = config;
        ParserGetter kit = new ParserGetter();
        m_parser = kit.getParser();
    }

    public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
    }

    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
        String sTag = tag.toString();
        if (sTag.equalsIgnoreCase("b")) {
            m_bold = true;
        } else if (sTag.equalsIgnoreCase("i")) {
            m_italic = true;
        } else if (sTag.equalsIgnoreCase("p")) {
        } else if (sTag.equalsIgnoreCase("blockquote")) {
            m_indent += m_indentsize;
            m_blockquote = true;
        } else if (sTag.equalsIgnoreCase("font")) {
            String size = attributes.getAttribute(HTML.Attribute.SIZE).toString();
            if (size.compareTo("3") >= 0) {
                m_largeFont = true;
            } else m_largeFont = false;
        }
    }

    public void handleText(char[] text, int position) {
        if (m_gBuffer == null) {
            System.out.println("PrintListing.handleText - no m_gBuffer allocated");
            return;
        }
        if (m_largeFont && m_bold) {
            m_gBuffer.setFont(m_largeBoldFont);
        } else if (m_bold && m_italic) {
            m_gBuffer.setFont(m_boldItalicFont);
        } else if (m_bold) {
            m_gBuffer.setFont(m_boldFont);
        } else if (m_italic) {
            m_gBuffer.setFont(m_italicFont);
        } else {
            m_gBuffer.setFont(m_normalFont);
        }
        int lineheight = (m_blockquote ? m_normFontHeight * 4 / 5 : m_normFontHeight);
        FontMetrics fm = m_gBuffer.getFontMetrics();
        StringTokenizer tokenizer = new StringTokenizer(String.valueOf(text), " ", true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (m_horizPos == 0 && token.trim().length() == 0) continue;
            int tokenWidth = fm.stringWidth(token);
            if (m_horizPos != 0 && m_horizPos + tokenWidth + m_indent > m_columnWidth) {
                m_vertPos += lineheight;
                m_horizPos = 0;
            }
            if (m_horizPos + tokenWidth + m_indent > m_columnWidth) {
                String subtoken = token;
                while (subtoken.length() > 0) {
                    int length = subtoken.length();
                    for (int i = length; i >= 1; i--) {
                        java.awt.geom.Rectangle2D rect2D = fm.getStringBounds(subtoken, 0, i, m_gBuffer);
                        int strWidth = rect2D.getBounds().width;
                        if (strWidth < m_columnWidth) {
                            if (!m_measureOnly) m_gBuffer.drawString(subtoken.substring(0, i), m_horizPos + m_indent, m_vertPos);
                            if (i == length) {
                                subtoken = "";
                                m_horizPos += strWidth;
                            } else {
                                subtoken = subtoken.substring(i + 1);
                                m_horizPos = 0;
                                m_vertPos += lineheight;
                            }
                            break;
                        }
                    }
                }
            } else {
                if (!m_measureOnly) m_gBuffer.drawString(token, m_horizPos + m_indent, m_vertPos);
                m_horizPos += tokenWidth;
            }
        }
    }

    public void handleEndTag(HTML.Tag tag, int position) {
        String sTag = tag.toString();
        if (sTag.equalsIgnoreCase("b")) {
            m_bold = false;
        } else if (sTag.equalsIgnoreCase("i")) {
            m_italic = false;
        } else if (sTag.equalsIgnoreCase("p")) {
            m_vertPos += m_normFontHeight;
            m_horizPos = 0;
        } else if (sTag.equalsIgnoreCase("blockquote")) {
            m_indent -= m_indentsize;
            m_blockquote = false;
        } else if (sTag.equalsIgnoreCase("font")) {
            m_largeFont = false;
        }
    }

    public void print(Frame frame, ProgramList progs, String heading, String footer) {
        JobAttributes ja = new JobAttributes();
        PageAttributes pa = new PageAttributes();
        PrintJob pj = Toolkit.getDefaultToolkit().getPrintJob(frame, "My Telly Schedule", ja, pa);
        if (pj != null) {
            WaitCursor wait = new WaitCursor(frame);
            try {
                int screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
                int topMargin = screenResolution * 2 / 3;
                int botMargin = screenResolution / 2;
                int leftMargin = screenResolution / 4;
                Dimension pageSize = new Dimension(pj.getPageDimension());
                int truePageBottom = pageSize.height;
                pageSize.width -= 2 * leftMargin;
                pageSize.height -= topMargin + botMargin;
                Graphics g = pj.getGraphics();
                if (g == null) {
                    System.out.println("Error getting printer");
                    return;
                }
                FontMetrics normalFM = g.getFontMetrics(m_normalFont);
                int numCols = 4;
                int colGap = 2 * normalFM.stringWidth(" ");
                m_indentsize = 4 * normalFM.stringWidth(" ");
                m_columnWidth = (pageSize.width / numCols) - (colGap);
                int curPage = 0;
                int curCol = 1;
                int curVertPos = 0;
                StringBuffer buffer = new StringBuffer();
                String curDate = "###";
                String curTime = "###";
                if (progs == null) return;
                progs.sortAndRemoveDups();
                Iterator it = progs.iterator();
                ProgItem elProgram;
                while (it.hasNext()) {
                    elProgram = (ProgItem) it.next();
                    if (elProgram != null) {
                        String start = Programs.getInstance().getStartTime(elProgram);
                        Calendar startTime = Utilities.makeCal(start);
                        StringBuffer sbDate = new StringBuffer();
                        sbDate.append("<b><font size=\"3\"><p>");
                        sbDate.append(Programs.formatDay(startTime));
                        sbDate.append("</p></font></b>");
                        sbDate.append("\r\n\r\n");
                        boolean dateAdded = false;
                        if (start.indexOf(curDate) != 0) {
                            curDate = start.substring(0, 8);
                            buffer.append(sbDate.toString());
                            dateAdded = true;
                        }
                        StringBuffer sbTime = new StringBuffer();
                        sbTime.append("<p><font size=\"3\">");
                        sbTime.append(Programs.formatTime(startTime));
                        sbTime.append("</font></p>");
                        sbTime.append("\r\n");
                        boolean timeAdded = false;
                        if (!start.equals(curTime)) {
                            curTime = start;
                            buffer.append(sbTime.toString());
                            timeAdded = true;
                        }
                        m_programs.formatProg(elProgram, startTime, buffer, true);
                        buffer.append("\r\n");
                        initNewPrintBlock(g, true);
                        StringReader r = new StringReader(buffer.toString());
                        try {
                            m_parser.parse(r, this, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (curVertPos == 0 || (curCol == numCols && curVertPos + m_vertPos + m_normFontHeight > pageSize.height)) {
                            if (curVertPos != 0) {
                                g.dispose();
                                g = pj.getGraphics();
                            }
                            curCol = 1;
                            curVertPos = 0;
                            curPage++;
                            int headerTop = screenResolution / 4;
                            g.setFont(m_headerFont);
                            FontMetrics headerFM = g.getFontMetrics();
                            int headerY = headerTop + headerFM.getAscent();
                            int headerX = leftMargin + pageSize.width / 2 - headerFM.stringWidth(heading) / 2;
                            g.drawString(heading, headerX, headerY);
                            g.setFont(m_footerFont);
                            FontMetrics footerFM = g.getFontMetrics();
                            int footerY = pageSize.height + (topMargin + botMargin) - (screenResolution / 2) + (footerFM.getHeight() - footerFM.getAscent());
                            int footerX = leftMargin;
                            g.drawString(footer, footerX, footerY);
                            String sPage = "Page: " + curPage;
                            footerX = leftMargin + pageSize.width - footerFM.stringWidth(sPage);
                            g.drawString(sPage, footerX, footerY);
                            g.translate(leftMargin, topMargin);
                            g.clipRect(0, 0, pageSize.width, pageSize.height);
                            if (!timeAdded) buffer.insert(0, sbTime.toString());
                            if (!dateAdded) buffer.insert(0, sbDate.toString());
                        }
                        if (curVertPos + m_vertPos + m_normFontHeight > pageSize.height) {
                            curCol++;
                            curVertPos = 0;
                            if (!timeAdded) buffer.insert(0, sbTime.toString());
                            if (!dateAdded) buffer.insert(0, sbDate.toString());
                        }
                        Graphics blockGraphics = g.create((curCol - 1) * (m_columnWidth + colGap), curVertPos, m_columnWidth, pageSize.height - curVertPos);
                        initNewPrintBlock(blockGraphics, false);
                        r = new StringReader(buffer.toString());
                        try {
                            m_parser.parse(r, this, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        curVertPos += m_vertPos;
                        buffer.delete(0, buffer.length());
                    }
                }
                if (g != null) {
                    g.dispose();
                }
            } finally {
                pj.end();
                wait.done();
            }
        }
    }

    private void initNewPrintBlock(Graphics newG, boolean measureOnly) {
        m_measureOnly = measureOnly;
        m_gBuffer = newG;
        FontMetrics normalFM = m_gBuffer.getFontMetrics(m_normalFont);
        m_normFontHeight = normalFM.getHeight();
        m_normFontAscent = normalFM.getAscent();
        m_horizPos = 0;
        m_vertPos = m_normFontAscent;
    }
}
