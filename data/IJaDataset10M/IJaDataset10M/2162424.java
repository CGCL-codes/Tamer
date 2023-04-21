package com.d_project.xprint.io;

import java.awt.Color;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import com.d_project.pdf.PDFImage;
import com.d_project.pdf.PDFJpegImage;
import com.d_project.pdf.writer.PDFWriter;
import com.d_project.xprint.core.AbstractXGraphics;

public class XPDFGraphics extends AbstractXGraphics {

    private PDFWriter out;

    public XPDFGraphics(PDFWriter out) {
        this.out = out;
    }

    public void translate(double x, double y) {
        out.translate(x, y);
    }

    public void fill(Shape s) {
        try {
            out.fill(s);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setColor(Color color) {
        out.setColor(color);
    }

    public void drawImage(BufferedImage image, double dx1, double dy1, double dx2, double dy2) {
        try {
            PDFImage pdfImage = new PDFJpegImage(ImageUtil.getJpegData(image, 1.0, 1.0f));
            out.drawImage(pdfImage, dx1, dy1, dx2 - dx1, dy2 - dy1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
