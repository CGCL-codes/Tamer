package com.iver.cit.gvsig.fmap.core;

import java.awt.Color;
import java.util.StringTokenizer;
import org.geotools.styling.Fill;
import org.geotools.styling.GraphicImpl;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.core.v02.FConstant;
import com.iver.cit.gvsig.fmap.core.v02.FSymbol;

/**
 * utility class for SLD functionality
 * @author laura
 */
public class SLDUtils {

    public static Color convertHexStringToColor(String str) throws NumberFormatException {
        int multiplier = 1;
        StringTokenizer tokenizer = new StringTokenizer(str, " \t\r\n\b:;[]()+");
        while (tokenizer.hasMoreTokens()) {
            multiplier = 1;
            String token = tokenizer.nextToken();
            if (null == token) {
                throw new NumberFormatException(str);
            }
            if (token.startsWith("-")) {
                multiplier = -1;
                token = token.substring(1);
            }
            int point_index = token.indexOf(".");
            if (point_index > 0) {
                token = token.substring(0, point_index);
            } else if (point_index == 0) {
                return new Color(0);
            }
            try {
                if (token.startsWith("0x")) {
                    return new Color(multiplier * Integer.parseInt(token.substring(2), 16));
                } else if (token.startsWith("#")) {
                    return new Color(multiplier * Integer.parseInt(token.substring(1), 16));
                } else if (token.startsWith("0") && !token.equals("0")) {
                    return new Color(multiplier * Integer.parseInt(token.substring(1), 8));
                } else {
                    return new Color(multiplier * Integer.parseInt(token));
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }
        throw new NumberFormatException(str);
    }

    public static String convertColorToHexString(java.awt.Color c) {
        String str = Integer.toHexString(c.getRGB() & 0xFFFFFF);
        return ("#" + "000000".substring(str.length()) + str.toUpperCase());
    }

    public String getWellKnownName(int symbolType) {
        return "";
    }

    public static Symbolizer toGeotoolsSymbol(ISymbol sym) {
        FSymbol symbol = (FSymbol) sym;
        StyleFactory styleFactory = StyleFactory.createStyleFactory();
        StyleBuilder styleBuilder = new StyleBuilder();
        StrokeImpl theStroke = (StrokeImpl) styleBuilder.createStroke();
        GraphicImpl graphic = (GraphicImpl) styleBuilder.createGraphic();
        try {
            switch(symbol.getSymbolType()) {
                case FConstant.SYMBOL_TYPE_POINT:
                    PointSymbolizer point = styleFactory.createPointSymbolizer();
                    graphic.setSize(symbol.getSize());
                    Mark[] mark = new Mark[1];
                    mark[0] = styleFactory.createMark();
                    graphic.setMarks(mark);
                    return point;
                case FConstant.SYMBOL_TYPE_LINE:
                    LineSymbolizer line = styleFactory.createLineSymbolizer();
                    theStroke.setColor(convertColorToHexString(symbol.getColor()));
                    line.setStroke(theStroke);
                    return line;
                case FConstant.SYMBOL_TYPE_FILL:
                    PolygonSymbolizer polygon = styleFactory.createPolygonSymbolizer();
                    Fill theFill = styleBuilder.createFill(symbol.getColor());
                    polygon.setFill(theFill);
                    return polygon;
                case FShape.MULTI:
                    return null;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
