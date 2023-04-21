package com.googlecode.lambda4jdt;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

@SuppressWarnings("unused")
public class CustomFoldingDrawing {

    static void drawCollapsedSummaryRegion(Integer moffset, ProjectionViewer viewer, GC gc, StyledText textWidget, int offset, int length) throws Exception {
        if (moffset == null) return;
        StyledTextContent content = textWidget.getContent();
        int line = content.getLineAtOffset(offset);
        int lineStart = content.getOffsetAtLine(line);
        String text = content.getLine(line);
        if (text == null) text = "";
        int lineLength = text.length();
        int lineEnd = lineStart + lineLength;
        if (offset == lineStart) {
            return;
        }
        int widgetOffset = viewer.modelOffset2WidgetOffset(moffset);
        if (widgetOffset <= 0) return;
        Point pS = textWidget.getLocationAtOffset(widgetOffset);
        Color c = gc.getForeground();
        Color gray = new Color(gc.getDevice(), 0xA0, 0xA0, 0xA0);
        Color gray1 = new Color(gc.getDevice(), 0xE3, 0xE3, 0xE3);
        Color black = new Color(gc.getDevice(), 0, 0, 0);
        Color wh = new Color(gc.getDevice(), 0xFF, 0xFF, 0xFF);
        gc.setForeground(gray);
        FontMetrics metrics = gc.getFontMetrics();
        int lineHeight = metrics.getHeight();
        int baseline = textWidget.getBaseline(offset);
        int descent = 1;
        int ascent = metrics.getAscent();
        int leading = baseline - ascent;
        int height = ascent + descent;
        int width = metrics.getAverageCharWidth();
        int pos = pS.x;
        gc.setForeground(black);
        gc.drawString("=>", pS.x, pS.y, true);
        gc.setForeground(c);
    }

    public static void drawCollapsedHolder(Annotation annotation, GC gc, StyledText textWidget, int offset, int length, Color color) {
        color = new Color(gc.getDevice(), 0xA0, 0xA0, 0xA0);
        StyledTextContent content = textWidget.getContent();
        int line = content.getLineAtOffset(offset);
        int lineStart = content.getOffsetAtLine(line);
        String text = content.getLine(line);
        int lineLength = text == null ? 0 : text.length();
        int lineEnd = lineStart + lineLength - 2;
        Point p = textWidget.getLocationAtOffset(lineEnd);
        Color c = gc.getForeground();
        gc.setForeground(color);
        FontMetrics metrics = gc.getFontMetrics();
        int baseline = textWidget.getBaseline(offset);
        int descent = 1;
        int ascent = metrics.getAscent();
        int leading = baseline - ascent;
        int height = ascent + descent;
        int width = metrics.getAverageCharWidth() * 2;
        gc.drawRectangle(p.x, p.y + leading, width, height);
        int third = width / 3;
        int dotsVertical = p.y + baseline - 2;
        gc.drawPoint(p.x + third, dotsVertical);
        gc.drawPoint(p.x + width - third, dotsVertical);
        gc.setForeground(c);
    }
}
