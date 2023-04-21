package com.iver.cit.gvsig.fmap.core.symbols;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.logging.Logger;
import javax.print.attribute.PrintRequestAttributeSet;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.core.CartographicSupportToolkit;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.core.styles.ILineStyle;
import com.iver.cit.gvsig.fmap.core.styles.IMask;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.threads.Cancellable;

/**
 * MultiShapeSymbol class allows to create a composition of several symbols with
 * different shapes and be treated as a single symbol.These shapes can be marker,line
 * or fill.
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public class MultiShapeSymbol implements ILineSymbol, IMarkerSymbol, IFillSymbol {

    private IMarkerSymbol marker = SymbologyFactory.createDefaultMarkerSymbol();

    private ILineSymbol line = SymbologyFactory.createDefaultLineSymbol();

    private IFillSymbol fill = SymbologyFactory.createDefaultFillSymbol();

    private IMask mask;

    private String desc;

    private int referenceSystem;

    private MultiShapeSymbol symSelect;

    public Color getLineColor() {
        return line.getColor();
    }

    public void setLineColor(Color color) {
        line.setLineColor(color);
    }

    public ILineStyle getLineStyle() {
        return line.getLineStyle();
    }

    public void setLineStyle(ILineStyle lineStyle) {
        line.setLineStyle(lineStyle);
    }

    public void setLineWidth(double width) {
        line.setLineWidth(width);
    }

    public double getLineWidth() {
        return line.getLineWidth();
    }

    public int getAlpha() {
        return line.getAlpha();
    }

    public void setAlpha(int outlineAlpha) {
        line.setAlpha(outlineAlpha);
    }

    public ISymbol getSymbolForSelection() {
        if (symSelect == null) {
            symSelect = new MultiShapeSymbol();
        }
        if (marker != null) {
            symSelect.setMarkerSymbol((IMarkerSymbol) marker.getSymbolForSelection());
        }
        if (line != null) {
            symSelect.setLineSymbol((ILineSymbol) line.getSymbolForSelection());
        }
        if (fill != null) {
            symSelect.setFillSymbol((IFillSymbol) fill.getSymbolForSelection());
        }
        return symSelect;
    }

    public void draw(Graphics2D g, AffineTransform affineTransform, FShape shp, Cancellable cancel) {
        switch(shp.getShapeType()) {
            case FShape.POINT:
            case FShape.POINT + FShape.Z:
                if (marker != null) marker.draw(g, affineTransform, shp, cancel);
                break;
            case FShape.LINE:
            case FShape.LINE + FShape.Z:
            case FShape.ARC:
            case FShape.ARC + FShape.Z:
                if (line != null) line.draw(g, affineTransform, shp, cancel);
                break;
            case FShape.POLYGON:
            case FShape.POLYGON + FShape.Z:
            case FShape.ELLIPSE:
            case FShape.ELLIPSE + FShape.Z:
            case FShape.CIRCLE:
            case FShape.CIRCLE + FShape.Z:
                if (fill != null) fill.draw(g, affineTransform, shp, cancel);
                break;
        }
    }

    public void getPixExtentPlus(FShape shp, float[] distances, ViewPort viewPort, int dpi) {
        throw new Error("Not yet implemented!");
    }

    public int getOnePointRgb() {
        int rMarker = 0;
        int gMarker = 0;
        int bMarker = 0;
        int aMarker = 0;
        if (marker != null && marker.getColor() != null) {
            rMarker = marker.getColor().getRed();
            gMarker = marker.getColor().getGreen();
            bMarker = marker.getColor().getBlue();
            aMarker = marker.getColor().getAlpha();
        }
        int rLine = 0;
        int gLine = 0;
        int bLine = 0;
        int aLine = 0;
        if (line != null && line.getColor() != null) {
            rLine = line.getColor().getRed();
            gLine = line.getColor().getGreen();
            bLine = line.getColor().getBlue();
            aLine = line.getColor().getAlpha();
        }
        int rFill = 0;
        int gFill = 0;
        int bFill = 0;
        int aFill = 0;
        if (fill != null) {
            Color colorOfFill = null;
            if (fill.getOutline() != null) {
                colorOfFill = fill.getOutline().getColor();
            } else if (fill.getFillColor() != null) {
                colorOfFill = fill.getFillColor();
            }
            if (colorOfFill != null) {
                rFill = colorOfFill.getRed();
                gFill = colorOfFill.getGreen();
                bFill = colorOfFill.getBlue();
                aFill = colorOfFill.getAlpha();
            }
        }
        int red = (rMarker + rLine + rFill) / 3;
        int green = (gMarker + gLine + gFill) / 3;
        int blue = (bMarker + bLine + bFill) / 3;
        int alpha = (aMarker + aLine + aFill) / 3;
        return (alpha) << 24 + (red << 16) + (green << 8) + blue;
    }

    public XMLEntity getXMLEntity() {
        XMLEntity xml = new XMLEntity();
        xml.putProperty("className", getClassName());
        xml.putProperty("desc", getDescription());
        xml.putProperty("unit", getUnit());
        if (marker != null) {
            XMLEntity markerXML = marker.getXMLEntity();
            markerXML.putProperty("id", "marker");
            xml.addChild(markerXML);
        }
        if (line != null) {
            XMLEntity lineXML = line.getXMLEntity();
            lineXML.putProperty("id", "line");
            xml.addChild(lineXML);
        }
        if (fill != null) {
            XMLEntity fillXML = fill.getXMLEntity();
            fillXML.putProperty("id", "fill");
            xml.addChild(fillXML);
        }
        return xml;
    }

    public void setXMLEntity(XMLEntity xml) {
        setDescription(xml.getStringProperty("desc"));
        setUnit(xml.getIntProperty("unit"));
        XMLEntity myXML;
        myXML = xml.firstChild("id", "marker");
        if (myXML != null) marker = (IMarkerSymbol) SymbologyFactory.createSymbolFromXML(myXML, null);
        myXML = xml.firstChild("id", "line");
        if (myXML != null) line = (ILineSymbol) SymbologyFactory.createSymbolFromXML(myXML, null);
        myXML = xml.firstChild("id", "fill");
        if (myXML != null) fill = (IFillSymbol) SymbologyFactory.createSymbolFromXML(myXML, null);
    }

    public String getDescription() {
        return desc;
    }

    public boolean isShapeVisible() {
        if (marker != null) {
            return marker.isShapeVisible();
        }
        if (line != null) return line.isShapeVisible();
        if (fill != null) fill.isShapeVisible();
        return false;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public int getSymbolType() {
        return FShape.MULTI;
    }

    public boolean isSuitableFor(IGeometry geom) {
        return true;
    }

    public void drawInsideRectangle(Graphics2D g, AffineTransform scaleInstance, Rectangle r, PrintRequestAttributeSet properties) throws SymbolDrawingException {
        double myWidth = (r.getWidth() / 3);
        Rectangle rect = new Rectangle(r.x, r.y, (int) myWidth, r.height);
        if (marker != null) {
            marker.drawInsideRectangle(g, scaleInstance, rect, properties);
        }
        rect = new Rectangle((int) (r.x + myWidth), r.y, (int) myWidth, r.height);
        if (line != null) {
            line.drawInsideRectangle(g, scaleInstance, rect, properties);
        }
        rect = new Rectangle((int) (r.x + myWidth + myWidth), r.y, (int) myWidth, r.height);
        if (fill != null) {
            fill.drawInsideRectangle(g, scaleInstance, rect, properties);
        }
    }

    public String getClassName() {
        return getClass().getName();
    }

    public void print(Graphics2D g, AffineTransform at, FShape shape, PrintRequestAttributeSet properties) {
        switch(shape.getShapeType()) {
            case FShape.POINT:
            case FShape.POINT + FShape.Z:
                if (marker != null) marker.print(g, at, shape, properties);
                break;
            case FShape.LINE:
            case FShape.LINE + FShape.Z:
            case FShape.ARC:
            case FShape.ARC + FShape.Z:
                if (line != null) line.print(g, at, shape, properties);
                break;
            case FShape.POLYGON:
            case FShape.POLYGON + FShape.Z:
            case FShape.ELLIPSE:
            case FShape.ELLIPSE + FShape.Z:
            case FShape.CIRCLE:
            case FShape.CIRCLE + FShape.Z:
                if (fill != null) fill.print(g, at, shape, properties);
                break;
        }
    }

    public double getRotation() {
        if (marker != null) return marker.getRotation();
        return 0;
    }

    public void setRotation(double rotation) {
        if (marker != null) marker.setRotation(rotation);
    }

    public Point2D getOffset() {
        if (marker != null) return marker.getOffset();
        return new Point2D.Double();
    }

    public void setOffset(Point2D offset) {
        if (marker != null) marker.setOffset(offset);
    }

    public double getSize() {
        if (marker != null) return marker.getSize();
        return 0;
    }

    public void setSize(double size) {
        if (marker != null) marker.setSize(size);
    }

    public Color getColor() {
        if (marker != null) return marker.getColor();
        return null;
    }

    public void setColor(Color color) {
        if (marker != null) marker.setColor(color);
    }

    public void setFillColor(Color color) {
        if (fill != null) fill.setFillColor(color);
    }

    public void setOutline(ILineSymbol outline) {
        if (fill != null) fill.setOutline(outline);
    }

    public Color getFillColor() {
        if (fill != null) return fill.getFillColor();
        return null;
    }

    public ILineSymbol getOutline() {
        if (fill != null) return fill.getOutline();
        return null;
    }

    public int getFillAlpha() {
        if (fill != null) return fill.getFillAlpha();
        return 255;
    }

    public IMask getMask() {
        return mask;
    }

    public void setUnit(int unitIndex) {
        if (marker != null) marker.setUnit(unitIndex);
        if (line != null) line.setUnit(unitIndex);
    }

    public int getUnit() {
        if (marker != null) return marker.getUnit();
        if (line != null) return line.getUnit();
        return -1;
    }

    public void setMask(IMask mask) {
        throw new Error("Not yet implemented!");
    }

    public int getReferenceSystem() {
        return this.referenceSystem;
    }

    public void setReferenceSystem(int system) {
        this.referenceSystem = system;
    }

    public double toCartographicSize(ViewPort viewPort, double dpi, FShape shp) {
        switch(shp.getShapeType()) {
            case FShape.POINT:
            case FShape.POINT + FShape.Z:
                if (marker != null) return marker.toCartographicSize(viewPort, dpi, shp);
            case FShape.LINE:
            case FShape.LINE + FShape.Z:
            case FShape.ARC:
            case FShape.ARC + FShape.Z:
                if (line != null) return line.toCartographicSize(viewPort, dpi, shp);
            case FShape.POLYGON:
            case FShape.POLYGON + FShape.Z:
            case FShape.ELLIPSE:
            case FShape.ELLIPSE + FShape.Z:
            case FShape.CIRCLE:
            case FShape.CIRCLE + FShape.Z:
                Logger.getAnonymousLogger().warning("Cartographic size does not have any sense for fill symbols");
        }
        return -1;
    }

    public void setCartographicSize(double cartographicSize, FShape shp) {
        switch(shp.getShapeType()) {
            case FShape.POINT:
            case FShape.POINT + FShape.Z:
                if (marker != null) marker.setCartographicSize(cartographicSize, null);
                break;
            case FShape.LINE:
            case FShape.LINE + FShape.Z:
            case FShape.ARC:
            case FShape.ARC + FShape.Z:
                if (line != null) line.setCartographicSize(cartographicSize, null);
                break;
            case FShape.POLYGON:
            case FShape.POLYGON + FShape.Z:
            case FShape.ELLIPSE:
            case FShape.ELLIPSE + FShape.Z:
            case FShape.CIRCLE:
            case FShape.CIRCLE + FShape.Z:
                Logger.getAnonymousLogger().warning("Cartographic size does not have any sense for fill symbols");
        }
    }

    public double getCartographicSize(ViewPort viewPort, double dpi, FShape shp) {
        switch(shp.getShapeType()) {
            case FShape.POINT:
            case FShape.POINT + FShape.Z:
                return CartographicSupportToolkit.getCartographicLength(marker, getSize(), viewPort, dpi);
            case FShape.LINE:
            case FShape.LINE + FShape.Z:
            case FShape.ARC:
            case FShape.ARC + FShape.Z:
                return CartographicSupportToolkit.getCartographicLength(line, getSize(), viewPort, dpi);
            case FShape.POLYGON:
            case FShape.POLYGON + FShape.Z:
            case FShape.ELLIPSE:
            case FShape.ELLIPSE + FShape.Z:
            case FShape.CIRCLE:
            case FShape.CIRCLE + FShape.Z:
                Logger.getAnonymousLogger().warning("Cartographic size does not have any sense for fill symbols");
        }
        return -1;
    }

    public IMarkerSymbol getMarkerSymbol() {
        return marker;
    }

    public ILineSymbol getLineSymbol() {
        return line;
    }

    public IFillSymbol getFillSymbol() {
        return fill;
    }

    public void setMarkerSymbol(IMarkerSymbol markerSymbol) {
        this.marker = markerSymbol;
    }

    public void setLineSymbol(ILineSymbol lineSymbol) {
        this.line = lineSymbol;
    }

    public void setFillSymbol(IFillSymbol fillFillSymbol) {
        this.fill = fillFillSymbol;
    }

    public boolean hasFill() {
        if (fill == null) return false;
        return fill.hasFill();
    }

    public void setHasFill(boolean hasFill) {
        if (fill != null) {
            fill.setHasFill(hasFill);
        }
    }

    public boolean hasOutline() {
        return false;
    }

    public void setHasOutline(boolean hasOutline) {
    }
}
