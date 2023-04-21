package com.eastmountain.chinese.printing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import com.eastmountain.chinese.chinesepod.common.ChineseElementProperties;
import com.eastmountain.chinese.chinesepod.exceptions.DBAccessException;
import com.eastmountain.chinese.gui.HTMLListPane;
import com.eastmountain.chinese.gui.controllers.HTMLMenuBar;
import com.eastmountain.chinese.observable.HZFlashcardsDataModel;

public class PrintingManager implements Printable {

    private HZFlashcardsDataModel hzFlashcardsDataModel = null;

    private HTMLMenuBar htmlMenuBar = null;

    private HTMLListPane htmlListPane = null;

    private Vector<ChineseElementProperties> chineseElementPropertiesGroup = null;

    private Dimension[] fieldDimensions = null;

    private int[] pageBreaks;

    private String[][] fieldValues;

    private static final String SAMPLE_CHINESE_CHARACTER = "本";

    private static final String SAMPLE_CHINESE_WORD = "日本語文字列";

    private static final String SAMPLE_CHINESE_CHARACTER_PINYIN = "xiang";

    private static final String SAMPLE_CHINESE_WORD_PINYIN = "xiangxiangxiangxiangxiang";

    private static final String SAMPLE_CHINESE_CHARACTER_MEANING = "some meaning";

    private static final String SAMPLE_CHINESE_WORD_MEANING = "some meaning";

    private static final String SAMPLE_LESSON_NUMBER = "999";

    private static final String SAMPLE_LESSON_LEVEL = "C";

    private static final String[] CHARACTER_LENGTH_FIELD_VALUES = { SAMPLE_CHINESE_CHARACTER, SAMPLE_CHINESE_CHARACTER_PINYIN, SAMPLE_CHINESE_CHARACTER_MEANING, SAMPLE_LESSON_NUMBER, SAMPLE_LESSON_LEVEL };

    private static final String[] WORD_LENGTH_FIELD_VALUES = { SAMPLE_CHINESE_WORD, SAMPLE_CHINESE_WORD_PINYIN, SAMPLE_CHINESE_WORD_MEANING, SAMPLE_LESSON_NUMBER, SAMPLE_LESSON_LEVEL };

    public PrintingManager() {
    }

    public void printReviewList(Vector<ChineseElementProperties> chineseElementReviewList) {
        System.out.println("\nPrintingManager.printReviewList() invoked\n");
        this.chineseElementPropertiesGroup = chineseElementReviewList;
        createHTMLListPanel(chineseElementReviewList);
    }

    public void doPrinting() {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PageFormat pf = job.pageDialog(aset);
        job.setPrintable(this, pf);
        boolean ok = job.printDialog(aset);
        if (ok) {
            try {
                job.print(aset);
            } catch (PrinterException ex) {
            }
        }
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int x = 0;
        int y = 0;
        Font font = new Font("Serif", Font.BOLD, 24);
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (fieldDimensions == null) {
            fieldDimensions = calculateFieldDimensions(graphics2D, font);
        }
        if (pageBreaks == null) {
            pageBreaks = calculatePageBreaks(pageFormat);
        }
        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }
        System.out.println("pageFormat.getImageableX(): " + pageFormat.getImageableX() + "pageFormat.getImageableY(): " + pageFormat.getImageableY());
        graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
        int end = (pageIndex == pageBreaks.length) ? fieldValues.length : pageBreaks[pageIndex];
        for (int line = start; line < end; line++) {
            x = 0;
            y += fieldDimensions[0].height;
            for (int i = 0; i < ChineseElementProperties.NUMBER_OF_FIELD_VALUES; i++) {
                graphics2D.drawString(fieldValues[line][i], x, y);
                x = x + fieldDimensions[i].width + 5;
            }
        }
        return PAGE_EXISTS;
    }

    /*************************************************
     *
     *   calculatePageBreaks
     *
     ************************************************/
    private int[] calculatePageBreaks(PageFormat pageFormat) {
        int linesPerPage;
        int numBreaks;
        int[] pageBreaks;
        linesPerPage = (int) (pageFormat.getImageableHeight() / fieldDimensions[0].height);
        numBreaks = (fieldValues.length - 1) / linesPerPage;
        pageBreaks = new int[numBreaks];
        for (int b = 0; b < numBreaks; b++) {
            pageBreaks[b] = (b + 1) * linesPerPage;
        }
        return (pageBreaks);
    }

    private Dimension[] calculateFieldDimensions(Graphics2D graphics2D, Font font) {
        Dimension[] dimensions = new Dimension[ChineseElementProperties.NUMBER_OF_FIELD_VALUES];
        int advance;
        FontMetrics metrics = graphics2D.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        for (int i = 0; i < ChineseElementProperties.NUMBER_OF_FIELD_VALUES; i++) {
            advance = metrics.stringWidth(CHARACTER_LENGTH_FIELD_VALUES[i]);
            dimensions[i] = new Dimension(advance + 2, lineHeight + 2);
        }
        return (dimensions);
    }

    private String[][] extractFieldValues(Vector<ChineseElementProperties> chineseElementPropertiesGroup) {
        String[][] fieldValues = new String[chineseElementPropertiesGroup.size()][ChineseElementProperties.NUMBER_OF_FIELD_VALUES];
        String lessonNumber = "";
        for (int i = 0; i < chineseElementPropertiesGroup.size(); i++) {
            fieldValues[i][0] = chineseElementPropertiesGroup.elementAt(i).getChineseElement();
            fieldValues[i][1] = chineseElementPropertiesGroup.elementAt(i).getPinyin();
            fieldValues[i][2] = chineseElementPropertiesGroup.elementAt(i).getEnglish();
            lessonNumber = Integer.toString(chineseElementPropertiesGroup.elementAt(i).getLessonNumber());
            fieldValues[i][3] = lessonNumber;
            fieldValues[i][4] = chineseElementPropertiesGroup.elementAt(i).getLevel();
        }
        return (fieldValues);
    }

    public void createHTMLListPanel(Vector<ChineseElementProperties> chineseElementPropertiesGroup) {
        JFrame frame = new JFrame("Character Review List");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        htmlListPane = new HTMLListPane(chineseElementPropertiesGroup);
        htmlListPane.setMinimumSize(new Dimension(200, 200));
        try {
            htmlMenuBar = new HTMLMenuBar(htmlListPane);
        } catch (DBAccessException exception) {
            exception.printStackTrace();
        }
        frame.setJMenuBar(htmlMenuBar);
        frame.getContentPane().add(htmlListPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
