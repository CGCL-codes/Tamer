package com.ohrasys.cad.gds;

import java.io.*;
import java.util.*;

/**
 * Represents GDSII ANGLE record.
 *
 * <p>Please see <a href='http://jgds.sf.net/gdsii.pdf'>The GDSII techincal
 * reference</a> for a more complete discussion of the GDSII stream syntax.</p>
 *
 * @author   $Author: tvaline $
 * @version  $Revision: 1.9 $
 * @since    1.5
 */
public class GDSAngleRecord extends GDSRecord {

    /** The angle */
    private double angle;

    /**
   * Creates a new GDSAngleRecord object based on an existing record.
   *
   * @param   rec  The base record.
   *
   * @throws  GDSRecordException  If the record is not a valid ANGLE record.
   */
    public GDSAngleRecord(GDSRecord rec) throws GDSRecordException {
        this(rec.getLength(), rec.getRectype(), rec.getDattype(), rec.getData());
    }

    /**
   * Creates a new GDSAngleRecord object.
   *
   * @param   angle  The angle.
   *
   * @throws  GDSRecordException  If an error occurs.
   */
    public GDSAngleRecord(double angle) throws GDSRecordException {
        setAngle(angle);
        this.rectype = ANGLE;
        this.dattype = DOUBLE_TYPE;
        this.length = (short) 12;
    }

    /**
   * Creates a new GDSAngleRecord object.
   *
   * @param   length   The record length in bytes.
   * @param   rectype  The record type.
   * @param   dattype  The data type.
   * @param   data     The record data.
   *
   * @throws  GDSRecordException  If the record is malformed.
   */
    public GDSAngleRecord(short length, byte rectype, byte dattype, byte data[]) throws GDSRecordException {
        super(length, rectype, dattype, data);
        validateDoubleRec(ANGLE);
        this.angle = GDSSpecificDataConverter.toDouble(data);
    }

    /**
   * Gets the angle.
   *
   * @return  The current angle.
   */
    public double getAngle() {
        return this.angle;
    }

    /**
   * Sets the angle.
   *
   * @param  angle  The new angle.
   */
    public void setAngle(double angle) {
        this.data = GDSSpecificDataConverter.writeJavaDouble(angle);
        this.angle = angle;
    }

    /**
   * Returns a description of the record.
   *
   * @return  A string representation of the record.
   */
    public String toString() {
        return GDSStringUtil.sprintf(i18n.getString(i18n.i18n_ANGLE_TOSTRING), angle);
    }
}
