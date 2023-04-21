package gov.nasa.worldwind.formats.shapefile;

import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.util.Logging;
import java.nio.*;
import java.text.*;
import java.util.logging.Level;

/**
 * @author Patrick Murris
 * @version $Id: DBaseRecord.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class DBaseRecord extends AVListImpl {

    private boolean deleted = false;

    private int recordNumber;

    private static final DateFormat dateformat = new SimpleDateFormat("yyyyMMdd");

    public DBaseRecord(DBaseFile dbaseFile, ByteBuffer buffer, int recordNumber) {
        if (dbaseFile == null) {
            String message = Logging.getMessage("nullValue.DBaseFileIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (buffer == null) {
            String message = Logging.getMessage("nullValue.BufferIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.readFromBuffer(dbaseFile, buffer, recordNumber);
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public int getRecordNumber() {
        return this.recordNumber;
    }

    @SuppressWarnings({ "StringEquality" })
    protected void readFromBuffer(DBaseFile dbaseFile, ByteBuffer buffer, int recordNumber) {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.recordNumber = recordNumber;
        byte b = buffer.get();
        this.deleted = (b == 0x2A);
        int maxFieldLength = 0;
        for (DBaseField field : dbaseFile.getFields()) {
            if (maxFieldLength < field.getLength()) maxFieldLength = field.getLength();
        }
        DBaseField[] fields = dbaseFile.getFields();
        byte[] bytes = new byte[maxFieldLength];
        for (DBaseField field : fields) {
            int numRead = dbaseFile.readZeroTerminatedString(buffer, bytes, field.getLength());
            if (dbaseFile.isStringEmpty(bytes, numRead)) {
                this.setValue(field.getName(), null);
                continue;
            }
            String value = dbaseFile.decodeString(bytes, numRead).trim();
            try {
                if (field.getType() == DBaseField.TYPE_BOOLEAN) {
                    this.setValue(field.getName(), value.equalsIgnoreCase("T") || value.equalsIgnoreCase("Y"));
                } else if (field.getType() == DBaseField.TYPE_CHAR) {
                    this.setValue(field.getName(), value);
                } else if (field.getType() == DBaseField.TYPE_DATE) {
                    this.setValue(field.getName(), dateformat.parse(value));
                } else if (field.getType() == DBaseField.TYPE_NUMBER) {
                    if (field.getDecimals() > 0) this.setValue(field.getName(), Double.valueOf(value)); else this.setValue(field.getName(), Long.valueOf(value));
                }
            } catch (Exception e) {
                Logging.logger().log(Level.WARNING, Logging.getMessage("SHP.FieldParsingError", field, value), e);
            }
        }
    }
}
