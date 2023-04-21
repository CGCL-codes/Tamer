package org.apache.poi.hssf.usermodel.examples;

import org.apache.poi.hssf.usermodel.*;
import java.io.*;

/**
 * Demonstrates how to use the office drawing capabilities of POI.
 *
 * @author Glen Stampoultzis (glens at apache.org)
 */
public class OfficeDrawing {

    public static void main(String[] args) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("new sheet");
        HSSFSheet sheet2 = wb.createSheet("second sheet");
        HSSFSheet sheet3 = wb.createSheet("third sheet");
        HSSFSheet sheet4 = wb.createSheet("fourth sheet");
        HSSFSheet sheet5 = wb.createSheet("fifth sheet");
        drawSheet1(sheet1);
        drawSheet2(sheet2);
        drawSheet3(sheet3);
        drawSheet4(sheet4, wb);
        drawSheet5(sheet5, wb);
        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
    }

    private static void drawSheet1(HSSFSheet sheet1) {
        HSSFRow row = sheet1.createRow(2);
        row.setHeight((short) 2800);
        row.createCell((short) 1);
        sheet1.setColumnWidth((short) 2, (short) 9000);
        HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
        drawLinesToCenter(patriarch);
        drawManyLines(patriarch);
        drawOval(patriarch);
        drawPolygon(patriarch);
        HSSFSimpleShape rect = patriarch.createSimpleShape(new HSSFClientAnchor(100, 100, 900, 200, (short) 0, 0, (short) 0, 0));
        rect.setShapeType(HSSFSimpleShape.OBJECT_TYPE_RECTANGLE);
    }

    private static void drawSheet2(HSSFSheet sheet2) {
        HSSFRow row = sheet2.createRow(2);
        row.createCell((short) 1);
        row.setHeightInPoints(240);
        sheet2.setColumnWidth((short) 2, (short) 9000);
        HSSFPatriarch patriarch = sheet2.createDrawingPatriarch();
        drawGrid(patriarch);
    }

    private static void drawSheet3(HSSFSheet sheet3) {
        HSSFRow row = sheet3.createRow(2);
        row.setHeightInPoints(140);
        row.createCell((short) 1);
        sheet3.setColumnWidth((short) 2, (short) 9000);
        HSSFPatriarch patriarch = sheet3.createDrawingPatriarch();
        HSSFShapeGroup group = patriarch.createGroup(new HSSFClientAnchor(0, 0, 900, 200, (short) 2, 2, (short) 2, 2));
        HSSFSimpleShape shape1 = group.createShape(new HSSFChildAnchor(3, 3, 500, 500));
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        ((HSSFChildAnchor) shape1.getAnchor()).setAnchor((short) 3, 3, 500, 500);
        HSSFSimpleShape shape2 = group.createShape(new HSSFChildAnchor((short) 1, 200, 400, 600));
        shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
    }

    private static void drawSheet4(HSSFSheet sheet4, HSSFWorkbook wb) {
        HSSFPatriarch patriarch = sheet4.createDrawingPatriarch();
        HSSFTextbox textbox1 = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 0, 0, (short) 1, 1, (short) 2, 2));
        textbox1.setString(new HSSFRichTextString("This is a test"));
        HSSFTextbox textbox2 = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 900, 100, (short) 3, 3, (short) 3, 4));
        textbox2.setString(new HSSFRichTextString("Woo"));
        textbox2.setFillColor(200, 0, 0);
        textbox2.setLineStyle(HSSFSimpleShape.LINESTYLE_DOTGEL);
        HSSFTextbox textbox3 = patriarch.createTextbox(new HSSFClientAnchor(0, 0, 900, 100, (short) 4, 4, (short) 5, 4 + 1));
        HSSFFont font = wb.createFont();
        font.setItalic(true);
        font.setUnderline(HSSFFont.U_DOUBLE);
        HSSFRichTextString string = new HSSFRichTextString("Woo!!!");
        string.applyFont(2, 5, font);
        textbox3.setString(string);
        textbox3.setFillColor(0x08000030);
        textbox3.setLineStyle(HSSFSimpleShape.LINESTYLE_NONE);
        textbox3.setNoFill(true);
    }

    private static void drawSheet5(HSSFSheet sheet5, HSSFWorkbook wb) throws IOException {
        HSSFPatriarch patriarch = sheet5.createDrawingPatriarch();
        HSSFClientAnchor anchor;
        anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 2, 2, (short) 4, 7);
        anchor.setAnchorType(2);
        patriarch.createPicture(anchor, loadPicture("src/resources/logos/logoKarmokar4.png", wb));
        anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 4, 2, (short) 5, 7);
        anchor.setAnchorType(2);
        patriarch.createPicture(anchor, loadPicture("src/resources/logos/logoKarmokar4edited.png", wb));
        anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, 2, (short) 8, 7);
        anchor.setAnchorType(2);
        HSSFPicture picture = patriarch.createPicture(anchor, loadPicture("src/resources/logos/logoKarmokar4s.png", wb));
        picture.resize();
        picture.setLineStyle(picture.LINESTYLE_DASHDOTGEL);
    }

    private static int loadPicture(String path, HSSFWorkbook wb) throws IOException {
        int pictureIndex;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(path);
            bos = new ByteArrayOutputStream();
            int c;
            while ((c = fis.read()) != -1) bos.write(c);
            pictureIndex = wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG);
        } finally {
            if (fis != null) fis.close();
            if (bos != null) bos.close();
        }
        return pictureIndex;
    }

    private static void drawOval(HSSFPatriarch patriarch) {
        HSSFClientAnchor a = new HSSFClientAnchor();
        a.setAnchor((short) 2, 2, 20, 20, (short) 2, 2, 190, 80);
        HSSFSimpleShape s = patriarch.createSimpleShape(a);
        s.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
        s.setLineStyleColor(10, 10, 10);
        s.setFillColor(90, 10, 200);
        s.setLineWidth(HSSFShape.LINEWIDTH_ONE_PT * 3);
        s.setLineStyle(HSSFShape.LINESTYLE_DOTSYS);
    }

    private static void drawPolygon(HSSFPatriarch patriarch) {
        HSSFClientAnchor a = new HSSFClientAnchor();
        a.setAnchor((short) 2, 2, 0, 0, (short) 3, 3, 1023, 255);
        HSSFShapeGroup g = patriarch.createGroup(a);
        g.setCoordinates(0, 0, 200, 200);
        HSSFPolygon p1 = g.createPolygon(new HSSFChildAnchor(0, 0, 200, 200));
        p1.setPolygonDrawArea(100, 100);
        p1.setPoints(new int[] { 0, 90, 50 }, new int[] { 5, 5, 44 });
        p1.setFillColor(0, 255, 0);
        HSSFPolygon p2 = g.createPolygon(new HSSFChildAnchor(20, 20, 200, 200));
        p2.setPolygonDrawArea(200, 200);
        p2.setPoints(new int[] { 120, 20, 150 }, new int[] { 105, 30, 195 });
        p2.setFillColor(255, 0, 0);
    }

    private static void drawManyLines(HSSFPatriarch patriarch) {
        int x1 = 100;
        int y1 = 100;
        int x2 = 800;
        int y2 = 200;
        int color = 0;
        for (int i = 0; i < 10; i++) {
            HSSFClientAnchor a2 = new HSSFClientAnchor();
            a2.setAnchor((short) 2, 2, x1, y1, (short) 2, 2, x2, y2);
            HSSFSimpleShape shape2 = patriarch.createSimpleShape(a2);
            shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            shape2.setLineStyleColor(color);
            y1 -= 10;
            y2 -= 10;
            color += 30;
        }
    }

    private static void drawGrid(HSSFPatriarch patriarch) {
        double xRatio = 3.22;
        double yRatio = 0.6711;
        int x1 = 000;
        int y1 = 000;
        int x2 = 000;
        int y2 = 200;
        for (int i = 0; i < 20; i++) {
            HSSFClientAnchor a2 = new HSSFClientAnchor();
            a2.setAnchor((short) 2, 2, (int) (x1 * xRatio), (int) (y1 * yRatio), (short) 2, 2, (int) (x2 * xRatio), (int) (y2 * yRatio));
            HSSFSimpleShape shape2 = patriarch.createSimpleShape(a2);
            shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            x1 += 10;
            x2 += 10;
        }
        x1 = 000;
        y1 = 000;
        x2 = 200;
        y2 = 000;
        for (int i = 0; i < 20; i++) {
            HSSFClientAnchor a2 = new HSSFClientAnchor();
            a2.setAnchor((short) 2, 2, (int) (x1 * xRatio), (int) (y1 * yRatio), (short) 2, 2, (int) (x2 * xRatio), (int) (y2 * yRatio));
            HSSFSimpleShape shape2 = patriarch.createSimpleShape(a2);
            shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            y1 += 10;
            y2 += 10;
        }
    }

    private static void drawLinesToCenter(HSSFPatriarch patriarch) {
        {
            HSSFClientAnchor a1 = new HSSFClientAnchor();
            a1.setAnchor((short) 2, 2, 0, 0, (short) 2, 2, 512, 128);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(a1);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        }
        {
            HSSFClientAnchor a1 = new HSSFClientAnchor();
            a1.setAnchor((short) 2, 2, 512, 128, (short) 2, 2, 1024, 0);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(a1);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        }
        {
            HSSFClientAnchor a1 = new HSSFClientAnchor();
            a1.setAnchor((short) 1, 1, 0, 0, (short) 1, 1, 512, 100);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(a1);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        }
        {
            HSSFClientAnchor a1 = new HSSFClientAnchor();
            a1.setAnchor((short) 1, 1, 512, 100, (short) 1, 1, 1024, 0);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(a1);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        }
    }
}
