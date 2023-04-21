package com.st.rrd.model;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class GraphDef {

    private boolean antiAliasing = false;

    private boolean textAntiAliasing = false;

    private long startTime, endTime;

    private TimeAxisSetting timeAxisSetting = null;

    private ValueAxisSetting valueAxisSetting = null;

    private boolean altYGrid = false;

    private boolean noMinorGrid = false;

    private boolean altYMrtg = false;

    private boolean altAutoscale = false;

    private boolean altAutoscaleMax = false;

    private int unitsExponent = Integer.MAX_VALUE;

    private int unitsLength = RrdGraphConstants.DEFAULT_UNITS_LENGTH;

    private String verticalLabel = null;

    private int width = 400;

    private int height = 100;

    private String imageInfo = null;

    private float imageQuality = RrdGraphConstants.DEFAULT_IMAGE_QUALITY;

    private String overlayImage = null;

    private String unit = null;

    private boolean lazy = false;

    private double minValue = Double.NaN;

    private double maxValue = Double.NaN;

    private boolean rigid = false;

    private double base = RrdGraphConstants.DEFAULT_BASE;

    private boolean logarithmic = false;

    private boolean noLegend = false;

    private boolean forceRulesLegend = false;

    private String title = null;

    private long step = 0;

    private Font smallFont = RrdGraphConstants.DEFAULT_SMALL_FONT;

    private Font largeFont = RrdGraphConstants.DEFAULT_LARGE_FONT;

    private boolean drawXGrid = true;

    private boolean drawYGrid = true;

    private int firstDayOfWeek = RrdGraphConstants.FIRST_DAY_OF_WEEK;

    private boolean showSignature = true;

    private boolean transparent = false;

    private String imageFormat;

    private List<GraphDatasource> datasources = new ArrayList<GraphDatasource>();

    private List<Area> areas = new ArrayList<Area>();

    private List<Line> lines = new ArrayList<Line>();

    private List<ColoredText> coloredText = new ArrayList<ColoredText>();

    private List<Print> gprints = new ArrayList<Print>();

    private HRule hrule;

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setHrule(HRule hrule) {
        this.hrule = hrule;
    }

    public HRule getHrule() {
        return hrule;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
    }

    public boolean isTextAntiAliasing() {
        return textAntiAliasing;
    }

    public void setTextAntiAliasing(boolean textAntiAliasing) {
        this.textAntiAliasing = textAntiAliasing;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public TimeAxisSetting getTimeAxisSetting() {
        return timeAxisSetting;
    }

    public void setTimeAxisSetting(TimeAxisSetting timeAxisSetting) {
        this.timeAxisSetting = timeAxisSetting;
    }

    public ValueAxisSetting getValueAxisSetting() {
        return valueAxisSetting;
    }

    public void setValueAxisSetting(ValueAxisSetting valueAxisSetting) {
        this.valueAxisSetting = valueAxisSetting;
    }

    public boolean isAltYGrid() {
        return altYGrid;
    }

    public void setAltYGrid(boolean altYGrid) {
        this.altYGrid = altYGrid;
    }

    public boolean isNoMinorGrid() {
        return noMinorGrid;
    }

    public void setNoMinorGrid(boolean noMinorGrid) {
        this.noMinorGrid = noMinorGrid;
    }

    public boolean isAltYMrtg() {
        return altYMrtg;
    }

    public void setAltYMrtg(boolean altYMrtg) {
        this.altYMrtg = altYMrtg;
    }

    public boolean isAltAutoscale() {
        return altAutoscale;
    }

    public void setAltAutoscale(boolean altAutoscale) {
        this.altAutoscale = altAutoscale;
    }

    public boolean isAltAutoscaleMax() {
        return altAutoscaleMax;
    }

    public void setAltAutoscaleMax(boolean altAutoscaleMax) {
        this.altAutoscaleMax = altAutoscaleMax;
    }

    public int getUnitsExponent() {
        return unitsExponent;
    }

    public void setUnitsExponent(int unitsExponent) {
        this.unitsExponent = unitsExponent;
    }

    public int getUnitsLength() {
        return unitsLength;
    }

    public void setUnitsLength(int unitsLength) {
        this.unitsLength = unitsLength;
    }

    public String getVerticalLabel() {
        return verticalLabel;
    }

    public void setVerticalLabel(String verticalLabel) {
        this.verticalLabel = verticalLabel;
    }

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

    public String getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(String imageInfo) {
        this.imageInfo = imageInfo;
    }

    public float getImageQuality() {
        return imageQuality;
    }

    public void setImageQuality(float imageQuality) {
        this.imageQuality = imageQuality;
    }

    public String getOverlayImage() {
        return overlayImage;
    }

    public void setOverlayImage(String overlayImage) {
        this.overlayImage = overlayImage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isRigid() {
        return rigid;
    }

    public void setRigid(boolean rigid) {
        this.rigid = rigid;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public boolean isLogarithmic() {
        return logarithmic;
    }

    public void setLogarithmic(boolean logarithmic) {
        this.logarithmic = logarithmic;
    }

    public boolean isNoLegend() {
        return noLegend;
    }

    public void setNoLegend(boolean noLegend) {
        this.noLegend = noLegend;
    }

    public boolean isForceRulesLegend() {
        return forceRulesLegend;
    }

    public void setForceRulesLegend(boolean forceRulesLegend) {
        this.forceRulesLegend = forceRulesLegend;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public Font getSmallFont() {
        return smallFont;
    }

    public void setSmallFont(Font smallFont) {
        this.smallFont = smallFont;
    }

    public Font getLargeFont() {
        return largeFont;
    }

    public void setLargeFont(Font largeFont) {
        this.largeFont = largeFont;
    }

    public boolean isDrawXGrid() {
        return drawXGrid;
    }

    public void setDrawXGrid(boolean drawXGrid) {
        this.drawXGrid = drawXGrid;
    }

    public boolean isDrawYGrid() {
        return drawYGrid;
    }

    public void setDrawYGrid(boolean drawYGrid) {
        this.drawYGrid = drawYGrid;
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public boolean isShowSignature() {
        return showSignature;
    }

    public void setShowSignature(boolean showSignature) {
        this.showSignature = showSignature;
    }

    public void setDatasources(List<GraphDatasource> datasources) {
        if (datasources == null) {
            throw new IllegalArgumentException("datasources cannot be null");
        }
        this.datasources = datasources;
    }

    public List<GraphDatasource> getDatasources() {
        return datasources;
    }

    public void addDatasource(GraphDatasource datasource) {
        datasources.add(datasource);
    }

    public void setAreas(List<Area> areas) {
        if (areas == null) {
            throw new IllegalArgumentException("areas cannot be null");
        }
        this.areas = areas;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void addArea(Area area, String legend) {
        coloredText.add(new ColoredText(area.getColor(), legend));
        areas.add(area);
    }

    public void setLines(List<Line> lines) {
        if (lines == null) {
            throw new IllegalArgumentException("lines cannot be null");
        }
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void setGprints(List<Print> gprints) {
        if (gprints == null) {
            throw new IllegalArgumentException("gprints cannot be null");
        }
        this.gprints = gprints;
    }

    public List<Print> getGprints() {
        return gprints;
    }

    public void addGPrint(Print gprint) {
        gprints.add(gprint);
    }
}
