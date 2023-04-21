package org.fudaa.ctulu.gis.shapefile;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.memoire.fu.Fu;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluLibString;

/**
 * Class to represent the header of a Dbase III file. Creation date: (5/15/2001 5:15:30 PM)
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/tags/2.2-RC3/plugin/shapefile/src/org/geotools/data/shapefile/dbf/DbaseFileHeader.java $
 */
public class DbaseFileHeader {

    private static final int FILE_DESCRIPTOR_SIZE = 32;

    private static final byte MAGIC = 0x03;

    private static final int MINIMUM_HEADER = 33;

    private Date date_ = new Date();

    private int recordCnt_;

    private int fieldCnt_;

    private int recordLength_ = 1;

    private int headerLength_ = -1;

    private int largestFieldSize_;

    /**
   * Class for holding the information assicated with a record.
   */
    static class DbaseField {

        String fieldName_;

        char fieldType_;

        int fieldDataAddress_;

        int fieldLength_;

        int decimalCount_;
    }

    private DbaseField[] fields_ = new DbaseField[0];

    private void read(final ByteBuffer _buffer, final ReadableByteChannel _channel) throws IOException {
        while (_buffer.remaining() > 0) {
            if (_channel.read(_buffer) == -1) {
                throw new EOFException("Premature end of file");
            }
        }
    }

    /**
   * Determine the most appropriate Java Class for representing the data in the field.
   * 
   * <PRE>
   * 
   * All packages are java.lang unless otherwise specified. C (Character) -> String N (Numeric) -> Integer or Double
   * (depends on field's decimal count) F (Floating) -> Double L (Logical) -> Boolean D (Date) -> java.util.Date Unknown ->
   * String
   * 
   * </PRE>
   * 
   * @param _i The index of the field, from 0 to <CODE>getNumFields() - 1</CODE> .
   * @return A Class which closely represents the dbase field type.
   */
    public Class getFieldClass(final int _i) {
        Class typeClass = null;
        switch(fields_[_i].fieldType_) {
            case 'C':
                typeClass = String.class;
                break;
            case 'N':
                if (fields_[_i].decimalCount_ == 0) {
                    if (fields_[_i].fieldLength_ < 10) {
                        typeClass = Integer.class;
                    } else {
                        typeClass = Long.class;
                    }
                } else {
                    typeClass = Double.class;
                }
                break;
            case 'F':
                typeClass = Double.class;
                break;
            case 'L':
                typeClass = Boolean.class;
                break;
            case 'D':
                typeClass = Date.class;
                break;
            default:
                typeClass = String.class;
                break;
        }
        return typeClass;
    }

    /**
   * Add a column to this DbaseFileHeader. The type is one of (C N L or D) character, number, logical(true/false), or
   * date. The Field length is the total length in bytes reserved for this column. The decimal count only applies to
   * numbers(N), and floating point values (F), and refers to the number of characters to reserve after the decimal
   * point. <B>Don't expect miracles from this...</B>
   * 
   * <PRE>
   * 
   * Field Type MaxLength ---------- --------- C 254 D 8 F 20 N 18
   * 
   * </PRE>
   * 
   * @param _inFieldName The name of the new field, must be less than 10 characters or it gets truncated.
   * @param _inFieldType A character representing the dBase field, ( see above ). Case insensitive.
   * @param _inFieldLength The length of the field, in bytes ( see above )
   * @param _inDecimalCount For numeric fields, the number of decimal places to track.
   * @throws DbaseFileException If the type is not recognized.
   */
    public void addColumn(final String _inFieldName, final char _inFieldType, final int _inFieldLength, final int _inDecimalCount) throws DbaseFileException {
        if (_inFieldLength <= 0) {
            throw new DbaseFileException("field length <= 0");
        }
        if (fields_ == null) {
            fields_ = new DbaseField[0];
        }
        int tempLength = 1;
        final DbaseField[] tempFieldDescriptors = new DbaseField[fields_.length + 1];
        for (int i = 0; i < fields_.length; i++) {
            fields_[i].fieldDataAddress_ = tempLength;
            tempLength = tempLength + fields_[i].fieldLength_;
            tempFieldDescriptors[i] = fields_[i];
        }
        tempFieldDescriptors[fields_.length] = new DbaseField();
        tempFieldDescriptors[fields_.length].fieldLength_ = _inFieldLength;
        tempFieldDescriptors[fields_.length].decimalCount_ = _inDecimalCount;
        tempFieldDescriptors[fields_.length].fieldDataAddress_ = tempLength;
        String tempFieldName = _inFieldName;
        if (tempFieldName == null) {
            tempFieldName = "NoName";
        }
        if (tempFieldName.length() > 10) {
            tempFieldName = tempFieldName.substring(0, 10);
            warn("FieldName " + _inFieldName + " is longer than 10 characters, truncating to " + tempFieldName);
        }
        tempFieldDescriptors[fields_.length].fieldName_ = tempFieldName;
        if ((_inFieldType == 'C') || (_inFieldType == 'c')) {
            tempFieldDescriptors[fields_.length].fieldType_ = 'C';
            if (_inFieldLength > 254) {
                warn(getFLength() + _inFieldName + getSetTo() + _inFieldLength + " Which is longer than 254, not consistent with dbase III");
            }
        } else if ((_inFieldType == 'S') || (_inFieldType == 's')) {
            tempFieldDescriptors[fields_.length].fieldType_ = 'C';
            warn("Field type for " + _inFieldName + " set to S which is flat out wrong people!, I am setting this to C, in the hopes you meant character.");
            if (_inFieldLength > 254) {
                warn(getFLength() + _inFieldName + getSetTo() + _inFieldLength + " Which is longer than 254, not consistent with dbase III");
            }
            tempFieldDescriptors[fields_.length].fieldLength_ = 8;
        } else if ((_inFieldType == 'D') || (_inFieldType == 'd')) {
            tempFieldDescriptors[fields_.length].fieldType_ = 'D';
            if (_inFieldLength != 8) {
                warn(getFLength() + _inFieldName + getSetTo() + _inFieldLength + " Setting to 8 digets YYYYMMDD");
            }
            tempFieldDescriptors[fields_.length].fieldLength_ = 8;
        } else if ((_inFieldType == 'F') || (_inFieldType == 'f')) {
            tempFieldDescriptors[fields_.length].fieldType_ = 'F';
            if (_inFieldLength > 20) {
                warn(getFLength() + _inFieldName + getSetTo() + _inFieldLength + " Preserving length, but should be set to Max of 20 not valid for dbase IV, and UP specification, not present in dbaseIII.");
            }
        } else if ((_inFieldType == 'N') || (_inFieldType == 'n')) {
            tempFieldDescriptors[fields_.length].fieldType_ = 'N';
            if (_inFieldLength > 18) {
                warn(getFLength() + _inFieldName + getSetTo() + _inFieldLength + " Preserving length, but should be set to Max of 18 for dbase III specification.");
            }
            if (_inDecimalCount < 0) {
                warn("Field Decimal Position for " + _inFieldName + getSetTo() + _inDecimalCount + " Setting to 0 no decimal data will be saved.");
                tempFieldDescriptors[fields_.length].decimalCount_ = 0;
            }
            if (_inDecimalCount > _inFieldLength - 1) {
                warn("Field Decimal Position for " + _inFieldName + getSetTo() + _inDecimalCount + " Setting to " + (_inFieldLength - 1) + " no non decimal data will be saved.");
                tempFieldDescriptors[fields_.length].decimalCount_ = _inFieldLength - 1;
            }
        } else if ((_inFieldType == 'L') || (_inFieldType == 'l')) {
            tempFieldDescriptors[fields_.length].fieldType_ = 'L';
            if (_inFieldLength != 1) {
                warn(getFLength() + _inFieldName + getSetTo() + _inFieldLength + " Setting to length of 1 for logical fields.");
            }
            tempFieldDescriptors[fields_.length].fieldLength_ = 1;
        } else {
            throw new DbaseFileException("Undefined field type " + _inFieldType + " For column " + _inFieldName);
        }
        tempLength = tempLength + tempFieldDescriptors[fields_.length].fieldLength_;
        fields_ = tempFieldDescriptors;
        fieldCnt_ = fields_.length;
        headerLength_ = MINIMUM_HEADER + 32 * fields_.length;
        recordLength_ = tempLength;
    }

    private String getFLength() {
        return "Field Length for ";
    }

    private String getSetTo() {
        return " set to ";
    }

    /**
   * Remove a column from this DbaseFileHeader.
   * 
   * @todo This is really ugly, don't know who wrote it, but it needs fixin...
   * @param _inFieldName The name of the field, will ignore case and trim.
   * @return index of the removed column, -1 if no found
   */
    public int removeColumn(final String _inFieldName) {
        int retCol = -1;
        int tempLength = 1;
        final DbaseField[] tempFieldDescriptors = new DbaseField[fields_.length - 1];
        for (int i = 0, j = 0; i < fields_.length; i++) {
            if (!_inFieldName.equalsIgnoreCase(fields_[i].fieldName_.trim())) {
                if (i == j && i == fields_.length - 1) {
                    System.err.println("Could not find a field named '" + _inFieldName + "' for removal");
                    return retCol;
                }
                tempFieldDescriptors[j] = fields_[i];
                tempFieldDescriptors[j].fieldDataAddress_ = tempLength;
                tempLength += tempFieldDescriptors[j].fieldLength_;
                j++;
            } else {
                retCol = i;
            }
        }
        fields_ = tempFieldDescriptors;
        headerLength_ = 33 + 32 * fields_.length;
        recordLength_ = tempLength;
        return retCol;
    }

    /**
   * @todo addProgessListener handling
   */
    private void warn(final String _inWarn) {
        if (Fu.DEBUG && FuLog.isDebug()) {
            FuLog.debug("CSI: " + _inWarn);
        }
    }

    /**
   * Returns the field length in bytes.
   * 
   * @param _inIndex The field index.
   * @return The length in bytes.
   */
    public int getFieldLength(final int _inIndex) {
        return fields_[_inIndex].fieldLength_;
    }

    /**
   * Get the decimal count of this field.
   * 
   * @param _inIndex The field index.
   * @return The decimal count.
   */
    public int getFieldDecimalCount(final int _inIndex) {
        return fields_[_inIndex].decimalCount_;
    }

    /**
   * Get the field name.
   * 
   * @param _inIndex The field index.
   * @return The name of the field.
   */
    public String getFieldName(final int _inIndex) {
        return fields_[_inIndex].fieldName_;
    }

    /**
   * Get the character class of the field.
   * 
   * @param _inIndex The field index.
   * @return The dbase character representing this field.
   */
    public char getFieldType(final int _inIndex) {
        return fields_[_inIndex].fieldType_;
    }

    /**
   * Get the date this file was last updated.
   * 
   * @return The Date last modified.
   */
    public Date getLastUpdateDate() {
        return date_;
    }

    /**
   * Return the number of fields in the records.
   * 
   * @return The number of fields in this table.
   */
    public int getNumFields() {
        return fields_.length;
    }

    /**
   * Return the number of records in the file.
   * 
   * @return The number of records in this table.
   */
    public int getNumRecords() {
        return recordCnt_;
    }

    /**
   * Get the length of the records in bytes.
   * 
   * @return The number of bytes per record.
   */
    public int getRecordLength() {
        return recordLength_;
    }

    /**
   * Get the length of the header.
   * 
   * @return The length of the header in bytes.
   */
    public int getHeaderLength() {
        return headerLength_;
    }

    /**
   * Read the header data from the DBF file.
   * 
   * @param _channel A readable byte channel. If you have an InputStream you need to use, you can call
   *          java.nio.Channels.getChannel(InputStream in).
   * @throws IOException If errors occur while reading.
   */
    public void readHeader(final ReadableByteChannel _channel) throws IOException {
        ByteBuffer in = ByteBuffer.allocateDirect(1024);
        in.order(ByteOrder.LITTLE_ENDIAN);
        in.limit(10);
        read(in, _channel);
        in.position(0);
        final byte magic = in.get();
        if (magic != MAGIC) {
            throw new IOException("Unsupported DBF file Type " + Integer.toHexString(magic));
        }
        int tempUpdateYear = in.get();
        final int tempUpdateMonth = in.get();
        final int tempUpdateDay = in.get();
        if (tempUpdateYear > 90) {
            tempUpdateYear = tempUpdateYear + 1900;
        } else {
            tempUpdateYear = tempUpdateYear + 2000;
        }
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, tempUpdateYear);
        c.set(Calendar.MONTH, tempUpdateMonth - 1);
        c.set(Calendar.DATE, tempUpdateDay);
        date_ = c.getTime();
        recordCnt_ = in.getInt();
        headerLength_ = (in.get() & 0xff) | ((in.get() & 0xff) << 8);
        if (headerLength_ > in.capacity()) {
            in = ByteBuffer.allocateDirect(headerLength_ - 10);
        }
        in.limit(headerLength_ - 10);
        in.position(0);
        read(in, _channel);
        in.position(0);
        recordLength_ = (in.get() & 0xff) | ((in.get() & 0xff) << 8);
        in.position(in.position() + 20);
        fieldCnt_ = (headerLength_ - FILE_DESCRIPTOR_SIZE - 1) / FILE_DESCRIPTOR_SIZE;
        final List lfields = new ArrayList();
        for (int i = 0; i < fieldCnt_; i++) {
            final DbaseField field = new DbaseField();
            final byte[] buffer = new byte[11];
            in.get(buffer);
            String name = new String(buffer);
            final int nullPoint = name.indexOf(0);
            if (nullPoint != -1) {
                name = name.substring(0, nullPoint);
            }
            field.fieldName_ = name.trim();
            field.fieldType_ = (char) in.get();
            field.fieldDataAddress_ = in.getInt();
            int length = in.get();
            if (length < 0) {
                length = length + 256;
            }
            field.fieldLength_ = length;
            if (length > largestFieldSize_) {
                largestFieldSize_ = length;
            }
            field.decimalCount_ = in.get();
            in.position(in.position() + 14);
            if (field.fieldLength_ > 0) {
                lfields.add(field);
            }
        }
        in.position(in.position() + 1);
        fields_ = new DbaseField[lfields.size()];
        fields_ = (DbaseField[]) lfields.toArray(fields_);
    }

    /**
   * Get the largest field size of this table.
   * 
   * @return The largt field size iiin bytes.
   */
    public int getLargestFieldSize() {
        return largestFieldSize_;
    }

    /**
   * Set the number of records in the file.
   * 
   * @param _inNumRecords The number of records.
   */
    public void setNumRecords(final int _inNumRecords) {
        recordCnt_ = _inNumRecords;
    }

    /**
   * Write the header data to the DBF file.
   * 
   * @param _out A channel to write to. If you have an OutputStream you can obtain the correct channel by using
   *          java.nio.Channels.newChannel(OutputStream out).
   * @throws IOException If errors occur.
   */
    public void writeHeader(final WritableByteChannel _out) throws IOException {
        if (headerLength_ == -1) {
            headerLength_ = MINIMUM_HEADER;
        }
        final ByteBuffer buffer = ByteBuffer.allocateDirect(headerLength_);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(MAGIC);
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        buffer.put((byte) (c.get(Calendar.YEAR) % 100));
        buffer.put((byte) (c.get(Calendar.MONTH) + 1));
        buffer.put((byte) (c.get(Calendar.DAY_OF_MONTH)));
        buffer.putInt(recordCnt_);
        buffer.putShort((short) headerLength_);
        buffer.putShort((short) recordLength_);
        buffer.position(buffer.position() + 20);
        int tempOffset = 0;
        for (int i = 0; i < fields_.length; i++) {
            for (int j = 0; j < 11; j++) {
                if (fields_[i].fieldName_.length() > j) {
                    buffer.put((byte) fields_[i].fieldName_.charAt(j));
                } else {
                    buffer.put((byte) 0);
                }
            }
            buffer.put((byte) fields_[i].fieldType_);
            buffer.putInt(tempOffset);
            tempOffset += fields_[i].fieldLength_;
            buffer.put((byte) fields_[i].fieldLength_);
            buffer.put((byte) fields_[i].decimalCount_);
            buffer.position(buffer.position() + 14);
        }
        buffer.put((byte) 0x0D);
        buffer.position(0);
        int r = buffer.remaining();
        while ((r -= _out.write(buffer)) > 0) {
            ;
        }
    }

    /**
   * Get a simple representation of this header.
   * 
   * @return A String representing the state of the header.
   */
    public String toString() {
        final StringBuffer fs = new StringBuffer();
        for (int i = 0, ii = fields_.length; i < ii; i++) {
            final DbaseField f = fields_[i];
            fs.append(f.fieldName_ + CtuluLibString.ESPACE + f.fieldType_ + CtuluLibString.ESPACE + f.fieldLength_ + CtuluLibString.ESPACE + f.decimalCount_ + CtuluLibString.ESPACE + f.fieldDataAddress_ + CtuluLibString.LINE_SEP_SIMPLE);
        }
        return "DB3 Header\n" + "Date : " + date_ + CtuluLibString.LINE_SEP_SIMPLE + "Records : " + recordCnt_ + CtuluLibString.LINE_SEP_SIMPLE + "Fields : " + fieldCnt_ + CtuluLibString.LINE_SEP_SIMPLE + fs;
    }
}
