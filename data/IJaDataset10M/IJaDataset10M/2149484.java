package com.ibm.tuningfork.timeslice;

import com.ibm.tuningfork.core.figure.HoverState;
import com.ibm.tuningfork.core.graphics.Area;
import com.ibm.tuningfork.core.graphics.Extent;
import com.ibm.tuningfork.core.graphics.Graphics;
import com.ibm.tuningfork.core.graphics.RGBColor;
import com.ibm.tuningfork.core.graphics.TFColor;
import com.ibm.tuningfork.core.graphics.TextStyle;
import com.ibm.tuningfork.infra.data.SliceInfo;
import com.ibm.tuningfork.infra.data.ValueInterval;
import com.ibm.tuningfork.infra.units.ITimeConverter;

public class TimeSlicePainter {

    private Area paintArea;

    private int boxSize;

    private int numberOfColumns;

    private int numberOfRows;

    private int numberOfBoxes;

    private ValueInterval displayRange;

    private int boxBorderSize = 1;

    private int minBoxSize = 3;

    private TextStyle labelStyle;

    private RGBColor missingBoxColor;

    public static final int CONTRASTING_COLOR_ALPHA_THRESHOLD = Graphics.SOLID_ALPHA * 2 / 3;

    public static final int BOX_SIZE_TEXT_INFO_THRESHOLD = 60;

    public static final int FONT_DOWNSIZING_ADJUSTMENT = 3;

    public TimeSlicePainter(ITimeConverter timeConverter, TFColor borderColor, TFColor gridColor, TextStyle labelStyle) {
        this.labelStyle = labelStyle;
        this.missingBoxColor = gridColor.toRGBColor();
    }

    public Area computeGeometry(Area area, ValueInterval displayRange) {
        this.displayRange = displayRange;
        int desiredNumberOfBoxes = (int) displayRange.size() + 1;
        if (desiredNumberOfBoxes == 0) {
            boxSize = 0;
            return Area.EMPTY;
        }
        boxSize = computeBoxSize(area, desiredNumberOfBoxes);
        int availableRows = area.height / boxSize;
        int availableColumns = area.width / boxSize;
        int drawableBoxes = availableRows * availableColumns;
        if (drawableBoxes < desiredNumberOfBoxes) {
            numberOfBoxes = drawableBoxes;
        } else {
            numberOfBoxes = desiredNumberOfBoxes;
        }
        if (availableColumns <= availableRows || drawableBoxes < desiredNumberOfBoxes) {
            numberOfColumns = availableColumns;
            numberOfRows = (numberOfBoxes + numberOfColumns - 1) / numberOfColumns;
        } else {
            numberOfRows = availableRows;
            numberOfColumns = (numberOfBoxes + numberOfRows - 1) / numberOfRows;
        }
        int width = numberOfColumns * boxSize - boxBorderSize;
        int height = numberOfRows * boxSize - boxBorderSize;
        paintArea = new Area(area.x, area.bottom() - height, width, height);
        return paintArea;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public SliceInfo[] paint(Graphics g, ISliceProjection projection, RGBColor[] colorMap, ITimeSliceMappingManager mapper, HoverState[] hoverStates) {
        if (boxSize == 0) {
            return null;
        }
        SliceInfo[] hoveredPages = HoverState.anyHover(hoverStates) ? new SliceInfo[hoverStates.length] : null;
        final boolean fromBottom = mapper.paintFromBottom();
        final int drawnBoxSize = boxSize - boxBorderSize;
        final TextStyle boxInfoStyle = labelStyle.beCentered();
        final TextStyle smallBoxInfoStyle = boxInfoStyle.withFontSize(boxInfoStyle.fontSize - FONT_DOWNSIZING_ADJUSTMENT);
        for (int i = 0; i < projection.size(); i++) {
            SliceInfo slice = projection.get(i);
            int addr = mapper.mapAddress(slice, displayRange);
            int alpha = mapper.mapIntensityToAlpha(slice);
            RGBColor color = mapper.mapColorIndexToColor(slice, colorMap);
            int row = addr / numberOfColumns;
            int column = addr % numberOfColumns;
            Area boxArea = boxCoordToBoxArea(fromBottom, drawnBoxSize, row, column);
            g.fillRectangle(color, alpha, boxArea);
            if (boxSize > BOX_SIZE_TEXT_INFO_THRESHOLD) {
                String[][] details = mapper.getDetailedInformation(slice);
                if (details != null) {
                    String[] strings = details[1];
                    TextStyle style = boxInfoStyle;
                    Extent e = g.multiStringExtent(style, strings);
                    if (e.width > boxSize || e.height > boxSize) {
                        style = smallBoxInfoStyle;
                        e = g.multiStringExtent(style, strings);
                    }
                    if (alpha > CONTRASTING_COLOR_ALPHA_THRESHOLD) {
                        style = style.withContrastingColorForText(color);
                    }
                    g.drawMultiString(style, strings, boxArea.center().moveLeftBy(e.width / 2).moveUpBy(e.height / 3));
                }
            }
            if (hoveredPages != null) {
                for (int h = 0; hoverStates != null && h < hoverStates.length; ++h) {
                    HoverState hs = hoverStates[h];
                    if (hs != null && hs.hover != null && boxArea.contains(hs.hover)) hoveredPages[h] = slice;
                }
            }
        }
        grayOutMissingBoxes(g, fromBottom);
        return hoveredPages;
    }

    private void grayOutMissingBoxes(Graphics g, final boolean fromBottom) {
        int oddNumberOfBoxes = numberOfBoxes % numberOfColumns;
        if (oddNumberOfBoxes != 0) {
            for (int c = numberOfColumns - 1; c >= oddNumberOfBoxes; c--) {
                Area fillArea = boxCoordToBoxArea(fromBottom, boxSize, numberOfRows - 1, c);
                g.fillRectangle(missingBoxColor, fillArea);
            }
        }
    }

    private Area boxCoordToBoxArea(final boolean fromBottom, final int drawnBoxSize, int row, int column) {
        int x = paintArea.x + (column * boxSize);
        int y = fromBottom ? paintArea.bottom() - (row + 1) * boxSize : paintArea.top() + row * boxSize;
        Area boxArea = new Area(x, y, drawnBoxSize, drawnBoxSize);
        return boxArea;
    }

    protected int computeBoxSize(Area area, int numberOfBoxes) {
        int pixelsPerItem = area.width * area.height / numberOfBoxes;
        int size = (int) Math.sqrt(pixelsPerItem);
        while (size > 0 && (area.width / size) * (area.height / size) < numberOfBoxes) {
            size--;
        }
        if (size < minBoxSize) {
            size = minBoxSize;
        }
        return size;
    }
}
