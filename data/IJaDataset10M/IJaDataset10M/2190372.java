package com.vividsolutions.jts.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.util.Assert;

/**
 * Converts a geometry in Well-Known Text format to a {@link Geometry}.
 * <p>
 * <code>WKTReader</code> supports
 * extracting <code>Geometry</code> objects from either {@link Reader}s or
 *  {@link String}s. This allows it to function as a parser to read <code>Geometry</code>
 *  objects from text blocks embedded in other data formats (e.g. XML). <P>
 * <p>
 *  A <code>WKTReader</code> is parameterized by a <code>GeometryFactory</code>,
 *  to allow it to create <code>Geometry</code> objects of the appropriate
 *  implementation. In particular, the <code>GeometryFactory</code>
 *  determines the <code>PrecisionModel</code> and <code>SRID</code> that is
 *  used. <P>
 *
 *  The <code>WKTReader</code> converts all input numbers to the precise
 *  internal representation.
 *
 * <h3>Notes:</h3>
 * <ul>
 * <li>Keywords are case-insensitive.
 * <li>The reader supports non-standard "LINEARRING" tags.
 * <li>The reader uses <tt>Double.parseDouble</tt> to perform the conversion of ASCII
 * numbers to floating point.  This means it supports the Java
 * syntax for floating point literals (including scientific notation).
 * </ul>
 *
 * <h3>Syntax</h3>
 * The following syntax specification describes the version of Well-Known Text
 * supported by JTS.
 * (The specification uses a syntax language similar to that used in
 * the C and Java language specifications.)
 * <p>
 *
 * <blockquote><pre>
 * <i>WKTGeometry:</i> one of<i>
 *
 *       WKTPoint  WKTLineString  WKTLinearRing  WKTPolygon
 *       WKTMultiPoint  WKTMultiLineString  WKTMultiPolygon
 *       WKTGeometryCollection</i>
 *
 * <i>WKTPoint:</i> <b>POINT ( </b><i>Coordinate</i> <b>)</b>
 *
 * <i>WKTLineString:</i> <b>LINESTRING</b> <i>CoordinateSequence</i>
 *
 * <i>WKTLinearRing:</i> <b>LINEARRING</b> <i>CoordinateSequence</i>
 *
 * <i>WKTPolygon:</i> <b>POLYGON</b> <i>CoordinateSequenceList</i>
 *
 * <i>WKTMultiPoint:</i> <b>MULTIPOINT</b> <i>CoordinateSingletonList</i>
 *
 * <i>WKTMultiLineString:</i> <b>MULTILINESTRING</b> <i>CoordinateSequenceList</i>
 *
 * <i>WKTMultiPolygon:</i>
 *         <b>MULTIPOLYGON (</b> <i>CoordinateSequenceList {</i> , <i>CoordinateSequenceList }</i> <b>)</b>
 *
 * <i>WKTGeometryCollection: </i>
 *         <b>GEOMETRYCOLLECTION (</b> <i>WKTGeometry {</i> , <i>WKTGeometry }</i> <b>)</b>
 *
 * <i>CoordinateSingletonList:</i>
 *         <b>(</b> <i>CoordinateSingleton {</i> <b>,</b> <i>CoordinateSingleton }</i> <b>)</b>
 *         | <b>EMPTY</b>
 *         
 * <i>CoordinateSingleton:</i>
 *         <b>(</b> <i>Coordinate <b>)</b>
 *         | <b>EMPTY</b>
 *
 * <i>CoordinateSequenceList:</i>
 *         <b>(</b> <i>CoordinateSequence {</i> <b>,</b> <i>CoordinateSequence }</i> <b>)</b>
 *         | <b>EMPTY</b>
 *
 * <i>CoordinateSequence:</i>
 *         <b>(</b> <i>Coordinate {</i> , <i>Coordinate }</i> <b>)</b>
 *         | <b>EMPTY</b>
 *
 * <i>Coordinate:
 *         Number Number Number<sub>opt</sub></i>
 *
 * <i>Number:</i> A Java-style floating-point number (including <tt>NaN</tt>, with arbitrary case)
 *
 * </pre></blockquote>
 *
 *
 *@version 1.7
 */
public class WKTReader {

    private static final String EMPTY = "EMPTY";

    private static final String COMMA = ",";

    private static final String L_PAREN = "(";

    private static final String R_PAREN = ")";

    private static final String NAN_SYMBOL = "NaN";

    private GeometryFactory geometryFactory;

    private PrecisionModel precisionModel;

    private StreamTokenizer tokenizer;

    /**
	 * Creates a reader that creates objects using the default {@link GeometryFactory}.
	 */
    public WKTReader() {
        this(new GeometryFactory());
    }

    /**
	 *  Creates a reader that creates objects using the given
	 *  {@link GeometryFactory}.
	 *
	 *@param  geometryFactory  the factory used to create <code>Geometry</code>s.
	 */
    public WKTReader(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        precisionModel = geometryFactory.getPrecisionModel();
    }

    /**
	 * Reads a Well-Known Text representation of a {@link Geometry}
	 * from a {@link String}.
	 *
	 * @param wellKnownText
	 *            one or more <Geometry Tagged Text>strings (see the OpenGIS
	 *            Simple Features Specification) separated by whitespace
	 * @return a <code>Geometry</code> specified by <code>wellKnownText</code>
	 * @throws ParseException
	 *             if a parsing problem occurs
	 */
    public Geometry read(String wellKnownText) throws ParseException {
        StringReader reader = new StringReader(wellKnownText);
        try {
            return read(reader);
        } finally {
            reader.close();
        }
    }

    /**
	 * Reads a Well-Known Text representation of a {@link Geometry}
	 * from a {@link Reader}.
	 *
	 *@param  reader           a Reader which will return a <Geometry Tagged Text>
	 *      string (see the OpenGIS Simple Features Specification)
	 *@return                  a <code>Geometry</code> read from <code>reader</code>
	 *@throws  ParseException  if a parsing problem occurs
	 */
    public Geometry read(Reader reader) throws ParseException {
        tokenizer = new StreamTokenizer(reader);
        tokenizer.resetSyntax();
        tokenizer.wordChars('a', 'z');
        tokenizer.wordChars('A', 'Z');
        tokenizer.wordChars(128 + 32, 255);
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('-', '-');
        tokenizer.wordChars('+', '+');
        tokenizer.wordChars('.', '.');
        tokenizer.whitespaceChars(0, ' ');
        tokenizer.commentChar('#');
        try {
            return readGeometryTaggedText();
        } catch (IOException e) {
            throw new ParseException(e.toString());
        }
    }

    /**
	 * Returns the next array of <code>Coordinate</code>s in the stream.
	 *
	 *@return                  the next array of <code>Coordinate</code>s in the
	 *      stream, or an empty array if EMPTY is the next element returned by
	 *      the stream.
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
    private Coordinate[] getCoordinates() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return new Coordinate[] {};
        }
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add(getPreciseCoordinate());
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            coordinates.add(getPreciseCoordinate());
            nextToken = getNextCloserOrComma();
        }
        Coordinate[] array = new Coordinate[coordinates.size()];
        return coordinates.toArray(array);
    }

    private Coordinate[] getCoordinatesNoLeftParen() throws IOException, ParseException {
        String nextToken = null;
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add(getPreciseCoordinate());
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            coordinates.add(getPreciseCoordinate());
            nextToken = getNextCloserOrComma();
        }
        Coordinate[] array = new Coordinate[coordinates.size()];
        return coordinates.toArray(array);
    }

    private Coordinate getPreciseCoordinate() throws IOException, ParseException {
        Coordinate coord = new Coordinate();
        coord.x = getNextNumber();
        coord.y = getNextNumber();
        if (isNumberNext()) {
            coord.z = getNextNumber();
        }
        precisionModel.makePrecise(coord);
        return coord;
    }

    private boolean isNumberNext() throws IOException {
        int type = tokenizer.nextToken();
        tokenizer.pushBack();
        return type == StreamTokenizer.TT_WORD;
    }

    /**
	 * Parses the next number in the stream.
	 * Numbers with exponents are handled.
	 * <tt>NaN</tt> values are handled correctly, and
	 * the case of the "NaN" symbol is not significant. 
	 *@return                  the next number in the stream
	 *@throws  ParseException  if the next token is not a valid number
	 *@throws  IOException     if an I/O error occurs
	 */
    private double getNextNumber() throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch(type) {
            case StreamTokenizer.TT_WORD:
                {
                    if (tokenizer.sval.equalsIgnoreCase(NAN_SYMBOL)) {
                        return Double.NaN;
                    }
                    try {
                        return Double.parseDouble(tokenizer.sval);
                    } catch (NumberFormatException ex) {
                        throw new ParseException("Invalid number: " + tokenizer.sval);
                    }
                }
        }
        parseError("number");
        return 0.0;
    }

    /**
	 *  Returns the next EMPTY or L_PAREN in the stream as uppercase text.
	 *@return                  the next EMPTY or L_PAREN in the stream as uppercase
	 *      text.
	 *@throws  ParseException  if the next token is not EMPTY or L_PAREN
	 *@throws  IOException     if an I/O error occurs
	 */
    private String getNextEmptyOrOpener() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(EMPTY) || nextWord.equals(L_PAREN)) {
            return nextWord;
        }
        parseError(EMPTY + " or " + L_PAREN);
        return null;
    }

    /**
	 *  Returns the next R_PAREN or COMMA in the stream.
	 *@return                  the next R_PAREN or COMMA in the stream
	 *@throws  ParseException  if the next token is not R_PAREN or COMMA
	 *@throws  IOException     if an I/O error occurs
	 */
    private String getNextCloserOrComma() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(COMMA) || nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(COMMA + " or " + R_PAREN);
        return null;
    }

    /**
	 *  Returns the next R_PAREN in the stream.
	 *@return                  the next R_PAREN in the stream
	 *@throws  ParseException  if the next token is not R_PAREN
	 *@throws  IOException     if an I/O error occurs
	 */
    private String getNextCloser() throws IOException, ParseException {
        String nextWord = getNextWord();
        if (nextWord.equals(R_PAREN)) {
            return nextWord;
        }
        parseError(R_PAREN);
        return null;
    }

    /**
	 *  Returns the next word in the stream.
	 *@return                  the next word in the stream as uppercase text
	 *@throws  ParseException  if the next token is not a word
	 *@throws  IOException     if an I/O error occurs
	 */
    private String getNextWord() throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch(type) {
            case StreamTokenizer.TT_WORD:
                String word = tokenizer.sval;
                if (word.equalsIgnoreCase(EMPTY)) return EMPTY;
                return word;
            case '(':
                return L_PAREN;
            case ')':
                return R_PAREN;
            case ',':
                return COMMA;
        }
        parseError("word");
        return null;
    }

    /**
	 *  Returns the next word in the stream.
	 *@return                  the next word in the stream as uppercase text
	 *@throws  ParseException  if the next token is not a word
	 *@throws  IOException     if an I/O error occurs
	 */
    private String lookaheadWord() throws IOException, ParseException {
        String nextWord = getNextWord();
        tokenizer.pushBack();
        return nextWord;
    }

    /**
	 * Throws a formatted ParseException for the current token.
	 *
	 * @param expected a description of what was expected
	 * @throws ParseException
	 */
    private void parseError(String expected) throws ParseException {
        if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) Assert.shouldNeverReachHere("Unexpected NUMBER token");
        if (tokenizer.ttype == StreamTokenizer.TT_EOL) Assert.shouldNeverReachHere("Unexpected EOL token");
        String tokenStr = tokenString();
        throw new ParseException("Expected " + expected + " but found " + tokenStr);
    }

    /**
	 * Gets a description of the current token
	 *
	 * @return a description of the current token
	 */
    private String tokenString() {
        switch(tokenizer.ttype) {
            case StreamTokenizer.TT_NUMBER:
                return "<NUMBER>";
            case StreamTokenizer.TT_EOL:
                return "End-of-Line";
            case StreamTokenizer.TT_EOF:
                return "End-of-Stream";
            case StreamTokenizer.TT_WORD:
                return "'" + tokenizer.sval + "'";
        }
        return "'" + (char) tokenizer.ttype + "'";
    }

    /**
	 *  Creates a <code>Geometry</code> using the next token in the stream.
	 *@return                  a <code>Geometry</code> specified by the next token
	 *      in the stream
	 *@throws  ParseException  if the coordinates used to create a <code>Polygon</code>
	 *      shell and holes do not form closed linestrings, or if an unexpected
	 *      token was encountered
	 *@throws  IOException     if an I/O error occurs
	 */
    private Geometry readGeometryTaggedText() throws IOException, ParseException {
        String type = null;
        try {
            type = getNextWord();
        } catch (IOException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
        if (type.equalsIgnoreCase("POINT")) {
            return readPointText();
        } else if (type.equalsIgnoreCase("LINESTRING")) {
            return readLineStringText();
        } else if (type.equalsIgnoreCase("LINEARRING")) {
            return readLinearRingText();
        } else if (type.equalsIgnoreCase("POLYGON")) {
            return readPolygonText();
        } else if (type.equalsIgnoreCase("MULTIPOINT")) {
            return readMultiPointText();
        } else if (type.equalsIgnoreCase("MULTILINESTRING")) {
            return readMultiLineStringText();
        } else if (type.equalsIgnoreCase("MULTIPOLYGON")) {
            return readMultiPolygonText();
        } else if (type.equalsIgnoreCase("GEOMETRYCOLLECTION")) {
            return readGeometryCollectionText();
        }
        throw new ParseException("Unknown geometry type: " + type);
    }

    /**
	 *  Creates a <code>Point</code> using the next token in the stream.
	 *@return                  a <code>Point</code> specified by the next token in
	 *      the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
    private Point readPointText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createPoint((Coordinate) null);
        }
        Point point = geometryFactory.createPoint(getPreciseCoordinate());
        getNextCloser();
        return point;
    }

    /**
	 *  Creates a <code>LineString</code> using the next token in the stream.
	 *@return                  a <code>LineString</code> specified by the next
	 *      token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
    private LineString readLineStringText() throws IOException, ParseException {
        return geometryFactory.createLineString(getCoordinates());
    }

    /**
	 *  Creates a <code>LinearRing</code> using the next token in the stream.
	 *@return                  a <code>LinearRing</code> specified by the next
	 *      token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if the coordinates used to create the <code>LinearRing</code>
	 *      do not form a closed linestring, or if an unexpected token was
	 *      encountered
	 */
    private LinearRing readLinearRingText() throws IOException, ParseException {
        return geometryFactory.createLinearRing(getCoordinates());
    }

    private static final boolean ALLOW_OLD_JTS_MULTIPOINT_SYNTAX = true;

    /**
	 *  Creates a <code>MultiPoint</code> using the next tokens in the stream.
	 *@return                  a <code>MultiPoint</code> specified by the next
	 *      token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
    private MultiPoint readMultiPointText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createMultiPoint(new Point[0]);
        }
        if (ALLOW_OLD_JTS_MULTIPOINT_SYNTAX) {
            String nextWord = lookaheadWord();
            if (nextWord != L_PAREN) {
                return geometryFactory.createMultiPoint(toPoints(getCoordinatesNoLeftParen()));
            }
        }
        ArrayList<Point> points = new ArrayList<Point>();
        Point point = readPointText();
        points.add(point);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            point = readPointText();
            points.add(point);
            nextToken = getNextCloserOrComma();
        }
        Point[] array = new Point[points.size()];
        return geometryFactory.createMultiPoint(points.toArray(array));
    }

    /**
	 *  Creates an array of <code>Point</code>s having the given <code>Coordinate</code>
	 *  s.
	 *
	 *@param  coordinates  the <code>Coordinate</code>s with which to create the
	 *      <code>Point</code>s
	 *@return              <code>Point</code>s created using this <code>WKTReader</code>
	 *      s <code>GeometryFactory</code>
	 */
    private Point[] toPoints(Coordinate[] coordinates) {
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < coordinates.length; i++) {
            points.add(geometryFactory.createPoint(coordinates[i]));
        }
        return points.toArray(new Point[] {});
    }

    /**
	 *  Creates a <code>Polygon</code> using the next token in the stream.
	 *@return                  a <code>Polygon</code> specified by the next token
	 *      in the stream
	 *@throws  ParseException  if the coordinates used to create the <code>Polygon</code>
	 *      shell and holes do not form closed linestrings, or if an unexpected
	 *      token was encountered.
	 *@throws  IOException     if an I/O error occurs
	 */
    private Polygon readPolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[] {}), new LinearRing[] {});
        }
        ArrayList<LinearRing> holes = new ArrayList<LinearRing>();
        LinearRing shell = readLinearRingText();
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            LinearRing hole = readLinearRingText();
            holes.add(hole);
            nextToken = getNextCloserOrComma();
        }
        LinearRing[] array = new LinearRing[holes.size()];
        return geometryFactory.createPolygon(shell, holes.toArray(array));
    }

    /**
	 *  Creates a <code>MultiLineString</code> using the next token in the stream.
	 *@return                  a <code>MultiLineString</code> specified by the
	 *      next token in the stream
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
    private com.vividsolutions.jts.geom.MultiLineString readMultiLineStringText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createMultiLineString(new LineString[] {});
        }
        ArrayList<LineString> lineStrings = new ArrayList<LineString>();
        LineString lineString = readLineStringText();
        lineStrings.add(lineString);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            lineString = readLineStringText();
            lineStrings.add(lineString);
            nextToken = getNextCloserOrComma();
        }
        LineString[] array = new LineString[lineStrings.size()];
        return geometryFactory.createMultiLineString(lineStrings.toArray(array));
    }

    /**
	 *  Creates a <code>MultiPolygon</code> using the next token in the stream.
	 *@return                  a <code>MultiPolygon</code> specified by the next
	 *      token in the stream, or if if the coordinates used to create the
	 *      <code>Polygon</code> shells and holes do not form closed linestrings.
	 *@throws  IOException     if an I/O error occurs
	 *@throws  ParseException  if an unexpected token was encountered
	 */
    private MultiPolygon readMultiPolygonText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createMultiPolygon(new Polygon[] {});
        }
        ArrayList<Polygon> polygons = new ArrayList<Polygon>();
        Polygon polygon = readPolygonText();
        polygons.add(polygon);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            polygon = readPolygonText();
            polygons.add(polygon);
            nextToken = getNextCloserOrComma();
        }
        Polygon[] array = new Polygon[polygons.size()];
        return geometryFactory.createMultiPolygon(polygons.toArray(array));
    }

    /**
	 *  Creates a <code>GeometryCollection</code> using the next token in the
	 *  stream.
	 *@return                  a <code>GeometryCollection</code> specified by the
	 *      next token in the stream
	 *@throws  ParseException  if the coordinates used to create a <code>Polygon</code>
	 *      shell and holes do not form closed linestrings, or if an unexpected
	 *      token was encountered
	 *@throws  IOException     if an I/O error occurs
	 */
    private GeometryCollection readGeometryCollectionText() throws IOException, ParseException {
        String nextToken = getNextEmptyOrOpener();
        if (nextToken.equals(EMPTY)) {
            return geometryFactory.createGeometryCollection(new Geometry[] {});
        }
        ArrayList<Geometry> geometries = new ArrayList<Geometry>();
        Geometry geometry = readGeometryTaggedText();
        geometries.add(geometry);
        nextToken = getNextCloserOrComma();
        while (nextToken.equals(COMMA)) {
            geometry = readGeometryTaggedText();
            geometries.add(geometry);
            nextToken = getNextCloserOrComma();
        }
        Geometry[] array = new Geometry[geometries.size()];
        return geometryFactory.createGeometryCollection(geometries.toArray(array));
    }
}
