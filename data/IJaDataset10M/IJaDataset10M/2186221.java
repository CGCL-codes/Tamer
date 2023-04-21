package com.ashs.jump.plugin;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.util.Assert;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;

public class CGDReader {

    private GeometryFactory geometryFactory;

    private PrecisionModel precisionModel;

    private String ID;

    public CGDReader() {
        this(new GeometryFactory());
    }

    public CGDReader(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        precisionModel = geometryFactory.getPrecisionModel();
    }

    public Geometry read(String wellKnownText) throws ParseException {
        StringReader reader = new StringReader(wellKnownText);
        try {
            return read(reader);
        } finally {
            reader.close();
        }
    }

    public Geometry read(Reader reader) throws ParseException {
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        try {
            return readGeometryTaggedText(tokenizer);
        } catch (IOException e) {
            throw new ParseException(e.toString());
        }
    }

    private Coordinate[] getCoordinates(StreamTokenizer tokenizer) throws IOException, ParseException {
        ArrayList coordinates = new ArrayList();
        coordinates.add(getPreciseCoordinate(tokenizer));
        while (isNumberNext(tokenizer)) {
            coordinates.add(getPreciseCoordinate(tokenizer));
        }
        Coordinate[] array = new Coordinate[coordinates.size()];
        return (Coordinate[]) coordinates.toArray(array);
    }

    private Coordinate getPreciseCoordinate(StreamTokenizer tokenizer) throws IOException, ParseException {
        Coordinate coord = new Coordinate();
        coord.x = getNextNumber(tokenizer);
        coord.y = getNextNumber(tokenizer);
        if (isNumberNext(tokenizer)) {
            coord.z = getNextNumber(tokenizer);
        }
        precisionModel.makePrecise(coord);
        return coord;
    }

    private boolean isNumberNext(StreamTokenizer tokenizer) throws IOException {
        try {
            return tokenizer.nextToken() == StreamTokenizer.TT_NUMBER;
        } finally {
            tokenizer.pushBack();
        }
    }

    private double getNextNumber(StreamTokenizer tokenizer) throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch(type) {
            case StreamTokenizer.TT_EOF:
                throw new ParseException("Expected number but encountered end of stream");
            case StreamTokenizer.TT_EOL:
                throw new ParseException("Expected number but encountered end of line");
            case StreamTokenizer.TT_NUMBER:
                return tokenizer.nval;
            case StreamTokenizer.TT_WORD:
                throw new ParseException("Expected number but encountered word: " + tokenizer.sval);
        }
        Assert.shouldNeverReachHere("Encountered unexpected StreamTokenizer type: " + type);
        return 0;
    }

    private String getID(StreamTokenizer tokenizer) throws IOException, ParseException {
        if (!(isNumberNext(tokenizer))) {
            String ID = getNextWord(tokenizer);
            if (ID.toUpperCase().equals("ID")) {
                return getNextWord(tokenizer);
            } else {
                throw new ParseException("Expected 'ID' but encountered '" + ID + "'");
            }
        }
        return "";
    }

    private String getNextWord(StreamTokenizer tokenizer) throws IOException, ParseException {
        int type = tokenizer.nextToken();
        switch(type) {
            case StreamTokenizer.TT_EOF:
                throw new ParseException("Expected word but encountered end of stream");
            case StreamTokenizer.TT_EOL:
                throw new ParseException("Expected word but encountered end of line");
            case StreamTokenizer.TT_NUMBER:
                throw new ParseException("Expected word but encountered number: " + tokenizer.nval);
            case StreamTokenizer.TT_WORD:
                return tokenizer.sval.toUpperCase();
        }
        Assert.shouldNeverReachHere("Encountered unexpected StreamTokenizer type: " + type);
        return null;
    }

    private Geometry readGeometryTaggedText(StreamTokenizer tokenizer) throws IOException, ParseException {
        String type = getNextWord(tokenizer);
        if (type.equals("POLY")) {
            return readPoly(tokenizer);
        }
        throw new ParseException("Unknown type: " + type);
    }

    private Geometry readPoly(StreamTokenizer tokenizer) throws IOException, ParseException {
        Coordinate[] array = getCoordinates(tokenizer);
        Coordinate firstCoord = array[0];
        Coordinate lastCoord = array[array.length - 1];
        if (firstCoord.equals2D(lastCoord)) {
            return geometryFactory.createLinearRing(array);
        } else {
            return geometryFactory.createLineString(array);
        }
    }
}
