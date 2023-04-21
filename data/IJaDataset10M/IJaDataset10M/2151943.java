package pkHtml;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Locale;

/**
 * INPUT: a preprocessed with a jEdit macro FolioViews FFF file.<br/>
 * OUTPUT: an SFI-file.
 *
 * @modified 2010.08.23
 * @since 2010.08.23 (v00.02.03)
 * @author HoKoNoUmo
 */
public class HtmlMapFolioViews {

    private BufferedWriter wrBuffered;

    private String sLinePrev = "";

    private String sFileIn = "";

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("USAGE:");
            System.out.println("java pkHtml.HtmlMapFolioViews <g:/file1/.../folio.html>");
        } else new HtmlMapFolioViews(args[0]);
    }

    /**
	 * Constructor.
	 *
	 * @param sFileIn
	 * 		The string of the file-path with the folio-views preprocessed-data.
	 *
	 * @modified 2010.08.23
	 * @since 2010.08.23 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    public HtmlMapFolioViews(String sFileIn) {
        this.sFileIn = sFileIn;
        BufferedReader rdIn;
        String sFileOut = sFileIn + ".FvOut";
        try {
            FileInputStream fis = new FileInputStream(sFileIn);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            rdIn = new BufferedReader(isr);
            FileOutputStream fos = new FileOutputStream(sFileOut);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
            wrBuffered = new BufferedWriter(osw);
            writeStart();
            String sLn = null;
            while ((sLn = rdIn.readLine()) != null) {
                if (sLn.startsWith("<h0>")) {
                    writeLine("<p class=\"h0\">");
                    writeSFI("h0");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0");
                    writeLine("</p>");
                } else if (sLn.startsWith("<h1>")) {
                    writeLine("<h1>");
                    writeSFI("h0.1");
                    if (sLn.indexOf("hd1:") != -1) {
                        String sObj = sLn.substring(sLn.indexOf("#hd1:") + 5, sLn.lastIndexOf("#"));
                        String sN = sObj.substring(sObj.indexOf("-") + 1);
                        sLn = sLn.substring(4, sLn.indexOf("#hd1:"));
                        writeSFI("ifi" + sObj);
                        sLn.trim();
                        sLn = mapInline(sLn);
                        writeLine("  hSBC-" + sN + ":: " + sLn);
                    } else {
                        sLn = sLn.substring(4);
                        sLn.trim();
                        sLn = mapInline(sLn);
                        writeLine("  " + sLn);
                    }
                    writeHideElement("h0.1");
                    writeLine("</h1>");
                } else if (sLn.startsWith("<h2>")) {
                    writeLine("<h2>");
                    writeSFI("h0.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.2");
                    writeLine("</h2>");
                } else if (sLn.startsWith("<h3>")) {
                    writeLine("<h3>");
                    writeSFI("h0.1.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.1.1");
                    writeLine("</h3>");
                } else if (sLn.startsWith("<h4>")) {
                    writeLine("<h4>");
                    writeSFI("h0.1.1.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.1.1.1");
                    writeLine("</h4>");
                } else if (sLn.startsWith("<h5>")) {
                    writeLine("<h5>");
                    writeSFI("h0.1.1.1.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.1.1.1.1");
                    writeLine("</h5>");
                } else if (sLn.startsWith("<h6>")) {
                    writeLine("<h6>");
                    writeSFI("h0.1.1.1.1.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.1.1.1.1.1");
                    writeLine("</h6>");
                } else if (sLn.startsWith("<h7>")) {
                    writeLine("<p class=\"h7\">");
                    writeSFI("h0.1.1.1.1.1.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.1.1.1.1.1.1");
                    writeLine("</p>");
                } else if (sLn.startsWith("<h8>")) {
                    writeLine("<p class=\"h8\">");
                    writeSFI("h0.1.1.1.1.1.1.1.1");
                    sLn = sLn.substring(4);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1.1.1.1.1.1.1.1");
                    writeLine("</p>");
                } else if (sLn.startsWith("<p>")) {
                    writeLine("<p>");
                    writeSFI("h0.1p1");
                    sLn = sLn.substring(3);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1p1");
                    writeLine("</p>");
                } else if (sLn.startsWith("<p class=\"margin-25\">")) {
                    writeLine("<p class=\"margin-25\">");
                    writeSFI("h0.1p1");
                    sLn = sLn.substring(21);
                    sLn = mapInline(sLn);
                    writeLine("  " + sLn);
                    writeHideElement("h0.1p1");
                    writeLine("</p>");
                }
            }
            writeEnd();
            rdIn.close();
            wrBuffered.close();
        } catch (IOException ioe) {
            System.out.println(">>HtmlUpdateSFI.Constructor: " + ioe.toString());
        }
        File f1 = new File(sFileIn);
        f1.renameTo(new File(sFileIn + "2"));
        File f2 = new File(sFileOut);
        f2.renameTo(new File(sFileIn));
    }

    /**
	 * Maps the inline-elements from FolioViews to Html of a String.
	 *
	 * @modified 2010.08.31
	 * @since 2010.08.24 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    private String mapInline(String sL) {
        String sOut = "";
        sOut = sL.replaceAll("<br/>", "\n  <br/>");
        if (sL.indexOf("a name=") != -1) {
            if (sL.startsWith("<a name=")) {
                String s1 = sL.substring(0, sL.indexOf(">") + 1);
                String s2 = sL.substring(sL.indexOf(">") + 1);
                sOut = s1 + "</a>\n  " + s2;
            } else {
                String s1 = sL.substring(0, sL.indexOf("<a name="));
                int niEndOfA = sL.indexOf(">", sL.indexOf("<a name=") + 2) + 1;
                String sName = sL.substring(sL.indexOf("<a name="), niEndOfA);
                String s2 = sL.substring(niEndOfA);
                sOut = sName + "</a>\n  " + s1 + s2;
            }
        }
        return sOut;
    }

    /**
	 *
	 * @modified 2010.08.24
	 * @since 2010.08.24 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    private void writeEnd() {
        try {
            writeLine("<h1>");
            writeLine("  <a name=\"h0.toc\"></a>");
            writeLine("  Table of Contents");
            writeLine("  <a class=\"hide\">#h0.toc h1#</a>");
            writeLine("</h1>");
            writeLine("<p class=\"notoc\">");
            writeLine("  File-ToC:");
            writeLine("  <br/>&#8680; <a href=\"#h0.1\">About KnowledgeBase</a>");
            writeLine("  <a class=\"hide\">#h0.toc#</a>");
            writeLine("</p>");
            writeLine("<hr/>");
            writeLine("<p class=\"notoc-last\">");
            writeLine("  This is an <a href=\"http://htmlmgr.sourceforge.net/index.html#ifiSFI\">SFI-file</a>, best viewed with <a href=\"http://htmlmgr.sourceforge.net/\">HtmlMgr</a>.");
            writeLine("  <a class=\"hide\">#h0.toc#</a>");
            writeLine("</p>");
            writeLine("<p class=\"last\">");
            writeLine("  VERSIONS (of this file):");
            String sFN = HtmlMgr.getFileName(sFileIn);
            writeLine("  <br/>* CURRENT: " + sFN.substring(0, sFN.length() - 5) + "-" + HtmlUtilities.setCurrentDate() + ".html");
            writeLine("  <br/>* PUBLISHED: ");
            writeLine("  <br/>* CREATED: " + sFN.substring(0, sFN.length() - 5) + "-" + HtmlUtilities.setCurrentDate() + ".html");
            writeLine("  <br/>* MAIL: ");
            writeLine("  <a class=\"hide\">#h0.toc#</a>");
            writeLine("</p>");
            writeLine("</body>");
            wrBuffered.write("</html>");
        } catch (IOException e) {
            System.out.println("!!!ex-HtmlMapFolioViews.writeEnd:: " + e.toString());
        }
    }

    /**
	 *
	 * @modified 2010.08.24
	 * @since 2010.08.24 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    private void writeHideElement(String sSFI) {
        sLinePrev = "  <a class=\"hide\">#" + sSFI + "#</a>";
        try {
            wrBuffered.write(sLinePrev);
            wrBuffered.newLine();
        } catch (IOException e) {
            System.out.println("!!!ex-HtmlMapFolioViews.writeHideElement:: " + e.toString());
        }
    }

    /**
	 *
	 * @modified 2010.08.24
	 * @since 2010.08.24 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    private void writeLine(String sLn) {
        sLinePrev = sLn;
        try {
            wrBuffered.write(sLinePrev);
            wrBuffered.newLine();
        } catch (IOException e) {
            System.out.println("ex-HtmlMapFolioViews.writeLine:: " + e.toString());
        }
    }

    /**
	 *
	 * @modified 2010.08.24
	 * @since 2010.08.24 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    private void writeSFI(String sSFI) {
        sLinePrev = "  <a name=\"" + sSFI + "\"></a>";
        try {
            wrBuffered.write(sLinePrev);
            wrBuffered.newLine();
        } catch (IOException e) {
            System.out.println("!!!ex-HtmlMapFolioViews.writeSFI:: " + e.toString());
        }
    }

    /**
	 * Writes on writer the all the lines of the file before
	 * the heading-0 element.
	 *
	 * @modified 2010.08.24
	 * @since 2010.08.24 (v00.02.03)
	 * @author HoKoNoUmo
	 */
    private void writeStart() {
        writeLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writeLine("<!DOCTYPE html");
        writeLine("  PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
        writeLine("  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        writeLine("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">");
        writeLine("<head>");
        writeLine("  <title>aaa.html</title>");
        writeLine("  <link href=\"../css-HoKoNoUmo.css\" rel=\"stylesheet\" type=\"text/css\"/>");
        writeLine("</head>");
        writeLine("<body>");
    }
}
