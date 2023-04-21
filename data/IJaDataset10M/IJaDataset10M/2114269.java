package com.whstudio.util.print;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;

/**
 * ������ſͻ��˴�ӡ�ؼ�
 * ��ݲ���ϵͳ�û���ѡ���Ӧ�Ĵ�ӡ����д�ӡ
 * �ն��û���yy0112 ��ӡ����ƣ�Client\YY0112#\Oki 5560SC1
 * ��ӡ����Ҳ��裬��ʼ��ʱ�г����п��ô�ӡ������һ��List��
 * ���ô�ӡ�����ʱ��ȡ���û���ת���ɴ�д����List�в��Ұ���û���Ĵ�ӡ����
 * �����ҵ��ĵ�һ����ӡ��������õ���ӡ������
 * 
 * @author wbwanggp
 * @version 1.0
 * 
 */
public class NeusoftPrint extends Applet implements Printable {

    private static final long serialVersionUID = -6999351213943537157L;

    private int PAGES = 10;

    private String printStr = "";

    private List printers = new ArrayList();

    /**
	 * 
	 */
    public void destroy() {
    }

    /**
	 * 
	 */
    public String getAppletInfo() {
        return "������Ŵ�ӡ�ؼ�";
    }

    /**
	 * 
	 */
    public void init() {
        super.init();
    }

    public void update(Graphics g) {
        paint(g);
    }

    /**
	 * 
	 */
    public void start() {
        printTextAction();
    }

    public void stop() {
    }

    public NeusoftPrint() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for (int i = 0; i < services.length; i++) {
            String printerName = services[i].getName();
            printers.add(printerName);
        }
    }

    private String getPrinterName(String osUserName) {
        for (Iterator iterator = printers.iterator(); iterator.hasNext(); ) {
            String printer = (String) iterator.next();
            if (printer.contains(osUserName.toUpperCase())) {
                return printer;
            }
        }
        return null;
    }

    public int print2(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setPaint(Color.black);
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        Font font = new Font("����", Font.BOLD, 10);
        g2.setFont(font);
        g2.drawString("��ӡ����", 10, 10);
        font = new Font("����", Font.ITALIC, 18);
        g2.setFont(font);
        g2.drawString("�ڶ���:", 10, 40);
        return Printable.PAGE_EXISTS;
    }

    public void print() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        Book book = new Book();
        book.append(new NeusoftPrint(), printerJob.defaultPage());
        printerJob.setPageable(book);
        HashAttributeSet hs = new HashAttributeSet();
        String osUserName = System.getProperty("user.name");
        String printerName = getPrinterName(osUserName);
        hs.add(new PrinterName(printerName, null));
        PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
        if (pss.length == 0) {
            System.out.println("�޷��ҵ���ӡ��:" + printerName);
            return;
        }
        try {
            printerJob.setPrintService(pss[0]);
            printerJob.print();
        } catch (PrinterException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.black);
        if (page >= PAGES) return Printable.NO_SUCH_PAGE;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        drawCurrentPageText(g2, pf, page);
        return Printable.PAGE_EXISTS;
    }

    private void drawCurrentPageText(Graphics2D g2, PageFormat pf, int page) {
        String s = getDrawText(printStr)[page];
        Font f = new Font("����", 12, 10);
        String drawText;
        float ascent = 16;
        int k, i = f.getSize(), lines = 0;
        while (s.length() > 0 && lines < 54) {
            k = s.indexOf("\n");
            if (k != -1) {
                lines += 1;
                drawText = s.substring(0, k);
                g2.drawString(drawText, 0, ascent);
                if (s.substring(k + 1).length() > 0) {
                    s = s.substring(k + 1);
                    ascent += i;
                }
            } else {
                lines += 1;
                drawText = s;
                g2.drawString(drawText, 0, ascent);
                s = "";
            }
        }
    }

    public String[] getDrawText(String s) {
        String[] drawText = new String[PAGES];
        for (int i = 0; i < PAGES; i++) drawText[i] = "";
        int k, suffix = 0, lines = 0;
        while (s.length() > 0) {
            if (lines < 54) {
                k = s.indexOf("\n");
                if (k != -1) {
                    lines += 1;
                    drawText[suffix] = drawText[suffix] + s.substring(0, k + 1);
                    if (s.substring(k + 1).length() > 0) s = s.substring(k + 1);
                } else {
                    lines += 1;
                    drawText[suffix] = drawText[suffix] + s;
                    s = "";
                }
            } else {
                lines = 0;
                suffix++;
            }
        }
        return drawText;
    }

    public int getPagesCount(String curStr) {
        int page = 0;
        int position, count = 0;
        String str = curStr;
        while (str.length() > 0) {
            position = str.indexOf("\n");
            count += 1;
            if (position != -1) str = str.substring(position + 1); else str = "";
        }
        if (count > 0) page = count / 54 + 1;
        return page;
    }

    private void printTextAction() {
        printStr = "Ҫ��ӡ��Ŀ���ı�";
        if (printStr != null && printStr.length() > 0) {
            PAGES = getPagesCount(printStr);
            PrinterJob myPrtJob = PrinterJob.getPrinterJob();
            PageFormat pageFormat = myPrtJob.defaultPage();
            myPrtJob.setPrintable(this, pageFormat);
            try {
                myPrtJob.print();
            } catch (PrinterException pe) {
                pe.printStackTrace();
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Sorry, Printer Job is Empty, Print Cancelled!", "Empty", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }
}
