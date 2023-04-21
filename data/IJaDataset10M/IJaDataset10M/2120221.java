package be.djdb.preparts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import be.djdb.Statics;
import be.djdb.generators.Toolbars;
import be.djdb.utils.SwingConstantsEnum;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;
import com.sun.pdfview.PagePanel;

/**
* @author Lieven Roegiers
* @copyright 2011
* @from JAVA_mylibs
*/
public class PDFviewer extends JInternalFrame {

    private static final long serialVersionUID = 2L;

    private static final boolean DEBUG = false;

    private PDFFile pdffile;

    private PagePanel panel = new PagePanel();

    private int numPages;

    private ActionListener printpdf = null, pdffirstpage = null;

    private ActionListener pdfPreviouspage = null;

    private ActionListener pdfNextpage = null;

    private ActionListener pdflastpage = null;

    private int pdfactualpageNr = 1;

    private JTextField pagenr;

    private int thiswidth;

    private int thisheight;

    private int minwidth = 450;

    private int minheight = 600;

    private int toolheight = 50;

    private Toolbars toolbars = new Toolbars("libs");

    private JScrollPane scrolpanel;

    private JSplitPane jSplitPane1;

    public PDFviewer(PDFFile pdffile) {
        setLayout(new BorderLayout());
        log(Level.WARNING, "=>initalise");
        if (pdffile != null) {
            this.pdffile = pdffile;
            numPages = this.pdffile.getNumPages();
            setmenu(false);
            setgui(false);
            getpage(pdfactualpageNr);
        } else {
            log(Level.WARNING, "ERROR:pdffile=null posible reset setPdffile,resetgui");
            scrolpanel = new JScrollPane();
            scrolpanel.setName("ERROR");
            add(scrolpanel, BorderLayout.CENTER);
        }
        setSize(50, 50);
        setTitle("Pdfvieuw");
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setResizable(true);
    }

    private void setmenu(boolean b) {
        setactions();
        pagenr = new JTextField();
        toolbars.addtoolbaritem("utilTools", "zoom", new JButton(), "img/zoom.gif");
        toolbars.addtoolbaritem("utilTools", "fit", new JButton(), "img/fit.gif");
        toolbars.addtoolbaritem("utilTools", "open", new JButton(), "img/open.gif");
        toolbars.addtoolbaritem("Tools", "pdf.btnPrint", new JButton(), "img/print.gif", printpdf);
        toolbars.addtoolbaritem("Tools", "pdf.btnFirstPage", new JButton(), "img/first.gif", pdffirstpage);
        toolbars.addtoolbaritem("Tools", "pdf.btnPrevious", new JButton(), "img/prev.gif", pdfPreviouspage);
        toolbars.addtoolbaritem("Tools", "pdf.txttoestand", pagenr, SwingConstantsEnum.CENTER);
        toolbars.addtoolbaritem("Tools", "pdf.txttoestand", pagenr, SwingConstantsEnum.CENTER);
        toolbars.addtoolbaritem("Tools", "pdf.btnNext", new JButton(), "img/next.gif", pdfNextpage);
        toolbars.addtoolbaritem("Tools", "pdf.btnLastPage", new JButton(), "img/last.gif", pdflastpage);
    }

    public void resetgui(boolean enabled) {
        if (pdffile != null) {
            numPages = this.pdffile.getNumPages();
            setmenu(enabled);
            setgui(enabled);
            getpage(pdfactualpageNr);
        } else {
            log(Level.WARNING, "ERROR:pdffile=null posible reset setPdffile,resetgui");
        }
    }

    private void setgui(boolean enabled) {
        add(toolbars.getatoolbar("utilTools"), BorderLayout.NORTH, 0);
        add(toolbars.getatoolbar("Tools"), BorderLayout.NORTH, 1);
        scrolpanel = new JScrollPane();
        scrolpanel.setPreferredSize(new java.awt.Dimension(minwidth, minheight));
        add(scrolpanel, BorderLayout.CENTER);
        panel = new PagePanel();
        panel.setSize(1, 1);
        panel.setPreferredSize(new java.awt.Dimension(minwidth - 10, minheight));
    }

    public void setPdffile(PDFFile pdffile) {
        this.pdffile = pdffile;
    }

    public void getpage(int i) {
        pagenr.setText("" + i + "/" + numPages);
        if (DEBUG) {
            log(Level.WARNING, "toon pagenr" + i + "of" + numPages);
        }
        ;
        panel.showPage(this.pdffile.getPage(i));
        scrolpanel.setViewportView(panel);
        scrolpanel.repaint();
        repaint();
    }

    public void getpage() {
        getpage(pdfactualpageNr);
    }

    private void setactions() {
        printpdf = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (DEBUG) {
                    log(Level.WARNING, "printpdf action, event=" + evt);
                }
                ;
                if (DEBUG) {
                    log(Level.WARNING, "isPrintable " + pdffile.isPrintable());
                }
                ;
                PDFPrintPage pages = new PDFPrintPage(pdffile);
                PrinterJob pjob = PrinterJob.getPrinterJob();
                PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
                pjob.setJobName("Statement");
                Book book = new Book();
                book.append(pages, pf, pdffile.getNumPages());
                pjob.setPageable(book);
                try {
                    if (pjob.printDialog()) {
                        pjob.print();
                    }
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }

            ;
        };
        pdffirstpage = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (DEBUG) {
                    log(Level.WARNING, "pdffirstpage action, event=" + evt);
                }
                pdfactualpageNr = 1;
                getpage();
            }
        };
        pdfPreviouspage = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (DEBUG) {
                    log(Level.WARNING, "pdfPreviouspage action, event=" + evt);
                }
                if (pdfactualpageNr > 1) {
                    pdfactualpageNr--;
                }
                getpage();
            }
        };
        pdfNextpage = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (DEBUG) {
                    log(Level.WARNING, "pdfNextpage action, event=" + evt);
                }
                if (pdfactualpageNr < numPages) {
                    pdfactualpageNr++;
                }
                getpage();
            }
        };
        pdflastpage = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (DEBUG) {
                    log(Level.WARNING, "pdflastpage.action, event=" + evt);
                }
                pdfactualpageNr = numPages;
                getpage();
            }
        };
    }

    private static void log(Level level, String msg) {
        String tag = "<>>>>*" + Statics.LIBNAME + "-" + Statics.COPYRIGHTBY + "*<<<<>";
        Logger.getLogger(PDFviewer.class.getName()).log(level, tag + msg);
    }

    public void setBounds(int top, int left, int with, int height) {
        thisheight = (height > minheight + toolheight) ? height : minheight + toolheight;
        thiswidth = (with > minwidth) ? with : minwidth;
        super.setBounds(top, left, thiswidth, thisheight);
        panel.setBounds(50, 5, thiswidth - 10, thisheight - 50);
    }

    public Dimension resizeto(Dimension d) {
        Rectangle cord = getBounds();
        cord.width = d.width;
        cord.height = d.height;
        this.setBounds(cord);
        return new Dimension(thiswidth, thisheight);
    }
}
