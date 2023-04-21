package jpicedt.graphic.io.parser;

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.geom.*;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import jpicedt.graphic.PicPoint;
import jpicedt.graphic.model.*;
import static jpicedt.graphic.model.PicAttributeName.*;
import static jpicedt.graphic.model.StyleConstants.*;
import static jpicedt.graphic.model.StyleConstants.LineStyle.*;
import static jpicedt.graphic.model.StyleConstants.FillStyle.*;
import static jpicedt.graphic.model.StyleConstants.PolydotsStyle.*;
import static jpicedt.graphic.model.PicText.*;
import static jpicedt.Log.*;

/**
 * Content-handler and error-handler for the JPICParser class.
 * @since jPicEdt 1.3.3
 * @author Sylvain Reynal
 * @version $Id: JPICXmlHandler.java,v 1.32 2012/02/04 16:50:20 vincentb1 Exp $
 */
public class JPICXmlHandler extends DefaultHandler {

    Drawing drawing;

    Rectangle2D.Double boundingBox;

    Locator locator;

    /** either the main drawing, or a subgroup ; each new parsed Element should be added to this group */
    PicGroup currentGroup;

    /**
	 * a fifo-like stack used to store the main drawing and its subgroups ; each time a "begin group" is encountered,
	 * the current PicGroup is pushed onto the stack, and a new PicGroup is instanciated, which then
	 * represents the current PicGroup ; the opposite operations are executed in the reverse order
	 * when a "end group" is found. */
    Stack<PicGroup> picGroupStack;

    /**
	 * Convenience used to share information (e.g. parameters, location, ...) across expressions acting
	 * on the same element. Generally, an InstanciationExpression reinits "currentObj" to an instance of
	 * an Element of the proper type, then ensuing expression modify this element's attributes and/or
	 * geometry.
	 */
    Element currentObj;

    /**
	 * A buffer which stores characters fed by the {@link #characters characters()} method on behalf
	 * of the SAXParser. It is cleared each time the {@link #startElement startElement()} method is invoked so
	 * that we get a fresh buffer each time a new XML tag is encountered.
	 */
    StringBuffer characterBuffer;

    /**
	 *
	 */
    public JPICXmlHandler() {
    }

    /**
	 * Return a new instance of the Drawing class populated from JPIC-XML tags
	 */
    public Drawing fetchParsedDrawing() {
        if (drawing == null) {
            drawing = new Drawing(currentGroup);
            Rectangle2D bb = this.boundingBox;
            if (bb == null) bb = drawing.getBoundingBox();
            if (bb != null) drawing.setBoundingBox(bb);
        }
        return drawing;
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() {
        currentGroup = new PicGroup();
        picGroupStack = new Stack<PicGroup>();
    }

    public void endDocument() {
    }

    /**
	 * Receive notification of the start of an element. Since we don't make use of any XML-namespace capability,
	 * only qName and attributes matter for out purpose.
	 * @param qName the qName of the processed tag, e.g. "rect", "g",...
	 */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (DEBUG) debug("qName=" + qName);
        characterBuffer = new StringBuffer();
        if (currentObj != null) throw new SAXParseException("Tag mismatch", locator);
        if (qName.equals("jpic")) startJPic(attributes); else if (qName.equals("text")) startText(attributes); else if (qName.equals("parallelogram")) startParallelogram(attributes); else if (qName.equals("ellipse")) startEllipse(attributes); else if (qName.equals("circle")) startCircle(attributes); else if (qName.equals("smoothpolygon")) startSmoothpolygon(attributes); else if (qName.equals("multicurve")) startMulticurve(attributes); else if (qName.equals("pscurve")) startPsCurve(attributes); else if (qName.equals("g")) startGroup(attributes);
    }

    private void startJPic(Attributes attributes) throws SAXException {
        boolean autoBounding = parseBoolean(attributes, "auto-bounding", true);
        if (!autoBounding) {
            double minX = parseDouble(attributes, "x-min", 0.0);
            double maxX = parseDouble(attributes, "x-max", 100.0);
            double minY = parseDouble(attributes, "y-min", 0.0);
            double maxY = parseDouble(attributes, "y-max", 100.0);
            this.boundingBox = new Rectangle2D.Double();
            this.boundingBox.setFrameFromDiagonal(minX, minY, maxX, maxY);
        } else this.boundingBox = null;
    }

    private void startText(Attributes attributes) throws SAXException {
        this.currentObj = new PicText();
        PicText text = (PicText) currentObj;
        text.setCtrlPt(PicText.P_ANCHOR, new PicPoint(attributes.getValue("anchor-point")), null);
        text.setAttributeSet(createAttributeSet(attributes));
        currentGroup.add(currentObj);
    }

    private void startParallelogram(Attributes attributes) throws SAXException {
        this.currentObj = new PicParallelogram(new PicPoint(attributes.getValue("p1")), new PicPoint(attributes.getValue("p2")), new PicPoint(attributes.getValue("p3")), createAttributeSet(attributes));
        currentGroup.add(currentObj);
        currentObj = null;
    }

    private void startCircle(Attributes attributes) throws SAXException {
        String closureVal = attributes.getValue("closure");
        boolean isPlain = false;
        int closure = PicEllipse.OPEN;
        if (closureVal == null) isPlain = true; else {
            if (closureVal.equals("plain")) isPlain = true; else if (closureVal.equals("open")) closure = PicEllipse.OPEN; else if (closureVal.equals("pie")) closure = PicEllipse.PIE; else if (closureVal.equals("chord")) closure = PicEllipse.CHORD;
        }
        this.currentObj = new PicCircleFrom3Points(new PicPoint(attributes.getValue("p1")), new PicPoint(attributes.getValue("p2")), new PicPoint(attributes.getValue("p3")), isPlain, closure, createAttributeSet(attributes));
        currentGroup.add(currentObj);
        currentObj = null;
    }

    private void startEllipse(Attributes attributes) throws SAXException {
        this.currentObj = new PicEllipse(new PicPoint(attributes.getValue("p1")), new PicPoint(attributes.getValue("p2")), new PicPoint(attributes.getValue("p3")), PicEllipse.OPEN, createAttributeSet(attributes));
        PicEllipse arc = (PicEllipse) currentObj;
        String closureStr = attributes.getValue("closure");
        if (closureStr == null || closureStr.equals("open")) arc.setArcType(PicEllipse.OPEN); else if (closureStr.equals("chord")) arc.setArcType(PicEllipse.CHORD); else if (closureStr.equals("pie")) arc.setArcType(PicEllipse.PIE);
        arc.setAngleStart(Double.parseDouble(attributes.getValue("angle-start")));
        arc.setAngleEnd(Double.parseDouble(attributes.getValue("angle-end")));
        currentGroup.add(currentObj);
        currentObj = null;
    }

    private void startSmoothpolygon(Attributes attributes) throws SAXException {
        PicPoint[] pts = parsePointList(attributes.getValue("points"));
        if (pts == null) throw new SAXParseException("Missing mandatory attribute:points", locator);
        double[] smoothCoeffs = parseDoubleList(attributes.getValue("smoothness"));
        PicAttributeSet set = createAttributeSet(attributes);
        boolean closed = "true".equals(attributes.getValue("closed"));
        if (smoothCoeffs == null) this.currentObj = new PicSmoothPolygon(pts, closed, set); else this.currentObj = new PicSmoothPolygon(pts, closed, smoothCoeffs, set);
        currentGroup.add(currentObj);
        currentObj = null;
    }

    private void startPsCurve(Attributes attributes) throws SAXException {
        PicPoint[] pts = parsePointList(attributes.getValue("points"));
        if (pts == null) throw new SAXParseException("Missing mandatory attribute:points", locator);
        double[] curvature = parseDoubleList(attributes.getValue("curvature"));
        PicAttributeSet set = createAttributeSet(attributes);
        boolean closed = "true".equals(attributes.getValue("closed"));
        if (curvature == null || curvature.length < 3) this.currentObj = new PicPsCurve(pts, closed, set); else this.currentObj = new PicPsCurve(pts, closed, curvature[0], curvature[1], curvature[2], set);
        currentGroup.add(currentObj);
        currentObj = null;
    }

    private void startMulticurve(Attributes attributes) throws SAXException {
        PicPoint[] pts = parsePointList(attributes.getValue("points"));
        if (pts == null) throw new SAXParseException("Missing mandatory attribute:points", locator);
        int errorCriterion = (pts.length / 3) * 3 + 1 - pts.length;
        if (errorCriterion != 0 && errorCriterion != 1) throw new SAXParseException("Invalid points count, shall be 3*N for closed curve, or 3*N+1 for open curves", locator);
        this.currentObj = new PicMultiCurve(pts, createAttributeSet(attributes));
        currentGroup.add(currentObj);
        currentObj = null;
    }

    private void startGroup(Attributes attributes) throws SAXException {
        picGroupStack.push(currentGroup);
        currentGroup = new PicGroup();
        currentObj = null;
        PicAttributeSet set = createAttributeSet(attributes);
        currentGroup.setAttributeSet(set);
        String compoundMode = attributes.getValue("compound-mode");
        if ("joint".equals(compoundMode)) currentGroup.setCompoundMode(BranchElement.CompoundMode.JOINT);
    }

    public void characters(char[] ch, int start, int length) {
        if (characterBuffer == null) characterBuffer = new StringBuffer();
        String str = new String(ch, start, length);
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        str = str.replaceAll("&amp;", "&");
        characterBuffer.append(str);
        if (DEBUG) debug("[" + characterBuffer.toString() + "]");
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("text")) endText(); else if (qName.equals("g")) endGroup();
    }

    private void endText() {
        if (currentObj instanceof TextEditable) {
            String str = characterBuffer.toString();
            if (str.startsWith("\n")) str = str.substring(1);
            if (str.startsWith("\r")) str = str.substring(1);
            if (str.endsWith("\n")) str = str.substring(0, str.length() - 1);
            if (str.endsWith("\r")) str = str.substring(0, str.length() - 1);
            ((TextEditable) currentObj).setText(str);
        }
        currentObj = null;
    }

    private void endGroup() throws SAXException {
        if (picGroupStack.empty()) throw new SAXParseException("End group mismatch", locator);
        PicGroup obj = currentGroup;
        currentGroup = (PicGroup) picGroupStack.pop();
        currentGroup.add(obj);
    }

    public void warning(SAXParseException e) throws SAXException {
        throw e;
    }

    /**
	 * Receive notification of a recoverable parser error.
	 */
    public void error(SAXParseException e) throws SAXException {
        throw e;
    }

    /**
	 * Report a fatal XML parsing error.
	 */
    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    private boolean parseBoolean(Attributes attributes, String name, boolean def) {
        String value = attributes.getValue(name);
        if (value == null) return def;
        return (value.equals("true"));
    }

    private double parseDouble(Attributes attributes, String name, double def) {
        String value = attributes.getValue(name);
        if (value == null) return def;
        return Double.parseDouble(value);
    }

    /**
	 * Convert the given string in RGB Hex radix into a Color object
	 * @param hexStr a string of the form "#rrggbb", where rr, gg and bb are hex numbers.
	 * [SR:pending] compare with static fields in class java.awt.Color, and return one of them if applicable.
	 */
    private Color hexRGBToColor(String hexStr) throws SAXParseException {
        if (!hexStr.startsWith("#") || hexStr.length() != 7) throw new SAXParseException("Wrong color formatting", locator);
        int red = Integer.parseInt(hexStr.substring(1, 3), 16);
        int green = Integer.parseInt(hexStr.substring(3, 5), 16);
        int blue = Integer.parseInt(hexStr.substring(5, 7), 16);
        return new Color(red, green, blue);
    }

    /**
	 * Returns an array point parsed from the given string, which is assumed to have the following format :
	 * <br>
	 * "(x1,y1);(x2,y2);...;(xn,yn)".
	 */
    private PicPoint[] parsePointList(String str) throws NumberFormatException {
        if (str == null) return null;
        ArrayList<PicPoint> ptList = new ArrayList<PicPoint>();
        StringTokenizer tokenizer = new StringTokenizer(str, ";");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            PicPoint pt = new PicPoint(token);
            ptList.add(pt);
        }
        PicPoint[] ptArray = new PicPoint[ptList.size()];
        ptList.toArray(ptArray);
        return ptArray;
    }

    /**
	 * Returns an array of doubles parsed from the given string, which is assumed to have the following format :
	 * <br>
	 * "x1;x2;...;xn".
	 */
    private double[] parseDoubleList(String str) throws NumberFormatException {
        if (str == null) return null;
        ArrayList<Double> list = new ArrayList<Double>();
        StringTokenizer tokenizer = new StringTokenizer(str, ";");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            Double x = new Double(token);
            list.add(x);
        }
        double[] xArray = new double[list.size()];
        for (int i = 0; i < xArray.length; i++) xArray[i] = ((Double) list.get(i)).doubleValue();
        return xArray;
    }

    /**
	 * Return a new PicAttributeSet initialized from the given JPIC-XML Attributes.
	 */
    private PicAttributeSet createAttributeSet(Attributes attr) throws SAXParseException {
        PicAttributeSet set = new PicAttributeSet();
        for (int i = 0; i < attr.getLength(); i++) {
            String name = attr.getQName(i);
            String value = attr.getValue(i);
            if (name.startsWith("stroke")) {
                if (name.equals(LINE_STYLE.getName())) {
                    for (LineStyle v : LineStyle.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(LINE_STYLE, v);
                            break;
                        }
                    }
                } else if (name.equals(LINE_WIDTH.getName())) set.setAttribute(LINE_WIDTH, new Double(value)); else if (name.equals(LINE_COLOR.getName())) set.setAttribute(LINE_COLOR, hexRGBToColor(value)); else if (name.equals("stroke-dasharray")) {
                    StringTokenizer tokenizer = new StringTokenizer(value, ";");
                    if (tokenizer.countTokens() < 2) throw new SAXParseException("Syntax error", locator);
                    set.setAttribute(DASH_OPAQUE, new Double(tokenizer.nextToken()));
                    set.setAttribute(DASH_TRANSPARENT, new Double(tokenizer.nextToken()));
                } else if (name.equals(DOT_SEP.getName())) set.setAttribute(DOT_SEP, new Double(value)); else if (name.equals(DOUBLE_LINE.getName())) set.setAttribute(DOUBLE_LINE, new Boolean(value)); else if (name.equals(DOUBLE_SEP.getName())) set.setAttribute(DOUBLE_SEP, new Double(value)); else if (name.equals(DOUBLE_COLOR.getName())) set.setAttribute(DOUBLE_COLOR, hexRGBToColor(value)); else if (name.equals(OVER_STRIKE.getName())) set.setAttribute(OVER_STRIKE, new Boolean(value)); else if (name.equals(OVER_STRIKE_WIDTH.getName())) set.setAttribute(OVER_STRIKE_WIDTH, new Double(value)); else if (name.equals(OVER_STRIKE_COLOR.getName())) set.setAttribute(OVER_STRIKE_COLOR, hexRGBToColor(value)); else throw new IncorrectAttributeName(name);
            } else if (name.startsWith("fill")) {
                if (name.equals(FILL_STYLE.getName())) {
                    for (FillStyle v : FillStyle.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(FILL_STYLE, v);
                            break;
                        }
                    }
                } else if (name.equals(FILL_COLOR.getName())) set.setAttribute(FILL_COLOR, hexRGBToColor(value)); else if (name.equals(HATCH_WIDTH.getName())) set.setAttribute(HATCH_WIDTH, new Double(value)); else if (name.equals(HATCH_SEP.getName())) set.setAttribute(HATCH_SEP, new Double(value)); else if (name.equals(HATCH_ANGLE.getName())) set.setAttribute(HATCH_ANGLE, new Double(value)); else if (name.equals(HATCH_COLOR.getName())) set.setAttribute(HATCH_COLOR, hexRGBToColor(value)); else throw new IncorrectAttributeName(name);
            } else if (name.startsWith("shadow")) {
                if (name.equals(SHADOW.getName())) set.setAttribute(SHADOW, new Boolean(value)); else if (name.equals(SHADOW_SIZE.getName())) set.setAttribute(SHADOW_SIZE, new Double(value)); else if (name.equals(SHADOW_ANGLE.getName())) set.setAttribute(SHADOW_ANGLE, new Double(value)); else if (name.equals(SHADOW_COLOR.getName())) set.setAttribute(SHADOW_COLOR, hexRGBToColor(value)); else throw new IncorrectAttributeName(name);
            } else if (name.equals(LEFT_ARROW.getName())) set.setAttribute(LEFT_ARROW, createArrow(value)); else if (name.equals(RIGHT_ARROW.getName())) set.setAttribute(RIGHT_ARROW, createArrow(value)); else if (name.startsWith("arrow")) {
                if (name.equals(ARROW_GLOBAL_SCALE_WIDTH.getName())) set.setAttribute(ARROW_GLOBAL_SCALE_WIDTH, new Double(value)); else if (name.equals(ARROW_GLOBAL_SCALE_LENGTH.getName())) set.setAttribute(ARROW_GLOBAL_SCALE_LENGTH, new Double(value)); else if (name.equals(ARROW_WIDTH_MINIMUM_MM.getName())) set.setAttribute(ARROW_WIDTH_MINIMUM_MM, new Double(value)); else if (name.equals(ARROW_WIDTH_LINEWIDTH_SCALE.getName())) set.setAttribute(ARROW_WIDTH_LINEWIDTH_SCALE, new Double(value)); else if (name.equals(ARROW_LENGTH_SCALE.getName())) set.setAttribute(ARROW_LENGTH_SCALE, new Double(value)); else if (name.equals(ARROW_INSET_SCALE.getName())) set.setAttribute(ARROW_INSET_SCALE, new Double(value)); else if (name.equals(TBAR_WIDTH_MINIMUM_MM.getName())) set.setAttribute(TBAR_WIDTH_MINIMUM_MM, new Double(value)); else if (name.equals(TBAR_WIDTH_LINEWIDTH_SCALE.getName())) set.setAttribute(TBAR_WIDTH_LINEWIDTH_SCALE, new Double(value)); else if (name.equals(BRACKET_LENGTH_SCALE.getName())) set.setAttribute(BRACKET_LENGTH_SCALE, new Double(value)); else if (name.equals(RBRACKET_LENGTH_SCALE.getName())) set.setAttribute(RBRACKET_LENGTH_SCALE, new Double(value)); else throw new IncorrectAttributeName(name);
            } else if (name.endsWith("custom")) {
                if (name.equals(PST_CUSTOM.getName())) set.setAttribute(PST_CUSTOM, value); else if (name.equals(TIKZ_CUSTOM.getName())) set.setAttribute(TIKZ_CUSTOM, value); else throw new IncorrectAttributeName(name);
            } else if (name.startsWith("polydots")) {
                if (name.equals(POLYDOTS_STYLE.getName())) {
                    for (PolydotsStyle v : PolydotsStyle.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(POLYDOTS_STYLE, v);
                            break;
                        }
                    }
                } else if (name.equals(POLYDOTS_SUPERIMPOSE.getName())) set.setAttribute(POLYDOTS_SUPERIMPOSE, new Boolean(value)); else if (name.equals(POLYDOTS_SIZE_MINIMUM_MM.getName())) set.setAttribute(POLYDOTS_SIZE_MINIMUM_MM, new Double(value)); else if (name.equals(POLYDOTS_SIZE_LINEWIDTH_SCALE.getName())) set.setAttribute(POLYDOTS_SIZE_LINEWIDTH_SCALE, new Double(value)); else if (name.equals(POLYDOTS_SCALE_H.getName())) set.setAttribute(POLYDOTS_SCALE_H, new Double(value)); else if (name.equals(POLYDOTS_SCALE_V.getName())) set.setAttribute(POLYDOTS_SCALE_V, new Double(value)); else if (name.equals(POLYDOTS_ANGLE.getName())) set.setAttribute(POLYDOTS_ANGLE, new Double(value));
            } else if (name.startsWith("text")) {
                if (name.equals(TEXT_VERT_ALIGN.getName())) {
                    for (VertAlign v : VertAlign.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(TEXT_VERT_ALIGN, v);
                            break;
                        }
                    }
                } else if (name.equals(TEXT_HOR_ALIGN.getName())) {
                    for (HorAlign v : HorAlign.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(TEXT_HOR_ALIGN, v);
                            break;
                        }
                    }
                } else if (name.equals(TEXT_FRAME.getName())) {
                    for (FrameStyle v : FrameStyle.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(TEXT_FRAME, v);
                            break;
                        }
                    }
                } else if (name.equals(TEXT_ROTATION.getName())) {
                    set.setAttribute(TEXT_ROTATION, new Double(value));
                } else if (name.equals(TEXT_MODE.getName())) {
                    for (TextMode v : TextMode.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(TEXT_MODE, v);
                        }
                    }
                } else if (name.equals(TEXT_ICON.getName())) {
                    for (TextIcon v : TextIcon.values()) {
                        if (value.equals(v.toString())) {
                            set.setAttribute(TEXT_ICON, v);
                        }
                    }
                } else throw new IncorrectAttributeName(name);
            }
        }
        return set;
    }

    /**
	 * Convert the given Arrow name to a predefined Arrow
	 * @see ArrowStyle.getPredefinedArrow()
	 */
    private ArrowStyle createArrow(String name) {
        for (ArrowStyle as : ArrowStyle.values()) if (as.toString().equals(name)) return as;
        return ArrowStyle.NONE;
    }

    class IncorrectAttributeValue extends SAXParseException {

        public IncorrectAttributeValue(String name, String value) {
            super("Incorrect value for attribute \"" + name + "\": " + value, locator);
        }
    }

    class IncorrectAttributeName extends SAXParseException {

        public IncorrectAttributeName(String name) {
            super("Attribute \"" + name + "\" is not supported", locator);
        }
    }
}
