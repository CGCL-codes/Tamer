package org.slasoi.infrastructure.monitoring.reporting;

import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import java.awt.*;
import java.io.ByteArrayOutputStream;

abstract class ReportTemplate {

    static Font titlePageTitleFont = new Font(Font.HELVETICA, 26, Font.BOLD);

    static Font titlePageSubtitleFont = new Font(Font.HELVETICA, 16, Font.BOLD);

    static Font chapterFont = new Font(Font.HELVETICA, 16, Font.BOLD);

    static Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);

    static Font subsectionFont = new Font(Font.HELVETICA, 12, Font.BOLD);

    static Font tableFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

    static Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

    static Font normalBoldFont = new Font(Font.HELVETICA, 10, Font.BOLD);

    static Color tableHeaderColor = Color.CYAN;

    static GradientPaint chartBackgroundPaint = new GradientPaint(0F, 0F, new Color(173, 220, 255), 500F, 400F, new Color(217, 229, 243));

    static Color chartBarsColor = new Color(17, 96, 178);

    static int chartWidth = 400;

    static int chartHeight = 300;

    public abstract ByteArrayOutputStream generate() throws Exception;

    void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
