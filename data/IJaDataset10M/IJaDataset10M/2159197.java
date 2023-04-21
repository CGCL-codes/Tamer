package de.laures.cewolf.example;

import java.io.Serializable;

public class ParamBean implements Serializable {

    private int width = 450;

    private int height = 300;

    private boolean antialias = true;

    private boolean xaxisinteger = false;

    private boolean yaxisinteger = false;

    private boolean xtickmarksvisible = true;

    private boolean ytickmarksvisible = true;

    private boolean xticklabelsvisible = true;

    private boolean yticklabelsvisible = true;

    private String title = "";

    private int fontsize = 18;

    private int minVal = -100;

    private int maxVal = 100;

    private String colorColor = "#FFFFFF";

    private String paint = "color";

    private String gradientColor1 = "#FFFFFF";

    private int gradientX1;

    private int gradientX2;

    private String gradientColor2 = "#FFFFFF";

    private int gradientY2;

    private int gradientY1;

    private String textureImage = "";

    private int textureWidth = 50;

    private int textureHeight = 50;

    private boolean cyclic;

    private boolean legend;

    private String legendAnchor = "south";

    private String extraTitle;

    private String mimeType = "image/png";

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAntialias(boolean antialias) {
        this.antialias = antialias;
    }

    public boolean getAntialias() {
        return antialias;
    }

    public boolean isXaxisinteger() {
        return xaxisinteger;
    }

    public void setXaxisinteger(boolean xaxisinteger) {
        this.xaxisinteger = xaxisinteger;
    }

    public boolean isYaxisinteger() {
        return yaxisinteger;
    }

    public void setYaxisinteger(boolean yaxisinteger) {
        this.yaxisinteger = yaxisinteger;
    }

    public boolean getXtickmarksvisible() {
        return xtickmarksvisible;
    }

    public void setXtickmarksvisible(boolean xtickmarksvisible) {
        this.xtickmarksvisible = xtickmarksvisible;
    }

    public boolean getYtickmarksvisible() {
        return ytickmarksvisible;
    }

    public void setYtickmarksvisible(boolean ytickmarksvisible) {
        this.ytickmarksvisible = ytickmarksvisible;
    }

    public boolean getXticklabelsvisible() {
        return xticklabelsvisible;
    }

    public void setXticklabelsvisible(boolean xticklabelsvisible) {
        this.xticklabelsvisible = xticklabelsvisible;
    }

    public boolean getYticklabelsvisible() {
        return yticklabelsvisible;
    }

    public void setYticklabelsvisible(boolean yticklabelsvisible) {
        this.yticklabelsvisible = yticklabelsvisible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFontsize() {
        return fontsize;
    }

    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public String getColorColor() {
        return colorColor;
    }

    public void setColorColor(String colorColor) {
        this.colorColor = colorColor;
    }

    public String getPaint() {
        return paint;
    }

    public void setPaint(String selectedPaint) {
        paint = selectedPaint;
    }

    public boolean isSelectedPaint(String p) {
        return paint.equalsIgnoreCase(p);
    }

    public String getGradientColor1() {
        return gradientColor1;
    }

    public void setGradientColor1(String gradientColor1) {
        this.gradientColor1 = gradientColor1;
    }

    public int getGradientX1() {
        return gradientX1;
    }

    public void setGradientX1(int gradientX1) {
        this.gradientX1 = gradientX1;
    }

    public int getGradientX2() {
        return gradientX2;
    }

    public void setGradientX2(int gradientX2) {
        this.gradientX2 = gradientX2;
    }

    public String getGradientColor2() {
        return gradientColor2;
    }

    public void setGradientColor2(String gradientColor2) {
        this.gradientColor2 = gradientColor2;
    }

    public int getGradientY2() {
        return gradientY2;
    }

    public void setGradientY2(int gradientY2) {
        this.gradientY2 = gradientY2;
    }

    public int getGradientY1() {
        return gradientY1;
    }

    public void setGradientY1(int gradientY1) {
        this.gradientY1 = gradientY1;
    }

    public String getTextureImage() {
        return textureImage;
    }

    public void setTextureImage(String textureImage) {
        this.textureImage = textureImage;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public void setTextureWidth(int textureWidth) {
        this.textureWidth = textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public void setTextureHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    public boolean getCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    public boolean isLegend() {
        return legend;
    }

    public void setLegend(boolean legend) {
        this.legend = legend;
    }

    public String getLegendAnchor() {
        return legendAnchor;
    }

    public void setLegendAnchor(String legendAnchor) {
        this.legendAnchor = legendAnchor;
    }

    public boolean isLegendAnchorSelected(String anchor) {
        return legendAnchor.equalsIgnoreCase(anchor);
    }

    public String getExtraTitle() {
        return extraTitle == null ? "" : extraTitle;
    }

    public void setExtraTitle(String extraTitle) {
        this.extraTitle = extraTitle;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
