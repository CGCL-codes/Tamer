package jpcsp.filesystems.umdiso.iso9660;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author gigaherz
 */
public class Iso9660File {

    private int fileLBA;

    private int fileSize;

    private int fileProperties;

    private String fileName;

    private Date timestamp;

    private int Ubyte(byte b) {
        return (b) & 255;
    }

    public Iso9660File(byte[] data, int length) throws IOException {
        fileLBA = Ubyte(data[1]) | (Ubyte(data[2]) << 8) | (Ubyte(data[3]) << 16) | (data[4] << 24);
        fileSize = Ubyte(data[9]) | (Ubyte(data[10]) << 8) | (Ubyte(data[11]) << 16) | (data[12] << 24);
        int year = Ubyte(data[17]);
        int month = Ubyte(data[18]);
        int day = Ubyte(data[19]);
        int hour = Ubyte(data[20]);
        int minute = Ubyte(data[21]);
        int second = Ubyte(data[22]);
        int gmtOffset = data[23];
        int gmtOffsetHours = gmtOffset / 4;
        int gmtOffsetMinutes = (gmtOffset % 4) * 15;
        String timeZoneName = "GMT";
        if (gmtOffset >= 0) {
            timeZoneName += "+";
        }
        timeZoneName += gmtOffsetHours;
        if (gmtOffsetMinutes > 0) {
            if (gmtOffsetMinutes < 10) {
                timeZoneName += "0";
            }
            timeZoneName += gmtOffsetMinutes;
        }
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneName);
        Calendar timestampCalendar = Calendar.getInstance(timeZone);
        timestampCalendar.set(1900 + year, month - 1, day, hour, minute, second);
        timestamp = timestampCalendar.getTime();
        fileProperties = data[24];
        if ((fileLBA < 0) || (fileSize < 0)) {
            throw new IOException("WTF?! Size or lba < 0?!");
        }
        int fileNameLength = data[31];
        fileName = "";
        for (int i = 0; i < fileNameLength; i++) {
            char c = (char) (data[32 + i]);
            if (c == 0) c = '.';
            fileName += c;
        }
    }

    public int getLBA() {
        return fileLBA;
    }

    public int getSize() {
        return fileSize;
    }

    public int getProperties() {
        return fileProperties;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
