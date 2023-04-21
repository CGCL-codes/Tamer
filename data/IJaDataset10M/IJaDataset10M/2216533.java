package DE.FhG.IGD.earth.model.input.shapefile;

import java.io.IOException;

/**
 * This is a skeleton of the MultiPoint record type.  No methods
 * are implemented.
 *
 * <H2>To Do</H2>
 * <UL>
 * <LI> Implement the methods of this class.</LI>
 * </UL>
 *
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author      : Werner Beutel
 * @version     : 1.0
 */
public class ESRIMultiPointRecord extends ESRIRecord {

    /**
     * Constructor skeleton.
     *
     * @param b the buffer
     * @param off the offset
     */
    public ESRIMultiPointRecord(byte b[], int off) {
    }

    /**
     * Gets this record's bounding box.
     *
     * @return a bounding box
     */
    public ESRIBoundingBox getBoundingBox() {
        return null;
    }

    /**
     * Gets this record's shape type as an int.  Shape types
     * are enumerated on the ShapeUtils class.
     *
     * @return the shape type as an int
     */
    public int getShapeType() {
        return SHAPE_TYPE_MULTIPOINT;
    }

    /**
     * Yields the length of this record's data portion.
     *
     * @return number of bytes equal to the size of this record's data
     */
    public int getRecordLength() {
        Debug.output("HACK: ESIRMultiPointRecord.getRecordLength: NYI");
        return 0;
    }

    /**
     * Writes this multipoint record to the given buffer at the given offset.
     *
     * @param b the buffer
     * @param off the offset
     * @return the number of bytes written
     */
    public int write(byte[] b, int off) {
        return 0;
    }
}
