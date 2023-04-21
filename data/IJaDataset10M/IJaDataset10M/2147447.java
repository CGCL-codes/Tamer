package org.xmlcml.cml.legacy2cml.graphics.wmf;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.xmlcml.cml.base.CMLRuntimeException;
import org.xmlcml.cml.legacy2cml.graphics.WMFConverter;
import org.xmlcml.euclid.Util;

public class MetaFileRecord extends Object {

    private int rdSize;

    private short rdFunction;

    private byte[] rdParm;

    private int recordIndex;

    private WMFConverter wmfConv;

    private int fontStyle;

    private int fontWeight;

    private boolean fontItalic = false;

    private double oldyy;

    private double oldxx;

    private short fontHeightShortx = 1;

    public MetaFileRecord(int rdSize, short rdFunction, byte[] rdParm, WMFConverter wmfConv) {
        this.rdSize = rdSize;
        this.rdFunction = rdFunction;
        this.rdParm = rdParm;
        this.wmfConv = wmfConv;
    }

    public void initialize(int rdSize, short rdFunction, byte[] rdParm) {
        this.rdSize = rdSize;
        this.rdFunction = rdFunction;
        this.rdParm = rdParm;
    }

    public String parmString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < rdSize; i++) {
            s.append(Integer.toHexString(rdParm[i]) + " ");
        }
        s.append("\n");
        for (int i = 0; i < rdSize; i++) {
            s.append((char) rdParm[i] + "");
        }
        return s.toString();
    }

    private void lookForPostscript(String s) {
        System.out.println("PostScript");
        int i = 0;
        while (true) {
            int idx = s.indexOf("%%EndProlog");
            if (idx == -1) {
                break;
            }
            s = s.substring(idx + "%%EndProlog".length());
            idx = s.indexOf("%%Trailer");
            String ss;
            if (idx != -1) {
                ss = s.substring(0, idx);
                s = s.substring(idx + "%%Trailer".length());
            } else {
                throw new CMLRuntimeException("missing postscript trailer");
            }
            ss = ss.trim();
            i++;
        }
    }

    public double readAndConvertX(DataInputStream parmStream) {
        short x = WMFConverter.readInt(parmStream);
        return mapX(x);
    }

    public double mapX(short x) {
        float d_x = (float) wmfConv.getDevExtX() / wmfConv.getLogExtX();
        double xx = (x - wmfConv.getLogOrgX()) * d_x;
        return Util.trimFloat(xx, 2);
    }

    public double readAndConvertY(DataInputStream parmStream) {
        short y = WMFConverter.readInt(parmStream);
        return mapY(y);
    }

    public double mapY(short y) {
        float d_y = (float) wmfConv.getDevExtY() / wmfConv.getLogExtY();
        double yy = (y - wmfConv.getLogOrgY()) * d_y;
        return Util.trimFloat(yy, 2);
    }

    /** read a record
     * hope to change this later
     * @param fromSelect
     */
    public static boolean processMetaRecord(MetaFileRecord mRecord, boolean fromSelect) {
        boolean readOk = true;
        if (mRecord == null) {
            return false;
        }
        String currentFont = "Dialog";
        if (mRecord.rdParm == null) {
            throw new CMLRuntimeException("null mRecord.rdParm");
        }
        ByteArrayInputStream parmIn = new ByteArrayInputStream(mRecord.rdParm);
        DataInputStream parmStream = new DataInputStream(parmIn);
        switch(mRecord.rdFunction) {
            case 0x1e:
                mRecord.saveDC(parmStream);
                break;
            case 0x102:
                mRecord.setBkMode(parmStream);
                break;
            case 0x104:
                mRecord.setRop2(parmStream);
                break;
            case 0x106:
                mRecord.setPolyFillMode(parmStream);
                break;
            case 0x107:
                mRecord.setStretchBltMode(parmStream);
                break;
            case 0x127:
                mRecord.restoreDC(parmStream);
                break;
            case 0x12d:
                MetaFileRecord mRecord1 = mRecord.selectObject(parmStream);
                processMetaRecord(mRecord1, true);
                break;
            case 0x12e:
                mRecord.setTextAlign(parmStream);
                break;
            case 0x1f0:
                mRecord.deleteObject(parmStream);
                break;
            case 0x201:
                mRecord.setBKColor(parmStream);
                break;
            case 0x209:
                mRecord.textColor(parmStream);
                break;
            case 0x20B:
                mRecord.setWindowOrg(parmStream);
                break;
            case 0x20C:
                mRecord.setWindowExt(parmStream);
                break;
            case 0x213:
                mRecord.lineto(parmStream);
                break;
            case 0x214:
                mRecord.moveto(parmStream);
                break;
            case 0x2fa:
                mRecord.createPenIndirect(fromSelect, parmStream);
                break;
            case 0x2fb:
                mRecord.createFontIndirect(fromSelect, parmStream, currentFont);
                break;
            case 0x2fc:
                mRecord.createBrushIndirect(fromSelect, parmStream);
                break;
            case 0x324:
                mRecord.createPolygon(parmStream);
                break;
            case 0x325:
                mRecord.createPolyline(parmStream);
                break;
            case 0x418:
                mRecord.createOval(parmStream);
                break;
            case 0x41b:
                mRecord.createRectangle(parmStream);
                break;
            case 0x521:
                mRecord.textOut(parmStream);
                break;
            case 0x538:
                mRecord.createPolypolyline(parmStream);
                break;
            case 0x626:
                mRecord.escape();
                break;
            case 0x6ff:
                if (!fromSelect) {
                    mRecord.wmfConv.ht.addObject(mRecord.recordIndex, mRecord);
                }
                break;
            case 0x817:
                mRecord.createArc(parmStream);
                break;
            case 0xa32:
                mRecord.extText(parmStream);
                break;
            case 0xF43:
                mRecord.stretchDIB(parmStream);
                break;
            default:
                System.out.println("MRECf UNKNOWN " + mRecord);
        }
        try {
            parmStream.close();
        } catch (IOException e) {
            System.err.println(e);
        }
        return readOk;
    }

    private void deleteObject(DataInputStream parmStream) {
        this.wmfConv.setWindowInt(WMFConverter.readInt(parmStream));
        this.wmfConv.ht.deleteObject(this.wmfConv.getWindowInt());
    }

    private MetaFileRecord selectObject(DataInputStream parmStream) {
        this.wmfConv.setWindowInt(WMFConverter.readInt(parmStream));
        return this.wmfConv.ht.selectObject(this.wmfConv.getWindowInt());
    }

    private void escape() {
        String s = this.parmString();
        String t = s.substring(0, Math.min(100, s.length()));
        if (s.indexOf("COLORDEF") != -1) {
        } else if (s.indexOf("25 21 50 53 2d 41 64 6f 62 65") != -1) {
            try {
                this.lookForPostscript(s);
            } catch (CMLRuntimeException e) {
                System.out.println("ERROR: " + e);
            }
        } else if (s.indexOf("%%") != -1) {
            this.lookForPostscript(s);
        } else if (t.startsWith("f 0 c 0 4d 61 74 68 54 79 70 65")) {
            System.out.println("MathType");
        } else if (t.startsWith("26 0")) {
        } else if (t.startsWith("25 0")) {
        } else if (t.startsWith("f 0 a 0 ffffffff")) {
        } else if (t.startsWith("25 0") && t.indexOf("4d 53 45 50 53") != -1) {
            System.out.println("MSEPS");
        } else {
            System.out.println(">>??ESCAPE-UNKNOWN???> " + t);
        }
    }

    private void setWindowExt(DataInputStream parmStream) {
        this.wmfConv.setLogExtY(WMFConverter.readInt(parmStream));
        this.wmfConv.setLogExtX(WMFConverter.readInt(parmStream));
    }

    private void setWindowOrg(DataInputStream parmStream) {
        this.wmfConv.setLogOrgY(WMFConverter.readInt(parmStream));
        this.wmfConv.setLogOrgX(WMFConverter.readInt(parmStream));
    }

    private void extText(DataInputStream parmStream) {
        String tempBuffer;
        byte[] textBuffer;
        double yy = readAndConvertY(parmStream);
        double xx = readAndConvertX(parmStream);
        short numChars = WMFConverter.readInt(parmStream);
        short wOptions = WMFConverter.readInt(parmStream);
        textBuffer = new byte[numChars];
        try {
            parmStream.read(textBuffer);
        } catch (IOException e) {
            System.err.println(e);
        }
        tempBuffer = new String(textBuffer);
        outputText(tempBuffer, yy, xx);
    }

    private void outputText(String tempBuffer, double yy, double xx) {
        int ch = tempBuffer.charAt(tempBuffer.length() - 1);
        if (ch < 31) {
            tempBuffer = tempBuffer.substring(0, tempBuffer.length() - 1);
        }
        wmfConv.getWmfSVG().outputText(xx, yy, tempBuffer);
    }

    private void textColor(DataInputStream parmStream) {
        int selColor;
        selColor = WMFConverter.readLong(parmStream);
        wmfConv.cvtTool.setColors(selColor);
    }

    private void lineto(DataInputStream parmStream) {
        double yy = readAndConvertY(parmStream);
        double xx = readAndConvertX(parmStream);
        wmfConv.getWmfSVG().lineto(xx, yy);
    }

    private void moveto(DataInputStream parmStream) {
        oldyy = readAndConvertY(parmStream);
        oldxx = readAndConvertX(parmStream);
        wmfConv.getWmfSVG().moveto(oldxx, oldyy);
    }

    private void createPolypolyline(DataInputStream parmStream) {
        int numPolys = WMFConverter.readInt(parmStream);
        short[] ncount = new short[numPolys];
        for (int j = 0; j < numPolys; j++) {
            ncount[j] = WMFConverter.readInt(parmStream);
        }
        for (int j = 0; j < numPolys; j++) {
            writePolygon(parmStream, ncount[j]);
        }
    }

    private void createPolygon(DataInputStream parmStream) {
        short numPoints = WMFConverter.readInt(parmStream);
        writePolygon(parmStream, numPoints);
    }

    private void writePolygon(DataInputStream parmStream, short numPoints) {
        double[][] points = storePoints(parmStream, numPoints);
        wmfConv.getWmfSVG().polygon(points);
    }

    private void writePolyline(DataInputStream parmStream, short numPoints) {
        double[][] points = storePoints(parmStream, numPoints);
        wmfConv.getWmfSVG().polyline(points);
    }

    private double[][] storePoints(DataInputStream parmStream, short numPoints) {
        double[][] points = new double[2][numPoints];
        for (int i = 0; i < numPoints; i++) {
            points[0][i] = readAndConvertX(parmStream);
            points[1][i] = readAndConvertY(parmStream);
        }
        return points;
    }

    private void createPolyline(DataInputStream parmStream) {
        short numPoints = WMFConverter.readInt(parmStream);
        writePolyline(parmStream, numPoints);
    }

    private void createPenIndirect(boolean fromSelect, DataInputStream parmStream) {
        int selColor;
        short lbstyle;
        String colorBuffer;
        if (!fromSelect) {
            wmfConv.ht.addObject(recordIndex, this);
        } else {
            lbstyle = WMFConverter.readInt(parmStream);
            short x = WMFConverter.readInt(parmStream);
            short y = WMFConverter.readInt(parmStream);
            selColor = WMFConverter.readLong(parmStream);
            colorBuffer = wmfConv.cvtTool.getRGBColor(selColor);
            String oldColor = wmfConv.getWmfSVG().getStroke();
            if (oldColor == null || !colorBuffer.equals(oldColor)) {
                System.out.println("COLOR " + colorBuffer);
            }
            wmfConv.getWmfSVG().setStroke(colorBuffer);
        }
    }

    private void createBrushIndirect(boolean fromSelect, DataInputStream parmStream) {
        int selColor;
        short lbhatch;
        short lbstyle;
        String colorBuffer;
        if (!fromSelect) {
            wmfConv.ht.addObject(recordIndex, this);
        } else {
            lbstyle = WMFConverter.readInt(parmStream);
            selColor = WMFConverter.readLong(parmStream);
            lbhatch = WMFConverter.readInt(parmStream);
            colorBuffer = wmfConv.cvtTool.getRGBColor(selColor);
            if (lbstyle > 0) {
                wmfConv.getWmfSVG().setStroke(colorBuffer);
            } else {
                wmfConv.getWmfSVG().setFill(colorBuffer);
            }
            System.out.println("BRUSH " + colorBuffer);
        }
    }

    private void createFontIndirect(boolean fromSelect, DataInputStream parmStream, String currentFont) {
        short x;
        short x2;
        short y2;
        String tempBuffer;
        byte[] textBuffer;
        String fontWeightS = "normal";
        if (!fromSelect) {
            wmfConv.ht.addObject(recordIndex, this);
        } else {
            fontHeightShortx = WMFConverter.readInt(parmStream);
            float fontHeight = fontHeightShortx;
            fontHeightShortx = (short) fontHeight;
            if (fontHeightShortx < 0) {
                fontHeightShortx *= -1;
                fontHeightShortx = (short) mapY(fontHeightShortx);
            } else {
                fontHeight = (fontHeight / wmfConv.getInch());
                fontHeight = (fontHeight * 72);
                fontHeightShortx = (short) fontHeight;
            }
            x2 = WMFConverter.readInt(parmStream);
            y2 = WMFConverter.readInt(parmStream);
            y2 = WMFConverter.readInt(parmStream);
            y2 = WMFConverter.readInt(parmStream);
            fontWeight = y2;
            textBuffer = new byte[1];
            try {
                parmStream.read(textBuffer);
            } catch (IOException e) {
                System.err.println(e);
            }
            x = (short) textBuffer[0];
            fontItalic = false;
            if (x < 0) {
                fontItalic = true;
            }
            textBuffer = new byte[7];
            try {
                parmStream.read(textBuffer);
            } catch (IOException e) {
                System.err.println(e);
            }
            tempBuffer = new String(textBuffer);
            textBuffer = new byte[32];
            try {
                parmStream.read(textBuffer);
            } catch (IOException e) {
                System.err.println(e);
            }
            tempBuffer = new String(textBuffer);
            currentFont = "Dialog";
            if (tempBuffer.startsWith("Courier")) {
                currentFont = "Courier";
            } else if (tempBuffer.startsWith("MS Sans Serif")) {
                currentFont = "Dialog";
            } else if (tempBuffer.startsWith("Arial")) {
                currentFont = "Helvetica";
            } else if (tempBuffer.startsWith("Arial Narrow")) {
                currentFont = "Helvetica";
            } else if (tempBuffer.startsWith("Arial Black")) {
                currentFont = "Helvetica";
                fontWeight = 700;
            } else if (tempBuffer.startsWith("Times New Roman")) {
                currentFont = "TimesRoman";
            } else if (tempBuffer.startsWith("Wingdings")) {
                currentFont = "ZapfDingbats";
            }
            if (fontItalic) {
                fontStyle = Font.ITALIC;
                if (fontWeight >= 700) {
                    fontStyle = 3;
                }
                System.out.println("ITALIC....." + fontStyle);
            } else {
                fontStyle = Font.PLAIN;
                if (fontWeight >= 700) {
                    fontStyle = Font.BOLD;
                    fontWeightS = "bold";
                } else {
                    fontWeightS = "normal";
                }
            }
        }
        wmfConv.getWmfSVG().setFontWeight(fontWeightS);
        wmfConv.getWmfSVG().setFontSize((double) fontHeightShortx);
        wmfConv.getWmfSVG().desc("Java Font definition:" + currentFont + " " + fontWeight);
    }

    private void createRectangle(DataInputStream parmStream) {
        double yy2 = readAndConvertY(parmStream);
        double xx2 = readAndConvertX(parmStream);
        double yy = readAndConvertY(parmStream);
        double xx = readAndConvertX(parmStream);
        wmfConv.getWmfSVG().rect(xx, yy, Math.abs(xx2 - xx), Math.abs(yy2 - yy));
    }

    private void createArc(DataInputStream parmStream) {
        double[] points = new double[8];
        for (int i = 0; i < 4; i++) {
            points[2 * i] = readAndConvertY(parmStream);
            points[2 * i + 1] = readAndConvertX(parmStream);
        }
        System.out.println("ARC");
    }

    private void createOval(DataInputStream parmStream) {
        double yy2 = readAndConvertY(parmStream);
        double xx2 = readAndConvertX(parmStream);
        double yy = readAndConvertY(parmStream);
        double xx = readAndConvertX(parmStream);
        double major;
        double minor;
        double angle;
        double w = Math.abs(xx2 - xx);
        double h = Math.abs(yy2 - yy);
        if (w > h) {
            major = w;
            minor = h;
            angle = 0;
        } else {
            major = h;
            minor = w;
            angle = 90;
        }
        double cx = xx + 0.5 * w;
        double cy = yy + 0.5 * h;
        wmfConv.getWmfSVG().ellipse(cx, cy, major, minor, angle);
    }

    private void textOut(DataInputStream parmStream) {
        short numChars = WMFConverter.readInt(parmStream);
        byte[] textBuffer = new byte[numChars + 1];
        try {
            parmStream.read(textBuffer);
        } catch (IOException e) {
            System.err.println(e);
        }
        String tempBuffer = new String(textBuffer);
        double yy = readAndConvertY(parmStream);
        double xx = readAndConvertX(parmStream);
        try {
            outputText(tempBuffer, yy, xx);
        } catch (CMLRuntimeException e) {
            System.err.println("" + e);
        }
    }

    private void setBkMode(DataInputStream parmStream) {
    }

    private void setRop2(DataInputStream parmStream) {
    }

    private void setPolyFillMode(DataInputStream parmStream) {
    }

    private void setStretchBltMode(DataInputStream parmStream) {
    }

    private void setTextAlign(DataInputStream parmStream) {
    }

    private void setBKColor(DataInputStream parmStream) {
    }

    private void saveDC(DataInputStream parmStream) {
    }

    private void restoreDC(DataInputStream parmStream) {
    }

    private void stretchDIB(DataInputStream parmStream) {
        this.wmfConv.getWmfSVG().desc("DIB - Device independent Bitmap - " + "will convert to JPEG in next release ");
    }

    public String toString() {
        return "rdFunction " + Integer.toHexString(rdFunction) + " rdSize " + rdSize + " rdParm " + parmString();
    }

    public void setRecordIndex(int recordIndex) {
        this.recordIndex = recordIndex;
    }

    public WMFConverter getWmfConv() {
        return wmfConv;
    }

    public void setWmfConv(WMFConverter wmfConv) {
        this.wmfConv = wmfConv;
    }
}
