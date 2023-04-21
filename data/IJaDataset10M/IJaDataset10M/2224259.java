package draw;

import java.awt.Color;
import java.awt.Font;
import model.Def;
import model.Spoint;

public class Settings {

    private double dpi = 72;

    private double cpi = 72 / 2.54;

    private double imageHeight = 0;

    private double imageWidth = 0;

    private Spoint origin = new Spoint(0, 0, "X");

    private int size = Def.LEGAL;

    private int RULER_INSET = 5;

    private double LEFT_MARGIN = dpi / 2;

    private double TOP_MARGIN = dpi / 2;

    private int originXoffset = 200;

    private int originYoffset = 100;

    private int drawingInfoX = 150;

    private int drawingInfoY = 250;

    private int drawingInfoLineSpacing = 15;

    private Font drawingInfoFont = new Font("Courier", Font.PLAIN + Font.PLAIN, 14);

    private Font boldDrawingInfoFont = new Font("Courier", Font.PLAIN + Font.BOLD, 14);

    private Color pointColor = Color.red;

    private Color pointLabelColor = Color.black;

    private Font pointLabelFont = new Font("Courier", Font.ITALIC + Font.PLAIN, 11);

    private Font pointLabelFontSuper = new Font("Courier", Font.ITALIC + Font.PLAIN, 8);

    private Font autoPointLabelFont = new Font("Courier", Font.ITALIC + Font.BOLD, 8);

    private int pointLabelXOffset = 5;

    private int pointLabelYOffset = 5;

    public Settings(double dpi, int size) {
        this.dpi = dpi;
        this.size = size;
        imageHeight = setImageHeight();
        imageWidth = setImageWidth();
        origin.setLocation(imageWidth - originXoffset, imageHeight - originYoffset);
        origin.setOrigin(true);
    }

    private double setImageWidth() {
        if (size == Def.LETTER) return 11 * dpi;
        if (size == Def.LEGAL) return 14 * dpi;
        if (size == Def.TABLOID) return 17 * dpi;
        if (size == Def.A2) return 17 * dpi;
        return -1;
    }

    private double setImageHeight() {
        if (size == Def.LETTER) return 8.5 * dpi;
        if (size == Def.LEGAL) return 8.5 * dpi;
        if (size == Def.TABLOID) return 11 * dpi;
        if (size == Def.A2) return 22 * dpi;
        return -1;
    }

    public double getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public double getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(double imageHeight) {
        this.imageHeight = imageHeight;
    }

    public double getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(double imageWidth) {
        this.imageWidth = imageWidth;
    }

    public double getLEFT_MARGIN() {
        return LEFT_MARGIN;
    }

    public void setLEFT_MARGIN(int left_margin) {
        LEFT_MARGIN = left_margin;
    }

    public Spoint getOrigin() {
        return origin;
    }

    public void setOrigin(Spoint origin) {
        this.origin = origin;
    }

    public Color getPointColor() {
        return pointColor;
    }

    public void setPointColor(Color pointColor) {
        this.pointColor = pointColor;
    }

    public Color getPointLabelColor() {
        return pointLabelColor;
    }

    public void setPointLabelColor(Color pointLabelColor) {
        this.pointLabelColor = pointLabelColor;
    }

    public Font getPointLabelFont() {
        return pointLabelFont;
    }

    public void setPointLabelFont(Font pointLabelFont) {
        this.pointLabelFont = pointLabelFont;
    }

    public int getRULER_INSET() {
        return RULER_INSET;
    }

    public void setRULER_INSET(int ruler_inset) {
        RULER_INSET = ruler_inset;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getCpi() {
        return cpi;
    }

    public double getTOP_MARGIN() {
        return TOP_MARGIN;
    }

    public void setTOP_MARGIN(double top_margin) {
        TOP_MARGIN = top_margin;
    }

    public void setLEFT_MARGIN(double left_margin) {
        LEFT_MARGIN = left_margin;
    }

    public int getPointLabelXOffset() {
        return pointLabelXOffset;
    }

    public void setPointLabelXOffset(int pointLabelXOffset) {
        this.pointLabelXOffset = pointLabelXOffset;
    }

    public int getPointLabelYOffset() {
        return pointLabelYOffset;
    }

    public void setPointLabelYOffset(int pointLabelYOffset) {
        this.pointLabelYOffset = pointLabelYOffset;
    }

    public Font getAutoPointLabelFont() {
        return autoPointLabelFont;
    }

    public void setAutoPointLabelFont(Font autoPointLabelFont) {
        this.autoPointLabelFont = autoPointLabelFont;
    }

    public int getDrawingInfoLineSpacing() {
        return drawingInfoLineSpacing;
    }

    public void setDrawingInfoLineSpacing(int drawingInfoLineSpacing) {
        this.drawingInfoLineSpacing = drawingInfoLineSpacing;
    }

    public int getDrawingInfoX() {
        return drawingInfoX;
    }

    public void setDrawingInfoX(int drawingInfoX) {
        this.drawingInfoX = drawingInfoX;
    }

    public int getDrawingInfoY() {
        return drawingInfoY;
    }

    public void setDrawingInfoY(int drawingInfoY) {
        this.drawingInfoY = drawingInfoY;
    }

    public int getOriginXoffset() {
        return originXoffset;
    }

    public void setOriginXoffset(int originXoffset) {
        this.originXoffset = originXoffset;
    }

    public int getOriginYoffset() {
        return originYoffset;
    }

    public void setOriginYoffset(int originYoffset) {
        this.originYoffset = originYoffset;
    }

    public Font getDrawingInfoFont() {
        return drawingInfoFont;
    }

    public void setDrawingInfoFont(Font drawingInfoFont) {
        this.drawingInfoFont = drawingInfoFont;
    }

    public Font getBoldDrawingInfoFont() {
        return boldDrawingInfoFont;
    }

    public void setBoldDrawingInfoFont(Font boldDrawingInfoFont) {
        this.boldDrawingInfoFont = boldDrawingInfoFont;
    }

    public Font getPointLabelFontSuper() {
        return pointLabelFontSuper;
    }
}
